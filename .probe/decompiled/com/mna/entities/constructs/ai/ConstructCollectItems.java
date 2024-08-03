package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructEntityAreaTask;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskFilterParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskItemStackParameter;
import com.mna.api.items.DynamicItemFilter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.tools.InventoryUtilities;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructCollectItems extends ConstructEntityAreaTask<ItemEntity, ConstructCollectItems> {

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.CARRY };

    private int numItemsCollected = 0;

    private DynamicItemFilter filter = new DynamicItemFilter();

    private ItemStack matchStack = ItemStack.EMPTY;

    public ConstructCollectItems(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon, ItemEntity.class);
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.getSelectedTarget() == null && !this.locateTarget()) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.collect_no_items", new Object[0]), false);
            this.forceFail();
        } else {
            if (!this.getSelectedTarget().m_6084_()) {
                this.releaseMutexes();
                this.setSelectedTarget(null);
                if (!this.locateTarget()) {
                    this.forceFail();
                    return;
                }
            }
            this.setMoveTarget(this.getSelectedTarget());
            if (this.doMove()) {
                InteractionHand[] emptyHands = this.construct.getEmptyHands();
                ItemStack merge = this.getSelectedTarget().getItem().copy();
                boolean putInHand = false;
                if (emptyHands.length != 0) {
                    for (int i = 0; i < emptyHands.length; i++) {
                        ItemStack hand = merge.copy();
                        hand.setCount(Math.min(this.construct.getCarrySize(), hand.getCount()));
                        this.construct.asEntity().m_21008_(emptyHands[i], hand);
                        merge.shrink(hand.getCount());
                        putInHand = true;
                    }
                }
                if (!merge.isEmpty() && !InventoryUtilities.mergeIntoInventory(this.construct, merge)) {
                    this.getSelectedTarget().setItem(merge);
                    if (!putInHand && this.numItemsCollected == 0) {
                        this.forceFail();
                    } else {
                        this.setSuccessCode();
                    }
                    return;
                }
                this.construct.asEntity().m_21053_(this.getSelectedTarget());
                this.construct.asEntity().m_7938_(this.getSelectedTarget(), this.getSelectedTarget().getItem().getCount());
                this.getSelectedTarget().m_146870_();
                this.numItemsCollected++;
                if (!this.locateTarget()) {
                    this.setSuccessCode();
                    return;
                }
            }
        }
    }

    protected boolean entityPredicate(ItemEntity candidate) {
        boolean basePredicate = candidate.m_6084_() && (this.construct.getEmptyHands().length > 0 || InventoryUtilities.hasRoomFor(this.construct, candidate.getItem()));
        if (!basePredicate) {
            return false;
        } else {
            return !this.matchStack.isEmpty() ? ItemStack.isSameItemSameTags(this.matchStack, candidate.getItem()) : this.filter.matches(candidate.getItem());
        }
    }

    protected ItemEntity selectTarget(Collection<ItemEntity> entities) {
        AbstractGolem c = this.getConstructAsEntity();
        List<ItemEntity> items = (List<ItemEntity>) entities.stream().sorted(Comparator.comparing(item -> item.m_20270_(c))).collect(Collectors.toList());
        int itemIndex = 0;
        ItemEntity candidate;
        for (candidate = (ItemEntity) items.get(itemIndex); !this.claimEntityMutex(candidate); candidate = (ItemEntity) items.get(itemIndex)) {
            if (++itemIndex >= items.size()) {
                return null;
            }
        }
        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.collect_target", new Object[] { this.translate(candidate) }), false);
        return candidate;
    }

    @Override
    public void stop() {
        this.clearMoveTarget();
        this.setSelectedTarget(null);
        this.construct.asEntity().m_21553_(false);
        super.m_8041_();
    }

    @Override
    public void onTaskSet() {
        super.onTaskSet();
        this.numItemsCollected = 0;
    }

    public void setArea(AABB aabb) {
        this.area = aabb;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.GATHER_ITEMS);
    }

    public ConstructCollectItems duplicate() {
        return new ConstructCollectItems(this.construct, this.guiIcon).copyFrom(this);
    }

    @Override
    public List<ConstructAITaskParameter> instantiateParameters() {
        List<ConstructAITaskParameter> parameters = super.instantiateParameters();
        parameters.add(new ConstructTaskFilterParameter("collect.filter"));
        parameters.add(new ConstructTaskItemStackParameter("collect.filter_single"));
        return parameters;
    }

    @Override
    public void inflateParameters() {
        super.inflateParameters();
        this.getParameter("collect.filter").ifPresent(param -> {
            if (param instanceof ConstructTaskFilterParameter) {
                this.filter.copyFrom(((ConstructTaskFilterParameter) param).getValue());
            }
        });
        this.getParameter("collect.filter_single").ifPresent(param -> {
            if (param instanceof ConstructTaskItemStackParameter) {
                this.matchStack = ((ConstructTaskItemStackParameter) param).getStack().copy();
            }
        });
    }

    public ConstructCollectItems copyFrom(ConstructAITask<?> other) {
        super.copyFrom(other);
        if (other instanceof ConstructCollectItems) {
            this.setSelectedTarget(((ConstructCollectItems) other).getSelectedTarget());
            this.filter.copyFrom(((ConstructCollectItems) other).filter);
            this.matchStack = ((ConstructCollectItems) other).matchStack.copy();
        }
        return this;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        return nbt;
    }

    @Override
    protected String getAreaIdentifier() {
        return "collect.area";
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }
}