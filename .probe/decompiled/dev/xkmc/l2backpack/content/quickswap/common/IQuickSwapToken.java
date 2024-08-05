package dev.xkmc.l2backpack.content.quickswap.common;

import dev.xkmc.l2backpack.content.quickswap.entry.ISwapEntry;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapType;
import java.util.List;
import net.minecraft.world.entity.player.Player;

public interface IQuickSwapToken<T extends ISwapEntry<T>> {

    void setSelected(int var1);

    List<T> getList();

    int getSelected();

    void shrink(int var1);

    QuickSwapType type();

    void swap(Player var1);
}