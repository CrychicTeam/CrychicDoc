package org.violetmoon.quark.integration.lootr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.module.ZetaModule;

public class LootrIntegration implements ILootrIntegration {

    public BlockEntityType<LootrVariantChestBlockEntity> chestTEType;

    public BlockEntityType<LootrVariantTrappedChestBlockEntity> trappedChestTEType;

    public final Map<Block, Block> chestMappings = new HashMap();

    public final List<Block> lootrRegularChests = new ArrayList();

    public final List<Block> lootrTrappedChests = new ArrayList();

    @Override
    public BlockEntityType<? extends ChestBlockEntity> chestTE() {
        return this.chestTEType;
    }

    @Override
    public BlockEntityType<? extends ChestBlockEntity> trappedChestTE() {
        return this.trappedChestTEType;
    }

    @Override
    public void makeChestBlocks(ZetaModule module, String name, Block base, BooleanSupplier condition, Block quarkRegularChest, Block quarkTrappedChest) {
        Block lootrRegularChest = new LootrVariantChestBlock(name, module, () -> this.chestTEType, BlockBehaviour.Properties.copy(base)).setCondition(condition);
        this.lootrRegularChests.add(lootrRegularChest);
        Block lootrTrappedChest = new LootrVariantTrappedChestBlock(name, module, () -> this.trappedChestTEType, BlockBehaviour.Properties.copy(base)).setCondition(condition);
        this.lootrTrappedChests.add(lootrTrappedChest);
        this.chestMappings.put(quarkRegularChest, lootrRegularChest);
        this.chestMappings.put(quarkTrappedChest, lootrTrappedChest);
    }

    @Nullable
    @Override
    public Block lootrVariant(Block base) {
        return (Block) this.chestMappings.get(base);
    }

    @Override
    public void postRegister() {
        this.chestTEType = BlockEntityType.Builder.<LootrVariantChestBlockEntity>of(LootrVariantChestBlockEntity::new, (Block[]) this.lootrRegularChests.toArray(new Block[0])).build(null);
        this.trappedChestTEType = BlockEntityType.Builder.<LootrVariantTrappedChestBlockEntity>of(LootrVariantTrappedChestBlockEntity::new, (Block[]) this.lootrTrappedChests.toArray(new Block[0])).build(null);
        Quark.ZETA.registry.register(this.chestTEType, "lootr_variant_chest", Registries.BLOCK_ENTITY_TYPE);
        Quark.ZETA.registry.register(this.trappedChestTEType, "lootr_variant_trapped_chest", Registries.BLOCK_ENTITY_TYPE);
    }
}