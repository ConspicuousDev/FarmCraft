package com.omniscient.FarmCraft.Commands;

import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.User.Permission;
import com.omniscient.FarmCraft.User.Rank;
import com.omniscient.FarmCraft.User.User;
import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RankCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User user = FarmCraft.onlineUsers.get(p.getDisplayName());
            if (!user.hasPermission(Permission.RANK_COMMAND, true)) {
                return true;
            }
        }
        if (args.length > 0) {
            String subCommand = args[0];
            if (subCommand.equals("set")) {
                if (args.length > 2) {
                    if (FarmCraft.onlineUsers.containsKey(args[1])) {
                        User user = FarmCraft.onlineUsers.get(args[1]);
                        for (Rank rank : Rank.values()) {
                            if (rank.name().equals(args[2])) {
                                user.setRank(rank);
                                String tag = "";
                                if (rank.getTag().equals("&7")) {
                                    tag = "&7[Membro]";
                                } else {
                                    tag = rank.getTag();
                                }
                                sender.sendMessage(Methods.color("&aO rank de &7" + user.getPlayer().getDisplayName() + " &afoi definido como &7" + tag + "&a."));
                                user.getPlayer().sendMessage(Methods.color("&aO seu rank foi definido como &7" + tag + "&a!"));
                                return true;
                            }
                        }
                        sender.sendMessage(Methods.color("&cO rank &7" + args[2] + " &cnão pôde ser encontrado. Utilize /rank list para ver todos os ranks disponíveis no servidor."));
                    } else {
                        sender.sendMessage(Methods.color("&cO jogador &7" + args[1] + " &cnão pôde ser encontrado."));
                    }
                } else {
                    sender.sendMessage(Methods.color("&cPor favor, use: /rank set <jogador> <ID do rank>."));
                }
            } else if (subCommand.equals("list")) {
                sender.sendMessage("");
                sender.sendMessage(Methods.color("&eLista de Ranks &7(ID do Rank : Tag do Rank)"));
                for (Rank rank : Rank.values()) {
                    sender.sendMessage(Methods.color("&7" + rank.name() + " : " + rank.getTag()));
                }
                sender.sendMessage("");
            } else if (subCommand.equals("help")) {
                sender.sendMessage("");
                sender.sendMessage(Methods.color("&eComandos de Ranks"));
                sender.sendMessage(Methods.color("&2/rank &1set &b<jogador> <ID do rank> &7- Define o rank do &b<jogador> &7como &b<ID do rank>&7."));
                sender.sendMessage(Methods.color("&2/rank &1list &7- Lista todos os ranks disponíveis no servidor."));
                sender.sendMessage(Methods.color("&2/rank &1help &7- Mostra a lista de comandos de ranks."));
                sender.sendMessage("");
            }
        } else {
            sender.sendMessage("");
            sender.sendMessage(Methods.color("&eComandos de Ranks"));
            sender.sendMessage(Methods.color("&2/rank &1set &b<jogador> <ID do rank> &7- Define o rank do &b<jogador> &7como &b<ID do rank>&7."));
            sender.sendMessage(Methods.color("&2/rank &1list &7- Lista todos os ranks disponíveis no servidor."));
            sender.sendMessage(Methods.color("&2/rank &1help &7- Mostra a lista de comandos de ranks."));
            sender.sendMessage("");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> subCommands = new ArrayList<>();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User user = FarmCraft.onlineUsers.get(p.getDisplayName());
            if (!user.hasPermission(Permission.RANK_COMMAND, false)) {
                return null;
            }
        }
        if (args.length > 1) {
            String subCommand = args[0];
            if (subCommand.equals("set")) {
                if (args.length == 2) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        String playerName = player.getDisplayName();
                        subCommands.add(playerName);
                    }
                } else if (args.length == 3) {
                    for (Rank rank : Rank.values()) {
                        subCommands.add(rank.name());
                    }
                }
            }
        } else {
            subCommands.add("set");
            subCommands.add("list");
            subCommands.add("help");
        }
        return subCommands;
    }
}
