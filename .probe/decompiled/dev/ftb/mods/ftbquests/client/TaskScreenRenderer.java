package dev.ftb.mods.ftbquests.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.ftb.mods.ftblibrary.icon.AtlasSpriteIcon;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.IconAnimation;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftbquests.block.TaskScreenBlock;
import dev.ftb.mods.ftbquests.block.entity.TaskScreenBlockEntity;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.EnergyTask;
import dev.ftb.mods.ftbquests.quest.task.FluidTask;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class TaskScreenRenderer implements BlockEntityRenderer<TaskScreenBlockEntity> {

    public static final ResourceLocation INPUT_ONLY_TEXTURE = new ResourceLocation("ftbquests", "tasks/input_only");

    public static final ResourceLocation TANK_TEXTURE = new ResourceLocation("ftbquests", "tasks/tank");

    public static final ResourceLocation FE_ENERGY_EMPTY_TEXTURE = new ResourceLocation("ftbquests", "tasks/fe_empty");

    public static final ResourceLocation FE_ENERGY_FULL_TEXTURE = new ResourceLocation("ftbquests", "tasks/fe_full");

    public static final ResourceLocation TR_ENERGY_EMPTY_TEXTURE = new ResourceLocation("ftbquests", "tasks/ic2_empty");

    public static final ResourceLocation TR_ENERGY_FULL_TEXTURE = new ResourceLocation("ftbquests", "tasks/ic2_full");

    private final BlockEntityRendererProvider.Context context;

    public TaskScreenRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public int getViewDistance() {
        return 64;
    }

    public boolean shouldRender(TaskScreenBlockEntity blockEntity, Vec3 vec3) {
        return BlockEntityRenderer.super.shouldRender(blockEntity, vec3);
    }

    public void render(TaskScreenBlockEntity taskScreen, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        if (ClientQuestFile.exists() && taskScreen.m_58900_().m_60734_() instanceof TaskScreenBlock taskScreenBlock) {
            TeamData var21 = ClientQuestFile.INSTANCE.getNullableTeamData(taskScreen.getTeamId());
            Task task = taskScreen.getTask();
            if (task != null && var21 != null) {
                poseStack.pushPose();
                poseStack.translate(0.5, 0.5, 0.5);
                float rotation = ((Direction) taskScreen.m_58900_().m_61143_(WallSignBlock.FACING)).toYRot() + 180.0F;
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
                poseStack.translate(-0.5, -0.5, -0.5);
                int size = taskScreenBlock.getSize() / 2;
                poseStack.translate((float) (-size), (float) (-size) * 2.0F, -0.02F);
                poseStack.scale((float) size * 2.0F + 1.0F, (float) size * 2.0F + 1.0F, 1.0F);
                Font font = this.context.getFont();
                double iconY = 0.5;
                Component top1 = (Component) (taskScreen.isInputOnly() ? Component.empty() : task.getQuest().getTitle());
                Component top2 = (Component) (taskScreen.isInputOnly() ? Component.empty() : task.getTitle());
                this.drawString(taskScreen, font, multiBufferSource, poseStack, top1, 0.02, 0.15);
                if (!top2.equals(Component.empty())) {
                    this.drawString(taskScreen, font, multiBufferSource, poseStack, top2, 0.17, 0.07);
                    iconY = 0.54;
                }
                if (!taskScreen.isInputOnly() && !task.hideProgressNumbers()) {
                    long progress = var21.getProgress(task);
                    ChatFormatting col = progress == 0L ? ChatFormatting.GOLD : (progress < task.getMaxProgress() ? ChatFormatting.YELLOW : ChatFormatting.GREEN);
                    Component txt = Component.literal(task.formatProgress(var21, progress) + " / " + task.formatMaxProgress()).withStyle(col);
                    this.drawString(taskScreen, font, multiBufferSource, poseStack, txt, 0.83, 0.15);
                }
                poseStack.pushPose();
                poseStack.translate(0.5, iconY, -0.01);
                poseStack.scale(taskScreen.isInputOnly() ? 0.5F : 0.45F, taskScreen.isInputOnly() ? 0.5F : 0.45F, 0.2F * (float) size);
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                if (taskScreen.isInputOnly() && !taskScreen.getInputModeIcon().isEmpty()) {
                    this.drawTaskIcon(taskScreen, var21, ItemIcon.getItemIcon(taskScreen.getInputModeIcon()), poseStack, multiBufferSource);
                } else {
                    this.drawTaskIcon(taskScreen, var21, task.getIcon(), poseStack, multiBufferSource);
                }
                poseStack.popPose();
                VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.text(InventoryMenu.BLOCK_ATLAS));
                float[] f = taskScreen.getFakeTextureUV();
                if (f != null && f.length == 4) {
                    poseStack.pushPose();
                    poseStack.scale(0.0625F, 0.0625F, 0.0625F);
                    poseStack.translate(0.0F, 0.0F, 0.01F);
                    RenderUtil.create(poseStack, vertexConsumer, 0.0F, 0.0F).withUV(f[0], f[1], f[2], f[3]).draw();
                    poseStack.popPose();
                }
                if (taskScreen.isInputOnly() && FTBQuestsClientEventHandler.inputOnlySprite != null) {
                    TextureAtlasSprite s = FTBQuestsClientEventHandler.inputOnlySprite;
                    poseStack.pushPose();
                    poseStack.scale(0.0625F, 0.0625F, 0.0625F);
                    RenderUtil.create(poseStack, vertexConsumer, 0.0F, 0.0F).withUV(s.getU0(), s.getV0(), s.getU1(), s.getV1()).draw();
                    poseStack.popPose();
                }
                poseStack.popPose();
            }
        }
    }

    private void drawTaskIcon(TaskScreenBlockEntity taskScreen, TeamData data, Icon icon, PoseStack poseStack, MultiBufferSource buffer) {
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.text(InventoryMenu.BLOCK_ATLAS));
        Task task = taskScreen.getTask();
        long progress = data.getProgress(task);
        poseStack.pushPose();
        poseStack.scale(0.0625F, 0.0625F, 0.0625F);
        if (icon instanceof IconAnimation anim) {
            icon = (Icon) anim.list.get((int) (System.currentTimeMillis() / 1000L % (long) anim.list.size()));
        }
        label32: {
            if (task instanceof FluidTask fluidTask && fluidTask.getIcon() instanceof AtlasSpriteIcon as && FTBQuestsClientEventHandler.tankSprite != null) {
                TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(as.getId());
                if (progress > 0L) {
                    float heightInterpolated = 16.0F * (float) ((double) progress / (double) task.getMaxProgress());
                    RenderUtil.create(poseStack, vertexConsumer, -8.0F, -8.0F).withColor(FluidStackHooks.getColor(fluidTask.getFluid()) | 0xFF000000).withSize(16.0F, heightInterpolated).withUV(sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV((double) heightInterpolated)).draw();
                }
                TextureAtlasSprite s = FTBQuestsClientEventHandler.tankSprite;
                poseStack.translate(0.0F, 0.0F, -0.05F);
                RenderUtil.create(poseStack, vertexConsumer, -8.0F, -8.0F).withUV(s.getU0(), s.getV0(), s.getU1(), s.getV1()).draw();
                break label32;
            }
            if (task instanceof EnergyTask energyTask) {
                TextureAtlasSprite empty = energyTask.getClientData().getEmptyTexture();
                TextureAtlasSprite full = energyTask.getClientData().getFullTexture();
                RenderUtil.create(poseStack, vertexConsumer, -8.0F, -8.0F).withUV(empty.getU0(), empty.getV0(), empty.getU1(), empty.getV1()).draw();
                if (progress > 0L) {
                    float heightInterpolated = 16.0F * (float) ((double) progress / (double) task.getMaxProgress());
                    poseStack.translate(0.0F, 0.0F, -0.05F);
                    RenderUtil.create(poseStack, vertexConsumer, -8.0F, -8.0F).withSize(16.0F, heightInterpolated).withUV(full.getU0(), full.getV0(), full.getU1(), full.getV((double) heightInterpolated)).draw();
                }
            } else if (icon instanceof ItemIcon itemIcon) {
                poseStack.scale(16.0F, 16.0F, 16.0F);
                Minecraft.getInstance().getItemRenderer().renderStatic(itemIcon.getStack(), ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, poseStack, buffer, Minecraft.getInstance().level, 0);
            } else if (icon instanceof AtlasSpriteIcon spriteIcon) {
                TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(spriteIcon.getId());
                RenderUtil.create(poseStack, vertexConsumer, -8.0F, -8.0F).withUV(sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1()).draw();
            }
        }
        poseStack.popPose();
    }

    private void drawString(TaskScreenBlockEntity taskScreen, Font font, MultiBufferSource bufferSource, PoseStack poseStack, Component text, double y, double size) {
        if (!text.equals(Component.empty())) {
            poseStack.pushPose();
            poseStack.translate(0.5, y, 0.0);
            int len = font.width(text);
            float scale = (float) (size / 9.0);
            double width = (double) ((float) len * scale);
            if (width > 1.0) {
                scale = (float) ((double) scale / width);
                width = 1.0;
            }
            if (width > 0.9) {
                scale = (float) ((double) scale * 0.9);
            }
            poseStack.scale(scale, scale, 1.0F);
            Matrix4f posMat = poseStack.last().pose();
            font.drawInBatch(text, (float) (-len) / 2.0F, 0.0F, -2565928, taskScreen.isTextShadow(), posMat, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            poseStack.popPose();
        }
    }
}