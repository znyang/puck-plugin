package com.zen.android.puck

import com.android.build.gradle.api.BaseVariant
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project

import static com.zen.android.puck.Version.GROUP
import static com.zen.android.puck.Version.VERSION

/**
 * PuckPlugin
 *
 * @author znyang 2017/2/3 0003
 */

public class PuckPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        // add plugin & library
        if (!project.pluginManager.hasPlugin("jacoco")) {
            project.pluginManager.apply('jacoco')
        }
        project.dependencies {
            testCompile "$GROUP:puck:$VERSION"
        }

        project.afterEvaluate {
            if (project.plugins.hasPlugin('com.android.application')) {
                setupTasks(project, (DomainObjectCollection<BaseVariant>) project.android.applicationVariants);
            } else if (project.plugins.hasPlugin('com.android.library')) {
                setupTasks(project, (DomainObjectCollection<BaseVariant>) project.android.libraryVariants);
            } else {
                // throw new IllegalArgumentException('Puck plugin requires the Android plugin to be configured');
            }
        }
    }

    static void setupTasks(Project project, def variants) {


        def sonar = project.tasks.findByName("sonarqube")
        def sonarRunner = project.tasks.findByName("sonarRunner")

        variants.all { variant ->
            def slug = variant.name.capitalize()
            if (variant.outputs.size() > 1) {
                slug += output.name.capitalize()
            }

            def copyAssets = project.tasks.create("copy${slug}AssetsFile", CopyAssetsTask)
            copyAssets.group = 'verification'
            copyAssets.variantName = variant.name
            copyAssets.dependsOn(variant.assemble)
            copyAssets.mustRunAfter(variant.assemble)

            def testUnitTask = project.tasks.findByName("test${slug}UnitTest")
            testUnitTask?.dependsOn(copyAssets)

            def jacoco = project.tasks.create("jacocoReprot${slug}", JacocoReportTask)
            jacoco.group = 'reporting'
            jacoco.variantName = variant.name
            jacoco.dependsOn(testUnitTask)
            jacoco.mustRunAfter(testUnitTask)

            sonar?.dependsOn(jacoco)
            sonarRunner?.dependsOn(jacoco)
        }
    }
}
