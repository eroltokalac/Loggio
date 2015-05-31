package org.tokalac.logging.modules.output.formatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */

public abstract class AbstractFormatter implements Formatter {

    protected String separator;
    protected boolean isDateEnabled;
    protected boolean isMessageEnabled;
    protected boolean isLevelEnabled;
    protected boolean isFQCNEnabled;
    protected boolean isProcessEnabled;
    protected boolean isAppNameEnabled;

    protected boolean isUserIdEnabled;
    protected boolean isSessionIdEnabled;
    protected boolean isCategoryIdEnabled;
    protected boolean isPriceEnabled;
    protected boolean isCartIdEnabled;
    protected boolean isQuantityEnabled;
    protected boolean isOnSaleEnabled;
    protected boolean isProductIdEnabled;

    protected List<String> other = new ArrayList<String>();

    public String getSeparator() {
        return separator;
    }
    public List<String> getOthers() {
        return other;
    }
    public boolean isLevelEnabled() {
        return isLevelEnabled;
    }
    public boolean isMessageEnabled() {
        return isMessageEnabled;
    }
    public boolean isDateEnabled() {
        return isDateEnabled;
    }
    public boolean isFQCNEnabled() {
        return isFQCNEnabled;
    }
    public boolean isProcessEnabled() { return isProcessEnabled;}
    public boolean isAppNameEnabled(){ return isAppNameEnabled;}

    public boolean isUserIdEnabled(){ return  isUserIdEnabled;}
    public boolean isSessionIdEnabled(){ return  isSessionIdEnabled;}
    public boolean isCategoryIdEnabled(){return  isCategoryIdEnabled;}
    public boolean isPriceEnabled(){ return  isPriceEnabled;}
    public boolean isCartIdEnabled(){ return isCartIdEnabled;}
    public boolean isQuantityEnabled(){return  isQuantityEnabled;}
    public boolean isOnSaleEnabled(){return  isOnSaleEnabled;}
    public boolean isProductIdEnabled(){return  isProductIdEnabled;}

}