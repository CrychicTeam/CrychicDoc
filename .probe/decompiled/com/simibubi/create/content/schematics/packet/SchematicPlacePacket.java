package com.simibubi.create.content.schematics.packet;

import com.simibubi.create.content.schematics.SchematicPrinter;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class SchematicPlacePacket extends SimplePacketBase {

    public ItemStack stack;

    public SchematicPlacePacket(ItemStack stack) {
        this.stack = stack;
    }

    public SchematicPlacePacket(FriendlyByteBuf buffer) {
        this.stack = buffer.readItem();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(this.stack);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                if (player.isCreative()) {
                    Level world = player.m_9236_();
                    SchematicPrinter printer = new SchematicPrinter();
                    printer.loadSchematic(this.stack, world, !player.m_36337_());
                    if (printer.isLoaded() && !printer.isErrored()) {
                        boolean includeAir = AllConfigs.server().schematics.creativePrintIncludesAir.get();
                        while (printer.advanceCurrentPos()) {
                            if (printer.shouldPlaceCurrent(world)) {
                                printer.handleCurrentTarget((pos, state, blockEntity) -> {
                                    boolean placingAir = state.m_60795_();
                                    if (!placingAir || includeAir) {
                                        CompoundTag data = BlockHelper.prepareBlockEntityData(state, blockEntity);
                                        BlockHelper.placeSchematicBlock(world, state, pos, null, data);
                                    }
                                }, (pos, entity) -> world.m_7967_(entity));
                            }
                        }
                    }
                }
            }
        });
        return true;
    }
}