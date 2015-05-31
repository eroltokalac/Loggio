package org.tokalac.logging.modules.output.handler;

/**
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */

import java.util.Properties;

import org.tokalac.logging.core.Level;
import org.tokalac.logging.core.LogManager;
import org.tokalac.logging.modules.config.Key;
import org.tokalac.logging.modules.output.formatter.Formatter;

public class FileHandler extends AbstractHandler {

    private String logFilePath;
    private Integer logFileLimitSize;
    private String loggerAlias;
    private Properties settings = LogManager.config.getSettings();

    public FileHandler() {
        this.setLogFilePath(null);
        this.setLogFileLimitSize(null);
    }

    public FileHandler(String logFilePath, Integer logFileLimitSize) {
        this.setLogFilePath(logFilePath);
        this.setLogFileLimitSize(logFileLimitSize);
    }

    private Printer printer = new Printer();

    @Override
    public void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter) {
        this.setLoggerAlias(fqcn);
        if(handler.equals(FileHandler.class.getName())) {
            printer.write(appName, level, message, fqcn, FileHandler.class.getName(), formatter, this.logFilePath, this.logFileLimitSize);
        }
    }

    @Override
    public void log(String appName, Level level, String userId, String sessionId, String cartId, String productId, String price, String qty, String onSale, String process, String categoryId, String message, String fqcn, String handler, Formatter formatter) {
        this.log(appName, level, message, fqcn, handler, formatter);
    }

    @Override
    public void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter,
                    Level levelFixed) {
        this.setLoggerAlias(fqcn);
        if (level == levelFixed) {
            if(handler.equals(FileHandler.class.getName())) {
                printer.write(appName, levelFixed, message, fqcn, FileHandler.class.getName(), formatter, this.logFilePath, this.logFileLimitSize);
            }
        }
    }

    @Override
    public void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter,
                    boolean forceLogging) {
        this.setLoggerAlias(fqcn);
        if (forceLogging) {
            if(this.getClass().getName().equals(handler)) {
                printer.write(appName, level, message, fqcn, FileHandler.class.getName(), formatter, this.logFilePath, this.logFileLimitSize);
            }
        }
    }


    public void setLogFilePath(String logFilePath) {
        String key = loggerAlias + ".LogFilePath";

        if(settings.containsKey(key)) {
            this.logFilePath = (String) settings.get(key);
        }

        if (logFilePath != null) {
            this.logFilePath = logFilePath;
        }

        if (this.logFilePath == null || this.logFilePath.isEmpty()) {
            this.logFilePath = (String) settings.get(Key.LogFilePath.name());
        }
    }


    public void setLogFileLimitSize(Integer logFileLimitSize) {
        String key = loggerAlias + ".LogFileSize";

        if(settings.containsKey(key)) {
            this.logFileLimitSize = this.initLogFileLimitSize((String) settings.get(key));
        }

        if (logFileLimitSize != null) {
            this.logFileLimitSize = this.initLogFileLimitSize(String.valueOf(logFileLimitSize));
        }

        if (this.logFileLimitSize == null) {
            this.logFileLimitSize = this.initLogFileLimitSize((String) settings.get(Key.LogFileSize.name()));
        }
    }

    private Integer initLogFileLimitSize(String limit) {
        String fileSize = limit;
        Integer limitSize = 0;
        try {
            limitSize = Integer.parseInt(fileSize.trim());
            limitSize *= 1000;
        }
        catch (NumberFormatException e) {
            System.out.print("Your log file size is not readable, please check it => ");
            e.printStackTrace();
        }
        return limitSize;
    }

    private void setLoggerAlias(String fqcn) {
        for(String key : settings.stringPropertyNames()) {
            String value = settings.getProperty(key);
            if (value.equalsIgnoreCase(fqcn)) {
                this.loggerAlias = key.split("\\.")[0];
            }
        }
        this.setLogFilePath(null);
        this.setLogFileLimitSize(null);
    }
}