package io.github.lightman314.lightmanscurrency.api.misc.player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PlayerReference {

    public static final PlayerReference NULL = new PlayerReference(new UUID(0L, 0L), "NULL");

    public final UUID id;

    private boolean forceName = false;

    private final String name;

    public String getName(boolean isClient) {
        if (!isClient && !this.forceName) {
            String n = getPlayerName(this.id);
            return n != null && !n.isBlank() ? n : this.name;
        } else {
            return this.name;
        }
    }

    public MutableComponent getNameComponent(boolean isClient) {
        return Component.literal(this.getName(isClient));
    }

    private PlayerReference(UUID playerID, String name) {
        this.id = playerID;
        this.name = name;
    }

    public PlayerReference copyWithName(String name) {
        PlayerReference copy = new PlayerReference(this.id, name);
        copy.forceName = true;
        return copy;
    }

    public boolean is(PlayerReference player) {
        return player == null ? false : this.is(player.id);
    }

    public boolean isExact(PlayerReference player) {
        return player == null ? false : this.is(player.id) && !this.forceName && !player.forceName;
    }

    public boolean is(GameProfile profile) {
        return this.is(profile.getId());
    }

    public boolean is(UUID entityID) {
        return entityID == null ? false : entityID.equals(this.id);
    }

    public boolean is(Entity entity) {
        return entity == null ? false : entity.getUUID().equals(this.id);
    }

    public boolean isOnline() {
        return this.getPlayer() != null;
    }

    public Player getPlayer() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server != null ? server.getPlayerList().getPlayer(this.id) : null;
    }

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        compound.putUUID("id", this.id);
        compound.putString("name", this.getName(false));
        if (this.forceName) {
            compound.putBoolean("forcedname", this.forceName);
        }
        return compound;
    }

    public JsonObject saveAsJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", this.id.toString());
        json.addProperty("name", this.getName(false));
        return json;
    }

    public static PlayerReference load(CompoundTag compound) {
        try {
            UUID id = compound.getUUID("id");
            String name = compound.getString("name");
            PlayerReference pr = of(id, name);
            if (compound.contains("forcedname")) {
                pr.forceName = compound.getBoolean("forcedname");
            }
            return pr;
        } catch (Exception var4) {
            LightmansCurrency.LogError("Error loading PlayerReference from tag.", var4);
            return null;
        }
    }

    public static PlayerReference load(JsonElement json) {
        try {
            if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
                return of(false, json.getAsString());
            } else {
                JsonObject j = json.getAsJsonObject();
                UUID id = UUID.fromString(j.get("id").getAsString());
                String name = j.get("name").getAsString();
                return of(id, name);
            }
        } catch (Exception var4) {
            LightmansCurrency.LogError("Error loading PlayerReference from JsonObject", var4);
            return null;
        }
    }

    public static void saveList(CompoundTag compound, List<PlayerReference> playerList, String tag) {
        ListTag list = new ListTag();
        for (PlayerReference playerReference : playerList) {
            if (playerReference != null) {
                list.add(playerReference.save());
            }
        }
        compound.put(tag, list);
    }

    public static JsonArray saveJsonList(List<PlayerReference> playerList) {
        JsonArray array = new JsonArray();
        for (PlayerReference playerReference : playerList) {
            if (playerReference != null) {
                array.add(playerReference.saveAsJson());
            }
        }
        return array;
    }

    public static List<PlayerReference> loadList(CompoundTag compound, String tag) {
        List<PlayerReference> playerList = new ArrayList();
        if (compound.contains(tag, 9)) {
            ListTag list = compound.getList(tag, 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag thisCompound = list.getCompound(i);
                PlayerReference player = load(thisCompound);
                if (player != null) {
                    playerList.add(player);
                }
            }
        }
        return playerList;
    }

    public static PlayerReference of(@Nonnull UUID playerID, String name) {
        if (playerID == null) {
            throw new RuntimeException("Cannot make a PlayerReference from a null player ID!");
        } else {
            return new PlayerReference(playerID, name);
        }
    }

    public static PlayerReference of(GameProfile playerProfile) {
        return playerProfile == null ? null : of(playerProfile.getId(), playerProfile.getName());
    }

    public static PlayerReference of(Entity entity) {
        return entity instanceof Player ? of((Player) entity) : null;
    }

    public static PlayerReference of(Player player) {
        return player == null ? null : of(player.getGameProfile());
    }

    public static PlayerReference of(boolean isClient, String playerName) {
        if (playerName.isBlank()) {
            return null;
        } else if (isClient) {
            LightmansCurrency.LogWarning("Attempted to assemble a player reference from name alone on a client. Should not be doing that.");
            return null;
        } else {
            UUID playerID = getPlayerID(playerName);
            return playerID != null ? of(playerID, playerName) : null;
        }
    }

    public static boolean isInList(List<PlayerReference> list, Entity entry) {
        return entry != null ? isInList(list, entry.getUUID()) : false;
    }

    public static boolean isInList(List<PlayerReference> list, PlayerReference entry) {
        return entry != null ? isInList(list, entry.id) : false;
    }

    public static boolean isInList(List<PlayerReference> list, UUID id) {
        for (PlayerReference player : list) {
            if (player != null && player.is(id)) {
                return true;
            }
        }
        return false;
    }

    public static boolean removeFromList(List<PlayerReference> list, PlayerReference entry) {
        return entry != null ? removeFromList(list, entry.id) : false;
    }

    public static boolean removeFromList(List<PlayerReference> list, UUID id) {
        for (int i = 0; i < list.size(); i++) {
            PlayerReference pr = (PlayerReference) list.get(i);
            if (pr != null && pr.is(id)) {
                list.remove(i);
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public static String getPlayerName(UUID playerID) {
        try {
            String name = UsernameCache.getLastKnownUsername(playerID);
            if (name != null) {
                return name;
            }
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                GameProfile profile = (GameProfile) server.getProfileCache().get(playerID).orElse(null);
                if (profile != null) {
                    return profile.getName();
                }
            }
        } catch (Throwable var4) {
            LightmansCurrency.LogError("Error getting player name.", var4);
        }
        return null;
    }

    public static UUID getPlayerID(String playerName) {
        try {
            for (Entry<UUID, String> entry : UsernameCache.getMap().entrySet()) {
                if (((String) entry.getValue()).equalsIgnoreCase(playerName)) {
                    return (UUID) entry.getKey();
                }
            }
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                GameProfile profile = (GameProfile) server.getProfileCache().get(playerName).orElse(null);
                if (profile != null) {
                    return profile.getId();
                }
            }
        } catch (Throwable var3) {
            LightmansCurrency.LogError("Error getting player ID from name.", var3);
        }
        return null;
    }

    static {
        NULL.forceName = true;
    }
}