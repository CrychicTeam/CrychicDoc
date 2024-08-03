package com.mna.blocks.tileentities.wizard_lab;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.tile.IEldrinConsumerTile;
import com.mna.api.events.GenericProgressionEvent;
import com.mna.api.events.ProgressionEventIDs;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.items.ItemInit;
import com.mna.spells.crafting.SpellRecipe;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public class AffinityTinkerTile extends WizardLabTile implements IEldrinConsumerTile {

    public static final int SLOT_SPELL = 0;

    public static final int SLOT_OUTPUT = 1;

    public static final int INVENTORY_SIZE = 2;

    private float ticksRequired = 100.0F;

    private float powerPerTick = 1.0F;

    private float powerAccumulation = 0.0F;

    private Affinity selectedAffinity = Affinity.UNKNOWN;

    private WizardLabTile.PowerStatus powerConsumeStatus = WizardLabTile.PowerStatus.NO_CONDUIT;

    public AffinityTinkerTile(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state, inventorySize);
    }

    public AffinityTinkerTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.AFFINITY_TINKER.get(), pos, state, 2);
    }

    @Override
    public boolean canActivate(Player player) {
        return this.selectedAffinity == Affinity.UNKNOWN ? false : this.canContinue();
    }

    @Override
    protected boolean canContinue() {
        return this.selectedAffinity != null && this.selectedAffinity != Affinity.UNKNOWN && this.hasStack(0) && !this.hasStack(1);
    }

    @Override
    public float getPctComplete() {
        return (float) this.getActiveTicks() / this.ticksRequired;
    }

    @Override
    protected List<Integer> getSyncedInventorySlots() {
        return Arrays.asList(0, 1);
    }

    @Override
    protected boolean canActiveTick() {
        float amount = this.consume(this.getCrafter(), this.m_58899_(), Vec3.atCenterOf(this.m_58899_()), this.selectedAffinity.getShiftAffinity(), this.powerPerTick);
        this.powerConsumeStatus = this.fromPowerCode(this.powerPerTick, amount);
        this.powerAccumulation = this.powerAccumulation + Math.max(amount, 0.0F);
        if (this.powerAccumulation < this.powerPerTick) {
            return false;
        } else {
            this.powerAccumulation = 0.0F;
            return true;
        }
    }

    @Override
    protected CompoundTag getMeta() {
        CompoundTag tag = new CompoundTag();
        this.writePowerConsumeStatus(this.powerConsumeStatus, tag);
        tag.putFloat("powerAccum", this.powerAccumulation);
        tag.putInt("affinity", this.selectedAffinity.ordinal());
        return tag;
    }

    @Override
    protected void loadMeta(CompoundTag tag) {
        this.powerConsumeStatus = this.readPowerConsumeStatus(tag);
        if (tag.contains("powerAccum")) {
            this.powerAccumulation = tag.getFloat("powerAccum");
        }
        if (tag.contains("affinity")) {
            this.selectedAffinity = Affinity.values()[tag.getInt("affinity")];
        }
    }

    public Affinity getAffinity() {
        return this.selectedAffinity;
    }

    public void setAffinity(Affinity affinity) {
        this.selectedAffinity = affinity;
    }

    @Override
    protected void onComplete() {
        ItemStack output = this.m_8020_(0);
        SpellRecipe recipe = SpellRecipe.fromNBT(output.getTag());
        if (recipe.isValid()) {
            Affinity original = recipe.getHighestAffinity();
            recipe.setOverrideAffinity(this.selectedAffinity);
            recipe.writeToNBT(output.getTag());
            this.m_6836_(0, ItemStack.EMPTY);
            this.m_6836_(1, output);
            if (this.getCrafter() != null && !this.m_58904_().isClientSide()) {
                Player crafter = this.getCrafter();
                if (crafter != null) {
                    MinecraftForge.EVENT_BUS.post(new GenericProgressionEvent(crafter, ProgressionEventIDs.SPELL_AFFINITY_TINKERED));
                    if (crafter instanceof ServerPlayer) {
                        CustomAdvancementTriggers.AFFINITY_TINKER.trigger((ServerPlayer) crafter, recipe, original, this.selectedAffinity);
                    }
                }
            }
        }
    }

    @Override
    protected void onDeactivated() {
        this.powerConsumeStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;
    }

    @Override
    public boolean clickMenuButton(Player player, int button) {
        if (!super.clickMenuButton(player, button)) {
            switch(button) {
                case 1:
                    this.selectedAffinity = Affinity.ARCANE;
                    return true;
                case 2:
                    this.selectedAffinity = Affinity.EARTH;
                    return true;
                case 3:
                    this.selectedAffinity = Affinity.ENDER;
                    return true;
                case 4:
                    this.selectedAffinity = Affinity.FIRE;
                    return true;
                case 5:
                    this.selectedAffinity = Affinity.WATER;
                    return true;
                case 6:
                    this.selectedAffinity = Affinity.WIND;
                    return true;
                case 7:
                    this.selectedAffinity = Affinity.ICE;
                    return true;
                case 8:
                    this.selectedAffinity = Affinity.LIGHTNING;
                    return true;
            }
        }
        return false;
    }

    @Override
    public int getXPCost(Player crafter) {
        return 20;
    }

    @Override
    protected void tick() {
        super.tick();
        if (this.m_58904_().isClientSide()) {
            this.spawnParticles();
        }
    }

    private void spawnParticles() {
    }

    @Override
    public HashMap<Affinity, WizardLabTile.PowerStatus> powerRequirementStatus() {
        HashMap<Affinity, WizardLabTile.PowerStatus> reqs = new HashMap();
        if (this.selectedAffinity != null) {
            reqs.put(this.selectedAffinity.getShiftAffinity(), this.isActive() ? this.powerConsumeStatus : WizardLabTile.PowerStatus.NOT_REQUESTING);
        }
        return reqs;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side != Direction.DOWN && side != Direction.UP ? new int[] { 0 } : new int[] { 1 };
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return !this.isActive();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (this.isActive()) {
            return false;
        } else {
            switch(index) {
                case 0:
                    return this.m_8020_(index).isEmpty() && stack.getItem() == ItemInit.SPELL.get();
                default:
                    return false;
            }
        }
    }
}