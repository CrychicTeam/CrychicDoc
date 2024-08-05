package yesman.epicfight.world.level.block.entity;

import com.mojang.datafixers.types.Type;
import java.util.Set;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class UniversalBlockEntityType<T extends BlockEntity> extends BlockEntityType<T> {

    public UniversalBlockEntityType(BlockEntityType.BlockEntitySupplier<T> blockEntityTypeBlockEntitySupplierT0, Set<Block> setBlock1, Type<?> type2) {
        super(blockEntityTypeBlockEntitySupplierT0, setBlock1, type2);
    }

    @Override
    public boolean isValid(BlockState blockState) {
        return true;
    }
}