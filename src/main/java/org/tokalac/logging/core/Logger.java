package org.tokalac.logging.core;

import org.tokalac.logging.modules.output.formatter.Formatter;
import org.tokalac.logging.modules.output.handler.Handler;
/**
 * Implementation of Log msg types and FQCN path
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */
public interface Logger {

    // TRACE
    boolean isTraceEnabled();
    void trace(String message);

    // RECOMMENDATION
    boolean isRecommendationEnabled();
    void recommendation(String message);
    void recommendation(String userid, String productid, String process, String message);
    void recommendation(String userid, String productid, String process, String qty, String onsale, String categoryid, String message);
    void recommendation(String userid, String sessionid, String cartid, String productid, String price, String qty, String onsale, String process, String categoryid, String message);


    // DEBUG
    boolean isDebugEnabled();
    void debug(String message);

    // INFO
    boolean isInfoEnabled();
    void info(String message);

    // WARNING
    boolean isWarnEnabled();
    void warn(String message);

    // ERROR
    boolean isErrorEnabled();
    void error(String message);

    void addHandler(Handler handler);

    void setLevel(Level level);

    void setFormatter(Formatter formatter);

    CodeLocation getCodeLocation();
}