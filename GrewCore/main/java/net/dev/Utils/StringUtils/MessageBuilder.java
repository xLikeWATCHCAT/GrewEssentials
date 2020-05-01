package net.dev.Utils.StringUtils;

import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.*;

public class MessageBuilder {
    public static TextComponent get(String message) {
        return new TextComponent(StringUtils.translateColorCodes(message));
    }

    public static TextComponent get(String message,String hover,String url) {
        TextComponent textComponent = new TextComponent(StringUtils.translateColorCodes(message));
        if (url != null) {
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
            textComponent.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(StringUtils.translateColorCodes(hover + url))).create()));
        }

        return textComponent;
    }

    public static TextComponent get(String message, String command, ChatColor color, String hover, boolean bold) {
        TextComponent textComponent = new TextComponent(StringUtils.translateColorCodes(message));
        if (command != null) {
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        }

        if (hover != null) {
            textComponent.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(StringUtils.translateColorCodes(hover))).create()));
        }

        if (color != null) {
            textComponent.setColor(color);
        }

        textComponent.setBold(bold);
        return textComponent;
    }
}
