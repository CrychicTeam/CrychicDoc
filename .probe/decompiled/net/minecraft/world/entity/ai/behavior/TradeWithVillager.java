package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TradeWithVillager extends Behavior<Villager> {

    private static final int INTERACT_DIST_SQR = 5;

    private static final float SPEED_MODIFIER = 0.5F;

    private Set<Item> trades = ImmutableSet.of();

    public TradeWithVillager() {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Villager villager1) {
        return BehaviorUtils.targetIsValid(villager1.getBrain(), MemoryModuleType.INTERACTION_TARGET, EntityType.VILLAGER);
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Villager villager1, long long2) {
        return this.checkExtraStartConditions(serverLevel0, villager1);
    }

    protected void start(ServerLevel serverLevel0, Villager villager1, long long2) {
        Villager $$3 = (Villager) villager1.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        BehaviorUtils.lockGazeAndWalkToEachOther(villager1, $$3, 0.5F);
        this.trades = figureOutWhatIAmWillingToTrade(villager1, $$3);
    }

    protected void tick(ServerLevel serverLevel0, Villager villager1, long long2) {
        Villager $$3 = (Villager) villager1.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        if (!(villager1.m_20280_($$3) > 5.0)) {
            BehaviorUtils.lockGazeAndWalkToEachOther(villager1, $$3, 0.5F);
            villager1.gossip(serverLevel0, $$3, long2);
            if (villager1.hasExcessFood() && (villager1.getVillagerData().getProfession() == VillagerProfession.FARMER || $$3.wantsMoreFood())) {
                throwHalfStack(villager1, Villager.FOOD_POINTS.keySet(), $$3);
            }
            if ($$3.getVillagerData().getProfession() == VillagerProfession.FARMER && villager1.m_35311_().m_18947_(Items.WHEAT) > Items.WHEAT.getMaxStackSize() / 2) {
                throwHalfStack(villager1, ImmutableSet.of(Items.WHEAT), $$3);
            }
            if (!this.trades.isEmpty() && villager1.m_35311_().m_18949_(this.trades)) {
                throwHalfStack(villager1, this.trades, $$3);
            }
        }
    }

    protected void stop(ServerLevel serverLevel0, Villager villager1, long long2) {
        villager1.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
    }

    private static Set<Item> figureOutWhatIAmWillingToTrade(Villager villager0, Villager villager1) {
        ImmutableSet<Item> $$2 = villager1.getVillagerData().getProfession().requestedItems();
        ImmutableSet<Item> $$3 = villager0.getVillagerData().getProfession().requestedItems();
        return (Set<Item>) $$2.stream().filter(p_24431_ -> !$$3.contains(p_24431_)).collect(Collectors.toSet());
    }

    private static void throwHalfStack(Villager villager0, Set<Item> setItem1, LivingEntity livingEntity2) {
        SimpleContainer $$3 = villager0.m_35311_();
        ItemStack $$4 = ItemStack.EMPTY;
        int $$5 = 0;
        while ($$5 < $$3.getContainerSize()) {
            ItemStack $$6;
            Item $$7;
            int $$8;
            label28: {
                $$6 = $$3.getItem($$5);
                if (!$$6.isEmpty()) {
                    $$7 = $$6.getItem();
                    if (setItem1.contains($$7)) {
                        if ($$6.getCount() > $$6.getMaxStackSize() / 2) {
                            $$8 = $$6.getCount() / 2;
                            break label28;
                        }
                        if ($$6.getCount() > 24) {
                            $$8 = $$6.getCount() - 24;
                            break label28;
                        }
                    }
                }
                $$5++;
                continue;
            }
            $$6.shrink($$8);
            $$4 = new ItemStack($$7, $$8);
            break;
        }
        if (!$$4.isEmpty()) {
            BehaviorUtils.throwItem(villager0, $$4, livingEntity2.m_20182_());
        }
    }
}