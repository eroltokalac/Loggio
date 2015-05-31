package org.tokalac.logging.modules.config;

import java.util.Enumeration;
import java.util.Properties;

/**
 * Abstract Class for Module Configurations
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */
public abstract class AbstractConfigurator implements Configurator {

    private static String path;

    /**
     * Return the local path of configuration file
     * @author Erol TOKALACOGLU
     * @param NONE
     * @return File path
     */
    public String getConfigFilePath() {
        return path;
    }

    /**
     * Set the local file path
     * @author Erol TOKALACOGLU
     * @param Local file path
     * @return NONE
     */
    public void setConfigFilePath(String path) {
        AbstractConfigurator.path = path;
    }

    public abstract Properties getSettings();

    public abstract Enumeration<?> getKeys();

}