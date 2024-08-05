package net.minecraft.nbt.visitors;

import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StreamTagVisitor;
import net.minecraft.nbt.TagType;

public class SkipFields extends CollectToTag {

    private final Deque<FieldTree> stack = new ArrayDeque();

    public SkipFields(FieldSelector... fieldSelector0) {
        FieldTree $$1 = FieldTree.createRoot();
        for (FieldSelector $$2 : fieldSelector0) {
            $$1.addEntry($$2);
        }
        this.stack.push($$1);
    }

    @Override
    public StreamTagVisitor.EntryResult visitEntry(TagType<?> tagType0, String string1) {
        FieldTree $$2 = (FieldTree) this.stack.element();
        if ($$2.isSelected(tagType0, string1)) {
            return StreamTagVisitor.EntryResult.SKIP;
        } else {
            if (tagType0 == CompoundTag.TYPE) {
                FieldTree $$3 = (FieldTree) $$2.fieldsToRecurse().get(string1);
                if ($$3 != null) {
                    this.stack.push($$3);
                }
            }
            return super.visitEntry(tagType0, string1);
        }
    }

    @Override
    public StreamTagVisitor.ValueResult visitContainerEnd() {
        if (this.m_197714_() == ((FieldTree) this.stack.element()).depth()) {
            this.stack.pop();
        }
        return super.visitContainerEnd();
    }
}