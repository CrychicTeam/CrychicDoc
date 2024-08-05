package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EventHooks;
import noppes.npcs.api.event.ItemEvent;
import noppes.npcs.api.event.PlayerEvent;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerScriptData;
import noppes.npcs.items.ItemScripted;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketPlayerLeftClicked extends PacketServerBasic {

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketPlayerLeftClicked msg, FriendlyByteBuf buf) {
    }

    public static SPacketPlayerLeftClicked decode(FriendlyByteBuf buf) {
        return new SPacketPlayerLeftClicked();
    }

    @Override
    protected void handle() {
        if (CustomNpcs.EnableScripting && !ScriptController.Instance.languages.isEmpty()) {
            ItemStack item = this.player.m_21205_();
            PlayerScriptData handler = PlayerData.get(this.player).scriptData;
            PlayerEvent.AttackEvent ev = new PlayerEvent.AttackEvent(handler.getPlayer(), 0, null);
            EventHooks.onPlayerAttack(handler, ev);
            if (item.getItem() == CustomItems.scripted_item) {
                ItemScriptedWrapper isw = ItemScripted.GetWrapper(item);
                ItemEvent.AttackEvent eve = new ItemEvent.AttackEvent(isw, handler.getPlayer(), 0, null);
                EventHooks.onScriptItemAttack(isw, eve);
            }
        }
    }
}