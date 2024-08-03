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
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.entity.mobs.goals.HomeOwner;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

@AutoSpellConfig
public class RecallSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "recall");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.ENDER_RESOURCE).setMaxLevel(1).setCooldownSeconds(300.0).build();

    public RecallSpell() {
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.RECALL_PREPARE.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.ENDERMAN_TELEPORT);
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        this.playSound(this.getCastFinishSound(), entity);
        if (entity instanceof ServerPlayer serverPlayer) {
            ServerLevel respawnLevel = ((ServerLevel) world).getServer().getLevel(serverPlayer.getRespawnDimension());
            respawnLevel = respawnLevel == null ? world.getServer().overworld() : respawnLevel;
            Optional<Vec3> spawnLocation = findSpawnPosition(respawnLevel, serverPlayer);
            if (spawnLocation.isPresent()) {
                Vec3 vec3 = (Vec3) spawnLocation.get();
                if (serverPlayer.f_19853_.dimension() != respawnLevel.m_46472_()) {
                    serverPlayer.changeDimension(respawnLevel, new RecallSpell.PortalTeleporter(vec3));
                } else {
                    serverPlayer.teleportTo(vec3.x, vec3.y, vec3.z);
                }
            } else {
                respawnLevel = world.getServer().overworld();
                if (serverPlayer.f_19853_.dimension() != respawnLevel.m_46472_()) {
                    serverPlayer.changeDimension(respawnLevel, new RecallSpell.PortalTeleporter(Vec3.ZERO));
                }
                serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.NO_RESPAWN_BLOCK_AVAILABLE, 0.0F));
                BlockPos pos = respawnLevel.m_220360_();
                serverPlayer.teleportTo((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_());
            }
        } else if (entity instanceof HomeOwner homeOwner && homeOwner.getHome() != null) {
            BlockPos pos = homeOwner.getHome();
            entity.m_6021_((double) pos.m_123341_(), (double) pos.m_123342_() + 0.15, (double) pos.m_123343_());
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    public static void ambientParticles(LivingEntity entity, SyncedSpellData spellData) {
        float f = (float) entity.f_19797_ * 0.125F;
        Vec3 trail1 = new Vec3((double) Mth.cos(f), (double) Mth.sin(f * 2.0F), (double) Mth.sin(f)).normalize();
        Vec3 trail2 = new Vec3((double) Mth.sin(f), (double) Mth.cos(f * 2.0F), (double) Mth.cos(f)).normalize();
        Vec3 trail3 = trail1.multiply(trail2).normalize().scale((double) (1.0F + (Mth.sin(f) + Mth.cos(f)) * 0.5F));
        Vec3 pos = entity.m_20191_().getCenter();
        entity.f_19853_.addParticle(ParticleHelper.UNSTABLE_ENDER, pos.x + trail1.x, pos.y + trail1.y, pos.z + trail1.z, 0.0, 0.0, 0.0);
        entity.f_19853_.addParticle(ParticleHelper.UNSTABLE_ENDER, pos.x + trail2.x, pos.y + trail2.y, pos.z + trail2.z, 0.0, 0.0, 0.0);
        entity.f_19853_.addParticle(ParticleHelper.UNSTABLE_ENDER, pos.x + trail3.x, pos.y + trail3.y, pos.z + trail3.z, 0.0, 0.0, 0.0);
    }

    @Override
    public void playSound(Optional<SoundEvent> sound, Entity entity) {
        sound.ifPresent(soundEvent -> entity.playSound(soundEvent, 2.0F, 1.0F));
    }

    public static Optional<Vec3> findSpawnPosition(ServerLevel level, ServerPlayer player) {
        BlockPos spawnBlockpos = player.getRespawnPosition();
        if (spawnBlockpos == null) {
            return Optional.empty();
        } else {
            BlockState blockstate = level.m_8055_(spawnBlockpos);
            Block block = blockstate.m_60734_();
            if (block instanceof RespawnAnchorBlock && (Integer) blockstate.m_61143_(RespawnAnchorBlock.CHARGE) > 0 && RespawnAnchorBlock.canSetSpawn(level)) {
                return RespawnAnchorBlock.findStandUpPosition(EntityType.PLAYER, level, spawnBlockpos);
            } else {
                return block instanceof BedBlock && BedBlock.canSetSpawn(level) ? BedBlock.findStandUpPosition(EntityType.PLAYER, level, spawnBlockpos, player.m_6350_(), player.m_146908_()) : Optional.empty();
            }
        }
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return AnimationHolder.none();
    }

    @Override
    public boolean stopSoundOnCancel() {
        return true;
    }

    public static class PortalTeleporter implements ITeleporter {

        private final Vec3 destinationPosition;

        PortalTeleporter(Vec3 destinationPosition) {
            this.destinationPosition = destinationPosition;
        }

        @Override
        public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
            entity.fallDistance = 0.0F;
            return (Entity) repositionEntity.apply(false);
        }

        @Nullable
        @Override
        public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
            return new PortalInfo(this.destinationPosition, Vec3.ZERO, entity.getYRot(), entity.getXRot());
        }

        @Override
        public boolean isVanilla() {
            return false;
        }

        @Override
        public boolean playTeleportSound(ServerPlayer player, ServerLevel sourceWorld, ServerLevel destWorld) {
            return false;
        }
    }
}