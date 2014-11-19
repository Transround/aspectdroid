Aspectdroid
==========

A Gradle plugin for Android build system. 
It modifies the default compile*Java tasks to use aspectj's compiler instead of default JavaCompiler.

Usage
-----
* Build aspectdroid project

* Copy aspectroid.jar to {project}/gradle-libs/aspectroid-{version}.jar.

* Add the below lines to the buildscript's section: 
```groovy
buildscript {

    ext {
        aspectjVersion = '1.8.2'
    }

    repositories {
        mavenCentral()
    }

    dependencies {        
        classpath 'com.android.tools.build:gradle:0.13.+'
        classpath "org.aspectj:aspectjtools:${aspectjVersion}"
        classpath files('gradle-libs/aspectdroid.jar')       
    }
}
```

* Appy the aspectdroid plugin:
```groovy
apply plugin: 'aspectdroid'
```