package noppes.npcs.client.gui.custom.components;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiAssetsSelectorWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiLabelWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.interfaces.IGuiComponent;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketCustomGuiButton;
import noppes.npcs.packets.server.SPacketCustomGuiTextUpdate;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;
import noppes.npcs.shared.client.util.AssetsFinder;

public class CustomGuiAssetsSelector extends AbstractWidget implements IGuiComponent {

    private String up = "..<" + I18n.get("gui.up") + ">..";

    private GuiCustom parent;

    private CustomGuiAssetsSelectorWrapper component;

    private GuiCustomScrollNop folders;

    private GuiCustomScrollNop items;

    private CustomGuiLabel label;

    public int id;

    private static final HashMap<String, List<ResourceLocation>> domains = new HashMap();

    private static final HashMap<String, ResourceLocation> textures = new HashMap();

    private String location = "";

    private String path = "";

    private String selectedDomain;

    public ResourceLocation prevResource = null;

    public ResourceLocation selectedResource = null;

    public CustomGuiAssetsSelector(GuiCustom parent, final CustomGuiAssetsSelectorWrapper component) {
        super(component.getPosX(), component.getPosY(), component.getWidth(), component.getHeight(), Component.empty());
        this.parent = parent;
        this.component = component;
        this.folders = new GuiCustomScrollNop(parent, 101);
        this.items = new GuiCustomScrollNop(parent, 102);
        this.label = new CustomGuiLabel(parent, (CustomGuiLabelWrapper) new CustomGuiLabelWrapper().setCentered(true));
        this.init();
        if (!component.getSelected().isEmpty()) {
            this.selectedResource = this.prevResource = new ResourceLocation(component.getSelected());
        }
        for (ResourceLocation loc : AssetsFinder.find(component.getRoot(), "." + component.getFileType())) {
            ((List) domains.computeIfAbsent(loc.getNamespace(), k -> new ArrayList())).add(loc);
        }
        if (this.selectedResource != null && !this.selectedResource.getPath().isEmpty()) {
            this.selectedDomain = this.selectedResource.getNamespace();
            if (!domains.containsKey(this.selectedDomain)) {
                this.selectedDomain = null;
            }
            int i = this.selectedResource.getPath().lastIndexOf(47);
            this.location = this.path = this.selectedResource.getPath().substring(0, i + 1);
            i = this.path.lastIndexOf(47, this.path.length() - 2);
            if (i > 0) {
                this.location = this.path.substring(0, i + 1);
            }
            this.label.setText(this.selectedDomain + ":" + this.location);
        }
        this.setFolders();
        this.setItems();
        this.folders.listener = new ICustomScrollListener() {

            @Override
            public void scrollClicked(double x, double y, int k, GuiCustomScrollNop scroll) {
                if (!scroll.getSelected().equals(CustomGuiAssetsSelector.this.up)) {
                    CustomGuiAssetsSelector.this.path = CustomGuiAssetsSelector.this.location + scroll.getSelected() + "/";
                    CustomGuiAssetsSelector.this.setItems();
                }
            }

            @Override
            public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
                if (CustomGuiAssetsSelector.this.selectedDomain == null) {
                    CustomGuiAssetsSelector.this.selectedDomain = scroll.getSelected();
                    if (!component.getRoot().isEmpty()) {
                        CustomGuiAssetsSelector.this.path = CustomGuiAssetsSelector.this.location = component.getRoot() + "/";
                    }
                } else if (scroll.getSelected().equals(CustomGuiAssetsSelector.this.up)) {
                    int i = CustomGuiAssetsSelector.this.location.lastIndexOf(47, CustomGuiAssetsSelector.this.location.length() - 2);
                    if (i > 0) {
                        CustomGuiAssetsSelector.this.path = CustomGuiAssetsSelector.this.location;
                        CustomGuiAssetsSelector.this.location = CustomGuiAssetsSelector.this.location.substring(0, i + 1);
                    } else {
                        CustomGuiAssetsSelector.this.path = CustomGuiAssetsSelector.this.location = "";
                    }
                    if (CustomGuiAssetsSelector.this.location.isEmpty()) {
                        CustomGuiAssetsSelector.this.selectedDomain = null;
                    }
                } else {
                    CustomGuiAssetsSelector.this.path = CustomGuiAssetsSelector.this.location = CustomGuiAssetsSelector.this.location + scroll.getSelected() + "/";
                }
                CustomGuiAssetsSelector.this.setFolders();
                CustomGuiAssetsSelector.this.setItems();
                CustomGuiAssetsSelector.this.label.setText(CustomGuiAssetsSelector.this.selectedDomain + ":" + CustomGuiAssetsSelector.this.location);
            }
        };
        this.items.listener = new ICustomScrollListener() {

            @Override
            public void scrollClicked(double i, double j, int k, GuiCustomScrollNop scroll) {
                CustomGuiAssetsSelector.this.selectedResource = (ResourceLocation) CustomGuiAssetsSelector.textures.get(scroll.getSelected());
                component.setSelected(((ResourceLocation) CustomGuiAssetsSelector.textures.get(scroll.getSelected())).toString());
                if (!component.disablePackets) {
                    Packets.sendServer(new SPacketCustomGuiTextUpdate(component.getUniqueID(), component.getSelected()));
                } else {
                    component.onChange(null);
                }
            }

            @Override
            public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
                if (!component.disablePackets) {
                    Packets.sendServer(new SPacketCustomGuiButton(component.getUniqueID()));
                } else {
                    component.onPress(null);
                }
            }
        };
    }

    @Override
    public void init() {
        this.id = this.component.getID();
        this.m_252865_(this.component.getPosX());
        this.m_253211_(this.component.getPosY());
        this.folders.guiTop = this.items.guiTop = this.m_252907_() + 10;
        this.m_93674_(this.component.getWidth());
        this.setHeight(this.component.getHeight());
        this.folders.setSize(this.component.getWidth() / 2 - 1, this.component.getHeight() - 10);
        this.items.setSize(this.component.getWidth() / 2 - 1, this.component.getHeight() - 10);
        this.folders.guiLeft = this.m_252754_();
        this.items.guiLeft = this.m_252754_() + this.component.getWidth() / 2 + 1;
        this.label.m_93674_(this.component.getWidth());
        this.label.m_252865_(this.m_252754_());
        this.label.m_253211_(this.m_252907_());
        this.label.setHeight(10);
        if (!this.component.getSelected().isEmpty()) {
            this.selectedResource = new ResourceLocation(this.component.getSelected());
        }
    }

    private void setFolders() {
        if (this.selectedDomain == null) {
            this.folders.setList(Lists.newArrayList(domains.keySet()));
            if (this.selectedResource != null) {
                this.selectedDomain = this.selectedResource.getNamespace();
                this.folders.setSelected(this.selectedDomain);
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
            this.folders.clearSelection();
            this.folders.setList(list);
            if (this.selectedResource != null && this.selectedResource.getPath().startsWith(this.location) && !this.location.equals(this.path)) {
                this.folders.setSelected(this.path.substring(this.location.length(), this.path.length() - 1));
                this.folders.scrollTo(this.folders.getSelected());
            }
        }
    }

    private void setItems() {
        if (this.selectedDomain != null) {
            textures.clear();
            List<ResourceLocation> data = (List<ResourceLocation>) domains.get(this.selectedDomain);
            List<String> list = new ArrayList();
            for (ResourceLocation td : data) {
                String name = td.getPath();
                String path = td.getPath();
                if (name.indexOf(47) >= 0) {
                    name = name.substring(name.lastIndexOf(47) + 1);
                    path = path.substring(0, path.lastIndexOf(47) + 1);
                }
                if (path.equals(this.path) && !list.contains(name)) {
                    list.add(name);
                    textures.put(name, td);
                }
            }
            this.items.clearSelection();
            this.items.setList(list);
            if (this.selectedResource != null) {
                int i = this.selectedResource.getPath().lastIndexOf(47);
                String namex = this.selectedResource.getPath().substring(i + 1);
                String pathx = this.selectedResource.getPath().substring(0, i + 1);
                if (pathx.equals(this.path)) {
                    this.items.setSelected(namex);
                }
            }
        }
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public void onRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public void onRenderPost(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93624_) {
            this.label.onRender(graphics, mouseX, mouseY, partialTicks);
            this.folders.render(graphics, mouseX, mouseY, partialTicks);
            this.items.render(graphics, mouseX, mouseY, partialTicks);
            boolean hovered = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            if (hovered && this.component.hasHoverText()) {
                this.parent.hoverText = this.component.getHoverTextList();
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double mouseScrolled) {
        return this.folders.mouseScrolled(mouseX, mouseY, mouseScrolled) || this.items.mouseScrolled(mouseX, mouseY, mouseScrolled);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.folders.mouseClicked(mouseX, mouseY, button) || this.items.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        return this.folders.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_) || this.items.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
    }

    @Override
    public boolean charTyped(char p_231042_1_, int p_231042_2_) {
        return this.folders.charTyped(p_231042_1_, p_231042_2_) || this.items.charTyped(p_231042_1_, p_231042_2_);
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
    }

    @Override
    public ICustomGuiComponent component() {
        return this.component;
    }
}