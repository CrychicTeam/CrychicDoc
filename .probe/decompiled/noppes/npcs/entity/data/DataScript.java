package noppes.npcs.entity.data;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.eventbus.api.Event;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.IScriptHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.entity.EntityNPCInterface;

public class DataScript implements IScriptHandler {

    private List<ScriptContainer> scripts = new ArrayList();

    private String scriptLanguage = "ECMAScript";

    private EntityNPCInterface npc;

    private boolean enabled = false;

    public long lastInited = -1L;

    public DataScript(EntityNPCInterface npc) {
        this.npc = npc;
    }

    public void load(CompoundTag compound) {
        this.scripts = NBTTags.GetScript(compound.getList("Scripts", 10), this);
        this.scriptLanguage = compound.getString("ScriptLanguage");
        this.enabled = compound.getBoolean("ScriptEnabled");
    }

    public CompoundTag save(CompoundTag compound) {
        compound.put("Scripts", NBTTags.NBTScript(this.scripts));
        compound.putString("ScriptLanguage", this.scriptLanguage);
        compound.putBoolean("ScriptEnabled", this.enabled);
        return compound;
    }

    @Override
    public void runScript(EnumScriptType type, Event event) {
        if (this.isEnabled()) {
            if (ScriptController.Instance.lastLoaded > this.lastInited) {
                this.lastInited = ScriptController.Instance.lastLoaded;
                if (type != EnumScriptType.INIT) {
                    EventHooks.onNPCInit(this.npc);
                }
            }
            for (ScriptContainer script : this.scripts) {
                script.run(type, event);
            }
        }
    }

    public boolean isEnabled() {
        return this.enabled && ScriptController.HasStart && !this.npc.m_9236_().isClientSide;
    }

    @Override
    public boolean isClient() {
        return this.npc.isClientSide();
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
        BlockPos pos = this.npc.m_20183_();
        return MoreObjects.toStringHelper(this.npc).add("x", pos.m_123341_()).add("y", pos.m_123342_()).add("z", pos.m_123343_()).toString();
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