package org.library.system.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window. Hosts the BookPanel and BorrowTransactionPanel
 * as tabs, and acts as the single top-level frame for the LMS desktop app.
 */
public class 
MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // center on screen

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Catalog", new BookPanel());
        tabbedPane.addTab("Borrow / Return", new BorrowTransactionPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}