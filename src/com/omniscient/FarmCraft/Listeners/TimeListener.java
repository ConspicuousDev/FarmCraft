package com.omniscient.FarmCraft.Listeners;

import com.omniscient.FarmCraft.Countries.Country;
import com.omniscient.FarmCraft.Countries.Month;
import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Utils.Database;
import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimeListener implements Listener {
    public static int year;
    public static Month month;
    public static int day;
    public static int hour;
    public static int minute;
    public static int velocity = 1;

    public static void startClock() throws SQLException {
        ResultSet resultSet = Database.get("SELECT * FROM server_data WHERE id = 1");
        if (resultSet.next()) {
            year = resultSet.getInt("year");
            month = Month.valueOf(resultSet.getString("month"));
            day = resultSet.getInt("day");
            hour = resultSet.getInt("hour");
            minute = resultSet.getInt("minute");
        } else {
            year = 0;
            month = Month.JANUARY;
            day = 1;
            hour = 0;
            minute = 0;
        }
        int i = Bukkit.getScheduler().scheduleSyncRepeatingTask(FarmCraft.instance, new BukkitRunnable() {
            @Override
            public void run() {
                minute += 15;
                if (minute == 60) {
                    minute = 0;
                    hour++;
                    for (Country country : Country.values()) {
                        Bukkit.getWorld(country.getID()).setTime(Methods.getHourTick(country.getHour(hour)));
                    }
                    if (hour == 24) {
                        hour = 0;
                        day++;
                        if (day > month.getDays()) {
                            day = 1;
                            month = getNextMonth(month.getID());
                            if (month == Month.JANUARY) {
                                year++;
                            }
                        }
                    }
                }
            }
        }, 0, 250 / velocity);
    }

    public static Month getNextMonth(int monthID) {
        for (Month month : Month.values()) {
            if (month.getID() == monthID + 1) {
                return month;
            }
        }
        return Month.JANUARY;
    }

    public static Month getLastMonth(int monthID) {
        for (Month month : Month.values()) {
            if (month.getID() == monthID - 1) {
                return month;
            }
        }
        return Month.DECEMBER;
    }
}
