package net.mehvahdjukaar.supplementaries.common.utils.forge;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.registries.ForgeRegistries;

public class FlowerPotHandlerImpl {

    private static Map<Block, Map<ResourceLocation, Supplier<? extends Block>>> FULL_POTS;

    public static Block getFullPot(FlowerPotBlock emptyPot, Block flowerBlock) {
        return (Block) ((Supplier) ((Map) FULL_POTS.get(emptyPot.getEmptyPot())).getOrDefault(Utils.getID(flowerBlock), (Supplier) () -> Blocks.AIR)).get();
    }

    public static boolean isEmptyPot(Block b) {
        return FULL_POTS != null && b != null && FULL_POTS.containsKey(b);
    }

    public static void setup() {
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(Utils.getID((Item) ModRegistry.FLAX_ITEM.get()), ModRegistry.FLAX_POT);
        Set<FlowerPotBlock> emptyPots = new HashSet();
        for (Block b : ForgeRegistries.BLOCKS) {
            if (b instanceof FlowerPotBlock flowerPotBlock) {
                emptyPots.add(flowerPotBlock.getEmptyPot());
            }
        }
        FULL_POTS = new IdentityHashMap();
        for (FlowerPotBlock pot : emptyPots) {
            FULL_POTS.put(pot, pot.getFullPotsView());
        }
    }

    public static Block getEmptyPot(FlowerPotBlock fullPot) {
        return fullPot.getEmptyPot();
    }
}