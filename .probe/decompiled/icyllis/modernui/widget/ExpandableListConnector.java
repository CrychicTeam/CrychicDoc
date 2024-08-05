package icyllis.modernui.widget;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.core.Core;
import icyllis.modernui.util.DataSetObserver;
import icyllis.modernui.util.Parcel;
import icyllis.modernui.util.Parcelable;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import javax.annotation.Nullable;

class ExpandableListConnector extends BaseAdapter implements Filterable {

    private ExpandableListAdapter mExpandableListAdapter;

    private ArrayList<ExpandableListConnector.GroupMetadata> mExpGroupMetadataList;

    private int mTotalExpChildrenCount;

    private int mMaxExpGroupCount = Integer.MAX_VALUE;

    private final DataSetObserver mDataSetObserver = new ExpandableListConnector.MyDataSetObserver();

    public ExpandableListConnector(ExpandableListAdapter expandableListAdapter) {
        this.mExpGroupMetadataList = new ArrayList();
        this.setExpandableListAdapter(expandableListAdapter);
    }

    public void setExpandableListAdapter(ExpandableListAdapter expandableListAdapter) {
        if (this.mExpandableListAdapter != null) {
            this.mExpandableListAdapter.unregisterDataSetObserver(this.mDataSetObserver);
        }
        this.mExpandableListAdapter = expandableListAdapter;
        expandableListAdapter.registerDataSetObserver(this.mDataSetObserver);
    }

    ExpandableListConnector.PositionMetadata getUnflattenedPos(int flPos) {
        ArrayList<ExpandableListConnector.GroupMetadata> egml = this.mExpGroupMetadataList;
        int numExpGroups = egml.size();
        int leftExpGroupIndex = 0;
        int rightExpGroupIndex = numExpGroups - 1;
        int midExpGroupIndex = 0;
        if (numExpGroups == 0) {
            return ExpandableListConnector.PositionMetadata.obtain(flPos, 2, flPos, -1, null, 0);
        } else {
            while (leftExpGroupIndex <= rightExpGroupIndex) {
                midExpGroupIndex = (rightExpGroupIndex - leftExpGroupIndex) / 2 + leftExpGroupIndex;
                ExpandableListConnector.GroupMetadata midExpGm = (ExpandableListConnector.GroupMetadata) egml.get(midExpGroupIndex);
                if (flPos > midExpGm.lastChildFlPos) {
                    leftExpGroupIndex = midExpGroupIndex + 1;
                } else if (flPos < midExpGm.flPos) {
                    rightExpGroupIndex = midExpGroupIndex - 1;
                } else {
                    if (flPos == midExpGm.flPos) {
                        return ExpandableListConnector.PositionMetadata.obtain(flPos, 2, midExpGm.gPos, -1, midExpGm, midExpGroupIndex);
                    }
                    if (flPos <= midExpGm.lastChildFlPos) {
                        int childPos = flPos - (midExpGm.flPos + 1);
                        return ExpandableListConnector.PositionMetadata.obtain(flPos, 1, midExpGm.gPos, childPos, midExpGm, midExpGroupIndex);
                    }
                }
            }
            int insertPosition = 0;
            int groupPos = 0;
            if (leftExpGroupIndex > midExpGroupIndex) {
                ExpandableListConnector.GroupMetadata leftExpGm = (ExpandableListConnector.GroupMetadata) egml.get(leftExpGroupIndex - 1);
                insertPosition = leftExpGroupIndex;
                groupPos = flPos - leftExpGm.lastChildFlPos + leftExpGm.gPos;
            } else {
                if (rightExpGroupIndex >= midExpGroupIndex) {
                    throw new RuntimeException("Unknown state");
                }
                ExpandableListConnector.GroupMetadata rightExpGm = (ExpandableListConnector.GroupMetadata) egml.get(++rightExpGroupIndex);
                insertPosition = rightExpGroupIndex;
                groupPos = rightExpGm.gPos - (rightExpGm.flPos - flPos);
            }
            return ExpandableListConnector.PositionMetadata.obtain(flPos, 2, groupPos, -1, null, insertPosition);
        }
    }

    ExpandableListConnector.PositionMetadata getFlattenedPos(ExpandableListPosition pos) {
        ArrayList<ExpandableListConnector.GroupMetadata> egml = this.mExpGroupMetadataList;
        int numExpGroups = egml.size();
        int leftExpGroupIndex = 0;
        int rightExpGroupIndex = numExpGroups - 1;
        int midExpGroupIndex = 0;
        if (numExpGroups == 0) {
            return ExpandableListConnector.PositionMetadata.obtain(pos.groupPos, pos.type, pos.groupPos, pos.childPos, null, 0);
        } else {
            while (leftExpGroupIndex <= rightExpGroupIndex) {
                midExpGroupIndex = (rightExpGroupIndex - leftExpGroupIndex) / 2 + leftExpGroupIndex;
                ExpandableListConnector.GroupMetadata midExpGm = (ExpandableListConnector.GroupMetadata) egml.get(midExpGroupIndex);
                if (pos.groupPos > midExpGm.gPos) {
                    leftExpGroupIndex = midExpGroupIndex + 1;
                } else if (pos.groupPos < midExpGm.gPos) {
                    rightExpGroupIndex = midExpGroupIndex - 1;
                } else if (pos.groupPos == midExpGm.gPos) {
                    if (pos.type == 2) {
                        return ExpandableListConnector.PositionMetadata.obtain(midExpGm.flPos, pos.type, pos.groupPos, pos.childPos, midExpGm, midExpGroupIndex);
                    }
                    if (pos.type == 1) {
                        return ExpandableListConnector.PositionMetadata.obtain(midExpGm.flPos + pos.childPos + 1, pos.type, pos.groupPos, pos.childPos, midExpGm, midExpGroupIndex);
                    }
                    return null;
                }
            }
            if (pos.type != 2) {
                return null;
            } else if (leftExpGroupIndex > midExpGroupIndex) {
                ExpandableListConnector.GroupMetadata leftExpGm = (ExpandableListConnector.GroupMetadata) egml.get(leftExpGroupIndex - 1);
                int flPos = leftExpGm.lastChildFlPos + (pos.groupPos - leftExpGm.gPos);
                return ExpandableListConnector.PositionMetadata.obtain(flPos, pos.type, pos.groupPos, pos.childPos, null, leftExpGroupIndex);
            } else if (rightExpGroupIndex < midExpGroupIndex) {
                ExpandableListConnector.GroupMetadata rightExpGm = (ExpandableListConnector.GroupMetadata) egml.get(++rightExpGroupIndex);
                int flPos = rightExpGm.flPos - (rightExpGm.gPos - pos.groupPos);
                return ExpandableListConnector.PositionMetadata.obtain(flPos, pos.type, pos.groupPos, pos.childPos, null, rightExpGroupIndex);
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        return this.mExpandableListAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int flatListPos) {
        ExpandableListConnector.PositionMetadata metadata = this.getUnflattenedPos(flatListPos);
        ExpandableListPosition pos = metadata.position;
        boolean retValue;
        if (pos.type == 1) {
            retValue = this.mExpandableListAdapter.isChildSelectable(pos.groupPos, pos.childPos);
        } else {
            retValue = true;
        }
        metadata.recycle();
        return retValue;
    }

    @Override
    public int getCount() {
        return this.mExpandableListAdapter.getGroupCount() + this.mTotalExpChildrenCount;
    }

    @Override
    public Object getItem(int flatListPos) {
        ExpandableListConnector.PositionMetadata posMetadata = this.getUnflattenedPos(flatListPos);
        Object retValue;
        if (posMetadata.position.type == 2) {
            retValue = this.mExpandableListAdapter.getGroup(posMetadata.position.groupPos);
        } else {
            if (posMetadata.position.type != 1) {
                throw new RuntimeException("Flat list position is of unknown type");
            }
            retValue = this.mExpandableListAdapter.getChild(posMetadata.position.groupPos, posMetadata.position.childPos);
        }
        posMetadata.recycle();
        return retValue;
    }

    @Override
    public long getItemId(int flatListPos) {
        ExpandableListConnector.PositionMetadata posMetadata = this.getUnflattenedPos(flatListPos);
        long groupId = this.mExpandableListAdapter.getGroupId(posMetadata.position.groupPos);
        long retValue;
        if (posMetadata.position.type == 2) {
            retValue = this.mExpandableListAdapter.getCombinedGroupId(groupId);
        } else {
            if (posMetadata.position.type != 1) {
                throw new RuntimeException("Flat list position is of unknown type");
            }
            long childId = this.mExpandableListAdapter.getChildId(posMetadata.position.groupPos, posMetadata.position.childPos);
            retValue = this.mExpandableListAdapter.getCombinedChildId(groupId, childId);
        }
        posMetadata.recycle();
        return retValue;
    }

    @Override
    public View getView(int flatListPos, @Nullable View convertView, @NonNull ViewGroup parent) {
        ExpandableListConnector.PositionMetadata posMetadata = this.getUnflattenedPos(flatListPos);
        View retValue;
        if (posMetadata.position.type == 2) {
            retValue = this.mExpandableListAdapter.getGroupView(posMetadata.position.groupPos, posMetadata.isExpanded(), convertView, parent);
        } else {
            if (posMetadata.position.type != 1) {
                throw new RuntimeException("Flat list position is of unknown type");
            }
            boolean isLastChild = posMetadata.groupMetadata.lastChildFlPos == flatListPos;
            retValue = this.mExpandableListAdapter.getChildView(posMetadata.position.groupPos, posMetadata.position.childPos, isLastChild, convertView, parent);
        }
        posMetadata.recycle();
        return retValue;
    }

    @Override
    public int getItemViewType(int flatListPos) {
        ExpandableListConnector.PositionMetadata metadata = this.getUnflattenedPos(flatListPos);
        ExpandableListPosition pos = metadata.position;
        int retValue;
        if (this.mExpandableListAdapter instanceof HeterogeneousExpandableList adapter) {
            if (pos.type == 2) {
                retValue = adapter.getGroupType(pos.groupPos);
            } else {
                int childType = adapter.getChildType(pos.groupPos, pos.childPos);
                retValue = adapter.getGroupTypeCount() + childType;
            }
        } else if (pos.type == 2) {
            retValue = 0;
        } else {
            retValue = 1;
        }
        metadata.recycle();
        return retValue;
    }

    @Override
    public int getViewTypeCount() {
        return this.mExpandableListAdapter instanceof HeterogeneousExpandableList adapter ? adapter.getGroupTypeCount() + adapter.getChildTypeCount() : 2;
    }

    @Override
    public boolean hasStableIds() {
        return this.mExpandableListAdapter.hasStableIds();
    }

    private void refreshExpGroupMetadataList(boolean forceChildrenCountRefresh, boolean syncGroupPositions) {
        ArrayList<ExpandableListConnector.GroupMetadata> egml = this.mExpGroupMetadataList;
        int egmlSize = egml.size();
        int curFlPos = 0;
        this.mTotalExpChildrenCount = 0;
        if (syncGroupPositions) {
            boolean positionsChanged = false;
            for (int i = egmlSize - 1; i >= 0; i--) {
                ExpandableListConnector.GroupMetadata curGm = (ExpandableListConnector.GroupMetadata) egml.get(i);
                int newGPos = this.findGroupPosition(curGm.gId, curGm.gPos);
                if (newGPos != curGm.gPos) {
                    if (newGPos == -1) {
                        egml.remove(i);
                        egmlSize--;
                    }
                    curGm.gPos = newGPos;
                    if (!positionsChanged) {
                        positionsChanged = true;
                    }
                }
            }
            if (positionsChanged) {
                Collections.sort(egml);
            }
        }
        int lastGPos = 0;
        for (int ix = 0; ix < egmlSize; ix++) {
            ExpandableListConnector.GroupMetadata curGm = (ExpandableListConnector.GroupMetadata) egml.get(ix);
            int gChildrenCount;
            if (curGm.lastChildFlPos != -1 && !forceChildrenCountRefresh) {
                gChildrenCount = curGm.lastChildFlPos - curGm.flPos;
            } else {
                gChildrenCount = this.mExpandableListAdapter.getChildrenCount(curGm.gPos);
            }
            this.mTotalExpChildrenCount += gChildrenCount;
            int var10 = curFlPos + (curGm.gPos - lastGPos);
            lastGPos = curGm.gPos;
            curGm.flPos = var10;
            curFlPos = var10 + gChildrenCount;
            curGm.lastChildFlPos = curFlPos;
        }
    }

    boolean collapseGroup(int groupPos) {
        ExpandableListPosition elGroupPos = ExpandableListPosition.obtain(2, groupPos, -1, -1);
        ExpandableListConnector.PositionMetadata pm = this.getFlattenedPos(elGroupPos);
        elGroupPos.recycle();
        if (pm == null) {
            return false;
        } else {
            boolean retValue = this.collapseGroup(pm);
            pm.recycle();
            return retValue;
        }
    }

    boolean collapseGroup(ExpandableListConnector.PositionMetadata posMetadata) {
        if (posMetadata.groupMetadata == null) {
            return false;
        } else {
            this.mExpGroupMetadataList.remove(posMetadata.groupMetadata);
            this.refreshExpGroupMetadataList(false, false);
            this.notifyDataSetChanged();
            this.mExpandableListAdapter.onGroupCollapsed(posMetadata.groupMetadata.gPos);
            return true;
        }
    }

    boolean expandGroup(int groupPos) {
        ExpandableListPosition elGroupPos = ExpandableListPosition.obtain(2, groupPos, -1, -1);
        ExpandableListConnector.PositionMetadata pm = this.getFlattenedPos(elGroupPos);
        elGroupPos.recycle();
        boolean retValue = this.expandGroup(pm);
        pm.recycle();
        return retValue;
    }

    boolean expandGroup(ExpandableListConnector.PositionMetadata posMetadata) {
        if (posMetadata.position.groupPos < 0) {
            throw new RuntimeException("Need group");
        } else if (this.mMaxExpGroupCount == 0) {
            return false;
        } else if (posMetadata.groupMetadata != null) {
            return false;
        } else {
            if (this.mExpGroupMetadataList.size() >= this.mMaxExpGroupCount) {
                ExpandableListConnector.GroupMetadata collapsedGm = (ExpandableListConnector.GroupMetadata) this.mExpGroupMetadataList.get(0);
                int collapsedIndex = this.mExpGroupMetadataList.indexOf(collapsedGm);
                this.collapseGroup(collapsedGm.gPos);
                if (posMetadata.groupInsertIndex > collapsedIndex) {
                    posMetadata.groupInsertIndex--;
                }
            }
            ExpandableListConnector.GroupMetadata expandedGm = ExpandableListConnector.GroupMetadata.obtain(-1, -1, posMetadata.position.groupPos, this.mExpandableListAdapter.getGroupId(posMetadata.position.groupPos));
            this.mExpGroupMetadataList.add(posMetadata.groupInsertIndex, expandedGm);
            this.refreshExpGroupMetadataList(false, false);
            this.notifyDataSetChanged();
            this.mExpandableListAdapter.onGroupExpanded(expandedGm.gPos);
            return true;
        }
    }

    public boolean isGroupExpanded(int groupPosition) {
        for (int i = this.mExpGroupMetadataList.size() - 1; i >= 0; i--) {
            ExpandableListConnector.GroupMetadata groupMetadata = (ExpandableListConnector.GroupMetadata) this.mExpGroupMetadataList.get(i);
            if (groupMetadata.gPos == groupPosition) {
                return true;
            }
        }
        return false;
    }

    public void setMaxExpGroupCount(int maxExpGroupCount) {
        this.mMaxExpGroupCount = maxExpGroupCount;
    }

    ExpandableListAdapter getAdapter() {
        return this.mExpandableListAdapter;
    }

    @Override
    public Filter getFilter() {
        ExpandableListAdapter adapter = this.getAdapter();
        return adapter instanceof Filterable ? ((Filterable) adapter).getFilter() : null;
    }

    ArrayList<ExpandableListConnector.GroupMetadata> getExpandedGroupMetadataList() {
        return this.mExpGroupMetadataList;
    }

    void setExpandedGroupMetadataList(ArrayList<ExpandableListConnector.GroupMetadata> expandedGroupMetadataList) {
        if (expandedGroupMetadataList != null && this.mExpandableListAdapter != null) {
            int numGroups = this.mExpandableListAdapter.getGroupCount();
            for (int i = expandedGroupMetadataList.size() - 1; i >= 0; i--) {
                if (((ExpandableListConnector.GroupMetadata) expandedGroupMetadataList.get(i)).gPos >= numGroups) {
                    return;
                }
            }
            this.mExpGroupMetadataList = expandedGroupMetadataList;
            this.refreshExpGroupMetadataList(true, false);
        }
    }

    @Override
    public boolean isEmpty() {
        ExpandableListAdapter adapter = this.getAdapter();
        return adapter != null ? adapter.isEmpty() : true;
    }

    int findGroupPosition(long groupIdToMatch, int seedGroupPosition) {
        int count = this.mExpandableListAdapter.getGroupCount();
        if (count == 0) {
            return -1;
        } else if (groupIdToMatch == Long.MIN_VALUE) {
            return -1;
        } else {
            seedGroupPosition = Math.max(0, seedGroupPosition);
            seedGroupPosition = Math.min(count - 1, seedGroupPosition);
            long endTime = Core.timeMillis() + 100L;
            int first = seedGroupPosition;
            int last = seedGroupPosition;
            boolean next = false;
            ExpandableListAdapter adapter = this.getAdapter();
            if (adapter == null) {
                return -1;
            } else {
                while (Core.timeMillis() <= endTime) {
                    long rowId = adapter.getGroupId(seedGroupPosition);
                    if (rowId == groupIdToMatch) {
                        return seedGroupPosition;
                    }
                    boolean hitLast = last == count - 1;
                    boolean hitFirst = first == 0;
                    if (hitLast && hitFirst) {
                        break;
                    }
                    if (!hitFirst && (!next || hitLast)) {
                        if (hitLast || !next && !hitFirst) {
                            seedGroupPosition = --first;
                            next = true;
                        }
                    } else {
                        seedGroupPosition = ++last;
                        next = false;
                    }
                }
                return -1;
            }
        }
    }

    static class GroupMetadata implements Parcelable, Comparable<ExpandableListConnector.GroupMetadata> {

        static final int REFRESH = -1;

        int flPos;

        int lastChildFlPos;

        int gPos;

        long gId;

        public static final Parcelable.Creator<ExpandableListConnector.GroupMetadata> CREATOR = in -> obtain(in.readInt(), in.readInt(), in.readInt(), in.readLong());

        private GroupMetadata() {
        }

        static ExpandableListConnector.GroupMetadata obtain(int flPos, int lastChildFlPos, int gPos, long gId) {
            ExpandableListConnector.GroupMetadata gm = new ExpandableListConnector.GroupMetadata();
            gm.flPos = flPos;
            gm.lastChildFlPos = lastChildFlPos;
            gm.gPos = gPos;
            gm.gId = gId;
            return gm;
        }

        public int compareTo(@NonNull ExpandableListConnector.GroupMetadata other) {
            return this.gPos - other.gPos;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeInt(this.flPos);
            dest.writeInt(this.lastChildFlPos);
            dest.writeInt(this.gPos);
            dest.writeLong(this.gId);
        }
    }

    protected class MyDataSetObserver implements DataSetObserver {

        @Override
        public void onChanged() {
            ExpandableListConnector.this.refreshExpGroupMetadataList(true, true);
            ExpandableListConnector.this.notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            ExpandableListConnector.this.refreshExpGroupMetadataList(true, true);
            ExpandableListConnector.this.notifyDataSetInvalidated();
        }
    }

    static class PositionMetadata {

        private static final int MAX_POOL_SIZE = 5;

        private static final ArrayDeque<ExpandableListConnector.PositionMetadata> sPool = new ArrayDeque(5);

        public ExpandableListPosition position;

        public ExpandableListConnector.GroupMetadata groupMetadata;

        public int groupInsertIndex;

        private void resetState() {
            if (this.position != null) {
                this.position.recycle();
                this.position = null;
            }
            this.groupMetadata = null;
            this.groupInsertIndex = 0;
        }

        private PositionMetadata() {
        }

        static ExpandableListConnector.PositionMetadata obtain(int flatListPos, int type, int groupPos, int childPos, ExpandableListConnector.GroupMetadata groupMetadata, int groupInsertIndex) {
            ExpandableListConnector.PositionMetadata pm = getRecycledOrCreate();
            pm.position = ExpandableListPosition.obtain(type, groupPos, childPos, flatListPos);
            pm.groupMetadata = groupMetadata;
            pm.groupInsertIndex = groupInsertIndex;
            return pm;
        }

        private static ExpandableListConnector.PositionMetadata getRecycledOrCreate() {
            ExpandableListConnector.PositionMetadata pm;
            synchronized (sPool) {
                if (sPool.isEmpty()) {
                    return new ExpandableListConnector.PositionMetadata();
                }
                pm = (ExpandableListConnector.PositionMetadata) sPool.pop();
            }
            pm.resetState();
            return pm;
        }

        public void recycle() {
            this.resetState();
            synchronized (sPool) {
                if (sPool.size() < 5) {
                    sPool.add(this);
                }
            }
        }

        public boolean isExpanded() {
            return this.groupMetadata != null;
        }
    }
}