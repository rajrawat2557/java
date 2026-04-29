import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoryFrame extends JFrame {
    public HistoryFrame(int walletId) {
        setTitle("Transaction History");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Object[][] data = TransactionService.getHistoryData(walletId);
        String[] columns = {"ID", "From", "To", "Amount", "Timestamp"};

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> this.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
