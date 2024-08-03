package io.github.lightman314.lightmanscurrency.common.traderinterface.handlers;

import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

public abstract class ConfigurableSidedHandler<H> extends SidedHandler<H> {

    protected final ConfigurableSidedHandler.DirectionalSettings inputSides;

    protected final ConfigurableSidedHandler.DirectionalSettings outputSides;

    protected static final String UPDATE_INPUT_SIDE = "inputSide";

    protected static final String UPDATE_OUTPUT_SIDE = "outputSide";

    public ConfigurableSidedHandler.DirectionalSettings getInputSides() {
        return this.inputSides;
    }

    public ConfigurableSidedHandler.DirectionalSettings getOutputSides() {
        return this.outputSides;
    }

    protected ConfigurableSidedHandler() {
        this(ImmutableList.of());
    }

    protected ConfigurableSidedHandler(ImmutableList<Direction> ignoreSides) {
        this.inputSides = new ConfigurableSidedHandler.DirectionalSettings(ignoreSides);
        this.outputSides = new ConfigurableSidedHandler.DirectionalSettings(ignoreSides);
    }

    public void toggleInputSide(@Nonnull Direction side) {
        this.inputSides.set(side, !this.inputSides.get(side));
        this.markDirty();
        if (this.isClient()) {
            CompoundTag message = initUpdateInfo("inputSide");
            message.putInt("side", side.get3DDataValue());
            message.putBoolean("newValue", this.inputSides.get(side));
            this.sendMessage(message);
        }
    }

    public static CompoundTag initUpdateInfo(String updateType) {
        CompoundTag compound = new CompoundTag();
        compound.putString("UpdateType", updateType);
        return compound;
    }

    public static boolean isUpdateType(CompoundTag updateInfo, String updateType) {
        return updateInfo.contains("UpdateType", 8) ? updateInfo.getString("UpdateType").contentEquals(updateType) : false;
    }

    public void toggleOutputSide(Direction side) {
        this.outputSides.set(side, !this.outputSides.get(side));
        this.markDirty();
        if (this.isClient()) {
            CompoundTag message = initUpdateInfo("outputSide");
            message.putInt("side", side.get3DDataValue());
            message.putBoolean("newValue", this.outputSides.get(side));
            this.sendMessage(message);
        }
    }

    @Override
    public void receiveMessage(CompoundTag compound) {
        if (isUpdateType(compound, "inputSide")) {
            Direction side = Direction.from3DDataValue(compound.getInt("side"));
            if (compound.getBoolean("newValue") != this.inputSides.get(side)) {
                this.toggleInputSide(side);
            }
        } else if (isUpdateType(compound, "outputSide")) {
            Direction side = Direction.from3DDataValue(compound.getInt("side"));
            if (compound.getBoolean("newValue") != this.outputSides.get(side)) {
                this.toggleOutputSide(side);
            }
        }
    }

    @Override
    public final CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        compound.put("InputSides", this.inputSides.save(new CompoundTag()));
        compound.put("OutputSides", this.outputSides.save(new CompoundTag()));
        this.saveAdditional(compound);
        return compound;
    }

    protected void saveAdditional(CompoundTag compound) {
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("InputSides", 10)) {
            this.inputSides.load(compound.getCompound("InputSides"));
        }
        if (compound.contains("OutputSides", 10)) {
            this.outputSides.load(compound.getCompound("OutputSides"));
        }
    }

    public static class DirectionalSettings {

        public final ImmutableList<Direction> ignoreSides;

        private final Map<Direction, Boolean> sideValues = new HashMap();

        public DirectionalSettings() {
            this(ImmutableList.of());
        }

        public DirectionalSettings(ImmutableList<Direction> ignoreSides) {
            this.ignoreSides = ignoreSides;
        }

        public boolean allows(Direction side) {
            return !this.ignoreSides.contains(side);
        }

        public boolean get(Direction side) {
            return this.ignoreSides.contains(side) ? false : (Boolean) this.sideValues.getOrDefault(side, false);
        }

        public void set(Direction side, boolean value) {
            if (!this.ignoreSides.contains(side)) {
                this.sideValues.put(side, value);
            }
        }

        public CompoundTag save(CompoundTag compound) {
            for (Direction side : Direction.values()) {
                if (!this.ignoreSides.contains(side)) {
                    compound.putBoolean(side.toString(), this.get(side));
                }
            }
            return compound;
        }

        public void load(CompoundTag compound) {
            this.sideValues.clear();
            for (Direction side : Direction.values()) {
                if (!this.ignoreSides.contains(side) && compound.contains(side.toString())) {
                    this.set(side, compound.getBoolean(side.toString()));
                }
            }
        }
    }
}