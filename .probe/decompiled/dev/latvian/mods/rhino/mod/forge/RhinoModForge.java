package dev.latvian.mods.rhino.mod.forge;

import dev.latvian.mods.rhino.mod.util.MojangMappings;
import dev.latvian.mods.rhino.mod.util.RemappingHelper;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.Pattern;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod("rhino")
public class RhinoModForge {

    public RhinoModForge() {
        FMLJavaModLoadingContext.get().getModEventBus().register(RhinoModForge.class);
    }

    @SubscribeEvent
    public static void loaded(FMLCommonSetupEvent event) {
        if (RemappingHelper.GENERATE) {
            RemappingHelper.run(FMLLoader.versionInfo().mcVersion(), RhinoModForge::generateMappings);
        }
    }

    private static void generateMappings(RemappingHelper.MappingContext context) throws Exception {
        MojangMappings.ClassDef current = null;
        ArrayList<String> srg = new ArrayList();
        BufferedReader reader = new BufferedReader(RemappingHelper.createReader("https://raw.githubusercontent.com/MinecraftForge/MCPConfig/master/versions/release/" + context.mcVersion() + "/joined.tsrg"));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                srg.add(line);
            }
        } catch (Throwable var10) {
            try {
                reader.close();
            } catch (Throwable var9) {
                var10.addSuppressed(var9);
            }
            throw var10;
        }
        reader.close();
        Pattern var11 = Pattern.compile("[\t ]");
        for (int var12 = 1; var12 < srg.size(); var12++) {
            String[] s = var11.split((CharSequence) srg.get(var12));
            if (s.length >= 3 && !s[1].isEmpty()) {
                if (!s[0].isEmpty()) {
                    s[0] = s[0].replace('/', '.');
                    current = context.mappings().getClass(s[0]);
                    if (current != null) {
                        RemappingHelper.LOGGER.info("- Checking class " + s[0] + " ; " + current.displayName);
                    } else {
                        RemappingHelper.LOGGER.info("- Skipping class " + s[0]);
                    }
                } else if (current != null) {
                    if (s.length == 5) {
                        if (!s[1].equals("<init>") && !s[1].equals("<clinit>")) {
                            String sigs = s[2].substring(0, s[2].lastIndexOf(41) + 1).replace('/', '.');
                            MojangMappings.NamedSignature sig = new MojangMappings.NamedSignature(s[1], context.mappings().readSignatureFromDescriptor(sigs));
                            MojangMappings.MemberDef m = (MojangMappings.MemberDef) current.members.get(sig);
                            if (m != null && !m.mmName().equals(s[3])) {
                                m.unmappedName().setValue(s[3]);
                                RemappingHelper.LOGGER.info("Remapped method " + s[3] + sigs + " to " + m.mmName());
                            } else if (m == null && !current.ignoredMembers.contains(sig)) {
                                RemappingHelper.LOGGER.info("Method " + s[3] + " [" + sig + "] not found!");
                            }
                        }
                    } else if (s.length == 4) {
                        MojangMappings.NamedSignature sig = new MojangMappings.NamedSignature(s[1], null);
                        MojangMappings.MemberDef m = (MojangMappings.MemberDef) current.members.get(sig);
                        if (m != null) {
                            if (!m.mmName().equals(s[2])) {
                                m.unmappedName().setValue(s[2]);
                                RemappingHelper.LOGGER.info("Remapped field " + s[2] + " [" + m.rawName() + "] to " + m.mmName());
                            }
                        } else if (!current.ignoredMembers.contains(sig)) {
                            RemappingHelper.LOGGER.info("Field " + s[2] + " [" + s[1] + "] not found!");
                        }
                    }
                }
            }
        }
    }
}