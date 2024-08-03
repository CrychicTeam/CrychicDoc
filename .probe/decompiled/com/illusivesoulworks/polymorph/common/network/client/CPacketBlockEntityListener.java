package com.illusivesoulworks.polymorph.common.network.client;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.common.util.BlockEntityTicker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record CPacketBlockEntityListener(boolean add) {

    public static void encode(CPacketBlockEntityListener packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.add);
    }

    public static CPacketBlockEntityListener decode(FriendlyByteBuf buffer) {
        return new CPacketBlockEntityListener(buffer.readBoolean());
    }

    public static void handle(CPacketBlockEntityListener packet, ServerPlayer player) {
        if (player != null) {
            if (packet.add) {
                AbstractContainerMenu container = player.f_36096_;
                PolymorphApi.common().getRecipeDataFromBlockEntity(container).ifPresent(recipeData -> BlockEntityTicker.add(player, recipeData));
            } else {
                BlockEntityTicker.remove(player);
            }
        }
    }
}