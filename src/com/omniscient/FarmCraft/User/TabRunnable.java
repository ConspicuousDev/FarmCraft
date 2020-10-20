package com.omniscient.FarmCraft.User;

import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Listeners.TimeListener;
import com.omniscient.FarmCraft.Potions.Effect;
import com.omniscient.FarmCraft.Utils.Methods;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabRunnable {
    public TabRunnable(User user) {
        Player p = user.getPlayer();
        PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
        user.setTabTaskID(Bukkit.getScheduler().scheduleSyncRepeatingTask(FarmCraft.instance, new Runnable() {
            @Override
            public void run() {
                List<String> headerStrings = new ArrayList<>();
                List<String> footerStrings = new ArrayList<>();

                String minuteString = String.valueOf(TimeListener.minute);
                if (TimeListener.minute == 0) {
                    minuteString = "00";
                }

                headerStrings.add("&aVocê está jogando no &e&lJOGAR.FARMCRAFT.COM");
                headerStrings.add("");
                headerStrings.add("&f" + user.getCountry().getMonth(TimeListener.hour, TimeListener.day, TimeListener.month).getName() + "&f, Dia " + user.getCountry().getDay(TimeListener.hour, TimeListener.day, TimeListener.month) + " &8- &7" + user.getCountry().getHour(TimeListener.hour) + "h" + minuteString);
                headerStrings.add("&f" + user.getCountry().getSeason(user.getCountry().getMonth(TimeListener.hour, TimeListener.day, TimeListener.month)).getFullName() + " &8- " + user.getCountry().getName());
                headerStrings.add("");

                List<String> effectList = new ArrayList<>();
                if (user.getEffects().size() > 0) {
                    for (Effect effect : user.getEffects()) {
                        effectList.add("&4Regeneração V &f18:57");
                    }
                } else {
                    effectList = Collections.singletonList("&7Não há efeitos ativos.");
                }

                int effectsPerLine = 1;
                if (effectList.size() > 5) {
                    effectsPerLine = 3;
                } else if (effectList.size() > 3) {
                    effectsPerLine = 2;
                }

                List<String> footerEffects = new ArrayList<>();
                if (effectsPerLine > 1) {
                    int totalLines = effectList.size() / effectsPerLine;

                    int j = 0;
                    for (int i = 0; i < totalLines; i++) {
                        String effectLine = "";
                        while (j < effectsPerLine + (i * effectsPerLine)) {
                            effectLine += effectList.get(j) + " ";
                            j++;
                        }
                        footerEffects.add(effectLine.substring(0, effectLine.length() - 1));
                    }
                    String effectLine = "";
                    while (j < effectList.size()) {
                        effectLine += effectList.get(j) + " ";
                        j++;
                    }
                    if (effectLine != "") {
                        footerEffects.add(effectLine.substring(0, effectLine.length() - 1));
                    }
                } else {
                    footerEffects = effectList;
                }

                footerStrings.add("");
                footerStrings.addAll(footerEffects);
                footerStrings.add("");
                footerStrings.add("&a&b&lRANKS&a, &6&lCASH &ae &cmais &aem &e&lLOJA.FARMCRAFT.COM");

                StringBuilder headerString = new StringBuilder();
                for (int i = 0; i < headerStrings.size(); i++) {
                    headerString.append(headerStrings.get(i));
                    if (i < headerStrings.size() - 1) {
                        headerString.append("\n");
                    }
                }
                StringBuilder footerString = new StringBuilder();
                for (int i = 0; i < footerStrings.size(); i++) {
                    footerString.append(footerStrings.get(i));
                    if (i < footerStrings.size() - 1) {
                        footerString.append("\n");
                    }
                }
                IChatBaseComponent header = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + Methods.color(headerString.toString()) + "\"}");
                IChatBaseComponent footer = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + Methods.color(footerString.toString()) + "\"}");
                PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(header);
                try {
                    Field field = packet.getClass().getDeclaredField("b");
                    field.setAccessible(true);
                    field.set(packet, footer);
                } catch (Exception e) {
                    Methods.consoleLog("&cOcorreu um erro ao definir os Headers e Footers do jogador " + user.getTagName() + "&c.");
                } finally {
                    connection.sendPacket(packet);
                }
            }
        }, 0, 20));
    }
}
