package com.akaita.android.easylauncher.filter;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class ColorRibbonFilter implements EasyLauncherFilter {

    static final boolean debug = Boolean.parseBoolean(System.getenv("EASYLAUNCHER_DEBUG"));

    final Color ribbonColor;

    final Color labelColor;

    String label;

    String fontName = "Default";

    int fontStyle = Font.PLAIN;

    boolean largeRibbon = false;

    public ColorRibbonFilter(String label, Color ribbonColor, Color labelColor) {
        this.label = label;
        this.ribbonColor = ribbonColor;
        this.labelColor = labelColor;
    }

    public ColorRibbonFilter(String label, Color ribbonColor) {
        this(label, ribbonColor, Color.WHITE);
    }

    private static int calculateMaxLabelWidth(int y) {
        return (int) Math.sqrt(Math.pow(y, 2) * 2);
    }

    private static void drawString(Graphics2D g, String str, int x, int y) {
        g.drawString(str, x, y);

        if (debug) {
            FontMetrics fm = g.getFontMetrics();
            Rectangle2D bounds = g.getFont().getStringBounds(str,
                    new FontRenderContext(g.getTransform(), true, true));

            g.drawRect(x, y - fm.getAscent(), (int) bounds.getWidth(), fm.getAscent());
        }
    }

    @Override
    public void setAdaptiveLauncherMode(boolean enable) {
        largeRibbon = enable;
    }

    @Override
    public void apply(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        Graphics2D g = (Graphics2D) image.getGraphics();

        g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(-45)));

        int y = height / (largeRibbon ? 2 : 4);

        // calculate the rectangle where the label is rendered
        FontRenderContext frc = new FontRenderContext(g.getTransform(), true, true);
        int maxLabelWidth = calculateMaxLabelWidth(y);
        g.setFont(getFont(maxLabelWidth, frc));
        Rectangle2D labelBounds = g.getFont().getStringBounds(label == null ? "" : label, frc);

        // draw the ribbon
        g.setColor(ribbonColor);
        g.fillRect(-width, y, width * 2, (int) (labelBounds.getHeight()));

        if (label != null) {
            // draw the label
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(labelColor);

            FontMetrics fm = g.getFontMetrics();

            drawString(g, label,
                    (int) -labelBounds.getWidth() / 2,
                    y + fm.getAscent());
        }
        g.dispose();
    }

    private Font getFont(int maxLabelWidth, FontRenderContext frc) {
        int max = largeRibbon ? 64 : 32;
        if (label == null) {
            return new Font(fontName, fontStyle, max / 2);
        }
        int min = 0;
        int x = max;

        for (int i = 0; i < 10; i++) {
            int m = ((max + min) / 2);
            if (m == x) {
                break;
            }

            Font font = new Font(fontName, fontStyle, m);
            Rectangle2D labelBounds = font.getStringBounds(label, frc);
            int px = (int) labelBounds.getWidth();

            if (px > maxLabelWidth) {
                max = m;
            } else {
                min = m;
            }
            x = m;
        }
        return new Font(fontName, fontStyle, x);
    }
}
