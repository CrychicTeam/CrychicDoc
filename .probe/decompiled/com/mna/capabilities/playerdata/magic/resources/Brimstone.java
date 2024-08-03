package com.mna.capabilities.playerdata.magic.resources;

import com.mna.api.capabilities.resource.CastingResourceIDs;
import com.mna.api.capabilities.resource.SimpleCastingResource;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.entities.DamageHelper;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.biome.Biome;

public class Brimstone extends SimpleCastingResource {

    public Brimstone() {
        super(GeneralConfigValues.TotalManaRegenTicks);
    }

    @Override
    public float getRegenerationModifier(LivingEntity caster) {
        float baseline = super.getRegenerationModifier(caster) / 2.0F;
        Holder<Biome> biome = caster.m_9236_().m_204166_(caster.m_20183_());
        if (biome.value().coldEnoughToSnow(caster.m_20183_())) {
            baseline *= 2.0F;
        } else if (biome.is(BiomeTags.SNOW_GOLEM_MELTS)) {
            baseline *= 0.5F;
        }
        return baseline;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return CastingResourceIDs.BRIMSTONE;
    }

    @Override
    public void setMaxAmountByLevel(int level) {
        this.setMaxAmount((float) (50 + 10 * level));
    }

    @Override
    public boolean hungerAffectsRegenRate() {
        return false;
    }

    @Override
    public void consume(LivingEntity caster, float amount) {
        if (this.getAmount() < amount) {
            ItemStack mainHand = caster.getMainHandItem();
            ItemStack offHand = caster.getOffhandItem();
            float brimstonePerHeart = (float) GeneralConfigValues.BrimstonePerHeart;
            if (mainHand.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) > 0 || offHand.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) > 0) {
                brimstonePerHeart *= 2.0F;
            }
            int heartsRequired = (int) Math.ceil((double) ((amount - this.getAmount()) / brimstonePerHeart));
            caster.hurt(DamageHelper.forType(DamageHelper.CONFLAGRATE, caster.m_9236_().registryAccess()), (float) heartsRequired);
        }
        super.consume(caster, amount);
    }

    @Override
    public boolean hasEnough(LivingEntity caster, float amount) {
        if (caster.m_9236_().m_46791_() == Difficulty.HARD) {
            return true;
        } else {
            float base = this.getAmount();
            float hearts_add = (caster.getHealth() - 1.0F) * (float) GeneralConfigValues.BrimstonePerHeart;
            return amount <= base + hearts_add;
        }
    }
}