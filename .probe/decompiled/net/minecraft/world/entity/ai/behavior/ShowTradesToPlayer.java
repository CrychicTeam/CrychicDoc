package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

public class ShowTradesToPlayer extends Behavior<Villager> {

    private static final int MAX_LOOK_TIME = 900;

    private static final int STARTING_LOOK_TIME = 40;

    @Nullable
    private ItemStack playerItemStack;

    private final List<ItemStack> displayItems = Lists.newArrayList();

    private int cycleCounter;

    private int displayIndex;

    private int lookTime;

    public ShowTradesToPlayer(int int0, int int1) {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_PRESENT), int0, int1);
    }

    public boolean checkExtraStartConditions(ServerLevel serverLevel0, Villager villager1) {
        Brain<?> $$2 = villager1.getBrain();
        if (!$$2.getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent()) {
            return false;
        } else {
            LivingEntity $$3 = (LivingEntity) $$2.getMemory(MemoryModuleType.INTERACTION_TARGET).get();
            return $$3.m_6095_() == EntityType.PLAYER && villager1.m_6084_() && $$3.isAlive() && !villager1.m_6162_() && villager1.m_20280_($$3) <= 17.0;
        }
    }

    public boolean canStillUse(ServerLevel serverLevel0, Villager villager1, long long2) {
        return this.checkExtraStartConditions(serverLevel0, villager1) && this.lookTime > 0 && villager1.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent();
    }

    public void start(ServerLevel serverLevel0, Villager villager1, long long2) {
        super.start(serverLevel0, villager1, long2);
        this.lookAtTarget(villager1);
        this.cycleCounter = 0;
        this.displayIndex = 0;
        this.lookTime = 40;
    }

    public void tick(ServerLevel serverLevel0, Villager villager1, long long2) {
        LivingEntity $$3 = this.lookAtTarget(villager1);
        this.findItemsToDisplay($$3, villager1);
        if (!this.displayItems.isEmpty()) {
            this.displayCyclingItems(villager1);
        } else {
            clearHeldItem(villager1);
            this.lookTime = Math.min(this.lookTime, 40);
        }
        this.lookTime--;
    }

    public void stop(ServerLevel serverLevel0, Villager villager1, long long2) {
        super.stop(serverLevel0, villager1, long2);
        villager1.getBrain().eraseMemory(MemoryModuleType.INTERACTION_TARGET);
        clearHeldItem(villager1);
        this.playerItemStack = null;
    }

    private void findItemsToDisplay(LivingEntity livingEntity0, Villager villager1) {
        boolean $$2 = false;
        ItemStack $$3 = livingEntity0.getMainHandItem();
        if (this.playerItemStack == null || !ItemStack.isSameItem(this.playerItemStack, $$3)) {
            this.playerItemStack = $$3;
            $$2 = true;
            this.displayItems.clear();
        }
        if ($$2 && !this.playerItemStack.isEmpty()) {
            this.updateDisplayItems(villager1);
            if (!this.displayItems.isEmpty()) {
                this.lookTime = 900;
                this.displayFirstItem(villager1);
            }
        }
    }

    private void displayFirstItem(Villager villager0) {
        displayAsHeldItem(villager0, (ItemStack) this.displayItems.get(0));
    }

    private void updateDisplayItems(Villager villager0) {
        for (MerchantOffer $$1 : villager0.m_6616_()) {
            if (!$$1.isOutOfStock() && this.playerItemStackMatchesCostOfOffer($$1)) {
                this.displayItems.add($$1.getResult());
            }
        }
    }

    private boolean playerItemStackMatchesCostOfOffer(MerchantOffer merchantOffer0) {
        return ItemStack.isSameItem(this.playerItemStack, merchantOffer0.getCostA()) || ItemStack.isSameItem(this.playerItemStack, merchantOffer0.getCostB());
    }

    private static void clearHeldItem(Villager villager0) {
        villager0.m_8061_(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        villager0.m_21409_(EquipmentSlot.MAINHAND, 0.085F);
    }

    private static void displayAsHeldItem(Villager villager0, ItemStack itemStack1) {
        villager0.m_8061_(EquipmentSlot.MAINHAND, itemStack1);
        villager0.m_21409_(EquipmentSlot.MAINHAND, 0.0F);
    }

    private LivingEntity lookAtTarget(Villager villager0) {
        Brain<?> $$1 = villager0.getBrain();
        LivingEntity $$2 = (LivingEntity) $$1.getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        $$1.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker($$2, true));
        return $$2;
    }

    private void displayCyclingItems(Villager villager0) {
        if (this.displayItems.size() >= 2 && ++this.cycleCounter >= 40) {
            this.displayIndex++;
            this.cycleCounter = 0;
            if (this.displayIndex > this.displayItems.size() - 1) {
                this.displayIndex = 0;
            }
            displayAsHeldItem(villager0, (ItemStack) this.displayItems.get(this.displayIndex));
        }
    }
}