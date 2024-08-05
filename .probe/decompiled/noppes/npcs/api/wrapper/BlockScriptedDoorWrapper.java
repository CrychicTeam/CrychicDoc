package noppes.npcs.api.wrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.ITimers;
import noppes.npcs.api.block.IBlockScriptedDoor;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.mixin.EntityIMixin;

public class BlockScriptedDoorWrapper extends BlockWrapper implements IBlockScriptedDoor {

    private TileScriptedDoor tile = (TileScriptedDoor) this.tile;

    public BlockScriptedDoorWrapper(Level level, Block block, BlockPos pos) {
        super(level, block, pos);
    }

    @Override
    public boolean getOpen() {
        BlockState state = this.level.getMCLevel().m_8055_(this.pos);
        return ((Boolean) state.m_61143_(DoorBlock.OPEN)).equals(true);
    }

    @Override
    public void setOpen(boolean open) {
        if (this.getOpen() != open && !this.isRemoved()) {
            BlockState state = this.level.getMCLevel().m_8055_(this.pos);
            ((DoorBlock) this.block).setOpen(null, this.level.getMCLevel(), state, this.pos, open);
        }
    }

    @Override
    public void setBlockModel(String name) {
        Block b = null;
        if (name != null) {
            b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
        }
        this.tile.setItemModel(b);
    }

    @Override
    public String getBlockModel() {
        return ForgeRegistries.BLOCKS.getKey(this.tile.blockModel) + "";
    }

    @Override
    public ITimers getTimers() {
        return this.tile.timers;
    }

    @Override
    public float getHardness() {
        return this.tile.blockHardness;
    }

    @Override
    public void setHardness(float hardness) {
        this.tile.blockHardness = hardness;
    }

    @Override
    public float getResistance() {
        return this.tile.blockResistance;
    }

    @Override
    public void setResistance(float resistance) {
        this.tile.blockResistance = resistance;
    }

    @Override
    protected void setTile(BlockEntity tile) {
        this.tile = (TileScriptedDoor) tile;
        super.setTile(tile);
    }

    @Override
    public String executeCommand(String command) {
        if (!this.tile.m_58904_().getServer().isCommandBlockEnabled()) {
            throw new CustomNPCsException("Command blocks need to be enabled to executeCommands");
        } else {
            FakePlayer player = EntityNPCInterface.CommandPlayer;
            ((EntityIMixin) player).setLevel((ServerLevel) this.tile.m_58904_());
            player.m_6034_((double) this.getX(), (double) this.getY(), (double) this.getZ());
            return NoppesUtilServer.runCommand(this.tile.m_58904_(), this.tile.m_58899_(), "ScriptBlock: " + this.tile.m_58899_(), command, null, player);
        }
    }
}