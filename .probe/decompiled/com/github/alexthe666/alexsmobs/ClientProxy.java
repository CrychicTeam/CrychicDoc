package com.github.alexthe666.alexsmobs;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.client.ClientLayerRegistry;
import com.github.alexthe666.alexsmobs.client.event.ClientEvents;
import com.github.alexthe666.alexsmobs.client.gui.GUIAnimalDictionary;
import com.github.alexthe666.alexsmobs.client.gui.GUITransmutationTable;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.client.particle.ParticleBearFreddy;
import com.github.alexthe666.alexsmobs.client.particle.ParticleBirdSong;
import com.github.alexthe666.alexsmobs.client.particle.ParticleBunfungusTransformation;
import com.github.alexthe666.alexsmobs.client.particle.ParticleDna;
import com.github.alexthe666.alexsmobs.client.particle.ParticleFungusBubble;
import com.github.alexthe666.alexsmobs.client.particle.ParticleGusterSandShot;
import com.github.alexthe666.alexsmobs.client.particle.ParticleGusterSandSpin;
import com.github.alexthe666.alexsmobs.client.particle.ParticleHemolymph;
import com.github.alexthe666.alexsmobs.client.particle.ParticleInvertDig;
import com.github.alexthe666.alexsmobs.client.particle.ParticlePlatypus;
import com.github.alexthe666.alexsmobs.client.particle.ParticleSimpleHeart;
import com.github.alexthe666.alexsmobs.client.particle.ParticleSkulkBoom;
import com.github.alexthe666.alexsmobs.client.particle.ParticleSmelly;
import com.github.alexthe666.alexsmobs.client.particle.ParticleStaticSpark;
import com.github.alexthe666.alexsmobs.client.particle.ParticleSunbirdFeather;
import com.github.alexthe666.alexsmobs.client.particle.ParticleTeethGlint;
import com.github.alexthe666.alexsmobs.client.particle.ParticleWhaleSplash;
import com.github.alexthe666.alexsmobs.client.particle.ParticleWormPortal;
import com.github.alexthe666.alexsmobs.client.render.AMRenderTypes;
import com.github.alexthe666.alexsmobs.client.render.RenderAlligatorSnappingTurtle;
import com.github.alexthe666.alexsmobs.client.render.RenderAnaconda;
import com.github.alexthe666.alexsmobs.client.render.RenderAnacondaPart;
import com.github.alexthe666.alexsmobs.client.render.RenderAnteater;
import com.github.alexthe666.alexsmobs.client.render.RenderBaldEagle;
import com.github.alexthe666.alexsmobs.client.render.RenderBananaSlug;
import com.github.alexthe666.alexsmobs.client.render.RenderBison;
import com.github.alexthe666.alexsmobs.client.render.RenderBlobfish;
import com.github.alexthe666.alexsmobs.client.render.RenderBlueJay;
import com.github.alexthe666.alexsmobs.client.render.RenderBoneSerpent;
import com.github.alexthe666.alexsmobs.client.render.RenderBoneSerpentPart;
import com.github.alexthe666.alexsmobs.client.render.RenderBunfungus;
import com.github.alexthe666.alexsmobs.client.render.RenderCachalotEcho;
import com.github.alexthe666.alexsmobs.client.render.RenderCachalotWhale;
import com.github.alexthe666.alexsmobs.client.render.RenderCaiman;
import com.github.alexthe666.alexsmobs.client.render.RenderCapuchinMonkey;
import com.github.alexthe666.alexsmobs.client.render.RenderCatfish;
import com.github.alexthe666.alexsmobs.client.render.RenderCentipedeBody;
import com.github.alexthe666.alexsmobs.client.render.RenderCentipedeHead;
import com.github.alexthe666.alexsmobs.client.render.RenderCentipedeTail;
import com.github.alexthe666.alexsmobs.client.render.RenderCockroach;
import com.github.alexthe666.alexsmobs.client.render.RenderCombJelly;
import com.github.alexthe666.alexsmobs.client.render.RenderCosmaw;
import com.github.alexthe666.alexsmobs.client.render.RenderCosmicCod;
import com.github.alexthe666.alexsmobs.client.render.RenderCrimsonMosquito;
import com.github.alexthe666.alexsmobs.client.render.RenderCrocodile;
import com.github.alexthe666.alexsmobs.client.render.RenderCrow;
import com.github.alexthe666.alexsmobs.client.render.RenderDevilsHolePupfish;
import com.github.alexthe666.alexsmobs.client.render.RenderDropBear;
import com.github.alexthe666.alexsmobs.client.render.RenderElephant;
import com.github.alexthe666.alexsmobs.client.render.RenderEmu;
import com.github.alexthe666.alexsmobs.client.render.RenderEndergrade;
import com.github.alexthe666.alexsmobs.client.render.RenderEnderiophage;
import com.github.alexthe666.alexsmobs.client.render.RenderFarseer;
import com.github.alexthe666.alexsmobs.client.render.RenderFart;
import com.github.alexthe666.alexsmobs.client.render.RenderFlutter;
import com.github.alexthe666.alexsmobs.client.render.RenderFly;
import com.github.alexthe666.alexsmobs.client.render.RenderFlyingFish;
import com.github.alexthe666.alexsmobs.client.render.RenderFrilledShark;
import com.github.alexthe666.alexsmobs.client.render.RenderFroststalker;
import com.github.alexthe666.alexsmobs.client.render.RenderGazelle;
import com.github.alexthe666.alexsmobs.client.render.RenderGeladaMonkey;
import com.github.alexthe666.alexsmobs.client.render.RenderGiantSquid;
import com.github.alexthe666.alexsmobs.client.render.RenderGorilla;
import com.github.alexthe666.alexsmobs.client.render.RenderGrizzlyBear;
import com.github.alexthe666.alexsmobs.client.render.RenderGust;
import com.github.alexthe666.alexsmobs.client.render.RenderGuster;
import com.github.alexthe666.alexsmobs.client.render.RenderHammerheadShark;
import com.github.alexthe666.alexsmobs.client.render.RenderHemolymph;
import com.github.alexthe666.alexsmobs.client.render.RenderHummingbird;
import com.github.alexthe666.alexsmobs.client.render.RenderIceShard;
import com.github.alexthe666.alexsmobs.client.render.RenderJerboa;
import com.github.alexthe666.alexsmobs.client.render.RenderKangaroo;
import com.github.alexthe666.alexsmobs.client.render.RenderKomodoDragon;
import com.github.alexthe666.alexsmobs.client.render.RenderLaviathan;
import com.github.alexthe666.alexsmobs.client.render.RenderLeafcutterAnt;
import com.github.alexthe666.alexsmobs.client.render.RenderLobster;
import com.github.alexthe666.alexsmobs.client.render.RenderManedWolf;
import com.github.alexthe666.alexsmobs.client.render.RenderMantisShrimp;
import com.github.alexthe666.alexsmobs.client.render.RenderMimicOctopus;
import com.github.alexthe666.alexsmobs.client.render.RenderMimicube;
import com.github.alexthe666.alexsmobs.client.render.RenderMoose;
import com.github.alexthe666.alexsmobs.client.render.RenderMosquitoSpit;
import com.github.alexthe666.alexsmobs.client.render.RenderMudBall;
import com.github.alexthe666.alexsmobs.client.render.RenderMudskipper;
import com.github.alexthe666.alexsmobs.client.render.RenderMungus;
import com.github.alexthe666.alexsmobs.client.render.RenderMurmurBody;
import com.github.alexthe666.alexsmobs.client.render.RenderMurmurHead;
import com.github.alexthe666.alexsmobs.client.render.RenderOrca;
import com.github.alexthe666.alexsmobs.client.render.RenderPlatypus;
import com.github.alexthe666.alexsmobs.client.render.RenderPollenBall;
import com.github.alexthe666.alexsmobs.client.render.RenderPotoo;
import com.github.alexthe666.alexsmobs.client.render.RenderRaccoon;
import com.github.alexthe666.alexsmobs.client.render.RenderRainFrog;
import com.github.alexthe666.alexsmobs.client.render.RenderRattlesnake;
import com.github.alexthe666.alexsmobs.client.render.RenderRhinoceros;
import com.github.alexthe666.alexsmobs.client.render.RenderRoadrunner;
import com.github.alexthe666.alexsmobs.client.render.RenderRockyRoller;
import com.github.alexthe666.alexsmobs.client.render.RenderSandShot;
import com.github.alexthe666.alexsmobs.client.render.RenderSeaBear;
import com.github.alexthe666.alexsmobs.client.render.RenderSeagull;
import com.github.alexthe666.alexsmobs.client.render.RenderSeal;
import com.github.alexthe666.alexsmobs.client.render.RenderSharkToothArrow;
import com.github.alexthe666.alexsmobs.client.render.RenderShoebill;
import com.github.alexthe666.alexsmobs.client.render.RenderSkelewag;
import com.github.alexthe666.alexsmobs.client.render.RenderSkreecher;
import com.github.alexthe666.alexsmobs.client.render.RenderSkunk;
import com.github.alexthe666.alexsmobs.client.render.RenderSnowLeopard;
import com.github.alexthe666.alexsmobs.client.render.RenderSoulVulture;
import com.github.alexthe666.alexsmobs.client.render.RenderSpectre;
import com.github.alexthe666.alexsmobs.client.render.RenderSquidGrapple;
import com.github.alexthe666.alexsmobs.client.render.RenderStraddleboard;
import com.github.alexthe666.alexsmobs.client.render.RenderStraddler;
import com.github.alexthe666.alexsmobs.client.render.RenderStradpole;
import com.github.alexthe666.alexsmobs.client.render.RenderSugarGlider;
import com.github.alexthe666.alexsmobs.client.render.RenderSunbird;
import com.github.alexthe666.alexsmobs.client.render.RenderTarantulaHawk;
import com.github.alexthe666.alexsmobs.client.render.RenderTasmanianDevil;
import com.github.alexthe666.alexsmobs.client.render.RenderTendonSegment;
import com.github.alexthe666.alexsmobs.client.render.RenderTerrapin;
import com.github.alexthe666.alexsmobs.client.render.RenderTiger;
import com.github.alexthe666.alexsmobs.client.render.RenderTossedItem;
import com.github.alexthe666.alexsmobs.client.render.RenderToucan;
import com.github.alexthe666.alexsmobs.client.render.RenderTriops;
import com.github.alexthe666.alexsmobs.client.render.RenderTusklin;
import com.github.alexthe666.alexsmobs.client.render.RenderUnderminer;
import com.github.alexthe666.alexsmobs.client.render.RenderVineLasso;
import com.github.alexthe666.alexsmobs.client.render.RenderVoidPortal;
import com.github.alexthe666.alexsmobs.client.render.RenderVoidWormBody;
import com.github.alexthe666.alexsmobs.client.render.RenderVoidWormHead;
import com.github.alexthe666.alexsmobs.client.render.RenderVoidWormShot;
import com.github.alexthe666.alexsmobs.client.render.RenderWarpedMosco;
import com.github.alexthe666.alexsmobs.client.render.RenderWarpedToad;
import com.github.alexthe666.alexsmobs.client.render.item.AMItemRenderProperties;
import com.github.alexthe666.alexsmobs.client.render.item.CustomArmorRenderProperties;
import com.github.alexthe666.alexsmobs.client.render.item.GhostlyPickaxeBakedModel;
import com.github.alexthe666.alexsmobs.client.render.tile.RenderCapsid;
import com.github.alexthe666.alexsmobs.client.render.tile.RenderTransmutationTable;
import com.github.alexthe666.alexsmobs.client.render.tile.RenderVoidWormBeak;
import com.github.alexthe666.alexsmobs.client.sound.SoundBearMusicBox;
import com.github.alexthe666.alexsmobs.client.sound.SoundLaCucaracha;
import com.github.alexthe666.alexsmobs.client.sound.SoundWormBoss;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityBlueJay;
import com.github.alexthe666.alexsmobs.entity.EntityCockroach;
import com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear;
import com.github.alexthe666.alexsmobs.entity.EntityVoidWorm;
import com.github.alexthe666.alexsmobs.entity.util.RainbowUtil;
import com.github.alexthe666.alexsmobs.inventory.AMMenuRegistry;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.item.ItemBloodSprayer;
import com.github.alexthe666.alexsmobs.item.ItemHemolymphBlaster;
import com.github.alexthe666.alexsmobs.item.ItemTarantulaHawkElytra;
import com.github.alexthe666.alexsmobs.item.ItemTendonWhip;
import com.github.alexthe666.alexsmobs.tileentity.AMTileEntityRegistry;
import com.mojang.blaze3d.vertex.BufferBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = "alexsmobs", value = { Dist.CLIENT })
public class ClientProxy extends CommonProxy {

    public static final Int2ObjectMap<SoundBearMusicBox> BEAR_MUSIC_BOX_SOUND_MAP = new Int2ObjectOpenHashMap();

    public static final Int2ObjectMap<SoundLaCucaracha> COCKROACH_SOUND_MAP = new Int2ObjectOpenHashMap();

    public static final Int2ObjectMap<SoundWormBoss> WORMBOSS_SOUND_MAP = new Int2ObjectOpenHashMap();

    public static final List<UUID> currentUnrenderedEntities = new ArrayList();

    public static int voidPortalCreationTime = 0;

    public CameraType prevPOV = CameraType.FIRST_PERSON;

    public boolean initializedRainbowBuffers = false;

    private int pupfishChunkX = 0;

    private int pupfishChunkZ = 0;

    private int singingBlueJayId = -1;

    private final ItemStack[] transmuteStacks = new ItemStack[3];

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onItemColors(RegisterColorHandlersEvent.Item event) {
        AlexsMobs.LOGGER.info("loaded in item colorizer");
        if (AMItemRegistry.STRADDLEBOARD.isPresent()) {
            event.register((stack, colorIn) -> colorIn < 1 ? -1 : ((DyeableLeatherItem) stack.getItem()).getColor(stack), AMItemRegistry.STRADDLEBOARD.get());
        } else {
            AlexsMobs.LOGGER.warn("Could not add straddleboard item to colorizer...");
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onBlockColors(RegisterColorHandlersEvent.Block event) {
        AlexsMobs.LOGGER.info("loaded in block colorizer");
        event.register((state, tintGetter, pos, tint) -> tintGetter != null && pos != null ? RainbowUtil.calculateGlassColor(pos) : -1, AMBlockRegistry.RAINBOW_GLASS.get());
    }

    @Override
    public void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ClientProxy::onBakingCompleted);
        bus.addListener(ClientProxy::onItemColors);
        bus.addListener(ClientProxy::onBlockColors);
        bus.addListener(ClientLayerRegistry::onAddLayers);
        bus.addListener(ClientProxy::setupParticles);
    }

    @Override
    public void clientInit() {
        this.initRainbowBuffers();
        ItemRenderer itemRendererIn = Minecraft.getInstance().getItemRenderer();
        EntityRenderers.register(AMEntityRegistry.GRIZZLY_BEAR.get(), RenderGrizzlyBear::new);
        EntityRenderers.register(AMEntityRegistry.ROADRUNNER.get(), RenderRoadrunner::new);
        EntityRenderers.register(AMEntityRegistry.BONE_SERPENT.get(), RenderBoneSerpent::new);
        EntityRenderers.register(AMEntityRegistry.BONE_SERPENT_PART.get(), RenderBoneSerpentPart::new);
        EntityRenderers.register(AMEntityRegistry.GAZELLE.get(), RenderGazelle::new);
        EntityRenderers.register(AMEntityRegistry.CROCODILE.get(), RenderCrocodile::new);
        EntityRenderers.register(AMEntityRegistry.FLY.get(), RenderFly::new);
        EntityRenderers.register(AMEntityRegistry.HUMMINGBIRD.get(), RenderHummingbird::new);
        EntityRenderers.register(AMEntityRegistry.ORCA.get(), RenderOrca::new);
        EntityRenderers.register(AMEntityRegistry.SUNBIRD.get(), RenderSunbird::new);
        EntityRenderers.register(AMEntityRegistry.GORILLA.get(), RenderGorilla::new);
        EntityRenderers.register(AMEntityRegistry.CRIMSON_MOSQUITO.get(), RenderCrimsonMosquito::new);
        EntityRenderers.register(AMEntityRegistry.MOSQUITO_SPIT.get(), RenderMosquitoSpit::new);
        EntityRenderers.register(AMEntityRegistry.RATTLESNAKE.get(), RenderRattlesnake::new);
        EntityRenderers.register(AMEntityRegistry.ENDERGRADE.get(), RenderEndergrade::new);
        EntityRenderers.register(AMEntityRegistry.HAMMERHEAD_SHARK.get(), RenderHammerheadShark::new);
        EntityRenderers.register(AMEntityRegistry.SHARK_TOOTH_ARROW.get(), RenderSharkToothArrow::new);
        EntityRenderers.register(AMEntityRegistry.LOBSTER.get(), RenderLobster::new);
        EntityRenderers.register(AMEntityRegistry.KOMODO_DRAGON.get(), RenderKomodoDragon::new);
        EntityRenderers.register(AMEntityRegistry.CAPUCHIN_MONKEY.get(), RenderCapuchinMonkey::new);
        EntityRenderers.register(AMEntityRegistry.TOSSED_ITEM.get(), RenderTossedItem::new);
        EntityRenderers.register(AMEntityRegistry.CENTIPEDE_HEAD.get(), RenderCentipedeHead::new);
        EntityRenderers.register(AMEntityRegistry.CENTIPEDE_BODY.get(), RenderCentipedeBody::new);
        EntityRenderers.register(AMEntityRegistry.CENTIPEDE_TAIL.get(), RenderCentipedeTail::new);
        EntityRenderers.register(AMEntityRegistry.WARPED_TOAD.get(), RenderWarpedToad::new);
        EntityRenderers.register(AMEntityRegistry.MOOSE.get(), RenderMoose::new);
        EntityRenderers.register(AMEntityRegistry.MIMICUBE.get(), RenderMimicube::new);
        EntityRenderers.register(AMEntityRegistry.RACCOON.get(), RenderRaccoon::new);
        EntityRenderers.register(AMEntityRegistry.BLOBFISH.get(), RenderBlobfish::new);
        EntityRenderers.register(AMEntityRegistry.SEAL.get(), RenderSeal::new);
        EntityRenderers.register(AMEntityRegistry.COCKROACH.get(), RenderCockroach::new);
        EntityRenderers.register(AMEntityRegistry.COCKROACH_EGG.get(), render -> new ThrownItemRenderer(render, 0.75F, true));
        EntityRenderers.register(AMEntityRegistry.SHOEBILL.get(), RenderShoebill::new);
        EntityRenderers.register(AMEntityRegistry.ELEPHANT.get(), RenderElephant::new);
        EntityRenderers.register(AMEntityRegistry.SOUL_VULTURE.get(), RenderSoulVulture::new);
        EntityRenderers.register(AMEntityRegistry.SNOW_LEOPARD.get(), RenderSnowLeopard::new);
        EntityRenderers.register(AMEntityRegistry.SPECTRE.get(), RenderSpectre::new);
        EntityRenderers.register(AMEntityRegistry.CROW.get(), RenderCrow::new);
        EntityRenderers.register(AMEntityRegistry.ALLIGATOR_SNAPPING_TURTLE.get(), RenderAlligatorSnappingTurtle::new);
        EntityRenderers.register(AMEntityRegistry.MUNGUS.get(), RenderMungus::new);
        EntityRenderers.register(AMEntityRegistry.MANTIS_SHRIMP.get(), RenderMantisShrimp::new);
        EntityRenderers.register(AMEntityRegistry.GUSTER.get(), RenderGuster::new);
        EntityRenderers.register(AMEntityRegistry.SAND_SHOT.get(), RenderSandShot::new);
        EntityRenderers.register(AMEntityRegistry.GUST.get(), RenderGust::new);
        EntityRenderers.register(AMEntityRegistry.WARPED_MOSCO.get(), RenderWarpedMosco::new);
        EntityRenderers.register(AMEntityRegistry.HEMOLYMPH.get(), RenderHemolymph::new);
        EntityRenderers.register(AMEntityRegistry.STRADDLER.get(), RenderStraddler::new);
        EntityRenderers.register(AMEntityRegistry.STRADPOLE.get(), RenderStradpole::new);
        EntityRenderers.register(AMEntityRegistry.STRADDLEBOARD.get(), RenderStraddleboard::new);
        EntityRenderers.register(AMEntityRegistry.EMU.get(), RenderEmu::new);
        EntityRenderers.register(AMEntityRegistry.EMU_EGG.get(), render -> new ThrownItemRenderer(render, 0.75F, true));
        EntityRenderers.register(AMEntityRegistry.PLATYPUS.get(), RenderPlatypus::new);
        EntityRenderers.register(AMEntityRegistry.DROPBEAR.get(), RenderDropBear::new);
        EntityRenderers.register(AMEntityRegistry.TASMANIAN_DEVIL.get(), RenderTasmanianDevil::new);
        EntityRenderers.register(AMEntityRegistry.KANGAROO.get(), RenderKangaroo::new);
        EntityRenderers.register(AMEntityRegistry.CACHALOT_WHALE.get(), RenderCachalotWhale::new);
        EntityRenderers.register(AMEntityRegistry.CACHALOT_ECHO.get(), RenderCachalotEcho::new);
        EntityRenderers.register(AMEntityRegistry.LEAFCUTTER_ANT.get(), RenderLeafcutterAnt::new);
        EntityRenderers.register(AMEntityRegistry.ENDERIOPHAGE.get(), RenderEnderiophage::new);
        EntityRenderers.register(AMEntityRegistry.ENDERIOPHAGE_ROCKET.get(), render -> new ThrownItemRenderer(render, 0.75F, true));
        EntityRenderers.register(AMEntityRegistry.BALD_EAGLE.get(), RenderBaldEagle::new);
        EntityRenderers.register(AMEntityRegistry.TIGER.get(), RenderTiger::new);
        EntityRenderers.register(AMEntityRegistry.TARANTULA_HAWK.get(), RenderTarantulaHawk::new);
        EntityRenderers.register(AMEntityRegistry.VOID_WORM.get(), RenderVoidWormHead::new);
        EntityRenderers.register(AMEntityRegistry.VOID_WORM_PART.get(), RenderVoidWormBody::new);
        EntityRenderers.register(AMEntityRegistry.VOID_WORM_SHOT.get(), RenderVoidWormShot::new);
        EntityRenderers.register(AMEntityRegistry.VOID_PORTAL.get(), RenderVoidPortal::new);
        EntityRenderers.register(AMEntityRegistry.FRILLED_SHARK.get(), RenderFrilledShark::new);
        EntityRenderers.register(AMEntityRegistry.MIMIC_OCTOPUS.get(), RenderMimicOctopus::new);
        EntityRenderers.register(AMEntityRegistry.SEAGULL.get(), RenderSeagull::new);
        EntityRenderers.register(AMEntityRegistry.FROSTSTALKER.get(), RenderFroststalker::new);
        EntityRenderers.register(AMEntityRegistry.ICE_SHARD.get(), RenderIceShard::new);
        EntityRenderers.register(AMEntityRegistry.TUSKLIN.get(), RenderTusklin::new);
        EntityRenderers.register(AMEntityRegistry.LAVIATHAN.get(), RenderLaviathan::new);
        EntityRenderers.register(AMEntityRegistry.COSMAW.get(), RenderCosmaw::new);
        EntityRenderers.register(AMEntityRegistry.TOUCAN.get(), RenderToucan::new);
        EntityRenderers.register(AMEntityRegistry.MANED_WOLF.get(), RenderManedWolf::new);
        EntityRenderers.register(AMEntityRegistry.ANACONDA.get(), RenderAnaconda::new);
        EntityRenderers.register(AMEntityRegistry.ANACONDA_PART.get(), RenderAnacondaPart::new);
        EntityRenderers.register(AMEntityRegistry.VINE_LASSO.get(), RenderVineLasso::new);
        EntityRenderers.register(AMEntityRegistry.ANTEATER.get(), RenderAnteater::new);
        EntityRenderers.register(AMEntityRegistry.ROCKY_ROLLER.get(), RenderRockyRoller::new);
        EntityRenderers.register(AMEntityRegistry.FLUTTER.get(), RenderFlutter::new);
        EntityRenderers.register(AMEntityRegistry.POLLEN_BALL.get(), RenderPollenBall::new);
        EntityRenderers.register(AMEntityRegistry.GELADA_MONKEY.get(), RenderGeladaMonkey::new);
        EntityRenderers.register(AMEntityRegistry.JERBOA.get(), RenderJerboa::new);
        EntityRenderers.register(AMEntityRegistry.TERRAPIN.get(), RenderTerrapin::new);
        EntityRenderers.register(AMEntityRegistry.COMB_JELLY.get(), RenderCombJelly::new);
        EntityRenderers.register(AMEntityRegistry.COSMIC_COD.get(), RenderCosmicCod::new);
        EntityRenderers.register(AMEntityRegistry.BUNFUNGUS.get(), RenderBunfungus::new);
        EntityRenderers.register(AMEntityRegistry.BISON.get(), RenderBison::new);
        EntityRenderers.register(AMEntityRegistry.GIANT_SQUID.get(), RenderGiantSquid::new);
        EntityRenderers.register(AMEntityRegistry.SQUID_GRAPPLE.get(), RenderSquidGrapple::new);
        EntityRenderers.register(AMEntityRegistry.SEA_BEAR.get(), RenderSeaBear::new);
        EntityRenderers.register(AMEntityRegistry.DEVILS_HOLE_PUPFISH.get(), RenderDevilsHolePupfish::new);
        EntityRenderers.register(AMEntityRegistry.CATFISH.get(), RenderCatfish::new);
        EntityRenderers.register(AMEntityRegistry.FLYING_FISH.get(), RenderFlyingFish::new);
        EntityRenderers.register(AMEntityRegistry.SKELEWAG.get(), RenderSkelewag::new);
        EntityRenderers.register(AMEntityRegistry.RAIN_FROG.get(), RenderRainFrog::new);
        EntityRenderers.register(AMEntityRegistry.POTOO.get(), RenderPotoo::new);
        EntityRenderers.register(AMEntityRegistry.MUDSKIPPER.get(), RenderMudskipper::new);
        EntityRenderers.register(AMEntityRegistry.MUD_BALL.get(), RenderMudBall::new);
        EntityRenderers.register(AMEntityRegistry.RHINOCEROS.get(), RenderRhinoceros::new);
        EntityRenderers.register(AMEntityRegistry.SUGAR_GLIDER.get(), RenderSugarGlider::new);
        EntityRenderers.register(AMEntityRegistry.FARSEER.get(), RenderFarseer::new);
        EntityRenderers.register(AMEntityRegistry.SKREECHER.get(), RenderSkreecher::new);
        EntityRenderers.register(AMEntityRegistry.UNDERMINER.get(), RenderUnderminer::new);
        EntityRenderers.register(AMEntityRegistry.MURMUR.get(), RenderMurmurBody::new);
        EntityRenderers.register(AMEntityRegistry.MURMUR_HEAD.get(), RenderMurmurHead::new);
        EntityRenderers.register(AMEntityRegistry.TENDON_SEGMENT.get(), RenderTendonSegment::new);
        EntityRenderers.register(AMEntityRegistry.SKUNK.get(), RenderSkunk::new);
        EntityRenderers.register(AMEntityRegistry.FART.get(), RenderFart::new);
        EntityRenderers.register(AMEntityRegistry.BANANA_SLUG.get(), RenderBananaSlug::new);
        EntityRenderers.register(AMEntityRegistry.BLUE_JAY.get(), RenderBlueJay::new);
        EntityRenderers.register(AMEntityRegistry.CAIMAN.get(), RenderCaiman::new);
        EntityRenderers.register(AMEntityRegistry.TRIOPS.get(), RenderTriops::new);
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        try {
            ItemProperties.register(AMItemRegistry.BLOOD_SPRAYER.get(), new ResourceLocation("empty"), (stack, p_239428_1_, p_239428_2_, j) -> ItemBloodSprayer.isUsable(stack) && (!(p_239428_2_ instanceof Player) || !((Player) p_239428_2_).getCooldowns().isOnCooldown(AMItemRegistry.BLOOD_SPRAYER.get())) ? 0.0F : 1.0F);
            ItemProperties.register(AMItemRegistry.HEMOLYMPH_BLASTER.get(), new ResourceLocation("empty"), (stack, p_239428_1_, p_239428_2_, j) -> ItemHemolymphBlaster.isUsable(stack) && (!(p_239428_2_ instanceof Player) || !((Player) p_239428_2_).getCooldowns().isOnCooldown(AMItemRegistry.HEMOLYMPH_BLASTER.get())) ? 0.0F : 1.0F);
            ItemProperties.register(AMItemRegistry.TARANTULA_HAWK_ELYTRA.get(), new ResourceLocation("broken"), (stack, p_239428_1_, p_239428_2_, j) -> ItemTarantulaHawkElytra.isUsable(stack) ? 0.0F : 1.0F);
            ItemProperties.register(AMItemRegistry.SHIELD_OF_THE_DEEP.get(), new ResourceLocation("blocking"), (stack, p_239421_1_, p_239421_2_, j) -> p_239421_2_ != null && p_239421_2_.isUsingItem() && p_239421_2_.getUseItem() == stack ? 1.0F : 0.0F);
            ItemProperties.register(AMItemRegistry.SOMBRERO.get(), new ResourceLocation("silly"), (stack, p_239421_1_, p_239421_2_, j) -> AlexsMobs.isAprilFools() ? 1.0F : 0.0F);
            ItemProperties.register(AMItemRegistry.TENDON_WHIP.get(), new ResourceLocation("active"), (stack, p_239421_1_, holder, j) -> ItemTendonWhip.isActive(stack, holder) ? 1.0F : 0.0F);
            ItemProperties.register(AMItemRegistry.PUPFISH_LOCATOR.get(), new ResourceLocation("in_chunk"), (stack, world, entity, j) -> {
                int x = this.pupfishChunkX * 16;
                int z = this.pupfishChunkZ * 16;
                return entity != null && entity.m_20185_() >= (double) x && entity.m_20185_() <= (double) (x + 16) && entity.m_20189_() >= (double) z && entity.m_20189_() <= (double) (z + 16) ? 1.0F : 0.0F;
            });
            ItemProperties.register(AMItemRegistry.SKELEWAG_SWORD.get(), new ResourceLocation("blocking"), (stack, p_239421_1_, p_239421_2_, j) -> p_239421_2_ != null && p_239421_2_.isUsingItem() && p_239421_2_.getUseItem() == stack ? 1.0F : 0.0F);
        } catch (Exception var3) {
            AlexsMobs.LOGGER.warn("Could not load item models for weapons");
        }
        BlockEntityRenderers.register(AMTileEntityRegistry.CAPSID.get(), RenderCapsid::new);
        BlockEntityRenderers.register(AMTileEntityRegistry.VOID_WORM_BEAK.get(), RenderVoidWormBeak::new);
        BlockEntityRenderers.register(AMTileEntityRegistry.TRANSMUTATION_TABLE.get(), RenderTransmutationTable::new);
        MenuScreens.register(AMMenuRegistry.TRANSMUTATION_TABLE.get(), GUITransmutationTable::new);
    }

    private void initRainbowBuffers() {
        Minecraft.getInstance().renderBuffers().fixedBuffers.put(AMRenderTypes.COMBJELLY_RAINBOW_GLINT, new BufferBuilder(AMRenderTypes.COMBJELLY_RAINBOW_GLINT.bufferSize()));
        Minecraft.getInstance().renderBuffers().fixedBuffers.put(AMRenderTypes.VOID_WORM_PORTAL_OVERLAY, new BufferBuilder(AMRenderTypes.VOID_WORM_PORTAL_OVERLAY.bufferSize()));
        Minecraft.getInstance().renderBuffers().fixedBuffers.put(AMRenderTypes.STATIC_PORTAL, new BufferBuilder(AMRenderTypes.STATIC_PORTAL.bufferSize()));
        Minecraft.getInstance().renderBuffers().fixedBuffers.put(AMRenderTypes.STATIC_PARTICLE, new BufferBuilder(AMRenderTypes.STATIC_PARTICLE.bufferSize()));
        Minecraft.getInstance().renderBuffers().fixedBuffers.put(AMRenderTypes.STATIC_ENTITY, new BufferBuilder(AMRenderTypes.STATIC_ENTITY.bufferSize()));
        this.initializedRainbowBuffers = true;
    }

    private static void onBakingCompleted(ModelEvent.ModifyBakingResult e) {
        String ghostlyPickaxe = "alexsmobs:ghostly_pickaxe";
        for (ResourceLocation id : e.getModels().keySet()) {
            if (id.toString().contains(ghostlyPickaxe)) {
                e.getModels().put(id, new GhostlyPickaxeBakedModel((BakedModel) e.getModels().get(id)));
            }
        }
    }

    @Override
    public void openBookGUI(ItemStack itemStackIn) {
        Minecraft.getInstance().setScreen(new GUIAnimalDictionary(itemStackIn));
    }

    @Override
    public void openBookGUI(ItemStack itemStackIn, String page) {
        Minecraft.getInstance().setScreen(new GUIAnimalDictionary(itemStackIn, page));
    }

    @Override
    public Player getClientSidePlayer() {
        return Minecraft.getInstance().player;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Object getArmorModel(int armorId, LivingEntity entity) {
        switch(armorId) {
            default:
                return null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onEntityStatus(Entity entity, byte updateKind) {
        if (updateKind == 67) {
            if (entity instanceof EntityCockroach && entity.isAlive()) {
                SoundLaCucaracha sound;
                if (COCKROACH_SOUND_MAP.get(entity.getId()) == null) {
                    sound = new SoundLaCucaracha((EntityCockroach) entity);
                    COCKROACH_SOUND_MAP.put(entity.getId(), sound);
                } else {
                    sound = (SoundLaCucaracha) COCKROACH_SOUND_MAP.get(entity.getId());
                }
                if (!Minecraft.getInstance().getSoundManager().isActive(sound) && sound.canPlaySound() && sound.isOnlyCockroach()) {
                    Minecraft.getInstance().getSoundManager().play(sound);
                }
            } else if (entity instanceof EntityVoidWorm && entity.isAlive()) {
                float f2 = Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MUSIC);
                if (f2 <= 0.0F) {
                    WORMBOSS_SOUND_MAP.clear();
                } else {
                    SoundWormBoss soundx;
                    if (WORMBOSS_SOUND_MAP.get(entity.getId()) == null) {
                        soundx = new SoundWormBoss((EntityVoidWorm) entity);
                        WORMBOSS_SOUND_MAP.put(entity.getId(), soundx);
                    } else {
                        soundx = (SoundWormBoss) WORMBOSS_SOUND_MAP.get(entity.getId());
                    }
                    if (!Minecraft.getInstance().getSoundManager().isActive(soundx) && soundx.isNearest()) {
                        Minecraft.getInstance().getSoundManager().play(soundx);
                    }
                }
            } else if (entity instanceof EntityGrizzlyBear && entity.isAlive()) {
                SoundBearMusicBox soundxx;
                if (BEAR_MUSIC_BOX_SOUND_MAP.get(entity.getId()) == null) {
                    soundxx = new SoundBearMusicBox((EntityGrizzlyBear) entity);
                    BEAR_MUSIC_BOX_SOUND_MAP.put(entity.getId(), soundxx);
                } else {
                    soundxx = (SoundBearMusicBox) BEAR_MUSIC_BOX_SOUND_MAP.get(entity.getId());
                }
                if (!Minecraft.getInstance().getSoundManager().isActive(soundxx) && soundxx.canPlaySound() && soundxx.isOnlyMusicBox()) {
                    Minecraft.getInstance().getSoundManager().play(soundxx);
                }
            } else if (entity instanceof EntityBlueJay && entity.isAlive()) {
                this.singingBlueJayId = entity.getId();
            }
        }
        if (entity instanceof EntityBlueJay && entity.isAlive() && updateKind == 68) {
            this.singingBlueJayId = -1;
        }
    }

    @Override
    public void updateBiomeVisuals(int x, int z) {
        Minecraft.getInstance().levelRenderer.setBlocksDirty(x - 32, 0, x - 32, z + 32, 255, z + 32);
    }

    public static void setupParticles(RegisterParticleProvidersEvent registry) {
        AlexsMobs.LOGGER.debug("Registered particle factories");
        registry.registerSpriteSet(AMParticleRegistry.GUSTER_SAND_SPIN.get(), ParticleGusterSandSpin.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.GUSTER_SAND_SHOT.get(), ParticleGusterSandShot.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.GUSTER_SAND_SPIN_RED.get(), ParticleGusterSandSpin.FactoryRed::new);
        registry.registerSpriteSet(AMParticleRegistry.GUSTER_SAND_SHOT_RED.get(), ParticleGusterSandShot.FactoryRed::new);
        registry.registerSpriteSet(AMParticleRegistry.GUSTER_SAND_SPIN_SOUL.get(), ParticleGusterSandSpin.FactorySoul::new);
        registry.registerSpriteSet(AMParticleRegistry.GUSTER_SAND_SHOT_SOUL.get(), ParticleGusterSandShot.FactorySoul::new);
        registry.registerSpriteSet(AMParticleRegistry.HEMOLYMPH.get(), ParticleHemolymph.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.PLATYPUS_SENSE.get(), ParticlePlatypus.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.WHALE_SPLASH.get(), ParticleWhaleSplash.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.DNA.get(), ParticleDna.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.SHOCKED.get(), ParticleSimpleHeart.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.WORM_PORTAL.get(), ParticleWormPortal.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.INVERT_DIG.get(), ParticleInvertDig.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.TEETH_GLINT.get(), ParticleTeethGlint.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.SMELLY.get(), ParticleSmelly.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.BUNFUNGUS_TRANSFORMATION.get(), ParticleBunfungusTransformation.Factory::new);
        registry.registerSpriteSet(AMParticleRegistry.FUNGUS_BUBBLE.get(), ParticleFungusBubble.Factory::new);
        registry.registerSpecial(AMParticleRegistry.BEAR_FREDDY.get(), new ParticleBearFreddy.Factory());
        registry.registerSpriteSet(AMParticleRegistry.SUNBIRD_FEATHER.get(), ParticleSunbirdFeather.Factory::new);
        registry.registerSpecial(AMParticleRegistry.STATIC_SPARK.get(), new ParticleStaticSpark.Factory());
        registry.registerSpecial(AMParticleRegistry.SKULK_BOOM.get(), new ParticleSkulkBoom.Factory());
        registry.registerSpriteSet(AMParticleRegistry.BIRD_SONG.get(), ParticleBirdSong.Factory::new);
    }

    @Override
    public void setRenderViewEntity(Entity entity) {
        this.prevPOV = Minecraft.getInstance().options.getCameraType();
        Minecraft.getInstance().setCameraEntity(entity);
        Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
    }

    @Override
    public void resetRenderViewEntity() {
        Minecraft.getInstance().setCameraEntity(Minecraft.getInstance().player);
    }

    @Override
    public int getPreviousPOV() {
        return this.prevPOV.ordinal();
    }

    @Override
    public boolean isFarFromCamera(double x, double y, double z) {
        Minecraft lvt_1_1_ = Minecraft.getInstance();
        return lvt_1_1_.gameRenderer.getMainCamera().getPosition().distanceToSqr(x, y, z) >= 256.0;
    }

    @Override
    public void resetVoidPortalCreation(Player player) {
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRegisterEntityRenders(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    @Override
    public Object getISTERProperties() {
        return new AMItemRenderProperties();
    }

    @Override
    public Object getArmorRenderProperties() {
        return new CustomArmorRenderProperties();
    }

    @Override
    public void spawnSpecialParticle(int type) {
        if (type == 0) {
            Minecraft.getInstance().level.addParticle(AMParticleRegistry.BEAR_FREDDY.get(), Minecraft.getInstance().player.m_20185_(), Minecraft.getInstance().player.m_20186_(), Minecraft.getInstance().player.m_20189_(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void processVisualFlag(Entity entity, int flag) {
        if (entity == Minecraft.getInstance().player && flag == 87) {
            ClientEvents.renderStaticScreenFor = 60;
        }
    }

    @Override
    public void setPupfishChunkForItem(int chunkX, int chunkZ) {
        this.pupfishChunkX = chunkX;
        this.pupfishChunkZ = chunkZ;
    }

    @Override
    public void setDisplayTransmuteResult(int slot, ItemStack stack) {
        this.transmuteStacks[Mth.clamp(slot, 0, 2)] = stack;
    }

    @Override
    public ItemStack getDisplayTransmuteResult(int slot) {
        ItemStack stack = this.transmuteStacks[Mth.clamp(slot, 0, 2)];
        return stack == null ? ItemStack.EMPTY : stack;
    }

    @Override
    public int getSingingBlueJayId() {
        return this.singingBlueJayId;
    }
}