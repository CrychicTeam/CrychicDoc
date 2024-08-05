package dev.xkmc.modulargolems.content.modifier.special;

import dev.xkmc.modulargolems.content.capability.GolemConfigEntry;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.content.entity.mode.GolemModes;
import dev.xkmc.modulargolems.events.event.GolemHandleExpEvent;
import dev.xkmc.modulargolems.events.event.GolemHandleItemEvent;
import dev.xkmc.modulargolems.init.ModularGolems;
import dev.xkmc.modulargolems.init.data.MGConfig;
import dev.xkmc.modulargolems.init.data.MGLangData;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import dev.xkmc.modulargolems.mixin.ExperienceOrbAccessor;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class PickupGoal extends Goal {

    private static final int INTERVAL = 10;

    private static final int DELAY = 80;

    private static final String KEY = "modulargolems:pickup_delay";

    private final AbstractGolemEntity<?, ?> golem;

    private final int lv;

    private int delay = 0;

    private int destroyItemCount = 0;

    private int destroyExpCount = 0;

    private BlockEntity target;

    public PickupGoal(AbstractGolemEntity<?, ?> golem, int lv) {
        this.golem = golem;
        this.lv = lv;
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void tick() {
        if (this.delay > 0) {
            this.delay--;
        } else {
            this.delay = 10;
            AABB box = this.golem.m_20191_().inflate((double) (this.lv * MGConfig.COMMON.basePickupRange.get()));
            this.tryHandleItem(box);
            this.tryHandleExp(box);
        }
    }

    private void tryHandleItem(AABB box) {
        Player player = this.golem.getOwner();
        List<ItemEntity> items = this.golem.m_9236_().getEntities(EntityTypeTest.forClass(ItemEntity.class), box, e -> true);
        this.validateTarget();
        GolemConfigEntry config = this.golem.getConfigEntry(null);
        for (ItemEntity item : items) {
            if (config == null || config.pickupFilter.allowPickup(item.getItem())) {
                this.handleLeftoverItem(item, player);
            }
        }
        if (this.destroyItemCount > 0 && player != null) {
            ModularGolems.LOGGER.info(MGLangData.DESTROY_ITEM.get(this.golem, this.destroyItemCount).getString());
            player.m_213846_(MGLangData.DESTROY_ITEM.get(this.golem, this.destroyItemCount));
            this.destroyItemCount = 0;
        }
    }

    private void tryHandleExp(AABB box) {
        Player player = this.golem.getOwner();
        List<ExperienceOrb> exps = this.golem.m_9236_().getEntities(EntityTypeTest.forClass(ExperienceOrb.class), box, e -> true);
        ExperienceOrb first = null;
        for (ExperienceOrb exp : exps) {
            exp.value = exp.value * ((ExperienceOrbAccessor) exp).getCount();
            ((ExperienceOrbAccessor) exp).setCount(1);
            if (first == null) {
                first = exp;
            } else {
                first.value = first.value + exp.value;
                exp.m_146870_();
            }
        }
        if (first != null) {
            this.handleLeftoverExp(first, player);
        }
        if (this.destroyExpCount > 0 && player != null) {
            player.m_213846_(MGLangData.DESTROY_EXP.get(this.golem, this.destroyExpCount));
            this.destroyExpCount = 0;
        }
    }

    private int repairGolemAndItems(int exp) {
        Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, this.golem, ItemStack::m_41768_);
        if (entry != null) {
            ItemStack itemstack = (ItemStack) entry.getValue();
            float ratio = itemstack.getXpRepairRatio();
            int recovered = Math.min((int) ((float) exp * ratio), itemstack.getDamageValue());
            itemstack.setDamageValue(itemstack.getDamageValue() - recovered);
            int remain = ratio <= 0.0F ? 0 : (int) Math.max(0.0F, (float) exp - (float) recovered / ratio);
            return remain > 0 ? this.repairGolemAndItems(remain) : 0;
        } else if (!this.golem.hasFlag(GolemFlags.MENDING)) {
            return exp;
        } else {
            float lost = this.golem.m_21233_() - this.golem.m_21223_();
            float ratio = (float) MGConfig.COMMON.mendingXpCost.get().intValue();
            float heal = Math.min(lost, (float) exp / ratio);
            int cost = (int) (heal * ratio);
            this.golem.m_5634_(heal);
            return exp - cost;
        }
    }

    private void handleLeftoverItem(ItemEntity item, @Nullable Player player) {
        if (!item.getPersistentData().contains("modulargolems:pickup_delay", 4) || item.getPersistentData().getLong("modulargolems:pickup_delay") <= item.m_9236_().getGameTime()) {
            GolemHandleItemEvent event = new GolemHandleItemEvent(this.golem, item);
            MinecraftForge.EVENT_BUS.post(event);
            if (item.getItem().isEmpty()) {
                item.m_146870_();
            }
            if (!item.m_213877_()) {
                if (this.target != null && this.golem.getMode() == GolemModes.STAND) {
                    LazyOptional<IItemHandler> opt = this.target.getCapability(ForgeCapabilities.ITEM_HANDLER);
                    if (opt.resolve().isPresent()) {
                        IItemHandler handler = (IItemHandler) opt.resolve().get();
                        ItemStack remain = ItemHandlerHelper.insertItem(handler, item.getItem(), false);
                        if (remain.isEmpty()) {
                            item.m_146870_();
                            return;
                        }
                        item.setItem(remain);
                    }
                }
                if (player != null && player.m_6084_()) {
                    item.playerTouch(player);
                    if (!item.m_213877_()) {
                        item.m_20219_(player.m_20182_());
                        item.getPersistentData().putLong("modulargolems:pickup_delay", item.m_9236_().getGameTime() + 80L);
                        return;
                    }
                }
                if (!item.m_213877_()) {
                    if (!this.golem.hasFlag(GolemFlags.NO_DESTROY) && !item.hasPickUpDelay()) {
                        this.destroyItemCount = this.destroyItemCount + item.getItem().getCount();
                        item.m_146870_();
                    }
                }
            }
        }
    }

    private void handleLeftoverExp(ExperienceOrb exp, @Nullable Player player) {
        exp.value = this.repairGolemAndItems(exp.value);
        GolemHandleExpEvent event = new GolemHandleExpEvent(this.golem, exp);
        MinecraftForge.EVENT_BUS.post(event);
        if (exp.value <= 0) {
            exp.m_146870_();
        }
        if (!exp.m_213877_()) {
            if (player != null) {
                player.takeXpDelay = 0;
                exp.playerTouch(player);
            }
            if (!exp.m_213877_()) {
                this.destroyExpCount = this.destroyExpCount + exp.value;
                exp.m_146870_();
            }
        }
    }

    private void validateTarget() {
        if (this.target == null || this.target.isRemoved() || this.target.getLevel() != this.golem.m_9236_() || !(this.target.getBlockPos().m_123331_(this.golem.m_20183_()) <= 9.0)) {
            this.target = null;
            BlockPos origin = this.golem.m_20183_();
            BlockPos.MutableBlockPos pos = origin.mutable();
            int r = 1;
            double dist = Double.POSITIVE_INFINITY;
            for (int i = -r; i <= r; i++) {
                for (int j = -r; j <= r; j++) {
                    for (int k = -r; k <= r; k++) {
                        pos.setWithOffset(origin, i, j, k);
                        if (this.golem.m_9236_().getBlockState(pos).m_204336_(MGTagGen.POTENTIAL_DST)) {
                            BlockEntity be = this.golem.m_9236_().getBlockEntity(pos);
                            if (be != null && be.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().isPresent()) {
                                double d = pos.m_203193_(this.golem.m_20182_());
                                if (d < dist) {
                                    this.target = be;
                                    dist = d;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}