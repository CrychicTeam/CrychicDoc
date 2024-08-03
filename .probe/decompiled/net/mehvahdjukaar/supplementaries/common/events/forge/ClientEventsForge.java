package net.mehvahdjukaar.supplementaries.common.events.forge;

import com.mojang.datafixers.util.Either;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.client.QuiverArrowSelectGui;
import net.mehvahdjukaar.supplementaries.client.cannon.CannonController;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.funny.JarredHeadLayer;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.layers.QuiverLayer;
import net.mehvahdjukaar.supplementaries.client.renderers.forge.CannonChargeOverlayImpl;
import net.mehvahdjukaar.supplementaries.client.renderers.forge.QuiverArrowSelectGuiImpl;
import net.mehvahdjukaar.supplementaries.client.renderers.items.AltimeterItemRenderer;
import net.mehvahdjukaar.supplementaries.common.block.blocks.EndermanSkullBlock;
import net.mehvahdjukaar.supplementaries.common.events.ClientEvents;
import net.mehvahdjukaar.supplementaries.common.items.tooltip_components.SherdTooltip;
import net.mehvahdjukaar.supplementaries.common.misc.songs.SongsManager;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientEventsForge {

    private static double wobble;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(ClientEventsForge.class);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ClientEventsForge::onAddLayers);
        bus.addListener(ClientEventsForge::onPackReload);
        bus.addListener(ClientEventsForge::onAddGuiLayers);
        bus.addListener(ClientEventsForge::onRegisterSkullModels);
    }

    public static void onRegisterSkullModels(EntityRenderersEvent.CreateSkullModels event) {
        event.registerSkullModel(EndermanSkullBlock.TYPE, new SkullModel(event.getEntityModelSet().bakeLayer(ModelLayers.SKELETON_SKULL)));
        SkullBlockRenderer.SKIN_BY_TYPE.put(EndermanSkullBlock.TYPE, Supplementaries.res("textures/entity/enderman_head.png"));
    }

    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (String skinType : event.getSkins()) {
            LivingEntityRenderer<? extends Player, ? extends EntityModel<? extends Player>> renderer = event.getSkin(skinType);
            if (renderer != null) {
                renderer.addLayer(new QuiverLayer(renderer, false));
                renderer.addLayer(new JarredHeadLayer<>(renderer, event.getEntityModels()));
            }
        }
        LivingEntityRenderer<Skeleton, ? extends EntityModel<Skeleton>> renderer = event.getRenderer(EntityType.SKELETON);
        if (renderer != null) {
            renderer.addLayer(new QuiverLayer(renderer, true));
        }
        LivingEntityRenderer<Stray, ? extends EntityModel<Stray>> renderer2 = event.getRenderer(EntityType.STRAY);
        if (renderer2 != null) {
            renderer2.addLayer(new QuiverLayer(renderer2, true));
        }
    }

    public static void onAddGuiLayers(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "quiver_overlay", new QuiverArrowSelectGuiImpl());
        event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "cannon_charge_overlay", new CannonChargeOverlayImpl());
    }

    @SubscribeEvent
    public static void itemTooltip(ItemTooltipEvent event) {
        if (event.getEntity() != null) {
            ClientEvents.onItemTooltip(event.getItemStack(), event.getFlags(), event.getToolTip());
        }
    }

    @SubscribeEvent
    public static void screenInit(ScreenEvent.Init.Post event) {
        if (CompatHandler.CONFIGURED) {
            ClientEvents.addConfigButton(event.getScreen(), event.getListenersList(), event::addListener);
        }
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ClientEvents.onClientTick(Minecraft.getInstance());
        }
    }

    @SubscribeEvent
    public static void onMouseScrolled(InputEvent.MouseScrollingEvent event) {
        if (QuiverArrowSelectGui.isActive() && QuiverArrowSelectGui.onMouseScrolled(event.getScrollDelta())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        if (Minecraft.getInstance().screen == null && !ClientRegistry.QUIVER_KEYBIND.isUnbound() && event.getKey() == ClientRegistry.QUIVER_KEYBIND.getKey().getValue()) {
            int a = event.getAction();
            if (a == 2 || a == 1) {
                QuiverArrowSelectGui.setUsingKeybind(true);
            } else if (a == 0) {
                QuiverArrowSelectGui.setUsingKeybind(false);
            }
        }
        if (CannonController.isActive()) {
            CannonController.onKeyPressed(event.getKey(), event.getAction(), event.getModifiers());
        }
    }

    @SubscribeEvent
    public static void onKeyPress(MovementInputUpdateEvent event) {
    }

    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        Player p = Minecraft.getInstance().player;
        if (p != null && !Minecraft.getInstance().isPaused()) {
            boolean isOnRope = ClientEvents.isIsOnRope();
            if (isOnRope || wobble != 0.0) {
                double period = (Double) ClientConfigs.Blocks.ROPE_WOBBLE_PERIOD.get();
                double newWobble = ((double) p.f_19797_ + event.getPartialTick()) / period % 1.0;
                if (!isOnRope && newWobble < wobble) {
                    wobble = 0.0;
                } else {
                    wobble = newWobble;
                }
                event.setRoll((float) ((double) event.getRoll() + (double) Mth.sin((float) (wobble * 2.0 * Math.PI)) * (Double) ClientConfigs.Blocks.ROPE_WOBBLE_AMPLITUDE.get()));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(ScreenEvent.Opening event) {
        if (event.getNewScreen() instanceof DeathScreen && event.getCurrentScreen() instanceof ChatScreen cs && (Boolean) ClientConfigs.Tweaks.DEATH_CHAT.get()) {
            cs.m_5534_('-', 0);
            cs.keyPressed(257, 0, 0);
        }
    }

    @SubscribeEvent
    public static void onSoundPlay(SoundEvent.SoundSourceEvent event) {
        SongsManager.recordNoteFromSound(event.getSound(), event.getName());
    }

    @SubscribeEvent
    public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        Item i = stack.getItem();
        ResourceKey<String> pattern = DecoratedPotPatterns.getResourceKey(i);
        if (pattern != null && i != Items.BRICK) {
            event.getTooltipElements().add(Either.right(new SherdTooltip(pattern)));
        }
    }

    public static void onPackReload(TextureStitchEvent.Post event) {
        if (event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            AltimeterItemRenderer.onReload();
        }
    }

    @SubscribeEvent
    public static void onClickInput(InputEvent.InteractionKeyMappingTriggered event) {
        if (CannonController.isActive()) {
            event.setCanceled(true);
            event.setSwingHand(false);
            CannonController.onPlayerAttack(event.isAttack());
        }
    }

    @SubscribeEvent
    public static void renderHandEvent(RenderHandEvent event) {
        if (CannonController.isActive()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderGuiOverlayPre(RenderGuiOverlayEvent.Pre event) {
        if (CannonController.isActive()) {
            NamedGuiOverlay overlay = event.getOverlay();
            if (overlay == VanillaGuiOverlay.EXPERIENCE_BAR.type() || overlay == VanillaGuiOverlay.HOTBAR.type()) {
                event.setCanceled(true);
            }
        }
    }
}