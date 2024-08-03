package dev.xkmc.l2backpack.init.advancement;

import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2screentracker.screen.source.ItemSource;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SlotClickTrigger extends BaseCriterion<SlotClickTrigger.Ins, SlotClickTrigger> {

    public static SlotClickTrigger.Ins fromGUI() {
        return new SlotClickTrigger.Ins(BackpackTriggers.SLOT_CLICK.m_7295_(), ContextAwarePredicate.ANY);
    }

    public static SlotClickTrigger.Ins fromKeyBind() {
        SlotClickTrigger.Ins ans = fromGUI();
        ans.keybind = true;
        return ans;
    }

    public static SlotClickTrigger.Ins fromBackpack(ItemSource<?> type) {
        SlotClickTrigger.Ins ans = fromGUI();
        ans.origin = type;
        return ans;
    }

    public SlotClickTrigger(ResourceLocation id) {
        super(id, SlotClickTrigger.Ins::new, SlotClickTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player, ItemSource<?> type, boolean keybind) {
        this.m_66234_(player, e -> (e.origin == null || e.origin == type) && e.keybind == keybind);
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<SlotClickTrigger.Ins, SlotClickTrigger> {

        @Nullable
        @SerialField
        private ItemSource<?> origin;

        @SerialField
        private boolean keybind = false;

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}