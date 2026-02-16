import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class MainApp extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public MainApp() {
        setTitle("Облік слабо-алкогольних напоїв (MySQL)");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Додати (+)");
        JButton btnRefresh = new JButton("Оновити");

        JLabel lblSearch = new JLabel("Пошук:");
        JTextField txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("Знайти");

        topPanel.add(btnAdd);
        topPanel.add(btnRefresh);
        topPanel.add(Box.createHorizontalStrut(20)); // Відступ
        topPanel.add(lblSearch);
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);

        add(topPanel, BorderLayout.NORTH);

        // --- Таблиця ---
        Vector<String> columns = new Vector<>();
        columns.add("ID"); columns.add("Вид"); columns.add("Марка");
        columns.add("Виробник"); columns.add("Постачальник");
        columns.add("Термін"); columns.add("Ціна");

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();

        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadData();
        });

        btnSearch.addActionListener(e -> {
            String text = txtSearch.getText();
            if (!text.trim().isEmpty()) {
                Vector<Vector<Object>> data = DbConnection.searchProducts(text);
                updateTable(data);
            }
        });

        btnAdd.addActionListener(e -> openAddDialog());
    }

    private void loadData() {
        Vector<Vector<Object>> data = DbConnection.getAllProducts();
        updateTable(data);
    }

    private void updateTable(Vector<Vector<Object>> data) {
        tableModel.setRowCount(0);
        for (Vector<Object> row : data) {
            tableModel.addRow(row);
        }
    }

    private void openAddDialog() {
        JDialog dialog = new JDialog(this, "Додати товар", true);
        dialog.setLayout(new GridLayout(7, 2, 5, 5));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JTextField tType = new JTextField();
        JTextField tBrand = new JTextField();
        JTextField tManuf = new JTextField();
        JTextField tSupp = new JTextField();
        JTextField tDate = new JTextField("2024-01-01");
        JTextField tPrice = new JTextField();
        JButton bSave = new JButton("Зберегти");

        dialog.add(new JLabel("  Вид:")); dialog.add(tType);
        dialog.add(new JLabel("  Марка:")); dialog.add(tBrand);
        dialog.add(new JLabel("  Виробник:")); dialog.add(tManuf);
        dialog.add(new JLabel("  Постачальник:")); dialog.add(tSupp);
        dialog.add(new JLabel("  Термін (РРРР-ММ-ДД):")); dialog.add(tDate);
        dialog.add(new JLabel("  Ціна:")); dialog.add(tPrice);
        dialog.add(new JLabel("")); dialog.add(bSave);

        bSave.addActionListener(ev -> {
            try {
                double price = Double.parseDouble(tPrice.getText().replace(",", "."));
                boolean success = DbConnection.addProduct(
                        tType.getText(), tBrand.getText(), tManuf.getText(),
                        tSupp.getText(), tDate.getText(), price
                );

                if (success) {
                    loadData();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Помилка БД! Перевірте формат дати.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Невірна ціна!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        LoginForm login = new LoginForm();
        login.setVisible(true);

        if (login.isAuthenticated) {
            SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
        } else {
            System.exit(0);
        }
    }
}
