package team.lodestar.lodestone.systems.multiblock;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class MultiBlockStructure {

    public final ArrayList<MultiBlockStructure.StructurePiece> structurePieces;

    public MultiBlockStructure(ArrayList<MultiBlockStructure.StructurePiece> structurePieces) {
        this.structurePieces = structurePieces;
    }

    public boolean canPlace(BlockPlaceContext context) {
        return this.structurePieces.stream().allMatch(p -> p.canPlace(context));
    }

    public void place(BlockPlaceContext context) {
        this.structurePieces.forEach(s -> s.place(context.getClickedPos(), context.m_43725_()));
    }

    public static MultiBlockStructure of(MultiBlockStructure.StructurePiece... pieces) {
        return new MultiBlockStructure(new ArrayList(List.of(pieces)));
    }

    public static class StructurePiece {

        public final Vec3i offset;

        public final BlockState state;

        public StructurePiece(int xOffset, int yOffset, int zOffset, BlockState state) {
            this.offset = new Vec3i(xOffset, yOffset, zOffset);
            this.state = state;
        }

        public boolean canPlace(BlockPlaceContext context) {
            Level level = context.m_43725_();
            Player player = context.m_43723_();
            BlockPos pos = context.getClickedPos().offset(this.offset);
            BlockState existingState = context.m_43725_().getBlockState(pos);
            CollisionContext collisioncontext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
            return existingState.m_247087_() && level.m_45752_(this.state, pos, collisioncontext);
        }

        public void place(BlockPos core, Level level) {
            this.place(core, level, this.state);
        }

        public void place(BlockPos core, Level level, BlockState state) {
            BlockPos pos = core.offset(this.offset);
            level.setBlock(pos, state, 3);
            if (level.getBlockEntity(pos) instanceof MultiBlockComponentEntity component) {
                component.corePos = core;
            }
        }
    }
}