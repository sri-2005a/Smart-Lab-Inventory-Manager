import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class ComponentItem {
    int id;
    String name;
    int quantity;
    double price;
    LocalDateTime lastUpdated;

    ComponentItem(int id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.lastUpdated = LocalDateTime.now();
    }

    public Object[] toRow() {
        return new Object[]{id, name, quantity, "₹" + price, lastUpdated.toString()};
    }
}

class IssueRecord {
    int componentId;
    String componentName;
    int quantityIssued;
    String issuedTo;
    LocalDateTime time;

    IssueRecord(int componentId, String componentName, int quantityIssued, String issuedTo) {
        this.componentId = componentId;
        this.componentName = componentName;
        this.quantityIssued = quantityIssued;
        this.issuedTo = issuedTo;
        this.time = LocalDateTime.now();
    }

    public Object[] toRow() {
        return new Object[]{componentId, componentName, quantityIssued, issuedTo, time.toString()};
    }
}

public class LabInventoryGUI extends JFrame {

    private final List<ComponentItem> inventory = new ArrayList<>();
    private final List<IssueRecord> records = new ArrayList<>();
    private final DefaultTableModel inventoryModel;

    public LabInventoryGUI() {

        setTitle("EEE Lab Inventory Management");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Buttons
        JButton addBtn = new JButton("Add Component");
        JButton updateBtn = new JButton("Update Component");
        JButton deleteBtn = new JButton("Delete Component");
        JButton issueBtn = new JButton("Issue Component");
        JButton viewRecordsBtn = new JButton("View Issued Records");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(issueBtn);
        buttonPanel.add(viewRecordsBtn);

        // Table
        inventoryModel = new DefaultTableModel(
                new Object[]{"ID", "Component", "Qty", "Price", "Last Updated"}, 0
        );

        JTable inventoryTable = new JTable(inventoryModel);

        // Highlight low stock
        inventoryTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                try {
                    int qty = Integer.parseInt(table.getValueAt(row, 2).toString());

                    if (c instanceof JComponent) {
                        JComponent jc = (JComponent) c;

                        if (qty < 5) {
                            jc.setBackground(Color.PINK); // low stock
                        } else {
                            jc.setBackground(Color.WHITE);
                        }
                    }

                } catch (Exception e) {
                    if (c instanceof JComponent) {
                        ((JComponent) c).setBackground(Color.WHITE);
                    }
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(inventoryTable);

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button actions
        addBtn.addActionListener(e -> addComponent());
        updateBtn.addActionListener(e -> updateComponent());
        deleteBtn.addActionListener(e -> deleteComponent());
        issueBtn.addActionListener(e -> issueComponent());
        viewRecordsBtn.addActionListener(e -> showIssuedDialog());

        setVisible(true);
    }

    private void addComponent() {
        try {

            int id = Integer.parseInt(
                    JOptionPane.showInputDialog(this, "Enter Component ID:")
            );

            String name = JOptionPane.showInputDialog(
                    this, "Enter Component Name:"
            );

            int qty = Integer.parseInt(
                    JOptionPane.showInputDialog(this, "Enter Quantity:")
            );

            double price = Double.parseDouble(
                    JOptionPane.showInputDialog(this, "Enter Price:")
            );

            ComponentItem c = new ComponentItem(id, name, qty, price);

            inventory.add(c);
            inventoryModel.addRow(c.toRow());

            JOptionPane.showMessageDialog(this, "Component added.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    private void updateComponent() {

        try {

            int id = Integer.parseInt(
                    JOptionPane.showInputDialog(this, "Enter Component ID to update:")
            );

            for (ComponentItem c : inventory) {

                if (c.id == id) {

                    int qty = Integer.parseInt(
                            JOptionPane.showInputDialog(this, "Enter new Quantity:")
                    );

                    double price = Double.parseDouble(
                            JOptionPane.showInputDialog(this, "Enter new Price:")
                    );

                    c.quantity = qty;
                    c.price = price;
                    c.lastUpdated = LocalDateTime.now();

                    refreshInventoryTable();

                    JOptionPane.showMessageDialog(this, "Component updated.");
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Component not found.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    private void deleteComponent() {

        try {

            int id = Integer.parseInt(
                    JOptionPane.showInputDialog(this, "Enter Component ID to delete:")
            );

            inventory.removeIf(c -> c.id == id);

            refreshInventoryTable();

            JOptionPane.showMessageDialog(this, "Component deleted.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    private void issueComponent() {

        try {

            int id = Integer.parseInt(
                    JOptionPane.showInputDialog(this, "Enter Component ID to issue:")
            );

            for (ComponentItem c : inventory) {

                if (c.id == id) {

                    int qty = Integer.parseInt(
                            JOptionPane.showInputDialog(this, "Enter quantity to issue:")
                    );

                    String issuedTo = JOptionPane.showInputDialog(
                            this, "Issued To (Student/Project):"
                    );

                    if (qty <= c.quantity) {

                        c.quantity -= qty;

                        records.add(
                                new IssueRecord(c.id, c.name, qty, issuedTo)
                        );

                        refreshInventoryTable();

                        JOptionPane.showMessageDialog(
                                this,
                                "Issued successfully to " + issuedTo
                        );

                        return;

                    } else {
                        JOptionPane.showMessageDialog(this, "Not enough stock.");
                        return;
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Component not found.");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    private void showIssuedDialog() {

        JFrame frame = new JFrame("Issued Records");
        frame.setSize(600, 400);

        DefaultTableModel issuedModel = new DefaultTableModel(
                new Object[]{"ID", "Component", "Qty Issued", "Issued To", "Time"}, 0
        );

        JTable issuedTable = new JTable(issuedModel);

        for (IssueRecord r : records) {
            issuedModel.addRow(r.toRow());
        }

        frame.add(new JScrollPane(issuedTable));
        frame.setVisible(true);
    }

    private void refreshInventoryTable() {

        inventoryModel.setRowCount(0);

        for (ComponentItem c : inventory) {
            inventoryModel.addRow(c.toRow());
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                LabInventoryGUI::new
        );
    }
}
