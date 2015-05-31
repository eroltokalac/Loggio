package org.tokalac.logging.core;

/**
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */

public class DefaultLogger extends AbstractLogger {

    public DefaultLogger(String name) {
        super(name);
    }

    @Override
    public void trace(String message) {
        if (!handlers.isEmpty() || !isNull(levelFixed)) {
            this.logByLoggerConfig(message, Level.TRACE);
        }

        else {
            this.logByPropConfig(message, Level.TRACE);
        }
    }

    @Override
    public void recommendation(String message) {
        this.logByRecommendationConfig(null, null, null, null, null, null, null, null, null, message, Level.RECOMMENDATION);
    }

    @Override
    public void recommendation(String userId, String productId, String process, String message) {
        this.logByRecommendationConfig(userId, null, null, productId, null, null, null, process, null, message, Level.RECOMMENDATION);
    }

    @Override
    public void recommendation(String userId, String productId, String process, String qty, String onSale, String categoryId, String message) {
        this.logByRecommendationConfig(userId, null, null, productId, null, qty, onSale, process, categoryId, message, Level.RECOMMENDATION);
    }

    @Override
    public void recommendation(String userId, String sessionId, String cartId, String productId, String price, String qty, String onSale, String process, String categoryId, String message) {

        this.logByRecommendationConfig(userId, sessionId, cartId, productId, price, qty, onSale, process, categoryId, message, Level.RECOMMENDATION);
    }

    @Override
    public void debug(String message) {
        if (!handlers.isEmpty() || !isNull(levelFixed)) {
            this.logByLoggerConfig(message, Level.DEBUG);
        }

        else {
            this.logByPropConfig(message, Level.DEBUG);
        }
    }

    @Override
    public void error(String message) {
        if (!handlers.isEmpty() || !isNull(levelFixed)) {
            this.logByLoggerConfig(message, Level.ERROR);
        }
        else {
            this.logByPropConfig(message, Level.ERROR);
        }
    }

    @Override
    public void warn(String message) {
        if (!handlers.isEmpty() || !isNull(levelFixed)) {
            this.logByLoggerConfig(message, Level.WARN);
        }
        else {
            this.logByPropConfig(message, Level.WARN);
        }
    }

    @Override
    public void info(String message) {
        if (!handlers.isEmpty() || !isNull(levelFixed)) {
            this.logByLoggerConfig(message, Level.INFO);
        }

        else {
            this.logByPropConfig(message, Level.INFO);
        }
    }

    /**
     * Returns the code location from where the method was called.
     *
     * @return the code location
     */
    public CodeLocation getCodeLocation()
    {
        return getCodeLocation(2);
    }

    /**
     * Returns the code location from where the method was called.
     *
     * @param depth the level of depth in the stack to use
     * @return the code location
     */
    private CodeLocation getCodeLocation(int depth)
    {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        StackTraceElement[] filteredStackTrace = new StackTraceElement[stackTrace.length - depth];

        System.arraycopy(stackTrace, depth, filteredStackTrace, 0, filteredStackTrace.length);

        return new CodeLocation(filteredStackTrace);
    }
}
