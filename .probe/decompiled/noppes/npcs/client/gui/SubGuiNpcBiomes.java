package noppes.npcs.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.controllers.data.SpawnData;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;

public class SubGuiNpcBiomes extends GuiBasic {

    private SpawnData data;

    private GuiCustomScrollNop scroll1;

    private GuiCustomScrollNop scroll2;

    public SubGuiNpcBiomes(SpawnData data) {
        this.data = data;
        this.setBackground("menubg.png");
        this.imageWidth = 346;
        this.imageHeight = 216;
    }

    @Override
    public void init() {
        super.init();
        if (this.scroll1 == null) {
            this.scroll1 = new GuiCustomScrollNop(this, 0);
            this.scroll1.setSize(140, 180);
        }
        this.scroll1.guiLeft = this.guiLeft + 4;
        this.scroll1.guiTop = this.guiTop + 14;
        this.addScroll(this.scroll1);
        this.addLabel(new GuiLabel(1, "spawning.availableBiomes", this.guiLeft + 4, this.guiTop + 4));
        if (this.scroll2 == null) {
            this.scroll2 = new GuiCustomScrollNop(this, 1);
            this.scroll2.setSize(140, 180);
        }
        this.scroll2.guiLeft = this.guiLeft + 200;
        this.scroll2.guiTop = this.guiTop + 14;
        this.addScroll(this.scroll2);
        this.addLabel(new GuiLabel(2, "spawning.spawningBiomes", this.guiLeft + 200, this.guiTop + 4));
        List<String> biomes = new ArrayList();
        for (Biome base : ForgeRegistries.BIOMES) {
            if (base != null && ForgeRegistries.BIOMES.getKey(base) != null && !this.data.biomes.contains(ForgeRegistries.BIOMES.getKey(base).toString())) {
                biomes.add(ForgeRegistries.BIOMES.getKey(base).toString());
            }
        }
        this.scroll1.setList(biomes);
        this.scroll2.setList((List<String>) this.data.biomes.stream().map(Object::toString).collect(Collectors.toList()));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 145, this.guiTop + 40, 55, 20, ">"));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 145, this.guiTop + 62, 55, 20, "<"));
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 145, this.guiTop + 90, 55, 20, ">>"));
        this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 145, this.guiTop + 112, 55, 20, "<<"));
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + 260, this.guiTop + 194, 60, 20, "gui.done"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 1 && this.scroll1.hasSelected()) {
            this.data.biomes.add(new ResourceLocation(this.scroll1.getSelected()));
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 2 && this.scroll2.hasSelected()) {
            this.data.biomes.remove(this.scroll2.getSelected());
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 3) {
            this.data.biomes.clear();
            for (Biome base : ForgeRegistries.BIOMES) {
                if (base != null) {
                    this.data.biomes.add(ForgeRegistries.BIOMES.getKey(base));
                }
            }
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 4) {
            this.data.biomes.clear();
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 66) {
            this.close();
        }
    }
}