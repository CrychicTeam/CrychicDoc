package org.violetmoon.quark.content.tweaks.client.layer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.SkullBlock;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.client.module.UsesForCursesModule;
import org.violetmoon.zeta.util.ItemNBTHelper;

public class ArmorStandFakePlayerLayer<M extends EntityModel<ArmorStand>> extends RenderLayer<ArmorStand, M> {

    private final PlayerModel<?> playerModel;

    private final PlayerModel<?> playerModelSlim;

    public ArmorStandFakePlayerLayer(RenderLayerParent<ArmorStand, M> parent, EntityModelSet models) {
        super(parent);
        this.playerModel = new PlayerModel(models.bakeLayer(ModelLayers.PLAYER), false);
        this.playerModelSlim = new PlayerModel(models.bakeLayer(ModelLayers.PLAYER_SLIM), true);
    }

    public void render(@NotNull PoseStack pose, @NotNull MultiBufferSource buffer, int light, @NotNull ArmorStand armor, float float0, float float1, float float2, float float3, float float4, float float5) {
        if (UsesForCursesModule.staticEnabled && UsesForCursesModule.bindArmorStandsWithPlayerHeads) {
            ItemStack head = armor.getItemBySlot(EquipmentSlot.HEAD);
            if (head.is(Items.PLAYER_HEAD) && EnchantmentHelper.hasBindingCurse(head)) {
                CompoundTag skullOwner = ItemNBTHelper.getCompound(head, "SkullOwner", true);
                GameProfile profile = skullOwner != null ? NbtUtils.readGameProfile(skullOwner) : null;
                RenderType rendertype = SkullBlockRenderer.getRenderType(SkullBlock.Types.PLAYER, profile);
                if (rendertype != null) {
                    boolean slim = false;
                    if (profile != null) {
                        MinecraftProfileTexture profileTexture = (MinecraftProfileTexture) Minecraft.getInstance().getSkinManager().getInsecureSkinInformation(profile).get(Type.SKIN);
                        if (profileTexture != null) {
                            String modelMeta = profileTexture.getMetadata("model");
                            slim = "slim".equals(modelMeta);
                        }
                    }
                    pose.pushPose();
                    if (armor.isBaby()) {
                        float s = 1.0F;
                        pose.translate(0.0F, 0.0F, 0.0F);
                        pose.scale(s, s, s);
                    } else {
                        float s = 2.0F;
                        pose.translate(0.0F, -1.5F, 0.0F);
                        pose.scale(s, s, s);
                    }
                    PlayerModel<?> model = slim ? this.playerModelSlim : this.playerModel;
                    model.f_102808_.visible = false;
                    model.f_102809_.visible = false;
                    this.rotateModel(model.f_102812_, armor.getLeftArmPose());
                    this.rotateModel(model.f_102811_, armor.getRightArmPose());
                    this.rotateModel(model.leftSleeve, armor.getLeftArmPose());
                    this.rotateModel(model.rightSleeve, armor.getRightArmPose());
                    this.rotateModel(model.f_102814_, armor.getLeftLegPose());
                    this.rotateModel(model.f_102813_, armor.getRightLegPose());
                    this.rotateModel(model.leftPants, armor.getLeftLegPose());
                    this.rotateModel(model.rightPants, armor.getRightLegPose());
                    model.m_7695_(pose, buffer.getBuffer(rendertype), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                    pose.popPose();
                }
            }
        }
    }

    private void rotateModel(ModelPart part, Rotations rot) {
        part.setRotation((float) (Math.PI / 180.0) * rot.getX(), (float) (Math.PI / 180.0) * rot.getY(), (float) (Math.PI / 180.0) * rot.getZ());
    }
}