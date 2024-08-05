package me.jellysquid.mods.sodium.client.platform.windows.api.msgbox;

import org.lwjgl.system.Callback;

public abstract class MsgBoxCallback extends Callback implements MsgBoxCallbackI {

    public static MsgBoxCallback create(MsgBoxCallbackI instance) {
        return (MsgBoxCallback) (instance instanceof MsgBoxCallback ? (MsgBoxCallback) instance : new MsgBoxCallback.Container(instance.address(), instance));
    }

    private MsgBoxCallback(long functionPointer) {
        super(functionPointer);
    }

    private static final class Container extends MsgBoxCallback {

        private final MsgBoxCallbackI delegate;

        Container(long functionPointer, MsgBoxCallbackI delegate) {
            super(functionPointer);
            this.delegate = delegate;
        }

        @Override
        public void invoke(long lpHelpInfo) {
            this.delegate.invoke(lpHelpInfo);
        }
    }
}