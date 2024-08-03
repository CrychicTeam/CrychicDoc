package net.mehvahdjukaar.supplementaries.reg;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.mehvahdjukaar.moonlight.api.client.model.NestedModelLoader;
import net.mehvahdjukaar.moonlight.api.client.renderer.FallingBlockRendererGeneric;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.client.BlackboardManager;
import net.mehvahdjukaar.supplementaries.client.GlobeManager;
import net.mehvahdjukaar.supplementaries.client.block_models.BlackboardBakedModel;
import net.mehvahdjukaar.supplementaries.client.block_models.BookPileModel;
import net.mehvahdjukaar.supplementaries.client.block_models.BuntingsBakedModel;
import net.mehvahdjukaar.supplementaries.client.block_models.FaucetModelLoader;
import net.mehvahdjukaar.supplementaries.client.block_models.FlowerBoxBakedModel;
import net.mehvahdjukaar.supplementaries.client.block_models.FrameBlockBakedModel;
import net.mehvahdjukaar.supplementaries.client.block_models.GobletModelLoader;
import net.mehvahdjukaar.supplementaries.client.block_models.JarModelLoader;
import net.mehvahdjukaar.supplementaries.client.block_models.RopeKnotBlockBakedModel;
import net.mehvahdjukaar.supplementaries.client.block_models.SignPostBlockBakedModel;
import net.mehvahdjukaar.supplementaries.client.particles.BombExplosionEmitterParticle;
import net.mehvahdjukaar.supplementaries.client.particles.BombExplosionParticle;
import net.mehvahdjukaar.supplementaries.client.particles.BombSmokeParticle;
import net.mehvahdjukaar.supplementaries.client.particles.BottlingXpParticle;
import net.mehvahdjukaar.supplementaries.client.particles.BubbleBlockParticle;
import net.mehvahdjukaar.supplementaries.client.particles.CannonFireParticle;
import net.mehvahdjukaar.supplementaries.client.particles.ConfettiParticle;
import net.mehvahdjukaar.supplementaries.client.particles.DrippingLiquidParticle;
import net.mehvahdjukaar.supplementaries.client.particles.FallingLiquidParticle;
import net.mehvahdjukaar.supplementaries.client.particles.FeatherParticle;
import net.mehvahdjukaar.supplementaries.client.particles.RotationTrailEmitter;
import net.mehvahdjukaar.supplementaries.client.particles.RotationTrailParticle;
import net.mehvahdjukaar.supplementaries.client.particles.SlingshotParticle;
import net.mehvahdjukaar.supplementaries.client.particles.SpeakerSoundParticle;
import net.mehvahdjukaar.supplementaries.client.particles.SplashingLiquidParticle;
import net.mehvahdjukaar.supplementaries.client.particles.StasisParticle;
import net.mehvahdjukaar.supplementaries.client.particles.SudsParticle;
import net.mehvahdjukaar.supplementaries.client.particles.SugarParticle;
import net.mehvahdjukaar.supplementaries.client.renderers.color.CogBlockColor;
import net.mehvahdjukaar.supplementaries.client.renderers.color.DefaultWaterColor;
import net.mehvahdjukaar.supplementaries.client.renderers.color.FlowerBoxColor;
import net.mehvahdjukaar.supplementaries.client.renderers.color.FluidColor;
import net.mehvahdjukaar.supplementaries.client.renderers.color.GunpowderBlockColor;
import net.mehvahdjukaar.supplementaries.client.renderers.color.MimicBlockColor;
import net.mehvahdjukaar.supplementaries.client.renderers.color.TippedSpikesColor;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.HatStandRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.RedMerchantRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.RopeArrowRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.SlingshotProjectileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.funny.JarredHeadLayer;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.funny.JarredModel;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.funny.PickleModel;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.models.EndermanSkullModel;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.models.HatStandModel;
import net.mehvahdjukaar.supplementaries.client.renderers.items.QuiverItemOverlayRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.items.SlingshotItemOverlayRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.BellowsBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.BlackboardBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.BookPileBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.BubbleBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.BuntingBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.CageBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.CannonBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.ClockBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.DoormatBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.EndermanSkullBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.FlagBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.GlobeBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.HourGlassBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.ItemShelfBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.JarBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.JarBoatTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.NoticeBoardBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.PedestalBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.SignPostBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.SpringLauncherArmBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.StatueBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.tiles.WindVaneBlockTileRenderer;
import net.mehvahdjukaar.supplementaries.client.screens.CannonScreen;
import net.mehvahdjukaar.supplementaries.client.screens.NoticeBoardScreen;
import net.mehvahdjukaar.supplementaries.client.screens.PresentScreen;
import net.mehvahdjukaar.supplementaries.client.screens.PulleyScreen;
import net.mehvahdjukaar.supplementaries.client.screens.RedMerchantScreen;
import net.mehvahdjukaar.supplementaries.client.screens.SackScreen;
import net.mehvahdjukaar.supplementaries.client.screens.TrappedPresentScreen;
import net.mehvahdjukaar.supplementaries.client.tooltip.BannerPatternTooltipComponent;
import net.mehvahdjukaar.supplementaries.client.tooltip.BlackboardTooltipComponent;
import net.mehvahdjukaar.supplementaries.client.tooltip.PaintingTooltipComponent;
import net.mehvahdjukaar.supplementaries.client.tooltip.QuiverTooltipComponent;
import net.mehvahdjukaar.supplementaries.client.tooltip.SherdTooltipComponent;
import net.mehvahdjukaar.supplementaries.common.block.placeable_book.PlaceableBookManager;
import net.mehvahdjukaar.supplementaries.common.block.tiles.TrappedPresentBlockTile;
import net.mehvahdjukaar.supplementaries.common.items.AntiqueInkItem;
import net.mehvahdjukaar.supplementaries.common.items.SlingshotItem;
import net.mehvahdjukaar.supplementaries.common.items.tooltip_components.BannerPatternTooltip;
import net.mehvahdjukaar.supplementaries.common.items.tooltip_components.PaintingTooltip;
import net.mehvahdjukaar.supplementaries.common.items.tooltip_components.QuiverTooltip;
import net.mehvahdjukaar.supplementaries.common.items.tooltip_components.SherdTooltip;
import net.mehvahdjukaar.supplementaries.common.misc.map_markers.client.ModMapMarkersClient;
import net.mehvahdjukaar.supplementaries.common.utils.FlowerPotHandler;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.integration.CompatHandlerClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SnowflakeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.jetbrains.annotations.Nullable;

public class ClientRegistry {

    public static final ResourceLocation RAGE_SHADER = Supplementaries.res("shaders/post/rage.json");

    public static final String BARBARIC_RAGE_SHADER = Supplementaries.res("shaders/post/barbaric_rage.json").toString();

    public static final ResourceLocation FLARE_SHADER = Supplementaries.res("shaders/post/flare.json");

    public static final ResourceLocation BLACK_AND_WHITE_SHADER = Supplementaries.res("shaders/post/black_and_white.json");

    public static final ResourceLocation VANILLA_DESATURATE = new ResourceLocation("shaders/post/desaturate.json");

    public static final ModelLayerLocation BELLOWS_MODEL = loc("bellows");

    public static final ModelLayerLocation CLOCK_HANDS_MODEL = loc("clock_hands");

    public static final ModelLayerLocation GLOBE_BASE_MODEL = loc("globe");

    public static final ModelLayerLocation GLOBE_SPECIAL_MODEL = loc("globe_special");

    public static final ModelLayerLocation RED_MERCHANT_MODEL = loc("red_merchant");

    public static final ModelLayerLocation HAT_STAND_MODEL = loc("hat_stand");

    public static final ModelLayerLocation HAT_STAND_MODEL_ARMOR = loc("hat_stand_armor");

    public static final ModelLayerLocation JARVIS_MODEL = loc("jarvis");

    public static final ModelLayerLocation JAR_MODEL = loc("jar");

    public static final ModelLayerLocation PICKLE_MODEL = loc("pickle");

    public static final ModelLayerLocation ENDERMAN_HEAD_MODEL = loc("enderman_head");

    public static final ModelLayerLocation CANNON_MODEL = loc("cannon");

    public static final ModelLayerLocation WIND_VANE_MODEL = loc("wind_vane");

    public static final ModelLayerLocation BUNTING_MODEL = loc("bunting");

    public static final ResourceLocation FLUTE_3D_MODEL = Supplementaries.res("item/flute_in_hand");

    public static final ResourceLocation FLUTE_2D_MODEL = Supplementaries.res("item/flute_gui");

    public static final ResourceLocation QUIVER_3D_MODEL = Supplementaries.res("item/quiver_in_hand_dyed");

    public static final ResourceLocation QUIVER_2D_MODEL = Supplementaries.res("item/quiver_gui_dyed");

    public static final ResourceLocation ALTIMETER_TEMPLATE = Supplementaries.res("item/altimeter_template");

    public static final ResourceLocation ALTIMETER_OVERLAY = Supplementaries.res("item/altimeter_overlay");

    public static final ResourceLocation BOAT_MODEL = Supplementaries.res("block/jar_boat_ship");

    public static final ResourceLocation BLACKBOARD_FRAME = Supplementaries.res("block/blackboard_frame");

    public static final Supplier<Map<WoodType, ResourceLocation>> SIGN_POST_MODELS = Suppliers.memoize(() -> (Map) WoodTypeRegistry.getTypes().stream().collect(Collectors.toMap(Function.identity(), w -> Supplementaries.res("block/sign_posts/" + w.getVariantId("sign_post")))));

    public static final KeyMapping QUIVER_KEYBIND = new KeyMapping("supplementaries.keybind.quiver", InputConstants.Type.KEYSYM, InputConstants.getKey("key.keyboard.v").getValue(), "supplementaries.gui.controls");

    private static ModelLayerLocation loc(String name) {
        return new ModelLayerLocation(Supplementaries.res(name), name);
    }

    public static void init() {
        CompatHandlerClient.init();
        ClientHelper.addClientSetup(ClientRegistry::setup);
        ClientHelper.addEntityRenderersRegistration(ClientRegistry::registerEntityRenderers);
        ClientHelper.addBlockEntityRenderersRegistration(ClientRegistry::registerBlockEntityRenderers);
        ClientHelper.addBlockColorsRegistration(ClientRegistry::registerBlockColors);
        ClientHelper.addItemColorsRegistration(ClientRegistry::registerItemColors);
        ClientHelper.addParticleRegistration(ClientRegistry::registerParticles);
        ClientHelper.addModelLayerRegistration(ClientRegistry::registerModelLayers);
        ClientHelper.addSpecialModelRegistration(ClientRegistry::registerSpecialModels);
        ClientHelper.addTooltipComponentRegistration(ClientRegistry::registerTooltipComponent);
        ClientHelper.addModelLoaderRegistration(ClientRegistry::registerModelLoaders);
        ClientHelper.addItemDecoratorsRegistration(ClientRegistry::registerItemDecorators);
        ClientHelper.addKeyBindRegistration(ClientRegistry::registerKeyBinds);
    }

    public static void setup() {
        CompatHandlerClient.setup();
        ModMapMarkersClient.init();
        MenuScreens.register((MenuType) ModMenuTypes.PULLEY_BLOCK.get(), PulleyScreen::new);
        MenuScreens.register((MenuType) ModMenuTypes.VARIABLE_SIZE.get(), SackScreen::new);
        MenuScreens.register((MenuType) ModMenuTypes.SAFE.get(), ShulkerBoxScreen::new);
        MenuScreens.register((MenuType) ModMenuTypes.PRESENT_BLOCK.get(), PresentScreen::new);
        MenuScreens.register((MenuType) ModMenuTypes.TRAPPED_PRESENT_BLOCK.get(), TrappedPresentScreen::new);
        MenuScreens.register((MenuType) ModMenuTypes.NOTICE_BOARD.get(), NoticeBoardScreen::new);
        MenuScreens.register((MenuType) ModMenuTypes.CANNON.get(), CannonScreen::new);
        MenuScreens.register((MenuType) ModMenuTypes.RED_MERCHANT.get(), RedMerchantScreen::new);
        ClientHelper.registerRenderType((Block) ModRegistry.WIND_VANE.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.BOOK_PILE.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.BOOK_PILE_H.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.GLOBE.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.GLOBE_SEPIA.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.CRANK.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.SIGN_POST.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.BELLOWS.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.SCONCE_WALL.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.SCONCE.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.SCONCE_WALL_SOUL.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.SCONCE_SOUL.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.SCONCE_WALL_GREEN.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.SCONCE_GREEN.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.ITEM_SHELF.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.CAGE.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.SCONCE_LEVER.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.HOURGLASS.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.BLACKBOARD.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.GOLD_DOOR.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.GOLD_TRAPDOOR.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.BAMBOO_SPIKES.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.NETHERITE_DOOR.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.NETHERITE_TRAPDOOR.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.ROPE.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.FLAX.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.FLAX_WILD.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.FLAX_POT.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.JAR_BOAT.get(), RenderType.translucent());
        ClientHelper.registerRenderType((Block) ModRegistry.GOBLET.get(), RenderType.translucent(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.FAUCET.get(), RenderType.translucent(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.JAR.get(), RenderType.translucent(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.FLOWER_BOX.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModRegistry.TIMBER_FRAME.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.TIMBER_BRACE.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.TIMBER_CROSS_BRACE.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.COG_BLOCK.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.IRON_GATE.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.GOLD_GATE.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.GUNPOWDER_BLOCK.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.ROPE_KNOT.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) ModRegistry.CRYSTAL_DISPLAY.get(), RenderType.cutout());
        ModRegistry.CANDLE_HOLDERS.values().forEach(c -> ClientHelper.registerRenderType((Block) c.get(), RenderType.cutout()));
        ItemProperties.register(Items.CROSSBOW, Supplementaries.res("rope_arrow"), new ClientRegistry.CrossbowProperty((Item) ModRegistry.ROPE_ARROW_ITEM.get()));
        ClampedItemPropertyFunction antiqueProp = (itemStack, clientLevel, livingEntity, i) -> AntiqueInkItem.hasAntiqueInk(itemStack) ? 1.0F : 0.0F;
        ItemProperties.register(Items.WRITTEN_BOOK, Supplementaries.res("antique_ink"), antiqueProp);
        ItemProperties.register(Items.FILLED_MAP, Supplementaries.res("antique_ink"), antiqueProp);
        ItemProperties.register((Item) ModRegistry.SLINGSHOT_ITEM.get(), Supplementaries.res("pull"), (stack, world, entity, s) -> entity != null && entity.getUseItem() == stack ? (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / (float) SlingshotItem.getChargeDuration(stack) : 0.0F);
        ItemProperties.register((Item) ModRegistry.SLINGSHOT_ITEM.get(), Supplementaries.res("pulling"), (stack, world, entity, s) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        ItemProperties.register((Item) ModRegistry.BUBBLE_BLOWER.get(), Supplementaries.res("using"), (stack, world, entity, s) -> entity != null && entity.isUsingItem() && ForgeHelper.areStacksEqual(stack, entity.getUseItem(), true) ? 1.0F : 0.0F);
        ModRegistry.PRESENTS.values().forEach(i -> ItemProperties.register(((Block) i.get()).asItem(), Supplementaries.res("packed"), (stack, world, entity, s) -> 1.0F));
        ModRegistry.TRAPPED_PRESENTS.values().forEach(i -> ItemProperties.register(((Block) i.get()).asItem(), Supplementaries.res("primed"), (stack, world, entity, s) -> TrappedPresentBlockTile.isPrimed(stack) ? 1.0F : 0.0F));
        ItemProperties.register((Item) ModRegistry.CANDY_ITEM.get(), Supplementaries.res("wrapping"), (stack, world, entity, s) -> MiscUtils.FESTIVITY.getCandyWrappingIndex());
        ItemProperties.register((Item) ModRegistry.QUIVER_ITEM.get(), Supplementaries.res("dyed"), (stack, world, entity, s) -> ((DyeableLeatherItem) stack.getItem()).hasCustomColor(stack) ? 1.0F : 0.0F);
        ItemProperties.register((Item) ModRegistry.GLOBE_ITEM.get(), Supplementaries.res("type"), new ClientRegistry.GlobeProperty());
    }

    private static void registerKeyBinds(ClientHelper.KeyBindEvent event) {
        event.register(QUIVER_KEYBIND);
    }

    private static void registerParticles(ClientHelper.ParticleEvent event) {
        event.register((SimpleParticleType) ModParticles.SPEAKER_SOUND.get(), SpeakerSoundParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.GREEN_FLAME.get(), FlameParticle.Provider::new);
        event.register((SimpleParticleType) ModParticles.DRIPPING_LIQUID.get(), DrippingLiquidParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.FALLING_LIQUID.get(), FallingLiquidParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.SPLASHING_LIQUID.get(), SplashingLiquidParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.BOMB_EXPLOSION_PARTICLE.get(), BombExplosionParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.BOMB_EXPLOSION_PARTICLE_EMITTER.get(), BombExplosionEmitterParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.BOMB_SMOKE_PARTICLE.get(), BombSmokeParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.BOTTLING_XP_PARTICLE.get(), BottlingXpParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.FEATHER_PARTICLE.get(), FeatherParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.SLINGSHOT_PARTICLE.get(), SlingshotParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.STASIS_PARTICLE.get(), StasisParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.CONFETTI_PARTICLE.get(), ConfettiParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.ROTATION_TRAIL.get(), RotationTrailParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.ROTATION_TRAIL_EMITTER.get(), RotationTrailEmitter.Factory::new);
        event.register((SimpleParticleType) ModParticles.SUDS_PARTICLE.get(), SudsParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.ASH_PARTICLE.get(), ClientRegistry.AshParticleFactory::new);
        event.register((SimpleParticleType) ModParticles.BUBBLE_BLOCK_PARTICLE.get(), BubbleBlockParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.SUGAR_PARTICLE.get(), SugarParticle.Factory::new);
        event.register((SimpleParticleType) ModParticles.CANNON_FIRE_PARTICLE.get(), CannonFireParticle.Factory::new);
    }

    private static void registerEntityRenderers(ClientHelper.EntityRendererEvent event) {
        event.register((EntityType) ModEntities.BOMB.get(), context -> new ThrownItemRenderer(context, 1.0F, false));
        event.register((EntityType) ModEntities.THROWABLE_BRICK.get(), context -> new ThrownItemRenderer(context, 1.0F, false));
        event.register((EntityType) ModEntities.SLINGSHOT_PROJECTILE.get(), SlingshotProjectileRenderer::new);
        event.register((EntityType) ModEntities.DISPENSER_MINECART.get(), c -> new MinecartRenderer(c, ModelLayers.HOPPER_MINECART));
        event.register((EntityType) ModEntities.RED_MERCHANT.get(), RedMerchantRenderer::new);
        event.register((EntityType) ModEntities.HAT_STAND.get(), HatStandRenderer::new);
        event.register((EntityType) ModEntities.ROPE_ARROW.get(), RopeArrowRenderer::new);
        event.register((EntityType) ModEntities.FALLING_URN.get(), FallingBlockRenderer::new);
        event.register((EntityType) ModEntities.FALLING_ASH.get(), FallingBlockRendererGeneric::new);
        event.register((EntityType) ModEntities.FALLING_SACK.get(), FallingBlockRenderer::new);
        event.register((EntityType) ModEntities.PEARL_MARKER.get(), NoopRenderer::new);
    }

    private static void registerBlockEntityRenderers(ClientHelper.BlockEntityRendererEvent event) {
        event.register((BlockEntityType) ModRegistry.DOORMAT_TILE.get(), DoormatBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.CLOCK_BLOCK_TILE.get(), ClockBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.PEDESTAL_TILE.get(), PedestalBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.WIND_VANE_TILE.get(), WindVaneBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.NOTICE_BOARD_TILE.get(), NoticeBoardBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.JAR_TILE.get(), JarBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.SPRING_LAUNCHER_ARM_TILE.get(), SpringLauncherArmBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.SIGN_POST_TILE.get(), SignPostBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.BELLOWS_TILE.get(), BellowsBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.FLAG_TILE.get(), FlagBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.ITEM_SHELF_TILE.get(), ItemShelfBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.CAGE_TILE.get(), CageBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.GLOBE_TILE.get(), GlobeBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.HOURGLASS_TILE.get(), HourGlassBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.BLACKBOARD_TILE.get(), BlackboardBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.STATUE_TILE.get(), StatueBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.BOOK_PILE_TILE.get(), BookPileBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.JAR_BOAT_TILE.get(), JarBoatTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.BUBBLE_BLOCK_TILE.get(), BubbleBlockTileRenderer::new);
        event.register((BlockEntityType) ModRegistry.ENDERMAN_SKULL_TILE.get(), EndermanSkullBlockTileRenderer::new);
    }

    private static void registerSpecialModels(ClientHelper.SpecialModelEvent event) {
        FlowerPotHandler.CUSTOM_MODELS.forEach(event::register);
        ((Map) SIGN_POST_MODELS.get()).values().forEach(event::register);
        PlaceableBookManager.getAll().forEach(b -> event.register(b.modelPath()));
        event.register(BLACKBOARD_FRAME);
        event.register(BOAT_MODEL);
        event.register(ALTIMETER_TEMPLATE);
        event.register(ALTIMETER_OVERLAY);
        if (PlatHelper.getPlatform().isFabric()) {
            event.register(FLUTE_3D_MODEL);
            event.register(FLUTE_2D_MODEL);
            event.register(QUIVER_2D_MODEL);
            event.register(QUIVER_3D_MODEL);
        }
    }

    private static void registerModelLoaders(ClientHelper.ModelLoaderEvent event) {
        event.register(Supplementaries.res("frame_block"), new NestedModelLoader("overlay", FrameBlockBakedModel::new));
        event.register(Supplementaries.res("flower_box"), new NestedModelLoader("box", FlowerBoxBakedModel::new));
        event.register(Supplementaries.res("rope_knot"), new NestedModelLoader("knot", RopeKnotBlockBakedModel::new));
        event.register(Supplementaries.res("blackboard"), new NestedModelLoader("frame", BlackboardBakedModel::new));
        event.register(Supplementaries.res("mimic_block"), SignPostBlockBakedModel::new);
        event.register(Supplementaries.res("goblet"), new GobletModelLoader());
        event.register(Supplementaries.res("faucet"), new FaucetModelLoader());
        event.register(Supplementaries.res("book_pile"), BookPileModel::new);
        event.register(Supplementaries.res("bunting"), BuntingsBakedModel::new);
        event.register(Supplementaries.res("jar"), new JarModelLoader());
    }

    private static void registerItemDecorators(ClientHelper.ItemDecoratorEvent event) {
        event.register((ItemLike) ModRegistry.SLINGSHOT_ITEM.get(), new SlingshotItemOverlayRenderer());
        event.register((ItemLike) ModRegistry.QUIVER_ITEM.get(), new QuiverItemOverlayRenderer());
    }

    private static void registerTooltipComponent(ClientHelper.TooltipComponentEvent event) {
        event.register(BlackboardManager.Key.class, BlackboardTooltipComponent::new);
        event.register(QuiverTooltip.class, QuiverTooltipComponent::new);
        event.register(BannerPatternTooltip.class, BannerPatternTooltipComponent::new);
        event.register(PaintingTooltip.class, PaintingTooltipComponent::new);
        event.register(SherdTooltip.class, SherdTooltipComponent::new);
    }

    private static void registerBlockColors(ClientHelper.BlockColorEvent event) {
        event.register(new TippedSpikesColor(), (Block) ModRegistry.BAMBOO_SPIKES.get());
        event.register(new DefaultWaterColor(), (Block) ModRegistry.JAR_BOAT.get());
        event.register(new MimicBlockColor(), (Block) ModRegistry.SIGN_POST.get(), (Block) ModRegistry.TIMBER_BRACE.get(), ModRegistry.TIMBER_FRAME.get(), (Block) ModRegistry.TIMBER_CROSS_BRACE.get(), (Block) ModRegistry.ROPE_KNOT.get());
        event.register(new CogBlockColor(), (Block) ModRegistry.COG_BLOCK.get());
        event.register(new GunpowderBlockColor(), (Block) ModRegistry.GUNPOWDER_BLOCK.get());
        event.register(new FlowerBoxColor(), (Block) ModRegistry.FLOWER_BOX.get());
        event.register(new FluidColor(), (Block) ModRegistry.GOBLET.get(), (Block) ModRegistry.JAR.get());
    }

    private static void registerItemColors(ClientHelper.ItemColorEvent event) {
        event.register(new TippedSpikesColor(), (ItemLike) ModRegistry.BAMBOO_SPIKES_TIPPED_ITEM.get());
        event.register(new DefaultWaterColor(), (ItemLike) ModRegistry.JAR_BOAT.get());
        event.register((itemStack, i) -> i != 1 ? -1 : ((DyeableLeatherItem) itemStack.getItem()).getColor(itemStack), (ItemLike) ModRegistry.QUIVER_ITEM.get());
    }

    private static void registerModelLayers(ClientHelper.ModelLayerEvent event) {
        event.register(BELLOWS_MODEL, BellowsBlockTileRenderer::createMesh);
        event.register(CLOCK_HANDS_MODEL, ClockBlockTileRenderer::createMesh);
        event.register(GLOBE_BASE_MODEL, GlobeBlockTileRenderer::createBaseMesh);
        event.register(GLOBE_SPECIAL_MODEL, GlobeBlockTileRenderer::createSpecialMesh);
        event.register(RED_MERCHANT_MODEL, RedMerchantRenderer::createMesh);
        event.register(HAT_STAND_MODEL, HatStandModel::createMesh);
        event.register(HAT_STAND_MODEL_ARMOR, HatStandModel::createArmorMesh);
        event.register(JARVIS_MODEL, JarredModel::createMesh);
        event.register(JAR_MODEL, JarredHeadLayer::createMesh);
        event.register(PICKLE_MODEL, PickleModel::createMesh);
        event.register(ENDERMAN_HEAD_MODEL, EndermanSkullModel::createMesh);
        event.register(CANNON_MODEL, CannonBlockTileRenderer::createMesh);
        event.register(WIND_VANE_MODEL, WindVaneBlockTileRenderer::createMesh);
        event.register(BUNTING_MODEL, BuntingBlockTileRenderer::createMesh);
    }

    public static LevelLightEngine getLightEngine() {
        return Minecraft.getInstance().level.m_5518_();
    }

    private static class AshParticleFactory extends SnowflakeParticle.Provider {

        public AshParticleFactory(SpriteSet pSprites) {
            super(pSprites);
        }

        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            Particle p = super.createParticle(pType, pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
            if (p != null) {
                p.setColor(0.42352942F, 0.40392157F, 0.40392157F);
            }
            return p;
        }
    }

    private static record CrossbowProperty(Item projectile) implements ClampedItemPropertyFunction {

        @Override
        public float call(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed) {
            return entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, this.projectile) ? 1.0F : 0.0F;
        }

        @Override
        public float unclampedCall(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
            return 0.0F;
        }
    }

    private static class GlobeProperty implements ClampedItemPropertyFunction {

        @Override
        public float call(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
            CompoundTag compoundTag = itemStack.getTagElement("display");
            if (compoundTag != null) {
                String n = compoundTag.getString("Name");
                MutableComponent mutableComponent = Component.Serializer.fromJson(n);
                if (mutableComponent != null) {
                    Integer v = GlobeManager.Type.getTextureID(mutableComponent.getString());
                    if (v != null) {
                        return Float.valueOf((float) v.intValue());
                    }
                }
            }
            return Float.NEGATIVE_INFINITY;
        }

        @Override
        public float unclampedCall(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
            return this.call(itemStack, clientLevel, livingEntity, i);
        }
    }
}