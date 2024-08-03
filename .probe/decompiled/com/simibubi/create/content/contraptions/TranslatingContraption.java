package com.simibubi.create.content.contraptions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public abstract class TranslatingContraption extends Contraption {

    protected Set<BlockPos> cachedColliders;

    protected Direction cachedColliderDirection;

    public Set<BlockPos> getOrCreateColliders(Level world, Direction movementDirection) {
        if (this.getBlocks() == null) {
            return Collections.emptySet();
        } else {
            if (this.cachedColliders == null || this.cachedColliderDirection != movementDirection) {
                this.cachedColliderDirection = movementDirection;
                this.cachedColliders = this.createColliders(world, movementDirection);
            }
            return this.cachedColliders;
        }
    }

    public Set<BlockPos> createColliders(Level world, Direction movementDirection) {
        Set<BlockPos> colliders = new HashSet();
        for (StructureTemplate.StructureBlockInfo info : this.getBlocks().values()) {
            BlockPos offsetPos = info.pos().relative(movementDirection);
            if (!info.state().m_60812_(world, offsetPos).isEmpty() && (!this.getBlocks().containsKey(offsetPos) || ((StructureTemplate.StructureBlockInfo) this.getBlocks().get(offsetPos)).state().m_60812_(world, offsetPos).isEmpty())) {
                colliders.add(info.pos());
            }
        }
        return colliders;
    }

    @Override
    public void removeBlocksFromWorld(Level world, BlockPos offset) {
        int count = this.blocks.size();
        super.removeBlocksFromWorld(world, offset);
        if (count != this.blocks.size()) {
            this.cachedColliders = null;
        }
    }

    @Override
    public boolean canBeStabilized(Direction facing, BlockPos localPos) {
        return false;
    }
}