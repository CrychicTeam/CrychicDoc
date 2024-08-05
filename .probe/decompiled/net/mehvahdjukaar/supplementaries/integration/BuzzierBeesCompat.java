package net.mehvahdjukaar.supplementaries.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CandleHolderBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BuzzierBeesCompat {

    private static final List<Supplier<? extends Block>> SOUL_CANDLE_HOLDERS = new ArrayList();

    public static void registerCandle(ResourceLocation id) {
        String name = id.getPath() + "_soul";
        RegSupplier<CandleHolderBlock> b = RegHelper.registerBlockWithItem(new ResourceLocation(id.getNamespace(), name), () -> new CandleHolderBlock(null, BlockBehaviour.Properties.copy((BlockBehaviour) ModRegistry.SCONCE.get()), CompatObjects.SMALL_SOUL_FLAME));
        SOUL_CANDLE_HOLDERS.add(b);
    }

    public static void setupClient() {
        SOUL_CANDLE_HOLDERS.forEach(b -> ClientHelper.registerRenderType((Block) b.get(), RenderType.cutout()));
    }
}