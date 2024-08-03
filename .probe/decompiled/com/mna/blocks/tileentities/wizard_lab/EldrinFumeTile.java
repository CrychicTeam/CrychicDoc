package com.mna.blocks.tileentities.wizard_lab;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.affinity.Affinity;
import com.mna.api.events.GenericProgressionEvent;
import com.mna.api.events.ProgressionEventIDs;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.items.ritual.MoteItem;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.eldrin.FumeFilterRecipe;
import com.mna.tools.ContainerTools;
import com.mojang.datafixers.util.Pair;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;

public class EldrinFumeTile extends WizardLabTile {

    public static final float GENERATION_RATE_PER_TICK = 0.01F;

    public static final int SLOT_FUEL = 0;

    public static final int SLOT_MOTE = 1;

    public static final int INVENTORY_SIZE = 2;

    private float fuelBurnTicks = 100.0F;

    private float moteBurnTicks = 0.0F;

    private float moteBurnTicksRemaining = 0.0F;

    private Affinity selectedAffinity = Affinity.UNKNOWN;

    public EldrinFumeTile(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize) {
        super(type, pos, state, inventorySize);
    }

    public EldrinFumeTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.ELDRIN_FUME.get(), pos, state, 2);
    }

    private boolean itemsPresent() {
        Pair<Affinity, Float> fuelValues = this.getFuelValues(this.f_58857_);
        return this.hasStack(0) && (this.moteBurnTicksRemaining > 0.0F || (Float) fuelValues.getSecond() > 0.0F && fuelValues.getFirst() != Affinity.UNKNOWN);
    }

    private Pair<Affinity, Float> getFuelValues(Level level) {
        Affinity affinity = Affinity.UNKNOWN;
        float amount = 0.0F;
        if (!this.hasStack(1)) {
            return new Pair(affinity, amount);
        } else {
            Optional<FumeFilterRecipe> recipe = level.getRecipeManager().<CraftingContainer, FumeFilterRecipe>getAllRecipesFor(RecipeInit.FUME_FILTER_TYPE.get()).stream().filter(r -> r.matches(ContainerTools.createTemporaryContainer(this.m_8020_(1)), level)).findFirst();
            return recipe.isEmpty() ? new Pair(affinity, amount) : new Pair(((FumeFilterRecipe) recipe.get()).getAffinity(), ((FumeFilterRecipe) recipe.get()).getTotalGeneration());
        }
    }

    @Override
    public boolean canActivate(Player player) {
        return this.itemsPresent() && ForgeHooks.getBurnTime(this.m_8020_(0), null) > 0;
    }

    @Override
    protected boolean canContinue() {
        this.injectPower();
        return true;
    }

    @Override
    public float getPctComplete() {
        return this.getFuelPctRemaining();
    }

    public float getMotePctRemaining() {
        return this.moteBurnTicksRemaining / this.moteBurnTicks;
    }

    public float getFuelPctRemaining() {
        return (float) this.getActiveTicks() / this.fuelBurnTicks;
    }

    @Override
    protected List<Integer> getSyncedInventorySlots() {
        return Arrays.asList(0, 1);
    }

    @Override
    protected boolean canActiveTick() {
        this.injectPower();
        return true;
    }

    private void injectPower() {
        if (!this.m_58904_().isClientSide() && this.selectedAffinity != null) {
            this.m_58904_().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> m.getWellspringRegistry().insertPower(this.getCrafterID(), this.f_58857_, this.selectedAffinity, 0.01F));
        }
    }

    @Override
    protected void onCraftStart(Player crafter) {
        ItemStack potentialFuel = this.m_8020_(0);
        this.fuelBurnTicks = (float) ForgeHooks.getBurnTime(potentialFuel, null);
        if (potentialFuel.hasCraftingRemainingItem()) {
            this.m_6836_(0, potentialFuel.getCraftingRemainingItem());
        } else {
            potentialFuel.shrink(1);
        }
        ItemStack burnableStack = this.m_8020_(1);
        if (!burnableStack.isEmpty()) {
            Pair<Affinity, Float> values = this.getFuelValues(this.f_58857_);
            if (values.getFirst() != Affinity.UNKNOWN && (Float) values.getSecond() > 0.0F) {
                this.selectedAffinity = (Affinity) values.getFirst();
                this.moteBurnTicks = (Float) values.getSecond();
                this.moteBurnTicksRemaining = this.moteBurnTicks;
                burnableStack.shrink(1);
            }
        }
        if (crafter != null && !crafter.m_9236_().isClientSide()) {
            MinecraftForge.EVENT_BUS.post(new GenericProgressionEvent(crafter, ProgressionEventIDs.ELDRIN_FUME_LIT));
            if (crafter instanceof ServerPlayer) {
                CustomAdvancementTriggers.LIGHT_FUME.trigger((ServerPlayer) crafter, this.selectedAffinity);
            }
        }
    }

    @Override
    protected void onComplete() {
        if (this.itemsPresent()) {
            ItemStack mote = this.m_8020_(1);
            if (mote.getItem() instanceof MoteItem) {
                this.selectedAffinity = ((MoteItem) mote.getItem()).getRelatedAffinity();
            }
            this.m_58904_().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> {
                if ((Float) m.getWellspringRegistry().getNodeNetworkAmountFor(this.getCrafterID(), this.f_58857_).getOrDefault(this.selectedAffinity, 0.0F) < 1000.0F) {
                    this.reactivate();
                }
            });
        }
    }

    @Override
    protected boolean needsPower() {
        return false;
    }

    @Override
    protected CompoundTag getMeta() {
        CompoundTag meta = new CompoundTag();
        meta.putFloat("burnTime", this.fuelBurnTicks);
        meta.putFloat("moteBurnTime", this.moteBurnTicks);
        meta.putFloat("moteBurnTimeRemaining", this.moteBurnTicksRemaining);
        meta.putInt("affinity", this.selectedAffinity.ordinal());
        return meta;
    }

    @Override
    protected void loadMeta(CompoundTag tag) {
        if (tag.contains("burnTime")) {
            this.fuelBurnTicks = tag.getFloat("burnTime");
        }
        if (tag.contains("moteBurnTime")) {
            this.moteBurnTicks = tag.getFloat("moteBurnTime");
        }
        if (tag.contains("moteBurnTimeRemaining")) {
            this.moteBurnTicksRemaining = tag.getFloat("moteBurnTimeRemaining");
        }
        if (tag.contains("affinity")) {
            this.selectedAffinity = Affinity.values()[tag.getInt("affinity")];
        }
    }

    @Override
    protected void tick() {
        super.tick();
        if (this.m_58904_().isClientSide()) {
            this.spawnParticles();
        } else if (this.isActive()) {
            this.moteBurnTicksRemaining--;
            if (this.moteBurnTicksRemaining < 1.0F) {
                this.setInactive();
            }
        }
    }

    private void spawnParticles() {
        if (this.isActive()) {
            int[] color = this.selectedAffinity.getColor();
            float colorMultiplier = 0.25F;
            float secondaryColorMultiplier = 0.075F;
            Vec3 particlePos = Vec3.upFromBottomCenterOf(this.m_58899_(), 1.2);
            this.m_58904_().addParticle(new MAParticleType(ParticleInit.FLAME.get()).setColor((int) ((float) color[0] * colorMultiplier), (int) ((float) color[1] * colorMultiplier), (int) ((float) color[2] * colorMultiplier)), particlePos.x, particlePos.y, particlePos.z, 0.0, 0.0, 0.0);
            this.m_58904_().addParticle(new MAParticleType(ParticleInit.FLAME.get()).setColor((int) ((float) color[0] * secondaryColorMultiplier), (int) ((float) color[1] * secondaryColorMultiplier), (int) ((float) color[2] * secondaryColorMultiplier)).setMaxAge(40), particlePos.x, particlePos.y, particlePos.z, 0.0, 0.015F, 0.0);
        }
    }

    public Affinity getGeneratingAffinity() {
        return this.selectedAffinity;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] { 1, 0 };
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
            return existing.isEmpty() || existing.getCount() < existing.getMaxStackSize() && ItemStack.isSameItemSameTags(stack, existing);
        }
    }
}