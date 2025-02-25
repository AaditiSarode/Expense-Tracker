package expense_income_tracker;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ExpenseIncomeTableModel extends AbstractTableModel {

    private final List<ExpenseIncomeEntry> entries;
    private final String[] columnNames = {"Date", "Description", "Amount", "Type"};

    public ExpenseIncomeTableModel() {
        entries = new ArrayList<>();
    }

    public void addEntry(ExpenseIncomeEntry entry) {
        entries.add(entry);
        fireTableRowsInserted(entries.size() - 1, entries.size() - 1);
    }

    public void editEntry(int rowIndex, ExpenseIncomeEntry updatedEntry) {
        entries.set(rowIndex, updatedEntry);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void removeEntry(int rowIndex) {
        entries.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void sortByColumn(int columnIndex) {
        Collections.sort(entries, new Comparator<ExpenseIncomeEntry>() {
            @Override
            public int compare(ExpenseIncomeEntry e1, ExpenseIncomeEntry e2) {
                Comparable<Object> value1 = (Comparable<Object>) getColumnValue(e1, columnIndex);
                Comparable<Object> value2 = (Comparable<Object>) getColumnValue(e2, columnIndex);
                return value1.compareTo(value2);
            }
        });
        fireTableDataChanged();
    }
    

    public void formatDateColumn(int columnIndex, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        for (int i = 0; i < entries.size(); i++) {
            fireTableCellUpdated(i, columnIndex);
        }
    }

    public void filterByType(String type) {
        entries.removeIf(entry -> !entry.getType().equals(type));
        fireTableDataChanged();
    }

    private Object getColumnValue(ExpenseIncomeEntry entry, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return entry.getDate();
            case 1:
                return entry.getDescription();
            case 2:
                return entry.getAmount();
            case 3:
                return entry.getType();
            default:
                return null;
        }
    }

    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ExpenseIncomeEntry entry = entries.get(rowIndex);
        return getColumnValue(entry, columnIndex);
    }
}
