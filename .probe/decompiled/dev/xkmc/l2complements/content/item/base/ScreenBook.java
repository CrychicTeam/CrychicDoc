package dev.xkmc.l2complements.content.item.base;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ScreenBook extends Item {

    public Supplier<Supplier<?>> sup;

    public ScreenBook(Item.Properties props, Supplier<Supplier<?>> sup) {
        super(props.stacksTo(1));
        this.sup = sup;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (world.isClientSide()) {
            player.playSound(SoundEvents.BOOK_PAGE_TURN, 1.0F, 1.0F);
            Minecraft.getInstance().setScreen((Screen) ((Supplier) this.sup.get()).get());
        }
        return InteractionResultHolder.success(stack);
    }
}