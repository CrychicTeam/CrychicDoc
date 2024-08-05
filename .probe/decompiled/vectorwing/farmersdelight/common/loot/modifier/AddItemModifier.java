package vectorwing.farmersdelight.common.loot.modifier;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class AddItemModifier extends LootModifier {

    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(inst.group(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.addedItem), Codec.INT.optionalFieldOf("count", 1).forGetter(m -> m.count))).apply(inst, AddItemModifier::new)));

    private final Item addedItem;

    private final int count;

    protected AddItemModifier(LootItemCondition[] conditionsIn, Item addedItemIn, int count) {
        super(conditionsIn);
        this.addedItem = addedItemIn;
        this.count = count;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ItemStack addedStack = new ItemStack(this.addedItem, this.count);
        if (addedStack.getCount() < addedStack.getMaxStackSize()) {
            generatedLoot.add(addedStack);
        } else {
            int i = addedStack.getCount();
            while (i > 0) {
                ItemStack subStack = addedStack.copy();
                subStack.setCount(Math.min(addedStack.getMaxStackSize(), i));
                i -= subStack.getCount();
                generatedLoot.add(subStack);
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return (Codec<? extends IGlobalLootModifier>) CODEC.get();
    }
}