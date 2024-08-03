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

public class ToolboxEquipPacket extends SimplePacketBase {

    private BlockPos toolboxPos;

    private int slot;

    private int hotbarSlot;

    public ToolboxEquipPacket(BlockPos toolboxPos, int slot, int hotbarSlot) {
        this.toolboxPos = toolboxPos;
        this.slot = slot;
        this.hotbarSlot = hotbarSlot;
    }

    public ToolboxEquipPacket(FriendlyByteBuf buffer) {
        if (buffer.readBoolean()) {
            this.toolboxPos = buffer.readBlockPos();
        }
        this.slot = buffer.readVarInt();
        this.hotbarSlot = buffer.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.toolboxPos != null);
        if (this.toolboxPos != null) {
            buffer.writeBlockPos(this.toolboxPos);
        }
        buffer.writeVarInt(this.slot);
        buffer.writeVarInt(this.hotbarSlot);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            Level world = player.m_9236_();
            if (this.toolboxPos == null) {
                ToolboxHandler.unequip(player, this.hotbarSlot, false);
                ToolboxHandler.syncData(player);
            } else {
                BlockEntity blockEntity = world.getBlockEntity(this.toolboxPos);
                double maxRange = ToolboxHandler.getMaxRange(player);
                if (!(player.m_20275_((double) this.toolboxPos.m_123341_() + 0.5, (double) this.toolboxPos.m_123342_(), (double) this.toolboxPos.m_123343_() + 0.5) > maxRange * maxRange)) {
                    if (blockEntity instanceof ToolboxBlockEntity) {
                        ToolboxHandler.unequip(player, this.hotbarSlot, false);
                        if (this.slot >= 0 && this.slot < 8) {
                            ToolboxBlockEntity toolboxBlockEntity = (ToolboxBlockEntity) blockEntity;
                            ItemStack playerStack = player.m_150109_().getItem(this.hotbarSlot);
                            if (!playerStack.isEmpty() && !ToolboxInventory.canItemsShareCompartment(playerStack, (ItemStack) toolboxBlockEntity.inventory.filters.get(this.slot))) {
                                toolboxBlockEntity.inventory.inLimitedMode(inventory -> {
                                    ItemStack remainder = ItemHandlerHelper.insertItemStacked(inventory, playerStack, false);
                                    if (!remainder.isEmpty()) {
                                        remainder = ItemHandlerHelper.insertItemStacked(new ItemReturnInvWrapper(player.m_150109_()), remainder, false);
                                    }
                                    if (remainder.getCount() != playerStack.getCount()) {
                                        player.m_150109_().setItem(this.hotbarSlot, remainder);
                                    }
                                });
                            }
                            CompoundTag compound = player.getPersistentData().getCompound("CreateToolboxData");
                            String key = String.valueOf(this.hotbarSlot);
                            CompoundTag data = new CompoundTag();
                            data.putInt("Slot", this.slot);
                            data.put("Pos", NbtUtils.writeBlockPos(this.toolboxPos));
                            compound.put(key, data);
                            player.getPersistentData().put("CreateToolboxData", compound);
                            toolboxBlockEntity.connectPlayer(this.slot, player, this.hotbarSlot);
                            ToolboxHandler.syncData(player);
                        } else {
                            ToolboxHandler.syncData(player);
                        }
                    }
                }
            }
        });
        return true;
    }
}