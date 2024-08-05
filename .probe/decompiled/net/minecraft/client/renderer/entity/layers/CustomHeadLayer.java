package net.minecraft.client.renderer.entity.layers;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.Map;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;

public class CustomHeadLayer<T extends LivingEntity, M extends EntityModel<T> & HeadedModel> extends RenderLayer<T, M> {

    private final float scaleX;

    private final float scaleY;

    private final float scaleZ;

    private final Map<SkullBlock.Type, SkullModelBase> skullModels;

    private final ItemInHandRenderer itemInHandRenderer;

    public CustomHeadLayer(RenderLayerParent<T, M> renderLayerParentTM0, EntityModelSet entityModelSet1, ItemInHandRenderer itemInHandRenderer2) {
        this(renderLayerParentTM0, entityModelSet1, 1.0F, 1.0F, 1.0F, itemInHandRenderer2);
    }

    public CustomHeadLayer(RenderLayerParent<T, M> renderLayerParentTM0, EntityModelSet entityModelSet1, float float2, float float3, float float4, ItemInHandRenderer itemInHandRenderer5) {
        super(renderLayerParentTM0);
        this.scaleX = float2;
        this.scaleY = float3;
        this.scaleZ = float4;
        this.skullModels = SkullBlockRenderer.createSkullRenderers(entityModelSet1);
        this.itemInHandRenderer = itemInHandRenderer5;
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        ItemStack $$10 = t3.getItemBySlot(EquipmentSlot.HEAD);
        if (!$$10.isEmpty()) {
            Item $$11 = $$10.getItem();
            poseStack0.pushPose();
            poseStack0.scale(this.scaleX, this.scaleY, this.scaleZ);
            boolean $$12 = t3 instanceof Villager || t3 instanceof ZombieVillager;
            if (t3.isBaby() && !(t3 instanceof Villager)) {
                float $$13 = 2.0F;
                float $$14 = 1.4F;
                poseStack0.translate(0.0F, 0.03125F, 0.0F);
                poseStack0.scale(0.7F, 0.7F, 0.7F);
                poseStack0.translate(0.0F, 1.0F, 0.0F);
            }
            ((HeadedModel) this.m_117386_()).getHead().translateAndRotate(poseStack0);
            if ($$11 instanceof BlockItem && ((BlockItem) $$11).getBlock() instanceof AbstractSkullBlock) {
                float $$15 = 1.1875F;
                poseStack0.scale(1.1875F, -1.1875F, -1.1875F);
                if ($$12) {
                    poseStack0.translate(0.0F, 0.0625F, 0.0F);
                }
                GameProfile $$16 = null;
                if ($$10.hasTag()) {
                    CompoundTag $$17 = $$10.getTag();
                    if ($$17.contains("SkullOwner", 10)) {
                        $$16 = NbtUtils.readGameProfile($$17.getCompound("SkullOwner"));
                    }
                }
                poseStack0.translate(-0.5, 0.0, -0.5);
                SkullBlock.Type $$18 = ((AbstractSkullBlock) ((BlockItem) $$11).getBlock()).getType();
                SkullModelBase $$19 = (SkullModelBase) this.skullModels.get($$18);
                RenderType $$20 = SkullBlockRenderer.getRenderType($$18, $$16);
                WalkAnimationState $$22;
                if (t3.m_20202_() instanceof LivingEntity $$21) {
                    $$22 = $$21.walkAnimation;
                } else {
                    $$22 = t3.walkAnimation;
                }
                float $$24 = $$22.position(float6);
                SkullBlockRenderer.renderSkull(null, 180.0F, $$24, poseStack0, multiBufferSource1, int2, $$19, $$20);
            } else if (!($$11 instanceof ArmorItem $$25) || $$25.getEquipmentSlot() != EquipmentSlot.HEAD) {
                translateToHead(poseStack0, $$12);
                this.itemInHandRenderer.renderItem(t3, $$10, ItemDisplayContext.HEAD, false, poseStack0, multiBufferSource1, int2);
            }
            poseStack0.popPose();
        }
    }

    public static void translateToHead(PoseStack poseStack0, boolean boolean1) {
        float $$2 = 0.625F;
        poseStack0.translate(0.0F, -0.25F, 0.0F);
        poseStack0.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack0.scale(0.625F, -0.625F, -0.625F);
        if (boolean1) {
            poseStack0.translate(0.0F, 0.1875F, 0.0F);
        }
    }
}