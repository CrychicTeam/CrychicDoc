package dev.xkmc.l2backpack.content.remote.worldchest;

import dev.xkmc.l2backpack.init.advancement.BackpackTriggers;
import dev.xkmc.l2library.util.Proxy;
import java.util.UUID;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

public class WorldChestInvWrapper extends InvWrapper {

    private final UUID id;

    public WorldChestInvWrapper(Container inv, UUID id) {
        super(inv);
        this.id = id;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStack ans = super.insertItem(slot, stack, simulate);
        if (stack.getCount() != ans.getCount() && !simulate) {
            Proxy.getServer().map(e -> e.getPlayerList().getPlayer(this.id)).ifPresent(BackpackTriggers.REMOTE::trigger);
        }
        return ans;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack ans = super.extractItem(slot, amount, simulate);
        if (!ans.isEmpty() && !simulate) {
            Proxy.getServer().map(e -> e.getPlayerList().getPlayer(this.id)).ifPresent(BackpackTriggers.REMOTE::trigger);
        }
        return ans;
    }
}