package net.minecraftforge.common.loot;

import com.mojang.datafixers.Products.P1;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.jetbrains.annotations.NotNull;

public abstract class LootModifier implements IGlobalLootModifier {

    protected final LootItemCondition[] conditions;

    private final Predicate<LootContext> combinedConditions;

    protected static <T extends LootModifier> P1<Mu<T>, LootItemCondition[]> codecStart(Instance<T> instance) {
        return instance.group(LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(lm -> lm.conditions));
    }

    protected LootModifier(LootItemCondition[] conditionsIn) {
        this.conditions = conditionsIn;
        this.combinedConditions = LootItemConditions.andConditions(conditionsIn);
    }

    @NotNull
    @Override
    public final ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        return this.combinedConditions.test(context) ? this.doApply(generatedLoot, context) : generatedLoot;
    }

    @NotNull
    protected abstract ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> var1, LootContext var2);
}