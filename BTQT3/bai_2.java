import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class bai_2 extends JFrame {
    private static final String DEMO_USERNAME = "admin";
    private static final String DEMO_PASSWORD = "123456";

    private final JTextField usernameField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);
    private final JCheckBox showPasswordCheckBox = new JCheckBox("Hien mat khau");

    public bai_2() {
        setTitle("BT2 - Form dang nhap");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(createHeader(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private JLabel createHeader() {
        JLabel titleLabel = new JLabel("DANG NHAP HE THONG", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(new Color(32, 80, 129));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(18, 20, 8, 20));
        return titleLabel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 28, 10, 28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Ten dang nhap:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Mat khau:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        showPasswordCheckBox.addActionListener(e -> togglePasswordVisibility());
        panel.add(showPasswordCheckBox, gbc);

        JLabel noteLabel = new JLabel("Tai khoan mau: admin / 123456");
        noteLabel.setForeground(Color.GRAY);
        noteLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(noteLabel, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(8, 20, 20, 20));

        JButton loginButton = new JButton("Dang nhap");
        JButton resetButton = new JButton("Nhap lai");
        JButton exitButton = new JButton("Thoat");

        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resetButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        loginButton.addActionListener(e -> handleLogin());
        resetButton.addActionListener(e -> resetForm());
        exitButton.addActionListener(e -> dispose());

        panel.add(loginButton);
        panel.add(resetButton);
        panel.add(exitButton);
        return panel;
    }

    private void togglePasswordVisibility() {
        passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? '\0' : '*');
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Vui long nhap day du ten dang nhap va mat khau.",
                    "Thieu thong tin",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (DEMO_USERNAME.equals(username) && DEMO_PASSWORD.equals(password)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Dang nhap thanh cong!",
                    "Thong bao",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Ten dang nhap hoac mat khau khong dung.",
                    "Dang nhap that bai",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.selectAll();
            passwordField.requestFocusInWindow();
        }
    }

    private void resetForm() {
        usernameField.setText("");
        passwordField.setText("");
        showPasswordCheckBox.setSelected(false);
        togglePasswordVisibility();
        usernameField.requestFocusInWindow();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Default Swing look and feel is acceptable if system look and feel is unavailable.
            }
            new bai_2().setVisible(true);
        });
    }
}
