package io.redspace.ironsspellbooks.spells.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.blood_needle.BloodNeedle;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@AutoSpellConfig
public class AcupunctureSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "acupuncture");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.BLOOD_RESOURCE).setMaxLevel(10).setCooldownSeconds(20.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation((double) this.getDamage(spellLevel, caster), 2)), Component.translatable("ui.irons_spellbooks.projectile_count", this.getCount(spellLevel, caster)));
    }

    public AcupunctureSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
        this.castTime = 0;
        this.baseManaCost = 25;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.15F);
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetData) {
            LivingEntity targetEntity = targetData.getTarget((ServerLevel) world);
            if (targetEntity != null) {
                int count = this.getCount(spellLevel, entity);
                float damage = this.getDamage(spellLevel, entity);
                Vec3 center = targetEntity.m_20182_().add(0.0, (double) (targetEntity.m_20192_() / 2.0F), 0.0);
                float degreesPerNeedle = 360.0F / (float) count;
                for (int i = 0; i < count; i++) {
                    Vec3 offset = new Vec3(0.0, Math.random(), 0.55).normalize().scale((double) (targetEntity.m_20205_() + 2.75F)).yRot(degreesPerNeedle * (float) i * (float) (Math.PI / 180.0));
                    Vec3 spawn = center.add(offset);
                    Vec3 motion = center.subtract(spawn).normalize();
                    BloodNeedle needle = new BloodNeedle(world, entity);
                    needle.m_20219_(spawn);
                    needle.shoot(motion.scale(0.35F));
                    needle.setDamage(damage);
                    needle.setScale(0.4F);
                    world.m_7967_(needle);
                }
            }
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private int getCount(int spellLevel, LivingEntity caster) {
        return (int) ((float) (4 + spellLevel) * this.getSpellPower(spellLevel, caster));
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return 1.0F + this.getSpellPower(spellLevel, caster);
    }
}