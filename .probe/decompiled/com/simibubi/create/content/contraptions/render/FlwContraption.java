package com.simibubi.create.content.contraptions.render;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.backend.gl.GlStateTracker;
import com.jozufozu.flywheel.backend.gl.GlStateTracker.State;
import com.jozufozu.flywheel.backend.instancing.Engine;
import com.jozufozu.flywheel.backend.instancing.InstancedRenderRegistry;
import com.jozufozu.flywheel.backend.instancing.SerialTaskEngine;
import com.jozufozu.flywheel.backend.instancing.batching.BatchingEngine;
import com.jozufozu.flywheel.backend.instancing.instancing.InstancingEngine;
import com.jozufozu.flywheel.backend.model.ArrayModelRenderer;
import com.jozufozu.flywheel.core.model.Model;
import com.jozufozu.flywheel.core.model.WorldModelBuilder;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.jozufozu.flywheel.event.BeginFrameEvent;
import com.jozufozu.flywheel.event.RenderLayerEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.foundation.render.CreateContexts;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class FlwContraption extends ContraptionRenderInfo {

    private final ContraptionLighter<?> lighter;

    private final Map<RenderType, ArrayModelRenderer> renderLayers = new HashMap();

    private final Matrix4f modelViewPartial = new Matrix4f();

    private final FlwContraption.ContraptionInstanceWorld instanceWorld;

    private boolean modelViewPartialReady;

    private AABB lightBox;

    public FlwContraption(Contraption contraption, VirtualRenderWorld renderWorld) {
        super(contraption, renderWorld);
        this.lighter = contraption.makeLighter();
        this.instanceWorld = new FlwContraption.ContraptionInstanceWorld(this);
        State restoreState = GlStateTracker.getRestoreState();
        this.buildLayers();
        if (ContraptionRenderDispatcher.canInstance()) {
            this.buildInstancedBlockEntities();
            this.buildActors();
        }
        restoreState.restore();
    }

    public ContraptionLighter<?> getLighter() {
        return this.lighter;
    }

    public void renderStructureLayer(RenderType layer, ContraptionProgram shader) {
        ArrayModelRenderer structure = (ArrayModelRenderer) this.renderLayers.get(layer);
        if (structure != null) {
            this.setup(shader);
            structure.draw();
        }
    }

    public void renderInstanceLayer(RenderLayerEvent event) {
        event.stack.pushPose();
        float partialTicks = AnimationTickHolder.getPartialTicks();
        AbstractContraptionEntity entity = this.contraption.entity;
        double x = Mth.lerp((double) partialTicks, entity.f_19790_, entity.m_20185_());
        double y = Mth.lerp((double) partialTicks, entity.f_19791_, entity.m_20186_());
        double z = Mth.lerp((double) partialTicks, entity.f_19792_, entity.m_20189_());
        event.stack.translate(x - event.camX, y - event.camY, z - event.camZ);
        ContraptionMatrices.transform(event.stack, this.getMatrices().getModel());
        this.instanceWorld.engine.render(SerialTaskEngine.INSTANCE, event);
        event.stack.popPose();
    }

    @Override
    public void beginFrame(BeginFrameEvent event) {
        super.beginFrame(event);
        this.modelViewPartial.identity();
        this.modelViewPartialReady = false;
        if (this.isVisible()) {
            this.instanceWorld.blockEntityInstanceManager.beginFrame(SerialTaskEngine.INSTANCE, event.getCamera());
            Vec3 cameraPos = event.getCameraPos();
            this.lightBox = this.lighter.lightVolume.toAABB().move(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        }
    }

    @Override
    public void setupMatrices(PoseStack viewProjection, double camX, double camY, double camZ) {
        super.setupMatrices(viewProjection, camX, camY, camZ);
        if (!this.modelViewPartialReady) {
            setupModelViewPartial(this.modelViewPartial, this.getMatrices().getModel().last().pose(), this.contraption.entity, camX, camY, camZ, AnimationTickHolder.getPartialTicks());
            this.modelViewPartialReady = true;
        }
    }

    void setup(ContraptionProgram shader) {
        if (this.modelViewPartialReady && this.lightBox != null) {
            shader.bind(this.modelViewPartial, this.lightBox);
            this.lighter.lightVolume.bind();
        }
    }

    @Override
    public void invalidate() {
        for (ArrayModelRenderer renderer : this.renderLayers.values()) {
            renderer.delete();
            renderer.getModel().delete();
        }
        this.renderLayers.clear();
        this.lighter.delete();
        this.instanceWorld.delete();
    }

    private void buildLayers() {
        for (ArrayModelRenderer renderer : this.renderLayers.values()) {
            renderer.delete();
            renderer.getModel().delete();
        }
        this.renderLayers.clear();
        List<RenderType> blockLayers = RenderType.chunkBufferLayers();
        Collection<StructureTemplate.StructureBlockInfo> renderedBlocks = this.contraption.getRenderedBlocks();
        for (RenderType layer : blockLayers) {
            Model layerModel = new WorldModelBuilder(layer).withRenderWorld(this.renderWorld).withModelData(this.contraption.modelData).withBlocks(renderedBlocks).toModel(layer + "_" + this.contraption.entity.m_19879_());
            this.renderLayers.put(layer, new ArrayModelRenderer(layerModel));
        }
    }

    private void buildInstancedBlockEntities() {
        for (BlockEntity be : this.contraption.maybeInstancedBlockEntities) {
            if (InstancedRenderRegistry.canInstance(be.getType())) {
                Level world = be.getLevel();
                be.setLevel(this.renderWorld);
                this.instanceWorld.blockEntityInstanceManager.add(be);
                be.setLevel(world);
            }
        }
    }

    private void buildActors() {
        this.contraption.getActors().forEach(this.instanceWorld.blockEntityInstanceManager::createActor);
    }

    public static void setupModelViewPartial(Matrix4f matrix, Matrix4f modelMatrix, AbstractContraptionEntity entity, double camX, double camY, double camZ, float pt) {
        float x = (float) (Mth.lerp((double) pt, entity.f_19790_, entity.m_20185_()) - camX);
        float y = (float) (Mth.lerp((double) pt, entity.f_19791_, entity.m_20186_()) - camY);
        float z = (float) (Mth.lerp((double) pt, entity.f_19792_, entity.m_20189_()) - camZ);
        matrix.setTranslation(x, y, z);
        matrix.mul(modelMatrix);
    }

    public void tick() {
        this.instanceWorld.blockEntityInstanceManager.tick();
    }

    public static class ContraptionInstanceWorld {

        private final Engine engine;

        private final ContraptionInstanceManager blockEntityInstanceManager;

        public ContraptionInstanceWorld(FlwContraption parent) {
            switch(Backend.getBackendType()) {
                case INSTANCING:
                    InstancingEngine<ContraptionProgram> engine = InstancingEngine.builder(CreateContexts.CWORLD).setGroupFactory(ContraptionGroup.forContraption(parent)).setIgnoreOriginCoordinate(true).build();
                    this.blockEntityInstanceManager = new ContraptionInstanceManager(engine, parent.renderWorld, parent.contraption);
                    engine.addListener(this.blockEntityInstanceManager);
                    this.engine = engine;
                    break;
                case BATCHING:
                    this.engine = new BatchingEngine();
                    this.blockEntityInstanceManager = new ContraptionInstanceManager(this.engine, parent.renderWorld, parent.contraption);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown engine type");
            }
        }

        public void delete() {
            this.engine.delete();
            this.blockEntityInstanceManager.invalidate();
        }
    }
}