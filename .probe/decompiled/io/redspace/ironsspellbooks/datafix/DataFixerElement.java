package io.redspace.ironsspellbooks.datafix;

import java.nio.charset.StandardCharsets;
import java.util.List;
import net.minecraft.nbt.CompoundTag;

public abstract class DataFixerElement {

    private List<byte[]> preScanData;

    public final List<byte[]> preScanValueBytes() {
        if (this.preScanData == null) {
            this.preScanData = this.preScanValuesToMatch().stream().map(item -> item.getBytes(StandardCharsets.UTF_8)).toList();
        }
        return this.preScanData;
    }

    public abstract List<String> preScanValuesToMatch();

    public abstract boolean runFixer(CompoundTag var1);
}