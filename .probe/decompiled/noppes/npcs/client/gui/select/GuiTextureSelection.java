package noppes.npcs.client.gui.select;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;
import noppes.npcs.shared.client.util.AssetsFinder;

public class GuiTextureSelection extends GuiNPCInterface implements ICustomScrollListener {

    private String up = "..<" + I18n.get("gui.up") + ">..";

    private GuiCustomScrollNop scrollCategories;

    private GuiCustomScrollNop scrollQuests;

    private String location = "";

    private String selectedDomain;

    public ResourceLocation selectedResource;

    private int type = 0;

    private static final HashMap<String, List<ResourceLocation>> domains = new HashMap();

    private static final HashMap<String, ResourceLocation> textures = new HashMap();

    public GuiTextureSelection(EntityNPCInterface npc, String texture, int type) {
        this.npc = npc;
        this.type = type;
        this.drawDefaultBackground = false;
        this.title = "";
        this.setBackground("menubg.png");
        this.imageWidth = 366;
        this.imageHeight = 226;
        if (domains.isEmpty()) {
            for (ResourceLocation loc : AssetsFinder.find("textures", ".png")) {
                ((List) domains.computeIfAbsent(loc.getNamespace(), k -> new ArrayList())).add(loc);
            }
        }
        if (texture != null && !texture.isEmpty() && !texture.startsWith("http")) {
            this.selectedResource = new ResourceLocation(texture);
            this.selectedDomain = this.selectedResource.getNamespace();
            if (!domains.containsKey(this.selectedDomain)) {
                this.selectedDomain = null;
            }
            int i = this.selectedResource.getPath().lastIndexOf(47);
            this.location = this.selectedResource.getPath().substring(0, i + 1);
        }
    }

    public static void clear() {
        domains.clear();
        textures.clear();
    }

    @Override
    public void init() {
        super.m_7856_();
        if (this.selectedDomain != null) {
            this.title = this.selectedDomain + ":" + this.location;
        } else {
            this.title = "";
        }
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 264, this.guiTop + 170, 90, 20, "gui.done"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 264, this.guiTop + 190, 90, 20, "gui.cancel"));
        if (this.scrollCategories == null) {
            this.scrollCategories = new GuiCustomScrollNop(this, 0);
            this.scrollCategories.setSize(120, 200);
        }
        if (this.selectedDomain == null) {
            this.scrollCategories.setList(Lists.newArrayList(domains.keySet()));
            if (this.selectedDomain != null) {
                this.scrollCategories.setSelected(this.selectedDomain);
            }
        } else {
            List<String> list = new ArrayList();
            list.add(this.up);
            for (ResourceLocation td : (List) domains.get(this.selectedDomain)) {
                String fullPath = td.getPath();
                if (fullPath.indexOf(47) >= 0) {
                    fullPath = fullPath.substring(0, fullPath.lastIndexOf(47) + 1);
                }
                if (this.location.isEmpty() || fullPath.startsWith(this.location) && !fullPath.equals(this.location)) {
                    String path = fullPath.substring(this.location.length());
                    int i = path.indexOf(47);
                    if (i >= 0) {
                        path = path.substring(0, i);
                        if (!path.isEmpty() && !list.contains(path)) {
                            list.add(path);
                        }
                    }
                }
            }
            this.scrollCategories.setList(list);
        }
        this.scrollCategories.guiLeft = this.guiLeft + 4;
        this.scrollCategories.guiTop = this.guiTop + 14;
        this.addScroll(this.scrollCategories);
        if (this.scrollQuests == null) {
            this.scrollQuests = new GuiCustomScrollNop(this, 1);
            this.scrollQuests.setSize(130, 200);
        }
        if (this.selectedDomain != null) {
            textures.clear();
            List<ResourceLocation> data = (List<ResourceLocation>) domains.get(this.selectedDomain);
            List<String> list = new ArrayList();
            String loc = this.location;
            if (this.scrollCategories.hasSelected() && !this.scrollCategories.getSelected().equals(this.up)) {
                loc = loc + this.scrollCategories.getSelected() + "/";
            }
            for (ResourceLocation td : data) {
                String name = td.getPath();
                String path = td.getPath();
                if (name.indexOf(47) >= 0) {
                    name = name.substring(name.lastIndexOf(47) + 1);
                    path = path.substring(0, path.lastIndexOf(47) + 1);
                }
                if (path.equals(loc) && !list.contains(name)) {
                    list.add(name);
                    textures.put(name, td);
                }
            }
            this.scrollQuests.setList(list);
        }
        if (this.selectedResource != null) {
            this.scrollQuests.setSelected(this.selectedResource.getPath());
        }
        this.scrollQuests.guiLeft = this.guiLeft + 125;
        this.scrollQuests.guiTop = this.guiTop + 14;
        this.addScroll(this.scrollQuests);
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        this.npc.textureLocation = null;
        if (guibutton.id == 2) {
            if (this.type == 0) {
                this.npc.display.setSkinTexture(this.selectedResource.toString());
            }
            if (this.type == 1) {
                this.npc.display.setCapeTexture(this.selectedResource.toString());
            }
            if (this.type == 2) {
                this.npc.display.setOverlayTexture(this.selectedResource.toString());
            }
        }
        this.close();
        if (this.wrapper.parent instanceof IGuiInterface igui) {
            igui.initGui();
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        if (this.type == 0) {
            this.npc.textureLocation = this.selectedResource;
        }
        if (this.type == 1) {
            this.npc.textureCloakLocation = this.selectedResource;
        }
        if (this.type == 2) {
            this.npc.textureGlowLocation = this.selectedResource;
        }
        this.drawNpc(graphics, this.npc, 333, 154, 2.0F, this.type == 1 ? 180 : 0);
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop scroll) {
        if (scroll == this.scrollQuests) {
            if (scroll.id == 1) {
                this.selectedResource = (ResourceLocation) textures.get(scroll.getSelected());
            }
        } else {
            this.init();
        }
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
        if (scroll == this.scrollCategories) {
            if (this.selectedDomain == null) {
                this.selectedDomain = selection;
            } else if (selection.equals(this.up)) {
                int i = this.location.lastIndexOf(47, this.location.length() - 2);
                if (i < 0) {
                    if (this.location.isEmpty()) {
                        this.selectedDomain = null;
                    }
                    this.location = "";
                } else {
                    this.location = this.location.substring(0, i + 1);
                }
            } else {
                this.location = this.location + selection + "/";
            }
            this.scrollCategories.clearSelection();
            this.scrollQuests.clearSelection();
            this.init();
        } else {
            if (this.type == 0) {
                this.npc.display.setSkinTexture(this.selectedResource.toString());
            }
            if (this.type == 1) {
                this.npc.display.setCapeTexture(this.selectedResource.toString());
            }
            if (this.type == 2) {
                this.npc.display.setOverlayTexture(this.selectedResource.toString());
            }
            this.close();
            if (this.wrapper.parent instanceof IGuiInterface igui) {
                igui.initGui();
            }
        }
    }
}