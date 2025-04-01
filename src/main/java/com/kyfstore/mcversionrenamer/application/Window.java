package com.kyfstore.mcversionrenamer.application;

import javax.swing.SwingUtilities;

public class Window {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().initWindow());
    }
}
