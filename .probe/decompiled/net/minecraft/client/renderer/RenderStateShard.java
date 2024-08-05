package net.minecraft.client.renderer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Triple;
import org.joml.Matrix4f;

public abstract class RenderStateShard {

    private static final float VIEW_SCALE_Z_EPSILON = 0.99975586F;

    public static final double MAX_ENCHANTMENT_GLINT_SPEED_MILLIS = 8.0;

    protected final String name;

    private final Runnable setupState;

    private final Runnable clearState;

    protected static final RenderStateShard.TransparencyStateShard NO_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("no_transparency", () -> RenderSystem.disableBlend(), () -> {
    });

    protected static final RenderStateShard.TransparencyStateShard ADDITIVE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("additive_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    protected static final RenderStateShard.TransparencyStateShard LIGHTNING_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("lightning_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    protected static final RenderStateShard.TransparencyStateShard GLINT_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("glint_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    protected static final RenderStateShard.TransparencyStateShard CRUMBLING_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("crumbling_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    protected static final RenderStateShard.TransparencyStateShard TRANSLUCENT_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    protected static final RenderStateShard.ShaderStateShard NO_SHADER = new RenderStateShard.ShaderStateShard();

    protected static final RenderStateShard.ShaderStateShard POSITION_COLOR_LIGHTMAP_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172832_);

    protected static final RenderStateShard.ShaderStateShard POSITION_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172808_);

    protected static final RenderStateShard.ShaderStateShard POSITION_COLOR_TEX_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172814_);

    protected static final RenderStateShard.ShaderStateShard POSITION_TEX_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172817_);

    protected static final RenderStateShard.ShaderStateShard POSITION_COLOR_TEX_LIGHTMAP_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172835_);

    protected static final RenderStateShard.ShaderStateShard POSITION_COLOR_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172811_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_SOLID_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172640_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_CUTOUT_MIPPED_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172643_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_CUTOUT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172646_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_TRANSLUCENT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172649_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_TRANSLUCENT_MOVING_BLOCK_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172652_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_TRANSLUCENT_NO_CRUMBLING_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172655_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ARMOR_CUTOUT_NO_CULL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172658_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_SOLID_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172661_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172664_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172667_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_CUTOUT_NO_CULL_Z_OFFSET_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172670_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172673_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172676_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_TRANSLUCENT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172679_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_234223_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_SMOOTH_CUTOUT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172682_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_BEACON_BEAM_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172685_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_DECAL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172688_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_NO_OUTLINE_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172691_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_SHADOW_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172694_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_ALPHA_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172697_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_EYES_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172700_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENERGY_SWIRL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172703_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_LEASH_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172706_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_WATER_MASK_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172709_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_OUTLINE_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172712_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ARMOR_GLINT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172738_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ARMOR_ENTITY_GLINT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172741_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_GLINT_TRANSLUCENT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172744_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_GLINT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172745_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_GLINT_DIRECT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172746_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_GLINT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172747_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172748_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_CRUMBLING_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172758_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_TEXT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172749_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_TEXT_BACKGROUND_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_269563_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_TEXT_INTENSITY_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172750_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_TEXT_SEE_THROUGH_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172751_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_TEXT_BACKGROUND_SEE_THROUGH_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_269511_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_TEXT_INTENSITY_SEE_THROUGH_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172752_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_LIGHTNING_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172753_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_TRIPWIRE_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172754_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_END_PORTAL_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172755_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_END_GATEWAY_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172756_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_LINES_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_172757_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_GUI_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_285858_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_GUI_OVERLAY_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_285975_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_GUI_TEXT_HIGHLIGHT_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_285738_);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_GUI_GHOST_RECIPE_OVERLAY_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::m_285862_);

    protected static final RenderStateShard.TextureStateShard BLOCK_SHEET_MIPPED = new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, true);

    protected static final RenderStateShard.TextureStateShard BLOCK_SHEET = new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false);

    protected static final RenderStateShard.EmptyTextureStateShard NO_TEXTURE = new RenderStateShard.EmptyTextureStateShard();

    protected static final RenderStateShard.TexturingStateShard DEFAULT_TEXTURING = new RenderStateShard.TexturingStateShard("default_texturing", () -> {
    }, () -> {
    });

    protected static final RenderStateShard.TexturingStateShard GLINT_TEXTURING = new RenderStateShard.TexturingStateShard("glint_texturing", () -> setupGlintTexturing(8.0F), () -> RenderSystem.resetTextureMatrix());

    protected static final RenderStateShard.TexturingStateShard ENTITY_GLINT_TEXTURING = new RenderStateShard.TexturingStateShard("entity_glint_texturing", () -> setupGlintTexturing(0.16F), () -> RenderSystem.resetTextureMatrix());

    protected static final RenderStateShard.LightmapStateShard LIGHTMAP = new RenderStateShard.LightmapStateShard(true);

    protected static final RenderStateShard.LightmapStateShard NO_LIGHTMAP = new RenderStateShard.LightmapStateShard(false);

    protected static final RenderStateShard.OverlayStateShard OVERLAY = new RenderStateShard.OverlayStateShard(true);

    protected static final RenderStateShard.OverlayStateShard NO_OVERLAY = new RenderStateShard.OverlayStateShard(false);

    protected static final RenderStateShard.CullStateShard CULL = new RenderStateShard.CullStateShard(true);

    protected static final RenderStateShard.CullStateShard NO_CULL = new RenderStateShard.CullStateShard(false);

    protected static final RenderStateShard.DepthTestStateShard NO_DEPTH_TEST = new RenderStateShard.DepthTestStateShard("always", 519);

    protected static final RenderStateShard.DepthTestStateShard EQUAL_DEPTH_TEST = new RenderStateShard.DepthTestStateShard("==", 514);

    protected static final RenderStateShard.DepthTestStateShard LEQUAL_DEPTH_TEST = new RenderStateShard.DepthTestStateShard("<=", 515);

    protected static final RenderStateShard.DepthTestStateShard GREATER_DEPTH_TEST = new RenderStateShard.DepthTestStateShard(">", 516);

    protected static final RenderStateShard.WriteMaskStateShard COLOR_DEPTH_WRITE = new RenderStateShard.WriteMaskStateShard(true, true);

    protected static final RenderStateShard.WriteMaskStateShard COLOR_WRITE = new RenderStateShard.WriteMaskStateShard(true, false);

    protected static final RenderStateShard.WriteMaskStateShard DEPTH_WRITE = new RenderStateShard.WriteMaskStateShard(false, true);

    protected static final RenderStateShard.LayeringStateShard NO_LAYERING = new RenderStateShard.LayeringStateShard("no_layering", () -> {
    }, () -> {
    });

    protected static final RenderStateShard.LayeringStateShard POLYGON_OFFSET_LAYERING = new RenderStateShard.LayeringStateShard("polygon_offset_layering", () -> {
        RenderSystem.polygonOffset(-1.0F, -10.0F);
        RenderSystem.enablePolygonOffset();
    }, () -> {
        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();
    });

    protected static final RenderStateShard.LayeringStateShard VIEW_OFFSET_Z_LAYERING = new RenderStateShard.LayeringStateShard("view_offset_z_layering", () -> {
        PoseStack $$0 = RenderSystem.getModelViewStack();
        $$0.pushPose();
        $$0.scale(0.99975586F, 0.99975586F, 0.99975586F);
        RenderSystem.applyModelViewMatrix();
    }, () -> {
        PoseStack $$0 = RenderSystem.getModelViewStack();
        $$0.popPose();
        RenderSystem.applyModelViewMatrix();
    });

    protected static final RenderStateShard.OutputStateShard MAIN_TARGET = new RenderStateShard.OutputStateShard("main_target", () -> {
    }, () -> {
    });

    protected static final RenderStateShard.OutputStateShard OUTLINE_TARGET = new RenderStateShard.OutputStateShard("outline_target", () -> Minecraft.getInstance().levelRenderer.entityTarget().bindWrite(false), () -> Minecraft.getInstance().getMainRenderTarget().bindWrite(false));

    protected static final RenderStateShard.OutputStateShard TRANSLUCENT_TARGET = new RenderStateShard.OutputStateShard("translucent_target", () -> {
        if (Minecraft.useShaderTransparency()) {
            Minecraft.getInstance().levelRenderer.getTranslucentTarget().bindWrite(false);
        }
    }, () -> {
        if (Minecraft.useShaderTransparency()) {
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        }
    });

    protected static final RenderStateShard.OutputStateShard PARTICLES_TARGET = new RenderStateShard.OutputStateShard("particles_target", () -> {
        if (Minecraft.useShaderTransparency()) {
            Minecraft.getInstance().levelRenderer.getParticlesTarget().bindWrite(false);
        }
    }, () -> {
        if (Minecraft.useShaderTransparency()) {
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        }
    });

    protected static final RenderStateShard.OutputStateShard WEATHER_TARGET = new RenderStateShard.OutputStateShard("weather_target", () -> {
        if (Minecraft.useShaderTransparency()) {
            Minecraft.getInstance().levelRenderer.getWeatherTarget().bindWrite(false);
        }
    }, () -> {
        if (Minecraft.useShaderTransparency()) {
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        }
    });

    protected static final RenderStateShard.OutputStateShard CLOUDS_TARGET = new RenderStateShard.OutputStateShard("clouds_target", () -> {
        if (Minecraft.useShaderTransparency()) {
            Minecraft.getInstance().levelRenderer.getCloudsTarget().bindWrite(false);
        }
    }, () -> {
        if (Minecraft.useShaderTransparency()) {
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        }
    });

    protected static final RenderStateShard.OutputStateShard ITEM_ENTITY_TARGET = new RenderStateShard.OutputStateShard("item_entity_target", () -> {
        if (Minecraft.useShaderTransparency()) {
            Minecraft.getInstance().levelRenderer.getItemEntityTarget().bindWrite(false);
        }
    }, () -> {
        if (Minecraft.useShaderTransparency()) {
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        }
    });

    protected static final RenderStateShard.LineStateShard DEFAULT_LINE = new RenderStateShard.LineStateShard(OptionalDouble.of(1.0));

    protected static final RenderStateShard.ColorLogicStateShard NO_COLOR_LOGIC = new RenderStateShard.ColorLogicStateShard("no_color_logic", () -> RenderSystem.disableColorLogicOp(), () -> {
    });

    protected static final RenderStateShard.ColorLogicStateShard OR_REVERSE_COLOR_LOGIC = new RenderStateShard.ColorLogicStateShard("or_reverse", () -> {
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
    }, () -> RenderSystem.disableColorLogicOp());

    public RenderStateShard(String string0, Runnable runnable1, Runnable runnable2) {
        this.name = string0;
        this.setupState = runnable1;
        this.clearState = runnable2;
    }

    public void setupRenderState() {
        this.setupState.run();
    }

    public void clearRenderState() {
        this.clearState.run();
    }

    public String toString() {
        return this.name;
    }

    private static void setupGlintTexturing(float float0) {
        long $$1 = (long) ((double) Util.getMillis() * Minecraft.getInstance().options.glintSpeed().get() * 8.0);
        float $$2 = (float) ($$1 % 110000L) / 110000.0F;
        float $$3 = (float) ($$1 % 30000L) / 30000.0F;
        Matrix4f $$4 = new Matrix4f().translation(-$$2, $$3, 0.0F);
        $$4.rotateZ((float) (Math.PI / 18)).scale(float0);
        RenderSystem.setTextureMatrix($$4);
    }

    static class BooleanStateShard extends RenderStateShard {

        private final boolean enabled;

        public BooleanStateShard(String string0, Runnable runnable1, Runnable runnable2, boolean boolean3) {
            super(string0, runnable1, runnable2);
            this.enabled = boolean3;
        }

        @Override
        public String toString() {
            return this.f_110133_ + "[" + this.enabled + "]";
        }
    }

    protected static class ColorLogicStateShard extends RenderStateShard {

        public ColorLogicStateShard(String string0, Runnable runnable1, Runnable runnable2) {
            super(string0, runnable1, runnable2);
        }
    }

    protected static class CullStateShard extends RenderStateShard.BooleanStateShard {

        public CullStateShard(boolean boolean0) {
            super("cull", () -> {
                if (!boolean0) {
                    RenderSystem.disableCull();
                }
            }, () -> {
                if (!boolean0) {
                    RenderSystem.enableCull();
                }
            }, boolean0);
        }
    }

    protected static class DepthTestStateShard extends RenderStateShard {

        private final String functionName;

        public DepthTestStateShard(String string0, int int1) {
            super("depth_test", () -> {
                if (int1 != 519) {
                    RenderSystem.enableDepthTest();
                    RenderSystem.depthFunc(int1);
                }
            }, () -> {
                if (int1 != 519) {
                    RenderSystem.disableDepthTest();
                    RenderSystem.depthFunc(515);
                }
            });
            this.functionName = string0;
        }

        @Override
        public String toString() {
            return this.f_110133_ + "[" + this.functionName + "]";
        }
    }

    protected static class EmptyTextureStateShard extends RenderStateShard {

        public EmptyTextureStateShard(Runnable runnable0, Runnable runnable1) {
            super("texture", runnable0, runnable1);
        }

        EmptyTextureStateShard() {
            super("texture", () -> {
            }, () -> {
            });
        }

        protected Optional<ResourceLocation> cutoutTexture() {
            return Optional.empty();
        }
    }

    protected static class LayeringStateShard extends RenderStateShard {

        public LayeringStateShard(String string0, Runnable runnable1, Runnable runnable2) {
            super(string0, runnable1, runnable2);
        }
    }

    protected static class LightmapStateShard extends RenderStateShard.BooleanStateShard {

        public LightmapStateShard(boolean boolean0) {
            super("lightmap", () -> {
                if (boolean0) {
                    Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
                }
            }, () -> {
                if (boolean0) {
                    Minecraft.getInstance().gameRenderer.lightTexture().turnOffLightLayer();
                }
            }, boolean0);
        }
    }

    protected static class LineStateShard extends RenderStateShard {

        private final OptionalDouble width;

        public LineStateShard(OptionalDouble optionalDouble0) {
            super("line_width", () -> {
                if (!Objects.equals(optionalDouble0, OptionalDouble.of(1.0))) {
                    if (optionalDouble0.isPresent()) {
                        RenderSystem.lineWidth((float) optionalDouble0.getAsDouble());
                    } else {
                        RenderSystem.lineWidth(Math.max(2.5F, (float) Minecraft.getInstance().getWindow().getWidth() / 1920.0F * 2.5F));
                    }
                }
            }, () -> {
                if (!Objects.equals(optionalDouble0, OptionalDouble.of(1.0))) {
                    RenderSystem.lineWidth(1.0F);
                }
            });
            this.width = optionalDouble0;
        }

        @Override
        public String toString() {
            return this.f_110133_ + "[" + (this.width.isPresent() ? this.width.getAsDouble() : "window_scale") + "]";
        }
    }

    protected static class MultiTextureStateShard extends RenderStateShard.EmptyTextureStateShard {

        private final Optional<ResourceLocation> cutoutTexture;

        MultiTextureStateShard(ImmutableList<Triple<ResourceLocation, Boolean, Boolean>> immutableListTripleResourceLocationBooleanBoolean0) {
            super(() -> {
                int $$1 = 0;
                UnmodifiableIterator var2 = immutableListTripleResourceLocationBooleanBoolean0.iterator();
                while (var2.hasNext()) {
                    Triple<ResourceLocation, Boolean, Boolean> $$2 = (Triple<ResourceLocation, Boolean, Boolean>) var2.next();
                    TextureManager $$3 = Minecraft.getInstance().getTextureManager();
                    $$3.getTexture((ResourceLocation) $$2.getLeft()).setFilter((Boolean) $$2.getMiddle(), (Boolean) $$2.getRight());
                    RenderSystem.setShaderTexture($$1++, (ResourceLocation) $$2.getLeft());
                }
            }, () -> {
            });
            this.cutoutTexture = immutableListTripleResourceLocationBooleanBoolean0.stream().findFirst().map(Triple::getLeft);
        }

        @Override
        protected Optional<ResourceLocation> cutoutTexture() {
            return this.cutoutTexture;
        }

        public static RenderStateShard.MultiTextureStateShard.Builder builder() {
            return new RenderStateShard.MultiTextureStateShard.Builder();
        }

        public static final class Builder {

            private final com.google.common.collect.ImmutableList.Builder<Triple<ResourceLocation, Boolean, Boolean>> builder = new com.google.common.collect.ImmutableList.Builder();

            public RenderStateShard.MultiTextureStateShard.Builder add(ResourceLocation resourceLocation0, boolean boolean1, boolean boolean2) {
                this.builder.add(Triple.of(resourceLocation0, boolean1, boolean2));
                return this;
            }

            public RenderStateShard.MultiTextureStateShard build() {
                return new RenderStateShard.MultiTextureStateShard(this.builder.build());
            }
        }
    }

    protected static final class OffsetTexturingStateShard extends RenderStateShard.TexturingStateShard {

        public OffsetTexturingStateShard(float float0, float float1) {
            super("offset_texturing", () -> RenderSystem.setTextureMatrix(new Matrix4f().translation(float0, float1, 0.0F)), () -> RenderSystem.resetTextureMatrix());
        }
    }

    protected static class OutputStateShard extends RenderStateShard {

        public OutputStateShard(String string0, Runnable runnable1, Runnable runnable2) {
            super(string0, runnable1, runnable2);
        }
    }

    protected static class OverlayStateShard extends RenderStateShard.BooleanStateShard {

        public OverlayStateShard(boolean boolean0) {
            super("overlay", () -> {
                if (boolean0) {
                    Minecraft.getInstance().gameRenderer.overlayTexture().setupOverlayColor();
                }
            }, () -> {
                if (boolean0) {
                    Minecraft.getInstance().gameRenderer.overlayTexture().teardownOverlayColor();
                }
            }, boolean0);
        }
    }

    protected static class ShaderStateShard extends RenderStateShard {

        private final Optional<Supplier<ShaderInstance>> shader;

        public ShaderStateShard(Supplier<ShaderInstance> supplierShaderInstance0) {
            super("shader", () -> RenderSystem.setShader(supplierShaderInstance0), () -> {
            });
            this.shader = Optional.of(supplierShaderInstance0);
        }

        public ShaderStateShard() {
            super("shader", () -> RenderSystem.setShader(() -> null), () -> {
            });
            this.shader = Optional.empty();
        }

        @Override
        public String toString() {
            return this.f_110133_ + "[" + this.shader + "]";
        }
    }

    protected static class TextureStateShard extends RenderStateShard.EmptyTextureStateShard {

        private final Optional<ResourceLocation> texture;

        private final boolean blur;

        private final boolean mipmap;

        public TextureStateShard(ResourceLocation resourceLocation0, boolean boolean1, boolean boolean2) {
            super(() -> {
                TextureManager $$3 = Minecraft.getInstance().getTextureManager();
                $$3.getTexture(resourceLocation0).setFilter(boolean1, boolean2);
                RenderSystem.setShaderTexture(0, resourceLocation0);
            }, () -> {
            });
            this.texture = Optional.of(resourceLocation0);
            this.blur = boolean1;
            this.mipmap = boolean2;
        }

        @Override
        public String toString() {
            return this.f_110133_ + "[" + this.texture + "(blur=" + this.blur + ", mipmap=" + this.mipmap + ")]";
        }

        @Override
        protected Optional<ResourceLocation> cutoutTexture() {
            return this.texture;
        }
    }

    protected static class TexturingStateShard extends RenderStateShard {

        public TexturingStateShard(String string0, Runnable runnable1, Runnable runnable2) {
            super(string0, runnable1, runnable2);
        }
    }

    protected static class TransparencyStateShard extends RenderStateShard {

        public TransparencyStateShard(String string0, Runnable runnable1, Runnable runnable2) {
            super(string0, runnable1, runnable2);
        }
    }

    protected static class WriteMaskStateShard extends RenderStateShard {

        private final boolean writeColor;

        private final boolean writeDepth;

        public WriteMaskStateShard(boolean boolean0, boolean boolean1) {
            super("write_mask_state", () -> {
                if (!boolean1) {
                    RenderSystem.depthMask(boolean1);
                }
                if (!boolean0) {
                    RenderSystem.colorMask(boolean0, boolean0, boolean0, boolean0);
                }
            }, () -> {
                if (!boolean1) {
                    RenderSystem.depthMask(true);
                }
                if (!boolean0) {
                    RenderSystem.colorMask(true, true, true, true);
                }
            });
            this.writeColor = boolean0;
            this.writeDepth = boolean1;
        }

        @Override
        public String toString() {
            return this.f_110133_ + "[writeColor=" + this.writeColor + ", writeDepth=" + this.writeDepth + "]";
        }
    }
}