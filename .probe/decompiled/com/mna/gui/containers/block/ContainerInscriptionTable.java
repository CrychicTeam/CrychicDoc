package com.mna.gui.containers.block;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.IModifiable;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.blocks.tileentities.wizard_lab.InscriptionTableTile;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.slots.InscriptionTableOutputSlot;
import com.mna.gui.containers.slots.SingleItemSlot;
import com.mna.items.ItemInit;
import com.mna.recipes.spells.ComponentRecipe;
import com.mna.recipes.spells.ModifierRecipe;
import com.mna.recipes.spells.ShapeRecipe;
import com.mna.spells.crafting.ModifiedSpellPart;
import java.util.HashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerInscriptionTable extends AbstractContainerMenu {

    private final IItemHandler inventory;

    private final InscriptionTableTile table;

    public static int SIZE = 4;

    static int rowLength = 9;

    static int rowCount = 3;

    private HashMap<ResourceLocation, Integer> cachedReagentList;

    public ContainerInscriptionTable(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(i, playerInventory, ((InscriptionTableTile) playerInventory.player.m_9236_().getBlockEntity(packetBuffer.readBlockPos())).readFrom(packetBuffer));
    }

    public ContainerInscriptionTable(int windowId, Inventory playerInventory, InscriptionTableTile inventory) {
        super(ContainerInit.INSCRIPTION_TABLE.get(), windowId);
        this.inventory = new InvWrapper(inventory);
        this.table = inventory;
        inventory.m_5856_(playerInventory.player);
        this.cachedReagentList = new HashMap();
        this.m_38897_(new SingleItemSlot(this.inventory, 0, 8, 8, ItemInit.ARCANIST_INK.get()));
        this.m_38897_(new SingleItemSlot(this.inventory, 1, 33, 8, Items.PAPER));
        this.m_38897_(new SingleItemSlot(this.inventory, 2, 58, 8, ItemInit.ARCANE_ASH.get()));
        this.m_38897_(new InscriptionTableOutputSlot(playerInventory.player, inventory, 3, 232, 8));
        for (int xpos = 0; xpos < 3; xpos++) {
            for (int ypos = 0; ypos < 9; ypos++) {
                this.m_38897_(new Slot(playerInventory, ypos + xpos * 9 + 9, 48 + ypos * 18, 174 + xpos * 18));
            }
        }
        for (int var6 = 0; var6 < 9; var6++) {
            this.m_38897_(new Slot(playerInventory, var6, 48 + var6 * 18, 232));
        }
        this.cacheCurrentReagents(playerInventory.player.m_9236_());
    }

    @Override
    public boolean stillValid(Player playerIn) {
        BlockPos pos = this.table.m_58899_();
        return pos.m_123331_(playerIn.m_20183_()) <= 64.0;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < SIZE) {
                if (!this.m_38903_(itemstack1, SIZE, this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(itemstack1, 0, SIZE, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    public boolean setCurrentShape(Shape shape, Level world) {
        if (this.table != null && !this.table.isBuilding()) {
            this.table.setCurrentShape(shape);
            this.cacheCurrentReagents(world);
            return true;
        } else {
            return false;
        }
    }

    public boolean setCurrentComponent(SpellEffect component, Level world) {
        if (this.table != null && !this.table.isBuilding()) {
            this.table.setCurrentComponent(component);
            this.cacheCurrentReagents(world);
            return true;
        } else {
            return false;
        }
    }

    public boolean setCurrentModifier(int index, Modifier modifier, Level world) {
        if (this.table != null && !this.table.isBuilding()) {
            this.table.setCurrentModifier(modifier, index);
            this.cacheCurrentReagents(world);
            return true;
        } else {
            return false;
        }
    }

    public ModifiedSpellPart<Shape> getCurrentShape() {
        return this.table.getCurrentShape();
    }

    public ModifiedSpellPart<SpellEffect> getCurrentComponent() {
        return this.table.getCurrentComponent();
    }

    public Modifier getCurrentModifier(int index) {
        return this.table.getCurrentModifier(index);
    }

    public void increaseAttribute(Player player, IModifiable<?> part, Attribute attribute, Level world, boolean isShiftDown) {
        if (!this.table.isBuilding()) {
            int steps = isShiftDown ? 5 : 1;
            IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            float bonus = 0.0F;
            if (progression != null && progression.getAlliedFaction() != null) {
                bonus = progression.getAlliedFaction().getMaxModifierBonus(attribute);
            }
            for (int i = 0; i < steps; i++) {
                if (part instanceof Shape) {
                    this.table.changeShapeAttributeValue(attribute, this.table.getCurrentShape().stepUp(attribute, bonus));
                } else if (part instanceof SpellEffect) {
                    this.table.changeComponentAttributeValue(attribute, this.table.getCurrentComponent().stepUp(attribute, bonus));
                }
            }
            this.cacheCurrentReagents(world);
        }
    }

    public void decreaseAttribute(Player player, IModifiable<?> part, Attribute attribute, Level world, boolean isShiftDown) {
        if (!this.table.isBuilding()) {
            int steps = isShiftDown ? 5 : 1;
            IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            float bonus = 0.0F;
            if (progression != null && progression.getAlliedFaction() != null) {
                bonus = progression.getAlliedFaction().getMinModifierBonus(attribute);
            }
            for (int i = 0; i < steps; i++) {
                if (part instanceof Shape) {
                    this.table.changeShapeAttributeValue(attribute, this.table.getCurrentShape().stepDown(attribute, bonus));
                } else if (part instanceof SpellEffect) {
                    this.table.changeComponentAttributeValue(attribute, this.table.getCurrentComponent().stepDown(attribute, bonus));
                }
            }
            this.cacheCurrentReagents(world);
        }
    }

    public float getManaCost(Player player) {
        return this.table.getManaCost(player);
    }

    public float getComplexity() {
        return this.table.getComplexity();
    }

    public InscriptionTableTile.CraftCheckResult isReadyToBuild(IPlayerProgression progression) {
        return this.table.canBeginCrafting(progression);
    }

    public int getRequiredInk() {
        return this.table.getInkRemaining();
    }

    public int getRequiredPaper() {
        return this.table.getPaperRemaining();
    }

    public int getRequiredAsh() {
        return this.table.getFuelRemaining();
    }

    public void cacheCurrentReagents(Level world) {
        this.cachedReagentList.clear();
        ModifiedSpellPart<Shape> shape = this.table.getCurrentShape();
        ModifiedSpellPart<SpellEffect> component = this.table.getCurrentComponent();
        if (shape != null) {
            world.getRecipeManager().byKey(shape.getPart().getRegistryName()).ifPresent(recipe -> {
                for (ResourceLocation rLoc : ((ShapeRecipe) recipe).getRequiredItems()) {
                    this.lookupAndCacheItem(rLoc);
                }
            });
        }
        if (component != null) {
            world.getRecipeManager().byKey(component.getPart().getRegistryName()).ifPresent(recipe -> {
                for (ResourceLocation rLoc : ((ComponentRecipe) recipe).getRequiredItems()) {
                    this.lookupAndCacheItem(rLoc);
                }
            });
        }
        for (int i = 0; i < 3; i++) {
            Modifier m = this.table.getCurrentModifier(i);
            if (m != null) {
                world.getRecipeManager().byKey(m.getRegistryName()).ifPresent(recipe -> {
                    for (ResourceLocation rLoc : ((ModifierRecipe) recipe).getRequiredItems()) {
                        this.lookupAndCacheItem(rLoc);
                    }
                });
            }
        }
    }

    private void lookupAndCacheItem(ResourceLocation rLoc) {
        if (this.cachedReagentList.containsKey(rLoc)) {
            this.cachedReagentList.put(rLoc, (Integer) this.cachedReagentList.get(rLoc) + 1);
        } else {
            this.cachedReagentList.put(rLoc, 1);
        }
    }

    public HashMap<ResourceLocation, Integer> getCurrentReagents() {
        return this.cachedReagentList;
    }

    public void sendStartBuild() {
        this.table.build();
    }

    public int getCraftTicks() {
        return this.table.getCraftTicks();
    }

    public int getCraftTicksConsumed() {
        return this.table.getCraftTicksConsumed();
    }
}