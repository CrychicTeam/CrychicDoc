package software.bernie.geckolib.cache.texture;

import com.mojang.blaze3d.pipeline.RenderCall;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.resource.GeoGlowingTextureMeta;

public class AutoGlowingTexture extends GeoAbstractTexture {

    private static final RenderStateShard.ShaderStateShard SHADER_STATE = new RenderStateShard.ShaderStateShard(GameRenderer::m_234223_);

    private static final RenderStateShard.TransparencyStateShard TRANSPARENCY_STATE = new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    private static final RenderStateShard.WriteMaskStateShard WRITE_MASK = new RenderStateShard.WriteMaskStateShard(true, true);

    private static final Function<ResourceLocation, RenderType> RENDER_TYPE_FUNCTION = Util.memoize((Function<ResourceLocation, RenderType>) (texture -> {
        RenderStateShard.TextureStateShard textureState = new RenderStateShard.TextureStateShard(texture, false, false);
        return RenderType.create("geo_glowing_layer", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(SHADER_STATE).setTextureState(textureState).setTransparencyState(TRANSPARENCY_STATE).setWriteMaskState(WRITE_MASK).createCompositeState(false));
    }));

    private static final String APPENDIX = "_glowmask";

    protected final ResourceLocation textureBase;

    protected final ResourceLocation glowLayer;

    public AutoGlowingTexture(ResourceLocation originalLocation, ResourceLocation location) {
        this.textureBase = originalLocation;
        this.glowLayer = location;
    }

    private static ResourceLocation getEmissiveResource(ResourceLocation baseResource) {
        ResourceLocation path = appendToPath(baseResource, "_glowmask");
        generateTexture(path, textureManager -> textureManager.register(path, new AutoGlowingTexture(baseResource, path)));
        return path;
    }

    @Nullable
    @Override
    protected RenderCall loadTexture(ResourceManager resourceManager, Minecraft mc) throws IOException {
        AbstractTexture originalTexture;
        try {
            originalTexture = (AbstractTexture) mc.m_18691_(() -> mc.getTextureManager().getTexture(this.textureBase)).get();
        } catch (ExecutionException | InterruptedException var14) {
            throw new IOException("Failed to load original texture: " + this.textureBase, var14);
        }
        Resource textureBaseResource = (Resource) resourceManager.m_213713_(this.textureBase).get();
        NativeImage baseImage = originalTexture instanceof DynamicTexture dynamicTexture ? dynamicTexture.getPixels() : NativeImage.read(textureBaseResource.open());
        NativeImage glowImage = null;
        Optional<TextureMetadataSection> textureBaseMeta = textureBaseResource.metadata().getSection(TextureMetadataSection.SERIALIZER);
        boolean blur = textureBaseMeta.isPresent() && ((TextureMetadataSection) textureBaseMeta.get()).isBlur();
        boolean clamp = textureBaseMeta.isPresent() && ((TextureMetadataSection) textureBaseMeta.get()).isClamp();
        try {
            Optional<Resource> glowLayerResource = resourceManager.m_213713_(this.glowLayer);
            GeoGlowingTextureMeta glowLayerMeta = null;
            if (glowLayerResource.isPresent()) {
                glowImage = NativeImage.read(((Resource) glowLayerResource.get()).open());
                glowLayerMeta = GeoGlowingTextureMeta.fromExistingImage(glowImage);
            } else {
                Optional<GeoGlowingTextureMeta> meta = textureBaseResource.metadata().getSection(GeoGlowingTextureMeta.DESERIALIZER);
                if (meta.isPresent()) {
                    glowLayerMeta = (GeoGlowingTextureMeta) meta.get();
                    glowImage = new NativeImage(baseImage.getWidth(), baseImage.getHeight(), true);
                }
            }
            if (glowLayerMeta != null) {
                glowLayerMeta.createImageMask(baseImage, glowImage);
                if (!FMLEnvironment.production) {
                    this.printDebugImageToDisk(this.textureBase, baseImage);
                    this.printDebugImageToDisk(this.glowLayer, glowImage);
                }
            }
        } catch (IOException var13) {
            GeckoLib.LOGGER.warn("Resource failed to open for glowlayer meta: {}", this.glowLayer, var13);
        }
        NativeImage mask = glowImage;
        return mask == null ? null : () -> {
            uploadSimple(this.m_117963_(), mask, blur, clamp);
            if (originalTexture instanceof DynamicTexture dynamicTexturex) {
                dynamicTexturex.upload();
            } else {
                uploadSimple(originalTexture.getId(), baseImage, blur, clamp);
            }
        };
    }

    public static RenderType getRenderType(ResourceLocation texture) {
        return (RenderType) RENDER_TYPE_FUNCTION.apply(getEmissiveResource(texture));
    }
}