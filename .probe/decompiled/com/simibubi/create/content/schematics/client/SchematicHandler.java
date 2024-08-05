package com.simibubi.create.content.schematics.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllKeys;
import com.simibubi.create.AllPackets;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.schematics.SchematicInstances;
import com.simibubi.create.content.schematics.SchematicItem;
import com.simibubi.create.content.schematics.SchematicWorld;
import com.simibubi.create.content.schematics.client.tools.ToolType;
import com.simibubi.create.content.schematics.packet.SchematicPlacePacket;
import com.simibubi.create.content.schematics.packet.SchematicSyncPacket;
import com.simibubi.create.foundation.outliner.AABBOutline;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.List;
import java.util.Vector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class SchematicHandler implements IGuiOverlay {

    private String displayedSchematic;

    private SchematicTransformation transformation;

    private AABB bounds;

    private boolean deployed;

    private boolean active;

    private ToolType currentTool;

    private static final int SYNC_DELAY = 10;

    private int syncCooldown;

    private int activeHotbarSlot;

    private ItemStack activeSchematicItem;

    private AABBOutline outline;

    private Vector<SchematicRenderer> renderers = new Vector(3);

    private SchematicHotbarSlotOverlay overlay;

    private ToolSelectionScreen selectionScreen;

    public SchematicHandler() {
        for (int i = 0; i < this.renderers.capacity(); i++) {
            this.renderers.add(new SchematicRenderer());
        }
        this.overlay = new SchematicHotbarSlotOverlay();
        this.currentTool = ToolType.DEPLOY;
        this.selectionScreen = new ToolSelectionScreen(ImmutableList.of(ToolType.DEPLOY), this::equip);
        this.transformation = new SchematicTransformation();
    }

    public void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gameMode.getPlayerMode() == GameType.SPECTATOR) {
            if (this.active) {
                this.active = false;
                this.syncCooldown = 0;
                this.activeHotbarSlot = 0;
                this.activeSchematicItem = null;
                this.renderers.forEach(r -> r.setActive(false));
            }
        } else {
            if (this.activeSchematicItem != null && this.transformation != null) {
                this.transformation.tick();
            }
            LocalPlayer player = mc.player;
            ItemStack stack = this.findBlueprintInHand(player);
            if (stack == null) {
                this.active = false;
                this.syncCooldown = 0;
                if (this.activeSchematicItem != null && this.itemLost(player)) {
                    this.activeHotbarSlot = 0;
                    this.activeSchematicItem = null;
                    this.renderers.forEach(r -> r.setActive(false));
                }
            } else {
                if (!this.active || !stack.getTag().getString("File").equals(this.displayedSchematic)) {
                    this.init(player, stack);
                }
                if (this.active) {
                    this.renderers.forEach(SchematicRenderer::tick);
                    if (this.syncCooldown > 0) {
                        this.syncCooldown--;
                    }
                    if (this.syncCooldown == 1) {
                        this.sync();
                    }
                    this.selectionScreen.update();
                    this.currentTool.getTool().updateSelection();
                }
            }
        }
    }

    private void init(LocalPlayer player, ItemStack stack) {
        this.loadSettings(stack);
        this.displayedSchematic = stack.getTag().getString("File");
        this.active = true;
        if (this.deployed) {
            this.setupRenderer();
            ToolType toolBefore = this.currentTool;
            this.selectionScreen = new ToolSelectionScreen(ToolType.getTools(player.m_7500_()), this::equip);
            if (toolBefore != null) {
                this.selectionScreen.setSelectedElement(toolBefore);
                this.equip(toolBefore);
            }
        } else {
            this.selectionScreen = new ToolSelectionScreen(ImmutableList.of(ToolType.DEPLOY), this::equip);
        }
    }

    private void setupRenderer() {
        Level clientWorld = Minecraft.getInstance().level;
        StructureTemplate schematic = SchematicItem.loadSchematic(clientWorld.m_246945_(Registries.BLOCK), this.activeSchematicItem);
        Vec3i size = schematic.getSize();
        if (!size.equals(Vec3i.ZERO)) {
            SchematicWorld w = new SchematicWorld(clientWorld);
            SchematicWorld wMirroredFB = new SchematicWorld(clientWorld);
            SchematicWorld wMirroredLR = new SchematicWorld(clientWorld);
            StructurePlaceSettings placementSettings = new StructurePlaceSettings();
            BlockPos pos = BlockPos.ZERO;
            try {
                schematic.placeInWorld(w, pos, pos, placementSettings, w.m_213780_(), 2);
            } catch (Exception var12) {
                Minecraft.getInstance().player.displayClientMessage(Lang.translate("schematic.error").component(), false);
                Create.LOGGER.error("Failed to load Schematic for Previewing", var12);
                return;
            }
            placementSettings.setMirror(Mirror.FRONT_BACK);
            pos = BlockPos.ZERO.east(size.getX() - 1);
            schematic.placeInWorld(wMirroredFB, pos, pos, placementSettings, wMirroredFB.m_213780_(), 2);
            StructureTransform transform = new StructureTransform(placementSettings.getRotationPivot(), Direction.Axis.Y, Rotation.NONE, placementSettings.getMirror());
            for (BlockEntity be : wMirroredFB.getRenderedBlockEntities()) {
                transform.apply(be);
            }
            placementSettings.setMirror(Mirror.LEFT_RIGHT);
            pos = BlockPos.ZERO.south(size.getZ() - 1);
            schematic.placeInWorld(wMirroredLR, pos, pos, placementSettings, wMirroredFB.m_213780_(), 2);
            transform = new StructureTransform(placementSettings.getRotationPivot(), Direction.Axis.Y, Rotation.NONE, placementSettings.getMirror());
            for (BlockEntity be : wMirroredLR.getRenderedBlockEntities()) {
                transform.apply(be);
            }
            ((SchematicRenderer) this.renderers.get(0)).display(w);
            ((SchematicRenderer) this.renderers.get(1)).display(wMirroredFB);
            ((SchematicRenderer) this.renderers.get(2)).display(wMirroredLR);
        }
    }

    public void render(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 camera) {
        boolean present = this.activeSchematicItem != null;
        if (this.active || present) {
            if (this.active) {
                ms.pushPose();
                this.currentTool.getTool().renderTool(ms, buffer, camera);
                ms.popPose();
            }
            ms.pushPose();
            this.transformation.applyTransformations(ms, camera);
            if (!this.renderers.isEmpty()) {
                float pt = AnimationTickHolder.getPartialTicks();
                boolean lr = this.transformation.getScaleLR().getValue(pt) < 0.0F;
                boolean fb = this.transformation.getScaleFB().getValue(pt) < 0.0F;
                if (lr && !fb) {
                    ((SchematicRenderer) this.renderers.get(2)).render(ms, buffer);
                } else if (fb && !lr) {
                    ((SchematicRenderer) this.renderers.get(1)).render(ms, buffer);
                } else {
                    ((SchematicRenderer) this.renderers.get(0)).render(ms, buffer);
                }
            }
            if (this.active) {
                this.currentTool.getTool().renderOnSchematic(ms, buffer);
            }
            ms.popPose();
        }
    }

    public void updateRenderers() {
        for (SchematicRenderer renderer : this.renderers) {
            renderer.update();
        }
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
        if (!Minecraft.getInstance().options.hideGui && this.active) {
            if (this.activeSchematicItem != null) {
                this.overlay.renderOn(graphics, this.activeHotbarSlot);
            }
            this.currentTool.getTool().renderOverlay(gui, graphics, partialTicks, width, height);
            this.selectionScreen.renderPassive(graphics, partialTicks);
        }
    }

    public boolean onMouseInput(int button, boolean pressed) {
        if (!this.active) {
            return false;
        } else if (pressed && button == 1) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player.isShiftKeyDown()) {
                return false;
            } else {
                if (mc.hitResult instanceof BlockHitResult blockRayTraceResult) {
                    BlockState clickedBlock = mc.level.m_8055_(blockRayTraceResult.getBlockPos());
                    if (AllBlocks.SCHEMATICANNON.has(clickedBlock)) {
                        return false;
                    }
                    if (AllBlocks.DEPLOYER.has(clickedBlock)) {
                        return false;
                    }
                }
                return this.currentTool.getTool().handleRightClick();
            }
        } else {
            return false;
        }
    }

    public void onKeyInput(int key, boolean pressed) {
        if (this.active) {
            if (key == AllKeys.TOOL_MENU.getBoundCode()) {
                if (pressed && !this.selectionScreen.focused) {
                    this.selectionScreen.focused = true;
                }
                if (!pressed && this.selectionScreen.focused) {
                    this.selectionScreen.focused = false;
                    this.selectionScreen.onClose();
                }
            }
        }
    }

    public boolean mouseScrolled(double delta) {
        if (!this.active) {
            return false;
        } else if (this.selectionScreen.focused) {
            this.selectionScreen.cycle((int) delta);
            return true;
        } else {
            return AllKeys.ctrlDown() ? this.currentTool.getTool().handleMouseWheel(delta) : false;
        }
    }

    private ItemStack findBlueprintInHand(Player player) {
        ItemStack stack = player.m_21205_();
        if (!AllItems.SCHEMATIC.isIn(stack)) {
            return null;
        } else if (!stack.hasTag()) {
            return null;
        } else {
            this.activeSchematicItem = stack;
            this.activeHotbarSlot = player.getInventory().selected;
            return stack;
        }
    }

    private boolean itemLost(Player player) {
        for (int i = 0; i < Inventory.getSelectionSize(); i++) {
            if (ItemStack.matches(player.getInventory().getItem(i), this.activeSchematicItem)) {
                return false;
            }
        }
        return true;
    }

    public void markDirty() {
        this.syncCooldown = 10;
    }

    public void sync() {
        if (this.activeSchematicItem != null) {
            AllPackets.getChannel().sendToServer(new SchematicSyncPacket(this.activeHotbarSlot, this.transformation.toSettings(), this.transformation.getAnchor(), this.deployed));
        }
    }

    public void equip(ToolType tool) {
        this.currentTool = tool;
        this.currentTool.getTool().init();
    }

    public void loadSettings(ItemStack blueprint) {
        CompoundTag tag = blueprint.getTag();
        BlockPos anchor = BlockPos.ZERO;
        StructurePlaceSettings settings = SchematicItem.getSettings(blueprint);
        this.transformation = new SchematicTransformation();
        this.deployed = tag.getBoolean("Deployed");
        if (this.deployed) {
            anchor = NbtUtils.readBlockPos(tag.getCompound("Anchor"));
        }
        Vec3i size = NBTHelper.readVec3i(tag.getList("Bounds", 3));
        this.bounds = new AABB(0.0, 0.0, 0.0, (double) size.getX(), (double) size.getY(), (double) size.getZ());
        this.outline = new AABBOutline(this.bounds);
        this.outline.getParams().colored(6850245).lineWidth(0.0625F);
        this.transformation.init(anchor, settings, this.bounds);
    }

    public void deploy() {
        if (!this.deployed) {
            List<ToolType> tools = ToolType.getTools(Minecraft.getInstance().player.m_7500_());
            this.selectionScreen = new ToolSelectionScreen(tools, this::equip);
        }
        this.deployed = true;
        this.setupRenderer();
    }

    public String getCurrentSchematicName() {
        return this.displayedSchematic != null ? this.displayedSchematic : "-";
    }

    public void printInstantly() {
        AllPackets.getChannel().sendToServer(new SchematicPlacePacket(this.activeSchematicItem.copy()));
        CompoundTag nbt = this.activeSchematicItem.getTag();
        nbt.putBoolean("Deployed", false);
        this.activeSchematicItem.setTag(nbt);
        SchematicInstances.clearHash(this.activeSchematicItem);
        this.renderers.forEach(r -> r.setActive(false));
        this.active = false;
        this.markDirty();
    }

    public boolean isActive() {
        return this.active;
    }

    public AABB getBounds() {
        return this.bounds;
    }

    public SchematicTransformation getTransformation() {
        return this.transformation;
    }

    public boolean isDeployed() {
        return this.deployed;
    }

    public ItemStack getActiveSchematicItem() {
        return this.activeSchematicItem;
    }

    public AABBOutline getOutline() {
        return this.outline;
    }
}