import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginForm extends JDialog {
    public boolean isAuthenticated = false;

    public LoginForm() {
        setTitle("Авторизація");
        setModal(true);
        setSize(300, 160);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField userField = new JTextField("admin");
        JPasswordField passField = new JPasswordField("admin");

        panel.add(new JLabel("  Логін:"));
        panel.add(userField);
        panel.add(new JLabel("  Пароль:"));
        panel.add(passField);

        JButton btnLogin = new JButton("Увійти");
        btnLogin.addActionListener((ActionEvent e) -> {
            // Проста перевірка
            if (userField.getText().equals("admin") && new String(passField.getPassword()).equals("admin")) {
                isAuthenticated = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Невірний логін або пароль", "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(panel, BorderLayout.CENTER);
        add(btnLogin, BorderLayout.SOUTH);
    }
}
