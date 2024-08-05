package io.redspace.ironsspellbooks.spells.evocation;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.ExtendedEvokerFang;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@AutoSpellConfig
public class FangStrikeSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "fang_strike");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE).setMaxLevel(10).setCooldownSeconds(5.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.fang_count", this.getCount(spellLevel, caster)), Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation((double) this.getDamage(spellLevel, caster), 2)));
    }

    public FangStrikeSpell() {
        this.manaCostPerLevel = 3;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 1;
        this.castTime = 15;
        this.baseManaCost = 30;
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
        return Optional.of(SoundEvents.EVOKER_PREPARE_ATTACK);
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 forward = entity.m_20156_().multiply(1.0, 0.0, 1.0).normalize();
        Vec3 start = entity.m_146892_().add(forward.scale(1.5));
        for (int i = 0; i < this.getCount(spellLevel, entity); i++) {
            Vec3 spawn = start.add(forward.scale((double) i));
            spawn = new Vec3(spawn.x, (double) this.getGroundLevel(world, spawn, 8), spawn.z);
            if (!world.getBlockState(BlockPos.containing(spawn).below()).m_60795_()) {
                int delay = i / 3;
                ExtendedEvokerFang fang = new ExtendedEvokerFang(world, spawn.x, spawn.y, spawn.z, (entity.m_146908_() - 90.0F) * (float) (Math.PI / 180.0), delay, entity, this.getDamage(spellLevel, entity));
                world.m_7967_(fang);
            }
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private int getGroundLevel(Level level, Vec3 start, int maxSteps) {
        if (!level.getBlockState(BlockPos.containing(start)).m_60795_()) {
            for (int i = 0; i < maxSteps; i++) {
                start = start.add(0.0, 1.0, 0.0);
                if (level.getBlockState(BlockPos.containing(start)).m_60795_()) {
                    break;
                }
            }
        }
        Vec3 lower = level.m_45547_(new ClipContext(start, start.add(0.0, (double) (maxSteps * -2), 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null)).m_82450_();
        return (int) lower.y;
    }

    @Override
    public boolean shouldAIStopCasting(int spellLevel, Mob mob, LivingEntity target) {
        float f = (float) this.getCount(spellLevel, mob) * 1.2F;
        return mob.m_20280_(target) > (double) (f * f);
    }

    private int getCount(int spellLevel, LivingEntity entity) {
        return 7 + spellLevel;
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity);
    }
}