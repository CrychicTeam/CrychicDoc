package io.redspace.ironsspellbooks.item.weapons;

import io.redspace.ironsspellbooks.util.TooltipsUtils;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AutoloaderCrossbow extends CrossbowItem {

    public static final String LOADING = "Loading";

    public static final String LOADING_TIMESTAMP = "LoadingTimestamp";

    public AutoloaderCrossbow(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pHand) {
        ItemStack itemstack = player.m_21120_(pHand);
        if (m_40932_(itemstack)) {
            m_40887_(pLevel, player, pHand, itemstack, m_40945_(itemstack), 1.0F);
            m_40884_(itemstack, false);
            if (!player.getProjectile(itemstack).isEmpty()) {
                startLoading(player, itemstack);
            } else {
                player.playSound(SoundEvents.ITEM_BREAK, 0.75F, 1.5F);
            }
            return InteractionResultHolder.consume(itemstack);
        } else if (isLoading(itemstack)) {
            if (player.m_6047_()) {
                setLoadingTicks(itemstack, 0);
                setLoading(itemstack, false);
            }
            return InteractionResultHolder.pass(itemstack);
        } else if (!player.getProjectile(itemstack).isEmpty()) {
            startLoading(player, itemstack);
            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    public static void startLoading(Player player, ItemStack itemstack) {
        setLoading(itemstack, true);
        setLoadingTicks(itemstack, 0);
    }

    @Override
    public void inventoryTick(ItemStack itemstack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        handleTicking(itemstack, pLevel, pEntity);
        super.m_6883_(itemstack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        int i = getLoadingTicks(stack);
        handleTicking(stack, entity.f_19853_, entity);
        if (i != getLoadingTicks(stack)) {
            ItemStack cloneStack = stack.copy();
            entity.setItem(cloneStack);
        }
        return super.onEntityItemUpdate(stack, entity);
    }

    protected static void handleTicking(ItemStack itemStack, Level level, @NotNull Entity entity) {
        if (!level.isClientSide && isLoading(itemStack)) {
            int i = getLoadingTicks(itemStack);
            if (i > getChargeDuration(itemStack)) {
                setLoading(itemStack, false);
                if (entity instanceof LivingEntity livingEntity && !m_40932_(itemStack) && m_40859_(livingEntity, itemStack)) {
                    m_40884_(itemStack, true);
                }
                SoundSource soundsource = entity instanceof Player ? SoundSource.PLAYERS : SoundSource.BLOCKS;
                if (m_40932_(itemStack)) {
                    level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CROSSBOW_LOADING_END, soundsource, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
                } else {
                    level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_BREAK, soundsource, 1.0F, 1.7F);
                }
            }
            setLoadingTicks(itemStack, ++i);
        }
    }

    public static int getChargeDuration(ItemStack pCrossbowStack) {
        return CrossbowItem.getChargeDuration(pCrossbowStack) * 3;
    }

    public static boolean isLoading(ItemStack pCrossbowStack) {
        CompoundTag compoundtag = pCrossbowStack.getTag();
        return compoundtag != null && compoundtag.getBoolean("Loading");
    }

    public static void setLoading(ItemStack pCrossbowStack, boolean isLoading) {
        pCrossbowStack.getOrCreateTag().putBoolean("Loading", isLoading);
    }

    public static int getLoadingTicks(ItemStack pCrossbowStack) {
        CompoundTag compoundtag = pCrossbowStack.getTag();
        return compoundtag != null ? compoundtag.getInt("LoadingTimestamp") : 0;
    }

    public static void setLoadingTicks(ItemStack pCrossbowStack, int timestamp) {
        pCrossbowStack.getOrCreateTag().putInt("LoadingTimestamp", timestamp);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        TooltipsUtils.addShiftTooltip(pTooltip, List.of(Component.translatable("item.irons_spellbooks.autoloader_crossbow.desc").withStyle(ChatFormatting.YELLOW)));
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
}