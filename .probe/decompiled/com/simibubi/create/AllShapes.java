package com.simibubi.create;

import com.simibubi.create.content.logistics.chute.ChuteShapes;
import com.simibubi.create.content.trains.track.TrackVoxelShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import java.util.function.BiFunction;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AllShapes {

    public static final VoxelShaper CASING_14PX = shape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0).forDirectional();

    public static final VoxelShaper CASING_13PX = shape(0.0, 0.0, 0.0, 16.0, 13.0, 16.0).forDirectional();

    public static final VoxelShaper CASING_12PX = shape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0).forDirectional();

    public static final VoxelShaper CASING_11PX = shape(0.0, 0.0, 0.0, 16.0, 11.0, 16.0).forDirectional();

    public static final VoxelShaper CASING_3PX = shape(0.0, 0.0, 0.0, 16.0, 3.0, 16.0).forDirectional();

    public static final VoxelShaper CASING_2PX = shape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0).forDirectional();

    public static final VoxelShaper MOTOR_BLOCK = shape(3.0, 0.0, 3.0, 13.0, 14.0, 13.0).forDirectional();

    public static final VoxelShaper FOUR_VOXEL_POLE = shape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0).forAxis();

    public static final VoxelShaper SIX_VOXEL_POLE = shape(5.0, 0.0, 5.0, 11.0, 16.0, 11.0).forAxis();

    public static final VoxelShaper EIGHT_VOXEL_POLE = shape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0).forAxis();

    public static final VoxelShaper TEN_VOXEL_POLE = shape(3.0, 0.0, 3.0, 13.0, 16.0, 13.0).forAxis();

    public static final VoxelShaper FURNACE_ENGINE = shape(1.0, 1.0, 0.0, 15.0, 15.0, 16.0).add(0.0, 0.0, 9.0, 16.0, 16.0, 14.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper PORTABLE_STORAGE_INTERFACE = shape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0).forDirectional();

    public static final VoxelShaper PULLEY = shape(0.0, 0.0, 0.0, 16.0, 16.0, 2.0).add(1.0, 1.0, 2.0, 15.0, 15.0, 14.0).add(2.0, 13.0, 2.0, 14.0, 16.0, 14.0).add(0.0, 0.0, 14.0, 16.0, 16.0, 16.0).forHorizontalAxis();

    public static final VoxelShaper ELEVATOR_PULLEY = shape(0.0, 0.0, 0.0, 16.0, 16.0, 2.0).add(0.0, 0.0, 14.0, 16.0, 16.0, 16.0).add(2.0, 0.0, 2.0, 14.0, 14.0, 14.0).forHorizontal(Direction.EAST);

    public static final VoxelShaper SAIL_FRAME_COLLISION = shape(0.0, 5.0, 0.0, 16.0, 9.0, 16.0).erase(2.0, 0.0, 2.0, 14.0, 16.0, 14.0).forDirectional();

    public static final VoxelShaper SAIL_FRAME = shape(0.0, 5.0, 0.0, 16.0, 9.0, 16.0).forDirectional();

    public static final VoxelShaper SAIL = shape(0.0, 5.0, 0.0, 16.0, 10.0, 16.0).forDirectional();

    public static final VoxelShaper HARVESTER_BASE = shape(0.0, 2.0, 0.0, 16.0, 14.0, 3.0).forDirectional(Direction.SOUTH);

    public static final VoxelShaper ROLLER_BASE = shape(0.0, 0.0, 0.0, 16.0, 16.0, 10.0).forDirectional(Direction.SOUTH);

    public static final VoxelShaper NOZZLE = shape(2.0, 0.0, 2.0, 14.0, 14.0, 14.0).add(1.0, 13.0, 1.0, 15.0, 15.0, 15.0).erase(3.0, 13.0, 3.0, 13.0, 15.0, 13.0).forDirectional();

    public static final VoxelShaper CRANK = shape(5.0, 0.0, 5.0, 11.0, 6.0, 11.0).add(1.0, 3.0, 1.0, 15.0, 8.0, 15.0).forDirectional();

    public static final VoxelShaper VALVE_HANDLE = shape(5.0, 0.0, 5.0, 11.0, 4.0, 11.0).add(1.0, 3.0, 1.0, 15.0, 8.0, 15.0).forDirectional();

    public static final VoxelShaper CART_ASSEMBLER = shape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0).add(-2.0, 0.0, 1.0, 18.0, 14.0, 15.0).forHorizontalAxis();

    public static final VoxelShaper CART_ASSEMBLER_PLAYER_COLLISION = shape(0.0, 0.0, 1.0, 16.0, 16.0, 15.0).forHorizontalAxis();

    public static final VoxelShaper STOCKPILE_SWITCH = shape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0).add(1.0, 0.0, 1.0, 15.0, 16.0, 15.0).add(0.0, 14.0, 0.0, 16.0, 16.0, 16.0).add(3.0, 3.0, -2.0, 13.0, 13.0, 2.0).forHorizontal(Direction.NORTH);

    public static final VoxelShaper CONTENT_OBSERVER = shape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0).add(1.0, 0.0, 1.0, 15.0, 16.0, 15.0).add(0.0, 14.0, 0.0, 16.0, 16.0, 16.0).add(3.0, 3.0, -2.0, 13.0, 13.0, 2.0).forHorizontal(Direction.NORTH);

    public static final VoxelShaper FUNNEL_COLLISION = shape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0).forDirectional(Direction.UP);

    public static final VoxelShaper BELT_FUNNEL_RETRACTED = shape(2.0, -2.0, 14.0, 14.0, 14.0, 18.0).add(0.0, -5.0, 8.0, 16.0, 16.0, 14.0).forHorizontal(Direction.NORTH);

    public static final VoxelShaper BELT_FUNNEL_EXTENDED = shape(2.0, -2.0, 14.0, 14.0, 14.0, 18.0).add(3.0, -4.0, 10.0, 13.0, 13.0, 14.0).add(2.0, -4.0, 6.0, 14.0, 14.0, 10.0).add(0.0, -5.0, 0.0, 16.0, 16.0, 6.0).forHorizontal(Direction.NORTH);

    public static final VoxelShaper BELT_FUNNEL_PERPENDICULAR = shape(2.0, -2.0, 14.0, 14.0, 14.0, 18.0).add(1.0, 8.0, 12.0, 15.0, 15.0, 14.0).add(0.1, 13.0, 7.0, 15.9, 15.0, 11.0).add(0.1, 9.0, 8.0, 15.9, 13.0, 12.0).add(0.1, 5.0, 9.0, 15.9, 9.0, 13.0).add(0.1, 1.0, 10.0, 15.9, 5.0, 14.0).add(0.1, -3.0, 11.0, 15.9, 1.0, 15.0).forHorizontal(Direction.NORTH);

    public static final VoxelShaper FUNNEL_WALL = shape(2.0, 2.0, 14.0, 14.0, 14.0, 18.0).add(1.0, 8.0, 12.0, 15.0, 15.0, 14.0).add(0.1, 13.0, 7.0, 15.9, 15.0, 11.0).add(0.1, 9.0, 8.0, 15.9, 13.0, 12.0).add(0.1, 5.0, 9.0, 15.9, 9.0, 13.0).add(0.1, 1.0, 10.0, 15.9, 5.0, 14.0).add(0.1, -1.0, 11.0, 15.9, 1.0, 15.0).forHorizontal(Direction.NORTH);

    public static final VoxelShaper FLUID_VALVE = shape(3.0, 0.0, 3.0, 13.0, 16.0, 13.0).add(2.0, 2.0, 2.0, 14.0, 14.0, 14.0).forAxis();

    public static final VoxelShaper TOOLBOX = shape(1.0, 0.0, 4.0, 15.0, 9.0, 12.0).forHorizontal(Direction.NORTH);

    public static final VoxelShaper SMART_FLUID_PIPE_FLOOR = shape(4.0, 4.0, 0.0, 12.0, 12.0, 16.0).add(3.0, 3.0, 3.0, 13.0, 13.0, 13.0).add(5.0, 13.0, 3.0, 11.0, 14.0, 11.0).add(5.0, 14.0, 4.0, 11.0, 15.0, 10.0).add(5.0, 15.0, 5.0, 11.0, 16.0, 9.0).add(5.0, 16.0, 6.0, 11.0, 17.0, 8.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper SMART_FLUID_PIPE_WALL = shape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0).add(3.0, 3.0, 3.0, 13.0, 13.0, 13.0).add(5.0, 5.0, 13.0, 11.0, 13.0, 14.0).add(5.0, 6.0, 14.0, 11.0, 12.0, 15.0).add(5.0, 7.0, 15.0, 11.0, 11.0, 16.0).add(5.0, 8.0, 16.0, 11.0, 10.0, 17.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper SMART_FLUID_PIPE_CEILING = shape(4.0, 4.0, 0.0, 12.0, 12.0, 16.0).add(3.0, 3.0, 3.0, 13.0, 13.0, 13.0).add(5.0, 2.0, 3.0, 11.0, 3.0, 11.0).add(5.0, 1.0, 4.0, 11.0, 2.0, 10.0).add(5.0, 0.0, 5.0, 11.0, 1.0, 9.0).add(5.0, -1.0, 6.0, 11.0, 0.0, 8.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper PUMP = shape(2.0, 0.0, 2.0, 14.0, 5.0, 14.0).add(4.0, 0.0, 4.0, 12.0, 16.0, 12.0).add(3.0, 11.0, 3.0, 13.0, 16.0, 13.0).forDirectional(Direction.UP);

    public static final VoxelShaper CRUSHING_WHEEL_CONTROLLER_COLLISION = shape(0.0, 0.0, 0.0, 16.0, 13.0, 16.0).forDirectional(Direction.DOWN);

    public static final VoxelShaper BELL_FLOOR = shape(0.0, 0.0, 5.0, 16.0, 11.0, 11.0).add(3.0, 1.0, 3.0, 13.0, 13.0, 13.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper BELL_WALL = shape(5.0, 5.0, 8.0, 11.0, 11.0, 16.0).add(3.0, 1.0, 3.0, 13.0, 13.0, 13.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper BELL_DOUBLE_WALL = shape(5.0, 5.0, 0.0, 11.0, 11.0, 16.0).add(3.0, 1.0, 3.0, 13.0, 13.0, 13.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper BELL_CEILING = shape(0.0, 5.0, 5.0, 16.0, 16.0, 11.0).add(3.0, 1.0, 3.0, 13.0, 13.0, 13.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper GIRDER_BEAM = shape(4.0, 2.0, 0.0, 12.0, 14.0, 16.0).forHorizontalAxis();

    public static final VoxelShaper GIRDER_BEAM_SHAFT = shape(GIRDER_BEAM.get(Direction.Axis.X)).add(SIX_VOXEL_POLE.get(Direction.Axis.Z)).forHorizontalAxis();

    public static final VoxelShaper STEP_BOTTOM = shape(0.0, 0.0, 8.0, 16.0, 8.0, 16.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper STEP_TOP = shape(0.0, 8.0, 8.0, 16.0, 16.0, 16.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper CONTROLS = shape(0.0, 0.0, 6.0, 16.0, 14.0, 16.0).forHorizontal(Direction.NORTH);

    public static final VoxelShaper CONTRAPTION_CONTROLS = shape(0.0, 0.0, 6.0, 2.0, 14.0, 16.0).add(14.0, 0.0, 6.0, 16.0, 14.0, 16.0).add(0.0, 0.0, 14.0, 16.0, 14.0, 16.0).add(0.0, 0.0, 7.0, 16.0, 10.0, 16.0).forHorizontal(Direction.NORTH);

    public static final VoxelShaper NIXIE_TUBE = shape(9.0, 0.0, 5.0, 15.0, 12.0, 11.0).add(1.0, 0.0, 5.0, 7.0, 12.0, 11.0).forHorizontalAxis();

    public static final VoxelShaper NIXIE_TUBE_CEILING = shape(9.0, 4.0, 5.0, 15.0, 16.0, 11.0).add(1.0, 4.0, 5.0, 7.0, 16.0, 11.0).forHorizontalAxis();

    public static final VoxelShaper NIXIE_TUBE_WALL = shape(5.0, 9.0, 0.0, 11.0, 15.0, 12.0).add(5.0, 1.0, 0.0, 11.0, 7.0, 12.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper FLAP_DISPLAY = shape(0.0, 0.0, 3.0, 16.0, 16.0, 13.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper DATA_GATHERER = shape(1.0, 0.0, 1.0, 15.0, 6.0, 15.0).add(3.0, 5.0, 3.0, 13.0, 9.0, 13.0).forDirectional();

    public static final VoxelShaper STEAM_ENGINE = shape(1.0, 0.0, 1.0, 15.0, 3.0, 15.0).add(3.0, 0.0, 3.0, 13.0, 15.0, 13.0).add(1.0, 5.0, 4.0, 15.0, 13.0, 12.0).forHorizontalAxis();

    public static final VoxelShaper STEAM_ENGINE_CEILING = shape(1.0, 13.0, 1.0, 15.0, 16.0, 15.0).add(3.0, 1.0, 3.0, 13.0, 16.0, 13.0).add(1.0, 3.0, 4.0, 15.0, 11.0, 12.0).forHorizontalAxis();

    public static final VoxelShaper STEAM_ENGINE_WALL = shape(1.0, 1.0, 0.0, 15.0, 15.0, 3.0).add(3.0, 3.0, 0.0, 13.0, 13.0, 15.0).add(1.0, 4.0, 5.0, 15.0, 12.0, 13.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper PLACARD = shape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0).forDirectional(Direction.UP);

    public static final VoxelShaper CLIPBOARD_FLOOR = shape(3.0, 0.0, 1.0, 13.0, 1.0, 15.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper CLIPBOARD_CEILING = shape(3.0, 15.0, 1.0, 13.0, 16.0, 15.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper CLIPBOARD_WALL = shape(3.0, 1.0, 0.0, 13.0, 15.0, 1.0).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper TRACK_ORTHO = shape(TrackVoxelShapes.orthogonal()).forHorizontal(Direction.NORTH);

    public static final VoxelShaper TRACK_ASC = shape(TrackVoxelShapes.ascending()).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper TRACK_DIAG = shape(TrackVoxelShapes.diagonal()).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper TRACK_ORTHO_LONG = shape(TrackVoxelShapes.longOrthogonalZOffset()).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper DEPLOYER_INTERACTION = shape(CASING_12PX.get(Direction.UP)).add(SIX_VOXEL_POLE.get(Direction.Axis.Y)).forDirectional(Direction.UP);

    public static final VoxelShaper WHISTLE_BASE = shape(1.0, 0.0, 1.0, 15.0, 3.0, 15.0).add(5.0, 0.0, 5.0, 11.0, 8.0, 11.0).forDirectional(Direction.UP);

    private static final VoxelShape PISTON_HEAD = ((BlockState) ((BlockState) Blocks.PISTON_HEAD.defaultBlockState().m_61124_(DirectionalBlock.FACING, Direction.UP)).m_61124_(PistonHeadBlock.SHORT, true)).m_60808_(null, null);

    private static final VoxelShape PISTON_EXTENDED = shape(CASING_12PX.get(Direction.UP)).add(FOUR_VOXEL_POLE.get(Direction.Axis.Y)).build();

    private static final VoxelShape SMALL_GEAR_SHAPE = cuboid(2.0, 6.0, 2.0, 14.0, 10.0, 14.0);

    private static final VoxelShape LARGE_GEAR_SHAPE = cuboid(0.0, 6.0, 0.0, 16.0, 10.0, 16.0);

    private static final VoxelShape VERTICAL_TABLET_SHAPE = cuboid(3.0, 1.0, -1.0, 13.0, 15.0, 3.0);

    private static final VoxelShape SQUARE_TABLET_SHAPE = cuboid(2.0, 2.0, -1.0, 14.0, 14.0, 3.0);

    private static final VoxelShape LOGISTICS_TABLE_SLOPE = shape(0.0, 10.0, 10.667, 16.0, 14.0, 15.0).add(0.0, 12.0, 6.333, 16.0, 16.0, 10.667).add(0.0, 14.0, 2.0, 16.0, 18.0, 6.333).build();

    private static final VoxelShape TANK_BOTTOM_LID = shape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0).build();

    private static final VoxelShape TANK_TOP_LID = shape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0).build();

    private static final VoxelShape WHISTLE_SMALL = shape(4.0, 3.0, 4.0, 12.0, 16.0, 12.0).build();

    private static final VoxelShape WHISTLE_MEDIUM = shape(3.0, 3.0, 3.0, 13.0, 16.0, 13.0).build();

    private static final VoxelShape WHISTLE_LARGE = shape(2.0, 3.0, 2.0, 14.0, 16.0, 14.0).build();

    public static final VoxelShape SCAFFOLD_HALF = shape(0.0, 8.0, 0.0, 16.0, 16.0, 16.0).build();

    public static final VoxelShape SCAFFOLD_FULL = shape(SCAFFOLD_HALF).add(0.0, 0.0, 0.0, 2.0, 16.0, 2.0).add(0.0, 0.0, 14.0, 2.0, 16.0, 16.0).add(14.0, 0.0, 0.0, 16.0, 16.0, 2.0).add(14.0, 0.0, 14.0, 16.0, 16.0, 16.0).build();

    public static final VoxelShape TRACK_CROSS = shape(TRACK_ORTHO.get(Direction.SOUTH)).add(TRACK_ORTHO.get(Direction.EAST)).build();

    public static final VoxelShape TRACK_CROSS_DIAG = shape(TRACK_DIAG.get(Direction.SOUTH)).add(TRACK_DIAG.get(Direction.EAST)).build();

    public static final VoxelShape TRACK_COLLISION = shape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0).build();

    public static final VoxelShape TRACK_FALLBACK = shape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0).build();

    public static final VoxelShape BASIN_BLOCK_SHAPE = shape(0.0, 2.0, 0.0, 16.0, 16.0, 16.0).erase(2.0, 2.0, 2.0, 14.0, 16.0, 14.0).add(2.0, 0.0, 2.0, 14.0, 2.0, 14.0).build();

    public static final VoxelShape BASIN_RAYTRACE_SHAPE = shape(0.0, 2.0, 0.0, 16.0, 16.0, 16.0).add(2.0, 0.0, 2.0, 14.0, 2.0, 14.0).build();

    public static final VoxelShape BASIN_COLLISION_SHAPE = shape(0.0, 2.0, 0.0, 16.0, 13.0, 16.0).erase(2.0, 5.0, 2.0, 14.0, 16.0, 14.0).add(2.0, 0.0, 2.0, 14.0, 2.0, 14.0).build();

    public static final VoxelShape GIRDER_CROSS = shape(TEN_VOXEL_POLE.get(Direction.Axis.Y)).add(GIRDER_BEAM.get(Direction.Axis.X)).add(GIRDER_BEAM.get(Direction.Axis.Z)).build();

    public static final VoxelShape BACKTANK = shape(3.0, 0.0, 3.0, 13.0, 12.0, 13.0).add(SIX_VOXEL_POLE.get(Direction.Axis.Y)).build();

    public static final VoxelShape SPEED_CONTROLLER = shape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0).add(1.0, 1.0, 1.0, 15.0, 13.0, 15.0).add(0.0, 8.0, 0.0, 16.0, 14.0, 16.0).build();

    public static final VoxelShape HEATER_BLOCK_SHAPE = shape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0).build();

    public static final VoxelShape HEATER_BLOCK_SPECIAL_COLLISION_SHAPE = shape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0).build();

    public static final VoxelShape CRUSHING_WHEEL_COLLISION_SHAPE = cuboid(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    public static final VoxelShape SEAT = cuboid(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    public static final VoxelShape SEAT_COLLISION = cuboid(0.0, 0.0, 0.0, 16.0, 6.0, 16.0);

    public static final VoxelShape SEAT_COLLISION_PLAYERS = cuboid(0.0, 0.0, 0.0, 16.0, 3.0, 16.0);

    public static final VoxelShape MECHANICAL_PROCESSOR_SHAPE = shape(Shapes.block()).erase(4.0, 0.0, 4.0, 12.0, 16.0, 12.0).build();

    public static final VoxelShape TURNTABLE_SHAPE = shape(1.0, 4.0, 1.0, 15.0, 8.0, 15.0).add(5.0, 0.0, 5.0, 11.0, 4.0, 11.0).build();

    public static final VoxelShape CRATE_BLOCK_SHAPE = cuboid(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);

    public static final VoxelShape TABLE_POLE_SHAPE = shape(4.0, 0.0, 4.0, 12.0, 2.0, 12.0).add(5.0, 2.0, 5.0, 11.0, 14.0, 11.0).build();

    public static final VoxelShape BELT_COLLISION_MASK = cuboid(0.0, 0.0, 0.0, 16.0, 19.0, 16.0);

    public static final VoxelShape SCHEMATICANNON_SHAPE = shape(1.0, 0.0, 1.0, 15.0, 8.0, 15.0).add(0.5, 8.0, 0.5, 15.5, 11.0, 15.5).build();

    public static final VoxelShape PULLEY_MAGNET = shape(3.0, 0.0, 3.0, 13.0, 3.0, 13.0).add(FOUR_VOXEL_POLE.get(Direction.UP)).build();

    public static final VoxelShape SPOUT = shape(1.0, 2.0, 1.0, 15.0, 14.0, 15.0).add(2.0, 0.0, 2.0, 14.0, 16.0, 14.0).build();

    public static final VoxelShape MILLSTONE = shape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0).add(2.0, 6.0, 2.0, 14.0, 16.0, 14.0).build();

    public static final VoxelShape CUCKOO_CLOCK = shape(1.0, 0.0, 1.0, 15.0, 19.0, 15.0).build();

    public static final VoxelShape GAUGE_SHAPE_UP = shape(1.0, 0.0, 0.0, 15.0, 2.0, 16.0).add(2.0, 2.0, 1.0, 14.0, 14.0, 15.0).build();

    public static final VoxelShape MECHANICAL_ARM = shape(2.0, 0.0, 2.0, 14.0, 10.0, 14.0).add(3.0, 0.0, 3.0, 13.0, 14.0, 13.0).add(0.0, 0.0, 0.0, 16.0, 6.0, 16.0).build();

    public static final VoxelShape MECHANICAL_ARM_CEILING = shape(2.0, 6.0, 2.0, 14.0, 16.0, 14.0).add(3.0, 2.0, 3.0, 13.0, 16.0, 13.0).add(0.0, 10.0, 0.0, 16.0, 16.0, 16.0).build();

    public static final VoxelShape CHUTE = shape(1.0, 8.0, 1.0, 15.0, 16.0, 15.0).add(2.0, 0.0, 2.0, 14.0, 8.0, 14.0).build();

    public static final VoxelShape TANK = shape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0).build();

    public static final VoxelShape TANK_TOP = shape(TANK_TOP_LID).add(TANK).build();

    public static final VoxelShape TANK_BOTTOM = shape(TANK_BOTTOM_LID).add(TANK).build();

    public static final VoxelShape TANK_TOP_BOTTOM = shape(TANK_BOTTOM_LID).add(TANK_TOP_LID).add(TANK).build();

    public static final VoxelShape FUNNEL_FLOOR = shape(2.0, -2.0, 2.0, 14.0, 8.0, 14.0).add(1.0, 1.0, 1.0, 15.0, 8.0, 15.0).add(0.0, 4.0, 0.0, 16.0, 10.0, 16.0).build();

    public static final VoxelShape FUNNEL_CEILING = shape(2.0, 8.0, 2.0, 14.0, 18.0, 14.0).add(1.0, 8.0, 1.0, 15.0, 15.0, 15.0).add(0.0, 6.0, 0.0, 16.0, 12.0, 16.0).build();

    public static final VoxelShape STATION = shape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0).add(1.0, 0.0, 1.0, 15.0, 13.0, 15.0).build();

    public static final VoxelShape WHISTLE_SMALL_FLOOR = shape(WHISTLE_SMALL).add(WHISTLE_BASE.get(Direction.UP)).build();

    public static final VoxelShape WHISTLE_MEDIUM_FLOOR = shape(WHISTLE_MEDIUM).add(WHISTLE_BASE.get(Direction.UP)).build();

    public static final VoxelShape WHISTLE_LARGE_FLOOR = shape(WHISTLE_LARGE).add(WHISTLE_BASE.get(Direction.UP)).build();

    public static final VoxelShape WHISTLE_EXTENDER_SMALL = shape(4.0, 0.0, 4.0, 12.0, 10.0, 12.0).build();

    public static final VoxelShape WHISTLE_EXTENDER_MEDIUM = shape(3.0, 0.0, 3.0, 13.0, 10.0, 13.0).build();

    public static final VoxelShape WHISTLE_EXTENDER_LARGE = shape(2.0, 0.0, 2.0, 14.0, 10.0, 14.0).build();

    public static final VoxelShape WHISTLE_EXTENDER_SMALL_DOUBLE = shape(4.0, 0.0, 4.0, 12.0, 18.0, 12.0).build();

    public static final VoxelShape WHISTLE_EXTENDER_MEDIUM_DOUBLE = shape(3.0, 0.0, 3.0, 13.0, 18.0, 13.0).build();

    public static final VoxelShape WHISTLE_EXTENDER_LARGE_DOUBLE = shape(2.0, 0.0, 2.0, 14.0, 18.0, 14.0).build();

    public static final VoxelShape WHISTLE_EXTENDER_SMALL_DOUBLE_CONNECTED = shape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0).build();

    public static final VoxelShape WHISTLE_EXTENDER_MEDIUM_DOUBLE_CONNECTED = shape(3.0, 0.0, 3.0, 13.0, 16.0, 13.0).build();

    public static final VoxelShape WHISTLE_EXTENDER_LARGE_DOUBLE_CONNECTED = shape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0).build();

    public static final VoxelShaper TRACK_CROSS_ORTHO_DIAG = shape(TRACK_DIAG.get(Direction.SOUTH)).add(TRACK_ORTHO.get(Direction.EAST)).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper TRACK_CROSS_DIAG_ORTHO = shape(TRACK_DIAG.get(Direction.SOUTH)).add(TRACK_ORTHO.get(Direction.SOUTH)).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper MECHANICAL_PISTON_HEAD = shape(PISTON_HEAD).forDirectional();

    public static final VoxelShaper MECHANICAL_PISTON = CASING_12PX;

    public static final VoxelShaper MECHANICAL_PISTON_EXTENDED = shape(PISTON_EXTENDED).forDirectional();

    public static final VoxelShaper SMALL_GEAR = shape(SMALL_GEAR_SHAPE).add(SIX_VOXEL_POLE.get(Direction.Axis.Y)).forAxis();

    public static final VoxelShaper LARGE_GEAR = shape(LARGE_GEAR_SHAPE).add(SIX_VOXEL_POLE.get(Direction.Axis.Y)).forAxis();

    public static final VoxelShaper LOGISTICAL_CONTROLLER = shape(SQUARE_TABLET_SHAPE).forDirectional(Direction.SOUTH);

    public static final VoxelShaper REDSTONE_BRIDGE = shape(VERTICAL_TABLET_SHAPE).forDirectional(Direction.SOUTH).withVerticalShapes(LOGISTICAL_CONTROLLER.get(Direction.UP));

    public static final VoxelShaper LOGISTICS_TABLE = shape(TABLE_POLE_SHAPE).add(LOGISTICS_TABLE_SLOPE).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper SCHEMATICS_TABLE = shape(4.0, 0.0, 4.0, 12.0, 12.0, 12.0).add(0.0, 11.0, 2.0, 16.0, 14.0, 14.0).forDirectional(Direction.SOUTH);

    public static final VoxelShaper CHUTE_SLOPE = shape(ChuteShapes.createSlope()).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper WHISTLE_SMALL_WALL = shape(WHISTLE_SMALL).add(WHISTLE_BASE.get(Direction.NORTH)).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper WHISTLE_MEDIUM_WALL = shape(WHISTLE_MEDIUM).add(WHISTLE_BASE.get(Direction.NORTH)).forHorizontal(Direction.SOUTH);

    public static final VoxelShaper WHISTLE_LARGE_WALL = shape(WHISTLE_LARGE).add(WHISTLE_BASE.get(Direction.NORTH)).forHorizontal(Direction.SOUTH);

    private static AllShapes.Builder shape(VoxelShape shape) {
        return new AllShapes.Builder(shape);
    }

    private static AllShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return shape(cuboid(x1, y1, z1, x2, y2, z2));
    }

    private static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }

    public static class Builder {

        private VoxelShape shape;

        public Builder(VoxelShape shape) {
            this.shape = shape;
        }

        public AllShapes.Builder add(VoxelShape shape) {
            this.shape = Shapes.or(this.shape, shape);
            return this;
        }

        public AllShapes.Builder add(double x1, double y1, double z1, double x2, double y2, double z2) {
            return this.add(AllShapes.cuboid(x1, y1, z1, x2, y2, z2));
        }

        public AllShapes.Builder erase(double x1, double y1, double z1, double x2, double y2, double z2) {
            this.shape = Shapes.join(this.shape, AllShapes.cuboid(x1, y1, z1, x2, y2, z2), BooleanOp.ONLY_FIRST);
            return this;
        }

        public VoxelShape build() {
            return this.shape;
        }

        public VoxelShaper build(BiFunction<VoxelShape, Direction, VoxelShaper> factory, Direction direction) {
            return (VoxelShaper) factory.apply(this.shape, direction);
        }

        public VoxelShaper build(BiFunction<VoxelShape, Direction.Axis, VoxelShaper> factory, Direction.Axis axis) {
            return (VoxelShaper) factory.apply(this.shape, axis);
        }

        public VoxelShaper forDirectional(Direction direction) {
            return this.build(VoxelShaper::forDirectional, direction);
        }

        public VoxelShaper forAxis() {
            return this.build(VoxelShaper::forAxis, Direction.Axis.Y);
        }

        public VoxelShaper forHorizontalAxis() {
            return this.build(VoxelShaper::forHorizontalAxis, Direction.Axis.Z);
        }

        public VoxelShaper forHorizontal(Direction direction) {
            return this.build(VoxelShaper::forHorizontal, direction);
        }

        public VoxelShaper forDirectional() {
            return this.forDirectional(Direction.UP);
        }
    }
}