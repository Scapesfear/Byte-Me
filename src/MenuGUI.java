import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MenuGUI extends JFrame {

    private JTable menuTable;
    private DefaultTableModel tableModel;
    private JButton closeButton;

    public MenuGUI() {
        setTitle("Menu Items");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a non-editable table model
        tableModel = new NonEditableTableModel();
        tableModel.addColumn("Item ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Price");
        tableModel.addColumn("Type");

        // Create the table
        menuTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(menuTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load menu items
        loadMenuItems();

        // Create the close button
        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> setVisible(false));

        // Add the close button to the bottom of the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadMenuItems() {
        List<Item> items = Menu.getAvailableMenuItems();
        for (Item item : items) {
            tableModel.addRow(new Object[]{
                    item.getItemID(),
                    item.getName(),
                    item.getPrice(),
                    item.getType()
            });
        }
    }

    public void showMenu() {
        setVisible(true);
    }

    // Custom table model that prevents editing
    private static class NonEditableTableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Prevent editing of cells
        }
    }
}