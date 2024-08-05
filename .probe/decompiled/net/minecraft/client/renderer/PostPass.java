package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.IOException;
import java.util.List;
import java.util.function.IntSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;
import org.joml.Matrix4f;

public class PostPass implements AutoCloseable {

    private final EffectInstance effect;

    public final RenderTarget inTarget;

    public final RenderTarget outTarget;

    private final List<IntSupplier> auxAssets = Lists.newArrayList();

    private final List<String> auxNames = Lists.newArrayList();

    private final List<Integer> auxWidths = Lists.newArrayList();

    private final List<Integer> auxHeights = Lists.newArrayList();

    private Matrix4f shaderOrthoMatrix;

    public PostPass(ResourceManager resourceManager0, String string1, RenderTarget renderTarget2, RenderTarget renderTarget3) throws IOException {
        this.effect = new EffectInstance(resourceManager0, string1);
        this.inTarget = renderTarget2;
        this.outTarget = renderTarget3;
    }

    public void close() {
        this.effect.close();
    }

    public final String getName() {
        return this.effect.getName();
    }

    public void addAuxAsset(String string0, IntSupplier intSupplier1, int int2, int int3) {
        this.auxNames.add(this.auxNames.size(), string0);
        this.auxAssets.add(this.auxAssets.size(), intSupplier1);
        this.auxWidths.add(this.auxWidths.size(), int2);
        this.auxHeights.add(this.auxHeights.size(), int3);
    }

    public void setOrthoMatrix(Matrix4f matrixF0) {
        this.shaderOrthoMatrix = matrixF0;
    }

    public void process(float float0) {
        this.inTarget.unbindWrite();
        float $$1 = (float) this.outTarget.width;
        float $$2 = (float) this.outTarget.height;
        RenderSystem.viewport(0, 0, (int) $$1, (int) $$2);
        this.effect.setSampler("DiffuseSampler", this.inTarget::m_83975_);
        for (int $$3 = 0; $$3 < this.auxAssets.size(); $$3++) {
            this.effect.setSampler((String) this.auxNames.get($$3), (IntSupplier) this.auxAssets.get($$3));
            this.effect.safeGetUniform("AuxSize" + $$3).set((float) ((Integer) this.auxWidths.get($$3)).intValue(), (float) ((Integer) this.auxHeights.get($$3)).intValue());
        }
        this.effect.safeGetUniform("ProjMat").set(this.shaderOrthoMatrix);
        this.effect.safeGetUniform("InSize").set((float) this.inTarget.width, (float) this.inTarget.height);
        this.effect.safeGetUniform("OutSize").set($$1, $$2);
        this.effect.safeGetUniform("Time").set(float0);
        Minecraft $$4 = Minecraft.getInstance();
        this.effect.safeGetUniform("ScreenSize").set((float) $$4.getWindow().getWidth(), (float) $$4.getWindow().getHeight());
        this.effect.apply();
        this.outTarget.clear(Minecraft.ON_OSX);
        this.outTarget.bindWrite(false);
        RenderSystem.depthFunc(519);
        BufferBuilder $$5 = Tesselator.getInstance().getBuilder();
        $$5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        $$5.m_5483_(0.0, 0.0, 500.0).endVertex();
        $$5.m_5483_((double) $$1, 0.0, 500.0).endVertex();
        $$5.m_5483_((double) $$1, (double) $$2, 500.0).endVertex();
        $$5.m_5483_(0.0, (double) $$2, 500.0).endVertex();
        BufferUploader.draw($$5.end());
        RenderSystem.depthFunc(515);
        this.effect.clear();
        this.outTarget.unbindWrite();
        this.inTarget.unbindRead();
        for (Object $$6 : this.auxAssets) {
            if ($$6 instanceof RenderTarget) {
                ((RenderTarget) $$6).unbindRead();
            }
        }
    }

    public EffectInstance getEffect() {
        return this.effect;
    }
}