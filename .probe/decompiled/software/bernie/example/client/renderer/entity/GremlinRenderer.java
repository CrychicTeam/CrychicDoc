package software.bernie.example.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import software.bernie.example.client.model.entity.GremlinModel;
import software.bernie.example.entity.DynamicExampleEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.DynamicGeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;

public class GremlinRenderer extends DynamicGeoEntityRenderer<DynamicExampleEntity> {

    private static final String LEFT_HAND = "bipedHandLeft";

    private static final String RIGHT_HAND = "bipedHandRight";

    private static final String LEFT_BOOT = "armorBipedLeftFoot";

    private static final String RIGHT_BOOT = "armorBipedRightFoot";

    private static final String LEFT_ARMOR_LEG = "armorBipedLeftLeg";

    private static final String RIGHT_ARMOR_LEG = "armorBipedRightLeg";

    private static final String CHESTPLATE = "armorBipedBody";

    private static final String RIGHT_SLEEVE = "armorBipedRightArm";

    private static final String LEFT_SLEEVE = "armorBipedLeftArm";

    private static final String HELMET = "armorBipedHead";

    protected final ResourceLocation CAPE_TEXTURE = new ResourceLocation("geckolib", "textures/entity/dynamic_entity_cape.png");

    protected ItemStack mainHandItem;

    protected ItemStack offhandItem;

    public GremlinRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GremlinModel());
        this.addRenderLayer(new ItemArmorGeoLayer<DynamicExampleEntity>(this) {

            @Nullable
            protected ItemStack getArmorItemForBone(GeoBone bone, DynamicExampleEntity animatable) {
                String var3 = bone.getName();
                return switch(var3) {
                    case "armorBipedLeftFoot", "armorBipedRightFoot" ->
                        this.bootsStack;
                    case "armorBipedLeftLeg", "armorBipedRightLeg" ->
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
            protected EquipmentSlot getEquipmentSlotForBone(GeoBone bone, ItemStack stack, DynamicExampleEntity animatable) {
                String var4 = bone.getName();
                return switch(var4) {
                    case "armorBipedLeftFoot", "armorBipedRightFoot" ->
                        EquipmentSlot.FEET;
                    case "armorBipedLeftLeg", "armorBipedRightLeg" ->
                        EquipmentSlot.LEGS;
                    case "armorBipedRightArm" ->
                        !animatable.m_21526_() ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                    case "armorBipedLeftArm" ->
                        animatable.m_21526_() ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                    case "armorBipedBody" ->
                        EquipmentSlot.CHEST;
                    case "armorBipedHead" ->
                        EquipmentSlot.HEAD;
                    default ->
                        super.getEquipmentSlotForBone(bone, stack, animatable);
                };
            }

            @Nonnull
            protected ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, ItemStack stack, DynamicExampleEntity animatable, HumanoidModel<?> baseModel) {
                String var6 = bone.getName();
                return switch(var6) {
                    case "armorBipedLeftFoot", "armorBipedLeftLeg" ->
                        baseModel.leftLeg;
                    case "armorBipedRightFoot", "armorBipedRightLeg" ->
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
        this.addRenderLayer(new BlockAndItemGeoLayer<DynamicExampleEntity>(this) {

            @Nullable
            protected ItemStack getStackForBone(GeoBone bone, DynamicExampleEntity animatable) {
                String var3 = bone.getName();
                return switch(var3) {
                    case "bipedHandLeft" ->
                        animatable.m_21526_() ? GremlinRenderer.this.mainHandItem : GremlinRenderer.this.offhandItem;
                    case "bipedHandRight" ->
                        animatable.m_21526_() ? GremlinRenderer.this.offhandItem : GremlinRenderer.this.mainHandItem;
                    default ->
                        null;
                };
            }

            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, DynamicExampleEntity animatable) {
                String var4 = bone.getName();
                return switch(var4) {
                    case "bipedHandLeft", "bipedHandRight" ->
                        ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                    default ->
                        ItemDisplayContext.NONE;
                };
            }

            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, DynamicExampleEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                if (stack == GremlinRenderer.this.mainHandItem) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0.0, 0.125, -0.25);
                    }
                } else if (stack == GremlinRenderer.this.offhandItem) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0.0, 0.125, 0.25);
                        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    }
                }
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }

    @Nullable
    protected ResourceLocation getTextureOverrideForBone(GeoBone bone, DynamicExampleEntity animatable, float partialTick) {
        return "bipedCape".equals(bone.getName()) ? this.CAPE_TEXTURE : null;
    }

    public void preRender(PoseStack poseStack, DynamicExampleEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        this.mainHandItem = animatable.m_21205_();
        this.offhandItem = animatable.m_21206_();
    }
}