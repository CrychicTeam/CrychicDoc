package io.redspace.ironsspellbooks.datafix;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
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
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagVisitor;

@ParametersAreNonnullByDefault
public class IronsTagTraverser implements TagVisitor {

    private final AtomicInteger changeCount;

    public IronsTagTraverser() {
        this.changeCount = new AtomicInteger(0);
    }

    private IronsTagTraverser(AtomicInteger changeCount) {
        this.changeCount = changeCount;
    }

    public boolean changesMade() {
        return this.changeCount.get() > 0;
    }

    public int totalChanges() {
        return this.changeCount.get();
    }

    public void visit(@Nullable Tag pTag) {
        if (pTag != null) {
            pTag.accept(this);
        }
    }

    @Override
    public void visitString(StringTag pTag) {
    }

    @Override
    public void visitByte(ByteTag pTag) {
    }

    @Override
    public void visitShort(ShortTag pTag) {
    }

    @Override
    public void visitInt(IntTag pTag) {
    }

    @Override
    public void visitLong(LongTag pTag) {
    }

    @Override
    public void visitFloat(FloatTag pTag) {
    }

    @Override
    public void visitDouble(DoubleTag pTag) {
    }

    @Override
    public void visitByteArray(ByteArrayTag pTag) {
    }

    @Override
    public void visitIntArray(IntArrayTag pTag) {
    }

    @Override
    public void visitLongArray(LongArrayTag pTag) {
    }

    @Override
    public void visitList(ListTag pTag) {
        for (int i = 0; i < pTag.size(); i++) {
            new IronsTagTraverser(this.changeCount).visit(pTag.get(i));
        }
    }

    @Override
    public void visitCompound(CompoundTag pTag) {
        if (DataFixerHelpers.doFixUps(pTag)) {
            this.changeCount.incrementAndGet();
        }
        List<String> list = Lists.newArrayList(pTag.getAllKeys());
        Collections.sort(list);
        for (String s : list) {
            new IronsTagTraverser(this.changeCount).visit(pTag.get(s));
        }
    }

    @Override
    public void visitEnd(EndTag pTag) {
    }
}