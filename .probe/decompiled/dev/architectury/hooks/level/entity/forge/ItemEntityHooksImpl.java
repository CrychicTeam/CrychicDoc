package dev.architectury.hooks.level.entity.forge;

import dev.architectury.utils.value.IntValue;
import net.minecraft.world.entity.item.ItemEntity;

public class ItemEntityHooksImpl {

    public static IntValue lifespan(ItemEntity entity) {
        return new IntValue() {

            public void accept(int value) {
                entity.lifespan = value;
            }

            public int getAsInt() {
                return entity.lifespan;
            }
        };
    }
}