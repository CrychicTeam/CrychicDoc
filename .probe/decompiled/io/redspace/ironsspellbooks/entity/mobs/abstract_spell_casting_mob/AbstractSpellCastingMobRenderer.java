package io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob;

import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.entity.mobs.HumanoidRenderer;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.render.ChargeSpellLayer;
import io.redspace.ironsspellbooks.render.EnergySwirlLayer;
import io.redspace.ironsspellbooks.render.GeoSpinAttackLayer;
import io.redspace.ironsspellbooks.render.GlowingEyesLayer;
import io.redspace.ironsspellbooks.render.SpellRenderingHelper;
import io.redspace.ironsspellbooks.render.SpellTargetingLayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.object.Color;

public abstract class AbstractSpellCastingMobRenderer extends HumanoidRenderer<AbstractSpellCastingMob> {

    private ResourceLocation textureResource;

    public AbstractSpellCastingMobRenderer(EntityRendererProvider.Context renderManager, AbstractSpellCastingMobModel model) {
        super(renderManager, model);
        this.f_114477_ = 0.5F;
        this.addRenderLayer(new EnergySwirlLayer.Geo(this, EnergySwirlLayer.EVASION_TEXTURE, 2L));
        this.addRenderLayer(new EnergySwirlLayer.Geo(this, EnergySwirlLayer.CHARGE_TEXTURE, 64L));
        this.addRenderLayer(new ChargeSpellLayer.Geo(this));
        this.addRenderLayer(new GlowingEyesLayer.Geo(this));
        this.addRenderLayer(new SpellTargetingLayer.Geo(this));
        this.addRenderLayer(new GeoSpinAttackLayer(this));
    }

    public static ItemStack makePotion(AbstractSpellCastingMob entity) {
        ItemStack healthPotion = new ItemStack(Items.POTION);
        return PotionUtils.setPotion(healthPotion, entity.m_21222_() ? Potions.HARMING : Potions.HEALING);
    }

    public void render(AbstractSpellCastingMob entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.m_7392_(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        SpellRenderingHelper.renderSpellHelper(ClientMagicData.getSyncedSpellData(this.animatable), this.animatable, poseStack, bufferSource, partialTick);
    }

    public Color getRenderColor(AbstractSpellCastingMob animatable, float partialTick, int packedLight) {
        return animatable.m_20145_() ? Color.ofRGBA(1.0F, 1.0F, 1.0F, 0.3F) : super.getRenderColor(animatable, partialTick, packedLight);
    }

    public RenderType getRenderType(AbstractSpellCastingMob animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return animatable.m_20145_() ? RenderType.entityTranslucent(texture) : super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}