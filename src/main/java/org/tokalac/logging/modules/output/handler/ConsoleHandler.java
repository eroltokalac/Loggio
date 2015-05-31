package org.tokalac.logging.modules.output.handler;
/**
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */
import org.tokalac.logging.core.Level;
import org.tokalac.logging.modules.output.formatter.Formatter;

public class ConsoleHandler extends AbstractHandler {

    private Printer printer = new Printer();
    @Override
    public void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter){
        if(this.getClass().getName().equals(handler)) {
            printer.write(appName, level, message, fqcn, ConsoleHandler.class.getName(), formatter);
        }
    }

    @Override
    public void log(String appName, Level level, String userId, String sessionId, String cartId, String productId, String price, String qty, String onSale, String process, String categoryId, String message, String fqcn, String handler, Formatter formatter) {
        this.log(appName, level, message, fqcn, handler, formatter);
    }

    @Override
    public void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter,
                    Level levelFixed) {
        if (level.equals(levelFixed)) {
            if(this.getClass().getName().equals(handler)) {
                printer.write(appName, levelFixed, message, fqcn, ConsoleHandler.class.getName(), formatter);
            }
        }
    }

    @Override
    public void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter,
                    boolean forceLogging) {
        if (forceLogging) {
            if(this.getClass().getName().equals(handler)) {
                printer.write(appName, level, message, fqcn, ConsoleHandler.class.getName(), formatter);
            }
        }
    }
}
