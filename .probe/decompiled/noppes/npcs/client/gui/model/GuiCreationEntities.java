package noppes.npcs.client.gui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.CustomEntities;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiButtonYesNo;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;

public class GuiCreationEntities extends GuiCreationScreenInterface implements ICustomScrollListener {

    private List<EntityType<? extends Entity>> types;

    private GuiCustomScrollNop scroll;

    private boolean resetToSelected = true;

    public GuiCreationEntities(EntityNPCInterface npc) {
        super(npc);
        this.types = getAllEntities(npc.m_9236_());
        Collections.sort(this.types, Comparator.comparing(t -> t.getDescriptionId().toLowerCase()));
        this.active = 1;
        this.xOffset = 60;
    }

    private static List<EntityType<? extends Entity>> getAllEntities(Level level) {
        List<EntityType<? extends Entity>> data = new ArrayList();
        for (EntityType<? extends Entity> ent : ForgeRegistries.ENTITY_TYPES.getValues()) {
            try {
                Entity e = ent.create(level);
                if (e != null) {
                    if (LivingEntity.class.isAssignableFrom(e.getClass())) {
                        data.add(ent);
                    }
                    e.discard();
                }
            } catch (Exception var5) {
            }
        }
        return data;
    }

    @Override
    public void init() {
        super.init();
        this.addButton(new GuiButtonNop(this, 10, this.guiLeft, this.guiTop + 46, 120, 20, "Reset To NPC", button -> {
            this.playerdata.setEntity(null);
            this.npc.display.setSkinTexture("customnpcs:textures/entity/humanmale/steve.png");
            this.resetToSelected = true;
            this.init();
        }));
        if (this.scroll == null) {
            this.scroll = new GuiCustomScrollNop(this, 0);
            this.scroll.setUnsortedList((List<String>) this.types.stream().map(EntityType::m_20675_).collect(Collectors.toList()));
        }
        this.scroll.guiLeft = this.guiLeft;
        this.scroll.guiTop = this.guiTop + 68;
        this.scroll.setSize(120, this.imageHeight - 96);
        int index = -1;
        EntityType selectedType = CustomEntities.entityCustomNpc;
        if (this.entity != null) {
            for (int i = 0; i < this.types.size(); i++) {
                EntityType type = (EntityType) this.types.get(i);
                if (type == this.entity.m_6095_()) {
                    index = i;
                    break;
                }
            }
        }
        if (index >= 0) {
            this.scroll.setSelectedIndex(index);
        } else {
            this.scroll.setSelected("entity.customnpcs.customnpc");
        }
        if (this.resetToSelected) {
            this.scroll.scrollTo(this.scroll.getSelected());
            this.resetToSelected = false;
        }
        this.addScroll(this.scroll);
        this.addLabel(new GuiLabel(110, "gui.simpleRenderer", this.guiLeft + 124, this.guiTop + 5, 16711680));
        this.addButton(new GuiButtonYesNo(this, 110, this.guiLeft + 260, this.guiTop, this.playerdata.simpleRender, b -> this.playerdata.simpleRender = ((GuiButtonYesNo) b).getBoolean()));
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop scroll) {
        String selected = scroll.getSelected();
        if (selected.equals("entity.customnpcs.customnpc")) {
            this.playerdata.setEntity(null);
        } else {
            this.playerdata.setEntity(ForgeRegistries.ENTITY_TYPES.getKey((EntityType<?>) this.types.get(scroll.getSelectedIndex())));
        }
        Entity entity = this.playerdata.getEntity(this.npc);
        if (entity != null) {
            EntityRenderer render = this.f_96541_.getEntityRenderDispatcher().getRenderer(entity);
            if (render instanceof LivingEntityRenderer && !render.getTextureLocation(entity).equals("minecraft:missingno")) {
                this.npc.display.setSkinTexture(render.getTextureLocation(entity).toString());
            }
        } else {
            this.npc.display.setSkinTexture("customnpcs:textures/entity/humanmale/steve.png");
        }
        this.init();
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
    }
}