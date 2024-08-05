package noobanidus.mods.lootr.client.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;
import noobanidus.mods.lootr.block.entities.LootrShulkerBlockEntity;
import noobanidus.mods.lootr.config.ConfigManager;

public class LootrShulkerBlockRenderer implements BlockEntityRenderer<LootrShulkerBlockEntity> {

    public static final Material MATERIAL = new Material(Sheets.SHULKER_SHEET, new ResourceLocation("lootr", "shulker"));

    public static final Material MATERIAL2 = new Material(Sheets.SHULKER_SHEET, new ResourceLocation("lootr", "shulker_opened"));

    public static final Material MATERIAL3 = new Material(Sheets.SHULKER_SHEET, new ResourceLocation("lootr", "old_shulker"));

    public static final Material MATERIAL4 = new Material(Sheets.SHULKER_SHEET, new ResourceLocation("lootr", "old_shulker_opened"));

    private final ShulkerModel<?> model;

    private UUID playerId;

    public LootrShulkerBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ShulkerModel(context.bakeLayer(ModelLayers.SHULKER));
    }

    protected Material getMaterial(LootrShulkerBlockEntity tile) {
        if (ConfigManager.isVanillaTextures()) {
            return Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION;
        } else {
            if (this.playerId == null) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player == null) {
                    return ConfigManager.isOldTextures() ? MATERIAL3 : MATERIAL;
                }
                this.playerId = mc.player.m_20148_();
            }
            if (tile.getOpeners().contains(this.playerId)) {
                return ConfigManager.isOldTextures() ? MATERIAL4 : MATERIAL2;
            } else {
                return ConfigManager.isOldTextures() ? MATERIAL3 : MATERIAL;
            }
        }
    }

    public void render(LootrShulkerBlockEntity pBlockEntity, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {
        Direction direction = Direction.UP;
        if (pBlockEntity.m_58898_()) {
            BlockState blockstate = pBlockEntity.m_58904_().getBlockState(pBlockEntity.m_58899_());
            if (blockstate.m_60734_() instanceof ShulkerBoxBlock) {
                direction = (Direction) blockstate.m_61143_(ShulkerBoxBlock.FACING);
            }
        }
        Material material = this.getMaterial(pBlockEntity);
        pMatrixStack.pushPose();
        pMatrixStack.translate(0.5, 0.5, 0.5);
        pMatrixStack.scale(0.9995F, 0.9995F, 0.9995F);
        pMatrixStack.mulPose(direction.getRotation());
        pMatrixStack.scale(1.0F, -1.0F, -1.0F);
        pMatrixStack.translate(0.0, -1.0, 0.0);
        ModelPart modelpart = this.model.getLid();
        modelpart.setPos(0.0F, 24.0F - pBlockEntity.getProgress(pPartialTicks) * 0.5F * 16.0F, 0.0F);
        modelpart.yRot = 270.0F * pBlockEntity.getProgress(pPartialTicks) * (float) (Math.PI / 180.0);
        VertexConsumer vertexconsumer = material.buffer(pBuffer, RenderType::m_110458_);
        this.model.m_7695_(pMatrixStack, vertexconsumer, pCombinedLight, pCombinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        pMatrixStack.popPose();
    }
}