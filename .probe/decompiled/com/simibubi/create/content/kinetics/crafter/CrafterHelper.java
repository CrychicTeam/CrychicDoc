package com.simibubi.create.content.kinetics.crafter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CrafterHelper {

    public static MechanicalCrafterBlockEntity getCrafter(BlockAndTintGetter reader, BlockPos pos) {
        BlockEntity blockEntity = reader.m_7702_(pos);
        return !(blockEntity instanceof MechanicalCrafterBlockEntity) ? null : (MechanicalCrafterBlockEntity) blockEntity;
    }

    public static ConnectedInputHandler.ConnectedInput getInput(BlockAndTintGetter reader, BlockPos pos) {
        MechanicalCrafterBlockEntity crafter = getCrafter(reader, pos);
        return crafter == null ? null : crafter.input;
    }

    public static boolean areCraftersConnected(BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos) {
        ConnectedInputHandler.ConnectedInput input1 = getInput(reader, pos);
        ConnectedInputHandler.ConnectedInput input2 = getInput(reader, otherPos);
        if (input1 == null || input2 == null) {
            return false;
        } else if (!input1.data.isEmpty() && !input2.data.isEmpty()) {
            try {
                if (pos.offset((Vec3i) input1.data.get(0)).equals(otherPos.offset((Vec3i) input2.data.get(0)))) {
                    return true;
                }
            } catch (IndexOutOfBoundsException var6) {
            }
            return false;
        } else {
            return false;
        }
    }
}