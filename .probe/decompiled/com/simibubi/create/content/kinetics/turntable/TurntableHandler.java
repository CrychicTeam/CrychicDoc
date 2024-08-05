package com.simibubi.create.content.kinetics.turntable;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class TurntableHandler {

    public static void gameRenderTick() {
        Minecraft mc = Minecraft.getInstance();
        BlockPos pos = mc.player.m_20183_();
        if (mc.gameMode != null) {
            if (AllBlocks.TURNTABLE.has(mc.level.m_8055_(pos))) {
                if (mc.player.m_20096_()) {
                    if (!mc.isPaused()) {
                        if (mc.level.m_7702_(pos) instanceof TurntableBlockEntity turnTable) {
                            float speed = turnTable.getSpeed() * 3.0F / 10.0F;
                            if (speed != 0.0F) {
                                Vec3 origin = VecHelper.getCenterOf(pos);
                                Vec3 offset = mc.player.m_20182_().subtract(origin);
                                if (offset.length() > 0.25) {
                                    speed = (float) ((double) speed * Mth.clamp((0.5 - offset.length()) * 2.0, 0.0, 1.0));
                                }
                                mc.player.m_146922_(mc.player.f_19859_ - speed * AnimationTickHolder.getPartialTicks());
                                mc.player.f_20883_ = mc.player.m_146908_();
                            }
                        }
                    }
                }
            }
        }
    }
}