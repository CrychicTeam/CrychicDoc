package io.github.lightman314.lightmanscurrency.client.resourcepacks;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.client.resourcepacks.data.item_trader.ItemPositionBlockManager;
import io.github.lightman314.lightmanscurrency.client.resourcepacks.data.item_trader.ItemPositionManager;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "lightmanscurrency", bus = Bus.MOD)
public class LCResourcePacks {

    private static final List<LCResourcePacks.CustomResourcePack> packList = new ArrayList();

    private LCResourcePacks() {
    }

    public static void registerPack(@Nonnull String modid, @Nonnull String path, @Nonnull Component name) {
        registerPack(new LCResourcePacks.CustomResourcePack(modid, path, name));
    }

    public static void registerPack(@Nonnull LCResourcePacks.CustomResourcePack pack) {
        if (!packList.contains(pack)) {
            packList.add(pack);
        }
    }

    @SubscribeEvent
    public static void registerPackSource(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            for (LCResourcePacks.CustomResourcePack pack : packList) {
                pack.addToRepository(p -> event.addRepositorySource(consumer -> consumer.accept(p)));
            }
        }
    }

    @SubscribeEvent
    public static void registerResourceListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(ItemPositionManager.INSTANCE);
        event.registerReloadListener(ItemPositionBlockManager.INSTANCE);
    }

    static {
        registerPack("lightmanscurrency", "RupeePack", LCText.RESOURCE_PACK_RUPEES.get());
        registerPack("lightmanscurrency", "CloserItemsPack", LCText.RESOURCE_PACK_CLOSER_ITEMS.get());
        registerPack("lightmanscurrency", "LegacyCoins", LCText.RESOURCE_PACK_LEGACY_COINS.get());
    }

    public static class CustomResourcePack {

        private final String modid;

        private final String path;

        private final Component name;

        public CustomResourcePack(@Nonnull String modid, @Nonnull String path, @Nonnull Component name) {
            this.modid = modid;
            this.path = path;
            this.name = name;
        }

        public void addToRepository(@Nonnull Consumer<Pack> consumer) {
            Path resourcePath = ModList.get().getModFileById(this.modid).getFile().findResource(new String[] { this.path });
            Pack pack = Pack.readMetaAndCreate("builtin/" + this.path, this.name, false, path -> new PathPackResources(path, resourcePath, false), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN);
            if (pack == null) {
                LightmansCurrency.LogWarning("Custom Resource Pack of '" + this.modid + "/" + this.path + " failed to load properly!");
            } else {
                consumer.accept(pack);
            }
        }
    }
}