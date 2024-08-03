package fuzs.puzzleslib.impl.client.event;

import com.google.common.base.Stopwatch;
import com.google.common.base.Suppliers;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.shaders.FogShape;
import fuzs.puzzleslib.api.client.event.v1.AddToastCallback;
import fuzs.puzzleslib.api.client.event.v1.ClientChunkEvents;
import fuzs.puzzleslib.api.client.event.v1.ClientEntityLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.ClientLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.ClientLevelTickEvents;
import fuzs.puzzleslib.api.client.event.v1.ClientPlayerEvents;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.ComputeCameraAnglesCallback;
import fuzs.puzzleslib.api.client.event.v1.ComputeFovModifierCallback;
import fuzs.puzzleslib.api.client.event.v1.ContainerScreenEvents;
import fuzs.puzzleslib.api.client.event.v1.CustomizeChatPanelCallback;
import fuzs.puzzleslib.api.client.event.v1.FogEvents;
import fuzs.puzzleslib.api.client.event.v1.GameRenderEvents;
import fuzs.puzzleslib.api.client.event.v1.GatherDebugTextEvents;
import fuzs.puzzleslib.api.client.event.v1.InputEvents;
import fuzs.puzzleslib.api.client.event.v1.InteractionInputEvents;
import fuzs.puzzleslib.api.client.event.v1.InventoryMobEffectsCallback;
import fuzs.puzzleslib.api.client.event.v1.ItemTooltipCallback;
import fuzs.puzzleslib.api.client.event.v1.ModelEvents;
import fuzs.puzzleslib.api.client.event.v1.MovementInputUpdateCallback;
import fuzs.puzzleslib.api.client.event.v1.RenderBlockOverlayCallback;
import fuzs.puzzleslib.api.client.event.v1.RenderGuiCallback;
import fuzs.puzzleslib.api.client.event.v1.RenderGuiElementEvents;
import fuzs.puzzleslib.api.client.event.v1.RenderHandCallback;
import fuzs.puzzleslib.api.client.event.v1.RenderHighlightCallback;
import fuzs.puzzleslib.api.client.event.v1.RenderLevelEvents;
import fuzs.puzzleslib.api.client.event.v1.RenderLivingEvents;
import fuzs.puzzleslib.api.client.event.v1.RenderNameTagCallback;
import fuzs.puzzleslib.api.client.event.v1.RenderPlayerEvents;
import fuzs.puzzleslib.api.client.event.v1.ScreenEvents;
import fuzs.puzzleslib.api.client.event.v1.ScreenKeyboardEvents;
import fuzs.puzzleslib.api.client.event.v1.ScreenMouseEvents;
import fuzs.puzzleslib.api.client.event.v1.ScreenOpeningCallback;
import fuzs.puzzleslib.api.client.event.v1.ScreenTooltipEvents;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import fuzs.puzzleslib.api.event.v1.core.ForgeEventInvokerRegistry;
import fuzs.puzzleslib.api.event.v1.data.DefaultedFloat;
import fuzs.puzzleslib.api.event.v1.data.DefaultedValue;
import fuzs.puzzleslib.api.event.v1.data.MutableBoolean;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import fuzs.puzzleslib.impl.PuzzlesLib;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ToastAddEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.Event.Result;

public final class ForgeClientEventInvokers {

    private static final Supplier<Set<ResourceLocation>> TOP_LEVEL_MODEL_LOCATIONS = Suppliers.memoize(ForgeClientEventInvokers::getTopLevelModelLocations);

    public static void registerLoadingHandlers() {
        ForgeEventInvokerRegistry.INSTANCE.register(ModelEvents.ModifyUnbakedModel.class, ModelEvent.ModifyBakingResult.class, (callback, evt) -> {
            Stopwatch stopwatch = Stopwatch.createStarted();
            Map<ResourceLocation, BakedModel> models = evt.getModels();
            Map<ForgeModelBakerImpl.BakedCacheKey, BakedModel> bakedCache = Maps.newHashMap();
            Multimap<ResourceLocation, Material> missingTextures = HashMultimap.create();
            BakedModel missingModel = (BakedModel) models.get(ModelBakery.MISSING_MODEL_LOCATION);
            Objects.requireNonNull(missingModel, "missing model is null");
            Map<ResourceLocation, UnbakedModel> additionalModels = Maps.newHashMap();
            Function<ResourceLocation, UnbakedModel> modelGetter = resourceLocation -> additionalModels.containsKey(resourceLocation) ? (UnbakedModel) additionalModels.get(resourceLocation) : evt.getModelBakery().getModel(resourceLocation);
            Map<UnbakedModel, BakedModel> unbakedCache = Maps.newIdentityHashMap();
            for (ResourceLocation modelLocation : (Set) TOP_LEVEL_MODEL_LOCATIONS.get()) {
                try {
                    EventResultHolder<UnbakedModel> result = callback.onModifyUnbakedModel(modelLocation, () -> (UnbakedModel) modelGetter.apply(modelLocation), modelGetter, (resourceLocation, unbakedModelx) -> {
                        if (resourceLocation instanceof ModelResourceLocation) {
                            throw new IllegalArgumentException("model resource location is not supported");
                        } else {
                            additionalModels.put(resourceLocation, unbakedModelx);
                        }
                    });
                    if (result.isInterrupt()) {
                        UnbakedModel unbakedModel = (UnbakedModel) result.getInterrupt().get();
                        additionalModels.put(modelLocation, unbakedModel);
                        BakedModel bakedModel = (BakedModel) unbakedCache.computeIfAbsent(unbakedModel, $ -> {
                            ForgeModelBakerImpl modelBaker = new ForgeModelBakerImpl(modelLocation, bakedCache, modelGetter, missingTextures::put, missingModel);
                            return modelBaker.bake(unbakedModel, modelLocation);
                        });
                        models.put(modelLocation, bakedModel);
                    }
                } catch (Exception var15) {
                    PuzzlesLib.LOGGER.error("Failed to modify unbaked model", var15);
                }
            }
            missingTextures.asMap().forEach((resourceLocation, materials) -> PuzzlesLib.LOGGER.warn("Missing textures in model {}:\n{}", resourceLocation, materials.stream().sorted(Material.COMPARATOR).map(material -> "    " + material.atlasLocation() + ":" + material.texture()).collect(Collectors.joining("\n"))));
            PuzzlesLib.LOGGER.info("Modifying unbaked models took {}ms", stopwatch.stop().elapsed().toMillis());
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ModelEvents.ModifyBakedModel.class, ModelEvent.ModifyBakingResult.class, (callback, evt) -> {
            Stopwatch stopwatch = Stopwatch.createStarted();
            Map<ResourceLocation, BakedModel> models = evt.getModels();
            Map<ForgeModelBakerImpl.BakedCacheKey, BakedModel> bakedCache = Maps.newHashMap();
            Multimap<ResourceLocation, Material> missingTextures = HashMultimap.create();
            BakedModel missingModel = (BakedModel) models.get(ModelBakery.MISSING_MODEL_LOCATION);
            Objects.requireNonNull(missingModel, "missing model is null");
            Function<ResourceLocation, ModelBaker> modelBaker = resourceLocation -> new ForgeModelBakerImpl(resourceLocation, bakedCache, evt.getModelBakery()::m_119341_, missingTextures::put, missingModel);
            Function<ResourceLocation, BakedModel> modelGetter = resourceLocation -> models.containsKey(resourceLocation) ? (BakedModel) models.get(resourceLocation) : ((ModelBaker) modelBaker.apply(resourceLocation)).bake(resourceLocation, BlockModelRotation.X0_Y0);
            for (ResourceLocation modelLocation : (Set) TOP_LEVEL_MODEL_LOCATIONS.get()) {
                try {
                    EventResultHolder<BakedModel> result = callback.onModifyBakedModel(modelLocation, () -> (BakedModel) modelGetter.apply(modelLocation), () -> (ModelBaker) modelBaker.apply(modelLocation), modelGetter, models::putIfAbsent);
                    result.getInterrupt().ifPresent(bakedModel -> models.put(modelLocation, bakedModel));
                } catch (Exception var12) {
                    PuzzlesLib.LOGGER.error("Failed to modify baked model", var12);
                }
            }
            missingTextures.asMap().forEach((resourceLocation, materials) -> PuzzlesLib.LOGGER.warn("Missing textures in model {}:\n{}", resourceLocation, materials.stream().sorted(Material.COMPARATOR).map(material -> "    " + material.atlasLocation() + ":" + material.texture()).collect(Collectors.joining("\n"))));
            PuzzlesLib.LOGGER.info("Modifying baked models took {}ms", stopwatch.stop().elapsed().toMillis());
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ModelEvents.AdditionalBakedModel.class, ModelEvent.ModifyBakingResult.class, (callback, evt) -> {
            Stopwatch stopwatch = Stopwatch.createStarted();
            Map<ResourceLocation, BakedModel> models = evt.getModels();
            Multimap<ResourceLocation, Material> missingTextures = HashMultimap.create();
            Map<ForgeModelBakerImpl.BakedCacheKey, BakedModel> bakedCache = Maps.newHashMap();
            BakedModel missingModel = (BakedModel) models.get(ModelBakery.MISSING_MODEL_LOCATION);
            Objects.requireNonNull(missingModel, "missing model is null");
            try {
                callback.onAdditionalBakedModel(models::putIfAbsent, resourceLocation -> (BakedModel) models.getOrDefault(resourceLocation, missingModel), () -> new ForgeModelBakerImpl(ModelBakery.MISSING_MODEL_LOCATION, bakedCache, evt.getModelBakery()::m_119341_, missingTextures::put, missingModel));
            } catch (Exception var8) {
                PuzzlesLib.LOGGER.error("Failed to add additional baked models", var8);
            }
            missingTextures.asMap().forEach((resourceLocation, materials) -> PuzzlesLib.LOGGER.warn("Missing textures:\n{}", materials.stream().sorted(Material.COMPARATOR).map(material -> "    " + material.atlasLocation() + ":" + material.texture()).collect(Collectors.joining("\n"))));
            PuzzlesLib.LOGGER.info("Adding additional baked models took {}ms", stopwatch.stop().elapsed().toMillis());
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ModelEvents.AfterModelLoading.class, ModelEvent.BakingCompleted.class, (callback, evt) -> callback.onAfterModelLoading(evt::getModelManager));
    }

    public static void register() {
        ForgeEventInvokerRegistry.INSTANCE.register(ClientTickEvents.Start.class, TickEvent.ClientTickEvent.class, (callback, evt) -> {
            if (evt.phase == TickEvent.Phase.START) {
                callback.onStartClientTick(Minecraft.getInstance());
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ClientTickEvents.End.class, TickEvent.ClientTickEvent.class, (callback, evt) -> {
            if (evt.phase == TickEvent.Phase.END) {
                callback.onEndClientTick(Minecraft.getInstance());
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(RenderGuiCallback.class, RenderGuiEvent.Post.class, (callback, evt) -> callback.onRenderGui(Minecraft.getInstance(), evt.getGuiGraphics(), evt.getPartialTick(), evt.getWindow().getGuiScaledWidth(), evt.getWindow().getGuiScaledHeight()));
        ForgeEventInvokerRegistry.INSTANCE.register(ItemTooltipCallback.class, ItemTooltipEvent.class, (callback, evt) -> callback.onItemTooltip(evt.getItemStack(), evt.getEntity(), evt.getToolTip(), evt.getFlags()));
        ForgeEventInvokerRegistry.INSTANCE.register(RenderNameTagCallback.class, RenderNameTagEvent.class, (callback, evt) -> {
            DefaultedValue<Component> content = DefaultedValue.fromEvent(evt::setContent, evt::getContent, evt::getOriginalContent);
            EventResult result = callback.onRenderNameTag(evt.getEntity(), content, evt.getEntityRenderer(), evt.getPoseStack(), evt.getMultiBufferSource(), evt.getPackedLight(), evt.getPartialTick());
            if (result.isInterrupt()) {
                evt.setResult(result.getAsBoolean() ? Result.ALLOW : Result.DENY);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ContainerScreenEvents.Background.class, ContainerScreenEvent.Render.Background.class, (callback, evt) -> callback.onDrawBackground(evt.getContainerScreen(), evt.getGuiGraphics(), evt.getMouseX(), evt.getMouseY()));
        ForgeEventInvokerRegistry.INSTANCE.register(ContainerScreenEvents.Foreground.class, ContainerScreenEvent.Render.Foreground.class, (callback, evt) -> callback.onDrawForeground(evt.getContainerScreen(), evt.getGuiGraphics(), evt.getMouseX(), evt.getMouseY()));
        ForgeEventInvokerRegistry.INSTANCE.register(InventoryMobEffectsCallback.class, ScreenEvent.RenderInventoryMobEffects.class, (callback, evt) -> {
            MutableBoolean fullSizeRendering = MutableBoolean.fromEvent(evt::setCompact, evt::isCompact);
            MutableInt horizontalOffset = MutableInt.fromEvent(evt::setHorizontalOffset, evt::getHorizontalOffset);
            EventResult result = callback.onInventoryMobEffects(evt.getScreen(), evt.getAvailableSpace(), fullSizeRendering, horizontalOffset);
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ScreenOpeningCallback.class, ScreenEvent.Opening.class, (callback, evt) -> {
            DefaultedValue<Screen> newScreen = DefaultedValue.fromEvent(evt::setNewScreen, evt::getNewScreen, evt::getScreen);
            EventResult result = callback.onScreenOpening(evt.getCurrentScreen(), newScreen);
            if (result.isInterrupt() || newScreen.getAsOptional().filter(screen -> screen == evt.getCurrentScreen()).isPresent()) {
                evt.setCanceled(true);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ComputeFovModifierCallback.class, ComputeFovModifierEvent.class, (callback, evt) -> {
            float fovEffectScale = Minecraft.getInstance().options.fovEffectScale().get().floatValue();
            if (fovEffectScale != 0.0F) {
                Consumer<Float> consumer = value -> evt.setNewFovModifier(Mth.lerp(fovEffectScale, 1.0F, value));
                Supplier<Float> supplier = () -> (evt.getNewFovModifier() - 1.0F) / fovEffectScale + 1.0F;
                callback.onComputeFovModifier(evt.getPlayer(), DefaultedFloat.fromEvent(consumer, supplier, evt::getFovModifier));
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ScreenEvents.BeforeInit.class, ScreenEvent.Init.Pre.class, (callback, evt) -> callback.onBeforeInit(Minecraft.getInstance(), evt.getScreen(), evt.getScreen().width, evt.getScreen().height, new ForgeButtonList(evt.getScreen().renderables)));
        ForgeEventInvokerRegistry.INSTANCE.register(ScreenEvents.AfterInit.class, ScreenEvent.Init.Post.class, (callback, evt) -> callback.onAfterInit(Minecraft.getInstance(), evt.getScreen(), evt.getScreen().width, evt.getScreen().height, new ForgeButtonList(evt.getScreen().renderables), evt::addListener, evt::removeListener));
        registerScreenEvent(ScreenEvents.BeforeInitV2.class, ScreenEvent.Init.Pre.class, (callback, evt) -> callback.onBeforeInit(Minecraft.getInstance(), evt.getScreen(), evt.getScreen().width, evt.getScreen().height, new ForgeButtonList(evt.getScreen().renderables)));
        registerScreenEvent(ScreenEvents.AfterInitV2.class, ScreenEvent.Init.Post.class, (callback, evt) -> {
            ScreenEvents.ConsumingOperator<GuiEventListener> addWidget = new ScreenEvents.ConsumingOperator<>(evt::addListener);
            ScreenEvents.ConsumingOperator<GuiEventListener> removeWidget = new ScreenEvents.ConsumingOperator<>(evt::removeListener);
            callback.onAfterInit(Minecraft.getInstance(), evt.getScreen(), evt.getScreen().width, evt.getScreen().height, new ForgeButtonList(evt.getScreen().renderables), addWidget, removeWidget);
        });
        registerScreenEvent(ScreenEvents.Remove.class, ScreenEvent.Closing.class, (callback, evt) -> callback.onRemove(evt.getScreen()));
        registerScreenEvent(ScreenEvents.BeforeRender.class, ScreenEvent.Render.Pre.class, (callback, evt) -> callback.onBeforeRender(evt.getScreen(), evt.getGuiGraphics(), evt.getMouseX(), evt.getMouseY(), evt.getPartialTick()));
        registerScreenEvent(ScreenEvents.AfterRender.class, ScreenEvent.Render.Post.class, (callback, evt) -> callback.onAfterRender(evt.getScreen(), evt.getGuiGraphics(), evt.getMouseX(), evt.getMouseY(), evt.getPartialTick()));
        registerScreenEvent(ScreenMouseEvents.BeforeMouseClick.class, ScreenEvent.MouseButtonPressed.Pre.class, (callback, evt) -> {
            EventResult result = callback.onBeforeMouseClick(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getButton());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        registerScreenEvent(ScreenMouseEvents.AfterMouseClick.class, ScreenEvent.MouseButtonPressed.Post.class, (callback, evt) -> callback.onAfterMouseClick(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getButton()));
        registerScreenEvent(ScreenMouseEvents.BeforeMouseRelease.class, ScreenEvent.MouseButtonReleased.Pre.class, (callback, evt) -> {
            EventResult result = callback.onBeforeMouseRelease(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getButton());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        registerScreenEvent(ScreenMouseEvents.AfterMouseRelease.class, ScreenEvent.MouseButtonReleased.Post.class, (callback, evt) -> callback.onAfterMouseRelease(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getButton()));
        registerScreenEvent(ScreenMouseEvents.BeforeMouseScroll.class, ScreenEvent.MouseScrolled.Pre.class, (callback, evt) -> {
            EventResult result = callback.onBeforeMouseScroll(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getScrollDelta(), evt.getScrollDelta());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        registerScreenEvent(ScreenMouseEvents.AfterMouseScroll.class, ScreenEvent.MouseScrolled.Post.class, (callback, evt) -> callback.onAfterMouseScroll(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getScrollDelta(), evt.getScrollDelta()));
        registerScreenEvent(ScreenMouseEvents.BeforeMouseDrag.class, ScreenEvent.MouseDragged.Pre.class, (callback, evt) -> {
            EventResult result = callback.onBeforeMouseDrag(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getMouseButton(), evt.getDragX(), evt.getDragY());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        registerScreenEvent(ScreenMouseEvents.AfterMouseDrag.class, ScreenEvent.MouseDragged.Post.class, (callback, evt) -> callback.onAfterMouseDrag(evt.getScreen(), evt.getMouseX(), evt.getMouseY(), evt.getMouseButton(), evt.getDragX(), evt.getDragY()));
        registerScreenEvent(ScreenKeyboardEvents.BeforeKeyPress.class, ScreenEvent.KeyPressed.Pre.class, (callback, evt) -> {
            EventResult result = callback.onBeforeKeyPress(evt.getScreen(), evt.getKeyCode(), evt.getScanCode(), evt.getModifiers());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        registerScreenEvent(ScreenKeyboardEvents.AfterKeyPress.class, ScreenEvent.KeyPressed.Post.class, (callback, evt) -> callback.onAfterKeyPress(evt.getScreen(), evt.getKeyCode(), evt.getScanCode(), evt.getModifiers()));
        registerScreenEvent(ScreenKeyboardEvents.BeforeKeyRelease.class, ScreenEvent.KeyReleased.Pre.class, (callback, evt) -> {
            EventResult result = callback.onBeforeKeyRelease(evt.getScreen(), evt.getKeyCode(), evt.getScanCode(), evt.getModifiers());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        registerScreenEvent(ScreenKeyboardEvents.AfterKeyRelease.class, ScreenEvent.KeyReleased.Post.class, (callback, evt) -> callback.onAfterKeyRelease(evt.getScreen(), evt.getKeyCode(), evt.getScanCode(), evt.getModifiers()));
        ForgeEventInvokerRegistry.INSTANCE.register(RenderGuiElementEvents.Before.class, RenderGuiOverlayEvent.Pre.class, (ForgeEventInvokerRegistry.ForgeEventContextConsumer<RenderGuiElementEvents.Before, RenderGuiOverlayEvent.Pre>) ((callback, evt, context) -> {
            Objects.requireNonNull(context, "context is null");
            RenderGuiElementEvents.GuiOverlay overlay = (RenderGuiElementEvents.GuiOverlay) context;
            Minecraft minecraft = Minecraft.getInstance();
            if (evt.getOverlay().id().equals(overlay.id()) && overlay.filter().test(minecraft)) {
                EventResult result = callback.onBeforeRenderGuiElement(minecraft, evt.getGuiGraphics(), evt.getPartialTick(), evt.getWindow().getGuiScaledWidth(), evt.getWindow().getGuiScaledHeight());
                if (result.isInterrupt()) {
                    evt.setCanceled(true);
                }
            }
        }));
        ForgeEventInvokerRegistry.INSTANCE.register(RenderGuiElementEvents.After.class, RenderGuiOverlayEvent.Post.class, (ForgeEventInvokerRegistry.ForgeEventContextConsumer<RenderGuiElementEvents.After, RenderGuiOverlayEvent.Post>) ((callback, evt, context) -> {
            Objects.requireNonNull(context, "context is null");
            RenderGuiElementEvents.GuiOverlay overlay = (RenderGuiElementEvents.GuiOverlay) context;
            Minecraft minecraft = Minecraft.getInstance();
            if (evt.getOverlay().id().equals(overlay.id()) && overlay.filter().test(minecraft)) {
                callback.onAfterRenderGuiElement(minecraft, evt.getGuiGraphics(), evt.getPartialTick(), evt.getWindow().getGuiScaledWidth(), evt.getWindow().getGuiScaledHeight());
            }
        }));
        ForgeEventInvokerRegistry.INSTANCE.register(CustomizeChatPanelCallback.class, CustomizeGuiOverlayEvent.Chat.class, (callback, evt) -> {
            MutableInt posX = MutableInt.fromEvent(evt::setPosX, evt::getPosX);
            MutableInt posY = MutableInt.fromEvent(evt::setPosY, evt::getPosY);
            callback.onRenderChatPanel(evt.getWindow(), evt.getGuiGraphics(), evt.getPartialTick(), posX, posY);
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ClientEntityLevelEvents.Load.class, EntityJoinLevelEvent.class, (callback, evt) -> {
            if (evt.getLevel().isClientSide) {
                EventResult result = callback.onEntityLoad(evt.getEntity(), (ClientLevel) evt.getLevel());
                if (result.isInterrupt()) {
                    if (evt.getEntity() instanceof Player) {
                        throw new UnsupportedOperationException("Cannot prevent player from spawning in!");
                    }
                    evt.setCanceled(true);
                }
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ClientEntityLevelEvents.Unload.class, EntityLeaveLevelEvent.class, (callback, evt) -> {
            if (evt.getLevel().isClientSide) {
                callback.onEntityUnload(evt.getEntity(), (ClientLevel) evt.getLevel());
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(InputEvents.BeforeMouseAction.class, InputEvent.MouseButton.Pre.class, (callback, evt) -> {
            EventResult result = callback.onBeforeMouseAction(evt.getButton(), evt.getAction(), evt.getModifiers());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(InputEvents.AfterMouseAction.class, InputEvent.MouseButton.Post.class, (callback, evt) -> callback.onAfterMouseAction(evt.getButton(), evt.getAction(), evt.getModifiers()));
        ForgeEventInvokerRegistry.INSTANCE.register(InputEvents.BeforeMouseScroll.class, InputEvent.MouseScrollingEvent.class, (callback, evt) -> {
            EventResult result = callback.onBeforeMouseScroll(evt.isLeftDown(), evt.isMiddleDown(), evt.isRightDown(), evt.getScrollDelta(), evt.getScrollDelta());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(InputEvents.AfterMouseScroll.class, InputEvent.MouseScrollingEvent.class, (callback, evt) -> callback.onAfterMouseScroll(evt.isLeftDown(), evt.isMiddleDown(), evt.isRightDown(), evt.getScrollDelta(), evt.getScrollDelta()));
        ForgeEventInvokerRegistry.INSTANCE.register(InputEvents.BeforeKeyAction.class, InputEvent.Key.class, (callback, evt) -> callback.onBeforeKeyAction(evt.getKey(), evt.getScanCode(), evt.getAction(), evt.getModifiers()));
        ForgeEventInvokerRegistry.INSTANCE.register(InputEvents.AfterKeyAction.class, InputEvent.Key.class, (callback, evt) -> callback.onAfterKeyAction(evt.getKey(), evt.getScanCode(), evt.getAction(), evt.getModifiers()));
        ForgeEventInvokerRegistry.INSTANCE.register(ComputeCameraAnglesCallback.class, ViewportEvent.ComputeCameraAngles.class, (callback, evt) -> {
            MutableFloat pitch = MutableFloat.fromEvent(evt::setPitch, evt::getPitch);
            MutableFloat yaw = MutableFloat.fromEvent(evt::setYaw, evt::getYaw);
            MutableFloat roll = MutableFloat.fromEvent(evt::setRoll, evt::getRoll);
            callback.onComputeCameraAngles(evt.getRenderer(), evt.getCamera(), (float) evt.getPartialTick(), pitch, yaw, roll);
        });
        ForgeEventInvokerRegistry.INSTANCE.register(RenderLivingEvents.Before.class, RenderLivingEvent.Pre.class, (callback, evt) -> {
            EventResult result = callback.onBeforeRenderEntity(evt.getEntity(), evt.getRenderer(), evt.getPartialTick(), evt.getPoseStack(), evt.getMultiBufferSource(), evt.getPackedLight());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(RenderLivingEvents.After.class, RenderLivingEvent.Post.class, (callback, evt) -> callback.onAfterRenderEntity(evt.getEntity(), evt.getRenderer(), evt.getPartialTick(), evt.getPoseStack(), evt.getMultiBufferSource(), evt.getPackedLight()));
        ForgeEventInvokerRegistry.INSTANCE.register(RenderPlayerEvents.Before.class, RenderPlayerEvent.Pre.class, (callback, evt) -> {
            EventResult result = callback.onBeforeRenderPlayer(evt.getEntity(), evt.getRenderer(), evt.getPartialTick(), evt.getPoseStack(), evt.getMultiBufferSource(), evt.getPackedLight());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(RenderPlayerEvents.After.class, RenderPlayerEvent.Post.class, (callback, evt) -> callback.onAfterRenderPlayer(evt.getEntity(), evt.getRenderer(), evt.getPartialTick(), evt.getPoseStack(), evt.getMultiBufferSource(), evt.getPackedLight()));
        ForgeEventInvokerRegistry.INSTANCE.register(RenderHandCallback.class, RenderHandEvent.class, (callback, evt) -> {
            Minecraft minecraft = Minecraft.getInstance();
            EventResult result = callback.onRenderHand(minecraft.player, evt.getHand(), evt.getItemStack(), evt.getPoseStack(), evt.getMultiBufferSource(), evt.getPackedLight(), evt.getPartialTick(), evt.getInterpolatedPitch(), evt.getSwingProgress(), evt.getEquipProgress());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ClientLevelTickEvents.Start.class, TickEvent.LevelTickEvent.class, (callback, evt) -> {
            if (evt.phase == TickEvent.Phase.START && evt.level instanceof ClientLevel level) {
                callback.onStartLevelTick(Minecraft.getInstance(), level);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ClientLevelTickEvents.End.class, TickEvent.LevelTickEvent.class, (callback, evt) -> {
            if (evt.phase == TickEvent.Phase.END && evt.level instanceof ClientLevel level) {
                callback.onEndLevelTick(Minecraft.getInstance(), level);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ClientChunkEvents.Load.class, ChunkEvent.Load.class, (callback, evt) -> {
            if (evt.getLevel() instanceof ClientLevel level) {
                callback.onChunkLoad(level, (LevelChunk) evt.getChunk());
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ClientChunkEvents.Unload.class, ChunkEvent.Unload.class, (callback, evt) -> {
            if (evt.getLevel() instanceof ClientLevel level) {
                callback.onChunkUnload(level, (LevelChunk) evt.getChunk());
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ClientPlayerEvents.LoggedIn.class, ClientPlayerNetworkEvent.LoggingIn.class, (callback, evt) -> callback.onLoggedIn(evt.getPlayer(), evt.getMultiPlayerGameMode(), evt.getConnection()));
        ForgeEventInvokerRegistry.INSTANCE.register(ClientPlayerEvents.LoggedOut.class, ClientPlayerNetworkEvent.LoggingOut.class, (callback, evt) -> {
            if (evt.getPlayer() != null && evt.getMultiPlayerGameMode() != null) {
                Objects.requireNonNull(evt.getConnection(), "connection is null");
                callback.onLoggedOut(evt.getPlayer(), evt.getMultiPlayerGameMode(), evt.getConnection());
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ClientPlayerEvents.Copy.class, ClientPlayerNetworkEvent.Clone.class, (callback, evt) -> callback.onCopy(evt.getOldPlayer(), evt.getNewPlayer(), evt.getMultiPlayerGameMode(), evt.getConnection()));
        ForgeEventInvokerRegistry.INSTANCE.register(InteractionInputEvents.Attack.class, InputEvent.InteractionKeyMappingTriggered.class, (callback, evt) -> {
            if (evt.isAttack()) {
                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft.hitResult != null) {
                    EventResult result = callback.onAttackInteraction(minecraft, minecraft.player);
                    if (result.isInterrupt()) {
                        evt.setSwingHand(false);
                        evt.setCanceled(true);
                    }
                }
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(InteractionInputEvents.AttackV2.class, InputEvent.InteractionKeyMappingTriggered.class, (callback, evt) -> {
            if (evt.isAttack()) {
                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft.hitResult != null) {
                    EventResult result = callback.onAttackInteraction(minecraft, minecraft.player, minecraft.hitResult);
                    if (result.isInterrupt()) {
                        evt.setSwingHand(false);
                        evt.setCanceled(true);
                    }
                }
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(InteractionInputEvents.Use.class, InputEvent.InteractionKeyMappingTriggered.class, (callback, evt) -> {
            if (evt.isUseItem()) {
                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft.hitResult != null && minecraft.player.m_21120_(evt.getHand()).isItemEnabled(minecraft.level.enabledFeatures()) && (minecraft.hitResult.getType() != HitResult.Type.ENTITY || minecraft.level.m_6857_().isWithinBounds(((EntityHitResult) minecraft.hitResult).getEntity().blockPosition()))) {
                    EventResult result = callback.onUseInteraction(minecraft, minecraft.player, evt.getHand(), minecraft.hitResult);
                    if (result.isInterrupt()) {
                        evt.setSwingHand(false);
                        evt.setCanceled(true);
                    }
                }
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(InteractionInputEvents.Pick.class, InputEvent.InteractionKeyMappingTriggered.class, (callback, evt) -> {
            if (evt.isPickBlock()) {
                Minecraft minecraft = Minecraft.getInstance();
                EventResult result = callback.onPickInteraction(minecraft, minecraft.player, minecraft.hitResult);
                if (result.isInterrupt()) {
                    evt.setCanceled(true);
                }
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ClientLevelEvents.Load.class, LevelEvent.Load.class, (callback, evt) -> {
            if (evt.getLevel() instanceof ClientLevel level) {
                callback.onLevelLoad(Minecraft.getInstance(), level);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ClientLevelEvents.Unload.class, LevelEvent.Unload.class, (callback, evt) -> {
            if (evt.getLevel() instanceof ClientLevel level) {
                callback.onLevelUnload(Minecraft.getInstance(), level);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(MovementInputUpdateCallback.class, MovementInputUpdateEvent.class, (callback, evt) -> callback.onMovementInputUpdate((LocalPlayer) evt.getEntity(), evt.getInput()));
        ForgeEventInvokerRegistry.INSTANCE.register(ModelEvents.ModifyBakingResult.class, ModelEvent.ModifyBakingResult.class, (callback, evt) -> callback.onModifyBakingResult(evt.getModels(), evt::getModelBakery));
        ForgeEventInvokerRegistry.INSTANCE.register(ModelEvents.BakingCompleted.class, ModelEvent.BakingCompleted.class, (callback, evt) -> callback.onBakingCompleted(evt::getModelManager, evt.getModels(), evt::getModelBakery));
        ForgeEventInvokerRegistry.INSTANCE.register(RenderBlockOverlayCallback.class, RenderBlockScreenEffectEvent.class, (callback, evt) -> {
            EventResult result = callback.onRenderBlockOverlay((LocalPlayer) evt.getPlayer(), evt.getPoseStack(), evt.getBlockState());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(FogEvents.Render.class, ViewportEvent.RenderFog.class, (callback, evt) -> {
            MutableFloat nearPlaneDistance = MutableFloat.fromEvent(t -> {
                evt.setNearPlaneDistance(t);
                evt.setCanceled(true);
            }, evt::getNearPlaneDistance);
            MutableFloat farPlaneDistance = MutableFloat.fromEvent(t -> {
                evt.setFarPlaneDistance(t);
                evt.setCanceled(true);
            }, evt::getFarPlaneDistance);
            MutableValue<FogShape> fogShape = MutableValue.fromEvent(t -> {
                evt.setFogShape(t);
                evt.setCanceled(true);
            }, evt::getFogShape);
            callback.onRenderFog(evt.getRenderer(), evt.getCamera(), (float) evt.getPartialTick(), evt.getMode(), evt.getType(), nearPlaneDistance, farPlaneDistance, fogShape);
        });
        ForgeEventInvokerRegistry.INSTANCE.register(FogEvents.ComputeColor.class, ViewportEvent.ComputeFogColor.class, (callback, evt) -> {
            MutableFloat red = MutableFloat.fromEvent(evt::setRed, evt::getRed);
            MutableFloat green = MutableFloat.fromEvent(evt::setGreen, evt::getGreen);
            MutableFloat blue = MutableFloat.fromEvent(evt::setBlue, evt::getBlue);
            callback.onComputeFogColor(evt.getRenderer(), evt.getCamera(), (float) evt.getPartialTick(), red, green, blue);
        });
        ForgeEventInvokerRegistry.INSTANCE.register(ScreenTooltipEvents.Render.class, RenderTooltipEvent.Pre.class, (callback, evt) -> {
            EventResult result = callback.onRenderTooltip(evt.getGraphics(), evt.getX(), evt.getY(), evt.getScreenWidth(), evt.getScreenHeight(), evt.getFont(), evt.getComponents(), evt.getTooltipPositioner());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(RenderHighlightCallback.class, RenderHighlightEvent.class, (callback, evt) -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.getCameraEntity() instanceof Player && !minecraft.options.hideGui) {
                EventResult result = callback.onRenderHighlight(evt.getLevelRenderer(), evt.getCamera(), minecraft.gameRenderer, evt.getTarget(), evt.getPartialTick(), evt.getPoseStack(), evt.getMultiBufferSource(), minecraft.level);
                if (result.isInterrupt()) {
                    evt.setCanceled(true);
                }
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(RenderLevelEvents.AfterTerrain.class, RenderLevelStageEvent.class, (callback, evt) -> {
            if (evt.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS) {
                Minecraft minecraft = Minecraft.getInstance();
                callback.onRenderLevelAfterTerrain(evt.getLevelRenderer(), evt.getCamera(), minecraft.gameRenderer, evt.getPartialTick(), evt.getPoseStack(), evt.getProjectionMatrix(), evt.getFrustum(), minecraft.level);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(RenderLevelEvents.AfterEntities.class, RenderLevelStageEvent.class, (callback, evt) -> {
            if (evt.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
                Minecraft minecraft = Minecraft.getInstance();
                callback.onRenderLevelAfterEntities(evt.getLevelRenderer(), evt.getCamera(), minecraft.gameRenderer, evt.getPartialTick(), evt.getPoseStack(), evt.getProjectionMatrix(), evt.getFrustum(), minecraft.level);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(RenderLevelEvents.AfterTranslucent.class, RenderLevelStageEvent.class, (callback, evt) -> {
            if (evt.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                Minecraft minecraft = Minecraft.getInstance();
                callback.onRenderLevelAfterTranslucent(evt.getLevelRenderer(), evt.getCamera(), minecraft.gameRenderer, evt.getPartialTick(), evt.getPoseStack(), evt.getProjectionMatrix(), evt.getFrustum(), minecraft.level);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(RenderLevelEvents.AfterLevel.class, RenderLevelStageEvent.class, (callback, evt) -> {
            if (evt.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
                Minecraft minecraft = Minecraft.getInstance();
                callback.onRenderLevelAfterLevel(evt.getLevelRenderer(), evt.getCamera(), minecraft.gameRenderer, evt.getPartialTick(), evt.getPoseStack(), evt.getProjectionMatrix(), evt.getFrustum(), minecraft.level);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(GameRenderEvents.Before.class, TickEvent.RenderTickEvent.class, (callback, evt) -> {
            if (evt.phase == TickEvent.Phase.START) {
                Minecraft minecraft = Minecraft.getInstance();
                callback.onBeforeGameRender(minecraft, minecraft.gameRenderer, evt.renderTickTime);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(GameRenderEvents.After.class, TickEvent.RenderTickEvent.class, (callback, evt) -> {
            if (evt.phase == TickEvent.Phase.END) {
                Minecraft minecraft = Minecraft.getInstance();
                callback.onAfterGameRender(minecraft, minecraft.gameRenderer, evt.renderTickTime);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(AddToastCallback.class, ToastAddEvent.class, (callback, evt) -> {
            Minecraft minecraft = Minecraft.getInstance();
            EventResult result = callback.onAddToast(minecraft.getToasts(), evt.getToast());
            if (result.isInterrupt()) {
                evt.setCanceled(true);
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(GatherDebugTextEvents.Left.class, CustomizeGuiOverlayEvent.DebugText.class, (callback, evt) -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.options.renderDebug) {
                callback.onGatherLeftDebugText(evt.getLeft());
            }
        });
        ForgeEventInvokerRegistry.INSTANCE.register(GatherDebugTextEvents.Right.class, CustomizeGuiOverlayEvent.DebugText.class, (callback, evt) -> {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.options.renderDebug) {
                callback.onGatherRightDebugText(evt.getRight());
            }
        });
    }

    private static <T, E extends ScreenEvent> void registerScreenEvent(Class<T> clazz, Class<E> event, BiConsumer<T, E> converter) {
        ForgeEventInvokerRegistry.INSTANCE.register(clazz, event, (ForgeEventInvokerRegistry.ForgeEventContextConsumer<Object, ScreenEvent>) ((callback, evt, context) -> {
            Objects.requireNonNull(context, "context is null");
            if (((Class) context).isInstance(evt.getScreen())) {
                converter.accept(callback, evt);
            }
        }));
    }

    private static Set<ResourceLocation> getTopLevelModelLocations() {
        Set<ResourceLocation> modelLocations = Sets.newHashSet(new ResourceLocation[] { ModelBakery.MISSING_MODEL_LOCATION });
        for (Block block : BuiltInRegistries.BLOCK) {
            block.getStateDefinition().getPossibleStates().forEach(blockState -> modelLocations.add(BlockModelShaper.stateToModelLocation(blockState)));
        }
        for (ResourceLocation resourcelocation : BuiltInRegistries.ITEM.m_6566_()) {
            modelLocations.add(new ModelResourceLocation(resourcelocation, "inventory"));
        }
        modelLocations.add(ItemRenderer.TRIDENT_IN_HAND_MODEL);
        modelLocations.add(ItemRenderer.SPYGLASS_IN_HAND_MODEL);
        return Collections.unmodifiableSet(modelLocations);
    }
}