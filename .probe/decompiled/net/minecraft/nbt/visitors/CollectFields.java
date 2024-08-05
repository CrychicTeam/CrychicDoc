package net.minecraft.nbt.visitors;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StreamTagVisitor;
import net.minecraft.nbt.TagType;

public class CollectFields extends CollectToTag {

    private int fieldsToGetCount;

    private final Set<TagType<?>> wantedTypes;

    private final Deque<FieldTree> stack = new ArrayDeque();

    public CollectFields(FieldSelector... fieldSelector0) {
        this.fieldsToGetCount = fieldSelector0.length;
        Builder<TagType<?>> $$1 = ImmutableSet.builder();
        FieldTree $$2 = FieldTree.createRoot();
        for (FieldSelector $$3 : fieldSelector0) {
            $$2.addEntry($$3);
            $$1.add($$3.type());
        }
        this.stack.push($$2);
        $$1.add(CompoundTag.TYPE);
        this.wantedTypes = $$1.build();
    }

    @Override
    public StreamTagVisitor.ValueResult visitRootEntry(TagType<?> tagType0) {
        return tagType0 != CompoundTag.TYPE ? StreamTagVisitor.ValueResult.HALT : super.visitRootEntry(tagType0);
    }

    @Override
    public StreamTagVisitor.EntryResult visitEntry(TagType<?> tagType0) {
        FieldTree $$1 = (FieldTree) this.stack.element();
        if (this.m_197714_() > $$1.depth()) {
            return super.visitEntry(tagType0);
        } else if (this.fieldsToGetCount <= 0) {
            return StreamTagVisitor.EntryResult.HALT;
        } else {
            return !this.wantedTypes.contains(tagType0) ? StreamTagVisitor.EntryResult.SKIP : super.visitEntry(tagType0);
        }
    }

    @Override
    public StreamTagVisitor.EntryResult visitEntry(TagType<?> tagType0, String string1) {
        FieldTree $$2 = (FieldTree) this.stack.element();
        if (this.m_197714_() > $$2.depth()) {
            return super.visitEntry(tagType0, string1);
        } else if ($$2.selectedFields().remove(string1, tagType0)) {
            this.fieldsToGetCount--;
            return super.visitEntry(tagType0, string1);
        } else {
            if (tagType0 == CompoundTag.TYPE) {
                FieldTree $$3 = (FieldTree) $$2.fieldsToRecurse().get(string1);
                if ($$3 != null) {
                    this.stack.push($$3);
                    return super.visitEntry(tagType0, string1);
                }
            }
            return StreamTagVisitor.EntryResult.SKIP;
        }
    }

    @Override
    public StreamTagVisitor.ValueResult visitContainerEnd() {
        if (this.m_197714_() == ((FieldTree) this.stack.element()).depth()) {
            this.stack.pop();
        }
        return super.visitContainerEnd();
    }

    public int getMissingFieldCount() {
        return this.fieldsToGetCount;
    }
}