package com.almostreliable.morejs.features.structure;

import com.almostreliable.morejs.core.Events;
import dev.latvian.mods.kubejs.event.EventJS;
import java.util.function.Consumer;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class StructureLoadEventJS extends EventJS {

    private final StructureTemplateAccess structure;

    private final ResourceLocation id;

    public StructureLoadEventJS(StructureTemplateAccess structure, ResourceLocation id) {
        this.structure = structure;
        this.id = id;
    }

    public static void invoke(StructureTemplate structure, ResourceLocation id) {
        if (structure instanceof StructureTemplateAccess sta) {
            Events.STRUCTURE_LOAD.post(new StructureLoadEventJS(sta, id));
        }
    }

    public Vec3i getStructureSize() {
        return this.structure.getBorderSize();
    }

    public String getId() {
        return this.id.toString();
    }

    public int getPalettesSize() {
        return this.structure.getPalettes().size();
    }

    public int getEntitiesSize() {
        return this.structure.getEntities().size();
    }

    public void removePalette(int index) {
        this.structure.getPalettes().remove(index);
    }

    public PaletteWrapper getPalette(int index) {
        return new PaletteWrapper((StructureTemplate.Palette) this.structure.getPalettes().get(index), this.structure.getBorderSize());
    }

    public void forEachPalettes(Consumer<PaletteWrapper> consumer) {
        this.structure.getPalettes().forEach(palette -> consumer.accept(new PaletteWrapper(palette, this.structure.getBorderSize())));
    }

    public EntityInfoWrapper getEntities() {
        return new EntityInfoWrapper(this.structure.getEntities(), this.structure.getBorderSize());
    }
}