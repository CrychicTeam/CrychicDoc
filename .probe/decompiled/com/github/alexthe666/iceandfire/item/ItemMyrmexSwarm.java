package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSwarmer;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemMyrmexSwarm extends Item {

    private final boolean jungle;

    public ItemMyrmexSwarm(boolean jungle) {
        super(new Item.Properties().stacksTo(1));
        this.jungle = jungle;
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand hand) {
        ItemStack itemStackIn = playerIn.m_21120_(hand);
        playerIn.m_6672_(hand);
        playerIn.m_6674_(hand);
        if (!playerIn.isCreative()) {
            itemStackIn.shrink(1);
            playerIn.getCooldowns().addCooldown(this, 20);
        }
        for (int i = 0; i < 5; i++) {
            EntityMyrmexSwarmer myrmex = new EntityMyrmexSwarmer(IafEntityRegistry.MYRMEX_SWARMER.get(), worldIn);
            myrmex.m_6034_(playerIn.m_20185_(), playerIn.m_20186_(), playerIn.m_20189_());
            myrmex.setJungleVariant(this.jungle);
            myrmex.setSummonedBy(playerIn);
            myrmex.setFlying(true);
            if (!worldIn.isClientSide) {
                worldIn.m_7967_(myrmex);
            }
        }
        playerIn.getCooldowns().addCooldown(this, 1800);
        return new InteractionResultHolder<>(InteractionResult.PASS, itemStackIn);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.myrmex_swarm.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.myrmex_swarm.desc_1").withStyle(ChatFormatting.GRAY));
    }
}