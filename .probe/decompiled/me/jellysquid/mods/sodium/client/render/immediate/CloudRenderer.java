package me.jellysquid.mods.sodium.client.render.immediate;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.IOException;
import java.io.InputStream;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.caffeinemc.mods.sodium.api.util.ColorMixer;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.caffeinemc.mods.sodium.api.vertex.format.common.ColorVertex;
import net.minecraft.client.Camera;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.embeddedt.embeddium.render.ShaderModBridge;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public class CloudRenderer {

    private static final ResourceLocation CLOUDS_TEXTURE_ID = new ResourceLocation("textures/environment/clouds.png");

    private static final int CLOUD_COLOR_NEG_Y = ColorABGR.pack(0.7F, 0.7F, 0.7F, 1.0F);

    private static final int CLOUD_COLOR_POS_Y = ColorABGR.pack(1.0F, 1.0F, 1.0F, 1.0F);

    private static final int CLOUD_COLOR_NEG_X = ColorABGR.pack(0.9F, 0.9F, 0.9F, 1.0F);

    private static final int CLOUD_COLOR_POS_X = ColorABGR.pack(0.9F, 0.9F, 0.9F, 1.0F);

    private static final int CLOUD_COLOR_NEG_Z = ColorABGR.pack(0.8F, 0.8F, 0.8F, 1.0F);

    private static final int CLOUD_COLOR_POS_Z = ColorABGR.pack(0.8F, 0.8F, 0.8F, 1.0F);

    private static final int DIR_NEG_Y = 1;

    private static final int DIR_POS_Y = 2;

    private static final int DIR_NEG_X = 4;

    private static final int DIR_POS_X = 8;

    private static final int DIR_NEG_Z = 16;

    private static final int DIR_POS_Z = 32;

    private static final int MAX_SINGLE_CLOUD_SIZE = 3072;

    private static final int CLOUD_PIXELS_TO_FOG_DISTANCE = 2048;

    private static final float CLOUD_PIXELS_TO_MINIMUM_RENDER_DISTANCE = 0.125F;

    private static final float CLOUD_PIXELS_TO_MAXIMUM_RENDER_DISTANCE = 0.0078125F;

    private VertexBuffer vertexBuffer;

    private CloudRenderer.CloudEdges edges;

    private ShaderInstance shader;

    private final FogRenderer.FogData fogData = new FogRenderer.FogData(FogRenderer.FogMode.FOG_TERRAIN);

    private int prevCenterCellX;

    private int prevCenterCellY;

    private int cachedRenderDistance;

    private float cloudSizeX;

    private float cloudSizeZ;

    private float fogDistanceMultiplier;

    private int cloudDistanceMinimum;

    private int cloudDistanceMaximum;

    private CloudStatus cloudRenderMode;

    public CloudRenderer(ResourceProvider factory) {
        this.reloadTextures(factory);
    }

    public void render(@Nullable ClientLevel world, LocalPlayer player, PoseStack matrices, Matrix4f projectionMatrix, float ticks, float tickDelta, double cameraX, double cameraY, double cameraZ) {
        if (world != null) {
            float cloudHeight = world.effects().getCloudHeight();
            if (!Float.isNaN(cloudHeight)) {
                Vec3 color = world.getCloudColor(tickDelta);
                double cloudTime = (double) ((ticks + tickDelta) * 0.03F);
                double cloudCenterX = cameraX + cloudTime;
                double cloudCenterZ = cameraZ + 3.96;
                int renderDistance = Minecraft.getInstance().options.getEffectiveRenderDistance();
                int cloudDistance = Math.max(this.cloudDistanceMinimum, renderDistance * this.cloudDistanceMaximum + 9);
                int centerCellX = (int) Math.floor(cloudCenterX / (double) this.cloudSizeX);
                int centerCellZ = (int) Math.floor(cloudCenterZ / (double) this.cloudSizeZ);
                if (this.vertexBuffer == null || this.prevCenterCellX != centerCellX || this.prevCenterCellY != centerCellZ || this.cachedRenderDistance != renderDistance || this.cloudRenderMode != Minecraft.getInstance().options.getCloudsType()) {
                    BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
                    bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                    this.cloudRenderMode = Minecraft.getInstance().options.getCloudsType();
                    this.rebuildGeometry(bufferBuilder, cloudDistance, centerCellX, centerCellZ);
                    if (this.vertexBuffer == null) {
                        this.vertexBuffer = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
                    }
                    this.vertexBuffer.bind();
                    this.vertexBuffer.upload(bufferBuilder.end());
                    VertexBuffer.unbind();
                    this.prevCenterCellX = centerCellX;
                    this.prevCenterCellY = centerCellZ;
                    this.cachedRenderDistance = renderDistance;
                }
                float previousEnd = RenderSystem.getShaderFogEnd();
                float previousStart = RenderSystem.getShaderFogStart();
                this.fogData.end = (float) cloudDistance * this.fogDistanceMultiplier;
                this.fogData.start = (float) cloudDistance * this.fogDistanceMultiplier - 16.0F;
                this.applyFogModifiers(world, this.fogData, player, (int) ((float) cloudDistance * this.fogDistanceMultiplier), tickDelta);
                RenderSystem.setShaderFogEnd(this.fogData.end);
                RenderSystem.setShaderFogStart(this.fogData.start);
                float translateX = (float) (cloudCenterX - (double) ((float) centerCellX * this.cloudSizeX));
                float translateZ = (float) (cloudCenterZ - (double) ((float) centerCellZ * this.cloudSizeZ));
                RenderSystem.enableDepthTest();
                this.vertexBuffer.bind();
                boolean insideClouds = cameraY < (double) (cloudHeight + 4.5F) && cameraY > (double) (cloudHeight - 0.5F);
                boolean fastClouds = this.cloudRenderMode == CloudStatus.FAST;
                if (!insideClouds && !fastClouds) {
                    RenderSystem.enableCull();
                } else {
                    RenderSystem.disableCull();
                }
                RenderSystem.setShaderColor((float) color.x, (float) color.y, (float) color.z, 0.8F);
                matrices.pushPose();
                Matrix4f modelViewMatrix = matrices.last().pose();
                modelViewMatrix.translate(-translateX, cloudHeight - (float) cameraY + 0.33F, -translateZ);
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
                RenderSystem.colorMask(false, false, false, false);
                this.vertexBuffer.drawWithShader(modelViewMatrix, projectionMatrix, this.shader);
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                RenderSystem.depthMask(false);
                RenderSystem.enableDepthTest();
                RenderSystem.depthFunc(514);
                RenderSystem.colorMask(true, true, true, true);
                this.vertexBuffer.drawWithShader(modelViewMatrix, projectionMatrix, this.shader);
                matrices.popPose();
                VertexBuffer.unbind();
                RenderSystem.disableBlend();
                RenderSystem.depthFunc(515);
                RenderSystem.enableCull();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderFogEnd(previousEnd);
                RenderSystem.setShaderFogStart(previousStart);
            }
        }
    }

    private void applyFogModifiers(ClientLevel world, FogRenderer.FogData fogData, LocalPlayer player, int cloudDistance, float tickDelta) {
        if (Minecraft.getInstance().gameRenderer != null && Minecraft.getInstance().gameRenderer.getMainCamera() != null) {
            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            FogType cameraSubmersionType = camera.getFluidInCamera();
            if (cameraSubmersionType == FogType.LAVA) {
                if (player.m_5833_()) {
                    fogData.start = -8.0F;
                    fogData.end = (float) cloudDistance * 0.5F;
                } else if (player.m_21023_(MobEffects.FIRE_RESISTANCE)) {
                    fogData.start = 0.0F;
                    fogData.end = 3.0F;
                } else {
                    fogData.start = 0.25F;
                    fogData.end = 1.0F;
                }
            } else if (cameraSubmersionType == FogType.POWDER_SNOW) {
                if (player.m_5833_()) {
                    fogData.start = -8.0F;
                    fogData.end = (float) cloudDistance * 0.5F;
                } else {
                    fogData.start = 0.0F;
                    fogData.end = 2.0F;
                }
            } else if (cameraSubmersionType == FogType.WATER) {
                fogData.start = -8.0F;
                fogData.end = 96.0F;
                fogData.end = fogData.end * Math.max(0.25F, player.getWaterVision());
                if (fogData.end > (float) cloudDistance) {
                    fogData.end = (float) cloudDistance;
                    fogData.shape = FogShape.CYLINDER;
                }
            } else if (world.effects().isFoggyAt(Mth.floor(camera.getPosition().x), Mth.floor(camera.getPosition().z)) || Minecraft.getInstance().gui.getBossOverlay().shouldCreateWorldFog()) {
                fogData.start = (float) cloudDistance * 0.05F;
                fogData.end = Math.min((float) cloudDistance, 192.0F) * 0.5F;
            }
            FogRenderer.MobEffectFogFunction fogModifier = FogRenderer.getPriorityFogFunction(player, tickDelta);
            if (fogModifier != null) {
                MobEffectInstance statusEffectInstance = player.m_21124_(fogModifier.getMobEffect());
                if (statusEffectInstance != null) {
                    fogModifier.setupFog(fogData, player, statusEffectInstance, (float) cloudDistance, tickDelta);
                }
            }
        }
    }

    private void rebuildGeometry(BufferBuilder bufferBuilder, int cloudDistance, int centerCellX, int centerCellZ) {
        VertexBufferWriter writer = VertexBufferWriter.of(bufferBuilder);
        boolean fastClouds = this.cloudRenderMode == CloudStatus.FAST;
        for (int offsetX = -cloudDistance; offsetX < cloudDistance; offsetX++) {
            for (int offsetZ = -cloudDistance; offsetZ < cloudDistance; offsetZ++) {
                int connectedEdges = this.edges.getEdges(centerCellX + offsetX, centerCellZ + offsetZ);
                if (connectedEdges != 0) {
                    int texel = this.edges.getColor(centerCellX + offsetX, centerCellZ + offsetZ);
                    float x = (float) offsetX * this.cloudSizeX;
                    float z = (float) offsetZ * this.cloudSizeZ;
                    MemoryStack stack = MemoryStack.stackPush();
                    label93: {
                        try {
                            long buffer = stack.nmalloc((fastClouds ? 4 : 24) * 16);
                            long ptr = buffer;
                            int count = 0;
                            if ((connectedEdges & 1) != 0) {
                                int mixedColor = ColorMixer.mul(texel, fastClouds ? CLOUD_COLOR_POS_Y : CLOUD_COLOR_NEG_Y);
                                ptr = writeVertex(buffer, x + this.cloudSizeX, 0.0F, z + this.cloudSizeZ, mixedColor);
                                ptr = writeVertex(ptr, x + 0.0F, 0.0F, z + this.cloudSizeZ, mixedColor);
                                ptr = writeVertex(ptr, x + 0.0F, 0.0F, z + 0.0F, mixedColor);
                                ptr = writeVertex(ptr, x + this.cloudSizeX, 0.0F, z + 0.0F, mixedColor);
                                count += 4;
                            }
                            if (fastClouds) {
                                writer.push(stack, buffer, count, ColorVertex.FORMAT);
                                break label93;
                            }
                            if ((connectedEdges & 2) != 0) {
                                int mixedColor = ColorMixer.mul(texel, CLOUD_COLOR_POS_Y);
                                ptr = writeVertex(ptr, x + 0.0F, 4.0F, z + this.cloudSizeZ, mixedColor);
                                ptr = writeVertex(ptr, x + this.cloudSizeX, 4.0F, z + this.cloudSizeZ, mixedColor);
                                ptr = writeVertex(ptr, x + this.cloudSizeX, 4.0F, z + 0.0F, mixedColor);
                                ptr = writeVertex(ptr, x + 0.0F, 4.0F, z + 0.0F, mixedColor);
                                count += 4;
                            }
                            if ((connectedEdges & 4) != 0) {
                                int mixedColor = ColorMixer.mul(texel, CLOUD_COLOR_NEG_X);
                                ptr = writeVertex(ptr, x + 0.0F, 0.0F, z + this.cloudSizeZ, mixedColor);
                                ptr = writeVertex(ptr, x + 0.0F, 4.0F, z + this.cloudSizeZ, mixedColor);
                                ptr = writeVertex(ptr, x + 0.0F, 4.0F, z + 0.0F, mixedColor);
                                ptr = writeVertex(ptr, x + 0.0F, 0.0F, z + 0.0F, mixedColor);
                                count += 4;
                            }
                            if ((connectedEdges & 8) != 0) {
                                int mixedColor = ColorMixer.mul(texel, CLOUD_COLOR_POS_X);
                                ptr = writeVertex(ptr, x + this.cloudSizeX, 4.0F, z + this.cloudSizeZ, mixedColor);
                                ptr = writeVertex(ptr, x + this.cloudSizeX, 0.0F, z + this.cloudSizeZ, mixedColor);
                                ptr = writeVertex(ptr, x + this.cloudSizeX, 0.0F, z + 0.0F, mixedColor);
                                ptr = writeVertex(ptr, x + this.cloudSizeX, 4.0F, z + 0.0F, mixedColor);
                                count += 4;
                            }
                            if ((connectedEdges & 16) != 0) {
                                int mixedColor = ColorMixer.mul(texel, CLOUD_COLOR_NEG_Z);
                                ptr = writeVertex(ptr, x + this.cloudSizeX, 4.0F, z + 0.0F, mixedColor);
                                ptr = writeVertex(ptr, x + this.cloudSizeX, 0.0F, z + 0.0F, mixedColor);
                                ptr = writeVertex(ptr, x + 0.0F, 0.0F, z + 0.0F, mixedColor);
                                ptr = writeVertex(ptr, x + 0.0F, 4.0F, z + 0.0F, mixedColor);
                                count += 4;
                            }
                            if ((connectedEdges & 32) != 0) {
                                int mixedColor = ColorMixer.mul(texel, CLOUD_COLOR_POS_Z);
                                ptr = writeVertex(ptr, x + this.cloudSizeX, 0.0F, z + this.cloudSizeZ, mixedColor);
                                ptr = writeVertex(ptr, x + this.cloudSizeX, 4.0F, z + this.cloudSizeZ, mixedColor);
                                ptr = writeVertex(ptr, x + 0.0F, 4.0F, z + this.cloudSizeZ, mixedColor);
                                ptr = writeVertex(ptr, x + 0.0F, 0.0F, z + this.cloudSizeZ, mixedColor);
                                count += 4;
                            }
                            if (count > 0) {
                                writer.push(stack, buffer, count, ColorVertex.FORMAT);
                            }
                        } catch (Throwable var21) {
                            if (stack != null) {
                                try {
                                    stack.close();
                                } catch (Throwable var20) {
                                    var21.addSuppressed(var20);
                                }
                            }
                            throw var21;
                        }
                        if (stack != null) {
                            stack.close();
                        }
                        continue;
                    }
                    if (stack != null) {
                        stack.close();
                    }
                }
            }
        }
    }

    private static long writeVertex(long buffer, float x, float y, float z, int color) {
        ColorVertex.put(buffer, x, y, z, color);
        return buffer + 16L;
    }

    public void reloadTextures(ResourceProvider factory) {
        this.destroy();
        this.edges = createCloudEdges();
        boolean shaderMod = ShaderModBridge.areShadersEnabled();
        float width = shaderMod ? 256.0F : (float) this.edges.width;
        float height = shaderMod ? 256.0F : (float) this.edges.height;
        this.cloudSizeX = 3072.0F / width;
        this.cloudSizeZ = 3072.0F / height;
        this.fogDistanceMultiplier = 2048.0F / width;
        this.cloudDistanceMinimum = (int) (width * 0.125F);
        this.cloudDistanceMaximum = (int) (width * 0.0078125F);
        try {
            this.shader = new ShaderInstance(factory, "clouds", DefaultVertexFormat.POSITION_COLOR);
        } catch (IOException var6) {
            throw new RuntimeException(var6);
        }
    }

    public void destroy() {
        if (this.shader != null) {
            this.shader.close();
            this.shader = null;
        }
        if (this.vertexBuffer != null) {
            this.vertexBuffer.close();
            this.vertexBuffer = null;
        }
    }

    private static CloudRenderer.CloudEdges createCloudEdges() {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        Resource resource = (Resource) resourceManager.m_213713_(CLOUDS_TEXTURE_ID).orElseThrow();
        try {
            InputStream inputStream = resource.open();
            CloudRenderer.CloudEdges var4;
            try (NativeImage nativeImage = NativeImage.read(inputStream)) {
                var4 = new CloudRenderer.CloudEdges(nativeImage);
            } catch (Throwable var9) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable var6) {
                        var9.addSuppressed(var6);
                    }
                }
                throw var9;
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return var4;
        } catch (IOException var10) {
            throw new RuntimeException("Failed to load texture data", var10);
        }
    }

    private static class CloudEdges {

        private final byte[] edges;

        private final int[] colors;

        private final int width;

        private final int height;

        public CloudEdges(NativeImage texture) {
            int width = texture.getWidth();
            int height = texture.getHeight();
            this.edges = new byte[width * height];
            this.colors = new int[width * height];
            this.width = width;
            this.height = height;
            for (int x = 0; x < width; x++) {
                for (int z = 0; z < height; z++) {
                    int index = index(x, z, width, height);
                    int cell = texture.getPixelRGBA(x, z);
                    this.colors[index] = cell;
                    int edges = 0;
                    if (isOpaqueCell(cell)) {
                        edges |= 3;
                        int negX = texture.getPixelRGBA(wrap(x - 1, width), wrap(z, height));
                        if (cell != negX) {
                            edges |= 4;
                        }
                        int posX = texture.getPixelRGBA(wrap(x + 1, width), wrap(z, height));
                        if (!isOpaqueCell(posX) && cell != posX) {
                            edges |= 8;
                        }
                        int negZ = texture.getPixelRGBA(wrap(x, width), wrap(z - 1, height));
                        if (cell != negZ) {
                            edges |= 16;
                        }
                        int posZ = texture.getPixelRGBA(wrap(x, width), wrap(z + 1, height));
                        if (!isOpaqueCell(posZ) && cell != posZ) {
                            edges |= 32;
                        }
                    }
                    this.edges[index] = (byte) edges;
                }
            }
        }

        private static boolean isOpaqueCell(int color) {
            return ColorARGB.unpackAlpha(color) > 1;
        }

        public int getEdges(int x, int z) {
            return this.edges[index(x, z, this.width, this.height)];
        }

        public int getColor(int x, int z) {
            return this.colors[index(x, z, this.width, this.height)];
        }

        private static int index(int posX, int posZ, int width, int height) {
            return wrap(posX, width) * height + wrap(posZ, height);
        }

        private static int wrap(int pos, int dim) {
            return Math.floorMod(pos, dim);
        }
    }
}