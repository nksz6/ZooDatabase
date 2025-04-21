package com.ics311.group3.group3projectphase4nk;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.swing.*;
import java.awt.*;

public class Launcher {

    public static void main(String[] args) {
        JFrame launcherFrame = new JFrame("Zoo Application");
        launcherFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        launcherFrame.setSize(400, 300);
        launcherFrame.setLayout(new GridLayout(3, 1));
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Buttons for tables
        JButton tasksButton = createButton("Tasks");
        JButton locationsButton = createButton("Locations");
        JButton employeesButton = createButton("Employees");

        // Listeners for buttons
        tasksButton.addActionListener(e -> {
            TaskManagement taskFrame = new TaskManagement();
            taskFrame.setVisible(true);
        });
        locationsButton.addActionListener(e -> {
        LocationGUI locationGUI = new LocationGUI();
        locationGUI.setVisible(true);
        });
        
        
        employeesButton.addActionListener(e -> showFrame("Employees", "Manage employees."));

        // Title for app
        JLabel titleLabel = new JLabel("Zoo Application - Group 3");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        buttonPanel.add(titleLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(tasksButton, gbc);
        gbc.gridy = 2;
        buttonPanel.add(locationsButton, gbc);
        gbc.gridy = 3;
        buttonPanel.add(employeesButton, gbc);

        
        // Logout button
        JButton logoutButton = createButton("Logout");
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                launcherFrame,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        JPanel logoutPanel = new JPanel();
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        logoutPanel.add(logoutButton);
        
        launcherFrame.add(buttonPanel, BorderLayout.CENTER);
        launcherFrame.add(logoutPanel, BorderLayout.SOUTH);
        
        launcherFrame.add(tasksButton);
        launcherFrame.add(locationsButton);
        launcherFrame.add(employeesButton);

        launcherFrame.setLocationRelativeTo(null);
        launcherFrame.setVisible(true);
    }

    // Method for creating buttons
    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        return button;
    }

    // Method for jframes
    private static void showFrame(String title, String message) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(label);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

