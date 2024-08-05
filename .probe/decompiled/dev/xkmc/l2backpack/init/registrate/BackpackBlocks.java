package dev.xkmc.l2backpack.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.l2backpack.content.drawer.DrawerBlock;
import dev.xkmc.l2backpack.content.drawer.DrawerBlockEntity;
import dev.xkmc.l2backpack.content.remote.common.EnderParticleBlock;
import dev.xkmc.l2backpack.content.remote.drawer.EnderDrawerBlock;
import dev.xkmc.l2backpack.content.remote.drawer.EnderDrawerBlockEntity;
import dev.xkmc.l2backpack.content.remote.worldchest.WorldChestBlock;
import dev.xkmc.l2backpack.content.remote.worldchest.WorldChestBlockEntity;
import dev.xkmc.l2backpack.content.render.DrawerRenderer;
import dev.xkmc.l2backpack.init.L2Backpack;
import dev.xkmc.l2modularblock.BlockProxy;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.type.BlockMethod;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BackpackBlocks {

    public static final BlockEntry<DelegateBlock> WORLD_CHEST = L2Backpack.REGISTRATE.block("dimensional_storage", p -> DelegateBlock.newBaseBlock(BlockBehaviour.Properties.copy(Blocks.ENDER_CHEST), new BlockMethod[] { BlockProxy.HORIZONTAL, WorldChestBlock.INSTANCE, EnderParticleBlock.INSTANCE, WorldChestBlock.TILE_ENTITY_SUPPLIER_BUILDER })).blockstate((ctx, pvd) -> pvd.horizontalBlock((Block) ctx.getEntry(), state -> pvd.models().withExistingParent(ctx.getName() + "_" + ((DyeColor) state.m_61143_(WorldChestBlock.COLOR)).getName(), pvd.modLoc("backpack")).texture("0", "block/dimensional_storage/" + ((DyeColor) state.m_61143_(WorldChestBlock.COLOR)).getName()))).loot((table, block) -> table.m_246125_(block, Blocks.ENDER_CHEST)).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL }).defaultLang().register();

    public static final BlockEntityEntry<WorldChestBlockEntity> TE_WORLD_CHEST = L2Backpack.REGISTRATE.blockEntity("dimensional_storage", WorldChestBlockEntity::new).validBlock(WORLD_CHEST).register();

    public static final BlockEntry<DelegateBlock> ENDER_DRAWER = L2Backpack.REGISTRATE.block("ender_drawer", p -> DelegateBlock.newBaseBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).requiresCorrectToolForDrops().strength(22.5F, 600.0F).lightLevel(state -> 15), new BlockMethod[] { BlockProxy.HORIZONTAL, EnderDrawerBlock.INSTANCE, EnderParticleBlock.INSTANCE, EnderDrawerBlock.BLOK_ENTITY })).blockstate((ctx, pvd) -> pvd.horizontalBlock((Block) ctx.getEntry(), state -> pvd.models().withExistingParent(ctx.getName(), pvd.modLoc("block/drawer_base")).texture("0", "block/drawer/ender_bottom").texture("1", "block/drawer/ender_front").texture("2", "block/drawer/ender_side").texture("3", "block/drawer/ender_top").renderType("cutout"))).loot((table, block) -> table.m_246125_(block, Blocks.ENDER_CHEST)).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL }).defaultLang().register();

    public static final BlockEntityEntry<EnderDrawerBlockEntity> TE_ENDER_DRAWER = L2Backpack.REGISTRATE.blockEntity("ender_drawer", EnderDrawerBlockEntity::new).validBlock(ENDER_DRAWER).renderer(() -> DrawerRenderer::new).register();

    public static final BlockEntry<DelegateBlock> DRAWER = L2Backpack.REGISTRATE.block("drawer", p -> DelegateBlock.newBaseBlock(BlockBehaviour.Properties.copy(Blocks.GLASS).requiresCorrectToolForDrops().lightLevel(state -> 15), new BlockMethod[] { BlockProxy.HORIZONTAL, DrawerBlock.INSTANCE, DrawerBlock.BLOCK_ENTITY })).blockstate((ctx, pvd) -> pvd.horizontalBlock((Block) ctx.getEntry(), state -> pvd.models().withExistingParent(ctx.getName(), pvd.modLoc("block/drawer_base")).texture("0", "block/drawer/drawer_bottom").texture("1", "block/drawer/drawer_front").texture("2", "block/drawer/drawer_side").texture("3", "block/drawer/drawer_top").renderType("cutout"))).loot((table, block) -> table.m_246125_(block, Blocks.GLASS)).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE }).defaultLang().register();

    public static final BlockEntityEntry<DrawerBlockEntity> TE_DRAWER = L2Backpack.REGISTRATE.blockEntity("drawer", DrawerBlockEntity::new).validBlock(DRAWER).renderer(() -> DrawerRenderer::new).register();

    public static void register() {
    }
}