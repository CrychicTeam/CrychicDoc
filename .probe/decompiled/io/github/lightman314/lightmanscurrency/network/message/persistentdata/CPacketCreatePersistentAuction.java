package io.github.lightman314.lightmanscurrency.network.message.persistentdata;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.common.player.LCAdminMode;
import io.github.lightman314.lightmanscurrency.common.traders.TraderSaveData;
import io.github.lightman314.lightmanscurrency.common.traders.auction.tradedata.AuctionTradeData;
import io.github.lightman314.lightmanscurrency.network.packet.ClientToServerPacket;
import io.github.lightman314.lightmanscurrency.network.packet.CustomPacket;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class CPacketCreatePersistentAuction extends ClientToServerPacket {

    public static final CustomPacket.Handler<CPacketCreatePersistentAuction> HANDLER = new CPacketCreatePersistentAuction.H();

    private static final String GENERATE_ID_FORMAT = "auction_";

    final CompoundTag auctionData;

    final String id;

    public CPacketCreatePersistentAuction(CompoundTag auctionData, String id) {
        this.auctionData = auctionData;
        this.id = id;
    }

    private JsonObject getAuctionJson(String id) {
        AuctionTradeData auction = new AuctionTradeData(this.auctionData);
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        return auction.saveToJson(json);
    }

    @Override
    public void encode(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeNbt(this.auctionData);
        buffer.writeUtf(this.id);
    }

    private static class H extends CustomPacket.Handler<CPacketCreatePersistentAuction> {

        @Nonnull
        public CPacketCreatePersistentAuction decode(@Nonnull FriendlyByteBuf buffer) {
            return new CPacketCreatePersistentAuction(buffer.readAnySizeNbt(), buffer.readUtf());
        }

        protected void handle(@Nonnull CPacketCreatePersistentAuction message, @Nullable ServerPlayer sender) {
            if (LCAdminMode.isAdminPlayer(sender)) {
                boolean generateID = message.id.isBlank();
                if (!generateID) {
                    JsonObject auctionJson = message.getAuctionJson(message.id);
                    JsonArray persistentAuctions = TraderSaveData.getPersistentTraderJson("Auctions");
                    for (int i = 0; i < persistentAuctions.size(); i++) {
                        JsonObject auctionData = persistentAuctions.get(i).getAsJsonObject();
                        if (auctionData.has("id") && auctionData.get("id").getAsString().equals(message.id)) {
                            persistentAuctions.set(i, auctionJson);
                            TraderSaveData.setPersistentTraderSection("Auctions", persistentAuctions);
                            sender.sendSystemMessage(LCText.MESSAGE_PERSISTENT_AUCTION_OVERWRITE.get(message.id));
                            return;
                        }
                    }
                    persistentAuctions.add(auctionJson);
                    TraderSaveData.setPersistentTraderSection("Auctions", persistentAuctions);
                    sender.sendSystemMessage(LCText.MESSAGE_PERSISTENT_AUCTION_ADD.get(message.id));
                } else {
                    List<String> knownIDs = new ArrayList();
                    JsonArray persistentAuctions = TraderSaveData.getPersistentTraderJson("Auctions");
                    for (int ix = 0; ix < persistentAuctions.size(); ix++) {
                        JsonObject auctionData = persistentAuctions.get(ix).getAsJsonObject();
                        if (auctionData.has("id")) {
                            knownIDs.add(auctionData.get("id").getAsString());
                        }
                    }
                    for (int ixx = 1; ixx < Integer.MAX_VALUE; ixx++) {
                        String genID = "auction_" + ixx;
                        if (knownIDs.stream().noneMatch(id -> id.equals(genID))) {
                            persistentAuctions.add(message.getAuctionJson(genID));
                            TraderSaveData.setPersistentTraderSection("Auctions", persistentAuctions);
                            sender.sendSystemMessage(LCText.MESSAGE_PERSISTENT_AUCTION_ADD.get(genID));
                            return;
                        }
                    }
                    LightmansCurrency.LogError("Could not generate ID, as all auction_# ID's are somehow spoken for.");
                }
            } else if (sender != null) {
                sender.sendSystemMessage(LCText.MESSAGE_PERSISTENT_AUCTION_FAIL.get());
            }
        }
    }
}