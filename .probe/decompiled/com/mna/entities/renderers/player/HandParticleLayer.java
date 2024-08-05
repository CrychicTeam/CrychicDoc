package com.mna.entities.renderers.player;

import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.adjusters.SpellAdjustingContext;
import com.mna.api.spells.adjusters.SpellCastStage;
import com.mna.items.sorcery.ItemBangle;
import com.mna.items.sorcery.ItemSpell;
import com.mna.items.sorcery.ItemSpellBook;
import com.mna.items.sorcery.ItemStaff;
import com.mna.spells.SpellAdjusters;
import com.mna.spells.crafting.SpellRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class HandParticleLayer<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {

    public HandParticleLayer(RenderLayerParent<T, M> rendererIn) {
        super(rendererIn);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.m_20145_()) {
            boolean playerIsRightHanded = entitylivingbaseIn.getMainArm() == HumanoidArm.RIGHT;
            boolean itemIsInUse = entitylivingbaseIn.getUseItemRemainingTicks() > 0;
            InteractionHand activeHand = entitylivingbaseIn.getUsedItemHand();
            ItemStack rightHandItem = playerIsRightHanded ? entitylivingbaseIn.getOffhandItem() : entitylivingbaseIn.getMainHandItem();
            ItemStack leftHandItem = playerIsRightHanded ? entitylivingbaseIn.getMainHandItem() : entitylivingbaseIn.getOffhandItem();
            if (!rightHandItem.isEmpty() || !leftHandItem.isEmpty()) {
                matrixStackIn.pushPose();
                if (this.m_117386_().young) {
                    matrixStackIn.translate(0.0, 0.75, 0.0);
                    matrixStackIn.scale(0.5F, 0.5F, 0.5F);
                }
                if (!itemIsInUse || playerIsRightHanded && activeHand == InteractionHand.OFF_HAND || !playerIsRightHanded && activeHand == InteractionHand.MAIN_HAND) {
                    this.renderHandParticle(entitylivingbaseIn, leftHandItem, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, matrixStackIn, bufferIn, packedLightIn);
                }
                if (!itemIsInUse || !playerIsRightHanded && activeHand == InteractionHand.OFF_HAND || playerIsRightHanded && activeHand == InteractionHand.MAIN_HAND) {
                    this.renderHandParticle(entitylivingbaseIn, rightHandItem, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, matrixStackIn, bufferIn, packedLightIn);
                }
                matrixStackIn.popPose();
            }
        }
    }

    private void renderHandParticle(LivingEntity living, ItemStack stack, ItemDisplayContext ItemDisplayContext, HumanoidArm side, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        if (!Minecraft.getInstance().isPaused()) {
            if (!stack.isEmpty() && stack.getItem() instanceof ItemSpell) {
                matrixStack.pushPose();
                CompoundTag nbt = ((ItemSpell) stack.getItem()).getSpellCompound(stack, living instanceof Player ? (Player) living : null);
                SpellRecipe recipe = SpellRecipe.fromNBT(nbt);
                Matrix4f curMatrix = matrixStack.last().pose();
                Matrix4f inverted = curMatrix.invert();
                curMatrix.mul(inverted);
                matrixStack.mulPose(Axis.YP.rotationDegrees(-living.yBodyRot));
                matrixStack.mulPose(Axis.XN.rotationDegrees(-90.0F));
                ((ArmedModel) this.m_117386_()).translateToHand(side, matrixStack);
                if (stack.getItem() instanceof ItemStaff && !(stack.getItem() instanceof ItemBangle)) {
                    matrixStack.translate(0.0, 1.5, 0.0);
                } else if (stack.getItem() instanceof ItemSpellBook && ((ItemSpellBook) stack.getItem()).renderBookModel) {
                    matrixStack.translate(0.0, 0.5, -0.55);
                }
                boolean leftHand = side == HumanoidArm.LEFT;
                if (leftHand) {
                    matrixStack.translate(0.225, 0.65, -0.95);
                } else {
                    matrixStack.translate(-0.225, 0.65, -0.95);
                }
                if (living instanceof Player && SpellAdjusters.checkHellfireStaff(new SpellAdjustingContext(stack, recipe, living, SpellCastStage.CASTING))) {
                    SpellAdjusters.modifyHellfireStaff(recipe, living);
                }
                Affinity[] affs = (Affinity[]) recipe.getAffinity().keySet().toArray(new Affinity[0]);
                this.spawnParticleFromMatrix(affs[(int) (Math.random() * (double) affs.length)], recipe, matrixStack, living, ItemDisplayContext);
                matrixStack.popPose();
            }
        }
    }

    private void spawnParticleFromMatrix(Affinity affinity, SpellRecipe recipe, PoseStack matrixStackIn, LivingEntity entitylivingbaseIn, ItemDisplayContext type) {
        Vec3 playerPos = entitylivingbaseIn.m_20182_();
        Matrix4f curMatrix = matrixStackIn.last().pose();
        Vec3 particlePos = playerPos.add(new Vec3((double) curMatrix.m03(), (double) curMatrix.m13(), (double) curMatrix.m23()));
        switch(affinity) {
            case ARCANE:
                {
                    Vec3 origin = new Vec3(particlePos.x, particlePos.y, particlePos.z);
                    Vec3 offset = new Vec3(entitylivingbaseIn.m_9236_().random.nextGaussian(), entitylivingbaseIn.m_9236_().random.nextGaussian(), entitylivingbaseIn.m_9236_().random.nextGaussian()).normalize().scale(0.3F);
                    origin = origin.add(offset);
                    entitylivingbaseIn.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ARCANE_LERP.get()), entitylivingbaseIn), origin.x, origin.y, origin.z, particlePos.x, particlePos.y, particlePos.z);
                    break;
                }
            case EARTH:
                entitylivingbaseIn.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.DUST.get()), entitylivingbaseIn), particlePos.x, particlePos.y, particlePos.z, -0.005F + Math.random() * 0.01F, 0.03F, -0.005F + Math.random() * 0.01F);
                break;
            case ENDER:
                {
                    Vec3 origin = new Vec3(particlePos.x, particlePos.y, particlePos.z);
                    Vec3 offset = new Vec3(entitylivingbaseIn.m_9236_().random.nextGaussian(), entitylivingbaseIn.m_9236_().random.nextGaussian(), entitylivingbaseIn.m_9236_().random.nextGaussian()).normalize().scale(0.3F);
                    origin = origin.add(offset);
                    entitylivingbaseIn.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ENDER.get()), entitylivingbaseIn), origin.x, origin.y, origin.z, particlePos.x, particlePos.y, particlePos.z);
                    break;
                }
            case FIRE:
                entitylivingbaseIn.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.FLAME.get()), entitylivingbaseIn), particlePos.x, particlePos.y, particlePos.z, 0.0, 0.0, 0.0);
                break;
            case HELLFIRE:
                entitylivingbaseIn.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.HELLFIRE.get()), entitylivingbaseIn), particlePos.x, particlePos.y, particlePos.z, 0.0, 0.0, 0.0);
                break;
            case LIGHTNING:
                entitylivingbaseIn.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()), entitylivingbaseIn), particlePos.x, particlePos.y, particlePos.z, particlePos.x - 0.4F + Math.random() * 0.8F, particlePos.y - 0.4F + Math.random() * 0.8F, particlePos.z - 0.4F + Math.random() * 0.8F);
                break;
            case WATER:
                entitylivingbaseIn.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.WATER.get()), entitylivingbaseIn), particlePos.x, particlePos.y, particlePos.z, -0.05 * Math.random() * 0.1, Math.random() * 0.05, -0.05 * Math.random() * 0.1);
                break;
            case ICE:
                entitylivingbaseIn.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.FROST.get()), entitylivingbaseIn), particlePos.x, particlePos.y, particlePos.z, 0.0, 0.0, 0.0);
                break;
            case WIND:
                entitylivingbaseIn.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setScale(0.05F).setColor(10, 10, 10), entitylivingbaseIn), particlePos.x, particlePos.y, particlePos.z, 0.3F, 0.01F, 0.05F);
            case UNKNOWN:
        }
    }
}