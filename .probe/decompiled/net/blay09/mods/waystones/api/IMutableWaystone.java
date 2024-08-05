package net.blay09.mods.waystones.api;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface IMutableWaystone {

    void setName(String var1);

    void setGlobal(boolean var1);

    void setDimension(ResourceKey<Level> var1);

    void setPos(BlockPos var1);

    void setOwnerUid(UUID var1);
}