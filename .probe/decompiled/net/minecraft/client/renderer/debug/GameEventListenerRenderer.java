package net.minecraft.client.renderer.debug;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import java.util.Optional;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

public class GameEventListenerRenderer implements DebugRenderer.SimpleDebugRenderer {

    private final Minecraft minecraft;

    private static final int LISTENER_RENDER_DIST = 32;

    private static final float BOX_HEIGHT = 1.0F;

    private final List<GameEventListenerRenderer.TrackedGameEvent> trackedGameEvents = Lists.newArrayList();

    private final List<GameEventListenerRenderer.TrackedListener> trackedListeners = Lists.newArrayList();

    public GameEventListenerRenderer(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    @Override
    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4) {
        Level $$5 = this.minecraft.level;
        if ($$5 == null) {
            this.trackedGameEvents.clear();
            this.trackedListeners.clear();
        } else {
            Vec3 $$6 = new Vec3(double2, 0.0, double4);
            this.trackedGameEvents.removeIf(GameEventListenerRenderer.TrackedGameEvent::m_173868_);
            this.trackedListeners.removeIf(p_234512_ -> p_234512_.isExpired($$5, $$6));
            VertexConsumer $$7 = multiBufferSource1.getBuffer(RenderType.lines());
            for (GameEventListenerRenderer.TrackedListener $$8 : this.trackedListeners) {
                $$8.getPosition($$5).ifPresent(p_269731_ -> {
                    double $$7x = p_269731_.x() - (double) $$8.getListenerRadius();
                    double $$8x = p_269731_.y() - (double) $$8.getListenerRadius();
                    double $$9 = p_269731_.z() - (double) $$8.getListenerRadius();
                    double $$10 = p_269731_.x() + (double) $$8.getListenerRadius();
                    double $$11 = p_269731_.y() + (double) $$8.getListenerRadius();
                    double $$12x = p_269731_.z() + (double) $$8.getListenerRadius();
                    LevelRenderer.renderVoxelShape(poseStack0, $$7, Shapes.create(new AABB($$7x, $$8x, $$9, $$10, $$11, $$12x)), -double2, -double3, -double4, 1.0F, 1.0F, 0.0F, 0.35F, true);
                });
            }
            VertexConsumer $$9 = multiBufferSource1.getBuffer(RenderType.debugFilledBox());
            for (GameEventListenerRenderer.TrackedListener $$10 : this.trackedListeners) {
                $$10.getPosition($$5).ifPresent(p_269724_ -> LevelRenderer.addChainedFilledBoxVertices(poseStack0, $$9, p_269724_.x() - 0.25 - double2, p_269724_.y() - double3, p_269724_.z() - 0.25 - double4, p_269724_.x() + 0.25 - double2, p_269724_.y() - double3 + 1.0, p_269724_.z() + 0.25 - double4, 1.0F, 1.0F, 0.0F, 0.35F));
            }
            for (GameEventListenerRenderer.TrackedListener $$11 : this.trackedListeners) {
                $$11.getPosition($$5).ifPresent(p_274713_ -> {
                    DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, "Listener Origin", p_274713_.x(), p_274713_.y() + 1.8F, p_274713_.z(), -1, 0.025F);
                    DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, BlockPos.containing(p_274713_).toString(), p_274713_.x(), p_274713_.y() + 1.5, p_274713_.z(), -6959665, 0.025F);
                });
            }
            for (GameEventListenerRenderer.TrackedGameEvent $$12 : this.trackedGameEvents) {
                Vec3 $$13 = $$12.position;
                double $$14 = 0.2F;
                double $$15 = $$13.x - 0.2F;
                double $$16 = $$13.y - 0.2F;
                double $$17 = $$13.z - 0.2F;
                double $$18 = $$13.x + 0.2F;
                double $$19 = $$13.y + 0.2F + 0.5;
                double $$20 = $$13.z + 0.2F;
                renderFilledBox(poseStack0, multiBufferSource1, new AABB($$15, $$16, $$17, $$18, $$19, $$20), 1.0F, 1.0F, 1.0F, 0.2F);
                DebugRenderer.renderFloatingText(poseStack0, multiBufferSource1, $$12.gameEvent.getName(), $$13.x, $$13.y + 0.85F, $$13.z, -7564911, 0.0075F);
            }
        }
    }

    private static void renderFilledBox(PoseStack poseStack0, MultiBufferSource multiBufferSource1, AABB aABB2, float float3, float float4, float float5, float float6) {
        Camera $$7 = Minecraft.getInstance().gameRenderer.getMainCamera();
        if ($$7.isInitialized()) {
            Vec3 $$8 = $$7.getPosition().reverse();
            DebugRenderer.renderFilledBox(poseStack0, multiBufferSource1, aABB2.move($$8), float3, float4, float5, float6);
        }
    }

    public void trackGameEvent(GameEvent gameEvent0, Vec3 vec1) {
        this.trackedGameEvents.add(new GameEventListenerRenderer.TrackedGameEvent(Util.getMillis(), gameEvent0, vec1));
    }

    public void trackListener(PositionSource positionSource0, int int1) {
        this.trackedListeners.add(new GameEventListenerRenderer.TrackedListener(positionSource0, int1));
    }

    static record TrackedGameEvent(long f_173861_, GameEvent f_173862_, Vec3 f_173863_) {

        private final long timeStamp;

        private final GameEvent gameEvent;

        private final Vec3 position;

        TrackedGameEvent(long f_173861_, GameEvent f_173862_, Vec3 f_173863_) {
            this.timeStamp = f_173861_;
            this.gameEvent = f_173862_;
            this.position = f_173863_;
        }

        public boolean isExpired() {
            return Util.getMillis() - this.timeStamp > 3000L;
        }
    }

    static class TrackedListener implements GameEventListener {

        public final PositionSource listenerSource;

        public final int listenerRange;

        public TrackedListener(PositionSource positionSource0, int int1) {
            this.listenerSource = positionSource0;
            this.listenerRange = int1;
        }

        public boolean isExpired(Level level0, Vec3 vec1) {
            return this.listenerSource.getPosition(level0).filter(p_234547_ -> p_234547_.distanceToSqr(vec1) <= 1024.0).isPresent();
        }

        public Optional<Vec3> getPosition(Level level0) {
            return this.listenerSource.getPosition(level0);
        }

        @Override
        public PositionSource getListenerSource() {
            return this.listenerSource;
        }

        @Override
        public int getListenerRadius() {
            return this.listenerRange;
        }

        @Override
        public boolean handleGameEvent(ServerLevel serverLevel0, GameEvent gameEvent1, GameEvent.Context gameEventContext2, Vec3 vec3) {
            return false;
        }
    }
}