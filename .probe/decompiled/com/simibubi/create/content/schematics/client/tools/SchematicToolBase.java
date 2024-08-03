package com.simibubi.create.content.schematics.client.tools;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllKeys;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.schematics.client.SchematicHandler;
import com.simibubi.create.content.schematics.client.SchematicTransformation;
import com.simibubi.create.foundation.outliner.AABBOutline;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.RaycastHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public abstract class SchematicToolBase implements ISchematicTool {

    protected SchematicHandler schematicHandler;

    protected BlockPos selectedPos;

    protected Vec3 chasingSelectedPos;

    protected Vec3 lastChasingSelectedPos;

    protected boolean selectIgnoreBlocks;

    protected int selectionRange;

    protected boolean schematicSelected;

    protected boolean renderSelectedFace;

    protected Direction selectedFace;

    protected final List<String> mirrors = Arrays.asList("none", "leftRight", "frontBack");

    protected final List<String> rotations = Arrays.asList("none", "cw90", "cw180", "cw270");

    @Override
    public void init() {
        this.schematicHandler = CreateClient.SCHEMATIC_HANDLER;
        this.selectedPos = null;
        this.selectedFace = null;
        this.schematicSelected = false;
        this.chasingSelectedPos = Vec3.ZERO;
        this.lastChasingSelectedPos = Vec3.ZERO;
    }

    @Override
    public void updateSelection() {
        this.updateTargetPos();
        if (this.selectedPos != null) {
            this.lastChasingSelectedPos = this.chasingSelectedPos;
            Vec3 target = Vec3.atLowerCornerOf(this.selectedPos);
            if (target.distanceTo(this.chasingSelectedPos) < 0.001953125) {
                this.chasingSelectedPos = target;
            } else {
                this.chasingSelectedPos = this.chasingSelectedPos.add(target.subtract(this.chasingSelectedPos).scale(0.5));
            }
        }
    }

    public void updateTargetPos() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (this.schematicHandler.isDeployed()) {
            SchematicTransformation transformation = this.schematicHandler.getTransformation();
            AABB localBounds = this.schematicHandler.getBounds();
            Vec3 traceOrigin = RaycastHelper.getTraceOrigin(player);
            Vec3 start = transformation.toLocalSpace(traceOrigin);
            Vec3 end = transformation.toLocalSpace(RaycastHelper.getTraceTarget(player, 70.0, traceOrigin));
            RaycastHelper.PredicateTraceResult result = RaycastHelper.rayTraceUntil(start, end, pos -> localBounds.contains(VecHelper.getCenterOf(pos)));
            this.schematicSelected = !result.missed();
            this.selectedFace = this.schematicSelected ? result.getFacing() : null;
        }
        boolean snap = this.selectedPos == null;
        if (this.selectIgnoreBlocks) {
            float pt = AnimationTickHolder.getPartialTicks();
            this.selectedPos = BlockPos.containing(player.m_20299_(pt).add(player.m_20154_().scale((double) this.selectionRange)));
            if (snap) {
                this.lastChasingSelectedPos = this.chasingSelectedPos = Vec3.atLowerCornerOf(this.selectedPos);
            }
        } else {
            this.selectedPos = null;
            BlockHitResult trace = RaycastHelper.rayTraceRange(player.m_9236_(), player, 75.0);
            if (trace != null && trace.getType() == HitResult.Type.BLOCK) {
                BlockPos hit = BlockPos.containing(trace.m_82450_());
                boolean replaceable = player.m_9236_().getBlockState(hit).m_247087_();
                if (trace.getDirection().getAxis().isVertical() && !replaceable) {
                    hit = hit.relative(trace.getDirection());
                }
                this.selectedPos = hit;
                if (snap) {
                    this.lastChasingSelectedPos = this.chasingSelectedPos = Vec3.atLowerCornerOf(this.selectedPos);
                }
            }
        }
    }

    @Override
    public void renderTool(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 camera) {
    }

    @Override
    public void renderOverlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
    }

    @Override
    public void renderOnSchematic(PoseStack ms, SuperRenderTypeBuffer buffer) {
        if (this.schematicHandler.isDeployed()) {
            ms.pushPose();
            AABBOutline outline = this.schematicHandler.getOutline();
            if (this.renderSelectedFace) {
                outline.getParams().highlightFace(this.selectedFace).withFaceTextures(AllSpecialTextures.CHECKERED, AllKeys.ctrlDown() ? AllSpecialTextures.HIGHLIGHT_CHECKERED : AllSpecialTextures.CHECKERED);
            }
            outline.getParams().colored(6850245).withFaceTexture(AllSpecialTextures.CHECKERED).lineWidth(0.0625F);
            outline.render(ms, buffer, Vec3.ZERO, AnimationTickHolder.getPartialTicks());
            outline.getParams().clearTextures();
            ms.popPose();
        }
    }
}