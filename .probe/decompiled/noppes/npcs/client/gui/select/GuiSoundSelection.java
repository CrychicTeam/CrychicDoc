package noppes.npcs.client.gui.select;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;

public class GuiSoundSelection extends GuiBasic implements ICustomScrollListener {

    private GuiCustomScrollNop scrollCategories;

    private GuiCustomScrollNop scrollQuests;

    private String selectedDomain;

    public ResourceLocation selectedResource;

    private HashMap<String, List<String>> domains = new HashMap();

    public GuiSoundSelection(String sound) {
        this.drawDefaultBackground = false;
        this.title = "";
        this.setBackground("menubg.png");
        this.imageWidth = 366;
        this.imageHeight = 226;
        SoundManager handler = Minecraft.getInstance().getSoundManager();
        for (ResourceLocation location : handler.getAvailableSounds()) {
            List<String> list = (List<String>) this.domains.get(location.getNamespace());
            if (list == null) {
                this.domains.put(location.getNamespace(), list = new ArrayList());
            }
            list.add(location.getPath());
            this.domains.put(location.getNamespace(), list);
        }
        if (sound != null && !sound.isEmpty()) {
            this.selectedResource = new ResourceLocation(sound);
            this.selectedDomain = this.selectedResource.getNamespace();
            if (!this.domains.containsKey(this.selectedDomain)) {
                this.selectedDomain = null;
            }
        }
    }

    @Override
    public void init() {
        super.init();
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + this.imageWidth - 26, this.guiTop + 4, 20, 20, "X"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 160, this.guiTop + 212, 70, 20, "gui.play", this.selectedResource != null));
        if (this.scrollCategories == null) {
            this.scrollCategories = new GuiCustomScrollNop(this, 0);
            this.scrollCategories.setSize(90, 200);
        }
        this.scrollCategories.setList(Lists.newArrayList(this.domains.keySet()));
        if (this.selectedDomain != null) {
            this.scrollCategories.setSelected(this.selectedDomain);
        }
        this.scrollCategories.guiLeft = this.guiLeft + 4;
        this.scrollCategories.guiTop = this.guiTop + 14;
        this.addScroll(this.scrollCategories);
        if (this.scrollQuests == null) {
            this.scrollQuests = new GuiCustomScrollNop(this, 1);
            this.scrollQuests.setSize(250, 200);
        }
        if (this.selectedDomain != null) {
            this.scrollQuests.setList((List<String>) this.domains.get(this.selectedDomain));
        }
        if (this.selectedResource != null) {
            this.scrollQuests.setSelected(this.selectedResource.getPath());
        }
        this.scrollQuests.guiLeft = this.guiLeft + 95;
        this.scrollQuests.guiTop = this.guiTop + 14;
        this.addScroll(this.scrollQuests);
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 1) {
            MusicController.Instance.stopMusic();
            BlockPos pos = this.player.m_20183_();
            MusicController.Instance.playSound(SoundSource.NEUTRAL, this.selectedResource.toString(), pos, 1.0F, 1.0F);
        }
        if (guibutton.id == 2) {
            this.close();
        }
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop scroll) {
        if (scroll.id == 0) {
            this.selectedDomain = scroll.getSelected();
            this.selectedResource = null;
            this.scrollQuests.clearSelection();
        }
        if (scroll.id == 1) {
            this.selectedResource = new ResourceLocation(this.selectedDomain, scroll.getSelected());
        }
        this.init();
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
        if (this.selectedResource != null) {
            this.close();
        }
    }
}