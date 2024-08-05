package se.mickelus.tetra.blocks.salvage;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ParametersAreNonnullByDefault
public class InteractiveBlockOverlay {

    private static boolean isDirty = false;

    private final Minecraft mc;

    private final InteractiveBlockOverlayGui gui = new InteractiveBlockOverlayGui();

    private BlockPos previousPos;

    private Direction previousFace;

    private BlockState previousState;

    public InteractiveBlockOverlay() {
        this.mc = Minecraft.getInstance();
    }

    public static void markDirty() {
        isDirty = true;
    }

    @SubscribeEvent
    public void renderOverlay(RenderHighlightEvent.Block event) {
        if (event.getTarget().getType().equals(HitResult.Type.BLOCK)) {
            BlockHitResult rayTrace = event.getTarget();
            Level world = Minecraft.getInstance().level;
            VoxelShape shape = world.getBlockState(rayTrace.getBlockPos()).m_60651_(Minecraft.getInstance().level, rayTrace.getBlockPos(), CollisionContext.of(this.mc.player));
            BlockPos blockPos = rayTrace.getBlockPos();
            Direction face = rayTrace.getDirection();
            BlockState blockState = world.getBlockState(blockPos);
            if (!shape.isEmpty()) {
                if (isDirty || !blockState.equals(this.previousState) || !blockPos.equals(this.previousPos) || !face.equals(this.previousFace)) {
                    this.gui.update(world, blockPos, blockState, face, Minecraft.getInstance().player, blockPos.equals(this.previousPos) && face.equals(this.previousFace));
                    this.previousPos = blockPos;
                    this.previousFace = face;
                    this.previousState = blockState;
                    isDirty = false;
                }
                GuiGraphics graphics = new GuiGraphics(this.mc, event.getPoseStack(), this.mc.renderBuffers().bufferSource());
                this.gui.draw(graphics, event.getCamera().getPosition(), rayTrace, shape);
            }
        }
    }
}