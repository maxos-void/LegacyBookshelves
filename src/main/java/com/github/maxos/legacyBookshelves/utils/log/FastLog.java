package com.github.maxos.legacyBookshelves.utils.log;

import java.util.logging.Logger;

public class FastLog {

    private static final Logger log = Logger.getLogger("LegacyBookshelves");

    public static void sendLog(LogType type, String logMsg) {
        switch (type) {
            case INFO -> log.info(logMsg);
            case WARN -> log.warning(logMsg);
            case ERR -> log.severe(logMsg);
        }
    }
}
