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

public class InfernalExpCompat {

    public static final Supplier<Block> SCONCE_GLOW = RegUtils.regBlock("sconce_glow", () -> new SconceBlock(BlockBehaviour.Properties.copy((BlockBehaviour) ModRegistry.SCONCE.get()), 13, CompatObjects.GLOW_FLAME));

    public static final Supplier<Block> SCONCE_WALL_GLOW = RegUtils.regBlock("sconce_wall_glow", () -> new SconceWallBlock(BlockBehaviour.Properties.copy((BlockBehaviour) ModRegistry.SCONCE.get()).dropsLike((Block) SCONCE_GLOW.get()), CompatObjects.GLOW_FLAME));

    public static final Supplier<Item> SCONCE_ITEM_GLOW = RegUtils.regItem("sconce_glow", () -> new StandingAndWallBlockItem((Block) SCONCE_GLOW.get(), (Block) SCONCE_WALL_GLOW.get(), new Item.Properties(), Direction.DOWN));

    public static void init() {
        ModRegistry.SCONCES.add(SCONCE_ITEM_GLOW);
    }

    public static void setupClient() {
        ClientHelper.registerRenderType((Block) SCONCE_GLOW.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) SCONCE_WALL_GLOW.get(), RenderType.cutout());
    }
}