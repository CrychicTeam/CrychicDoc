package com.simibubi.create.content.contraptions.render;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.backend.RenderLayer;
import com.jozufozu.flywheel.backend.gl.GlStateTracker;
import com.jozufozu.flywheel.backend.gl.GlTextureUnit;
import com.jozufozu.flywheel.backend.gl.GlStateTracker.State;
import com.jozufozu.flywheel.config.BackendType;
import com.jozufozu.flywheel.core.Formats;
import com.jozufozu.flywheel.core.Materials.Names;
import com.jozufozu.flywheel.core.compile.ProgramContext;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.jozufozu.flywheel.event.RenderLayerEvent;
import com.jozufozu.flywheel.util.Textures;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.foundation.render.CreateContexts;
import net.minecraft.world.level.LevelAccessor;
import org.lwjgl.opengl.GL11;

public class FlwContraptionManager extends ContraptionRenderingWorld<FlwContraption> {

    public FlwContraptionManager(LevelAccessor world) {
        super(world);
    }

    @Override
    public void tick() {
        super.tick();
        for (FlwContraption contraption : this.visible) {
            contraption.tick();
        }
    }

    @Override
    public void renderLayer(RenderLayerEvent event) {
        super.renderLayer(event);
        if (!this.visible.isEmpty()) {
            State restoreState = GlStateTracker.getRestoreState();
            GlTextureUnit active = GlTextureUnit.getActive();
            BackendType backendType = Backend.getBackendType();
            if (backendType != BackendType.OFF) {
                this.renderStructures(event);
            }
            if (backendType != BackendType.BATCHING && event.getLayer() != null) {
                for (FlwContraption renderer : this.visible) {
                    renderer.renderInstanceLayer(event);
                }
            }
            GlTextureUnit.T4.makeActive();
            GL11.glBindTexture(32879, 0);
            event.type.m_110188_();
            active.makeActive();
            restoreState.restore();
        }
    }

    private void renderStructures(RenderLayerEvent event) {
        event.type.m_110185_();
        Textures.bindActiveTextures();
        ContraptionProgram structureShader = (ContraptionProgram) CreateContexts.STRUCTURE.getProgram(ProgramContext.create(Names.PASSTHRU, Formats.BLOCK, RenderLayer.getLayer(event.type)));
        structureShader.bind();
        structureShader.uploadViewProjection(event.viewProjection);
        structureShader.uploadCameraPos(event.camX, event.camY, event.camZ);
        for (FlwContraption flwContraption : this.visible) {
            flwContraption.renderStructureLayer(event.type, structureShader);
        }
    }

    protected FlwContraption create(Contraption c) {
        VirtualRenderWorld renderWorld = ContraptionRenderDispatcher.setupRenderWorld(this.world, c);
        return new FlwContraption(c, renderWorld);
    }

    @Override
    public void removeDeadRenderers() {
        boolean removed = this.renderInfos.values().removeIf(renderer -> {
            if (renderer.isDead()) {
                renderer.invalidate();
                return true;
            } else {
                return false;
            }
        });
        if (removed) {
            this.collectVisible();
        }
    }
}