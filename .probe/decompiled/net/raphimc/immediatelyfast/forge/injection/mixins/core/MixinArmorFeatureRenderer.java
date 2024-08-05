package net.raphimc.immediatelyfast.forge.injection.mixins.core;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { HumanoidArmorLayer.class }, priority = 500)
public abstract class MixinArmorFeatureRenderer<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    @Unique
    private final List<Runnable> immediatelyFast$trimRenderers = new ArrayList();

    @Shadow
    protected abstract void setPartVisibility(A var1, EquipmentSlot var2);

    @Shadow
    protected abstract void renderTrim(ArmorMaterial var1, PoseStack var2, MultiBufferSource var3, int var4, ArmorTrim var5, Model var6, boolean var7);

    public MixinArmorFeatureRenderer(RenderLayerParent<T, M> context) {
        super(context);
    }

    @Inject(method = { "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V" }, at = { @At("RETURN") })
    private void renderTrimsSeparate(CallbackInfo ci) {
        this.immediatelyFast$trimRenderers.forEach(Runnable::run);
        this.immediatelyFast$trimRenderers.clear();
    }

    @Overwrite
    private void lambda$renderArmorPiece$0(ArmorItem armorItem, PoseStack matrices, MultiBufferSource vertexConsumers, int light, Model model, boolean leggings, ArmorTrim trim) {
        this.immediatelyFast$trimRenderers.add((Runnable) () -> {
            ((HumanoidModel) this.m_117386_()).copyPropertiesTo((HumanoidModel<T>) model);
            this.setPartVisibility((A) model, armorItem.getEquipmentSlot());
            this.renderTrim(armorItem.getMaterial(), matrices, vertexConsumers, light, trim, model, leggings);
        });
    }
}