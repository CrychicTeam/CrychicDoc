package net.blay09.mods.craftingtweaks;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.craftingtweaks.client.CraftingTweaksClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("craftingtweaks")
public class ForgeCraftingTweaks {

    public ForgeCraftingTweaks() {
        Balm.initialize("craftingtweaks", CraftingTweaks::initialize);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> BalmClient.initialize("craftingtweaks", CraftingTweaksClient::initialize));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(IMCHandler::processInterMod);
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31", (a, b) -> true));
    }
}