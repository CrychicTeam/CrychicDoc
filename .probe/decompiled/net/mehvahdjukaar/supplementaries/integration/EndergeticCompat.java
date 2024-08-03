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

public class EndergeticCompat {

    public static final Supplier<Block> SCONCE_ENDER = RegUtils.regBlock("sconce_ender", () -> new SconceBlock(BlockBehaviour.Properties.copy((BlockBehaviour) ModRegistry.SCONCE.get()), 13, CompatObjects.ENDER_FLAME));

    public static final Supplier<Block> SCONCE_WALL_ENDER = RegUtils.regBlock("sconce_wall_ender", () -> new SconceWallBlock(BlockBehaviour.Properties.copy((BlockBehaviour) SCONCE_ENDER.get()).dropsLike((Block) SCONCE_ENDER.get()), CompatObjects.ENDER_FLAME));

    public static final Supplier<Item> SCONCE_ITEM_ENDER = RegUtils.regItem("sconce_ender", () -> new StandingAndWallBlockItem((Block) SCONCE_ENDER.get(), (Block) SCONCE_WALL_ENDER.get(), new Item.Properties(), Direction.DOWN));

    public static void init() {
        ModRegistry.SCONCES.add(SCONCE_ITEM_ENDER);
    }

    public static void setupClient() {
        ClientHelper.registerRenderType((Block) SCONCE_ENDER.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) SCONCE_WALL_ENDER.get(), RenderType.cutout());
    }
}