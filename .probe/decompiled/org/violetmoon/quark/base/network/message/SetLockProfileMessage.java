package org.violetmoon.quark.base.network.message;

import org.violetmoon.quark.content.tweaks.module.LockRotationModule;
import org.violetmoon.zeta.network.IZetaMessage;
import org.violetmoon.zeta.network.IZetaNetworkEventContext;

public class SetLockProfileMessage implements IZetaMessage {

    private static final long serialVersionUID = 1037317801540162515L;

    public LockRotationModule.LockProfile profile;

    public SetLockProfileMessage() {
    }

    public SetLockProfileMessage(LockRotationModule.LockProfile profile) {
        this.profile = profile;
    }

    @Override
    public boolean receive(IZetaNetworkEventContext context) {
        context.enqueueWork(() -> LockRotationModule.setProfile(context.getSender(), this.profile));
        return true;
    }
}