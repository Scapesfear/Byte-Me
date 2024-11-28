import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.TreeSet;

public class PendingOrdersGUI extends JFrame {

    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private JButton closeButton;

    public PendingOrdersGUI() {
        setTitle("Pending Orders");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);


        tableModel = new NonEditableTableModel();
        tableModel.addColumn("Order ID");
        tableModel.addColumn("Customer ID");
        tableModel.addColumn("Total Price");
        tableModel.addColumn("Status");
        tableModel.addColumn("VIP Status");
        tableModel.addColumn("Timestamp");


        ordersTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        add(scrollPane, BorderLayout.CENTER);


        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> setVisible(false));


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);


        loadPendingOrders();
    }

    private void loadPendingOrders() {
        try {
            TreeSet<Order> pendingOrders = Order.getPendingOrdersSet();

            if (pendingOrders.isEmpty()) {

                tableModel.addRow(new Object[]{"", "", "", "No pending orders", "", ""});
            } else {
                for (Order order : pendingOrders) {
                    tableModel.addRow(new Object[]{
                            order.getOrderID(),
                            order.getCustomerID(),
                            String.format("$%.2f", order.getTotalPrice()),
                            order.getStatus(),
                            order.isVIP() ? "Yes" : "No",
                            new Date(order.getTimestamp())
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading pending orders: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    public void showPendingOrders() {
        setVisible(true);
    }

    private static class NonEditableTableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Prevent editing of cells
        }
    }
}