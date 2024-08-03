package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.Level;
import noppes.npcs.CustomNpcs;
import noppes.npcs.schematics.Blueprint;
import noppes.npcs.schematics.BlueprintUtil;
import noppes.npcs.schematics.ISchematic;
import noppes.npcs.schematics.Schematic;
import noppes.npcs.schematics.SchematicWrapper;
import noppes.npcs.schematics.SpongeSchem;
import noppes.npcs.shared.common.CommonUtil;
import noppes.npcs.shared.common.util.LogWriter;
import noppes.npcs.util.ValueUtil;

public class SchematicController {

    public static SchematicController Instance = new SchematicController();

    private SchematicWrapper building = null;

    private CommandSourceStack buildStarter = null;

    private int buildingPercentage = 0;

    public List<String> included = Arrays.asList("archery_range.schematic", "bakery.schematic", "barn.schematic", "building_site.schematic", "chapel.schematic", "church.schematic", "gate.schematic", "glassworks.schematic", "guard_tower.schematic", "guild_house.schematic", "house.schematic", "house_small.schematic", "inn.schematic", "library.schematic", "lighthouse.schematic", "mill.schematic", "observatory.schematic", "ship.schematic", "shop.schematic", "stall.schematic", "stall2.schematic", "stall3.schematic", "tier_house1.schematic", "tier_house2.schematic", "tier_house3.schematic", "tower.schematic", "wall.schematic", "wall_corner.schematic");

    public List<String> list() {
        List<String> list = new ArrayList();
        list.addAll(this.included);
        for (File file : this.getDir().listFiles()) {
            String name = file.getName();
            if (ValueUtil.isValidPath(name) && (name.toLowerCase().endsWith(".schematic") || name.toLowerCase().endsWith(".schem") || name.toLowerCase().endsWith(".blueprint"))) {
                list.add(name);
            }
        }
        Collections.sort(list);
        return list;
    }

    public File getDir() {
        File dir = new File(CustomNpcs.getLevelSaveDirectory(), "schematics");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }

    public void info(CommandSourceStack sender) {
        if (this.building == null) {
            this.sendMessage(sender, "Nothing is being build");
        } else {
            this.sendMessage(sender, "Already building: " + this.building.schema.getName() + " - " + this.building.getPercentage() + "%");
            if (this.buildStarter != null) {
                this.sendMessage(sender, "Build started by: " + this.buildStarter.getDisplayName());
            }
        }
    }

    private void sendMessage(CommandSourceStack sender, String message) {
        if (sender != null) {
            sender.sendSuccess(() -> Component.literal(message), false);
        }
    }

    public void stop(CommandSourceStack sender) {
        if (this.building != null && this.building.isBuilding) {
            this.sendMessage(sender, "Stopped building: " + this.building.schema.getName());
            this.building = null;
        } else {
            this.sendMessage(sender, "Not building");
        }
    }

    public void build(SchematicWrapper schem, CommandSourceStack sender) {
        if (this.building != null && this.building.isBuilding) {
            this.info(sender);
        } else {
            this.buildingPercentage = 0;
            this.building = schem;
            this.building.isBuilding = true;
            this.buildStarter = sender;
        }
    }

    public void updateBuilding() {
        if (this.building != null) {
            this.building.build();
            if (this.buildStarter != null && this.building.getPercentage() - this.buildingPercentage >= 10) {
                this.sendMessage(this.buildStarter, "Building at " + this.building.getPercentage() + "%");
                this.buildingPercentage = this.building.getPercentage();
            }
            if (!this.building.isBuilding) {
                if (this.buildStarter != null) {
                    this.sendMessage(this.buildStarter, "Building finished");
                }
                this.building = null;
            }
        }
    }

    public SchematicWrapper load(String name) {
        InputStream stream = null;
        if (this.included.contains(name)) {
            ResourceLocation resource = new ResourceLocation("customnpcs", "schematics/" + name);
            Resource ir = (Resource) CustomNpcs.Server.getServerResources().resourceManager().m_213713_(resource).orElse(null);
            if (ir != null) {
                try {
                    stream = ir.open();
                } catch (IOException var8) {
                }
            }
        }
        if (stream == null) {
            File file = new File(this.getDir(), name);
            if (!file.exists()) {
                return null;
            }
            try {
                stream = new FileInputStream(file);
            } catch (FileNotFoundException var7) {
                return null;
            }
        }
        try {
            CompoundTag compound = NbtIo.readCompressed(stream);
            stream.close();
            if (name.toLowerCase().endsWith(".schem")) {
                SpongeSchem bp = new SpongeSchem(name);
                bp.load(compound);
                return new SchematicWrapper(bp);
            } else if (name.toLowerCase().endsWith(".blueprint")) {
                Blueprint bp = BlueprintUtil.readBlueprintFromNBT(compound);
                bp.setName(name);
                return new SchematicWrapper(bp);
            } else {
                Schematic schema = new Schematic(name);
                schema.load(compound);
                return new SchematicWrapper(schema);
            }
        } catch (IOException var6) {
            LogWriter.except(var6);
            return null;
        }
    }

    public void save(CommandSourceStack sender, String name, BlockPos pos, short height, short width, short length) {
        name = name.replace(" ", "_");
        if (!this.included.contains(name)) {
            Level level = sender.getLevel();
            File file = new File(this.getDir(), name + ".schem");
            ISchematic schema = SpongeSchem.Create(level, name, pos, height, width, length);
            CommonUtil.NotifyOPs(sender.getServer(), "Schematic " + name + " succesfully created");
            try {
                NbtIo.writeCompressed(schema.getNBT(), new FileOutputStream(file));
            } catch (Exception var11) {
                var11.printStackTrace();
            }
        }
    }
}