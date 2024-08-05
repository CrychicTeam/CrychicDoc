package team.lodestar.lodestone.systems.multiblock;

import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public abstract class MultiBlockCoreEntity extends LodestoneBlockEntity implements IMultiBlockCore {

    ArrayList<BlockPos> componentPositions = new ArrayList();

    public final MultiBlockStructure structure;

    public MultiBlockCoreEntity(BlockEntityType<?> type, MultiBlockStructure structure, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.structure = structure;
        this.setupMultiblock(pos);
    }

    @Override
    public MultiBlockStructure getStructure() {
        return this.structure;
    }

    @Override
    public ArrayList<BlockPos> getComponentPositions() {
        return this.componentPositions;
    }

    @Override
    public void onBreak(@Nullable Player player) {
        this.destroyMultiblock(player, this.f_58857_, this.f_58858_);
    }
}