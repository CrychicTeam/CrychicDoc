package com.mna.capabilities.worlddata;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IRitualTeleportLocation;
import com.mna.api.capabilities.IWorldMagic;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class WorldMagicProvider implements ICapabilitySerializable<Tag> {

    private boolean isOverworld = false;

    public static final Capability<IWorldMagic> MAGIC = CapabilityManager.get(new CapabilityToken<IWorldMagic>() {
    });

    private final LazyOptional<IWorldMagic> holder = LazyOptional.of(() -> new WorldMagic(this.isOverworld));

    private final String KEY_TELEPORT_LOCATIONS = "teleport_locations";

    private final String KEY_TELEPORT_LOCATION_COUNT = "teleport_locations_size";

    private final String KEY_TELEPORT_LOCATION_PREFIX = "teleport_location_";

    private final String KEY_RUNES = "runes";

    private final String KEY_WARDING_CANDLE_LOCATIONS = "warding_candle_locations";

    private final String KEY_WELLSPRINGS = "wellsprings";

    public WorldMagicProvider(boolean isOverworld) {
        this.isOverworld = isOverworld;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return MAGIC.orEmpty(cap, this.holder);
    }

    @Override
    public Tag serializeNBT() {
        IWorldMagic instance = this.holder.orElse(null);
        CompoundTag nbt_main = new CompoundTag();
        IRitualTeleportLocation[] locations = instance.getAllTeleportLocations();
        CompoundTag teleport_locations = new CompoundTag();
        teleport_locations.putInt("teleport_locations_size", locations.length);
        for (int i = 0; i < locations.length; i++) {
            CompoundTag teleport_location_nbt = new CompoundTag();
            ((RitualTeleportLocation) locations[i]).writeToNBT(teleport_location_nbt);
            teleport_locations.put("teleport_location_" + i, teleport_location_nbt);
        }
        nbt_main.put("teleport_locations", teleport_locations);
        ListTag runes = new ListTag();
        UUID[] playerIDs = instance.getKnownPlayersWithSigils();
        for (UUID playerID : playerIDs) {
            if (playerID != null) {
                UUID[] known_runes = instance.getKnownSigilsForPlayer(playerID);
                if (known_runes.length != 0) {
                    CompoundTag playerRuneData = new CompoundTag();
                    playerRuneData.putString("playerID", playerID.toString());
                    ListTag runeIDs = new ListTag();
                    for (UUID runeID : known_runes) {
                        runeIDs.add(StringTag.valueOf(runeID.toString()));
                    }
                    playerRuneData.put("runeIDs", runeIDs);
                    runes.add(playerRuneData);
                }
            }
        }
        nbt_main.put("runes", runes);
        nbt_main.put("warding_candle_locations", new LongArrayTag(instance.getAllWardingCandleLocations()));
        CompoundTag wellspringData = new CompoundTag();
        instance.getWellspringRegistry().writeToNBT(wellspringData);
        nbt_main.put("wellsprings", wellspringData);
        return nbt_main;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        IWorldMagic instance = this.holder.orElse(null);
        if (!(nbt instanceof CompoundTag nbt_main)) {
            ManaAndArtifice.LOGGER.error("World Magic NBT passed back not an instance of CompoundNBT - all save data was NOT loaded!");
        } else {
            if (nbt_main.contains("teleport_locations")) {
                CompoundTag teleport_locations = nbt_main.getCompound("teleport_locations");
                if (teleport_locations.contains("teleport_locations_size")) {
                    int count = teleport_locations.getInt("teleport_locations_size");
                    for (int i = 0; i < count; i++) {
                        if (teleport_locations.contains("teleport_location_" + i)) {
                            CompoundTag teleport_location_nbt = teleport_locations.getCompound("teleport_location_" + i);
                            RitualTeleportLocation loc = RitualTeleportLocation.fromNBT(teleport_location_nbt);
                            if (loc != null) {
                                instance.setRitualTeleportLocation(loc);
                            } else {
                                ManaAndArtifice.LOGGER.error("Teleport Location NBT failed to load at index " + i + " - this teleport location was NOT loaded!");
                            }
                        } else {
                            ManaAndArtifice.LOGGER.error("Teleport Locations NBT does not contain teleport location data key " + i + " - this teleport location was NOT loaded!");
                        }
                    }
                } else {
                    ManaAndArtifice.LOGGER.error("Teleport Locations NBT does not contain teleport locations count key - teleport location save data was NOT loaded!");
                }
            } else {
                ManaAndArtifice.LOGGER.error("World Magic NBT does not contain teleport locations key - teleport location save data was NOT loaded!");
            }
            if (nbt_main.contains("runes")) {
                ListTag runes = nbt_main.getList("runes", 10);
                runes.forEach(inbt_player_runes -> {
                    CompoundTag player_rune_nbt = (CompoundTag) inbt_player_runes;
                    if (player_rune_nbt.contains("playerID") && player_rune_nbt.contains("runeIDs")) {
                        try {
                            UUID playerID = UUID.fromString(player_rune_nbt.getString("playerID"));
                            ListTag player_known_runes = player_rune_nbt.getList("runeIDs", 8);
                            for (int ix = 0; ix < player_known_runes.size(); ix++) {
                                UUID runeID = null;
                                try {
                                    runeID = UUID.fromString(player_known_runes.getString(ix));
                                } catch (Exception var8x) {
                                    ManaAndArtifice.LOGGER.error("Failed to load player rune data");
                                    ManaAndArtifice.LOGGER.catching(var8x);
                                }
                                if (runeID != null) {
                                    instance.addUnreferencedSigil(playerID, runeID);
                                }
                            }
                        } catch (Exception var9x) {
                            ManaAndArtifice.LOGGER.error("Failed to load player rune data");
                            ManaAndArtifice.LOGGER.catching(var9x);
                        }
                    }
                });
            } else {
                ManaAndArtifice.LOGGER.error("World Magic NBT does not contain known runes key - known rune save data was NOT loaded!  This is expected if you're upgrading your world from an M&A version less than 1.0.8.2.");
            }
            if (nbt_main.contains("warding_candle_locations", 12)) {
                instance.setWardingCandleLocations((List<Long>) Arrays.stream(nbt_main.getLongArray("warding_candle_locations")).boxed().collect(Collectors.toList()));
            }
            if (nbt_main.contains("wellsprings")) {
                instance.getWellspringRegistry().readFromNBT(nbt_main.getCompound("wellsprings"));
            }
        }
    }
}