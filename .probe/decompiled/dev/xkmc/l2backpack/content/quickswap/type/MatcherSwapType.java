package dev.xkmc.l2backpack.content.quickswap.type;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class MatcherSwapType extends QuickSwapType {

    private final boolean allowsOffhand;

    MatcherSwapType(String name, int index, boolean allowsOffhand) {
        super(name, index);
        this.allowsOffhand = allowsOffhand;
    }

    public MatcherSwapType(String name, boolean allowsOffhand) {
        super(name);
        this.allowsOffhand = allowsOffhand;
    }

    @Override
    public ItemStack getSignatureItem(Player player) {
        ItemStack main = player.m_21205_();
        if (this.match(main)) {
            return main;
        } else {
            if (this.allowsOffhand) {
                ItemStack off = player.m_21206_();
                if (this.match(off)) {
                    return off;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    public abstract boolean match(ItemStack var1);

    public boolean allowsOffhand() {
        return this.allowsOffhand;
    }
}