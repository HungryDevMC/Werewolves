package org.pixelgalaxy.frameworks.registry.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@AllArgsConstructor
public enum LogType {

    ERROR(ChatColor.DARK_RED),
    INFO(ChatColor.AQUA),
    WARNING(ChatColor.YELLOW);

    @Getter
    private ChatColor color;

}
