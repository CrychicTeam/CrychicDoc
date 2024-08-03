package dev.xkmc.l2artifacts.content.search.token;

import javax.annotation.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IArtifactFeature {

    MutableComponent getDesc();

    @Nullable
    default NonNullList<ItemStack> getTooltipItems() {
        return null;
    }

    public interface ItemIcon extends IArtifactFeature {

        Item getItemIcon();
    }

    public interface Sprite extends IArtifactFeature {

        ResourceLocation getIcon();
    }
}