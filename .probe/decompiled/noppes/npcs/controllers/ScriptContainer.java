package noppes.npcs.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.Function;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.eventbus.api.Event;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NBTTags;
import noppes.npcs.api.constants.AnimationType;
import noppes.npcs.api.constants.EntitiesType;
import noppes.npcs.api.constants.JobType;
import noppes.npcs.api.constants.ParticleType;
import noppes.npcs.api.constants.PotionEffectType;
import noppes.npcs.api.constants.RoleType;
import noppes.npcs.api.constants.SideType;
import noppes.npcs.api.wrapper.BlockPosWrapper;
import noppes.npcs.constants.EnumScriptType;
import noppes.npcs.shared.client.util.NoppesStringUtils;
import noppes.npcs.shared.common.CommonUtil;
import noppes.npcs.shared.common.util.LogWriter;

public class ScriptContainer {

    private static final String lock = "lock";

    public static ScriptContainer Current;

    private static String CurrentType;

    public static final HashMap<String, Object> Data = new HashMap();

    public String fullscript = "";

    public String script = "";

    public TreeMap<Long, String> console = new TreeMap();

    public boolean errored = false;

    public List<String> scripts = new ArrayList();

    private HashSet<String> unknownFunctions = new HashSet();

    public long lastCreated = 0L;

    private String currentScriptLanguage = null;

    private ScriptEngine engine = null;

    private IScriptHandler handler = null;

    private boolean init = false;

    private static Method luaCoerce;

    private static Method luaCall;

    private static void FillMap(Class c) {
        try {
            Data.put(c.getSimpleName(), c.newInstance());
        } catch (Exception var8) {
        }
        Field[] declaredFields = c.getDeclaredFields();
        for (Field field : declaredFields) {
            try {
                if (Modifier.isStatic(field.getModifiers()) && field.getType() == int.class) {
                    Data.put(c.getSimpleName() + "_" + field.getName(), field.getInt(null));
                }
            } catch (Exception var7) {
            }
        }
    }

    public ScriptContainer(IScriptHandler handler) {
        this.handler = handler;
    }

    public void load(CompoundTag compound) {
        this.script = compound.getString("Script");
        this.console = NBTTags.GetLongStringMap(compound.getList("Console", 10));
        this.scripts = NBTTags.getStringList(compound.getList("ScriptList", 10));
        this.lastCreated = 0L;
    }

    public CompoundTag save(CompoundTag compound) {
        compound.putString("Script", this.script);
        compound.put("Console", NBTTags.NBTLongStringMap(this.console));
        compound.put("ScriptList", NBTTags.nbtStringList(this.scripts));
        return compound;
    }

    private String getFullCode() {
        if (!this.init) {
            this.fullscript = this.script;
            if (!this.fullscript.isEmpty()) {
                this.fullscript = this.fullscript + "\n";
            }
            for (String loc : this.scripts) {
                String code = (String) ScriptController.Instance.scripts.get(loc);
                if (code != null && !code.isEmpty()) {
                    this.fullscript = this.fullscript + code + "\n";
                }
            }
            this.unknownFunctions = new HashSet();
        }
        return this.fullscript;
    }

    public void run(EnumScriptType type, Event event) {
        this.run(type.function, event);
    }

    public void run(String type, Object event) {
        if (!this.errored && this.hasCode() && !this.unknownFunctions.contains(type) && CustomNpcs.EnableScripting) {
            this.setEngine(this.handler.getLanguage());
            if (this.engine != null) {
                if (ScriptController.Instance.lastLoaded > this.lastCreated) {
                    this.lastCreated = ScriptController.Instance.lastLoaded;
                    this.init = false;
                }
                synchronized ("lock") {
                    Current = this;
                    CurrentType = type;
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    this.engine.getContext().setWriter(pw);
                    this.engine.getContext().setErrorWriter(pw);
                    try {
                        if (!this.init) {
                            this.engine.eval(this.getFullCode());
                            this.init = true;
                        }
                        if (this.engine.getFactory().getLanguageName().equals("lua")) {
                            Object ob = this.engine.get(type);
                            if (ob != null) {
                                if (luaCoerce == null) {
                                    luaCoerce = Class.forName("org.luaj.vm2.lib.jse.CoerceJavaToLua").getMethod("coerce", Object.class);
                                    luaCall = ob.getClass().getMethod("call", Class.forName("org.luaj.vm2.LuaValue"));
                                }
                                luaCall.invoke(ob, luaCoerce.invoke(null, event));
                            } else {
                                this.unknownFunctions.add(type);
                            }
                        } else {
                            ((Invocable) this.engine).invokeFunction(type, new Object[] { event });
                        }
                    } catch (NoSuchMethodException var13) {
                        this.unknownFunctions.add(type);
                    } catch (Throwable var14) {
                        this.errored = true;
                        var14.printStackTrace(pw);
                        CommonUtil.NotifyOPs(CustomNpcs.Server, this.handler.noticeString() + " script errored");
                    } finally {
                        this.appandConsole(sw.getBuffer().toString().trim());
                        pw.close();
                        Current = null;
                    }
                }
            }
        }
    }

    public void appandConsole(String message) {
        if (message != null && !message.isEmpty()) {
            long time = System.currentTimeMillis();
            if (this.console.containsKey(time)) {
                message = (String) this.console.get(time) + "\n" + message;
            }
            this.console.put(time, message);
            while (this.console.size() > 40) {
                this.console.remove(this.console.firstKey());
            }
        }
    }

    public boolean hasCode() {
        return !this.getFullCode().isEmpty();
    }

    public void setEngine(String scriptLanguage) {
        if (this.currentScriptLanguage == null || !this.currentScriptLanguage.equals(scriptLanguage)) {
            this.engine = ScriptController.Instance.getEngineByName(scriptLanguage);
            if (this.engine == null) {
                this.errored = true;
            } else {
                for (Entry<String, Object> entry : Data.entrySet()) {
                    this.engine.put((String) entry.getKey(), entry.getValue());
                }
                this.engine.put("dump", new ScriptContainer.Dump());
                this.engine.put("log", new ScriptContainer.Log());
                this.currentScriptLanguage = scriptLanguage;
                this.init = false;
            }
        }
    }

    public boolean isValid() {
        return this.init && !this.errored;
    }

    static {
        FillMap(AnimationType.class);
        FillMap(EntitiesType.class);
        FillMap(RoleType.class);
        FillMap(JobType.class);
        FillMap(SideType.class);
        FillMap(PotionEffectType.class);
        FillMap(ParticleType.class);
        Data.put("PosZero", new BlockPosWrapper(BlockPos.ZERO));
    }

    private class Dump implements Function<Object, String> {

        public String apply(Object o) {
            if (o == null) {
                return "null";
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append(o + ":" + NoppesStringUtils.newLine());
                for (Field field : o.getClass().getFields()) {
                    try {
                        builder.append(field.getName() + " - " + field.getType().getSimpleName() + ", ");
                    } catch (IllegalArgumentException var12) {
                    }
                }
                for (Method method : o.getClass().getMethods()) {
                    try {
                        String s = method.getName() + "(";
                        for (Class c : method.getParameterTypes()) {
                            s = s + c.getSimpleName() + ", ";
                        }
                        if (s.endsWith(", ")) {
                            s = s.substring(0, s.length() - 2);
                        }
                        builder.append(s + "), ");
                    } catch (IllegalArgumentException var13) {
                    }
                }
                return builder.toString();
            }
        }
    }

    private class Log implements Function<Object, Void> {

        public Void apply(Object o) {
            ScriptContainer.this.appandConsole(o + "");
            LogWriter.info(o + "");
            return null;
        }
    }
}