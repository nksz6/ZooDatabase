package com.ics311.group3.group3projectphase4nk;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationGUI extends JFrame {
    private JTextField txtLocationCode, txtLocationName;
    private JButton btnInsert, btnUpdate, btnDelete;
    private JTable table;
    private DefaultTableModel tableModel;

    public LocationGUI() {
        setTitle("Manage Locations");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel lblLocationCode = new JLabel("Location Code:");
        lblLocationCode.setBounds(20, 20, 100, 25);
        add(lblLocationCode);

        txtLocationCode = new JTextField();
        txtLocationCode.setBounds(130, 20, 150, 25);
        txtLocationCode.setEditable(false); // Location code is not editable
        add(txtLocationCode);

        JLabel lblLocationName = new JLabel("Location Name:");
        lblLocationName.setBounds(20, 60, 100, 25);
        add(lblLocationName);

        txtLocationName = new JTextField();
        txtLocationName.setBounds(130, 60, 150, 25);
        add(txtLocationName);

        btnInsert = new JButton("Insert");
        btnInsert.setBounds(300, 20, 100, 25);
        add(btnInsert);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(300, 60, 100, 25);
        add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(420, 20, 100, 25);
        add(btnDelete);

        tableModel = new DefaultTableModel(new String[]{"Location Code", "Location Name"}, 0);
        table = new JTable(tableModel);
        table.setBounds(20, 120, 550, 200);
        table.getSelectionModel().addListSelectionListener(event -> populateFields());
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 120, 550, 200);
        add(scrollPane);

        // Button Actions
        btnInsert.addActionListener(e -> insertLocation());
        btnUpdate.addActionListener(e -> updateLocation());
        btnDelete.addActionListener(e -> deleteLocation());

        // Load Initial Data
        fetchLocations();
    }

    private void populateFields() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            txtLocationCode.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtLocationName.setText(tableModel.getValueAt(selectedRow, 1).toString());
        }
    }

    private void insertLocation() {
        String locationName = txtLocationName.getText();
        if (locationName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Location name cannot be empty.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Location (location_name) VALUES (?)")) {
            stmt.setString(1, locationName);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Location inserted successfully.");
            fetchLocations();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error inserting location: " + e.getMessage());
        }
    }

    private void updateLocation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to update.");
            return;
        }

        String locationCode = txtLocationCode.getText();
        String locationName = txtLocationName.getText();

        if (locationName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Location name cannot be empty.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Location SET location_name = ? WHERE location_code = ?")) {
            stmt.setString(1, locationName);
            stmt.setString(2, locationCode);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Location updated successfully.");
            fetchLocations();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating location: " + e.getMessage());
        }
    }

    private void deleteLocation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }

        String locationCode = txtLocationCode.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Location WHERE location_code = ?")) {
            stmt.setString(1, locationCode);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Location deleted successfully.");
            fetchLocations();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting location: " + e.getMessage());
        }
    }

    private void fetchLocations() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT location_code, location_name FROM Location")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getString("location_code"), rs.getString("location_name")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching locations: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LocationGUI frame = new LocationGUI();
            frame.setVisible(true);
        });
    }
}