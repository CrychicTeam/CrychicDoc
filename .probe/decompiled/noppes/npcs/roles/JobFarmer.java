package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.data.role.IJobFarmer;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.controllers.MassBlockController;
import noppes.npcs.controllers.data.BlockData;
import noppes.npcs.entity.EntityNPCInterface;

public class JobFarmer extends JobInterface implements MassBlockController.IMassBlock, IJobFarmer {

    public int chestMode = 1;

    private List<BlockPos> trackedBlocks = new ArrayList();

    private int ticks = 0;

    private int walkTicks = 0;

    private int blockTicks = 800;

    private boolean waitingForBlocks = false;

    private BlockPos ripe = null;

    private BlockPos chest = null;

    private ItemStack holding = ItemStack.EMPTY;

    public JobFarmer(EntityNPCInterface npc) {
        super(npc);
        this.overrideMainHand = true;
    }

    @Override
    public IItemStack getMainhand() {
        String name = this.npc.getJobData();
        ItemStack item = this.stringToItem(name);
        return item.isEmpty() ? (IItemStack) this.npc.inventory.weapons.get(0) : NpcAPI.Instance().getIItemStack(item);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("JobChestMode", this.chestMode);
        if (!this.holding.isEmpty()) {
            compound.put("JobHolding", this.holding.save(new CompoundTag()));
        }
        return compound;
    }

    @Override
    public void load(CompoundTag compound) {
        this.chestMode = compound.getInt("JobChestMode");
        this.holding = ItemStack.of(compound.getCompound("JobHolding"));
        this.blockTicks = 1100;
    }

    public void setHolding(ItemStack item) {
        this.holding = item;
        this.npc.setJobData(this.itemToString(this.holding));
    }

    @Override
    public boolean aiShouldExecute() {
        if (!this.holding.isEmpty()) {
            if (this.chestMode == 0) {
                this.setHolding(ItemStack.EMPTY);
            } else if (this.chestMode == 1) {
                if (this.chest == null) {
                    this.dropItem(this.holding);
                    this.setHolding(ItemStack.EMPTY);
                } else {
                    this.chest();
                }
            } else if (this.chestMode == 2) {
                this.dropItem(this.holding);
                this.setHolding(ItemStack.EMPTY);
            }
            return false;
        } else if (this.ripe != null) {
            this.pluck();
            return false;
        } else {
            if (!this.waitingForBlocks && this.blockTicks++ > 1200) {
                this.blockTicks = 0;
                this.waitingForBlocks = true;
                MassBlockController.Queue(this);
            }
            if (this.ticks++ < 100) {
                return false;
            } else {
                this.ticks = 0;
                return true;
            }
        }
    }

    private void dropItem(ItemStack item) {
        ItemEntity entityitem = new ItemEntity(this.npc.m_9236_(), this.npc.m_20185_(), this.npc.m_20186_(), this.npc.m_20189_(), item);
        entityitem.setDefaultPickUpDelay();
        this.npc.m_9236_().m_7967_(entityitem);
    }

    private void chest() {
        BlockPos pos = this.chest;
        this.npc.m_21573_().moveTo((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), 1.0);
        this.npc.m_21563_().setLookAt((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), 10.0F, (float) this.npc.m_8132_());
        if (this.npc.nearPosition(pos) || this.walkTicks++ > 400) {
            if (this.walkTicks < 400) {
                this.npc.m_6674_(InteractionHand.MAIN_HAND);
            }
            this.npc.m_21573_().stop();
            this.ticks = 100;
            this.walkTicks = 0;
            BlockState state = this.npc.m_9236_().getBlockState(pos);
            BlockEntity tile = this.npc.m_9236_().getBlockEntity(pos);
            Container inventory = tile instanceof Container ? (Container) tile : null;
            if (state.m_60734_() instanceof ChestBlock) {
                inventory = ChestBlock.getContainer((ChestBlock) state.m_60734_(), state, this.npc.m_9236_(), pos, true);
            }
            if (inventory == null) {
                this.chest = null;
            } else {
                for (int i = 0; !this.holding.isEmpty() && i < inventory.getContainerSize(); i++) {
                    this.holding = this.mergeStack(inventory, i, this.holding);
                }
                for (int i = 0; !this.holding.isEmpty() && i < inventory.getContainerSize(); i++) {
                    ItemStack item = inventory.getItem(i);
                    if (item.isEmpty()) {
                        inventory.setItem(i, this.holding);
                        this.holding = ItemStack.EMPTY;
                    }
                }
                if (!this.holding.isEmpty()) {
                    this.dropItem(this.holding);
                    this.holding = ItemStack.EMPTY;
                }
            }
            this.setHolding(this.holding);
        }
    }

    private ItemStack mergeStack(Container inventory, int slot, ItemStack item) {
        ItemStack item2 = inventory.getItem(slot);
        if (!NoppesUtilPlayer.compareItems(item, item2, false, false)) {
            return item;
        } else {
            int size = item2.getMaxStackSize() - item2.getCount();
            if (size >= item.getCount()) {
                item2.setCount(item2.getCount() + item.getCount());
                return ItemStack.EMPTY;
            } else {
                item2.setCount(item2.getMaxStackSize());
                item.setCount(item.getCount() - size);
                return item.isEmpty() ? ItemStack.EMPTY : item;
            }
        }
    }

    private void pluck() {
        BlockPos pos = this.ripe;
        this.npc.m_21573_().moveTo((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), 1.0);
        this.npc.m_21563_().setLookAt((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), 10.0F, (float) this.npc.m_8132_());
        if (this.npc.nearPosition(pos) || this.walkTicks++ > 400) {
            if (this.walkTicks > 400) {
                pos = NoppesUtilServer.GetClosePos(pos, this.npc.m_9236_());
                this.npc.m_6021_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5);
            }
            this.ripe = null;
            this.npc.m_21573_().stop();
            this.ticks = 90;
            this.walkTicks = 0;
            this.npc.m_6674_(InteractionHand.MAIN_HAND);
            BlockState state = this.npc.m_9236_().getBlockState(pos);
            Block b = state.m_60734_();
            if (b instanceof CropBlock && ((CropBlock) b).isMaxAge(state)) {
                CropBlock crop = (CropBlock) b;
                Item item = crop.getCloneItemStack(this.npc.m_9236_(), pos, state).getItem();
                LootParams.Builder builder = new LootParams.Builder((ServerLevel) this.npc.m_9236_()).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.TOOL, this.npc.getMainHandItem()).withParameter(LootContextParams.BLOCK_STATE, state).withOptionalParameter(LootContextParams.BLOCK_ENTITY, this.npc.m_9236_().getBlockEntity(pos));
                LootTable loottable = this.npc.m_20194_().getLootData().m_278676_(b.m_60589_());
                List<ItemStack> l = loottable.getRandomItems(builder.create(LootContextParamSets.BLOCK));
                this.npc.m_9236_().setBlock(pos, crop.getStateForAge(0), 2);
                if (l.isEmpty()) {
                    this.holding = ItemStack.EMPTY;
                } else if (l.size() == 1) {
                    this.holding = (ItemStack) l.get(0);
                } else {
                    List<ItemStack> fl = (List<ItemStack>) l.stream().filter(t -> t.getItem() != item).collect(Collectors.toList());
                    if (fl.isEmpty()) {
                        fl = l;
                    }
                    this.holding = (ItemStack) fl.get(this.npc.m_217043_().nextInt(fl.size()));
                }
                this.holding.setCount(1);
            }
            if (b instanceof StemGrownBlock) {
                b = this.npc.m_9236_().getBlockState(pos).m_60734_();
                this.npc.m_9236_().removeBlock(pos, false);
                this.holding = new ItemStack(b);
            }
            this.setHolding(this.holding);
        }
    }

    @Override
    public boolean aiContinueExecute() {
        return false;
    }

    @Override
    public void aiUpdateTask() {
        Iterator<BlockPos> ite = this.trackedBlocks.iterator();
        while (ite.hasNext() && this.ripe == null) {
            BlockPos pos = (BlockPos) ite.next();
            BlockState state = this.npc.m_9236_().getBlockState(pos);
            Block b = state.m_60734_();
            if ((b instanceof CropBlock && ((CropBlock) b).isMaxAge(state) || b instanceof StemGrownBlock) && b.m_60589_() != BuiltInLootTables.EMPTY) {
                this.ripe = pos;
            } else {
                ite.remove();
            }
        }
        this.npc.ais.returnToStart = this.ripe == null;
        if (this.ripe != null) {
            this.npc.m_21573_().stop();
            this.npc.m_21563_().setLookAt((double) this.ripe.m_123341_(), (double) this.ripe.m_123342_(), (double) this.ripe.m_123343_(), 10.0F, (float) this.npc.m_8132_());
        }
    }

    @Override
    public boolean isPlucking() {
        return this.ripe != null || !this.holding.isEmpty();
    }

    @Override
    public EntityNPCInterface getNpc() {
        return this.npc;
    }

    @Override
    public int getRange() {
        return 16;
    }

    @Override
    public void processed(List<BlockData> list) {
        List<BlockPos> trackedBlocks = new ArrayList();
        BlockPos chest = null;
        for (BlockData data : list) {
            BlockEntity tile = this.npc.m_9236_().getBlockEntity(data.pos);
            Block b = data.state.m_60734_();
            if (tile instanceof RandomizableContainerBlockEntity) {
                if (chest == null || this.npc.m_20275_((double) chest.m_123341_(), (double) chest.m_123342_(), (double) chest.m_123343_()) > this.npc.m_20275_((double) data.pos.m_123341_(), (double) data.pos.m_123342_(), (double) data.pos.m_123343_())) {
                    chest = data.pos;
                }
            } else if ((b instanceof CropBlock || b instanceof StemBlock) && !trackedBlocks.contains(data.pos)) {
                trackedBlocks.add(data.pos);
            }
        }
        this.chest = chest;
        this.trackedBlocks = trackedBlocks;
        this.waitingForBlocks = false;
    }

    @Override
    public EnumSet<Goal.Flag> getFlags() {
        return EnumSet.of(Goal.Flag.MOVE);
    }

    @Override
    public int getType() {
        return 11;
    }
}