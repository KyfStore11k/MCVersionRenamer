package com.kyfstore.mcversionrenamer.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;

import org.json.*;

import static com.kyfstore.mcversionrenamer.util.PublicData.*;

public class App {
    private JComboBox<String> mcVersionDropdown;
    private JComboBox<String> renamerVersionDropdown;

    private final Map<String, List<ModrinthVersion>> mcVersionMap = new HashMap<>();
    private final String modrinthApiUrl = "https://api.modrinth.com/v2/project/mcversionrenamer/version";

    public void initWindow() {
        JFrame window = new JFrame(windowTitle);
        window.setSize(windowWidth, windowHeight);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLayout(new FlowLayout());

        mcVersionDropdown = new JComboBox<>();
        renamerVersionDropdown = new JComboBox<>();
        JButton downloadButton = new JButton("Download");

        fetchModrinthVersions();

        mcVersionDropdown.addActionListener(e -> updateRenamerVersions());

        downloadButton.addActionListener(e -> downloadSelectedVersion());

        window.add(new JLabel("Select Minecraft Version:"));
        window.add(mcVersionDropdown);
        window.add(new JLabel("Select Renamer Version:"));
        window.add(renamerVersionDropdown);
        window.add(downloadButton);

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        window,
                        "Are you sure you want to exit?",
                        "Exit Confirmation",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    logger.shutdown();

                    window.dispose();
                    System.exit(0);
                }
            }
        });
    }

    private void fetchModrinthVersions() {
        try {
            String jsonResponse = fetchJSON();
            JSONArray versions = new JSONArray(jsonResponse);

            for (int i = 0; i < versions.length(); i++) {
                JSONObject version = versions.getJSONObject(i);
                JSONArray gameVersions = version.getJSONArray("game_versions");
                String versionNumber = version.getString("version_number");
                JSONArray files = version.getJSONArray("files");
                String filename = files.getJSONObject(0).getString("filename");
                String downloadUrl = files.getJSONObject(0).getString("url");
                String sha512Hash = files.getJSONObject(0).getJSONObject("hashes").getString("sha512");

                for (int j = 0; j < gameVersions.length(); j++) {
                    String mcVersion = gameVersions.getString(j);
                    mcVersionMap.computeIfAbsent(mcVersion, k -> new ArrayList<>())
                            .add(new ModrinthVersion(versionNumber, filename, downloadUrl, sha512Hash));
                }
            }

            for (String mcVersion : mcVersionMap.keySet()) {
                mcVersionDropdown.addItem(mcVersion);
            }

            updateRenamerVersions();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to fetch Modrinth data: " + e.getMessage());
            logger.error(e.getMessage());
        }
    }

    private void updateRenamerVersions() {
        renamerVersionDropdown.removeAllItems();
        String selectedMCVersion = (String) mcVersionDropdown.getSelectedItem();
        if (selectedMCVersion != null && mcVersionMap.containsKey(selectedMCVersion)) {
            for (ModrinthVersion version : mcVersionMap.get(selectedMCVersion)) {
                renamerVersionDropdown.addItem(version.versionNumber);
            }
        }
    }

    private void downloadSelectedVersion() {
        String selectedMCVersion = (String) mcVersionDropdown.getSelectedItem();
        String selectedRenamerVersion = (String) renamerVersionDropdown.getSelectedItem();
        if (selectedMCVersion != null && selectedRenamerVersion != null) {
            List<ModrinthVersion> versions = mcVersionMap.get(selectedMCVersion);
            for (ModrinthVersion version : versions) {
                if (version.versionNumber.equals(selectedRenamerVersion)) {
                    try {
                        downloadFile(version);
                        JOptionPane.showMessageDialog(null, "Download complete!");
                    } catch (IOException | NoSuchAlgorithmException | URISyntaxException e) {
                        JOptionPane.showMessageDialog(null, "Failed to download: " + e.getMessage());
                        logger.error(e.getMessage());
                    }
                    break;
                }
            }
        }
    }

    private String fetchJSON() throws IOException, URISyntaxException {
        HttpURLConnection connection = (HttpURLConnection) new URI(modrinthApiUrl).toURL().openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    private void downloadFile(ModrinthVersion version) throws IOException, NoSuchAlgorithmException, URISyntaxException {
        String filePath = "./" + version.filename;
        File file = new File(filePath);

        if (file.exists()) {
            if (!file.delete()) logger.error(new FileNotFoundException("File not found: " + file.getAbsolutePath()).getMessage());
        }

        HttpURLConnection connection = (HttpURLConnection) new URI(version.downloadUrl).toURL().openConnection();
        connection.setRequestMethod("GET");
        InputStream inputStream = connection.getInputStream();
        Files.copy(inputStream, Paths.get(filePath));
        inputStream.close();

        if (!verifyFileHash(filePath, version.sha512Hash)) {
            logger.error(new IOException("Hash verification failed for downloaded file.").getMessage());
        }
    }

    private boolean verifyFileHash(String filePath, String expectedHash) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        byte[] hashBytes = digest.digest(fileBytes);

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString().equalsIgnoreCase(expectedHash);
    }
}

class ModrinthVersion {
    String versionNumber;
    String filename;
    String downloadUrl;
    String sha512Hash;

    public ModrinthVersion(String versionNumber, String filename, String downloadUrl, String sha512Hash) {
        this.versionNumber = versionNumber;
        this.filename = filename;
        this.downloadUrl = downloadUrl;
        this.sha512Hash = sha512Hash;
    }
}
