package net.mehvahdjukaar.supplementaries.mixins;

import java.util.Optional;
import net.mehvahdjukaar.supplementaries.common.items.tooltip_components.BannerPatternTooltip;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ BannerPatternItem.class })
public abstract class BannerPatternItemMixin extends Item {

    @Shadow
    @Final
    private TagKey<BannerPattern> bannerPattern;

    protected BannerPatternItemMixin(Item.Properties properties) {
        super(properties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return ClientConfigs.Tweaks.BANNER_PATTERN_TOOLTIP.get() ? Optional.of(new BannerPatternTooltip(this.bannerPattern)) : Optional.empty();
    }
}