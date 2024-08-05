package dev.xkmc.l2artifacts.content.upgrades;

import dev.xkmc.l2artifacts.content.config.StatTypeConfig;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class StatContainerItem extends UpgradeEnhanceItem {

    private static final String KEY = "stat";

    public static ItemStack setStat(ItemStack item, ResourceLocation type) {
        item.getOrCreateTag().putString("stat", type.toString());
        return item;
    }

    public static Optional<ResourceLocation> getType(ItemStack item) {
        if (item.isEmpty()) {
            return Optional.empty();
        } else {
            ItemCompoundTag tag = ItemCompoundTag.of(item);
            if (!tag.isPresent()) {
                return Optional.empty();
            } else if (!tag.getOrCreate().contains("stat", 8)) {
                return Optional.empty();
            } else {
                String str = tag.getOrCreate().getString("stat");
                return str.isEmpty() ? Optional.empty() : Optional.of(new ResourceLocation(str));
            }
        }
    }

    public StatContainerItem(Item.Properties props, int rank) {
        super(props, rank);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        getType(stack).ifPresentOrElse(e -> {
            list.add(LangData.STAT_INFO.get(StatTypeConfig.get(e).getDesc().withStyle(ChatFormatting.BLUE)).withStyle(ChatFormatting.DARK_GREEN));
            list.add(LangData.STAT_USE_INFO.get().withStyle(ChatFormatting.GRAY));
        }, () -> list.add(LangData.STAT_CAPTURE_INFO.get().withStyle(ChatFormatting.GRAY)));
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return getType(pStack).isPresent();
    }
}