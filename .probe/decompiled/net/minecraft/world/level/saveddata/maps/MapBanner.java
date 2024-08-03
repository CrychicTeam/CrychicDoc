package net.minecraft.world.level.saveddata.maps;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

public class MapBanner {

    private final BlockPos pos;

    private final DyeColor color;

    @Nullable
    private final Component name;

    public MapBanner(BlockPos blockPos0, DyeColor dyeColor1, @Nullable Component component2) {
        this.pos = blockPos0;
        this.color = dyeColor1;
        this.name = component2;
    }

    public static MapBanner load(CompoundTag compoundTag0) {
        BlockPos $$1 = NbtUtils.readBlockPos(compoundTag0.getCompound("Pos"));
        DyeColor $$2 = DyeColor.byName(compoundTag0.getString("Color"), DyeColor.WHITE);
        Component $$3 = compoundTag0.contains("Name") ? Component.Serializer.fromJson(compoundTag0.getString("Name")) : null;
        return new MapBanner($$1, $$2, $$3);
    }

    @Nullable
    public static MapBanner fromWorld(BlockGetter blockGetter0, BlockPos blockPos1) {
        if (blockGetter0.getBlockEntity(blockPos1) instanceof BannerBlockEntity $$3) {
            DyeColor $$4 = $$3.getBaseColor();
            Component $$5 = $$3.m_8077_() ? $$3.getCustomName() : null;
            return new MapBanner(blockPos1, $$4, $$5);
        } else {
            return null;
        }
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public DyeColor getColor() {
        return this.color;
    }

    public MapDecoration.Type getDecoration() {
        switch(this.color) {
            case WHITE:
                return MapDecoration.Type.BANNER_WHITE;
            case ORANGE:
                return MapDecoration.Type.BANNER_ORANGE;
            case MAGENTA:
                return MapDecoration.Type.BANNER_MAGENTA;
            case LIGHT_BLUE:
                return MapDecoration.Type.BANNER_LIGHT_BLUE;
            case YELLOW:
                return MapDecoration.Type.BANNER_YELLOW;
            case LIME:
                return MapDecoration.Type.BANNER_LIME;
            case PINK:
                return MapDecoration.Type.BANNER_PINK;
            case GRAY:
                return MapDecoration.Type.BANNER_GRAY;
            case LIGHT_GRAY:
                return MapDecoration.Type.BANNER_LIGHT_GRAY;
            case CYAN:
                return MapDecoration.Type.BANNER_CYAN;
            case PURPLE:
                return MapDecoration.Type.BANNER_PURPLE;
            case BLUE:
                return MapDecoration.Type.BANNER_BLUE;
            case BROWN:
                return MapDecoration.Type.BANNER_BROWN;
            case GREEN:
                return MapDecoration.Type.BANNER_GREEN;
            case RED:
                return MapDecoration.Type.BANNER_RED;
            case BLACK:
            default:
                return MapDecoration.Type.BANNER_BLACK;
        }
    }

    @Nullable
    public Component getName() {
        return this.name;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (object0 != null && this.getClass() == object0.getClass()) {
            MapBanner $$1 = (MapBanner) object0;
            return Objects.equals(this.pos, $$1.pos) && this.color == $$1.color && Objects.equals(this.name, $$1.name);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.pos, this.color, this.name });
    }

    public CompoundTag save() {
        CompoundTag $$0 = new CompoundTag();
        $$0.put("Pos", NbtUtils.writeBlockPos(this.pos));
        $$0.putString("Color", this.color.getName());
        if (this.name != null) {
            $$0.putString("Name", Component.Serializer.toJson(this.name));
        }
        return $$0;
    }

    public String getId() {
        return "banner-" + this.pos.m_123341_() + "," + this.pos.m_123342_() + "," + this.pos.m_123343_();
    }
}