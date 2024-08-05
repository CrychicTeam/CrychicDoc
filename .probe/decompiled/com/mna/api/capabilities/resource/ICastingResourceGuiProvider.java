package com.mna.api.capabilities.resource;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface ICastingResourceGuiProvider {

    ResourceLocation getTexture();

    int getFrameU();

    int getFrameV();

    int getFrameWidth();

    int getFrameHeight();

    int getBadgeSize();

    default ItemStack getBadgeItem() {
        return ItemStack.EMPTY;
    }

    default int getBadgeItemOffsetX() {
        return 24;
    }

    default int getBadgeItemOffsetY() {
        return 8;
    }

    int getBarColor();

    int getBarManaCostEstimateColor();

    int getResourceNumericTextColor();

    int getXPBarColor();

    default int getFillStartX() {
        return 19;
    }

    default int getFillStartY() {
        return 6;
    }

    default int getFillWidth() {
        return 128;
    }

    default int getFillHeight() {
        return this.getFrameHeight() - 12;
    }

    default int getXPBarOffsetX() {
        return 18;
    }

    default int getXPBarOffsetY() {
        return 15;
    }

    default int getXPBarHeight() {
        return 2;
    }

    default int getResourceNumericOffsetX() {
        return 19;
    }

    default int getResourceNumericOffsetY() {
        return 8;
    }

    default int getLevelDisplayX() {
        return 1;
    }

    default int getLevelDisplayY() {
        return this.getFrameHeight() - 4;
    }

    default int getLowChargeOffsetY() {
        return -3;
    }
}