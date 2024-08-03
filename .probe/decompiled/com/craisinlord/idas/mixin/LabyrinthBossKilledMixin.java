package com.craisinlord.idas.mixin;

import com.craisinlord.idas.IDAS;
import com.craisinlord.idas.IDASTags;
import com.craisinlord.idas.state.IStateCacheProvider;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LivingEntity.class })
public abstract class LabyrinthBossKilledMixin extends Entity {

    public LabyrinthBossKilledMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = { "die" }, at = { @At("HEAD") })
    private void idas_labyrinthBossDie(DamageSource damageSource, CallbackInfo info) {
        if (this.m_9236_() instanceof ServerLevel serverLevel) {
            if (this.isBoss(this)) {
                StructureStart structureStart = serverLevel.structureManager().getStructureWithPieceAt(this.m_20183_(), IDASTags.APPLIES_MINING_FATIGUE);
                if (structureStart.isValid()) {
                    BlockPos structureStartPos = structureStart.getChunkPos().getWorldPosition();
                    ((IStateCacheProvider) this.m_9236_()).getStateCache().setCleared(structureStartPos, true);
                    List<ServerPlayer> players = serverLevel.players();
                    players.forEach(player -> {
                        if (this.m_9236_().isLoaded(player.m_20183_()) && serverLevel.structureManager().getStructureWithPieceAt(player.m_20183_(), IDASTags.APPLIES_MINING_FATIGUE).isValid()) {
                            player.connection.send(new ClientboundSoundPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.BEACON_DEACTIVATE), SoundSource.HOSTILE, this.m_20185_(), this.m_20186_(), this.m_20189_(), 1.0F, 1.0F, serverLevel.getSeed()));
                            player.m_21195_(MobEffects.DIG_SLOWDOWN);
                        }
                    });
                    IDAS.LOGGER.info("CLEARED Labyrinth AT {}", structureStartPos);
                }
            }
        }
    }

    private boolean isBoss(Object object) {
        if (!(object instanceof Mob mob)) {
            return false;
        } else if (Objects.equals(mob.m_20078_(), "iceandfire:gorgon")) {
            return true;
        } else {
            if (mob instanceof Husk) {
                for (ItemStack armorItem : mob.getArmorSlots()) {
                    if (armorItem.is(Items.PLAYER_HEAD) && armorItem.hasTag()) {
                        CompoundTag compoundTag = armorItem.getTag();
                        if (compoundTag.contains("SkullOwner", 10)) {
                            CompoundTag skullOwner = compoundTag.getCompound("SkullOwner");
                            if (skullOwner.contains("Properties", 10)) {
                                CompoundTag properties = skullOwner.getCompound("Properties");
                                if (properties.contains("textures", 9)) {
                                    ListTag textures = properties.getList("textures", 10);
                                    if (textures.size() == 1) {
                                        CompoundTag texture1 = (CompoundTag) textures.get(0);
                                        if (texture1.getString("Value").equals("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTAyNDkwMGIzZDU0ZDEzMDkxOTRkMmMyZjRjNjJhOTVmMTI3ZGY1OWI4MmM2NDE1NDdlNmU4ZmVlNTk3ZTZjIn19fQ==")) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
    }
}