package dev.xkmc.l2backpack.init.advancement;

import dev.xkmc.l2backpack.network.DrawerInteractToServer;
import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class DrawerInteractTrigger extends BaseCriterion<DrawerInteractTrigger.Ins, DrawerInteractTrigger> {

    public static DrawerInteractTrigger.Ins fromType(DrawerInteractToServer.Type type) {
        DrawerInteractTrigger.Ins ans = new DrawerInteractTrigger.Ins(BackpackTriggers.DRAWER.m_7295_(), ContextAwarePredicate.ANY);
        ans.type = type;
        return ans;
    }

    public DrawerInteractTrigger(ResourceLocation id) {
        super(id, DrawerInteractTrigger.Ins::new, DrawerInteractTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player, DrawerInteractToServer.Type type) {
        this.m_66234_(player, e -> e.type == null || e.type == type);
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<DrawerInteractTrigger.Ins, DrawerInteractTrigger> {

        @Nullable
        @SerialField
        private DrawerInteractToServer.Type type;

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}