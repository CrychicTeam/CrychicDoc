package dev.xkmc.l2complements.content.enchantment.digging;

import java.util.List;
import net.minecraft.network.chat.Component;

public interface BlockBreaker {

    BlockBreakerInstance getInstance(DiggerContext var1);

    int getMaxLevel();

    default boolean ignoreHardness() {
        return false;
    }

    List<Component> descFull(int var1, String var2, boolean var3, boolean var4);
}