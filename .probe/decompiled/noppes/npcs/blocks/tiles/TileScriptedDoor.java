package noppes.npcs.blocks.tiles;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Event;
import noppes.npcs.CustomBlocks;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.api.block.IBlock;
import noppes.npcs.api.wrapper.BlockScriptedDoorWrapper;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.IScriptBlockHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.entity.data.DataTimers;

public class TileScriptedDoor extends TileDoor implements IScriptBlockHandler {

    public List<ScriptContainer> scripts = new ArrayList();

    public boolean shouldRefreshData = false;

    public String scriptLanguage = "ECMAScript";

    public boolean enabled = false;

    private IBlock blockDummy = null;

    public DataTimers timers = new DataTimers(this);

    public long lastInited = -1L;

    private short tickCount = 0;

    public int newPower = 0;

    public int prevPower = 0;

    public float blockHardness = 5.0F;

    public float blockResistance = 10.0F;

    public TileScriptedDoor(BlockPos pos, BlockState state) {
        super(CustomBlocks.tile_scripteddoor, pos, state);
    }

    @Override
    public IBlock getBlock() {
        if (this.blockDummy == null) {
            this.blockDummy = new BlockScriptedDoorWrapper(this.m_58904_(), CustomBlocks.scripted_door, this.m_58899_());
        }
        return this.blockDummy;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.setNBT(compound);
        this.timers.load(compound);
    }

    public void setNBT(CompoundTag compound) {
        this.scripts = NBTTags.GetScript(compound.getList("Scripts", 10), this);
        this.scriptLanguage = compound.getString("ScriptLanguage");
        this.enabled = compound.getBoolean("ScriptEnabled");
        this.prevPower = compound.getInt("BlockPrevPower");
        if (compound.contains("BlockHardness")) {
            this.blockHardness = compound.getFloat("BlockHardness");
            this.blockResistance = compound.getFloat("BlockResistance");
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        this.getNBT(compound);
        this.timers.save(compound);
        super.saveAdditional(compound);
    }

    public CompoundTag getNBT(CompoundTag compound) {
        compound.put("Scripts", NBTTags.NBTScript(this.scripts));
        compound.putString("ScriptLanguage", this.scriptLanguage);
        compound.putBoolean("ScriptEnabled", this.enabled);
        compound.putInt("BlockPrevPower", this.prevPower);
        compound.putFloat("BlockHardness", this.blockHardness);
        compound.putFloat("BlockResistance", this.blockResistance);
        return compound;
    }

    @Override
    public void runScript(EnumScriptType type, Event event) {
        if (this.isEnabled()) {
            if (ScriptController.Instance.lastLoaded > this.lastInited) {
                this.lastInited = ScriptController.Instance.lastLoaded;
                if (type != EnumScriptType.INIT) {
                    EventHooks.onScriptBlockInit(this);
                }
            }
            for (ScriptContainer script : this.scripts) {
                script.run(type, event);
            }
        }
    }

    private boolean isEnabled() {
        return this.enabled && ScriptController.HasStart && !this.f_58857_.isClientSide;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileScriptedDoor tile) {
        tile.tickCount++;
        if (tile.prevPower != tile.newPower) {
            EventHooks.onScriptBlockRedstonePower(tile, tile.prevPower, tile.newPower);
            tile.prevPower = tile.newPower;
        }
        tile.timers.update();
        if (tile.tickCount >= 10) {
            EventHooks.onScriptBlockUpdate(tile);
            tile.tickCount = 0;
        }
    }

    @Override
    public boolean isClient() {
        return this.m_58904_().isClientSide;
    }

    @Override
    public boolean getEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean bo) {
        this.enabled = bo;
    }

    @Override
    public String getLanguage() {
        return this.scriptLanguage;
    }

    @Override
    public void setLanguage(String lang) {
        this.scriptLanguage = lang;
    }

    @Override
    public List<ScriptContainer> getScripts() {
        return this.scripts;
    }

    @Override
    public String noticeString() {
        BlockPos pos = this.m_58899_();
        return MoreObjects.toStringHelper(this).add("x", pos.m_123341_()).add("y", pos.m_123342_()).add("z", pos.m_123343_()).toString();
    }

    @Override
    public Map<Long, String> getConsoleText() {
        Map<Long, String> map = new TreeMap();
        int tab = 0;
        for (ScriptContainer script : this.getScripts()) {
            tab++;
            for (Entry<Long, String> entry : script.console.entrySet()) {
                map.put((Long) entry.getKey(), " tab " + tab + ":\n" + (String) entry.getValue());
            }
        }
        return map;
    }

    @Override
    public void clearConsole() {
        for (ScriptContainer script : this.getScripts()) {
            script.console.clear();
        }
    }
}