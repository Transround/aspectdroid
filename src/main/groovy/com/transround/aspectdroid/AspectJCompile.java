
package com.transround.aspectdroid;

import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.compile.AbstractCompile;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageHandler;
import org.aspectj.tools.ajc.Main;

public class AspectJCompile extends AbstractCompile {
    private Main ajc;
    
    String sourceRoots;
    String bootClassPath;

    public AspectJCompile() {
        super();
    }
    
    @TaskAction
    protected void compile() {
        Logger logger = getProject().getLogger();

        String[] args = {
                "-showWeaveInfo",
                "-encoding", "UTF-8",
                "-" + getSourceCompatibility(),
                "-inpath", getDestinationDir().getAbsolutePath(),
                "-aspectpath", getClasspath().getAsPath(),
                "-d", getDestinationDir().getAbsolutePath(),
                "-classpath", getClasspath().getAsPath(),
                "-bootclasspath", getBootClassPath(),
                "-sourceroots", getSourceRoots()                
        };
        
        logger.debug("aspect compile arguments: ");
        for (String arg : args) {
            logger.debug(arg);
        }

        MessageHandler handler = new MessageHandler(true);
        logger.info("Starting aspectj compiler...");
        ajc = new Main();
        ajc.run(args, handler);
        for (IMessage message : handler.getMessages(null, true)) {            
            if (message.getKind().equals(IMessage.ABORT)
                    || message.getKind().equals(IMessage.ERROR)
                    || message.getKind().equals(IMessage.FAIL)) {
                logger.error(message.getMessage());    
                throw new GradleException(message.getMessage(), message.getThrown());
            } else if (message.getKind().equals(IMessage.WARNING)) {
                logger.warn(message.getMessage());
            } else if (message.getKind().equals(IMessage.INFO)) {
                logger.info(message.getMessage());
            } else if (message.getKind().equals(IMessage.DEBUG)) {
                logger.debug(message.getMessage());
            }
        }
    }

    public String getSourceRoots() {
        return sourceRoots;
    }

    public void setSourceRoots(String sourceRoots) {
        this.sourceRoots = sourceRoots;
    }

    public String getBootClassPath() {
        return bootClassPath;
    }

    public void setBootClassPath(String bootClassPath) {
        this.bootClassPath = bootClassPath;
    }    
}
