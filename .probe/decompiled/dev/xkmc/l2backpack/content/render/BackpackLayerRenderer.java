package dev.xkmc.l2backpack.content.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.l2backpack.compat.CuriosCompat;
import dev.xkmc.l2backpack.content.common.BackpackModelItem;
import java.util.Optional;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BackpackLayerRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends RenderLayer<T, M> {

    public static final ModelLayerLocation MLL = new ModelLayerLocation(new ResourceLocation("l2backpack", "backpack"), "main");

    private final BackpackModel<T> model;

    public BackpackLayerRenderer(RenderLayerParent<T, M> parent, EntityModelSet set) {
        super(parent);
        this.model = new BackpackModel<>(set.bakeLayer(MLL));
    }

    public void render(PoseStack pose, MultiBufferSource buffer, int i, T entity, float f0, float f1, float f2, float f3, float f4, float f5) {
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
        BackpackModelItem item = null;
        if (stack.getItem() instanceof BackpackModelItem backpack) {
            item = backpack;
        } else {
            Optional<ItemStack> opt = CuriosCompat.getRenderingSlot(entity, e -> e.getItem() instanceof BackpackModelItem);
            if (opt.isPresent()) {
                stack = (ItemStack) opt.get();
                if (!stack.isEmpty()) {
                    item = (BackpackModelItem) stack.getItem();
                }
            }
        }
        if (item != null) {
            ((HumanoidModel) this.m_117386_()).copyPropertiesTo(this.model);
            this.model.setupAnim(entity, f0, f1, f3, f4, f5);
            ResourceLocation texture = item.getModelTexture(stack);
            VertexConsumer vc = buffer.getBuffer(RenderType.armorCutoutNoCull(texture));
            pose.pushPose();
            ((HumanoidModel) this.m_117386_()).body.translateAndRotate(pose);
            pose.mulPose(Axis.YP.rotationDegrees(180.0F));
            pose.scale(0.6F, 0.6F, 0.6F);
            this.model.f_102810_.getChild("main_body").render(pose, vc, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pose.popPose();
        }
    }
}