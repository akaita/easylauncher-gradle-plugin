package com.akaita.android.easylauncher.plugin;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import com.akaita.android.easylauncher.filter.EasyLauncherFilter;

import javax.imageio.ImageIO;

public class EasyLauncher {

    private final File inputFile;

    private final File outputFile;

    private final BufferedImage image;

    public EasyLauncher(File inputFile, File outputFile) throws IOException {
        this.inputFile = inputFile;
        this.outputFile = outputFile;

        BufferedImage readImage = ImageIO.read(inputFile);
        image = new BufferedImage(readImage.getWidth(), readImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(readImage, 0, 0, null);
        g2d.dispose();
    }

    public void save() throws IOException {
        outputFile.getParentFile().mkdirs();
        ImageIO.write(image, "png", outputFile);
    }

    public void process(Stream<EasyLauncherFilter> filters) {
        filters.forEach(filter -> filter.apply(image));
    }
}