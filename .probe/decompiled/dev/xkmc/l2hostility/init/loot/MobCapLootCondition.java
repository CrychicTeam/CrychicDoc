package dev.xkmc.l2hostility.init.loot;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

@SerialClass
public class MobCapLootCondition implements LootItemCondition {

    @SerialField
    public int minLevel;

    @SerialField
    public int maxLevel;

    @Deprecated
    public MobCapLootCondition() {
    }

    public MobCapLootCondition(int minLevel) {
        this.minLevel = minLevel;
    }

    @Override
    public LootItemConditionType getType() {
        return (LootItemConditionType) TraitGLMProvider.MOB_LEVEL.get();
    }

    public boolean test(LootContext lootContext) {
        if (lootContext.getParam(LootContextParams.THIS_ENTITY) instanceof LivingEntity le && MobTraitCap.HOLDER.isProper(le)) {
            MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(le);
            return (this.minLevel <= 0 || cap.getLevel() >= this.minLevel) && (this.maxLevel <= 0 || cap.getLevel() < this.maxLevel);
        }
        return false;
    }
}