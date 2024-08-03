package org.violetmoon.quark.base.network.message.oddities;

import net.minecraft.server.level.ServerPlayer;
import org.violetmoon.quark.addons.oddities.block.be.MatrixEnchantingTableBlockEntity;
import org.violetmoon.quark.addons.oddities.inventory.MatrixEnchantingMenu;
import org.violetmoon.zeta.network.IZetaMessage;
import org.violetmoon.zeta.network.IZetaNetworkEventContext;

public class MatrixEnchanterOperationMessage implements IZetaMessage {

    private static final long serialVersionUID = 2272401655489445173L;

    public int operation;

    public int arg0;

    public int arg1;

    public int arg2;

    public MatrixEnchanterOperationMessage() {
    }

    public MatrixEnchanterOperationMessage(int operation, int arg0, int arg1, int arg2) {
        this.operation = operation;
        this.arg0 = arg0;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public boolean receive(IZetaNetworkEventContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player.f_36096_ instanceof MatrixEnchantingMenu matrixMenu) {
                MatrixEnchantingTableBlockEntity enchanter = matrixMenu.enchanter;
                enchanter.onOperation(player, this.operation, this.arg0, this.arg1, this.arg2);
            }
        });
        return true;
    }
}