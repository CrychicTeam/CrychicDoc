package dev.latvian.mods.kubejs.block.entity;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityBuilder extends BuilderBase<BlockEntityType<?>> {

    public BlockEntityInfo info;

    public BlockEntityBuilder(ResourceLocation i, BlockEntityInfo info) {
        super(i);
        this.info = info;
    }

    @Override
    public RegistryInfo getRegistryType() {
        return RegistryInfo.BLOCK_ENTITY_TYPE;
    }

    public BlockEntityType<?> createObject() {
        this.info.entityType = BlockEntityType.Builder.of(this.info::createBlockEntity, this.info.blockBuilder.get()).build(null);
        return this.info.entityType;
    }
}