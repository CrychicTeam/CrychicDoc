package noppes.npcs.client.renderer;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.layer.LayerGlow;
import noppes.npcs.client.layer.LayerHeadwear;
import noppes.npcs.client.layer.LayerNpcCloak;
import noppes.npcs.client.layer.LayerParts;
import noppes.npcs.client.layer.LayerPreRender;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.mixin.ArmorLayerMixin;
import noppes.npcs.mixin.LivingRenderer2Mixin;

public class RenderCustomNpc<T extends EntityCustomNpc, M extends HumanoidModel<T>> extends RenderNPCInterface<T, M> {

    private float partialTicks;

    private LivingEntity entity;

    private EntityNPCInterface npc;

    private LivingEntityRenderer renderEntity;

    public M npcmodel;

    public Model otherModel;

    public ArmorLayerMixin armorLayer;

    public final List<RenderLayer<T, M>> npclayers = Lists.newArrayList();

    private RenderLayer renderLayer = new RenderLayer(null) {

        @Override
        public void render(PoseStack mStack, MultiBufferSource typeBuffer, int lightmapUV, Entity p_225628_4_, float limbSwing, float limbSwingAmount, float partialTicks, float age, float netHeadYaw, float headPitch) {
            for (Object layer : ((LivingRenderer2Mixin) RenderCustomNpc.this.renderEntity).layers()) {
                ((RenderLayer) layer).render(mStack, typeBuffer, lightmapUV, RenderCustomNpc.this.entity, limbSwing, limbSwingAmount, partialTicks, age, netHeadYaw, headPitch);
            }
        }
    };

    private final HumanoidModel renderModel;

    public RenderCustomNpc(EntityRendererProvider.Context manager, M model) {
        super(manager, model, 0.5F);
        this.npcmodel = model;
        this.m_115326_(new CustomHeadLayer<>(this, manager.getModelSet(), manager.getItemInHandRenderer()));
        this.m_115326_(new LayerHeadwear(this));
        this.m_115326_(new LayerNpcCloak(this));
        this.m_115326_(new LayerParts<>(this));
        this.m_115326_(new ItemInHandLayer<>(this, manager.getItemInHandRenderer()));
        this.m_115326_(new LayerGlow(this));
        HumanoidArmorLayer armorLayer = new HumanoidArmorLayer<>(this, new HumanoidModel(manager.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(manager.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), manager.getModelManager());
        this.m_115326_(armorLayer);
        this.armorLayer = (ArmorLayerMixin) armorLayer;
        this.renderModel = new HumanoidModel(manager.bakeLayer(ModelLayers.PLAYER)) {

            @Override
            public void renderToBuffer(PoseStack mStack, VertexConsumer iVertex, int lightmapUV, int packedOverlayIn, float red, float green, float blue, float alpha) {
                int color = RenderCustomNpc.this.npc.display.getTint();
                if (color < 16777215) {
                    red = (float) (color >> 16 & 0xFF) / 255.0F;
                    green = (float) (color >> 8 & 0xFF) / 255.0F;
                    blue = (float) (color & 0xFF) / 255.0F;
                }
                RenderCustomNpc.this.otherModel.renderToBuffer(mStack, iVertex, lightmapUV, packedOverlayIn, red, green, blue, alpha);
            }

            @Override
            public void setupAnim(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
                if (RenderCustomNpc.this.otherModel instanceof EntityModel em) {
                    em.setupAnim(RenderCustomNpc.this.entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                }
            }

            @Override
            public void prepareMobModel(Entity npc, float animationPos, float animationSpeed, float partialTicks) {
                if (PixelmonHelper.isPixelmon(RenderCustomNpc.this.entity)) {
                    Model pixModel = (Model) PixelmonHelper.getModel(RenderCustomNpc.this.entity);
                    if (pixModel != null) {
                        RenderCustomNpc.this.otherModel = pixModel;
                        PixelmonHelper.setupModel(RenderCustomNpc.this.entity, pixModel);
                    }
                }
                if (RenderCustomNpc.this.otherModel instanceof HumanoidModel bm) {
                    bm.swimAmount = ((EntityCustomNpc) npc).m_20998_(partialTicks);
                    bm.crouching = RenderCustomNpc.this.npcmodel.crouching;
                }
                if (RenderCustomNpc.this.otherModel instanceof EntityModel em) {
                    em.riding = RenderCustomNpc.this.entity.m_20159_() && RenderCustomNpc.this.entity.m_20202_() != null && RenderCustomNpc.this.entity.m_20202_().shouldRiderSit();
                    em.young = RenderCustomNpc.this.entity.isBaby();
                    em.attackTime = RenderCustomNpc.this.m_115342_((EntityCustomNpc) npc, partialTicks);
                    em.prepareMobModel(RenderCustomNpc.this.entity, animationPos, animationSpeed, partialTicks);
                }
            }
        };
    }

    public Vec3 getRenderOffset(T npc, float partialTicks) {
        float xOffset = 0.0F;
        float yOffset = npc.currentAnimation == 0 ? npc.ais.bodyOffsetY / 10.0F - 0.5F : 0.0F;
        float zOffset = 0.0F;
        if (npc.m_6084_()) {
            if (npc.m_5803_()) {
                xOffset = (float) (-Math.cos(Math.toRadians((double) (180 - npc.ais.orientation))));
                zOffset = (float) (-Math.sin(Math.toRadians((double) npc.ais.orientation)));
                yOffset += 0.14F;
            } else if (npc.currentAnimation == 1 || npc.m_20159_()) {
                yOffset -= 0.5F - npc.modelData.getLegsY() * 0.8F;
            } else if (npc.m_6047_()) {
                yOffset = (float) ((double) yOffset - 0.125);
            }
        }
        return new Vec3((double) xOffset, (double) (yOffset * ((float) npc.display.getSize() / 5.0F)), (double) zOffset);
    }

    public void render(T npc, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        this.npc = npc;
        this.partialTicks = partialTicks;
        Entity prevEntity = this.entity;
        this.entity = npc.modelData.getEntity(npc);
        if (prevEntity != null && this.entity == null) {
            this.f_115290_ = this.npcmodel;
            this.renderEntity = null;
            this.f_115291_.clear();
            this.f_115291_.addAll(this.npclayers);
        }
        if (this.entity != null) {
            EntityRenderer render = this.f_114476_.getRenderer(this.entity);
            if (npc.modelData.simpleRender) {
                this.renderEntity = null;
                matrixStack.pushPose();
                render.render(this.entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
                this.renderNameTag(npc, Component.empty(), matrixStack, buffer, packedLight);
                matrixStack.popPose();
                return;
            }
            if (render instanceof LivingEntityRenderer) {
                this.renderEntity = (LivingEntityRenderer) render;
                this.otherModel = this.renderEntity.getModel();
                this.f_115290_ = this.renderModel;
                this.f_115291_.clear();
                this.f_115291_.add(this.renderLayer);
                if (render instanceof RenderCustomNpc) {
                    for (Object layer : ((LivingRenderer2Mixin) this.renderEntity).layers()) {
                        if (layer instanceof LayerPreRender) {
                            ((LayerPreRender) layer).preRender((EntityCustomNpc) this.entity);
                        }
                    }
                }
            } else {
                this.renderEntity = null;
                this.entity = null;
                this.f_115290_ = this.npcmodel;
                this.f_115291_.clear();
                this.f_115291_.addAll(this.npclayers);
            }
        } else {
            for (RenderLayer<T, M> layerx : this.f_115291_) {
                if (layerx instanceof LayerPreRender) {
                    ((LayerPreRender) layerx).preRender(npc);
                }
            }
        }
        this.npcmodel.rightArmPose = this.getPose(npc, npc.m_21205_());
        this.npcmodel.leftArmPose = this.getPose(npc, npc.m_21206_());
        super.render(npc, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    protected RenderType getRenderType(T p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        ResourceLocation resourcelocation = this.getTextureLocation(p_230496_1_);
        return p_230496_2_ && this.f_115290_ == this.renderModel ? this.otherModel.renderType(resourcelocation) : super.m_7225_(p_230496_1_, p_230496_2_, p_230496_3_, p_230496_4_);
    }

    public HumanoidModel.ArmPose getPose(T npc, ItemStack item) {
        if (NoppesUtilServer.IsItemStackNull(item)) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if (npc.m_21212_() > 0) {
                UseAnim enumaction = item.getUseAnimation();
                if (enumaction == UseAnim.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }
                if (enumaction == UseAnim.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }
            }
            return HumanoidModel.ArmPose.ITEM;
        }
    }

    protected void scale(T npc, PoseStack matrixScale, float f) {
        if (this.renderEntity != null) {
            this.renderColor(npc);
            int size = npc.display.getSize();
            if (this.entity instanceof EntityNPCInterface) {
                ((EntityNPCInterface) this.entity).display.setSize(5);
            }
            npc.display.setSize(size);
            matrixScale.scale(0.2F * (float) npc.display.getSize(), 0.2F * (float) npc.display.getSize(), 0.2F * (float) npc.display.getSize());
        } else {
            super.scale(npc, matrixScale, f);
        }
    }
}