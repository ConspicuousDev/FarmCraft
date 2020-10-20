package com.omniscient.FarmCraft.Company;

import com.omniscient.FarmCraft.Terrain.Terrain;
import com.omniscient.FarmCraft.Utils.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Company {
    String ID;
    String name;
    String ownerID;
    List<String> memberIDs;
    List<Terrain> terrains;
    int bank;

    public Company(String ID, String name, String ownerID, List<String> memberIDs, int bank) {
        this.ID = ID;
        this.name = name;
        this.ownerID = ownerID;
        this.memberIDs = memberIDs;
        this.bank = bank;
        this.terrains = new ArrayList<>();
    }

    public String getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public String getOwnerID() {
        return this.ownerID;
    }

    public List<String> getMemberIDs() {
        return this.memberIDs;
    }

    public List<Terrain> getTerrains() {
        return this.terrains;
    }

    public int getBank() {
        return this.bank;
    }

    public void addMember(String memberID) {
        this.memberIDs.add(memberID);
    }

    public void addTerrain(Terrain terrain) {
        this.terrains.add(terrain);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public void setMemberIDs(List<String> memberIDs) {
        this.memberIDs = memberIDs;
    }

    public void setTerrains(List<Terrain> terrains) {
        this.terrains = terrains;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public void saveData() throws SQLException {
        String memberIDs = "";
        for (int i = 0; i < this.memberIDs.size(); i++) {
            memberIDs += this.memberIDs.get(i);
            if (i + 1 < this.memberIDs.size()) {
                memberIDs += ",";
            }
        }
        String terrainIDs = "";
        if (this.terrains != null) {
            for (int i = 0; i < this.terrains.size(); i++) {
                terrainIDs += this.terrains.get(i).getID();
                if (i + 1 < this.terrains.size()) {
                    terrainIDs += ",";
                }
            }
        }
        Database.set("UPDATE company_data SET name = '" + this.name + "', owner_id = '" + this.ownerID + "', member_ids = '" + memberIDs + "', terrains = '" + terrainIDs + "', bank = '" + this.bank + "' WHERE id = '" + this.ID + "'");
    }
}
