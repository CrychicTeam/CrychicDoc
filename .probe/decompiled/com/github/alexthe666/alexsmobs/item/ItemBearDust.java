package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import java.util.Random;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class ItemBearDust extends Item implements CustomTabBehavior {

    private final Random random = new Random();

    public ItemBearDust(Item.Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        playerIn.m_146850_(GameEvent.ITEM_INTERACT_START);
        worldIn.playSound(null, playerIn.m_20185_(), playerIn.m_20186_(), playerIn.m_20189_(), AMSoundRegistry.BEAR_DUST.get(), SoundSource.PLAYERS, 0.75F, this.random.nextFloat() * 0.2F + 0.9F);
        playerIn.getCooldowns().addCooldown(this, 3);
        playerIn.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public void fillItemCategory(CreativeModeTab.Output contents) {
    }
}