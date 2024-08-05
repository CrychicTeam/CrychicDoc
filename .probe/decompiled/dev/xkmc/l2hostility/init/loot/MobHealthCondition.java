package dev.xkmc.l2hostility.init.loot;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

@SerialClass
public class MobHealthCondition implements LootItemCondition {

    @SerialField
    public int minHealth;

    @Deprecated
    public MobHealthCondition() {
    }

    public MobHealthCondition(int minHealth) {
        this.minHealth = minHealth;
    }

    @Override
    public LootItemConditionType getType() {
        return (LootItemConditionType) TraitGLMProvider.MIN_HEALTH.get();
    }

    public boolean test(LootContext lootContext) {
        return lootContext.getParam(LootContextParams.THIS_ENTITY) instanceof LivingEntity le ? le.getMaxHealth() >= (float) this.minHealth : false;
    }
}