package com.simibubi.create.content.trains.station;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.map.CustomRenderedMapDecoration;
import com.simibubi.create.foundation.utility.Components;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.joml.Matrix4f;

public class StationMarker {

    public static final MapDecoration.Type TYPE = MapDecoration.Type.RED_MARKER;

    private final BlockPos source;

    private final BlockPos target;

    private final Component name;

    private final String id;

    public StationMarker(BlockPos source, BlockPos target, Component name) {
        this.source = source;
        this.target = target;
        this.name = name;
        this.id = "create:station-" + target.m_123341_() + "," + target.m_123342_() + "," + target.m_123343_();
    }

    public static StationMarker load(CompoundTag tag) {
        BlockPos source = NbtUtils.readBlockPos(tag.getCompound("source"));
        BlockPos target = NbtUtils.readBlockPos(tag.getCompound("target"));
        Component name = Component.Serializer.fromJson(tag.getString("name"));
        if (name == null) {
            name = Components.immutableEmpty();
        }
        return new StationMarker(source, target, name);
    }

    public static StationMarker fromWorld(BlockGetter level, BlockPos pos) {
        Optional<StationBlockEntity> stationOption = AllBlockEntityTypes.TRACK_STATION.get(level, pos);
        if (!stationOption.isEmpty() && ((StationBlockEntity) stationOption.get()).getStation() != null) {
            String name = ((StationBlockEntity) stationOption.get()).getStation().name;
            return new StationMarker(pos, BlockEntityBehaviour.get((BlockEntity) stationOption.get(), TrackTargetingBehaviour.TYPE).getPositionForMapMarker(), Components.literal(name));
        } else {
            return null;
        }
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.put("source", NbtUtils.writeBlockPos(this.source));
        tag.put("target", NbtUtils.writeBlockPos(this.target));
        tag.putString("name", Component.Serializer.toJson(this.name));
        return tag;
    }

    public BlockPos getSource() {
        return this.source;
    }

    public BlockPos getTarget() {
        return this.target;
    }

    public Component getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            StationMarker that = (StationMarker) o;
            return !this.target.equals(that.target) ? false : this.name.equals(that.name);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.target, this.name });
    }

    public static class Decoration extends MapDecoration implements CustomRenderedMapDecoration {

        private static final ResourceLocation TEXTURE = Create.asResource("textures/gui/station_map_icon.png");

        public Decoration(byte x, byte y, Component name) {
            super(StationMarker.TYPE, x, y, (byte) 0, name);
        }

        public static StationMarker.Decoration from(MapDecoration decoration) {
            return new StationMarker.Decoration(decoration.getX(), decoration.getY(), decoration.getName());
        }

        @Override
        public boolean renderOnFrame() {
            return true;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource bufferSource, boolean active, int packedLight, MapItemSavedData mapData, int index) {
            poseStack.pushPose();
            poseStack.translate((double) this.m_77804_() / 2.0 + 64.0, (double) this.m_77805_() / 2.0 + 64.0, -0.02);
            poseStack.pushPose();
            poseStack.translate(0.5F, 0.0F, 0.0F);
            poseStack.scale(4.5F, 4.5F, 3.0F);
            VertexConsumer buffer = bufferSource.getBuffer(RenderType.text(TEXTURE));
            Matrix4f mat = poseStack.last().pose();
            float zOffset = -0.001F;
            buffer.vertex(mat, -1.0F, -1.0F, zOffset * (float) index).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(packedLight).endVertex();
            buffer.vertex(mat, -1.0F, 1.0F, zOffset * (float) index).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(packedLight).endVertex();
            buffer.vertex(mat, 1.0F, 1.0F, zOffset * (float) index).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(packedLight).endVertex();
            buffer.vertex(mat, 1.0F, -1.0F, zOffset * (float) index).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(packedLight).endVertex();
            poseStack.popPose();
            if (this.m_77810_() != null) {
                Font font = Minecraft.getInstance().font;
                Component component = this.m_77810_();
                float f6 = (float) font.width(component);
                poseStack.pushPose();
                poseStack.translate(0.0, 6.0, -0.005F);
                poseStack.scale(0.8F, 0.8F, 1.0F);
                poseStack.translate(-f6 / 2.0F + 0.5F, 0.0F, 0.0F);
                font.drawInBatch(component, 0.0F, 0.0F, -1, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, Integer.MIN_VALUE, packedLight);
                poseStack.popPose();
            }
            poseStack.popPose();
        }

        public boolean render(int index) {
            return true;
        }
    }
}