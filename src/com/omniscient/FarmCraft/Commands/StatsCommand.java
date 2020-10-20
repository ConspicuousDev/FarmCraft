package com.omniscient.FarmCraft.Commands;

import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.User.Permission;
import com.omniscient.FarmCraft.User.Stat;
import com.omniscient.FarmCraft.User.User;
import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StatsCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User user = FarmCraft.onlineUsers.get(p.getDisplayName());
            if (!user.hasPermission(Permission.STATS_COMMAND, true)) {
                return true;
            }
        }
        if (args.length > 2) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getDisplayName().equalsIgnoreCase(args[0])) {
                    User user = FarmCraft.onlineUsers.get(player.getDisplayName());
                    for (Stat stat : Stat.values()) {
                        if (args[1].equals(stat.name())) {
                            try {
                                int i = Integer.parseInt(args[2]);
                                if (stat == Stat.HEALTH || stat == Stat.INTELLIGENCE) {
                                    user.setMaxStat(stat, i);
                                }
                                user.setStat(stat, i);
                                sender.sendMessage(Methods.color("&aO stat " + stat.getName() + "&a de " + user.getTagName() + " &afoi definido como " + stat.getColor() + user.getStat(stat)) + stat.getSymbol() + ChatColor.GREEN + ".");
                                player.sendMessage(Methods.color("&aO seu stat " + stat.getName() + " &afoi definido como " + stat.getColor() + user.getStat(stat)) + stat.getSymbol() + ChatColor.GREEN + ".");
                            } catch (NumberFormatException e) {
                                sender.sendMessage(Methods.color("&cO valor precisa ser um número."));
                                return true;
                            }
                            return true;
                        }
                    }
                    sender.sendMessage(Methods.color("&cO stat &7" + args[1] + " &cnão foi encontrado."));
                    return true;
                }
            }
            sender.sendMessage(Methods.color("&cO jogador &7" + args[0] + " &cnão foi encontrado."));
        } else {
            sender.sendMessage(Methods.color("&cUse: /stats <jogador> <stat> <valor>."));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> subCommands = new ArrayList<>();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User user = FarmCraft.onlineUsers.get(p.getDisplayName());
            if (!user.hasPermission(Permission.STATS_COMMAND, false)) {
                return null;
            }
        }
        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                String playerName = player.getDisplayName();
                subCommands.add(playerName);
            }
        } else if (args.length == 2) {
            for (Stat stat : Stat.values()) {
                subCommands.add(stat.name());
            }
        } else if (args.length == 3) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getDisplayName().equalsIgnoreCase(args[0])) {
                    User user = FarmCraft.onlineUsers.get(player.getDisplayName());
                    for (Stat stat : Stat.values()) {
                        if (args[1].equals(stat.name())) {
                            subCommands.add(String.valueOf(user.getMaxStat(stat)));
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return subCommands;
    }
}
