package com.simibubi.create.content.redstone.nixieTube;

import com.simibubi.create.content.redstone.displayLink.DisplayLinkBlock;
import com.simibubi.create.content.trains.signal.SignalBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.DynamicComponent;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class NixieTubeBlockEntity extends SmartBlockEntity {

    private static final Couple<String> EMPTY = Couple.create("", "");

    private int redstoneStrength;

    private Optional<DynamicComponent> customText = Optional.empty();

    private int nixieIndex;

    private Couple<String> displayedStrings;

    private WeakReference<SignalBlockEntity> cachedSignalTE;

    public SignalBlockEntity.SignalState signalState;

    public NixieTubeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.redstoneStrength = 0;
        this.cachedSignalTE = new WeakReference(null);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_58857_.isClientSide) {
            this.signalState = null;
            SignalBlockEntity signalBlockEntity = (SignalBlockEntity) this.cachedSignalTE.get();
            if (signalBlockEntity != null && !signalBlockEntity.m_58901_()) {
                this.signalState = signalBlockEntity.getState();
            } else {
                Direction facing = NixieTubeBlock.getFacing(this.m_58900_());
                if (this.f_58857_.getBlockEntity(this.f_58858_.relative(facing.getOpposite())) instanceof SignalBlockEntity signal) {
                    this.signalState = signal.getState();
                    this.cachedSignalTE = new WeakReference(signal);
                }
            }
        }
    }

    @Override
    public void initialize() {
        if (this.f_58857_.isClientSide) {
            this.updateDisplayedStrings();
        }
    }

    public boolean reactsToRedstone() {
        return this.customText.isEmpty();
    }

    public Couple<String> getDisplayedStrings() {
        return this.displayedStrings == null ? EMPTY : this.displayedStrings;
    }

    public MutableComponent getFullText() {
        return (MutableComponent) this.customText.map(DynamicComponent::get).orElse(Components.literal(this.redstoneStrength + ""));
    }

    public void updateRedstoneStrength(int signalStrength) {
        this.clearCustomText();
        this.redstoneStrength = signalStrength;
        DisplayLinkBlock.notifyGatherers(this.f_58857_, this.f_58858_);
        this.notifyUpdate();
    }

    public void displayCustomText(String tagElement, int nixiePositionInRow) {
        if (tagElement != null) {
            if (!this.customText.filter(d -> d.sameAs(tagElement)).isPresent()) {
                DynamicComponent component = (DynamicComponent) this.customText.orElseGet(DynamicComponent::new);
                component.displayCustomText(this.f_58857_, this.f_58858_, tagElement);
                this.customText = Optional.of(component);
                this.nixieIndex = nixiePositionInRow;
                DisplayLinkBlock.notifyGatherers(this.f_58857_, this.f_58858_);
                this.notifyUpdate();
            }
        }
    }

    public void updateDisplayedStrings() {
        if (this.signalState == null) {
            this.customText.map(DynamicComponent::resolve).ifPresentOrElse(fullText -> this.displayedStrings = Couple.create(this.charOrEmpty(fullText, this.nixieIndex * 2), this.charOrEmpty(fullText, this.nixieIndex * 2 + 1)), () -> this.displayedStrings = Couple.create(this.redstoneStrength < 10 ? "0" : "1", String.valueOf(this.redstoneStrength % 10)));
        }
    }

    public void clearCustomText() {
        this.nixieIndex = 0;
        this.customText = Optional.empty();
    }

    public int getRedstoneStrength() {
        return this.redstoneStrength;
    }

    @Override
    protected void read(CompoundTag nbt, boolean clientPacket) {
        super.read(nbt, clientPacket);
        if (nbt.contains("CustomText")) {
            DynamicComponent component = (DynamicComponent) this.customText.orElseGet(DynamicComponent::new);
            component.read(this.f_58857_, this.f_58858_, nbt);
            if (component.isValid()) {
                this.customText = Optional.of(component);
                this.nixieIndex = nbt.getInt("CustomTextIndex");
            } else {
                this.customText = Optional.empty();
                this.nixieIndex = 0;
            }
        }
        if (this.customText.isEmpty()) {
            this.redstoneStrength = nbt.getInt("RedstoneStrength");
        }
        if (clientPacket) {
            this.updateDisplayedStrings();
        }
    }

    @Override
    protected void write(CompoundTag nbt, boolean clientPacket) {
        super.write(nbt, clientPacket);
        if (this.customText.isPresent()) {
            nbt.putInt("CustomTextIndex", this.nixieIndex);
            ((DynamicComponent) this.customText.get()).write(nbt);
        } else {
            nbt.putInt("RedstoneStrength", this.redstoneStrength);
        }
    }

    private String charOrEmpty(String string, int index) {
        return string.length() <= index ? " " : string.substring(index, index + 1);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }
}