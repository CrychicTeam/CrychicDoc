package io.github.apace100.origins.util;

import net.minecraft.world.inventory.MenuType;

public enum InventoryType {

    THREE_BY_THREE(MenuType.GENERIC_3x3),
    NINE_BY_ONE(MenuType.GENERIC_9x1),
    NINE_BY_TWO(MenuType.GENERIC_9x2),
    NINE_BY_THREE(MenuType.GENERIC_9x3),
    NINE_BY_FOUR(MenuType.GENERIC_9x4),
    NINE_BY_FIVE(MenuType.GENERIC_9x5),
    NINE_BY_SIX(MenuType.GENERIC_9x6);

    private final MenuType<?> type;

    private InventoryType(MenuType<?> type) {
        this.type = type;
    }

    public MenuType<?> getType() {
        return this.type;
    }
}