package net.blay09.mods.defaultoptions;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.client.keymappings.KeyModifier;
import net.blay09.mods.defaultoptions.mixin.ForgeKeyMappingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;

@Mod("defaultoptions")
public class ForgeDefaultOptions {

    public ForgeDefaultOptions() {
        PlatformBindings.INSTANCE = new PlatformBindings() {

            @Override
            public void setDefaultKeyModifier(KeyMapping keyMapping, KeyModifier keyModifier) {
                net.minecraftforge.client.settings.KeyModifier forgeKeyModifier = switch(keyModifier) {
                    case ALT ->
                        net.minecraftforge.client.settings.KeyModifier.ALT;
                    case SHIFT ->
                        net.minecraftforge.client.settings.KeyModifier.SHIFT;
                    case CONTROL ->
                        net.minecraftforge.client.settings.KeyModifier.CONTROL;
                    default ->
                        net.minecraftforge.client.settings.KeyModifier.NONE;
                };
                ((ForgeKeyMappingAccessor) keyMapping).setKeyModifierDefault(forgeKeyModifier);
            }

            @Override
            public void setKeyModifier(KeyMapping keyMapping, KeyModifier keyModifier) {
                keyMapping.setKeyModifierAndCode(toForge(keyModifier), keyMapping.getKey());
            }

            @Override
            public KeyModifier getKeyModifier(KeyMapping keyMapping) {
                return fromForge(keyMapping.getKeyModifier());
            }

            @Override
            public KeyModifier getDefaultKeyModifier(KeyMapping keyMapping) {
                return fromForge(keyMapping.getDefaultKeyModifier());
            }

            private static KeyModifier fromForge(net.minecraftforge.client.settings.KeyModifier keyModifier) {
                return switch(keyModifier) {
                    case NONE ->
                        KeyModifier.NONE;
                    case SHIFT ->
                        KeyModifier.SHIFT;
                    case CONTROL ->
                        KeyModifier.CONTROL;
                    case ALT ->
                        KeyModifier.ALT;
                };
            }

            private static net.minecraftforge.client.settings.KeyModifier toForge(KeyModifier keyModifier) {
                return switch(keyModifier) {
                    case ALT ->
                        net.minecraftforge.client.settings.KeyModifier.ALT;
                    case SHIFT ->
                        net.minecraftforge.client.settings.KeyModifier.SHIFT;
                    case CONTROL ->
                        net.minecraftforge.client.settings.KeyModifier.CONTROL;
                    case NONE ->
                        net.minecraftforge.client.settings.KeyModifier.NONE;
                };
            }
        };
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            Balm.initialize("defaultoptions", () -> {
            });
            BalmClient.initialize("defaultoptions", DefaultOptions::initialize);
        });
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31", (a, b) -> true));
    }
}