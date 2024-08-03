package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructCommandTileEntityInteract;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskBooleanParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskFilterParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskIntegerParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskItemStackParameter;
import com.mna.api.items.DynamicItemFilter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.tools.InventoryUtilities;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructTakeItem extends ConstructCommandTileEntityInteract<BlockEntity, ConstructTakeItem> {

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.CARRY };

    private DynamicItemFilter filter;

    private boolean randomItemFromFilter = false;

    private int minimumQuantityToTake = 0;

    private int interactTimer = 20;

    public ConstructTakeItem(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon, BlockEntity.class);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.filter = new DynamicItemFilter();
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public void tick() {
        super.m_8037_();
        AbstractGolem c = this.getConstructAsEntity();
        if (this.interactTimer < 0) {
            this.interactTimer--;
            if (this.interactTimer <= -16) {
                this.forceFail();
            }
        } else if (!InventoryUtilities.hasEmptySlot(this.construct) && this.construct.getEmptyHands().length == 0) {
            this.forceFail();
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.take_hands_full", new Object[] { this.translate(this.getTileEntity()) }));
        } else {
            if (this.getTileEntity() != null) {
                LazyOptional<IItemHandler> handler = this.getTileEntity().getCapability(ForgeCapabilities.ITEM_HANDLER, this.side);
                if (handler.isPresent()) {
                    if (this.doMove()) {
                        if (this.interactTimer > 0) {
                            if (this.interactTimer == 5 && !InventoryUtilities.getFirstItemFromContainer(this.filter, this.construct.getCarrySize(), (IItemHandler) handler.resolve().get(), this.side, this.randomItemFromFilter, true).isEmpty()) {
                                this.construct.getHandWithCapability(ConstructCapability.CARRY).ifPresent(h -> c.m_6674_(h));
                            }
                            this.interactTimer--;
                        } else if (this.interactTimer == 0) {
                            this.preInteract();
                            ItemStack toTake = ItemStack.EMPTY;
                            boolean didTake = false;
                            if (!this.randomItemFromFilter && this.construct.getIntelligence() > 8) {
                                DynamicItemFilter tempFilter = new DynamicItemFilter();
                                for (ItemStack filterStack : this.filter.getWhiteList()) {
                                    tempFilter.setWhitelist(NonNullList.of(ItemStack.EMPTY, filterStack), this.filter.getWhitelistMatchDurability(), this.filter.getWhitelistMatchTag());
                                    toTake = InventoryUtilities.getFirstItemFromContainer(tempFilter, this.construct.getCarrySize(), (IItemHandler) handler.resolve().get(), this.side, false, true);
                                    if (this.take(toTake, (IItemHandler) handler.resolve().get(), c)) {
                                        didTake = true;
                                        break;
                                    }
                                }
                            } else {
                                toTake = InventoryUtilities.getFirstItemFromContainer(this.filter, this.construct.getCarrySize(), (IItemHandler) handler.resolve().get(), this.side, this.randomItemFromFilter, true);
                                didTake = this.take(toTake, (IItemHandler) handler.resolve().get(), c);
                            }
                            if (didTake) {
                                this.setSuccessCode();
                            } else {
                                this.forceFail();
                            }
                            this.interactTimer = -1;
                        }
                    }
                } else {
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.inv_missing_cap", new Object[] { this.translate(this.getTileEntity()) }));
                }
            } else {
                this.exitCode = 1;
                c.f_21345_.removeGoal(this);
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.te_missing", new Object[0]));
            }
        }
    }

    private boolean take(ItemStack toTake, IItemHandler handler, AbstractGolem c) {
        if (!toTake.isEmpty()) {
            int count = InventoryUtilities.countItem(toTake, handler, this.side, this.filter.getWhitelistMatchDurability(), this.filter.getWhitelistMatchTag());
            if (count > this.minimumQuantityToTake) {
                ItemStack stack = InventoryUtilities.getFirstItemFromContainer(this.filter, this.construct.getCarrySize(), handler, this.side, this.randomItemFromFilter, true);
                InteractionHand[] emptyHands = this.construct.getEmptyHands();
                if (emptyHands.length != 0) {
                    stack = InventoryUtilities.getFirstItemFromContainer(this.filter, this.construct.getCarrySize(), handler, this.side, this.randomItemFromFilter, false);
                    c.m_21008_(emptyHands[0], stack);
                } else {
                    if (!InventoryUtilities.hasRoomFor(this.construct, stack)) {
                        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.take_fail", new Object[] { this.translate(this.getTileEntity()) }));
                        return false;
                    }
                    stack = InventoryUtilities.getFirstItemFromContainer(this.filter, this.construct.getCarrySize(), handler, this.side, this.randomItemFromFilter, false);
                    InventoryUtilities.mergeIntoInventory(this.construct, stack);
                }
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.take_success", new Object[] { this.translate(toTake), this.translate(this.getTileEntity()) }));
                return true;
            } else {
                return false;
            }
        } else {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.take_fail", new Object[] { this.translate(this.getTileEntity()) }));
            return false;
        }
    }

    @Override
    public void start() {
        super.start();
        this.interactTimer = 20;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.TAKE);
    }

    public ConstructTakeItem duplicate() {
        return new ConstructTakeItem(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructTakeItem copyFrom(ConstructAITask<?> other) {
        super.copyFrom(other);
        if (other instanceof ConstructTakeItem) {
            this.filter.copyFrom(((ConstructTakeItem) other).filter);
            this.randomItemFromFilter = ((ConstructTakeItem) other).randomItemFromFilter;
            this.minimumQuantityToTake = ((ConstructTakeItem) other).minimumQuantityToTake;
        }
        return this;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
        super.readNBT(nbt);
        if (nbt.contains("filter")) {
            this.filter.loadFromTag(nbt.getCompound("filter"));
        }
        if (nbt.contains("random")) {
            this.randomItemFromFilter = nbt.getBoolean("random");
        }
        if (nbt.contains("minimum")) {
            this.minimumQuantityToTake = nbt.getInt("minimum");
        }
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        nbt = super.writeInternal(nbt);
        nbt.put("filter", this.filter.getTag());
        nbt.putBoolean("random", this.randomItemFromFilter);
        nbt.putInt("minimum", this.minimumQuantityToTake);
        return nbt;
    }

    @Override
    public void inflateParameters() {
        super.inflateParameters();
        this.getParameter("take.filter").ifPresent(param -> {
            if (param instanceof ConstructTaskFilterParameter) {
                this.filter.copyFrom(((ConstructTaskFilterParameter) param).getValue());
            }
        });
        this.getParameter("take.stack").ifPresent(param -> {
            if (param instanceof ConstructTaskItemStackParameter) {
                ItemStack paramStack = ((ConstructTaskItemStackParameter) param).getStack();
                if (!paramStack.isEmpty()) {
                    this.filter.clear();
                    this.filter.getWhiteList().add(paramStack);
                }
            }
        });
        this.getParameter("take.random").ifPresent(param -> {
            if (param instanceof ConstructTaskBooleanParameter) {
                this.randomItemFromFilter = ((ConstructTaskBooleanParameter) param).getValue();
            }
        });
        this.getParameter("take.quantity").ifPresent(param -> {
            if (param instanceof ConstructTaskIntegerParameter) {
                this.minimumQuantityToTake = ((ConstructTaskIntegerParameter) param).getValue();
            }
        });
    }

    @Override
    public List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskFilterParameter("take.filter"));
        parameters.add(new ConstructTaskItemStackParameter("take.stack"));
        parameters.add(new ConstructTaskBooleanParameter("take.random"));
        parameters.add(new ConstructTaskIntegerParameter("take.quantity", 0, 128, 1, 0));
        return parameters;
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }
}