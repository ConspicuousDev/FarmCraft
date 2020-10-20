package com.omniscient.FarmCraft.Commands;

import com.omniscient.FarmCraft.Company.Company;
import com.omniscient.FarmCraft.Countries.Country;
import com.omniscient.FarmCraft.Countries.Month;
import com.omniscient.FarmCraft.CustomItems.CustomItem;
import com.omniscient.FarmCraft.CustomItems.Enchantment;
import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Listeners.TimeListener;
import com.omniscient.FarmCraft.Terrain.Terrain;
import com.omniscient.FarmCraft.User.Permission;
import com.omniscient.FarmCraft.User.User;
import com.omniscient.FarmCraft.Utils.Methods;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FarmCraftCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User user = FarmCraft.onlineUsers.get(p.getDisplayName());
            if (!user.hasPermission(Permission.FARMCRAFT_COMMAND, true)) {
                return true;
            }
        }
        if (args.length > 0) {
            String subCommand = args[0];
            if (subCommand.equals("give")) {
                if (args.length > 2) {
                    if (FarmCraft.onlineUsers.containsKey(args[1])) {
                        User user = FarmCraft.onlineUsers.get(args[1]);
                        for (CustomItem customItem : CustomItem.values()) {
                            if (customItem.name().equals(args[2])) {
                                user.getPlayer().getInventory().addItem(customItem.getItem());
                                sender.sendMessage(Methods.color("&aO jogador &7" + user.getTagName() + " &arecebeu o item &7" + customItem.getRarity().getColor() + customItem.getName() + "&a."));
                                user.getPlayer().sendMessage(Methods.color("&aVocê recebeu o item &7" + customItem.getRarity().getColor() + customItem.getName() + "&a!"));
                                return true;
                            }
                        }
                        sender.sendMessage(Methods.color("&cO item &7" + args[2] + " &cnão pôde ser encontrado."));
                    } else {
                        sender.sendMessage(Methods.color("&cO jogador &7" + args[1] + " &cnão pôde ser encontrado."));
                    }
                } else {
                    sender.sendMessage(Methods.color("&cPor favor, use: /farmcraft give <jogador> <ID do item>."));
                }
            } else if (subCommand.equals("company")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (args.length > 1) {
                        User user = FarmCraft.onlineUsers.get(p.getDisplayName());
                        if (user.getCompany() == null) {
                            String name = args[1];
                            if (name.equalsIgnoreCase("null")) {
                                sender.sendMessage("&cEste nome não é válido.");
                                return true;
                            }
                            try {
                                Company company = Methods.createCompany(name, p);
                                user.setCompany(company);
                                sender.sendMessage(Methods.color("&aAgora você lidera a companhia &7" + company.getName() + "&a."));
                            } catch (SQLException e) {
                                sender.sendMessage(Methods.color("&cOcorreu um erro na criação da companhia."));
                            }
                        } else {
                            sender.sendMessage(Methods.color("&cVocê já faz parte da companhia &f" + user.getCompany().getName() + "&c."));
                        }
                    } else {
                        sender.sendMessage(Methods.color("&cPor favor, use: /farmcraft company <nome>."));
                    }
                } else {
                    sender.sendMessage(Methods.color("&cVocê precisa ser um jogador para utilizar esse comando."));
                }
            } else if (subCommand.equals("terrain")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (args.length > 1) {
                        if (FarmCraft.onlineUsers.get(p.getDisplayName()).getCompany() != null) {
                            User user = FarmCraft.onlineUsers.get(p.getDisplayName());
                            for (Country country : Country.values()) {
                                if (country.name().equals(args[1])) {
                                    try {
                                        Terrain terrain = Methods.createTerrain(user.getCompany(), country);
                                        sender.sendMessage(Methods.color("&aUm terreno no país " + terrain.getCountry().getName() + " &afoi criado com sucesso para a companhia &f" + terrain.getCompany().getName() + "&a."));
                                        p.teleport(terrain.getWorld().getSpawnLocation());
                                        user.setCountry(terrain.getCountry());
                                        return true;
                                    } catch (SQLException e) {
                                        sender.sendMessage(Methods.color("&cOcorreu um erro durante a criação do terreno."));
                                        return true;
                                    }
                                }
                            }
                            sender.sendMessage(Methods.color("&cO país &7" + args[1] + " &cnão pôde ser encontrado."));
                        } else {
                            sender.sendMessage(Methods.color("&cVocê precisa integrar uma companhia para poder possuir um terreno."));
                        }
                    } else {
                        sender.sendMessage(Methods.color("&cPor favor, use: /farmcraft terrain <país>."));
                    }
                } else {
                    sender.sendMessage(Methods.color("&cVocê precisa ser um jogador para utilizar esse comando."));
                }
            } else if (subCommand.equals("travel")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (args.length > 2) {
                        if (FarmCraft.onlineUsers.containsKey(args[1])) {
                            User user = FarmCraft.onlineUsers.get(args[1]);
                            for (Country country : Country.values()) {
                                if (country.name().equals(args[2])) {
                                    sender.sendMessage(Methods.color("&aO jogador &7" + user.getTagName() + " &aviajou para o país &7" + country.getName() + "&a."));
                                    user.setCountry(country);
                                    return true;
                                }
                            }
                            sender.sendMessage(Methods.color("&cO país &7" + args[2] + " &cnão pôde ser encontrado."));
                        } else {
                            sender.sendMessage(Methods.color("&cO jogador &7" + args[1] + " &cnão pôde ser encontrado."));
                        }
                    } else {
                        sender.sendMessage(Methods.color("&cPor favor, use: /farmcraft travel <país>."));
                    }
                } else {
                    sender.sendMessage(Methods.color("&cVocê precisa ser um jogador para utilizar esse comando."));
                }
            } else if (subCommand.equals("time")) {
                if (args.length > 2) {
                    if (args[1].equals("minute") || args[1].equals("hour") || args[1].equals("month") || args[1].equals("velocity")) {
                        try {
                            int value = Integer.parseInt(args[2]);
                            switch (args[1]) {
                                case "minute":
                                    if (value == 0 || value == 15 || value == 30 || value == 45 || value == 60) {
                                        if (value == 60) {
                                            TimeListener.hour++;
                                            TimeListener.minute = 0;
                                        } else {
                                            TimeListener.minute = value;
                                        }
                                        sender.sendMessage(Methods.color("&aOs minutos foram definidos como &7" + TimeListener.minute + "&a."));
                                    } else {
                                        sender.sendMessage(Methods.color("&cO valor dos minutos deve ser 0, 15, 30, 45 ou 60."));
                                    }
                                    break;
                                case "hour":
                                    if (value >= 0 && value <= 24) {
                                        if (value == 24) {
                                            TimeListener.hour = 0;
                                        } else {
                                            TimeListener.hour = value;
                                        }
                                        sender.sendMessage(Methods.color("&aAs horas foram definidas como &7" + TimeListener.hour + "&a."));
                                    } else {
                                        sender.sendMessage(Methods.color("&cO valor das horas deve ser entre 0 e 24."));
                                    }
                                    break;
                                case "month":
                                    if (value >= 1 && value <= 12) {
                                        for (Month month : Month.values()) {
                                            if (month.getID() == value) {
                                                TimeListener.month = month;
                                                sender.sendMessage(Methods.color("&aO mês foi definido como &7" + month.getName() + "&a."));
                                            }
                                        }
                                    } else {
                                        sender.sendMessage(Methods.color("&cO valor das horas deve ser entre 1 e 12."));
                                    }
                                    break;
                                case "velocity":
                                    if (value >= 1 && value <= 10) {
                                        TimeListener.velocity = value;
                                    } else {
                                        sender.sendMessage(Methods.color("&cO valor da velocidade deve ser entre 1 e 10."));
                                    }
                                    sender.sendMessage(Methods.color("&aA velocidade foi definida como &7" + TimeListener.velocity + "&a."));
                                    break;
                            }
                        } catch (NumberFormatException e) {
                            sender.sendMessage(Methods.color("&cO valor precisa ser um número."));
                        }
                    } else {
                        sender.sendMessage(Methods.color("&cUse: /farmcraft time <minute | hour | month | velocity> <valor>."));
                    }
                } else {
                    sender.sendMessage(Methods.color("&cUse: /farmcraft time <minute | hour | month | velocity> <valor>."));
                }
            } else if (subCommand.equals("enchant")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    User user = FarmCraft.onlineUsers.get(p.getDisplayName());
                    if (args.length > 2) {
                        for (Enchantment enchantment : Enchantment.values()) {
                            if (args[1].equals(enchantment.getID())) {
                                try {
                                    int level = Integer.parseInt(args[2]);
                                    if (p.getItemInHand() != null) {
                                        NBTItem nbtItem = new NBTItem(p.getItemInHand());
                                        if (nbtItem.hasNBTData()) {
                                            if (nbtItem.hasKey("ENCHANTMENTS")) {
                                                CustomItem customItem = CustomItem.valueOf(nbtItem.getString("ID"));
                                                customItem.addEnchantment(enchantment, level);
                                                String enchantmentString = "";
                                                if (!nbtItem.getString("ENCHANTMENTS").equals("")) {
                                                    enchantmentString += ",";
                                                }
                                                enchantmentString += enchantment.getID() + ":" + level;
                                                nbtItem.setString("ENCHANTMENTS", nbtItem.getString("ENCHANTMENTS") + enchantmentString);
                                                p.sendMessage(nbtItem.getString("ENCHANTMENTS") + enchantmentString);
                                                ItemStack itemStack = p.getItemInHand();
                                                ItemMeta itemMeta = itemStack.getItemMeta();
                                                itemMeta.setLore(customItem.getItem().getItemMeta().getLore());
                                                itemStack.setItemMeta(itemMeta);
                                                p.sendMessage(Methods.color("&aVocê aplicou o encantamento &9" + enchantment.getName() + " " + enchantment.getLevel() + " &aao item " + customItem.getItem().getItemMeta().getDisplayName() + "&a."));
                                                return true;
                                            }
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    p.sendMessage(Methods.color("&cO nível precisa ser um número."));
                                }
                                return true;
                            }
                        }
                    } else {
                        p.sendMessage(Methods.color("&cUse: /farmcraft enchant <encantamento> <nível>."));
                    }
                } else {
                    sender.sendMessage(Methods.color("&cVocê precisa ser um jogador para utilizar esse comando."));
                }
            }
        } else {
            //help msg
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> subCommands = new ArrayList<>();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User user = FarmCraft.onlineUsers.get(p.getDisplayName());
            if (!user.hasPermission(Permission.FARMCRAFT_COMMAND, false)) {
                return subCommands;
            }
        }
        if (args.length > 1) {
            String subCommand = args[0];
            if (subCommand.equals("give")) {
                if (args.length == 2) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        String playerName = player.getDisplayName();
                        subCommands.add(playerName);
                    }
                } else if (args.length == 3) {
                    for (CustomItem customItem : CustomItem.values()) {
                        subCommands.add(customItem.name());
                    }
                }
            } else if (subCommand.equals("terrain")) {
                if (args.length == 2) {
                    for (Country country : Country.values()) {
                        subCommands.add(country.name());
                    }
                }
            } else if (subCommand.equals("travel")) {
                if (args.length == 2) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        String playerName = player.getDisplayName();
                        subCommands.add(playerName);
                    }
                } else if (args.length == 3) {
                    for (Country country : Country.values()) {
                        subCommands.add(country.name());
                    }
                }
            } else if (subCommand.equals("time")) {
                if (args.length == 2) {
                    subCommands.add("minute");
                    subCommands.add("hour");
                    subCommands.add("month");
                    subCommands.add("velocity");
                }
            } else if (subCommand.equals("enchant")) {
                if (args.length == 2) {
                    for (Enchantment enchantment : Enchantment.values()) {
                        subCommands.add(enchantment.getID());
                    }
                } else if (args.length == 3) {
                    subCommands.add(String.valueOf(Enchantment.valueOf(args[1]).getMaxLevel()));
                }
            }
        } else {
            subCommands.add("give");
            subCommands.add("travel");
            subCommands.add("company");
            subCommands.add("terrain");
            subCommands.add("help");
            subCommands.add("time");
            subCommands.add("enchant");
        }
        return subCommands;
    }
}
