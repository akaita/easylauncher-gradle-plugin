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
    public ColorRibbonFilter customColorRibbonFilter(String nm) {
        return new ColorRibbonFilter(name, Color.decode(nm));
    }

    public ColorRibbonFilter grayRibbonFilter() {
        return new ColorRibbonFilter(name, new Color(0x60, 0x60, 0x60, 0x99));
    }

    public ColorRibbonFilter greenRibbonFilter() {
        return new ColorRibbonFilter(name, new Color(0, 0x72, 0, 0x99));
    }

    public ColorRibbonFilter orangeRibbonFilter() {
        return new ColorRibbonFilter(name, new Color(0xff, 0x76, 0, 0x99));
    }

    public ColorRibbonFilter yellowRibbonFilter() {
        return new ColorRibbonFilter(name, new Color(0xff, 251, 0, 0x99));
    }

    public ColorRibbonFilter redRibbonFilter() {
        return new ColorRibbonFilter(name, new Color(0xff, 0, 0, 0x99));
    }

    public ColorRibbonFilter blueRibbonFilter() {
        return new ColorRibbonFilter(name, new Color(0, 0, 255, 0x99));
    }

    public OverlayFilter overlayFilter(File fgFile) {
        return new OverlayFilter(fgFile);
    }

    public GrayscaleFilter grayscaleFilter() {
        return new GrayscaleFilter();
    }

    //endregion
}
