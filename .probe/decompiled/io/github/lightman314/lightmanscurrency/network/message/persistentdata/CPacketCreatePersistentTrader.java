package io.github.lightman314.lightmanscurrency.network.message.persistentdata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketCreatePersistentTrader extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketCreatePersistentTrader> HANDLER = new CPacketCreatePersistentTrader.H();

    private static final String GENERATE_ID_FORMAT = "trader_";

    final long traderID;

    final String id;

    final String owner;

    public CPacketCreatePersistentTrader(long traderID, String id, String owner) {
        this.traderID = traderID;
        this.id = id;
        this.owner = owner.isBlank() ? "Minecraft" : owner;
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeLong(this.traderID);
        buffer.writeUtf(this.id);
        buffer.writeUtf(this.owner);
    }

    private static class H extends CustomPacket.Handler<CPacketCreatePersistentTrader> {

        @Nonnull
        public CPacketCreatePersistentTrader decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketCreatePersistentTrader(buffer.readLong(), buffer.readUtf(), buffer.readUtf());
        }

        protected void handle(@Nonnull CPacketCreatePersistentTrader message, @Nullable ServerPlayer sender) {
            if (LCAdminMode.isAdminPlayer(sender)) {
                TraderData trader = TraderSaveData.GetTrader(false, message.traderID);
                if (trader != null && trader.canMakePersistent()) {
                    boolean generateID = message.id.isBlank();
                    if (!generateID) {
                        try {
                            JsonObject traderJson = trader.saveToJson(message.id, message.owner);
                            JsonArray persistentTraders = TraderSaveData.getPersistentTraderJson("Traders");
                            for (int i = 0; i < persistentTraders.size(); i++) {
                                JsonObject traderData = persistentTraders.get(i).getAsJsonObject();
                                if (traderData.has("ID") && traderData.get("ID").getAsString().equals(message.id) || traderData.has("id") && traderData.get("id").getAsString().equals(message.id)) {
                                    persistentTraders.set(i, traderJson);
                                    TraderSaveData.setPersistentTraderSection("Traders", persistentTraders);
                                    sender.sendSystemMessage(LCText.MESSAGE_PERSISTENT_TRADER_OVERWRITE.get(message.id));
                                    return;
                                }
                            }
                            persistentTraders.add(traderJson);
                            TraderSaveData.setPersistentTraderSection("Traders", persistentTraders);
                            sender.sendSystemMessage(LCText.MESSAGE_PERSISTENT_TRADER_ADD.get(message.id));
                        } catch (Throwable var10) {
                            LightmansCurrency.LogError("Error occurred while creating a persistent trader!", var10);
                        }
                    } else {
                        try {
                            List<String> knownIDs = new ArrayList();
                            JsonArray persistentTraders = TraderSaveData.getPersistentTraderJson("Traders");
                            for (int ix = 0; ix < persistentTraders.size(); ix++) {
                                JsonObject traderData = persistentTraders.get(ix).getAsJsonObject();
                                if (traderData.has("id")) {
                                    knownIDs.add(traderData.get("id").getAsString());
                                }
                                if (traderData.has("ID")) {
                                    knownIDs.add(traderData.get("ID").getAsString());
                                }
                            }
                            for (int ix = 1; ix < Integer.MAX_VALUE; ix++) {
                                String genID = "trader_" + ix;
                                if (knownIDs.stream().noneMatch(id -> id.equals(genID))) {
                                    persistentTraders.add(trader.saveToJson(genID, message.owner));
                                    TraderSaveData.setPersistentTraderSection("Traders", persistentTraders);
                                    sender.sendSystemMessage(LCText.MESSAGE_PERSISTENT_TRADER_ADD.get(genID));
                                    return;
                                }
                            }
                            LightmansCurrency.LogError("Could not generate ID, as all trader_# ID's are somehow spoken for.");
                        } catch (Throwable var9) {
                            LightmansCurrency.LogError("Error occurred while creating a persistent trader!", var9);
                        }
                    }
                }
            } else if (sender != null) {
                sender.sendSystemMessage(LCText.MESSAGE_PERSISTENT_TRADER_FAIL.get());
            }
        }
    }
}