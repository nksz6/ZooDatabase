//ICS311-01 -- Group 3 -- Nick Kelley -- TaskManagement

//package
package com.ics311.group3.group3projectphase4nk;

//imports
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//TaskManagement Class
public class TaskManagement extends JFrame {
    
    //GUI components
    private JTextField jobCodeField;    //input for taskCode/jobCode
    private JTextField jobDescField;    //input for taskDesc/jobDesc 
    private JComboBox<String> empIdComboBox;        //employee dropdown
    private JComboBox<String> locationComboBox;     //location dropdown
    
    //table components
    private JTable taskTable;               //table w/ all tasks
    private DefaultTableModel tableModel;       //data model for table
    
    //buttons to do things
    private JButton addButton;          //add new task
    private JButton updateButton;       //update selected task
    private JButton deleteButton;       //delete selected task
    private JButton clearButton;        //clear things in input field
    
    //TaskManagement() method
    public TaskManagement() {
        
        //main frame setup
        setTitle("Task Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));
        
        //setup components
        initComponents();
        setupLayout();
        
        //load data from database
        loadEmployees();
        loadLocations();
        loadTasks();
        
        //center justification
        setLocationRelativeTo(null);
    }
    
    //initializing gui and listeners
    private void initComponents() {
        
        //init input fields
        jobCodeField = new JTextField(20);
        jobDescField = new JTextField(20);
        
        //init dropdowns
        empIdComboBox = new JComboBox<>();
        locationComboBox = new JComboBox<>();
        
        //init buttons
        addButton = new JButton("Add Task");
        updateButton = new JButton("Update Task");
        deleteButton = new JButton("Delete Task");
        clearButton = new JButton("Clear Fields");
        
        //init table
        String[] columnNames = {"Job Code", "Job Description", "Employee ID", "Location Code"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //init listeners
        addButton.addActionListener(e -> addTask());
        updateButton.addActionListener(e -> updateTask());
        deleteButton.addActionListener(e -> deleteTask());
        clearButton.addActionListener(e -> clearFields());
        
        //config listeners
        taskTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow != -1) {
                    jobCodeField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    jobDescField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    
                    String employeeValue = tableModel.getValueAt(selectedRow, 2).toString();
                    String locationValue = tableModel.getValueAt(selectedRow, 3).toString();
                    
                    //fix emp selection
                    for (int i = 0; i < empIdComboBox.getItemCount(); i++) {
                        if (empIdComboBox.getItemAt(i).toString().equals(employeeValue)) {
                            empIdComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                    
                    //fix loc selection
                    for (int i = 0; i < locationComboBox.getItemCount(); i++) {
                        if (locationComboBox.getItemAt(i).toString().equals(locationValue)) {
                            locationComboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });
    }
    
    //setup and organize layout
    private void setupLayout() {
        
        //create input panel w/ GridBag
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        //add and label input comps
        
        //Job Code
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Job Code:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(jobCodeField, gbc);
        
        //Job Desc
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Job Description:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(jobDescField, gbc);
        
        //Employee
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Employee:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(empIdComboBox, gbc);
        
        //Location
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(locationComboBox, gbc);
        
        
        //makin button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        //combine both panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        //adding em to main frame
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(taskTable), BorderLayout.CENTER);
    }
    
    //load emp data into ComboBox
    private void loadEmployees() {
        
        //query to get emp data
        String query = "SELECT emp_ID, CONCAT(f_name, ' ', l_name) AS full_name FROM Employee ORDER BY emp_ID";
        
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery()) {
            
            //clear existing items
            empIdComboBox.removeAllItems();
            
            //populating
            while (rs.next()) {
                String displayText = rs.getString("emp_ID") + " - " + rs.getString("full_name");
                empIdComboBox.addItem(displayText);
            }
            
            //catch issue
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error Loading Employees: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //load location data into ComboBox
    private void loadLocations() {
        
        //query for loc data
        String query = "SELECT location_code, location_name FROM Location ORDER BY location_code";
        
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement(query);
                ResultSet rs = pst.executeQuery()) {
            
            //clear existing items
            locationComboBox.removeAllItems();
            
            //populating
            while (rs.next()){
                String displayText = rs.getString("location_code") + " - " + rs.getString("location_name");
                locationComboBox.addItem(displayText);
            }
            
            //catch issue
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error Loading Locations: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //load tasks from database and join w/ other tables
    private void loadTasks() {
        
        //query to get task/job info w/ emps and loc data
        String query = "SELECT t.job_code, t.job_desc, " +
                "CONCAT(e.emp_ID, ' - ', e.f_name, ' ', e.l_name) AS employee, " +
                "CONCAT(l.location_code, ' - ', l.location_name) AS location " +
                "FROM Task t " +
                "JOIN Employee e ON t.emp_ID = e.emp_ID " +
                "JOIN Location l ON t.location_code = l.location_code " +
                "ORDER BY t.job_code";
        
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement(query);
                ResultSet rs = pst.executeQuery()) {
                
            //clear existing table datas
            tableModel.setRowCount(0);
            
            //populating
            while(rs.next()) {
                Object[] row = {
                    rs.getString("job_code"),
                    rs.getString("job_desc"),
                    rs.getString("employee"),
                    rs.getString("location")
                };
                tableModel.addRow(row);
            }      
            
            //catch issue
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error Loading Tasks: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //extracting IDs from ComboBox
    private String extractId(String comboBoxValue) {
        if (comboBoxValue != null) {
            return comboBoxValue.split("-")[0].trim();
        }
        return null;
    }
    
    //method to reset input fields
    private void clearFields() {
        jobCodeField.setText("");
        jobDescField.setText("");
        empIdComboBox.setSelectedIndex(0);
        locationComboBox.setSelectedIndex(0);
        taskTable.clearSelection();
    }
    
    //method to add a new task
    private void addTask() {
        
        //get and trim input vals
        String jobCode = jobCodeField.getText().trim();
        String jobDesc = jobDescField.getText().trim();
        String empId = extractId((String) empIdComboBox.getSelectedItem());
        String locationCode = extractId((String) locationComboBox.getSelectedItem());
        
        //validating fields
        if (jobCode.isEmpty() || jobDesc.isEmpty() || empId == null || locationCode == null) {
            JOptionPane.showMessageDialog(this, "FILL IN ALL THE FIELDS!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //query for task insertion
        String query = "INSERT INTO Task (job_code, job_desc, emp_id, location_code) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement(query)) {
            
            //setting query params
            pst.setString(1, jobCode);
            pst.setString(2, jobDesc);
            pst.setString(3, empId);
            pst.setString(4, locationCode);
            pst.executeUpdate();
            
            //confirmation mesg
            JOptionPane.showMessageDialog(this, "Task Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            //refresh table
            loadTasks();
            //clear input fields
            clearFields();
        
            //catch issue
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "ERROR ADDING TASK: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //method to delete a task
    private void deleteTask() {
        
        //get row
        int selectedRow = taskTable.getSelectedRow();
        
        //validate you selected something
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "YOU NEED TO SELECT A TASK TO DELETE!", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;    
        }
        
        //getting task identifiers
        String jobCode = tableModel.getValueAt(selectedRow, 0).toString();
        String jobDesc = tableModel.getValueAt(selectedRow, 1).toString();
        
        //confirm you wanna delete it
        int confirm = JOptionPane.showConfirmDialog(this, "ARE YOU SURE YOU WANNA DELETE IT?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            
            //query for deletion
            String query = "DELETE FROM Task WHERE job_code = ? AND job_desc = ?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement pst = conn.prepareStatement(query)) {
                
                //query params
                pst.setString(1, jobCode);
                pst.setString(2, jobDesc);
                pst.executeUpdate();
                
                //confirmation mesg
                JOptionPane.showMessageDialog(this, "TASK DELETED SUCCESSFULLY!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                //refresh table
                loadTasks();
                //clear input fields
                clearFields();
           
                //catch issue
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error Deleting Task: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    //method to update existing tasks
    private void updateTask() {
        
        //get and trim input vals
        int selectedRow = taskTable.getSelectedRow();
        String jobCode = jobCodeField.getText().trim();
        String jobDesc = jobDescField.getText().trim();
        String empId = extractId((String) empIdComboBox.getSelectedItem());
        String locationCode = extractId((String) locationComboBox.getSelectedItem());
        
        //validate you actually selected something
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task to update!", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        //validate you got good inputs
        if (jobCode.isEmpty() || jobDesc.isEmpty() || empId == null || locationCode == null) {
            JOptionPane.showMessageDialog(this, "Fill in all the fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
        
        //query for updating task
        String query = "UPDATE Task SET emp_id = ?, location_code = ? WHERE job_code = ? AND job_desc = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement(query)) {
            
            
            //set query params
            pst.setString(1, empId);
            pst.setString(2, locationCode);
            pst.setString(3, jobCode);
            pst.setString(4, jobDesc);
            pst.executeUpdate();
            
            //confirmation
            JOptionPane.showMessageDialog(this, "Task Updated Sucessfully!", "Sucess", JOptionPane.INFORMATION_MESSAGE);
            
            //refresh table
            loadTasks();
            //clear fields
            clearFields();
            
            //catch issues
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error Updating Task: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //main - display TaskManagement interface
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TaskManagement frame = new TaskManagement();
            frame.setVisible(true);
        });
    }
}
