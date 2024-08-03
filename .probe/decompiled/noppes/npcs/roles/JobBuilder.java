package noppes.npcs.roles;

import java.util.Stack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.data.role.IJobBuilder;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.controllers.data.BlockData;
import noppes.npcs.entity.EntityNPCInterface;

public class JobBuilder extends JobInterface implements IJobBuilder {

    public TileBuilder build = null;

    private BlockPos possibleBuildPos = null;

    private Stack<BlockData> placingList = null;

    private BlockData placing = null;

    private int tryTicks = 0;

    private int ticks = 0;

    public JobBuilder(EntityNPCInterface npc) {
        super(npc);
        this.overrideMainHand = true;
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        if (this.build != null) {
            compound.putInt("BuildX", this.build.m_58899_().m_123341_());
            compound.putInt("BuildY", this.build.m_58899_().m_123342_());
            compound.putInt("BuildZ", this.build.m_58899_().m_123343_());
            if (this.placingList != null && !this.placingList.isEmpty()) {
                ListTag list = new ListTag();
                for (BlockData data : this.placingList) {
                    list.add(data.getNBT());
                }
                if (this.placing != null) {
                    list.add(this.placing.getNBT());
                }
                compound.put("Placing", list);
            }
        }
        return compound;
    }

    @Override
    public void load(CompoundTag compound) {
        if (compound.contains("BuildX")) {
            this.possibleBuildPos = new BlockPos(compound.getInt("BuildX"), compound.getInt("BuildY"), compound.getInt("BuildZ"));
        }
        if (this.possibleBuildPos != null && compound.contains("Placing")) {
            Stack<BlockData> placing = new Stack();
            ListTag list = compound.getList("Placing", 10);
            for (int i = 0; i < list.size(); i++) {
                BlockData data = BlockData.getData(list.getCompound(i));
                if (data != null) {
                    placing.add(data);
                }
            }
            this.placingList = placing;
        }
        this.npc.ais.doorInteract = 1;
    }

    @Override
    public IItemStack getMainhand() {
        String name = this.npc.getJobData();
        ItemStack item = this.stringToItem(name);
        return item.isEmpty() ? (IItemStack) this.npc.inventory.weapons.get(0) : NpcAPI.Instance().getIItemStack(item);
    }

    @Override
    public boolean aiShouldExecute() {
        if (this.possibleBuildPos != null) {
            BlockEntity tile = this.npc.m_9236_().getBlockEntity(this.possibleBuildPos);
            if (tile instanceof TileBuilder) {
                this.build = (TileBuilder) tile;
            } else {
                this.placingList.clear();
            }
            this.possibleBuildPos = null;
        }
        return this.build != null;
    }

    @Override
    public void aiUpdateTask() {
        if ((!this.build.finished || this.placingList != null) && this.build.enabled && !this.build.m_58901_()) {
            if (this.ticks++ >= 10) {
                this.ticks = 0;
                if ((this.placingList == null || this.placingList.isEmpty()) && this.placing == null) {
                    this.placingList = this.build.getBlock();
                    this.npc.setJobData("");
                } else {
                    if (this.placing == null) {
                        this.placing = (BlockData) this.placingList.pop();
                        if (this.placing.state.m_60734_() == Blocks.STRUCTURE_VOID) {
                            this.placing = null;
                            return;
                        }
                        this.tryTicks = 0;
                        this.npc.setJobData(this.blockToString(this.placing));
                    }
                    this.npc.m_21573_().moveTo((double) this.placing.pos.m_123341_(), (double) (this.placing.pos.m_123342_() + 1), (double) this.placing.pos.m_123343_(), 1.0);
                    if (this.tryTicks++ > 40 || this.npc.nearPosition(this.placing.pos)) {
                        BlockPos blockPos = this.placing.pos;
                        this.placeBlock();
                        if (this.tryTicks > 40) {
                            blockPos = NoppesUtilServer.GetClosePos(blockPos, this.npc.m_9236_());
                            this.npc.m_6021_((double) blockPos.m_123341_() + 0.5, (double) blockPos.m_123342_(), (double) blockPos.m_123343_() + 0.5);
                        }
                    }
                }
            }
        } else {
            this.build = null;
            this.npc.m_21573_().moveTo((double) this.npc.getStartXPos(), this.npc.getStartYPos(), (double) this.npc.getStartZPos(), 1.0);
        }
    }

    private String blockToString(BlockData data) {
        return data.state.m_60734_() == Blocks.AIR ? ForgeRegistries.ITEMS.getKey(Items.IRON_PICKAXE).toString() : this.itemToString(data.getStack());
    }

    @Override
    public void stop() {
        this.reset();
    }

    @Override
    public void reset() {
        this.build = null;
        this.npc.setJobData("");
    }

    public void placeBlock() {
        if (this.placing != null) {
            this.npc.m_21573_().stop();
            this.npc.m_6674_(InteractionHand.MAIN_HAND);
            this.npc.m_9236_().setBlock(this.placing.pos, this.placing.state, 2);
            if (this.placing.state.m_60734_() instanceof EntityBlock && this.placing.tile != null) {
                BlockEntity tile = this.npc.m_9236_().getBlockEntity(this.placing.pos);
                if (tile != null) {
                    try {
                        tile.load(this.placing.tile);
                    } catch (Exception var3) {
                    }
                }
            }
            this.placing = null;
        }
    }

    @Override
    public boolean isBuilding() {
        return this.build != null && this.build.enabled && !this.build.finished && this.build.started;
    }

    @Override
    public int getType() {
        return 10;
    }
}