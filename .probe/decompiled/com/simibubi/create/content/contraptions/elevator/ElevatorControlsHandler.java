package com.simibubi.create.content.contraptions.elevator;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionHandler;
import com.simibubi.create.content.contraptions.ContraptionHandlerClient;
import com.simibubi.create.content.contraptions.actors.contraptionControls.ContraptionControlsBlockEntity;
import com.simibubi.create.content.contraptions.actors.contraptionControls.ContraptionControlsMovement;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.utility.Couple;
import java.lang.ref.WeakReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.MutablePair;

public class ElevatorControlsHandler {

    private static ContraptionControlsBlockEntity.ControlsSlot slot = new ElevatorControlsHandler.ElevatorControlsSlot();

    @OnlyIn(Dist.CLIENT)
    public static boolean onScroll(double delta) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return false;
        } else if (player.m_5833_()) {
            return false;
        } else if (mc.level == null) {
            return false;
        } else {
            Couple<Vec3> rayInputs = ContraptionHandlerClient.getRayInputs(player);
            Vec3 origin = rayInputs.getFirst();
            Vec3 target = rayInputs.getSecond();
            AABB aabb = new AABB(origin, target).inflate(16.0);
            for (WeakReference<AbstractContraptionEntity> ref : ContraptionHandler.loadedContraptions.get(mc.level).values()) {
                AbstractContraptionEntity contraptionEntity = (AbstractContraptionEntity) ref.get();
                if (contraptionEntity != null) {
                    Contraption contraption = contraptionEntity.getContraption();
                    if (contraption instanceof ElevatorContraption) {
                        ElevatorContraption ec = (ElevatorContraption) contraption;
                        if (contraptionEntity.m_20191_().intersects(aabb)) {
                            BlockHitResult rayTraceResult = ContraptionHandlerClient.rayTraceContraption(origin, target, contraptionEntity);
                            if (rayTraceResult != null) {
                                BlockPos pos = rayTraceResult.getBlockPos();
                                StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(pos);
                                if (info != null && AllBlocks.CONTRAPTION_CONTROLS.has(info.state()) && slot.testHit(info.state(), rayTraceResult.m_82450_().subtract(Vec3.atLowerCornerOf(pos)))) {
                                    MovementContext ctx = null;
                                    for (MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> pair : contraption.getActors()) {
                                        if (info.equals(pair.left)) {
                                            ctx = (MovementContext) pair.right;
                                            break;
                                        }
                                    }
                                    if (!(ctx.temporaryData instanceof ContraptionControlsMovement.ElevatorFloorSelection)) {
                                        ctx.temporaryData = new ContraptionControlsMovement.ElevatorFloorSelection();
                                    }
                                    ContraptionControlsMovement.ElevatorFloorSelection efs = (ContraptionControlsMovement.ElevatorFloorSelection) ctx.temporaryData;
                                    int prev = efs.currentIndex;
                                    efs.currentIndex = (int) ((double) efs.currentIndex + delta);
                                    ContraptionControlsMovement.tickFloorSelection(efs, ec);
                                    if (prev != efs.currentIndex && !ec.namesList.isEmpty()) {
                                        float pitch = (float) efs.currentIndex / (float) ec.namesList.size();
                                        pitch = Mth.lerp(pitch, 1.0F, 1.5F);
                                        AllSoundEvents.SCROLL_VALUE.play(mc.player.m_9236_(), mc.player, BlockPos.containing(contraptionEntity.toGlobalVector(rayTraceResult.m_82450_(), 1.0F)), 1.0F, pitch);
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    private static class ElevatorControlsSlot extends ContraptionControlsBlockEntity.ControlsSlot {

        @Override
        public boolean testHit(BlockState state, Vec3 localHit) {
            Vec3 offset = this.getLocalOffset(state);
            return offset == null ? false : localHit.distanceTo(offset) < (double) this.scale * 0.85;
        }
    }
}