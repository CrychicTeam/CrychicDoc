package org.violetmoon.quark.base.network.message.oddities;

import net.minecraft.server.level.ServerPlayer;
import org.violetmoon.quark.addons.oddities.inventory.CrateMenu;
import org.violetmoon.zeta.network.IZetaMessage;
import org.violetmoon.zeta.network.IZetaNetworkEventContext;

public class ScrollCrateMessage implements IZetaMessage {

    private static final long serialVersionUID = -921358009630134620L;

    public boolean down;

    public ScrollCrateMessage() {
    }

    public ScrollCrateMessage(boolean down) {
        this.down = down;
    }

    @Override
    public boolean receive(IZetaNetworkEventContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player.f_36096_ instanceof CrateMenu crate) {
                crate.scroll(this.down, false);
            }
        });
        return true;
    }
}