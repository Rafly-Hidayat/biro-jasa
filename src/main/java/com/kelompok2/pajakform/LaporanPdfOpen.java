package com.kelompok2.pajakform;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.table.TableModel;

/**
 * Versi laporan PDF berbasis OpenPDF.
 *
 * Perilakunya setara {@link LaporanPdf} — header (logo + identitas), body (tabel),
 * footer (nomor halaman) dan blok tanda tangan user yang sedang login — tetapi
 * paginasi, pengulangan header tabel, dan pembungkusan teks ditangani oleh library.
 */
public final class LaporanPdfOpen {

    public static final String PERUSAHAAN = "PT BIRO JASA AKBAR BERJAYA";
    public static final String SUB_PERUSAHAAN = "Biro Jasa Pajak Kendaraan Bermotor";
    public static final String ALAMAT = "Jl. H. Rijin Gg. H. Sarih No. 133a, RT 05/RW 11, Tugu, "
            + "Kec. Cimanggis, Kota Depok, Jawa Barat 16451";
    public static final String TELEPON = "Telp. 081514130554";
    public static final String KOTA = "Depok";

    private static final String LOGO_PATH = "/images/logo-akbar-rounded.png";

    /**
     * A4 mendatar sebagai MediaBox langsung. {@code PageSize.A4.rotate()} hanya menyetel
     * flag {@code /Rotate}, yang diabaikan sebagian renderer sehingga isi tampil memutar.
     */
    private static final Rectangle A4_LANDSCAPE =
            new Rectangle(PageSize.A4.getHeight(), PageSize.A4.getWidth());

    private static final Color NAVY = new Color(18, 42, 84);
    private static final Color NAVY_SOFT = new Color(236, 240, 248);
    private static final Color GOLD = new Color(198, 160, 68);
    private static final Color TEKS = new Color(18, 24, 38);
    private static final Color MUTED = new Color(92, 105, 122);
    private static final Color GRID = new Color(206, 214, 226);
    private static final Color TOTAL_BG = new Color(249, 243, 227);

    private static final float MARGIN = 32f;
    private static final float HEADER_HEIGHT = 74f;
    private static final float FOOTER_HEIGHT = 26f;
    private static final float CELL_PADDING = 4f;
    private static final float LOGO_HEIGHT = 50f;
    private static final float SIGNATURE_WIDTH = 210f;
    private static final float SIGNATURE_GAP = 40f;
    private static final float COL_MIN_WIDTH = 32f;
    private static final float COL_MAX_WIDTH = 150f;
    private static final float NO_COL_WIDTH = 26f;

    private static final Font FONT_CELL = font(7.5f, Font.NORMAL, TEKS);
    private static final Font FONT_CELL_MUTED = font(7.5f, Font.NORMAL, MUTED);
    private static final Font FONT_CELL_HEAD = font(7.5f, Font.BOLD, Color.WHITE);
    private static final Font FONT_TOTAL = font(7.5f, Font.BOLD, NAVY);
    private static final Font FONT_FOOTER = font(7.5f, Font.NORMAL, MUTED);
    private static final Font FONT_TTD = font(8.5f, Font.NORMAL, TEKS);
    private static final Font FONT_NAMA = font(9f, Font.BOLD, NAVY);
    private static final Font FONT_USERNAME = font(7.5f, Font.NORMAL, MUTED);

    private static final NumberFormat NOMINAL = NumberFormat.getInstance(new Locale("id", "ID"));
    private static final SimpleDateFormat TANGGAL_PANJANG =
            new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
    private static final SimpleDateFormat TANGGAL_JAM =
            new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("id", "ID"));

    private final String judul;
    private final String periode;
    private final String namaPetugas;
    private final String usernamePetugas;
    private final String[] columns;
    private final List<String[]> rows;
    private final boolean[] rataKanan;
    private final String[] totals;

    public LaporanPdfOpen(String judul, String periode, String namaPetugas, String usernamePetugas,
            String[] columns, List<String[]> rows) {
        this.judul = judul;
        this.periode = periode;
        this.namaPetugas = namaPetugas;
        this.usernamePetugas = usernamePetugas;
        this.columns = columns.clone();
        this.rows = List.copyOf(rows);
        this.rataKanan = new boolean[this.columns.length];
        for (int c = 0; c < this.columns.length; c++) {
            this.rataKanan[c] = kolomNominal(this.columns[c]);
        }
        this.totals = hitungTotal();
    }

    /** Ambil seluruh isi {@link TableModel}. */
    public static LaporanPdfOpen fromTableModel(TableModel model, String judul, String periode,
            String namaPetugas, String usernamePetugas) {
        return fromTableModel(model, judul, periode, namaPetugas, usernamePetugas, null);
    }

    /**
     * Ambil sebagian kolom {@link TableModel} sesuai {@code kolomDipakai} (urutan mengikuti
     * daftar tersebut). Nama kolom yang tidak ada di model diabaikan; {@code null} berarti
     * semua kolom ikut dicetak.
     */
    public static LaporanPdfOpen fromTableModel(TableModel model, String judul, String periode,
            String namaPetugas, String usernamePetugas, String[] kolomDipakai) {
        int[] indexes = resolveColumns(model, kolomDipakai);
        String[] cols = new String[indexes.length];
        for (int c = 0; c < indexes.length; c++) {
            cols[c] = model.getColumnName(indexes[c]);
        }
        List<String[]> data = new ArrayList<>(model.getRowCount());
        for (int r = 0; r < model.getRowCount(); r++) {
            String[] row = new String[indexes.length];
            for (int c = 0; c < indexes.length; c++) {
                Object value = model.getValueAt(r, indexes[c]);
                row[c] = value == null ? "" : String.valueOf(value);
            }
            data.add(row);
        }
        return new LaporanPdfOpen(judul, periode, namaPetugas, usernamePetugas, cols, data);
    }

    private static int[] resolveColumns(TableModel model, String[] kolomDipakai) {
        if (kolomDipakai == null || kolomDipakai.length == 0) {
            int[] all = new int[model.getColumnCount()];
            for (int c = 0; c < all.length; c++) {
                all[c] = c;
            }
            return all;
        }
        List<Integer> picked = new ArrayList<>(kolomDipakai.length);
        for (String wanted : kolomDipakai) {
            for (int c = 0; c < model.getColumnCount(); c++) {
                if (model.getColumnName(c).equalsIgnoreCase(wanted)) {
                    picked.add(c);
                    break;
                }
            }
        }
        if (picked.isEmpty()) {
            return resolveColumns(model, null);
        }
        int[] indexes = new int[picked.size()];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = picked.get(i);
        }
        return indexes;
    }

    public void writeTo(File target) throws IOException {
        try (OutputStream out = Files.newOutputStream(target.toPath())) {
            writeTo(out);
        }
    }

    public void writeTo(OutputStream out) throws IOException {
        Document document = new Document(A4_LANDSCAPE,
                MARGIN, MARGIN, MARGIN + HEADER_HEIGHT, MARGIN + FOOTER_HEIGHT);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            writer.setPageEvent(new HeaderFooter(new Date()));
            document.open();
            document.add(buildTable());
            document.add(buildSignature());
            document.close();
        } catch (DocumentException ex) {
            throw new IOException("Gagal menyusun PDF: " + ex.getMessage(), ex);
        }
    }

    // --- body -------------------------------------------------------------

    private PdfPTable buildTable() throws DocumentException {
        PdfPTable table = new PdfPTable(columns.length + 1);
        table.setWidthPercentage(100f);
        table.setWidths(columnWidths());
        table.setHeaderRows(1);

        table.addCell(headCell("No", Element.ALIGN_LEFT));
        for (int c = 0; c < columns.length; c++) {
            table.addCell(headCell(columns[c],
                    rataKanan[c] ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT));
        }

        for (int r = 0; r < rows.size(); r++) {
            Color background = r % 2 == 1 ? NAVY_SOFT : Color.WHITE;
            table.addCell(bodyCell(String.valueOf(r + 1), FONT_CELL_MUTED,
                    Element.ALIGN_LEFT, background));
            String[] row = rows.get(r);
            for (int c = 0; c < columns.length; c++) {
                String value = c < row.length ? row[c] : "";
                table.addCell(bodyCell(value, FONT_CELL,
                        rataKanan[c] ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT, background));
            }
        }

        if (hasTotals()) {
            addTotalRow(table);
        }
        return table;
    }

    private void addTotalRow(PdfPTable table) {
        table.addCell(totalCell("", Element.ALIGN_LEFT, true));
        boolean labelDrawn = false;
        for (int c = 0; c < columns.length; c++) {
            if (totals[c] != null) {
                table.addCell(totalCell(totals[c], Element.ALIGN_RIGHT, true));
            } else if (!labelDrawn) {
                table.addCell(totalCell("TOTAL", Element.ALIGN_LEFT, true));
                labelDrawn = true;
            } else {
                table.addCell(totalCell("", Element.ALIGN_LEFT, true));
            }
        }
    }

    private static PdfPCell headCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_CELL_HEAD));
        cell.setBackgroundColor(NAVY);
        cell.setBorderColor(NAVY);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(CELL_PADDING);
        return cell;
    }

    private static PdfPCell bodyCell(String text, Font font, int alignment, Color background) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(background);
        cell.setBorderColor(GRID);
        cell.setBorderWidth(0.4f);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(CELL_PADDING);
        return cell;
    }

    private static PdfPCell totalCell(String text, int alignment, boolean topBorder) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_TOTAL));
        cell.setBackgroundColor(TOTAL_BG);
        cell.setBorderColor(GRID);
        cell.setBorderWidth(0.4f);
        if (topBorder) {
            cell.setBorderColorTop(NAVY);
            cell.setBorderWidthTop(1f);
        }
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(CELL_PADDING);
        return cell;
    }

    // --- tanda tangan -----------------------------------------------------

    private PdfPTable buildSignature() {
        PdfPTable block = new PdfPTable(1);
        block.setWidthPercentage(SIGNATURE_WIDTH / printableWidth() * 100f);
        block.setHorizontalAlignment(Element.ALIGN_RIGHT);
        block.setSpacingBefore(14f);
        block.setKeepTogether(true);

        block.addCell(ttdCell(KOTA + ", " + TANGGAL_PANJANG.format(new Date()), FONT_TTD, 0f));
        block.addCell(ttdCell(" ", FONT_TTD, SIGNATURE_GAP));

        String nama = namaPetugas == null || namaPetugas.isBlank() ? "-" : namaPetugas;
        PdfPCell namaCell = ttdCell("( " + nama + " )", FONT_NAMA, 0f);
        namaCell.setBorderWidthTop(0.7f);
        namaCell.setBorderColorTop(TEKS);
        namaCell.setPaddingTop(5f);
        block.addCell(namaCell);
        
        return block;
    }

    private static float printableWidth() {
        return A4_LANDSCAPE.getWidth() - (MARGIN * 2);
    }

    private static PdfPCell ttdCell(String text, Font font, float extraHeight) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPaddingTop(2f);
        cell.setPaddingBottom(2f + extraHeight);
        return cell;
    }

    // --- header + footer tiap halaman -------------------------------------

    private final class HeaderFooter extends PdfPageEventHelper {

        private final Date dicetak;
        private PdfTemplate totalPagesBox;

        private HeaderFooter(Date dicetak) {
            this.dicetak = dicetak;
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            totalPagesBox = writer.getDirectContent().createTemplate(40f, 10f);
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            drawHeader(writer, document);
            drawFooter(writer, document);
        }

        /** Isi jumlah halaman sebenarnya setelah seluruh halaman selesai dibuat. */
        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            String total = String.valueOf(writer.getPageNumber() - 1);
            totalPagesBox.beginText();
            totalPagesBox.setFontAndSize(FONT_FOOTER.getBaseFont(), 7.5f);
            totalPagesBox.setColorFill(MUTED);
            totalPagesBox.showText(total);
            totalPagesBox.endText();
        }

        private void drawHeader(PdfWriter writer, Document document) {
            Rectangle page = document.getPageSize();
            float left = MARGIN;
            float right = page.getWidth() - MARGIN;
            float top = page.getHeight() - MARGIN;
            float textX = left;

            Image logo = loadLogo();
            if (logo != null) {
                float logoWidth = LOGO_HEIGHT * (logo.getWidth() / logo.getHeight());
                logo.scaleAbsolute(logoWidth, LOGO_HEIGHT);
                logo.setAbsolutePosition(left, top - LOGO_HEIGHT + 2f);
                try {
                    writer.getDirectContent().addImage(logo);
                } catch (DocumentException ignored) {
                    // Logo gagal digambar: header tetap tampil tanpa logo.
                }
                textX = left + logoWidth + 12f;
            }

            PdfContentByte canvas = writer.getDirectContent();
            show(canvas, textX, top - 12f, font(15f, Font.BOLD, NAVY), PERUSAHAAN, Element.ALIGN_LEFT);
            show(canvas, textX, top - 26f, font(8.5f, Font.NORMAL, MUTED), SUB_PERUSAHAAN, Element.ALIGN_LEFT);
            show(canvas, textX, top - 37f, font(7.5f, Font.NORMAL, MUTED), ALAMAT, Element.ALIGN_LEFT);
            show(canvas, textX, top - 47f, font(7.5f, Font.NORMAL, MUTED), TELEPON, Element.ALIGN_LEFT);

            show(canvas, right, top - 11f, font(13f, Font.BOLD, NAVY),
                    "LAPORAN " + judul.toUpperCase(new Locale("id", "ID")), Element.ALIGN_RIGHT);
            show(canvas, right, top - 27f, font(8f, Font.NORMAL, TEKS),
                    "Periode: " + periode, Element.ALIGN_RIGHT);
            show(canvas, right, top - 39f, font(8f, Font.NORMAL, MUTED),
                    "Jumlah data: " + rows.size() + " baris", Element.ALIGN_RIGHT);
            show(canvas, right, top - 51f, font(8f, Font.NORMAL, MUTED),
                    "Dicetak: " + TANGGAL_JAM.format(dicetak), Element.ALIGN_RIGHT);

            float ruleY = top - HEADER_HEIGHT + 12f;
            line(canvas, left, ruleY, right, ruleY, 1.4f, NAVY);
            line(canvas, left, ruleY - 2.6f, right, ruleY - 2.6f, 0.8f, GOLD);
        }

        private void drawFooter(PdfWriter writer, Document document) {
            Rectangle page = document.getPageSize();
            float left = MARGIN;
            float right = page.getWidth() - MARGIN;
            float baseline = MARGIN + 6f;

            PdfContentByte canvas = writer.getDirectContent();
            line(canvas, left, baseline + 12f, right, baseline + 12f, 0.5f, GRID);

            String operator = namaPetugas == null || namaPetugas.isBlank() ? "-" : namaPetugas;
            show(canvas, left, baseline, FONT_FOOTER, "Dicetak oleh: " + operator, Element.ALIGN_LEFT);
            show(canvas, page.getWidth() / 2f, baseline, FONT_FOOTER,
                    PERUSAHAAN + " - " + SUB_PERUSAHAAN, Element.ALIGN_CENTER);

            String prefix = "Halaman " + writer.getPageNumber() + " dari ";
            float prefixWidth = FONT_FOOTER.getBaseFont().getWidthPoint(prefix, 7.5f);
            float totalWidth = 14f;
            float startX = right - prefixWidth - totalWidth;
            show(canvas, startX, baseline, FONT_FOOTER, prefix, Element.ALIGN_LEFT);
            canvas.addTemplate(totalPagesBox, startX + prefixWidth, baseline);
        }
    }

    private static void show(PdfContentByte canvas, float x, float y, Font font, String text,
            int alignment) {
        canvas.beginText();
        canvas.setFontAndSize(font.getBaseFont(), font.getSize());
        canvas.setColorFill(font.getColor());
        canvas.showTextAligned(alignment, text, x, y, 0f);
        canvas.endText();
    }

    private static void line(PdfContentByte canvas, float x1, float y1, float x2, float y2,
            float width, Color color) {
        canvas.saveState();
        canvas.setLineWidth(width);
        canvas.setColorStroke(color);
        canvas.moveTo(x1, y1);
        canvas.lineTo(x2, y2);
        canvas.stroke();
        canvas.restoreState();
    }

    // --- perhitungan ------------------------------------------------------

    /**
     * Lebar relatif kolom: lebar alami isi dibatasi {@link #COL_MIN_WIDTH} dan
     * {@link #COL_MAX_WIDTH}. OpenPDF yang membagi sisa ruang dan membungkus teks,
     * jadi nilai ini cukup sebagai proporsi.
     */
    private float[] columnWidths() {
        float[] widths = new float[columns.length + 1];
        widths[0] = NO_COL_WIDTH;
        for (int c = 0; c < columns.length; c++) {
            float max = FONT_CELL_HEAD.getBaseFont()
                    .getWidthPoint(columns[c], FONT_CELL_HEAD.getSize());
            for (String[] row : rows) {
                String value = c < row.length ? row[c] : "";
                max = Math.max(max, FONT_CELL.getBaseFont().getWidthPoint(value, FONT_CELL.getSize()));
            }
            widths[c + 1] = clamp(max + (CELL_PADDING * 2), COL_MIN_WIDTH, COL_MAX_WIDTH);
        }
        return widths;
    }

    private String[] hitungTotal() {
        String[] hasil = new String[columns.length];
        for (int c = 0; c < columns.length; c++) {
            if (!rataKanan[c]) {
                continue;
            }
            double jumlah = 0d;
            boolean adaAngka = false;
            for (String[] row : rows) {
                Double nilai = parseNominal(c < row.length ? row[c] : "");
                if (nilai != null) {
                    jumlah += nilai;
                    adaAngka = true;
                }
            }
            if (adaAngka) {
                hasil[c] = NOMINAL.format(jumlah);
            }
        }
        return hasil;
    }

    private boolean hasTotals() {
        for (String total : totals) {
            if (total != null) {
                return true;
            }
        }
        return false;
    }

    private static Double parseNominal(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String cleaned = value.replace("Rp", "").trim();
        ParsePosition position = new ParsePosition(0);
        Number parsed = NOMINAL.parse(cleaned, position);
        if (parsed == null || position.getIndex() != cleaned.length()) {
            return null;
        }
        return parsed.doubleValue();
    }

    private static boolean kolomNominal(String nama) {
        if (nama == null) {
            return false;
        }
        String lower = nama.toLowerCase(new Locale("id", "ID"));
        return lower.startsWith("biaya") || lower.startsWith("total");
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private static Font font(float size, int style, Color color) {
        return FontFactory.getFont(FontFactory.HELVETICA, size, style, color);
    }

    private static Image loadLogo() {
        try (InputStream in = LaporanPdfOpen.class.getResourceAsStream(LOGO_PATH)) {
            if (in == null) {
                return null;
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            in.transferTo(buffer);
            return Image.getInstance(buffer.toByteArray());
        } catch (IOException | RuntimeException ex) {
            return null;
        }
    }
}
