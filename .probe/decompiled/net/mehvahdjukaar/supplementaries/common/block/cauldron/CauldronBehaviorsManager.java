package net.mehvahdjukaar.supplementaries.common.block.cauldron;

import java.util.Optional;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.map.MapHelper;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;

public class CauldronBehaviorsManager {

    private static final CauldronInteraction MAP_INTERACTION = (state, level, pos, player, hand, stack) -> {
        if (MapHelper.removeAllCustomMarkers(level, stack, player)) {
            LayeredCauldronBlock.lowerFillLevel(state, level, pos);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    };

    public static void registerBehaviors() {
        for (Supplier<Block> item : ModRegistry.FLAGS.values()) {
            CauldronInteraction.WATER.put(((Block) item.get()).asItem(), CauldronInteraction.BANNER);
        }
        CauldronInteraction.WATER.put((Item) ModRegistry.QUIVER_ITEM.get(), CauldronInteraction.DYED_ITEM);
        Optional<Item> atlas = BuiltInRegistries.ITEM.m_6612_(new ResourceLocation("map_atlases:atlas"));
        atlas.ifPresent(itemx -> CauldronInteraction.WATER.put(itemx, MAP_INTERACTION));
        CauldronInteraction.WATER.put(Items.FILLED_MAP, MAP_INTERACTION);
    }
}