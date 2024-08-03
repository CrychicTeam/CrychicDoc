package icyllis.modernui.core;

import icyllis.modernui.util.Parcel;
import icyllis.modernui.util.Parcelable;

public abstract class UndoOperation<DATA> implements Parcelable {

    UndoOwner mOwner;

    public UndoOperation(UndoOwner owner) {
        this.mOwner = owner;
    }

    protected UndoOperation(Parcel src, ClassLoader loader) {
    }

    public UndoOwner getOwner() {
        return this.mOwner;
    }

    public DATA getOwnerData() {
        return (DATA) this.mOwner.getData();
    }

    public boolean matchOwner(UndoOwner owner) {
        return owner == this.getOwner();
    }

    public boolean hasData() {
        return true;
    }

    public boolean allowMerge() {
        return true;
    }

    public abstract void commit();

    public abstract void undo();

    public abstract void redo();
}