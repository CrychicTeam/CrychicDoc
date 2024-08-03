package org.violetmoon.quark.mixin.mixins.client;

import net.minecraft.client.model.ArmorStandModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.quark.content.client.module.UsesForCursesModule;

@Mixin({ ArmorStandModel.class })
public class ArmorStandModelMixin {

    @Shadow
    @Final
    private ModelPart rightBodyStick;

    @Shadow
    @Final
    private ModelPart leftBodyStick;

    @Shadow
    @Final
    private ModelPart shoulderStick;

    @Inject(method = { "setupAnim(Lnet/minecraft/world/entity/decoration/ArmorStand;FFFFF)V" }, at = { @At("HEAD") })
    public void resetModelPartVisibility(ArmorStand armorStand, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ArmorStandModel model = (ArmorStandModel) this;
        model.f_102813_.visible = true;
        model.f_102814_.visible = true;
        this.rightBodyStick.visible = true;
        this.leftBodyStick.visible = true;
        this.shoulderStick.visible = true;
        model.f_102808_.visible = true;
        model.f_102810_.visible = true;
    }

    @Inject(method = { "setupAnim(Lnet/minecraft/world/entity/decoration/ArmorStand;FFFFF)V" }, at = { @At("RETURN") })
    public void setModelPartsVisible(ArmorStand armorStand, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ArmorStandModel model = (ArmorStandModel) this;
        ItemStack head = armorStand.getItemBySlot(EquipmentSlot.HEAD);
        if (UsesForCursesModule.shouldHideArmorStandModel(head)) {
            model.f_102813_.visible = false;
            model.f_102814_.visible = false;
            this.rightBodyStick.visible = false;
            this.leftBodyStick.visible = false;
            this.shoulderStick.visible = false;
            model.f_102808_.visible = false;
            model.f_102810_.visible = false;
        }
    }
}