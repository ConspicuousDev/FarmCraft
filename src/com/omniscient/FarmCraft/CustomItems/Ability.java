package com.omniscient.FarmCraft.CustomItems;

import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.User.User;
import com.omniscient.FarmCraft.Utils.Methods;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.json.simple.parser.ParseException;

public interface Ability {
    String name = "&cOcorreu um erro!";
    String description = "&cOcorreu um erro!";
    String input = "&cOcorreu um erro!";
    boolean hidden = false;
    double cooldown = 0;

    default void run(Event e, Player p, Ability ability) throws ParseException {
        User user = FarmCraft.onlineUsers.get(p.getDisplayName());
        if (ability.meetsCondition(e)) {
            if (!user.getCooldown(ability)) {
                ability.execute(e);
                user.setCooldown(ability);
                sendMessage(p, ability);
            } else if (!ability.isHidden()) {
                p.sendMessage(Methods.color("&cAguarde um pouco."));
            }
        }
    }

    boolean meetsCondition(Event event);

    void execute(Event event) throws ParseException;

    static void sendMessage(Player p, Ability ability) {
        if (!ability.isHidden()) {
            Methods.sendActionBar(p, "&aA habilidade &6" + ability.getName() + " &afoi utilizada.");
        }
    }

    public String getName();

    public String getDescription();

    public String getInput();

    public boolean isHidden();

    public double getCooldown();
}
