package net.minecraft.advancements.critereon;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class InventoryChangeTrigger extends SimpleCriterionTrigger<InventoryChangeTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("inventory_changed");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public InventoryChangeTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        JsonObject $$3 = GsonHelper.getAsJsonObject(jsonObject0, "slots", new JsonObject());
        MinMaxBounds.Ints $$4 = MinMaxBounds.Ints.fromJson($$3.get("occupied"));
        MinMaxBounds.Ints $$5 = MinMaxBounds.Ints.fromJson($$3.get("full"));
        MinMaxBounds.Ints $$6 = MinMaxBounds.Ints.fromJson($$3.get("empty"));
        ItemPredicate[] $$7 = ItemPredicate.fromJsonArray(jsonObject0.get("items"));
        return new InventoryChangeTrigger.TriggerInstance(contextAwarePredicate1, $$4, $$5, $$6, $$7);
    }

    public void trigger(ServerPlayer serverPlayer0, Inventory inventory1, ItemStack itemStack2) {
        int $$3 = 0;
        int $$4 = 0;
        int $$5 = 0;
        for (int $$6 = 0; $$6 < inventory1.getContainerSize(); $$6++) {
            ItemStack $$7 = inventory1.getItem($$6);
            if ($$7.isEmpty()) {
                $$4++;
            } else {
                $$5++;
                if ($$7.getCount() >= $$7.getMaxStackSize()) {
                    $$3++;
                }
            }
        }
        this.trigger(serverPlayer0, inventory1, itemStack2, $$3, $$4, $$5);
    }

    private void trigger(ServerPlayer serverPlayer0, Inventory inventory1, ItemStack itemStack2, int int3, int int4, int int5) {
        this.m_66234_(serverPlayer0, p_43166_ -> p_43166_.matches(inventory1, itemStack2, int3, int4, int5));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final MinMaxBounds.Ints slotsOccupied;

        private final MinMaxBounds.Ints slotsFull;

        private final MinMaxBounds.Ints slotsEmpty;

        private final ItemPredicate[] predicates;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, MinMaxBounds.Ints minMaxBoundsInts1, MinMaxBounds.Ints minMaxBoundsInts2, MinMaxBounds.Ints minMaxBoundsInts3, ItemPredicate[] itemPredicate4) {
            super(InventoryChangeTrigger.ID, contextAwarePredicate0);
            this.slotsOccupied = minMaxBoundsInts1;
            this.slotsFull = minMaxBoundsInts2;
            this.slotsEmpty = minMaxBoundsInts3;
            this.predicates = itemPredicate4;
        }

        public static InventoryChangeTrigger.TriggerInstance hasItems(ItemPredicate... itemPredicate0) {
            return new InventoryChangeTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, itemPredicate0);
        }

        public static InventoryChangeTrigger.TriggerInstance hasItems(ItemLike... itemLike0) {
            ItemPredicate[] $$1 = new ItemPredicate[itemLike0.length];
            for (int $$2 = 0; $$2 < itemLike0.length; $$2++) {
                $$1[$$2] = new ItemPredicate(null, ImmutableSet.of(itemLike0[$$2].asItem()), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY);
            }
            return hasItems($$1);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            if (!this.slotsOccupied.m_55327_() || !this.slotsFull.m_55327_() || !this.slotsEmpty.m_55327_()) {
                JsonObject $$2 = new JsonObject();
                $$2.add("occupied", this.slotsOccupied.m_55328_());
                $$2.add("full", this.slotsFull.m_55328_());
                $$2.add("empty", this.slotsEmpty.m_55328_());
                $$1.add("slots", $$2);
            }
            if (this.predicates.length > 0) {
                JsonArray $$3 = new JsonArray();
                for (ItemPredicate $$4 : this.predicates) {
                    $$3.add($$4.serializeToJson());
                }
                $$1.add("items", $$3);
            }
            return $$1;
        }

        public boolean matches(Inventory inventory0, ItemStack itemStack1, int int2, int int3, int int4) {
            if (!this.slotsFull.matches(int2)) {
                return false;
            } else if (!this.slotsEmpty.matches(int3)) {
                return false;
            } else if (!this.slotsOccupied.matches(int4)) {
                return false;
            } else {
                int $$5 = this.predicates.length;
                if ($$5 == 0) {
                    return true;
                } else if ($$5 != 1) {
                    List<ItemPredicate> $$6 = new ObjectArrayList(this.predicates);
                    int $$7 = inventory0.getContainerSize();
                    for (int $$8 = 0; $$8 < $$7; $$8++) {
                        if ($$6.isEmpty()) {
                            return true;
                        }
                        ItemStack $$9 = inventory0.getItem($$8);
                        if (!$$9.isEmpty()) {
                            $$6.removeIf(p_43194_ -> p_43194_.matches($$9));
                        }
                    }
                    return $$6.isEmpty();
                } else {
                    return !itemStack1.isEmpty() && this.predicates[0].matches(itemStack1);
                }
            }
        }
    }
}