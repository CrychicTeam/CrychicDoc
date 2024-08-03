package com.mna.items.renderers;

import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.items.ItemInit;
import com.mna.items.SpellIconList;
import com.mna.items.sorcery.ItemSpell;
import com.mna.spells.crafting.SpellRecipe;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class ItemSpellRenderer extends BlockEntityWithoutLevelRenderer {

    private static HashMap<Integer, BakedModel> bakedSpellModels;

    private BakedModel defaultSpellModel;

    private BakedModel defaultBangleModel;

    public static final ResourceLocation location_spell = new ResourceLocation("mna", "item/spell_texture");

    public static final ResourceLocation location_bangle = new ResourceLocation("mna", "item/bangle_texture");

    public ItemSpellRenderer(BlockEntityRenderDispatcher berd, EntityModelSet ems) {
        super(berd, ems);
        if (bakedSpellModels == null) {
            bakedSpellModels = new HashMap();
        }
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext ItemDisplayContext, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (ItemDisplayContext != ItemDisplayContext.THIRD_PERSON_LEFT_HAND && ItemDisplayContext != ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
            if (ItemDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
                this.renderArm(matrixStack, buffer, combinedLight, HumanoidArm.RIGHT);
                this.spawnFirstPersonParticlesForStack(stack, HumanoidArm.RIGHT);
            } else if (ItemDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
                this.renderArm(matrixStack, buffer, combinedLight, HumanoidArm.LEFT);
                this.spawnFirstPersonParticlesForStack(stack, HumanoidArm.LEFT);
            } else {
                this.renderDefaultSpellItem(stack, ItemDisplayContext, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }

    private void renderDefaultSpellItem(ItemStack stack, ItemDisplayContext ItemDisplayContext, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (!stack.isEmpty()) {
            BakedModel spellModel = ItemDisplayContext == ItemDisplayContext.GUI ? this.getSpellModel(stack) : this.getDefaultSpellModel(stack.getItem() == ItemInit.BANGLE.get());
            matrixStack.popPose();
            matrixStack.pushPose();
            if (ItemDisplayContext != ItemDisplayContext.GUI) {
                Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext, ItemDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND, matrixStack, buffer, combinedLight, combinedOverlay, spellModel);
            } else {
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
                boolean flag = !spellModel.usesBlockLight();
                if (flag) {
                    Lighting.setupForFlatItems();
                }
                Minecraft.getInstance().getItemRenderer().render(stack, ItemDisplayContext.GUI, false, matrixStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, spellModel);
                bufferSource.endBatch();
                RenderSystem.enableDepthTest();
                if (flag) {
                    Lighting.setupFor3DItems();
                }
            }
        }
    }

    private BakedModel getSpellModel(ItemStack stack) {
        if (stack.getItem() == ItemInit.BANGLE.get()) {
            return this.getDefaultSpellModel(true);
        } else {
            int customIconIndex = ItemSpell.getCustomIcon(stack);
            if (customIconIndex >= 0 && customIconIndex < SpellIconList.ALL.length) {
                if (!bakedSpellModels.containsKey(customIconIndex)) {
                    bakedSpellModels.put(customIconIndex, Minecraft.getInstance().getModelManager().getModel(SpellIconList.ALL[customIconIndex]));
                }
                BakedModel customModel = (BakedModel) bakedSpellModels.get(customIconIndex);
                return customModel != null ? customModel : this.getDefaultSpellModel(false);
            } else {
                return this.getDefaultSpellModel(false);
            }
        }
    }

    private BakedModel getDefaultSpellModel(boolean bangle) {
        if (this.defaultSpellModel == null) {
            this.defaultSpellModel = Minecraft.getInstance().getModelManager().getModel(location_spell);
            this.defaultBangleModel = Minecraft.getInstance().getModelManager().getModel(location_bangle);
        }
        return bangle ? this.defaultBangleModel : this.defaultSpellModel;
    }

    private void renderArm(PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, HumanoidArm side) {
        Minecraft mc = Minecraft.getInstance();
        EntityRenderer<? super AbstractClientPlayer> playerrenderer = mc.getEntityRenderDispatcher().getRenderer(mc.player);
        if (playerrenderer instanceof PlayerRenderer) {
            RenderSystem.setShaderTexture(0, mc.player.m_108560_());
            matrixStackIn.pushPose();
            if (side == HumanoidArm.RIGHT) {
                matrixStackIn.translate(0.75, -0.25, 0.0);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(95.0F));
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-30.0F));
                ((PlayerRenderer) playerrenderer).renderRightHand(matrixStackIn, bufferIn, combinedLightIn, mc.player);
            } else {
                matrixStackIn.translate(0.25, -0.25, 0.0);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(260.0F));
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(30.0F));
                ((PlayerRenderer) playerrenderer).renderLeftHand(matrixStackIn, bufferIn, combinedLightIn, mc.player);
            }
            matrixStackIn.popPose();
        }
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