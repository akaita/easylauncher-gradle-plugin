package com.akaita.android.easylauncher.plugin;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import groovy.util.XmlSlurper;
import groovy.util.slurpersupport.GPathResult;

import static java.util.Collections.unmodifiableList;

public class Resources {

    public static String resourceFilePattern(String name) {
        if (name.startsWith("@")) {
            String[] pair = name.substring(1).split("/", 2);
            String baseResType = pair[0];
            String fileName = pair[1];
            if (fileName == null) {
                throw new IllegalArgumentException(
                        "Icon names does include resource types (e.g. drawable/ic_launcher): "
                                + name);
            }
            return baseResType + "*/" + fileName + ".*";
        } else {
            return name;
        }
    }

    public static List<String> getLauncherIcons(File manifestFile)
            throws SAXException, ParserConfigurationException, IOException {
        GPathResult manifestXml = new XmlSlurper().parse(manifestFile);
        GPathResult applicationNode = (GPathResult) manifestXml.getProperty("application");

        String icon = String.valueOf(applicationNode.getProperty("@android:icon"));
        String roundIcon = String.valueOf(applicationNode.getProperty("@android:roundIcon"));

        List<String> icons = new ArrayList<>(2);
        if (!icon.isEmpty()) {
            icons.add(icon);
        }
        if (!roundIcon.isEmpty()) {
            icons.add(roundIcon);
        }

        return unmodifiableList(icons);

    }
}
