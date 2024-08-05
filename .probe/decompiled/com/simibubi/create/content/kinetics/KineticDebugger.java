package com.simibubi.create.content.kinetics;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class KineticDebugger {

    public static void tick() {
        if (!isActive()) {
            if (KineticBlockEntityRenderer.rainbowMode) {
                KineticBlockEntityRenderer.rainbowMode = false;
                CreateClient.BUFFER_CACHE.invalidate();
            }
        } else {
            KineticBlockEntity be = getSelectedBE();
            if (be != null) {
                Level world = Minecraft.getInstance().level;
                BlockPos toOutline = be.hasSource() ? be.source : be.m_58899_();
                BlockState state = be.m_58900_();
                VoxelShape shape = world.getBlockState(toOutline).m_60816_(world, toOutline);
                if (be.getTheoreticalSpeed() != 0.0F && !shape.isEmpty()) {
                    CreateClient.OUTLINER.chaseAABB("kineticSource", shape.bounds().move(toOutline)).lineWidth(0.0625F).colored(be.hasSource() ? Color.generateFromLong(be.network).getRGB() : 16763904);
                }
                if (state.m_60734_() instanceof IRotate) {
                    Direction.Axis axis = ((IRotate) state.m_60734_()).getRotationAxis(state);
                    Vec3 vec = Vec3.atLowerCornerOf(Direction.get(Direction.AxisDirection.POSITIVE, axis).getNormal());
                    Vec3 center = VecHelper.getCenterOf(be.m_58899_());
                    CreateClient.OUTLINER.showLine("rotationAxis", center.add(vec), center.subtract(vec)).lineWidth(0.0625F);
                }
            }
        }
    }

    public static boolean isActive() {
        return isF3DebugModeActive() && AllConfigs.client().rainbowDebug.get();
    }

    public static boolean isF3DebugModeActive() {
        return Minecraft.getInstance().options.renderDebug;
    }

    public static KineticBlockEntity getSelectedBE() {
        HitResult obj = Minecraft.getInstance().hitResult;
        ClientLevel world = Minecraft.getInstance().level;
        if (obj == null) {
            return null;
        } else if (world == null) {
            return null;
        } else if (!(obj instanceof BlockHitResult ray)) {
            return null;
        } else {
            BlockEntity be = world.m_7702_(ray.getBlockPos());
            return !(be instanceof KineticBlockEntity) ? null : (KineticBlockEntity) be;
        }
    }
}