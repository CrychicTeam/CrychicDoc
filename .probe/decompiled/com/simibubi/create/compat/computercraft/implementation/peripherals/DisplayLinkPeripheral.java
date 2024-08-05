package com.simibubi.create.compat.computercraft.implementation.peripherals;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlockEntity;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import dan200.computercraft.api.lua.LuaFunction;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;

public class DisplayLinkPeripheral extends SyncedPeripheral<DisplayLinkBlockEntity> {

    public static final String TAG_KEY = "ComputerSourceList";

    private final AtomicInteger cursorX = new AtomicInteger();

    private final AtomicInteger cursorY = new AtomicInteger();

    public DisplayLinkPeripheral(DisplayLinkBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final void setCursorPos(int x, int y) {
        this.cursorX.set(x - 1);
        this.cursorY.set(y - 1);
    }

    @LuaFunction
    public final Object[] getCursorPos() {
        return new Object[] { this.cursorX.get() + 1, this.cursorY.get() + 1 };
    }

    @LuaFunction(mainThread = true)
    public final Object[] getSize() {
        DisplayTargetStats stats = this.blockEntity.activeTarget.provideStats(new DisplayLinkContext(this.blockEntity.m_58904_(), this.blockEntity));
        return new Object[] { stats.maxRows(), stats.maxColumns() };
    }

    @LuaFunction
    public final boolean isColor() {
        return false;
    }

    @LuaFunction
    public final boolean isColour() {
        return false;
    }

    @LuaFunction
    public final void write(String text) {
        ListTag tag = this.blockEntity.getSourceConfig().getList("ComputerSourceList", 8);
        int x = this.cursorX.get();
        int y = this.cursorY.get();
        for (int i = tag.size(); i <= y; i++) {
            tag.add(StringTag.valueOf(""));
        }
        StringBuilder builder = new StringBuilder(tag.getString(y));
        builder.append(" ".repeat(Math.max(0, x - builder.length())));
        builder.replace(x, x + text.length(), text);
        tag.set(y, (Tag) StringTag.valueOf(builder.toString()));
        synchronized ((DisplayLinkBlockEntity) this.blockEntity) {
            this.blockEntity.getSourceConfig().put("ComputerSourceList", tag);
        }
        this.cursorX.set(x + text.length());
    }

    @LuaFunction
    public final void clearLine() {
        ListTag tag = this.blockEntity.getSourceConfig().getList("ComputerSourceList", 8);
        if (tag.size() > this.cursorY.get()) {
            tag.set(this.cursorY.get(), (Tag) StringTag.valueOf(""));
        }
        synchronized ((DisplayLinkBlockEntity) this.blockEntity) {
            this.blockEntity.getSourceConfig().put("ComputerSourceList", tag);
        }
    }

    @LuaFunction
    public final void clear() {
        synchronized ((DisplayLinkBlockEntity) this.blockEntity) {
            this.blockEntity.getSourceConfig().put("ComputerSourceList", new ListTag());
        }
    }

    @LuaFunction(mainThread = true)
    public final void update() {
        this.blockEntity.tickSource();
    }

    @NotNull
    public String getType() {
        return "Create_DisplayLink";
    }
}