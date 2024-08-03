package lio.playeranimatorapi.API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import java.util.ArrayList;
import java.util.List;
import lio.liosmultiloaderutils.utils.NetworkManager;
import lio.playeranimatorapi.ModInit;
import lio.playeranimatorapi.data.PlayerAnimationData;
import lio.playeranimatorapi.data.PlayerParts;
import lio.playeranimatorapi.modifier.CommonModifier;
import lio.playeranimatorapi.utils.CommonPlayerLookup;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerAnimAPI {

    public static final ResourceLocation playerAnimPacket = new ResourceLocation("liosplayeranimatorapi", "player_anim");

    public static final ResourceLocation playerAnimStopPacket = new ResourceLocation("liosplayeranimatorapi", "player_anim_stop");

    public static final ResourceLocation MIRROR_ON_ALT_HAND = new ResourceLocation("playeranimatorapi", "mirroronalthand");

    public static final List<CommonModifier> gameplayModifiers = new ArrayList<CommonModifier>() {

        {
            this.add(new CommonModifier(PlayerAnimAPI.MIRROR_ON_ALT_HAND, null));
        }
    };

    private static final Logger logger = LogManager.getLogger(ModInit.class);

    public static Gson gson = new GsonBuilder().setLenient().serializeNulls().create();

    public static void playPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID) {
        playPlayerAnim(level, player, animationID, PlayerParts.allEnabled, null, -1, -1, false, false);
    }

    public static void playPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers, boolean important) {
        playPlayerAnim(level, player, animationID, parts, modifiers, -1, -1, false, important);
    }

    public static void playPlayerAnim(ServerLevel level, Player player, PlayerAnimationData data) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUtf(gson.toJson((JsonElement) PlayerAnimationData.CODEC.encodeStart(JsonOps.INSTANCE, data).getOrThrow(true, logger::warn)));
        NetworkManager.sendToPlayers(CommonPlayerLookup.tracking(level, player.m_146902_()), playerAnimPacket, buf);
    }

    public static void playPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID, PlayerParts parts, List<CommonModifier> modifiers, int fadeLength, int easeID, boolean firstPersonEnabled, boolean important) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        PlayerAnimationData data = new PlayerAnimationData(player.m_20148_(), animationID, parts, modifiers, fadeLength, easeID, firstPersonEnabled, important);
        buf.writeUtf(gson.toJson((JsonElement) PlayerAnimationData.CODEC.encodeStart(JsonOps.INSTANCE, data).getOrThrow(true, logger::warn)));
        NetworkManager.sendToPlayers(CommonPlayerLookup.tracking(level, player.m_146902_()), playerAnimPacket, buf);
    }

    public static void stopPlayerAnim(ServerLevel level, Player player, ResourceLocation animationID) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUUID(player.m_20148_());
        buf.writeResourceLocation(animationID);
        NetworkManager.sendToPlayers(CommonPlayerLookup.tracking(level, player.m_146902_()), playerAnimStopPacket, buf);
    }
}