package com.simibubi.create.content.equipment.zapper.terrainzapper;

import com.simibubi.create.content.equipment.zapper.ConfigureZapperPacket;
import com.simibubi.create.content.equipment.zapper.PlacementPatterns;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class ConfigureWorldshaperPacket extends ConfigureZapperPacket {

    protected TerrainBrushes brush;

    protected int brushParamX;

    protected int brushParamY;

    protected int brushParamZ;

    protected TerrainTools tool;

    protected PlacementOptions placement;

    public ConfigureWorldshaperPacket(InteractionHand hand, PlacementPatterns pattern, TerrainBrushes brush, int brushParamX, int brushParamY, int brushParamZ, TerrainTools tool, PlacementOptions placement) {
        super(hand, pattern);
        this.brush = brush;
        this.brushParamX = brushParamX;
        this.brushParamY = brushParamY;
        this.brushParamZ = brushParamZ;
        this.tool = tool;
        this.placement = placement;
    }

    public ConfigureWorldshaperPacket(FriendlyByteBuf buffer) {
        super(buffer);
        this.brush = buffer.readEnum(TerrainBrushes.class);
        this.brushParamX = buffer.readVarInt();
        this.brushParamY = buffer.readVarInt();
        this.brushParamZ = buffer.readVarInt();
        this.tool = buffer.readEnum(TerrainTools.class);
        this.placement = buffer.readEnum(PlacementOptions.class);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        super.write(buffer);
        buffer.writeEnum(this.brush);
        buffer.writeVarInt(this.brushParamX);
        buffer.writeVarInt(this.brushParamY);
        buffer.writeVarInt(this.brushParamZ);
        buffer.writeEnum(this.tool);
        buffer.writeEnum(this.placement);
    }

    @Override
    public void configureZapper(ItemStack stack) {
        WorldshaperItem.configureSettings(stack, this.pattern, this.brush, this.brushParamX, this.brushParamY, this.brushParamZ, this.tool, this.placement);
    }
}