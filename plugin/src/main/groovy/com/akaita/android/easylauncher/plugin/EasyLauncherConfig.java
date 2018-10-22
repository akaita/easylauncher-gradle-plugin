package com.akaita.android.easylauncher.plugin;

import com.akaita.android.easylauncher.filter.ColorRibbonFilter;
import com.akaita.android.easylauncher.filter.EasyLauncherFilter;
import com.akaita.android.easylauncher.filter.GrayscaleFilter;
import com.akaita.android.easylauncher.filter.OverlayFilter;
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mikel on 02/01/2018.
 */

public class EasyLauncherConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @Nullable
    private Boolean mEnabled = true;
    private List<EasyLauncherFilter> mFilters = Lists.newArrayList();


    public String name;
    public EasyLauncherConfig(final String name) {
        this.name = name;
    }

    /**
     * @see #getEnabled()
     */
    @NonNull
    public EasyLauncherConfig enable(@Nullable Boolean enabled) {
        mEnabled = enabled;
        return this;
    }

    public EasyLauncherConfig setFilters(Iterable<EasyLauncherFilter> filters) {
        filters(Iterables.toArray(filters, EasyLauncherFilter.class));
        return this;
    }

    public EasyLauncherConfig setFilters(EasyLauncherFilter filter) {
        mFilters.add(filter);
        return this;
    }

    private void filters(EasyLauncherFilter... filters) {
        mFilters.addAll(Arrays.asList(filters));
    }

    @Nullable
    public List<EasyLauncherFilter> getFilters() {
        return mFilters;
    }

    @Nullable
    public Boolean getEnabled() {
        return mEnabled;
    }


    //region Filters
    public ColorRibbonFilter customColorRibbonFilter(String name, String ribbonColor, String labelColor, String position, float textSizeRatio) {
        return new ColorRibbonFilter(name, Color.decode(ribbonColor), Color.decode(labelColor), ColorRibbonFilter.Gravity.valueOf(position.toUpperCase()), textSizeRatio);
    }

    public ColorRibbonFilter customColorRibbonFilter(String name, String ribbonColor, String labelColor, String gravity) {
        return new ColorRibbonFilter(name, Color.decode(ribbonColor), Color.decode(labelColor), ColorRibbonFilter.Gravity.valueOf(gravity.toUpperCase()));
    }

    public ColorRibbonFilter customColorRibbonFilter(String name, String ribbonColor, String labelColor) {
        return new ColorRibbonFilter(name, Color.decode(ribbonColor), Color.decode(labelColor));
    }
	
    public ColorRibbonFilter customColorRibbonFilter(String name, String ribbonColor) {
        return new ColorRibbonFilter(name, Color.decode(ribbonColor));
    }

    public ColorRibbonFilter customColorRibbonFilter(String ribbonColor) {
        return new ColorRibbonFilter(this.name, Color.decode(ribbonColor));
    }

    public ColorRibbonFilter grayRibbonFilter(String name) {
        return new ColorRibbonFilter(name, new Color(0x60, 0x60, 0x60, 0x99));
    }

    public ColorRibbonFilter grayRibbonFilter() {
        return new ColorRibbonFilter(this.name, new Color(0x60, 0x60, 0x60, 0x99));
    }

    public ColorRibbonFilter greenRibbonFilter(String name) {
        return new ColorRibbonFilter(name, new Color(0, 0x72, 0, 0x99));
    }

    public ColorRibbonFilter greenRibbonFilter() {
        return new ColorRibbonFilter(this.name, new Color(0, 0x72, 0, 0x99));
    }

    public ColorRibbonFilter orangeRibbonFilter(String name) {
        return new ColorRibbonFilter(name, new Color(0xff, 0x76, 0, 0x99));
    }

    public ColorRibbonFilter orangeRibbonFilter() {
        return new ColorRibbonFilter(this.name, new Color(0xff, 0x76, 0, 0x99));
    }

    public ColorRibbonFilter yellowRibbonFilter(String name) {
        return new ColorRibbonFilter(name, new Color(0xff, 251, 0, 0x99));
    }

    public ColorRibbonFilter yellowRibbonFilter() {
        return new ColorRibbonFilter(this.name, new Color(0xff, 251, 0, 0x99));
    }

    public ColorRibbonFilter redRibbonFilter(String name) {
        return new ColorRibbonFilter(name, new Color(0xff, 0, 0, 0x99));
    }

    public ColorRibbonFilter redRibbonFilter() {
        return new ColorRibbonFilter(this.name, new Color(0xff, 0, 0, 0x99));
    }

    public ColorRibbonFilter blueRibbonFilter(String name) {
        return new ColorRibbonFilter(name, new Color(0, 0, 255, 0x99));
    }

    public ColorRibbonFilter blueRibbonFilter() {
        return new ColorRibbonFilter(this.name, new Color(0, 0, 255, 0x99));
    }

    public OverlayFilter overlayFilter(File fgFile) {
        return new OverlayFilter(fgFile);
    }

    public GrayscaleFilter grayscaleFilter() {
        return new GrayscaleFilter();
    }

    //endregion
}
