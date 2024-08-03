package com.mna.blocks.tileentities.wizard_lab;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.affinity.Affinity;
import com.mna.api.blocks.tile.IEldrinConsumerTile;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.events.GenericProgressionEvent;
import com.mna.api.events.ProgressionEventIDs;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.items.ItemInit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class DisenchanterTile extends WizardLabTile implements IEldrinConsumerTile {

    public static final int SLOT_INPUT = 0;

    public static final int SLOT_OUTPUT = 1;

    public static final int INVENTORY_SIZE = 2;

    private float ticksRequired = 100.0F;

    private float powerPerTick = 3.0F;

    private WizardLabTile.PowerStatus fireConsumeStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;

    private WizardLabTile.PowerStatus windConsumeStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;

    private float firePowerAccum = 0.0F;

    private float windPowerAccum = 0.0F;

    public DisenchanterTile(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state, inventorySize);
    }

    public DisenchanterTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.DISENCHANTER.get(), pos, state, 2);
    }

    @Override
    public boolean canActivate(Player player) {
        boolean baseActivate = this.canContinue();
        if (!baseActivate) {
            return false;
        } else {
            ItemStack input = this.m_8020_(0);
            if (EnchantmentHelper.getEnchantments(input).size() == 0) {
                return false;
            } else {
                ItemStack output = this.m_8020_(1);
                return EnchantmentHelper.getEnchantments(output).size() > 0 ? false : !GeneralConfigValues.DisenchantBlacklist.contains(ForgeRegistries.ITEMS.getKey(input.getItem()).toString());
            }
        }
    }

    @Override
    protected boolean canContinue() {
        return this.hasStack(0) && this.hasStack(1) && !GeneralConfigValues.DisenchantBlacklist.contains(ForgeRegistries.ITEMS.getKey(this.m_8020_(0).getItem()).toString());
    }

    @Override
    public float getPctComplete() {
        return (float) this.getActiveTicks() / this.ticksRequired;
    }

    @Override
    protected List<Integer> getSyncedInventorySlots() {
        return Arrays.asList(0, 1);
    }

    @Override
    protected boolean canActiveTick() {
        this.fireConsumeStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;
        this.windConsumeStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;
        if (this.firePowerAccum < this.powerPerTick) {
            float amount = this.consume(this.getCrafter(), this.m_58899_(), Vec3.atCenterOf(this.m_58899_()), Affinity.FIRE, this.powerPerTick);
            this.firePowerAccum = this.firePowerAccum + Math.max(amount, 0.0F);
            this.fireConsumeStatus = this.fromPowerCode(this.powerPerTick, amount);
        } else if (this.fireConsumeStatus != WizardLabTile.PowerStatus.SUPPLIED) {
            this.fireConsumeStatus = WizardLabTile.PowerStatus.SUPPLIED;
        }
        if (this.windPowerAccum < this.powerPerTick) {
            float amount = this.consume(this.getCrafter(), this.m_58899_(), Vec3.atCenterOf(this.m_58899_()), Affinity.WIND, this.powerPerTick);
            this.windPowerAccum = this.windPowerAccum + Math.max(amount, 0.0F);
            this.windConsumeStatus = this.fromPowerCode(this.powerPerTick, amount);
        } else if (this.windConsumeStatus != WizardLabTile.PowerStatus.SUPPLIED) {
            this.windConsumeStatus = WizardLabTile.PowerStatus.SUPPLIED;
        }
        if (this.fireConsumeStatus.allowMachineOperation() && this.windConsumeStatus.allowMachineOperation()) {
            this.windPowerAccum = 0.0F;
            this.firePowerAccum = 0.0F;
            if (this.getActiveTicks() == 90 && !this.m_58904_().isClientSide()) {
                this.m_58904_().playSound(null, (double) this.m_58899_().m_123341_(), (double) this.m_58899_().m_123342_(), (double) this.m_58899_().m_123343_(), SFX.Spell.Buff.ARCANE, SoundSource.BLOCKS, 1.0F, (float) (0.95 + Math.random() * 0.1F));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected CompoundTag getMeta() {
        CompoundTag tag = new CompoundTag();
        this.writePowerConsumeStatus(this.fireConsumeStatus, tag, "firePowerStatus");
        tag.putFloat("firePowerAccum", this.firePowerAccum);
        this.writePowerConsumeStatus(this.windConsumeStatus, tag, "windPowerStatus");
        tag.putFloat("windPowerAccum", this.windPowerAccum);
        return tag;
    }

    @Override
    protected void loadMeta(CompoundTag tag) {
        this.fireConsumeStatus = this.readPowerConsumeStatus(tag, "firePowerStatus");
        this.windConsumeStatus = this.readPowerConsumeStatus(tag, "windPowerStatus");
        if (tag.contains("firePowerAccum")) {
            this.firePowerAccum = tag.getFloat("firePowerAccum");
        }
        if (tag.contains("windPowerAccum")) {
            this.windPowerAccum = tag.getFloat("windPowerAccum");
        }
    }

    @Override
    protected void onComplete() {
        ItemStack input = this.m_8020_(0);
        ItemStack output = this.m_8020_(1);
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(input);
        int indexToMove = (int) ((double) enchantments.size() * Math.random());
        enchantments.entrySet().stream().skip((long) indexToMove).findFirst().ifPresent(e -> {
            HashMap<Enchantment, Integer> moved = new HashMap();
            moved.put((Enchantment) e.getKey(), (Integer) e.getValue());
            EnchantmentHelper.setEnchantments(moved, output);
            enchantments.remove(e.getKey());
        });
        EnchantmentHelper.setEnchantments(enchantments, input);
        if (input.getItem() instanceof EnchantedBookItem && input.hasTag()) {
            ListTag chants = input.getTag().getList("StoredEnchantments", 10);
            if (chants.size() == 1) {
                this.m_6836_(0, new ItemStack(Items.BOOK));
            }
        }
        if (this.getCrafter() != null && !this.m_58904_().isClientSide()) {
            Player crafter = this.getCrafter();
            if (crafter != null) {
                MinecraftForge.EVENT_BUS.post(new GenericProgressionEvent(crafter, ProgressionEventIDs.REMOVE_ENCHANTMENT));
                if (crafter instanceof ServerPlayer) {
                    CustomAdvancementTriggers.DISENCHANT.trigger((ServerPlayer) crafter, output);
                }
            }
        }
    }

    @Override
    protected void onDeactivated() {
        this.fireConsumeStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;
        this.windConsumeStatus = WizardLabTile.PowerStatus.NOT_REQUESTING;
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
            Random random = new Random();
            float radius = 1.25F - this.getPctComplete();
            Vec3 pos = Vec3.upFromBottomCenterOf(this.m_58899_(), 2.0);
            Vec3 offset = new Vec3(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize().scale((double) radius);
            this.m_58904_().addParticle(new MAParticleType(ParticleInit.ARCANE_LERP.get()), pos.x + offset.x, pos.y + offset.y, pos.z + offset.z, pos.x, pos.y, pos.z);
            int numGlows = (int) (5.0F * this.getPctComplete());
            for (int i = 0; i < numGlows; i++) {
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.ARCANE_MAGELIGHT.get()), pos.x + offset.x, pos.y + offset.y, pos.z + offset.z, 0.0, 0.0, 0.0);
            }
            if ((double) this.getPctComplete() > 0.95) {
                for (int i = 0; i < 150; i++) {
                    this.m_58904_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_GRAVITY.get()), pos.x(), pos.y() - 0.8, pos.z(), -0.5 + Math.random(), 0.01, -0.5 + Math.random());
                }
            }
        }
    }

    @Override
    public HashMap<Affinity, WizardLabTile.PowerStatus> powerRequirementStatus() {
        HashMap<Affinity, WizardLabTile.PowerStatus> reqs = new HashMap();
        reqs.put(Affinity.FIRE, this.fireConsumeStatus);
        reqs.put(Affinity.WIND, this.windConsumeStatus);
        return reqs;
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends WizardLabTile> state) {
        return this.isActive() ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.laboratory_disenchantment_armature.active")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.laboratory_disenchantment_armature.idle"));
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side != Direction.DOWN && side != Direction.UP ? new int[] { 0, 1 } : new int[] { 1 };
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
            switch(index) {
                case 0:
                    return this.m_8020_(index).isEmpty() && EnchantmentHelper.getEnchantments(stack).size() > 0;
                case 1:
                    return this.m_8020_(index).isEmpty() && stack.getItem() == ItemInit.RUNE_PROJECTION.get();
                default:
                    return false;
            }
        }
    }
}