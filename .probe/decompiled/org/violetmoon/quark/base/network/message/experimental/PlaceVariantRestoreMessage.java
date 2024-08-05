package org.violetmoon.quark.base.network.message.experimental;

import org.violetmoon.quark.content.experimental.module.VariantSelectorModule;
import org.violetmoon.zeta.network.IZetaMessage;
import org.violetmoon.zeta.network.IZetaNetworkEventContext;

public class PlaceVariantRestoreMessage implements IZetaMessage {

    private static final long serialVersionUID = -5122685825175210844L;

    public String variant;

    public PlaceVariantRestoreMessage() {
    }

    public PlaceVariantRestoreMessage(String variant) {
        this.variant = variant;
    }

    @Override
    public boolean receive(IZetaNetworkEventContext context) {
        context.enqueueWork(() -> VariantSelectorModule.Client.setClientVariant(this.variant, false));
        return true;
    }
}