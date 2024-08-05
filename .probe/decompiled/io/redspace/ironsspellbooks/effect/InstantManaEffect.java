package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.network.ClientboundSyncMana;
import io.redspace.ironsspellbooks.setup.Messages;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class InstantManaEffect extends CustomDescriptionMobEffect {

    public static final int manaPerAmplifier = 25;

    public static final float manaPerAmplifierPercent = 0.05F;

    public InstantManaEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public Component getDescriptionLine(MobEffectInstance instance) {
        int amp = instance.getAmplifier() + 1;
        int addition = amp * 25;
        int percent = (int) ((float) amp * 0.05F * 100.0F);
        return Component.translatable("tooltip.irons_spellbooks.instant_mana_description", addition, percent).withStyle(ChatFormatting.BLUE);
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity livingEntity, int pAmplifier, double pHealth) {
        int i = pAmplifier + 1;
        int maxMana = (int) livingEntity.getAttributeValue(AttributeRegistry.MAX_MANA.get());
        int manaAdd = (int) ((float) (i * 25) + (float) maxMana * (float) i * 0.05F);
        MagicData pmg = MagicData.getPlayerMagicData(livingEntity);
        pmg.setMana(pmg.getMana() + (float) manaAdd);
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            Messages.sendToPlayer(new ClientboundSyncMana(pmg), serverPlayer);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int pAmplifier) {
        this.applyInstantenousEffect(null, null, livingEntity, pAmplifier, (double) livingEntity.getHealth());
    }
}