package com.simibubi.create.content.schematics.cannon;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ConfigureSchematicannonPacket extends SimplePacketBase {

    private ConfigureSchematicannonPacket.Option option;

    private boolean set;

    public ConfigureSchematicannonPacket(ConfigureSchematicannonPacket.Option option, boolean set) {
        this.option = option;
        this.set = set;
    }

    public ConfigureSchematicannonPacket(FriendlyByteBuf buffer) {
        this(buffer.readEnum(ConfigureSchematicannonPacket.Option.class), buffer.readBoolean());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.option);
        buffer.writeBoolean(this.set);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null && player.f_36096_ instanceof SchematicannonMenu) {
                SchematicannonBlockEntity be = ((SchematicannonMenu) player.f_36096_).contentHolder;
                switch(this.option) {
                    case DONT_REPLACE:
                    case REPLACE_ANY:
                    case REPLACE_EMPTY:
                    case REPLACE_SOLID:
                        be.replaceMode = this.option.ordinal();
                        break;
                    case SKIP_MISSING:
                        be.skipMissing = this.set;
                        break;
                    case SKIP_BLOCK_ENTITIES:
                        be.replaceBlockEntities = this.set;
                        break;
                    case PLAY:
                        be.state = SchematicannonBlockEntity.State.RUNNING;
                        be.statusMsg = "running";
                        break;
                    case PAUSE:
                        be.state = SchematicannonBlockEntity.State.PAUSED;
                        be.statusMsg = "paused";
                        break;
                    case STOP:
                        be.state = SchematicannonBlockEntity.State.STOPPED;
                        be.statusMsg = "stopped";
                }
                be.sendUpdate = true;
            }
        });
        return true;
    }

    public static enum Option {

        DONT_REPLACE,
        REPLACE_SOLID,
        REPLACE_ANY,
        REPLACE_EMPTY,
        SKIP_MISSING,
        SKIP_BLOCK_ENTITIES,
        PLAY,
        PAUSE,
        STOP
    }
}