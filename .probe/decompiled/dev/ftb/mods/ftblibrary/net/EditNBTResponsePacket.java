package dev.ftb.mods.ftblibrary.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.ftb.mods.ftblibrary.FTBLibraryCommands;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EditNBTResponsePacket extends BaseC2SMessage {

    private final CompoundTag info;

    private final CompoundTag tag;

    public EditNBTResponsePacket(FriendlyByteBuf buf) {
        this.info = buf.readNbt();
        this.tag = buf.readAnySizeNbt();
    }

    public EditNBTResponsePacket(CompoundTag i, CompoundTag t) {
        this.info = i;
        this.tag = t;
    }

    @Override
    public MessageType getType() {
        return FTBLibraryNet.EDIT_NBT_RESPONSE;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(this.info);
        buf.writeNbt(this.tag);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        ServerPlayer player = (ServerPlayer) context.getPlayer();
        if (this.info.equals(FTBLibraryCommands.EDITING_NBT.remove(player.m_20148_()))) {
            String var3 = this.info.getString("type");
            switch(var3) {
                case "block":
                    BlockPos pos = new BlockPos(this.info.getInt("x"), this.info.getInt("y"), this.info.getInt("z"));
                    if (player.m_9236_().isLoaded(pos)) {
                        BlockEntity blockEntity = player.m_9236_().getBlockEntity(pos);
                        if (blockEntity != null) {
                            this.tag.putInt("x", pos.m_123341_());
                            this.tag.putInt("y", pos.m_123342_());
                            this.tag.putInt("z", pos.m_123343_());
                            this.tag.putString("id", this.info.getString("id"));
                            blockEntity.load(this.tag);
                            blockEntity.setChanged();
                            player.m_9236_().sendBlockUpdated(pos, blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                        }
                    }
                    break;
                case "entity":
                    Entity entity = player.m_9236_().getEntity(this.info.getInt("id"));
                    if (entity != null) {
                        UUID uUID = entity.getUUID();
                        entity.load(this.tag);
                        entity.setUUID(uUID);
                    }
                    break;
                case "player":
                    ServerPlayer player1 = player.m_9236_().getServer().getPlayerList().getPlayer(this.info.getUUID("id"));
                    if (player1 != null) {
                        UUID uUID = player1.m_20148_();
                        player1.m_20258_(this.tag);
                        player1.m_20084_(uUID);
                        player1.moveTo(player1.m_20185_(), player1.m_20186_(), player1.m_20189_());
                    }
                    break;
                case "item":
                    player.m_21008_(InteractionHand.MAIN_HAND, ItemStack.of(this.tag));
            }
        }
    }
}