package org.violetmoon.zeta.network;

import java.util.function.IntSupplier;

public abstract class ZetaHandshakeMessage implements IntSupplier, IZetaMessage {

    private transient int loginIndex;

    public void setLoginIndex(int loginIndex) {
        this.loginIndex = loginIndex;
    }

    public int getLoginIndex() {
        return this.loginIndex;
    }

    public int getAsInt() {
        return this.loginIndex;
    }
}