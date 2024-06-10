package com.brucepang.prpc.logger.support;

import com.brucepang.prpc.logger.Logger;

public class GenerateStandAloneLogger implements Logger {
    private Logger logger;
    private static boolean disable = false;

    public GenerateStandAloneLogger(Logger logger) {
        this.logger = logger;
    }

    public static void setDisable(boolean disable) {
        GenerateStandAloneLogger.disable = disable;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Logger getLogger() {
        return logger;
    }

    /**
     * Append context message
     * @param msg message
     * @return
     */
    private String appendContextMessage(String msg) {
        return " [PRPC] " + msg;
    }

    @Override
    public void trace(String msg) {
        if (disable) {
            return;
        }
        try {
            logger.trace(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void trace(Throwable e) {
        if (disable) {
            return;
        }
        try {
            logger.trace(e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void trace(String msg, Throwable e) {
        if (disable) {
            return;
        }
        try {
            logger.trace(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void debug(String msg) {
        if (disable) {
            return;
        }
        try {
            logger.debug(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void debug(Throwable e) {
        if (disable) {
            return;
        }
        try {
            logger.debug(e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void debug(String msg, Throwable e) {
        if (disable) {
            return;
        }
        try {
            logger.debug(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void info(String msg) {
        if (disable) {
            return;
        }
        try {
            logger.info(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void info(Throwable e) {
        if (disable) {
            return;
        }
        try {
            logger.info(e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void info(String msg, Throwable e) {
        if (disable) {
            return;
        }
        try {
            logger.info(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void warn(String msg) {
        if (disable) {
            return;
        }
        try {
            logger.warn(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void warn(Throwable e) {
        if (disable) {
            return;
        }
        try {
            logger.warn(e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void warn(String msg, Throwable e) {
        if (disable) {
            return;
        }
        try {
            logger.warn(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void error(String msg) {
        if (disable) {
            return;
        }
        try {
            logger.error(appendContextMessage(msg));
        } catch (Throwable t) {
        }
    }

    @Override
    public void error(Throwable e) {
        if (disable) {
            return;
        }
        try {
            logger.error(e);
        } catch (Throwable t) {
        }
    }

    @Override
    public void error(String msg, Throwable e) {
        if (disable) {
            return;
        }
        try {
            logger.error(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }

    @Override
    public boolean isTraceEnabled() {
       if (disable) {
            return false;
        }
        try {
            return logger.isTraceEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public boolean isDebugEnabled() {
        if (disable) {
            return false;
        }
        try {
            return logger.isDebugEnabled();
        }  catch (Throwable t) {
            return false;
        }
    }

    @Override
    public boolean isInfoEnabled() {
        if (disable) {
            return false;
        }
        try {
            return logger.isInfoEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public boolean isWarnEnabled() {
        if (disable) {
            return false;
        }
        try {
            return logger.isWarnEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public boolean isErrorEnabled() {
        if (disable) {
            return false;
        }
        try {
            return logger.isErrorEnabled();
        } catch (Throwable t) {
            return false;
        }
    }
}