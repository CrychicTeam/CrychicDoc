package com.simibubi.create.content.contraptions;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.contraptions.sync.ContraptionInteractionPacket;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.TrainRelocator;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.RaycastHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.lang.ref.WeakReference;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.commons.lang3.mutable.MutableObject;

@EventBusSubscriber
public class ContraptionHandlerClient {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void preventRemotePlayersWalkingAnimations(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            if (event.player instanceof RemotePlayer remotePlayer) {
                CompoundTag data = remotePlayer.getPersistentData();
                if (data.contains("LastOverrideLimbSwingUpdate")) {
                    int lastOverride = data.getInt("LastOverrideLimbSwingUpdate");
                    data.putInt("LastOverrideLimbSwingUpdate", lastOverride + 1);
                    if (lastOverride > 5) {
                        data.remove("LastOverrideLimbSwingUpdate");
                        data.remove("OverrideLimbSwing");
                    } else {
                        float limbSwing = data.getFloat("OverrideLimbSwing");
                        remotePlayer.f_19854_ = remotePlayer.m_20185_() - (double) (limbSwing / 4.0F);
                        remotePlayer.f_19856_ = remotePlayer.m_20189_();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void rightClickingOnContraptionsGetsHandledLocally(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null) {
            if (!player.m_5833_()) {
                if (mc.level != null) {
                    if (event.isUseItem()) {
                        Couple<Vec3> rayInputs = getRayInputs(player);
                        Vec3 origin = rayInputs.getFirst();
                        Vec3 target = rayInputs.getSecond();
                        AABB aabb = new AABB(origin, target).inflate(16.0);
                        for (WeakReference<AbstractContraptionEntity> ref : ContraptionHandler.loadedContraptions.get(mc.level).values()) {
                            AbstractContraptionEntity contraptionEntity = (AbstractContraptionEntity) ref.get();
                            if (contraptionEntity != null && contraptionEntity.m_20191_().intersects(aabb)) {
                                BlockHitResult rayTraceResult = rayTraceContraption(origin, target, contraptionEntity);
                                if (rayTraceResult != null) {
                                    InteractionHand hand = event.getHand();
                                    Direction face = rayTraceResult.getDirection();
                                    BlockPos pos = rayTraceResult.getBlockPos();
                                    if (contraptionEntity.handlePlayerInteraction(player, pos, face, hand)) {
                                        AllPackets.getChannel().sendToServer(new ContraptionInteractionPacket(contraptionEntity, hand, pos, face));
                                    } else if (!handleSpecialInteractions(contraptionEntity, player, pos, face, hand)) {
                                        continue;
                                    }
                                    event.setCanceled(true);
                                    event.setSwingHand(false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean handleSpecialInteractions(AbstractContraptionEntity contraptionEntity, Player player, BlockPos localPos, Direction side, InteractionHand interactionHand) {
        return AllItems.WRENCH.isIn(player.m_21120_(interactionHand)) && contraptionEntity instanceof CarriageContraptionEntity car ? TrainRelocator.carriageWrenched(car.toGlobalVector(VecHelper.getCenterOf(localPos), 1.0F), car) : false;
    }

    @OnlyIn(Dist.CLIENT)
    public static Couple<Vec3> getRayInputs(LocalPlayer player) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 origin = RaycastHelper.getTraceOrigin(player);
        double reach = (double) mc.gameMode.getPickRange();
        if (mc.hitResult != null && mc.hitResult.getLocation() != null) {
            reach = Math.min(mc.hitResult.getLocation().distanceTo(origin), reach);
        }
        Vec3 target = RaycastHelper.getTraceTarget(player, reach, origin);
        return Couple.create(origin, target);
    }

    @Nullable
    public static BlockHitResult rayTraceContraption(Vec3 origin, Vec3 target, AbstractContraptionEntity contraptionEntity) {
        Vec3 localOrigin = contraptionEntity.toLocalVector(origin, 1.0F);
        Vec3 localTarget = contraptionEntity.toLocalVector(target, 1.0F);
        Contraption contraption = contraptionEntity.getContraption();
        MutableObject<BlockHitResult> mutableResult = new MutableObject();
        RaycastHelper.PredicateTraceResult predicateResult = RaycastHelper.rayTraceUntil(localOrigin, localTarget, p -> {
            for (Direction d : Iterate.directions) {
                if (d != Direction.UP) {
                    BlockPos pos = d == Direction.DOWN ? p : p.relative(d);
                    StructureTemplate.StructureBlockInfo blockInfo = (StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(pos);
                    if (blockInfo != null) {
                        BlockState state = blockInfo.state();
                        VoxelShape raytraceShape = state.m_60808_(contraption.getContraptionWorld(), BlockPos.ZERO.below());
                        if (!raytraceShape.isEmpty() && !contraption.isHiddenInPortal(pos)) {
                            BlockHitResult rayTrace = raytraceShape.clip(localOrigin, localTarget, pos);
                            if (rayTrace != null) {
                                mutableResult.setValue(rayTrace);
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        });
        return predicateResult != null && !predicateResult.missed() ? (BlockHitResult) mutableResult.getValue() : null;
    }
}