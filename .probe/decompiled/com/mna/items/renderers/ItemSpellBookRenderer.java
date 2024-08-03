package com.mna.items.renderers;

import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.items.renderers.books.ItemBookRenderer;
import com.mna.items.sorcery.ItemSpell;
import com.mna.spells.crafting.SpellRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ItemSpellBookRenderer extends ItemBookRenderer {

    private boolean renderBookInFirstPerson = true;

    public ItemSpellBookRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems, ResourceLocation openModel, ResourceLocation closedModel, boolean renderBookInFirstPerson) {
        super(berd, ems, openModel, closedModel);
        this.renderBookInFirstPerson = renderBookInFirstPerson;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ItemDisplayContext, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (ItemDisplayContext != ItemDisplayContext.THIRD_PERSON_LEFT_HAND && ItemDisplayContext != ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
            if (ItemDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
                if (!this.renderBookInFirstPerson) {
                    this.renderArm(matrixStack, buffer, combinedLight, HumanoidArm.RIGHT);
                }
                this.spawnFirstPersonParticlesForStack(stack, HumanoidArm.RIGHT);
                if (!this.renderBookInFirstPerson) {
                    return;
                }
            } else if (ItemDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
                if (!this.renderBookInFirstPerson) {
                    this.renderArm(matrixStack, buffer, combinedLight, HumanoidArm.LEFT);
                }
                this.spawnFirstPersonParticlesForStack(stack, HumanoidArm.LEFT);
                if (!this.renderBookInFirstPerson) {
                    return;
                }
            }
        } else if (!this.renderBookInFirstPerson) {
            return;
        }
        super.renderByItem(stack, ItemDisplayContext, matrixStack, buffer, combinedLight, combinedOverlay);
    }

    private void renderArm(PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, HumanoidArm side) {
        Minecraft mc = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0, mc.player.m_108560_());
        PlayerRenderer playerrenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().<AbstractClientPlayer>getRenderer(mc.player);
        matrixStackIn.pushPose();
        if (side == HumanoidArm.RIGHT) {
            matrixStackIn.translate(0.75, -0.25, 0.0);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(95.0F));
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-30.0F));
            playerrenderer.renderRightHand(matrixStackIn, bufferIn, combinedLightIn, mc.player);
        } else {
            matrixStackIn.translate(0.25, -0.25, 0.0);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(260.0F));
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(30.0F));
            playerrenderer.renderLeftHand(matrixStackIn, bufferIn, combinedLightIn, mc.player);
        }
        matrixStackIn.popPose();
    }

    private void spawnFirstPersonParticlesForStack(ItemStack stack, HumanoidArm hand) {
        if (!Minecraft.getInstance().isPaused()) {
            if (stack.getItem() instanceof ItemSpell) {
                Minecraft mc = Minecraft.getInstance();
                Player player = mc.player;
                boolean playerIsRightHanded = player.getMainArm() == HumanoidArm.RIGHT;
                boolean itemIsInUse = player.m_21212_() > 0;
                InteractionHand activeHand = player.m_7655_();
                if (!itemIsInUse || playerIsRightHanded && activeHand == InteractionHand.MAIN_HAND && hand == HumanoidArm.LEFT || !playerIsRightHanded && activeHand == InteractionHand.MAIN_HAND && hand == HumanoidArm.RIGHT) {
                    Vec3 particlePos = player.m_20182_().add(0.0, (double) (player.m_20192_() - 0.2F), 0.0);
                    Vec3 look = player.m_20154_().normalize().scale(0.5);
                    Vec3 perp = look.cross(new Vec3(0.0, 1.0, 0.0)).normalize().scale(hand == HumanoidArm.LEFT ? -0.4F : 0.4F);
                    particlePos = particlePos.add(look).add(perp);
                    SpellRecipe recipe = SpellRecipe.fromNBT(((ItemSpell) stack.getItem()).getSpellCompound(stack, player));
                    Affinity[] affs = (Affinity[]) recipe.getAffinity().keySet().toArray(new Affinity[0]);
                    switch(affs[(int) (Math.random() * (double) affs.length)]) {
                        case ARCANE:
                            {
                                Vec3 origin = new Vec3(particlePos.x, particlePos.y, particlePos.z);
                                Vec3 offset = new Vec3(player.m_9236_().random.nextGaussian(), player.m_9236_().random.nextGaussian(), player.m_9236_().random.nextGaussian()).normalize().scale(0.3F);
                                origin = origin.add(offset);
                                player.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ARCANE_LERP.get()), player), origin.x, origin.y, origin.z, particlePos.x, particlePos.y, particlePos.z);
                                break;
                            }
                        case EARTH:
                            player.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.DUST.get()), player), particlePos.x, particlePos.y, particlePos.z, -0.005F + Math.random() * 0.01F, 0.03F, -0.005F + Math.random() * 0.01F);
                            break;
                        case ENDER:
                            {
                                Vec3 origin = new Vec3(particlePos.x, particlePos.y, particlePos.z);
                                Vec3 offset = new Vec3(player.m_9236_().random.nextGaussian(), player.m_9236_().random.nextGaussian(), player.m_9236_().random.nextGaussian()).normalize().scale(0.3F);
                                origin = origin.add(offset);
                                player.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ENDER.get()), player), origin.x, origin.y, origin.z, particlePos.x, particlePos.y, particlePos.z);
                                break;
                            }
                        case FIRE:
                            player.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.FLAME.get()), player), particlePos.x, particlePos.y, particlePos.z, 0.0, 0.0, 0.0);
                            break;
                        case HELLFIRE:
                            player.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.HELLFIRE.get()), player), particlePos.x, particlePos.y, particlePos.z, 0.0, 0.0, 0.0);
                            break;
                        case LIGHTNING:
                            player.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()), player), particlePos.x, particlePos.y, particlePos.z, particlePos.x - 0.2F + Math.random() * 0.4F, particlePos.y - 0.2F + Math.random() * 0.4F, particlePos.z - 0.2F + Math.random() * 0.4F);
                            break;
                        case WATER:
                            player.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.WATER.get()), player), particlePos.x, particlePos.y, particlePos.z, -0.05 * Math.random() * 0.1, Math.random() * 0.05, -0.05 * Math.random() * 0.1);
                            break;
                        case ICE:
                            player.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.FROST.get()), player), particlePos.x, particlePos.y, particlePos.z, 0.0, 0.0, 0.0);
                            break;
                        case WIND:
                            player.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setScale(0.02F).setColor(20, 20, 20), player), particlePos.x, particlePos.y, particlePos.z, 0.3F, 0.01F, 0.05F);
                        case UNKNOWN:
                    }
                }
            }
        }
    }
}