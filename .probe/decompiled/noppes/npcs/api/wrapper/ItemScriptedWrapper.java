package noppes.npcs.api.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import noppes.npcs.EventHooks;
import noppes.npcs.NBTTags;
import noppes.npcs.api.CustomNPCsException;
import noppes.npcs.api.item.IItemScripted;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.controllers.IScriptHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;

public class ItemScriptedWrapper extends ItemStackWrapper implements IItemScripted, IScriptHandler {

    public List<ScriptContainer> scripts = new ArrayList();

    public String scriptLanguage = "ECMAScript";

    public boolean enabled = false;

    public long lastInited = -1L;

    public boolean updateClient = false;

    public boolean durabilityShow = true;

    public float durabilityValue = 1.0F;

    public int durabilityColor = -1;

    public int itemColor = -1;

    public int stackSize = 64;

    public boolean loaded = false;

    public ResourceLocation texture = null;

    public ItemScriptedWrapper(ItemStack item) {
        super(item);
    }

    @Override
    public boolean hasTexture(int damage) {
        return this.texture != null;
    }

    @Override
    public String getTexture(int damage) {
        return this.getTexture();
    }

    @Override
    public String getTexture() {
        return this.texture == null ? null : this.texture.toString();
    }

    @Override
    public void setTexture(int damage, String texture) {
        this.setTexture(texture);
    }

    @Override
    public void setTexture(String texture) {
        if (texture == null) {
            this.texture = null;
        } else {
            this.texture = new ResourceLocation(texture);
        }
    }

    public CompoundTag getScriptNBT(CompoundTag compound) {
        compound.put("Scripts", NBTTags.NBTScript(this.scripts));
        compound.putString("ScriptLanguage", this.scriptLanguage);
        compound.putBoolean("ScriptEnabled", this.enabled);
        if (this.texture != null) {
            compound.putString("ScriptTexture", this.texture.toString());
        }
        return compound;
    }

    @Override
    public CompoundTag getMCNbt() {
        CompoundTag compound = super.getMCNbt();
        this.getScriptNBT(compound);
        compound.putBoolean("DurabilityShow", this.durabilityShow);
        compound.putFloat("DurabilityValue", this.durabilityValue);
        compound.putInt("DurabilityColor", this.durabilityColor);
        compound.putInt("ItemColor", this.itemColor);
        compound.putInt("MaxStackSize", this.stackSize);
        if (this.item.getTag() != null) {
            compound.put("ItemTags", this.item.getTag());
        }
        return compound;
    }

    public void setScriptNBT(CompoundTag compound) {
        if (compound.contains("Scripts")) {
            this.scripts = NBTTags.GetScript(compound.getList("Scripts", 10), this);
            this.scriptLanguage = compound.getString("ScriptLanguage");
            this.enabled = compound.getBoolean("ScriptEnabled");
            if (compound.contains("ScriptTexture")) {
                this.texture = new ResourceLocation(compound.getString("ScriptTexture"));
            }
        }
    }

    @Override
    public void setMCNbt(CompoundTag compound) {
        super.setMCNbt(compound);
        this.setScriptNBT(compound);
        this.durabilityShow = compound.getBoolean("DurabilityShow");
        this.durabilityValue = compound.getFloat("DurabilityValue");
        if (compound.contains("DurabilityColor")) {
            this.durabilityColor = compound.getInt("DurabilityColor");
        }
        this.itemColor = compound.getInt("ItemColor");
        this.stackSize = compound.getInt("MaxStackSize");
        if (compound.contains("ItemTags")) {
            this.item.setTag(compound.getCompound("ItemTags"));
        }
    }

    @Override
    public int getType() {
        return 6;
    }

    @Override
    public void runScript(EnumScriptType type, Event event) {
        if (!this.loaded) {
            this.loadScriptData();
            this.loaded = true;
        }
        if (this.isEnabled()) {
            if (ScriptController.Instance.lastLoaded > this.lastInited) {
                this.lastInited = ScriptController.Instance.lastLoaded;
                if (type != EnumScriptType.INIT) {
                    EventHooks.onScriptItemInit(this);
                }
            }
            for (ScriptContainer script : this.scripts) {
                script.run(type, event);
            }
        }
    }

    private boolean isEnabled() {
        return this.enabled && ScriptController.HasStart;
    }

    @Override
    public boolean isClient() {
        return false;
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
        return "ScriptedItem";
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

    @Override
    public int getMaxStackSize() {
        return this.stackSize;
    }

    @Override
    public void setMaxStackSize(int size) {
        if (size >= 1 && size <= 64) {
            this.stackSize = size;
        } else {
            throw new CustomNPCsException("Stacksize has to be between 1 and 64");
        }
    }

    @Override
    public double getDurabilityValue() {
        return (double) this.durabilityValue;
    }

    @Override
    public void setDurabilityValue(float value) {
        if (value != this.durabilityValue) {
            this.updateClient = true;
        }
        this.durabilityValue = value;
    }

    @Override
    public boolean getDurabilityShow() {
        return this.durabilityShow;
    }

    @Override
    public void setDurabilityShow(boolean bo) {
        if (bo != this.durabilityShow) {
            this.updateClient = true;
        }
        this.durabilityShow = bo;
    }

    @Override
    public int getDurabilityColor() {
        return this.durabilityColor;
    }

    @Override
    public void setDurabilityColor(int color) {
        if (color != this.durabilityColor) {
            this.updateClient = true;
        }
        this.durabilityColor = color;
    }

    @Override
    public int getColor() {
        return this.itemColor;
    }

    @Override
    public void setColor(int color) {
        if (color != this.itemColor) {
            this.updateClient = true;
        }
        this.itemColor = color;
    }

    public void saveScriptData() {
        CompoundTag c = this.item.getTag();
        if (c == null) {
            this.item.setTag(c = new CompoundTag());
        }
        c.put("ScriptedData", this.getScriptNBT(new CompoundTag()));
    }

    public void loadScriptData() {
        CompoundTag c = this.item.getTag();
        if (c != null) {
            this.setScriptNBT(c.getCompound("ScriptedData"));
        }
    }
}