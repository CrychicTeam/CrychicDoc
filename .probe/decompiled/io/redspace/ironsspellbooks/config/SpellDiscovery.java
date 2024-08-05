package io.redspace.ironsspellbooks.config;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

public final class SpellDiscovery {

    public static List<AbstractSpell> getSpellsForConfig() {
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        Set<String> spellClassNames = new HashSet();
        allScanData.forEach(scanData -> scanData.getAnnotations().forEach(annotationData -> {
            if (Objects.equals(annotationData.annotationType(), Type.getType(AutoSpellConfig.class))) {
                spellClassNames.add(annotationData.memberName());
            }
        }));
        List<AbstractSpell> spells = new ArrayList();
        spellClassNames.forEach(spellName -> {
            try {
                Class<?> pluginClass = Class.forName(spellName);
                Class<? extends AbstractSpell> pluginClassSubclass = pluginClass.asSubclass(AbstractSpell.class);
                Constructor<? extends AbstractSpell> constructor = pluginClassSubclass.getDeclaredConstructor();
                AbstractSpell instance = (AbstractSpell) constructor.newInstance();
                spells.add(instance);
            } catch (Exception var6) {
                IronsSpellbooks.LOGGER.error("SpellDiscovery:  {}, {}", spellName, var6);
            }
        });
        return spells;
    }
}