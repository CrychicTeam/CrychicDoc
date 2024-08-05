package com.mna.blocks.tileentities.wizard_lab;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.tile.IEldrinConsumerTile;
import com.mna.api.events.GenericProgressionEvent;
import com.mna.api.events.ProgressionEventIDs;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ICanContainSpell;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.tools.MATags;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.items.ItemInit;
import com.mna.items.artifice.charms.ItemContingencyCharm;
import com.mna.items.runes.ItemStoneRune;
import com.mna.items.sorcery.ItemBookOfRote;
import com.mna.spells.crafting.SpellRecipe;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class TranscriptionTableTile extends WizardLabTile implements IEldrinConsumerTile {

    public static final int SLOT_INK = 0;

    public static final int SLOT_LAPIS = 1;

    public static final int SLOT_BOOK = 2;

    public static final int SLOT_INPUT = 3;

    public static final int INVENTORY_SIZE = 4;

    private static final int LAPIS_REQUIRED_PER_TIER = 10;

    private static final int LAPIS_REQUIRED_PER_TIER_RECHARGE = 2;

    private static final int LAPIS_REQUIRED_PER_TIER_GLYPH = 1;

    private static final int INK_REQUIRED_PER_TIER = 1;

    private float ticksRequired = 100.0F;

    private SpellRecipe copyRecipe;

    private WizardLabTile.PowerStatus powerConsumeStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;

    private float powerAccumulation = 0.0F;

    private Affinity requiredAffinity = Affinity.UNKNOWN;

    public TranscriptionTableTile(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state, inventorySize);
    }

    public TranscriptionTableTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.TRANSCRIPTION_TABLE.get(), pos, state, 4);
    }

    @Override
    public boolean canActivate(Player player) {
        ItemStack spellSlotItem = this.m_8020_(3);
        ItemStack bookStack = this.m_8020_(2);
        if (!bookStack.isEmpty() && bookStack.getItem() instanceof ICanContainSpell) {
            ISpellDefinition spell = ((ICanContainSpell) bookStack.getItem()).getSpell(bookStack, player);
            return this.hasStack(3) && this.hasStack(0) && this.m_8020_(0).getDamageValue() + this.getInkRequired() <= this.m_8020_(0).getMaxDamage() && this.hasStack(1) && this.m_8020_(1).getCount() >= this.getLapisRequired(player) && this.hasStack(2) && spellSlotItem.getItem() instanceof ICanContainSpell && ((ICanContainSpell) spellSlotItem.getItem()).canAcceptSpell(spellSlotItem, spell);
        } else {
            return false;
        }
    }

    @Override
    protected boolean canContinue() {
        ItemStack spellSlotItem = this.m_8020_(3);
        ItemStack bookStack = this.m_8020_(2);
        if (!bookStack.isEmpty() && bookStack.getItem() instanceof ICanContainSpell) {
            ISpellDefinition spell = ((ICanContainSpell) bookStack.getItem()).getSpell(bookStack, this.getCrafter());
            return this.copyRecipe != null && this.hasStack(3) && this.hasStack(2) && spellSlotItem.getItem() instanceof ICanContainSpell && ((ICanContainSpell) spellSlotItem.getItem()).canAcceptSpell(spellSlotItem, spell);
        } else {
            return false;
        }
    }

    @Override
    public float getPctComplete() {
        return (float) this.getActiveTicks() / this.ticksRequired;
    }

    public int getLapisRequired(@Nullable Player player) {
        if (this.copyRecipe != null && this.hasStack(3)) {
            ItemStack inputStack = this.m_8020_(3);
            if (inputStack.getItem() instanceof ItemContingencyCharm) {
                ISpellDefinition spell = ((ICanContainSpell) inputStack.getItem()).getSpell(inputStack, player);
                if (this.copyRecipe.isSame(spell, false, true, true)) {
                    return this.copyRecipe.getTier(this.f_58857_) * 2;
                }
            }
            return MATags.isItemIn(inputStack.getItem(), MATags.Items.STONE_RUNES) ? this.copyRecipe.getTier(this.f_58857_) * 1 : this.copyRecipe.getTier(this.f_58857_) * 10;
        } else {
            return 0;
        }
    }

    public int getInkRequired() {
        return this.copyRecipe != null && this.hasStack(3) ? this.copyRecipe.getTier(this.f_58857_) * 1 : 0;
    }

    @Override
    protected List<Integer> getSyncedInventorySlots() {
        return Arrays.asList(0, 1, 2, 3);
    }

    @Override
    protected void onCraftStart(Player crafter) {
        ItemStack book = this.m_8020_(2);
        if (!book.isEmpty() && book.getItem() instanceof ItemBookOfRote) {
            ItemStack input = this.m_8020_(3);
            if (!input.isEmpty() && input.getItem() instanceof ICanContainSpell) {
                ItemStack lapis = this.m_8020_(1);
                lapis.shrink(this.getLapisRequired(crafter));
                ItemStack ink = this.m_8020_(0);
                if (!this.m_58904_().isClientSide()) {
                    ink.hurt(this.getInkRequired(), this.f_58857_.random, (ServerPlayer) crafter);
                    if (ink.getDamageValue() >= ink.getMaxDamage()) {
                        this.m_6836_(0, ItemStack.EMPTY);
                    }
                }
                if (!this.m_58904_().isClientSide()) {
                    CompoundTag spellTag = ItemInit.ROTE_BOOK.get().getSpellCompound(book, crafter);
                    this.copyRecipe = SpellRecipe.fromNBT(spellTag);
                    if (!this.copyRecipe.isValid()) {
                        this.setInactive();
                    } else {
                        this.requiredAffinity = this.copyRecipe.getHighestAffinity().getShiftAffinity();
                    }
                }
            } else {
                this.setInactive();
            }
        } else {
            this.setInactive();
        }
    }

    public float getPowerPerTick() {
        return this.m_8020_(3).getItem() instanceof ItemStoneRune ? 1.0F : 3.0F;
    }

    @Override
    protected boolean canActiveTick() {
        if (this.copyRecipe != null && this.copyRecipe.isValid()) {
            float amount = this.consume(this.getCrafter(), this.m_58899_(), Vec3.atCenterOf(this.m_58899_()), this.requiredAffinity, this.getPowerPerTick());
            this.powerConsumeStatus = this.fromPowerCode(this.getPowerPerTick(), amount);
            this.powerAccumulation = this.powerAccumulation + Math.max(amount, 0.0F);
            if (this.powerAccumulation < this.getPowerPerTick()) {
                return false;
            } else {
                this.powerAccumulation = 0.0F;
                return true;
            }
        } else {
            if (!this.m_58904_().isClientSide()) {
                this.setInactive();
            }
            return false;
        }
    }

    @Override
    protected void onComplete() {
        ItemStack output = this.m_8020_(3).copy();
        if (!output.isEmpty()) {
            output = ((ICanContainSpell) output.getItem()).setSpell(output, this.copyRecipe);
            ((ICanContainSpell) output.getItem()).setTranscribedSpell(output);
            if (this.m_8020_(2).hasCustomHoverName()) {
                output.setHoverName(this.m_8020_(2).getHoverName());
            }
            this.m_6836_(3, output);
            if (this.getCrafter() != null && !this.m_58904_().isClientSide()) {
                Player crafter = this.getCrafter();
                if (crafter != null) {
                    MinecraftForge.EVENT_BUS.post(new GenericProgressionEvent(crafter, ProgressionEventIDs.TRANSCRIBE_SPELL));
                    if (crafter instanceof ServerPlayer) {
                        CustomAdvancementTriggers.TRANSCRIBE_SPELL.trigger((ServerPlayer) crafter, this.copyRecipe, output);
                    }
                }
            }
            if (!this.m_58904_().isClientSide()) {
                this.m_58904_().playSound(null, (double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_(), SFX.Event.Eldrin.DRAW_IN_ITEM, SoundSource.BLOCKS, 1.0F, (float) (0.95 + Math.random() * 0.1F));
            }
        }
    }

    @Override
    protected void onDeactivated() {
        this.powerConsumeStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;
    }

    @Override
    protected CompoundTag getMeta() {
        CompoundTag tag = new CompoundTag();
        if (this.copyRecipe != null) {
            this.copyRecipe.writeToNBT(tag);
        }
        this.writePowerConsumeStatus(this.powerConsumeStatus, tag);
        tag.putFloat("powerAccum", this.powerAccumulation);
        tag.putInt("affinity", this.requiredAffinity.ordinal());
        return tag;
    }

    @Override
    protected void loadMeta(CompoundTag tag) {
        this.copyRecipe = SpellRecipe.fromNBT(tag);
        this.powerConsumeStatus = this.readPowerConsumeStatus(tag);
        if (tag.contains("powerAccum")) {
            this.powerAccumulation = tag.getFloat("powerAccum");
        }
        if (tag.contains("affinity")) {
            int aff = tag.getInt("affinity");
            this.requiredAffinity = Affinity.values()[aff % Affinity.values().length];
        }
    }

    @Override
    public int getXPCost(Player crafter) {
        return 20;
    }

    @Override
    public HashMap<Affinity, WizardLabTile.PowerStatus> powerRequirementStatus() {
        HashMap<Affinity, WizardLabTile.PowerStatus> reqs = new HashMap();
        reqs.put(this.requiredAffinity, this.powerConsumeStatus);
        return reqs;
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends WizardLabTile> state) {
        return this.isActive() ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.laboratory_transcription_armature.active")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.laboratory_transcription_armature.idle"));
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] { 0, 1, 3 };
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (this.isActive()) {
            return false;
        } else {
            return index != 3 ? true : direction == Direction.UP || direction == Direction.DOWN;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (this.isActive()) {
            return false;
        } else {
            ItemStack existing = this.m_8020_(index);
            switch(index) {
                case 0:
                    return stack.getItem() == ItemInit.ARCANIST_INK.get() && (existing.isEmpty() || existing.getCount() < existing.getMaxStackSize());
                case 1:
                    return stack.getItem() == Items.LAPIS_LAZULI && (existing.isEmpty() || existing.getCount() < existing.getMaxStackSize());
                case 2:
                default:
                    return false;
                case 3:
                    if (stack.getItem() instanceof ICanContainSpell inter && inter.canAcceptSpell(stack, this.copyRecipe) && existing.isEmpty()) {
                        return true;
                    }
                    return false;
            }
        }
    }
}