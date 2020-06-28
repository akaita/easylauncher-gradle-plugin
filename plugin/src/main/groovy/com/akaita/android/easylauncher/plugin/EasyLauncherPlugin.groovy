package com.akaita.android.easylauncher.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.akaita.android.easylauncher.filter.EasyLauncherFilter
import com.google.common.collect.Lists
import groovy.transform.CompileStatic
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

// see http://www.gradle.org/docs/current/userguide/custom_plugins.html

@CompileStatic
class EasyLauncherPlugin implements Plugin<Project> {

    static {
        System.setProperty("java.awt.headless", "true")

        // workaround for an Android Studio issue
        try {
            Class.forName(System.getProperty("java.awt.graphicsenv"))
        } catch (ClassNotFoundException e) {
            System.err.println("[WARN] java.awt.graphicsenv: " + e)
            System.setProperty("java.awt.graphicsenv", "sun.awt.CGraphicsEnvironment")
        }
        try {
            Class.forName(System.getProperty("awt.toolkit"))
        } catch (ClassNotFoundException e) {
            System.err.println("[WARN] awt.toolkit: " + e)
            System.setProperty("awt.toolkit", "sun.lwawt.macosx.LWCToolkit")
        }
    }

    @Override
    void apply(Project project) {
        project.extensions.add(EasyLauncherExtension.NAME, EasyLauncherExtension)

        NamedDomainObjectContainer<EasyLauncherConfig> ribbonVariants =
                project.container(EasyLauncherConfig)
        project.extensions.add('variants', ribbonVariants)
        NamedDomainObjectContainer<EasyLauncherConfig> ribbonBuildTypes =
                project.container(EasyLauncherConfig)
        project.extensions.add('buildTypes', ribbonBuildTypes)
        NamedDomainObjectContainer<EasyLauncherConfig> ribbonProductFlavors =
                project.container(EasyLauncherConfig)
        project.extensions.add('productFlavors', ribbonProductFlavors)

        project.afterEvaluate {
            def android = project.extensions.findByType(AppExtension)
            if (!android) {
                throw new Exception(
                        "Not an Android application; did you forget `apply plugin: 'com.android.application`?")
            }
            def extension = project.extensions.findByType(EasyLauncherExtension)


            def tasks = new ArrayList<Task>()

            android.applicationVariants.all { ApplicationVariant variant ->

                List<EasyLauncherConfig> configs = Lists.newArrayList()
                ribbonVariants.each{
                    if (variant.name == it.name) {
                        configs.add(it)
                    }
                }

                if (configs.empty) {
                    ribbonProductFlavors.each {
                        if (variant.flavorName == it.name) {
                            configs.add(it)
                        }
                    }
                    ribbonBuildTypes.each {
                        if (variant.buildType.name == it.name) {
                            configs.add(it)
                        }
                    }
                }

                def enabled = true
                configs.each {
                    enabled = enabled && it.getEnabled()
                }

                if (enabled) {
                    List<EasyLauncherFilter> filters = Lists.newArrayList()
                    configs.each {
                        filters.addAll(it.getFilters())
                    }

                    //set default ribbon
                    if (filters.empty && variant.buildType.debuggable) {
                        def ribbonText = extension.defaultFlavorNaming ? variant.flavorName : variant.buildType.name
                        filters.add(new EasyLauncherConfig(ribbonText).greenRibbonFilter())
                    }

                    def generatedResDir = getGeneratedResDir(project, variant)
                    android.sourceSets.findByName(variant.name).res.srcDir(generatedResDir)

                    def name = "${EasyLauncherTask.NAME}${capitalize(variant.name)}"
                    def task = project.task(name, type: EasyLauncherTask) as EasyLauncherTask
                    task.variantName = variant.name
                    task.outputDir = generatedResDir
                    task.iconNames = new HashSet<String>(extension.iconNames)
                    task.foregroundIconNames = new HashSet<String>(extension.foregroundIconNames)
                    task.filters = filters
                    tasks.add(task)

                    def generateResources = project.
                            getTasksByName("generate${capitalize(variant.name)}Resources", false)
                    generateResources.forEach { Task t ->
                        t.dependsOn(task)
                    }
                }
            }

            project.task(EasyLauncherTask.NAME, dependsOn: tasks)
        }
    }

    static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1)
    }

    static File getGeneratedResDir(Project project, ApplicationVariant variant) {
        return new File(project.buildDir,
                "generated/easylauncher/res/${variant.name}")
    }

}
