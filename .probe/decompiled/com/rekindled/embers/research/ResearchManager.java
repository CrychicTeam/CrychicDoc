package com.rekindled.embers.research;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.compat.curios.CuriosCompat;
import com.rekindled.embers.item.EmberStorageItem;
import com.rekindled.embers.network.PacketHandler;
import com.rekindled.embers.network.message.MessageResearchData;
import com.rekindled.embers.network.message.MessageResearchTick;
import com.rekindled.embers.research.capability.DefaultResearchCapability;
import com.rekindled.embers.research.capability.IResearchCapability;
import com.rekindled.embers.research.capability.ResearchCapabilityProvider;
import com.rekindled.embers.research.subtypes.ResearchFakePage;
import com.rekindled.embers.research.subtypes.ResearchShowItem;
import com.rekindled.embers.research.subtypes.ResearchSwitchCategory;
import com.rekindled.embers.util.Vec2i;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.PacketDistributor;

public class ResearchManager {

    public static final ResourceLocation PLAYER_RESEARCH = new ResourceLocation("embers", "research");

    public static final ResourceLocation PAGE_ICONS = new ResourceLocation("embers", "textures/gui/codex_pageicons.png");

    public static final double PAGE_ICON_SIZE = 48.0;

    public static List<ResearchCategory> researches = new ArrayList();

    public static ResearchBase dials;

    public static ResearchBase ores;

    public static ResearchBase hammer;

    public static ResearchBase ancient_golem;

    public static ResearchBase gauge;

    public static ResearchBase caminite;

    public static ResearchBase access;

    public static ResearchBase bore;

    public static ResearchBase excavation_buckets;

    public static ResearchBase crystals;

    public static ResearchBase activator;

    public static ResearchBase tinker_lens;

    public static ResearchBase reaction_chamber;

    public static ResearchBase heat_exchanger;

    public static ResearchBase copper_cell;

    public static ResearchBase emitters;

    public static ResearchBase relays;

    public static ResearchBase dawnstone;

    public static ResearchBase melter;

    public static ResearchBase stamper;

    public static ResearchBase mixer;

    public static ResearchBase breaker;

    public static ResearchBase hearth_coil;

    public static ResearchBase char_instiller;

    public static ResearchBase atmospheric_bellows;

    public static ResearchBase heat_insulation;

    public static ResearchBase pressureRefinery;

    public static ResearchBase mini_boiler;

    public static ResearchBase pump;

    public static ResearchBase clockwork_attenuator;

    public static ResearchBase geo_separator;

    public static ResearchBase beam_cannon;

    public static ResearchBase pulser;

    public static ResearchBase splitter;

    public static ResearchBase crystal_cell;

    public static ResearchBase cinder_staff;

    public static ResearchBase clockwork_tools;

    public static ResearchBase blazing_ray;

    public static ResearchBase charger;

    public static ResearchBase jars;

    public static ResearchBase alchemy;

    public static ResearchBase cinder_plinth;

    public static ResearchBase aspecti;

    public static ResearchBase ember_siphon;

    public static ResearchBase tyrfing;

    public static ResearchBase waste;

    public static ResearchBase slate;

    public static ResearchBase mnemonic_inscriber;

    public static ResearchBase entropic_enumerator;

    public static ResearchBase catalytic_plug;

    public static ResearchBase cluster;

    public static ResearchBase ashen_cloak;

    public static ResearchBase inflictor;

    public static ResearchBase materia;

    public static ResearchBase field_chart;

    public static ResearchBase glimmer;

    public static ResearchBase metallurgic_dust;

    public static ResearchBase augments;

    public static ResearchBase inferno_forge;

    public static ResearchBase heat;

    public static ResearchBase dawnstone_anvil;

    public static ResearchBase autohammer;

    public static ResearchBase dismantling;

    public static ResearchBase pipes;

    public static ResearchBase tank;

    public static ResearchBase bin;

    public static ResearchBase dropper;

    public static ResearchBase reservoir;

    public static ResearchBase vacuum;

    public static ResearchBase transfer;

    public static ResearchBase golem_eye;

    public static ResearchBase requisition;

    public static ResearchBase adhesive;

    public static ResearchBase hellish_synthesis;

    public static ResearchBase archaic_brick;

    public static ResearchBase motive_core;

    public static ResearchBase dwarven_oil;

    public static ResearchBase wildfire;

    public static ResearchBase combustor;

    public static ResearchBase catalyzer;

    public static ResearchBase reactor;

    public static ResearchBase injector;

    public static ResearchBase stirling;

    public static ResearchBase ember_pipe;

    public static ResearchBase superheater;

    public static ResearchBase caster_orb;

    public static ResearchBase resonating_bell;

    public static ResearchBase blasting_core;

    public static ResearchBase winding_gears;

    public static ResearchBase cinder_jet;

    public static ResearchBase eldritch_insignia;

    public static ResearchBase intelligent_apparatus;

    public static ResearchBase flame_barrier;

    public static ResearchBase tinker_lens_augment;

    public static ResearchBase anti_tinker_lens;

    public static ResearchBase shifting_scales;

    public static ResearchBase diffraction_barrel;

    public static ResearchBase focal_lens;

    public static ResearchBase cost_reduction;

    public static ResearchBase mantle_bulb;

    public static ResearchBase explosion_charm;

    public static ResearchBase nonbeliever_amulet;

    public static ResearchBase ashen_amulet;

    public static ResearchBase dawnstone_mail;

    public static ResearchBase explosion_pedestal;

    public static ResearchBase gearbox;

    public static ResearchBase mergebox;

    public static ResearchBase axle_iron;

    public static ResearchBase gear_iron;

    public static ResearchBase actuator;

    public static ResearchBase steam_engine;

    public static ResearchCategory categoryWorld;

    public static ResearchCategory categoryMechanisms;

    public static ResearchCategory categoryMetallurgy;

    public static ResearchCategory categoryAlchemy;

    public static ResearchCategory categorySmithing;

    public static ResearchCategory categoryMateria;

    public static ResearchCategory categoryCore;

    public static ResearchCategory subCategoryPipes;

    public static ResearchCategory subCategoryWeaponAugments;

    public static ResearchCategory subCategoryArmorAugments;

    public static ResearchCategory subCategoryProjectileAugments;

    public static ResearchCategory subCategoryMiscAugments;

    public static ResearchCategory subCategoryMechanicalPower;

    public static ResearchCategory subCategoryBaubles;

    public static ResearchCategory subCategorySimpleAlchemy;

    public static ResearchCategory subCategoryWildfire;

    public static boolean isPathToLock(ResearchBase entry) {
        for (ResearchCategory category : researches) {
            for (ResearchBase target : category.prerequisites) {
                if (isPathTowards(entry, target)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPathTowards(ResearchBase entry, ResearchBase target) {
        if (entry.isPathTowards(target)) {
            return true;
        } else {
            for (ResearchBase ancestor : target.getAllRequirements()) {
                if (isPathTowards(entry, ancestor)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static void sendResearchData(ServerPlayer player) {
        IResearchCapability research = getPlayerResearch(player);
        if (research != null) {
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new MessageResearchData(research.getCheckmarks()));
        }
    }

    public static void receiveResearchData(Map<String, Boolean> checkmarks) {
        for (ResearchBase research : getAllResearch()) {
            Boolean checked = (Boolean) checkmarks.get(research.name);
            if (checked != null) {
                research.check(checked);
            }
        }
    }

    public static void sendCheckmark(ResearchBase research, boolean checked) {
        PacketHandler.INSTANCE.sendToServer(new MessageResearchTick(research.name, checked));
    }

    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player && !event.getCapabilities().containsKey(PLAYER_RESEARCH)) {
            event.addCapability(PLAYER_RESEARCH, new ResearchCapabilityProvider(new DefaultResearchCapability()));
        }
    }

    public static void onClone(PlayerEvent.Clone event) {
        IResearchCapability oldCap = getPlayerResearch(event.getOriginal());
        IResearchCapability newCap = getPlayerResearch(event.getEntity());
        if (oldCap != null && newCap != null) {
            CompoundTag compound = new CompoundTag();
            oldCap.writeToNBT(compound);
            newCap.readFromNBT(compound);
        }
    }

    public static IResearchCapability getPlayerResearch(Player player) {
        return (IResearchCapability) player.getCapability(ResearchCapabilityProvider.researchCapability).orElse(null);
    }

    public static List<ResearchBase> getAllResearch() {
        Set<ResearchBase> result = new HashSet();
        for (ResearchCategory category : researches) {
            category.getAllResearch(result);
        }
        return new ArrayList(result);
    }

    public static Map<ResearchBase, Integer> findByTag(String match) {
        HashMap<ResearchBase, Integer> result = new HashMap();
        HashSet<ResearchCategory> categories = new HashSet();
        if (!match.isEmpty()) {
            for (ResearchCategory category : researches) {
                category.findByTag(match, result, categories);
            }
        }
        return result;
    }

    public static void initResearches() {
        categoryWorld = new ResearchCategory("world", 16.0);
        categoryMechanisms = new ResearchCategory("mechanisms", 32.0);
        categoryMetallurgy = new ResearchCategory("metallurgy", 48.0);
        categoryAlchemy = new ResearchCategory("alchemy", 64.0);
        categorySmithing = new ResearchCategory("smithing", 80.0);
        categoryMateria = new ResearchCategoryComingSoon("materia", 224.0, 0.0);
        categoryCore = new ResearchCategoryComingSoon("core", 224.0, 16.0);
        Vec2i[] ringPositions = new Vec2i[] { new Vec2i(1, 1), new Vec2i(0, 3), new Vec2i(0, 5), new Vec2i(1, 7), new Vec2i(11, 7), new Vec2i(12, 5), new Vec2i(12, 3), new Vec2i(11, 1), new Vec2i(4, 1), new Vec2i(2, 4), new Vec2i(4, 7), new Vec2i(8, 7), new Vec2i(10, 4), new Vec2i(8, 1) };
        subCategoryWeaponAugments = new ResearchCategory("weapon_augments", 0.0).pushGoodLocations(ringPositions);
        subCategoryArmorAugments = new ResearchCategory("armor_augments", 0.0).pushGoodLocations(ringPositions);
        subCategoryProjectileAugments = new ResearchCategory("projectile_augments", 0.0).pushGoodLocations(ringPositions);
        subCategoryMiscAugments = new ResearchCategory("misc_augments", 0.0).pushGoodLocations(ringPositions);
        subCategoryPipes = new ResearchCategory("pipes", 0.0);
        subCategoryMechanicalPower = new ResearchCategory("mystical_mechanics", 0.0);
        subCategoryBaubles = new ResearchCategory("baubles", 0.0);
        subCategorySimpleAlchemy = new ResearchCategory("simple_alchemy", 0.0);
        subCategoryWildfire = new ResearchCategory("wildfire", 0.0);
        ores = new ResearchBase("ores", new ItemStack(RegistryManager.RAW_LEAD.get()), 0.0, 7.0);
        hammer = new ResearchBase("hammer", new ItemStack(RegistryManager.TINKER_HAMMER.get()), 0.0, 3.0).addAncestor(ores);
        ancient_golem = new ResearchBase("ancient_golem", ItemStack.EMPTY, 0.0, 0.0).setIconBackground(PAGE_ICONS, 48.0, 0.0);
        gauge = new ResearchBase("gauge", new ItemStack(RegistryManager.ATMOSPHERIC_GAUGE_ITEM.get()), 4.0, 3.0).addAncestor(ores);
        caminite = new ResearchBase("caminite", new ItemStack(RegistryManager.CAMINITE_BRICK.get()), 6.0, 7.0);
        access = new ResearchBase("access", new ItemStack(RegistryManager.MECHANICAL_CORE_ITEM.get()), 7.0, 2.0).addAncestor(caminite);
        bore = new ResearchBase("bore", new ItemStack(RegistryManager.EMBER_BORE_ITEM.get()), 9.0, 0.0).addAncestor(hammer).addAncestor(access);
        excavation_buckets = new ResearchBase("excavation_buckets", new ItemStack(RegistryManager.EXCAVATION_BUCKETS_ITEM.get()), 6.0, 0.0).addAncestor(bore);
        crystals = new ResearchBase("crystals", new ItemStack(RegistryManager.EMBER_CRYSTAL.get()), 12.0, 3.0).addAncestor(bore);
        activator = new ResearchBase("activator", new ItemStack(RegistryManager.EMBER_ACTIVATOR_ITEM.get()), 10.0, 6.0).addAncestor(crystals);
        dials = new ResearchBase("dials", new ItemStack(RegistryManager.EMBER_DIAL_ITEM.get()), 5.0, 5.0).addAncestor(hammer);
        tinker_lens = new ResearchBase("tinker_lens", new ItemStack(RegistryManager.TINKER_LENS.get()), 4.0, 7.0).addAncestor(hammer);
        heat_exchanger = new ResearchBase("heat_exchanger", new ItemStack(RegistryManager.HEAT_EXCHANGER_ITEM.get()), 12.0, 7.0).addAncestor(activator);
        pipes = new ResearchBase("pipes", new ItemStack(RegistryManager.FLUID_EXTRACTOR_ITEM.get()), 2.0, 4.0);
        pipes.addPage(new ResearchShowItem("routing", ItemStack.EMPTY, 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.ITEM_PIPE_ITEM.get()), new ItemStack(RegistryManager.FLUID_PIPE_ITEM.get()))));
        pipes.addPage(new ResearchShowItem("valves", ItemStack.EMPTY, 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.ITEM_EXTRACTOR_ITEM.get()), new ItemStack(RegistryManager.FLUID_EXTRACTOR_ITEM.get()))));
        pipes.addPage(new ResearchShowItem("pipe_tools", ItemStack.EMPTY, 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.TINKER_HAMMER.get()), new ItemStack(Items.STICK))));
        transfer = new ResearchBase("transfer", new ItemStack(RegistryManager.ITEM_TRANSFER_ITEM.get()), 5.0, 5.0).addAncestor(pipes);
        transfer.addPage(new ResearchShowItem("fluid_transfer", ItemStack.EMPTY, 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.FLUID_TRANSFER_ITEM.get()))));
        vacuum = new ResearchBase("vacuum", new ItemStack(RegistryManager.ITEM_VACUUM_ITEM.get()), 8.0, 4.0).addPage(new ResearchBase("vacuum_transfer", ItemStack.EMPTY, 0.0, 0.0)).addAncestor(pipes);
        dropper = new ResearchBase("dropper", new ItemStack(RegistryManager.ITEM_DROPPER_ITEM.get()), 8.0, 6.0).addAncestor(pipes);
        bin = new ResearchBase("bin", new ItemStack(RegistryManager.BIN_ITEM.get()), 4.0, 3.0).addAncestor(pipes);
        tank = new ResearchBase("tank", new ItemStack(RegistryManager.FLUID_VESSEL_ITEM.get()), 3.0, 1.0).addAncestor(pipes);
        reservoir = new ResearchBase("reservoir", new ItemStack(RegistryManager.RESERVOIR_ITEM.get()), 6.0, 0.0).addAncestor(tank).addPage(new ResearchShowItem("reservoir_valve", new ItemStack(RegistryManager.CAMINITE_VALVE_ITEM.get()), 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.CAMINITE_VALVE_ITEM.get()))));
        emitters = new ResearchShowItem("emitters", new ItemStack(RegistryManager.EMBER_EMITTER_ITEM.get()), 0.0, 2.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.EMBER_EMITTER_ITEM.get()))).addPage(new ResearchShowItem("receivers", new ItemStack(RegistryManager.EMBER_RECEIVER_ITEM.get()), 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.EMBER_RECEIVER_ITEM.get())))).addPage(new ResearchShowItem("linking", ItemStack.EMPTY, 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.EMBER_EMITTER_ITEM.get()), new ItemStack(RegistryManager.TINKER_HAMMER.get()), new ItemStack(RegistryManager.EMBER_RECEIVER_ITEM.get()))));
        relays = new ResearchShowItem("relays", new ItemStack(RegistryManager.EMBER_RELAY_ITEM.get()), 2.0, 7.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.EMBER_RELAY_ITEM.get()))).addAncestor(emitters).addPage(new ResearchShowItem("mirror_relay", new ItemStack(RegistryManager.MIRROR_RELAY_ITEM.get()), 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.MIRROR_RELAY_ITEM.get()))));
        melter = new ResearchBase("melter", new ItemStack(RegistryManager.MELTER_ITEM.get()), 2.0, 0.0).addAncestor(emitters);
        geo_separator = new ResearchBase("geo_separator", new ItemStack(RegistryManager.GEOLOGIC_SEPARATOR_ITEM.get()), 0.0, 0.0).addAncestor(melter);
        stamper = new ResearchBase("stamper", new ItemStack(RegistryManager.STAMPER_ITEM.get()), 3.0, 4.0).addAncestor(melter).addAncestor(emitters);
        mixer = new ResearchBase("mixer", new ItemStack(RegistryManager.MIXER_CENTRIFUGE_ITEM.get()), 5.0, 2.0).addAncestor(stamper).addAncestor(melter);
        dawnstone = new ResearchBase("dawnstone", new ItemStack(RegistryManager.DAWNSTONE_INGOT.get()), 11.0, 4.0).addAncestor(mixer);
        pressureRefinery = new ResearchBase("pressure_refinery", new ItemStack(RegistryManager.PRESSURE_REFINERY_ITEM.get()), 10.0, 0.0).addAncestor(dawnstone);
        pump = new ResearchBase("pump", new ItemStack(RegistryManager.MECHANICAL_PUMP_ITEM.get()), 7.0, 0.0).addAncestor(pressureRefinery);
        mini_boiler = new ResearchBase("mini_boiler", new ItemStack(RegistryManager.MINI_BOILER_ITEM.get()), 8.0, 5.0).addAncestor(pump);
        copper_cell = new ResearchBase("copper_cell", new ItemStack(RegistryManager.COPPER_CELL_ITEM.get()), 0.0, 5.0).addAncestor(emitters);
        hearth_coil = new ResearchBase("hearth_coil", new ItemStack(RegistryManager.HEARTH_COIL_ITEM.get()), 6.0, 6.0).addAncestor(copper_cell);
        char_instiller = new ResearchBase("char_instiller", new ItemStack(RegistryManager.CHAR_INSTILLER_ITEM.get()), 8.0, 7.0).addAncestor(hearth_coil);
        atmospheric_bellows = new ResearchBase("atmospheric_bellows", new ItemStack(RegistryManager.ATMOSPHERIC_BELLOWS_ITEM.get()), 10.0, 7.0).addAncestor(hearth_coil);
        heat_insulation = new ResearchBase("heat_insulation", new ItemStack(RegistryManager.HEAT_INSULATION_ITEM.get()), 4.0, 7.0).addAncestor(hearth_coil);
        clockwork_attenuator = new ResearchBase("clockwork_attenuator", new ItemStack(RegistryManager.CLOCKWORK_ATTENUATOR.get()), 12.0, 7.0);
        crystal_cell = new ResearchBase("crystal_cell", new ItemStack(RegistryManager.CRYSTAL_CELL_ITEM.get()), 0.0, 1.0);
        pulser = new ResearchShowItem("pulser", new ItemStack(RegistryManager.EMBER_EJECTOR_ITEM.get()), 0.0, 3.5).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.EMBER_EJECTOR_ITEM.get()))).addAncestor(crystal_cell).addPage(new ResearchShowItem("ember_funnel", new ItemStack(RegistryManager.EMBER_FUNNEL_ITEM.get()), 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.EMBER_FUNNEL_ITEM.get()))));
        charger = new ResearchBase("charger", new ItemStack(RegistryManager.COPPER_CHARGER_ITEM.get()), 4.0, 0.0);
        ember_siphon = new ResearchBase("ember_siphon", new ItemStack(RegistryManager.EMBER_SIPHON_ITEM.get()), 2.0, 0.0).addAncestor(charger);
        ItemStack fullJar = EmberStorageItem.withFill(RegistryManager.EMBER_JAR.get(), ((EmberStorageItem) RegistryManager.EMBER_JAR.get()).getCapacity());
        jars = new ResearchBase("jars", fullJar, 7.0, 1.0).addAncestor(charger);
        clockwork_tools = new ResearchBase("clockwork_tools", new ItemStack(RegistryManager.CLOCKWORK_AXE.get()), 2.0, 2.0).addAncestor(jars).addPage(new ResearchShowItem("clockwork_pickaxe", ItemStack.EMPTY, 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.CLOCKWORK_PICKAXE.get())))).addPage(new ResearchShowItem("clockwork_hammer", ItemStack.EMPTY, 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.GRANDHAMMER.get())))).addPage(new ResearchShowItem("clockwork_axe", ItemStack.EMPTY, 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.CLOCKWORK_AXE.get()))));
        splitter = new ResearchBase("splitter", new ItemStack(RegistryManager.BEAM_SPLITTER_ITEM.get()), 0.0, 6.0).addAncestor(pulser);
        cinder_staff = new ResearchBase("cinder_staff", new ItemStack(RegistryManager.CINDER_STAFF.get()), 4.0, 4.0).addAncestor(jars);
        blazing_ray = new ResearchBase("blazing_ray", new ItemStack(RegistryManager.BLAZING_RAY.get()), 6.0, 5.0).addAncestor(jars);
        aspecti = new ResearchBase("aspecti", new ItemStack(RegistryManager.DAWNSTONE_ASPECTUS.get()), 12.0, 1.0);
        cinder_plinth = new ResearchBase("cinder_plinth", new ItemStack(RegistryManager.CINDER_PLINTH_ITEM.get()), 9.0, 0.0);
        beam_cannon = new ResearchBase("beam_cannon", new ItemStack(RegistryManager.BEAM_CANNON_ITEM.get()), 9.0, 7.0);
        alchemy = new ResearchBase("alchemy", new ItemStack(RegistryManager.ALCHEMY_TABLET_ITEM.get()), 9.0, 4.0).addAncestor(aspecti).addAncestor(beam_cannon);
        waste = new ResearchBase("waste", new ItemStack(RegistryManager.ALCHEMICAL_WASTE.get()), 6.0, 0.0).addPage(new ResearchBase("waste_page_2", new ItemStack(RegistryManager.ALCHEMICAL_WASTE.get()), 0.0, 0.0));
        slate = new ResearchBase("slate", new ItemStack(RegistryManager.CODEBREAKING_SLATE.get()), 6.0, 2.0).addAncestor(waste).addPage(new ResearchBase("slate_alchemy_recap", ItemStack.EMPTY, 0.0, 0.0));
        mnemonic_inscriber = new ResearchBase("mnemonic_inscriber", new ItemStack(RegistryManager.MNEMONIC_INSCRIBER_ITEM.get()), 4.0, 1.0).addAncestor(slate);
        entropic_enumerator = new ResearchBase("entropic_enumerator", new ItemStack(RegistryManager.ENTROPIC_ENUMERATOR_ITEM.get()), 1.0, 0.0).addAncestor(slate);
        catalytic_plug = new ResearchBase("catalytic_plug", new ItemStack(RegistryManager.CATALYTIC_PLUG_ITEM.get()), 12.0, 5.0).addAncestor(slate);
        materia = new ResearchBase("materia", new ItemStack(RegistryManager.ISOLATED_MATERIA.get()), 6.0, 5.0).addAncestor(slate);
        cluster = new ResearchBase("cluster", new ItemStack(RegistryManager.EMBER_CRYSTAL_CLUSTER.get()), 3.0, 4.0).addAncestor(slate);
        ashen_cloak = new ResearchShowItem("ashen_cloak", new ItemStack(RegistryManager.ASHEN_CLOAK.get()), 9.0, 4.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.ASHEN_GOGGLES.get()), new ItemStack(RegistryManager.ASHEN_CLOAK.get()), new ItemStack(RegistryManager.ASHEN_LEGGINGS.get()), new ItemStack(RegistryManager.ASHEN_BOOTS.get()))).addAncestor(slate);
        field_chart = new ResearchBase("field_chart", new ItemStack(RegistryManager.FIELD_CHART_ITEM.get()), 0.0, 5.0).addAncestor(cluster);
        inflictor = new ResearchBase("inflictor", new ItemStack(RegistryManager.INFLICTOR_GEM.get()), 11.0, 7.0).addAncestor(ashen_cloak);
        tyrfing = new ResearchBase("tyrfing", new ItemStack(RegistryManager.TYRFING.get()), 8.0, 6.0).addAncestor(slate);
        glimmer = new ResearchBase("glimmer", new ItemStack(RegistryManager.GLIMMER_CRYSTAL.get()), 9.0, 0.0).addAncestor(slate);
        adhesive = new ResearchBase("adhesive", new ItemStack(RegistryManager.ADHESIVE.get()), 10.0, 1.0);
        hellish_synthesis = new ResearchBase("hellish_synthesis", new ItemStack(Items.NETHERRACK), 2.0, 1.0);
        archaic_brick = new ResearchBase("archaic_brick", new ItemStack(RegistryManager.ARCHAIC_BRICK.get()), 5.0, 2.0).addAncestor(hellish_synthesis);
        motive_core = new ResearchBase("motive_core", new ItemStack(RegistryManager.ANCIENT_MOTIVE_CORE.get()), 4.0, 4.0).addAncestor(archaic_brick);
        dwarven_oil = new ResearchBase("dwarven_oil", new ItemStack(RegistryManager.DWARVEN_OIL.FLUID_BUCKET.get()), 1.0, 4.0).addAncestor(hellish_synthesis);
        wildfire = new ResearchBase("wildfire", new ItemStack(RegistryManager.WILDFIRE_CORE.get()), 1.0, 5.0);
        injector = new ResearchBase("injector", new ItemStack(RegistryManager.EMBER_INJECTOR_ITEM.get()), 0.0, 7.0).addAncestor(wildfire).addPage(new ResearchShowItem("crystal_level", ItemStack.EMPTY, 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.IRON_CRYSTAL_SEED.ITEM.get()), new ItemStack(RegistryManager.GOLD_CRYSTAL_SEED.ITEM.get()), new ItemStack(RegistryManager.COPPER_CRYSTAL_SEED.ITEM.get()), new ItemStack(RegistryManager.TIN_CRYSTAL_SEED.ITEM.get()))).addItem(new ResearchShowItem.DisplayItem(new ItemStack(RegistryManager.SILVER_CRYSTAL_SEED.ITEM.get()), new ItemStack(RegistryManager.LEAD_CRYSTAL_SEED.ITEM.get()), new ItemStack(RegistryManager.NICKEL_CRYSTAL_SEED.ITEM.get()), new ItemStack(RegistryManager.ALUMINUM_CRYSTAL_SEED.ITEM.get()))));
        combustor = new ResearchBase("combustor", new ItemStack(RegistryManager.COMBUSTION_CHAMBER_ITEM.get()), 6.0, 5.0).addAncestor(wildfire);
        combustor.addPage(new ResearchShowItem("empty", ItemStack.EMPTY, 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem("combustor_coal", new ItemStack(Items.COAL))).addItem(new ResearchShowItem.DisplayItem("combustor_nether_brick", new ItemStack(Items.NETHER_BRICK))).addItem(new ResearchShowItem.DisplayItem("combustor_blaze_powder", new ItemStack(Items.BLAZE_POWDER))));
        catalyzer = new ResearchBase("catalyzer", new ItemStack(RegistryManager.CATALYSIS_CHAMBER_ITEM.get()), 5.0, 7.0).addAncestor(wildfire);
        catalyzer.addPage(new ResearchShowItem("empty", ItemStack.EMPTY, 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem("catalyzer_grit", new ItemStack(RegistryManager.EMBER_GRIT.get()))).addItem(new ResearchShowItem.DisplayItem("catalyzer_gunpowder", new ItemStack(Items.GUNPOWDER))).addItem(new ResearchShowItem.DisplayItem("catalyzer_glowstone", new ItemStack(Items.GLOWSTONE_DUST))));
        reactor = new ResearchBase("reactor", new ItemStack(RegistryManager.IGNEM_REACTOR_ITEM.get()), 9.0, 7.0).addAncestor(combustor).addAncestor(catalyzer);
        stirling = new ResearchBase("stirling", new ItemStack(RegistryManager.WILDFIRE_STIRLING_ITEM.get()), 0.0, 2.0).addAncestor(wildfire);
        dawnstone_anvil = new ResearchBase("dawnstone_anvil", new ItemStack(RegistryManager.DAWNSTONE_ANVIL_ITEM.get()), 12.0, 7.0);
        autohammer = new ResearchBase("autohammer", new ItemStack(RegistryManager.AUTOMATIC_HAMMER_ITEM.get()), 9.0, 5.0).addAncestor(dawnstone_anvil);
        heat = new ResearchBase("heat", new ItemStack(RegistryManager.EMBER_CRYSTAL.get()), 7.0, 7.0).addAncestor(dawnstone_anvil);
        augments = new ResearchBase("augments", new ItemStack(RegistryManager.ANCIENT_MOTIVE_CORE.get()), 5.0, 7.0).addAncestor(heat);
        dismantling = new ResearchBase("dismantling", ItemStack.EMPTY, 3.0, 5.0).setIconBackground(PAGE_ICONS, 96.0, 0.0).addAncestor(augments);
        inferno_forge = new ResearchBase("inferno_forge", new ItemStack(RegistryManager.INFERNO_FORGE_ITEM.get()), 6.0, 4.0).addAncestor(heat);
        superheater = new ResearchBase("superheater", new ItemStack(RegistryManager.SUPERHEATER.get()), subCategoryWeaponAugments.popGoodLocation());
        blasting_core = new ResearchBase("blasting_core", new ItemStack(RegistryManager.BLASTING_CORE.get()), subCategoryWeaponAugments.popGoodLocation());
        caster_orb = new ResearchBase("caster_orb", new ItemStack(RegistryManager.CASTER_ORB.get()), subCategoryWeaponAugments.popGoodLocation()).addPage(new ResearchBase("caster_orb_addendum", ItemStack.EMPTY, 0.0, 0.0));
        resonating_bell = new ResearchBase("resonating_bell", new ItemStack(RegistryManager.RESONATING_BELL.get()), subCategoryWeaponAugments.popGoodLocation());
        winding_gears = new ResearchBase("winding_gears", new ItemStack(RegistryManager.WINDING_GEARS.get()), subCategoryWeaponAugments.popGoodLocation()).addPage(new ResearchShowItem("winding_gears_boots", ItemStack.EMPTY, 0.0, 0.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(Items.IRON_BOOTS))));
        eldritch_insignia = new ResearchBase("eldritch_insignia", new ItemStack(RegistryManager.ELDRITCH_INSIGNIA.get()), subCategoryArmorAugments.popGoodLocation());
        intelligent_apparatus = new ResearchBase("intelligent_apparatus", new ItemStack(RegistryManager.INTELLIGENT_APPARATUS.get()), subCategoryArmorAugments.popGoodLocation());
        flame_barrier = new ResearchBase("flame_barrier", new ItemStack(RegistryManager.FLAME_BARRIER.get()), subCategoryArmorAugments.popGoodLocation());
        cinder_jet = new ResearchBase("cinder_jet", new ItemStack(RegistryManager.CINDER_JET.get()), subCategoryArmorAugments.popGoodLocation());
        tinker_lens_augment = new ResearchBase("tinker_lens_augment", new ItemStack(RegistryManager.TINKER_LENS.get()), subCategoryArmorAugments.popGoodLocation());
        anti_tinker_lens = new ResearchBase("anti_tinker_lens", new ItemStack(RegistryManager.SMOKY_TINKER_LENS.get()), subCategoryArmorAugments.popGoodLocation()).addAncestor(tinker_lens_augment);
        shifting_scales = new ResearchBase("shifting_scales", new ItemStack(RegistryManager.SHIFTING_SCALES.get()), subCategoryArmorAugments.popGoodLocation());
        diffraction_barrel = new ResearchBase("diffraction_barrel", new ItemStack(RegistryManager.DIFFRACTION_BARREL.get()), subCategoryProjectileAugments.popGoodLocation());
        focal_lens = new ResearchBase("focal_lens", new ItemStack(RegistryManager.FOCAL_LENS.get()), subCategoryProjectileAugments.popGoodLocation());
        ResearchBase infernoForgeWeapon = new ResearchFakePage(inferno_forge, 6.0, 4.0);
        subCategoryWeaponAugments.addResearch(infernoForgeWeapon);
        subCategoryWeaponAugments.addResearch(superheater.addAncestor(infernoForgeWeapon));
        subCategoryWeaponAugments.addResearch(blasting_core.addAncestor(infernoForgeWeapon));
        subCategoryWeaponAugments.addResearch(caster_orb.addAncestor(infernoForgeWeapon));
        subCategoryWeaponAugments.addResearch(resonating_bell.addAncestor(infernoForgeWeapon));
        subCategoryWeaponAugments.addResearch(winding_gears.addAncestor(infernoForgeWeapon));
        ResearchBase infernoForgeArmor = new ResearchFakePage(inferno_forge, 6.0, 4.0);
        subCategoryArmorAugments.addResearch(infernoForgeArmor);
        subCategoryArmorAugments.addResearch(cinder_jet.addAncestor(infernoForgeArmor));
        subCategoryArmorAugments.addResearch(eldritch_insignia.addAncestor(infernoForgeArmor));
        subCategoryArmorAugments.addResearch(intelligent_apparatus.addAncestor(infernoForgeArmor));
        subCategoryArmorAugments.addResearch(flame_barrier.addAncestor(infernoForgeArmor));
        subCategoryArmorAugments.addResearch(new ResearchFakePage(blasting_core, subCategoryArmorAugments.popGoodLocation()).addAncestor(infernoForgeArmor));
        subCategoryArmorAugments.addResearch(tinker_lens_augment.addAncestor(infernoForgeArmor));
        subCategoryArmorAugments.addResearch(anti_tinker_lens.addAncestor(infernoForgeArmor));
        subCategoryArmorAugments.addResearch(shifting_scales.addAncestor(infernoForgeArmor));
        subCategoryArmorAugments.addResearch(new ResearchFakePage(winding_gears, subCategoryArmorAugments.popGoodLocation()).addAncestor(infernoForgeArmor));
        ResearchBase infernoForgeProjectile = new ResearchFakePage(inferno_forge, 6.0, 4.0);
        subCategoryProjectileAugments.addResearch(infernoForgeProjectile);
        subCategoryProjectileAugments.addResearch(diffraction_barrel.addAncestor(infernoForgeProjectile));
        subCategoryProjectileAugments.addResearch(focal_lens.addAncestor(infernoForgeProjectile));
        ResearchBase infernoForgeMisc = new ResearchFakePage(inferno_forge, 6.0, 4.0);
        subCategoryMiscAugments.addResearch(infernoForgeMisc);
        subCategoryPipes.addResearch(pipes);
        subCategoryPipes.addResearch(bin);
        subCategoryPipes.addResearch(tank);
        subCategoryPipes.addResearch(reservoir);
        subCategoryPipes.addResearch(transfer);
        subCategoryPipes.addResearch(vacuum);
        subCategoryPipes.addResearch(dropper);
        subCategorySimpleAlchemy.addResearch(hellish_synthesis);
        subCategorySimpleAlchemy.addResearch(archaic_brick);
        subCategorySimpleAlchemy.addResearch(motive_core);
        subCategorySimpleAlchemy.addResearch(adhesive);
        subCategorySimpleAlchemy.addResearch(dwarven_oil);
        subCategoryWildfire.addResearch(wildfire);
        subCategoryWildfire.addResearch(injector);
        subCategoryWildfire.addResearch(combustor);
        subCategoryWildfire.addResearch(catalyzer);
        subCategoryWildfire.addResearch(reactor);
        subCategoryWildfire.addResearch(stirling);
        if (ModList.get().isLoaded("curios")) {
            ResearchBase baublesSwitch = makeCategorySwitch(subCategoryBaubles, 5, 7, ItemStack.EMPTY, 5, 1);
            CuriosCompat.initCuriosCategory();
            baublesSwitch.addAncestor(cluster);
            categoryAlchemy.addResearch(baublesSwitch);
        }
        ResearchBase pipeSwitch = makeCategorySwitch(subCategoryPipes, 3, 0, new ItemStack(RegistryManager.FLUID_PIPE_ITEM.get()), 0, 1).addAncestor(hammer);
        ResearchBase weaponAugmentSwitch = makeCategorySwitch(subCategoryWeaponAugments, 2, 1, ItemStack.EMPTY, 1, 1).setMinEntries(2).addAncestor(inferno_forge);
        ResearchBase armorAugmentSwitch = makeCategorySwitch(subCategoryArmorAugments, 1, 3, ItemStack.EMPTY, 2, 1).setMinEntries(2).addAncestor(inferno_forge);
        ResearchBase projectileAugmentSwitch = makeCategorySwitch(subCategoryProjectileAugments, 11, 3, ItemStack.EMPTY, 3, 1).setMinEntries(2).addAncestor(inferno_forge);
        ResearchBase miscAugmentSwitch = makeCategorySwitch(subCategoryMiscAugments, 10, 1, ItemStack.EMPTY, 0, 1).setMinEntries(2).addAncestor(inferno_forge);
        ResearchBase wildfireSwitch = makeCategorySwitch(subCategoryWildfire, 1, 7, new ItemStack(RegistryManager.WILDFIRE_CORE.get()), 0, 1).addAncestor(cluster);
        ResearchBase simpleAlchemySwitch = makeCategorySwitch(subCategorySimpleAlchemy, 12, 1, new ItemStack(Items.SOUL_SAND), 0, 1).addAncestor(slate);
        pipes.subCategory = pipeSwitch;
        infernoForgeWeapon.subCategory = weaponAugmentSwitch;
        infernoForgeArmor.subCategory = armorAugmentSwitch;
        infernoForgeProjectile.subCategory = projectileAugmentSwitch;
        infernoForgeMisc.subCategory = miscAugmentSwitch;
        wildfire.subCategory = wildfireSwitch;
        adhesive.subCategory = simpleAlchemySwitch;
        hellish_synthesis.subCategory = simpleAlchemySwitch;
        categoryWorld.addResearch(ores).addResearch(hammer).addResearch(ancient_golem).addResearch(gauge).addResearch(tinker_lens).addResearch(caminite).addResearch(access).addResearch(bore).addResearch(excavation_buckets).addResearch(pipeSwitch).addResearch(crystals).addResearch(activator).addResearch(dials).addResearch(heat_exchanger);
        categoryMechanisms.addResearch(melter).addResearch(stamper).addResearch(hearth_coil).addResearch(char_instiller).addResearch(atmospheric_bellows).addResearch(heat_insulation).addResearch(mixer).addResearch(pump).addResearch(pressureRefinery).addResearch(mini_boiler).addResearch(dawnstone).addResearch(emitters).addResearch(relays).addResearch(copper_cell).addResearch(clockwork_attenuator).addResearch(geo_separator);
        categoryMetallurgy.addResearch(splitter).addResearch(pulser).addResearch(crystal_cell).addResearch(charger).addResearch(ember_siphon).addResearch(jars).addResearch(clockwork_tools).addResearch(cinder_staff).addResearch(blazing_ray).addResearch(cinder_plinth).addResearch(aspecti).addResearch(alchemy).addResearch(beam_cannon);
        categoryAlchemy.addResearch(waste).addResearch(slate).addResearch(mnemonic_inscriber).addResearch(entropic_enumerator).addResearch(simpleAlchemySwitch).addResearch(catalytic_plug).addResearch(cluster).addResearch(ashen_cloak).addResearch(inflictor).addResearch(field_chart).addResearch(materia).addResearch(tyrfing).addResearch(glimmer).addResearch(wildfireSwitch);
        categorySmithing.addResearch(dawnstone_anvil).addResearch(autohammer).addResearch(heat).addResearch(augments).addResearch(dismantling).addResearch(inferno_forge).addResearch(weaponAugmentSwitch).addResearch(armorAugmentSwitch).addResearch(projectileAugmentSwitch).addResearch(miscAugmentSwitch);
        categoryMechanisms.addPrerequisite(activator);
        categoryMetallurgy.addPrerequisite(dawnstone);
        categoryAlchemy.addPrerequisite(alchemy);
        categorySmithing.addPrerequisite(wildfire);
        researches.add(categoryWorld);
        researches.add(categoryMechanisms);
        researches.add(categoryMetallurgy);
        researches.add(categoryAlchemy);
        researches.add(categorySmithing);
    }

    private static ResearchSwitchCategory makeCategorySwitch(ResearchCategory targetCategory, int x, int y, ItemStack icon, int u, int v) {
        return (ResearchSwitchCategory) new ResearchSwitchCategory(targetCategory.name + "_category", icon, (double) x, (double) y).setTargetCategory(targetCategory).setIconBackground(PAGE_ICONS, 48.0 * (double) u, 48.0 * (double) v);
    }
}