package net.minecraft.client.renderer.blockentity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Camera;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.HitResult;

public class BlockEntityRenderDispatcher implements ResourceManagerReloadListener {

    private Map<BlockEntityType<?>, BlockEntityRenderer<?>> renderers = ImmutableMap.of();

    private final Font font;

    private final EntityModelSet entityModelSet;

    public Level level;

    public Camera camera;

    public HitResult cameraHitResult;

    private final Supplier<BlockRenderDispatcher> blockRenderDispatcher;

    private final Supplier<ItemRenderer> itemRenderer;

    private final Supplier<EntityRenderDispatcher> entityRenderer;

    public BlockEntityRenderDispatcher(Font font0, EntityModelSet entityModelSet1, Supplier<BlockRenderDispatcher> supplierBlockRenderDispatcher2, Supplier<ItemRenderer> supplierItemRenderer3, Supplier<EntityRenderDispatcher> supplierEntityRenderDispatcher4) {
        this.itemRenderer = supplierItemRenderer3;
        this.entityRenderer = supplierEntityRenderDispatcher4;
        this.font = font0;
        this.entityModelSet = entityModelSet1;
        this.blockRenderDispatcher = supplierBlockRenderDispatcher2;
    }

    @Nullable
    public <E extends BlockEntity> BlockEntityRenderer<E> getRenderer(E e0) {
        return (BlockEntityRenderer<E>) this.renderers.get(e0.getType());
    }

    public void prepare(Level level0, Camera camera1, HitResult hitResult2) {
        if (this.level != level0) {
            this.setLevel(level0);
        }
        this.camera = camera1;
        this.cameraHitResult = hitResult2;
    }

    public <E extends BlockEntity> void render(E e0, float float1, PoseStack poseStack2, MultiBufferSource multiBufferSource3) {
        BlockEntityRenderer<E> $$4 = this.getRenderer(e0);
        if ($$4 != null) {
            if (e0.hasLevel() && e0.getType().isValid(e0.getBlockState())) {
                if ($$4.shouldRender(e0, this.camera.getPosition())) {
                    tryRender(e0, () -> setupAndRender($$4, e0, float1, poseStack2, multiBufferSource3));
                }
            }
        }
    }

    private static <T extends BlockEntity> void setupAndRender(BlockEntityRenderer<T> blockEntityRendererT0, T t1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4) {
        Level $$5 = t1.getLevel();
        int $$6;
        if ($$5 != null) {
            $$6 = LevelRenderer.getLightColor($$5, t1.getBlockPos());
        } else {
            $$6 = 15728880;
        }
        blockEntityRendererT0.render(t1, float2, poseStack3, multiBufferSource4, $$6, OverlayTexture.NO_OVERLAY);
    }

    public <E extends BlockEntity> boolean renderItem(E e0, PoseStack poseStack1, MultiBufferSource multiBufferSource2, int int3, int int4) {
        BlockEntityRenderer<E> $$5 = this.getRenderer(e0);
        if ($$5 == null) {
            return true;
        } else {
            tryRender(e0, () -> $$5.render(e0, 0.0F, poseStack1, multiBufferSource2, int3, int4));
            return false;
        }
    }

    private static void tryRender(BlockEntity blockEntity0, Runnable runnable1) {
        try {
            runnable1.run();
        } catch (Throwable var5) {
            CrashReport $$3 = CrashReport.forThrowable(var5, "Rendering Block Entity");
            CrashReportCategory $$4 = $$3.addCategory("Block Entity Details");
            blockEntity0.fillCrashReportCategory($$4);
            throw new ReportedException($$3);
        }
    }

    public void setLevel(@Nullable Level level0) {
        this.level = level0;
        if (level0 == null) {
            this.camera = null;
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager0) {
        BlockEntityRendererProvider.Context $$1 = new BlockEntityRendererProvider.Context(this, (BlockRenderDispatcher) this.blockRenderDispatcher.get(), (ItemRenderer) this.itemRenderer.get(), (EntityRenderDispatcher) this.entityRenderer.get(), this.entityModelSet, this.font);
        this.renderers = BlockEntityRenderers.createEntityRenderers($$1);
    }
}