package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EventHooks;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketPlayerKeyPressed extends PacketServerBasic {

    private final int button;

    private final boolean ctrlDown;

    private final boolean shiftDown;

    private final boolean altDown;

    private final boolean metaDown;

    private final boolean released;

    private final String openGui;

    public SPacketPlayerKeyPressed(int button, boolean ctrlDown, boolean shiftDown, boolean altDown, boolean metaDown, boolean released, String openGui) {
        this.button = button;
        this.ctrlDown = ctrlDown;
        this.shiftDown = shiftDown;
        this.altDown = altDown;
        this.metaDown = metaDown;
        this.released = released;
        this.openGui = openGui;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketPlayerKeyPressed msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.button);
        buf.writeBoolean(msg.ctrlDown);
        buf.writeBoolean(msg.shiftDown);
        buf.writeBoolean(msg.altDown);
        buf.writeBoolean(msg.metaDown);
        buf.writeBoolean(msg.released);
        buf.writeUtf(msg.openGui == null ? "" : msg.openGui);
    }

    public static SPacketPlayerKeyPressed decode(FriendlyByteBuf buf) {
        return new SPacketPlayerKeyPressed(buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readUtf(32767));
    }

    @Override
    protected void handle() {
        if (CustomNpcs.EnableScripting && !ScriptController.Instance.languages.isEmpty()) {
            EventHooks.onPlayerKeyEvent(this.player, this.button, this.ctrlDown, this.shiftDown, this.altDown, this.metaDown, this.released, this.openGui);
        }
    }
}