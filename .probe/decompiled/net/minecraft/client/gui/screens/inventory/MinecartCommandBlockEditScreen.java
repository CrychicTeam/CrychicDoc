package net.minecraft.client.gui.screens.inventory;

import net.minecraft.network.protocol.game.ServerboundSetCommandMinecartPacket;
import net.minecraft.world.entity.vehicle.MinecartCommandBlock;
import net.minecraft.world.level.BaseCommandBlock;

public class MinecartCommandBlockEditScreen extends AbstractCommandBlockEditScreen {

    private final BaseCommandBlock commandBlock;

    public MinecartCommandBlockEditScreen(BaseCommandBlock baseCommandBlock0) {
        this.commandBlock = baseCommandBlock0;
    }

    @Override
    public BaseCommandBlock getCommandBlock() {
        return this.commandBlock;
    }

    @Override
    int getPreviousY() {
        return 150;
    }

    @Override
    protected void init() {
        super.init();
        this.f_97646_.setValue(this.getCommandBlock().getCommand());
    }

    @Override
    protected void populateAndSendPacket(BaseCommandBlock baseCommandBlock0) {
        if (baseCommandBlock0 instanceof MinecartCommandBlock.MinecartCommandBase $$1) {
            this.f_96541_.getConnection().send(new ServerboundSetCommandMinecartPacket($$1.getMinecart().m_19879_(), this.f_97646_.getValue(), baseCommandBlock0.isTrackOutput()));
        }
    }
}