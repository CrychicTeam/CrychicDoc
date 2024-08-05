package io.redspace.ironsspellbooks.entity.mobs.goals;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public interface HomeOwner {

    @Nullable
    BlockPos getHome();

    void setHome(BlockPos var1);

    default void serializeHome(HomeOwner self, CompoundTag tag) {
        if (self.getHome() != null) {
            tag.putIntArray("HomePos", new int[] { this.getHome().m_123341_(), this.getHome().m_123342_(), this.getHome().m_123343_() });
        }
    }

    default void deserializeHome(HomeOwner self, CompoundTag tag) {
        if (tag.contains("HomePos")) {
            int[] home = tag.getIntArray("HomePos");
            self.setHome(new BlockPos(home[0], home[1], home[2]));
        }
    }
}