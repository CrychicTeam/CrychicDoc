package com.mna.blocks.tileentities.wizard_lab;

import com.mna.Registries;
import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.tile.IEldrinConsumerTile;
import com.mna.api.capabilities.IPlayerRoteSpells;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.blocks.artifice.SpellSpecializationBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.IForgeRegistry;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class SpellSpecializationTile extends WizardLabTile implements IEldrinConsumerTile, ISelectSpellComponents {

    public static final int INVENTORY_SIZE = 0;

    private ISpellComponent selectedPart;

    private float ticksRequired = 3600.0F;

    private float powerPerTick = 10.0F;

    private float powerAccum = 0.0F;

    private WizardLabTile.PowerStatus powerConsumeStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;

    public SpellSpecializationTile(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state, inventorySize);
    }

    public SpellSpecializationTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.SPELL_SPECIALIZAITON.get(), pos, state, 0);
    }

    @Override
    public boolean canActivate(Player player) {
        if (this.selectedPart == null) {
            return false;
        } else {
            LazyOptional<IPlayerRoteSpells> rote = player.getCapability(PlayerRoteSpellsProvider.ROTE);
            return !rote.isPresent() ? false : !(((IPlayerRoteSpells) rote.resolve().get()).getMastery(this.selectedPart) >= 0.5F);
        }
    }

    @Override
    protected boolean canContinue() {
        return this.selectedPart != null;
    }

    @Override
    public float getPctComplete() {
        return (float) this.getActiveTicks() / this.ticksRequired;
    }

    @Override
    protected List<Integer> getSyncedInventorySlots() {
        return Arrays.asList();
    }

    @Override
    protected boolean canActiveTick() {
        if (this.getCrafter() == null) {
            return false;
        } else {
            Affinity aff = this.getSelectedAffinity();
            float amount = this.consume(this.getCrafter(), this.m_58899_(), Vec3.atCenterOf(this.m_58899_()), aff, this.powerPerTick);
            this.powerConsumeStatus = this.fromPowerCode(this.powerPerTick, amount);
            this.powerAccum = this.powerAccum + Math.max(amount, 0.0F);
            if (this.powerAccum >= this.powerPerTick) {
                this.powerAccum = 0.0F;
                return true;
            } else {
                return false;
            }
        }
    }

    public Affinity getSelectedAffinity() {
        return this.selectedPart instanceof SpellEffect ? ((SpellEffect) this.selectedPart).getAffinity().getShiftAffinity() : Affinity.ARCANE;
    }

    @Override
    public ISpellComponent getSpellComponent() {
        return this.selectedPart;
    }

    @Override
    public void setSpellComponent(ISpellComponent part) {
        this.selectedPart = part;
    }

    @Override
    protected CompoundTag getMeta() {
        CompoundTag tag = new CompoundTag();
        if (this.selectedPart != null) {
            tag.putString("part", this.selectedPart.getRegistryName().toString());
        }
        this.writePowerConsumeStatus(this.powerConsumeStatus, tag);
        tag.putFloat("accum", this.powerAccum);
        return tag;
    }

    @Override
    protected void loadMeta(CompoundTag tag) {
        this.powerConsumeStatus = this.readPowerConsumeStatus(tag);
        if (tag.contains("accum")) {
            this.powerAccum = tag.getFloat("accum");
        }
        this.selectedPart = null;
        if (tag.contains("part")) {
            ResourceLocation part = new ResourceLocation(tag.getString("part"));
            if (((IForgeRegistry) Registries.Shape.get()).containsKey(part)) {
                this.selectedPart = (ISpellComponent) ((IForgeRegistry) Registries.Shape.get()).getValue(part);
            } else if (((IForgeRegistry) Registries.SpellEffect.get()).containsKey(part)) {
                this.selectedPart = (ISpellComponent) ((IForgeRegistry) Registries.SpellEffect.get()).getValue(part);
            } else if (((IForgeRegistry) Registries.Modifier.get()).containsKey(part)) {
                this.selectedPart = (ISpellComponent) ((IForgeRegistry) Registries.Modifier.get()).getValue(part);
            }
        }
    }

    @Override
    protected void onComplete() {
        this.powerConsumeStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;
        Player craft = this.getCrafter();
        if (craft != null) {
            craft.getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> {
                r.addMastery(craft, this.selectedPart, 0.05F);
                if (!craft.m_9236_().isClientSide()) {
                    craft.m_213846_(Component.translatable("gui.mna.mastery_increased", Component.translatable(this.selectedPart.getRegistryName().toString()).getString(), (int) (r.getMastery(this.selectedPart) * 100.0F)));
                }
            });
        }
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
    protected void tick() {
        super.tick();
        if (this.m_58904_().isClientSide()) {
            this.spawnParticles();
        }
    }

    private void spawnParticles() {
        if (this.isActive()) {
            Vec3 particleEnd = Vec3.atCenterOf(this.m_58899_());
            Vec3 particleStart;
            switch((Direction) this.m_58900_().m_61143_(SpellSpecializationBlock.f_54117_)) {
                case EAST:
                    particleStart = new Vec3(-0.4, 0.75, 0.0);
                    particleEnd = particleEnd.add(0.1, 0.3, 0.0);
                    break;
                case NORTH:
                    particleStart = new Vec3(0.0, 0.75, 0.4);
                    particleEnd = particleEnd.add(0.0, 0.3, -0.2);
                    break;
                case WEST:
                    particleStart = new Vec3(0.4, 0.75, 0.0);
                    particleEnd = particleEnd.add(-0.1, 0.3, 0.0);
                    break;
                case SOUTH:
                default:
                    particleStart = new Vec3(0.0, 0.75, -0.4);
                    particleEnd = particleEnd.add(0.0, 0.3, 0.2);
            }
            this.m_58904_().addParticle(ParticleTypes.ENCHANT, particleEnd.x, particleEnd.y, particleEnd.z, particleStart.x, particleStart.y, particleStart.z);
        }
    }

    @Override
    public HashMap<Affinity, WizardLabTile.PowerStatus> powerRequirementStatus() {
        HashMap<Affinity, WizardLabTile.PowerStatus> reqs = new HashMap();
        reqs.put(this.getSelectedAffinity(), this.powerConsumeStatus);
        return reqs;
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends WizardLabTile> state) {
        return state.setAndContinue(RawAnimation.begin().thenLoop("animation.laboratory_mastery_armature.idle"));
    }
}