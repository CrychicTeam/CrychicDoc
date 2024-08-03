package com.simibubi.create.content.schematics.packet;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.schematics.SchematicInstances;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraftforge.network.NetworkEvent;

public class SchematicSyncPacket extends SimplePacketBase {

    public int slot;

    public boolean deployed;

    public BlockPos anchor;

    public Rotation rotation;

    public Mirror mirror;

    public SchematicSyncPacket(int slot, StructurePlaceSettings settings, BlockPos anchor, boolean deployed) {
        this.slot = slot;
        this.deployed = deployed;
        this.anchor = anchor;
        this.rotation = settings.getRotation();
        this.mirror = settings.getMirror();
    }

    public SchematicSyncPacket(FriendlyByteBuf buffer) {
        this.slot = buffer.readVarInt();
        this.deployed = buffer.readBoolean();
        this.anchor = buffer.readBlockPos();
        this.rotation = buffer.readEnum(Rotation.class);
        this.mirror = buffer.readEnum(Mirror.class);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.slot);
        buffer.writeBoolean(this.deployed);
        buffer.writeBlockPos(this.anchor);
        buffer.writeEnum(this.rotation);
        buffer.writeEnum(this.mirror);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ItemStack stack = ItemStack.EMPTY;
                if (this.slot == -1) {
                    stack = player.m_21205_();
                } else {
                    stack = player.m_150109_().getItem(this.slot);
                }
                if (AllItems.SCHEMATIC.isIn(stack)) {
                    CompoundTag tag = stack.getOrCreateTag();
                    tag.putBoolean("Deployed", this.deployed);
                    tag.put("Anchor", NbtUtils.writeBlockPos(this.anchor));
                    tag.putString("Rotation", this.rotation.name());
                    tag.putString("Mirror", this.mirror.name());
                    SchematicInstances.clearHash(stack);
                }
            }
        });
        return true;
    }
}