package com.omniscient.FarmCraft.Terrain;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Random;

public class TerrainGenerator extends ChunkGenerator {
    SimplexOctaveGenerator noiseMap;
    TerrainType terrainType;

    public TerrainGenerator(long seed, TerrainType terrainType) {
        this.terrainType = terrainType;
        noiseMap = new SimplexOctaveGenerator(seed, 4);
        noiseMap.setScale((double) 1 / this.terrainType.getGeneralScale());
    }

    @Override
    public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomes) {
        byte[][] chunk = new byte[world.getMaxHeight() / 16][];
        int worldX = chunkX * 16;
        int worldZ = chunkZ * 16;
        for (int x = worldX; x < worldX + 16; x++) {
            for (int z = worldZ; z < worldZ + 16; z++) {
                double noise = noiseMap.noise(x, z, this.terrainType.getFrequency(), this.terrainType.getAmplitude(), true) + 1;
                int chunkY = (int) (noise * this.terrainType.getYScale() + this.terrainType.getMinHeight());
                for (int y = 0; y <= chunkY; y++) {
                    Material material = Material.AIR;
                    if (y == 0) {
                        material = Material.BEDROCK;
                    }
                    switch (this.terrainType) {
                        case PLAINS:
                            if (y == chunkY) {
                                material = Material.GRASS;
                            } else if (y > chunkY - 4) {
                                material = Material.DIRT;
                            } else {
                                material = Material.STONE;
                            }
                            break;
                        case LARGE_MOUNTAINS:
                            if (y == chunkY) {
                                if (chunkY > 120 && y > (((1.5 * this.terrainType.getYScale()) + 56) * 90 / 100) + new Random().nextInt(3)) {
                                    material = Material.SNOW_BLOCK;
                                } else {
                                    material = Material.GRASS;
                                }
                            } else if (y > chunkY - 4) {
                                if (y > 90) {
                                    material = Material.STONE;
                                } else {
                                    material = Material.DIRT;
                                }
                            } else {
                                material = Material.STONE;
                            }
                            break;
                        default:

                    }
                    setBlock(chunk, x - worldX, y, z - worldZ, (byte) material.getId());
                }
            }
        }
        return chunk;
    }

    private void setBlock(byte[][] chunk, int x, int y, int z, byte id) {
        if (chunk[y >> 4] == null) {
            chunk[y >> 4] = new byte[4096];
        }
        chunk[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = id;
    }
}
