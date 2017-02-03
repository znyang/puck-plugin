package com.zen.android.puck

import org.apache.commons.io.FileUtils;
import org.gradle.testing.jacoco.tasks.JacocoReport;

/**
 * JacocoReportTask
 *
 * @author znyang 2017/2/3 0003
 */

public class JacocoReportTask extends JacocoReport {

    def variantName = "debug"
    def FILE_SEP = File.separator

    JacocoReportTask() {
        super()

        reports {
            xml.enabled = true
            html.enabled = true
        }
        // "gradle dependencies" - shows configurations
        // "jacocoAgent/jacocoAnt" and "androidJacocoAgent/androidJacocoAnt"
        // The Android version is a newer version
        // Use hidden configuration, for details look into JacocoPlugin.groovy
        jacocoClasspath = project.configurations['androidJacocoAnt']

        // exclude auto-generated classes and tests
        def fileFilter = ['**/R.class', '**/R$*.class', '**/BuildConfig.*', '**/Manifest*.*', '**/*Test*.*', 'android/**/*.*']
        def sourceTree = project.fileTree(dir: "${project.buildDir}/intermediates/classes/${variantName}"
                .replace('/', FILE_SEP), excludes: fileFilter)
        def mainSrc = "${project.projectDir}/src/main/java".replace('/', FILE_SEP)
        def log = new File("${project.buildDir}/reports/${name}_${new Date().toLocaleString().replace(':', '_')}.log")

        sourceDirectories = project.files([mainSrc])
        classDirectories = project.files([sourceTree])
        executionData = project.fileTree(dir: project.projectDir, includes: ['**/*.exec'])

        StringBuilder builder = new StringBuilder()
        builder.append("----sourceDirectories----\r\n")
        sourceDirectories.findAll {
            builder.append(it.path).append("\r\n")
        }
        builder.append("----classDirectories----\r\n")
        classDirectories.findAll {
            builder.append(it.path).append("\r\n")
        }
        builder.append("----executionData----\r\n")
        executionData.findAll {
            builder.append(it.path).append("\r\n")
        }
        FileUtils.write(log, builder.toString())

        // Bit hacky but fixes https://code.google.com/p/android/issues/detail?id=69174.
        // We iterate through the compiled .class tree and rename $$ to $.
        doFirst {
            new File("$project.buildDir/intermediates/classes/${variantName}").eachFileRecurse { file ->
                if (file.name.contains('$$')) {
                    file.renameTo(file.path.replace('$$', '$'))
                }
            }
        }
    }

}
