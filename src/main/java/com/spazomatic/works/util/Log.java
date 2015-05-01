/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spazomatic.works.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author samuelsegal
 */
public class Log {
    
    private static final Logger LOG = Logger.getLogger(Log.class.getPackage().getName());
    private Log(){}
    static {       
        //Get rid of annoying extra Log line
        LOG.setUseParentHandlers(false);
        Handler logHandler = new ConsoleHandler();
        logHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return record.getLevel() + " : "
                        + record.getSourceClassName() + " : "
                        + record.getSourceMethodName() + " : "
                        + record.getMessage() + "\n";
            }
        });
        LOG.addHandler(logHandler);
    }
    public static Logger getLog(){
        return LOG;
    }
}
