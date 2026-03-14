package com.github.maxos.legacyBookshelves.command;

import com.github.maxos.legacyBookshelves.LegacyBookshelves;
import com.github.maxos.legacyBookshelves.config.impl.MessagesConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LbsCommand implements CommandExecutor {

    private final LegacyBookshelves plugin;
    private final MessagesConfig msgConfig;

    public LbsCommand(LegacyBookshelves plugin, MessagesConfig msgConfig) {
        this.plugin = plugin;
        this.msgConfig = msgConfig;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {

        if (args.length == 0) return false;

        String sub = args[0];
        switch (sub) {
            case "reload" -> {
                plugin.reload(sender);
                return true;
            }
        }  // не хейтите за свитч из одного кейса!!! это на будущее!!!

        return false;
    }

}
