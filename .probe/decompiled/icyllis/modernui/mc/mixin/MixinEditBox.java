package icyllis.modernui.mc.mixin;

import icyllis.modernui.core.UndoManager;
import icyllis.modernui.core.UndoOwner;
import icyllis.modernui.mc.EditBoxEditAction;
import icyllis.modernui.mc.IModernEditBox;
import icyllis.modernui.mc.MuiModApi;
import icyllis.modernui.text.method.WordIterator;
import net.minecraft.Util;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ EditBox.class })
public abstract class MixinEditBox implements IModernEditBox {

    @Shadow
    private int cursorPos;

    @Shadow
    private String value;

    @Shadow
    private int frame;

    @Unique
    private WordIterator modernUI_MC$wordIterator;

    @Unique
    private long modernUI_MC$lastInsertTextNanos;

    @Unique
    private final UndoManager modernUI_MC$undoManager = new UndoManager();

    @Inject(method = { "setCursorPosition" }, at = { @At("RETURN") })
    public void onSetCursorPosition(int pos, CallbackInfo ci) {
        this.frame = 0;
    }

    @Inject(method = { "getCursorPos" }, at = { @At("HEAD") }, cancellable = true)
    public void onGetCursorPosition(int dir, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(MuiModApi.offsetByGrapheme(this.value, this.cursorPos, dir));
    }

    @Inject(method = { "getWordPosition(IIZ)I" }, at = { @At("HEAD") }, cancellable = true)
    public void onGetWordPosition(int dir, int cursor, boolean withEndSpace, CallbackInfoReturnable<Integer> cir) {
        if ((dir == -1 || dir == 1) && !this.value.startsWith("/")) {
            WordIterator wordIterator = this.modernUI_MC$wordIterator;
            if (wordIterator == null) {
                this.modernUI_MC$wordIterator = wordIterator = new WordIterator();
            }
            wordIterator.setCharSequence(this.value, cursor, cursor);
            int offset;
            if (dir == -1) {
                offset = wordIterator.preceding(cursor);
            } else {
                offset = wordIterator.following(cursor);
            }
            if (offset != -1) {
                cir.setReturnValue(offset);
            } else {
                cir.setReturnValue(cursor);
            }
        }
    }

    @Inject(method = { "setValue" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/EditBox;value:Ljava/lang/String;", opcode = 181) })
    public void onSetValue(String string, CallbackInfo ci) {
        if (!this.modernUI_MC$undoManager.isInUndo()) {
            if (!this.value.isEmpty() || !string.isEmpty()) {
                EditBoxEditAction edit = new EditBoxEditAction(this.modernUI_MC$undoOwner(), this.cursorPos, this.value, 0, string);
                this.modernUI_MC$addEdit(edit, false);
            }
        }
    }

    @Inject(method = { "insertText" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/EditBox;value:Ljava/lang/String;", opcode = 181) }, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void onInsertText(String string, CallbackInfo ci, int i, int j, int k, String string2, int l, String string3) {
        if (!this.modernUI_MC$undoManager.isInUndo()) {
            String oldText = this.value.substring(i, j);
            if (!oldText.isEmpty() || !string2.isEmpty()) {
                EditBoxEditAction edit = new EditBoxEditAction(this.modernUI_MC$undoOwner(), this.cursorPos, oldText, i, string2);
                long nanos = Util.getNanos();
                boolean mergeInsert;
                if (this.modernUI_MC$lastInsertTextNanos >= nanos - 3000000L) {
                    mergeInsert = true;
                } else {
                    this.modernUI_MC$lastInsertTextNanos = nanos;
                    mergeInsert = false;
                }
                this.modernUI_MC$addEdit(edit, mergeInsert);
            }
        }
    }

    @Inject(method = { "deleteChars" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/EditBox;value:Ljava/lang/String;", opcode = 181) }, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void onDeleteChars(int i, CallbackInfo ci, int j, int k, int l, String string) {
        if (!this.modernUI_MC$undoManager.isInUndo()) {
            String oldText = this.value.substring(k, l);
            if (!oldText.isEmpty()) {
                EditBoxEditAction edit = new EditBoxEditAction(this.modernUI_MC$undoOwner(), this.cursorPos, oldText, k, "");
                this.modernUI_MC$addEdit(edit, false);
            }
        }
    }

    @Unique
    public void modernUI_MC$addEdit(EditBoxEditAction edit, boolean mergeInsert) {
        UndoManager mgr = this.modernUI_MC$undoManager;
        mgr.beginUpdate("addEdit");
        EditBoxEditAction lastEdit = mgr.getLastOperation(EditBoxEditAction.class, edit.getOwner(), 1);
        if (lastEdit == null) {
            mgr.addOperation(edit, 0);
        } else if (!mergeInsert || !lastEdit.mergeInsertWith(edit)) {
            mgr.commitState(edit.getOwner());
            mgr.addOperation(edit, 0);
        }
        mgr.endUpdate();
    }

    @Inject(method = { "keyPressed" }, at = { @At("TAIL") }, cancellable = true)
    public void onKeyPressed(int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        if ((i == 90 || i == 89) && Screen.hasControlDown() && !Screen.hasAltDown()) {
            if (!Screen.hasShiftDown()) {
                UndoOwner[] owners = new UndoOwner[] { this.modernUI_MC$undoOwner() };
                if (i == 90) {
                    if (this.modernUI_MC$undoManager.countUndos(owners) > 0) {
                        this.modernUI_MC$undoManager.undo(owners, 1);
                        cir.setReturnValue(true);
                    }
                } else if (this.modernUI_MC$tryRedo(owners)) {
                    cir.setReturnValue(true);
                }
            } else if (i == 90) {
                UndoOwner[] owners = new UndoOwner[] { this.modernUI_MC$undoOwner() };
                if (this.modernUI_MC$tryRedo(owners)) {
                    cir.setReturnValue(true);
                }
            }
        }
    }

    @Unique
    private UndoOwner modernUI_MC$undoOwner() {
        return this.modernUI_MC$undoManager.getOwner("EditBox", this);
    }

    @Unique
    private boolean modernUI_MC$tryRedo(UndoOwner[] owners) {
        if (this.modernUI_MC$undoManager.countRedos(owners) > 0) {
            this.modernUI_MC$undoManager.redo(owners, 1);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UndoManager modernUI_MC$getUndoManager() {
        return this.modernUI_MC$undoManager;
    }
}