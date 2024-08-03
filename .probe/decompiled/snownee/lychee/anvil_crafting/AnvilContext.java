package snownee.lychee.anvil_crafting;

import java.util.Map;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import snownee.lychee.core.LycheeContext;

public class AnvilContext extends LycheeContext {

    public final ItemStack left;

    public final ItemStack right;

    public final String name;

    public int levelCost;

    public int materialCost = 1;

    protected AnvilContext(RandomSource pRandom, Level level, Map<LootContextParam<?>, Object> pParams, ItemStack left, ItemStack right, String name) {
        super(pRandom, level, pParams);
        this.left = left;
        this.right = right;
        this.name = name;
    }

    public static class Builder extends LycheeContext.Builder<AnvilContext> {

        private final ItemStack left;

        private final ItemStack right;

        private final String name;

        public Builder(Level level, ItemStack left, ItemStack right, String name) {
            super(level);
            this.left = left;
            this.right = right;
            this.name = name;
        }

        public AnvilContext create(LootContextParamSet pParameterSet) {
            this.beforeCreate(pParameterSet);
            return new AnvilContext(this.random, this.level, this.params, this.left, this.right, this.name);
        }
    }
}