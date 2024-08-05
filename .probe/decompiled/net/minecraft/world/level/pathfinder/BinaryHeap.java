package net.minecraft.world.level.pathfinder;

public class BinaryHeap {

    private Node[] heap = new Node[128];

    private int size;

    public Node insert(Node node0) {
        if (node0.heapIdx >= 0) {
            throw new IllegalStateException("OW KNOWS!");
        } else {
            if (this.size == this.heap.length) {
                Node[] $$1 = new Node[this.size << 1];
                System.arraycopy(this.heap, 0, $$1, 0, this.size);
                this.heap = $$1;
            }
            this.heap[this.size] = node0;
            node0.heapIdx = this.size;
            this.upHeap(this.size++);
            return node0;
        }
    }

    public void clear() {
        this.size = 0;
    }

    public Node peek() {
        return this.heap[0];
    }

    public Node pop() {
        Node $$0 = this.heap[0];
        this.heap[0] = this.heap[--this.size];
        this.heap[this.size] = null;
        if (this.size > 0) {
            this.downHeap(0);
        }
        $$0.heapIdx = -1;
        return $$0;
    }

    public void remove(Node node0) {
        this.heap[node0.heapIdx] = this.heap[--this.size];
        this.heap[this.size] = null;
        if (this.size > node0.heapIdx) {
            if (this.heap[node0.heapIdx].f < node0.f) {
                this.upHeap(node0.heapIdx);
            } else {
                this.downHeap(node0.heapIdx);
            }
        }
        node0.heapIdx = -1;
    }

    public void changeCost(Node node0, float float1) {
        float $$2 = node0.f;
        node0.f = float1;
        if (float1 < $$2) {
            this.upHeap(node0.heapIdx);
        } else {
            this.downHeap(node0.heapIdx);
        }
    }

    public int size() {
        return this.size;
    }

    private void upHeap(int int0) {
        Node $$1 = this.heap[int0];
        float $$2 = $$1.f;
        while (int0 > 0) {
            int $$3 = int0 - 1 >> 1;
            Node $$4 = this.heap[$$3];
            if (!($$2 < $$4.f)) {
                break;
            }
            this.heap[int0] = $$4;
            $$4.heapIdx = int0;
            int0 = $$3;
        }
        this.heap[int0] = $$1;
        $$1.heapIdx = int0;
    }

    private void downHeap(int int0) {
        Node $$1 = this.heap[int0];
        float $$2 = $$1.f;
        while (true) {
            int $$3 = 1 + (int0 << 1);
            int $$4 = $$3 + 1;
            if ($$3 >= this.size) {
                break;
            }
            Node $$5 = this.heap[$$3];
            float $$6 = $$5.f;
            Node $$7;
            float $$8;
            if ($$4 >= this.size) {
                $$7 = null;
                $$8 = Float.POSITIVE_INFINITY;
            } else {
                $$7 = this.heap[$$4];
                $$8 = $$7.f;
            }
            if ($$6 < $$8) {
                if (!($$6 < $$2)) {
                    break;
                }
                this.heap[int0] = $$5;
                $$5.heapIdx = int0;
                int0 = $$3;
            } else {
                if (!($$8 < $$2)) {
                    break;
                }
                this.heap[int0] = $$7;
                $$7.heapIdx = int0;
                int0 = $$4;
            }
        }
        this.heap[int0] = $$1;
        $$1.heapIdx = int0;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public Node[] getHeap() {
        Node[] $$0 = new Node[this.size()];
        System.arraycopy(this.heap, 0, $$0, 0, this.size());
        return $$0;
    }
}