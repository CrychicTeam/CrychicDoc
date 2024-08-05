package com.simibubi.create;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class AllPartialModels {

    public static final PartialModel SCHEMATICANNON_CONNECTOR = block("schematicannon/connector");

    public static final PartialModel SCHEMATICANNON_PIPE = block("schematicannon/pipe");

    public static final PartialModel SHAFTLESS_COGWHEEL = block("cogwheel_shaftless");

    public static final PartialModel SHAFTLESS_LARGE_COGWHEEL = block("large_cogwheel_shaftless");

    public static final PartialModel COGWHEEL_SHAFT = block("cogwheel_shaft");

    public static final PartialModel SHAFT_HALF = block("shaft_half");

    public static final PartialModel BELT_PULLEY = block("belt_pulley");

    public static final PartialModel BELT_START = block("belt/start");

    public static final PartialModel BELT_MIDDLE = block("belt/middle");

    public static final PartialModel BELT_END = block("belt/end");

    public static final PartialModel BELT_START_BOTTOM = block("belt/start_bottom");

    public static final PartialModel BELT_MIDDLE_BOTTOM = block("belt/middle_bottom");

    public static final PartialModel BELT_END_BOTTOM = block("belt/end_bottom");

    public static final PartialModel BELT_DIAGONAL_START = block("belt/diagonal_start");

    public static final PartialModel BELT_DIAGONAL_MIDDLE = block("belt/diagonal_middle");

    public static final PartialModel BELT_DIAGONAL_END = block("belt/diagonal_end");

    public static final PartialModel ANDESITE_BELT_COVER_X = block("belt_cover/andesite_belt_cover_x");

    public static final PartialModel BRASS_BELT_COVER_X = block("belt_cover/brass_belt_cover_x");

    public static final PartialModel ANDESITE_BELT_COVER_Z = block("belt_cover/andesite_belt_cover_z");

    public static final PartialModel BRASS_BELT_COVER_Z = block("belt_cover/brass_belt_cover_z");

    public static final PartialModel ENCASED_FAN_INNER = block("encased_fan/propeller");

    public static final PartialModel HAND_CRANK_HANDLE = block("hand_crank/handle");

    public static final PartialModel MECHANICAL_PRESS_HEAD = block("mechanical_press/head");

    public static final PartialModel MECHANICAL_MIXER_POLE = block("mechanical_mixer/pole");

    public static final PartialModel MECHANICAL_MIXER_HEAD = block("mechanical_mixer/head");

    public static final PartialModel MECHANICAL_CRAFTER_LID = block("mechanical_crafter/lid");

    public static final PartialModel MECHANICAL_CRAFTER_ARROW = block("mechanical_crafter/arrow");

    public static final PartialModel MECHANICAL_CRAFTER_BELT_FRAME = block("mechanical_crafter/belt");

    public static final PartialModel MECHANICAL_CRAFTER_BELT = block("mechanical_crafter/belt_animated");

    public static final PartialModel SAW_BLADE_HORIZONTAL_ACTIVE = block("mechanical_saw/blade_horizontal_active");

    public static final PartialModel SAW_BLADE_HORIZONTAL_INACTIVE = block("mechanical_saw/blade_horizontal_inactive");

    public static final PartialModel SAW_BLADE_HORIZONTAL_REVERSED = block("mechanical_saw/blade_horizontal_reversed");

    public static final PartialModel SAW_BLADE_VERTICAL_ACTIVE = block("mechanical_saw/blade_vertical_active");

    public static final PartialModel SAW_BLADE_VERTICAL_INACTIVE = block("mechanical_saw/blade_vertical_inactive");

    public static final PartialModel SAW_BLADE_VERTICAL_REVERSED = block("mechanical_saw/blade_vertical_reversed");

    public static final PartialModel GAUGE_DIAL = block("gauge/dial");

    public static final PartialModel GAUGE_INDICATOR = block("gauge/indicator");

    public static final PartialModel GAUGE_HEAD_SPEED = block("gauge/speedometer/head");

    public static final PartialModel GAUGE_HEAD_STRESS = block("gauge/stressometer/head");

    public static final PartialModel BEARING_TOP = block("bearing/top");

    public static final PartialModel BEARING_TOP_WOODEN = block("bearing/top_wooden");

    public static final PartialModel DRILL_HEAD = block("mechanical_drill/head");

    public static final PartialModel HARVESTER_BLADE = block("mechanical_harvester/blade");

    public static final PartialModel DEPLOYER_POLE = block("deployer/pole");

    public static final PartialModel DEPLOYER_HAND_POINTING = block("deployer/hand_pointing");

    public static final PartialModel DEPLOYER_HAND_PUNCHING = block("deployer/hand_punching");

    public static final PartialModel DEPLOYER_HAND_HOLDING = block("deployer/hand_holding");

    public static final PartialModel ANALOG_LEVER_HANDLE = block("analog_lever/handle");

    public static final PartialModel ANALOG_LEVER_INDICATOR = block("analog_lever/indicator");

    public static final PartialModel FUNNEL_FLAP = block("funnel/flap");

    public static final PartialModel BELT_FUNNEL_FLAP = block("belt_funnel/flap");

    public static final PartialModel BELT_TUNNEL_FLAP = block("belt_tunnel/flap");

    public static final PartialModel FLEXPEATER_INDICATOR = block("diodes/indicator");

    public static final PartialModel ROLLER_WHEEL = block("mechanical_roller/wheel");

    public static final PartialModel ROLLER_FRAME = block("mechanical_roller/frame");

    public static final PartialModel CUCKOO_MINUTE_HAND = block("cuckoo_clock/minute_hand");

    public static final PartialModel CUCKOO_HOUR_HAND = block("cuckoo_clock/hour_hand");

    public static final PartialModel CUCKOO_LEFT_DOOR = block("cuckoo_clock/left_door");

    public static final PartialModel CUCKOO_RIGHT_DOOR = block("cuckoo_clock/right_door");

    public static final PartialModel CUCKOO_PIG = block("cuckoo_clock/pig");

    public static final PartialModel CUCKOO_CREEPER = block("cuckoo_clock/creeper");

    public static final PartialModel GANTRY_COGS = block("gantry_carriage/wheels");

    public static final PartialModel ROPE_COIL = block("rope_pulley/rope_coil");

    public static final PartialModel ROPE_HALF = block("rope_pulley/rope_half");

    public static final PartialModel ROPE_HALF_MAGNET = block("rope_pulley/rope_half_magnet");

    public static final PartialModel HOSE_COIL = block("hose_pulley/rope_coil");

    public static final PartialModel HOSE = block("hose_pulley/rope");

    public static final PartialModel HOSE_MAGNET = block("hose_pulley/pulley_magnet");

    public static final PartialModel HOSE_HALF = block("hose_pulley/rope_half");

    public static final PartialModel HOSE_HALF_MAGNET = block("hose_pulley/rope_half_magnet");

    public static final PartialModel ELEVATOR_COIL = block("elevator_pulley/rope_coil");

    public static final PartialModel ELEVATOR_MAGNET = block("elevator_pulley/pulley_magnet");

    public static final PartialModel ELEVATOR_BELT = block("elevator_pulley/rope");

    public static final PartialModel ELEVATOR_BELT_HALF = block("elevator_pulley/rope_half");

    public static final PartialModel MILLSTONE_COG = block("millstone/inner");

    public static final PartialModel SYMMETRY_PLANE = block("symmetry_effect/plane");

    public static final PartialModel SYMMETRY_CROSSPLANE = block("symmetry_effect/crossplane");

    public static final PartialModel SYMMETRY_TRIPLEPLANE = block("symmetry_effect/tripleplane");

    public static final PartialModel STICKER_HEAD = block("sticker/head");

    public static final PartialModel PORTABLE_STORAGE_INTERFACE_MIDDLE = block("portable_storage_interface/block_middle");

    public static final PartialModel PORTABLE_STORAGE_INTERFACE_MIDDLE_POWERED = block("portable_storage_interface/block_middle_powered");

    public static final PartialModel PORTABLE_STORAGE_INTERFACE_TOP = block("portable_storage_interface/block_top");

    public static final PartialModel PORTABLE_FLUID_INTERFACE_MIDDLE = block("portable_fluid_interface/block_middle");

    public static final PartialModel PORTABLE_FLUID_INTERFACE_MIDDLE_POWERED = block("portable_fluid_interface/block_middle_powered");

    public static final PartialModel PORTABLE_FLUID_INTERFACE_TOP = block("portable_fluid_interface/block_top");

    public static final PartialModel ARM_COG = block("mechanical_arm/cog");

    public static final PartialModel ARM_BASE = block("mechanical_arm/base");

    public static final PartialModel ARM_LOWER_BODY = block("mechanical_arm/lower_body");

    public static final PartialModel ARM_UPPER_BODY = block("mechanical_arm/upper_body");

    public static final PartialModel ARM_CLAW_BASE = block("mechanical_arm/claw_base");

    public static final PartialModel ARM_CLAW_BASE_GOGGLES = block("mechanical_arm/claw_base_goggles");

    public static final PartialModel ARM_CLAW_GRIP_UPPER = block("mechanical_arm/upper_claw_grip");

    public static final PartialModel ARM_CLAW_GRIP_LOWER = block("mechanical_arm/lower_claw_grip");

    public static final PartialModel MECHANICAL_PUMP_COG = block("mechanical_pump/cog");

    public static final PartialModel FLUID_PIPE_CASING = block("fluid_pipe/casing");

    public static final PartialModel FLUID_VALVE_POINTER = block("fluid_valve/pointer");

    public static final PartialModel SPOUT_TOP = block("spout/top");

    public static final PartialModel SPOUT_MIDDLE = block("spout/middle");

    public static final PartialModel SPOUT_BOTTOM = block("spout/bottom");

    public static final PartialModel PECULIAR_BELL = block("peculiar_bell");

    public static final PartialModel HAUNTED_BELL = block("haunted_bell");

    public static final PartialModel TOOLBOX_DRAWER = block("toolbox/drawer");

    public static final PartialModel SPEED_CONTROLLER_BRACKET = block("rotation_speed_controller/bracket");

    public static final PartialModel GOGGLES = block("goggles");

    public static final PartialModel EJECTOR_TOP = block("weighted_ejector/top");

    public static final PartialModel COPPER_BACKTANK_SHAFT = block("copper_backtank/block_shaft_input");

    public static final PartialModel COPPER_BACKTANK_COGS = block("copper_backtank/block_cogs");

    public static final PartialModel NETHERITE_BACKTANK_SHAFT = block("netherite_backtank/block_shaft_input");

    public static final PartialModel NETHERITE_BACKTANK_COGS = block("netherite_backtank/block_cogs");

    public static final PartialModel TRACK_SEGMENT_LEFT = block("track/segment_left");

    public static final PartialModel TRACK_SEGMENT_RIGHT = block("track/segment_right");

    public static final PartialModel TRACK_TIE = block("track/tie");

    public static final PartialModel GIRDER_SEGMENT_TOP = block("metal_girder/segment_top");

    public static final PartialModel GIRDER_SEGMENT_MIDDLE = block("metal_girder/segment_middle");

    public static final PartialModel GIRDER_SEGMENT_BOTTOM = block("metal_girder/segment_bottom");

    public static final PartialModel TRACK_STATION_OVERLAY = block("track_overlay/station");

    public static final PartialModel TRACK_SIGNAL_OVERLAY = block("track_overlay/signal");

    public static final PartialModel TRACK_ASSEMBLING_OVERLAY = block("track_overlay/assembling");

    public static final PartialModel TRACK_SIGNAL_DUAL_OVERLAY = block("track_overlay/signal_dual");

    public static final PartialModel TRACK_OBSERVER_OVERLAY = block("track_overlay/observer");

    public static final PartialModel BOGEY_FRAME = block("track/bogey/bogey_frame");

    public static final PartialModel SMALL_BOGEY_WHEELS = block("track/bogey/bogey_wheel");

    public static final PartialModel BOGEY_PIN = block("track/bogey/bogey_drive_wheel_pin");

    public static final PartialModel BOGEY_PISTON = block("track/bogey/bogey_drive_piston");

    public static final PartialModel BOGEY_DRIVE = block("track/bogey/bogey_drive");

    public static final PartialModel LARGE_BOGEY_WHEELS = block("track/bogey/bogey_drive_wheel");

    public static final PartialModel TRAIN_COUPLING_HEAD = block("track/bogey/coupling_head");

    public static final PartialModel TRAIN_COUPLING_CABLE = block("track/bogey/coupling_cable");

    public static final PartialModel TRAIN_CONTROLS_COVER = block("controls/train/cover");

    public static final PartialModel TRAIN_CONTROLS_LEVER = block("controls/train/lever");

    public static final PartialModel CONTRAPTION_CONTROLS_BUTTON = block("contraption_controls/button");

    public static final PartialModel ENGINE_PISTON = block("steam_engine/piston");

    public static final PartialModel ENGINE_LINKAGE = block("steam_engine/linkage");

    public static final PartialModel ENGINE_CONNECTOR = block("steam_engine/shaft_connector");

    public static final PartialModel BOILER_GAUGE = block("steam_engine/gauge");

    public static final PartialModel BOILER_GAUGE_DIAL = block("steam_engine/gauge_dial");

    public static final PartialModel SIGNAL_ON = block("track_signal/indicator_on");

    public static final PartialModel SIGNAL_OFF = block("track_signal/indicator_off");

    public static final PartialModel DISPLAY_LINK_TUBE = block("display_link/tube");

    public static final PartialModel DISPLAY_LINK_GLOW = block("display_link/glow");

    public static final PartialModel STATION_ON = block("track_station/flag_on");

    public static final PartialModel STATION_OFF = block("track_station/flag_off");

    public static final PartialModel STATION_ASSEMBLE = block("track_station/flag_assemble");

    public static final PartialModel SIGNAL_PANEL = block("track_signal/panel");

    public static final PartialModel SIGNAL_WHITE_CUBE = block("track_signal/white_cube");

    public static final PartialModel SIGNAL_WHITE_GLOW = block("track_signal/white_glow");

    public static final PartialModel SIGNAL_WHITE = block("track_signal/white_tube");

    public static final PartialModel SIGNAL_RED_CUBE = block("track_signal/red_cube");

    public static final PartialModel SIGNAL_RED_GLOW = block("track_signal/red_glow");

    public static final PartialModel SIGNAL_RED = block("track_signal/red_tube");

    public static final PartialModel SIGNAL_YELLOW_CUBE = block("track_signal/yellow_cube");

    public static final PartialModel SIGNAL_YELLOW_GLOW = block("track_signal/yellow_glow");

    public static final PartialModel SIGNAL_YELLOW = block("track_signal/yellow_tube");

    public static final PartialModel BLAZE_INERT = block("blaze_burner/blaze/inert");

    public static final PartialModel BLAZE_SUPER_ACTIVE = block("blaze_burner/blaze/super_active");

    public static final PartialModel BLAZE_GOGGLES = block("blaze_burner/goggles");

    public static final PartialModel BLAZE_GOGGLES_SMALL = block("blaze_burner/goggles_small");

    public static final PartialModel BLAZE_IDLE = block("blaze_burner/blaze/idle");

    public static final PartialModel BLAZE_ACTIVE = block("blaze_burner/blaze/active");

    public static final PartialModel BLAZE_SUPER = block("blaze_burner/blaze/super");

    public static final PartialModel BLAZE_BURNER_FLAME = block("blaze_burner/flame");

    public static final PartialModel BLAZE_BURNER_RODS = block("blaze_burner/rods_small");

    public static final PartialModel BLAZE_BURNER_RODS_2 = block("blaze_burner/rods_large");

    public static final PartialModel BLAZE_BURNER_SUPER_RODS = block("blaze_burner/superheated_rods_small");

    public static final PartialModel BLAZE_BURNER_SUPER_RODS_2 = block("blaze_burner/superheated_rods_large");

    public static final PartialModel WHISTLE_MOUTH_LARGE = block("steam_whistle/large_mouth");

    public static final PartialModel WHISTLE_MOUTH_MEDIUM = block("steam_whistle/medium_mouth");

    public static final PartialModel WHISTLE_MOUTH_SMALL = block("steam_whistle/small_mouth");

    public static final PartialModel WATER_WHEEL = block("water_wheel/wheel");

    public static final PartialModel LARGE_WATER_WHEEL = block("large_water_wheel/block");

    public static final PartialModel LARGE_WATER_WHEEL_EXTENSION = block("large_water_wheel/block_extension");

    public static final PartialModel CRAFTING_BLUEPRINT_1x1 = entity("crafting_blueprint_small");

    public static final PartialModel CRAFTING_BLUEPRINT_2x2 = entity("crafting_blueprint_medium");

    public static final PartialModel CRAFTING_BLUEPRINT_3x3 = entity("crafting_blueprint_large");

    public static final PartialModel TRAIN_HAT = entity("train_hat");

    public static final PartialModel COUPLING_ATTACHMENT = entity("minecart_coupling/attachment");

    public static final PartialModel COUPLING_RING = entity("minecart_coupling/ring");

    public static final PartialModel COUPLING_CONNECTOR = entity("minecart_coupling/connector");

    public static final Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>> PIPE_ATTACHMENTS = new EnumMap(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);

    public static final Map<Direction, PartialModel> METAL_GIRDER_BRACKETS = new EnumMap(Direction.class);

    public static final Map<DyeColor, PartialModel> TOOLBOX_LIDS = new EnumMap(DyeColor.class);

    public static final Map<ResourceLocation, Couple<PartialModel>> FOLDING_DOORS = new HashMap();

    public static final List<PartialModel> CONTRAPTION_CONTROLS_INDICATOR = new ArrayList();

    private static void putFoldingDoor(String path) {
        FOLDING_DOORS.put(Create.asResource(path), Couple.create(block(path + "/fold_left"), block(path + "/fold_right")));
    }

    private static PartialModel block(String path) {
        return new PartialModel(Create.asResource("block/" + path));
    }

    private static PartialModel entity(String path) {
        return new PartialModel(Create.asResource("entity/" + path));
    }

    public static void init() {
    }

    static {
        for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials type : FluidTransportBehaviour.AttachmentTypes.ComponentPartials.values()) {
            Map<Direction, PartialModel> map = new HashMap();
            for (Direction d : Iterate.directions) {
                String asId = Lang.asId(type.name());
                map.put(d, block("fluid_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
            }
            PIPE_ATTACHMENTS.put(type, map);
        }
        for (DyeColor color : DyeColor.values()) {
            TOOLBOX_LIDS.put(color, block("toolbox/lid/" + Lang.asId(color.name())));
        }
        for (Direction d : Iterate.horizontalDirections) {
            METAL_GIRDER_BRACKETS.put(d, block("metal_girder/bracket_" + Lang.asId(d.name())));
        }
        for (int i = 0; i < 8; i++) {
            CONTRAPTION_CONTROLS_INDICATOR.add(block("contraption_controls/indicator_" + i));
        }
        putFoldingDoor("andesite_door");
        putFoldingDoor("copper_door");
    }
}