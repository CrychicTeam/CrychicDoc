package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructCommandTileEntityInteract;
import com.mna.api.items.DynamicItemFilter;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.tools.InventoryUtilities;
import java.util.EnumSet;
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

public class ConstructPlaceItem extends ConstructCommandTileEntityInteract<BlockEntity, ConstructPlaceItem> {

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.CARRY };

    private int interactTimer = 0;

    private int interactionTime = 5;

    public ConstructPlaceItem(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon, BlockEntity.class);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return super.canUse() && (this.construct.getCarryingHands().length > 0 || !InventoryUtilities.getFirstItemFromContainer(new DynamicItemFilter(), this.construct.getCarrySize(), this.construct, this.side, true).isEmpty());
    }

    @Override
    public void tick() {
        super.m_8037_();
        AbstractGolem c = this.getConstructAsEntity();
        if (this.construct.getCarryingHands().length == 0 && InventoryUtilities.getFirstItemFromContainer(new DynamicItemFilter(), this.construct.getCarrySize(), this.construct, this.side, true).isEmpty()) {
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_hands_empty", new Object[] { this.translate(this.getTileEntity()) }));
            this.forceFail();
        } else {
            if (this.getTileEntity() != null) {
                LazyOptional<IItemHandler> handler = this.getTileEntity().getCapability(ForgeCapabilities.ITEM_HANDLER, this.side);
                if (handler.isPresent()) {
                    if (this.doMove()) {
                        InteractionHand[] carrying = this.construct.getCarryingHands();
                        if (this.interactTimer > 0) {
                            if (this.interactTimer == 5) {
                                if (carrying.length > 0) {
                                    c.m_6674_(carrying[0]);
                                } else {
                                    c.m_6674_(InteractionHand.MAIN_HAND);
                                }
                            }
                            this.interactTimer--;
                        } else if (this.interactTimer == 0) {
                            this.preInteract();
                            ItemStack stack = ItemStack.EMPTY;
                            if (carrying.length > 0) {
                                stack = c.m_21120_(carrying[0]);
                            } else {
                                stack = InventoryUtilities.getFirstItemFromContainer(new DynamicItemFilter(), this.construct.getCarrySize(), this.construct, this.side, true);
                            }
                            if (stack.isEmpty()) {
                                this.forceFail();
                                return;
                            }
                            String stackTranslated = this.translate(stack);
                            String containerTranslated = this.translate(this.getTileEntity());
                            if (InventoryUtilities.mergeIntoInventory((IItemHandler) handler.resolve().get(), stack)) {
                                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_success", new Object[] { stackTranslated, containerTranslated }));
                                if (carrying.length > 0) {
                                    c.m_21008_(carrying[0], ItemStack.EMPTY);
                                } else {
                                    InventoryUtilities.getFirstItemFromContainer(new DynamicItemFilter(), this.construct.getCarrySize(), this.construct, this.side);
                                }
                                if (this.construct.getCarryingHands().length == 0 && InventoryUtilities.getFirstItemFromContainer(new DynamicItemFilter(), this.construct.getCarrySize(), this.construct, this.side, true).isEmpty()) {
                                    this.setSuccessCode();
                                    return;
                                }
                            } else {
                                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.place_fail", new Object[] { stackTranslated, containerTranslated }));
                                this.forceFail();
                            }
                        }
                    }
                } else {
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.inv_missing_cap", new Object[] { this.translate(this.getTileEntity()) }));
                    this.forceFail();
                }
            }
        }
    }

    @Override
    public void start() {
        super.start();
        this.interactionTime = Math.max(this.getInteractTime(ConstructCapability.CARRY) / 5, 1);
        this.interactTimer = this.interactionTime;
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.PLACE_ITEM);
    }

    public ConstructCommandTileEntityInteract<BlockEntity, ConstructPlaceItem> duplicate() {
        return new ConstructPlaceItem(this.construct, this.guiIcon).copyFrom(this);
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }

    @Override
    public boolean areCapabilitiesMet() {
        return this.construct.getConstructData().areCapabilitiesEnabled(ConstructCapability.ITEM_STORAGE) ? true : super.areCapabilitiesMet();
    }
}