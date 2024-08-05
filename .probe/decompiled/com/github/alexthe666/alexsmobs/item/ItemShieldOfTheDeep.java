package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import java.util.function.Consumer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ItemShieldOfTheDeep extends Item {

    public ItemShieldOfTheDeep(Item.Properties group) {
        super(group);
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_77661_1_) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_77659_1_, Player p_77659_2_, InteractionHand p_77659_3_) {
        ItemStack lvt_4_1_ = p_77659_2_.m_21120_(p_77659_3_);
        p_77659_2_.m_6672_(p_77659_3_);
        return InteractionResultHolder.consume(lvt_4_1_);
    }

    @Override
    public boolean isValidRepairItem(ItemStack p_82789_1_, ItemStack p_82789_2_) {
        return AMItemRegistry.SERRATED_SHARK_TOOTH.get() == p_82789_2_.getItem() || super.isValidRepairItem(p_82789_1_, p_82789_2_);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) AlexsMobs.PROXY.getISTERProperties());
    }
}