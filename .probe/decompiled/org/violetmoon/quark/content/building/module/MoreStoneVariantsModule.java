package org.violetmoon.quark.content.building.module;

import java.util.function.BooleanSupplier;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.violetmoon.quark.content.building.block.MyalitePillarBlock;
import org.violetmoon.quark.content.world.block.MyaliteBlock;
import org.violetmoon.quark.content.world.module.NewStoneTypesModule;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.block.ZetaBlockWrapper;
import org.violetmoon.zeta.block.ZetaPillarBlock;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.config.ConfigFlagManager;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.loading.ZGatherAdditionalFlags;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.BooleanSuppliers;

@ZetaLoadModule(category = "building", loadPhase = 10)
public class MoreStoneVariantsModule extends ZetaModule {

    @Config(flag = "stone_bricks")
    public boolean enableBricks = true;

    @Config(flag = "stone_chiseled")
    public boolean enableChiseledBricks = true;

    @Config(flag = "stone_pillar")
    public boolean enablePillar = true;

    public static MoreStoneVariantsModule instance;

    @LoadEvent
    public final void register(ZRegister event) {
        Block polishedCalcite = this.expandVanillaStone(event, this, Blocks.CALCITE, "calcite");
        Block polishedDripstone = this.expandVanillaStone(event, this, Blocks.DRIPSTONE_BLOCK, "dripstone");
        Block polishedTuff = this.expandVanillaStone(event, this, Blocks.TUFF, "tuff");
        this.add(event, "granite", MapColor.DIRT, SoundType.STONE, Blocks.POLISHED_GRANITE, BooleanSuppliers.TRUE);
        this.add(event, "diorite", MapColor.QUARTZ, SoundType.STONE, Blocks.POLISHED_DIORITE, BooleanSuppliers.TRUE);
        this.add(event, "andesite", MapColor.STONE, SoundType.STONE, Blocks.POLISHED_ANDESITE, BooleanSuppliers.TRUE);
        this.add(event, "calcite", MapColor.TERRACOTTA_WHITE, SoundType.CALCITE, polishedCalcite, BooleanSuppliers.TRUE);
        this.add(event, "dripstone", MapColor.TERRACOTTA_BROWN, SoundType.DRIPSTONE_BLOCK, polishedDripstone, BooleanSuppliers.TRUE);
        this.add(event, "tuff", MapColor.TERRACOTTA_GRAY, SoundType.TUFF, polishedTuff, BooleanSuppliers.TRUE);
        this.add(event, "limestone", MapColor.STONE, SoundType.STONE, (Block) NewStoneTypesModule.polishedBlocks.get(NewStoneTypesModule.limestoneBlock), () -> NewStoneTypesModule.enableLimestone);
        this.add(event, "jasper", MapColor.TERRACOTTA_RED, SoundType.STONE, (Block) NewStoneTypesModule.polishedBlocks.get(NewStoneTypesModule.jasperBlock), () -> NewStoneTypesModule.enableJasper);
        this.add(event, "shale", MapColor.ICE, SoundType.STONE, (Block) NewStoneTypesModule.polishedBlocks.get(NewStoneTypesModule.shaleBlock), () -> NewStoneTypesModule.enableShale);
        this.add(event, "myalite", MapColor.COLOR_PURPLE, SoundType.STONE, (Block) NewStoneTypesModule.polishedBlocks.get(NewStoneTypesModule.myaliteBlock), () -> NewStoneTypesModule.enableMyalite, MyaliteBlock::new, MyalitePillarBlock::new);
        instance = this;
    }

    @PlayEvent
    public final void moreFlags(ZGatherAdditionalFlags event) {
        ConfigFlagManager manager = event.flagManager();
        manager.putFlag(this, "granite", true);
        manager.putFlag(this, "diorite", true);
        manager.putFlag(this, "andesite", true);
        manager.putFlag(this, "calcite", true);
        manager.putFlag(this, "dripstone", true);
        manager.putFlag(this, "tuff", true);
    }

    public Block expandVanillaStone(ZRegister event, ZetaModule module, Block raw, String name) {
        ZetaBlockWrapper wrap = new ZetaBlockWrapper(raw, this);
        CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.BUILDING_BLOCKS, wrap, Blocks.DEEPSLATE, true);
        return NewStoneTypesModule.makeStone(event, module, raw, name, null, null, BooleanSuppliers.TRUE, null, ZetaBlock::new);
    }

    private void add(ZRegister event, String name, MapColor color, SoundType sound, Block basePolished, BooleanSupplier cond) {
        this.add(event, name, color, sound, basePolished, cond, ZetaBlock::new, ZetaPillarBlock::new);
    }

    private void add(ZRegister event, String name, MapColor color, SoundType sound, Block basePolished, BooleanSupplier cond, ZetaBlock.Constructor<ZetaBlock> constr, ZetaBlock.Constructor<ZetaPillarBlock> pillarConstr) {
        BlockBehaviour.Properties props = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM).mapColor(color).sound(sound).strength(1.5F, 6.0F);
        CreativeTabManager.daisyChain();
        ZetaBlock bricks = (ZetaBlock) constr.make(name + "_bricks", this, props).setCondition(() -> cond.getAsBoolean() && this.enableBricks).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS, basePolished, false);
        constr.make("chiseled_" + name + "_bricks", this, props).setCondition(() -> cond.getAsBoolean() && this.enableBricks && this.enableChiseledBricks).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS);
        pillarConstr.make(name + "_pillar", this, props).setCondition(() -> cond.getAsBoolean() && this.enablePillar).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS);
        event.getVariantRegistry().addSlabStairsWall(bricks, null);
        CreativeTabManager.endDaisyChain();
    }
}