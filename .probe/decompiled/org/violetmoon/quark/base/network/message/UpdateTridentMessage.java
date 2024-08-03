package org.violetmoon.quark.base.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.violetmoon.zeta.network.IZetaMessage;
import org.violetmoon.zeta.network.IZetaNetworkEventContext;

public class UpdateTridentMessage implements IZetaMessage {

    private static final long serialVersionUID = -4716987873031723456L;

    public int tridentID;

    public ItemStack stack;

    public UpdateTridentMessage() {
    }

    public UpdateTridentMessage(int trident, ItemStack stack) {
        this.tridentID = trident;
        this.stack = stack;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean receive(IZetaNetworkEventContext context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null && level.getEntity(this.tridentID) instanceof ThrownTrident trident) {
                trident.tridentItem = this.stack;
            }
        });
        return true;
    }
}