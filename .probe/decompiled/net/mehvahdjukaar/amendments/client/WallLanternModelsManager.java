package net.mehvahdjukaar.amendments.client;

import com.google.gson.JsonElement;
import java.io.InputStream;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import net.mehvahdjukaar.amendments.Amendments;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WallLanternModelsManager {

    private static final Map<Block, ResourceLocation> SPECIAL_MOUNT_TEXTURES = new IdentityHashMap();

    private static final Map<Block, ResourceLocation> SPECIAL_LANTERN_MODELS = new IdentityHashMap();

    public static void refreshModels(ResourceManager manager) {
        reloadTextures(manager);
        reloadModels(manager);
    }

    private static void reloadModels(ResourceManager manager) {
        SPECIAL_LANTERN_MODELS.clear();
        for (Block l : BlockScanner.getLanterns()) {
            ResourceLocation reg = Utils.getID(l);
            String namespace = !reg.getNamespace().equals("minecraft") && !reg.getNamespace().equals("amendments") ? reg.getNamespace() + "/" : "";
            String s = "block/custom_wall_lanterns/" + namespace + reg.getPath();
            ResourceLocation fullPath = Amendments.res("models/" + s + ".json");
            Optional<Resource> resource = manager.m_213713_(fullPath);
            if (resource.isPresent()) {
                SPECIAL_LANTERN_MODELS.put(l, Amendments.res(s));
            }
        }
    }

    private static void reloadTextures(ResourceManager manager) {
        SPECIAL_MOUNT_TEXTURES.clear();
        for (Block l : BlockScanner.getLanterns()) {
            ResourceLocation reg = Utils.getID(l);
            String namespace = !reg.getNamespace().equals("minecraft") && !reg.getNamespace().equals("amendments") ? reg.getNamespace() + "/" : "";
            String s = "textures/block/wall_lanterns/" + namespace + reg.getPath() + ".json";
            ResourceLocation fullPath = Amendments.res(s);
            Optional<Resource> resource = manager.m_213713_(fullPath);
            if (resource.isPresent()) {
                try {
                    InputStream stream = ((Resource) resource.get()).open();
                    try {
                        JsonElement bsElement = RPUtils.deserializeJson(stream);
                        String texture = RPUtils.findFirstResourceInJsonRecursive(bsElement);
                        if (!texture.isEmpty()) {
                            SPECIAL_MOUNT_TEXTURES.put(l, new ResourceLocation(texture));
                        }
                    } catch (Throwable var12) {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (Throwable var11) {
                                var12.addSuppressed(var11);
                            }
                        }
                        throw var12;
                    }
                    if (stream != null) {
                        stream.close();
                    }
                } catch (Exception var13) {
                }
            }
        }
    }

    @Nullable
    public static TextureAtlasSprite getTexture(Block block) {
        ResourceLocation res = (ResourceLocation) SPECIAL_MOUNT_TEXTURES.get(block);
        return res == null ? null : (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(res);
    }

    public static void registerSpecialModels(ClientHelper.SpecialModelEvent event) {
        SPECIAL_LANTERN_MODELS.values().forEach(event::register);
    }

    public static BakedModel getModel(BlockModelShaper blockModelShaper, BlockState lantern) {
        ResourceLocation special = (ResourceLocation) SPECIAL_LANTERN_MODELS.get(lantern.m_60734_());
        return special != null ? ClientHelper.getModel(Minecraft.getInstance().getModelManager(), special) : blockModelShaper.getBlockModel(lantern);
    }
}