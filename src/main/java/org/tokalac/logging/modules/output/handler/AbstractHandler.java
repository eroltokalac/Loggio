package org.tokalac.logging.modules.output.handler;

/**
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */
import org.tokalac.logging.core.Level;
import org.tokalac.logging.modules.output.formatter.Formatter;

public abstract class AbstractHandler implements Handler {

    public abstract void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter);
    public abstract void log(String appName, Level level, String userId, String sessionId, String cartId, String productId, String price, String qty, String onSale, String process, String categoryId, String message, String fqcn, String handler, Formatter formatter);
    public abstract void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter, Level levelFixed);
    public abstract void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter, boolean forceLogging);
}
