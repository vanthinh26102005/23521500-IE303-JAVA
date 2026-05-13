import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

public class BTTH3 extends JFrame {
    private static final int WINDOW_WIDTH = 1180;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color PAGE_BG = Color.WHITE;
    private static final Color CARD_BG = new Color(242, 242, 242);
    private static final Color TEXT_DARK = new Color(74, 74, 74);
    private static final Color TEXT_MUTED = new Color(166, 166, 166);
    private static final Color SELECTED_BLUE = new Color(96, 148, 255);

    private final List<Product> products = new ArrayList<>();
    private final List<ProductCard> cards = new ArrayList<>();
    private final DetailPanel detailPanel = new DetailPanel();
    private int selectedIndex = 0;

    public BTTH3() {
        setTitle("BTTH3 - Adidas Product Shop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        createProducts();
        setContentPane(createContent());
        selectProduct(0, false);

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createContent() {
        JPanel page = new JPanel(new BorderLayout(24, 0));
        page.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        page.setBackground(PAGE_BG);
        page.setBorder(BorderFactory.createEmptyBorder(82, 16, 14, 28));

        page.add(detailPanel, BorderLayout.WEST);
        page.add(createProductScroll(), BorderLayout.CENTER);
        return page;
    }

    private JScrollPane createProductScroll() {
        JPanel grid = new JPanel(new GridLayout(0, 4, 10, 10));
        grid.setBackground(PAGE_BG);

        for (int i = 0; i < products.size(); i++) {
            ProductCard card = new ProductCard(products.get(i), i);
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ProductCard source = (ProductCard) e.getSource();
                    selectProduct(source.getProductIndex(), true);
                }
            });
            cards.add(card);
            grid.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private void selectProduct(int index, boolean animate) {
        if (index < 0 || index >= products.size()) {
            return;
        }

        selectedIndex = index;
        Product selected = products.get(index);
        detailPanel.showProduct(selected, animate);

        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setSelected(i == selectedIndex);
        }
    }

    private void createProducts() {
        String saleText = "This product is excluded from all promotional discounts and offers.";
        products.add(new Product("4DFWD PULSE SHOES", saleText, "Adidas", 160.00, "img1.png"));
        products.add(new Product("FORUM MID SHOES", saleText, "Adidas", 100.00, "img2.png"));
        products.add(new Product("SUPERNOVA SHOES", "NMD City Stock 2", "Adidas", 150.00, "img3.png"));
        products.add(new Product("Adidas", "NMD City Stock 2", "Adidas", 160.00, "img4.png"));
        products.add(new Product("Adidas", "NMD City Stock 2", "Adidas", 120.00, "img5.png"));
        products.add(new Product("4DFWD PULSE SHOES", saleText, "Adidas", 160.00, "img6.png"));
        products.add(new Product("4DFWD PULSE SHOES", saleText, "Adidas", 160.00, "img1.png"));
        products.add(new Product("FORUM MID SHOES", saleText, "Adidas", 100.00, "img2.png"));
    }

    private static BufferedImage loadProductImage(String fileName) {
        String[] candidates = {
                fileName,
                "BTTH3/" + fileName,
                "./BTTH3/" + fileName
        };

        for (String path : candidates) {
            File file = new File(path);
            if (file.exists()) {
                try {
                    return trimTransparentPixels(ImageIO.read(file));
                } catch (IOException e) {
                    break;
                }
            }
        }

        return createPlaceholderImage(fileName);
    }

    private static BufferedImage trimTransparentPixels(BufferedImage source) {
        if (source == null || !source.getColorModel().hasAlpha()) {
            return source;
        }

        int minX = source.getWidth();
        int minY = source.getHeight();
        int maxX = -1;
        int maxY = -1;

        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                int alpha = (source.getRGB(x, y) >>> 24) & 0xff;
                if (alpha > 8) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        if (maxX < minX || maxY < minY) {
            return source;
        }

        int pad = 8;
        minX = Math.max(0, minX - pad);
        minY = Math.max(0, minY - pad);
        maxX = Math.min(source.getWidth() - 1, maxX + pad);
        maxY = Math.min(source.getHeight() - 1, maxY + pad);
        return source.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    private static BufferedImage createPlaceholderImage(String text) {
        BufferedImage image = new BufferedImage(420, 260, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        setQuality(g2);
        g2.setColor(new Color(230, 230, 230));
        g2.fillRoundRect(0, 0, 420, 260, 28, 28);
        g2.setColor(TEXT_MUTED);
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        drawCenteredString(g2, text, 0, 0, 420, 260);
        g2.dispose();
        return image;
    }

    private static void drawImageFit(Graphics2D g2, Image image, int x, int y, int width, int height) {
        if (image == null || image.getWidth(null) <= 0 || image.getHeight(null) <= 0) {
            return;
        }

        double scale = Math.min((double) width / image.getWidth(null), (double) height / image.getHeight(null));
        int drawWidth = (int) Math.round(image.getWidth(null) * scale);
        int drawHeight = (int) Math.round(image.getHeight(null) * scale);
        int drawX = x + (width - drawWidth) / 2;
        int drawY = y + (height - drawHeight) / 2;
        g2.drawImage(image, drawX, drawY, drawWidth, drawHeight, null);
    }

    private static String fitText(FontMetrics fm, String text, int maxWidth) {
        if (fm.stringWidth(text) <= maxWidth) {
            return text;
        }

        String dots = "...";
        int end = text.length();
        while (end > 0 && fm.stringWidth(text.substring(0, end) + dots) > maxWidth) {
            end--;
        }
        return end == 0 ? dots : text.substring(0, end) + dots;
    }

    private static void drawCenteredString(Graphics2D g2, String text, int x, int y, int width, int height) {
        FontMetrics fm = g2.getFontMetrics();
        int drawX = x + (width - fm.stringWidth(text)) / 2;
        int drawY = y + ((height - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(text, drawX, drawY);
    }

    private static void setQuality(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Keep the default look and feel if the system one is unavailable.
            }
            new BTTH3().setVisible(true);
        });
    }

    private static class Product {
        private final String name;
        private final String description;
        private final String brand;
        private final double price;
        private final BufferedImage image;

        Product(String name, String description, String brand, double price, String imageFile) {
            this.name = name;
            this.description = description;
            this.brand = brand;
            this.price = price;
            this.image = loadProductImage(imageFile);
        }

        String priceText() {
            return String.format(Locale.US, "$%.2f", price);
        }
    }

    private static class DetailPanel extends JPanel {
        private final HeroImagePanel imagePanel = new HeroImagePanel();
        private final JLabel nameLabel = createLabel(Font.BOLD, 21, TEXT_DARK);
        private final JLabel priceLabel = createLabel(Font.BOLD, 21, TEXT_DARK);
        private final JLabel brandLabel = createLabel(Font.PLAIN, 13, TEXT_DARK);
        private final JLabel descriptionLabel = createLabel(Font.BOLD, 14, TEXT_MUTED);

        DetailPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(282, 504));
            setLayout(new BorderLayout(0, 0));

            add(imagePanel, BorderLayout.NORTH);

            JPanel info = new JPanel(null);
            info.setOpaque(false);
            info.setPreferredSize(new Dimension(282, 260));

            JSeparatorLine separator = new JSeparatorLine();
            separator.setBounds(0, 0, 282, 1);
            nameLabel.setBounds(0, 20, 282, 28);
            priceLabel.setBounds(0, 52, 282, 28);
            brandLabel.setBounds(0, 88, 282, 22);
            descriptionLabel.setBounds(0, 118, 282, 72);

            info.add(separator);
            info.add(nameLabel);
            info.add(priceLabel);
            info.add(brandLabel);
            info.add(descriptionLabel);
            add(info, BorderLayout.CENTER);
        }

        void showProduct(Product product, boolean animate) {
            imagePanel.showImage(product.image, animate);
            nameLabel.setText(product.name);
            priceLabel.setText(product.priceText());
            brandLabel.setText(product.brand);
            descriptionLabel.setText(wrapText(product.description, 36));
        }

        private static JLabel createLabel(int style, int size, Color color) {
            JLabel label = new JLabel();
            label.setFont(new Font("Arial", style, size));
            label.setForeground(color);
            label.setVerticalAlignment(SwingConstants.TOP);
            return label;
        }

        private static String wrapText(String text, int maxCharsPerLine) {
            StringBuilder html = new StringBuilder("<html>");
            int lineLength = 0;
            for (String word : text.split(" ")) {
                if (lineLength > 0 && lineLength + word.length() > maxCharsPerLine) {
                    html.append("<br>");
                    lineLength = 0;
                } else if (lineLength > 0) {
                    html.append(' ');
                    lineLength++;
                }
                html.append(word);
                lineLength += word.length();
            }
            html.append("</html>");
            return html.toString();
        }
    }

    private static class HeroImagePanel extends JPanel {
        private BufferedImage currentImage;
        private BufferedImage oldImage;
        private BufferedImage nextImage;
        private double progress = 1.0;
        private Timer timer;

        HeroImagePanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(282, 168));
        }

        void showImage(BufferedImage image, boolean animate) {
            if (!animate || currentImage == null) {
                stopTimer();
                currentImage = image;
                oldImage = null;
                nextImage = null;
                progress = 1.0;
                repaint();
                return;
            }

            if (currentImage == image) {
                return;
            }

            oldImage = currentImage;
            nextImage = image;
            progress = 0.0;
            stopTimer();

            timer = new Timer(16, e -> {
                progress += 0.075;
                if (progress >= 1.0) {
                    progress = 1.0;
                    currentImage = nextImage;
                    oldImage = null;
                    nextImage = null;
                    stopTimer();
                }
                repaint();
            });
            timer.start();
        }

        private void stopTimer() {
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            setQuality(g2);

            if (oldImage != null && nextImage != null) {
                float oldAlpha = (float) (1.0 - progress);
                float nextAlpha = (float) progress;
                drawAnimatedImage(g2, oldImage, oldAlpha, -18 * progress, 1.0 - 0.04 * progress);
                drawAnimatedImage(g2, nextImage, nextAlpha, 18 * (1.0 - progress), 0.96 + 0.04 * progress);
            } else {
                drawImageFit(g2, currentImage, 8, 8, getWidth() - 16, getHeight() - 18);
            }

            g2.dispose();
        }

        private void drawAnimatedImage(Graphics2D g2, BufferedImage image, float alpha, double offsetX, double scale) {
            Graphics2D copy = (Graphics2D) g2.create();
            copy.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            int boxX = (int) Math.round(8 + offsetX);
            int boxY = (int) Math.round(8 + (1.0 - scale) * 18);
            int boxW = (int) Math.round((getWidth() - 16) * scale);
            int boxH = (int) Math.round((getHeight() - 18) * scale);
            drawImageFit(copy, image, boxX, boxY, boxW, boxH);
            copy.dispose();
        }
    }

    private static class ProductCard extends JPanel {
        private final Product product;
        private final int productIndex;
        private boolean selected;
        private boolean hovered;

        ProductCard(Product product, int productIndex) {
            this.product = product;
            this.productIndex = productIndex;
            setPreferredSize(new Dimension(200, 238));
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setToolTipText(product.name);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        int getProductIndex() {
            return productIndex;
        }

        void setSelected(boolean selected) {
            this.selected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            setQuality(g2);

            int arc = 14;
            int inset = selected ? 1 : 0;
            RoundRectangle2D card = new RoundRectangle2D.Double(
                    inset,
                    inset,
                    getWidth() - inset * 2 - 1,
                    getHeight() - inset * 2 - 1,
                    arc,
                    arc
            );

            g2.setColor(hovered ? new Color(236, 236, 236) : CARD_BG);
            g2.fill(card);

            if (selected) {
                g2.setStroke(new BasicStroke(1.4f));
                g2.setColor(SELECTED_BLUE);
                g2.draw(card);
            }

            g2.setColor(TEXT_DARK);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            FontMetrics titleMetrics = g2.getFontMetrics();
            g2.drawString(fitText(titleMetrics, product.name, 168), 10, 28);

            g2.setColor(TEXT_MUTED);
            g2.setFont(new Font("Arial", Font.BOLD, 13));
            FontMetrics descMetrics = g2.getFontMetrics();
            g2.drawString(fitText(descMetrics, product.description, 168), 10, 54);

            drawImageFit(g2, product.image, 18, 72, 164, 92);

            g2.setColor(TEXT_DARK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString(product.brand, 10, 222);

            g2.setFont(new Font("Arial", Font.BOLD, 20));
            String price = product.priceText();
            int priceWidth = g2.getFontMetrics().stringWidth(price);
            g2.drawString(price, getWidth() - 10 - priceWidth, 222);

            g2.dispose();
        }
    }

    private static class JSeparatorLine extends JPanel {
        JSeparatorLine() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(170, 178, 188));
            g.drawLine(0, 0, getWidth(), 0);
        }
    }
}
