package noppes.npcs.packets.server;

import java.util.HashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.data.RecipeCarpentry;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketRecipesGet extends PacketServerBasic {

    private int width;

    public SPacketRecipesGet(int width) {
        this.width = width;
    }

    public static void encode(SPacketRecipesGet msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.width);
    }

    public static SPacketRecipesGet decode(FriendlyByteBuf buf) {
        return new SPacketRecipesGet(buf.readInt());
    }

    @Override
    protected void handle() {
        sendRecipeData(this.player, this.width);
    }

    public static void sendRecipeData(ServerPlayer player, int size) {
        HashMap<String, Integer> map = new HashMap();
        if (size == 3) {
            for (RecipeCarpentry var4 : RecipeController.instance.globalRecipes.values()) {
                ;
            }
        } else {
            for (RecipeCarpentry var6 : RecipeController.instance.anvilRecipes.values()) {
                ;
            }
        }
        NoppesUtilServer.sendScrollData(player, map);
    }
}