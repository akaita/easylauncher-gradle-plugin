package com.akaita.android.easylauncher.filter;

import java.awt.image.BufferedImage;

/**
 * Created by mikel on 05/01/2018.
 */

public interface EasyLauncherFilter {
    void setAdaptiveLauncherMode(boolean enable);
    void apply(BufferedImage image);
}
