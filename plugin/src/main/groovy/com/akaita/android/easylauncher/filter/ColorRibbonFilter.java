package com.akaita.android.easylauncher.filter;

import com.akaita.android.easylauncher.utils.LoggerHelper;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;


public class ColorRibbonFilter implements EasyLauncherFilter {

    static final boolean debug = Boolean.parseBoolean(System.getenv("EASYLAUNCHER_DEBUG"));

    private Logger logger;

    final Color ribbonColor;

    final Color labelColor;

    final LayoutPosition position;

    String label;

    String fontName = "Default";

    int fontStyle = Font.PLAIN;

    boolean largeRibbon = false;

    public enum LayoutPosition {
        TopHorizontal,
        BottomHorizontal,
        Default
    }

    public ColorRibbonFilter(String label, Color ribbonColor, Color labelColor, LayoutPosition position) {
        this.label = label;
        this.ribbonColor = ribbonColor;
        this.labelColor = labelColor;
        this.position = position;

        // only setup logger if in debug mode
        if (debug) {
            logger = LoggerHelper.getLogger();
        }
    }

    public ColorRibbonFilter(String label, Color ribbonColor, Color labelColor) {
        this(label, ribbonColor, labelColor, LayoutPosition.Default);
    }

    public ColorRibbonFilter(String label, Color ribbonColor) {
        this(label, ribbonColor, Color.WHITE, LayoutPosition.Default);
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


//        logger.info("width: " + width);
//        logger.info("height: " + height);
        /*
            NOTE: different sizes:

            108 x 108
            162 x 162
            432 x 432
            324 x 324
            216 x 216
            48 x 48
            72 x 72
            192 x 192
            144 x 144
         */

        Graphics2D g = (Graphics2D) image.getGraphics();

        int transformAmount = 0;

        switch (position) {
            case TopHorizontal:
            case BottomHorizontal:
                transformAmount = 0;
                break;
            case Default:
                transformAmount = -45;
                break;
        }

        // transform
        g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(transformAmount)));

        int y = height / 2;
        int x = 0;

        FontRenderContext frc = new FontRenderContext(g.getTransform(), true, true);
        // calculate the rectangle where the label is rendered
        int maxLabelWidth = calculateMaxLabelWidth(y);
        g.setFont(getFont(height, maxLabelWidth, frc));

        Rectangle2D labelBounds = g.getFont().getStringBounds(label == null ? "" : label, frc);
        int labelHeight = (int) labelBounds.getHeight();

        int padding = largeRibbon ? (labelHeight / 8) : (labelHeight / 10);

        // update y position after calculating font size
        switch (position) {
            case TopHorizontal:
                y = labelHeight;
                break;
            case BottomHorizontal:
                int bannerHeight = labelHeight + (padding * 2);
                y = height - bannerHeight - labelHeight;
                break;
            case Default:
                y = height / (largeRibbon ? 2 : 4);
                break;
        }


        // draw the ribbon
        g.setColor(ribbonColor);

        if (position == LayoutPosition.TopHorizontal || position == LayoutPosition.BottomHorizontal) {
            g.fillRect(0, y, width, (labelHeight + (padding * 2)));
        } else {
            g.fillRect(-width, y, width * 2, labelHeight);
        }


        if (label != null) {
            // draw the label
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(labelColor);

            FontMetrics fm = g.getFontMetrics();

            if (position == LayoutPosition.TopHorizontal || position == LayoutPosition.BottomHorizontal) {
                drawString(g, label,
                        (width / 2) - ((int) labelBounds.getWidth() / 2),
                        y + fm.getAscent() + padding);
            } else {
                drawString(g, label,
                        (int) -labelBounds.getWidth() / 2,
                        y + fm.getAscent());
            }

        }
        g.dispose();
    }

    private Font getFont(int iconHeight, int maxLabelWidth, FontRenderContext frc) {
//        int max = largeRibbon ? 64 : 32;
//        int max = largeRibbon ? 45 : 32;
        int max = iconHeight / 8;
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
