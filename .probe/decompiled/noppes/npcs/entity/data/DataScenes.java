package noppes.npcs.entity.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.constants.AnimationType;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.controllers.data.Line;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.EntityProjectile;
import noppes.npcs.shared.common.CommonUtil;
import noppes.npcs.util.ValueUtil;

public class DataScenes {

    private EntityNPCInterface npc;

    public List<DataScenes.SceneContainer> scenes = new ArrayList();

    public static Map<String, DataScenes.SceneState> StartedScenes = new HashMap();

    public static List<DataScenes.SceneContainer> ScenesToRun = new ArrayList();

    private LivingEntity owner = null;

    private String ownerScene = null;

    public DataScenes(EntityNPCInterface npc) {
        this.npc = npc;
    }

    public CompoundTag save(CompoundTag compound) {
        ListTag list = new ListTag();
        for (DataScenes.SceneContainer scene : this.scenes) {
            list.add(scene.save(new CompoundTag()));
        }
        compound.put("Scenes", list);
        return compound;
    }

    public void load(CompoundTag compound) {
        ListTag list = compound.getList("Scenes", 10);
        List<DataScenes.SceneContainer> scenes = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            DataScenes.SceneContainer scene = new DataScenes.SceneContainer();
            scene.load(list.getCompound(i));
            scenes.add(scene);
        }
        this.scenes = scenes;
    }

    public LivingEntity getOwner() {
        return this.owner;
    }

    public static void Toggle(MinecraftServer server, String id) {
        DataScenes.SceneState state = (DataScenes.SceneState) StartedScenes.get(id.toLowerCase());
        if (state != null && !state.paused) {
            state.paused = true;
            CommonUtil.NotifyOPs(server, "Paused scene %s at %s", id, state.ticks);
        } else {
            Start(server, id);
        }
    }

    public static void Start(MinecraftServer server, String id) {
        DataScenes.SceneState state = (DataScenes.SceneState) StartedScenes.get(id.toLowerCase());
        if (state == null) {
            CommonUtil.NotifyOPs(server, "Started scene %s", id);
            StartedScenes.put(id.toLowerCase(), new DataScenes.SceneState());
        } else if (state.paused) {
            state.paused = false;
            CommonUtil.NotifyOPs(server, "Started scene %s from %s", id, state.ticks);
        }
    }

    public static void Pause(CommandSourceStack sender, String id) {
        if (id == null) {
            for (DataScenes.SceneState state : StartedScenes.values()) {
                state.paused = true;
            }
            CommonUtil.NotifyOPs(sender.getServer(), "Paused all scenes");
        } else {
            DataScenes.SceneState state = (DataScenes.SceneState) StartedScenes.get(id.toLowerCase());
            if (state == null) {
                sender.sendSuccess(() -> Component.translatable("Unknown scene %s ", id), false);
            } else {
                state.paused = true;
                CommonUtil.NotifyOPs(sender.getServer(), "Paused scene %s at %s", id, state.ticks);
            }
        }
    }

    public static void Reset(CommandSourceStack sender, String id) {
        if (id == null) {
            if (StartedScenes.isEmpty()) {
                return;
            }
            StartedScenes = new HashMap();
            CommonUtil.NotifyOPs(sender.getServer(), "Reset all scene");
        } else if (StartedScenes.remove(id.toLowerCase()) == null) {
            sender.sendSuccess(() -> Component.translatable("Unknown scene %s ", id), false);
        } else {
            CommonUtil.NotifyOPs(sender.getServer(), "Reset scene %s", id);
        }
    }

    public void update() {
        for (DataScenes.SceneContainer scene : this.scenes) {
            if (scene.validState()) {
                ScenesToRun.add(scene);
            }
        }
        if (this.owner != null && !StartedScenes.containsKey(this.ownerScene.toLowerCase())) {
            this.owner = null;
            this.ownerScene = null;
        }
    }

    public void addScene(String name) {
        if (!name.isEmpty()) {
            DataScenes.SceneContainer scene = new DataScenes.SceneContainer();
            scene.name = name;
            this.scenes.add(scene);
        }
    }

    public class SceneContainer {

        public int btn = 0;

        public String name = "";

        public String lines = "";

        public boolean enabled = false;

        public int ticks = -1;

        private DataScenes.SceneState state = null;

        private List<DataScenes.SceneEvent> events = new ArrayList();

        public CompoundTag save(CompoundTag compound) {
            compound.putBoolean("Enabled", this.enabled);
            compound.putString("Name", this.name);
            compound.putString("Lines", this.lines);
            compound.putInt("Button", this.btn);
            compound.putInt("Ticks", this.ticks);
            return compound;
        }

        public boolean validState() {
            if (!this.enabled) {
                return false;
            } else {
                if (this.state != null) {
                    if (DataScenes.StartedScenes.containsValue(this.state)) {
                        return !this.state.paused;
                    }
                    this.state = null;
                }
                this.state = (DataScenes.SceneState) DataScenes.StartedScenes.get(this.name.toLowerCase());
                if (this.state == null) {
                    this.state = (DataScenes.SceneState) DataScenes.StartedScenes.get(this.btn + "btn");
                }
                return this.state != null ? !this.state.paused : false;
            }
        }

        public void load(CompoundTag compound) {
            this.enabled = compound.getBoolean("Enabled");
            this.name = compound.getString("Name");
            this.lines = compound.getString("Lines");
            this.btn = compound.getInt("Button");
            this.ticks = compound.getInt("Ticks");
            ArrayList<DataScenes.SceneEvent> events = new ArrayList();
            for (String line : this.lines.split("\r\n|\r|\n")) {
                DataScenes.SceneEvent event = DataScenes.SceneEvent.parse(line);
                if (event != null) {
                    events.add(event);
                }
            }
            Collections.sort(events);
            this.events = events;
        }

        public void update() {
            if (this.enabled && !this.events.isEmpty() && this.state != null) {
                for (DataScenes.SceneEvent event : this.events) {
                    if (event.ticks > this.state.ticks) {
                        break;
                    }
                    if (event.ticks == this.state.ticks) {
                        try {
                            this.handle(event);
                        } catch (Exception var4) {
                        }
                    }
                }
                this.ticks = this.state.ticks;
            }
        }

        private LivingEntity getEntity(String name) {
            UUID uuid = null;
            try {
                uuid = UUID.fromString(name);
            } catch (Exception var5) {
            }
            for (Entity entity : ((ServerLevel) DataScenes.this.npc.getCommandSenderWorld()).getEntities().getAll()) {
                if (entity instanceof LivingEntity) {
                    if (uuid != null && entity.getUUID() == uuid) {
                        return (LivingEntity) entity;
                    }
                    if (name.equalsIgnoreCase(entity.getName().getString())) {
                        return (LivingEntity) entity;
                    }
                }
            }
            return null;
        }

        private BlockPos parseBlockPos(BlockPos blockpos, String[] args, int startIndex, boolean centerBlock) throws Exception {
            return new BlockPos((int) this.parseDouble((double) blockpos.m_123341_(), args[startIndex], -30000000, 30000000, centerBlock), (int) this.parseDouble((double) blockpos.m_123342_(), args[startIndex + 1], 0, 256, false), (int) this.parseDouble((double) blockpos.m_123343_(), args[startIndex + 2], -30000000, 30000000, centerBlock));
        }

        private double parseDouble(double base, String input, int min, int max, boolean centerBlock) throws Exception {
            boolean flag = input.startsWith("~");
            if (flag && Double.isNaN(base)) {
                throw new Exception("invalid number");
            } else {
                double d0 = flag ? base : 0.0;
                if (!flag || input.length() > 1) {
                    boolean flag1 = input.contains(".");
                    if (flag) {
                        input = input.substring(1);
                    }
                    d0 += Double.parseDouble(input);
                    if (!flag1 && !flag && centerBlock) {
                        d0 += 0.5;
                    }
                }
                if (min != 0 || max != 0) {
                    if (d0 < (double) min) {
                        throw new Exception("number too small");
                    }
                    if (d0 > (double) max) {
                        throw new Exception("number too big");
                    }
                }
                return d0;
            }
        }

        private void handle(DataScenes.SceneEvent event) throws Exception {
            if (event.type == DataScenes.SceneType.MOVE) {
                String[] param = event.param.split(" ");
                while (param.length > 1) {
                    boolean move = false;
                    if (param[0].startsWith("to")) {
                        move = true;
                    } else if (!param[0].startsWith("tp")) {
                        break;
                    }
                    BlockPos pos = null;
                    if (param[0].startsWith("@")) {
                        LivingEntity entitylivingbase = this.getEntity(param[0]);
                        if (entitylivingbase != null) {
                            pos = entitylivingbase.m_20183_();
                        }
                        param = (String[]) Arrays.copyOfRange(param, 2, param.length);
                    } else {
                        if (param.length < 4) {
                            return;
                        }
                        pos = this.parseBlockPos(DataScenes.this.npc.m_20183_(), param, 1, false);
                        param = (String[]) Arrays.copyOfRange(param, 4, param.length);
                    }
                    if (pos != null) {
                        DataScenes.this.npc.ais.setStartPos(pos);
                        DataScenes.this.npc.m_21573_().stop();
                        if (move) {
                            Path pathentity = DataScenes.this.npc.m_21573_().createPath(pos, 0);
                            DataScenes.this.npc.m_21573_().moveTo(pathentity, 1.0);
                        } else if (!DataScenes.this.npc.isInRange((double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5, 2.0)) {
                            DataScenes.this.npc.m_6034_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5);
                        }
                    }
                }
            } else if (event.type == DataScenes.SceneType.SAY) {
                DataScenes.this.npc.saySurrounding(new Line(event.param));
            } else if (event.type == DataScenes.SceneType.ROTATE) {
                if (event.param.startsWith("@")) {
                    LivingEntity entitylivingbase = this.getEntity(event.param);
                    DataScenes.this.npc.lookAi.rotate(DataScenes.this.npc.m_9236_().m_45930_(entitylivingbase, 30.0));
                } else if (event.param.equals("clear")) {
                    DataScenes.this.npc.lookAi.stop();
                } else {
                    DataScenes.this.npc.lookAi.rotate(Integer.parseInt(event.param));
                }
            } else if (event.type == DataScenes.SceneType.EQUIP) {
                String[] args = event.param.split(" ");
                if (args.length < 2) {
                    return;
                }
                IItemStack itemstack = null;
                if (!args[1].equalsIgnoreCase("none")) {
                    ResourceLocation resourcelocation = new ResourceLocation(args[1]);
                    Item item = ForgeRegistries.ITEMS.getValue(resourcelocation);
                    int i = args.length >= 3 ? ValueUtil.CorrectInt(Integer.parseInt(args[2]), 1, 64) : 1;
                    itemstack = NpcAPI.Instance().getIItemStack(new ItemStack(item, i));
                }
                if (args[0].equalsIgnoreCase("main")) {
                    DataScenes.this.npc.inventory.weapons.put(0, itemstack);
                } else if (args[0].equalsIgnoreCase("off")) {
                    DataScenes.this.npc.inventory.weapons.put(2, itemstack);
                } else if (args[0].equalsIgnoreCase("proj")) {
                    DataScenes.this.npc.inventory.weapons.put(1, itemstack);
                } else if (args[0].equalsIgnoreCase("head")) {
                    DataScenes.this.npc.inventory.armor.put(0, itemstack);
                } else if (args[0].equalsIgnoreCase("body")) {
                    DataScenes.this.npc.inventory.armor.put(1, itemstack);
                } else if (args[0].equalsIgnoreCase("legs")) {
                    DataScenes.this.npc.inventory.armor.put(2, itemstack);
                } else if (args[0].equalsIgnoreCase("boots")) {
                    DataScenes.this.npc.inventory.armor.put(3, itemstack);
                }
            } else if (event.type == DataScenes.SceneType.ATTACK) {
                if (event.param.equals("none")) {
                    DataScenes.this.npc.setTarget(null);
                } else {
                    LivingEntity entity = this.getEntity(event.param);
                    if (entity != null) {
                        DataScenes.this.npc.setTarget(entity);
                    }
                }
            } else if (event.type == DataScenes.SceneType.THROW) {
                String[] argsx = event.param.split(" ");
                LivingEntity entity = this.getEntity(argsx[0]);
                if (entity == null) {
                    return;
                }
                float damage = Float.parseFloat(argsx[1]);
                if (damage <= 0.0F) {
                    damage = 0.01F;
                }
                ItemStack stack = ItemStackWrapper.MCItem(DataScenes.this.npc.inventory.getProjectile());
                if (argsx.length > 2) {
                    ResourceLocation resourcelocation = new ResourceLocation(argsx[2]);
                    Item item = ForgeRegistries.ITEMS.getValue(resourcelocation);
                    stack = new ItemStack(item, 1);
                }
                EntityProjectile projectile = DataScenes.this.npc.shoot(entity, 100, stack, false);
                projectile.damage = damage;
            } else if (event.type == DataScenes.SceneType.ANIMATE) {
                DataScenes.this.npc.animateAi.temp = AnimationType.valueOf(event.param);
            } else if (event.type == DataScenes.SceneType.COMMAND) {
                NoppesUtilServer.runCommand(DataScenes.this.npc, DataScenes.this.npc.getName().getString(), event.param, null);
            } else if (event.type == DataScenes.SceneType.STATS) {
                int i = event.param.indexOf(" ");
                if (i <= 0) {
                    return;
                }
                String type = event.param.substring(0, i).toLowerCase();
                String value = event.param.substring(i).trim();
                try {
                    if (type.equals("walking_speed")) {
                        DataScenes.this.npc.ais.setWalkingSpeed(ValueUtil.CorrectInt(Integer.parseInt(value), 0, 10));
                    } else if (type.equals("size")) {
                        DataScenes.this.npc.display.setSize(ValueUtil.CorrectInt(Integer.parseInt(value), 1, 30));
                    } else {
                        CommonUtil.NotifyOPs(DataScenes.this.npc.m_9236_().getServer(), "Unknown scene stat: " + type);
                    }
                } catch (NumberFormatException var8) {
                    CommonUtil.NotifyOPs(DataScenes.this.npc.m_9236_().getServer(), "Unknown scene stat " + type + " value: " + value);
                }
            } else if (event.type == DataScenes.SceneType.FACTION) {
                DataScenes.this.npc.setFaction(Integer.parseInt(event.param));
            } else if (event.type == DataScenes.SceneType.FOLLOW) {
                if (event.param.equalsIgnoreCase("none")) {
                    DataScenes.this.owner = null;
                    DataScenes.this.ownerScene = null;
                } else {
                    LivingEntity entityx = this.getEntity(event.param);
                    if (entityx == null) {
                        return;
                    }
                    DataScenes.this.owner = entityx;
                    DataScenes.this.ownerScene = this.name;
                }
            }
        }
    }

    public static class SceneEvent implements Comparable<DataScenes.SceneEvent> {

        public int ticks = 0;

        public DataScenes.SceneType type;

        public String param = "";

        public String toString() {
            return this.ticks + " " + this.type.name() + " " + this.param;
        }

        public static DataScenes.SceneEvent parse(String str) {
            DataScenes.SceneEvent event = new DataScenes.SceneEvent();
            int i = str.indexOf(" ");
            if (i <= 0) {
                return null;
            } else {
                try {
                    event.ticks = Integer.parseInt(str.substring(0, i));
                    str = str.substring(i + 1);
                } catch (NumberFormatException var8) {
                    return null;
                }
                i = str.indexOf(" ");
                if (i <= 0) {
                    return null;
                } else {
                    String name = str.substring(0, i);
                    for (DataScenes.SceneType type : DataScenes.SceneType.values()) {
                        if (name.equalsIgnoreCase(type.name())) {
                            event.type = type;
                        }
                    }
                    if (event.type == null) {
                        return null;
                    } else {
                        event.param = str.substring(i + 1);
                        return event;
                    }
                }
            }
        }

        public int compareTo(DataScenes.SceneEvent o) {
            return this.ticks - o.ticks;
        }
    }

    public static class SceneState {

        public boolean paused = false;

        public int ticks = -1;
    }

    public static enum SceneType {

        ANIMATE,
        MOVE,
        FACTION,
        COMMAND,
        EQUIP,
        THROW,
        ATTACK,
        FOLLOW,
        SAY,
        ROTATE,
        STATS
    }
}