package net.cristellib.builtinpacks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.cristellib.CristelLib;
import net.cristellib.CristelLibExpectPlatform;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.util.Tuple;

public class BuiltInDataPacks {

    private static final List<Tuple<Tuple<Component, PackResources>, Supplier<Boolean>>> list = new ArrayList();

    public static void registerAlwaysOnPack(ResourceLocation path, String modid, Component displayName) {
        registerPack(path, modid, displayName, () -> true);
    }

    public static void registerPack(ResourceLocation path, String modid, Component displayName, Supplier<Boolean> supplier) {
        registerPack(CristelLibExpectPlatform.registerBuiltinResourcePack(path, displayName, modid), displayName, supplier);
    }

    public static void registerPack(PackResources packResource, Component displayName, Supplier<Boolean> supplier) {
        list.add(new Tuple<>(new Tuple<>(displayName, packResource), supplier));
    }

    public static void getPacks(Consumer<Pack> consumer) {
        if (!list.isEmpty()) {
            for (Tuple<Tuple<Component, PackResources>, Supplier<Boolean>> entry : list) {
                if ((Boolean) entry.getB().get()) {
                    PackResources packResources = entry.getA().getB();
                    Component displayName = entry.getA().getA();
                    if (packResources == null) {
                        CristelLib.LOGGER.error("Pack for " + displayName.getString() + " is null");
                    } else if (!packResources.getNamespaces(PackType.SERVER_DATA).isEmpty()) {
                        Pack pack = Pack.readMetaAndCreate(packResources.packId(), displayName, true, ignored -> packResources, PackType.SERVER_DATA, Pack.Position.TOP, new BuiltinResourcePackSource());
                        if (pack != null) {
                            consumer.accept(pack);
                            CristelLib.LOGGER.warn(displayName.getString() + " accepted!");
                        } else {
                            CristelLib.LOGGER.error(displayName.getString() + " couldn't be created");
                        }
                    } else {
                        CristelLib.LOGGER.error(displayName.getString() + " has no data");
                    }
                } else {
                    CristelLib.LOGGER.error(entry.getA().getA().getString() + " condition is false, skipping");
                }
            }
        }
    }
}