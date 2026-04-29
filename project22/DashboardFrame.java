import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DashboardFrame extends JFrame {
    private int walletId;
    private JLabel balanceLabel;

    public DashboardFrame(int walletId) {
        this.walletId = walletId;

        setTitle("EasyPay - Dashboard");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel accountLabel = new JLabel("Account Number: " + walletId);
        accountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        balanceLabel = new JLabel("Balance: $" + WalletService.getBalance(walletId));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 20));

        headerPanel.add(accountLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(balanceLabel);

        add(headerPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton transferBtn = new JButton("Transfer");
        JButton historyBtn = new JButton("History");

        depositBtn.addActionListener(this::handleDeposit);
        withdrawBtn.addActionListener(this::handleWithdraw);
        transferBtn.addActionListener(this::handleTransfer);
        historyBtn.addActionListener(e -> new HistoryFrame(walletId).setVisible(true));

        buttonPanel.add(depositBtn);
        buttonPanel.add(withdrawBtn);
        buttonPanel.add(transferBtn);
        buttonPanel.add(historyBtn);

        add(buttonPanel, BorderLayout.CENTER);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            new AuthFrame().setVisible(true);
            this.dispose();
        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(logoutBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void updateBalance() {
        balanceLabel.setText("Balance: $" + WalletService.getBalance(walletId));
    }

    private void handleDeposit(ActionEvent e) {
        String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (input != null && !input.isEmpty()) {
            try {
                double amt = Double.parseDouble(input);
                if (amt > 0) {
                    if (WalletService.deposit(walletId, amt)) {
                        JOptionPane.showMessageDialog(this, "Deposit successful!");
                        updateBalance();
                    } else {
                        JOptionPane.showMessageDialog(this, "Deposit failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleWithdraw(ActionEvent e) {
        String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (input != null && !input.isEmpty()) {
            try {
                double amt = Double.parseDouble(input);
                if (amt > 0) {
                    if (WalletService.withdraw(walletId, amt)) {
                        JOptionPane.showMessageDialog(this, "Withdrawal successful!");
                        updateBalance();
                    } else {
                        JOptionPane.showMessageDialog(this, "Withdrawal failed. Insufficient balance?", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleTransfer(ActionEvent e) {
        JTextField toField = new JTextField();
        JTextField amountField = new JTextField();
        JPasswordField pinField = new JPasswordField();
        Object[] message = {
            "Receiver Wallet ID:", toField,
            "Amount:", amountField,
            "Transaction PIN:", pinField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Transfer Money", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int toId = Integer.parseInt(toField.getText());
                double amt = Double.parseDouble(amountField.getText());
                String pin = new String(pinField.getPassword());
                
                if (amt > 0) {
                    if (!UserService.verifyPin(walletId, pin)) {
                        JOptionPane.showMessageDialog(this, "Incorrect PIN.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (TransactionService.transfer(walletId, toId, amt)) {
                        JOptionPane.showMessageDialog(this, "Transfer successful!");
                        updateBalance();
                    } else {
                        JOptionPane.showMessageDialog(this, "Transfer failed. Check details.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
