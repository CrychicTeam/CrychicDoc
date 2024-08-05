package net.minecraft.nbt.visitors;

import net.minecraft.nbt.StreamTagVisitor;
import net.minecraft.nbt.TagType;

public interface SkipAll extends StreamTagVisitor {

    SkipAll INSTANCE = new SkipAll() {
    };

    @Override
    default StreamTagVisitor.ValueResult visitEnd() {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    default StreamTagVisitor.ValueResult visit(String string0) {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    default StreamTagVisitor.ValueResult visit(byte byte0) {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    default StreamTagVisitor.ValueResult visit(short short0) {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    default StreamTagVisitor.ValueResult visit(int int0) {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    default StreamTagVisitor.ValueResult visit(long long0) {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    default StreamTagVisitor.ValueResult visit(float float0) {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    default StreamTagVisitor.ValueResult visit(double double0) {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    default StreamTagVisitor.ValueResult visit(byte[] byte0) {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    default StreamTagVisitor.ValueResult visit(int[] int0) {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    default StreamTagVisitor.ValueResult visit(long[] long0) {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    default StreamTagVisitor.ValueResult visitList(TagType<?> tagType0, int int1) {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    default StreamTagVisitor.EntryResult visitElement(TagType<?> tagType0, int int1) {
        return StreamTagVisitor.EntryResult.SKIP;
    }

    @Override
    default StreamTagVisitor.EntryResult visitEntry(TagType<?> tagType0) {
        return StreamTagVisitor.EntryResult.SKIP;
    }

    @Override
    default StreamTagVisitor.EntryResult visitEntry(TagType<?> tagType0, String string1) {
        return StreamTagVisitor.EntryResult.SKIP;
    }

    @Override
    default StreamTagVisitor.ValueResult visitContainerEnd() {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }

    @Override
    default StreamTagVisitor.ValueResult visitRootEntry(TagType<?> tagType0) {
        return StreamTagVisitor.ValueResult.CONTINUE;
    }
}