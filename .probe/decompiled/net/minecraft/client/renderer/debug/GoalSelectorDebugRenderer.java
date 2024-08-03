package net.minecraft.client.renderer.debug;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;

public class GoalSelectorDebugRenderer implements DebugRenderer.SimpleDebugRenderer {

    private static final int MAX_RENDER_DIST = 160;

    private final Minecraft minecraft;

    private final Map<Integer, List<GoalSelectorDebugRenderer.DebugGoal>> goalSelectors = Maps.newHashMap();

    @Override
    public void clear() {
        this.goalSelectors.clear();
    }

    public void addGoalSelector(int int0, List<GoalSelectorDebugRenderer.DebugGoal> listGoalSelectorDebugRendererDebugGoal1) {
        this.goalSelectors.put(int0, listGoalSelectorDebugRendererDebugGoal1);
    }

    public void removeGoalSelector(int int0) {
        this.goalSelectors.remove(int0);
    }

    public GoalSelectorDebugRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        Camera $$5 = this.minecraft.gameRenderer.getMainCamera();
        BlockPos $$6 = BlockPos.containing($$5.getPosition().x, 0.0, $$5.getPosition().z);
        this.goalSelectors.forEach((p_269742_, p_269743_) -> {
            for (int $$5x = 0; $$5x < p_269743_.size(); $$5x++) {
                GoalSelectorDebugRenderer.DebugGoal $$6x = (GoalSelectorDebugRenderer.DebugGoal) p_269743_.get($$5x);
                if ($$6.m_123314_($$6x.pos, 160.0)) {
                    double $$7 = (double) $$6x.pos.m_123341_() + 0.5;
                    double $$8 = (double) $$6x.pos.m_123342_() + 2.0 + (double) $$5x * 0.25;
                    double $$9 = (double) $$6x.pos.m_123343_() + 0.5;
                    int $$10 = $$6x.isRunning ? -16711936 : -3355444;
                    DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, $$6x.name, $$7, $$8, $$9, $$10);
                }
            }
        });
    }

    public static class DebugGoal {

        public final BlockPos pos;

        public final int priority;

        public final String name;

        public final boolean isRunning;

        public DebugGoal(BlockPos blockPos0, int int1, String string2, boolean boolean3) {
            this.pos = blockPos0;
            this.priority = int1;
            this.name = string2;
            this.isRunning = boolean3;
        }
    }
}