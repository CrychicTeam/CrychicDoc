package software.bernie.geckolib.renderer.layer;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class ItemArmorGeoLayer<T extends LivingEntity & GeoAnimatable> extends GeoRenderLayer<T> {

    protected static final Map<String, ResourceLocation> ARMOR_PATH_CACHE = new Object2ObjectOpenHashMap();

    protected static final HumanoidModel<LivingEntity> INNER_ARMOR_MODEL = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));

    protected static final HumanoidModel<LivingEntity> OUTER_ARMOR_MODEL = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));

    @Nullable
    protected ItemStack mainHandStack;

    @Nullable
    protected ItemStack offhandStack;

    @Nullable
    protected ItemStack helmetStack;

    @Nullable
    protected ItemStack chestplateStack;

    @Nullable
    protected ItemStack leggingsStack;

    @Nullable
    protected ItemStack bootsStack;

    public ItemArmorGeoLayer(GeoRenderer<T> geoRenderer) {
        super(geoRenderer);
    }

    @Nonnull
    protected EquipmentSlot getEquipmentSlotForBone(GeoBone bone, ItemStack stack, T animatable) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR && stack == animatable.getItemBySlot(slot)) {
                return slot;
            }
        }
        return EquipmentSlot.CHEST;
    }

    @Nonnull
    protected ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, ItemStack stack, T animatable, HumanoidModel<?> baseModel) {
        return baseModel.body;
    }

    @Nullable
    protected ItemStack getArmorItemForBone(GeoBone bone, T animatable) {
        return null;
    }

    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        this.mainHandStack = animatable.getItemBySlot(EquipmentSlot.MAINHAND);
        this.offhandStack = animatable.getItemBySlot(EquipmentSlot.OFFHAND);
        this.helmetStack = animatable.getItemBySlot(EquipmentSlot.HEAD);
        this.chestplateStack = animatable.getItemBySlot(EquipmentSlot.CHEST);
        this.leggingsStack = animatable.getItemBySlot(EquipmentSlot.LEGS);
        this.bootsStack = animatable.getItemBySlot(EquipmentSlot.FEET);
    }

    public void renderForBone(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        ItemStack armorStack = this.getArmorItemForBone(bone, animatable);
        if (armorStack != null) {
            label24: {
                if (armorStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof AbstractSkullBlock skullBlock) {
                    this.renderSkullAsArmor(poseStack, bone, armorStack, skullBlock, bufferSource, packedLight);
                    break label24;
                }
                EquipmentSlot slot = this.getEquipmentSlotForBone(bone, armorStack, animatable);
                HumanoidModel<?> model = this.getModelForItem(bone, slot, armorStack, animatable);
                ModelPart modelPart = this.getModelPartForBone(bone, slot, armorStack, animatable, model);
                if (!modelPart.cubes.isEmpty()) {
                    poseStack.pushPose();
                    poseStack.scale(-1.0F, -1.0F, 1.0F);
                    if (model instanceof GeoArmorRenderer<?> geoArmorRenderer) {
                        this.prepModelPartForRender(poseStack, bone, modelPart);
                        geoArmorRenderer.prepForRender(animatable, armorStack, slot, model);
                        geoArmorRenderer.applyBoneVisibilityByPart(slot, modelPart, model);
                        geoArmorRenderer.renderToBuffer(poseStack, null, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
                    } else if (armorStack.getItem() instanceof ArmorItem) {
                        this.prepModelPartForRender(poseStack, bone, modelPart);
                        this.renderVanillaArmorPiece(poseStack, animatable, bone, slot, armorStack, modelPart, bufferSource, partialTick, packedLight, packedOverlay);
                    }
                    poseStack.popPose();
                }
            }
            buffer = bufferSource.getBuffer(renderType);
        }
    }

    protected <I extends Item & GeoItem> void renderVanillaArmorPiece(PoseStack poseStack, T animatable, GeoBone bone, EquipmentSlot slot, ItemStack armorStack, ModelPart modelPart, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
        ResourceLocation texture = this.getVanillaArmorResource(animatable, armorStack, slot, "");
        VertexConsumer buffer = this.getArmorBuffer(bufferSource, null, texture, armorStack.hasFoil());
        if (armorStack.getItem() instanceof DyeableArmorItem dyable) {
            int color = dyable.m_41121_(armorStack);
            modelPart.render(poseStack, buffer, packedLight, packedOverlay, (float) (color >> 16 & 0xFF) / 255.0F, (float) (color >> 8 & 0xFF) / 255.0F, (float) (color & 0xFF) / 255.0F, 1.0F);
            texture = this.getVanillaArmorResource(animatable, armorStack, slot, "overlay");
            buffer = this.getArmorBuffer(bufferSource, null, texture, false);
        }
        modelPart.render(poseStack, buffer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected VertexConsumer getArmorBuffer(MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable ResourceLocation texturePath, boolean enchanted) {
        if (renderType == null) {
            renderType = RenderType.armorCutoutNoCull(texturePath);
        }
        return ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, false, enchanted);
    }

    @Nonnull
    protected HumanoidModel<?> getModelForItem(GeoBone bone, EquipmentSlot slot, ItemStack stack, T animatable) {
        HumanoidModel<?> defaultModel = slot == EquipmentSlot.LEGS ? INNER_ARMOR_MODEL : OUTER_ARMOR_MODEL;
        return IClientItemExtensions.of(stack).getHumanoidArmorModel(animatable, stack, slot, defaultModel);
    }

    public ResourceLocation getVanillaArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, String type) {
        String domain = "minecraft";
        String path = ((ArmorItem) stack.getItem()).getMaterial().getName();
        String[] materialNameSplit = path.split(":", 2);
        if (materialNameSplit.length > 1) {
            domain = materialNameSplit[0];
            path = materialNameSplit[1];
        }
        if (!type.isBlank()) {
            type = "_" + type;
        }
        String texture = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, path, slot == EquipmentSlot.LEGS ? 2 : 1, type);
        texture = ForgeHooksClient.getArmorTexture(entity, stack, texture, slot, type);
        return (ResourceLocation) ARMOR_PATH_CACHE.computeIfAbsent(texture, ResourceLocation::new);
    }

    protected void renderSkullAsArmor(PoseStack poseStack, GeoBone bone, ItemStack stack, AbstractSkullBlock skullBlock, MultiBufferSource bufferSource, int packedLight) {
        GameProfile skullProfile = null;
        CompoundTag stackTag = stack.getTag();
        if (stackTag != null) {
            Tag skullTag = stackTag.get("SkullOwner");
            if (skullTag instanceof CompoundTag compoundTag) {
                skullProfile = NbtUtils.readGameProfile(compoundTag);
            } else if (skullTag instanceof StringTag tag) {
                String skullOwner = tag.getAsString();
                if (!skullOwner.isBlank()) {
                    CompoundTag profileTag = new CompoundTag();
                    SkullBlockEntity.updateGameprofile(new GameProfile(null, skullOwner), name -> stackTag.put("SkullOwner", NbtUtils.writeGameProfile(profileTag, name)));
                    skullProfile = NbtUtils.readGameProfile(profileTag);
                }
            }
        }
        SkullBlock.Type type = skullBlock.getType();
        SkullModelBase model = (SkullModelBase) SkullBlockRenderer.createSkullRenderers(Minecraft.getInstance().getEntityModels()).get(type);
        RenderType renderType = SkullBlockRenderer.getRenderType(type, skullProfile);
        poseStack.pushPose();
        RenderUtils.translateAndRotateMatrixForBone(poseStack, bone);
        poseStack.scale(1.1875F, 1.1875F, 1.1875F);
        poseStack.translate(-0.5F, 0.0F, -0.5F);
        SkullBlockRenderer.renderSkull(null, 0.0F, 0.0F, poseStack, bufferSource, packedLight, model, renderType);
        poseStack.popPose();
    }

    protected void prepModelPartForRender(PoseStack poseStack, GeoBone bone, ModelPart sourcePart) {
        GeoCube firstCube = (GeoCube) bone.getCubes().get(0);
        ModelPart.Cube armorCube = (ModelPart.Cube) sourcePart.cubes.get(0);
        double armorBoneSizeX = firstCube.size().x();
        double armorBoneSizeY = firstCube.size().y();
        double armorBoneSizeZ = firstCube.size().z();
        double actualArmorSizeX = (double) Math.abs(armorCube.maxX - armorCube.minX);
        double actualArmorSizeY = (double) Math.abs(armorCube.maxY - armorCube.minY);
        double actualArmorSizeZ = (double) Math.abs(armorCube.maxZ - armorCube.minZ);
        float scaleX = (float) (armorBoneSizeX / actualArmorSizeX);
        float scaleY = (float) (armorBoneSizeY / actualArmorSizeY);
        float scaleZ = (float) (armorBoneSizeZ / actualArmorSizeZ);
        sourcePart.setPos(-(bone.getPivotX() - (bone.getPivotX() * scaleX - bone.getPivotX()) / scaleX), -(bone.getPivotY() - (bone.getPivotY() * scaleY - bone.getPivotY()) / scaleY), bone.getPivotZ() - (bone.getPivotZ() * scaleZ - bone.getPivotZ()) / scaleZ);
        sourcePart.xRot = -bone.getRotX();
        sourcePart.yRot = -bone.getRotY();
        sourcePart.zRot = bone.getRotZ();
        poseStack.scale(scaleX, scaleY, scaleZ);
    }
}