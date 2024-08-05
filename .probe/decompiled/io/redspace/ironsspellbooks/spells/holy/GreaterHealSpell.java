package io.redspace.ironsspellbooks.spells.holy;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.network.spell.ClientboundHealParticles;
import io.redspace.ironsspellbooks.setup.Messages;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

@AutoSpellConfig
public class GreaterHealSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "greater_heal");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.HOLY_RESOURCE).setMaxLevel(1).setCooldownSeconds(45.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.greater_healing"));
    }

    public GreaterHealSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 0;
        this.castTime = 120;
        this.baseManaCost = 100;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
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
        float healAmount = entity.getMaxHealth();
        MinecraftForge.EVENT_BUS.post(new SpellHealEvent(entity, entity, healAmount, this.getSchoolType()));
        entity.heal(healAmount);
        Messages.sendToPlayersTrackingEntity(new ClientboundHealParticles(entity.m_20182_()), entity, true);
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }
}