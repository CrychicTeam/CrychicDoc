package io.redspace.ironsspellbooks.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class AppendLootModifier<V> extends LootModifier {

    public static final Supplier<Codec<AppendLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(Codec.STRING.fieldOf("key").forGetter(m -> m.resourceLocationKey)).apply(inst, AppendLootModifier::new)));

    private final String resourceLocationKey;

    protected AppendLootModifier(LootItemCondition[] conditionsIn, String resourceLocationKey) {
        super(conditionsIn);
        this.resourceLocationKey = resourceLocationKey;
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        IronsSpellbooks.LOGGER.debug("AppendLootModifier.doApply {}", this.resourceLocationKey);
        ResourceLocation path = new ResourceLocation(this.resourceLocationKey);
        LootTable lootTable = context.getLevel().getServer().getLootData().m_278676_(path);
        ObjectArrayList<ItemStack> objectarraylist = new ObjectArrayList();
        lootTable.getRandomItemsRaw(context, objectarraylist::add);
        generatedLoot.addAll(objectarraylist);
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return (Codec<? extends IGlobalLootModifier>) CODEC.get();
    }
}