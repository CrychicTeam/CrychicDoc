package team.lodestar.lodestone.systems.multiblock;

import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IMultiBlockCore {

    ArrayList<BlockPos> getComponentPositions();

    @Nullable
    MultiBlockStructure getStructure();

    default void setupMultiblock(BlockPos pos) {
        if (this.getStructure() != null) {
            this.getStructure().structurePieces.forEach(p -> {
                Vec3i offset = p.offset;
                this.getComponentPositions().add(pos.offset(offset));
            });
        }
    }

    default boolean isModular() {
        return false;
    }

    default void destroyMultiblock(@Nullable Player player, Level level, BlockPos pos) {
        if (!this.isModular()) {
            this.getComponentPositions().forEach(p -> {
                if (level.getBlockEntity(p) instanceof MultiBlockComponentEntity) {
                    level.m_46961_(p, false);
                }
            });
            boolean dropBlock = player == null || !player.isCreative();
            if (level.getBlockEntity(pos) instanceof MultiBlockCoreEntity) {
                level.m_46961_(pos, dropBlock);
            }
        }
    }
}