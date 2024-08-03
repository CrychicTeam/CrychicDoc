package com.mna.gui.containers.item;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.spells.ICanContainSpell;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.IModifiable;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.gui.containers.ContainerInit;
import com.mna.items.ItemInit;
import com.mna.items.sorcery.ItemBookOfRote;
import com.mna.items.sorcery.ItemStaff;
import com.mna.spells.crafting.ModifiedSpellPart;
import com.mna.spells.crafting.SpellRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ContainerSpellAdjustments extends AbstractContainerMenu {

    private final SpellRecipe recipe;

    private final ItemStack spellStack;

    private final InteractionHand hand;

    public ContainerSpellAdjustments(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(i, playerInventory);
    }

    public ContainerSpellAdjustments(int windowId, Inventory playerInventory) {
        super(ContainerInit.SPELL_ADJUSTMENTS.get(), windowId);
        this.hand = playerInventory.player.m_21205_().getItem() != ItemInit.SPELL.get() && !(playerInventory.player.m_21205_().getItem() instanceof ItemBookOfRote) && !(playerInventory.player.m_21205_().getItem() instanceof ItemStaff) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        this.spellStack = playerInventory.player.m_21120_(this.hand);
        this.recipe = SpellRecipe.fromNBT(this.spellStack.getOrCreateTag());
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    public ModifiedSpellPart<Shape> getShape() {
        return this.recipe.getShape();
    }

    public ModifiedSpellPart<SpellEffect> getComponent() {
        return this.recipe.getComponent(0);
    }

    public Modifier getModifier(int index) {
        return this.recipe.getModifier(index);
    }

    public boolean isTranscribed() {
        return !(this.getSpellStack().getItem() instanceof ICanContainSpell) ? false : ((ICanContainSpell) this.getSpellStack().getItem()).isTranscribedSpell(this.getSpellStack());
    }

    public void increaseAttribute(Player player, IModifiable<?> part, Attribute attribute, Level world, boolean isShiftDown) {
        if (!this.isTranscribed()) {
            int steps = isShiftDown ? 5 : 1;
            IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            float bonus = 0.0F;
            if (progression != null && progression.getAlliedFaction() != null) {
                bonus = progression.getAlliedFaction().getMaxModifierBonus(attribute);
            }
            for (int i = 0; i < steps; i++) {
                if (part instanceof Shape) {
                    this.getShape().stepUp(attribute, bonus);
                } else if (part instanceof SpellEffect) {
                    this.getComponent().stepUp(attribute, bonus);
                }
            }
            this.recipe.calculateComplexity();
            this.recipe.calculateManaCost();
        }
    }

    public void decreaseAttribute(Player player, IModifiable<?> part, Attribute attribute, Level world, boolean isShiftDown) {
        if (!this.isTranscribed()) {
            int steps = isShiftDown ? 5 : 1;
            IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            float bonus = 0.0F;
            if (progression != null && progression.getAlliedFaction() != null) {
                bonus = progression.getAlliedFaction().getMinModifierBonus(attribute);
            }
            for (int i = 0; i < steps; i++) {
                if (part instanceof Shape) {
                    this.getShape().stepDown(attribute, bonus);
                } else if (part instanceof SpellEffect) {
                    this.getComponent().stepDown(attribute, bonus);
                }
            }
            this.recipe.calculateComplexity();
            this.recipe.calculateManaCost();
        }
    }

    public float getManaCost(Player player) {
        return this.recipe.getManaCost();
    }

    public float getComplexity(Player player) {
        return this.recipe.getComplexity();
    }

    public ItemStack getSpellStack() {
        return this.spellStack;
    }

    public String getSpellName() {
        return this.spellStack.getHoverName().getString();
    }

    public SpellRecipe getRecipe() {
        return this.recipe;
    }

    public InteractionHand getHand() {
        return this.hand;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }
}