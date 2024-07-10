package io.github.kituin.chatimage.integration;

import io.github.kituin.ChatImageCode.IChatImageCodeLogger;
import io.github.kituin.chatimage.ChatImage;

public class ChatImageLogger implements IChatImageCodeLogger {

    @Override
    public void info(String s) {
        ChatImage.LOGGER.info(s);
    }

    @Override
    public void info(String s, Object... objects) {
        ChatImage.LOGGER.info(s, objects);
    }

    @Override
    public void debug(String s) {
        ChatImage.LOGGER.debug(s);
    }

    @Override
    public void debug(String s, Object... objects) {
        ChatImage.LOGGER.debug(s, objects);
    }

    @Override
    public void error(String s) {
        ChatImage.LOGGER.error(s);
    }

    @Override
    public void error(String s, Object... objects) {
        ChatImage.LOGGER.error(s, objects);
    }
}
