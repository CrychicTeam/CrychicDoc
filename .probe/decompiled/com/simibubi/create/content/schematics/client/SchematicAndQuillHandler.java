package com.simibubi.create.content.schematics.client;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllKeys;
import com.simibubi.create.AllPackets;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.Create;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.schematics.SchematicExport;
import com.simibubi.create.content.schematics.packet.InstantSchematicPacket;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.outliner.Outliner;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.RaycastHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SchematicAndQuillHandler {

    private Object outlineSlot = new Object();

    public BlockPos firstPos;

    public BlockPos secondPos;

    private BlockPos selectedPos;

    private Direction selectedFace;

    private int range = 10;

    public boolean mouseScrolled(double delta) {
        if (!this.isActive()) {
            return false;
        } else if (!AllKeys.ctrlDown()) {
            return false;
        } else {
            if (this.secondPos == null) {
                this.range = (int) Mth.clamp((double) this.range + delta, 1.0, 100.0);
            }
            if (this.selectedFace == null) {
                return true;
            } else {
                AABB bb = new AABB(this.firstPos, this.secondPos);
                Vec3i vec = this.selectedFace.getNormal();
                Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
                if (bb.contains(projectedView)) {
                    delta *= -1.0;
                }
                int x = (int) ((double) vec.getX() * delta);
                int y = (int) ((double) vec.getY() * delta);
                int z = (int) ((double) vec.getZ() * delta);
                Direction.AxisDirection axisDirection = this.selectedFace.getAxisDirection();
                if (axisDirection == Direction.AxisDirection.NEGATIVE) {
                    bb = bb.move((double) (-x), (double) (-y), (double) (-z));
                }
                double maxX = Math.max(bb.maxX - (double) (x * axisDirection.getStep()), bb.minX);
                double maxY = Math.max(bb.maxY - (double) (y * axisDirection.getStep()), bb.minY);
                double maxZ = Math.max(bb.maxZ - (double) (z * axisDirection.getStep()), bb.minZ);
                bb = new AABB(bb.minX, bb.minY, bb.minZ, maxX, maxY, maxZ);
                this.firstPos = BlockPos.containing(bb.minX, bb.minY, bb.minZ);
                this.secondPos = BlockPos.containing(bb.maxX, bb.maxY, bb.maxZ);
                LocalPlayer player = Minecraft.getInstance().player;
                Lang.translate("schematicAndQuill.dimensions", (int) bb.getXsize() + 1, (int) bb.getYsize() + 1, (int) bb.getZsize() + 1).sendStatus(player);
                return true;
            }
        }
    }

    public boolean onMouseInput(int button, boolean pressed) {
        if (!pressed || button != 1) {
            return false;
        } else if (!this.isActive()) {
            return false;
        } else {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player.isShiftKeyDown()) {
                this.discard();
                return true;
            } else if (this.secondPos != null) {
                ScreenOpener.open(new SchematicPromptScreen());
                return true;
            } else if (this.selectedPos == null) {
                Lang.translate("schematicAndQuill.noTarget").sendStatus(player);
                return true;
            } else if (this.firstPos != null) {
                this.secondPos = this.selectedPos;
                Lang.translate("schematicAndQuill.secondPos").sendStatus(player);
                return true;
            } else {
                this.firstPos = this.selectedPos;
                Lang.translate("schematicAndQuill.firstPos").sendStatus(player);
                return true;
            }
        }
    }

    public void discard() {
        LocalPlayer player = Minecraft.getInstance().player;
        this.firstPos = null;
        this.secondPos = null;
        Lang.translate("schematicAndQuill.abort").sendStatus(player);
    }

    public void tick() {
        if (this.isActive()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (AllKeys.ACTIVATE_TOOL.isPressed()) {
                float pt = AnimationTickHolder.getPartialTicks();
                Vec3 targetVec = player.m_20299_(pt).add(player.m_20154_().scale((double) this.range));
                this.selectedPos = BlockPos.containing(targetVec);
            } else {
                BlockHitResult trace = RaycastHelper.rayTraceRange(player.m_9236_(), player, 75.0);
                if (trace != null && trace.getType() == HitResult.Type.BLOCK) {
                    BlockPos hit = trace.getBlockPos();
                    boolean replaceable = player.m_9236_().getBlockState(hit).m_60629_(new BlockPlaceContext(new UseOnContext(player, InteractionHand.MAIN_HAND, trace)));
                    if (trace.getDirection().getAxis().isVertical() && !replaceable) {
                        hit = hit.relative(trace.getDirection());
                    }
                    this.selectedPos = hit;
                } else {
                    this.selectedPos = null;
                }
            }
            this.selectedFace = null;
            if (this.secondPos != null) {
                AABB bb = new AABB(this.firstPos, this.secondPos).expandTowards(1.0, 1.0, 1.0).inflate(0.45F);
                Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
                boolean inside = bb.contains(projectedView);
                RaycastHelper.PredicateTraceResult result = RaycastHelper.rayTraceUntil(player, 70.0, pos -> inside ^ bb.contains(VecHelper.getCenterOf(pos)));
                this.selectedFace = result.missed() ? null : (inside ? result.getFacing().getOpposite() : result.getFacing());
            }
            AABB currentSelectionBox = this.getCurrentSelectionBox();
            if (currentSelectionBox != null) {
                this.outliner().chaseAABB(this.outlineSlot, currentSelectionBox).colored(6850245).withFaceTextures(AllSpecialTextures.CHECKERED, AllSpecialTextures.HIGHLIGHT_CHECKERED).lineWidth(0.0625F).highlightFace(this.selectedFace);
            }
        }
    }

    private AABB getCurrentSelectionBox() {
        if (this.secondPos == null) {
            if (this.firstPos == null) {
                return this.selectedPos == null ? null : new AABB(this.selectedPos);
            } else {
                return this.selectedPos == null ? new AABB(this.firstPos) : new AABB(this.firstPos, this.selectedPos).expandTowards(1.0, 1.0, 1.0);
            }
        } else {
            return new AABB(this.firstPos, this.secondPos).expandTowards(1.0, 1.0, 1.0);
        }
    }

    private boolean isActive() {
        return this.isPresent() && AllItems.SCHEMATIC_AND_QUILL.isIn(Minecraft.getInstance().player.m_21205_());
    }

    private boolean isPresent() {
        return Minecraft.getInstance() != null && Minecraft.getInstance().level != null && Minecraft.getInstance().screen == null;
    }

    public void saveSchematic(String string, boolean convertImmediately) {
        SchematicExport.SchematicExportResult result = SchematicExport.saveSchematic(SchematicExport.SCHEMATICS, string, false, Minecraft.getInstance().level, this.firstPos, this.secondPos);
        LocalPlayer player = Minecraft.getInstance().player;
        if (result == null) {
            Lang.translate("schematicAndQuill.failed").style(ChatFormatting.RED).sendStatus(player);
        } else {
            Path file = result.file();
            Lang.translate("schematicAndQuill.saved", file.getFileName()).sendStatus(player);
            this.firstPos = null;
            this.secondPos = null;
            if (convertImmediately) {
                try {
                    if (!ClientSchematicLoader.validateSizeLimitation(Files.size(file))) {
                        return;
                    }
                    AllPackets.getChannel().sendToServer(new InstantSchematicPacket(result.fileName(), result.origin(), result.bounds()));
                } catch (IOException var7) {
                    Create.LOGGER.error("Error instantly uploading Schematic file: " + file, var7);
                }
            }
        }
    }

    private Outliner outliner() {
        return CreateClient.OUTLINER;
    }
}