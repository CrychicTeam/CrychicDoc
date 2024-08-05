package dev.xkmc.modulargolems.content.item.card;

import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class ClickEntityFilterCard<T> extends TargetFilterCard {

    public ClickEntityFilterCard(Item.Properties properties) {
        super(properties);
    }

    protected abstract List<T> getList(ItemStack var1);

    protected abstract T getValue(LivingEntity var1);

    protected abstract Component getName(T var1);

    protected abstract void setList(ItemStack var1, List<T> var2);

    protected InteractionResult addTargetEntity(Player player, ItemStack stack, LivingEntity target) {
        List<T> list = this.getList(stack);
        if (list.contains(this.getValue(target))) {
            return InteractionResult.SUCCESS;
        } else {
            if (!player.m_9236_().isClientSide()) {
                T val = this.getValue(target);
                list.add(val);
                this.setList(stack, list);
                player.m_213846_(MGLangData.TARGET_MSG_ADDED.get(this.getName(val)));
            }
            return InteractionResult.SUCCESS;
        }
    }

    protected InteractionResult removeTargetEntity(Player player, ItemStack stack, LivingEntity target) {
        List<T> list = this.getList(stack);
        if (!list.contains(this.getValue(target))) {
            return InteractionResult.FAIL;
        } else {
            if (!player.m_9236_().isClientSide()) {
                T val = this.getValue(target);
                if (list.remove(val)) {
                    this.setList(stack, list);
                    player.m_213846_(MGLangData.TARGET_MSG_REMOVED.get(this.getName(val)));
                }
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    protected InteractionResultHolder<ItemStack> removeLast(Player player, ItemStack stack) {
        List<T> list = this.getList(stack);
        if (list.size() == 0) {
            return InteractionResultHolder.fail(stack);
        } else {
            if (!player.m_9236_().isClientSide()) {
                T val = (T) list.remove(list.size() - 1);
                this.setList(stack, list);
                player.m_213846_(MGLangData.TARGET_MSG_REMOVED.get(this.getName(val)));
            }
            return InteractionResultHolder.success(stack);
        }
    }

    @Override
    public Predicate<LivingEntity> mayTarget(ItemStack stack) {
        HashSet<T> set = new HashSet(this.getList(stack));
        return e -> set.contains(this.getValue(e));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        ItemStack item = player.m_21120_(hand);
        return player.m_6144_() ? this.removeTargetEntity(player, item, target) : this.addTargetEntity(player, item, target);
    }
}