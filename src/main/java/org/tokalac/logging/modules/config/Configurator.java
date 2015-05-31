package org.tokalac.logging.modules.config;

import java.util.Enumeration;
import java.util.Properties;

/**
 * Interface of Config module
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */
public interface Configurator {

    public String getConfigFilePath();

    public void setConfigFilePath(String path);

    public Properties getSettings();

    public Enumeration<?> getKeys();

}
