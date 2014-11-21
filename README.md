Aspectdroid
==========

A Gradle plugin for Android build system. 
It modifies the default compile*Java tasks to use aspectj's compiler instead of default JavaCompiler.

Requirements
------------
* Gradle 2.1 or later
* com.android.tools.build:gradle:0.13.+
* org.aspectj:aspectjtools:1.8.+


Usage
-----
* Use our maven repository
	+ Add the below lines to the buildscript's section:

		```groovy
		 buildscript {		   
		      ext {
		          aspectjVersion = '1.8.2'
		      }		  
		      repositories {
		          mavenCentral()
		          maven {
		           url 'http://www.transround.com/repositories/public/'
		          }
		      }		   
		      dependencies {        
		          classpath 'com.android.tools.build:gradle:0.13.+'
		          classpath 'com.transround:aspectdroid:1.0.+'
		      }
		  }
		```	
	
	+ Appy the aspectdroid plugin:
		 
		```groovy
			apply plugin: 'aspectdroid'
		```

* Build it yourself
	+ Build aspectdroid project
	
	+ Copy aspectroid.jar to {project}/gradle-libs/aspectroid-{version}.jar.	

	+ Add the below lines to the buildscript's section: 

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
		         classpath files('gradle-libs/aspectdroid-{version}.jar')       
		     }
		}
		```
 
	+ Appy the aspectdroid plugin:
		```groovy
			apply plugin: 'aspectdroid'
		```