package io.redspace.ironsspellbooks.spells.evocation;

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
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.ArrowVolleyEntity;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@AutoSpellConfig
public class ArrowVolleySpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "arrow_volley");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE).setMaxLevel(6).setCooldownSeconds(15.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation((double) this.getDamage(spellLevel, caster), 1)), Component.translatable("ui.irons_spellbooks.projectile_count", this.getCount(spellLevel, caster)));
    }

    public ArrowVolleySpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 1;
        this.castTime = 30;
        this.baseManaCost = 40;
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
        return Optional.of(SoundRegistry.ARROW_VOLLEY_PREPARE.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.EVOKER_CAST_SPELL);
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        Utils.preCastTargetHelper(level, entity, playerMagicData, this, 48, 0.25F, false);
        return true;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 targetLocation = null;
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargetingData) {
            targetLocation = castTargetingData.getTargetPosition((ServerLevel) level);
        }
        if (targetLocation == null) {
            targetLocation = Utils.raycastForEntity(level, entity, 100.0F, true).getLocation();
        }
        Vec3 backward = new Vec3(targetLocation.x - entity.m_20185_(), 0.0, targetLocation.z - entity.m_20189_()).normalize().scale(-4.0);
        Vec3 raycastTarget = Utils.moveToRelativeGroundLevel(level, targetLocation.add(0.0, 2.0, 0.0), 4).add(backward).add(0.0, 6.0, 0.0);
        Vec3 spawnLocation = Utils.raycastForBlock(level, targetLocation, raycastTarget, ClipContext.Fluid.NONE).m_82450_();
        spawnLocation = spawnLocation.subtract(targetLocation).scale(0.9F).add(targetLocation);
        float dx = Mth.sqrt((float) ((spawnLocation.x - targetLocation.x) * (spawnLocation.x - targetLocation.x) + (spawnLocation.z - targetLocation.z) * (spawnLocation.z - targetLocation.z)));
        float arrowAngleX = dx == 0.0F ? 70.0F : (float) (Mth.atan2((double) dx, spawnLocation.y - targetLocation.y) * 180.0F / (float) Math.PI);
        float arrowAngleY = entity.m_20185_() == targetLocation.x && entity.m_20189_() == targetLocation.z ? (entity.m_146908_() - 90.0F) * (float) (Math.PI / 180.0) : Utils.getAngle(entity.m_20185_(), entity.m_20189_(), targetLocation.x, targetLocation.z);
        ArrowVolleyEntity arrowVolleyEntity = new ArrowVolleyEntity(EntityRegistry.ARROW_VOLLEY_ENTITY.get(), level);
        arrowVolleyEntity.m_20219_(spawnLocation);
        arrowVolleyEntity.m_146922_(arrowAngleY * (180.0F / (float) Math.PI) + 90.0F);
        arrowVolleyEntity.m_146926_(arrowAngleX + 25.0F);
        arrowVolleyEntity.setDamage(this.getDamage(spellLevel, entity));
        arrowVolleyEntity.setArrowsPerRow(this.getArrowsPerRow(spellLevel, entity));
        arrowVolleyEntity.setRows(this.getRows(spellLevel, entity));
        arrowVolleyEntity.m_5602_(entity);
        level.m_7967_(arrowVolleyEntity);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private int getCount(int spellLevel, LivingEntity entity) {
        return this.getRows(spellLevel, entity) * this.getArrowsPerRow(spellLevel, entity);
    }

    private int getRows(int spellLevel, LivingEntity entity) {
        return 4 + spellLevel;
    }

    private int getArrowsPerRow(int spellLevel, LivingEntity entity) {
        return 5 + spellLevel / 2;
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) * 0.25F;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_RAISED_HAND;
    }
}