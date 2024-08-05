package journeymap.client.service.webmap.kotlin.routes;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.NativeImage;
import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.spark.kotlin.RouteHandler;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.nio.channels.Channels;
import java.util.UUID;
import journeymap.client.texture.IgnSkin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;

@Metadata(mv = { 1, 8, 0 }, k = 2, xi = 48, d1 = { "\u0000\u000e\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000¨\u0006\u0004" }, d2 = { "skinGet", "", "handler", "Linfo/journeymap/shaded/kotlin/spark/kotlin/RouteHandler;", "journeymap" })
public final class SkinKt {

    @NotNull
    public static final Object skinGet(@NotNull RouteHandler handler) {
        UUID uuid;
        String var7;
        label19: {
            Intrinsics.checkNotNullParameter(handler, "handler");
            uuid = UUID.fromString(handler.params("uuid"));
            ClientPacketListener var10000 = Minecraft.getInstance().getConnection();
            if (var10000 != null) {
                PlayerInfo var5 = var10000.getPlayerInfo(uuid);
                if (var5 != null) {
                    GameProfile var6 = var5.getProfile();
                    if (var6 != null) {
                        var7 = var6.getName();
                        break label19;
                    }
                }
            }
            var7 = null;
        }
        String username = var7;
        NativeImage img = null;
        NativeImage var8;
        if (username == null) {
            var8 = new NativeImage(24, 24, false);
        } else {
            var8 = IgnSkin.getFaceImage(uuid, username);
            Intrinsics.checkNotNullExpressionValue(var8, "{\n        IgnSkin.getFac…age(uuid, username)\n    }");
        }
        img = var8;
        handler.getResponse().raw().setContentType("image/png");
        img.writeToChannel(Channels.newChannel(handler.getResponse().raw().getOutputStream()));
        handler.getResponse().raw().getOutputStream().flush();
        return handler.getResponse();
    }
}