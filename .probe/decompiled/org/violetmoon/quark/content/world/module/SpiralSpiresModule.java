package org.violetmoon.quark.content.world.module;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.material.MapColor;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.world.block.MyaliteCrystalBlock;
import org.violetmoon.quark.content.world.gen.SpiralSpireGenerator;
import org.violetmoon.zeta.advancement.ManualTrigger;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.block.OldMaterials;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.config.type.CompoundBiomeConfig;
import org.violetmoon.zeta.config.type.DimensionConfig;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.entity.ZEntityTeleport;
import org.violetmoon.zeta.event.play.loading.ZGatherHints;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;
import org.violetmoon.zeta.world.WorldGenHandler;

@ZetaLoadModule(category = "world")
public class SpiralSpiresModule extends ZetaModule {

    @Config
    public static DimensionConfig dimensions = DimensionConfig.end(false);

    @Config
    public static CompoundBiomeConfig biomes = CompoundBiomeConfig.fromBiomeReslocs(false, "minecraft:end_highlands");

    @Config
    public static int rarity = 200;

    @Config
    public static int radius = 15;

    @Config(flag = "myalite_viaduct")
    public static boolean enableMyaliteViaducts = true;

    @Config
    @Config.Min(2.0)
    @Config.Max(1024.0)
    public static int myaliteConduitDistance = 24;

    @Config
    public static boolean renewableMyalite = true;

    public static ManualTrigger useViaductTrigger;

    @Hint
    public static Block dusky_myalite;

    public static Block myalite_crystal;

    @LoadEvent
    public final void register(ZRegister event) {
        BlockBehaviour.Properties props = OldMaterials.stone().mapColor(MapColor.TERRACOTTA_PURPLE).requiresCorrectToolForDrops().strength(1.5F, 6.0F);
        dusky_myalite = new ZetaBlock("dusky_myalite", this, props).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS, Blocks.PURPUR_BLOCK, true);
        myalite_crystal = new MyaliteCrystalBlock(this);
        ((IZetaBlock) dusky_myalite).setCreativeTab(CreativeModeTabs.NATURAL_BLOCKS, Blocks.END_STONE, false);
        ((IZetaBlock) myalite_crystal).setCreativeTab(CreativeModeTabs.NATURAL_BLOCKS, Blocks.END_STONE, false);
        useViaductTrigger = event.getAdvancementModifierRegistry().registerManualTrigger("use_viaduct");
    }

    @LoadEvent
    public final void setup(ZCommonSetup event) {
        WorldGenHandler.addGenerator(this, new SpiralSpireGenerator(dimensions), GenerationStep.Decoration.SURFACE_STRUCTURES, 0);
    }

    @PlayEvent
    public void addAdditionalHints(ZGatherHints event) {
        MutableComponent comp = Component.translatable("quark.jei.hint.myalite_crystal_get");
        if (enableMyaliteViaducts) {
            comp = comp.append(" ").append(Component.translatable("quark.jei.hint.myalite_crystal_viaduct"));
        }
        if (renewableMyalite && Quark.ZETA.modules.isEnabled(CorundumModule.class)) {
            comp = comp.append(" ").append(Component.translatable("quark.jei.hint.myalite_crystal_grow"));
        }
        event.accept(myalite_crystal.asItem(), comp);
    }

    @PlayEvent
    public void onTeleport(ZEntityTeleport event) {
        if (enableMyaliteViaducts) {
            Entity entity = event.getEntity();
            Level world = entity.level();
            BlockPos pos = BlockPos.containing(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            if (world.isLoaded(pos)) {
                List<BlockPos> myalite = getAdjacentMyalite(null, world, pos, null);
                if (myalite == null || myalite.isEmpty()) {
                    pos = pos.below();
                    myalite = getAdjacentMyalite(null, world, pos, null);
                }
                if (myalite == null || myalite.isEmpty()) {
                    pos = pos.relative(event.getEntity().getDirection());
                    myalite = getAdjacentMyalite(null, world, pos, null);
                }
                if (myalite != null && !myalite.isEmpty()) {
                    BlockPos cond = pos;
                    List<BlockPos> found = new ArrayList();
                    int moves = 0;
                    BlockPos prev;
                    do {
                        prev = cond;
                        cond = (BlockPos) myalite.get(world.random.nextInt(myalite.size()));
                        found.add(cond);
                        myalite = getAdjacentMyalite(found, world, cond, null);
                        if (myalite == null || ++moves > myaliteConduitDistance) {
                            return;
                        }
                    } while (!myalite.isEmpty());
                    BlockPos test = cond.offset(cond.m_123341_() - prev.m_123341_(), cond.m_123342_() - prev.m_123342_(), cond.m_123343_() - prev.m_123343_());
                    if (!world.getBlockState(test).m_60795_()) {
                        Direction[] sworld = Direction.values();
                        int var12 = sworld.length;
                        int f = 0;
                        while (true) {
                            if (f >= var12) {
                                return;
                            }
                            Direction d = sworld[f];
                            test = cond.relative(d);
                            if (world.getBlockState(test).m_60795_()) {
                                if (d.getAxis() == Direction.Axis.Y) {
                                    test = test.relative(d);
                                }
                                break;
                            }
                            f++;
                        }
                    }
                    event.setTargetX((double) test.m_123341_() + 0.5);
                    event.setTargetY((double) test.m_123342_() + 0.5);
                    event.setTargetZ((double) test.m_123343_() + 0.5);
                    if (event.getEntity() instanceof ServerPlayer sp) {
                        useViaductTrigger.trigger(sp);
                    }
                    if (world instanceof ServerLevel sworld) {
                        for (BlockPos f : found) {
                            sworld.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, myalite_crystal.defaultBlockState()), (double) f.m_123341_() + 0.5, (double) f.m_123342_() + 0.5, (double) f.m_123343_() + 0.5, 30, 0.25, 0.25, 0.25, 0.0);
                        }
                    }
                }
            }
        }
    }

    private static List<BlockPos> getAdjacentMyalite(List<BlockPos> found, Level world, BlockPos pos, Direction ignore) {
        List<BlockPos> ret = new ArrayList(6);
        List<BlockPos> collisions = new ArrayList();
        for (Direction d : Direction.values()) {
            if (d != ignore) {
                BlockPos off = pos.relative(d);
                if (world.getBlockState(off).m_60734_() == myalite_crystal) {
                    if (found != null && found.contains(off)) {
                        collisions.add(off);
                    } else {
                        ret.add(off);
                    }
                }
            }
        }
        return ret.isEmpty() && collisions.size() > 1 ? null : ret;
    }
}