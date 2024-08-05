package com.mna.api.capabilities;

import com.mna.api.spells.base.ISpellSigil;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IWorldMagic {

    @Nullable
    IRitualTeleportLocation getRitualTeleportBlockLocation(List<ResourceLocation> var1, ResourceKey<Level> var2);

    @Nullable
    List<ResourceLocation> getRitualTeleportBlockReagents(BlockPos var1);

    boolean setRitualTeleportLocation(ServerLevel var1, BlockPos var2, List<ResourceLocation> var3, Direction var4);

    boolean setRitualTeleportLocation(IRitualTeleportLocation var1);

    void removeRitualTeleportLocation(BlockPos var1);

    IRitualTeleportLocation[] getAllTeleportLocations();

    boolean canPlayerUnlock(BlockPos var1, Player var2);

    boolean wardBlock(BlockPos var1, Player var2, Collection<ResourceLocation> var3);

    void addSigil(Player var1, ISpellSigil<?> var2, byte var3);

    void addUnreferencedSigil(UUID var1, UUID var2);

    void removeSigil(@Nullable UUID var1, UUID var2);

    boolean isSigilKnown(UUID var1);

    boolean wasSigilRemoved(UUID var1, UUID var2);

    UUID[] getKnownPlayersWithSigils();

    UUID[] getKnownSigilsForPlayer(UUID var1);

    List<Long> getAllWardingCandleLocations();

    void setWardingCandleLocations(List<Long> var1);

    void addWardingCandleLocation(BlockPos var1);

    void removeWardingCandleLocation(BlockPos var1);

    boolean isWithinWardingCandle(BlockPos var1);

    IWellspringNodeRegistry getWellspringRegistry();
}