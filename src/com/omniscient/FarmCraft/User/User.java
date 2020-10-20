package com.omniscient.FarmCraft.User;

import com.omniscient.FarmCraft.Company.Company;
import com.omniscient.FarmCraft.Countries.Country;
import com.omniscient.FarmCraft.Countries.MapLocation;
import com.omniscient.FarmCraft.CustomItems.Ability;
import com.omniscient.FarmCraft.Potions.Effect;
import com.omniscient.FarmCraft.Utils.Database;
import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    Player player;
    String tagName;
    Rank rank;
    Company company;
    List<Ability> cooldownList = new ArrayList<>();
    int purse;
    int scoreboardTaskID;
    int statBarTaskID;
    int regenTaskID;
    int tabTaskID;
    Country currentCountry;
    MapLocation location;
    List<Effect> effects;
    Map<Stat, Integer> maxStats = new HashMap<>();
    Map<Stat, Integer> stats = new HashMap<>();

    public User(Player player, Rank rank, int purse, Country country) {
        this.player = player;
        this.setRank(rank);
        this.company = null;
        this.purse = purse;
        this.currentCountry = country;
        this.location = MapLocation.NONE;
        this.effects = new ArrayList<>();
        this.setMaxStat(Stat.HEALTH, 100);
        this.setMaxStat(Stat.DEFENSE, 5000);
        this.setMaxStat(Stat.CRITICAL_DAMAGE, 500);
        this.setMaxStat(Stat.CRITICAL_CHANCE, 100);
        this.setMaxStat(Stat.STRENGTH, 500);
        this.setMaxStat(Stat.INTELLIGENCE, 100);
        this.setMaxStat(Stat.SPEED, 500);
        this.setMaxStat(Stat.LUCK, 100);
        this.setMaxStat(Stat.REGEN, this.getMaxStat(Stat.HEALTH) / 4);
        this.setStat(Stat.HEALTH, this.getMaxStat(Stat.HEALTH));
        this.setStat(Stat.DEFENSE, 100);
        this.setStat(Stat.CRITICAL_DAMAGE, 150);
        this.setStat(Stat.CRITICAL_CHANCE, 20);
        this.setStat(Stat.STRENGTH, 20);
        this.setStat(Stat.INTELLIGENCE, this.getMaxStat(Stat.INTELLIGENCE));
        this.setStat(Stat.SPEED, 100);
        this.setStat(Stat.LUCK, 0);
        this.setStat(Stat.REGEN, this.getMaxStat(Stat.HEALTH) / 20);
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getTagName() {
        return this.tagName;
    }

    public Company getCompany() {
        return this.company;
    }

    public Rank getRank() {
        return this.rank;
    }

    public boolean getCooldown(Ability ability) {
        return this.cooldownList.contains(ability);
    }

    public int getPurse() {
        return this.purse;
    }

    public int getScoreboardTaskID() {
        return this.scoreboardTaskID;
    }

    public int getStatBarTaskID() {
        return this.statBarTaskID;
    }

    public int getRegenTaskID() {
        return this.regenTaskID;
    }

    public int getTabTaskID() {
        return this.tabTaskID;
    }

    public Country getCountry() {
        return this.currentCountry;
    }

    public MapLocation getLocation() {
        return this.location;
    }

    public List<Effect> getEffects() {
        return this.effects;
    }

    public int getMaxStat(Stat stat) {
        return this.maxStats.get(stat);
    }

    public int getStat(Stat stat) {
        return this.stats.get(stat);
    }

    public boolean hasPermission(Permission permission, boolean notify) {
        if (this.rank.getPermissions().contains(permission)) {
            return true;
        }
        if (notify) {
            this.getPlayer().sendMessage(Methods.color("&cVocê não tem permissão para isso!"));
        }
        return false;
    }

    public boolean isOnTerrain(World world) {
        for (Country country : Country.values()) {
            if (country.getID().equals(world.getName())) {
                return false;
            }
        }
        return true;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
        String tag = rank.getTag();
        if (rank != Rank.NONE) {
            tag += " ";
        }
        this.tagName = Methods.color(tag + this.player.getDisplayName());
        Scoreboard scoreboard = this.player.getScoreboard();
        Team team = scoreboard.getTeam(rank.name());
        team.addPlayer(this.player);
        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard playerScoreboard = player.getScoreboard();
            Team playerTeam = playerScoreboard.getTeam(rank.name());
            playerTeam.addPlayer(this.player);
        }
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setCooldown(Ability ability) {
        if (!this.hasPermission(Permission.IGNORE_COOLDOWN, false)) {
            this.cooldownList.add(ability);
            new CooldownRunnable(this, ability);
        }
    }

    public void setPurse(int amount) {
        this.purse = amount;
    }

    public void setScoreboardTaskID(int scoreboardTaskID) {
        this.scoreboardTaskID = scoreboardTaskID;
    }

    public void setStatBarTaskID(int statBarTaskID) {
        this.statBarTaskID = statBarTaskID;
    }

    public void setTabTaskID(int tabTaskID) {
        this.tabTaskID = tabTaskID;
    }

    public void setRegenTaskID(int regenTaskID) {
        this.regenTaskID = regenTaskID;
    }

    public void setCountry(Country country) {
        this.player.teleport(Bukkit.getWorld(country.getID()).getSpawnLocation());
        if (this.currentCountry != country) {
            this.currentCountry = country;
            this.player.setBedSpawnLocation(Bukkit.getWorld(country.getID()).getSpawnLocation());
            this.player.sendMessage(Methods.color("&aVocê viajou para o país &7" + country.getName() + "&a!"));
        }
    }

    public void setLocation(MapLocation mapLocation) {
        this.location = mapLocation;
    }

    public void addEffect(Effect effect) {
        this.getEffects().add(effect);
    }

    public void setMaxStat(Stat stat, int value) {
        if (stat == Stat.SPEED) {
            if (value > 500) {
                value = 500;
            } else if (value < 0) {
                value = 0;
            }
            double speedValue = (0.2 * value) / 100;
            this.getPlayer().setFlySpeed((float) speedValue);
            this.getPlayer().setWalkSpeed((float) speedValue);
        } else if (stat == Stat.HEALTH) {
            this.setMaxStat(Stat.REGEN, value / 4);
            this.setStat(Stat.REGEN, value / 20);
        }
        this.maxStats.put(stat, value);
    }

    public void setStat(Stat stat, int value) {
        if (value > this.getMaxStat(stat)) {
            value = this.getMaxStat(stat);
        } else if (value < 0) {
            value = 0;
        }
        if (stat == Stat.SPEED) {
            double speedValue = (0.2 * value) / 100;
            this.getPlayer().setFlySpeed((float) speedValue);
            this.getPlayer().setWalkSpeed((float) speedValue);
        } else if (stat == Stat.HEALTH) {
            double health = this.getPlayer().getMaxHealth() * value / this.getMaxStat(Stat.HEALTH);
            if (health > 0) {
                this.getPlayer().setHealth(health);
            } else {
                value = this.getMaxStat(stat);
                this.kill();
                this.getPlayer().setHealth(this.getPlayer().getMaxHealth());
            }
        }
        this.stats.put(stat, value);
    }

    public void kill() {
        Player p = this.getPlayer();
        int purse = this.getPurse();
        int newPurse = this.getPurse();
        if (!this.isOnTerrain(this.getPlayer().getWorld())) {
            newPurse = purse / 2;
        }
        this.setPurse(newPurse);
        if (purse - newPurse > 0) {
            StringBuilder purseString = new StringBuilder();
            int i = 0;
            for (int c = String.valueOf(purse - newPurse).length(); c > 0; c--) {
                i++;
                purseString.insert(0, String.valueOf(purse - newPurse).charAt(c - 1));
                if (i % 3 == 0 && c != 1) {
                    purseString.insert(0, ".");
                }
            }
            p.sendMessage(Methods.color("&cVocê morreu e perdeu &7" + purseString + " &cmoedas."));
        } else {
            p.sendMessage(Methods.color("&cVocê morreu."));
        }
        p.teleport(Bukkit.getWorld(this.getCountry().getID()).getSpawnLocation());
        p.setFireTicks(0);
    }

    public void saveData() throws SQLException {
        String company = null;
        if (this.company != null) {
            company = this.company.getID();
        }
        Database.set("UPDATE player_data SET player_name = '" + this.player.getDisplayName() + "', company = '" + company + "', rank_id = '" + this.rank.toString() + "', purse = " + this.purse + ", country = '" + this.currentCountry.getID() + "' WHERE uuid = '" + this.player.getUniqueId().toString() + "'");
    }
}
