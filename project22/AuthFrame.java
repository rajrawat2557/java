import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
public class AuthFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JPasswordField pinField;
    public AuthFrame() {
        setTitle("EasyPay - Login / Signup");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JPanel centerPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.add(new JLabel("Username:"));
        userField = new JTextField();
        centerPanel.add(userField);
        centerPanel.add(new JLabel("Password:"));
        passField = new JPasswordField();
        centerPanel.add(passField);
        centerPanel.add(new JLabel("PIN (For Signup):"));
        pinField = new JPasswordField();
        centerPanel.add(pinField);
        add(centerPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Signup");
        loginBtn.addActionListener(this::handleLogin);
        signupBtn.addActionListener(this::handleSignup);
        bottomPanel.add(loginBtn);
        bottomPanel.add(signupBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    private void handleLogin(ActionEvent e) {
        String u = userField.getText();
        String p = new String(passField.getPassword());
        if(u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both fields");
            return;
        }
        int userId = UserService.login(u, p);
        if (userId != -1) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            int walletId = WalletService.getWalletId(userId);
            new DashboardFrame(walletId).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void handleSignup(ActionEvent e) {
        String u = userField.getText();
        String p = new String(passField.getPassword());
        String pin = new String(pinField.getPassword());
        if(u.isEmpty() || p.isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username, password, and PIN");
            return;
        }
        int userId = UserService.signup(u, p, pin);
        if (userId != -1) {
            JOptionPane.showMessageDialog(this, "Signup successful! You can now login.");
        } else {
            JOptionPane.showMessageDialog(this, "Signup failed. User might exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
