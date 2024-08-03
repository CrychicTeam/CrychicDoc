package dev.latvian.mods.rhino;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class EmbeddedSlotMap implements SlotMap {

    private static final int INITIAL_SLOT_SIZE = 4;

    private ScriptableObject.Slot[] slots;

    private ScriptableObject.Slot firstAdded;

    private ScriptableObject.Slot lastAdded;

    private int count;

    private static void copyTable(ScriptableObject.Slot[] oldSlots, ScriptableObject.Slot[] newSlots) {
        for (ScriptableObject.Slot slot : oldSlots) {
            while (slot != null) {
                ScriptableObject.Slot nextSlot = slot.next;
                slot.next = null;
                addKnownAbsentSlot(newSlots, slot);
                slot = nextSlot;
            }
        }
    }

    private static void addKnownAbsentSlot(ScriptableObject.Slot[] addSlots, ScriptableObject.Slot slot) {
        int insertPos = getSlotIndex(addSlots.length, slot.indexOrHash);
        ScriptableObject.Slot old = addSlots[insertPos];
        addSlots[insertPos] = slot;
        slot.next = old;
    }

    private static int getSlotIndex(int tableSize, int indexOrHash) {
        return indexOrHash & tableSize - 1;
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public boolean isEmpty() {
        return this.count == 0;
    }

    public Iterator<ScriptableObject.Slot> iterator() {
        return new EmbeddedSlotMap.Iter(this.firstAdded);
    }

    @Override
    public ScriptableObject.Slot query(Object key, int index) {
        if (this.slots == null) {
            return null;
        } else {
            int indexOrHash = key != null ? key.hashCode() : index;
            int slotIndex = getSlotIndex(this.slots.length, indexOrHash);
            for (ScriptableObject.Slot slot = this.slots[slotIndex]; slot != null; slot = slot.next) {
                Object skey = slot.name;
                if (indexOrHash == slot.indexOrHash && Objects.equals(key, skey)) {
                    return slot;
                }
            }
            return null;
        }
    }

    @Override
    public ScriptableObject.Slot get(Object key, int index, ScriptableObject.SlotAccess accessType) {
        if (this.slots == null && accessType == ScriptableObject.SlotAccess.QUERY) {
            return null;
        } else {
            int indexOrHash = key != null ? key.hashCode() : index;
            ScriptableObject.Slot slot = null;
            if (this.slots != null) {
                int slotIndex = getSlotIndex(this.slots.length, indexOrHash);
                for (slot = this.slots[slotIndex]; slot != null; slot = slot.next) {
                    Object skey = slot.name;
                    if (indexOrHash == slot.indexOrHash && Objects.equals(key, skey)) {
                        break;
                    }
                }
                switch(accessType) {
                    case QUERY:
                        return slot;
                    case MODIFY:
                    case MODIFY_CONST:
                        if (slot != null) {
                            return slot;
                        }
                        break;
                    case MODIFY_GETTER_SETTER:
                        if (slot instanceof ScriptableObject.GetterSlot) {
                            return slot;
                        }
                        break;
                    case CONVERT_ACCESSOR_TO_DATA:
                        if (!(slot instanceof ScriptableObject.GetterSlot)) {
                            return slot;
                        }
                }
            }
            return this.createSlot(key, indexOrHash, accessType, slot);
        }
    }

    private ScriptableObject.Slot createSlot(Object key, int indexOrHash, ScriptableObject.SlotAccess accessType, ScriptableObject.Slot existingSlot) {
        if (this.count == 0) {
            this.slots = new ScriptableObject.Slot[4];
        } else if (existingSlot != null) {
            int insertPos = getSlotIndex(this.slots.length, indexOrHash);
            ScriptableObject.Slot prev = this.slots[insertPos];
            ScriptableObject.Slot slot;
            for (slot = prev; slot != null && (slot.indexOrHash != indexOrHash || !Objects.equals(key, slot.name)); slot = slot.next) {
                prev = slot;
            }
            if (slot != null) {
                ScriptableObject.Slot newSlot;
                if (accessType == ScriptableObject.SlotAccess.MODIFY_GETTER_SETTER && !(slot instanceof ScriptableObject.GetterSlot)) {
                    newSlot = new ScriptableObject.GetterSlot(key, indexOrHash, slot.getAttributes());
                } else {
                    if (accessType != ScriptableObject.SlotAccess.CONVERT_ACCESSOR_TO_DATA || !(slot instanceof ScriptableObject.GetterSlot)) {
                        if (accessType == ScriptableObject.SlotAccess.MODIFY_CONST) {
                            return null;
                        }
                        return slot;
                    }
                    newSlot = new ScriptableObject.Slot(key, indexOrHash, slot.getAttributes());
                }
                newSlot.value = slot.value;
                newSlot.next = slot.next;
                if (slot == this.firstAdded) {
                    this.firstAdded = newSlot;
                } else {
                    ScriptableObject.Slot ps = this.firstAdded;
                    while (ps != null && ps.orderedNext != slot) {
                        ps = ps.orderedNext;
                    }
                    if (ps != null) {
                        ps.orderedNext = newSlot;
                    }
                }
                newSlot.orderedNext = slot.orderedNext;
                if (slot == this.lastAdded) {
                    this.lastAdded = newSlot;
                }
                if (prev == slot) {
                    this.slots[insertPos] = newSlot;
                } else {
                    prev.next = newSlot;
                }
                return newSlot;
            }
        }
        if (4 * (this.count + 1) > 3 * this.slots.length) {
            ScriptableObject.Slot[] newSlots = new ScriptableObject.Slot[this.slots.length * 2];
            copyTable(this.slots, newSlots);
            this.slots = newSlots;
        }
        ScriptableObject.Slot newSlotx = (ScriptableObject.Slot) (accessType == ScriptableObject.SlotAccess.MODIFY_GETTER_SETTER ? new ScriptableObject.GetterSlot(key, indexOrHash, 0) : new ScriptableObject.Slot(key, indexOrHash, 0));
        if (accessType == ScriptableObject.SlotAccess.MODIFY_CONST) {
            newSlotx.setAttributes(13);
        }
        this.insertNewSlot(newSlotx);
        return newSlotx;
    }

    @Override
    public void addSlot(ScriptableObject.Slot newSlot) {
        if (this.slots == null) {
            this.slots = new ScriptableObject.Slot[4];
        }
        this.insertNewSlot(newSlot);
    }

    private void insertNewSlot(ScriptableObject.Slot newSlot) {
        this.count++;
        if (this.lastAdded != null) {
            this.lastAdded.orderedNext = newSlot;
        }
        if (this.firstAdded == null) {
            this.firstAdded = newSlot;
        }
        this.lastAdded = newSlot;
        addKnownAbsentSlot(this.slots, newSlot);
    }

    @Override
    public void remove(Object key, int index, Context cx) {
        int indexOrHash = key != null ? key.hashCode() : index;
        if (this.count != 0) {
            int slotIndex = getSlotIndex(this.slots.length, indexOrHash);
            ScriptableObject.Slot prev = this.slots[slotIndex];
            ScriptableObject.Slot slot;
            for (slot = prev; slot != null && (slot.indexOrHash != indexOrHash || !Objects.equals(key, slot.name)); slot = slot.next) {
                prev = slot;
            }
            if (slot != null) {
                if ((slot.getAttributes() & 4) != 0) {
                    if (cx.isStrictMode()) {
                        throw ScriptRuntime.typeError1(cx, "msg.delete.property.with.configurable.false", key);
                    }
                    return;
                }
                this.count--;
                if (prev == slot) {
                    this.slots[slotIndex] = slot.next;
                } else {
                    prev.next = slot.next;
                }
                if (slot == this.firstAdded) {
                    prev = null;
                    this.firstAdded = slot.orderedNext;
                } else {
                    prev = this.firstAdded;
                    while (prev.orderedNext != slot) {
                        prev = prev.orderedNext;
                    }
                    prev.orderedNext = slot.orderedNext;
                }
                if (slot == this.lastAdded) {
                    this.lastAdded = prev;
                }
            }
        }
    }

    private static final class Iter implements Iterator<ScriptableObject.Slot> {

        private ScriptableObject.Slot next;

        Iter(ScriptableObject.Slot slot) {
            this.next = slot;
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public ScriptableObject.Slot next() {
            ScriptableObject.Slot ret = this.next;
            if (ret == null) {
                throw new NoSuchElementException();
            } else {
                this.next = this.next.orderedNext;
                return ret;
            }
        }
    }
}