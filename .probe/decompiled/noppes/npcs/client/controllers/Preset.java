package noppes.npcs.client.controllers;

import java.util.HashMap;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.ModelData;
import noppes.npcs.constants.EnumParts;

public class Preset {

    public ModelData data = new ModelData(null);

    public String name;

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        compound.putString("PresetName", this.name);
        compound.put("PresetData", this.data.save());
        return compound;
    }

    public void load(CompoundTag compound) {
        this.name = compound.getString("PresetName");
        this.data.load(compound.getCompound("PresetData"));
    }

    public static void FillDefault(HashMap<String, Preset> presets) {
        ModelData data = new ModelData(null);
        Preset preset = new Preset();
        preset.name = "Elf Male";
        preset.data = data;
        data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.85F, 1.15F);
        data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.85F, 1.15F);
        data.getPartConfig(EnumParts.BODY).setScale(0.85F, 1.15F);
        data.getPartConfig(EnumParts.HEAD).setScale(0.85F, 0.95F);
        presets.put("elf male", preset);
        data = new ModelData(null);
        preset = new Preset();
        preset.name = "Elf Female";
        preset.data = data;
        data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.8F, 1.05F);
        data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.8F, 1.05F);
        data.getPartConfig(EnumParts.BODY).setScale(0.8F, 1.05F);
        data.getPartConfig(EnumParts.HEAD).setScale(0.8F, 0.85F);
        presets.put("elf female", preset);
        data = new ModelData(null);
        preset = new Preset();
        preset.name = "Dwarf Male";
        preset.data = data;
        data.getPartConfig(EnumParts.LEG_LEFT).setScale(1.1F, 0.7F, 0.9F);
        data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.9F, 0.7F);
        data.getPartConfig(EnumParts.BODY).setScale(1.2F, 0.7F, 1.5F);
        data.getPartConfig(EnumParts.HEAD).setScale(0.85F, 0.85F);
        presets.put("dwarf male", preset);
        data = new ModelData(null);
        preset = new Preset();
        preset.name = "Dwarf Female";
        preset.data = data;
        data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.9F, 0.65F);
        data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.9F, 0.65F);
        data.getPartConfig(EnumParts.BODY).setScale(1.0F, 0.65F, 1.1F);
        data.getPartConfig(EnumParts.HEAD).setScale(0.85F, 0.85F);
        presets.put("dwarf female", preset);
        data = new ModelData(null);
        preset = new Preset();
        preset.name = "Orc Male";
        preset.data = data;
        data.getPartConfig(EnumParts.LEG_LEFT).setScale(1.2F, 1.05F);
        data.getPartConfig(EnumParts.ARM_LEFT).setScale(1.2F, 1.05F);
        data.getPartConfig(EnumParts.BODY).setScale(1.4F, 1.1F, 1.5F);
        data.getPartConfig(EnumParts.HEAD).setScale(1.2F, 1.1F);
        presets.put("orc male", preset);
        data = new ModelData(null);
        preset = new Preset();
        preset.name = "Orc Female";
        preset.data = data;
        data.getPartConfig(EnumParts.LEG_LEFT).setScale(1.1F, 1.0F);
        data.getPartConfig(EnumParts.ARM_LEFT).setScale(1.1F, 1.0F);
        data.getPartConfig(EnumParts.BODY).setScale(1.1F, 1.0F, 1.25F);
        presets.put("orc female", preset);
        data = new ModelData(null);
        preset = new Preset();
        preset.name = "Human Male";
        preset.data = data;
        presets.put("human male", preset);
        data = new ModelData(null);
        preset = new Preset();
        preset.name = "Human Female";
        preset.data = data;
        data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.92F, 0.92F);
        data.getPartConfig(EnumParts.HEAD).setScale(0.95F, 0.95F);
        data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.8F, 0.92F);
        data.getPartConfig(EnumParts.BODY).setScale(0.92F, 0.92F);
        presets.put("human female", preset);
        data = new ModelData(null);
        preset = new Preset();
        preset.name = "Cat Male";
        preset.data = data;
        presets.put("cat male", preset);
        data = new ModelData(null);
        preset = new Preset();
        preset.name = "Cat Female";
        preset.data = data;
        data.getPartConfig(EnumParts.HEAD).setScale(0.95F, 0.95F);
        data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.92F, 0.92F);
        data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.8F, 0.92F);
        data.getPartConfig(EnumParts.BODY).setScale(0.92F, 0.92F);
        presets.put("cat female", preset);
        data = new ModelData(null);
        preset = new Preset();
        preset.name = "Wolf Male";
        preset.data = data;
        presets.put("wolf male", preset);
        data = new ModelData(null);
        preset = new Preset();
        preset.name = "Wolf Female";
        preset.data = data;
        data.getPartConfig(EnumParts.HEAD).setScale(0.95F, 0.95F);
        data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.92F, 0.92F);
        data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.8F, 0.92F);
        data.getPartConfig(EnumParts.BODY).setScale(0.92F, 0.92F);
        presets.put("wolf female", preset);
        data = new ModelData(null);
        preset = new Preset();
        preset.name = "Enderchibi";
        preset.data = data;
        data.getPartConfig(EnumParts.LEG_LEFT).setScale(0.65F, 0.75F);
        data.getPartConfig(EnumParts.ARM_LEFT).setScale(0.5F, 1.45F);
        presets.put("enderchibi", preset);
    }
}