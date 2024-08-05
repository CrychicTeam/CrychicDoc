package com.yungnickyoung.minecraft.yungsapi.module;

import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import com.yungnickyoung.minecraft.yungsapi.world.structure.YungJigsawStructure;
import net.minecraft.world.level.levelgen.structure.StructureType;

@AutoRegister("yungsapi")
public class StructureTypeModule {

    @AutoRegister("yung_jigsaw")
    public static StructureType<YungJigsawStructure> YUNG_JIGSAW = () -> YungJigsawStructure.CODEC;
}