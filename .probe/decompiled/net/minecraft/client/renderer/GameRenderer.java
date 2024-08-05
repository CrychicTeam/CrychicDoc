package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.Program;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.slf4j.Logger;

public class GameRenderer implements AutoCloseable {

    private static final ResourceLocation NAUSEA_LOCATION = new ResourceLocation("textures/misc/nausea.png");

    static final Logger LOGGER = LogUtils.getLogger();

    private static final boolean DEPTH_BUFFER_DEBUG = false;

    public static final float PROJECTION_Z_NEAR = 0.05F;

    private static final float GUI_Z_NEAR = 1000.0F;

    final Minecraft minecraft;

    private final ResourceManager resourceManager;

    private final RandomSource random = RandomSource.create();

    private float renderDistance;

    public final ItemInHandRenderer itemInHandRenderer;

    private final MapRenderer mapRenderer;

    private final RenderBuffers renderBuffers;

    private int tick;

    private float fov;

    private float oldFov;

    private float darkenWorldAmount;

    private float darkenWorldAmountO;

    private boolean renderHand = true;

    private boolean renderBlockOutline = true;

    private long lastScreenshotAttempt;

    private boolean hasWorldScreenshot;

    private long lastActiveTime = Util.getMillis();

    private final LightTexture lightTexture;

    private final OverlayTexture overlayTexture = new OverlayTexture();

    private boolean panoramicMode;

    private float zoom = 1.0F;

    private float zoomX;

    private float zoomY;

    public static final int ITEM_ACTIVATION_ANIMATION_LENGTH = 40;

    @Nullable
    private ItemStack itemActivationItem;

    private int itemActivationTicks;

    private float itemActivationOffX;

    private float itemActivationOffY;

    @Nullable
    PostChain postEffect;

    static final ResourceLocation[] EFFECTS = new ResourceLocation[] { new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json") };

    public static final int EFFECT_NONE = EFFECTS.length;

    int effectIndex = EFFECT_NONE;

    private boolean effectActive;

    private final Camera mainCamera = new Camera();

    public ShaderInstance blitShader;

    private final Map<String, ShaderInstance> shaders = Maps.newHashMap();

    @Nullable
    private static ShaderInstance positionShader;

    @Nullable
    private static ShaderInstance positionColorShader;

    @Nullable
    private static ShaderInstance positionColorTexShader;

    @Nullable
    private static ShaderInstance positionTexShader;

    @Nullable
    private static ShaderInstance positionTexColorShader;

    @Nullable
    private static ShaderInstance particleShader;

    @Nullable
    private static ShaderInstance positionColorLightmapShader;

    @Nullable
    private static ShaderInstance positionColorTexLightmapShader;

    @Nullable
    private static ShaderInstance positionTexColorNormalShader;

    @Nullable
    private static ShaderInstance positionTexLightmapColorShader;

    @Nullable
    private static ShaderInstance rendertypeSolidShader;

    @Nullable
    private static ShaderInstance rendertypeCutoutMippedShader;

    @Nullable
    private static ShaderInstance rendertypeCutoutShader;

    @Nullable
    private static ShaderInstance rendertypeTranslucentShader;

    @Nullable
    private static ShaderInstance rendertypeTranslucentMovingBlockShader;

    @Nullable
    private static ShaderInstance rendertypeTranslucentNoCrumblingShader;

    @Nullable
    private static ShaderInstance rendertypeArmorCutoutNoCullShader;

    @Nullable
    private static ShaderInstance rendertypeEntitySolidShader;

    @Nullable
    private static ShaderInstance rendertypeEntityCutoutShader;

    @Nullable
    private static ShaderInstance rendertypeEntityCutoutNoCullShader;

    @Nullable
    private static ShaderInstance rendertypeEntityCutoutNoCullZOffsetShader;

    @Nullable
    private static ShaderInstance rendertypeItemEntityTranslucentCullShader;

    @Nullable
    private static ShaderInstance rendertypeEntityTranslucentCullShader;

    @Nullable
    private static ShaderInstance rendertypeEntityTranslucentShader;

    @Nullable
    private static ShaderInstance rendertypeEntityTranslucentEmissiveShader;

    @Nullable
    private static ShaderInstance rendertypeEntitySmoothCutoutShader;

    @Nullable
    private static ShaderInstance rendertypeBeaconBeamShader;

    @Nullable
    private static ShaderInstance rendertypeEntityDecalShader;

    @Nullable
    private static ShaderInstance rendertypeEntityNoOutlineShader;

    @Nullable
    private static ShaderInstance rendertypeEntityShadowShader;

    @Nullable
    private static ShaderInstance rendertypeEntityAlphaShader;

    @Nullable
    private static ShaderInstance rendertypeEyesShader;

    @Nullable
    private static ShaderInstance rendertypeEnergySwirlShader;

    @Nullable
    private static ShaderInstance rendertypeLeashShader;

    @Nullable
    private static ShaderInstance rendertypeWaterMaskShader;

    @Nullable
    private static ShaderInstance rendertypeOutlineShader;

    @Nullable
    private static ShaderInstance rendertypeArmorGlintShader;

    @Nullable
    private static ShaderInstance rendertypeArmorEntityGlintShader;

    @Nullable
    private static ShaderInstance rendertypeGlintTranslucentShader;

    @Nullable
    private static ShaderInstance rendertypeGlintShader;

    @Nullable
    private static ShaderInstance rendertypeGlintDirectShader;

    @Nullable
    private static ShaderInstance rendertypeEntityGlintShader;

    @Nullable
    private static ShaderInstance rendertypeEntityGlintDirectShader;

    @Nullable
    private static ShaderInstance rendertypeTextShader;

    @Nullable
    private static ShaderInstance rendertypeTextBackgroundShader;

    @Nullable
    private static ShaderInstance rendertypeTextIntensityShader;

    @Nullable
    private static ShaderInstance rendertypeTextSeeThroughShader;

    @Nullable
    private static ShaderInstance rendertypeTextBackgroundSeeThroughShader;

    @Nullable
    private static ShaderInstance rendertypeTextIntensitySeeThroughShader;

    @Nullable
    private static ShaderInstance rendertypeLightningShader;

    @Nullable
    private static ShaderInstance rendertypeTripwireShader;

    @Nullable
    private static ShaderInstance rendertypeEndPortalShader;

    @Nullable
    private static ShaderInstance rendertypeEndGatewayShader;

    @Nullable
    private static ShaderInstance rendertypeLinesShader;

    @Nullable
    private static ShaderInstance rendertypeCrumblingShader;

    @Nullable
    private static ShaderInstance rendertypeGuiShader;

    @Nullable
    private static ShaderInstance rendertypeGuiOverlayShader;

    @Nullable
    private static ShaderInstance rendertypeGuiTextHighlightShader;

    @Nullable
    private static ShaderInstance rendertypeGuiGhostRecipeOverlayShader;

    public GameRenderer(Minecraft minecraft0, ItemInHandRenderer itemInHandRenderer1, ResourceManager resourceManager2, RenderBuffers renderBuffers3) {
        this.minecraft = minecraft0;
        this.resourceManager = resourceManager2;
        this.itemInHandRenderer = itemInHandRenderer1;
        this.mapRenderer = new MapRenderer(minecraft0.getTextureManager());
        this.lightTexture = new LightTexture(this, minecraft0);
        this.renderBuffers = renderBuffers3;
        this.postEffect = null;
    }

    public void close() {
        this.lightTexture.close();
        this.mapRenderer.close();
        this.overlayTexture.close();
        this.shutdownEffect();
        this.shutdownShaders();
        if (this.blitShader != null) {
            this.blitShader.close();
        }
    }

    public void setRenderHand(boolean boolean0) {
        this.renderHand = boolean0;
    }

    public void setRenderBlockOutline(boolean boolean0) {
        this.renderBlockOutline = boolean0;
    }

    public void setPanoramicMode(boolean boolean0) {
        this.panoramicMode = boolean0;
    }

    public boolean isPanoramicMode() {
        return this.panoramicMode;
    }

    public void shutdownEffect() {
        if (this.postEffect != null) {
            this.postEffect.close();
        }
        this.postEffect = null;
        this.effectIndex = EFFECT_NONE;
    }

    public void togglePostEffect() {
        this.effectActive = !this.effectActive;
    }

    public void checkEntityPostEffect(@Nullable Entity entity0) {
        if (this.postEffect != null) {
            this.postEffect.close();
        }
        this.postEffect = null;
        if (entity0 instanceof Creeper) {
            this.loadEffect(new ResourceLocation("shaders/post/creeper.json"));
        } else if (entity0 instanceof Spider) {
            this.loadEffect(new ResourceLocation("shaders/post/spider.json"));
        } else if (entity0 instanceof EnderMan) {
            this.loadEffect(new ResourceLocation("shaders/post/invert.json"));
        }
    }

    public void cycleEffect() {
        if (this.minecraft.getCameraEntity() instanceof Player) {
            if (this.postEffect != null) {
                this.postEffect.close();
            }
            this.effectIndex = (this.effectIndex + 1) % (EFFECTS.length + 1);
            if (this.effectIndex == EFFECT_NONE) {
                this.postEffect = null;
            } else {
                this.loadEffect(EFFECTS[this.effectIndex]);
            }
        }
    }

    void loadEffect(ResourceLocation resourceLocation0) {
        if (this.postEffect != null) {
            this.postEffect.close();
        }
        try {
            this.postEffect = new PostChain(this.minecraft.getTextureManager(), this.resourceManager, this.minecraft.getMainRenderTarget(), resourceLocation0);
            this.postEffect.resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
            this.effectActive = true;
        } catch (IOException var3) {
            LOGGER.warn("Failed to load shader: {}", resourceLocation0, var3);
            this.effectIndex = EFFECT_NONE;
            this.effectActive = false;
        } catch (JsonSyntaxException var4) {
            LOGGER.warn("Failed to parse shader: {}", resourceLocation0, var4);
            this.effectIndex = EFFECT_NONE;
            this.effectActive = false;
        }
    }

    public PreparableReloadListener createReloadListener() {
        return new SimplePreparableReloadListener<GameRenderer.ResourceCache>() {

            protected GameRenderer.ResourceCache prepare(ResourceManager p_251213_, ProfilerFiller p_251006_) {
                Map<ResourceLocation, Resource> $$2 = p_251213_.listResources("shaders", p_251575_ -> {
                    String $$1 = p_251575_.getPath();
                    return $$1.endsWith(".json") || $$1.endsWith(Program.Type.FRAGMENT.getExtension()) || $$1.endsWith(Program.Type.VERTEX.getExtension()) || $$1.endsWith(".glsl");
                });
                Map<ResourceLocation, Resource> $$3 = new HashMap();
                $$2.forEach((p_250354_, p_250712_) -> {
                    try {
                        InputStream $$3x = p_250712_.open();
                        try {
                            byte[] $$4 = $$3x.readAllBytes();
                            $$3.put(p_250354_, new Resource(p_250712_.source(), () -> new ByteArrayInputStream($$4)));
                        } catch (Throwable var7) {
                            if ($$3x != null) {
                                try {
                                    $$3x.close();
                                } catch (Throwable var6) {
                                    var7.addSuppressed(var6);
                                }
                            }
                            throw var7;
                        }
                        if ($$3x != null) {
                            $$3x.close();
                        }
                    } catch (Exception var8) {
                        GameRenderer.LOGGER.warn("Failed to read resource {}", p_250354_, var8);
                    }
                });
                return new GameRenderer.ResourceCache(p_251213_, $$3);
            }

            protected void apply(GameRenderer.ResourceCache p_251168_, ResourceManager p_248902_, ProfilerFiller p_251909_) {
                GameRenderer.this.reloadShaders(p_251168_);
                if (GameRenderer.this.postEffect != null) {
                    GameRenderer.this.postEffect.close();
                }
                GameRenderer.this.postEffect = null;
                if (GameRenderer.this.effectIndex == GameRenderer.EFFECT_NONE) {
                    GameRenderer.this.checkEntityPostEffect(GameRenderer.this.minecraft.getCameraEntity());
                } else {
                    GameRenderer.this.loadEffect(GameRenderer.EFFECTS[GameRenderer.this.effectIndex]);
                }
            }

            @Override
            public String getName() {
                return "Shader Loader";
            }
        };
    }

    public void preloadUiShader(ResourceProvider resourceProvider0) {
        if (this.blitShader != null) {
            throw new RuntimeException("Blit shader already preloaded");
        } else {
            try {
                this.blitShader = new ShaderInstance(resourceProvider0, "blit_screen", DefaultVertexFormat.BLIT_SCREEN);
            } catch (IOException var3) {
                throw new RuntimeException("could not preload blit shader", var3);
            }
            rendertypeGuiShader = this.preloadShader(resourceProvider0, "rendertype_gui", DefaultVertexFormat.POSITION_COLOR);
            rendertypeGuiOverlayShader = this.preloadShader(resourceProvider0, "rendertype_gui_overlay", DefaultVertexFormat.POSITION_COLOR);
            positionShader = this.preloadShader(resourceProvider0, "position", DefaultVertexFormat.POSITION);
            positionColorShader = this.preloadShader(resourceProvider0, "position_color", DefaultVertexFormat.POSITION_COLOR);
            positionColorTexShader = this.preloadShader(resourceProvider0, "position_color_tex", DefaultVertexFormat.POSITION_COLOR_TEX);
            positionTexShader = this.preloadShader(resourceProvider0, "position_tex", DefaultVertexFormat.POSITION_TEX);
            positionTexColorShader = this.preloadShader(resourceProvider0, "position_tex_color", DefaultVertexFormat.POSITION_TEX_COLOR);
            rendertypeTextShader = this.preloadShader(resourceProvider0, "rendertype_text", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
        }
    }

    private ShaderInstance preloadShader(ResourceProvider resourceProvider0, String string1, VertexFormat vertexFormat2) {
        try {
            ShaderInstance $$3 = new ShaderInstance(resourceProvider0, string1, vertexFormat2);
            this.shaders.put(string1, $$3);
            return $$3;
        } catch (Exception var5) {
            throw new IllegalStateException("could not preload shader " + string1, var5);
        }
    }

    void reloadShaders(ResourceProvider resourceProvider0) {
        RenderSystem.assertOnRenderThread();
        List<Program> $$1 = Lists.newArrayList();
        $$1.addAll(Program.Type.FRAGMENT.getPrograms().values());
        $$1.addAll(Program.Type.VERTEX.getPrograms().values());
        $$1.forEach(Program::m_85543_);
        List<Pair<ShaderInstance, Consumer<ShaderInstance>>> $$2 = Lists.newArrayListWithCapacity(this.shaders.size());
        try {
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "particle", DefaultVertexFormat.PARTICLE), (Consumer) p_172714_ -> particleShader = p_172714_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "position", DefaultVertexFormat.POSITION), (Consumer) p_172711_ -> positionShader = p_172711_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "position_color", DefaultVertexFormat.POSITION_COLOR), (Consumer) p_172708_ -> positionColorShader = p_172708_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "position_color_lightmap", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP), (Consumer) p_172705_ -> positionColorLightmapShader = p_172705_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "position_color_tex", DefaultVertexFormat.POSITION_COLOR_TEX), (Consumer) p_172702_ -> positionColorTexShader = p_172702_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "position_color_tex_lightmap", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP), (Consumer) p_172699_ -> positionColorTexLightmapShader = p_172699_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "position_tex", DefaultVertexFormat.POSITION_TEX), (Consumer) p_172696_ -> positionTexShader = p_172696_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "position_tex_color", DefaultVertexFormat.POSITION_TEX_COLOR), (Consumer) p_172693_ -> positionTexColorShader = p_172693_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "position_tex_color_normal", DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL), (Consumer) p_172690_ -> positionTexColorNormalShader = p_172690_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "position_tex_lightmap_color", DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR), (Consumer) p_172687_ -> positionTexLightmapColorShader = p_172687_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_solid", DefaultVertexFormat.BLOCK), (Consumer) p_172684_ -> rendertypeSolidShader = p_172684_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_cutout_mipped", DefaultVertexFormat.BLOCK), (Consumer) p_172681_ -> rendertypeCutoutMippedShader = p_172681_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_cutout", DefaultVertexFormat.BLOCK), (Consumer) p_172678_ -> rendertypeCutoutShader = p_172678_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_translucent", DefaultVertexFormat.BLOCK), (Consumer) p_172675_ -> rendertypeTranslucentShader = p_172675_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_translucent_moving_block", DefaultVertexFormat.BLOCK), (Consumer) p_172672_ -> rendertypeTranslucentMovingBlockShader = p_172672_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_translucent_no_crumbling", DefaultVertexFormat.BLOCK), (Consumer) p_172669_ -> rendertypeTranslucentNoCrumblingShader = p_172669_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_armor_cutout_no_cull", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172666_ -> rendertypeArmorCutoutNoCullShader = p_172666_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_entity_solid", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172663_ -> rendertypeEntitySolidShader = p_172663_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_entity_cutout", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172660_ -> rendertypeEntityCutoutShader = p_172660_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_entity_cutout_no_cull", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172657_ -> rendertypeEntityCutoutNoCullShader = p_172657_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_entity_cutout_no_cull_z_offset", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172654_ -> rendertypeEntityCutoutNoCullZOffsetShader = p_172654_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_item_entity_translucent_cull", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172651_ -> rendertypeItemEntityTranslucentCullShader = p_172651_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_entity_translucent_cull", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172648_ -> rendertypeEntityTranslucentCullShader = p_172648_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_entity_translucent", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172645_ -> rendertypeEntityTranslucentShader = p_172645_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_entity_translucent_emissive", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172642_ -> rendertypeEntityTranslucentEmissiveShader = p_172642_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_entity_smooth_cutout", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172639_ -> rendertypeEntitySmoothCutoutShader = p_172639_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_beacon_beam", DefaultVertexFormat.BLOCK), (Consumer) p_172840_ -> rendertypeBeaconBeamShader = p_172840_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_entity_decal", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172837_ -> rendertypeEntityDecalShader = p_172837_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_entity_no_outline", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172834_ -> rendertypeEntityNoOutlineShader = p_172834_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_entity_shadow", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172831_ -> rendertypeEntityShadowShader = p_172831_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_entity_alpha", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172828_ -> rendertypeEntityAlphaShader = p_172828_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_eyes", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172825_ -> rendertypeEyesShader = p_172825_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_energy_swirl", DefaultVertexFormat.NEW_ENTITY), (Consumer) p_172822_ -> rendertypeEnergySwirlShader = p_172822_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_leash", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP), (Consumer) p_172819_ -> rendertypeLeashShader = p_172819_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_water_mask", DefaultVertexFormat.POSITION), (Consumer) p_172816_ -> rendertypeWaterMaskShader = p_172816_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_outline", DefaultVertexFormat.POSITION_COLOR_TEX), (Consumer) p_172813_ -> rendertypeOutlineShader = p_172813_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_armor_glint", DefaultVertexFormat.POSITION_TEX), (Consumer) p_172810_ -> rendertypeArmorGlintShader = p_172810_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_armor_entity_glint", DefaultVertexFormat.POSITION_TEX), (Consumer) p_172807_ -> rendertypeArmorEntityGlintShader = p_172807_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_glint_translucent", DefaultVertexFormat.POSITION_TEX), (Consumer) p_172805_ -> rendertypeGlintTranslucentShader = p_172805_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_glint", DefaultVertexFormat.POSITION_TEX), (Consumer) p_172803_ -> rendertypeGlintShader = p_172803_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_glint_direct", DefaultVertexFormat.POSITION_TEX), (Consumer) p_172801_ -> rendertypeGlintDirectShader = p_172801_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_entity_glint", DefaultVertexFormat.POSITION_TEX), (Consumer) p_172799_ -> rendertypeEntityGlintShader = p_172799_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_entity_glint_direct", DefaultVertexFormat.POSITION_TEX), (Consumer) p_172796_ -> rendertypeEntityGlintDirectShader = p_172796_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_text", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP), (Consumer) p_172794_ -> rendertypeTextShader = p_172794_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_text_background", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP), (Consumer) p_269657_ -> rendertypeTextBackgroundShader = p_269657_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_text_intensity", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP), (Consumer) p_172792_ -> rendertypeTextIntensityShader = p_172792_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_text_see_through", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP), (Consumer) p_172789_ -> rendertypeTextSeeThroughShader = p_172789_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_text_background_see_through", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP), (Consumer) p_269656_ -> rendertypeTextBackgroundSeeThroughShader = p_269656_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_text_intensity_see_through", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP), (Consumer) p_172787_ -> rendertypeTextIntensitySeeThroughShader = p_172787_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_lightning", DefaultVertexFormat.POSITION_COLOR), (Consumer) p_172785_ -> rendertypeLightningShader = p_172785_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_tripwire", DefaultVertexFormat.BLOCK), (Consumer) p_172782_ -> rendertypeTripwireShader = p_172782_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_end_portal", DefaultVertexFormat.POSITION), (Consumer) p_172778_ -> rendertypeEndPortalShader = p_172778_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_end_gateway", DefaultVertexFormat.POSITION), (Consumer) p_172774_ -> rendertypeEndGatewayShader = p_172774_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_lines", DefaultVertexFormat.POSITION_COLOR_NORMAL), (Consumer) p_172733_ -> rendertypeLinesShader = p_172733_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_crumbling", DefaultVertexFormat.BLOCK), (Consumer) p_234230_ -> rendertypeCrumblingShader = p_234230_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_gui", DefaultVertexFormat.POSITION_COLOR), (Consumer) p_286148_ -> rendertypeGuiShader = p_286148_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_gui_overlay", DefaultVertexFormat.POSITION_COLOR), (Consumer) p_286146_ -> rendertypeGuiOverlayShader = p_286146_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_gui_text_highlight", DefaultVertexFormat.POSITION_COLOR), (Consumer) p_286145_ -> rendertypeGuiTextHighlightShader = p_286145_));
            $$2.add(Pair.of(new ShaderInstance(resourceProvider0, "rendertype_gui_ghost_recipe_overlay", DefaultVertexFormat.POSITION_COLOR), (Consumer) p_286147_ -> rendertypeGuiGhostRecipeOverlayShader = p_286147_));
        } catch (IOException var5) {
            $$2.forEach(p_172729_ -> ((ShaderInstance) p_172729_.getFirst()).close());
            throw new RuntimeException("could not reload shaders", var5);
        }
        this.shutdownShaders();
        $$2.forEach(p_234225_ -> {
            ShaderInstance $$1x = (ShaderInstance) p_234225_.getFirst();
            this.shaders.put($$1x.getName(), $$1x);
            ((Consumer) p_234225_.getSecond()).accept($$1x);
        });
    }

    private void shutdownShaders() {
        RenderSystem.assertOnRenderThread();
        this.shaders.values().forEach(ShaderInstance::close);
        this.shaders.clear();
    }

    @Nullable
    public ShaderInstance getShader(@Nullable String string0) {
        return string0 == null ? null : (ShaderInstance) this.shaders.get(string0);
    }

    public void tick() {
        this.tickFov();
        this.lightTexture.tick();
        if (this.minecraft.getCameraEntity() == null) {
            this.minecraft.setCameraEntity(this.minecraft.player);
        }
        this.mainCamera.tick();
        this.tick++;
        this.itemInHandRenderer.tick();
        this.minecraft.levelRenderer.tickRain(this.mainCamera);
        this.darkenWorldAmountO = this.darkenWorldAmount;
        if (this.minecraft.gui.getBossOverlay().shouldDarkenScreen()) {
            this.darkenWorldAmount += 0.05F;
            if (this.darkenWorldAmount > 1.0F) {
                this.darkenWorldAmount = 1.0F;
            }
        } else if (this.darkenWorldAmount > 0.0F) {
            this.darkenWorldAmount -= 0.0125F;
        }
        if (this.itemActivationTicks > 0) {
            this.itemActivationTicks--;
            if (this.itemActivationTicks == 0) {
                this.itemActivationItem = null;
            }
        }
    }

    @Nullable
    public PostChain currentEffect() {
        return this.postEffect;
    }

    public void resize(int int0, int int1) {
        if (this.postEffect != null) {
            this.postEffect.resize(int0, int1);
        }
        this.minecraft.levelRenderer.resize(int0, int1);
    }

    public void pick(float float0) {
        Entity $$1 = this.minecraft.getCameraEntity();
        if ($$1 != null) {
            if (this.minecraft.level != null) {
                this.minecraft.getProfiler().push("pick");
                this.minecraft.crosshairPickEntity = null;
                double $$2 = (double) this.minecraft.gameMode.getPickRange();
                this.minecraft.hitResult = $$1.pick($$2, float0, false);
                Vec3 $$3 = $$1.getEyePosition(float0);
                boolean $$4 = false;
                int $$5 = 3;
                double $$6 = $$2;
                if (this.minecraft.gameMode.hasFarPickRange()) {
                    $$6 = 6.0;
                    $$2 = $$6;
                } else {
                    if ($$2 > 3.0) {
                        $$4 = true;
                    }
                    $$2 = $$2;
                }
                $$6 *= $$6;
                if (this.minecraft.hitResult != null) {
                    $$6 = this.minecraft.hitResult.getLocation().distanceToSqr($$3);
                }
                Vec3 $$7 = $$1.getViewVector(1.0F);
                Vec3 $$8 = $$3.add($$7.x * $$2, $$7.y * $$2, $$7.z * $$2);
                float $$9 = 1.0F;
                AABB $$10 = $$1.getBoundingBox().expandTowards($$7.scale($$2)).inflate(1.0, 1.0, 1.0);
                EntityHitResult $$11 = ProjectileUtil.getEntityHitResult($$1, $$3, $$8, $$10, p_234237_ -> !p_234237_.isSpectator() && p_234237_.isPickable(), $$6);
                if ($$11 != null) {
                    Entity $$12 = $$11.getEntity();
                    Vec3 $$13 = $$11.m_82450_();
                    double $$14 = $$3.distanceToSqr($$13);
                    if ($$4 && $$14 > 9.0) {
                        this.minecraft.hitResult = BlockHitResult.miss($$13, Direction.getNearest($$7.x, $$7.y, $$7.z), BlockPos.containing($$13));
                    } else if ($$14 < $$6 || this.minecraft.hitResult == null) {
                        this.minecraft.hitResult = $$11;
                        if ($$12 instanceof LivingEntity || $$12 instanceof ItemFrame) {
                            this.minecraft.crosshairPickEntity = $$12;
                        }
                    }
                }
                this.minecraft.getProfiler().pop();
            }
        }
    }

    private void tickFov() {
        float $$0 = 1.0F;
        if (this.minecraft.getCameraEntity() instanceof AbstractClientPlayer) {
            AbstractClientPlayer $$1 = (AbstractClientPlayer) this.minecraft.getCameraEntity();
            $$0 = $$1.getFieldOfViewModifier();
        }
        this.oldFov = this.fov;
        this.fov = this.fov + ($$0 - this.fov) * 0.5F;
        if (this.fov > 1.5F) {
            this.fov = 1.5F;
        }
        if (this.fov < 0.1F) {
            this.fov = 0.1F;
        }
    }

    private double getFov(Camera camera0, float float1, boolean boolean2) {
        if (this.panoramicMode) {
            return 90.0;
        } else {
            double $$3 = 70.0;
            if (boolean2) {
                $$3 = (double) this.minecraft.options.fov().get().intValue();
                $$3 *= (double) Mth.lerp(float1, this.oldFov, this.fov);
            }
            if (camera0.getEntity() instanceof LivingEntity && ((LivingEntity) camera0.getEntity()).isDeadOrDying()) {
                float $$4 = Math.min((float) ((LivingEntity) camera0.getEntity()).deathTime + float1, 20.0F);
                $$3 /= (double) ((1.0F - 500.0F / ($$4 + 500.0F)) * 2.0F + 1.0F);
            }
            FogType $$5 = camera0.getFluidInCamera();
            if ($$5 == FogType.LAVA || $$5 == FogType.WATER) {
                $$3 *= Mth.lerp(this.minecraft.options.fovEffectScale().get(), 1.0, 0.85714287F);
            }
            return $$3;
        }
    }

    private void bobHurt(PoseStack poseStack0, float float1) {
        if (this.minecraft.getCameraEntity() instanceof LivingEntity) {
            LivingEntity $$2 = (LivingEntity) this.minecraft.getCameraEntity();
            float $$3 = (float) $$2.hurtTime - float1;
            if ($$2.isDeadOrDying()) {
                float $$4 = Math.min((float) $$2.deathTime + float1, 20.0F);
                poseStack0.mulPose(Axis.ZP.rotationDegrees(40.0F - 8000.0F / ($$4 + 200.0F)));
            }
            if ($$3 < 0.0F) {
                return;
            }
            $$3 /= (float) $$2.hurtDuration;
            $$3 = Mth.sin($$3 * $$3 * $$3 * $$3 * (float) Math.PI);
            float $$5 = $$2.getHurtDir();
            poseStack0.mulPose(Axis.YP.rotationDegrees(-$$5));
            float $$6 = (float) ((double) (-$$3) * 14.0 * this.minecraft.options.damageTiltStrength().get());
            poseStack0.mulPose(Axis.ZP.rotationDegrees($$6));
            poseStack0.mulPose(Axis.YP.rotationDegrees($$5));
        }
    }

    private void bobView(PoseStack poseStack0, float float1) {
        if (this.minecraft.getCameraEntity() instanceof Player) {
            Player $$2 = (Player) this.minecraft.getCameraEntity();
            float $$3 = $$2.f_19787_ - $$2.f_19867_;
            float $$4 = -($$2.f_19787_ + $$3 * float1);
            float $$5 = Mth.lerp(float1, $$2.oBob, $$2.bob);
            poseStack0.translate(Mth.sin($$4 * (float) Math.PI) * $$5 * 0.5F, -Math.abs(Mth.cos($$4 * (float) Math.PI) * $$5), 0.0F);
            poseStack0.mulPose(Axis.ZP.rotationDegrees(Mth.sin($$4 * (float) Math.PI) * $$5 * 3.0F));
            poseStack0.mulPose(Axis.XP.rotationDegrees(Math.abs(Mth.cos($$4 * (float) Math.PI - 0.2F) * $$5) * 5.0F));
        }
    }

    public void renderZoomed(float float0, float float1, float float2) {
        this.zoom = float0;
        this.zoomX = float1;
        this.zoomY = float2;
        this.setRenderBlockOutline(false);
        this.setRenderHand(false);
        this.renderLevel(1.0F, 0L, new PoseStack());
        this.zoom = 1.0F;
    }

    private void renderItemInHand(PoseStack poseStack0, Camera camera1, float float2) {
        if (!this.panoramicMode) {
            this.resetProjectionMatrix(this.getProjectionMatrix(this.getFov(camera1, float2, false)));
            poseStack0.setIdentity();
            poseStack0.pushPose();
            this.bobHurt(poseStack0, float2);
            if (this.minecraft.options.bobView().get()) {
                this.bobView(poseStack0, float2);
            }
            boolean $$3 = this.minecraft.getCameraEntity() instanceof LivingEntity && ((LivingEntity) this.minecraft.getCameraEntity()).isSleeping();
            if (this.minecraft.options.getCameraType().isFirstPerson() && !$$3 && !this.minecraft.options.hideGui && this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
                this.lightTexture.turnOnLightLayer();
                this.itemInHandRenderer.renderHandsWithItems(float2, poseStack0, this.renderBuffers.bufferSource(), this.minecraft.player, this.minecraft.getEntityRenderDispatcher().getPackedLightCoords(this.minecraft.player, float2));
                this.lightTexture.turnOffLightLayer();
            }
            poseStack0.popPose();
            if (this.minecraft.options.getCameraType().isFirstPerson() && !$$3) {
                ScreenEffectRenderer.renderScreenEffect(this.minecraft, poseStack0);
                this.bobHurt(poseStack0, float2);
            }
            if (this.minecraft.options.bobView().get()) {
                this.bobView(poseStack0, float2);
            }
        }
    }

    public void resetProjectionMatrix(Matrix4f matrixF0) {
        RenderSystem.setProjectionMatrix(matrixF0, VertexSorting.DISTANCE_TO_ORIGIN);
    }

    public Matrix4f getProjectionMatrix(double double0) {
        PoseStack $$1 = new PoseStack();
        $$1.last().pose().identity();
        if (this.zoom != 1.0F) {
            $$1.translate(this.zoomX, -this.zoomY, 0.0F);
            $$1.scale(this.zoom, this.zoom, 1.0F);
        }
        $$1.last().pose().mul(new Matrix4f().setPerspective((float) (double0 * (float) (Math.PI / 180.0)), (float) this.minecraft.getWindow().getWidth() / (float) this.minecraft.getWindow().getHeight(), 0.05F, this.getDepthFar()));
        return $$1.last().pose();
    }

    public float getDepthFar() {
        return this.renderDistance * 4.0F;
    }

    public static float getNightVisionScale(LivingEntity livingEntity0, float float1) {
        MobEffectInstance $$2 = livingEntity0.getEffect(MobEffects.NIGHT_VISION);
        return !$$2.endsWithin(200) ? 1.0F : 0.7F + Mth.sin(((float) $$2.getDuration() - float1) * (float) Math.PI * 0.2F) * 0.3F;
    }

    public void render(float float0, long long1, boolean boolean2) {
        if (!this.minecraft.isWindowActive() && this.minecraft.options.pauseOnLostFocus && (!this.minecraft.options.touchscreen().get() || !this.minecraft.mouseHandler.isRightPressed())) {
            if (Util.getMillis() - this.lastActiveTime > 500L) {
                this.minecraft.pauseGame(false);
            }
        } else {
            this.lastActiveTime = Util.getMillis();
        }
        if (!this.minecraft.noRender) {
            int $$3 = (int) (this.minecraft.mouseHandler.xpos() * (double) this.minecraft.getWindow().getGuiScaledWidth() / (double) this.minecraft.getWindow().getScreenWidth());
            int $$4 = (int) (this.minecraft.mouseHandler.ypos() * (double) this.minecraft.getWindow().getGuiScaledHeight() / (double) this.minecraft.getWindow().getScreenHeight());
            RenderSystem.viewport(0, 0, this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
            if (boolean2 && this.minecraft.level != null) {
                this.minecraft.getProfiler().push("level");
                this.renderLevel(float0, long1, new PoseStack());
                this.tryTakeScreenshotIfNeeded();
                this.minecraft.levelRenderer.doEntityOutline();
                if (this.postEffect != null && this.effectActive) {
                    RenderSystem.disableBlend();
                    RenderSystem.disableDepthTest();
                    RenderSystem.resetTextureMatrix();
                    this.postEffect.process(float0);
                }
                this.minecraft.getMainRenderTarget().bindWrite(true);
            }
            Window $$5 = this.minecraft.getWindow();
            RenderSystem.clear(256, Minecraft.ON_OSX);
            Matrix4f $$6 = new Matrix4f().setOrtho(0.0F, (float) ((double) $$5.getWidth() / $$5.getGuiScale()), (float) ((double) $$5.getHeight() / $$5.getGuiScale()), 0.0F, 1000.0F, 21000.0F);
            RenderSystem.setProjectionMatrix($$6, VertexSorting.ORTHOGRAPHIC_Z);
            PoseStack $$7 = RenderSystem.getModelViewStack();
            $$7.pushPose();
            $$7.setIdentity();
            $$7.translate(0.0F, 0.0F, -11000.0F);
            RenderSystem.applyModelViewMatrix();
            Lighting.setupFor3DItems();
            GuiGraphics $$8 = new GuiGraphics(this.minecraft, this.renderBuffers.bufferSource());
            if (boolean2 && this.minecraft.level != null) {
                this.minecraft.getProfiler().popPush("gui");
                if (this.minecraft.player != null) {
                    float $$9 = Mth.lerp(float0, this.minecraft.player.oSpinningEffectIntensity, this.minecraft.player.spinningEffectIntensity);
                    float $$10 = this.minecraft.options.screenEffectScale().get().floatValue();
                    if ($$9 > 0.0F && this.minecraft.player.m_21023_(MobEffects.CONFUSION) && $$10 < 1.0F) {
                        this.renderConfusionOverlay($$8, $$9 * (1.0F - $$10));
                    }
                }
                if (!this.minecraft.options.hideGui || this.minecraft.screen != null) {
                    this.renderItemActivationAnimation(this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight(), float0);
                    this.minecraft.gui.render($$8, float0);
                    RenderSystem.clear(256, Minecraft.ON_OSX);
                }
                this.minecraft.getProfiler().pop();
            }
            if (this.minecraft.getOverlay() != null) {
                try {
                    this.minecraft.getOverlay().m_88315_($$8, $$3, $$4, this.minecraft.getDeltaFrameTime());
                } catch (Throwable var16) {
                    CrashReport $$12 = CrashReport.forThrowable(var16, "Rendering overlay");
                    CrashReportCategory $$13 = $$12.addCategory("Overlay render details");
                    $$13.setDetail("Overlay name", (CrashReportDetail<String>) (() -> this.minecraft.getOverlay().getClass().getCanonicalName()));
                    throw new ReportedException($$12);
                }
            } else if (this.minecraft.screen != null) {
                try {
                    this.minecraft.screen.renderWithTooltip($$8, $$3, $$4, this.minecraft.getDeltaFrameTime());
                } catch (Throwable var15) {
                    CrashReport $$15 = CrashReport.forThrowable(var15, "Rendering screen");
                    CrashReportCategory $$16 = $$15.addCategory("Screen render details");
                    $$16.setDetail("Screen name", (CrashReportDetail<String>) (() -> this.minecraft.screen.getClass().getCanonicalName()));
                    $$16.setDetail("Mouse location", (CrashReportDetail<String>) (() -> String.format(Locale.ROOT, "Scaled: (%d, %d). Absolute: (%f, %f)", $$3, $$4, this.minecraft.mouseHandler.xpos(), this.minecraft.mouseHandler.ypos())));
                    $$16.setDetail("Screen size", (CrashReportDetail<String>) (() -> String.format(Locale.ROOT, "Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %f", this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight(), this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight(), this.minecraft.getWindow().getGuiScale())));
                    throw new ReportedException($$15);
                }
                try {
                    if (this.minecraft.screen != null) {
                        this.minecraft.screen.handleDelayedNarration();
                    }
                } catch (Throwable var14) {
                    CrashReport $$18 = CrashReport.forThrowable(var14, "Narrating screen");
                    CrashReportCategory $$19 = $$18.addCategory("Screen details");
                    $$19.setDetail("Screen name", (CrashReportDetail<String>) (() -> this.minecraft.screen.getClass().getCanonicalName()));
                    throw new ReportedException($$18);
                }
            }
            this.minecraft.getProfiler().push("toasts");
            this.minecraft.getToasts().render($$8);
            this.minecraft.getProfiler().pop();
            $$8.flush();
            $$7.popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }

    private void tryTakeScreenshotIfNeeded() {
        if (!this.hasWorldScreenshot && this.minecraft.isLocalServer()) {
            long $$0 = Util.getMillis();
            if ($$0 - this.lastScreenshotAttempt >= 1000L) {
                this.lastScreenshotAttempt = $$0;
                IntegratedServer $$1 = this.minecraft.getSingleplayerServer();
                if ($$1 != null && !$$1.m_129918_()) {
                    $$1.m_182649_().ifPresent(p_234239_ -> {
                        if (Files.isRegularFile(p_234239_, new LinkOption[0])) {
                            this.hasWorldScreenshot = true;
                        } else {
                            this.takeAutoScreenshot(p_234239_);
                        }
                    });
                }
            }
        }
    }

    private void takeAutoScreenshot(Path path0) {
        if (this.minecraft.levelRenderer.countRenderedChunks() > 10 && this.minecraft.levelRenderer.hasRenderedAllChunks()) {
            NativeImage $$1 = Screenshot.takeScreenshot(this.minecraft.getMainRenderTarget());
            Util.ioPool().execute(() -> {
                int $$2 = $$1.getWidth();
                int $$3 = $$1.getHeight();
                int $$4 = 0;
                int $$5 = 0;
                if ($$2 > $$3) {
                    $$4 = ($$2 - $$3) / 2;
                    $$2 = $$3;
                } else {
                    $$5 = ($$3 - $$2) / 2;
                    $$3 = $$2;
                }
                try (NativeImage $$6 = new NativeImage(64, 64, false)) {
                    $$1.resizeSubRectTo($$4, $$5, $$2, $$3, $$6);
                    $$6.writeToFile(path0);
                } catch (IOException var16) {
                    LOGGER.warn("Couldn't save auto screenshot", var16);
                } finally {
                    $$1.close();
                }
            });
        }
    }

    private boolean shouldRenderBlockOutline() {
        if (!this.renderBlockOutline) {
            return false;
        } else {
            Entity $$0 = this.minecraft.getCameraEntity();
            boolean $$1 = $$0 instanceof Player && !this.minecraft.options.hideGui;
            if ($$1 && !((Player) $$0).getAbilities().mayBuild) {
                ItemStack $$2 = ((LivingEntity) $$0).getMainHandItem();
                HitResult $$3 = this.minecraft.hitResult;
                if ($$3 != null && $$3.getType() == HitResult.Type.BLOCK) {
                    BlockPos $$4 = ((BlockHitResult) $$3).getBlockPos();
                    BlockState $$5 = this.minecraft.level.m_8055_($$4);
                    if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
                        $$1 = $$5.m_60750_(this.minecraft.level, $$4) != null;
                    } else {
                        BlockInWorld $$6 = new BlockInWorld(this.minecraft.level, $$4, false);
                        Registry<Block> $$7 = this.minecraft.level.m_9598_().registryOrThrow(Registries.BLOCK);
                        $$1 = !$$2.isEmpty() && ($$2.hasAdventureModeBreakTagForBlock($$7, $$6) || $$2.hasAdventureModePlaceTagForBlock($$7, $$6));
                    }
                }
            }
            return $$1;
        }
    }

    public void renderLevel(float float0, long long1, PoseStack poseStack2) {
        this.lightTexture.updateLightTexture(float0);
        if (this.minecraft.getCameraEntity() == null) {
            this.minecraft.setCameraEntity(this.minecraft.player);
        }
        this.pick(float0);
        this.minecraft.getProfiler().push("center");
        boolean $$3 = this.shouldRenderBlockOutline();
        this.minecraft.getProfiler().popPush("camera");
        Camera $$4 = this.mainCamera;
        this.renderDistance = (float) (this.minecraft.options.getEffectiveRenderDistance() * 16);
        PoseStack $$5 = new PoseStack();
        double $$6 = this.getFov($$4, float0, true);
        $$5.mulPoseMatrix(this.getProjectionMatrix($$6));
        this.bobHurt($$5, float0);
        if (this.minecraft.options.bobView().get()) {
            this.bobView($$5, float0);
        }
        float $$7 = this.minecraft.options.screenEffectScale().get().floatValue();
        float $$8 = Mth.lerp(float0, this.minecraft.player.oSpinningEffectIntensity, this.minecraft.player.spinningEffectIntensity) * $$7 * $$7;
        if ($$8 > 0.0F) {
            int $$9 = this.minecraft.player.m_21023_(MobEffects.CONFUSION) ? 7 : 20;
            float $$10 = 5.0F / ($$8 * $$8 + 5.0F) - $$8 * 0.04F;
            $$10 *= $$10;
            Axis $$11 = Axis.of(new Vector3f(0.0F, Mth.SQRT_OF_TWO / 2.0F, Mth.SQRT_OF_TWO / 2.0F));
            $$5.mulPose($$11.rotationDegrees(((float) this.tick + float0) * (float) $$9));
            $$5.scale(1.0F / $$10, 1.0F, 1.0F);
            float $$12 = -((float) this.tick + float0) * (float) $$9;
            $$5.mulPose($$11.rotationDegrees($$12));
        }
        Matrix4f $$13 = $$5.last().pose();
        this.resetProjectionMatrix($$13);
        $$4.setup(this.minecraft.level, (Entity) (this.minecraft.getCameraEntity() == null ? this.minecraft.player : this.minecraft.getCameraEntity()), !this.minecraft.options.getCameraType().isFirstPerson(), this.minecraft.options.getCameraType().isMirrored(), float0);
        poseStack2.mulPose(Axis.XP.rotationDegrees($$4.getXRot()));
        poseStack2.mulPose(Axis.YP.rotationDegrees($$4.getYRot() + 180.0F));
        Matrix3f $$14 = new Matrix3f(poseStack2.last().normal()).invert();
        RenderSystem.setInverseViewRotationMatrix($$14);
        this.minecraft.levelRenderer.prepareCullFrustum(poseStack2, $$4.getPosition(), this.getProjectionMatrix(Math.max($$6, (double) this.minecraft.options.fov().get().intValue())));
        this.minecraft.levelRenderer.renderLevel(poseStack2, float0, long1, $$3, $$4, this, this.lightTexture, $$13);
        this.minecraft.getProfiler().popPush("hand");
        if (this.renderHand) {
            RenderSystem.clear(256, Minecraft.ON_OSX);
            this.renderItemInHand(poseStack2, $$4, float0);
        }
        this.minecraft.getProfiler().pop();
    }

    public void resetData() {
        this.itemActivationItem = null;
        this.mapRenderer.resetData();
        this.mainCamera.reset();
        this.hasWorldScreenshot = false;
    }

    public MapRenderer getMapRenderer() {
        return this.mapRenderer;
    }

    public void displayItemActivation(ItemStack itemStack0) {
        this.itemActivationItem = itemStack0;
        this.itemActivationTicks = 40;
        this.itemActivationOffX = this.random.nextFloat() * 2.0F - 1.0F;
        this.itemActivationOffY = this.random.nextFloat() * 2.0F - 1.0F;
    }

    private void renderItemActivationAnimation(int int0, int int1, float float2) {
        if (this.itemActivationItem != null && this.itemActivationTicks > 0) {
            int $$3 = 40 - this.itemActivationTicks;
            float $$4 = ((float) $$3 + float2) / 40.0F;
            float $$5 = $$4 * $$4;
            float $$6 = $$4 * $$5;
            float $$7 = 10.25F * $$6 * $$5 - 24.95F * $$5 * $$5 + 25.5F * $$6 - 13.8F * $$5 + 4.0F * $$4;
            float $$8 = $$7 * (float) Math.PI;
            float $$9 = this.itemActivationOffX * (float) (int0 / 4);
            float $$10 = this.itemActivationOffY * (float) (int1 / 4);
            RenderSystem.enableDepthTest();
            RenderSystem.disableCull();
            PoseStack $$11 = new PoseStack();
            $$11.pushPose();
            $$11.translate((float) (int0 / 2) + $$9 * Mth.abs(Mth.sin($$8 * 2.0F)), (float) (int1 / 2) + $$10 * Mth.abs(Mth.sin($$8 * 2.0F)), -50.0F);
            float $$12 = 50.0F + 175.0F * Mth.sin($$8);
            $$11.scale($$12, -$$12, $$12);
            $$11.mulPose(Axis.YP.rotationDegrees(900.0F * Mth.abs(Mth.sin($$8))));
            $$11.mulPose(Axis.XP.rotationDegrees(6.0F * Mth.cos($$4 * 8.0F)));
            $$11.mulPose(Axis.ZP.rotationDegrees(6.0F * Mth.cos($$4 * 8.0F)));
            MultiBufferSource.BufferSource $$13 = this.renderBuffers.bufferSource();
            this.minecraft.getItemRenderer().renderStatic(this.itemActivationItem, ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, $$11, $$13, this.minecraft.level, 0);
            $$11.popPose();
            $$13.endBatch();
            RenderSystem.enableCull();
            RenderSystem.disableDepthTest();
        }
    }

    private void renderConfusionOverlay(GuiGraphics guiGraphics0, float float1) {
        int $$2 = guiGraphics0.guiWidth();
        int $$3 = guiGraphics0.guiHeight();
        guiGraphics0.pose().pushPose();
        float $$4 = Mth.lerp(float1, 2.0F, 1.0F);
        guiGraphics0.pose().translate((float) $$2 / 2.0F, (float) $$3 / 2.0F, 0.0F);
        guiGraphics0.pose().scale($$4, $$4, $$4);
        guiGraphics0.pose().translate((float) (-$$2) / 2.0F, (float) (-$$3) / 2.0F, 0.0F);
        float $$5 = 0.2F * float1;
        float $$6 = 0.4F * float1;
        float $$7 = 0.2F * float1;
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        guiGraphics0.setColor($$5, $$6, $$7, 1.0F);
        guiGraphics0.blit(NAUSEA_LOCATION, 0, 0, -90, 0.0F, 0.0F, $$2, $$3, $$2, $$3);
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics0.pose().popPose();
    }

    public Minecraft getMinecraft() {
        return this.minecraft;
    }

    public float getDarkenWorldAmount(float float0) {
        return Mth.lerp(float0, this.darkenWorldAmountO, this.darkenWorldAmount);
    }

    public float getRenderDistance() {
        return this.renderDistance;
    }

    public Camera getMainCamera() {
        return this.mainCamera;
    }

    public LightTexture lightTexture() {
        return this.lightTexture;
    }

    public OverlayTexture overlayTexture() {
        return this.overlayTexture;
    }

    @Nullable
    public static ShaderInstance getPositionShader() {
        return positionShader;
    }

    @Nullable
    public static ShaderInstance getPositionColorShader() {
        return positionColorShader;
    }

    @Nullable
    public static ShaderInstance getPositionColorTexShader() {
        return positionColorTexShader;
    }

    @Nullable
    public static ShaderInstance getPositionTexShader() {
        return positionTexShader;
    }

    @Nullable
    public static ShaderInstance getPositionTexColorShader() {
        return positionTexColorShader;
    }

    @Nullable
    public static ShaderInstance getParticleShader() {
        return particleShader;
    }

    @Nullable
    public static ShaderInstance getPositionColorLightmapShader() {
        return positionColorLightmapShader;
    }

    @Nullable
    public static ShaderInstance getPositionColorTexLightmapShader() {
        return positionColorTexLightmapShader;
    }

    @Nullable
    public static ShaderInstance getPositionTexColorNormalShader() {
        return positionTexColorNormalShader;
    }

    @Nullable
    public static ShaderInstance getPositionTexLightmapColorShader() {
        return positionTexLightmapColorShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeSolidShader() {
        return rendertypeSolidShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeCutoutMippedShader() {
        return rendertypeCutoutMippedShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeCutoutShader() {
        return rendertypeCutoutShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeTranslucentShader() {
        return rendertypeTranslucentShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeTranslucentMovingBlockShader() {
        return rendertypeTranslucentMovingBlockShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeTranslucentNoCrumblingShader() {
        return rendertypeTranslucentNoCrumblingShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeArmorCutoutNoCullShader() {
        return rendertypeArmorCutoutNoCullShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEntitySolidShader() {
        return rendertypeEntitySolidShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEntityCutoutShader() {
        return rendertypeEntityCutoutShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEntityCutoutNoCullShader() {
        return rendertypeEntityCutoutNoCullShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEntityCutoutNoCullZOffsetShader() {
        return rendertypeEntityCutoutNoCullZOffsetShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeItemEntityTranslucentCullShader() {
        return rendertypeItemEntityTranslucentCullShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEntityTranslucentCullShader() {
        return rendertypeEntityTranslucentCullShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEntityTranslucentShader() {
        return rendertypeEntityTranslucentShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEntityTranslucentEmissiveShader() {
        return rendertypeEntityTranslucentEmissiveShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEntitySmoothCutoutShader() {
        return rendertypeEntitySmoothCutoutShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeBeaconBeamShader() {
        return rendertypeBeaconBeamShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEntityDecalShader() {
        return rendertypeEntityDecalShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEntityNoOutlineShader() {
        return rendertypeEntityNoOutlineShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEntityShadowShader() {
        return rendertypeEntityShadowShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEntityAlphaShader() {
        return rendertypeEntityAlphaShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEyesShader() {
        return rendertypeEyesShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEnergySwirlShader() {
        return rendertypeEnergySwirlShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeLeashShader() {
        return rendertypeLeashShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeWaterMaskShader() {
        return rendertypeWaterMaskShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeOutlineShader() {
        return rendertypeOutlineShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeArmorGlintShader() {
        return rendertypeArmorGlintShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeArmorEntityGlintShader() {
        return rendertypeArmorEntityGlintShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeGlintTranslucentShader() {
        return rendertypeGlintTranslucentShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeGlintShader() {
        return rendertypeGlintShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeGlintDirectShader() {
        return rendertypeGlintDirectShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEntityGlintShader() {
        return rendertypeEntityGlintShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEntityGlintDirectShader() {
        return rendertypeEntityGlintDirectShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeTextShader() {
        return rendertypeTextShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeTextBackgroundShader() {
        return rendertypeTextBackgroundShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeTextIntensityShader() {
        return rendertypeTextIntensityShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeTextSeeThroughShader() {
        return rendertypeTextSeeThroughShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeTextBackgroundSeeThroughShader() {
        return rendertypeTextBackgroundSeeThroughShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeTextIntensitySeeThroughShader() {
        return rendertypeTextIntensitySeeThroughShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeLightningShader() {
        return rendertypeLightningShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeTripwireShader() {
        return rendertypeTripwireShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEndPortalShader() {
        return rendertypeEndPortalShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeEndGatewayShader() {
        return rendertypeEndGatewayShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeLinesShader() {
        return rendertypeLinesShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeCrumblingShader() {
        return rendertypeCrumblingShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeGuiShader() {
        return rendertypeGuiShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeGuiOverlayShader() {
        return rendertypeGuiOverlayShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeGuiTextHighlightShader() {
        return rendertypeGuiTextHighlightShader;
    }

    @Nullable
    public static ShaderInstance getRendertypeGuiGhostRecipeOverlayShader() {
        return rendertypeGuiGhostRecipeOverlayShader;
    }

    public static record ResourceCache(ResourceProvider f_244315_, Map<ResourceLocation, Resource> f_243825_) implements ResourceProvider {

        private final ResourceProvider original;

        private final Map<ResourceLocation, Resource> cache;

        public ResourceCache(ResourceProvider f_244315_, Map<ResourceLocation, Resource> f_243825_) {
            this.original = f_244315_;
            this.cache = f_243825_;
        }

        @Override
        public Optional<Resource> getResource(ResourceLocation p_251007_) {
            Resource $$1 = (Resource) this.cache.get(p_251007_);
            return $$1 != null ? Optional.of($$1) : this.original.getResource(p_251007_);
        }
    }
}