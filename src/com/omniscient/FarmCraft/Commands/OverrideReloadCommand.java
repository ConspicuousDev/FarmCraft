package com.omniscient.FarmCraft.Commands;

import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class OverrideReloadCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(Methods.color("&cEste commando está bloqueado. Digite &7/restart &ccaso queira reiniciar o servidor."));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommands = new ArrayList<>();
        return subCommands;
    }
}
