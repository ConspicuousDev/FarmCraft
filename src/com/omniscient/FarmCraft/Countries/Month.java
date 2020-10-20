package com.omniscient.FarmCraft.Countries;

public enum Month {
    JANUARY(1, "Janeiro", 31),
    FEBRUARY(2, "Fevereiro", 28),
    MARCH(3, "Março", 31),
    APRIL(4, "Abril", 30),
    MAY(5, "Maio", 31),
    JUNE(6, "Junho", 30),
    JULY(7, "Julho", 31),
    AUGUST(8, "Agosto", 31),
    SEPTEMBER(9, "Setembro", 30),
    OCTOBER(10, "Outubro", 31),
    NOVEMBER(11, "Novembro", 30),
    DECEMBER(12, "Dezembro", 31);

    int ID;
    String name;
    int days;

    Month(int ID, String name, int days) {
        this.ID = ID;
        this.name = name;
        this.days = days;
    }

    public int getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public int getDays() {
        return this.days;
    }
}
