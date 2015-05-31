package org.tokalac.logging.modules.output.handler;
/**
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 * Param <V>
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;

import org.tokalac.logging.core.Level;
import org.tokalac.logging.modules.output.formatter.Formatter;

public class Printer {

    private Mongo mongo;

    private void writeInFile(FileWriter fw, Map<String, String> log, File logFile, double limitSize, Formatter formatter) throws IOException {

        if (limitSize ==  0 || logFile.length() <= limitSize) {

            if(log.get("DATE") != null) {
                fw.write(String.format("%-5s %1s", log.get("DATE"), formatter.getSeparator()));
            }

            if(log.get("APPNAME") != null) {
                fw.write(String.format("%-5s %1s", log.get("APPNAME"), formatter.getSeparator()));
            }

            if(log.get("FQCN") != null) {
                fw.write(String.format(" %-52s %5s",log.get("FQCN"), formatter.getSeparator()));
            }

            if(log.get("LEVEL") != null) {
                fw.write(String.format(" %-5s %1s", log.get("LEVEL"), formatter.getSeparator()));
            }

            if(log.get("MESSAGE") != null) {
                fw.write(String.format(" %-6s%n", log.get("MESSAGE")));
            }
            fw.close();

        }

        else {
            File folder = logFile.getParentFile();
            int counter = 0;
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.getName().contains(logFile.getName().split("\\.")[0])) {
                    if (fileEntry.getName().contains("_")) {
                        int max;
                        String[] tokens = fileEntry.getName().split("_");
                        String firstPart = tokens[tokens.length - 1];
                        if (firstPart != null && !firstPart.isEmpty()) {
                            if (firstPart.split("\\.")[0].matches("\\d+(\\.\\d+)?")) {
                                max = Integer.parseInt(firstPart.split("\\.")[0]);
                                if (counter < max) {
                                    counter = max;
                                }
                            }
                        }
                    }
                }
            }
            counter += 1;
            StringBuffer newName = new StringBuffer();
            newName.append(logFile.getName().split("\\.")[0] + "_" + counter);
            if (logFile.getName().split("\\.").length > 1) {
                newName.append("." + logFile.getName().split("\\.")[1]);
            }
            File newlogFile = new File(logFile.getParent() + "/" + newName.toString());
            fw.close();
            logFile.renameTo(newlogFile);
        }
    }

    public  void write(String appName, Level level, String message, String fqcn, String handler, Formatter formatter){
        this.write(appName, level, message, null, null, null, null, null, null, null, null, null, fqcn, handler, formatter, null, 0, null, null, null, null);
    }

    public  void write(String appName, Level level, String message, String fqcn, String handler, Formatter formatter, String logFilePath, int logFileLimitSize){
        this.write(appName, level, message, null, null, null, null, null, null, null, null, null, fqcn, handler, formatter, logFilePath, logFileLimitSize, null, null, null, null);
    }

    public  void write(String appName, Level level, String message, String fqcn, String handler, Formatter formatter, Client client){
        this.write(appName, level, message, null, null, null, null, null, null, null, null, null, fqcn, handler, formatter, null, 0, client, null, null, null);
    }

    public  void write(String appName, Level level, String message, String process, String userId, String sessionId, String cartId, String productId, String price, String qty, String onSale, String categoryId, String fqcn, String handler, Formatter formatter, String reDBAddress, Integer rePort, String reDBPath){
        this.write(appName, level, message, process, userId, sessionId, cartId, productId, price, qty, onSale, categoryId, fqcn, handler, formatter, null, 0, null, reDBAddress, rePort, reDBPath);
    }

    public void write(String appName, Level level, String message, String process, String userId, String sessionId, String cartId, String productId, String price, String qty, String onSale, String categoryId, String fqcn, String handler, Formatter formatter, String logFilePath, int logFileLimitSize, Client client, String reDBAddress, Integer rePort, String reDBPath) {

        Map<String, String> log = new HashMap<String, String>();

        log = this.initMap(log, appName, level, fqcn, message, formatter);

        if (handler.equals(ConsoleHandler.class.getName())) {
            if(log.get("DATE") != null)
                System.out.printf("%-5s %1s", log.get("DATE"), formatter.getSeparator());

            if(log.get("APPNAME") != null)
                System.out.printf(" %-5s %1s", log.get("APPNAME"), formatter.getSeparator());

            if(log.get("FQCN") != null)
                System.out.printf(" %-52s %5s", log.get("FQCN"), formatter.getSeparator());

            if(log.get("LEVEL") != null)
                System.out.printf(" %-5s %1s", log.get("LEVEL"), formatter.getSeparator());

            if(log.get("MESSAGE") != null)
                System.out.printf(" %-6s%n", log.get("MESSAGE"));
        }

        else if (handler.equals(FileHandler.class.getName())) {
            this.writeToFile(appName, level, message, fqcn, FileHandler.class.getName(), formatter, logFilePath, logFileLimitSize);
        }
        else if(handler.equals(REHandler.class.getName())){
             this.writeToRE(appName, level, message, process, userId, sessionId, cartId, productId, price, qty, onSale, categoryId, fqcn, REHandler.class.getName(), formatter, reDBAddress, rePort, reDBPath);
        }
        else if(handler.equals(ESHandler.class.getName())){
             this.writeToESS(appName, level, message, fqcn, ESHandler.class.getName(), formatter, client);
        }
    }

    private void writeToRE(String appName, Level level, String message, String process, String userId, String sessionId, String cartId, String productId, String price, String qty, String onSale, String categoryId, String fqcn, String name, Formatter formatter, String reDBAddress, Integer rePort, String reDBPath){
        Map<String, String> log = new HashMap<String, String>();

        log = this.initMap(log, appName, level, fqcn, message, process, userId, sessionId, cartId, productId, price, qty, onSale, categoryId, formatter);

        ServerAddress address = new ServerAddress(reDBAddress, rePort);
        //MongoClientURI clientURI = new MongoClientURI(reDBAddress+":"+rePort+"/"+reDBPath);

        try {
            mongo = new Mongo(address);
            DB db = mongo.getDB(reDBPath);
            DBCollection logs = db.getCollection("logs");

            BasicDBObject logObj = new BasicDBObject();

            if(log.get("DATE") != null) {
                logObj.put("DATE", String.format("%-5s %1s", log.get("DATE"), formatter.getSeparator()));
            }

            if(log.get("APPNAME") != null) {
                logObj.put("APPNAME", String.format("%-5s %1s", log.get("APPNAME"), formatter.getSeparator()));
            }

            if(log.get("FQCN") != null) {
                logObj.put("FQCN", String.format(" %-52s %5s", log.get("FQCN"), formatter.getSeparator()));
            }

            if(log.get("LEVEL") != null) {
                logObj.put("LEVEL", String.format(" %-5s %1s", log.get("LEVEL"), formatter.getSeparator()));
            }

            if(log.get("PROCESS") != null) {
                logObj.put("PROCESS", String.format(" %-5s %1s", log.get("PROCESS"), formatter.getSeparator()));
            }

            if(log.get("USER_ID") != null) {
                logObj.put("USER_ID", String.format(" %-5s %1s", log.get("USER_ID"), formatter.getSeparator()));
            }

            if(log.get("CART_ID") != null) {
                logObj.put("CART_ID", String.format(" %-5s %1s", log.get("CART_ID"), formatter.getSeparator()));
            }

            if(log.get("CATEGORY_ID") != null) {
                logObj.put("CATEGORY_ID", String.format(" %-5s %1s", log.get("CATEGORY_ID"), formatter.getSeparator()));
            }

            if(log.get("PRICE") != null) {
                logObj.put("PRICE", String.format(" %-5s %1s", log.get("PRICE"), formatter.getSeparator()));
            }

            if(log.get("PRODUCT_ID") != null) {
                logObj.put("PRODUCT_ID", String.format(" %-5s %1s", log.get("PRODUCT_ID"), formatter.getSeparator()));
            }

            if(log.get("ON_SALE") != null) {
                logObj.put("ON_SALE", String.format(" %-5s %1s", log.get("ON_SALE"), formatter.getSeparator()));
            }

            if(log.get("QTY") != null) {
                logObj.put("QTY", String.format(" %-5s %1s", log.get("QTY"), formatter.getSeparator()));
            }

            if(log.get("SESSION_ID") != null) {
                logObj.put("SESSION_ID", String.format(" %-5s %1s", log.get("SESSION_ID"), formatter.getSeparator()));
            }

            if(log.get("MESSAGE") != null) {
                logObj.put("MESSAGE", String.format(" %-6s%n", log.get("MESSAGE"), formatter.getSeparator()));
            }

            logs.insert(logObj);

            String processMap = "function() { "+
                    "var category; " +
                    "if ( this.PROCESS == Search ) "+
                    "category = 'Search'; " +
                    "else " +
                    "category = 'Payment'; "+
                    "emit(category, {name: this.USER_ID});}";

            String processReduce = "function(key, values) { " +
                    "var sum = 0; " +
                    "values.forEach(function(doc) { " +
                    "sum += 1; "+
                    "}); " +
                    "return {process: sum};} ";

            String userMap = "function() { "+
                    "var category; " +
                    "if ( this.QTY >= 2 ) "+
                    "category = 'Multiple'; " +
                    "else " +
                    "category = 'Single'; "+
                    "emit(category, {name: this.USER_ID});}";

            String userReduce = "function(key, values) { " +
                    "var sum = 0; " +
                    "values.forEach(function(doc) { " +
                    "sum += 1; "+
                    "}); " +
                    "return {process: sum};} ";

            MapReduceCommand processCmd = new MapReduceCommand(logs, processMap, processReduce,
                    null, MapReduceCommand.OutputType.INLINE, null);

            MapReduceCommand userCmd = new MapReduceCommand(logs, userMap, userReduce,
                    null, MapReduceCommand.OutputType.INLINE, null);

            MapReduceOutput processOut = logs.mapReduce(processCmd);
            MapReduceOutput userOut = logs.mapReduce(userCmd);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void writeToESS(String appName, Level level, String message, String fqcn, String name, Formatter formatter, Client client){
        Map<String, String> log = new HashMap<String, String>();

        log = this.initMap(log, appName, level, fqcn, message, formatter);

        Date date = new Date();
        long now = date.getTime();
        SimpleDateFormat dateFormater = new SimpleDateFormat("dd-MM-yy");

        String indexStr = new String((dateFormater.format(now).toString())+"-LOG-"+level);

        try{
           IndexResponse response = client.prepareIndex(indexStr, appName)
                   .setSource(jsonBuilder()
                                   .startObject()
                                   .field("DATE", String.format("%-5s %1s", log.get("DATE"), formatter.getSeparator()))
                                   .field("FQCN", String.format(" %-52s %5s", log.get("FQCN"), formatter.getSeparator()))
                                   .field("LEVEL", String.format(" %-5s %1s", log.get("LEVEL"), formatter.getSeparator()))
                                   .field("MESSAGE", String.format(" %-6s%n", log.get("MESSAGE"), formatter.getSeparator()))
                                   .endObject()
                   )
                   .execute()
                   .actionGet();
            client.close();
        }catch (IOException e){
           System.out.println(e.getLocalizedMessage());
        }
    }

    private void writeToFile(String appName, Level level, String message, String fqcn, String name,
                             Formatter formatter, String logFilePath, double logFileLimitSize) {

        Map<String, String> log = new HashMap<String, String>();

        log = this.initMap(log, appName, level, fqcn, message, formatter);

        FileWriter fw = null;
        final File logFile = new File (logFilePath);
        final File parent_directory = logFile.getParentFile();

        try {

            if (null != parent_directory) {
                parent_directory.mkdirs();
            }

            fw = new FileWriter (logFile, true);

            this.writeInFile(fw, log, logFile, logFileLimitSize, formatter);
        }
        catch (IOException exception) {
            System.out.print("Unable to write in file => ");
            exception.printStackTrace();
        }

        finally {
            try {
                fw.close();
            } catch (IOException e) {
                System.out.println ("Unable to close File => ");
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> initMap(Map<String, String> log, String appName, Level level, String fqcn, String message,
                                        Formatter formatter) {
        Date date = new Date();
        long now = date.getTime();
        SimpleDateFormat dateFormater = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

        if(formatter.isDateEnabled()) {
            log.put("DATE", dateFormater.format(now));
        }

        if(formatter.isFQCNEnabled()) {
            log.put("FQCN", fqcn);
        }

        if (formatter.isAppNameEnabled()) {
            log.put("APPNAME", appName);
        }

        if (formatter.isLevelEnabled()) {
            log.put("LEVEL", level.getName());
        }

        if (formatter.isMessageEnabled()) {
            log.put("MESSAGE", message);
        }

        return log;
    }

    private Map<String, String> initMap(Map<String, String> log, String appName, Level level, String fqcn, String message, String process, String userId, String sessionId,
                                        String cartId, String productId, String price, String qty, String onSale, String categoryId, Formatter formatter) {
        Date date = new Date();
        long now = date.getTime();
        SimpleDateFormat dateFormater = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

        if(formatter.isDateEnabled()) {
            log.put("DATE", dateFormater.format(now));
        }

        if(formatter.isFQCNEnabled()) {
            log.put("FQCN", fqcn);
        }

        if (formatter.isLevelEnabled()) {
            log.put("LEVEL", level.getName());
        }

        if (formatter.isAppNameEnabled()) {
            log.put("APPNAME", appName);
        }

        if (formatter.isMessageEnabled()) {
            log.put("MESSAGE", message);
        }

        if(formatter.isProcessEnabled()){
            log.put("PROCESS", process);
        }

        if(formatter.isUserIdEnabled()){
            log.put("USER_ID", userId);
        }

        if(formatter.isCartIdEnabled()){
            log.put("CART_ID", cartId);
        }

        if(formatter.isCategoryIdEnabled()){
            log.put("CATEGORY_ID", categoryId);
        }

        if(formatter.isPriceEnabled()){
            log.put("PRICE", price);
        }

        if(formatter.isProductIdEnabled()){
            log.put("PRODUCT_ID", productId);
        }

        if(formatter.isOnSaleEnabled()){
            log.put("ON_SALE", onSale);
        }

        if(formatter.isQuantityEnabled()){
            log.put("QTY", qty);
        }

        if(formatter.isSessionIdEnabled()){
            log.put("SESSION_ID", sessionId);
        }

        return log;
    }
}