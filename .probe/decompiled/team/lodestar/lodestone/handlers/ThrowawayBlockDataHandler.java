package team.lodestar.lodestone.handlers;

import java.util.HashMap;
import java.util.function.Supplier;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.ForgeRegistries;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.systems.block.LodestoneBlockProperties;
import team.lodestar.lodestone.systems.block.LodestoneThrowawayBlockData;
import team.lodestar.lodestone.systems.datagen.LodestoneDatagenBlockData;

public class ThrowawayBlockDataHandler {

    public static HashMap<LodestoneBlockProperties, LodestoneThrowawayBlockData> THROWAWAY_DATA_CACHE = new HashMap();

    public static HashMap<LodestoneBlockProperties, LodestoneDatagenBlockData> DATAGEN_DATA_CACHE = new HashMap();

    public static void wipeCache(InterModEnqueueEvent event) {
        THROWAWAY_DATA_CACHE = null;
        DATAGEN_DATA_CACHE = null;
    }

    public static void setRenderLayers(FMLClientSetupEvent event) {
        DataHelper.getAll(ForgeRegistries.BLOCKS.getValues(), b -> {
            if (b.f_60439_ instanceof LodestoneBlockProperties blockProperties && blockProperties.getThrowawayData().hasCustomRenderType()) {
                return true;
            }
            return false;
        }).forEach(b -> ItemBlockRenderTypes.setRenderLayer(b, (RenderType) ((Supplier) ((LodestoneBlockProperties) b.f_60439_).getThrowawayData().getRenderType().get()).get()));
    }
}