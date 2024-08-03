package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructCommandTileEntityInteract;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskItemStackParameter;
import com.mna.api.sound.SFX;
import com.mna.blocks.tileentities.wizard_lab.RunescribingTableTile;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.items.ItemInit;
import com.mna.recipes.runeforging.RunescribingRecipe;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructRunescribe extends ConstructCommandTileEntityInteract<BlockEntity, ConstructRunescribe> {

    private static final String CAP_ID_RECIPE = "runescribe.recipe";

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.SMITH, ConstructCapability.CARRY };

    private int interactTimer = this.getInteractTime(ConstructCapability.SMITH);

    private RunescribingRecipe _cached_target_recipe;

    private ItemStack recipeStack = ItemStack.EMPTY;

    public ConstructRunescribe(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon, BlockEntity.class);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.getTileEntity() instanceof RunescribingTableTile && ((RunescribingTableTile) this.getTileEntity()).m_8020_(2).getItem() == ItemInit.RUNE_PATTERN.get();
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.getTileEntity() != null && this.getTileEntity() instanceof RunescribingTableTile) {
            if (this.doMove()) {
                if (this.interactTimer > 0) {
                    this.interactTimer--;
                } else {
                    this.interactTimer = this.getInteractTime(ConstructCapability.SMITH);
                    this.preInteract();
                    this.scribeNext();
                }
            }
        } else {
            if (!this.isSuccess()) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.runescribe_no_recipe", new Object[0]));
            }
        }
    }

    private void scribeNext() {
        RunescribingTableTile te = (RunescribingTableTile) this.getTileEntity();
        if (te.m_8020_(2).isEmpty()) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.runescribe_no_item", new Object[0]));
            this.exitCode = 1;
        } else if (te.m_8020_(2).getItem() != ItemInit.RUNE_PATTERN.get()) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.runescribe_invalid_item", new Object[0]));
            this.exitCode = 1;
        } else {
            if (this._cached_target_recipe == null) {
                this._cached_target_recipe = te.getRecipeFromGuideSlot();
                if (this._cached_target_recipe == null) {
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.runescribe_recipe_not_found", new Object[0]));
                    this.exitCode = 1;
                    return;
                }
            }
            Player player = this.createFakePlayer();
            long current_h_mutex = te.getHMutex();
            long current_v_mutex = te.getVMutex();
            AbstractGolem c = this.getConstructAsEntity();
            RandomSource source = c.m_9236_().getRandom();
            this.construct.getHandWithCapability(ConstructCapability.SMITH).ifPresent(h -> c.m_6674_(h));
            long target_h_mutex = this._cached_target_recipe.getHMutex();
            if (current_h_mutex != target_h_mutex) {
                for (int i = 0; i < 64; i++) {
                    if (this.isBitSet(target_h_mutex, i) && !this.isBitSet(current_h_mutex, i)) {
                        current_h_mutex |= 1L << i;
                        c.m_9236_().playSound(null, c.m_20185_(), c.m_20186_(), c.m_20189_(), SFX.Gui.CHISEL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        if (te.writeMutexChanges(current_h_mutex, current_v_mutex, player, -1, false, source)) {
                            this.exitCode = 0;
                            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.runescribe_success", new Object[0]));
                        }
                        return;
                    }
                }
            }
            long target_v_mutex = this._cached_target_recipe.getVMutex();
            if (current_v_mutex != target_v_mutex) {
                for (int ix = 0; ix < 64; ix++) {
                    if (this.isBitSet(target_v_mutex, ix) && !this.isBitSet(current_v_mutex, ix)) {
                        current_v_mutex |= 1L << ix;
                        c.m_9236_().playSound(null, c.m_20185_(), c.m_20186_(), c.m_20189_(), SFX.Gui.CHISEL, SoundSource.BLOCKS, 1.0F, 1.0F);
                        if (te.writeMutexChanges(current_h_mutex, current_v_mutex, player, -1, false, source)) {
                            this.exitCode = 0;
                            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.runescribe_success", new Object[0]));
                        }
                        return;
                    }
                }
            }
            te.writeMutexChanges(target_h_mutex, target_v_mutex, player, -1, false, source);
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.runescribe_success", new Object[0]));
            this.exitCode = 0;
        }
    }

    private boolean isBitSet(long value, int bitIndex) {
        return (value & 1L << bitIndex) != 0L;
    }

    @Override
    public void start() {
        super.start();
        this.interactTimer = this.getInteractTime(ConstructCapability.SMITH);
        if (!this.recipeStack.isEmpty()) {
            this._cached_target_recipe = ItemInit.RECIPE_SCRAP_RUNESCRIBING.get().getRecipe(this.recipeStack, this.construct.asEntity().m_9236_());
        }
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.RUNESCRIBE);
    }

    public ConstructRunescribe copyFrom(ConstructAITask<?> other) {
        super.copyFrom(other);
        if (other instanceof ConstructRunescribe) {
            this.recipeStack = ((ConstructRunescribe) other).recipeStack;
        }
        return this;
    }

    public ConstructRunescribe duplicate() {
        return new ConstructRunescribe(this.construct, this.guiIcon).copyFrom(this);
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        super.readNBT(nbt);
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        return super.writeInternal(nbt);
    }

    @Override
    protected String getPointIdentifier() {
        return "runescribe.point";
    }

    @Override
    protected List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskItemStackParameter("runescribe.recipe"));
        return parameters;
    }

    @Override
    public void inflateParameters() {
        super.inflateParameters();
        this.getParameter("runescribe.recipe").ifPresent(param -> {
            if (param instanceof ConstructTaskItemStackParameter) {
                this.recipeStack = ((ConstructTaskItemStackParameter) param).getStack();
            }
        });
    }
}