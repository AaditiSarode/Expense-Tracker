package expense_income_tracker;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;

public class ExpensesIncomesTracker extends JFrame {

    private final ExpenseIncomeTableModel tableModel;
    private final JTable table;
    private final JTextField dateField;
    private final JTextField descriptionField;
    private final JTextField amountField;
    private final JComboBox<String> typeCombobox;
    private final JButton addButton;
    private final JButton editButton;
    private final JButton removeButton;
    private final JLabel balanceLabel;
    private double balance;

    public ExpensesIncomesTracker() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace(); // Print the exception details
            System.err.println("Failed to Set FlatDarkLaf LookAndFeel");
        }

        tableModel = new ExpenseIncomeTableModel();
        table = new JTable(tableModel);
        dateField = new JTextField(10);
        descriptionField = new JTextField(20);
        amountField = new JTextField(10);
        typeCombobox = new JComboBox<>(new String[]{"Expense", "Income"});
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        removeButton = new JButton("Remove");
        balanceLabel = new JLabel("Balance: Rs " + balance);

        addButton.addActionListener(e -> addEntry());
        editButton.addActionListener(e -> editEntry());
        removeButton.addActionListener(e -> removeEntry());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Date(YYYY-MM-DD)"));
        inputPanel.add(dateField);

        inputPanel.add(new JLabel("Description"));
        inputPanel.add(descriptionField);

        inputPanel.add(new JLabel("Amount"));
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Type"));
        inputPanel.add(typeCombobox);

        inputPanel.add(addButton);
        inputPanel.add(editButton);
        inputPanel.add(removeButton);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(balanceLabel);
        setLayout(new BorderLayout());

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setTitle("Budget Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addEntry() {
        String date = dateField.getText();
        String description = descriptionField.getText();
        String amountStr = amountField.getText();
        String type = (String) typeCombobox.getSelectedItem();
        double amount;

        if (amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the Amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Amount Format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (type.equals("Expense")) {
            amount *= -1;
        }

        ExpenseIncomeEntry entry = new ExpenseIncomeEntry(date, description, amount, type);
        tableModel.addEntry(entry);

        balance += amount;
        balanceLabel.setText("Balance: Rs." + balance);

        clearInputFields();
    }

    private void editEntry() {
        int selectedRowIndex = table.getSelectedRow();
        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to edit", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String updatedDate = dateField.getText();
        String updatedDescription = descriptionField.getText();
        String updatedAmountStr = amountField.getText();
        String updatedType = (String) typeCombobox.getSelectedItem();

        if (updatedAmountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the Updated Amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double updatedAmount = Double.parseDouble(updatedAmountStr);
            if (updatedType.equals("Expense")) {
                updatedAmount *= -1;
            }

            ExpenseIncomeEntry updatedEntry = new ExpenseIncomeEntry(updatedDate, updatedDescription, updatedAmount, updatedType);
            tableModel.editEntry(selectedRowIndex, updatedEntry);

            balance += updatedAmount;
            balanceLabel.setText("Balance: Rs." + balance);

            clearInputFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Updated Amount Format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeEntry() {
        int selectedRowIndex = table.getSelectedRow();
        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to remove", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double removedAmount = (double) table.getValueAt(selectedRowIndex, 2);
        tableModel.removeEntry(selectedRowIndex);

        balance -= removedAmount;
        balanceLabel.setText("Balance: Rs." + balance);
    }

    private void clearInputFields() {
        dateField.setText("");
        descriptionField.setText("");
        amountField.setText("");
        typeCombobox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpensesIncomesTracker());
    }
}
