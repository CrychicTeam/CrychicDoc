package io.redspace.ironsspellbooks.capabilities.magic;

import com.google.common.collect.Maps;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.network.ClientBoundRemoveRecast;
import io.redspace.ironsspellbooks.network.ClientBoundSyncRecast;
import io.redspace.ironsspellbooks.network.ClientboundSyncRecasts;
import io.redspace.ironsspellbooks.setup.Messages;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PlayerRecasts {

    private static final RecastInstance EMPTY = new RecastInstance(SpellRegistry.none().getSpellId(), 0, 0, 0, CastSource.NONE, null);

    private final Map<String, RecastInstance> recastLookup;

    private final ServerPlayer serverPlayer;

    public PlayerRecasts() {
        this.recastLookup = Maps.newHashMap();
        this.serverPlayer = null;
    }

    public PlayerRecasts(ServerPlayer serverPlayer) {
        this.recastLookup = Maps.newHashMap();
        this.serverPlayer = serverPlayer;
    }

    @OnlyIn(Dist.CLIENT)
    public PlayerRecasts(Map<String, RecastInstance> recastLookup) {
        this.recastLookup = recastLookup;
        this.serverPlayer = null;
    }

    public boolean addRecast(RecastInstance recastInstance, MagicData magicData) {
        RecastInstance existingRecastInstance = (RecastInstance) this.recastLookup.get(recastInstance.spellId);
        if (!this.isRecastActive(existingRecastInstance)) {
            magicData.getPlayerCooldowns().removeCooldown(recastInstance.spellId);
            this.recastLookup.put(recastInstance.spellId, recastInstance);
            this.syncToPlayer(recastInstance);
            return true;
        } else {
            return false;
        }
    }

    public boolean isRecastActive(RecastInstance recastInstance) {
        return recastInstance != null && recastInstance.remainingRecasts > 0 && recastInstance.remainingTicks > 0;
    }

    @OnlyIn(Dist.CLIENT)
    public void removeRecast(String spellId) {
        this.recastLookup.remove(spellId);
    }

    @OnlyIn(Dist.CLIENT)
    public void forceAddRecast(RecastInstance recastInstance) {
        this.recastLookup.put(recastInstance.spellId, recastInstance);
    }

    @OnlyIn(Dist.CLIENT)
    public void tickRecasts() {
        if (!this.recastLookup.isEmpty()) {
            this.recastLookup.values().stream().toList().forEach(x -> x.remainingTicks--);
        }
    }

    public boolean hasRecastsActive() {
        return !this.recastLookup.isEmpty();
    }

    public boolean hasRecastForSpell(AbstractSpell spell) {
        return this.isRecastActive((RecastInstance) this.recastLookup.get(spell.getSpellId()));
    }

    public boolean hasRecastForSpell(String spellId) {
        return this.isRecastActive((RecastInstance) this.recastLookup.get(spellId));
    }

    public int getRemainingRecastsForSpell(String spellId) {
        RecastInstance recastInstance = (RecastInstance) this.recastLookup.getOrDefault(spellId, EMPTY);
        return this.isRecastActive(recastInstance) ? recastInstance.remainingRecasts : 0;
    }

    public int getRemainingRecastsForSpell(AbstractSpell spell) {
        return this.getRemainingRecastsForSpell(spell.getSpellId());
    }

    public RecastInstance getRecastInstance(String spellId) {
        return (RecastInstance) this.recastLookup.get(spellId);
    }

    public List<RecastInstance> getAllRecasts() {
        return this.recastLookup.values().stream().toList();
    }

    public List<RecastInstance> getActiveRecasts() {
        return this.recastLookup.values().stream().filter(this::isRecastActive).toList();
    }

    public void decrementRecastCount(String spellId) {
        RecastInstance recastInstance = (RecastInstance) this.recastLookup.get(spellId);
        if (this.isRecastActive(recastInstance)) {
            recastInstance.remainingRecasts--;
            if (recastInstance.remainingRecasts > 0) {
                recastInstance.remainingTicks = recastInstance.ticksToLive;
                this.syncToPlayer(recastInstance);
            } else {
                this.removeRecast(recastInstance, RecastResult.USED_ALL_RECASTS);
            }
        } else if (recastInstance != null) {
            this.removeRecast(recastInstance, RecastResult.TIMEOUT);
        }
    }

    public void decrementRecastCount(AbstractSpell spell) {
        this.decrementRecastCount(spell.getSpellId());
    }

    public void syncAllToPlayer() {
        if (this.serverPlayer != null) {
            Messages.sendToPlayer(new ClientboundSyncRecasts(this.recastLookup), this.serverPlayer);
        }
    }

    public void syncToPlayer(RecastInstance recastInstance) {
        if (this.serverPlayer != null) {
            Messages.sendToPlayer(new ClientBoundSyncRecast(recastInstance), this.serverPlayer);
        }
    }

    public void syncRemoveToPlayer(String spellId) {
        if (this.serverPlayer != null) {
            Messages.sendToPlayer(new ClientBoundRemoveRecast(spellId), this.serverPlayer);
        }
    }

    private void triggerRecastComplete(RecastInstance recastInstance, RecastResult recastResult) {
        SpellRegistry.getSpell(recastInstance.getSpellId()).onRecastFinished(this.serverPlayer, recastInstance, recastResult, recastInstance.castData);
    }

    private void removeRecast(RecastInstance recastInstance, RecastResult recastResult, boolean doSync) {
        this.recastLookup.remove(recastInstance.spellId);
        if (doSync) {
            this.syncRemoveToPlayer(recastInstance.spellId);
        }
        this.triggerRecastComplete(recastInstance, recastResult);
    }

    public void removeRecast(RecastInstance recastInstance, RecastResult recastResult) {
        this.removeRecast(recastInstance, recastResult, true);
    }

    public void removeAll(RecastResult recastResult) {
        this.recastLookup.values().stream().toList().forEach(recastInstance -> this.removeRecast(recastInstance, recastResult, false));
        this.syncAllToPlayer();
    }

    public ListTag saveNBTData() {
        ListTag listTag = new ListTag();
        this.recastLookup.values().stream().filter(this::isRecastActive).forEach(recastInstance -> {
            if (recastInstance.remainingRecasts > 0 && recastInstance.remainingTicks > 0) {
                listTag.add(recastInstance.serializeNBT());
            }
        });
        return listTag;
    }

    public void loadNBTData(ListTag listTag) {
        if (listTag != null) {
            listTag.forEach(tag -> {
                RecastInstance recastInstance = new RecastInstance();
                recastInstance.deserializeNBT((CompoundTag) tag);
                if (recastInstance.remainingRecasts > 0 && recastInstance.remainingTicks > 0) {
                    this.recastLookup.put(recastInstance.spellId, recastInstance);
                } else {
                    IronsSpellbooks.LOGGER.warn("Trimming recast data: {}", recastInstance);
                }
            });
        }
    }

    public void tick(int actualTicks) {
        if (this.serverPlayer != null && this.serverPlayer.f_19853_.getGameTime() % (long) actualTicks == 0L) {
            this.recastLookup.values().stream().filter(r -> {
                r.remainingTicks -= actualTicks;
                return r.remainingTicks <= 0;
            }).toList().forEach(recastInstance -> this.removeRecast(recastInstance, RecastResult.TIMEOUT));
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        this.recastLookup.values().forEach(recastInstance -> {
            sb.append(recastInstance.toString());
            sb.append("\n");
        });
        return sb.toString();
    }
}