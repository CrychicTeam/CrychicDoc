package org.violetmoon.quark.content.building.module;

import java.util.function.ToIntFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.building.block.SoulFurnaceBlock;
import org.violetmoon.quark.content.building.block.VariantFurnaceBlock;
import org.violetmoon.quark.content.building.block.be.VariantFurnaceBlockEntity;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "building")
public class VariantFurnacesModule extends ZetaModule {

    public static BlockEntityType<VariantFurnaceBlockEntity> blockEntityType;

    public static Block deepslateFurnace;

    @Hint
    public static Block blackstoneFurnace;

    @LoadEvent
    public final void register(ZRegister event) {
        deepslateFurnace = new VariantFurnaceBlock("deepslate", this, BlockBehaviour.Properties.copy(Blocks.DEEPSLATE).lightLevel(litBlockEmission(13)));
        blackstoneFurnace = new SoulFurnaceBlock("blackstone", this, BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).lightLevel(litBlockEmission(13)));
        blockEntityType = BlockEntityType.Builder.<VariantFurnaceBlockEntity>of(VariantFurnaceBlockEntity::new, deepslateFurnace, blackstoneFurnace).build(null);
        Quark.ZETA.registry.register(blockEntityType, "variant_furnace", Registries.BLOCK_ENTITY_TYPE);
    }

    private static ToIntFunction<BlockState> litBlockEmission(int lvl) {
        return s -> s.m_61143_(BlockStateProperties.LIT) ? lvl : 0;
    }
}