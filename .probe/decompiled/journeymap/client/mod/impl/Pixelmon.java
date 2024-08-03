package journeymap.client.mod.impl;

import journeymap.client.mod.IModBlockHandler;
import journeymap.client.model.BlockMD;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class Pixelmon implements IModBlockHandler {

    public static Pixelmon INSTANCE;

    public static boolean loaded = false;

    public Pixelmon() {
        INSTANCE = new Pixelmon();
        loaded = true;
    }

    public ResourceLocation getPixelmonResource(Entity entity) {
        if (isInstanceOf(entity, "com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon")) {
            try {
                ResourceLocation pixelmonSprite = (ResourceLocation) entity.getClass().getMethod("getSprite").invoke(entity);
                if (pixelmonSprite != null) {
                    return pixelmonSprite;
                }
                return null;
            } catch (Exception var3) {
                Journeymap.getLogger().error(String.format("Error getting pixelmon sprite from %s:", LogFormatter.toPartialString(var3)));
            }
        }
        return null;
    }

    private static boolean isInstanceOf(Object pokemonEntity, String... classPaths) {
        for (String classPath : classPaths) {
            try {
                Class matchedClass = Class.forName(classPath);
                if (matchedClass.isInstance(pokemonEntity)) {
                    return true;
                }
            } catch (Exception var8) {
                return false;
            }
        }
        return false;
    }

    @Override
    public void initialize(BlockMD blockMD) {
    }
}