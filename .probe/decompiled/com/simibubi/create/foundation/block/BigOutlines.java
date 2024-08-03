package com.simibubi.create.foundation.block;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.trains.track.TrackBlock;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.RaycastHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public class BigOutlines {

    static BlockHitResult result = null;

    public static void pick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.cameraEntity instanceof LocalPlayer player) {
            if (mc.level != null) {
                result = null;
                Vec3 origin = player.m_20299_(AnimationTickHolder.getPartialTicks(mc.level));
                double maxRange = mc.hitResult == null ? Double.MAX_VALUE : mc.hitResult.getLocation().distanceToSqr(origin);
                AttributeInstance range = player.m_21051_(ForgeMod.BLOCK_REACH.get());
                Vec3 target = RaycastHelper.getTraceTarget(player, Math.min(maxRange, range.getValue()) + 1.0, origin);
                RaycastHelper.rayTraceUntil(origin, target, pos -> {
                    BlockPos.MutableBlockPos p = BlockPos.ZERO.mutable();
                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            p.set(pos.m_123341_() + x, pos.m_123342_(), pos.m_123343_() + z);
                            BlockState blockState = mc.level.m_8055_(p);
                            if (blockState.m_60734_() instanceof TrackBlock || blockState.m_60734_() instanceof SlidingDoorBlock) {
                                BlockHitResult hit = blockState.m_60820_(mc.level, p).clip(origin, target, p.immutable());
                                if (hit != null && (result == null || !(Vec3.atCenterOf(p).distanceToSqr(origin) >= Vec3.atCenterOf(result.getBlockPos()).distanceToSqr(origin)))) {
                                    Vec3 vec = hit.m_82450_();
                                    double interactionDist = vec.distanceToSqr(origin);
                                    if (!(interactionDist >= maxRange)) {
                                        BlockPos hitPos = hit.getBlockPos();
                                        vec = vec.subtract(Vec3.atCenterOf(hitPos));
                                        vec = VecHelper.clampComponentWise(vec, 1.0F);
                                        vec = vec.add(Vec3.atCenterOf(hitPos));
                                        result = new BlockHitResult(vec, hit.getDirection(), hitPos, hit.isInside());
                                    }
                                }
                            }
                        }
                    }
                    return result != null;
                });
                if (result != null) {
                    mc.hitResult = result;
                }
            }
        }
    }

    static boolean isValidPos(Player player, BlockPos pos) {
        double x = player.m_20185_() - ((double) pos.m_123341_() + 0.5);
        double y = player.m_20186_() - ((double) pos.m_123342_() + 0.5) + 1.5;
        double z = player.m_20189_() - ((double) pos.m_123343_() + 0.5);
        double distSqr = x * x + y * y + z * z;
        double maxDist = player.m_21051_(ForgeMod.BLOCK_REACH.get()).getValue() + 1.0;
        maxDist *= maxDist;
        return distSqr <= maxDist;
    }
}