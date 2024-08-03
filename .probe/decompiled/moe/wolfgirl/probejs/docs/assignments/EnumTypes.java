package moe.wolfgirl.probejs.docs.assignments;

import dev.latvian.mods.rhino.util.EnumTypeWrapper;
import java.util.concurrent.locks.ReentrantLock;
import moe.wolfgirl.probejs.lang.java.clazz.Clazz;
import moe.wolfgirl.probejs.lang.typescript.ScriptDump;
import moe.wolfgirl.probejs.lang.typescript.code.type.BaseType;
import moe.wolfgirl.probejs.lang.typescript.code.type.Types;
import moe.wolfgirl.probejs.plugin.ProbeJSPlugin;

public class EnumTypes extends ProbeJSPlugin {

    private static final ReentrantLock LOCK = new ReentrantLock();

    @Override
    public void assignType(ScriptDump scriptDump) {
        LOCK.lock();
        try {
            for (Clazz recordedClass : scriptDump.recordedClasses) {
                if (recordedClass.original.isEnum()) {
                    EnumTypeWrapper<?> typeWrapper = EnumTypeWrapper.get(recordedClass.original);
                    BaseType[] types = (BaseType[]) typeWrapper.nameValues.keySet().stream().map(Types::literal).toArray(BaseType[]::new);
                    scriptDump.assignType(recordedClass.classPath, Types.or(types));
                }
            }
        } finally {
            LOCK.unlock();
        }
    }
}