package org.tokalac.logging.modules.config;

/**
 * Created by Erol TOKALACOGLU on 31/05/15.
 * Version 1.0
 */

public enum Key {
    //Level activation config
    LevelINFO,
    LevelDEBUG,
    LevelWARN,
    LevelERROR,
    LevelTrace,
    LevelRecommendation,

    //OUTPUT Handler Config

    //FileHandler Config
    FileHandler,
    LogFilePath,
    LogFileSize,

    //Console Handler
    ConsoleHandler,

    //Elastic Search Handler
    ESHandler,
    ESSAddress,
    ESSPort,

    //Recommendation Engine Handler
    REHandler,
    REDBAddress,
    REPort,
    REDBPath,

    //OUTPUT Format Configuration
    ShowLEVEL,
    ShowDATE,
    ShowFQCN,
    ShowProcess,
    ShowAppName,
    ShowUserId,
    ShowSessionId,
    ShowCategoryId,
    ShowPrice,
    ShowCartId,
    ShowQuantity,
    ShowOnSale,
    ShowProductId,
    Separator,
}