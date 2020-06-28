package com.akaita.android.easylauncher.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.builder.model.SourceProvider
import com.akaita.android.easylauncher.filter.EasyLauncherFilter
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import java.util.function.Function
import java.util.stream.Stream

class EasyLauncherTask extends DefaultTask {

    static final String NAME = "easylauncher"

    @Input
    String variantName

    @OutputDirectory
    File outputDir

    // `iconNames` includes: "@drawable/icon", "@mipmap/ic_launcher", etc.
    @Input
    Set<String> iconNames
    @Input
    Set<String> foregroundIconNames

    @Input
    List<EasyLauncherFilter> filters = []

    @TaskAction
    void run() {
        if (filters.empty) {
            return
        }

        def t0 = System.currentTimeMillis()

        def android = project.extensions.findByType(AppExtension)
        ApplicationVariant variant = android.applicationVariants.find { it.name == variantName }

        def names = new HashSet<String>(iconNames)
        names.addAll(getLauncherIconNames(variant))

        def foregroundNames = new HashSet<String>(foregroundIconNames)

        variant.sourceSets
                .stream()
                .flatMap(new Function<SourceProvider, Stream>() {
                    @Override
                    Stream apply(SourceProvider sourceProvider) {
                        return sourceProvider.resDirectories.stream()
                    }
                })
                .forEach { File resDir ->
                    if (resDir == outputDir) {
                        return
                    }

                    names.forEach { String name ->
                        project.fileTree(
                                dir: resDir,
                                include: Resources.resourceFilePattern(name),
                                exclude: "**/*.xml",
                        ).forEach { File inputFile ->
                            info "process $inputFile"

                            def basename = inputFile.name
                            def resType = inputFile.parentFile.name
                            def outputFile = new File(outputDir, "${resType}/${basename}")
                            outputFile.parentFile.mkdirs()

                            def easyLauncher = new EasyLauncher(inputFile, outputFile)
                            easyLauncher.process(filters.stream())
                            easyLauncher.save()
                        }
                    }
                    foregroundNames.forEach { String name ->
                        project.fileTree(
                                dir: resDir,
                                include: Resources.resourceFilePattern(name),
                                exclude: "**/*.xml",
                        ).forEach { File inputFile ->
                            info "process $inputFile"

                            def basename = inputFile.name
                            def resType = inputFile.parentFile.name
                            def outputFile = new File(outputDir, "${resType}/${basename}")
                            outputFile.parentFile.mkdirs()

                            def largeRibbonFilters = filters.collect {
                                it.setAdaptiveLauncherMode(true)
                                it
                            }

                            def easyLauncher = new EasyLauncher(inputFile, outputFile)
                            easyLauncher.process(largeRibbonFilters.stream())
                            easyLauncher.save()
                        }
                    }
                }

        info("task finished in ${System.currentTimeMillis() - t0}ms")
    }

    void info(String message) {
        project.logger.info("[$name] $message")
    }

    Set<String> getLauncherIconNames(ApplicationVariant variant) {
        def names = new HashSet<String>()
        getAndroidManifestFiles(variant).forEach { File manifestFile ->
            names.addAll(Resources.getLauncherIcons(manifestFile))
        }
        return names
    }

    private List<File> getAndroidManifestFiles(ApplicationVariant variant) {
        AppExtension android = project.extensions.findByType(AppExtension)

        return ["main", variant.name, variant.buildType.name, variant.flavorName].stream()
                .filter({ name -> !name.empty })
                .distinct()
                .map({ name -> project.file(android.sourceSets[name].manifest.srcFile) })
                .filter({ manifestFile -> manifestFile.exists() })
                .toArray()
                .toList() as List<File>
    }
}