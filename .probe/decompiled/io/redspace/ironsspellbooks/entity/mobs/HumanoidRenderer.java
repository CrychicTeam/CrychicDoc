package io.redspace.ironsspellbooks.entity.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;

public class HumanoidRenderer<T extends Mob & GeoAnimatable> extends GeoEntityRenderer<T> {

    private static final String LEFT_HAND = "bipedHandLeft";

    private static final String RIGHT_HAND = "bipedHandRight";

    private static final String LEFT_BOOT = "armorBipedLeftFoot";

    private static final String RIGHT_BOOT = "armorBipedRightFoot";

    private static final String LEFT_BOOT_2 = "armorBipedLeftFoot2";

    private static final String RIGHT_BOOT_2 = "armorBipedRightFoot2";

    private static final String LEFT_ARMOR_LEG = "armorBipedLeftLeg";

    private static final String RIGHT_ARMOR_LEG = "armorBipedRightLeg";

    private static final String LEFT_ARMOR_LEG_2 = "armorBipedLeftLeg2";

    private static final String RIGHT_ARMOR_LEG_2 = "armorBipedRightLeg2";

    private static final String CHESTPLATE = "armorBipedBody";

    private static final String RIGHT_SLEEVE = "armorBipedRightArm";

    private static final String LEFT_SLEEVE = "armorBipedLeftArm";

    private static final String HELMET = "armorBipedHead";

    public HumanoidRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
        this.addRenderLayer(new ItemArmorGeoLayer<T>(this) {

            @Nullable
            protected ItemStack getArmorItemForBone(GeoBone bone, T animatable) {
                String var3 = bone.getName();
                return switch(var3) {
                    case "armorBipedLeftFoot", "armorBipedRightFoot", "armorBipedLeftFoot2", "armorBipedRightFoot2" ->
                        this.bootsStack;
                    case "armorBipedLeftLeg", "armorBipedRightLeg", "armorBipedLeftLeg2", "armorBipedRightLeg2" ->
                        this.leggingsStack;
                    case "armorBipedBody", "armorBipedRightArm", "armorBipedLeftArm" ->
                        this.chestplateStack;
                    case "armorBipedHead" ->
                        this.helmetStack;
                    default ->
                        null;
                };
            }

            @Nonnull
            protected EquipmentSlot getEquipmentSlotForBone(GeoBone bone, ItemStack stack, T animatable) {
                String var4 = bone.getName();
                return switch(var4) {
                    case "armorBipedLeftFoot", "armorBipedRightFoot", "armorBipedLeftFoot2", "armorBipedRightFoot2" ->
                        EquipmentSlot.FEET;
                    case "armorBipedLeftLeg", "armorBipedRightLeg", "armorBipedLeftLeg2", "armorBipedRightLeg2" ->
                        EquipmentSlot.LEGS;
                    case "armorBipedRightArm" ->
                        !animatable.isLeftHanded() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                    case "armorBipedLeftArm" ->
                        animatable.isLeftHanded() ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                    case "armorBipedBody" ->
                        EquipmentSlot.CHEST;
                    case "armorBipedHead" ->
                        EquipmentSlot.HEAD;
                    default ->
                        super.getEquipmentSlotForBone(bone, stack, animatable);
                };
            }

            @Nonnull
            protected ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, ItemStack stack, T animatable, HumanoidModel<?> baseModel) {
                String var6 = bone.getName();
                return switch(var6) {
                    case "armorBipedLeftFoot", "armorBipedLeftFoot2", "armorBipedLeftLeg", "armorBipedLeftLeg2" ->
                        baseModel.leftLeg;
                    case "armorBipedRightFoot", "armorBipedRightFoot2", "armorBipedRightLeg", "armorBipedRightLeg2" ->
                        baseModel.rightLeg;
                    case "armorBipedRightArm" ->
                        baseModel.rightArm;
                    case "armorBipedLeftArm" ->
                        baseModel.leftArm;
                    case "armorBipedBody" ->
                        baseModel.body;
                    case "armorBipedHead" ->
                        baseModel.head;
                    default ->
                        super.getModelPartForBone(bone, slot, stack, animatable, baseModel);
                };
            }
        });
        this.addRenderLayer(new BlockAndItemGeoLayer<T>(this) {

            @Nullable
            protected ItemStack getStackForBone(GeoBone bone, T animatable) {
                if (animatable instanceof AbstractSpellCastingMob castingMob) {
                    String boneName = bone.getName();
                    if (HumanoidRenderer.this.isBoneMainHand(castingMob, boneName)) {
                        if (castingMob.isDrinkingPotion()) {
                            return AbstractSpellCastingMobRenderer.makePotion(castingMob);
                        }
                        if (HumanoidRenderer.this.shouldWeaponBeSheathed(castingMob) && castingMob.m_6844_(EquipmentSlot.MAINHAND).getItem() instanceof SwordItem) {
                            return ItemStack.EMPTY;
                        }
                    }
                    if (boneName.equals("torso") && HumanoidRenderer.this.shouldWeaponBeSheathed(castingMob) && castingMob.m_6844_(EquipmentSlot.MAINHAND).getItem() instanceof SwordItem) {
                        return castingMob.m_6844_(EquipmentSlot.MAINHAND);
                    }
                }
                String var5 = bone.getName();
                return switch(var5) {
                    case "bipedHandLeft" ->
                        animatable.isLeftHanded() ? animatable.m_21205_() : animatable.m_21206_();
                    case "bipedHandRight" ->
                        animatable.isLeftHanded() ? animatable.m_21206_() : animatable.m_21205_();
                    default ->
                        null;
                };
            }

            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, T animatable) {
                String var4 = bone.getName();
                return switch(var4) {
                    case "bipedHandLeft", "bipedHandRight" ->
                        ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                    default ->
                        ItemDisplayContext.NONE;
                };
            }

            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, T animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                poseStack.translate(0.0, 0.0, -0.0625);
                poseStack.translate(0.0, -0.0625, 0.0);
                boolean offhand = stack == animatable.m_21206_();
                if (!offhand) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0.0, 0.125, -0.25);
                    }
                } else {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0.0, 0.125, 0.25);
                        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    }
                }
                if (animatable instanceof AbstractSpellCastingMob mob && bone.getChildBones().equals("torso") && HumanoidRenderer.this.shouldWeaponBeSheathed(mob)) {
                    float hipOffset = animatable.getItemBySlot(EquipmentSlot.CHEST).isEmpty() ? 0.25F : 0.325F;
                    poseStack.translate(animatable.isLeftHanded() ? (double) hipOffset : (double) (-hipOffset), -0.45, -0.225);
                    poseStack.mulPose(Axis.XP.rotationDegrees(-140.0F));
                    poseStack.scale(0.85F, 0.85F, 0.85F);
                }
                HumanoidRenderer.this.adjustHandItemRendering(poseStack, stack, animatable, partialTick, offhand);
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }

    protected boolean isBoneMainHand(AbstractSpellCastingMob entity, String boneName) {
        return entity.m_21526_() && boneName.equals("bipedHandLeft") || !entity.m_21526_() && boneName.equals("bipedHandRight");
    }

    protected boolean shouldWeaponBeSheathed(AbstractSpellCastingMob entity) {
        return entity.shouldSheathSword() && !entity.m_5912_();
    }

    protected void adjustHandItemRendering(PoseStack poseStack, ItemStack stack, T animatable, float partialTick, boolean offhand) {
    }
}