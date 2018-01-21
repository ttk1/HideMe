package net.ttk1;

import java.io.IOException;
import java.util.logging.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.File;

public class HideMeLogger {
    private HideMe plg;
    private Logger logger;

    public HideMeLogger(HideMe plg) {
        this.plg = plg;

        File logDir;
        try {
            logDir = new File(plg.getDataFolder(), "log");
            if (!logDir.exists()) {
                try {
                    logDir.mkdir();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss'.log'");
            File logFile = new File(logDir, sdf.format(new Date(System.currentTimeMillis())));

            FileHandler fh = new FileHandler(logFile.getAbsolutePath(), false);
            fh.setFormatter(new MyFormatter());

            logger = Logger.getLogger(HideMeLogger.class.getName());
            logger.setUseParentHandlers(false); // ログが汚れるので親ロガーには伝搬しない
            logger.addHandler(fh);
            logger.setLevel(Level.INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Logger getLogger(){
        return logger;
    }
    public void info(String logMsg){
        logger.info(logMsg);
    }
    public void warning(String logMsg) {
        logger.warning(logMsg);
    }

    /**
     * 一行でログを出力するための独自フォーマット
     */
    private class MyFormatter extends Formatter {
        private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        @Override
        public synchronized String format(LogRecord record){
            StringBuffer sb = new StringBuffer();

            sb.append("[");
            sb.append(sdf.format(new Date(System.currentTimeMillis())));
            sb.append(" ");
            /*
            if (record.getLevel() == Level.INFO) {
                sb.append("INFO");
            } else if (record.getLevel() == Level.WARNING) {
                sb.append("WARN");
            } else {
                sb.append("OTHER")
            }*/
            sb.append(record.getLevel().getName());
            sb.append("]: ");

            sb.append(record.getMessage());
            sb.append("\n");
            return sb.toString();
        }
    }
}
