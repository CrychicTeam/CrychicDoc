package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class KillTraitsTrigger extends BaseCriterion<KillTraitsTrigger.Ins, KillTraitsTrigger> {

    public static KillTraitsTrigger.Ins ins(MobTrait... traits) {
        KillTraitsTrigger.Ins ans = new KillTraitsTrigger.Ins(HostilityTriggers.KILL_TRAITS.m_7295_(), ContextAwarePredicate.ANY);
        ans.traits = traits;
        return ans;
    }

    public KillTraitsTrigger(ResourceLocation id) {
        super(id, KillTraitsTrigger.Ins::new, KillTraitsTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player, MobTraitCap cap) {
        this.m_66234_(player, e -> e.matchAll(cap));
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<KillTraitsTrigger.Ins, KillTraitsTrigger> {

        @SerialField
        public MobTrait[] traits;

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }

        public boolean matchAll(MobTraitCap cap) {
            if (cap.traits.isEmpty()) {
                return false;
            } else {
                for (MobTrait e : this.traits) {
                    if (!cap.hasTrait(e)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }
}