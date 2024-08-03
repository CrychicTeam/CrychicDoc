package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructBlockAreaTask;
import com.mna.api.tools.MATags;
import com.mna.api.tools.RLoc;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.tools.BlockUtils;
import java.util.EnumSet;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructHarvest extends ConstructBlockAreaTask<ConstructHarvest> {

    public static final ResourceLocation construct_harvestable = RLoc.create("blocks/construct_harvestables");

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.HARVEST };

    public ConstructHarvest(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public void tick() {
        super.m_8037_();
        if (this.currentTarget == null) {
            this.findBlockTarget();
        } else {
            if (this.moveToCurrentTarget()) {
                this.harvest();
            }
        }
    }

    private void harvest() {
        AbstractGolem c = this.getConstructAsEntity();
        BlockState state = c.m_9236_().getBlockState(this.currentTarget);
        this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.harvesting", new Object[] { this.translate(state), this.currentTarget.m_123341_(), this.currentTarget.m_123342_(), this.currentTarget.m_123343_() }));
        boolean success = false;
        if (this.construct.getIntelligence() >= 16) {
            Player player = FakePlayerFactory.getMinecraft((ServerLevel) this.construct.asEntity().m_9236_());
            player.m_146884_(this.construct.asEntity().m_20182_());
            success = this.harvestAndReplant(this.construct.asEntity().m_9236_(), this.currentTarget, state, player);
        } else {
            success = BlockUtils.destroyBlock((LivingEntity) (this.construct.getOwner() != null ? this.construct.getOwner() : c), c.m_9236_(), this.currentTarget, true, Tiers.NETHERITE);
        }
        this.knownTargets.remove(this.currentTarget);
        this.lastLocation = 0;
        this.currentTarget = null;
        this.exitCode = success ? 0 : 1;
    }

    private boolean harvestAndReplant(Level world, BlockPos pos, BlockState inWorld, Player player) {
        if (!(world instanceof ServerLevel)) {
            return false;
        } else {
            AbstractGolem c = this.getConstructAsEntity();
            Optional<IntegerProperty> ageProp = inWorld.m_61147_().stream().filter(p -> p.getName() == "age").map(p -> (IntegerProperty) p).findFirst();
            if (ageProp.isPresent() && !MATags.isBlockIn(inWorld.m_60734_(), MATags.Blocks.CONSTRUCT_HARVESTABLES_NO_AGE)) {
                if (inWorld.m_61143_((Property) ageProp.get()) != ((IntegerProperty) ageProp.get()).getPossibleValues().stream().max(Integer::compare).get()) {
                    return false;
                } else {
                    Item blockItem = inWorld.m_60734_().asItem();
                    Block.getDrops(inWorld, (ServerLevel) world, pos, world.getBlockEntity(pos), player, ItemStack.EMPTY).forEach(stack -> {
                        if (stack.getItem() == blockItem) {
                            stack.shrink(1);
                        }
                        if (!stack.isEmpty()) {
                            Block.popResource(world, pos, stack);
                        }
                    });
                    inWorld.m_222967_((ServerLevel) world, pos, ItemStack.EMPTY, true);
                    if (!world.isClientSide) {
                        BlockState newBlock = (BlockState) inWorld.m_61124_((Property) ageProp.get(), 0);
                        world.m_46796_(2001, pos, Block.getId(newBlock));
                        world.setBlockAndUpdate(pos, newBlock);
                    }
                    return true;
                }
            } else {
                return BlockUtils.destroyBlock((LivingEntity) (this.construct.getOwner() != null ? this.construct.getOwner() : c), c.m_9236_(), this.currentTarget, true, Tiers.NETHERITE);
            }
        }
    }

    @Override
    public boolean isValidBlock(BlockState state, BlockPos pos) {
        return validBlock(state);
    }

    public static boolean validBlock(BlockState state) {
        if (!MATags.isBlockIn(state.m_60734_(), MATags.Blocks.CONSTRUCT_HARVESTABLE_EXCLUDE) && !state.m_60795_()) {
            boolean no_age = MATags.isBlockIn(state.m_60734_(), RLoc.create("construct_harvestables_no_age"));
            boolean valid = no_age || MATags.isBlockIn(state.m_60734_(), RLoc.create("construct_harvestables"));
            if (state.m_60734_() instanceof CropBlock) {
                valid &= ((CropBlock) state.m_60734_()).isMaxAge(state);
            } else {
                Optional<IntegerProperty> ageProp = state.m_61147_().stream().filter(p -> p.getName() == "age").map(p -> (IntegerProperty) p).findFirst();
                if (ageProp.isPresent() && !no_age) {
                    int maximum = (Integer) ((IntegerProperty) ageProp.get()).getPossibleValues().stream().max(Integer::compare).get();
                    int value = (Integer) state.m_61143_((Property) ageProp.get());
                    valid &= value == maximum;
                }
            }
            return valid;
        } else {
            return false;
        }
    }

    @Override
    protected int getInteractTimer() {
        return this.getInteractTime(ConstructCapability.HARVEST);
    }

    @Override
    public void start() {
        super.m_8056_();
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.interactTimer = this.getInteractTime(ConstructCapability.HARVEST);
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.HARVEST);
    }

    public ConstructHarvest duplicate() {
        return new ConstructHarvest(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructHarvest copyFrom(ConstructAITask<?> other) {
        super.copyFrom(other);
        return this;
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
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
        return "harvest.area";
    }
}