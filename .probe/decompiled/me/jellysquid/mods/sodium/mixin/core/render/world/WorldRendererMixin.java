package me.jellysquid.mods.sodium.mixin.core.render.world;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicBoolean;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.viewport.Viewport;
import me.jellysquid.mods.sodium.client.render.viewport.ViewportProvider;
import me.jellysquid.mods.sodium.client.world.WorldRendererExtended;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraftforge.client.ForgeHooksClient;
import org.embeddedt.embeddium.util.sodium.FlawlessFrames;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LevelRenderer.class })
public abstract class WorldRendererMixin implements WorldRendererExtended {

    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    @Shadow
    @Final
    private Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress;

    @Shadow
    private boolean needsFullRenderChunkUpdate;

    @Shadow
    private int ticks;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private SodiumWorldRenderer renderer;

    @Unique
    private int frame;

    @Shadow
    @Final
    private AtomicBoolean needsFrustumUpdate;

    @Shadow(remap = false)
    public Frustum getFrustum() {
        return null;
    }

    @Shadow
    public abstract boolean shouldShowEntityOutlines();

    @Override
    public SodiumWorldRenderer sodium$getWorldRenderer() {
        return this.renderer;
    }

    @Redirect(method = { "allChanged()V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getEffectiveRenderDistance()I", ordinal = 1))
    private int nullifyBuiltChunkStorage(Options options) {
        return 0;
    }

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void init(Minecraft client, EntityRenderDispatcher entityRenderDispatcher, BlockEntityRenderDispatcher blockEntityRenderDispatcher, RenderBuffers bufferBuilderStorage, CallbackInfo ci) {
        this.renderer = new SodiumWorldRenderer(client);
    }

    @Inject(method = { "setLevel" }, at = { @At("RETURN") })
    private void onWorldChanged(ClientLevel world, CallbackInfo ci) {
        RenderDevice.enterManagedCode();
        try {
            this.renderer.setWorld(world);
        } finally {
            RenderDevice.exitManagedCode();
        }
    }

    @Overwrite
    public int countRenderedChunks() {
        return this.renderer.getVisibleChunkCount();
    }

    @Overwrite
    public boolean hasRenderedAllChunks() {
        return this.renderer.isTerrainRenderComplete();
    }

    @Inject(method = { "needsUpdate" }, at = { @At("RETURN") })
    private void onTerrainUpdateScheduled(CallbackInfo ci) {
        this.renderer.scheduleTerrainUpdate();
    }

    @Overwrite
    private void renderChunkLayer(RenderType renderLayer, PoseStack matrices, double x, double y, double z, Matrix4f matrix) {
        RenderDevice.enterManagedCode();
        try {
            this.renderer.drawChunkLayer(renderLayer, matrices, x, y, z);
        } finally {
            RenderDevice.exitManagedCode();
        }
        renderLayer.m_110185_();
        ForgeHooksClient.dispatchRenderStage(renderLayer, (LevelRenderer) this, matrices, matrix, this.ticks, this.minecraft.gameRenderer.getMainCamera(), this.getFrustum());
        renderLayer.m_110188_();
    }

    @Overwrite
    private void setupRender(Camera camera, Frustum frustum, boolean hasForcedFrustum, boolean spectator) {
        Viewport viewport = ((ViewportProvider) frustum).sodium$createViewport();
        if (this.needsFullRenderChunkUpdate || this.needsFrustumUpdate.compareAndSet(true, false)) {
            this.renderer.scheduleTerrainUpdate();
        }
        RenderDevice.enterManagedCode();
        try {
            this.renderer.setupTerrain(camera, viewport, this.frame++, spectator, FlawlessFrames.isActive());
        } finally {
            RenderDevice.exitManagedCode();
        }
        this.needsFullRenderChunkUpdate = false;
    }

    @Overwrite
    public void setBlocksDirty(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.renderer.scheduleRebuildForBlockArea(minX, minY, minZ, maxX, maxY, maxZ, false);
    }

    @Overwrite
    public void setSectionDirtyWithNeighbors(int x, int y, int z) {
        this.renderer.scheduleRebuildForChunks(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1, false);
    }

    @Overwrite
    private void setBlockDirty(BlockPos pos, boolean important) {
        this.renderer.scheduleRebuildForBlockArea(pos.m_123341_() - 1, pos.m_123342_() - 1, pos.m_123343_() - 1, pos.m_123341_() + 1, pos.m_123342_() + 1, pos.m_123343_() + 1, important);
    }

    @Overwrite
    private void setSectionDirty(int x, int y, int z, boolean important) {
        this.renderer.scheduleRebuildForChunk(x, y, z, important);
    }

    @Overwrite
    public boolean isChunkCompiled(BlockPos pos) {
        return this.renderer.isSectionReady(pos.m_123341_() >> 4, pos.m_123342_() >> 4, pos.m_123343_() >> 4);
    }

    @Inject(method = { "allChanged()V" }, at = { @At("RETURN") })
    private void onReload(CallbackInfo ci) {
        RenderDevice.enterManagedCode();
        try {
            this.renderer.reload();
        } finally {
            RenderDevice.exitManagedCode();
        }
    }

    @Inject(method = { "renderLevel" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/LevelRenderer;globalBlockEntities:Ljava/util/Set;", shift = Shift.BEFORE, ordinal = 0) })
    private void onRenderBlockEntities(PoseStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightmapTextureManager, Matrix4f positionMatrix, CallbackInfo ci) {
        this.renderer.renderBlockEntities(matrices, this.renderBuffers, this.destructionProgress, camera, tickDelta);
    }

    @ModifyVariable(method = { "renderLevel" }, at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/LevelRenderer;globalBlockEntities:Ljava/util/Set;", shift = Shift.BEFORE, ordinal = 0), ordinal = 3)
    private boolean changeEntityOutlineFlag(boolean bl) {
        return bl || this.renderer.didBlockEntityRequestOutline() && this.shouldShowEntityOutlines();
    }

    @Overwrite
    public String getChunkStatistics() {
        return this.renderer.getChunksDebugString();
    }
}