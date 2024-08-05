package com.simibubi.create.content.kinetics.base;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.KineticDebugger;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.render.SuperByteBufferCache;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import org.apache.commons.lang3.ArrayUtils;

public class KineticBlockEntityRenderer<T extends KineticBlockEntity> extends SafeBlockEntityRenderer<T> {

    public static final SuperByteBufferCache.Compartment<BlockState> KINETIC_BLOCK = new SuperByteBufferCache.Compartment<>();

    public static boolean rainbowMode = false;

    protected static final RenderType[] REVERSED_CHUNK_BUFFER_LAYERS = (RenderType[]) RenderType.chunkBufferLayers().toArray(RenderType[]::new);

    public KineticBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(T be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(be.m_58904_())) {
            BlockState state = this.getRenderedBlockState(be);
            RenderType type = this.getRenderType(be, state);
            if (type != null) {
                renderRotatingBuffer(be, this.getRotatedModel(be, state), ms, buffer.getBuffer(type), light);
            }
        }
    }

    protected BlockState getRenderedBlockState(T be) {
        return be.m_58900_();
    }

    protected RenderType getRenderType(T be, BlockState state) {
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
        ChunkRenderTypeSet typeSet = model.getRenderTypes(state, RandomSource.create(42L), ModelData.EMPTY);
        for (RenderType type : REVERSED_CHUNK_BUFFER_LAYERS) {
            if (typeSet.contains(type)) {
                return type;
            }
        }
        return null;
    }

    protected SuperByteBuffer getRotatedModel(T be, BlockState state) {
        return CachedBufferer.block(KINETIC_BLOCK, state);
    }

    public static void renderRotatingKineticBlock(KineticBlockEntity be, BlockState renderedState, PoseStack ms, VertexConsumer buffer, int light) {
        SuperByteBuffer superByteBuffer = CachedBufferer.block(KINETIC_BLOCK, renderedState);
        renderRotatingBuffer(be, superByteBuffer, ms, buffer, light);
    }

    public static void renderRotatingBuffer(KineticBlockEntity be, SuperByteBuffer superBuffer, PoseStack ms, VertexConsumer buffer, int light) {
        standardKineticRotationTransform(superBuffer, be, light).renderInto(ms, buffer);
    }

    public static float getAngleForTe(KineticBlockEntity be, BlockPos pos, Direction.Axis axis) {
        float time = AnimationTickHolder.getRenderTime(be.m_58904_());
        float offset = getRotationOffsetForPosition(be, pos, axis);
        return (time * be.getSpeed() * 3.0F / 10.0F + offset) % 360.0F / 180.0F * (float) Math.PI;
    }

    public static SuperByteBuffer standardKineticRotationTransform(SuperByteBuffer buffer, KineticBlockEntity be, int light) {
        BlockPos pos = be.m_58899_();
        Direction.Axis axis = ((IRotate) be.m_58900_().m_60734_()).getRotationAxis(be.m_58900_());
        return kineticRotationTransform(buffer, be, axis, getAngleForTe(be, pos, axis), light);
    }

    public static SuperByteBuffer kineticRotationTransform(SuperByteBuffer buffer, KineticBlockEntity be, Direction.Axis axis, float angle, int light) {
        buffer.light(light);
        buffer.rotateCentered(Direction.get(Direction.AxisDirection.POSITIVE, axis), angle);
        if (KineticDebugger.isActive()) {
            rainbowMode = true;
            buffer.color(be.hasNetwork() ? Color.generateFromLong(be.network) : Color.WHITE);
        } else {
            float overStressedEffect = be.effects.overStressedEffect;
            if (overStressedEffect != 0.0F) {
                if (overStressedEffect > 0.0F) {
                    buffer.color(Color.WHITE.mixWith(Color.RED, overStressedEffect));
                } else {
                    buffer.color(Color.WHITE.mixWith(Color.SPRING_GREEN, -overStressedEffect));
                }
            } else {
                buffer.color(Color.WHITE);
            }
        }
        return buffer;
    }

    public static float getRotationOffsetForPosition(KineticBlockEntity be, BlockPos pos, Direction.Axis axis) {
        float offset = ICogWheel.isLargeCog(be.m_58900_()) ? 11.25F : 0.0F;
        double d = (double) (((axis == Direction.Axis.X ? 0 : pos.m_123341_()) + (axis == Direction.Axis.Y ? 0 : pos.m_123342_()) + (axis == Direction.Axis.Z ? 0 : pos.m_123343_())) % 2);
        if (d == 0.0) {
            offset = 22.5F;
        }
        return offset + (float) be.getRotationAngleOffset(axis);
    }

    public static BlockState shaft(Direction.Axis axis) {
        return (BlockState) AllBlocks.SHAFT.getDefaultState().m_61124_(BlockStateProperties.AXIS, axis);
    }

    public static Direction.Axis getRotationAxisOf(KineticBlockEntity be) {
        return ((IRotate) be.m_58900_().m_60734_()).getRotationAxis(be.m_58900_());
    }

    static {
        ArrayUtils.reverse(REVERSED_CHUNK_BUFFER_LAYERS);
    }
}