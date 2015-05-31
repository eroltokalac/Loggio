package org.tokalac.logging.modules.output.handler;

/**
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */

import org.tokalac.logging.core.Level;
import org.tokalac.logging.core.LogManager;
import org.tokalac.logging.modules.config.Key;
import org.tokalac.logging.modules.output.formatter.Formatter;

import java.util.Properties;

public class REHandler extends AbstractHandler {

    private String reDBAddress;
    private Integer rePort;
    private String reDBPath;
    private String loggerAlias;
    private Properties settings = LogManager.config.getSettings();

    private Printer printer = new Printer();

    public REHandler() {
        this.setREDBAddress(null);
        this.setREDBPath(null);
        this.setREPort(null);
    }

    public REHandler(String address, Integer port, String path) {
        this.setREDBAddress(address);
        this.setREDBPath(path);
        this.setREPort(port);
    }

    @Override
    public void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter) {
        this.setLoggerAlias(fqcn);
        if(this.getClass().getName().equals(handler)) {

            //Send to MongoDB
            //printer.write(level, message, fqcn, FileHandler.class.getName(), formatter, this.reDBAddress,this.rePort, this.reDBPath);
            this.log(appName, level, null, null, null, null, null, null, null, null, null, message, fqcn, handler, formatter);
        }
    }

    @Override
    public void log(String appName, Level level, String userId, String sessionId, String cartId, String productId, String price, String qty, String onSale, String process, String categoryId, String message, String fqcn, String handler, Formatter formatter) {
        this.setLoggerAlias(fqcn);
        if(this.getClass().getName().equals(handler)) {

            //Send to MongoDB
            printer.write(appName, level, message, process, userId, sessionId, cartId, productId, price, qty, onSale, categoryId, fqcn, FileHandler.class.getName(), formatter, this.reDBAddress,this.rePort, this.reDBPath);
        }
    }

    @Override
    public void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter,
                    Level levelFixed) {
        this.setLoggerAlias(fqcn);
        if(level == levelFixed){
            if(this.getClass().getName().equals(handler)) {

                //Send to MongoDB
                //printer.write(levelFixed, message,  fqcn, FileHandler.class.getName(), formatter, this.reDBAddress, this.rePort, this.reDBPath);
                this.log(appName, level, null, null, null, null, null, null, null, null, null, message, fqcn, handler, formatter);
            }
        }
    }

    @Override
    public void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter,
                    boolean forceLogging) {
        this.setLoggerAlias(fqcn);
        if(forceLogging){
            if(this.getClass().getName().equals(handler)) {

                //Send to MongoDB
                //printer.write(level, message, fqcn, FileHandler.class.getName(), formatter, this.reDBAddress, this.rePort, this.reDBPath);
                this.log(appName, level, null, null, null, null, null, null, null, null, null, message, fqcn, handler, formatter);
            }
        }

    }

    public void setREDBAddress(String dbAddress) {
        String key = loggerAlias + ".REDBAddress";

        if(settings.containsKey(key)) {
            this.reDBAddress = (String) settings.get(key);
        }

        if (dbAddress != null) {
            this.reDBAddress = dbAddress;
        }

        if (this.reDBAddress == null || this.reDBAddress.isEmpty()) {
            this.reDBAddress = (String) settings.get(Key.REDBAddress.name());
        }
    }

    public void setREDBPath(String dbPath) {
        String key = loggerAlias + ".REDBPath";

        if(settings.containsKey(key)) {
            this.reDBPath = (String) settings.get(key);
        }

        if (dbPath != null) {
            this.reDBPath = dbPath;
        }

        if (this.reDBPath == null || this.reDBPath.isEmpty()) {
            this.reDBPath = (String) settings.get(Key.REDBPath.name());
        }
    }

    public void setREPort(Integer rePort) {
        String key = loggerAlias + ".ESSPort";

        if(settings.containsKey(key)) {
            this.rePort = this.initPort((String) settings.get(key));
        }

        if (rePort != null) {
            this.rePort = rePort;
        }

        if (this.rePort == null) {
            this.rePort = this.initPort((String)settings.get(Key.REPort.name()));
        }
    }

    private Integer initPort(String limit) {
        String port = limit;
        Integer portNum = 0;
        try {
            portNum = Integer.parseInt(port.trim());
        }
        catch (NumberFormatException e) {
            System.out.print("Your RE Port is not readable, please check it => ");
            e.printStackTrace();
        }
        return portNum;
    }

    private void setLoggerAlias(String fqcn) {
        for(String key : settings.stringPropertyNames()) {
            String value = settings.getProperty(key);
            if (value.equalsIgnoreCase(fqcn)) {
                this.loggerAlias = key.split("\\.")[0];
            }
        }
        this.setREDBAddress(null);
        this.setREDBPath(null);
        this.setREPort(null);
    }
}
