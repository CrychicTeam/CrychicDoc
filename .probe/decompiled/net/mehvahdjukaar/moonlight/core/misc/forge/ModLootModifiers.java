package net.mehvahdjukaar.moonlight.core.misc.forge;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ModLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, "moonlight");

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM_GLM = LOOT_MODIFIERS.register("add_item", ModLootModifiers.AddItemModifier.CODEC);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> REPLACE_ITEM_GLM = LOOT_MODIFIERS.register("replace_item", ModLootModifiers.ReplaceItemModifier.CODEC);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_TABLE = LOOT_MODIFIERS.register("add_loot_table", ModLootModifiers.AddTableModifier.CODEC);

    public static void register() {
        LOOT_MODIFIERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static class AddItemModifier extends LootModifier {

        public static final Supplier<Codec<ModLootModifiers.AddItemModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ItemStack.CODEC.fieldOf("item").forGetter(m -> m.addedItemStack)).apply(inst, ModLootModifiers.AddItemModifier::new)));

        private final ItemStack addedItemStack;

        protected AddItemModifier(LootItemCondition[] conditionsIn, ItemStack addedItemStack) {
            super(conditionsIn);
            this.addedItemStack = addedItemStack;
        }

        @NotNull
        @Override
        protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
            ItemStack addedStack = this.addedItemStack.copy();
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

    public static class AddTableModifier extends LootModifier {

        public static final Supplier<Codec<ModLootModifiers.AddTableModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ResourceLocation.CODEC.fieldOf("loot_table").forGetter(m -> m.injectTableId)).apply(inst, ModLootModifiers.AddTableModifier::new)));

        private final ResourceLocation injectTableId;

        protected AddTableModifier(LootItemCondition[] conditionsIn, ResourceLocation addedItemStack) {
            super(conditionsIn);
            this.injectTableId = addedItemStack;
        }

        @NotNull
        @Override
        protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
            LootTable lootTable = context.getLevel().getServer().getLootData().m_278676_(this.injectTableId);
            lootTable.getRandomItems(context, generatedLoot::add);
            return generatedLoot;
        }

        @Override
        public Codec<? extends IGlobalLootModifier> codec() {
            return (Codec<? extends IGlobalLootModifier>) CODEC.get();
        }
    }

    public static class ReplaceItemModifier extends LootModifier {

        public static final Supplier<Codec<ModLootModifiers.ReplaceItemModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ItemStack.CODEC.fieldOf("item").forGetter(m -> m.itemStack)).apply(inst, ModLootModifiers.ReplaceItemModifier::new)));

        private final ItemStack itemStack;

        protected ReplaceItemModifier(LootItemCondition[] conditionsIn, ItemStack addedItemStack) {
            super(conditionsIn);
            this.itemStack = addedItemStack;
        }

        @NotNull
        @Override
        protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
            if (!generatedLoot.isEmpty()) {
                generatedLoot.set(0, this.itemStack.copy());
            }
            return generatedLoot;
        }

        @Override
        public Codec<? extends IGlobalLootModifier> codec() {
            return (Codec<? extends IGlobalLootModifier>) CODEC.get();
        }
    }
}