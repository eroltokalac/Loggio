package org.tokalac.logging.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.tokalac.logging.modules.config.Key;
import org.tokalac.logging.modules.output.formatter.DefaultFormatter;
import org.tokalac.logging.modules.output.formatter.Formatter;
import org.tokalac.logging.modules.output.handler.ConsoleHandler;
import org.tokalac.logging.modules.output.handler.ESHandler;
import org.tokalac.logging.modules.output.handler.REHandler;
import org.tokalac.logging.modules.output.handler.FileHandler;
import org.tokalac.logging.modules.output.handler.Handler;

/**
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */
public abstract class AbstractLogger implements Logger {

    private String fqcn;
    private String appName;
    private Handler CONSOLE = new ConsoleHandler();
    private Handler FILE = new FileHandler();
    private Handler ES = new ESHandler();
    private Handler RE = new REHandler();
    protected Map<String, Handler> handlers = new HashMap<String, Handler>();
    protected Level levelFixed;
    protected Formatter formatter;
    protected Properties settings = LogManager.config.getSettings();

    public AbstractLogger(String name) {
        String configAppName = name + ".AppName";
        String configClass = name + ".Name";
        String configLevel = name + ".Level";
        String configHandlers = name + ".Handlers";
        String configFormatter = name + ".Formatter";

        if(settings.containsKey(configAppName)) {
            this.setAppName((String)settings.get(configAppName));
        }

        if(settings.containsKey(configClass)) {
            this.setFQCN((String) settings.get(configClass));
            //System.out.println(this.fqcn);
        }
        else
            this.setFQCN(name);

        if(settings.containsKey(configLevel)) {
            this.setLevel(Level.getLevel((String) settings.get(configLevel)));
            //System.out.println(this.levelFixed.name);
        }

        if(settings.containsKey(configHandlers)) {
            String[] handlers = settings.getProperty(configHandlers).split(",");
            for(int i = 0; i < handlers.length; i++) {
                Class<?> configHandler;
                try {
                    configHandler = Class.forName(handlers[i].trim());
                    this.addHandlerClass(configHandler);
                    //System.out.println(configHandler.getCanonicalName());
                } catch (ClassNotFoundException e) {
                    System.out.print("The class you have specified as handler does not exist: ");
                    e.printStackTrace();
                }
            }
        }

        if(settings.containsKey(configFormatter)) {
            String formater = settings.getProperty(configFormatter);
            Class<?> formatterClass;
            try {
                formatterClass = Class.forName(formater.trim());
                this.setFormatterClass(formatterClass);
            } catch (ClassNotFoundException e) {
                setFormatter(new DefaultFormatter());
                System.out.print("The class you have specified as formatter does not exist: ");
                e.printStackTrace();
            }
        }
        else
            setFormatter(new DefaultFormatter());

    }

    public abstract void trace(String message);
    public abstract void debug(String message);
    public abstract void info(String message);
    public abstract void warn(String message);
    public abstract void error(String message);
    public abstract void recommendation(String message);
    public abstract void recommendation(String userid, String productid, String process, String message);
    public abstract void recommendation(String userid, String productid, String process, String qty, String onsale, String categoryid, String message);
    public abstract void recommendation(String userid, String sessionid, String cartid, String productid, String price, String qty, String onsale, String process, String categoryid, String message);


    private String getFQCN() {
        return fqcn;
    }

    private void setFQCN(String fqcn) {
        this.fqcn = fqcn;
    }

    private String getAppName() {
        return appName;
    }

    private void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isEnabled(String levelFixed) {
        String value;
        boolean result = false;
        try {
            if (settings.get(levelFixed) != null) {
                value = (String) settings.get(levelFixed);
                result = value.equalsIgnoreCase("true") ? true : false;
            }
        }
        catch (NullPointerException e) {
            System.out.print("Settings not set! please check your config: ");
            e.printStackTrace();
        }
        return result;
    }

    public boolean isInfoEnabled() {
        return isEnabled(Key.LevelINFO.name());
    }

    public boolean isWarnEnabled() {
        return isEnabled(Key.LevelWARN.name());
    }

    public boolean isErrorEnabled() {
        return isEnabled(Key.LevelERROR.name());
    }

    public boolean isDebugEnabled() {
        return isEnabled(Key.LevelDEBUG.name());
    }

    public boolean isTraceEnabled() {
        return isEnabled(Key.LevelTrace.name());
    }

    public boolean isRecommendationEnabled() { return  isEnabled(Key.LevelRecommendation.name());}

    public void addHandler(Handler handler) {
        handlers.put(handler.getClass().getName(), handler);
    }
    private void addHandlerClass(Class<?> configHandler) {
        try {
            Handler handler = (Handler) configHandler.newInstance();
            this.addHandler(handler);
            //System.out.println(this.handlers.toString());
        } catch (InstantiationException e) {
            System.out.print("Unable to instantiate your provided handler: ");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.print("Can't access to your provided handler: ");
            e.printStackTrace();
        }
    }

    public void setLevel(Level levelFixed) {
        this.levelFixed = levelFixed;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }
    private void setFormatterClass(Class<?> formatterClass) {
        try {
            Formatter formatter = (Formatter) formatterClass.newInstance();
            this.setFormatter(formatter);
            //System.out.println(this.formatter.getClass().getCanonicalName());
        } catch (InstantiationException e) {
            System.out.print("Unable to instantiate your provided formatter: ");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.print("Can't access to your provided formatter: ");
            e.printStackTrace();
        }
    }

    protected void logByLoggerConfig(String message, Level level) {
        if (!handlers.isEmpty()) {
            Set<String> keys = handlers.keySet();
            Iterator<String> iterator = keys.iterator();
            while(iterator.hasNext()) {
                String key = iterator.next();
                Handler handler = handlers.get(key);
                if(!isNull(levelFixed)) {
                    if(levelFixed.getValue() > level.getValue()) {
                        handler.log(this.getAppName(), level, message, this.getFQCN(), handler.getClass().getName(), formatter, true);
                    }
                    else
                        handler.log(this.getAppName(), level, message, this.getFQCN(), handler.getClass().getName(), formatter, levelFixed);
                }
                else
                    handler.log(this.getAppName(), level, message, this.getFQCN(), handler.getClass().getName(), formatter);
            }
        }
        else if(!isNull(levelFixed)) {
            if (isEnabled(Key.ConsoleHandler.name())) {
                if(levelFixed.getValue() > level.getValue()) {
                    CONSOLE.log(this.getAppName(), level, message, this.getFQCN(), ConsoleHandler.class.getName(), formatter, true);
                }
                else
                    CONSOLE.log(this.getAppName(), level, message, this.getFQCN(), ConsoleHandler.class.getName(), formatter, levelFixed);
            }

            if (isEnabled(Key.FileHandler.name())) {
                if(levelFixed.getValue() > level.getValue()) {
                    FILE.log(this.getAppName(), level, message, this.getFQCN(), FileHandler.class.getName(), formatter, true);
                }
                else
                    FILE.log(this.getAppName(), level, message, this.getFQCN(), FileHandler.class.getName(), formatter, levelFixed);
            }

            if (isEnabled(Key.ESHandler.name())) {
                if(levelFixed.getValue() > level.getValue()) {
                    ES.log(this.getAppName(), level, message, this.getFQCN(), ESHandler.class.getName(), formatter, true);
                }
                else
                    ES.log(this.getAppName(), level, message, this.getFQCN(), ESHandler.class.getName(), formatter, levelFixed);
            }
            /*
            if (isEnabled(Key.REHandler.name())) {
                if(levelFixed.getValue() > level.getValue()) {
                    RE.log(this.getAppName(), level, message, this.getFQCN(), REHandler.class.getName(), formatter, true);
                }
                else
                    RE.log(this.getAppName(), level, message, this.getFQCN(), REHandler.class.getName(), formatter, levelFixed);
            }
            */
        }
    }

    protected void logByPropConfig(String message, Level level) {
        if (handlers.isEmpty() & isNull(levelFixed)) {
            if (isEnabled(Key.ConsoleHandler.name())) {
                CONSOLE.log(this.getAppName(), level, message, this.getFQCN(), ConsoleHandler.class.getName(), formatter);
            }

            if (isEnabled(Key.FileHandler.name())) {
                FILE.log(this.getAppName(), level, message, this.getFQCN(), FileHandler.class.getName(), formatter);
            }

            if (isEnabled(Key.ESHandler.name())) {
                ES.log(this.getAppName(), level, message, this.getFQCN(), ESHandler.class.getName(), formatter);
            }
            /*
            if (isEnabled(Key.REHandler.name())) {
                RE.log(this.getAppName(), level, message, this.getFQCN(), REHandler.class.getName(), formatter);
            }
            */
        }
    }

    protected void logByRecommendationConfig(String userId, String sessionId, String cartId, String productId, String price, String qty, String onSale, String process, String categoryId, String message, Level level) {
        if (isEnabled(Key.REHandler.name())) {
            RE.log(this.getAppName(), level, userId, sessionId, cartId, productId, price, qty, onSale, process, categoryId, message, this.getFQCN(), REHandler.class.getName(), formatter);
        }
    }

    protected boolean isNull(Level levelFixed) {
        return this.levelFixed == null || this.levelFixed.getName() == " " ? true : false;
    }
}