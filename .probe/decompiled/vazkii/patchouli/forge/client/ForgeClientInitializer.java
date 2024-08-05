package vazkii.patchouli.forge.client;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.base.BookModel;
import vazkii.patchouli.client.base.ClientAdvancements;
import vazkii.patchouli.client.base.ClientTicker;
import vazkii.patchouli.client.base.PersistentData;
import vazkii.patchouli.client.book.BookContentResourceListenerLoader;
import vazkii.patchouli.client.book.ClientBookRegistry;
import vazkii.patchouli.client.handler.BookRightClickHandler;
import vazkii.patchouli.client.handler.MultiblockVisualizationHandler;
import vazkii.patchouli.client.handler.TooltipHandler;
import vazkii.patchouli.common.book.BookRegistry;
import vazkii.patchouli.common.item.ItemModBook;
import vazkii.patchouli.common.item.PatchouliItems;

@EventBusSubscriber(modid = "patchouli", bus = Bus.MOD, value = { Dist.CLIENT })
public class ForgeClientInitializer {

    private static final Lock BOOK_LOAD_LOCK = new ReentrantLock();

    private static final Condition BOOK_LOAD_CONDITION = BOOK_LOAD_LOCK.newCondition();

    private static boolean booksLoaded = false;

    public static void signalBooksLoaded() {
        BOOK_LOAD_LOCK.lock();
        booksLoaded = true;
        BOOK_LOAD_CONDITION.signalAll();
        BOOK_LOAD_LOCK.unlock();
    }

    private static List<ResourceLocation> getBookModels() {
        BOOK_LOAD_LOCK.lock();
        List var0;
        try {
            while (!booksLoaded) {
                BOOK_LOAD_CONDITION.awaitUninterruptibly();
            }
            var0 = BookRegistry.INSTANCE.books.values().stream().map(b -> b.model).toList();
        } finally {
            BOOK_LOAD_LOCK.unlock();
        }
        return var0;
    }

    @SubscribeEvent
    public static void modelRegistry(ModelEvent.RegisterAdditional e) {
        getBookModels().stream().map(model -> new ModelResourceLocation(model, "inventory")).forEach(e::register);
        ItemPropertyFunction prop = (stack, world, entity, seed) -> ItemModBook.getCompletion(stack);
        ItemProperties.register(PatchouliItems.BOOK, new ResourceLocation("patchouli", "completion"), prop);
    }

    @SubscribeEvent
    public static void registerReloadListeners(RegisterClientReloadListenersEvent e) {
        e.registerReloadListener(BookContentResourceListenerLoader.INSTANCE);
        e.registerReloadListener((ResourceManagerReloadListener) manager -> {
            if (Minecraft.getInstance().level != null) {
                PatchouliAPI.LOGGER.info("Reloading resource pack-based books");
                ClientBookRegistry.INSTANCE.reload();
            } else {
                PatchouliAPI.LOGGER.debug("Not reloading resource pack-based books as client world is missing");
            }
        });
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent evt) {
        evt.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "book_right_click", (gui, poseStack, partialTick, width, height) -> BookRightClickHandler.onRenderHUD(poseStack, partialTick));
        evt.registerBelow(VanillaGuiOverlay.BOSS_EVENT_PROGRESS.id(), "multiblock_progress", (gui, poseStack, partialTick, width, height) -> MultiblockVisualizationHandler.onRenderHUD(poseStack, partialTick));
    }

    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent evt) {
        ClientBookRegistry.INSTANCE.init();
        PersistentData.setup();
        MinecraftForge.EVENT_BUS.addListener(e -> {
            if (e.phase == TickEvent.Phase.END) {
                ClientTicker.endClientTick(Minecraft.getInstance());
            }
        });
        MinecraftForge.EVENT_BUS.addListener(e -> BookRightClickHandler.onRightClick(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec()));
        MinecraftForge.EVENT_BUS.addListener(e -> {
            InteractionResult result = MultiblockVisualizationHandler.onPlayerInteract(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
            if (result.consumesAction()) {
                e.setCanceled(true);
                e.setCancellationResult(result);
            }
        });
        MinecraftForge.EVENT_BUS.addListener(e -> {
            if (e.phase == TickEvent.Phase.END) {
                MultiblockVisualizationHandler.onClientTick(Minecraft.getInstance());
            }
        });
        MinecraftForge.EVENT_BUS.addListener(e -> {
            if (e.phase == TickEvent.Phase.START) {
                ClientTicker.renderTickStart(e.renderTickTime);
            } else {
                ClientTicker.renderTickEnd();
            }
        });
        MinecraftForge.EVENT_BUS.addListener(e -> ClientAdvancements.playerLogout());
        MinecraftForge.EVENT_BUS.addListener(e -> TooltipHandler.onTooltip(e.getGraphics(), e.getItemStack(), e.getX(), e.getY()));
    }

    @SubscribeEvent
    public static void replaceBookModel(ModelEvent.ModifyBakingResult evt) {
        ModelResourceLocation key = new ModelResourceLocation(PatchouliItems.BOOK_ID, "inventory");
        evt.getModels().computeIfPresent(key, (k, oldModel) -> new BookModel(oldModel, evt.getModelBakery()));
    }
}