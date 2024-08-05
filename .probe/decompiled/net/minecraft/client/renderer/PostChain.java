package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.joml.Matrix4f;

public class PostChain implements AutoCloseable {

    private static final String MAIN_RENDER_TARGET = "minecraft:main";

    private final RenderTarget screenTarget;

    private final ResourceManager resourceManager;

    private final String name;

    private final List<PostPass> passes = Lists.newArrayList();

    private final Map<String, RenderTarget> customRenderTargets = Maps.newHashMap();

    private final List<RenderTarget> fullSizedTargets = Lists.newArrayList();

    private Matrix4f shaderOrthoMatrix;

    private int screenWidth;

    private int screenHeight;

    private float time;

    private float lastStamp;

    public PostChain(TextureManager textureManager0, ResourceManager resourceManager1, RenderTarget renderTarget2, ResourceLocation resourceLocation3) throws IOException, JsonSyntaxException {
        this.resourceManager = resourceManager1;
        this.screenTarget = renderTarget2;
        this.time = 0.0F;
        this.lastStamp = 0.0F;
        this.screenWidth = renderTarget2.viewWidth;
        this.screenHeight = renderTarget2.viewHeight;
        this.name = resourceLocation3.toString();
        this.updateOrthoMatrix();
        this.load(textureManager0, resourceLocation3);
    }

    private void load(TextureManager textureManager0, ResourceLocation resourceLocation1) throws IOException, JsonSyntaxException {
        Resource $$2 = this.resourceManager.m_215593_(resourceLocation1);
        try {
            Reader $$3 = $$2.openAsReader();
            try {
                JsonObject $$4 = GsonHelper.parse($$3);
                if (GsonHelper.isArrayNode($$4, "targets")) {
                    JsonArray $$5 = $$4.getAsJsonArray("targets");
                    int $$6 = 0;
                    for (JsonElement $$7 : $$5) {
                        try {
                            this.parseTargetNode($$7);
                        } catch (Exception var14) {
                            ChainedJsonException $$9 = ChainedJsonException.forException(var14);
                            $$9.prependJsonKey("targets[" + $$6 + "]");
                            throw $$9;
                        }
                        $$6++;
                    }
                }
                if (GsonHelper.isArrayNode($$4, "passes")) {
                    JsonArray $$10 = $$4.getAsJsonArray("passes");
                    int $$11 = 0;
                    for (JsonElement $$12 : $$10) {
                        try {
                            this.parsePassNode(textureManager0, $$12);
                        } catch (Exception var13) {
                            ChainedJsonException $$14 = ChainedJsonException.forException(var13);
                            $$14.prependJsonKey("passes[" + $$11 + "]");
                            throw $$14;
                        }
                        $$11++;
                    }
                }
            } catch (Throwable var15) {
                if ($$3 != null) {
                    try {
                        $$3.close();
                    } catch (Throwable var12) {
                        var15.addSuppressed(var12);
                    }
                }
                throw var15;
            }
            if ($$3 != null) {
                $$3.close();
            }
        } catch (Exception var16) {
            ChainedJsonException $$16 = ChainedJsonException.forException(var16);
            $$16.setFilenameAndFlush(resourceLocation1.getPath() + " (" + $$2.sourcePackId() + ")");
            throw $$16;
        }
    }

    private void parseTargetNode(JsonElement jsonElement0) throws ChainedJsonException {
        if (GsonHelper.isStringValue(jsonElement0)) {
            this.addTempTarget(jsonElement0.getAsString(), this.screenWidth, this.screenHeight);
        } else {
            JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "target");
            String $$2 = GsonHelper.getAsString($$1, "name");
            int $$3 = GsonHelper.getAsInt($$1, "width", this.screenWidth);
            int $$4 = GsonHelper.getAsInt($$1, "height", this.screenHeight);
            if (this.customRenderTargets.containsKey($$2)) {
                throw new ChainedJsonException($$2 + " is already defined");
            }
            this.addTempTarget($$2, $$3, $$4);
        }
    }

    private void parsePassNode(TextureManager textureManager0, JsonElement jsonElement1) throws IOException {
        JsonObject $$2 = GsonHelper.convertToJsonObject(jsonElement1, "pass");
        String $$3 = GsonHelper.getAsString($$2, "name");
        String $$4 = GsonHelper.getAsString($$2, "intarget");
        String $$5 = GsonHelper.getAsString($$2, "outtarget");
        RenderTarget $$6 = this.getRenderTarget($$4);
        RenderTarget $$7 = this.getRenderTarget($$5);
        if ($$6 == null) {
            throw new ChainedJsonException("Input target '" + $$4 + "' does not exist");
        } else if ($$7 == null) {
            throw new ChainedJsonException("Output target '" + $$5 + "' does not exist");
        } else {
            PostPass $$8 = this.addPass($$3, $$6, $$7);
            JsonArray $$9 = GsonHelper.getAsJsonArray($$2, "auxtargets", null);
            if ($$9 != null) {
                int $$10 = 0;
                for (JsonElement $$11 : $$9) {
                    try {
                        JsonObject $$12 = GsonHelper.convertToJsonObject($$11, "auxtarget");
                        String $$13 = GsonHelper.getAsString($$12, "name");
                        String $$14 = GsonHelper.getAsString($$12, "id");
                        boolean $$15;
                        String $$16;
                        if ($$14.endsWith(":depth")) {
                            $$15 = true;
                            $$16 = $$14.substring(0, $$14.lastIndexOf(58));
                        } else {
                            $$15 = false;
                            $$16 = $$14;
                        }
                        RenderTarget $$19 = this.getRenderTarget($$16);
                        if ($$19 == null) {
                            if ($$15) {
                                throw new ChainedJsonException("Render target '" + $$16 + "' can't be used as depth buffer");
                            }
                            ResourceLocation $$20 = new ResourceLocation("textures/effect/" + $$16 + ".png");
                            this.resourceManager.m_213713_($$20).orElseThrow(() -> new ChainedJsonException("Render target or texture '" + $$16 + "' does not exist"));
                            RenderSystem.setShaderTexture(0, $$20);
                            textureManager0.bindForSetup($$20);
                            AbstractTexture $$21 = textureManager0.getTexture($$20);
                            int $$22 = GsonHelper.getAsInt($$12, "width");
                            int $$23 = GsonHelper.getAsInt($$12, "height");
                            boolean $$24 = GsonHelper.getAsBoolean($$12, "bilinear");
                            if ($$24) {
                                RenderSystem.texParameter(3553, 10241, 9729);
                                RenderSystem.texParameter(3553, 10240, 9729);
                            } else {
                                RenderSystem.texParameter(3553, 10241, 9728);
                                RenderSystem.texParameter(3553, 10240, 9728);
                            }
                            $$8.addAuxAsset($$13, $$21::m_117963_, $$22, $$23);
                        } else if ($$15) {
                            $$8.addAuxAsset($$13, $$19::m_83980_, $$19.width, $$19.height);
                        } else {
                            $$8.addAuxAsset($$13, $$19::m_83975_, $$19.width, $$19.height);
                        }
                    } catch (Exception var26) {
                        ChainedJsonException $$26 = ChainedJsonException.forException(var26);
                        $$26.prependJsonKey("auxtargets[" + $$10 + "]");
                        throw $$26;
                    }
                    $$10++;
                }
            }
            JsonArray $$27 = GsonHelper.getAsJsonArray($$2, "uniforms", null);
            if ($$27 != null) {
                int $$28 = 0;
                for (JsonElement $$29 : $$27) {
                    try {
                        this.parseUniformNode($$29);
                    } catch (Exception var25) {
                        ChainedJsonException $$31 = ChainedJsonException.forException(var25);
                        $$31.prependJsonKey("uniforms[" + $$28 + "]");
                        throw $$31;
                    }
                    $$28++;
                }
            }
        }
    }

    private void parseUniformNode(JsonElement jsonElement0) throws ChainedJsonException {
        JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "uniform");
        String $$2 = GsonHelper.getAsString($$1, "name");
        Uniform $$3 = ((PostPass) this.passes.get(this.passes.size() - 1)).getEffect().getUniform($$2);
        if ($$3 == null) {
            throw new ChainedJsonException("Uniform '" + $$2 + "' does not exist");
        } else {
            float[] $$4 = new float[4];
            int $$5 = 0;
            for (JsonElement $$7 : GsonHelper.getAsJsonArray($$1, "values")) {
                try {
                    $$4[$$5] = GsonHelper.convertToFloat($$7, "value");
                } catch (Exception var12) {
                    ChainedJsonException $$9 = ChainedJsonException.forException(var12);
                    $$9.prependJsonKey("values[" + $$5 + "]");
                    throw $$9;
                }
                $$5++;
            }
            switch($$5) {
                case 0:
                default:
                    break;
                case 1:
                    $$3.set($$4[0]);
                    break;
                case 2:
                    $$3.set($$4[0], $$4[1]);
                    break;
                case 3:
                    $$3.set($$4[0], $$4[1], $$4[2]);
                    break;
                case 4:
                    $$3.set($$4[0], $$4[1], $$4[2], $$4[3]);
            }
        }
    }

    public RenderTarget getTempTarget(String string0) {
        return (RenderTarget) this.customRenderTargets.get(string0);
    }

    public void addTempTarget(String string0, int int1, int int2) {
        RenderTarget $$3 = new TextureTarget(int1, int2, true, Minecraft.ON_OSX);
        $$3.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        this.customRenderTargets.put(string0, $$3);
        if (int1 == this.screenWidth && int2 == this.screenHeight) {
            this.fullSizedTargets.add($$3);
        }
    }

    public void close() {
        for (RenderTarget $$0 : this.customRenderTargets.values()) {
            $$0.destroyBuffers();
        }
        for (PostPass $$1 : this.passes) {
            $$1.close();
        }
        this.passes.clear();
    }

    public PostPass addPass(String string0, RenderTarget renderTarget1, RenderTarget renderTarget2) throws IOException {
        PostPass $$3 = new PostPass(this.resourceManager, string0, renderTarget1, renderTarget2);
        this.passes.add(this.passes.size(), $$3);
        return $$3;
    }

    private void updateOrthoMatrix() {
        this.shaderOrthoMatrix = new Matrix4f().setOrtho(0.0F, (float) this.screenTarget.width, 0.0F, (float) this.screenTarget.height, 0.1F, 1000.0F);
    }

    public void resize(int int0, int int1) {
        this.screenWidth = this.screenTarget.width;
        this.screenHeight = this.screenTarget.height;
        this.updateOrthoMatrix();
        for (PostPass $$2 : this.passes) {
            $$2.setOrthoMatrix(this.shaderOrthoMatrix);
        }
        for (RenderTarget $$3 : this.fullSizedTargets) {
            $$3.resize(int0, int1, Minecraft.ON_OSX);
        }
    }

    public void process(float float0) {
        if (float0 < this.lastStamp) {
            this.time = this.time + (1.0F - this.lastStamp);
            this.time += float0;
        } else {
            this.time = this.time + (float0 - this.lastStamp);
        }
        this.lastStamp = float0;
        while (this.time > 20.0F) {
            this.time -= 20.0F;
        }
        for (PostPass $$1 : this.passes) {
            $$1.process(this.time / 20.0F);
        }
    }

    public final String getName() {
        return this.name;
    }

    @Nullable
    private RenderTarget getRenderTarget(@Nullable String string0) {
        if (string0 == null) {
            return null;
        } else {
            return string0.equals("minecraft:main") ? this.screenTarget : (RenderTarget) this.customRenderTargets.get(string0);
        }
    }
}