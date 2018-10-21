package com.akaita.android.easylauncher.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerHelper {

    private static Logger instance;

    public static Logger getInstance(){
        if(instance == null){
            instance = getLogger();
        }
        return instance;
    }

    public static Logger getLogger(){
        Logger logger = Logger.getLogger("ELLog");
        FileHandler fh;

        try {

            // This block configure the logger with handler and formatter
            fh = new FileHandler("/Users/scottterry/git/easylauncher-gradle-plugin/debuglogs/debug.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages
            logger.info("Logger initialized");

        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }

        return logger;
    }
}
