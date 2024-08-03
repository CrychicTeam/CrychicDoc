package net.raphimc.immediatelyfast.injection.mixins.map_atlas_generation;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.raphimc.immediatelyfast.feature.map_atlas_generation.MapAtlasTexture;
import net.raphimc.immediatelyfast.injection.interfaces.IMapRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ MapRenderer.class })
public abstract class MixinMapRenderer implements IMapRenderer {

    @Unique
    private final Int2ObjectMap<MapAtlasTexture> immediatelyFast$mapAtlasTextures = new Int2ObjectOpenHashMap();

    @Unique
    private final Int2IntMap immediatelyFast$mapIdToAtlasMapping = new Int2IntOpenHashMap();

    @Inject(method = { "clearStateTextures" }, at = { @At("RETURN") })
    private void clearMapAtlasTextures(CallbackInfo ci) {
        ObjectIterator var2 = this.immediatelyFast$mapAtlasTextures.values().iterator();
        while (var2.hasNext()) {
            MapAtlasTexture texture = (MapAtlasTexture) var2.next();
            texture.close();
        }
        this.immediatelyFast$mapAtlasTextures.clear();
        this.immediatelyFast$mapIdToAtlasMapping.clear();
    }

    @Inject(method = { "getMapTexture" }, at = { @At("HEAD") })
    private void createMapAtlasTexture(int id, MapItemSavedData state, CallbackInfoReturnable<MapRenderer.MapInstance> cir) {
        this.immediatelyFast$mapIdToAtlasMapping.computeIfAbsent(id, k -> {
            ObjectIterator atlasTexture = this.immediatelyFast$mapAtlasTextures.values().iterator();
            while (atlasTexture.hasNext()) {
                MapAtlasTexture atlasTexturex = (MapAtlasTexture) atlasTexture.next();
                int location = atlasTexturex.getNextMapLocation();
                if (location != -1) {
                    return location;
                }
            }
            MapAtlasTexture atlasTexturex = new MapAtlasTexture(this.immediatelyFast$mapAtlasTextures.size());
            this.immediatelyFast$mapAtlasTextures.put(atlasTexturex.getId(), atlasTexturex);
            return atlasTexturex.getNextMapLocation();
        });
    }

    @Override
    public MapAtlasTexture immediatelyFast$getMapAtlasTexture(int id) {
        return (MapAtlasTexture) this.immediatelyFast$mapAtlasTextures.get(id);
    }

    @Override
    public int immediatelyFast$getAtlasMapping(int mapId) {
        return this.immediatelyFast$mapIdToAtlasMapping.getOrDefault(mapId, -1);
    }
}