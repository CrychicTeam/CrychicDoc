package me.jellysquid.mods.sodium.client.render.chunk.lists;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.util.iterator.ReversibleObjectArrayIterator;

public class SortedRenderLists implements ChunkRenderListIterable {

    private static final SortedRenderLists EMPTY = new SortedRenderLists(ObjectArrayList.of());

    private final ObjectArrayList<ChunkRenderList> lists;

    SortedRenderLists(ObjectArrayList<ChunkRenderList> lists) {
        this.lists = lists;
    }

    public ReversibleObjectArrayIterator<ChunkRenderList> iterator(boolean reverse) {
        return new ReversibleObjectArrayIterator<>(this.lists, reverse);
    }

    public static SortedRenderLists empty() {
        return EMPTY;
    }

    public static class Builder {

        private final ObjectArrayList<ChunkRenderList> lists = new ObjectArrayList();

        private final int frame;

        public Builder(int frame) {
            this.frame = frame;
        }

        public void add(RenderSection section) {
            RenderRegion region = section.getRegion();
            ChunkRenderList list = region.getRenderList();
            if (list.getLastVisibleFrame() != this.frame) {
                list.reset(this.frame);
                this.lists.add(list);
            }
            if (section.getFlags() != 0) {
                list.add(section);
            }
        }

        public SortedRenderLists build() {
            ObjectArrayList<ChunkRenderList> filtered = new ObjectArrayList(this.lists.size());
            ObjectListIterator var2 = this.lists.iterator();
            while (var2.hasNext()) {
                ChunkRenderList list = (ChunkRenderList) var2.next();
                if (list.size() > 0) {
                    filtered.add(list);
                }
            }
            return new SortedRenderLists(filtered);
        }
    }
}