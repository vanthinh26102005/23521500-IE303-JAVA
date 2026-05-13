import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class bai_3 extends JFrame {
    private final DecimalFormat numberFormat = new DecimalFormat(
            "0.##########",
            DecimalFormatSymbols.getInstance(Locale.US));
    private final JTextField displayField = new JTextField("0");
    private final JLabel expressionLabel = new JLabel(" ");

    private double storedValue;
    private String pendingOperator;
    private boolean startNewNumber = true;

    public bai_3() {
        setTitle("BT3 - May tinh co ban");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(createDisplayPanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 10, 18));

        expressionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        expressionLabel.setForeground(Color.GRAY);
        expressionLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        displayField.setEditable(false);
        displayField.setHorizontalAlignment(SwingConstants.RIGHT);
        displayField.setFont(new Font("Arial", Font.BOLD, 28));
        displayField.setBackground(Color.WHITE);
        displayField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));

        panel.add(expressionLabel, BorderLayout.NORTH);
        panel.add(displayField, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 4, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 18, 18, 18));

        String[] buttons = {
                "C", "CE", "<-", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "+/-", "0", ".", "="
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.setFocusPainted(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.addActionListener(e -> handleButton(text));
            panel.add(button);
        }

        return panel;
    }

    private void handleButton(String command) {
        if (command.matches("\\d")) {
            appendDigit(command);
            return;
        }

        switch (command) {
            case ".":
                appendDecimalPoint();
                break;
            case "+/-":
                changeSign();
                break;
            case "C":
                clearAll();
                break;
            case "CE":
                clearEntry();
                break;
            case "<-":
                backspace();
                break;
            case "+":
            case "-":
            case "*":
            case "/":
                chooseOperator(command);
                break;
            case "=":
                calculateResult();
                break;
            default:
                break;
        }
    }

    private void appendDigit(String digit) {
        if (startNewNumber || displayField.getText().equals("0")) {
            displayField.setText(digit);
            startNewNumber = false;
        } else {
            displayField.setText(displayField.getText() + digit);
        }
    }

    private void appendDecimalPoint() {
        if (startNewNumber) {
            displayField.setText("0.");
            startNewNumber = false;
        } else if (!displayField.getText().contains(".")) {
            displayField.setText(displayField.getText() + ".");
        }
    }

    private void changeSign() {
        String text = displayField.getText();
        if (text.equals("0")) {
            return;
        }
        displayField.setText(text.startsWith("-") ? text.substring(1) : "-" + text);
    }

    private void clearAll() {
        displayField.setText("0");
        expressionLabel.setText(" ");
        storedValue = 0;
        pendingOperator = null;
        startNewNumber = true;
    }

    private void clearEntry() {
        displayField.setText("0");
        startNewNumber = true;
    }

    private void backspace() {
        if (startNewNumber) {
            return;
        }

        String text = displayField.getText();
        if (text.length() <= 1 || (text.length() == 2 && text.startsWith("-"))) {
            displayField.setText("0");
            startNewNumber = true;
        } else {
            displayField.setText(text.substring(0, text.length() - 1));
        }
    }

    private void chooseOperator(String operator) {
        double currentValue = readDisplayValue();

        if (pendingOperator != null && !startNewNumber) {
            if (!applyPendingOperation(currentValue)) {
                return;
            }
            currentValue = readDisplayValue();
        }

        storedValue = currentValue;
        pendingOperator = operator;
        expressionLabel.setText(formatNumber(storedValue) + " " + operator);
        startNewNumber = true;
    }

    private void calculateResult() {
        if (pendingOperator == null) {
            return;
        }

        double currentValue = readDisplayValue();
        String expression = formatNumber(storedValue) + " " + pendingOperator + " "
                + formatNumber(currentValue) + " =";

        if (applyPendingOperation(currentValue)) {
            expressionLabel.setText(expression);
            pendingOperator = null;
            startNewNumber = true;
        }
    }

    private boolean applyPendingOperation(double secondValue) {
        double result;

        switch (pendingOperator) {
            case "+":
                result = storedValue + secondValue;
                break;
            case "-":
                result = storedValue - secondValue;
                break;
            case "*":
                result = storedValue * secondValue;
                break;
            case "/":
                if (secondValue == 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Khong the chia cho 0.",
                            "Loi tinh toan",
                            JOptionPane.ERROR_MESSAGE);
                    clearAll();
                    return false;
                }
                result = storedValue / secondValue;
                break;
            default:
                return false;
        }

        storedValue = result;
        displayField.setText(formatNumber(result));
        return true;
    }

    private double readDisplayValue() {
        return Double.parseDouble(displayField.getText());
    }

    private String formatNumber(double value) {
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            return "0";
        }
        if (Math.abs(value) < 0.0000000001) {
            return "0";
        }
        return numberFormat.format(value);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Default Swing look and feel is acceptable if system look and feel is unavailable.
            }
            new bai_3().setVisible(true);
        });
    }
}
