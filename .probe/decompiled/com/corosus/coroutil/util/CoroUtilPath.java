package com.corosus.coroutil.util;

import com.corosus.coroutil.config.ConfigCoroUtil;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class CoroUtilPath {

    public static boolean tryMoveToEntityLivingLongDist(Mob entSource, Entity entityTo, double moveSpeedAmp) {
        return tryMoveToXYZLongDist(entSource, entityTo.getX(), entityTo.getBoundingBox().minY, entityTo.getZ(), moveSpeedAmp);
    }

    public static boolean tryMoveToXYZLongDist(Mob ent, double x, double y, double z, double moveSpeedAmp) {
        Level world = ent.m_9236_();
        boolean success = false;
        try {
            if (ent.getNavigation().isDone()) {
                double distToPlayer = Math.sqrt(ent.m_20275_(x, y, z));
                double followDist = ent.m_21051_(Attributes.FOLLOW_RANGE).getValue();
                if (distToPlayer <= followDist) {
                    success = CoroUtilCompatibility.tryPathToXYZModCompat(ent, Mth.floor(x), Mth.floor(y), Mth.floor(z), moveSpeedAmp);
                } else {
                    double d = x + 0.5 - ent.m_20185_();
                    double d2 = z + 0.5 - ent.m_20189_();
                    double d1 = y + 0.5 - (ent.m_20186_() + (double) ent.m_20192_());
                    double d3 = (double) Mth.sqrt((float) (d * d + d2 * d2));
                    float f2 = (float) (Math.atan2(d2, d) * 180.0 / (float) Math.PI) - 90.0F;
                    float f3 = (float) (-(Math.atan2(d1, d3) * 180.0 / (float) Math.PI));
                    float rotationPitch = -f3;
                    Random rand = new Random();
                    float randLook = (float) (rand.nextInt(90) - 45);
                    double dist = followDist * 0.75 + (double) rand.nextInt((int) followDist / 2);
                    int gatherX = (int) Math.floor(ent.m_20185_() + -Math.sin((double) ((f2 + randLook) / 180.0F * (float) Math.PI)) * dist);
                    int gatherY = (int) ent.m_20186_();
                    int gatherZ = (int) Math.floor(ent.m_20189_() + Math.cos((double) ((f2 + randLook) / 180.0F * (float) Math.PI)) * dist);
                    BlockPos pos = new BlockPos(gatherX, gatherY, gatherZ);
                    if (!world.isLoaded(pos)) {
                        return false;
                    }
                    BlockState state = world.getBlockState(pos);
                    int tries = 0;
                    if (!CoroUtilBlock.isAir(state.m_60734_())) {
                        for (int offset = -5; tries < 30 && !CoroUtilBlock.isAir(state.m_60734_()) && state.m_60647_(ent.m_9236_(), pos, PathComputationType.LAND); tries++) {
                            gatherY += offset++;
                            pos = new BlockPos(gatherX, gatherY, gatherZ);
                            state = world.getBlockState(pos);
                        }
                    } else {
                        while (tries < 30 && (CoroUtilBlock.isAir(state.m_60734_()) || !state.m_60647_(ent.m_9236_(), pos, PathComputationType.LAND))) {
                            pos = new BlockPos(gatherX, --gatherY, gatherZ);
                            state = world.getBlockState(pos);
                            tries++;
                        }
                    }
                    if (tries < 30) {
                        success = CoroUtilCompatibility.tryPathToXYZModCompat(ent, gatherX, gatherY, gatherZ, moveSpeedAmp);
                    } else {
                        pos = new BlockPos(pos.m_123341_(), world.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.m_123341_(), pos.m_123343_()), pos.m_123343_()).below();
                        if (!world.isLoaded(pos)) {
                            return false;
                        }
                        state = world.getBlockState(pos);
                        if (state.m_60647_(ent.m_9236_(), pos, PathComputationType.LAND)) {
                            success = CoroUtilCompatibility.tryPathToXYZModCompat(ent, pos.m_123341_(), pos.m_123342_(), pos.m_123343_(), moveSpeedAmp);
                        }
                    }
                }
            }
        } catch (Exception var39) {
            CULog.err("Exception trying to pathfind");
            if (ConfigCoroUtil.useLoggingError) {
                var39.printStackTrace();
            }
        }
        return success;
    }
}