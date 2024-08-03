package io.github.lightman314.lightmanscurrency.common.capability.event_unlocks;

import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;

public interface IEventUnlocks {

    @Nonnull
    List<String> getUnlockedList();

    boolean isUnlocked(@Nonnull String var1);

    void unlock(@Nonnull String var1);

    void lock(@Nonnull String var1);

    boolean isDirty();

    void clean();

    CompoundTag save();

    void load(CompoundTag var1);

    void sync(@Nonnull List<String> var1);
}