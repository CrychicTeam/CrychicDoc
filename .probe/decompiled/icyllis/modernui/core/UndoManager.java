package icyllis.modernui.core;

import icyllis.modernui.text.TextUtils;
import icyllis.modernui.util.ArrayMap;
import icyllis.modernui.util.Parcel;
import java.util.ArrayList;
import java.util.Objects;

public class UndoManager {

    private final ArrayMap<String, UndoOwner> mOwners = new ArrayMap<>(1);

    private final ArrayList<UndoManager.UndoState> mUndos = new ArrayList();

    private final ArrayList<UndoManager.UndoState> mRedos = new ArrayList();

    private int mUpdateCount;

    private int mHistorySize = 25;

    private UndoManager.UndoState mWorking;

    private int mCommitId = 1;

    private boolean mInUndo;

    private boolean mMerged;

    private int mStateSeq;

    private int mNextSavedIdx;

    private UndoOwner[] mStateOwners;

    public static final int MERGE_MODE_NONE = 0;

    public static final int MERGE_MODE_UNIQUE = 1;

    public static final int MERGE_MODE_ANY = 2;

    public UndoOwner getOwner(String tag, Object data) {
        if (tag == null) {
            throw new NullPointerException("tag can't be null");
        } else if (data == null) {
            throw new NullPointerException("data can't be null");
        } else {
            UndoOwner owner = this.mOwners.get(tag);
            if (owner != null) {
                if (owner.mData != data) {
                    if (owner.mData != null) {
                        throw new IllegalStateException("Owner " + owner + " already exists with data " + owner.mData + " but giving different data " + data);
                    }
                    owner.mData = data;
                }
                return owner;
            } else {
                owner = new UndoOwner(tag, this);
                owner.mData = data;
                this.mOwners.put(tag, owner);
                return owner;
            }
        }
    }

    void removeOwner(UndoOwner owner) {
    }

    public void saveInstanceState(Parcel p) {
        if (this.mUpdateCount > 0) {
            throw new IllegalStateException("Can't save state while updating");
        } else {
            this.mStateSeq++;
            if (this.mStateSeq <= 0) {
                this.mStateSeq = 0;
            }
            this.mNextSavedIdx = 0;
            p.writeInt(this.mHistorySize);
            p.writeInt(this.mOwners.size());
            int i = this.mUndos.size();
            while (i > 0) {
                p.writeInt(1);
                ((UndoManager.UndoState) this.mUndos.get(--i)).writeToParcel(p);
            }
            i = this.mRedos.size();
            while (i > 0) {
                p.writeInt(2);
                ((UndoManager.UndoState) this.mRedos.get(--i)).writeToParcel(p);
            }
            p.writeInt(0);
        }
    }

    void saveOwner(UndoOwner owner, Parcel out) {
        if (owner.mStateSeq == this.mStateSeq) {
            out.writeInt(owner.mSavedIdx);
        } else {
            owner.mStateSeq = this.mStateSeq;
            owner.mSavedIdx = this.mNextSavedIdx;
            out.writeInt(owner.mSavedIdx);
            out.writeString(owner.mTag);
            out.writeInt(owner.mOpCount);
            this.mNextSavedIdx++;
        }
    }

    public void restoreInstanceState(Parcel p, ClassLoader loader) {
        if (this.mUpdateCount > 0) {
            throw new IllegalStateException("Can't save state while updating");
        } else {
            this.forgetUndos(null, -1);
            this.forgetRedos(null, -1);
            this.mHistorySize = p.readInt();
            this.mStateOwners = new UndoOwner[p.readInt()];
            int stype;
            while ((stype = p.readInt()) != 0) {
                UndoManager.UndoState ustate = new UndoManager.UndoState(this, p, loader);
                if (stype == 1) {
                    this.mUndos.add(0, ustate);
                } else {
                    this.mRedos.add(0, ustate);
                }
            }
        }
    }

    UndoOwner restoreOwner(Parcel in) {
        int idx = in.readInt();
        UndoOwner owner = this.mStateOwners[idx];
        if (owner == null) {
            String tag = in.readString();
            int opCount = in.readInt();
            owner = new UndoOwner(tag, this);
            owner.mOpCount = opCount;
            this.mStateOwners[idx] = owner;
            this.mOwners.put(tag, owner);
        }
        return owner;
    }

    public void setHistorySize(int size) {
        this.mHistorySize = size;
        if (this.mHistorySize >= 0 && this.countUndos(null) > this.mHistorySize) {
            this.forgetUndos(null, this.countUndos(null) - this.mHistorySize);
        }
    }

    public int getHistorySize() {
        return this.mHistorySize;
    }

    public int undo(UndoOwner[] owners, int count) {
        if (this.mWorking != null) {
            throw new IllegalStateException("Can't be called during an update");
        } else {
            int num = 0;
            int i = -1;
            this.mInUndo = true;
            UndoManager.UndoState us = this.getTopUndo(null);
            if (us != null) {
                us.makeExecuted();
            }
            while (count > 0 && (i = this.findPrevState(this.mUndos, owners, i)) >= 0) {
                UndoManager.UndoState state = (UndoManager.UndoState) this.mUndos.remove(i);
                state.undo();
                this.mRedos.add(state);
                count--;
                num++;
            }
            this.mInUndo = false;
            return num;
        }
    }

    public int redo(UndoOwner[] owners, int count) {
        if (this.mWorking != null) {
            throw new IllegalStateException("Can't be called during an update");
        } else {
            int num = 0;
            int i = -1;
            for (this.mInUndo = true; count > 0 && (i = this.findPrevState(this.mRedos, owners, i)) >= 0; num++) {
                UndoManager.UndoState state = (UndoManager.UndoState) this.mRedos.remove(i);
                state.redo();
                this.mUndos.add(state);
                count--;
            }
            this.mInUndo = false;
            return num;
        }
    }

    public boolean isInUndo() {
        return this.mInUndo;
    }

    public int forgetUndos(UndoOwner[] owners, int count) {
        if (count < 0) {
            count = this.mUndos.size();
        }
        int removed = 0;
        int i = 0;
        while (i < this.mUndos.size() && removed < count) {
            UndoManager.UndoState state = (UndoManager.UndoState) this.mUndos.get(i);
            if (count > 0 && this.matchOwners(state, owners)) {
                state.destroy();
                this.mUndos.remove(i);
                removed++;
            } else {
                i++;
            }
        }
        return removed;
    }

    public int forgetRedos(UndoOwner[] owners, int count) {
        if (count < 0) {
            count = this.mRedos.size();
        }
        int removed = 0;
        int i = 0;
        while (i < this.mRedos.size() && removed < count) {
            UndoManager.UndoState state = (UndoManager.UndoState) this.mRedos.get(i);
            if (count > 0 && this.matchOwners(state, owners)) {
                state.destroy();
                this.mRedos.remove(i);
                removed++;
            } else {
                i++;
            }
        }
        return removed;
    }

    public int countUndos(UndoOwner[] owners) {
        if (owners == null) {
            return this.mUndos.size();
        } else {
            int count = 0;
            for (int i = 0; (i = this.findNextState(this.mUndos, owners, i)) >= 0; i++) {
                count++;
            }
            return count;
        }
    }

    public int countRedos(UndoOwner[] owners) {
        if (owners == null) {
            return this.mRedos.size();
        } else {
            int count = 0;
            for (int i = 0; (i = this.findNextState(this.mRedos, owners, i)) >= 0; i++) {
                count++;
            }
            return count;
        }
    }

    public CharSequence getUndoLabel(UndoOwner[] owners) {
        UndoManager.UndoState state = this.getTopUndo(owners);
        return state != null ? state.getLabel() : null;
    }

    public CharSequence getRedoLabel(UndoOwner[] owners) {
        UndoManager.UndoState state = this.getTopRedo(owners);
        return state != null ? state.getLabel() : null;
    }

    public void beginUpdate(CharSequence label) {
        if (this.mInUndo) {
            throw new IllegalStateException("Can't being update while performing undo/redo");
        } else {
            if (this.mUpdateCount <= 0) {
                this.createWorkingState();
                this.mMerged = false;
                this.mUpdateCount = 0;
            }
            this.mWorking.updateLabel(label);
            this.mUpdateCount++;
        }
    }

    private void createWorkingState() {
        this.mWorking = new UndoManager.UndoState(this, this.mCommitId++);
        if (this.mCommitId < 0) {
            this.mCommitId = 1;
        }
    }

    public boolean isInUpdate() {
        return this.mUpdateCount > 0;
    }

    public void setUndoLabel(CharSequence label) {
        if (this.mWorking == null) {
            throw new IllegalStateException("Must be called during an update");
        } else {
            this.mWorking.setLabel(label);
        }
    }

    public void suggestUndoLabel(CharSequence label) {
        if (this.mWorking == null) {
            throw new IllegalStateException("Must be called during an update");
        } else {
            this.mWorking.updateLabel(label);
        }
    }

    public int getUpdateNestingLevel() {
        return this.mUpdateCount;
    }

    public boolean hasOperation(UndoOwner owner) {
        if (this.mWorking == null) {
            throw new IllegalStateException("Must be called during an update");
        } else {
            return this.mWorking.hasOperation(owner);
        }
    }

    public UndoOperation<?> getLastOperation(int mergeMode) {
        return this.getLastOperation(null, null, mergeMode);
    }

    public UndoOperation<?> getLastOperation(UndoOwner owner, int mergeMode) {
        return this.getLastOperation(null, owner, mergeMode);
    }

    public <T extends UndoOperation<?>> T getLastOperation(Class<T> clazz, UndoOwner owner, int mergeMode) {
        if (this.mWorking == null) {
            throw new IllegalStateException("Must be called during an update");
        } else {
            if (mergeMode != 0 && !this.mMerged && !this.mWorking.hasData()) {
                UndoManager.UndoState state = this.getTopUndo(null);
                T last;
                if (state != null && (mergeMode == 2 || !state.hasMultipleOwners()) && state.canMerge() && (last = state.getLastOperation(clazz, owner)) != null && last.allowMerge()) {
                    this.mWorking.destroy();
                    this.mWorking = state;
                    this.mUndos.remove(state);
                    this.mMerged = true;
                    return last;
                }
            }
            return this.mWorking.getLastOperation(clazz, owner);
        }
    }

    public void addOperation(UndoOperation<?> op, int mergeMode) {
        if (this.mWorking == null) {
            throw new IllegalStateException("Must be called during an update");
        } else {
            UndoOwner owner = op.getOwner();
            if (owner.mManager != this) {
                throw new IllegalArgumentException("Given operation's owner is not in this undo manager.");
            } else {
                if (mergeMode != 0 && !this.mMerged && !this.mWorking.hasData()) {
                    UndoManager.UndoState state = this.getTopUndo(null);
                    if (state != null && (mergeMode == 2 || !state.hasMultipleOwners()) && state.canMerge() && state.hasOperation(op.getOwner())) {
                        this.mWorking.destroy();
                        this.mWorking = state;
                        this.mUndos.remove(state);
                        this.mMerged = true;
                    }
                }
                this.mWorking.addOperation(op);
            }
        }
    }

    public void endUpdate() {
        if (this.mWorking == null) {
            throw new IllegalStateException("Must be called during an update");
        } else {
            this.mUpdateCount--;
            if (this.mUpdateCount == 0) {
                this.pushWorkingState();
            }
        }
    }

    private void pushWorkingState() {
        int N = this.mUndos.size() + 1;
        if (this.mWorking.hasData()) {
            this.mUndos.add(this.mWorking);
            this.forgetRedos(null, -1);
            this.mWorking.commit();
            if (N >= 2) {
                ((UndoManager.UndoState) this.mUndos.get(N - 2)).makeExecuted();
            }
        } else {
            this.mWorking.destroy();
        }
        this.mWorking = null;
        if (this.mHistorySize >= 0 && N > this.mHistorySize) {
            this.forgetUndos(null, N - this.mHistorySize);
        }
    }

    public int commitState(UndoOwner owner) {
        if (this.mWorking != null && this.mWorking.hasData()) {
            if (owner == null || this.mWorking.hasOperation(owner)) {
                this.mWorking.setCanMerge(false);
                int commitId = this.mWorking.getCommitId();
                this.pushWorkingState();
                this.createWorkingState();
                this.mMerged = true;
                return commitId;
            }
        } else {
            UndoManager.UndoState state = this.getTopUndo(null);
            if (state != null && (owner == null || state.hasOperation(owner))) {
                state.setCanMerge(false);
                return state.getCommitId();
            }
        }
        return -1;
    }

    public boolean uncommitState(int commitId, UndoOwner owner) {
        if (this.mWorking != null && this.mWorking.getCommitId() == commitId) {
            if (owner == null || this.mWorking.hasOperation(owner)) {
                return this.mWorking.setCanMerge(true);
            }
        } else {
            UndoManager.UndoState state = this.getTopUndo(null);
            if (state != null && (owner == null || state.hasOperation(owner)) && state.getCommitId() == commitId) {
                return state.setCanMerge(true);
            }
        }
        return false;
    }

    UndoManager.UndoState getTopUndo(UndoOwner[] owners) {
        if (this.mUndos.size() <= 0) {
            return null;
        } else {
            int i = this.findPrevState(this.mUndos, owners, -1);
            return i >= 0 ? (UndoManager.UndoState) this.mUndos.get(i) : null;
        }
    }

    UndoManager.UndoState getTopRedo(UndoOwner[] owners) {
        if (this.mRedos.size() <= 0) {
            return null;
        } else {
            int i = this.findPrevState(this.mRedos, owners, -1);
            return i >= 0 ? (UndoManager.UndoState) this.mRedos.get(i) : null;
        }
    }

    boolean matchOwners(UndoManager.UndoState state, UndoOwner[] owners) {
        if (owners == null) {
            return true;
        } else {
            for (int i = 0; i < owners.length; i++) {
                if (state.matchOwner(owners[i])) {
                    return true;
                }
            }
            return false;
        }
    }

    int findPrevState(ArrayList<UndoManager.UndoState> states, UndoOwner[] owners, int from) {
        int N = states.size();
        if (from == -1) {
            from = N - 1;
        }
        if (from >= N) {
            return -1;
        } else if (owners == null) {
            return from;
        } else {
            while (from >= 0) {
                UndoManager.UndoState state = (UndoManager.UndoState) states.get(from);
                if (this.matchOwners(state, owners)) {
                    return from;
                }
                from--;
            }
            return -1;
        }
    }

    int findNextState(ArrayList<UndoManager.UndoState> states, UndoOwner[] owners, int from) {
        int N = states.size();
        if (from < 0) {
            from = 0;
        }
        if (from >= N) {
            return -1;
        } else if (owners == null) {
            return from;
        } else {
            while (from < N) {
                UndoManager.UndoState state = (UndoManager.UndoState) states.get(from);
                if (this.matchOwners(state, owners)) {
                    return from;
                }
                from++;
            }
            return -1;
        }
    }

    static final class UndoState {

        private final UndoManager mManager;

        private final int mCommitId;

        private final ArrayList<UndoOperation<?>> mOperations = new ArrayList();

        private ArrayList<UndoOperation<?>> mRecent;

        private CharSequence mLabel;

        private boolean mCanMerge = true;

        private boolean mExecuted;

        UndoState(UndoManager manager, int commitId) {
            this.mManager = manager;
            this.mCommitId = commitId;
        }

        UndoState(UndoManager manager, Parcel p, ClassLoader loader) {
            this.mManager = manager;
            this.mCommitId = p.readInt();
            this.mCanMerge = p.readInt() != 0;
            this.mExecuted = p.readInt() != 0;
            this.mLabel = TextUtils.createFromParcel(p);
            int N = p.readInt();
            for (int i = 0; i < N; i++) {
                UndoOwner owner = this.mManager.restoreOwner(p);
                UndoOperation<?> op = p.readParcelable(loader, UndoOperation.class);
                ((UndoOperation) Objects.requireNonNull(op)).mOwner = owner;
                this.mOperations.add(op);
            }
        }

        void writeToParcel(Parcel p) {
            if (this.mRecent != null) {
                throw new IllegalStateException("Can't save state before committing");
            } else {
                p.writeInt(this.mCommitId);
                p.writeInt(this.mCanMerge ? 1 : 0);
                p.writeInt(this.mExecuted ? 1 : 0);
                TextUtils.writeToParcel(this.mLabel, p, 0);
                p.writeInt(this.mOperations.size());
                for (UndoOperation<?> op : this.mOperations) {
                    this.mManager.saveOwner(op.mOwner, p);
                    op.writeToParcel(p, 0);
                }
            }
        }

        int getCommitId() {
            return this.mCommitId;
        }

        void setLabel(CharSequence label) {
            this.mLabel = label;
        }

        void updateLabel(CharSequence label) {
            if (this.mLabel != null) {
                this.mLabel = label;
            }
        }

        CharSequence getLabel() {
            return this.mLabel;
        }

        boolean setCanMerge(boolean state) {
            if (state && this.mExecuted) {
                return false;
            } else {
                this.mCanMerge = state;
                return true;
            }
        }

        void makeExecuted() {
            this.mExecuted = true;
        }

        boolean canMerge() {
            return this.mCanMerge && !this.mExecuted;
        }

        int countOperations() {
            return this.mOperations.size();
        }

        boolean hasOperation(UndoOwner owner) {
            if (owner == null) {
                return !this.mOperations.isEmpty();
            } else {
                for (UndoOperation<?> operation : this.mOperations) {
                    if (operation.getOwner() == owner) {
                        return true;
                    }
                }
                return false;
            }
        }

        boolean hasMultipleOwners() {
            int N = this.mOperations.size();
            if (N <= 1) {
                return false;
            } else {
                UndoOwner owner = ((UndoOperation) this.mOperations.get(0)).getOwner();
                for (int i = 1; i < N; i++) {
                    if (((UndoOperation) this.mOperations.get(i)).getOwner() != owner) {
                        return true;
                    }
                }
                return false;
            }
        }

        void addOperation(UndoOperation<?> op) {
            if (this.mOperations.contains(op)) {
                throw new IllegalStateException("Already holds " + op);
            } else {
                this.mOperations.add(op);
                if (this.mRecent == null) {
                    this.mRecent = new ArrayList();
                    this.mRecent.add(op);
                }
                op.mOwner.mOpCount++;
            }
        }

        <T extends UndoOperation<?>> T getLastOperation(Class<T> clazz, UndoOwner owner) {
            int N = this.mOperations.size();
            if (clazz == null && owner == null) {
                return (T) (N > 0 ? this.mOperations.get(N - 1) : null);
            } else {
                for (int i = N - 1; i >= 0; i--) {
                    UndoOperation<?> op = (UndoOperation<?>) this.mOperations.get(i);
                    if (owner == null || op.getOwner() == owner) {
                        return (T) (clazz != null && op.getClass() != clazz ? null : op);
                    }
                }
                return null;
            }
        }

        boolean matchOwner(UndoOwner owner) {
            for (int i = this.mOperations.size() - 1; i >= 0; i--) {
                if (((UndoOperation) this.mOperations.get(i)).matchOwner(owner)) {
                    return true;
                }
            }
            return false;
        }

        boolean hasData() {
            for (int i = this.mOperations.size() - 1; i >= 0; i--) {
                if (((UndoOperation) this.mOperations.get(i)).hasData()) {
                    return true;
                }
            }
            return false;
        }

        void commit() {
            int N = this.mRecent != null ? this.mRecent.size() : 0;
            for (int i = 0; i < N; i++) {
                ((UndoOperation) this.mRecent.get(i)).commit();
            }
            this.mRecent = null;
        }

        void undo() {
            for (int i = this.mOperations.size() - 1; i >= 0; i--) {
                ((UndoOperation) this.mOperations.get(i)).undo();
            }
        }

        void redo() {
            int N = this.mOperations.size();
            for (int i = 0; i < N; i++) {
                ((UndoOperation) this.mOperations.get(i)).redo();
            }
        }

        void destroy() {
            for (int i = this.mOperations.size() - 1; i >= 0; i--) {
                UndoOwner owner = ((UndoOperation) this.mOperations.get(i)).mOwner;
                owner.mOpCount--;
                if (owner.mOpCount <= 0) {
                    if (owner.mOpCount < 0) {
                        throw new IllegalStateException("Underflow of op count on owner " + owner + " in op " + this.mOperations.get(i));
                    }
                    this.mManager.removeOwner(owner);
                }
            }
        }
    }
}