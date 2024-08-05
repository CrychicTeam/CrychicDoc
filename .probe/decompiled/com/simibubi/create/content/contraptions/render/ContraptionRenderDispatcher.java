package com.simibubi.create.content.contraptions.render;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.backend.gl.error.GlError;
import com.jozufozu.flywheel.config.BackendType;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.jozufozu.flywheel.core.model.WorldModelBuilder;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.jozufozu.flywheel.event.BeginFrameEvent;
import com.jozufozu.flywheel.event.GatherContextEvent;
import com.jozufozu.flywheel.event.ReloadRenderersEvent;
import com.jozufozu.flywheel.event.RenderLayerEvent;
import com.jozufozu.flywheel.util.WorldAttached;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionWorld;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.render.BlockEntityRenderHelper;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.commons.lang3.tuple.Pair;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber({ Dist.CLIENT })
public class ContraptionRenderDispatcher {

    private static WorldAttached<ContraptionRenderingWorld<?>> WORLDS = new WorldAttached(SBBContraptionManager::new);

    public static boolean invalidate(Contraption contraption) {
        Level level = contraption.entity.m_9236_();
        return ((ContraptionRenderingWorld) WORLDS.get(level)).invalidate(contraption);
    }

    public static void tick(Level world) {
        if (!Minecraft.getInstance().isPaused()) {
            ((ContraptionRenderingWorld) WORLDS.get(world)).tick();
        }
    }

    @SubscribeEvent
    public static void beginFrame(BeginFrameEvent event) {
        ((ContraptionRenderingWorld) WORLDS.get(event.getWorld())).beginFrame(event);
    }

    @SubscribeEvent
    public static void renderLayer(RenderLayerEvent event) {
        ((ContraptionRenderingWorld) WORLDS.get(event.getWorld())).renderLayer(event);
        GlError.pollAndThrow(() -> "contraption layer: " + event.getLayer());
    }

    @SubscribeEvent
    public static void onRendererReload(ReloadRenderersEvent event) {
        reset();
    }

    public static void gatherContext(GatherContextEvent e) {
        reset();
    }

    public static void renderFromEntity(AbstractContraptionEntity entity, Contraption contraption, MultiBufferSource buffers) {
        Level world = entity.m_9236_();
        ContraptionRenderInfo renderInfo = ((ContraptionRenderingWorld) WORLDS.get(world)).getRenderInfo(contraption);
        ContraptionMatrices matrices = renderInfo.getMatrices();
        if (matrices.isReady()) {
            VirtualRenderWorld renderWorld = renderInfo.renderWorld;
            renderBlockEntities(world, renderWorld, contraption, matrices, buffers);
            if (buffers instanceof MultiBufferSource.BufferSource) {
                ((MultiBufferSource.BufferSource) buffers).endBatch();
            }
            renderActors(world, renderWorld, contraption, matrices, buffers);
        }
    }

    public static VirtualRenderWorld setupRenderWorld(Level world, Contraption c) {
        ContraptionWorld contraptionWorld = c.getContraptionWorld();
        BlockPos origin = c.anchor;
        int minBuildHeight = contraptionWorld.getMinBuildHeight();
        int height = contraptionWorld.getHeight();
        VirtualRenderWorld renderWorld = new VirtualRenderWorld(world, minBuildHeight, height, origin) {

            public boolean supportsFlywheel() {
                return ContraptionRenderDispatcher.canInstance();
            }
        };
        renderWorld.setBlockEntities(c.presentBlockEntities.values());
        for (StructureTemplate.StructureBlockInfo info : c.getBlocks().values()) {
            renderWorld.m_7731_(info.pos(), info.state(), 0);
        }
        renderWorld.runLightEngine();
        return renderWorld;
    }

    public static void renderBlockEntities(Level world, VirtualRenderWorld renderWorld, Contraption c, ContraptionMatrices matrices, MultiBufferSource buffer) {
        BlockEntityRenderHelper.renderBlockEntities(world, renderWorld, c.getSpecialRenderedBEs(), matrices.getModelViewProjection(), matrices.getLight(), buffer);
    }

    protected static void renderActors(Level world, VirtualRenderWorld renderWorld, Contraption c, ContraptionMatrices matrices, MultiBufferSource buffer) {
        PoseStack m = matrices.getModel();
        for (Pair<StructureTemplate.StructureBlockInfo, MovementContext> actor : c.getActors()) {
            MovementContext context = (MovementContext) actor.getRight();
            if (context != null) {
                if (context.world == null) {
                    context.world = world;
                }
                StructureTemplate.StructureBlockInfo blockInfo = (StructureTemplate.StructureBlockInfo) actor.getLeft();
                MovementBehaviour movementBehaviour = AllMovementBehaviours.getBehaviour(blockInfo.state());
                if (movementBehaviour != null && !c.isHiddenInPortal(blockInfo.pos())) {
                    m.pushPose();
                    TransformStack.cast(m).translate(blockInfo.pos());
                    movementBehaviour.renderInContraption(context, renderWorld, matrices, buffer);
                    m.popPose();
                }
            }
        }
    }

    public static SuperByteBuffer buildStructureBuffer(VirtualRenderWorld renderWorld, Contraption c, RenderType layer) {
        Collection<StructureTemplate.StructureBlockInfo> values = c.getRenderedBlocks();
        ShadeSeparatedBufferedData data = new WorldModelBuilder(layer).withRenderWorld(renderWorld).withBlocks(values).withModelData(c.modelData).build();
        SuperByteBuffer sbb = new SuperByteBuffer(data);
        data.release();
        return sbb;
    }

    public static int getLight(Level world, float lx, float ly, float lz) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        float block = 0.0F;
        float sky = 0.0F;
        float offset = 0.125F;
        for (float zOffset = offset; zOffset >= -offset; zOffset -= 2.0F * offset) {
            for (float yOffset = offset; yOffset >= -offset; yOffset -= 2.0F * offset) {
                for (float xOffset = offset; xOffset >= -offset; xOffset -= 2.0F * offset) {
                    pos.set((double) (lx + xOffset), (double) (ly + yOffset), (double) (lz + zOffset));
                    block += (float) world.m_45517_(LightLayer.BLOCK, pos) / 8.0F;
                    sky += (float) world.m_45517_(LightLayer.SKY, pos) / 8.0F;
                }
            }
        }
        return LightTexture.pack((int) block, (int) sky);
    }

    public static int getContraptionWorldLight(MovementContext context, VirtualRenderWorld renderWorld) {
        return LevelRenderer.getLightColor(renderWorld, context.localPos);
    }

    public static void reset() {
        WORLDS.empty(ContraptionRenderingWorld::delete);
        if (Backend.isOn()) {
            WORLDS = new WorldAttached(FlwContraptionManager::new);
        } else {
            WORLDS = new WorldAttached(SBBContraptionManager::new);
        }
    }

    public static boolean canInstance() {
        return Backend.getBackendType() == BackendType.INSTANCING;
    }
}