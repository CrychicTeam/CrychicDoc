package icyllis.modernui.core;

public class UndoOwner {

    final String mTag;

    final UndoManager mManager;

    Object mData;

    int mOpCount;

    int mStateSeq;

    int mSavedIdx;

    UndoOwner(String tag, UndoManager manager) {
        if (tag == null) {
            throw new NullPointerException("tag can't be null");
        } else if (manager == null) {
            throw new NullPointerException("manager can't be null");
        } else {
            this.mTag = tag;
            this.mManager = manager;
        }
    }

    public String getTag() {
        return this.mTag;
    }

    public Object getData() {
        return this.mData;
    }

    public String toString() {
        return "UndoOwner:[mTag=" + this.mTag + " mManager=" + this.mManager + " mData=" + this.mData + " mData=" + this.mData + " mOpCount=" + this.mOpCount + " mStateSeq=" + this.mStateSeq + " mSavedIdx=" + this.mSavedIdx + "]";
    }
}