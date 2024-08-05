package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import noppes.npcs.containers.ContainerManageRecipes;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.data.RecipeCarpentry;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;

public class SPacketRecipeGet extends PacketServerBasic {

    private int recipe;

    public SPacketRecipeGet(int recipe) {
        this.recipe = recipe;
    }

    public static void encode(SPacketRecipeGet msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.recipe);
    }

    public static SPacketRecipeGet decode(FriendlyByteBuf buf) {
        return new SPacketRecipeGet(buf.readInt());
    }

    @Override
    protected void handle() {
        RecipeCarpentry r = RecipeController.instance.getRecipe(this.recipe);
        setRecipeGui(this.player, r);
    }

    public static void setRecipeGui(ServerPlayer player, RecipeCarpentry recipe) {
        if (recipe != null) {
            if (player.f_36096_ instanceof ContainerManageRecipes container) {
                container.setRecipe(recipe, player.m_9236_().registryAccess());
                Packets.send(player, new PacketGuiData(recipe.writeNBT()));
            }
        }
    }
}