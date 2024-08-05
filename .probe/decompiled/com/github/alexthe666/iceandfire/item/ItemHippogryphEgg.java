package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityHippogryphEgg;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemHippogryphEgg extends Item {

    public ItemHippogryphEgg() {
        super(new Item.Properties().stacksTo(1));
    }

    public static ItemStack createEggStack(EnumHippogryphTypes parent1, EnumHippogryphTypes parent2) {
        EnumHippogryphTypes eggType = ThreadLocalRandom.current().nextBoolean() ? parent1 : parent2;
        ItemStack stack = new ItemStack(IafItemRegistry.HIPPOGRYPH_EGG.get());
        CompoundTag tag = new CompoundTag();
        tag.putInt("EggOrdinal", eggType.ordinal());
        stack.setTag(tag);
        return stack;
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        if (!playerIn.isCreative()) {
            itemstack.shrink(1);
        }
        worldIn.playSound(null, playerIn.m_20185_(), playerIn.m_20186_(), playerIn.m_20189_(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isClientSide) {
            EntityHippogryphEgg entityegg = new EntityHippogryphEgg(IafEntityRegistry.HIPPOGRYPH_EGG.get(), worldIn, playerIn, itemstack);
            entityegg.m_37251_(playerIn, playerIn.m_146909_(), playerIn.m_146908_(), 0.0F, 1.5F, 1.0F);
            worldIn.m_7967_(entityegg);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        CompoundTag tag = stack.getTag();
        int eggOrdinal = 0;
        if (tag != null) {
            eggOrdinal = tag.getInt("EggOrdinal");
        }
        String type = EnumHippogryphTypes.values()[Mth.clamp(eggOrdinal, 0, EnumHippogryphTypes.values().length - 1)].name().toLowerCase();
        tooltip.add(Component.translatable("entity.iceandfire.hippogryph." + type).withStyle(ChatFormatting.GRAY));
    }
}