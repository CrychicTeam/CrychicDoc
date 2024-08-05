package io.redspace.ironsspellbooks.spells.ice;

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
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.ice_block.IceBlockProjectile;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@AutoSpellConfig
public class IceBlockSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "ice_block");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.ICE_RESOURCE).setMaxLevel(6).setCooldownSeconds(15.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation((double) this.getSpellPower(spellLevel, caster), 1)));
    }

    public IceBlockSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 2;
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
        return Optional.of(SoundRegistry.ICE_BLOCK_CAST.get());
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        Utils.preCastTargetHelper(level, entity, playerMagicData, this, 48, 0.35F, false);
        return true;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 spawn = null;
        LivingEntity target = null;
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargetingData) {
            target = castTargetingData.getTarget((ServerLevel) level);
            if (target != null) {
                spawn = target.m_20182_();
            }
        }
        if (spawn == null) {
            HitResult raycast = Utils.raycastForEntity(level, entity, 32.0F, true, 0.25F);
            if (raycast.getType() == HitResult.Type.ENTITY) {
                spawn = ((EntityHitResult) raycast).getEntity().position();
                if (((EntityHitResult) raycast).getEntity() instanceof LivingEntity livingEntity) {
                    target = livingEntity;
                }
            } else {
                spawn = raycast.getLocation().subtract(entity.m_20156_().normalize());
            }
        }
        IceBlockProjectile iceBlock = new IceBlockProjectile(level, entity, target);
        iceBlock.m_20219_(this.raiseWithCollision(spawn, 4, level));
        iceBlock.setAirTime(target == null ? 25 : 35);
        iceBlock.setDamage(this.getDamage(spellLevel, entity));
        level.m_7967_(iceBlock);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private Vec3 raiseWithCollision(Vec3 start, int blocks, Level level) {
        for (int i = 0; i < blocks; i++) {
            Vec3 raised = start.add(0.0, 1.0, 0.0);
            if (!level.getBlockState(BlockPos.containing(raised)).m_60795_()) {
                break;
            }
            start = raised;
        }
        return start;
    }

    @Override
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setFreezeTicks(100);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity);
    }
}