package dev.architectury.hooks.level.entity;

import dev.architectury.hooks.level.entity.forge.ItemEntityHooksImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.utils.value.IntValue;
import net.minecraft.world.entity.item.ItemEntity;

public final class ItemEntityHooks {

    private ItemEntityHooks() {
    }

    @ExpectPlatform
    @Transformed
    public static IntValue lifespan(ItemEntity entity) {
        return ItemEntityHooksImpl.lifespan(entity);
    }
}