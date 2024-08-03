package snownee.lychee.core;

import java.util.List;
import java.util.Map;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.jetbrains.annotations.Nullable;
import snownee.lychee.core.input.ItemHolderCollection;
import snownee.lychee.util.RecipeMatcher;

public class ItemShapelessContext extends LycheeContext {

    public final List<ItemEntity> itemEntities;

    public List<ItemEntity> filteredItems;

    private RecipeMatcher<ItemStack> match;

    public int totalItems;

    protected ItemShapelessContext(RandomSource pRandom, Level level, Map<LootContextParam<?>, Object> pParams, List<ItemEntity> itemEntities) {
        super(pRandom, level, pParams);
        this.itemEntities = itemEntities;
        this.totalItems = itemEntities.stream().map(ItemEntity::m_32055_).mapToInt(ItemStack::m_41613_).sum();
    }

    public void setMatch(@Nullable RecipeMatcher<ItemStack> match) {
        this.match = match;
        if (match == null) {
            this.itemHolders = ItemHolderCollection.EMPTY;
        } else {
            ItemEntity[] entities = new ItemEntity[match.tests.size()];
            for (int i = 0; i < match.inputUsed.length; i++) {
                for (int j = 0; j < match.inputUsed[i]; j++) {
                    entities[match.use[i][j]] = (ItemEntity) this.filteredItems.get(i);
                }
            }
            this.itemHolders = ItemHolderCollection.InWorld.of(entities);
        }
    }

    @Nullable
    public RecipeMatcher<ItemStack> getMatch() {
        return this.match;
    }

    public static class Builder<C extends ItemShapelessContext> extends LycheeContext.Builder<C> {

        public final List<ItemEntity> itemEntities;

        public Builder(Level level, List<ItemEntity> itemEntities) {
            super(level);
            this.itemEntities = itemEntities;
        }

        public C create(LootContextParamSet pParameterSet) {
            this.beforeCreate(pParameterSet);
            return (C) (new ItemShapelessContext(this.random, this.level, this.params, this.itemEntities));
        }
    }
}