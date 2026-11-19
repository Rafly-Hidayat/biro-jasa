package com.kelompok2.pajakform;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

/**
 * Membuat konten JFrame yang memakai layout absolut (setLayout(null))
 * ikut menyesuaikan ukuran ketika jendela di-resize.
 *
 * Bounds, font, dan row height tabel direkam saat enable() dipanggil
 * dan diskalakan secara proporsional setiap kali contentPane berubah ukuran.
 */
public final class Responsive {

    private Responsive() {
    }

    public static void enable(JFrame frame) {
        final Container content = frame.getContentPane();

        final Map<Component, Rectangle> bounds = new LinkedHashMap<>();
        for (Component c : content.getComponents()) {
            bounds.put(c, new Rectangle(c.getBounds()));
        }

        final Map<Component, Font> fonts = new LinkedHashMap<>();
        captureFonts(content, fonts);

        final Map<JTable, Integer> rowHeights = new LinkedHashMap<>();
        captureRowHeights(content, rowHeights);

        final Dimension[] designSize = new Dimension[] { null };

        content.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = content.getWidth();
                int h = content.getHeight();
                if (w <= 0 || h <= 0) {
                    return;
                }
                if (designSize[0] == null) {
                    designSize[0] = new Dimension(w, h);
                    return;
                }
                float sx = (float) w / designSize[0].width;
                float sy = (float) h / designSize[0].height;
                float sf = Math.min(sx, sy);

                for (Map.Entry<Component, Rectangle> entry : bounds.entrySet()) {
                    Rectangle r = entry.getValue();
                    entry.getKey().setBounds(
                            Math.round(r.x * sx),
                            Math.round(r.y * sy),
                            Math.round(r.width * sx),
                            Math.round(r.height * sy));
                }
                for (Map.Entry<Component, Font> entry : fonts.entrySet()) {
                    Font orig = entry.getValue();
                    float newSize = Math.max(8f, orig.getSize2D() * sf);
                    entry.getKey().setFont(orig.deriveFont(newSize));
                }
                for (Map.Entry<JTable, Integer> entry : rowHeights.entrySet()) {
                    int newRow = Math.max(12, Math.round(entry.getValue() * sf));
                    entry.getKey().setRowHeight(newRow);
                }

                content.revalidate();
                content.repaint();
            }
        });
    }

    private static void captureFonts(Container c, Map<Component, Font> map) {
        for (Component child : c.getComponents()) {
            if (isTextual(child) && child.getFont() != null) {
                map.put(child, child.getFont());
            }
            if (child instanceof JTable) {
                JTableHeader h = ((JTable) child).getTableHeader();
                if (h != null && h.getFont() != null) {
                    map.put(h, h.getFont());
                }
            }
            if (child instanceof Container) {
                captureFonts((Container) child, map);
            }
        }
    }

    private static void captureRowHeights(Container c, Map<JTable, Integer> map) {
        for (Component child : c.getComponents()) {
            if (child instanceof JTable) {
                map.put((JTable) child, ((JTable) child).getRowHeight());
            }
            if (child instanceof Container) {
                captureRowHeights((Container) child, map);
            }
        }
    }

    private static boolean isTextual(Component c) {
        return c instanceof JLabel
                || c instanceof AbstractButton
                || c instanceof JTextComponent
                || c instanceof JTable
                || c instanceof JTableHeader
                || c instanceof JComboBox;
    }
}
