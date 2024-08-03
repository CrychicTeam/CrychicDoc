package noppes.npcs.blocks.tiles;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileNpcEntity extends BlockEntity {

    public Map<String, Object> tempData = new HashMap();

    public TileNpcEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        CompoundTag extraData = compound.getCompound("ExtraData");
        if (!extraData.isEmpty()) {
            this.getPersistentData().put("CustomNPCsData", extraData);
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
    }
}