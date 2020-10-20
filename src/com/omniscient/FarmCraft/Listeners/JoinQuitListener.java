package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.Countries.Country;
import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.User.*;
import com.omniscient.FarmCraft.Utils.Database;
import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JoinQuitListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) throws SQLException {
        Player p = e.getPlayer();
        e.setJoinMessage("");
        p.getInventory().setItem(8, FarmCraftMenuListener.getMenuItem());
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        p.setScoreboard(scoreboard);
        for (Rank rank : Rank.values()) {
            Team team = scoreboard.registerNewTeam(rank.name());
            String tag = rank.getTag();
            if (rank != Rank.NONE) {
                tag += " ";
            }
            team.setPrefix(tag);
            team.setNameTagVisibility(NameTagVisibility.ALWAYS);
        }
        User user;
        ResultSet resultSet = Database.get("SELECT * FROM player_data WHERE uuid = '" + p.getUniqueId().toString() + "'");
        if (resultSet.next()) {
            user = new User(p, Rank.valueOf(resultSet.getString("rank_id")), resultSet.getInt("purse"), Country.valueOf(resultSet.getString("country")));
            if (!resultSet.getString("company").equals("null")) {
                user.setCompany(FarmCraft.companyList.get(resultSet.getString("company")));
            } else {
                user.setCompany(null);
            }
            p.teleport(Bukkit.getWorld(user.getCountry().name()).getSpawnLocation());
        } else {
            user = new User(p, Rank.NONE, 0, Country.BRAZIL);
            user.setCompany(null);
            p.setGameMode(GameMode.SURVIVAL);
            Database.set("INSERT INTO player_data (uuid, player_name, company, rank_id) VALUES ('" + p.getUniqueId().toString() + "', '" + p.getDisplayName() + "', NULL, '" + user.getRank() + "')");
            p.sendMessage(Methods.color(""));
            p.sendMessage(Methods.color("&aBem-vindo ao &e&lFARM&f&lCRAFT &b&lCOMPANY&a, &7" + p.getDisplayName() + "&a!"));
            p.sendMessage(Methods.color(""));
        }
        p.teleport(Bukkit.getWorld(user.getCountry().getID()).getSpawnLocation());
        FarmCraft.onlineUsers.put(p.getDisplayName(), user);
        TabRunnable tabRunnable = new TabRunnable(user);
        StatBarRunnable statBarRunnable = new StatBarRunnable(user);
        RegenRunnable regenRunnable = new RegenRunnable(user);
        for (Player player : Bukkit.getOnlinePlayers()) {
            User u = FarmCraft.onlineUsers.get(player.getDisplayName());
            Rank r = u.getRank();
            Team team = scoreboard.getTeam(r.name());
            team.addPlayer(player);
            Rank rank = user.getRank();
            Scoreboard playerScoreboard = player.getScoreboard();
            Team playerTeam = playerScoreboard.getTeam(rank.name());
            playerTeam.addPlayer(p);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) throws SQLException {
        Player p = e.getPlayer();
        e.setQuitMessage("");
        User user = FarmCraft.onlineUsers.remove(p.getDisplayName());
        Bukkit.getScheduler().cancelTask(user.getScoreboardTaskID());
        Bukkit.getScheduler().cancelTask(user.getTabTaskID());
        Bukkit.getScheduler().cancelTask(user.getStatBarTaskID());
        for (Team team : p.getScoreboard().getTeams()) {
            team.unregister();
        }
        for (Objective objective : p.getScoreboard().getObjectives()) {
            objective.unregister();
        }
        p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        user.saveData();
    }
}