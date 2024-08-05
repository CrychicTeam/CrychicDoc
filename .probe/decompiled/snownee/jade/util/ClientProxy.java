package snownee.jade.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.ItemDecoratorHandler;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.Nullable;
import snownee.jade.Jade;
import snownee.jade.JadeClient;
import snownee.jade.addon.harvest.SpecialToolHandler;
import snownee.jade.addon.harvest.ToolHandler;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.fluid.JadeFluidObject;
import snownee.jade.api.ui.IElement;
import snownee.jade.command.JadeClientCommand;
import snownee.jade.compat.JEICompat;
import snownee.jade.gui.BaseOptionsScreen;
import snownee.jade.gui.HomeConfigScreen;
import snownee.jade.impl.ObjectDataCenter;
import snownee.jade.impl.theme.ThemeHelper;
import snownee.jade.impl.ui.FluidStackElement;
import snownee.jade.network.RequestEntityPacket;
import snownee.jade.network.RequestTilePacket;
import snownee.jade.overlay.DatapackBlockManager;
import snownee.jade.overlay.OverlayRenderer;
import snownee.jade.overlay.WailaTickHandler;

public final class ClientProxy {

    private static final List<KeyMapping> keys = Lists.newArrayList();

    private static final List<PreparableReloadListener> listeners = Lists.newArrayList();

    public static boolean hasJEI = CommonProxy.isModLoaded("jei");

    public static boolean hasREI = false;

    public static boolean hasFastScroll = CommonProxy.isModLoaded("fastscroll");

    public static boolean maybeLowVisionUser = CommonProxy.isModLoaded("minecraft_access");

    private static boolean bossbarShown;

    private static int bossbarHeight;

    public static Optional<String> getModName(String namespace) {
        String modMenuKey = "modmenu.nameTranslation.%s".formatted(namespace);
        return I18n.exists(modMenuKey) ? Optional.of(I18n.get(modMenuKey)) : ModList.get().getModContainerById(namespace).map(ModContainer::getModInfo).map(IModInfo::getDisplayName).filter(Predicate.not(Strings::isNullOrEmpty));
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(ClientProxy::onEntityJoin);
        MinecraftForge.EVENT_BUS.addListener(ClientProxy::onEntityLeave);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, ClientProxy::onTooltip);
        MinecraftForge.EVENT_BUS.addListener(ClientProxy::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(ClientProxy::onPlayerLeave);
        MinecraftForge.EVENT_BUS.addListener(ClientProxy::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(ClientProxy::onKeyPressed);
        MinecraftForge.EVENT_BUS.addListener(ClientProxy::onGui);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, true, ClientProxy::onDrawBossBar);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, RenderGuiEvent.Post.class, event -> {
            if (Minecraft.getInstance().screen == null) {
                onRenderTick(event.getGuiGraphics());
            }
        });
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ScreenEvent.Render.Post.class, event -> onRenderTick(event.getGuiGraphics()));
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(EventPriority.NORMAL, false, RegisterClientReloadListenersEvent.class, event -> {
            event.registerReloadListener(ThemeHelper.INSTANCE);
            listeners.forEach(event::registerReloadListener);
            listeners.clear();
        });
        modEventBus.addListener(EventPriority.NORMAL, false, RegisterKeyMappingsEvent.class, event -> {
            keys.forEach(event::register);
            keys.clear();
        });
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new HomeConfigScreen(screen)));
        for (int i = 320; i < 330; i++) {
            InputConstants.Key key = InputConstants.Type.KEYSYM.getOrCreate(i);
            key.displayName = new LazyLoadedValue<>(() -> Component.translatable(key.getName()));
        }
        JadeClient.init();
    }

    private static void onEntityJoin(EntityJoinLevelEvent event) {
        DatapackBlockManager.onEntityJoin(event.getEntity());
    }

    private static void onEntityLeave(EntityLeaveLevelEvent event) {
        DatapackBlockManager.onEntityLeave(event.getEntity());
    }

    private static void onTooltip(ItemTooltipEvent event) {
        JadeClient.onTooltip(event.getToolTip(), event.getItemStack(), event.getFlags());
    }

    public static void onRenderTick(GuiGraphics guiGraphics) {
        try {
            OverlayRenderer.renderOverlay478757(guiGraphics);
        } catch (Throwable var5) {
            WailaExceptionHandler.handleErr(var5, null, null, null);
        } finally {
            bossbarShown = false;
        }
    }

    private static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            try {
                WailaTickHandler.instance().tickClient();
            } catch (Throwable var2) {
                WailaExceptionHandler.handleErr(var2, null, null, null);
            }
        }
    }

    private static void onPlayerLeave(ClientPlayerNetworkEvent.LoggingOut event) {
        ObjectDataCenter.serverConnected = false;
    }

    public static void registerCommands(RegisterClientCommandsEvent event) {
        JadeClientCommand.register(event.getDispatcher());
    }

    private static void onKeyPressed(InputEvent.Key event) {
        JadeClient.onKeyPressed(event.getAction());
        if (JadeClient.showUses != null && hasJEI) {
            JEICompat.onKeyPressed(1);
        }
    }

    private static void onGui(ScreenEvent.Init event) {
        JadeClient.onGui(event.getScreen());
    }

    public static KeyMapping registerKeyBinding(String desc, int defaultKey) {
        KeyMapping key = new KeyMapping("key.jade." + desc, KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM.getOrCreate(defaultKey), "modmenu.nameTranslation.jade");
        keys.add(key);
        return key;
    }

    public static boolean shouldRegisterRecipeViewerKeys() {
        return hasJEI || hasREI;
    }

    public static void requestBlockData(BlockAccessor accessor) {
        CommonProxy.NETWORK.sendToServer(new RequestTilePacket(accessor));
    }

    public static void requestEntityData(EntityAccessor accessor) {
        CommonProxy.NETWORK.sendToServer(new RequestEntityPacket(accessor));
    }

    public static IElement elementFromLiquid(LiquidBlock block) {
        Fluid fluid = block.getFluid();
        return new FluidStackElement(JadeFluidObject.of(fluid));
    }

    public static void registerReloadListener(ResourceManagerReloadListener listener) {
        listeners.add(listener);
    }

    private static void onDrawBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        IWailaConfig.BossBarOverlapMode mode = Jade.CONFIG.get().getGeneral().getBossBarOverlapMode();
        if (mode != IWailaConfig.BossBarOverlapMode.NO_OPERATION) {
            if (mode == IWailaConfig.BossBarOverlapMode.HIDE_BOSS_BAR && OverlayRenderer.shown) {
                event.setCanceled(true);
            } else if (mode != IWailaConfig.BossBarOverlapMode.PUSH_DOWN || !event.isCanceled()) {
                bossbarHeight = event.getY() + event.getIncrement();
                bossbarShown = true;
            }
        }
    }

    @Nullable
    public static Rect2i getBossBarRect() {
        if (!bossbarShown) {
            return null;
        } else {
            int i = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int k = i / 2 - 91;
            return new Rect2i(k, 12, 182, bossbarHeight - 12);
        }
    }

    public static boolean isShowDetailsPressed() {
        return JadeClient.showDetails.isDown();
    }

    public static boolean shouldShowWithOverlay(Minecraft mc, @Nullable Screen screen) {
        return screen == null || screen instanceof BaseOptionsScreen || screen instanceof ChatScreen;
    }

    public static void getFluidSpriteAndColor(JadeFluidObject fluid, BiConsumer<TextureAtlasSprite, Integer> consumer) {
        Fluid type = fluid.getType();
        FluidStack fluidStack = CommonProxy.toFluidStack(fluid);
        Minecraft minecraft = Minecraft.getInstance();
        IClientFluidTypeExtensions handler = IClientFluidTypeExtensions.of(type);
        ResourceLocation fluidStill = handler.getStillTexture(fluidStack);
        TextureAtlasSprite fluidStillSprite = (TextureAtlasSprite) minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
        int fluidColor = handler.getTintColor(fluidStack);
        if (OverlayRenderer.alpha != 1.0F) {
            fluidColor = IWailaConfig.IConfigOverlay.applyAlpha(fluidColor, OverlayRenderer.alpha);
        }
        consumer.accept(fluidStillSprite, fluidColor);
    }

    public static KeyMapping registerDetailsKeyBinding() {
        return registerKeyBinding("show_details", 340);
    }

    public static ToolHandler createSwordToolHandler() {
        SpecialToolHandler handler = new SpecialToolHandler("sword", Items.WOODEN_SWORD.getDefaultInstance());
        handler.blocks.add(Blocks.COBWEB);
        return handler;
    }

    public static void renderItemDecorationsExtra(GuiGraphics guiGraphics, Font font, ItemStack stack, int x, int y, String text) {
        ItemDecoratorHandler.of(stack).render(guiGraphics, font, stack, x, y);
    }

    public static InputConstants.Key getBoundKeyOf(KeyMapping keyMapping) {
        return keyMapping.getKey();
    }
}