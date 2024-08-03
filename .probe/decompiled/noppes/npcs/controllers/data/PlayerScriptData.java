package noppes.npcs.controllers.data;

import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.IScriptHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;

public class PlayerScriptData implements IScriptHandler {

    private List<ScriptContainer> scripts = new ArrayList();

    private String scriptLanguage = "ECMAScript";

    private Player player;

    private IPlayer playerAPI;

    private long lastPlayerUpdate = 0L;

    public long lastInited = -1L;

    public boolean hadInteract = true;

    private boolean enabled = false;

    private static Map<Long, String> console = new TreeMap();

    private static List<Integer> errored = new ArrayList();

    public PlayerScriptData(Player player) {
        this.player = player;
    }

    public void clear() {
        console = new TreeMap();
        errored = new ArrayList();
        this.scripts = new ArrayList();
    }

    public void load(CompoundTag compound) {
        this.scripts = NBTTags.GetScript(compound.getList("Scripts", 10), this);
        this.scriptLanguage = compound.getString("ScriptLanguage");
        this.enabled = compound.getBoolean("ScriptEnabled");
        console = NBTTags.GetLongStringMap(compound.getList("ScriptConsole", 10));
    }

    public CompoundTag save(CompoundTag compound) {
        compound.put("Scripts", NBTTags.NBTScript(this.scripts));
        compound.putString("ScriptLanguage", this.scriptLanguage);
        compound.putBoolean("ScriptEnabled", this.enabled);
        compound.put("ScriptConsole", NBTTags.NBTLongStringMap(console));
        return compound;
    }

    @Override
    public void runScript(EnumScriptType type, Event event) {
        if (this.isEnabled()) {
            if (ScriptController.Instance.lastLoaded > this.lastInited || ScriptController.Instance.lastPlayerUpdate > this.lastPlayerUpdate) {
                this.lastInited = ScriptController.Instance.lastLoaded;
                errored.clear();
                if (this.player != null) {
                    this.scripts.clear();
                    for (ScriptContainer script : ScriptController.Instance.playerScripts.scripts) {
                        ScriptContainer s = new ScriptContainer(this);
                        s.load(script.save(new CompoundTag()));
                        this.scripts.add(s);
                    }
                }
                this.lastPlayerUpdate = ScriptController.Instance.lastPlayerUpdate;
                if (type != EnumScriptType.INIT) {
                    EventHooks.onPlayerInit(this);
                }
            }
            for (int i = 0; i < this.scripts.size(); i++) {
                ScriptContainer script = (ScriptContainer) this.scripts.get(i);
                if (!errored.contains(i)) {
                    script.run(type, event);
                    if (script.errored) {
                        errored.add(i);
                    }
                    for (Entry<Long, String> entry : script.console.entrySet()) {
                        if (!console.containsKey(entry.getKey())) {
                            console.put((Long) entry.getKey(), " tab " + (i + 1) + ":\n" + (String) entry.getValue());
                        }
                    }
                    script.console.clear();
                }
            }
        }
    }

    public boolean isEnabled() {
        return ScriptController.Instance.playerScripts.enabled && ScriptController.HasStart && (this.player == null || !this.player.m_9236_().isClientSide);
    }

    @Override
    public boolean isClient() {
        return this.player.m_9236_().isClientSide();
    }

    @Override
    public boolean getEnabled() {
        return ScriptController.Instance.playerScripts.enabled;
    }

    @Override
    public void setEnabled(boolean bo) {
        this.enabled = bo;
    }

    @Override
    public String getLanguage() {
        return ScriptController.Instance.playerScripts.scriptLanguage;
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
        if (this.player == null) {
            return "Global script";
        } else {
            BlockPos pos = this.player.m_20183_();
            return MoreObjects.toStringHelper(this.player).add("x", pos.m_123341_()).add("y", pos.m_123342_()).add("z", pos.m_123343_()).toString();
        }
    }

    public IPlayer getPlayer() {
        if (this.playerAPI == null) {
            this.playerAPI = (IPlayer) NpcAPI.Instance().getIEntity(this.player);
        }
        return this.playerAPI;
    }

    @Override
    public Map<Long, String> getConsoleText() {
        return console;
    }

    @Override
    public void clearConsole() {
        console.clear();
    }
}