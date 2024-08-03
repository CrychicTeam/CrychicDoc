package noppes.npcs.client.gui.script;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import noppes.npcs.NBTTags;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.controllers.IScriptHandler;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.controllers.ScriptController;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiMenuTopButton;
import noppes.npcs.shared.client.gui.components.GuiTextArea;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.client.gui.listeners.IGuiData;
import noppes.npcs.shared.client.gui.listeners.ITextChangeListener;
import noppes.npcs.shared.client.util.NoppesStringUtils;

public class GuiScriptInterface extends GuiNPCInterface implements IGuiData, ITextChangeListener {

    private int activeTab = 0;

    public IScriptHandler handler;

    public Map<String, List<String>> languages = new HashMap();

    public List<String> methods = new ArrayList();

    public boolean showFunctions = false;

    public GuiScriptInterface() {
        this.drawDefaultBackground = true;
        this.imageWidth = 420;
        this.setBackground("menubg.png");
    }

    @Override
    public void init() {
        this.imageWidth = (int) ((double) this.f_96543_ * 0.88);
        this.imageHeight = (int) ((double) this.imageWidth * 0.56);
        if ((double) this.imageHeight > (double) this.f_96544_ * 0.95) {
            this.imageHeight = (int) ((double) this.f_96544_ * 0.95);
            this.imageWidth = (int) ((double) this.imageHeight / 0.56);
        }
        this.bgScale = (float) this.imageWidth / 400.0F;
        super.m_7856_();
        this.guiTop += 10;
        int yoffset = (int) ((double) this.imageHeight * 0.02);
        GuiMenuTopButton top;
        this.addTopButton(top = new GuiMenuTopButton(this, 0, this.guiLeft + 4, this.guiTop - 17, "gui.settings"));
        for (int i = 0; i < this.handler.getScripts().size(); i++) {
            ScriptContainer script = (ScriptContainer) this.handler.getScripts().get(i);
            this.addTopButton(top = new GuiMenuTopButton(this, i + 1, top, i + 1 + ""));
        }
        if (this.handler.getScripts().size() < 40) {
            this.addTopButton(new GuiMenuTopButton(this, 41, top, "+"));
        }
        top = this.getTopButton(this.activeTab);
        if (top == null) {
            this.activeTab = 0;
            top = this.getTopButton(0);
        }
        top.active = true;
        if (this.activeTab > 0) {
            ScriptContainer container = (ScriptContainer) this.handler.getScripts().get(this.activeTab - 1);
            final GuiTextArea ta = new GuiTextArea(3, this.guiLeft + 1 + yoffset, this.guiTop + yoffset, this.imageWidth - 108 - yoffset, (int) ((double) this.imageHeight * 0.96) - yoffset * 2, container == null ? "" : container.script);
            ta.enableCodeHighlighting();
            ta.setListener(this);
            this.add(ta);
            int left = this.guiLeft + this.imageWidth - 104;
            this.addButton(new GuiButtonNop(this, 99, left, this.guiTop + yoffset, 121, 20, this.showFunctions ? "script.hideFunctions" : "script.showFuncions", button -> {
                this.showFunctions = !this.showFunctions;
                this.init();
            }));
            if (!this.showFunctions) {
                this.addButton(new GuiButtonNop(this, 102, left, this.guiTop + yoffset + 22, 60, 20, "gui.clear"));
                this.addButton(new GuiButtonNop(this, 101, left + 61, this.guiTop + yoffset + 22, 60, 20, "gui.paste"));
                this.addButton(new GuiButtonNop(this, 100, left, this.guiTop + 21 + yoffset + 22, 60, 20, "gui.copy"));
                this.addButton(new GuiButtonNop(this, 105, left + 61, this.guiTop + 21 + yoffset + 22, 60, 20, "gui.remove"));
                this.addButton(new GuiButtonNop(this, 107, left, this.guiTop + 66 + yoffset, 121, 20, "script.loadscript"));
                GuiCustomScrollNop scroll = new GuiCustomScrollNop(this, 0).setUnselectable();
                scroll.setSize(104 + (int) (16.0F * this.bgScale), (int) ((double) this.imageHeight * 0.54) - yoffset * 2);
                scroll.guiLeft = left;
                scroll.guiTop = this.guiTop + 88 + yoffset;
                if (container != null) {
                    scroll.setList(container.scripts);
                }
                this.addScroll(scroll);
            } else {
                GuiCustomScrollNop scroll = new GuiCustomScrollNop(this, 1);
                scroll.setSize(104 + (int) (16.0F * this.bgScale), (int) ((double) this.imageHeight * 0.6) - yoffset * 2);
                scroll.guiLeft = left;
                scroll.guiTop = this.guiTop + yoffset + 22;
                scroll.setList(this.methods);
                scroll.listener = new ICustomScrollListener() {

                    @Override
                    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop scroll) {
                    }

                    @Override
                    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
                        ta.addText(selection);
                    }
                };
                this.addScroll(scroll);
                scroll = new GuiCustomScrollNop(this, 2);
                scroll.setSize(104 + (int) (16.0F * this.bgScale), (int) ((double) this.imageHeight * 0.32) - yoffset * 2);
                scroll.guiLeft = left;
                scroll.guiTop = this.guiTop + yoffset + (int) ((double) this.imageHeight * 0.6) - yoffset * 2 + 26;
                scroll.setList(new ArrayList(ScriptContainer.Data.keySet()));
                scroll.listener = new ICustomScrollListener() {

                    @Override
                    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop scroll) {
                    }

                    @Override
                    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
                        ta.addText(selection);
                    }
                };
                this.addScroll(scroll);
            }
        } else {
            GuiTextArea ta = new GuiTextArea(3, this.guiLeft + 4 + yoffset, this.guiTop + 6 + yoffset, this.imageWidth - 160 - yoffset, (int) ((float) this.imageHeight * 0.92F) - yoffset * 2, this.getConsoleText());
            ta.enabled = false;
            this.add(ta);
            int left = this.guiLeft + this.imageWidth - 150;
            this.addButton(new GuiButtonNop(this, 100, left, this.guiTop + 145, 60, 20, "gui.copy"));
            this.addButton(new GuiButtonNop(this, 102, left, this.guiTop + 166, 60, 20, "gui.clear"));
            this.addLabel(new GuiLabel(1, "script.language", left, this.guiTop + 15));
            this.addButton(new GuiButtonNop(this, 103, left + 60, this.guiTop + 10, 80, 20, (String[]) this.languages.keySet().toArray(new String[this.languages.keySet().size()]), this.getScriptIndex()));
            this.getButton(103).f_93623_ = this.languages.size() > 0;
            this.addLabel(new GuiLabel(2, "gui.enabled", left, this.guiTop + 36));
            this.addButton(new GuiButtonNop(this, 104, left + 60, this.guiTop + 31, 50, 20, new String[] { "gui.no", "gui.yes" }, this.handler.getEnabled() ? 1 : 0));
            if (this.player.m_20194_() != null) {
                this.addButton(new GuiButtonNop(this, 106, left, this.guiTop + 55, 150, 20, "script.openfolder"));
            }
            this.addButton(new GuiButtonNop(this, 109, left, this.guiTop + 78, 80, 20, "gui.website"));
            this.addButton(new GuiButtonNop(this, 112, left + 81, this.guiTop + 78, 80, 20, "script.examples"));
            this.addButton(new GuiButtonNop(this, 110, left, this.guiTop + 99, 80, 20, "script.apidoc"));
            this.addButton(new GuiButtonNop(this, 111, left + 81, this.guiTop + 99, 80, 20, "script.apisrc"));
        }
        this.imageWidth = 420;
        this.imageHeight = 256;
    }

    private String getConsoleText() {
        Map<Long, String> map = this.handler.getConsoleText();
        StringBuilder builder = new StringBuilder();
        for (Entry<Long, String> entry : map.entrySet()) {
            builder.insert(0, new Date((Long) entry.getKey()) + (String) entry.getValue() + "\n");
        }
        return builder.toString();
    }

    private int getScriptIndex() {
        int i = 0;
        for (String language : this.languages.keySet()) {
            if (language.equalsIgnoreCase(this.handler.getLanguage())) {
                return i;
            }
            i++;
        }
        return 0;
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id >= 0 && guibutton.id < 41) {
            this.setScript();
            this.activeTab = guibutton.id;
            this.init();
        }
        if (guibutton.id == 41) {
            this.handler.getScripts().add(new ScriptContainer(this.handler));
            this.activeTab = this.handler.getScripts().size();
            this.init();
        }
        if (guibutton.id == 109) {
            this.setScreen(new ConfirmLinkScreen(bo -> {
                if (bo) {
                    Util.getPlatform().openUri("http://www.kodevelopment.nl/minecraft/customnpcs/scripting");
                }
                this.setScreen(this);
            }, "http://www.kodevelopment.nl/minecraft/customnpcs/scripting", true));
        }
        if (guibutton.id == 110) {
            this.setScreen(new ConfirmLinkScreen(bo -> {
                if (bo) {
                    Util.getPlatform().openUri("http://www.kodevelopment.nl/customnpcs/api/");
                }
                this.setScreen(this);
            }, "http://www.kodevelopment.nl/customnpcs/api/", true));
        }
        if (guibutton.id == 111) {
            this.setScreen(new ConfirmLinkScreen(bo -> {
                if (bo) {
                    Util.getPlatform().openUri("https://github.com/Noppes/CustomNPCsAPI");
                }
                this.setScreen(this);
            }, "https://github.com/Noppes/CustomNPCsAPI", true));
        }
        if (guibutton.id == 112) {
            this.setScreen(new ConfirmLinkScreen(bo -> {
                if (bo) {
                    Util.getPlatform().openUri("https://github.com/Noppes/cnpcs-scripting-examples");
                }
                this.setScreen(this);
            }, "https://github.com/Noppes/cnpcs-scripting-examples", true));
        }
        if (guibutton.id == 100) {
            NoppesStringUtils.setClipboardContents(((GuiTextArea) this.get(3)).getText());
        }
        if (guibutton.id == 101) {
            ((GuiTextArea) this.get(3)).setText(NoppesStringUtils.getClipboardContents());
        }
        if (guibutton.id == 102) {
            if (this.activeTab > 0) {
                ScriptContainer container = (ScriptContainer) this.handler.getScripts().get(this.activeTab - 1);
                container.script = "";
            } else {
                this.handler.clearConsole();
            }
            this.init();
        }
        if (guibutton.id == 103) {
            this.handler.setLanguage(guibutton.m_6035_().getString());
        }
        if (guibutton.id == 104) {
            this.handler.setEnabled(guibutton.getValue() == 1);
        }
        if (guibutton.id == 105) {
            ConfirmScreen guiyesno = new ConfirmScreen(bo -> {
                this.handler.getScripts().remove(this.activeTab - 1);
                this.activeTab = 0;
                this.setScreen(this);
            }, Component.translatable(""), Component.translatable("gui.deleteMessage"));
            this.setScreen(guiyesno);
        }
        if (guibutton.id == 106) {
            NoppesUtil.openFolder(ScriptController.Instance.dir);
        }
        if (guibutton.id == 107) {
            ScriptContainer container = (ScriptContainer) this.handler.getScripts().get(this.activeTab - 1);
            if (container == null) {
                this.handler.getScripts().add(container = new ScriptContainer(this.handler));
            }
            this.setSubGui(new GuiScriptList((List<String>) this.languages.get(this.handler.getLanguage()), container));
        }
        if (guibutton.id == 108) {
            ScriptContainer container = (ScriptContainer) this.handler.getScripts().get(this.activeTab - 1);
            if (container != null) {
                this.setScript();
            }
        }
    }

    private void setScript() {
        if (this.activeTab > 0) {
            ScriptContainer container = (ScriptContainer) this.handler.getScripts().get(this.activeTab - 1);
            if (container == null) {
                this.handler.getScripts().add(container = new ScriptContainer(this.handler));
            }
            String text = ((GuiTextArea) this.get(3)).getText();
            text = text.replace("\r\n", "\n");
            text = text.replace("\r", "\n");
            container.script = text;
        }
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        ListTag data = compound.getList("Languages", 10);
        Map<String, List<String>> languages = new HashMap();
        for (int i = 0; i < data.size(); i++) {
            CompoundTag comp = data.getCompound(i);
            List<String> scripts = new ArrayList();
            ListTag list = comp.getList("Scripts", 8);
            for (int j = 0; j < list.size(); j++) {
                scripts.add(list.getString(j));
            }
            languages.put(comp.getString("Language"), scripts);
        }
        this.languages = languages;
        this.methods = NBTTags.getStringList(compound.getList("Methods", 10));
        this.init();
    }

    @Override
    public void save() {
        this.setScript();
    }

    @Override
    public void textUpdate(String text) {
        ScriptContainer container = (ScriptContainer) this.handler.getScripts().get(this.activeTab - 1);
        if (container != null) {
            container.script = text;
        }
    }
}