package me.jellysquid.mods.sodium.client.platform.windows.api.msgbox;

import java.nio.ByteBuffer;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Pointer;
import org.lwjgl.system.Struct;
import org.lwjgl.system.Struct.Layout;
import org.lwjgl.system.Struct.Member;

public class MsgBoxParamSw extends Struct {

    public static final int SIZEOF;

    public static final int ALIGNOF;

    public static final int OFFSET_CB_SIZE;

    public static final int OFFSET_HWND_OWNER;

    public static final int OFFSET_HINSTANCE;

    public static final int OFFSET_LPSZ_TEXT;

    public static final int OFFSET_LPSZ_CAPTION;

    public static final int OFFSET_DW_STYLE;

    public static final int OFFSET_LPSZ_ICON;

    public static final int OFFSET_DW_CONTEXT_HELP_ID;

    public static final int OFFSET_LPFN_MSG_BOX_CALLBACK;

    public static final int OFFSET_DW_LANGUAGE_ID;

    public static MsgBoxParamSw allocate(MemoryStack stack) {
        return new MsgBoxParamSw(stack.ncalloc(ALIGNOF, 1, SIZEOF), null);
    }

    private MsgBoxParamSw(long address, @Nullable ByteBuffer container) {
        super(address, container);
    }

    protected Struct create(long address, @Nullable ByteBuffer container) {
        return new MsgBoxParamSw(address, container);
    }

    public int sizeof() {
        return SIZEOF;
    }

    public void setCbSize(int size) {
        MemoryUtil.memPutInt(this.address + (long) OFFSET_CB_SIZE, size);
    }

    public void setHWndOwner(long hWnd) {
        MemoryUtil.memPutAddress(this.address + (long) OFFSET_HWND_OWNER, hWnd);
    }

    public void setText(ByteBuffer buffer) {
        MemoryUtil.memPutAddress(this.address + (long) OFFSET_LPSZ_TEXT, MemoryUtil.memAddress(buffer));
    }

    public void setCaption(ByteBuffer buffer) {
        MemoryUtil.memPutAddress(this.address + (long) OFFSET_LPSZ_CAPTION, MemoryUtil.memAddress(buffer));
    }

    public void setStyle(int style) {
        MemoryUtil.memPutInt(this.address + (long) OFFSET_DW_STYLE, style);
    }

    public void setCallback(@Nullable MsgBoxCallbackI callback) {
        MemoryUtil.memPutAddress(this.address + (long) OFFSET_LPFN_MSG_BOX_CALLBACK, callback == null ? 0L : callback.address());
    }

    static {
        Layout layout = __struct(new Member[] { __member(4), __member(Pointer.POINTER_SIZE), __member(Pointer.POINTER_SIZE), __member(Pointer.POINTER_SIZE), __member(Pointer.POINTER_SIZE), __member(4), __member(Pointer.POINTER_SIZE), __member(Pointer.POINTER_SIZE), __member(Pointer.POINTER_SIZE), __member(4) });
        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();
        OFFSET_CB_SIZE = layout.offsetof(0);
        OFFSET_HWND_OWNER = layout.offsetof(1);
        OFFSET_HINSTANCE = layout.offsetof(2);
        OFFSET_LPSZ_TEXT = layout.offsetof(3);
        OFFSET_LPSZ_CAPTION = layout.offsetof(4);
        OFFSET_DW_STYLE = layout.offsetof(5);
        OFFSET_LPSZ_ICON = layout.offsetof(6);
        OFFSET_DW_CONTEXT_HELP_ID = layout.offsetof(7);
        OFFSET_LPFN_MSG_BOX_CALLBACK = layout.offsetof(8);
        OFFSET_DW_LANGUAGE_ID = layout.offsetof(9);
    }
}