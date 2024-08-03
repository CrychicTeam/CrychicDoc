package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructCommandTileEntityInteract;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskItemStackParameter;
import com.mna.blocks.tileentities.wizard_lab.MagiciansWorkbenchTile;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructCraft extends ConstructCommandTileEntityInteract<MagiciansWorkbenchTile, ConstructCraft> {

    private static final int INTERACT_TIME = 20;

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.CARRY, ConstructCapability.CRAFT };

    private int interactTimer = 20;

    private ItemStack output = ItemStack.EMPTY;

    public ConstructCraft(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon, MagiciansWorkbenchTile.class);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return !super.canUse() ? false : this.isFullyConfigured();
    }

    @Override
    public void tick() {
        super.m_8037_();
        this.setMoveTarget(this.blockPos);
        if (this.doMove()) {
            if (this.interactTimer > 0) {
                this.interactTimer--;
            } else {
                ItemStack output = this.performCraft();
                if (!output.isEmpty()) {
                    this.insertOrDropItem(output);
                    this.exitCode = 0;
                } else {
                    this.exitCode = 1;
                }
                this.interactTimer = 20;
                this.construct.asEntity().m_6674_(Math.random() < 0.5 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
            }
        }
    }

    private ItemStack performCraft() {
        MagiciansWorkbenchTile te = this.getTileEntity();
        if (te == null) {
            return ItemStack.EMPTY;
        } else {
            MagiciansWorkbenchTile.AutoCraftResult result = te.craftFromRemembered(this.output);
            if (result == MagiciansWorkbenchTile.AutoCraftResult.NO_RECIPE) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.craft_no_recipe", new Object[] { this.translate(this.output) }), true);
                return ItemStack.EMPTY;
            } else if (result == MagiciansWorkbenchTile.AutoCraftResult.MISSING_ITEMS) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.craft_missing_items", new Object[] { this.translate(this.output) }), true);
                return ItemStack.EMPTY;
            } else {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.craft_success", new Object[] { this.translate(this.output) }), true);
                return this.output.copy();
            }
        }
    }

    @Override
    public void start() {
        super.start();
        this.interactTimer = 20;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.CRAFT);
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }

    public ConstructCraft copyFrom(ConstructAITask<?> other) {
        super.copyFrom(other);
        if (other instanceof ConstructCraft) {
            this.output = ((ConstructCraft) other).output;
        }
        return this;
    }

    public ConstructCraft duplicate() {
        return new ConstructCraft(this.construct, this.guiIcon).copyFrom(this);
    }

    @Override
    public void readNBT(CompoundTag nbt) {
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        return nbt;
    }

    @Override
    public void inflateParameters() {
        super.inflateParameters();
        this.getParameter("craft.stack").ifPresent(param -> {
            if (param instanceof ConstructTaskItemStackParameter) {
                this.output = ((ConstructTaskItemStackParameter) param).getStack();
            }
        });
    }

    @Override
    public List<ConstructAITaskParameter> instantiateParameters() {
        super.instantiateParameters();
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskItemStackParameter("craft.stack"));
        return parameters;
    }

    @Override
    protected String getPointIdentifier() {
        return "gui.mna.parameter.craft.point";
    }

    @Override
    public boolean isFullyConfigured() {
        return this.blockPos != null && !this.output.isEmpty();
    }

    @Override
    public int getRequiredIntelligence() {
        return 16;
    }
}