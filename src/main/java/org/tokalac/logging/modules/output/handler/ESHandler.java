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

import org.elasticsearch.common.transport.*;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;

import java.util.Properties;

public class ESHandler extends AbstractHandler {

    private String essAddress;
    private Integer essPort;
    private Client client;
    private String loggerAlias;
    private Properties settings = LogManager.config.getSettings();

    private Printer printer = new Printer();

    public ESHandler() {
        this.setESSddress(null);
        this.setESSPort(null);
        this.setClient();
    }

    public ESHandler(String address, Integer port) {
        this.setESSddress(address);
        this.setESSPort(port);
        this.setClient();
    }

    @Override
    public void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter) {
        this.setLoggerAlias(fqcn);
        if(this.getClass().getName().equals(handler)) {

            //Send to Elastic Search Server
            printer.write(appName, level, message, fqcn, FileHandler.class.getName(), formatter, this.client);
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
        if(level == levelFixed){
            if(this.getClass().getName().equals(handler)) {

                //Send to Elastic Search Server
                printer.write(appName, levelFixed, message, fqcn, FileHandler.class.getName(), formatter, this.client);
            }
        }
    }

    @Override
    public void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter,
                    boolean forceLogging) {
        this.setLoggerAlias(fqcn);
        if(forceLogging){
            if(this.getClass().getName().equals(handler)) {

                //Send to Elastic Search Server
                printer.write(appName, level, message, fqcn, FileHandler.class.getName(), formatter, this.client);
            }
        }
    }

    public void setESSddress(String essAddress) {
        String key = loggerAlias + ".ESSAddress";

        if(settings.containsKey(key)) {
            this.essAddress = (String) settings.get(key);
        }

        if (essAddress != null) {
            this.essAddress = essAddress;
        }

        if (this.essAddress == null || this.essAddress.isEmpty()) {
            this.essAddress = (String) settings.get(Key.ESSAddress.name());
        }
    }

    public void setESSPort(Integer essPort) {
        String key = loggerAlias + ".ESSPort";

        if(settings.containsKey(key)) {
            this.essPort = this.initPort((String) settings.get(key));
        }

        if (essPort != null) {
            this.essPort = essPort;
        }

        if (this.essPort == null) {
            this.essPort = this.initPort((String)settings.get(Key.ESSPort.name()));
        }
    }

    private Integer initPort(String limit) {
        String port = limit;
        Integer portNum = 0;
        try {
            portNum = Integer.parseInt(port.trim());
        }
        catch (NumberFormatException e) {
            System.out.print("Your ES Port is not readable, please check it => ");
            e.printStackTrace();
        }
        return portNum;
    }

    public void setClient(){
         this.client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress(this.essAddress, this.essPort))
                .addTransportAddress(new InetSocketTransportAddress(this.essAddress, this.essPort));
    }

    private void setLoggerAlias(String fqcn) {
        for(String key : settings.stringPropertyNames()) {
            String value = settings.getProperty(key);
            if (value.equalsIgnoreCase(fqcn)) {
                this.loggerAlias = key.split("\\.")[0];
            }
        }
        this.setESSddress(null);
        this.setESSPort(null);
        this.setClient();
    }
}
