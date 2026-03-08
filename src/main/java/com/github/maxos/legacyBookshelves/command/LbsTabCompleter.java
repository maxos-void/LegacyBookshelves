package com.github.maxos.legacyBookshelves.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LbsTabCompleter implements TabCompleter {

    private final List<String> commands = List.of(
            "reload"
    ); // не хейтите за лист из одной команды!!! это на будущее!!!

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (args.length == 1) return commands;
        else return null;
    }

}
