package com.simibubi.create.foundation.ponder.element;

import com.jozufozu.flywheel.core.model.ModelUtil;
import com.jozufozu.flywheel.core.model.ShadeSeparatedBufferedData;
import com.jozufozu.flywheel.core.model.ShadeSeparatingVertexConsumer;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.outliner.AABBOutline;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.render.BlockEntityRenderHelper;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.render.SuperByteBufferCache;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.data.ModelData;

public class WorldSectionElement extends AnimatedSceneElement {

    public static final SuperByteBufferCache.Compartment<Pair<Integer, Integer>> DOC_WORLD_SECTION = new SuperByteBufferCache.Compartment<>();

    private static final ThreadLocal<WorldSectionElement.ThreadLocalObjects> THREAD_LOCAL_OBJECTS = ThreadLocal.withInitial(WorldSectionElement.ThreadLocalObjects::new);

    List<BlockEntity> renderedBlockEntities;

    List<Pair<BlockEntity, Consumer<Level>>> tickableBlockEntities;

    Selection section;

    boolean redraw;

    Vec3 prevAnimatedOffset = Vec3.ZERO;

    Vec3 animatedOffset = Vec3.ZERO;

    Vec3 prevAnimatedRotation = Vec3.ZERO;

    Vec3 animatedRotation = Vec3.ZERO;

    Vec3 centerOfRotation = Vec3.ZERO;

    Vec3 stabilizationAnchor = null;

    BlockPos selectedBlock;

    public WorldSectionElement() {
    }

    public WorldSectionElement(Selection section) {
        this.section = section.copy();
        this.centerOfRotation = section.getCenter();
    }

    public void mergeOnto(WorldSectionElement other) {
        this.setVisible(false);
        if (other.isEmpty()) {
            other.set(this.section);
        } else {
            other.add(this.section);
        }
    }

    public void set(Selection selection) {
        this.applyNewSelection(selection.copy());
    }

    public void add(Selection toAdd) {
        this.applyNewSelection(this.section.add(toAdd));
    }

    public void erase(Selection toErase) {
        this.applyNewSelection(this.section.substract(toErase));
    }

    private void applyNewSelection(Selection selection) {
        this.section = selection;
        this.queueRedraw();
    }

    public void setCenterOfRotation(Vec3 center) {
        this.centerOfRotation = center;
    }

    public void stabilizeRotation(Vec3 anchor) {
        this.stabilizationAnchor = anchor;
    }

    @Override
    public void reset(PonderScene scene) {
        super.reset(scene);
        this.resetAnimatedTransform();
        this.resetSelectedBlock();
    }

    public void selectBlock(BlockPos pos) {
        this.selectedBlock = pos;
    }

    public void resetSelectedBlock() {
        this.selectedBlock = null;
    }

    public void resetAnimatedTransform() {
        this.prevAnimatedOffset = Vec3.ZERO;
        this.animatedOffset = Vec3.ZERO;
        this.prevAnimatedRotation = Vec3.ZERO;
        this.animatedRotation = Vec3.ZERO;
    }

    public void queueRedraw() {
        this.redraw = true;
    }

    public boolean isEmpty() {
        return this.section == null;
    }

    public void setEmpty() {
        this.section = null;
    }

    public void setAnimatedRotation(Vec3 eulerAngles, boolean force) {
        this.animatedRotation = eulerAngles;
        if (force) {
            this.prevAnimatedRotation = this.animatedRotation;
        }
    }

    public Vec3 getAnimatedRotation() {
        return this.animatedRotation;
    }

    public void setAnimatedOffset(Vec3 offset, boolean force) {
        this.animatedOffset = offset;
        if (force) {
            this.prevAnimatedOffset = this.animatedOffset;
        }
    }

    public Vec3 getAnimatedOffset() {
        return this.animatedOffset;
    }

    @Override
    public boolean isVisible() {
        return super.isVisible() && !this.isEmpty();
    }

    public Pair<Vec3, BlockHitResult> rayTrace(PonderWorld world, Vec3 source, Vec3 target) {
        world.setMask(this.section);
        Vec3 transformedTarget = this.reverseTransformVec(target);
        BlockHitResult rayTraceBlocks = world.m_45547_(new ClipContext(this.reverseTransformVec(source), transformedTarget, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null));
        world.clearMask();
        if (rayTraceBlocks == null) {
            return null;
        } else if (rayTraceBlocks.m_82450_() == null) {
            return null;
        } else {
            double t = rayTraceBlocks.m_82450_().subtract(transformedTarget).lengthSqr() / source.subtract(target).lengthSqr();
            Vec3 actualHit = VecHelper.lerp((float) t, target, source);
            return Pair.of(actualHit, rayTraceBlocks);
        }
    }

    private Vec3 reverseTransformVec(Vec3 in) {
        float pt = AnimationTickHolder.getPartialTicks();
        in = in.subtract(VecHelper.lerp(pt, this.prevAnimatedOffset, this.animatedOffset));
        if (!this.animatedRotation.equals(Vec3.ZERO) || !this.prevAnimatedRotation.equals(Vec3.ZERO)) {
            if (this.centerOfRotation == null) {
                this.centerOfRotation = this.section.getCenter();
            }
            double rotX = Mth.lerp((double) pt, this.prevAnimatedRotation.x, this.animatedRotation.x);
            double rotZ = Mth.lerp((double) pt, this.prevAnimatedRotation.z, this.animatedRotation.z);
            double rotY = Mth.lerp((double) pt, this.prevAnimatedRotation.y, this.animatedRotation.y);
            in = in.subtract(this.centerOfRotation);
            in = VecHelper.rotate(in, -rotX, Direction.Axis.X);
            in = VecHelper.rotate(in, -rotZ, Direction.Axis.Z);
            in = VecHelper.rotate(in, -rotY, Direction.Axis.Y);
            in = in.add(this.centerOfRotation);
            if (this.stabilizationAnchor != null) {
                in = in.subtract(this.stabilizationAnchor);
                in = VecHelper.rotate(in, rotX, Direction.Axis.X);
                in = VecHelper.rotate(in, rotZ, Direction.Axis.Z);
                in = VecHelper.rotate(in, rotY, Direction.Axis.Y);
                in = in.add(this.stabilizationAnchor);
            }
        }
        return in;
    }

    public void transformMS(PoseStack ms, float pt) {
        TransformStack.cast(ms).translate(VecHelper.lerp(pt, this.prevAnimatedOffset, this.animatedOffset));
        if (!this.animatedRotation.equals(Vec3.ZERO) || !this.prevAnimatedRotation.equals(Vec3.ZERO)) {
            if (this.centerOfRotation == null) {
                this.centerOfRotation = this.section.getCenter();
            }
            double rotX = Mth.lerp((double) pt, this.prevAnimatedRotation.x, this.animatedRotation.x);
            double rotZ = Mth.lerp((double) pt, this.prevAnimatedRotation.z, this.animatedRotation.z);
            double rotY = Mth.lerp((double) pt, this.prevAnimatedRotation.y, this.animatedRotation.y);
            ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(ms).translate(this.centerOfRotation)).rotateX(rotX)).rotateZ(rotZ)).rotateY(rotY)).translateBack(this.centerOfRotation);
            if (this.stabilizationAnchor != null) {
                ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(ms).translate(this.stabilizationAnchor)).rotateX(-rotX)).rotateZ(-rotZ)).rotateY(-rotY)).translateBack(this.stabilizationAnchor);
            }
        }
    }

    @Override
    public void tick(PonderScene scene) {
        this.prevAnimatedOffset = this.animatedOffset;
        this.prevAnimatedRotation = this.animatedRotation;
        if (this.isVisible()) {
            this.loadBEsIfMissing(scene.getWorld());
            this.renderedBlockEntities.removeIf(be -> scene.getWorld().m_7702_(be.getBlockPos()) != be);
            this.tickableBlockEntities.removeIf(be -> scene.getWorld().m_7702_(((BlockEntity) be.getFirst()).getBlockPos()) != be.getFirst());
            this.tickableBlockEntities.forEach(be -> ((Consumer) be.getSecond()).accept(scene.getWorld()));
        }
    }

    @Override
    public void whileSkipping(PonderScene scene) {
        if (this.redraw) {
            this.renderedBlockEntities = null;
            this.tickableBlockEntities = null;
        }
        this.redraw = false;
    }

    protected void loadBEsIfMissing(PonderWorld world) {
        if (this.renderedBlockEntities == null) {
            this.tickableBlockEntities = new ArrayList();
            this.renderedBlockEntities = new ArrayList();
            this.section.forEach(pos -> {
                BlockEntity blockEntity = world.m_7702_(pos);
                BlockState blockState = world.getBlockState(pos);
                Block block = blockState.m_60734_();
                if (blockEntity != null) {
                    if (block instanceof EntityBlock) {
                        blockEntity.setBlockState(world.getBlockState(pos));
                        BlockEntityTicker<?> ticker = ((EntityBlock) block).getTicker(world, blockState, blockEntity.getType());
                        if (ticker != null) {
                            this.addTicker(blockEntity, ticker);
                        }
                        this.renderedBlockEntities.add(blockEntity);
                    }
                }
            });
        }
    }

    private <T extends BlockEntity> void addTicker(T blockEntity, BlockEntityTicker<?> ticker) {
        this.tickableBlockEntities.add(Pair.of(blockEntity, w -> ticker.tick(w, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity)));
    }

    @Override
    public void renderFirst(PonderWorld world, MultiBufferSource buffer, PoseStack ms, float fade, float pt) {
        int light = -1;
        if (fade != 1.0F) {
            light = (int) Mth.lerp(fade, 5.0F, 14.0F);
        }
        if (this.redraw) {
            this.renderedBlockEntities = null;
            this.tickableBlockEntities = null;
        }
        ms.pushPose();
        this.transformMS(ms, pt);
        world.pushFakeLight(light);
        this.renderBlockEntities(world, ms, buffer, pt);
        world.popLight();
        Map<BlockPos, Integer> blockBreakingProgressions = world.getBlockBreakingProgressions();
        PoseStack overlayMS = null;
        for (Entry<BlockPos, Integer> entry : blockBreakingProgressions.entrySet()) {
            BlockPos pos = (BlockPos) entry.getKey();
            if (this.section.test(pos)) {
                if (overlayMS == null) {
                    overlayMS = new PoseStack();
                    overlayMS.last().pose().set(ms.last().pose());
                    overlayMS.last().normal().set(ms.last().normal());
                }
                VertexConsumer builder = new SheetedDecalTextureGenerator(buffer.getBuffer((RenderType) ModelBakery.DESTROY_TYPES.get((Integer) entry.getValue())), overlayMS.last().pose(), overlayMS.last().normal(), 1.0F);
                ms.pushPose();
                ms.translate((float) pos.m_123341_(), (float) pos.m_123342_(), (float) pos.m_123343_());
                ModelUtil.VANILLA_RENDERER.renderBreakingTexture(world.getBlockState(pos), pos, world, ms, builder, ModelData.EMPTY);
                ms.popPose();
            }
        }
        ms.popPose();
    }

    @Override
    protected void renderLayer(PonderWorld world, MultiBufferSource buffer, RenderType type, PoseStack ms, float fade, float pt) {
        SuperByteBufferCache bufferCache = CreateClient.BUFFER_CACHE;
        int code = this.hashCode() ^ world.hashCode();
        Pair<Integer, Integer> key = Pair.of(code, RenderType.chunkBufferLayers().indexOf(type));
        if (this.redraw) {
            bufferCache.invalidate(DOC_WORLD_SECTION, key);
        }
        SuperByteBuffer contraptionBuffer = bufferCache.get(DOC_WORLD_SECTION, key, () -> this.buildStructureBuffer(world, type));
        if (!contraptionBuffer.isEmpty()) {
            this.transformMS(contraptionBuffer.getTransforms(), pt);
            int light = this.lightCoordsFromFade(fade);
            contraptionBuffer.light(light).renderInto(ms, buffer.getBuffer(type));
        }
    }

    @Override
    protected void renderLast(PonderWorld world, MultiBufferSource buffer, PoseStack ms, float fade, float pt) {
        this.redraw = false;
        if (this.selectedBlock != null) {
            BlockState blockState = world.getBlockState(this.selectedBlock);
            if (!blockState.m_60795_()) {
                VoxelShape shape = blockState.m_60651_(world, this.selectedBlock, CollisionContext.of(Minecraft.getInstance().player));
                if (!shape.isEmpty()) {
                    ms.pushPose();
                    this.transformMS(ms, pt);
                    ms.translate((float) this.selectedBlock.m_123341_(), (float) this.selectedBlock.m_123342_(), (float) this.selectedBlock.m_123343_());
                    AABBOutline aabbOutline = new AABBOutline(shape.bounds());
                    aabbOutline.getParams().lineWidth(0.015625F).colored(15724527).disableLineNormals();
                    aabbOutline.render(ms, (SuperRenderTypeBuffer) buffer, Vec3.ZERO, pt);
                    ms.popPose();
                }
            }
        }
    }

    private void renderBlockEntities(PonderWorld world, PoseStack ms, MultiBufferSource buffer, float pt) {
        this.loadBEsIfMissing(world);
        BlockEntityRenderHelper.renderBlockEntities(world, this.renderedBlockEntities, ms, buffer, pt);
    }

    private SuperByteBuffer buildStructureBuffer(PonderWorld world, RenderType layer) {
        BlockRenderDispatcher dispatcher = ModelUtil.VANILLA_RENDERER;
        ModelBlockRenderer renderer = dispatcher.getModelRenderer();
        WorldSectionElement.ThreadLocalObjects objects = (WorldSectionElement.ThreadLocalObjects) THREAD_LOCAL_OBJECTS.get();
        PoseStack poseStack = objects.poseStack;
        RandomSource random = objects.random;
        ShadeSeparatingVertexConsumer shadeSeparatingWrapper = objects.shadeSeparatingWrapper;
        BufferBuilder shadedBuilder = objects.shadedBuilder;
        BufferBuilder unshadedBuilder = objects.unshadedBuilder;
        shadedBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
        unshadedBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
        shadeSeparatingWrapper.prepare(shadedBuilder, unshadedBuilder);
        world.setMask(this.section);
        ModelBlockRenderer.enableCaching();
        this.section.forEach(pos -> {
            BlockState state = world.getBlockState(pos);
            FluidState fluidState = world.m_6425_(pos);
            poseStack.pushPose();
            poseStack.translate((float) pos.m_123341_(), (float) pos.m_123342_(), (float) pos.m_123343_());
            if (state.m_60799_() == RenderShape.MODEL) {
                BakedModel model = dispatcher.getBlockModel(state);
                BlockEntity blockEntity = world.m_7702_(pos);
                ModelData modelData = blockEntity != null ? blockEntity.getModelData() : ModelData.EMPTY;
                modelData = model.getModelData(world, pos, state, modelData);
                long seed = state.m_60726_(pos);
                random.setSeed(seed);
                if (model.getRenderTypes(state, random, modelData).contains(layer)) {
                    renderer.tesselateBlock(world, model, state, pos, poseStack, shadeSeparatingWrapper, true, random, seed, OverlayTexture.NO_OVERLAY, modelData, layer);
                }
            }
            if (!fluidState.isEmpty() && ItemBlockRenderTypes.getRenderLayer(fluidState) == layer) {
                dispatcher.renderLiquid(pos, world, shadedBuilder, state, fluidState);
            }
            poseStack.popPose();
        });
        ModelBlockRenderer.clearCache();
        world.clearMask();
        shadeSeparatingWrapper.clear();
        ShadeSeparatedBufferedData bufferedData = ModelUtil.endAndCombine(shadedBuilder, unshadedBuilder);
        SuperByteBuffer sbb = new SuperByteBuffer(bufferedData);
        bufferedData.release();
        return sbb;
    }

    private static class ThreadLocalObjects {

        public final PoseStack poseStack = new PoseStack();

        public final RandomSource random = RandomSource.createNewThreadLocalInstance();

        public final ShadeSeparatingVertexConsumer shadeSeparatingWrapper = new ShadeSeparatingVertexConsumer();

        public final BufferBuilder shadedBuilder = new BufferBuilder(512);

        public final BufferBuilder unshadedBuilder = new BufferBuilder(512);
    }

    class WorldSectionRayTraceResult {

        Vec3 actualHitVec;

        BlockPos worldPos;
    }
}