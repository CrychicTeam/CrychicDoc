package io.wispforest.lmft.forge;

import io.wispforest.lmft.LMFTCommon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("lmft")
public class LMFTForge {

    public LMFTForge() {
        LMFTCommon.init(FMLPaths.CONFIGDIR.get());
        MinecraftForge.EVENT_BUS.addListener(e -> LMFTCommon.sendMessage(e.getEntity()));
    }
}