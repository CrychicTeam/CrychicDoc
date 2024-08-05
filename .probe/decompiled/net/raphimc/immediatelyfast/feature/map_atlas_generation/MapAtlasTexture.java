package net.raphimc.immediatelyfast.feature.map_atlas_generation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

public class MapAtlasTexture implements AutoCloseable {

    public static final int ATLAS_SIZE = 4096;

    public static final int MAP_SIZE = 128;

    public static final int MAPS_PER_ATLAS = 1024;

    private final int id;

    private final ResourceLocation identifier;

    private final DynamicTexture texture;

    private int mapCount;

    public MapAtlasTexture(int id) {
        this.id = id;
        this.identifier = new ResourceLocation("immediatelyfast", "map_atlas/" + id);
        this.texture = new DynamicTexture(4096, 4096, true);
        Minecraft.getInstance().getTextureManager().register(this.identifier, this.texture);
    }

    public int getNextMapLocation() {
        if (this.mapCount >= 1024) {
            return -1;
        } else {
            byte atlasX = (byte) (this.mapCount % 32);
            byte atlasY = (byte) (this.mapCount / 32);
            this.mapCount++;
            return this.id << 16 | atlasX << 8 | atlasY;
        }
    }

    public int getId() {
        return this.id;
    }

    public ResourceLocation getIdentifier() {
        return this.identifier;
    }

    public DynamicTexture getTexture() {
        return this.texture;
    }

    public void close() {
        this.texture.close();
        Minecraft.getInstance().getTextureManager().release(this.identifier);
    }
}