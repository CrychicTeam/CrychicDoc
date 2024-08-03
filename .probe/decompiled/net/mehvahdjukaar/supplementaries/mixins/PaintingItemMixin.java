package net.mehvahdjukaar.supplementaries.mixins;

import java.util.Optional;
import net.mehvahdjukaar.supplementaries.common.items.tooltip_components.PaintingTooltip;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ HangingEntityItem.class })
public abstract class PaintingItemMixin extends Item {

    @Shadow
    @Final
    private EntityType<? extends HangingEntity> type;

    protected PaintingItemMixin(Item.Properties properties) {
        super(properties);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if ((Boolean) ClientConfigs.Tweaks.PAINTINGS_TOOLTIPS.get() && this.type == EntityType.PAINTING) {
            CompoundTag tag = stack.getTag();
            if (tag != null && tag.contains("EntityTag")) {
                ResourceLocation v = ResourceLocation.tryParse(tag.getCompound("EntityTag").getString("variant"));
                if (v != null) {
                    Optional<PaintingVariant> variant = BuiltInRegistries.PAINTING_VARIANT.m_6612_(v);
                    return variant.map(PaintingTooltip::new);
                }
            }
        }
        return Optional.empty();
    }
}