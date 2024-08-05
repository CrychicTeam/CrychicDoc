package net.minecraft.world.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.apache.commons.lang3.Validate;

public class BannerItem extends StandingAndWallBlockItem {

    private static final String PATTERN_PREFIX = "block.minecraft.banner.";

    public BannerItem(Block block0, Block block1, Item.Properties itemProperties2) {
        super(block0, block1, itemProperties2, Direction.DOWN);
        Validate.isInstanceOf(AbstractBannerBlock.class, block0);
        Validate.isInstanceOf(AbstractBannerBlock.class, block1);
    }

    public static void appendHoverTextFromBannerBlockEntityTag(ItemStack itemStack0, List<Component> listComponent1) {
        CompoundTag $$2 = BlockItem.getBlockEntityData(itemStack0);
        if ($$2 != null && $$2.contains("Patterns")) {
            ListTag $$3 = $$2.getList("Patterns", 10);
            for (int $$4 = 0; $$4 < $$3.size() && $$4 < 6; $$4++) {
                CompoundTag $$5 = $$3.getCompound($$4);
                DyeColor $$6 = DyeColor.byId($$5.getInt("Color"));
                Holder<BannerPattern> $$7 = BannerPattern.byHash($$5.getString("Pattern"));
                if ($$7 != null) {
                    $$7.unwrapKey().map(p_220002_ -> p_220002_.location().toShortLanguageKey()).ifPresent(p_220006_ -> listComponent1.add(Component.translatable("block.minecraft.banner." + p_220006_ + "." + $$6.getName()).withStyle(ChatFormatting.GRAY)));
                }
            }
        }
    }

    public DyeColor getColor() {
        return ((AbstractBannerBlock) this.m_40614_()).getColor();
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        appendHoverTextFromBannerBlockEntityTag(itemStack0, listComponent2);
    }
}