package dev.xkmc.modulargolems.content.item.card;

import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class DefaultFilterCard extends TargetFilterCard {

    public static boolean defaultPredicate(LivingEntity e) {
        return e instanceof Enemy && !(e instanceof Creeper);
    }

    public DefaultFilterCard(Item.Properties properties) {
        super(properties);
    }

    @Override
    public Predicate<LivingEntity> mayTarget(ItemStack stack) {
        return DefaultFilterCard::defaultPredicate;
    }

    @Override
    protected InteractionResultHolder<ItemStack> removeLast(Player player, ItemStack stack) {
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(MGLangData.TARGET_DEFAULT.get());
    }
}