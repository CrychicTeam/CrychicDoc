package io.redspace.ironsspellbooks.spells.holy;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

@AutoSpellConfig
public class HealSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "heal");

    final float twoPi = 6.283F;

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.HOLY_RESOURCE).setMaxLevel(10).setCooldownSeconds(25.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.healing", Utils.stringTruncation((double) this.getSpellPower(spellLevel, caster), 1)));
    }

    public HealSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 30;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float healAmount = this.getSpellPower(spellLevel, entity);
        MinecraftForge.EVENT_BUS.post(new SpellHealEvent(entity, entity, healAmount, this.getSchoolType()));
        entity.heal(healAmount);
        int count = 16;
        float radius = 1.25F;
        for (int i = 0; i < count; i++) {
            double theta = Math.toRadians((double) (360 / count)) * (double) i;
            double x = Math.cos(theta) * (double) radius;
            double z = Math.sin(theta) * (double) radius;
            MagicManager.spawnParticles(world, ParticleTypes.HEART, entity.m_20182_().x + x, entity.m_20182_().y, entity.m_20182_().z + z, 1, 0.0, 0.0, 0.0, 0.1, false);
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }
}