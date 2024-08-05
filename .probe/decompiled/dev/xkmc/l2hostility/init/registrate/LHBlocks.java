package dev.xkmc.l2hostility.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.l2hostility.content.item.beacon.HostilityBeaconBlock;
import dev.xkmc.l2hostility.content.item.beacon.HostilityBeaconBlockEntity;
import dev.xkmc.l2hostility.content.item.beacon.HostilityBeaconMenu;
import dev.xkmc.l2hostility.content.item.beacon.HostilityBeaconRenderer;
import dev.xkmc.l2hostility.content.item.beacon.HostilityBeaconScreen;
import dev.xkmc.l2hostility.content.item.spawner.BurstSpawnerBlockEntity;
import dev.xkmc.l2hostility.content.item.spawner.TraitSpawnerBlock;
import dev.xkmc.l2hostility.content.traits.goals.EnderTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHTagGen;
import dev.xkmc.l2modularblock.DelegateBlock;
import dev.xkmc.l2modularblock.type.BlockMethod;
import java.util.Locale;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;

public class LHBlocks {

    public static final RegistryEntry<CreativeModeTab> TAB = L2Hostility.REGISTRATE.buildL2CreativeTab("hostility", "L2Hostility", e -> e.icon(() -> ((EnderTrait) LHTraits.ENDER.get()).m_5456_().getDefaultInstance()));

    public static final BlockEntry<Block> CHAOS = L2Hostility.REGISTRATE.block("chaos_block", p -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK))).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL, LHTagGen.BEACON_BLOCK }).simpleItem().register();

    public static final BlockEntry<Block> MIRACLE = L2Hostility.REGISTRATE.block("miracle_block", p -> new Block(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK))).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL, LHTagGen.BEACON_BLOCK }).simpleItem().register();

    public static final BlockEntry<DelegateBlock> BURST_SPAWNER = L2Hostility.REGISTRATE.block("hostility_spawner", p -> DelegateBlock.newBaseBlock(BlockBehaviour.Properties.copy(Blocks.SPAWNER).strength(50.0F, 1200.0F), new BlockMethod[] { TraitSpawnerBlock.BASE, TraitSpawnerBlock.CLICK, TraitSpawnerBlock.BURST, TraitSpawnerBlock.BE_BURST })).blockstate((ctx, pvd) -> pvd.getVariantBuilder((Block) ctx.get()).forAllStates(state -> {
        TraitSpawnerBlock.State s = (TraitSpawnerBlock.State) state.m_61143_(TraitSpawnerBlock.STATE);
        String name = s == TraitSpawnerBlock.State.IDLE ? ctx.getName() : ctx.getName() + "_" + s.name().toLowerCase(Locale.ROOT);
        BlockModelBuilder ans = pvd.models().cubeAll(name, pvd.modLoc("block/" + name)).renderType("cutout");
        return ConfiguredModel.builder().modelFile(ans).build();
    })).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE }).simpleItem().register();

    public static final BlockEntityEntry<BurstSpawnerBlockEntity> BE_BURST = L2Hostility.REGISTRATE.blockEntity("hostility_spawner", BurstSpawnerBlockEntity::new).validBlocks(new NonNullSupplier[] { BURST_SPAWNER }).register();

    public static final BlockEntry<HostilityBeaconBlock> HOSTILITY_BEACON = L2Hostility.REGISTRATE.block("hostility_beacon", p -> new HostilityBeaconBlock(BlockBehaviour.Properties.copy(Blocks.BEACON))).blockstate(HostilityBeaconBlock::buildModel).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_DIAMOND_TOOL }).simpleItem().register();

    public static final BlockEntityEntry<HostilityBeaconBlockEntity> BE_BEACON = L2Hostility.REGISTRATE.blockEntity("hostility_beacon", HostilityBeaconBlockEntity::new).renderer(() -> HostilityBeaconRenderer::new).validBlocks(new NonNullSupplier[] { HOSTILITY_BEACON }).register();

    public static final MenuEntry<HostilityBeaconMenu> MT_BEACON = L2Hostility.REGISTRATE.menu("hostility_beacon", HostilityBeaconMenu::new, () -> HostilityBeaconScreen::new).register();

    public static void register() {
    }
}