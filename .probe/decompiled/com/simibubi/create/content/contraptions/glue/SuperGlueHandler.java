package com.simibubi.create.content.contraptions.glue;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.contraptions.BlockMovementChecks;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.worldWrappers.RayTraceWorld;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;

@EventBusSubscriber
public class SuperGlueHandler {

    @SubscribeEvent
    public static void glueListensForBlockPlacement(BlockEvent.EntityPlaceEvent event) {
        LevelAccessor world = event.getLevel();
        Entity entity = event.getEntity();
        BlockPos pos = event.getPos();
        if (entity != null && world != null && pos != null) {
            if (!world.m_5776_()) {
                Set<SuperGlueEntity> cached = new HashSet();
                for (Direction direction : Iterate.directions) {
                    BlockPos relative = pos.relative(direction);
                    if (SuperGlueEntity.isGlued(world, pos, direction, cached) && BlockMovementChecks.isMovementNecessary(world.m_8055_(relative), entity.level(), relative)) {
                        AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new GlueEffectPacket(pos, direction, true));
                    }
                }
                if (entity instanceof Player) {
                    glueInOffHandAppliesOnBlockPlace(event, pos, (Player) entity);
                }
            }
        }
    }

    public static void glueInOffHandAppliesOnBlockPlace(BlockEvent.EntityPlaceEvent event, BlockPos pos, Player placer) {
        ItemStack itemstack = placer.m_21206_();
        AttributeInstance reachAttribute = placer.m_21051_(ForgeMod.BLOCK_REACH.get());
        if (AllItems.SUPER_GLUE.isIn(itemstack) && reachAttribute != null) {
            if (!AllItems.WRENCH.isIn(placer.m_21205_())) {
                if (event.getPlacedAgainst() != IPlacementHelper.ID) {
                    double distance = reachAttribute.getValue();
                    Vec3 start = placer.m_20299_(1.0F);
                    Vec3 look = placer.m_20252_(1.0F);
                    Vec3 end = start.add(look.x * distance, look.y * distance, look.z * distance);
                    Level world = placer.m_9236_();
                    RayTraceWorld rayTraceWorld = new RayTraceWorld(world, (p, state) -> p.equals(pos) ? Blocks.AIR.defaultBlockState() : state);
                    BlockHitResult ray = rayTraceWorld.m_45547_(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, placer));
                    Direction face = ray.getDirection();
                    if (face != null && ray.getType() != HitResult.Type.MISS) {
                        BlockPos gluePos = ray.getBlockPos();
                        if (!gluePos.relative(face).equals(pos)) {
                            event.setCanceled(true);
                        } else if (!SuperGlueEntity.isGlued(world, gluePos, face, null)) {
                            SuperGlueEntity entity = new SuperGlueEntity(world, SuperGlueEntity.span(gluePos, gluePos.relative(face)));
                            CompoundTag compoundnbt = itemstack.getTag();
                            if (compoundnbt != null) {
                                EntityType.updateCustomEntityTag(world, placer, entity, compoundnbt);
                            }
                            if (SuperGlueEntity.isValidFace(world, gluePos, face)) {
                                if (!world.isClientSide) {
                                    world.m_7967_(entity);
                                    AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new GlueEffectPacket(gluePos, face, true));
                                }
                                itemstack.hurtAndBreak(1, placer, SuperGlueItem::onBroken);
                            }
                        }
                    }
                }
            }
        }
    }
}