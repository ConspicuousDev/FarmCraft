package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.User.User;
import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreboardListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        User user = FarmCraft.onlineUsers.get(p.getDisplayName());
        Scoreboard scoreboard = p.getScoreboard();

        Objective objective = scoreboard.registerNewObjective(p.getDisplayName(), "dummy");
        objective.setDisplayName(Methods.color("&e&lFARM&f&lCRAFT &b&lCOMPANY"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team spacer1 = scoreboard.registerNewTeam("spacer1");
        spacer1.addEntry(Methods.color(" "));

        Team spacer2 = scoreboard.registerNewTeam("spacer2");
        spacer2.addEntry(Methods.color("  "));

        Team spacer3 = scoreboard.registerNewTeam("spacer3");
        spacer3.addEntry(Methods.color("   "));

        Team date = scoreboard.registerNewTeam("date");
        date.addEntry(Methods.color("&a"));

        Team season = scoreboard.registerNewTeam("season");
        season.addEntry(Methods.color("&7"));

        Team gameDate = scoreboard.registerNewTeam("gameDate");
        gameDate.addEntry(Methods.color("&f, Dia "));

        Team hour = scoreboard.registerNewTeam("hour");
        hour.addEntry(Methods.color("&c"));

        Team location = scoreboard.registerNewTeam("location");
        location.addEntry(Methods.color("&7("));

        Team purse = scoreboard.registerNewTeam("purse");
        purse.addEntry(Methods.color("&fCarteira: "));

        Team link = scoreboard.registerNewTeam("link");
        link.addEntry(Methods.color("&efarmcraft.net"));

        user.setScoreboardTaskID(Bukkit.getScheduler().scheduleSyncRepeatingTask(FarmCraft.instance, new BukkitRunnable() {
            @Override
            public void run() {
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss");
                date.setPrefix(Methods.color("&7" + dateFormat.format(now) + " "));
                date.setSuffix(Methods.color("&8" + hourFormat.format(now)));
                objective.getScore(Methods.color("&a")).setScore(9);

                objective.getScore(Methods.color("   ")).setScore(8);

                season.setPrefix(Methods.color("&7Estação: "));
                season.setSuffix(Methods.color(user.getCountry().getSeason(user.getCountry().getMonth(TimeListener.hour, TimeListener.day, TimeListener.month)).getName()));
                objective.getScore(Methods.color("&7")).setScore(7);

                gameDate.setPrefix(Methods.color("&f" + user.getCountry().getMonth(TimeListener.hour, TimeListener.day, TimeListener.month).getName()));
                gameDate.setSuffix(Methods.color(String.valueOf(user.getCountry().getDay(TimeListener.hour, TimeListener.day, TimeListener.month))));
                objective.getScore(Methods.color("&f, Dia ")).setScore(6);

                String minute = String.valueOf(TimeListener.minute);
                if (TimeListener.minute == 0) {
                    minute = "00";
                }
                hour.setPrefix(Methods.color("&7" + user.getCountry().getHour(TimeListener.hour) + "h" + minute + " "));
                objective.getScore(Methods.color("&c")).setScore(5);

                location.setPrefix(Methods.color("&7⏣ " + user.getLocation().getName() + " "));
                location.setSuffix(Methods.color(user.getCountry().getName() + "&7)"));
                objective.getScore(Methods.color("&7(")).setScore(4);

                objective.getScore(Methods.color("  ")).setScore(3);

                StringBuilder purseString = new StringBuilder();
                int i = 0;
                for (int c = String.valueOf(user.getPurse()).length(); c > 0; c--) {
                    i++;
                    purseString.insert(0, String.valueOf(user.getPurse()).charAt(c - 1));
                    if (i % 3 == 0 && c != 1) {
                        purseString.insert(0, ".");
                    }
                }
                purse.setSuffix(Methods.color("&6" + purseString.toString()));
                objective.getScore(Methods.color("&fCarteira: ")).setScore(2);

                objective.getScore(Methods.color(" ")).setScore(1);

                objective.getScore(Methods.color("&efarmcraft.net")).setScore(0);
            }
        }, 0, 20));
    }
}
