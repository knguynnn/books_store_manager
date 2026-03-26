package Frontend.GUI.NhapHangGUI;

import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public final class NhapHangActions {
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,### đ");

    private NhapHangActions() {
    }

    public static String extractCode(String rawText) {
        if (rawText == null) {
            return "";
        }
        String text = rawText.trim();
        if (text.isEmpty()) {
            return "";
        }
        int splitIndex = text.indexOf(" - ");
        if (splitIndex > 0) {
            return text.substring(0, splitIndex).trim();
        }
        return text;
    }

    public static long parseMoney(String rawText) {
        if (rawText == null) {
            throw new NumberFormatException("Giá trị rỗng");
        }
        String digits = rawText.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) {
            throw new NumberFormatException("Giá trị không hợp lệ");
        }
        return Long.parseLong(digits);
    }

    public static String formatMoney(long value) {
        return CURRENCY_FORMAT.format(value);
    }

    public static void selectComboByCode(JComboBox<String> comboBox, String code) {
        if (comboBox == null || code == null || code.isBlank()) {
            return;
        }
        String normalizedCode = code.trim();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            String itemCode = extractCode(String.valueOf(comboBox.getItemAt(i)));
            if (itemCode.equalsIgnoreCase(normalizedCode)) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
        comboBox.setSelectedItem(normalizedCode);
    }

    public static void chooseFromCombo(Component parent, JComboBox<String> comboBox, String title) {
        if (comboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(parent, "Danh sách đang trống", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Object selected = JOptionPane.showInputDialog(
                parent,
                "Chọn " + title + ":",
                "Chọn dữ liệu",
                JOptionPane.PLAIN_MESSAGE,
                null,
                getComboItems(comboBox),
                comboBox.getSelectedItem());
        if (selected != null) {
            comboBox.setSelectedItem(selected);
        }
    }

    private static Object[] getComboItems(JComboBox<String> comboBox) {
        Object[] items = new Object[comboBox.getItemCount()];
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            items[i] = comboBox.getItemAt(i);
        }
        return items;
    }
}
