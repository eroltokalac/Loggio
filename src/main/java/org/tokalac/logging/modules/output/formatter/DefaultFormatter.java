package org.tokalac.logging.modules.output.formatter;

import java.util.Properties;

import org.tokalac.logging.core.LogManager;
import org.tokalac.logging.modules.config.Key;

/**
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */
public class DefaultFormatter extends AbstractFormatter {

    private Properties settings = LogManager.config.getSettings();

    public DefaultFormatter() {
        this.isDateEnabled = this.dateEnabled();
        this.isFQCNEnabled = this.FQCNEnabled();
        this.isLevelEnabled = this.levelEnabled();
        this.isProcessEnabled = this.processEnabled();
        this.isMessageEnabled = true;
        this.isAppNameEnabled = this.appNameEnabled();
        this.isCartIdEnabled = this.cartIdEnabled();
        this.isCategoryIdEnabled = this.categoryIdEnabled();
        this.isOnSaleEnabled = this.onSaleEnabled();
        this.isSessionIdEnabled = this.sessionIdEnabled();
        this.isPriceEnabled = this.priceEnabled();
        this.isQuantityEnabled = this.quantityEnabled();
        this.isProductIdEnabled = this.productIdEnabled();
        this.isUserIdEnabled = this.userIdEnabled();

        if(settings.get((Key.Separator.name())) != null) {
            try {
                this.separator = (String) settings.get(Key.Separator.name());
            }
            catch (ClassCastException e) {
                System.out.println("Please check your default separator: ");
                e.printStackTrace();
                this.separator = "|";
            }
        }
        else
            this.separator = "|";
    }

    public boolean isEnabled(String token) {
        String value;
        boolean result = false;
        try {
            if (settings.get(token) != null) {
                value = (String) settings.get(token);
                result = value.equalsIgnoreCase("true") ? true : false;
            }
        }
        catch (NullPointerException e) {
            System.out.print("Settings not set! please check your config: ");
            e.printStackTrace();
        }
        return result;
    }

    private boolean levelEnabled() {
        return isEnabled(Key.ShowLEVEL.name());
    }

    private boolean dateEnabled() {
        return isEnabled(Key.ShowDATE.name());
    }

    private boolean FQCNEnabled() {
        return isEnabled(Key.ShowFQCN.name());
    }

    private boolean processEnabled() { return  isEnabled(Key.ShowProcess.name());}

    private boolean appNameEnabled() { return  isEnabled(Key.ShowAppName.name());}

    private boolean cartIdEnabled() { return  isEnabled(Key.ShowCartId.name());}

    private boolean userIdEnabled() { return  isEnabled(Key.ShowUserId.name());}

    private boolean sessionIdEnabled() { return isEnabled(Key.ShowSessionId.name());}

    private boolean categoryIdEnabled(){ return  isEnabled(Key.ShowCategoryId.name());}

    private boolean priceEnabled(){return  isEnabled(Key.ShowPrice.name());}

    private boolean quantityEnabled(){return isEnabled(Key.ShowQuantity.name());}

    private boolean onSaleEnabled(){return isEnabled(Key.ShowOnSale.name());}

    private boolean productIdEnabled(){return isEnabled(Key.ShowProductId.name());}

}