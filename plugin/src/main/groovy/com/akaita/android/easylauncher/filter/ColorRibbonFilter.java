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

    private static final boolean debug = Boolean.parseBoolean(System.getenv("EASYLAUNCHER_DEBUG"));

    private final Color ribbonColor;

    private final Color labelColor;

    private final LayoutPosition position;

    private String label;

    private String fontName = "DEFAULT";

    private int fontStyle = Font.PLAIN;

    private boolean largeRibbon = false;

    public enum LayoutPosition {
        TOP,
        BOTTOM,
        TOPLEFT,
        TOPRIGHT
    }

    public ColorRibbonFilter(String label, Color ribbonColor, Color labelColor, LayoutPosition position) {
        this.label = label;
        this.ribbonColor = ribbonColor;
        this.labelColor = labelColor;
        this.position = position;
    }

    public ColorRibbonFilter(String label, Color ribbonColor, Color labelColor) {
        this(label, ribbonColor, labelColor, LayoutPosition.TOPLEFT);
    }

    public ColorRibbonFilter(String label, Color ribbonColor) {
        this(label, ribbonColor, Color.WHITE, LayoutPosition.TOPLEFT);
    }

    private static int calculateMaxLabelWidth(int y) {
        return (int) Math.sqrt(Math.pow(y, 2) * 2);
    }

    @Override
    public void setAdaptiveLauncherMode(boolean enable) {
        largeRibbon = enable;
    }

    @Override
    public void apply(BufferedImage image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        Graphics2D g = (Graphics2D) image.getGraphics();

        // transform
        switch (position) {
            case TOP:
            case BOTTOM:
                break;
            case TOPRIGHT:
                g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(45), imageWidth, 0));
                break;
            case TOPLEFT:
            default:
                g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(-45)));
                break;
        }

        FontRenderContext frc = new FontRenderContext(g.getTransform(), true, true);
        // calculate the rectangle where the label is rendered
        int maxLabelWidth = calculateMaxLabelWidth(imageHeight/2);
        g.setFont(getFont(imageHeight, maxLabelWidth, frc));

        Rectangle2D textBounds = g.getFont().getStringBounds(label == null ? "" : label, frc);
        int textHeight = (int) textBounds.getHeight();
        int textPadding = textHeight / 10;
        int labelHeight = textHeight + textPadding*2;

        // update y position after calculating font size
        int y;
        switch (position) {
            case TOP:
                y = largeRibbon ? imageHeight/4 : 0;
                break;
            case BOTTOM:
                y = imageHeight - labelHeight - (largeRibbon ? imageHeight/4 : 0);
                break;
            case TOPRIGHT:
            case TOPLEFT:
            default:
                y = imageHeight / (largeRibbon ? 2 : 4);
                break;
        }

        // draw the ribbon
        g.setColor(ribbonColor);

        if (position == LayoutPosition.TOP || position == LayoutPosition.BOTTOM) {
            g.fillRect(0, y, imageWidth, labelHeight);
        } else if (position == LayoutPosition.TOPRIGHT) {
            g.fillRect(0, y, imageWidth * 2, labelHeight);
        } else {
            g.fillRect(-imageWidth, y, imageWidth * 2, labelHeight);
        }


        if (label != null) {
            // draw the label
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(labelColor);

            FontMetrics fm = g.getFontMetrics();

            if (position == LayoutPosition.TOP || position == LayoutPosition.BOTTOM) {
                g.drawString(label,
                        (imageWidth / 2) - ((int) textBounds.getWidth() / 2),
                        y + fm.getAscent());
            } else if (position == LayoutPosition.TOPRIGHT) {
                g.drawString(label,
                        imageWidth - ((int) textBounds.getWidth() / 2),
                        y + fm.getAscent());
            } else {
                g.drawString(label,
                        (int) -textBounds.getWidth() / 2,
                        y + fm.getAscent());
            }

        }
        g.dispose();
    }

    private Font getFont(int iconHeight, int maxLabelWidth, FontRenderContext frc) {
        int max = iconHeight / 8;
        int min = 0;
        if (label == null) {
            return new Font(fontName, fontStyle, max / 2);
        }

        int size = max;
        for (int i = 0; i < 10; i++) {
            int mid = ((max + min) / 2);
            if (mid == size) {
                break;
            }

            Font font = new Font(fontName, fontStyle, mid);
            Rectangle2D labelBounds = font.getStringBounds(label, frc);
            if ((int) labelBounds.getWidth() > maxLabelWidth) {
                max = mid;
            } else {
                min = mid;
            }

            size = mid;
        }
        return new Font(fontName, fontStyle, size);
    }
}
