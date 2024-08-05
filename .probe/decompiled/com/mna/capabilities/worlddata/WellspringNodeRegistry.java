package com.mna.capabilities.worlddata;

import com.mna.ManaAndArtifice;
import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IWellspringNodeRegistry;
import com.mna.api.capabilities.WellspringNode;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.tools.MATags;
import com.mna.network.ClientMessageDispatcher;
import com.mna.network.ServerMessageDispatcher;
import com.mna.tools.math.MathUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class WellspringNodeRegistry implements IWellspringNodeRegistry {

    public static final TagKey<Biome> IS_VOID = TagKey.create(Registries.BIOME, new ResourceLocation("forge", "is_void"));

    public static final float STRENGTH_CONVERSION_FACTOR = 0.01F;

    public static final float POWER_CAP = 1000.0F;

    private HashMap<BlockPos, WellspringNode> _registry;

    private static final HashMap<UUID, HashMap<Affinity, Float>> player_strengths_cached = new HashMap();

    private static final HashMap<UUID, HashMap<Affinity, Float>> player_amounts_cached = new HashMap();

    boolean hasDoneInitialSync = false;

    boolean isOverworld = false;

    public WellspringNodeRegistry() {
        this._registry = new HashMap();
    }

    @Override
    public void serverTick(MinecraftServer server) {
        for (UUID playerUUID : player_amounts_cached.keySet()) {
            ServerPlayer player = server.getPlayerList().getPlayer(playerUUID);
            if (player != null) {
                HashMap<Affinity, Float> strengths = this.getNodeNetworkStrengthFor(player);
                HashMap<Affinity, Float> amounts = this.getNodeNetworkAmountFor(player);
                strengths.entrySet().forEach(entry -> {
                    float newAmt = MathUtils.clamp((Float) amounts.get(entry.getKey()) + (Float) entry.getValue() * 0.01F, 0.0F, 1000.0F);
                    amounts.put((Affinity) entry.getKey(), newAmt);
                });
            }
        }
    }

    @Override
    public HashMap<BlockPos, WellspringNode> getNearbyNodes(BlockPos origin, int minDist, int maxDist) {
        int minDistSq = minDist * minDist;
        int maxDistSq = maxDist * maxDist;
        BlockPos testPos = new BlockPos(origin.m_123341_(), 0, origin.m_123343_());
        return (HashMap<BlockPos, WellspringNode>) this._registry.entrySet().stream().filter(e -> {
            double dist = ((BlockPos) e.getKey()).m_123331_(testPos);
            return dist >= (double) minDistSq && dist <= (double) maxDistSq;
        }).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (prev, next) -> next, HashMap::new));
    }

    @Override
    public Optional<WellspringNode> getNodeAt(BlockPos blockPos) {
        BlockPos checkPos = new BlockPos(blockPos.m_123341_(), 0, blockPos.m_123343_());
        return Optional.ofNullable((WellspringNode) this._registry.getOrDefault(checkPos, null));
    }

    @Override
    public HashMap<Affinity, Float> getNodeNetworkStrengthFor(Player player) {
        if (player == null) {
            return new HashMap();
        } else {
            if (player.m_9236_().isClientSide() && !this.hasDoneInitialSync) {
                this.hasDoneInitialSync = true;
                ClientMessageDispatcher.sendRequestWellspringNetworkSyncMessage(false);
            }
            return this._getNodeNetworkStrengthFor(player);
        }
    }

    @Override
    public HashMap<Affinity, Float> getNodeNetworkAmountFor(UUID player, Level world) {
        if (world.isClientSide && !this.hasDoneInitialSync) {
            this.hasDoneInitialSync = true;
            ClientMessageDispatcher.sendRequestWellspringNetworkSyncMessage(false);
        }
        return this._getAmountsForPlayer(player);
    }

    @Override
    public HashMap<Affinity, Float> getNodeNetworkAmountFor(Player player) {
        return player != null && player.getGameProfile() != null && player.getGameProfile().getId() != null ? this.getNodeNetworkAmountFor(player.getGameProfile().getId(), player.m_9236_()) : new HashMap();
    }

    @Override
    public float consumePower(UUID player, Level world, Affinity type, float amount) {
        if (player == null) {
            return 0.0F;
        } else {
            HashMap<Affinity, Float> nodeNetworkPower = this.getNodeNetworkAmountFor(player, world);
            if (!nodeNetworkPower.containsKey(type)) {
                return 0.0F;
            } else {
                float existing = (Float) nodeNetworkPower.get(type);
                float amountConsumed = Math.min(existing, amount);
                nodeNetworkPower.put(type, existing - amountConsumed);
                return amountConsumed;
            }
        }
    }

    @Override
    public float insertPower(UUID player, Level world, Affinity type, float amount) {
        if (player == null) {
            return 0.0F;
        } else {
            HashMap<Affinity, Float> nodeNetworkPower = this.getNodeNetworkAmountFor(player, world);
            if (!nodeNetworkPower.containsKey(type)) {
                return 0.0F;
            } else {
                float existing = (Float) nodeNetworkPower.get(type);
                float amountInserted = Math.min(1000.0F - existing, amount);
                nodeNetworkPower.put(type, existing + amountInserted);
                return amountInserted;
            }
        }
    }

    private HashMap<Affinity, Float> _getNodeNetworkStrengthFor(Player player) {
        if (player == null) {
            return new HashMap();
        } else {
            UUID playerProfileUUID = player.m_20148_();
            return this._getNodeNetworkStrengthFor(playerProfileUUID);
        }
    }

    private HashMap<Affinity, Float> _getAmountsForPlayer(Player player) {
        if (player == null) {
            return new HashMap();
        } else {
            UUID playerProfileUUID = player.m_20148_();
            return this._getAmountsForPlayer(playerProfileUUID);
        }
    }

    private HashMap<Affinity, Float> _getNodeNetworkStrengthFor(UUID playerProfileUUID) {
        if (playerProfileUUID == null) {
            return new HashMap();
        } else {
            if (!player_strengths_cached.containsKey(playerProfileUUID) || player_strengths_cached.get(playerProfileUUID) == null) {
                HashMap<Affinity, Float> affs = new HashMap();
                for (Affinity aff : Affinity.values()) {
                    if (!affs.containsKey(aff.getShiftAffinity())) {
                        affs.put(aff.getShiftAffinity(), 0.0F);
                    }
                }
                player_strengths_cached.put(playerProfileUUID, affs);
            }
            return (HashMap<Affinity, Float>) player_strengths_cached.get(playerProfileUUID);
        }
    }

    private HashMap<Affinity, Float> _getAmountsForPlayer(UUID playerProfileUUID) {
        if (playerProfileUUID == null) {
            return new HashMap();
        } else {
            if (!player_amounts_cached.containsKey(playerProfileUUID) || player_amounts_cached.get(playerProfileUUID) == null) {
                HashMap<Affinity, Float> affs = new HashMap();
                for (Affinity aff : Affinity.values()) {
                    if (!affs.containsKey(aff.getShiftAffinity())) {
                        affs.put(aff.getShiftAffinity(), 0.0F);
                    }
                }
                player_amounts_cached.put(playerProfileUUID, affs);
            }
            return (HashMap<Affinity, Float>) player_amounts_cached.get(playerProfileUUID);
        }
    }

    private void syncNetworkStrengthFor(ServerLevel world, UUID playerId) {
        if (playerId != null) {
            ServerPlayer player = (ServerPlayer) world.m_46003_(playerId);
            if (player != null) {
                ServerMessageDispatcher.sendWellspringPowerNetworkSyncMessage(world, player, true);
                ServerMessageDispatcher.sendWellspringSyncMessage(world, player, 128);
            }
        }
    }

    @Override
    public boolean addRandomNode(Level world, BlockPos origin) {
        if (!world.getWorldBorder().isWithinBounds(origin)) {
            return false;
        } else if (GeneralConfigValues.GenericWellsprings) {
            return this.addNode(world, origin, () -> new WellspringNode(Affinity.UNKNOWN, (float) (5.0 + Math.random() * 20.0)), false);
        } else {
            Holder<Biome> biome = world.m_204166_(origin);
            return this.addNode(world, origin, () -> new WellspringNode(this.randomAffinityFromBiome(world, biome, origin), (float) (5.0 + Math.random() * 20.0)), false);
        }
    }

    @Override
    public boolean deleteNodeAt(BlockPos position) {
        if (this._registry.containsKey(position)) {
            this._registry.remove(position);
            return true;
        } else {
            return false;
        }
    }

    private Affinity randomAffinityFromBiome(Level world, Holder<Biome> holder, BlockPos pos) {
        if (!(Math.random() < 0.15) && holder.isBound()) {
            ArrayList<Affinity> validAffinities = new ArrayList();
            if (holder.is(MATags.Biomes.Wellspring.FIRE)) {
                validAffinities.add(Affinity.FIRE);
            }
            if (holder.is(MATags.Biomes.Wellspring.WATER)) {
                validAffinities.add(Affinity.WATER);
            }
            if (holder.is(MATags.Biomes.Wellspring.WIND)) {
                validAffinities.add(Affinity.WIND);
            }
            if (holder.is(MATags.Biomes.Wellspring.EARTH)) {
                validAffinities.add(Affinity.EARTH);
            }
            if (holder.is(MATags.Biomes.Wellspring.ARCANE)) {
                validAffinities.add(Affinity.ARCANE);
            }
            if (holder.is(MATags.Biomes.Wellspring.ENDER) && !holder.is(IS_VOID)) {
                validAffinities.add(Affinity.ENDER);
            }
            return validAffinities.size() == 0 ? Affinity.UNKNOWN : this.randomAffinityIn((Affinity[]) validAffinities.toArray(new Affinity[0]));
        } else {
            return this.randomAffinityIn(Affinity.values());
        }
    }

    private Affinity randomAffinityIn(Affinity... values) {
        return values[(int) (Math.random() * (double) values.length)];
    }

    @Override
    public boolean addNode(Level world, BlockPos origin, Supplier<WellspringNode> nodeData, boolean force) {
        if (!world.getWorldBorder().isWithinBounds(origin)) {
            return false;
        } else {
            int distance = GeneralConfigValues.WellspringDistance;
            BlockPos testPos = new BlockPos(origin.m_123341_(), 0, origin.m_123343_());
            if (!force && this.getNearbyNodes(testPos, 0, distance).size() > 0) {
                return false;
            } else {
                this._registry.put(testPos, (WellspringNode) nodeData.get());
                return true;
            }
        }
    }

    @Override
    public boolean claimNode(ServerLevel world, UUID playerId, ResourceLocation playerFaction, BlockPos origin, Affinity affinity) {
        if (playerId != null && playerFaction != null) {
            MutableBoolean claimed = new MutableBoolean(false);
            this.getNodeAt(origin).ifPresent(node -> {
                if (!node.isClaimed() && playerId != null) {
                    if (playerFaction != null) {
                        node.setClaimedBy(playerId, playerFaction, origin.m_123342_());
                        if (node.getAffinity() == Affinity.UNKNOWN) {
                            node.setAffinity(affinity);
                        }
                        HashMap<Affinity, Float> playerStr = this._getNodeNetworkStrengthFor(playerId);
                        playerStr.put(node.getAffinity().getShiftAffinity(), (Float) playerStr.getOrDefault(node.getAffinity().getShiftAffinity(), 0.0F) + node.getStrength());
                        this.syncNetworkStrengthFor(world, playerId);
                        claimed.setTrue();
                    }
                } else if ((node.isClaimedBy(playerId) || node.isClaimedBy(playerFaction)) && node.getYLevel() == origin.m_123342_()) {
                    claimed.setTrue();
                }
            });
            if (!claimed.getValue()) {
                ServerPlayer player = (ServerPlayer) world.m_46003_(playerId);
                if (player != null) {
                    ServerMessageDispatcher.sendWellspringSyncMessage(world, player, 128);
                }
            }
            return claimed.getValue();
        } else {
            return false;
        }
    }

    @Override
    public boolean unclaimNode(ServerLevel world, BlockPos origin) {
        MutableBoolean unclaimed = new MutableBoolean(false);
        this.getNodeAt(origin).ifPresent(node -> {
            if (node.isClaimed() && (!node.hasForcedYLevel() || node.getYLevel() == origin.m_123342_())) {
                UUID playerId = node.getClaimedBy();
                if (playerId != null) {
                    HashMap<Affinity, Float> playerStr = this._getNodeNetworkStrengthFor(playerId);
                    playerStr.put(node.getAffinity().getShiftAffinity(), Math.max((Float) playerStr.getOrDefault(node.getAffinity().getShiftAffinity(), 0.0F) - node.getStrength(), 0.0F));
                }
                node.clearClaimedBy();
                if (playerId != null) {
                    this.syncNetworkStrengthFor(world, playerId);
                }
                unclaimed.setTrue();
            }
        });
        return unclaimed.getValue();
    }

    @Override
    public void serializeNetworkStrength(Player player, CompoundTag nbt, boolean fullSync) {
        if (player != null) {
            nbt.putBoolean("fullSync", fullSync);
            if (fullSync) {
                CompoundTag playerStrength = new CompoundTag();
                CompoundTag playerAmounts = new CompoundTag();
                ListTag factionStrength = new ListTag();
                this.serializePlayerNetworkStrength(player, playerStrength);
                this.serializePlayerNetworkAmount(player, playerAmounts);
                nbt.put("player", playerStrength);
                nbt.put("player_amounts", playerAmounts);
                nbt.put("factions", factionStrength);
            } else {
                CompoundTag playerStrength = new CompoundTag();
                CompoundTag playerAmounts = new CompoundTag();
                this.serializePlayerNetworkStrength(player, playerStrength);
                this.serializePlayerNetworkStrength(player, playerAmounts);
                nbt.put("player", playerStrength);
                nbt.put("player_amounts", playerStrength);
            }
        }
    }

    @Override
    public void deserializeNetworkStrength(Player player, CompoundTag nbt) {
        if (player != null && player.m_20148_() != null) {
            if (nbt.getBoolean("fullSync")) {
                CompoundTag playerStrength = nbt.getCompound("player");
                CompoundTag playerAmounts = nbt.getCompound("player_amounts");
                this.deserializePlayerNetworkStrength(player, playerStrength);
                this.deserializePlayerNetworkAmount(player, playerAmounts);
            } else {
                this.deserializePlayerNetworkStrength(player, nbt.getCompound("player"));
                this.deserializePlayerNetworkAmount(player, nbt.getCompound("player_amounts"));
            }
        } else {
            ManaAndArtifice.LOGGER.error("Received NULL player or player with no UUID to deserializeNetworkStrength");
        }
    }

    private void serializePlayerNetworkAmount(Player player, CompoundTag nbt) {
        if (player != null) {
            HashMap<Affinity, Float> strengths = this._getAmountsForPlayer(player);
            strengths.entrySet().forEach(e -> nbt.putFloat(((Affinity) e.getKey()).getShiftAffinity().name(), (Float) e.getValue()));
        }
    }

    private void deserializePlayerNetworkAmount(Player player, CompoundTag nbt) {
        if (player != null) {
            HashMap<Affinity, Float> strengths = new HashMap();
            for (Affinity aff : Affinity.values()) {
                strengths.put(aff.getShiftAffinity(), nbt.contains(aff.getShiftAffinity().name()) ? nbt.getFloat(aff.getShiftAffinity().name()) : 0.0F);
            }
            player_amounts_cached.put(player.m_20148_(), strengths);
        }
    }

    private void serializePlayerNetworkStrength(Player player, CompoundTag nbt) {
        if (player != null) {
            HashMap<Affinity, Float> strengths = this._getNodeNetworkStrengthFor(player);
            strengths.entrySet().forEach(e -> nbt.putFloat(((Affinity) e.getKey()).getShiftAffinity().name(), (Float) e.getValue()));
        }
    }

    private void deserializePlayerNetworkStrength(Player player, CompoundTag nbt) {
        if (player != null) {
            HashMap<Affinity, Float> strengths = new HashMap();
            for (Affinity aff : Affinity.values()) {
                strengths.put(aff.getShiftAffinity(), nbt.contains(aff.getShiftAffinity().name()) ? nbt.getFloat(aff.getShiftAffinity().name()) : 0.0F);
            }
            player_strengths_cached.put(player.m_20148_(), strengths);
        }
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        ListTag wellspringData = new ListTag();
        this._registry.entrySet().stream().forEach(e -> {
            CompoundTag node = new CompoundTag();
            node.putLong("pos", ((BlockPos) e.getKey()).asLong());
            ((WellspringNode) e.getValue()).writeToNBT(node);
            wellspringData.add(node);
        });
        nbt.put("wellspringData", wellspringData);
        CompoundTag wellspringGlobal = new CompoundTag();
        ListTag player_strengths = new ListTag();
        ListTag faction_strengths = new ListTag();
        ListTag player_amounts = new ListTag();
        if (this.isOverworld()) {
            player_strengths_cached.entrySet().forEach(e -> {
                CompoundTag playerStrength = new CompoundTag();
                playerStrength.putUUID("uuid", (UUID) e.getKey());
                ListTag strengths = new ListTag();
                ((HashMap) e.getValue()).entrySet().forEach(str -> {
                    CompoundTag str_entry = new CompoundTag();
                    str_entry.putInt("affinity", ((Affinity) str.getKey()).getShiftAffinity().ordinal());
                    str_entry.putFloat("strength", (Float) str.getValue());
                    strengths.add(str_entry);
                });
                playerStrength.put("strengths", strengths);
                player_strengths.add(playerStrength);
            });
            player_amounts_cached.entrySet().forEach(e -> {
                CompoundTag playerAmount = new CompoundTag();
                playerAmount.putString("uuid", ((UUID) e.getKey()).toString());
                ListTag strengths = new ListTag();
                ((HashMap) e.getValue()).entrySet().forEach(str -> {
                    CompoundTag str_entry = new CompoundTag();
                    str_entry.putInt("affinity", ((Affinity) str.getKey()).getShiftAffinity().ordinal());
                    str_entry.putFloat("strength", (Float) str.getValue());
                    strengths.add(str_entry);
                });
                playerAmount.put("strengths", strengths);
                player_amounts.add(playerAmount);
            });
            wellspringGlobal.put("players", player_strengths);
            wellspringGlobal.put("factions", faction_strengths);
            wellspringGlobal.put("player_amounts", player_amounts);
            nbt.put("wellspringGlobal", wellspringGlobal);
        }
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        if (nbt.contains("wellspringData")) {
            ListTag wellspringData = nbt.getList("wellspringData", 10);
            this._registry.clear();
            wellspringData.forEach(inbt -> {
                CompoundTag node = (CompoundTag) inbt;
                BlockPos pos = BlockPos.of(node.getLong("pos"));
                WellspringNode value = WellspringNode.fromNBT(node);
                this._registry.put(pos, value);
            });
        }
        if (this.isOverworld) {
            if (nbt.contains("wellspringGlobal")) {
                CompoundTag wellspringGlobal = nbt.getCompound("wellspringGlobal");
                ListTag player_strengths = wellspringGlobal.getList("players", 10);
                ListTag player_amounts = wellspringGlobal.getList("player_amounts", 10);
                player_strengths.forEach(inbt -> {
                    CompoundTag playerStrength = (CompoundTag) inbt;
                    UUID uuid = playerStrength.getUUID("uuid");
                    ListTag strengths = playerStrength.getList("strengths", 10);
                    strengths.forEach(strNbt -> {
                        CompoundTag str_entry = (CompoundTag) strNbt;
                        this._getNodeNetworkStrengthFor(uuid).put(Affinity.values()[str_entry.getInt("affinity")], str_entry.getFloat("strength"));
                    });
                });
                player_amounts.forEach(inbt -> {
                    CompoundTag playerAmount = (CompoundTag) inbt;
                    UUID uuid = null;
                    try {
                        uuid = playerAmount.getUUID("uuid");
                    } catch (IllegalArgumentException var7) {
                        try {
                            uuid = UUID.fromString(playerAmount.getString("uuid"));
                        } catch (Exception var6) {
                        }
                    }
                    if (uuid != null) {
                        ListTag strengths = playerAmount.getList("strengths", 10);
                        UUID toUse = uuid;
                        strengths.forEach(strNbt -> {
                            CompoundTag str_entry = (CompoundTag) strNbt;
                            this._getAmountsForPlayer(toUse).put(Affinity.values()[str_entry.getInt("affinity")], str_entry.getFloat("strength"));
                        });
                    }
                });
            } else if (!nbt.contains("sync")) {
                player_strengths_cached.clear();
                this._registry.entrySet().forEach(e -> {
                    if (((WellspringNode) e.getValue()).isClaimed()) {
                        UUID uuid = ((WellspringNode) e.getValue()).getClaimedBy();
                        HashMap<Affinity, Float> nnStrUUID = this._getNodeNetworkStrengthFor(uuid);
                        nnStrUUID.put(((WellspringNode) e.getValue()).getAffinity().getShiftAffinity(), (Float) nnStrUUID.get(((WellspringNode) e.getValue()).getAffinity().getShiftAffinity()) + ((WellspringNode) e.getValue()).getStrength());
                    }
                });
            }
        }
    }

    @Override
    public boolean writeToNBT(CompoundTag nbt, BlockPos center, int radius) {
        ListTag wellspringData = new ListTag();
        HashMap<BlockPos, WellspringNode> nearbyNodes = this.getNearbyNodes(center, 0, radius);
        if (nearbyNodes.size() == 0) {
            return false;
        } else {
            nearbyNodes.entrySet().stream().forEach(e -> {
                CompoundTag node = new CompoundTag();
                node.putLong("pos", ((BlockPos) e.getKey()).asLong());
                ((WellspringNode) e.getValue()).writeToNBT(node);
                wellspringData.add(node);
            });
            nbt.put("wellspringData", wellspringData);
            nbt.putBoolean("sync", true);
            return true;
        }
    }

    @Override
    public void setWellspringPower(ServerPlayer player, Affinity aff, float level) {
        if (!player_strengths_cached.containsKey(player.m_20148_())) {
            player_strengths_cached.put(player.m_20148_(), new HashMap());
        }
        ((HashMap) player_strengths_cached.get(player.m_20148_())).put(aff, level);
    }

    @Override
    public boolean isOverworld() {
        return this.isOverworld;
    }

    @Override
    public void setOverworld() {
        this.isOverworld = true;
    }
}