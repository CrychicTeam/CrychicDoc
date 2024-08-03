package dev.shadowsoffire.attributeslib.api.client;

import java.util.Set;
import java.util.UUID;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;

public class GatherSkippedAttributeTooltipsEvent extends PlayerEvent {

    protected final ItemStack stack;

    protected final Set<UUID> skips;

    protected final TooltipFlag flag;

    public GatherSkippedAttributeTooltipsEvent(ItemStack stack, @Nullable Player player, Set<UUID> skips, TooltipFlag flag) {
        super(player);
        this.stack = stack;
        this.skips = skips;
        this.flag = flag;
    }

    public TooltipFlag getFlags() {
        return this.flag;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public void skipUUID(UUID id) {
        this.skips.add(id);
    }

    @Nullable
    @Override
    public Player getEntity() {
        return super.getEntity();
    }
}