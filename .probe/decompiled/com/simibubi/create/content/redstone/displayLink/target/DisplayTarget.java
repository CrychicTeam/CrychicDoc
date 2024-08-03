package com.simibubi.create.content.redstone.displayLink.target;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.redstone.displayLink.DisplayBehaviour;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class DisplayTarget extends DisplayBehaviour {

    public abstract void acceptText(int var1, List<MutableComponent> var2, DisplayLinkContext var3);

    public abstract DisplayTargetStats provideStats(DisplayLinkContext var1);

    public AABB getMultiblockBounds(LevelAccessor level, BlockPos pos) {
        VoxelShape shape = level.m_8055_(pos).m_60808_(level, pos);
        return shape.isEmpty() ? new AABB(pos) : shape.bounds().move(pos);
    }

    public Component getLineOptionText(int line) {
        return Lang.translateDirect("display_target.line", line + 1);
    }

    public static void reserve(int line, BlockEntity target, DisplayLinkContext context) {
        if (line != 0) {
            CompoundTag tag = target.getPersistentData();
            CompoundTag compound = tag.getCompound("DisplayLink");
            compound.putLong("Line" + line, context.blockEntity().m_58899_().asLong());
            tag.put("DisplayLink", compound);
        }
    }

    public boolean isReserved(int line, BlockEntity target, DisplayLinkContext context) {
        CompoundTag tag = target.getPersistentData();
        CompoundTag compound = tag.getCompound("DisplayLink");
        if (!compound.contains("Line" + line)) {
            return false;
        } else {
            long l = compound.getLong("Line" + line);
            BlockPos reserved = BlockPos.of(l);
            if (!reserved.equals(context.blockEntity().m_58899_()) && AllBlocks.DISPLAY_LINK.has(target.getLevel().getBlockState(reserved))) {
                return true;
            } else {
                compound.remove("Line" + line);
                if (compound.isEmpty()) {
                    tag.remove("DisplayLink");
                }
                return false;
            }
        }
    }
}