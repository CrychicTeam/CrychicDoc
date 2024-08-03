package se.mickelus.tetra.module.improvement;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class SettlePacket extends AbstractPacket {

    ItemStack itemStack;

    String slot;

    public SettlePacket() {
    }

    public SettlePacket(ItemStack itemStack, String slot) {
        this.itemStack = itemStack;
        this.slot = slot;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeItem(this.itemStack);
        buffer.writeUtf(this.slot);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        this.itemStack = buffer.readItem();
        this.slot = buffer.readUtf();
    }

    @Override
    public void handle(Player player) {
        ProgressionHelper.showSettleToastClient(this.itemStack, this.slot);
    }
}