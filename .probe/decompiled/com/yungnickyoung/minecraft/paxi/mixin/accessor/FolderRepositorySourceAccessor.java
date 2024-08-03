package com.yungnickyoung.minecraft.paxi.mixin.accessor;

import java.nio.file.Path;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ FolderRepositorySource.class })
public interface FolderRepositorySourceAccessor {

    @Accessor
    PackType getPackType();

    @Accessor
    Path getFolder();
}