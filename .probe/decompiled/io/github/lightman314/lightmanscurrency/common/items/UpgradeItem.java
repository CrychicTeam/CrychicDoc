package io.github.lightman314.lightmanscurrency.common.items;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.upgrades.IUpgradeItem;
import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeData;
import io.github.lightman314.lightmanscurrency.api.upgrades.UpgradeType;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public abstract class UpgradeItem extends Item implements IUpgradeItem {

    protected final UpgradeType upgradeType;

    private boolean addTooltips = true;

    Function<UpgradeData, List<Component>> customTooltips = null;

    public UpgradeItem(UpgradeType upgradeType, Item.Properties properties) {
        super(properties);
        this.upgradeType = upgradeType;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (stack.hasTag() && player.m_6047_()) {
            CompoundTag tag = stack.getTag();
            boolean success = this.upgradeType.clearDataFromStack(tag);
            if (tag.contains("UpgradeData")) {
                tag.remove("UpgradeData");
                success = true;
            }
            if (success) {
                level.playLocalSound(player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.1F, (level.random.nextFloat() - level.random.nextFloat()) * 0.35F + 0.9F, false);
                return InteractionResultHolder.success(stack);
            }
        }
        return super.use(level, player, hand);
    }

    public final boolean addsTooltips() {
        return this.addTooltips;
    }

    protected final void ignoreTooltips() {
        this.addTooltips = false;
    }

    protected final void setCustomTooltips(Function<UpgradeData, List<Component>> customTooltips) {
        this.customTooltips = customTooltips;
    }

    @Nonnull
    @Override
    public UpgradeType getUpgradeType() {
        return this.upgradeType;
    }

    @Nonnull
    @Override
    public UpgradeData getDefaultUpgradeData() {
        UpgradeData data = this.upgradeType.getDefaultData();
        this.fillUpgradeData(data);
        return data;
    }

    protected abstract void fillUpgradeData(UpgradeData var1);

    public static UpgradeData getUpgradeData(ItemStack stack) {
        if (stack.getItem() instanceof UpgradeItem) {
            UpgradeData data = ((UpgradeItem) stack.getItem()).getDefaultUpgradeData();
            if (stack.hasTag()) {
                CompoundTag tag = stack.getTag();
                if (tag.contains("UpgradeData", 10)) {
                    data.read(tag.getCompound("UpgradeData"));
                }
            }
            return data;
        } else {
            return UpgradeData.EMPTY;
        }
    }

    public static void setUpgradeData(ItemStack stack, UpgradeData data) {
        if (stack.getItem() instanceof UpgradeItem upgradeItem) {
            CompoundTag tag = stack.getOrCreateTag();
            tag.put("UpgradeData", data.writeToNBT(upgradeItem.upgradeType));
        } else {
            CompoundTag tag = stack.getOrCreateTag();
            tag.put("UpgradeData", data.writeToNBT());
        }
    }

    public static List<Component> getUpgradeTooltip(ItemStack stack) {
        try {
            return getUpgradeTooltip(stack, false);
        } catch (Throwable var2) {
            return new ArrayList();
        }
    }

    public static List<Component> getUpgradeTooltip(ItemStack stack, boolean forceCollection) {
        if (!(stack.getItem() instanceof UpgradeItem item)) {
            return Lists.newArrayList();
        } else if (!item.addTooltips && !forceCollection) {
            return Lists.newArrayList();
        } else {
            UpgradeType type = item.getUpgradeType();
            UpgradeData data = getUpgradeData(stack);
            if (item.customTooltips != null) {
                return (List<Component>) item.customTooltips.apply(data);
            } else {
                List<Component> tooltip = new ArrayList(type.getTooltip(data));
                List<Component> targets = type.getPossibleTargets();
                if (!targets.isEmpty()) {
                    tooltip.add(LCText.TOOLTIP_UPGRADE_TARGETS.getWithStyle(ChatFormatting.GRAY));
                    for (Component target : targets) {
                        MutableComponent mc = EasyText.makeMutable(target);
                        tooltip.add(mc.withStyle(ChatFormatting.GRAY));
                    }
                }
                return tooltip;
            }
        }
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
        List<Component> upgradeTooltips = getUpgradeTooltip(stack);
        if (upgradeTooltips != null) {
            tooltip.addAll(upgradeTooltips);
        }
        super.appendHoverText(stack, level, tooltip, flagIn);
    }

    public static class Simple extends UpgradeItem {

        public Simple(UpgradeType upgradeType, Item.Properties properties) {
            super(upgradeType, properties);
        }

        @Override
        protected void fillUpgradeData(UpgradeData data) {
        }
    }
}