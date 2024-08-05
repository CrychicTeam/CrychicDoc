package io.redspace.ironsspellbooks.spells.ender;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
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
import io.redspace.ironsspellbooks.entity.spells.black_hole.BlackHole;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@AutoSpellConfig
public class BlackHoleSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "black_hole");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.LEGENDARY).setSchoolResource(SchoolRegistry.ENDER_RESOURCE).setMaxLevel(6).setCooldownSeconds(120.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.aoe_damage", Utils.stringTruncation((double) this.getDamage(spellLevel, caster), 1)), Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation((double) this.getRadius(spellLevel, caster), 1)));
    }

    public BlackHoleSpell() {
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
        this.castTime = 100;
        this.baseManaCost = 300;
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.BLACK_HOLE_CHARGE.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.BLACK_HOLE_CAST.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = this.getRadius(spellLevel, entity);
        HitResult raycast = Utils.raycastForEntity(level, entity, 16.0F + radius * 1.5F, true);
        Vec3 center = raycast.getLocation();
        if (raycast instanceof BlockHitResult blockHitResult) {
            if (blockHitResult.getDirection().getAxis().isHorizontal()) {
                center = center.subtract(0.0, (double) radius, 0.0);
            } else if (blockHitResult.getDirection() == Direction.DOWN) {
                center = center.subtract(0.0, (double) (radius * 2.0F), 0.0);
            }
        }
        level.playSound(null, center.x, center.y, center.z, SoundRegistry.BLACK_HOLE_CAST.get(), SoundSource.AMBIENT, 4.0F, 1.0F);
        BlackHole blackHole = new BlackHole(level, entity);
        blackHole.setRadius(radius);
        blackHole.setDamage(this.getDamage(spellLevel, entity));
        blackHole.m_20219_(center);
        level.m_7967_(blackHole);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) * 2.0F;
    }

    private float getRadius(int spellLevel, LivingEntity entity) {
        return (float) (2 * spellLevel + 4) + 0.125F * this.getSpellPower(spellLevel, entity);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.FINISH_ANIMATION;
    }

    @Override
    public boolean stopSoundOnCancel() {
        return true;
    }
}