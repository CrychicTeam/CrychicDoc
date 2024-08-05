package dev.xkmc.l2backpack.content.drawer;

import dev.xkmc.l2library.base.tile.BaseBlockEntity;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public abstract class IDrawerBlockEntity extends BaseBlockEntity {

    public IDrawerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract Item getItem();
}