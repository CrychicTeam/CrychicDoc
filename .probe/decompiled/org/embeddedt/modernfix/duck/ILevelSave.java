package org.embeddedt.modernfix.duck;

import net.minecraft.world.level.storage.LevelStorageSource;

public interface ILevelSave {

    void runWorldPersistenceHooks(LevelStorageSource var1);
}