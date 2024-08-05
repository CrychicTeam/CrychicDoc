package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.rekindled.embers.blockentity.EntropicEnumeratorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Quaterniond;
import org.joml.Quaternionf;

public class EntropicEnumeratorBlockEntityRenderer implements BlockEntityRenderer<EntropicEnumeratorBlockEntity> {

    public static BakedModel[][][] cubies = new BakedModel[2][2][2];

    public static Quaternionf quat = new Quaternionf();

    public EntropicEnumeratorBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    public void render(EntropicEnumeratorBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockRenderDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
        Level level = blockEntity.m_58904_();
        BlockState blockState = level.getBlockState(blockEntity.m_58899_());
        float turnTicks = (float) (level.getGameTime() - blockEntity.nextMoveTime) + partialTick;
        poseStack.translate(0.5, 0.5, 0.5);
        if (turnTicks > 0.0F && blockEntity.moveQueue.length > 0) {
            int currentMove = blockEntity.moveQueue.length - 1;
            for (int i = 0; i < blockEntity.moveQueue.length - 1; i++) {
                turnTicks -= (float) blockEntity.offsetQueue[i];
                if (turnTicks < (float) blockEntity.offsetQueue[i + 1]) {
                    currentMove = i;
                    break;
                }
            }
            if (currentMove == blockEntity.moveQueue.length - 1) {
                turnTicks -= (float) blockEntity.offsetQueue[blockEntity.moveQueue.length - 1];
            }
            float turnAmount;
            if (blockEntity.solving) {
                turnAmount = Math.min(2.0F * turnTicks / (float) blockEntity.moveQueue[currentMove].length, 1.0F);
            } else {
                turnAmount = Math.min(turnTicks / (float) blockEntity.moveQueue[currentMove].length, 1.0F);
            }
            if (currentMove != blockEntity.previousMove) {
                if (blockEntity.previousMove + 1 == currentMove) {
                    blockEntity.moveQueue[blockEntity.previousMove].makeMove(blockEntity.visualCube);
                } else {
                    for (int x = 0; x < 2; x++) {
                        for (int y = 0; y < 2; y++) {
                            for (int z = 0; z < 2; z++) {
                                blockEntity.visualCube[x][y][z] = new EntropicEnumeratorBlockEntity.Cubie(blockEntity.cube[x][y][z].basePosition, new Quaterniond(blockEntity.cube[x][y][z].rotation));
                            }
                        }
                    }
                    for (int ix = 0; ix < currentMove; ix++) {
                        blockEntity.moveQueue[ix].makeMove(blockEntity.visualCube);
                    }
                }
                blockEntity.previousMove = currentMove;
            }
            for (int x = 0; x < 2; x++) {
                for (int y = 0; y < 2; y++) {
                    for (int z = 0; z < 2; z++) {
                        poseStack.pushPose();
                        poseStack.mulPose(blockEntity.moveQueue[currentMove].makePartialMove(quat.set(blockEntity.visualCube[x][y][z].rotation), blockEntity.visualCube[x][y][z].getPos(), turnAmount));
                        poseStack.translate(-0.5, -0.5, -0.5);
                        if (cubies[x][y][z] != null) {
                            blockrendererdispatcher.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(Sheets.solidBlockSheet()), blockState, cubies[x][y][z], 0.0F, 0.0F, 0.0F, packedLight, packedOverlay, ModelData.EMPTY, Sheets.solidBlockSheet());
                        }
                        poseStack.popPose();
                    }
                }
            }
        } else {
            for (int x = 0; x < 2; x++) {
                for (int y = 0; y < 2; y++) {
                    for (int z = 0; z < 2; z++) {
                        poseStack.pushPose();
                        poseStack.mulPose(quat.set(blockEntity.cube[x][y][z].rotation));
                        poseStack.translate(-0.5, -0.5, -0.5);
                        if (cubies[x][y][z] != null) {
                            blockrendererdispatcher.getModelRenderer().renderModel(poseStack.last(), bufferSource.getBuffer(Sheets.solidBlockSheet()), blockState, cubies[x][y][z], 0.0F, 0.0F, 0.0F, packedLight, packedOverlay, ModelData.EMPTY, Sheets.solidBlockSheet());
                        }
                        poseStack.popPose();
                    }
                }
            }
        }
    }
}