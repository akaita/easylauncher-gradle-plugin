package com.akaita.android.easylauncher.filter;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class OverlayFilter implements EasyLauncherFilter, Serializable {

    private String fgFileName;

    private boolean addPadding = false;

    public OverlayFilter(final File fgFile) {
        this.fgFileName = fgFile.getAbsolutePath();
    }

    @Override
    public void setAdaptiveLauncherMode(boolean enable) {
        addPadding = enable;
    }

    @Override
    public void apply(BufferedImage image) {
        Image fgImage = null;
        try {
            File fgFile = new File(fgFileName);
            fgImage = ImageIO.read(fgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fgImage != null) {
            int width = image.getWidth();
            int height = image.getWidth();
            float scale = Math.min(width / (float) fgImage.getWidth(null), height / (float) fgImage.getHeight(null));
            if (addPadding)
                scale = scale * (72f / 108);
            Image fgImageScaled = fgImage.getScaledInstance(
                    (int) (fgImage.getWidth(null) * scale),
                    (int) (fgImage.getWidth(null) * scale),
                    Image.SCALE_SMOOTH);

            Graphics2D g = image.createGraphics();

            //TODO allow to choose the gravity for the overlay
            //TODO allow to choose the scaling type
            if (addPadding)
                g.drawImage(fgImageScaled, (int) (width * (1 - 72f / 108) / 2), (int) (height * (1 - 72f / 108) / 2), null);
            else
                g.drawImage(fgImageScaled, 0, 0, null);
            g.dispose();
        }
    }
}