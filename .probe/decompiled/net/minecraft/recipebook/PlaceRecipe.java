package net.minecraft.recipebook;

import java.util.Iterator;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;

public interface PlaceRecipe<T> {

    default void placeRecipe(int int0, int int1, int int2, Recipe<?> recipe3, Iterator<T> iteratorT4, int int5) {
        int $$6 = int0;
        int $$7 = int1;
        if (recipe3 instanceof ShapedRecipe $$8) {
            $$6 = $$8.getWidth();
            $$7 = $$8.getHeight();
        }
        int $$9 = 0;
        for (int $$10 = 0; $$10 < int1; $$10++) {
            if ($$9 == int2) {
                $$9++;
            }
            boolean $$11 = (float) $$7 < (float) int1 / 2.0F;
            int $$12 = Mth.floor((float) int1 / 2.0F - (float) $$7 / 2.0F);
            if ($$11 && $$12 > $$10) {
                $$9 += int0;
                $$10++;
            }
            for (int $$13 = 0; $$13 < int0; $$13++) {
                if (!iteratorT4.hasNext()) {
                    return;
                }
                $$11 = (float) $$6 < (float) int0 / 2.0F;
                $$12 = Mth.floor((float) int0 / 2.0F - (float) $$6 / 2.0F);
                int $$14 = $$6;
                boolean $$15 = $$13 < $$6;
                if ($$11) {
                    $$14 = $$12 + $$6;
                    $$15 = $$12 <= $$13 && $$13 < $$12 + $$6;
                }
                if ($$15) {
                    this.addItemToSlot(iteratorT4, $$9, int5, $$10, $$13);
                } else if ($$14 == $$13) {
                    $$9 += int0 - $$13;
                    break;
                }
                $$9++;
            }
        }
    }

    void addItemToSlot(Iterator<T> var1, int var2, int var3, int var4, int var5);
}