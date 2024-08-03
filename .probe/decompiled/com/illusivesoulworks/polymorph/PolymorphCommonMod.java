package com.illusivesoulworks.polymorph;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.common.base.IPolymorphCommon;
import com.illusivesoulworks.polymorph.client.impl.PolymorphClient;
import com.illusivesoulworks.polymorph.common.capability.FurnaceRecipeData;
import com.illusivesoulworks.polymorph.common.integration.PolymorphIntegrations;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PolymorphCommonMod {

    public static void init() {
        PolymorphIntegrations.init();
    }

    public static void setup() {
        IPolymorphCommon commonApi = PolymorphApi.common();
        commonApi.registerBlockEntity2RecipeData(blockEntity -> blockEntity instanceof AbstractFurnaceBlockEntity ? new FurnaceRecipeData((AbstractFurnaceBlockEntity) blockEntity) : null);
        commonApi.registerContainer2BlockEntity(container -> {
            for (Slot inventorySlot : container.slots) {
                Container inventory = inventorySlot.container;
                if (inventory instanceof BlockEntity) {
                    return (BlockEntity) inventory;
                }
            }
            return null;
        });
        PolymorphIntegrations.setup();
    }

    public static void clientSetup() {
        PolymorphClient.setup();
        PolymorphIntegrations.clientSetup();
    }
}