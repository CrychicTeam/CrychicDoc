package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.tools.RLoc;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import com.mna.tools.math.MathUtils;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructMine extends ConstructAITask<ConstructMine> {

    private static final ResourceLocation MINING_LOOT_OVERWORLD = RLoc.create("entities/construct/mining_minecraft_overworld");

    private static final ConstructCapability[] required_capabilities = new ConstructCapability[] { ConstructCapability.MINE, ConstructCapability.CARRY };

    protected BlockPos blockPos;

    private static final int WANDER_DIST = 5;

    protected int waitCount = 0;

    protected boolean isWaiting = false;

    public ConstructMine(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isWaiting) {
            this.setMoveTarget(this.blockPos);
            if (this.doMove()) {
                this.construct.setMining(true);
                this.waitCount = 0;
                this.isWaiting = true;
            }
        } else {
            this.waitCount++;
            if (this.waitCount >= this.getWaitTime()) {
                this.construct.resetActions();
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.mine_success", new Object[0]));
                this.exitCode = 0;
                return;
            }
            if (this.waitCount == this.getWaitTime() - 30) {
                this.construct.setHappy(100);
                this.construct.setMining(false);
                this.rollLoot();
            }
        }
    }

    @Override
    public void onTaskSet() {
        super.onTaskSet();
        this.waitCount = 0;
        this.isWaiting = false;
        AbstractGolem c = this.getConstructAsEntity();
        int rX = (int) c.m_20208_(5.0);
        int rY = (int) c.m_20187_();
        int rZ = (int) c.m_20262_(5.0);
        this.blockPos = new BlockPos(rX, rY, rZ);
        BlockState state = c.m_9236_().getBlockState(this.blockPos);
        int count;
        for (count = 0; !state.m_60795_() && count < 10; count++) {
            this.blockPos = this.blockPos.offset(0, 1, 0);
            state = c.m_9236_().getBlockState(this.blockPos);
        }
        while (state.m_60795_() && count < 10) {
            this.blockPos = this.blockPos.offset(0, -1, 0);
            state = c.m_9236_().getBlockState(this.blockPos);
            count++;
        }
        InteractionHand[] hands = this.construct.getCarryingHands();
        for (int i = 0; i < hands.length; i++) {
            ItemStack held = c.m_21120_(hands[i]);
            BlockPos dropPos = c.m_20183_();
            ItemEntity item = new ItemEntity(c.m_9236_(), (double) dropPos.m_123341_() + 0.5, (double) dropPos.m_123342_() + 0.5, (double) dropPos.m_123343_() + 0.5, held.copy());
            item.m_20334_(0.0, 0.25, 0.0);
            item.setDefaultPickUpDelay();
            c.m_9236_().m_7967_(item);
            c.m_21008_(hands[i], ItemStack.EMPTY);
        }
    }

    private void rollLoot() {
        int num_items = Math.max(this.construct.getEmptyHands().length, 1);
        if (this.construct.getConstructData().isCapabilityEnabled(ConstructCapability.ITEM_STORAGE)) {
            num_items += 1 + MathUtils.weightedRandomNumber(15);
        }
        ServerLevel serverWorld = (ServerLevel) this.construct.asEntity().m_9236_();
        ResourceLocation lootTableId = RLoc.create("entities/construct/mining_" + serverWorld.m_46472_().location().toString().replace(':', '_'));
        LootTable loottable = serverWorld.getServer().getLootData().m_278676_(lootTableId);
        if (loottable == LootTable.EMPTY) {
            loottable = serverWorld.getServer().getLootData().m_278676_(MINING_LOOT_OVERWORLD);
        }
        if (loottable != null) {
            LootParams lootparams = new LootParams.Builder((ServerLevel) this.construct.asEntity().m_9236_()).withParameter(LootContextParams.THIS_ENTITY, this.construct.asEntity()).withParameter(LootContextParams.ORIGIN, this.construct.asEntity().m_20182_()).withParameter(LootContextParams.DAMAGE_SOURCE, this.construct.asEntity().m_269291_().generic()).create(LootContextParamSets.ENTITY);
            List<ItemStack> items = loottable.getRandomItems(lootparams);
            for (int i = 0; i < Math.min(num_items, items.size()); i++) {
                this.insertOrDropItem((ItemStack) items.get(i));
            }
        }
    }

    private int getWaitTime() {
        return !this.construct.getConstructData().areCapabilitiesEnabled(ConstructCapability.ITEM_STORAGE) ? this.getInteractTime(ConstructCapability.MINE, 9600) : this.getInteractTime(ConstructCapability.MINE, 4800);
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.MINE);
    }

    @Override
    public CompoundTag writeInternal(CompoundTag nbt) {
        return nbt;
    }

    @Override
    public void readNBT(CompoundTag nbt) {
    }

    @Override
    public boolean isFullyConfigured() {
        return true;
    }

    @Override
    public void inflateParameters() {
    }

    public ConstructMine copyFrom(ConstructAITask<?> other) {
        return this;
    }

    public ConstructMine duplicate() {
        return new ConstructMine(this.construct, this.guiIcon).copyFrom(this);
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return required_capabilities;
    }

    @Override
    public int getRequiredIntelligence() {
        return 16;
    }
}