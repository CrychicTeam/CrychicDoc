package com.mna.blocks.tileentities.wizard_lab;

import com.mna.Registries;
import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.tile.IEldrinConsumerTile;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.sound.SFX;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.items.ItemInit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.IForgeRegistry;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class ThesisDeskTile extends WizardLabTile implements IEldrinConsumerTile, ISelectSpellComponents {

    public static final int SLOT_PAPER = 0;

    public static final int SLOT_INK = 1;

    public static final int SLOT_BOOK = 2;

    public static final int SLOT_OUTPUT = 3;

    public static final int INVENTORY_SIZE = 4;

    private float ticksRequired = 1800.0F;

    private float powerPerTick = 5.0F;

    private float powerAccum = 0.0F;

    private ISpellComponent selectedComponent;

    private WizardLabTile.PowerStatus powerConsumeStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;

    public ThesisDeskTile(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state, inventorySize);
    }

    public ThesisDeskTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.THESIS_DESK.get(), pos, state, 4);
    }

    @Override
    public boolean canActivate(Player player) {
        boolean baseActivate = this.canContinue();
        if (!baseActivate) {
            return false;
        } else if (this.hasStack(0) && this.m_8020_(0).getCount() >= this.getPaperCount() && this.hasStack(1) && this.m_8020_(1).getMaxDamage() - this.m_8020_(1).getDamageValue() >= this.getInkDamage()) {
            LazyOptional<IPlayerRoteSpells> roteData = player.getCapability(PlayerRoteSpellsProvider.ROTE);
            return player.isCreative() || roteData.isPresent() && ((IPlayerRoteSpells) roteData.resolve().get()).isRote(this.selectedComponent);
        } else {
            return false;
        }
    }

    @Override
    protected void onCraftStart(Player crafter) {
        if (crafter instanceof ServerPlayer) {
            this.m_8020_(1).hurt(this.getInkDamage(), this.f_58857_.random, (ServerPlayer) crafter);
            if (this.m_8020_(1).getDamageValue() >= this.m_8020_(1).getMaxDamage()) {
                this.m_6836_(1, ItemStack.EMPTY);
            }
            this.m_8020_(0).shrink(this.getPaperCount());
        }
    }

    private int getInkDamage() {
        return this.selectedComponent == null ? 0 : this.selectedComponent.getTier(this.f_58857_) * 5;
    }

    private int getPaperCount() {
        return this.selectedComponent == null ? 0 : this.selectedComponent.getTier(this.f_58857_) * 5;
    }

    @Override
    protected CompoundTag getMeta() {
        CompoundTag tag = new CompoundTag();
        if (this.selectedComponent != null) {
            tag.putString("part", this.selectedComponent.getRegistryName().toString());
        }
        this.writePowerConsumeStatus(this.powerConsumeStatus, tag);
        tag.putFloat("accum", this.powerAccum);
        return tag;
    }

    @Override
    protected void loadMeta(CompoundTag tag) {
        this.powerConsumeStatus = this.readPowerConsumeStatus(tag);
        this.selectedComponent = null;
        if (tag.contains("part")) {
            ResourceLocation part = new ResourceLocation(tag.getString("part"));
            if (((IForgeRegistry) Registries.Shape.get()).containsKey(part)) {
                this.selectedComponent = (ISpellComponent) ((IForgeRegistry) Registries.Shape.get()).getValue(part);
            } else if (((IForgeRegistry) Registries.Modifier.get()).containsKey(part)) {
                this.selectedComponent = (ISpellComponent) ((IForgeRegistry) Registries.Modifier.get()).getValue(part);
            } else if (((IForgeRegistry) Registries.SpellEffect.get()).containsKey(part)) {
                this.selectedComponent = (ISpellComponent) ((IForgeRegistry) Registries.SpellEffect.get()).getValue(part);
            }
        }
        if (tag.contains("accum")) {
            this.powerAccum = tag.getFloat("accum");
        }
    }

    @Override
    protected boolean canContinue() {
        return this.selectedComponent != null && this.hasStack(2) && !this.hasStack(3);
    }

    @Override
    public float getPctComplete() {
        return (float) this.getActiveTicks() / this.ticksRequired;
    }

    @Override
    protected List<Integer> getSyncedInventorySlots() {
        return Arrays.asList(0, 1, 2);
    }

    public Affinity getSelectedAffinity() {
        return this.selectedComponent instanceof SpellEffect ? ((SpellEffect) this.selectedComponent).getAffinity().getShiftAffinity() : Affinity.ARCANE;
    }

    @Override
    protected boolean canActiveTick() {
        float amount = this.consume(this.getCrafter(), this.m_58899_(), Vec3.atCenterOf(this.m_58899_()), this.getSelectedAffinity(), this.powerPerTick);
        this.powerConsumeStatus = this.fromPowerCode(this.powerPerTick, amount);
        this.powerAccum = this.powerAccum + Math.max(amount, 0.0F);
        if (this.powerAccum >= this.powerPerTick) {
            this.powerAccum = 0.0F;
            if (this.getActiveTicks() == 1799 && !this.m_58904_().isClientSide()) {
                this.m_58904_().playSound(null, (double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_(), SFX.Gui.CHARCOAL_SCRIBBLE, SoundSource.BLOCKS, 1.0F, (float) (0.95 + Math.random() * 0.1F));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ISpellComponent getSpellComponent() {
        return this.selectedComponent;
    }

    @Override
    public void setSpellComponent(ISpellComponent component) {
        if (!this.isActive()) {
            this.selectedComponent = component;
        }
    }

    @Override
    protected void onComplete() {
        ItemStack thesis = new ItemStack(ItemInit.SPELL_PART_THESIS.get());
        ItemInit.SPELL_PART_THESIS.get().setComponent(thesis, this.selectedComponent);
        this.m_6836_(3, thesis);
        this.powerConsumeStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;
    }

    @Override
    protected void onDeactivated() {
        this.powerConsumeStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;
    }

    @Override
    public int getXPCost(Player crafter) {
        return 100;
    }

    @Override
    public HashMap<Affinity, WizardLabTile.PowerStatus> powerRequirementStatus() {
        HashMap<Affinity, WizardLabTile.PowerStatus> reqs = new HashMap();
        reqs.put(this.getSelectedAffinity(), this.powerConsumeStatus);
        return reqs;
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends WizardLabTile> state) {
        return this.isActive() ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.thesis.quillwrite")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.thesis.quillidle"));
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side != Direction.DOWN && side != Direction.UP ? new int[] { 1, 0 } : new int[] { 3 };
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
            ItemStack existing = this.m_8020_(index);
            switch(index) {
                case 0:
                    return stack.getItem() == ItemInit.VELLUM.get() && (existing.isEmpty() || existing.getCount() < existing.getMaxStackSize());
                case 1:
                    return stack.getItem() == ItemInit.ARCANIST_INK.get() && existing.isEmpty();
                default:
                    return false;
            }
        }
    }
}