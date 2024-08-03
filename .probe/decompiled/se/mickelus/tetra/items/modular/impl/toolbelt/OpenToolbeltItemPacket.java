package se.mickelus.tetra.items.modular.impl.toolbelt;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class OpenToolbeltItemPacket extends AbstractPacket {

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
    }

    @Override
    public void handle(Player player) {
        ItemStack itemStack = ToolbeltHelper.findToolbelt(player);
        if (!itemStack.isEmpty()) {
            NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) itemStack.getItem());
        }
    }
}