package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.shaders.AbstractUniform;
import com.mojang.blaze3d.shaders.BlendMode;
import com.mojang.blaze3d.shaders.Effect;
import com.mojang.blaze3d.shaders.EffectProgram;
import com.mojang.blaze3d.shaders.Program;
import com.mojang.blaze3d.shaders.ProgramManager;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.function.IntSupplier;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.slf4j.Logger;

public class EffectInstance implements Effect, AutoCloseable {

    private static final String EFFECT_SHADER_PATH = "shaders/program/";

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final AbstractUniform DUMMY_UNIFORM = new AbstractUniform();

    private static final boolean ALWAYS_REAPPLY = true;

    private static EffectInstance lastAppliedEffect;

    private static int lastProgramId = -1;

    private final Map<String, IntSupplier> samplerMap = Maps.newHashMap();

    private final List<String> samplerNames = Lists.newArrayList();

    private final List<Integer> samplerLocations = Lists.newArrayList();

    private final List<Uniform> uniforms = Lists.newArrayList();

    private final List<Integer> uniformLocations = Lists.newArrayList();

    private final Map<String, Uniform> uniformMap = Maps.newHashMap();

    private final int programId;

    private final String name;

    private boolean dirty;

    private final BlendMode blend;

    private final List<Integer> attributes;

    private final List<String> attributeNames;

    private final EffectProgram vertexProgram;

    private final EffectProgram fragmentProgram;

    public EffectInstance(ResourceManager resourceManager0, String string1) throws IOException {
        ResourceLocation $$2 = new ResourceLocation("shaders/program/" + string1 + ".json");
        this.name = string1;
        Resource $$3 = resourceManager0.m_215593_($$2);
        try {
            Reader $$4 = $$3.openAsReader();
            try {
                JsonObject $$5 = GsonHelper.parse($$4);
                String $$6 = GsonHelper.getAsString($$5, "vertex");
                String $$7 = GsonHelper.getAsString($$5, "fragment");
                JsonArray $$8 = GsonHelper.getAsJsonArray($$5, "samplers", null);
                if ($$8 != null) {
                    int $$9 = 0;
                    for (JsonElement $$10 : $$8) {
                        try {
                            this.parseSamplerNode($$10);
                        } catch (Exception var20) {
                            ChainedJsonException $$12 = ChainedJsonException.forException(var20);
                            $$12.prependJsonKey("samplers[" + $$9 + "]");
                            throw $$12;
                        }
                        $$9++;
                    }
                }
                JsonArray $$13 = GsonHelper.getAsJsonArray($$5, "attributes", null);
                if ($$13 != null) {
                    int $$14 = 0;
                    this.attributes = Lists.newArrayListWithCapacity($$13.size());
                    this.attributeNames = Lists.newArrayListWithCapacity($$13.size());
                    for (JsonElement $$15 : $$13) {
                        try {
                            this.attributeNames.add(GsonHelper.convertToString($$15, "attribute"));
                        } catch (Exception var19) {
                            ChainedJsonException $$17 = ChainedJsonException.forException(var19);
                            $$17.prependJsonKey("attributes[" + $$14 + "]");
                            throw $$17;
                        }
                        $$14++;
                    }
                } else {
                    this.attributes = null;
                    this.attributeNames = null;
                }
                JsonArray $$18 = GsonHelper.getAsJsonArray($$5, "uniforms", null);
                if ($$18 != null) {
                    int $$19 = 0;
                    for (JsonElement $$20 : $$18) {
                        try {
                            this.parseUniformNode($$20);
                        } catch (Exception var18) {
                            ChainedJsonException $$22 = ChainedJsonException.forException(var18);
                            $$22.prependJsonKey("uniforms[" + $$19 + "]");
                            throw $$22;
                        }
                        $$19++;
                    }
                }
                this.blend = parseBlendNode(GsonHelper.getAsJsonObject($$5, "blend", null));
                this.vertexProgram = getOrCreate(resourceManager0, Program.Type.VERTEX, $$6);
                this.fragmentProgram = getOrCreate(resourceManager0, Program.Type.FRAGMENT, $$7);
                this.programId = ProgramManager.createProgram();
                ProgramManager.linkShader(this);
                this.updateLocations();
                if (this.attributeNames != null) {
                    for (String $$23 : this.attributeNames) {
                        int $$24 = Uniform.glGetAttribLocation(this.programId, $$23);
                        this.attributes.add($$24);
                    }
                }
            } catch (Throwable var21) {
                if ($$4 != null) {
                    try {
                        $$4.close();
                    } catch (Throwable var17) {
                        var21.addSuppressed(var17);
                    }
                }
                throw var21;
            }
            if ($$4 != null) {
                $$4.close();
            }
        } catch (Exception var22) {
            ChainedJsonException $$26 = ChainedJsonException.forException(var22);
            $$26.setFilenameAndFlush($$2.getPath() + " (" + $$3.sourcePackId() + ")");
            throw $$26;
        }
        this.markDirty();
    }

    public static EffectProgram getOrCreate(ResourceManager resourceManager0, Program.Type programType1, String string2) throws IOException {
        Program $$3 = (Program) programType1.getPrograms().get(string2);
        if ($$3 != null && !($$3 instanceof EffectProgram)) {
            throw new InvalidClassException("Program is not of type EffectProgram");
        } else {
            EffectProgram $$7;
            if ($$3 == null) {
                ResourceLocation $$4 = new ResourceLocation("shaders/program/" + string2 + programType1.getExtension());
                Resource $$5 = resourceManager0.m_215593_($$4);
                InputStream $$6 = $$5.open();
                try {
                    $$7 = EffectProgram.compileShader(programType1, string2, $$6, $$5.sourcePackId());
                } catch (Throwable var11) {
                    if ($$6 != null) {
                        try {
                            $$6.close();
                        } catch (Throwable var10) {
                            var11.addSuppressed(var10);
                        }
                    }
                    throw var11;
                }
                if ($$6 != null) {
                    $$6.close();
                }
            } else {
                $$7 = (EffectProgram) $$3;
            }
            return $$7;
        }
    }

    public static BlendMode parseBlendNode(@Nullable JsonObject jsonObject0) {
        if (jsonObject0 == null) {
            return new BlendMode();
        } else {
            int $$1 = 32774;
            int $$2 = 1;
            int $$3 = 0;
            int $$4 = 1;
            int $$5 = 0;
            boolean $$6 = true;
            boolean $$7 = false;
            if (GsonHelper.isStringValue(jsonObject0, "func")) {
                $$1 = BlendMode.stringToBlendFunc(jsonObject0.get("func").getAsString());
                if ($$1 != 32774) {
                    $$6 = false;
                }
            }
            if (GsonHelper.isStringValue(jsonObject0, "srcrgb")) {
                $$2 = BlendMode.stringToBlendFactor(jsonObject0.get("srcrgb").getAsString());
                if ($$2 != 1) {
                    $$6 = false;
                }
            }
            if (GsonHelper.isStringValue(jsonObject0, "dstrgb")) {
                $$3 = BlendMode.stringToBlendFactor(jsonObject0.get("dstrgb").getAsString());
                if ($$3 != 0) {
                    $$6 = false;
                }
            }
            if (GsonHelper.isStringValue(jsonObject0, "srcalpha")) {
                $$4 = BlendMode.stringToBlendFactor(jsonObject0.get("srcalpha").getAsString());
                if ($$4 != 1) {
                    $$6 = false;
                }
                $$7 = true;
            }
            if (GsonHelper.isStringValue(jsonObject0, "dstalpha")) {
                $$5 = BlendMode.stringToBlendFactor(jsonObject0.get("dstalpha").getAsString());
                if ($$5 != 0) {
                    $$6 = false;
                }
                $$7 = true;
            }
            if ($$6) {
                return new BlendMode();
            } else {
                return $$7 ? new BlendMode($$2, $$3, $$4, $$5, $$1) : new BlendMode($$2, $$3, $$1);
            }
        }
    }

    public void close() {
        for (Uniform $$0 : this.uniforms) {
            $$0.close();
        }
        ProgramManager.releaseProgram(this);
    }

    public void clear() {
        RenderSystem.assertOnRenderThread();
        ProgramManager.glUseProgram(0);
        lastProgramId = -1;
        lastAppliedEffect = null;
        for (int $$0 = 0; $$0 < this.samplerLocations.size(); $$0++) {
            if (this.samplerMap.get(this.samplerNames.get($$0)) != null) {
                GlStateManager._activeTexture(33984 + $$0);
                GlStateManager._bindTexture(0);
            }
        }
    }

    public void apply() {
        RenderSystem.assertOnGameThread();
        this.dirty = false;
        lastAppliedEffect = this;
        this.blend.apply();
        if (this.programId != lastProgramId) {
            ProgramManager.glUseProgram(this.programId);
            lastProgramId = this.programId;
        }
        for (int $$0 = 0; $$0 < this.samplerLocations.size(); $$0++) {
            String $$1 = (String) this.samplerNames.get($$0);
            IntSupplier $$2 = (IntSupplier) this.samplerMap.get($$1);
            if ($$2 != null) {
                RenderSystem.activeTexture(33984 + $$0);
                int $$3 = $$2.getAsInt();
                if ($$3 != -1) {
                    RenderSystem.bindTexture($$3);
                    Uniform.uploadInteger((Integer) this.samplerLocations.get($$0), $$0);
                }
            }
        }
        for (Uniform $$4 : this.uniforms) {
            $$4.upload();
        }
    }

    @Override
    public void markDirty() {
        this.dirty = true;
    }

    @Nullable
    public Uniform getUniform(String string0) {
        RenderSystem.assertOnRenderThread();
        return (Uniform) this.uniformMap.get(string0);
    }

    public AbstractUniform safeGetUniform(String string0) {
        RenderSystem.assertOnGameThread();
        Uniform $$1 = this.getUniform(string0);
        return (AbstractUniform) ($$1 == null ? DUMMY_UNIFORM : $$1);
    }

    private void updateLocations() {
        RenderSystem.assertOnRenderThread();
        IntList $$0 = new IntArrayList();
        for (int $$1 = 0; $$1 < this.samplerNames.size(); $$1++) {
            String $$2 = (String) this.samplerNames.get($$1);
            int $$3 = Uniform.glGetUniformLocation(this.programId, $$2);
            if ($$3 == -1) {
                LOGGER.warn("Shader {} could not find sampler named {} in the specified shader program.", this.name, $$2);
                this.samplerMap.remove($$2);
                $$0.add($$1);
            } else {
                this.samplerLocations.add($$3);
            }
        }
        for (int $$4 = $$0.size() - 1; $$4 >= 0; $$4--) {
            this.samplerNames.remove($$0.getInt($$4));
        }
        for (Uniform $$5 : this.uniforms) {
            String $$6 = $$5.getName();
            int $$7 = Uniform.glGetUniformLocation(this.programId, $$6);
            if ($$7 == -1) {
                LOGGER.warn("Shader {} could not find uniform named {} in the specified shader program.", this.name, $$6);
            } else {
                this.uniformLocations.add($$7);
                $$5.setLocation($$7);
                this.uniformMap.put($$6, $$5);
            }
        }
    }

    private void parseSamplerNode(JsonElement jsonElement0) {
        JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "sampler");
        String $$2 = GsonHelper.getAsString($$1, "name");
        if (!GsonHelper.isStringValue($$1, "file")) {
            this.samplerMap.put($$2, null);
            this.samplerNames.add($$2);
        } else {
            this.samplerNames.add($$2);
        }
    }

    public void setSampler(String string0, IntSupplier intSupplier1) {
        if (this.samplerMap.containsKey(string0)) {
            this.samplerMap.remove(string0);
        }
        this.samplerMap.put(string0, intSupplier1);
        this.markDirty();
    }

    private void parseUniformNode(JsonElement jsonElement0) throws ChainedJsonException {
        JsonObject $$1 = GsonHelper.convertToJsonObject(jsonElement0, "uniform");
        String $$2 = GsonHelper.getAsString($$1, "name");
        int $$3 = Uniform.getTypeFromString(GsonHelper.getAsString($$1, "type"));
        int $$4 = GsonHelper.getAsInt($$1, "count");
        float[] $$5 = new float[Math.max($$4, 16)];
        JsonArray $$6 = GsonHelper.getAsJsonArray($$1, "values");
        if ($$6.size() != $$4 && $$6.size() > 1) {
            throw new ChainedJsonException("Invalid amount of values specified (expected " + $$4 + ", found " + $$6.size() + ")");
        } else {
            int $$7 = 0;
            for (JsonElement $$8 : $$6) {
                try {
                    $$5[$$7] = GsonHelper.convertToFloat($$8, "value");
                } catch (Exception var13) {
                    ChainedJsonException $$10 = ChainedJsonException.forException(var13);
                    $$10.prependJsonKey("values[" + $$7 + "]");
                    throw $$10;
                }
                $$7++;
            }
            if ($$4 > 1 && $$6.size() == 1) {
                while ($$7 < $$4) {
                    $$5[$$7] = $$5[0];
                    $$7++;
                }
            }
            int $$11 = $$4 > 1 && $$4 <= 4 && $$3 < 8 ? $$4 - 1 : 0;
            Uniform $$12 = new Uniform($$2, $$3 + $$11, $$4, this);
            if ($$3 <= 3) {
                $$12.setSafe((int) $$5[0], (int) $$5[1], (int) $$5[2], (int) $$5[3]);
            } else if ($$3 <= 7) {
                $$12.setSafe($$5[0], $$5[1], $$5[2], $$5[3]);
            } else {
                $$12.set($$5);
            }
            this.uniforms.add($$12);
        }
    }

    @Override
    public Program getVertexProgram() {
        return this.vertexProgram;
    }

    @Override
    public Program getFragmentProgram() {
        return this.fragmentProgram;
    }

    @Override
    public void attachToProgram() {
        this.fragmentProgram.attachToEffect(this);
        this.vertexProgram.attachToEffect(this);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int getId() {
        return this.programId;
    }
}