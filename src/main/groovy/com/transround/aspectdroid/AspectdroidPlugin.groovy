package com.transround.aspectdroid


import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

class AspectdroidPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        final def variants
        final def plugin
        if (project.plugins.hasPlugin(AppPlugin)) {
            variants = project.android.applicationVariants
            plugin = project.plugins.getPlugin(AppPlugin)
        } else if (project.plugins.hasPlugin(LibraryPlugin)) {
            variants = project.android.libraryVariants
            plugin = project.plugins.getPlugin(LibraryPlugin)
        } else {
            throw new GradleException("The 'android' or 'android-library' plugin is required.")
        }

        if (!project.hasProperty('aspectjVersion')) {
            throw new GradleException("You must set the property 'aspectjVersion' before applying the aspectj plugin")
        }

        project.repositories { mavenCentral() }
        project.dependencies {
            compile "org.aspectj:aspectjrt:${project.aspectjVersion}"
        }

        project.afterEvaluate {
            def bootClasspath
            if (plugin.properties['runtimeJarList']) {
                bootClasspath = plugin.runtimeJarList
            } else {
                bootClasspath = plugin.bootClasspath
            }

            variants.all { variant ->
                def variantName = variant.name.capitalize()
                def taskName = "compile${variantName}AspectJ"
                JavaCompile javaCompile = variant.javaCompile

                def ajc = project.tasks.create(taskName, AspectJCompile)
                ajc.bootClassPath = bootClasspath.join(File.pathSeparator)
                ajc.sourceCompatibility = javaCompile.sourceCompatibility
                ajc.classpath = javaCompile.classpath
                ajc.destinationDir = javaCompile.destinationDir
                ajc.source = javaCompile.source

                ajc.doFirst {
                    def sourceRoots = []
                    javaCompile.source.sourceCollections.each {
                        it.asFileTrees.each { sourceRoots << it.dir }
                    }
                    ajc.sourceRoots = sourceRoots.join(File.pathSeparator)
                    ajc.source = javaCompile.source
                    ajc.classpath = javaCompile.classpath
                }

                ajc.setDependsOn(project.tasks."compile${variantName}Java".dependsOn)

                project.tasks."compile${variantName}Java".deleteAllActions()
                project.tasks."compile${variantName}Java".dependsOn ajc

                def packageJarTask = project.tasks.findByName("package${variantName}Jar")
                if( packageJarTask != null ){
                    project.logger.info 'packageJarTask is exist'
                    project.tasks."package${variantName}Jar".exclude("**/*.aj")    
                }                

            }
        }
    }
}
