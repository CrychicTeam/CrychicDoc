package com.mna.blocks.tileentities;

import com.mna.api.blocks.tile.TileEntityWithInventory;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.tools.MATags;
import com.mna.api.tools.RLoc;
import com.mna.blocks.BlockInit;
import com.mna.blocks.runeforging.PedestalBlock;
import com.mna.blocks.runeforging.RuneforgeBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.arcanefurnace.ArcaneFurnaceRecipe;
import com.mna.tools.ContainerTools;
import com.mna.tools.InventoryUtilities;
import com.mna.tools.math.MathUtils;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.extensions.IForgeBlockEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class RuneForgeTile extends TileEntityWithInventory implements IForgeBlockEntity {

    private static final ResourceLocation tag_blacklist = RLoc.create("arcane_furnace_doubling_blacklist");

    private static final int MAX_ITEMS = 64;

    public static final int INVENTORY_SLOT_INDEX = 0;

    int smeltTicks = 0;

    int burnTime = 0;

    private boolean ignoreRecipeCheck = false;

    private boolean isRepairing = false;

    private ItemStack __cachedRecipeOutput;

    private ArcaneFurnaceRecipe __cachedRecipe;

    public RuneForgeTile(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state, 64);
    }

    public RuneForgeTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.RUNEFORGE.get(), pos, state);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        super.setItem(index, stack);
        if (!this.m_58904_().isClientSide()) {
            boolean active = false;
            this.m_58904_().setBlock(this.m_58899_(), (BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_58900_().m_61124_(RuneforgeBlock.ACTIVE, false)).m_61124_(RuneforgeBlock.ORE_DOUBLING, this.hasMultiUpgrade())).m_61124_(RuneforgeBlock.REPAIR, this.hasRepairUpgrade())).m_61124_(RuneforgeBlock.SPEED, this.hasSpeedUpgrade()), 1);
            if (!this.ignoreRecipeCheck) {
                active = this.cacheRecipe();
            }
            if (active) {
                this.smeltTicks = 0;
                this.m_58904_().setBlock(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(RuneforgeBlock.ACTIVE, true), 3);
            }
            this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
        }
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack output = super.removeItem(index, count);
        if (!output.isEmpty()) {
            this.m_58904_().setBlock(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(RuneforgeBlock.ACTIVE, false), 3);
        } else {
            this.m_58904_().setBlock(this.m_58899_(), this.m_58900_(), 3);
        }
        return output;
    }

    @Override
    public void setChanged() {
        if (this.f_58857_ != null) {
            this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
        }
        super.m_6596_();
    }

    private boolean cacheRecipe() {
        CraftingContainer inv = ContainerTools.createTemporaryContainer(this.m_8020_(0));
        ArcaneFurnaceRecipe smeltRecipe = (ArcaneFurnaceRecipe) this.m_58904_().getRecipeManager().getRecipeFor(RecipeInit.ARCANE_FURNACE_TYPE.get(), inv, this.f_58857_).orElse(null);
        this.__cachedRecipeOutput = null;
        ItemStack ingredient = this.m_8020_(0);
        if (this.hasRepairUpgrade()) {
            if (!MATags.isItemEqual(ingredient, tag_blacklist)) {
                this.isRepairing = true;
                return true;
            } else {
                return false;
            }
        } else {
            boolean ret = false;
            if (smeltRecipe != null) {
                this.__cachedRecipe = smeltRecipe;
                this.__cachedRecipeOutput = smeltRecipe.getResultItem();
                this.__cachedRecipeOutput.setCount(this.__cachedRecipeOutput.getCount() * this.m_8020_(0).getCount());
                this.burnTime = smeltRecipe.getBurnTime() * this.m_8020_(0).getCount();
                ret = true;
            } else {
                Optional<CampfireCookingRecipe> cook_recipe = this.m_58904_().getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, inv, this.f_58857_);
                if (cook_recipe.isPresent()) {
                    this.__cachedRecipeOutput = ((CampfireCookingRecipe) cook_recipe.get()).m_5874_(inv, this.f_58857_.registryAccess());
                    this.__cachedRecipeOutput.setCount(this.__cachedRecipeOutput.getCount() * this.m_8020_(0).getCount());
                    this.burnTime = ((CampfireCookingRecipe) cook_recipe.get()).m_43753_() / 2 * this.m_8020_(0).getCount();
                    ret = true;
                } else {
                    Optional<SmeltingRecipe> furnace_recipe = this.m_58904_().getRecipeManager().getRecipeFor(RecipeType.SMELTING, inv, this.f_58857_);
                    if (furnace_recipe.isPresent()) {
                        this.__cachedRecipeOutput = ((SmeltingRecipe) furnace_recipe.get()).m_5874_(inv, this.f_58857_.registryAccess());
                        this.__cachedRecipeOutput.setCount(this.__cachedRecipeOutput.getCount() * this.m_8020_(0).getCount());
                        this.burnTime = ((SmeltingRecipe) furnace_recipe.get()).m_43753_() * 2 * this.m_8020_(0).getCount();
                        ret = true;
                    } else {
                        Optional<BlastingRecipe> blasting_recipe = this.m_58904_().getRecipeManager().getRecipeFor(RecipeType.BLASTING, inv, this.f_58857_);
                        if (blasting_recipe.isPresent()) {
                            this.__cachedRecipeOutput = ((BlastingRecipe) blasting_recipe.get()).m_5874_(inv, this.f_58857_.registryAccess());
                            this.__cachedRecipeOutput.setCount(this.__cachedRecipeOutput.getCount() * this.m_8020_(0).getCount());
                            this.burnTime = ((BlastingRecipe) blasting_recipe.get()).m_43753_() * 2 * this.m_8020_(0).getCount();
                            ret = true;
                        }
                    }
                }
            }
            if ((Integer) this.m_58900_().m_61143_(RuneforgeBlock.MATERIAL) == 1) {
                this.burnTime /= 8;
            }
            if ((Boolean) this.m_58900_().m_61143_(RuneforgeBlock.SPEED)) {
                this.burnTime /= 6;
            }
            if (ret && this.burnTime < 1) {
                this.burnTime = 1;
            }
            if (!MATags.isItemEqual(ingredient, tag_blacklist) && ret && this.hasMultiUpgrade()) {
                this.__cachedRecipeOutput.setCount(Math.min(this.__cachedRecipeOutput.getCount() * 2, 64));
            }
            return ret;
        }
    }

    private boolean hasMultiUpgrade() {
        switch((Direction) this.m_58900_().m_61143_(RuneforgeBlock.FACING)) {
            case EAST:
            case WEST:
                return this.getPedestalUpgradeType(new BlockPos(Direction.NORTH.getStepX(), Direction.NORTH.getStepY(), Direction.NORTH.getStepZ())) == RuneForgeTile.CrystalUpgrade.DOUBLE && this.getPedestalUpgradeType(new BlockPos(Direction.SOUTH.getStepX(), Direction.SOUTH.getStepY(), Direction.SOUTH.getStepZ())) == RuneForgeTile.CrystalUpgrade.DOUBLE;
            case NORTH:
            case SOUTH:
            default:
                return this.getPedestalUpgradeType(new BlockPos(Direction.EAST.getStepX(), Direction.EAST.getStepY(), Direction.EAST.getStepZ())) == RuneForgeTile.CrystalUpgrade.DOUBLE && this.getPedestalUpgradeType(new BlockPos(Direction.WEST.getStepX(), Direction.WEST.getStepY(), Direction.WEST.getStepZ())) == RuneForgeTile.CrystalUpgrade.DOUBLE;
        }
    }

    private boolean hasRepairUpgrade() {
        switch((Direction) this.m_58900_().m_61143_(RuneforgeBlock.FACING)) {
            case EAST:
            case WEST:
                return this.getPedestalUpgradeType(new BlockPos(Direction.NORTH.getStepX(), Direction.NORTH.getStepY(), Direction.NORTH.getStepZ())) == RuneForgeTile.CrystalUpgrade.REPAIR && this.getPedestalUpgradeType(new BlockPos(Direction.SOUTH.getStepX(), Direction.SOUTH.getStepY(), Direction.SOUTH.getStepZ())) == RuneForgeTile.CrystalUpgrade.REPAIR;
            case NORTH:
            case SOUTH:
            default:
                return this.getPedestalUpgradeType(new BlockPos(Direction.EAST.getStepX(), Direction.EAST.getStepY(), Direction.EAST.getStepZ())) == RuneForgeTile.CrystalUpgrade.REPAIR && this.getPedestalUpgradeType(new BlockPos(Direction.WEST.getStepX(), Direction.WEST.getStepY(), Direction.WEST.getStepZ())) == RuneForgeTile.CrystalUpgrade.REPAIR;
        }
    }

    private boolean hasSpeedUpgrade() {
        switch((Direction) this.m_58900_().m_61143_(RuneforgeBlock.FACING)) {
            case EAST:
            case WEST:
                return this.getPedestalUpgradeType(new BlockPos(Direction.NORTH.getStepX(), Direction.NORTH.getStepY(), Direction.NORTH.getStepZ())) == RuneForgeTile.CrystalUpgrade.SPEED && this.getPedestalUpgradeType(new BlockPos(Direction.SOUTH.getStepX(), Direction.SOUTH.getStepY(), Direction.SOUTH.getStepZ())) == RuneForgeTile.CrystalUpgrade.SPEED;
            case NORTH:
            case SOUTH:
            default:
                return this.getPedestalUpgradeType(new BlockPos(Direction.EAST.getStepX(), Direction.EAST.getStepY(), Direction.EAST.getStepZ())) == RuneForgeTile.CrystalUpgrade.SPEED && this.getPedestalUpgradeType(new BlockPos(Direction.WEST.getStepX(), Direction.WEST.getStepY(), Direction.WEST.getStepZ())) == RuneForgeTile.CrystalUpgrade.SPEED;
        }
    }

    private RuneForgeTile.CrystalUpgrade getPedestalUpgradeType(BlockPos offset) {
        if (this.m_58904_().getBlockState(this.f_58858_.offset(offset)).m_60734_() instanceof PedestalBlock) {
            PedestalTile pedestal = (PedestalTile) this.f_58857_.getBlockEntity(this.f_58858_.offset(offset));
            ItemStack stack = pedestal.m_8020_(0);
            if (!stack.isEmpty() && stack.getItem() instanceof BlockItem) {
                BlockItem blockItem = (BlockItem) stack.getItem();
                if (blockItem.getBlock() == BlockInit.CHIMERITE_CRYSTAL_ORANGE.get() || blockItem.getBlock() == BlockInit.CHIMERITE_CRYSTAL_RED.get() || blockItem.getBlock() == BlockInit.CHIMERITE_CRYSTAL_YELLOW.get()) {
                    return RuneForgeTile.CrystalUpgrade.DOUBLE;
                }
                if (blockItem.getBlock() == BlockInit.CHIMERITE_CRYSTAL_BLUE.get() || blockItem.getBlock() == BlockInit.CHIMERITE_CRYSTAL_CYAN.get() || blockItem.getBlock() == BlockInit.CHIMERITE_CRYSTAL_LIGHT_BLUE.get() || blockItem.getBlock() == BlockInit.CHIMERITE_CRYSTAL_WHITE.get()) {
                    return RuneForgeTile.CrystalUpgrade.REPAIR;
                }
                if (blockItem.getBlock() == BlockInit.CHIMERITE_CRYSTAL_PURPLE.get() || blockItem.getBlock() == BlockInit.CHIMERITE_CRYSTAL_MAGENTA.get() || blockItem.getBlock() == BlockInit.CHIMERITE_CRYSTAL_PINK.get()) {
                    return RuneForgeTile.CrystalUpgrade.SPEED;
                }
            }
        }
        return RuneForgeTile.CrystalUpgrade.NONE;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack ret = super.removeItemNoUpdate(index);
        if (!this.m_58904_().isClientSide()) {
            this.m_58904_().setBlock(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(RuneforgeBlock.ACTIVE, false), 3);
            this.smeltTicks = 0;
        }
        return ret;
    }

    @Override
    public int getContainerSize() {
        return 64;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag base = super.m_5995_();
        CompoundTag sub = new CompoundTag();
        this.m_8020_(0).save(sub);
        base.put("invSync", sub);
        base.putBoolean("repairing", this.isRepairing);
        base.putInt("burnTime", this.burnTime);
        base.putInt("smeltTicks", this.smeltTicks);
        return base;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        CompoundTag sub = tag.getCompound("invSync");
        this.setItem(0, ItemStack.of(sub));
        this.isRepairing = tag.getBoolean("repairing");
        this.burnTime = tag.getInt("burnTime");
        this.smeltTicks = tag.getInt("smeltTicks");
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag data = pkt.getTag();
        CompoundTag sub = data.getCompound("invSync");
        this.setItem(0, ItemStack.of(sub));
        this.isRepairing = data.getBoolean("repairing");
        this.burnTime = data.getInt("burnTime");
        this.smeltTicks = data.getInt("smeltTicks");
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    public ItemStack getDisplayedItem() {
        return this.m_8020_(0);
    }

    public boolean MatchesReagent(ResourceLocation rLoc) {
        return this.m_8020_(0).isEmpty() ? false : MATags.isItemEqual(this.m_8020_(0), rLoc);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("smelt_ticks", this.smeltTicks);
        compound.putInt("burn_ticks", this.burnTime);
        compound.putBoolean("repairing", this.isRepairing);
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("smelt_ticks")) {
            this.smeltTicks = compound.getInt("smelt_ticks");
        }
        if (compound.contains("burn_ticks")) {
            this.burnTime = compound.getInt("burn_ticks");
        }
        if (compound.contains("repairing")) {
            this.isRepairing = compound.getBoolean("repairing");
        }
        super.load(compound);
    }

    public static void Tick(Level level, BlockPos pos, BlockState state, RuneForgeTile tile) {
        if ((Boolean) state.m_61143_(RuneforgeBlock.ACTIVE)) {
            if (!level.isClientSide()) {
                if (tile.hasRepairUpgrade()) {
                    tile.tickLogic_repair();
                } else {
                    tile.tickLogic_smelt();
                }
            } else {
                if (tile.hasRepairUpgrade()) {
                    tile.tickLogic_repair();
                } else {
                    tile.smeltTicks++;
                }
                tile.spawnParticles();
            }
        }
    }

    private void tickLogic_repair() {
        ItemStack stack = this.m_8020_(0);
        if (stack.isEmpty() || !stack.isRepairable() || !stack.isDamaged() || GeneralConfigValues.RepairBlacklist.contains(ForgeRegistries.ITEMS.getKey(stack.getItem()).toString())) {
            this.resetSelf();
        }
        this.smeltTicks++;
        int repair_rate = this.m_58900_().m_61143_(RuneforgeBlock.MATERIAL) == 1 ? 1 : 5;
        if (this.smeltTicks % repair_rate == 0) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
    }

    private void tickLogic_smelt() {
        if (this.__cachedRecipeOutput == null && !this.cacheRecipe()) {
            this.resetSelf();
        } else {
            this.smeltTicks++;
            if (this.smeltTicks >= this.burnTime) {
                this.ignoreRecipeCheck = true;
                this.setItem(0, this.__cachedRecipeOutput.copy());
                if (this.__cachedRecipe != null) {
                    for (ItemStack byproduct : this.__cachedRecipe.rollByproducts(this.f_58857_.getRandom())) {
                        InventoryUtilities.DropItemAt(byproduct, Vec3.atCenterOf(this.m_58899_().above()), this.f_58857_, true);
                    }
                }
                this.__cachedRecipe = null;
                this.ignoreRecipeCheck = false;
                this.resetSelf();
                this.m_58904_().setBlock(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(RuneforgeBlock.ACTIVE, false), 2);
            }
        }
    }

    private void resetSelf() {
        this.smeltTicks = 0;
        this.burnTime = 0;
        this.isRepairing = false;
        if (!this.m_58904_().isClientSide()) {
            this.m_58904_().setBlock(this.m_58899_(), (BlockState) this.m_58900_().m_61124_(RuneforgeBlock.ACTIVE, false), 2);
        }
    }

    private void spawnParticles() {
        BlockState state = this.m_58900_();
        if ((Boolean) state.m_61143_(RuneforgeBlock.ACTIVE)) {
            this.spawnActiveParticles(state);
            if ((Boolean) state.m_61143_(RuneforgeBlock.ORE_DOUBLING)) {
                this.spawnDoublingParticles(state);
            }
            if ((Boolean) state.m_61143_(RuneforgeBlock.SPEED)) {
                this.spawnSpeedParticles(state);
            }
            if ((Boolean) state.m_61143_(RuneforgeBlock.REPAIR)) {
                this.spawnRepairParticles(state);
            }
        }
    }

    private void spawnActiveParticles(BlockState state) {
        float xOffset = 0.5F;
        float zOffset = 0.5F;
        float yOffset = 0.95F;
        float spread = 0.15F;
        float upVel = this.isRepairing ? 0.03F : 0.01F;
        MAParticleType particle = new MAParticleType(this.isRepairing ? ParticleInit.ARCANE.get() : ParticleInit.FLAME.get());
        if ((Integer) state.m_61143_(RuneforgeBlock.MATERIAL) == 0) {
            particle.setColor(20, 40, 60);
        } else {
            particle.setScale(0.06F);
            particle.setMaxAge(8);
            upVel = 0.06F;
            if (Math.random() < 0.6F) {
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get()).setColor(156, 60, 30).setGravity(0.01F).setMaxAge(20), (double) ((float) this.f_58858_.m_123341_() + xOffset + -spread + this.f_58857_.getRandom().nextFloat() * spread * 2.0F), (double) ((float) this.f_58858_.m_123342_() + yOffset), (double) ((float) this.f_58858_.m_123343_() + zOffset + -spread + this.f_58857_.getRandom().nextFloat() * spread * 2.0F), -0.05 + Math.random() * 0.1, 0.06, -0.05 + Math.random() * 0.1);
            }
        }
        for (int i = 0; i < 2; i++) {
            this.m_58904_().addParticle(particle, (double) ((float) this.f_58858_.m_123341_() + xOffset + -spread + this.f_58857_.getRandom().nextFloat() * spread * 2.0F), (double) ((float) this.f_58858_.m_123342_() + yOffset), (double) ((float) this.f_58858_.m_123343_() + zOffset + -spread + this.f_58857_.getRandom().nextFloat() * spread * 2.0F), 0.0, (double) upVel, 0.0);
        }
    }

    private void spawnDoublingParticles(BlockState state) {
        BlockPos offset_1 = null;
        BlockPos offset_2 = null;
        switch((Direction) this.m_58900_().m_61143_(RuneforgeBlock.FACING)) {
            case EAST:
            case WEST:
                offset_1 = new BlockPos(Direction.NORTH.getStepX(), Direction.NORTH.getStepY(), Direction.NORTH.getStepZ());
                offset_2 = new BlockPos(Direction.SOUTH.getStepX(), Direction.SOUTH.getStepY(), Direction.SOUTH.getStepZ());
                break;
            case NORTH:
            case SOUTH:
            default:
                offset_1 = new BlockPos(Direction.EAST.getStepX(), Direction.EAST.getStepY(), Direction.EAST.getStepZ());
                offset_2 = new BlockPos(Direction.WEST.getStepX(), Direction.WEST.getStepY(), Direction.WEST.getStepZ());
        }
        if (offset_1 != null && offset_2 != null) {
            for (int i = 0; i < 5; i++) {
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.FLAME_ORBIT.get()), (double) ((float) (this.f_58858_.m_123341_() + offset_1.m_123341_()) + 0.5F), (double) ((float) (this.f_58858_.m_123342_() + offset_1.m_123342_()) + 0.75F), (double) ((float) (this.f_58858_.m_123343_() + offset_1.m_123343_()) + 0.5F), 0.1F, 0.02F, 0.1F);
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.FLAME_ORBIT.get()), (double) ((float) (this.f_58858_.m_123341_() + offset_2.m_123341_()) + 0.5F), (double) ((float) (this.f_58858_.m_123342_() + offset_2.m_123342_()) + 0.75F), (double) ((float) (this.f_58858_.m_123343_() + offset_2.m_123343_()) + 0.5F), 0.1F, 0.02F, 0.1F);
            }
        }
    }

    private void spawnSpeedParticles(BlockState state) {
        BlockPos offset_1 = null;
        BlockPos offset_2 = null;
        switch((Direction) this.m_58900_().m_61143_(RuneforgeBlock.FACING)) {
            case EAST:
            case WEST:
                offset_1 = new BlockPos(Direction.NORTH.getStepX(), Direction.NORTH.getStepY(), Direction.NORTH.getStepZ());
                offset_2 = new BlockPos(Direction.SOUTH.getStepX(), Direction.SOUTH.getStepY(), Direction.SOUTH.getStepZ());
                break;
            case NORTH:
            case SOUTH:
            default:
                offset_1 = new BlockPos(Direction.EAST.getStepX(), Direction.EAST.getStepY(), Direction.EAST.getStepZ());
                offset_2 = new BlockPos(Direction.WEST.getStepX(), Direction.WEST.getStepY(), Direction.WEST.getStepZ());
        }
        if (offset_1 != null && offset_2 != null) {
            for (int i = 0; i < 5; i++) {
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.FLAME_ORBIT.get()).setColor(91, 36, 143), (double) ((float) (this.f_58858_.m_123341_() + offset_1.m_123341_()) + 0.5F), (double) ((float) (this.f_58858_.m_123342_() + offset_1.m_123342_()) + 0.75F), (double) ((float) (this.f_58858_.m_123343_() + offset_1.m_123343_()) + 0.5F), 0.1F, 0.02F, 0.1F);
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.FLAME_ORBIT.get()).setColor(91, 36, 143), (double) ((float) (this.f_58858_.m_123341_() + offset_2.m_123341_()) + 0.5F), (double) ((float) (this.f_58858_.m_123342_() + offset_2.m_123342_()) + 0.75F), (double) ((float) (this.f_58858_.m_123343_() + offset_2.m_123343_()) + 0.5F), 0.1F, 0.02F, 0.1F);
            }
        }
    }

    private void spawnRepairParticles(BlockState state) {
        BlockPos offset_1 = null;
        BlockPos offset_2 = null;
        switch((Direction) this.m_58900_().m_61143_(RuneforgeBlock.FACING)) {
            case EAST:
            case WEST:
                offset_1 = new BlockPos(Direction.NORTH.getStepX(), Direction.NORTH.getStepY(), Direction.NORTH.getStepZ());
                offset_2 = new BlockPos(Direction.SOUTH.getStepX(), Direction.SOUTH.getStepY(), Direction.SOUTH.getStepZ());
                break;
            case NORTH:
            case SOUTH:
            default:
                offset_1 = new BlockPos(Direction.EAST.getStepX(), Direction.EAST.getStepY(), Direction.EAST.getStepZ());
                offset_2 = new BlockPos(Direction.WEST.getStepX(), Direction.WEST.getStepY(), Direction.WEST.getStepZ());
        }
        if (offset_1 != null && offset_2 != null) {
            for (int i = 0; i < 5; i++) {
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_ORBIT.get()), (double) ((float) (this.f_58858_.m_123341_() + offset_1.m_123341_()) + 0.5F), (double) ((float) (this.f_58858_.m_123342_() + offset_1.m_123342_()) + 0.75F), (double) ((float) (this.f_58858_.m_123343_() + offset_1.m_123343_()) + 0.5F), 0.1F, 0.02F, 0.1F);
                this.m_58904_().addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_ORBIT.get()), (double) ((float) (this.f_58858_.m_123341_() + offset_2.m_123341_()) + 0.5F), (double) ((float) (this.f_58858_.m_123342_() + offset_2.m_123342_()) + 0.75F), (double) ((float) (this.f_58858_.m_123343_() + offset_2.m_123343_()) + 0.5F), 0.1F, 0.02F, 0.1F);
            }
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return index == 0 ? this.m_8020_(0).isEmpty() : false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        if (!ItemStack.matches(this.m_8020_(index), stack) || (Boolean) this.m_58900_().m_61143_(RuneforgeBlock.ACTIVE)) {
            return false;
        } else {
            return index == 0 ? !this.m_8020_(0).isEmpty() : false;
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] { 0 };
    }

    public float getBurnPct() {
        return this.burnTime == 0 ? 0.0F : MathUtils.clamp01((float) this.smeltTicks / (float) this.burnTime);
    }

    private static enum CrystalUpgrade {

        NONE, DOUBLE, REPAIR, SPEED
    }
}