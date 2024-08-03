package net.minecraft.nbt.visitors;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StreamTagVisitor;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagType;

public class CollectToTag implements StreamTagVisitor {

    private String lastId = "";

    @Nullable
    private Tag rootTag;

    private final Deque<Consumer<Tag>> consumerStack = new ArrayDeque();

    @Nullable
    public Tag getResult() {
        return this.rootTag;
    }

    protected int depth() {
        return this.consumerStack.size();
    }

    private void appendEntry(Tag tag0) {
        ((Consumer) this.consumerStack.getLast()).accept(tag0);
    }

    @Override
    public StreamTagVisitor.ValueResult visitEnd() {
        this.appendEntry(EndTag.INSTANCE);
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(String string0) {
        this.appendEntry(StringTag.valueOf(string0));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(byte byte0) {
        this.appendEntry(ByteTag.valueOf(byte0));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(short short0) {
        this.appendEntry(ShortTag.valueOf(short0));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(int int0) {
        this.appendEntry(IntTag.valueOf(int0));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(long long0) {
        this.appendEntry(LongTag.valueOf(long0));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(float float0) {
        this.appendEntry(FloatTag.valueOf(float0));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(double double0) {
        this.appendEntry(DoubleTag.valueOf(double0));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(byte[] byte0) {
        this.appendEntry(new ByteArrayTag(byte0));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(int[] int0) {
        this.appendEntry(new IntArrayTag(int0));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visit(long[] long0) {
        this.appendEntry(new LongArrayTag(long0));
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visitList(TagType<?> tagType0, int int1) {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.EntryResult visitElement(TagType<?> tagType0, int int1) {
        this.enterContainerIfNeeded(tagType0);
        return StreamTagVisitor.EntryResult.ENTER;
    }

    @Override
    public StreamTagVisitor.EntryResult visitEntry(TagType<?> tagType0) {
        return StreamTagVisitor.EntryResult.ENTER;
    }

    @Override
    public StreamTagVisitor.EntryResult visitEntry(TagType<?> tagType0, String string1) {
        this.lastId = string1;
        this.enterContainerIfNeeded(tagType0);
        return StreamTagVisitor.EntryResult.ENTER;
    }

    private void enterContainerIfNeeded(TagType<?> tagType0) {
        if (tagType0 == ListTag.TYPE) {
            ListTag $$1 = new ListTag();
            this.appendEntry($$1);
            this.consumerStack.addLast($$1::add);
        } else if (tagType0 == CompoundTag.TYPE) {
            CompoundTag $$2 = new CompoundTag();
            this.appendEntry($$2);
            this.consumerStack.addLast((Consumer) p_197703_ -> $$2.put(this.lastId, p_197703_));
        }
    }

    @Override
    public StreamTagVisitor.ValueResult visitContainerEnd() {
        this.consumerStack.removeLast();
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    public StreamTagVisitor.ValueResult visitRootEntry(TagType<?> tagType0) {
        if (tagType0 == ListTag.TYPE) {
            ListTag $$1 = new ListTag();
            this.rootTag = $$1;
            this.consumerStack.addLast($$1::add);
        } else if (tagType0 == CompoundTag.TYPE) {
            CompoundTag $$2 = new CompoundTag();
            this.rootTag = $$2;
            this.consumerStack.addLast((Consumer) p_197681_ -> $$2.put(this.lastId, p_197681_));
        } else {
            this.consumerStack.addLast((Consumer) p_197705_ -> this.rootTag = p_197705_);
        }
        return StreamTagVisitor.ValueResult.CONTINUE;
    }
}