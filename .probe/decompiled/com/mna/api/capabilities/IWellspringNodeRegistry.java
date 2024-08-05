package com.mna.api.capabilities;

import com.mna.api.affinity.Affinity;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface IWellspringNodeRegistry {

    HashMap<BlockPos, WellspringNode> getNearbyNodes(BlockPos var1, int var2, int var3);

    boolean addRandomNode(Level var1, BlockPos var2);

    boolean addNode(Level var1, BlockPos var2, Supplier<WellspringNode> var3, boolean var4);

    HashMap<Affinity, Float> getNodeNetworkStrengthFor(Player var1);

    HashMap<Affinity, Float> getNodeNetworkAmountFor(Player var1);

    HashMap<Affinity, Float> getNodeNetworkAmountFor(UUID var1, Level var2);

    float insertPower(UUID var1, Level var2, Affinity var3, float var4);

    float consumePower(UUID var1, Level var2, Affinity var3, float var4);

    boolean claimNode(ServerLevel var1, UUID var2, ResourceLocation var3, BlockPos var4, Affinity var5);

    boolean unclaimNode(ServerLevel var1, BlockPos var2);

    Optional<WellspringNode> getNodeAt(BlockPos var1);

    void serializeNetworkStrength(Player var1, CompoundTag var2, boolean var3);

    void deserializeNetworkStrength(Player var1, CompoundTag var2);

    void writeToNBT(CompoundTag var1);

    boolean writeToNBT(CompoundTag var1, BlockPos var2, int var3);

    void readFromNBT(CompoundTag var1);

    boolean isOverworld();

    void setOverworld();

    void serverTick(MinecraftServer var1);

    void setWellspringPower(ServerPlayer var1, Affinity var2, float var3);

    boolean deleteNodeAt(BlockPos var1);
}