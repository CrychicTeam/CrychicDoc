package net.minecraft.world.entity.player;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import java.util.BitSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public class StackedContents {

    private static final int EMPTY = 0;

    public final Int2IntMap contents = new Int2IntOpenHashMap();

    public void accountSimpleStack(ItemStack itemStack0) {
        if (!itemStack0.isDamaged() && !itemStack0.isEnchanted() && !itemStack0.hasCustomHoverName()) {
            this.accountStack(itemStack0);
        }
    }

    public void accountStack(ItemStack itemStack0) {
        this.accountStack(itemStack0, 64);
    }

    public void accountStack(ItemStack itemStack0, int int1) {
        if (!itemStack0.isEmpty()) {
            int $$2 = getStackingIndex(itemStack0);
            int $$3 = Math.min(int1, itemStack0.getCount());
            this.put($$2, $$3);
        }
    }

    public static int getStackingIndex(ItemStack itemStack0) {
        return BuiltInRegistries.ITEM.m_7447_(itemStack0.getItem());
    }

    boolean has(int int0) {
        return this.contents.get(int0) > 0;
    }

    int take(int int0, int int1) {
        int $$2 = this.contents.get(int0);
        if ($$2 >= int1) {
            this.contents.put(int0, $$2 - int1);
            return int0;
        } else {
            return 0;
        }
    }

    void put(int int0, int int1) {
        this.contents.put(int0, this.contents.get(int0) + int1);
    }

    public boolean canCraft(Recipe<?> recipe0, @Nullable IntList intList1) {
        return this.canCraft(recipe0, intList1, 1);
    }

    public boolean canCraft(Recipe<?> recipe0, @Nullable IntList intList1, int int2) {
        return new StackedContents.RecipePicker(recipe0).tryPick(int2, intList1);
    }

    public int getBiggestCraftableStack(Recipe<?> recipe0, @Nullable IntList intList1) {
        return this.getBiggestCraftableStack(recipe0, Integer.MAX_VALUE, intList1);
    }

    public int getBiggestCraftableStack(Recipe<?> recipe0, int int1, @Nullable IntList intList2) {
        return new StackedContents.RecipePicker(recipe0).tryPickAll(int1, intList2);
    }

    public static ItemStack fromStackingIndex(int int0) {
        return int0 == 0 ? ItemStack.EMPTY : new ItemStack(Item.byId(int0));
    }

    public void clear() {
        this.contents.clear();
    }

    class RecipePicker {

        private final Recipe<?> recipe;

        private final List<Ingredient> ingredients = Lists.newArrayList();

        private final int ingredientCount;

        private final int[] items;

        private final int itemCount;

        private final BitSet data;

        private final IntList path = new IntArrayList();

        public RecipePicker(Recipe<?> recipe0) {
            this.recipe = recipe0;
            this.ingredients.addAll(recipe0.getIngredients());
            this.ingredients.removeIf(Ingredient::m_43947_);
            this.ingredientCount = this.ingredients.size();
            this.items = this.getUniqueAvailableIngredientItems();
            this.itemCount = this.items.length;
            this.data = new BitSet(this.ingredientCount + this.itemCount + this.ingredientCount + this.ingredientCount * this.itemCount);
            for (int $$1 = 0; $$1 < this.ingredients.size(); $$1++) {
                IntList $$2 = ((Ingredient) this.ingredients.get($$1)).getStackingIds();
                for (int $$3 = 0; $$3 < this.itemCount; $$3++) {
                    if ($$2.contains(this.items[$$3])) {
                        this.data.set(this.getIndex(true, $$3, $$1));
                    }
                }
            }
        }

        public boolean tryPick(int int0, @Nullable IntList intList1) {
            if (int0 <= 0) {
                return true;
            } else {
                int $$2;
                for ($$2 = 0; this.dfs(int0); $$2++) {
                    StackedContents.this.take(this.items[this.path.getInt(0)], int0);
                    int $$3 = this.path.size() - 1;
                    this.setSatisfied(this.path.getInt($$3));
                    for (int $$4 = 0; $$4 < $$3; $$4++) {
                        this.toggleResidual(($$4 & 1) == 0, this.path.get($$4), this.path.get($$4 + 1));
                    }
                    this.path.clear();
                    this.data.clear(0, this.ingredientCount + this.itemCount);
                }
                boolean $$5 = $$2 == this.ingredientCount;
                boolean $$6 = $$5 && intList1 != null;
                if ($$6) {
                    intList1.clear();
                }
                this.data.clear(0, this.ingredientCount + this.itemCount + this.ingredientCount);
                int $$7 = 0;
                List<Ingredient> $$8 = this.recipe.getIngredients();
                for (int $$9 = 0; $$9 < $$8.size(); $$9++) {
                    if ($$6 && ((Ingredient) $$8.get($$9)).isEmpty()) {
                        intList1.add(0);
                    } else {
                        for (int $$10 = 0; $$10 < this.itemCount; $$10++) {
                            if (this.hasResidual(false, $$7, $$10)) {
                                this.toggleResidual(true, $$10, $$7);
                                StackedContents.this.put(this.items[$$10], int0);
                                if ($$6) {
                                    intList1.add(this.items[$$10]);
                                }
                            }
                        }
                        $$7++;
                    }
                }
                return $$5;
            }
        }

        private int[] getUniqueAvailableIngredientItems() {
            IntCollection $$0 = new IntAVLTreeSet();
            for (Ingredient $$1 : this.ingredients) {
                $$0.addAll($$1.getStackingIds());
            }
            IntIterator $$2 = $$0.iterator();
            while ($$2.hasNext()) {
                if (!StackedContents.this.has($$2.nextInt())) {
                    $$2.remove();
                }
            }
            return $$0.toIntArray();
        }

        private boolean dfs(int int0) {
            int $$1 = this.itemCount;
            for (int $$2 = 0; $$2 < $$1; $$2++) {
                if (StackedContents.this.contents.get(this.items[$$2]) >= int0) {
                    this.visit(false, $$2);
                    while (!this.path.isEmpty()) {
                        int $$3 = this.path.size();
                        boolean $$4 = ($$3 & 1) == 1;
                        int $$5 = this.path.getInt($$3 - 1);
                        if (!$$4 && !this.isSatisfied($$5)) {
                            break;
                        }
                        int $$6 = $$4 ? this.ingredientCount : $$1;
                        int $$7 = 0;
                        while (true) {
                            if ($$7 < $$6) {
                                if (this.hasVisited($$4, $$7) || !this.hasConnection($$4, $$5, $$7) || !this.hasResidual($$4, $$5, $$7)) {
                                    $$7++;
                                    continue;
                                }
                                this.visit($$4, $$7);
                            }
                            $$7 = this.path.size();
                            if ($$7 == $$3) {
                                this.path.removeInt($$7 - 1);
                            }
                            break;
                        }
                    }
                    if (!this.path.isEmpty()) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean isSatisfied(int int0) {
            return this.data.get(this.getSatisfiedIndex(int0));
        }

        private void setSatisfied(int int0) {
            this.data.set(this.getSatisfiedIndex(int0));
        }

        private int getSatisfiedIndex(int int0) {
            return this.ingredientCount + this.itemCount + int0;
        }

        private boolean hasConnection(boolean boolean0, int int1, int int2) {
            return this.data.get(this.getIndex(boolean0, int1, int2));
        }

        private boolean hasResidual(boolean boolean0, int int1, int int2) {
            return boolean0 != this.data.get(1 + this.getIndex(boolean0, int1, int2));
        }

        private void toggleResidual(boolean boolean0, int int1, int int2) {
            this.data.flip(1 + this.getIndex(boolean0, int1, int2));
        }

        private int getIndex(boolean boolean0, int int1, int int2) {
            int $$3 = boolean0 ? int1 * this.ingredientCount + int2 : int2 * this.ingredientCount + int1;
            return this.ingredientCount + this.itemCount + this.ingredientCount + 2 * $$3;
        }

        private void visit(boolean boolean0, int int1) {
            this.data.set(this.getVisitedIndex(boolean0, int1));
            this.path.add(int1);
        }

        private boolean hasVisited(boolean boolean0, int int1) {
            return this.data.get(this.getVisitedIndex(boolean0, int1));
        }

        private int getVisitedIndex(boolean boolean0, int int1) {
            return (boolean0 ? 0 : this.ingredientCount) + int1;
        }

        public int tryPickAll(int int0, @Nullable IntList intList1) {
            int $$2 = 0;
            int $$3 = Math.min(int0, this.getMinIngredientCount()) + 1;
            while (true) {
                int $$4 = ($$2 + $$3) / 2;
                if (this.tryPick($$4, null)) {
                    if ($$3 - $$2 <= 1) {
                        if ($$4 > 0) {
                            this.tryPick($$4, intList1);
                        }
                        return $$4;
                    }
                    $$2 = $$4;
                } else {
                    $$3 = $$4;
                }
            }
        }

        private int getMinIngredientCount() {
            int $$0 = Integer.MAX_VALUE;
            for (Ingredient $$1 : this.ingredients) {
                int $$2 = 0;
                IntListIterator var5 = $$1.getStackingIds().iterator();
                while (var5.hasNext()) {
                    int $$3 = (Integer) var5.next();
                    $$2 = Math.max($$2, StackedContents.this.contents.get($$3));
                }
                if ($$0 > 0) {
                    $$0 = Math.min($$0, $$2);
                }
            }
            return $$0;
        }
    }
}