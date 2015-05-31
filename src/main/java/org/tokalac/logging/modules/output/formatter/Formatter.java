package org.tokalac.logging.modules.output.formatter;


import java.util.List;

/**
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */

public interface Formatter {

    String getSeparator();
    List<String> getOthers();
    boolean isLevelEnabled();
    boolean isMessageEnabled();
    boolean isDateEnabled();
    boolean isFQCNEnabled();
    boolean isProcessEnabled();
    boolean isAppNameEnabled();
    boolean isUserIdEnabled();
    boolean isSessionIdEnabled();
    boolean isCategoryIdEnabled();
    boolean isPriceEnabled();
    boolean isCartIdEnabled();
    boolean isQuantityEnabled();
    boolean isOnSaleEnabled();
    boolean isProductIdEnabled();

}
