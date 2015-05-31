package org.tokalac.logging.modules.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Config module : Read configuration from Properties local file
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */
public class DefaultConfigurator extends AbstractConfigurator {

    public final static Properties config = new Properties();


    /**
     * Return the content of Properties file
     * @param NONE
     * @return Properties config
     */

    public DefaultConfigurator() {

        setConfigFilePath("loggio.properties");

        FileInputStream configFileStream;

        try {
            configFileStream = new FileInputStream(getConfigFilePath());
            try {
                config.load(configFileStream);
                configFileStream.close();
            }
            catch (IOException e) {
                System.out.print("Unable to read property File: ");
                e.printStackTrace();
            }
        } catch (FileNotFoundException e1) {
            System.out.print("Unable to get property File: ");
            e1.printStackTrace();
        }
    }

    public Properties getSettings(){
        return config;
    }

    public Enumeration<?> getKeys() {
        return config.propertyNames();
    }


}