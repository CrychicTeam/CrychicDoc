package fr.frinn.custommachinery.impl.component.config;

import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.impl.codec.EnumCodec;
import java.util.Locale;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public enum RelativeSide {

    TOP,
    BOTTOM,
    FRONT,
    RIGHT,
    BACK,
    LEFT;

    public static final NamedCodec<RelativeSide> CODEC = EnumCodec.of(RelativeSide.class);

    public Component getTranslationName() {
        return Component.translatable("custommachinery.side." + this.name().toLowerCase(Locale.ROOT));
    }

    public Direction getDirection(@NotNull Direction facing) {
        return switch(this) {
            case FRONT ->
                facing;
            case BACK ->
                facing.getOpposite();
            case LEFT ->
                facing != Direction.DOWN && facing != Direction.UP ? facing.getClockWise() : Direction.EAST;
            case RIGHT ->
                facing != Direction.DOWN && facing != Direction.UP ? facing.getCounterClockWise() : Direction.WEST;
            case TOP ->
                {
                    switch(facing) {
                        case DOWN:
                            ???;
                        case UP:
                            ???;
                        default:
                            ???;
                    }
                }
            case BOTTOM ->
                {
                    switch(facing) {
                        case DOWN:
                            ???;
                        case UP:
                            ???;
                        default:
                            ???;
                    }
                }
        };
    }

    public static RelativeSide fromDirections(@NotNull Direction facing, @NotNull Direction side) {
        if (side == facing) {
            return FRONT;
        } else if (side == facing.getOpposite()) {
            return BACK;
        } else if (facing == Direction.DOWN || facing == Direction.UP) {
            return switch(side) {
                case NORTH ->
                    facing == Direction.DOWN ? TOP : BOTTOM;
                case SOUTH ->
                    facing == Direction.DOWN ? BOTTOM : TOP;
                case WEST ->
                    RIGHT;
                case EAST ->
                    LEFT;
                default ->
                    throw new IllegalStateException("Case should have been caught earlier.");
            };
        } else if (side == Direction.DOWN) {
            return BOTTOM;
        } else if (side == Direction.UP) {
            return TOP;
        } else if (side == facing.getCounterClockWise()) {
            return RIGHT;
        } else {
            return side == facing.getClockWise() ? LEFT : FRONT;
        }
    }
}