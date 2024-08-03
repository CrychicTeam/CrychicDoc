package dev.latvian.mods.kubejs.block.predicate;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEntityPredicate implements BlockPredicate {

    private final ResourceLocation id;

    private BlockEntityPredicateDataCheck checkData;

    public BlockEntityPredicate(ResourceLocation i) {
        this.id = i;
    }

    public BlockEntityPredicate data(BlockEntityPredicateDataCheck cd) {
        this.checkData = cd;
        return this;
    }

    @Override
    public boolean check(BlockContainerJS block) {
        BlockEntity tileEntity = block.getEntity();
        return tileEntity != null && this.id.equals(RegistryInfo.BLOCK_ENTITY_TYPE.getId(tileEntity.getType())) && (this.checkData == null || this.checkData.checkData(block.getEntityData()));
    }

    public String toString() {
        return "{entity=" + this.id + "}";
    }
}