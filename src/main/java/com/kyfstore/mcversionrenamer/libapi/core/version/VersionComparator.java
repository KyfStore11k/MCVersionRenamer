package com.kyfstore.mcversionrenamer.libapi.core.version;

public class VersionComparator {

    public static Boolean compareVersions(String currentVersion, String latestVersion) {
        String[] currentParts = splitVersion(currentVersion);
        String[] latestParts = splitVersion(latestVersion);

        int numericComparison = compareNumericVersions(currentParts[0], latestParts[0]);

        if (numericComparison != 0) {
            return numericComparison > 0;
        }

        if (currentParts[1].equals(latestParts[1])) {
            return null;
        }

        return compareTextualVersions(currentParts[1], latestParts[1]);
    }

    private static String[] splitVersion(String version) {
        String[] parts = version.split("-", 2);
        if (parts.length == 1) {
            parts = new String[] { parts[0], "" };
        }
        return parts;
    }

    private static int compareNumericVersions(String currentVersion, String latestVersion) {
        String[] currentParts = currentVersion.split("\\.");
        String[] latestParts = latestVersion.split("\\.");

        for (int i = 0; i < Math.max(currentParts.length, latestParts.length); i++) {
            int currentPart = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int latestPart = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;

            if (currentPart < latestPart) {
                return -1;
            } else if (currentPart > latestPart) {
                return 1;
            }
        }

        return 0;
    }

    private static Boolean compareTextualVersions(String currentSuffix, String latestSuffix) {
        String[] labels = {"PRE-ALPHA", "ALPHA", "BETA", "RELEASE"};

        int currentIndex = indexOf(labels, currentSuffix);
        int latestIndex = indexOf(labels, latestSuffix);

        if (currentIndex == -1 && latestIndex == -1) {
            return null;
        }
        if (currentIndex == -1) {
            return false;
        }
        if (latestIndex == -1) {
            return true;
        }

        return currentIndex > latestIndex;
    }

    private static int indexOf(String[] labels, String version) {
        for (int i = 0; i < labels.length; i++) {
            if (labels[i].equalsIgnoreCase(version)) {
                return i;
            }
        }
        return -1;
    }
    public static String removePrefix(String str, String prefix) {
        if (str != null && str.startsWith(prefix)) {
            return str.substring(prefix.length());
        }
        return str;
    }

}
