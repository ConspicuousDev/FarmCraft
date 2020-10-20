package com.omniscient.FarmCraft.User;

import com.omniscient.FarmCraft.Utils.Methods;

import java.util.Arrays;
import java.util.List;

public enum Rank {
    NONE(Methods.color("&7"), Arrays.asList(Permission.JOIN)),
    VIP(Methods.color("&a[VIP]"), Arrays.asList(Permission.JOIN)),
    VIP_P(Methods.color("&a[VIP&6+&a]"), Arrays.asList(Permission.JOIN)),
    MVP(Methods.color("&b[MVP]"), Arrays.asList(Permission.JOIN)),
    MVP_P(Methods.color("&b[MVP&6+&b]"), Arrays.asList(Permission.JOIN)),
    YT(Methods.color("&c[&fYT&c]"), Arrays.asList(Permission.JOIN)),
    AJD(Methods.color("&e[AJD]"), Arrays.asList(Permission.JOIN)),
    MOD(Methods.color("&2[MOD]"), Arrays.asList(Permission.JOIN)),
    ADM(Methods.color("&c[Admin]"), Arrays.asList(Permission.JOIN)),
    MST(Methods.color("&6[Master]"), Arrays.asList(Permission.JOIN, Permission.RANK_COMMAND, Permission.FARMCRAFT_COMMAND, Permission.BUILD, Permission.IGNORE_COOLDOWN, Permission.WHITELIST, Permission.STATS_COMMAND));

    String tag;
    List<Permission> permissions;

    Rank(String tag, List<Permission> permissions) {
        this.tag = tag;
        this.permissions = permissions;
    }

    public String getTag() {
        return this.tag;
    }

    public List<Permission> getPermissions() {
        return this.permissions;
    }
}
