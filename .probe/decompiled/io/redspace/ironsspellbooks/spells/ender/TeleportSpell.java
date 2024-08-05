package io.redspace.ironsspellbooks.spells.ender;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.network.spell.ClientboundTeleportParticles;
import io.redspace.ironsspellbooks.setup.Messages;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@AutoSpellConfig
public class TeleportSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "teleport");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.ENDER_RESOURCE).setMaxLevel(5).setCooldownSeconds(3.0).build();

    public TeleportSpell() {
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 10;
        this.baseManaCost = 20;
        this.manaCostPerLevel = 2;
        this.castTime = 0;
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.ENDERMAN_TELEPORT);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        TeleportSpell.TeleportData teleportData = (TeleportSpell.TeleportData) playerMagicData.getAdditionalCastData();
        Vec3 dest = null;
        if (teleportData != null) {
            Vec3 potentialTarget = teleportData.getTeleportTargetPosition();
            if (potentialTarget != null) {
                dest = potentialTarget;
            }
        }
        if (dest == null) {
            dest = findTeleportLocation(level, entity, this.getDistance(spellLevel, entity));
        }
        Messages.sendToPlayersTrackingEntity(new ClientboundTeleportParticles(entity.m_20182_(), dest), entity, true);
        if (entity.m_20159_()) {
            entity.stopRiding();
        }
        entity.m_6021_(dest.x, dest.y, dest.z);
        entity.m_183634_();
        playerMagicData.resetAdditionalCastData();
        entity.m_5496_((SoundEvent) this.getCastFinishSound().get(), 2.0F, 1.0F);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public static Vec3 findTeleportLocation(Level level, LivingEntity entity, float maxDistance) {
        BlockHitResult blockHitResult = Utils.getTargetBlock(level, entity, ClipContext.Fluid.NONE, (double) maxDistance);
        BlockPos pos = blockHitResult.getBlockPos();
        Vec3 bbOffset = entity.m_20156_().normalize().multiply((double) (entity.m_20205_() / 3.0F), 0.0, (double) (entity.m_20206_() / 3.0F));
        Vec3 bbImpact = blockHitResult.m_82450_().subtract(bbOffset);
        int ledgeY = (int) level.m_45547_(new ClipContext(Vec3.atBottomCenterOf(pos).add(0.0, 3.0, 0.0), Vec3.atBottomCenterOf(pos), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null)).m_82450_().y;
        boolean isAir = level.getBlockState(new BlockPos(new Vec3i(pos.m_123341_(), ledgeY, pos.m_123343_()))).m_60795_();
        boolean los = level.m_45547_(new ClipContext(bbImpact, bbImpact.add(0.0, (double) (ledgeY - pos.m_123342_()), 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS;
        if (isAir && los && Math.abs(ledgeY - pos.m_123342_()) <= 3) {
            Vec3 correctedPos = new Vec3((double) pos.m_123341_(), (double) ledgeY, (double) pos.m_123343_());
            return correctedPos.add(0.5, 0.076, 0.5);
        } else {
            return level.m_45547_(new ClipContext(bbImpact, bbImpact.add(0.0, (double) (-entity.m_20206_()), 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).m_82450_().add(0.0, 0.076, 0.0);
        }
    }

    public static void particleCloud(Level level, Vec3 pos) {
        if (level.isClientSide) {
            double width = 0.5;
            float height = 1.0F;
            for (int i = 0; i < 55; i++) {
                double x = pos.x + Utils.random.nextDouble() * width * 2.0 - width;
                double y = pos.y + (double) height + Utils.random.nextDouble() * (double) height * 1.2 * 2.0 - (double) height * 1.2;
                double z = pos.z + Utils.random.nextDouble() * width * 2.0 - width;
                double dx = Utils.random.nextDouble() * 0.1 * (double) (Utils.random.nextBoolean() ? 1 : -1);
                double dy = Utils.random.nextDouble() * 0.1 * (double) (Utils.random.nextBoolean() ? 1 : -1);
                double dz = Utils.random.nextDouble() * 0.1 * (double) (Utils.random.nextBoolean() ? 1 : -1);
                level.addParticle(ParticleTypes.PORTAL, true, x, y, z, dx, dy, dz);
            }
        }
    }

    private float getDistance(int spellLevel, LivingEntity sourceEntity) {
        return this.getSpellPower(spellLevel, sourceEntity);
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation((double) this.getDistance(spellLevel, caster), 1)));
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return AnimationHolder.none();
    }

    public static class TeleportData implements ICastData {

        private Vec3 teleportTargetPosition;

        public TeleportData(Vec3 teleportTargetPosition) {
            this.teleportTargetPosition = teleportTargetPosition;
        }

        public void setTeleportTargetPosition(Vec3 targetPosition) {
            this.teleportTargetPosition = targetPosition;
        }

        public Vec3 getTeleportTargetPosition() {
            return this.teleportTargetPosition;
        }

        @Override
        public void reset() {
        }
    }
}