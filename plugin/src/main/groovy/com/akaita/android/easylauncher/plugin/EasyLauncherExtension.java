package com.akaita.android.easylauncher.plugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class EasyLauncherExtension {

    public static String NAME = "easylauncher";

    Set<String> iconNames = new HashSet<>();
    Set<String> foregroundIconNames = new HashSet<>();
    boolean defaultFlavorNaming = false;

    public EasyLauncherExtension() {
    }

    public Set<String> getIconNames() {
        return iconNames;
    }

    /**
     * @param resNames Names of icons. For example "@drawable/ic_launcher", "@mipmap/icon"
     */
    public void setIconNames(Collection<String> resNames) {
        iconNames = new HashSet<>(resNames);
    }

    /**
     * @param resNames Names of icons. For example "@drawable/ic_launcher", "@mipmap/icon"
     */
    public void iconNames(Collection<String> resNames) {
        setIconNames(resNames);
    }

    /**
     * @param resNames Names of icons. For example "@drawable/ic_launcher", "@mipmap/icon"
     */
    public void iconNames(String... resNames) {
        setIconNames(Arrays.asList(resNames));
    }

    /**
     * @param resName A name of icons. For example "@drawable/ic_launcher", "@mipmap/icon"
     */
    public void iconName(String resName) {
        iconNames.add(resName);
    }

    public Set<String> getForegroundIconNames() {
        return foregroundIconNames;
    }

    /**
     * @param resNames Names of icons. For example "@drawable/ic_launcher", "@mipmap/icon"
     */
    public void setForegroundIconNames(Collection<String> resNames) {
        foregroundIconNames = new HashSet<>(resNames);
    }

    /**
     * @param resNames Names of icons. For example "@drawable/ic_launcher", "@mipmap/icon"
     */
    public void foregroundIconNames(Collection<String> resNames) {
        setForegroundIconNames(resNames);
    }

    /**
     * @param resNames Names of icons. For example "@drawable/ic_launcher", "@mipmap/icon"
     */
    public void foregroundIconNames(String... resNames) {
        setForegroundIconNames(Arrays.asList(resNames));
    }

    /**
     * @param resName A name of icons. For example "@drawable/ic_launcher", "@mipmap/icon"
     */
    public void foregroundIconName(String resName) {
        foregroundIconNames.add(resName);
    }

    /**
     * True to use flavor name for default ribbons, false to use type name
     */
    public boolean isDefaultFlavorNaming() {
        return defaultFlavorNaming;
    }

    /**
     * @param defaultFlavorNaming true to use flavor name for default ribbons, false to use type name
     */
    public void setDefaultFlavorNaming(boolean defaultFlavorNaming) {
        this.defaultFlavorNaming = defaultFlavorNaming;
    }

    /**
     * @param defaultFlavorNaming true to use flavor name for default ribbons, false to use type name
     */
    public void defaultFlavorNaming(boolean defaultFlavorNaming) {
        this.defaultFlavorNaming = defaultFlavorNaming;
    }
}
