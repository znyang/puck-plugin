package com.zen.android.puck

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.util.regex.Pattern;

/**
 * CopyAssetsTask
 *
 * @author znyang 2017/2/3 0003
 */

public class CopyAssetsTask extends DefaultTask {

    final FILE_SEP = File.separator.equals('\\') ? "\\\\" : File.separator
    final COMPILE_KEY = "intermediates/exploded-aar/[^/]+/[^/]+/[^/]+/assets".replace('/', FILE_SEP)


    def variantName

    @TaskAction
    void executeCopy() {
        def assetsPath = "${project.buildDir}/intermediates/bundles/${variantName}/assets".replace('/', FILE_SEP)
        def assetsDir = new File(assetsPath)

        long startTime = System.currentTimeMillis()
        listAssets(Pattern.compile(COMPILE_KEY), new File("${project.buildDir}/intermediates/exploded-aar"))
                .findAll {
            FileUtils.copyDirectory(it, assetsDir)
        }
        println "DEBUG: copy assets file cost ${System.currentTimeMillis() - startTime}(ms)"
    }

    List<File> listAssets(Pattern pattern, File root) {
        List<File> files = new ArrayList<>()
        root.listFiles(new FileFilter() {
            @Override
            boolean accept(File file) {
                return file.exists() && file.isDirectory()
            }
        }).findAll {
            if (pattern.matcher(it.path).find()) {
                files.add(it)
            } else {
                files.addAll(listAssets(pattern, it))
            }
        }
        files
    }

}
