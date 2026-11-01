
import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public Login() {

        setTitle("Smart Library Management System - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 244, 248));

        JLabel title = new JLabel("SMART LIBRARY");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        title.setForeground(new Color(25, 45, 85));
        title.setBounds(105, 30, 250, 35);
        panel.add(title);

        JLabel subtitle = new JLabel("Management System - Admin Login");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setBounds(105, 70, 250, 25);
        panel.add(subtitle);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(65, 120, 120, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(65, 145, 310, 35);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(65, 190, 120, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(65, 215, 310, 35);
        panel.add(passwordField);

        JButton loginButton = new JButton("LOGIN");
        loginButton.setBounds(65, 265, 310, 35);
        loginButton.setBackground(new Color(25, 45, 85));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        panel.add(loginButton);

        loginButton.addActionListener(e -> login());

        add(panel);
        setVisible(true);
    }

    private void login() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals("admin") && password.equals("admin123")) {

            JOptionPane.showMessageDialog(
                this,
                "Login Successful! Welcome Admin."
            );

            dispose();

            Main main = new Main();
            main.setVisible(true);

        } else {

            JOptionPane.showMessageDialog(
                this,
                "Invalid Username or Password!",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE
            );

            passwordField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}
