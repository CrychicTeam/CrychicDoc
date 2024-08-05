package com.simibubi.create.content.equipment.toolbox;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class ToolboxDisposeAllPacket extends SimplePacketBase {

    private BlockPos toolboxPos;

    public ToolboxDisposeAllPacket(BlockPos toolboxPos) {
        this.toolboxPos = toolboxPos;
    }

    public ToolboxDisposeAllPacket(FriendlyByteBuf buffer) {
        this.toolboxPos = buffer.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.toolboxPos);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Level world = player.m_9236_();
            BlockEntity blockEntity = world.getBlockEntity(this.toolboxPos);
            double maxRange = ToolboxHandler.getMaxRange(player);
            if (!(player.m_20275_((double) this.toolboxPos.m_123341_() + 0.5, (double) this.toolboxPos.m_123342_(), (double) this.toolboxPos.m_123343_() + 0.5) > maxRange * maxRange)) {
                if (blockEntity instanceof ToolboxBlockEntity toolbox) {
                    CompoundTag compound = player.getPersistentData().getCompound("CreateToolboxData");
                    MutableBoolean sendData = new MutableBoolean(false);
                    toolbox.inventory.inLimitedMode(inventory -> {
                        for (int i = 0; i < 36; i++) {
                            String key = String.valueOf(i);
                            if (compound.contains(key) && NbtUtils.readBlockPos(compound.getCompound(key).getCompound("Pos")).equals(this.toolboxPos)) {
                                ToolboxHandler.unequip(player, i, true);
                                sendData.setTrue();
                            }
                            ItemStack itemStack = player.m_150109_().getItem(i);
                            ItemStack remainder = ItemHandlerHelper.insertItemStacked(toolbox.inventory, itemStack, false);
                            if (remainder.getCount() != itemStack.getCount()) {
                                player.m_150109_().setItem(i, remainder);
                            }
                        }
                    });
                    if (sendData.booleanValue()) {
                        ToolboxHandler.syncData(player);
                    }
                }
            }
        });
        return true;
    }
}