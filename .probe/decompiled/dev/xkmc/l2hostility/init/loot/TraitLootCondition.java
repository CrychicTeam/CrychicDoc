package dev.xkmc.l2hostility.init.loot;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

@SerialClass
public class TraitLootCondition implements LootItemCondition {

    @SerialField
    public MobTrait trait;

    @SerialField
    public int minLevel;

    @SerialField
    public int maxLevel;

    @Deprecated
    public TraitLootCondition() {
    }

    public TraitLootCondition(MobTrait trait, int minLevel, int maxLevel) {
        this.trait = trait;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    @Override
    public LootItemConditionType getType() {
        return (LootItemConditionType) TraitGLMProvider.TRAIT_AND_LEVEL.get();
    }

    public boolean test(LootContext lootContext) {
        if (lootContext.getParam(LootContextParams.THIS_ENTITY) instanceof LivingEntity le && MobTraitCap.HOLDER.isProper(le)) {
            MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(le);
            if (!cap.hasTrait(this.trait)) {
                return false;
            }
            int lv = cap.getTraitLevel(this.trait);
            return lv >= this.minLevel && lv <= this.maxLevel;
        }
        return false;
    }
}