package net.mehvahdjukaar.supplementaries.integration;

import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SconceBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SconceWallBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.RegUtils;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ArchitectsPalCompat {

    public static final Supplier<Block> SCONCE_NETHER_BRASS = RegUtils.regBlock("sconce_nether_brass", () -> new SconceBlock(BlockBehaviour.Properties.copy((BlockBehaviour) ModRegistry.SCONCE.get()), 14, CompatObjects.NETHER_BRASS_FLAME));

    public static final Supplier<Block> SCONCE_WALL_NETHER_BRASS = RegUtils.regBlock("sconce_wall_nether_brass", () -> new SconceWallBlock(BlockBehaviour.Properties.copy((BlockBehaviour) ModRegistry.SCONCE.get()).dropsLike((Block) SCONCE_NETHER_BRASS.get()), CompatObjects.NETHER_BRASS_FLAME));

    public static final Supplier<Item> SCONCE_ITEM_NETHER_BRASS = RegUtils.regItem("sconce_nether_brass", () -> new StandingAndWallBlockItem((Block) SCONCE_NETHER_BRASS.get(), (Block) SCONCE_WALL_NETHER_BRASS.get(), new Item.Properties(), Direction.DOWN));

    public static void init() {
        ModRegistry.SCONCES.add(SCONCE_ITEM_NETHER_BRASS);
    }

    public static void setupClient() {
        ClientHelper.registerRenderType((Block) SCONCE_NETHER_BRASS.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) SCONCE_WALL_NETHER_BRASS.get(), RenderType.cutout());
    }
}