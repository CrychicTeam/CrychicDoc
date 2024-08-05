package org.violetmoon.quark.content.automation.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.automation.module.PistonsMoveTileEntitiesModule;

public class QuarkPistonBlockEntityRenderer {

    public static boolean renderPistonBlock(PistonMovingBlockEntity piston, float partialTicks, PoseStack matrix, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (PistonsMoveTileEntitiesModule.staticEnabled && !(piston.getProgress(partialTicks) > 1.0F)) {
            BlockState state = piston.getMovedState();
            BlockPos truePos = piston.m_58899_();
            if (state.m_60734_() instanceof EntityBlock eb) {
                BlockEntity tile = eb.newBlockEntity(truePos, state);
                if (tile == null) {
                    return false;
                } else {
                    CompoundTag tileTag = PistonsMoveTileEntitiesModule.getMovingBlockEntityData(piston.m_58904_(), truePos);
                    if (tileTag != null && tile.getType() == BuiltInRegistries.BLOCK_ENTITY_TYPE.get(new ResourceLocation(tileTag.getString("id")))) {
                        tile.load(tileTag);
                    }
                    Vec3 offset = new Vec3((double) piston.getXOff(partialTicks), (double) piston.getYOff(partialTicks), (double) piston.getZOff(partialTicks));
                    return renderTESafely(piston.m_58904_(), truePos, state, tile, piston, partialTicks, offset, matrix, bufferIn, combinedLightIn, combinedOverlayIn);
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean renderTESafely(Level world, BlockPos truePos, BlockState state, BlockEntity tile, BlockEntity sourceTE, float partialTicks, Vec3 offset, PoseStack matrix, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Block block = state.m_60734_();
        String id = Objects.toString(BuiltInRegistries.BLOCK.getKey(block));
        PoseStack.Pose currEntry = matrix.last();
        boolean tileentityrenderer;
        try {
            if (tile != null && block != Blocks.PISTON_HEAD && !PistonsMoveTileEntitiesModule.renderBlacklist.contains(id)) {
                matrix.pushPose();
                Minecraft mc = Minecraft.getInstance();
                BlockEntityRenderer<BlockEntity> tileentityrendererx = mc.getBlockEntityRenderDispatcher().getRenderer(tile);
                if (tileentityrendererx != null) {
                    tile.setLevel(sourceTE.getLevel());
                    tile.clearRemoved();
                    matrix.translate(offset.x, offset.y, offset.z);
                    tile.blockState = state;
                    tileentityrendererx.render(tile, partialTicks, matrix, bufferIn, combinedLightIn, combinedOverlayIn);
                    return state.m_60799_() != RenderShape.MODEL;
                }
            }
            return state.m_60799_() != RenderShape.MODEL;
        } catch (Exception var19) {
            Quark.LOG.warn("{} can't be rendered for piston TE moving", id, var19);
            PistonsMoveTileEntitiesModule.renderBlacklist.add(id);
            tileentityrenderer = false;
        } finally {
            while (matrix.last() != currEntry) {
                matrix.popPose();
            }
        }
        return tileentityrenderer;
    }
}