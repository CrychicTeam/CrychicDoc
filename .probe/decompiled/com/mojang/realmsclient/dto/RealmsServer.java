package com.mojang.realmsclient.dto;

import com.google.common.base.Joiner;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.util.JsonUtils;
import com.mojang.realmsclient.util.RealmsUtil;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.slf4j.Logger;

public class RealmsServer extends ValueObject {

    private static final Logger LOGGER = LogUtils.getLogger();

    public long id;

    public String remoteSubscriptionId;

    public String name;

    public String motd;

    public RealmsServer.State state;

    public String owner;

    public String ownerUUID;

    public List<PlayerInfo> players;

    public Map<Integer, RealmsWorldOptions> slots;

    public boolean expired;

    public boolean expiredTrial;

    public int daysLeft;

    public RealmsServer.WorldType worldType;

    public int activeSlot;

    public String minigameName;

    public int minigameId;

    public String minigameImage;

    public RealmsServerPing serverPing = new RealmsServerPing();

    public String getDescription() {
        return this.motd;
    }

    public String getName() {
        return this.name;
    }

    public String getMinigameName() {
        return this.minigameName;
    }

    public void setName(String string0) {
        this.name = string0;
    }

    public void setDescription(String string0) {
        this.motd = string0;
    }

    public void updateServerPing(RealmsServerPlayerList realmsServerPlayerList0) {
        List<String> $$1 = Lists.newArrayList();
        int $$2 = 0;
        for (String $$3 : realmsServerPlayerList0.players) {
            if (!$$3.equals(Minecraft.getInstance().getUser().getUuid())) {
                String $$4 = "";
                try {
                    $$4 = RealmsUtil.uuidToName($$3);
                } catch (Exception var8) {
                    LOGGER.error("Could not get name for {}", $$3, var8);
                    continue;
                }
                $$1.add($$4);
                $$2++;
            }
        }
        this.serverPing.nrOfPlayers = String.valueOf($$2);
        this.serverPing.playerList = Joiner.on('\n').join($$1);
    }

    public static RealmsServer parse(JsonObject jsonObject0) {
        RealmsServer $$1 = new RealmsServer();
        try {
            $$1.id = JsonUtils.getLongOr("id", jsonObject0, -1L);
            $$1.remoteSubscriptionId = JsonUtils.getStringOr("remoteSubscriptionId", jsonObject0, null);
            $$1.name = JsonUtils.getStringOr("name", jsonObject0, null);
            $$1.motd = JsonUtils.getStringOr("motd", jsonObject0, null);
            $$1.state = getState(JsonUtils.getStringOr("state", jsonObject0, RealmsServer.State.CLOSED.name()));
            $$1.owner = JsonUtils.getStringOr("owner", jsonObject0, null);
            if (jsonObject0.get("players") != null && jsonObject0.get("players").isJsonArray()) {
                $$1.players = parseInvited(jsonObject0.get("players").getAsJsonArray());
                sortInvited($$1);
            } else {
                $$1.players = Lists.newArrayList();
            }
            $$1.daysLeft = JsonUtils.getIntOr("daysLeft", jsonObject0, 0);
            $$1.expired = JsonUtils.getBooleanOr("expired", jsonObject0, false);
            $$1.expiredTrial = JsonUtils.getBooleanOr("expiredTrial", jsonObject0, false);
            $$1.worldType = getWorldType(JsonUtils.getStringOr("worldType", jsonObject0, RealmsServer.WorldType.NORMAL.name()));
            $$1.ownerUUID = JsonUtils.getStringOr("ownerUUID", jsonObject0, "");
            if (jsonObject0.get("slots") != null && jsonObject0.get("slots").isJsonArray()) {
                $$1.slots = parseSlots(jsonObject0.get("slots").getAsJsonArray());
            } else {
                $$1.slots = createEmptySlots();
            }
            $$1.minigameName = JsonUtils.getStringOr("minigameName", jsonObject0, null);
            $$1.activeSlot = JsonUtils.getIntOr("activeSlot", jsonObject0, -1);
            $$1.minigameId = JsonUtils.getIntOr("minigameId", jsonObject0, -1);
            $$1.minigameImage = JsonUtils.getStringOr("minigameImage", jsonObject0, null);
        } catch (Exception var3) {
            LOGGER.error("Could not parse McoServer: {}", var3.getMessage());
        }
        return $$1;
    }

    private static void sortInvited(RealmsServer realmsServer0) {
        realmsServer0.players.sort((p_87502_, p_87503_) -> ComparisonChain.start().compareFalseFirst(p_87503_.getAccepted(), p_87502_.getAccepted()).compare(p_87502_.getName().toLowerCase(Locale.ROOT), p_87503_.getName().toLowerCase(Locale.ROOT)).result());
    }

    private static List<PlayerInfo> parseInvited(JsonArray jsonArray0) {
        List<PlayerInfo> $$1 = Lists.newArrayList();
        for (JsonElement $$2 : jsonArray0) {
            try {
                JsonObject $$3 = $$2.getAsJsonObject();
                PlayerInfo $$4 = new PlayerInfo();
                $$4.setName(JsonUtils.getStringOr("name", $$3, null));
                $$4.setUuid(JsonUtils.getStringOr("uuid", $$3, null));
                $$4.setOperator(JsonUtils.getBooleanOr("operator", $$3, false));
                $$4.setAccepted(JsonUtils.getBooleanOr("accepted", $$3, false));
                $$4.setOnline(JsonUtils.getBooleanOr("online", $$3, false));
                $$1.add($$4);
            } catch (Exception var6) {
            }
        }
        return $$1;
    }

    private static Map<Integer, RealmsWorldOptions> parseSlots(JsonArray jsonArray0) {
        Map<Integer, RealmsWorldOptions> $$1 = Maps.newHashMap();
        for (JsonElement $$2 : jsonArray0) {
            try {
                JsonObject $$3 = $$2.getAsJsonObject();
                JsonParser $$4 = new JsonParser();
                JsonElement $$5 = $$4.parse($$3.get("options").getAsString());
                RealmsWorldOptions $$6;
                if ($$5 == null) {
                    $$6 = RealmsWorldOptions.createDefaults();
                } else {
                    $$6 = RealmsWorldOptions.parse($$5.getAsJsonObject());
                }
                int $$8 = JsonUtils.getIntOr("slotId", $$3, -1);
                $$1.put($$8, $$6);
            } catch (Exception var9) {
            }
        }
        for (int $$9 = 1; $$9 <= 3; $$9++) {
            if (!$$1.containsKey($$9)) {
                $$1.put($$9, RealmsWorldOptions.createEmptyDefaults());
            }
        }
        return $$1;
    }

    private static Map<Integer, RealmsWorldOptions> createEmptySlots() {
        Map<Integer, RealmsWorldOptions> $$0 = Maps.newHashMap();
        $$0.put(1, RealmsWorldOptions.createEmptyDefaults());
        $$0.put(2, RealmsWorldOptions.createEmptyDefaults());
        $$0.put(3, RealmsWorldOptions.createEmptyDefaults());
        return $$0;
    }

    public static RealmsServer parse(String string0) {
        try {
            return parse(new JsonParser().parse(string0).getAsJsonObject());
        } catch (Exception var2) {
            LOGGER.error("Could not parse McoServer: {}", var2.getMessage());
            return new RealmsServer();
        }
    }

    private static RealmsServer.State getState(String string0) {
        try {
            return RealmsServer.State.valueOf(string0);
        } catch (Exception var2) {
            return RealmsServer.State.CLOSED;
        }
    }

    private static RealmsServer.WorldType getWorldType(String string0) {
        try {
            return RealmsServer.WorldType.valueOf(string0);
        } catch (Exception var2) {
            return RealmsServer.WorldType.NORMAL;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.id, this.name, this.motd, this.state, this.owner, this.expired });
    }

    public boolean equals(Object object0) {
        if (object0 == null) {
            return false;
        } else if (object0 == this) {
            return true;
        } else if (object0.getClass() != this.getClass()) {
            return false;
        } else {
            RealmsServer $$1 = (RealmsServer) object0;
            return new EqualsBuilder().append(this.id, $$1.id).append(this.name, $$1.name).append(this.motd, $$1.motd).append(this.state, $$1.state).append(this.owner, $$1.owner).append(this.expired, $$1.expired).append(this.worldType, this.worldType).isEquals();
        }
    }

    public RealmsServer clone() {
        RealmsServer $$0 = new RealmsServer();
        $$0.id = this.id;
        $$0.remoteSubscriptionId = this.remoteSubscriptionId;
        $$0.name = this.name;
        $$0.motd = this.motd;
        $$0.state = this.state;
        $$0.owner = this.owner;
        $$0.players = this.players;
        $$0.slots = this.cloneSlots(this.slots);
        $$0.expired = this.expired;
        $$0.expiredTrial = this.expiredTrial;
        $$0.daysLeft = this.daysLeft;
        $$0.serverPing = new RealmsServerPing();
        $$0.serverPing.nrOfPlayers = this.serverPing.nrOfPlayers;
        $$0.serverPing.playerList = this.serverPing.playerList;
        $$0.worldType = this.worldType;
        $$0.ownerUUID = this.ownerUUID;
        $$0.minigameName = this.minigameName;
        $$0.activeSlot = this.activeSlot;
        $$0.minigameId = this.minigameId;
        $$0.minigameImage = this.minigameImage;
        return $$0;
    }

    public Map<Integer, RealmsWorldOptions> cloneSlots(Map<Integer, RealmsWorldOptions> mapIntegerRealmsWorldOptions0) {
        Map<Integer, RealmsWorldOptions> $$1 = Maps.newHashMap();
        for (Entry<Integer, RealmsWorldOptions> $$2 : mapIntegerRealmsWorldOptions0.entrySet()) {
            $$1.put((Integer) $$2.getKey(), ((RealmsWorldOptions) $$2.getValue()).clone());
        }
        return $$1;
    }

    public String getWorldName(int int0) {
        return this.name + " (" + ((RealmsWorldOptions) this.slots.get(int0)).getSlotName(int0) + ")";
    }

    public ServerData toServerData(String string0) {
        return new ServerData(this.name, string0, false);
    }

    public static class McoServerComparator implements Comparator<RealmsServer> {

        private final String refOwner;

        public McoServerComparator(String string0) {
            this.refOwner = string0;
        }

        public int compare(RealmsServer realmsServer0, RealmsServer realmsServer1) {
            return ComparisonChain.start().compareTrueFirst(realmsServer0.state == RealmsServer.State.UNINITIALIZED, realmsServer1.state == RealmsServer.State.UNINITIALIZED).compareTrueFirst(realmsServer0.expiredTrial, realmsServer1.expiredTrial).compareTrueFirst(realmsServer0.owner.equals(this.refOwner), realmsServer1.owner.equals(this.refOwner)).compareFalseFirst(realmsServer0.expired, realmsServer1.expired).compareTrueFirst(realmsServer0.state == RealmsServer.State.OPEN, realmsServer1.state == RealmsServer.State.OPEN).compare(realmsServer0.id, realmsServer1.id).result();
        }
    }

    public static enum State {

        CLOSED, OPEN, UNINITIALIZED
    }

    public static enum WorldType {

        NORMAL, MINIGAME, ADVENTUREMAP, EXPERIENCE, INSPIRATION
    }
}