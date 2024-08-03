package snownee.kiwi.customization.block.behavior;

import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.customization.CustomFeatureTags;
import snownee.kiwi.customization.block.KBlockSettings;
import snownee.kiwi.customization.block.KBlockUtils;
import snownee.kiwi.customization.block.component.KBlockComponent;

public class SitManager {

    public static final Component ENTITY_NAME = Component.literal("Seat from Kiwi");

    public static final double VERTICAL_OFFSET = 0.23;

    public static void tick(Display.BlockDisplay display) {
        if (display.f_19797_ >= 7) {
            if (!display.m_20160_()) {
                display.m_146870_();
            }
            BlockPos pos = BlockPos.containing(display.m_20185_(), display.m_20186_() + 0.23, display.m_20189_());
            BlockState blockState = display.m_9236_().getBlockState(pos);
            if (!blockState.m_60713_(display.getBlockState().m_60734_())) {
                display.m_146870_();
            }
        }
    }

    public static boolean sit(Player player, BlockHitResult hitResult) {
        if (hitResult.getDirection() != Direction.DOWN && !player.isSecondaryUseActive()) {
            Level level = player.m_9236_();
            BlockPos pos = hitResult.getBlockPos();
            BlockState blockState = level.getBlockState(pos);
            if (!blockState.m_204336_(CustomFeatureTags.SITTABLE)) {
                return false;
            } else {
                Block block = blockState.m_60734_();
                if (block instanceof BedBlock) {
                    if ((Boolean) blockState.m_61143_(BedBlock.OCCUPIED) || !BedBlock.canSetSpawn(level)) {
                        return false;
                    }
                    Direction direction = (Direction) blockState.m_61143_(BedBlock.f_54117_);
                    if (player instanceof ServerPlayer serverPlayer && serverPlayer.bedInRange(pos, direction)) {
                        return false;
                    }
                } else if (player.m_146892_().distanceToSqr(hitResult.m_82450_()) > 12.0) {
                    return false;
                }
                if (!player.m_21205_().isEmpty() && player.m_21205_().is(block.asItem())) {
                    return false;
                } else if (!KSitCommonConfig.sitOnStairs && block instanceof StairBlock) {
                    return false;
                } else if (!KSitCommonConfig.sitOnSlab && block instanceof SlabBlock) {
                    return false;
                } else if (!KSitCommonConfig.sitOnCarpet && block instanceof CarpetBlock) {
                    return false;
                } else if (!KSitCommonConfig.sitOnBed && block instanceof BedBlock) {
                    return false;
                } else if (!level.getEntities(EntityType.BLOCK_DISPLAY, new AABB(pos).expandTowards(0.0, 1.0, 0.0), SitManager::isSeatEntity).isEmpty()) {
                    return false;
                } else {
                    if (!level.isClientSide) {
                        Display.BlockDisplay display = new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, level);
                        display.m_6593_(ENTITY_NAME);
                        display.setBlockState(blockState);
                        VoxelShape shape = blockState.m_60651_(level, pos, CollisionContext.of(player));
                        Vec3 seatPos = null;
                        Direction facing = guessBlockFacing(blockState, player);
                        if (!shape.isEmpty()) {
                            AABB bounds = shape.bounds();
                            double x = (bounds.minX + bounds.maxX) / 2.0;
                            double y = bounds.maxX + 0.1;
                            double z = (bounds.minZ + bounds.maxZ) / 2.0;
                            if (facing != null) {
                                x += (double) facing.getStepX() * 0.1;
                                z += (double) facing.getStepZ() * 0.1;
                            }
                            Vec3 traceStart = new Vec3((double) pos.m_123341_() + x, (double) pos.m_123342_() + y, (double) pos.m_123343_() + z);
                            Vec3 traceEnd = traceStart.add(0.0, -2.0, 0.0);
                            BlockHitResult hit = shape.clip(traceStart, traceEnd, pos);
                            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                                seatPos = hit.m_82450_();
                            }
                        }
                        if (facing != null) {
                            float yRot = facing.toYRot();
                            display.m_146922_(yRot);
                            display.m_20242_(true);
                        }
                        if (seatPos == null) {
                            seatPos = Vec3.atCenterOf(pos);
                        }
                        double clampedY = Mth.clamp(seatPos.y, (double) pos.m_123342_(), (double) pos.m_123342_() + 0.999);
                        display.m_6034_(seatPos.x, clampedY - 0.23, seatPos.z);
                        if (level.m_7967_(display)) {
                            player.m_7998_(display, true);
                        }
                    }
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Nullable
    public static Direction guessBlockFacing(BlockState blockState, @Nullable Player player) {
        if (blockState.m_204336_(BlockTags.BEDS)) {
            return null;
        } else {
            KBlockSettings settings = KBlockSettings.of(blockState.m_60734_());
            if (settings != null) {
                for (KBlockComponent component : settings.components.values()) {
                    Direction facing = component.getHorizontalFacing(blockState);
                    if (facing != null) {
                        return facing;
                    }
                }
            }
            boolean oppose = false;
            try {
                String shape = KBlockUtils.getValueString(blockState, "shape");
                if (!"straight".equals(shape)) {
                    return null;
                }
                oppose = blockState.m_60734_() instanceof StairBlock;
            } catch (Throwable var6) {
            }
            if (blockState.m_61138_(BlockStateProperties.HORIZONTAL_FACING)) {
                Direction direction = (Direction) blockState.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
                return oppose ? direction.getOpposite() : direction;
            } else {
                if (player != null && blockState.m_61138_(BlockStateProperties.HORIZONTAL_AXIS)) {
                    Direction.Axis axis = (Direction.Axis) blockState.m_61143_(BlockStateProperties.HORIZONTAL_AXIS);
                    Direction direction = player.m_6350_();
                    if (axis.test(direction)) {
                        return direction;
                    }
                }
                return null;
            }
        }
    }

    public static boolean isSeatEntity(@Nullable Entity entity) {
        if (entity != null && entity.getType() == EntityType.BLOCK_DISPLAY) {
            Component customName = entity.getCustomName();
            return customName != null && Objects.equals(customName.getString(), ENTITY_NAME.getString());
        } else {
            return false;
        }
    }

    public static void clampRotation(Player player, Entity seat) {
        if (seat.isNoGravity()) {
            float seatRot = Mth.wrapDegrees(seat.getYRot());
            float f = Mth.wrapDegrees(player.m_146908_() - seatRot);
            float f1 = Mth.clamp(f, -105.0F, 105.0F);
            player.f_19859_ += f1 - f;
            player.m_146922_(player.m_146908_() + f1 - f);
            player.m_5616_(player.m_146908_());
            player.m_5618_(seatRot);
            f = Mth.wrapDegrees(player.m_146909_());
            f1 = Math.max(f, -45.0F);
            player.m_146926_(f1);
        }
    }

    public static Vec3 dismount(Entity display, LivingEntity passenger) {
        Direction direction;
        if (display.isNoGravity()) {
            direction = display.getDirection();
        } else {
            direction = passenger.m_6350_();
        }
        BlockPos pos = BlockPos.containing(display.getX(), display.getY() + 0.23, display.getZ());
        Optional<Vec3> vec3 = BedBlock.findStandUpPosition(passenger.m_6095_(), passenger.m_9236_(), pos, direction, passenger.m_146908_());
        return (Vec3) vec3.orElseGet(() -> Vec3.atBottomCenterOf(pos.above()));
    }
}