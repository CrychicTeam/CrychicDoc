package dev.latvian.mods.kubejs.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.bindings.event.StartupEvents;
import dev.latvian.mods.kubejs.bindings.event.WorldgenEvents;
import dev.latvian.mods.kubejs.entity.forge.LivingEntityDropsEventJS;
import dev.latvian.mods.kubejs.item.creativetab.CreativeTabCallback;
import dev.latvian.mods.kubejs.item.creativetab.CreativeTabEvent;
import dev.latvian.mods.kubejs.item.creativetab.KubeJSCreativeTabs;
import dev.latvian.mods.kubejs.item.forge.ItemDestroyedEventJS;
import dev.latvian.mods.kubejs.platform.forge.IngredientForgeHelper;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

@Mod("kubejs")
public class KubeJSForge {

    public KubeJSForge() throws Throwable {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus("kubejs", bus);
        bus.addListener(EventPriority.LOW, KubeJSForge::loadComplete);
        bus.addListener(EventPriority.LOW, KubeJSForge::initRegistries);
        bus.addListener(EventPriority.LOW, KubeJSForge::commonSetup);
        bus.addListener(EventPriority.LOW, KubeJSForge::creativeTab);
        KubeJS.instance = new KubeJS();
        KubeJS.instance.setup();
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31", (a, b) -> true));
        MinecraftForge.EVENT_BUS.addListener(KubeJSForge::itemDestroyed);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, KubeJSForge::livingDrops);
        if (!CommonProperties.get().serverOnly) {
            ForgeMod.enableMilkFluid();
            IngredientForgeHelper.register();
            KubeJSCreativeTabs.init();
        }
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> new KubeJSForgeClient());
    }

    private static void initRegistries(RegisterEvent event) {
        RegistryInfo<?> info = RegistryInfo.of(event.getRegistryKey());
        info.registerObjects((id, supplier) -> event.register(UtilsJS.cast(info.key), id, supplier));
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        WorldgenEvents.post();
    }

    private static void creativeTab(BuildCreativeModeTabContentsEvent event) {
        ResourceLocation tabId = event.getTabKey().location();
        if (StartupEvents.MODIFY_CREATIVE_TAB.hasListeners(tabId)) {
            StartupEvents.MODIFY_CREATIVE_TAB.post(ScriptType.STARTUP, tabId, new CreativeTabEvent(event.getTab(), event.hasPermissions(), new KubeJSForge.CreativeTabCallbackForge(event)));
        }
    }

    private static void loadComplete(FMLLoadCompleteEvent event) {
        KubeJS.instance.loadComplete();
    }

    private static void itemDestroyed(PlayerDestroyItemEvent event) {
        if (ForgeKubeJSEvents.ITEM_DESTROYED.hasListeners()) {
            ForgeKubeJSEvents.ITEM_DESTROYED.post(event.getEntity(), event.getOriginal().getItem(), new ItemDestroyedEventJS(event));
        }
    }

    private static void livingDrops(LivingDropsEvent event) {
        if (ForgeKubeJSEvents.ENTITY_DROPS.hasListeners()) {
            LivingEntityDropsEventJS e = new LivingEntityDropsEventJS(event);
            if (ForgeKubeJSEvents.ENTITY_DROPS.post(event.getEntity(), e.getEntity().m_6095_(), e).interruptFalse()) {
                event.setCanceled(true);
            } else if (e.eventDrops != null) {
                event.getDrops().clear();
                event.getDrops().addAll(e.eventDrops);
            }
        }
    }

    private static record CreativeTabCallbackForge(BuildCreativeModeTabContentsEvent event) implements CreativeTabCallback {

        @Override
        public void addAfter(ItemStack order, ItemStack[] items, CreativeModeTab.TabVisibility visibility) {
            for (ItemStack item : items) {
                this.event.accept(item, visibility);
            }
        }

        @Override
        public void addBefore(ItemStack order, ItemStack[] items, CreativeModeTab.TabVisibility visibility) {
            for (ItemStack item : items) {
                this.event.accept(item, visibility);
            }
        }

        @Override
        public void remove(Ingredient filter, boolean removeDisplay, boolean removeSearch) {
            ArrayList<Entry<ItemStack, CreativeModeTab.TabVisibility>> entries = new ArrayList();
            for (Entry<ItemStack, CreativeModeTab.TabVisibility> entry : this.event.getEntries()) {
                if (filter.test((ItemStack) entry.getKey())) {
                    CreativeModeTab.TabVisibility visibility = (CreativeModeTab.TabVisibility) entry.getValue();
                    if (removeDisplay && removeSearch) {
                        visibility = null;
                    }
                    if (removeDisplay && visibility == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS) {
                        visibility = CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY;
                    }
                    if (removeSearch && visibility == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS) {
                        visibility = CreativeModeTab.TabVisibility.PARENT_TAB_ONLY;
                    }
                    entries.add(new SimpleEntry((ItemStack) entry.getKey(), visibility));
                }
            }
            for (Entry<ItemStack, CreativeModeTab.TabVisibility> entryx : entries) {
                if (entryx.getValue() == null) {
                    this.event.getEntries().remove((ItemStack) entryx.getKey());
                } else {
                    this.event.getEntries().put((ItemStack) entryx.getKey(), (CreativeModeTab.TabVisibility) entryx.getValue());
                }
            }
        }
    }
}