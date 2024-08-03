package vazkii.patchouli.forge.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegisterEvent;
import vazkii.patchouli.common.base.PatchouliSounds;
import vazkii.patchouli.common.book.BookRegistry;
import vazkii.patchouli.common.command.OpenBookCommand;
import vazkii.patchouli.common.handler.LecternEventHandler;
import vazkii.patchouli.common.handler.ReloadContentsHandler;
import vazkii.patchouli.common.item.ItemModBook;
import vazkii.patchouli.common.item.PatchouliItems;
import vazkii.patchouli.forge.network.ForgeNetworkHandler;

@EventBusSubscriber(modid = "patchouli", bus = Bus.MOD)
@Mod("patchouli")
public class ForgeModInitializer {

    public ForgeModInitializer() {
        ForgePatchouliConfig.setup();
    }

    @SubscribeEvent
    public static void register(RegisterEvent evt) {
        evt.register(Registries.SOUND_EVENT, rh -> PatchouliSounds.submitRegistrations(rh::register));
        evt.register(Registries.ITEM, rh -> PatchouliItems.submitItemRegistrations(rh::register));
        evt.register(Registries.RECIPE_SERIALIZER, rh -> PatchouliItems.submitRecipeSerializerRegistrations(rh::register));
    }

    @SubscribeEvent
    public static void processCreativeTabs(BuildCreativeModeTabContentsEvent evt) {
        BookRegistry.INSTANCE.books.values().forEach(b -> {
            if (!b.noBook) {
                ItemStack book = ItemModBook.forBook(b);
                if (evt.getTab() == CreativeModeTabs.searchTab()) {
                    evt.m_246342_(book);
                } else if (b.creativeTab != null && evt.getTab() == CreativeModeTabRegistry.getTab(b.creativeTab)) {
                    evt.m_246342_(book);
                }
            }
        });
    }

    @SubscribeEvent
    public static void onInitialize(FMLCommonSetupEvent evt) {
        MinecraftForge.EVENT_BUS.addListener(e -> OpenBookCommand.register(e.getDispatcher()));
        MinecraftForge.EVENT_BUS.addListener(e -> {
            InteractionResult result = LecternEventHandler.rightClick(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
            if (result.consumesAction()) {
                e.setCanceled(true);
                e.setCancellationResult(result);
            }
        });
        ForgeNetworkHandler.registerMessages();
        BookRegistry.INSTANCE.init();
        MinecraftForge.EVENT_BUS.addListener(e -> ReloadContentsHandler.dataReloaded(e.getServer()));
    }
}