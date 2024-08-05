package com.mna.capabilities.worlddata;

import com.mna.api.capabilities.IRitualTeleportLocation;
import com.mna.api.capabilities.IWellspringNodeRegistry;
import com.mna.api.capabilities.IWorldMagic;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.spells.base.ISpellSigil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class WorldMagic implements IWorldMagic {

    private NonNullList<IRitualTeleportLocation> teleport_locations = NonNullList.create();

    private WellspringNodeRegistry wellsprings;

    private ArrayList<BlockPos> warding_candle_locations;

    private Map<UUID, LinkedList<UUID>> playerRunes = new HashMap();

    private Map<UUID, Entity> knownRunes = new HashMap();

    public WorldMagic() {
        this(false);
    }

    public WorldMagic(boolean isOverworld) {
        this.warding_candle_locations = new ArrayList();
        this.wellsprings = new WellspringNodeRegistry();
        this.wellsprings.isOverworld = isOverworld;
    }

    @Nullable
    @Override
    public IRitualTeleportLocation getRitualTeleportBlockLocation(List<ResourceLocation> reagents, ResourceKey<Level> world) {
        for (int i = 0; i < this.teleport_locations.size(); i++) {
            if (this.teleport_locations.get(i).matches(reagents)) {
                IRitualTeleportLocation location = this.teleport_locations.get(i);
                location.tryCorrectWorldKey(world);
                return location;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public List<ResourceLocation> getRitualTeleportBlockReagents(BlockPos pos) {
        for (int i = 0; i < this.teleport_locations.size(); i++) {
            if (this.teleport_locations.get(i).matches(pos)) {
                return this.teleport_locations.get(i).getReagents();
            }
        }
        return null;
    }

    @Override
    public IRitualTeleportLocation[] getAllTeleportLocations() {
        IRitualTeleportLocation[] locations = new IRitualTeleportLocation[this.teleport_locations.size()];
        return (IRitualTeleportLocation[]) this.teleport_locations.toArray(locations);
    }

    @Override
    public boolean setRitualTeleportLocation(ServerLevel world, BlockPos pos, List<ResourceLocation> reagents, Direction direction) {
        return this.setRitualTeleportLocation(new RitualTeleportLocation(world.m_46472_(), pos, reagents, direction));
    }

    @Override
    public boolean setRitualTeleportLocation(IRitualTeleportLocation location) {
        if (this.getRitualTeleportBlockLocation(location.getReagents(), location.getWorldType()) != null) {
            return false;
        } else {
            this.teleport_locations.add(location);
            return true;
        }
    }

    @Override
    public void removeRitualTeleportLocation(BlockPos pos) {
        for (int i = 0; i < this.teleport_locations.size(); i++) {
            if (this.teleport_locations.get(i).matches(pos)) {
                this.teleport_locations.remove(i);
                break;
            }
        }
    }

    private boolean isBlockWarded(BlockPos pos) {
        return false;
    }

    @Override
    public boolean canPlayerUnlock(BlockPos pos, Player player) {
        return !this.isBlockWarded(pos) ? true : player.isCreative();
    }

    @Override
    public boolean wardBlock(BlockPos pos, Player caster, Collection<ResourceLocation> patterns) {
        return false;
    }

    @Override
    public void addSigil(Player player, ISpellSigil<?> rune, byte cast_with_bonus) {
        if (!this.isSigilKnown(rune.getID())) {
            if (!this.playerRunes.containsKey(player.m_20148_())) {
                this.playerRunes.put(player.m_20148_(), new LinkedList());
            }
            LinkedList<UUID> player_owned_runes = (LinkedList<UUID>) this.playerRunes.get(player.m_20148_());
            if (!player_owned_runes.contains(rune.getID())) {
                while (player_owned_runes.size() >= GeneralConfigValues.SigilLimit + rune.getCountBonus()) {
                    UUID first = (UUID) player_owned_runes.poll();
                    this.removeSigil(player.m_20148_(), first);
                }
                player_owned_runes.add(rune.getID());
            }
            this.knownRunes.put(rune.getID(), rune);
        }
    }

    @Override
    public void addUnreferencedSigil(UUID player, UUID rune) {
        if (!this.playerRunes.containsKey(player)) {
            this.playerRunes.put(player, new LinkedList());
        }
        LinkedList<UUID> player_owned_runes = (LinkedList<UUID>) this.playerRunes.get(player);
        player_owned_runes.add(rune);
    }

    @Override
    public void removeSigil(UUID player, UUID rune) {
        if (this.playerRunes.containsKey(player)) {
            ((LinkedList) this.playerRunes.get(player)).remove(rune);
        }
        if (this.knownRunes.containsKey(rune)) {
            Entity runeEntity = (Entity) this.knownRunes.get(rune);
            if (runeEntity != null && runeEntity.isAddedToWorld()) {
                runeEntity.remove(Entity.RemovalReason.DISCARDED);
            }
            this.knownRunes.remove(rune);
        }
    }

    @Override
    public boolean isSigilKnown(UUID rune) {
        return rune == null ? false : this.knownRunes.containsKey(rune);
    }

    @Override
    public boolean wasSigilRemoved(UUID player, UUID rune) {
        return !this.playerRunes.containsKey(player) || !((LinkedList) this.playerRunes.get(player)).contains(rune);
    }

    @Override
    public UUID[] getKnownPlayersWithSigils() {
        return (UUID[]) this.playerRunes.keySet().toArray(new UUID[0]);
    }

    @Override
    public UUID[] getKnownSigilsForPlayer(UUID player) {
        return !this.playerRunes.containsKey(player) ? new UUID[0] : (UUID[]) ((LinkedList) this.playerRunes.get(player)).toArray(new UUID[0]);
    }

    @Override
    public List<Long> getAllWardingCandleLocations() {
        return (List<Long>) this.warding_candle_locations.stream().map(bp -> bp.asLong()).collect(Collectors.toList());
    }

    @Override
    public void setWardingCandleLocations(List<Long> locations) {
        this.warding_candle_locations.clear();
        this.warding_candle_locations.addAll((Collection) locations.stream().map(l -> BlockPos.of(l)).collect(Collectors.toList()));
    }

    @Override
    public void addWardingCandleLocation(BlockPos location) {
        long posLong = location.asLong();
        if (!this.warding_candle_locations.stream().anyMatch(pos -> pos.asLong() == posLong)) {
            this.warding_candle_locations.add(location);
        }
    }

    @Override
    public void removeWardingCandleLocation(BlockPos location) {
        long posLong = location.asLong();
        this.warding_candle_locations.removeIf(pos -> pos.asLong() == posLong);
    }

    @Override
    public boolean isWithinWardingCandle(BlockPos pos) {
        return this.warding_candle_locations.stream().anyMatch(p -> Math.abs(p.m_123341_() - pos.m_123341_()) <= 32 && Math.abs(p.m_123342_() - pos.m_123342_()) <= 32 && Math.abs(p.m_123343_() - pos.m_123343_()) <= 32);
    }

    @Override
    public IWellspringNodeRegistry getWellspringRegistry() {
        return this.wellsprings;
    }
}