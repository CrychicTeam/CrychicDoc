package noppes.npcs.items;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;
import noppes.npcs.packets.client.PacketGuiOpen;

public class ItemNbtBook extends Item {

    public ItemNbtBook() {
        super(new Item.Properties().stacksTo(1));
    }

    public void blockEvent(PlayerInteractEvent.RightClickBlock event) {
        Packets.send((ServerPlayer) event.getEntity(), new PacketGuiOpen(EnumGuiType.NbtBook, event.getPos()));
        BlockState state = event.getLevel().getBlockState(event.getPos());
        CompoundTag data = new CompoundTag();
        BlockEntity tile = event.getLevel().getBlockEntity(event.getPos());
        if (tile != null) {
            tile.saveWithFullMetadata();
        }
        CompoundTag compound = new CompoundTag();
        compound.put("Data", data);
        Packets.send((ServerPlayer) event.getEntity(), new PacketGuiData(compound));
    }

    public void entityEvent(PlayerInteractEvent.EntityInteract event) {
        Packets.send((ServerPlayer) event.getEntity(), new PacketGuiOpen(EnumGuiType.NbtBook, BlockPos.ZERO));
        CompoundTag data = new CompoundTag();
        event.getTarget().saveAsPassenger(data);
        CompoundTag compound = new CompoundTag();
        compound.putInt("EntityId", event.getTarget().getId());
        compound.put("Data", data);
        Packets.send((ServerPlayer) event.getEntity(), new PacketGuiData(compound));
    }
}