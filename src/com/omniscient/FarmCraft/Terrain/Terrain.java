package com.omniscient.FarmCraft.Terrain;

import com.omniscient.FarmCraft.Company.Company;
import com.omniscient.FarmCraft.Countries.Country;
import com.omniscient.FarmCraft.FarmCraft;
import com.omniscient.FarmCraft.Utils.Database;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.sql.SQLException;

public class Terrain {
    String ID;
    Company company;
    Country country;
    String resourcesJSON;
    TerrainType terrainType;

    public Terrain(String ID, String company, Country country, String resourcesJSON, TerrainType terrainType) {
        this.ID = ID;
        this.company = FarmCraft.companyList.get(company);
        this.country = country;
        this.resourcesJSON = resourcesJSON;
        this.terrainType = terrainType;
    }

    public String getID() {
        return this.ID;
    }

    public Company getCompany() {
        return this.company;
    }

    public Country getCountry() {
        return this.country;
    }

    public String getResourcesJSON() {
        return this.resourcesJSON;
    }

    public TerrainType getTerrainType() {
        return this.terrainType;
    }

    public World getWorld() {
        return Bukkit.getWorld(this.ID);
    }

    public void setCompany(String company) {
        this.company = FarmCraft.companyList.get(company);
    }

    public void setResourcesJSON(String resourcesJSON) {
        this.resourcesJSON = resourcesJSON;
    }

    public void saveData() throws SQLException {
        Database.set("UPDATE terrain_data SET company = '" + this.company.getID() + "', country = '" + this.country.getID() + "', resources = '" + this.resourcesJSON + "', type = '" + this.terrainType.getID() + "' WHERE id = '" + this.ID + "'");
    }
}
