package com.simibubi.create.content.equipment.zapper.terrainzapper;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class WorldshaperRenderHandler {

    private static Supplier<Collection<BlockPos>> renderedPositions;

    public static void tick() {
        gatherSelectedBlocks();
        if (renderedPositions != null) {
            CreateClient.OUTLINER.showCluster("terrainZapper", (Iterable<BlockPos>) renderedPositions.get()).colored(12566463).disableLineNormals().lineWidth(0.03125F).withFaceTexture(AllSpecialTextures.CHECKERED);
        }
    }

    protected static void gatherSelectedBlocks() {
        LocalPlayer player = Minecraft.getInstance().player;
        ItemStack heldMain = player.m_21205_();
        ItemStack heldOff = player.m_21206_();
        boolean zapperInMain = AllItems.WORLDSHAPER.isIn(heldMain);
        boolean zapperInOff = AllItems.WORLDSHAPER.isIn(heldOff);
        if (zapperInMain) {
            CompoundTag tag = heldMain.getOrCreateTag();
            if (!tag.contains("_Swap") || !zapperInOff) {
                createBrushOutline(tag, player, heldMain);
                return;
            }
        }
        if (zapperInOff) {
            CompoundTag tag = heldOff.getOrCreateTag();
            createBrushOutline(tag, player, heldOff);
        } else {
            renderedPositions = null;
        }
    }

    public static void createBrushOutline(CompoundTag tag, LocalPlayer player, ItemStack zapper) {
        if (!tag.contains("BrushParams")) {
            renderedPositions = null;
        } else {
            Brush brush = ((TerrainBrushes) NBTHelper.readEnum(tag, "Brush", TerrainBrushes.class)).get();
            PlacementOptions placement = NBTHelper.readEnum(tag, "Placement", PlacementOptions.class);
            TerrainTools tool = NBTHelper.readEnum(tag, "Tool", TerrainTools.class);
            BlockPos params = NbtUtils.readBlockPos(tag.getCompound("BrushParams"));
            brush.set(params.m_123341_(), params.m_123342_(), params.m_123343_());
            Vec3 start = player.m_20182_().add(0.0, (double) player.m_20192_(), 0.0);
            Vec3 range = player.m_20154_().scale(128.0);
            BlockHitResult raytrace = player.m_9236_().m_45547_(new ClipContext(start, start.add(range), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
            if (raytrace != null && raytrace.getType() != HitResult.Type.MISS) {
                BlockPos pos = raytrace.getBlockPos().offset(brush.getOffset(player.m_20154_(), raytrace.getDirection(), placement));
                renderedPositions = () -> brush.addToGlobalPositions(player.m_9236_(), pos, raytrace.getDirection(), new ArrayList(), tool);
            } else {
                renderedPositions = null;
            }
        }
    }
}