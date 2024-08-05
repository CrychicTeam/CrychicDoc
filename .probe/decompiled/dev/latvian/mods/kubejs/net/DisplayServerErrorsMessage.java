package dev.latvian.mods.kubejs.net;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.script.ConsoleLine;
import dev.latvian.mods.kubejs.script.ScriptType;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;

public class DisplayServerErrorsMessage extends BaseS2CMessage {

    private final ScriptType type;

    private final List<ConsoleLine> errors;

    private final List<ConsoleLine> warnings;

    public DisplayServerErrorsMessage(ScriptType type, List<ConsoleLine> errors, List<ConsoleLine> warnings) {
        this.type = type;
        this.errors = errors;
        this.warnings = warnings;
    }

    DisplayServerErrorsMessage(FriendlyByteBuf buf) {
        this.type = ScriptType.values()[buf.readByte()];
        this.errors = buf.readList(ConsoleLine::new);
        this.warnings = buf.readList(ConsoleLine::new);
    }

    @Override
    public MessageType getType() {
        return KubeJSNet.DISPLAY_SERVER_ERRORS;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeByte(this.type.ordinal());
        buf.writeCollection(this.errors, ConsoleLine::writeToNet);
        buf.writeCollection(this.warnings, ConsoleLine::writeToNet);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        KubeJS.PROXY.openErrors(this.type, this.errors, this.warnings);
    }
}