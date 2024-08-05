package top.theillusivec4.curios.common.util;

import com.google.gson.JsonObject;
import javax.annotation.Nonnull;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotPredicate;

public class EquipCurioTrigger extends SimpleCriterionTrigger<EquipCurioTrigger.Instance> {

    public static final ResourceLocation ID = new ResourceLocation("curios", "equip_curio");

    public static final EquipCurioTrigger INSTANCE = new EquipCurioTrigger();

    private EquipCurioTrigger() {
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Nonnull
    public EquipCurioTrigger.Instance createInstance(@Nonnull JsonObject json, @Nonnull ContextAwarePredicate playerPred, @Nonnull DeserializationContext conditions) {
        return new EquipCurioTrigger.Instance(playerPred, ItemPredicate.fromJson(json.get("item")), LocationPredicate.fromJson(json.get("location")), SlotPredicate.fromJson(json.get("curios:slot")));
    }

    public void trigger(ServerPlayer player, ItemStack stack, ServerLevel world, double x, double y, double z) {
        this.m_66234_(player, instance -> instance.test(null, stack, world, x, y, z));
    }

    public void trigger(SlotContext slotContext, ServerPlayer player, ItemStack stack, ServerLevel world, double x, double y, double z) {
        this.m_66234_(player, instance -> instance.test(slotContext, stack, world, x, y, z));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate item;

        private final LocationPredicate location;

        private final SlotPredicate slot;

        public Instance(ContextAwarePredicate playerPred, ItemPredicate count, LocationPredicate indexPos, SlotPredicate slotPredicate) {
            super(EquipCurioTrigger.ID, playerPred);
            this.item = count;
            this.location = indexPos;
            this.slot = slotPredicate;
        }

        @Nonnull
        @Override
        public JsonObject serializeToJson(@Nonnull SerializationContext pContext) {
            JsonObject jsonobject = super.serializeToJson(pContext);
            jsonobject.add("location", this.location.serializeToJson());
            jsonobject.add("item", this.item.serializeToJson());
            jsonobject.add("curios:slot", this.slot.serializeToJson());
            return jsonobject;
        }

        @Nonnull
        @Override
        public ResourceLocation getCriterion() {
            return EquipCurioTrigger.ID;
        }

        boolean test(SlotContext slotContext, ItemStack stack, ServerLevel world, double x, double y, double z) {
            return this.slot != null && slotContext != null && !this.slot.matches(slotContext) ? false : this.item.matches(stack) && this.location.matches(world, x, y, z);
        }
    }
}