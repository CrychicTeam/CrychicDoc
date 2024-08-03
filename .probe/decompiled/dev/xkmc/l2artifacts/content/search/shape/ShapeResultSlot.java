package dev.xkmc.l2artifacts.content.search.shape;

import dev.xkmc.l2library.base.menu.base.PredSlot;
import java.util.function.Predicate;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class ShapeResultSlot extends PredSlot {

    public ShapeResultSlot(Container inv, int ind, int x, int y, Predicate<ItemStack> pred) {
        super(inv, ind, x, y, pred);
    }

    @Override
    public ItemStack remove(int amount) {
        this.m_6654_();
        return super.m_6201_(amount);
    }
}