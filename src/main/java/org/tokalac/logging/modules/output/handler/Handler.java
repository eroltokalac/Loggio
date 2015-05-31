package org.tokalac.logging.modules.output.handler;
/**
 * Choose of information stored
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */
import org.tokalac.logging.core.Level;
import org.tokalac.logging.modules.output.formatter.Formatter;

public interface Handler {

    void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter);
    void log(String appName, Level level, String message, String fqcn, String handler, Formatter formatter, Level levelFixed);
    void log(String appName, Level level, String message, String fqcn, String name, Formatter formatter, boolean forceLogging);
    void log(String appName, Level level, String userId, String sessionId, String cartId, String productId, String price, String qty, String onSale, String process, String categoryId, String message, String fqcn, String handler, Formatter formatter);
}
