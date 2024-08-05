package de.keksuccino.konkrete.properties;

import de.keksuccino.konkrete.file.FileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class PropertiesSerializer {

    public static PropertiesSet getProperties(String filePath) {
        File f = new File(filePath);
        if (f.exists() && f.isFile()) {
            List<String> lines = FileUtils.getFileLines(f);
            List<PropertiesSection> data = new ArrayList();
            String propertiesType = null;
            PropertiesSection currentData = null;
            boolean insideData = false;
            for (String s : lines) {
                String comp = s.replace(" ", "");
                if (comp.startsWith("type=") && !insideData) {
                    propertiesType = comp.split("[=]", 2)[1];
                } else if (comp.endsWith("{")) {
                    if (!insideData) {
                        insideData = true;
                    } else {
                        System.out.println("######################### WARNING #########################");
                        System.out.println("Invalid properties found in '" + filePath + "'! (Leaking properties section; Missing '}')");
                        System.out.println("###########################################################");
                        if (currentData != null) {
                            data.add(currentData);
                        }
                    }
                    currentData = new PropertiesSection(comp.split("[{]")[0]);
                } else if (comp.startsWith("}") && insideData) {
                    data.add(currentData);
                    insideData = false;
                } else if (insideData && comp.contains("=")) {
                    String value = s.split("[=]", 2)[1];
                    if (value.startsWith(" ")) {
                        value = value.substring(1);
                    }
                    currentData.addEntry(comp.split("[=]", 2)[0], value);
                }
            }
            if (propertiesType != null) {
                PropertiesSet set = new PropertiesSet(propertiesType);
                for (PropertiesSection d : data) {
                    set.addProperties(d);
                }
                return set;
            }
            System.out.println("######################### WARNING #########################");
            System.out.println("Invalid properties file found: " + filePath + " (Missing properties type)");
            System.out.println("###########################################################");
        }
        return null;
    }

    public static void writeProperties(PropertiesSet props, String path) {
        try {
            List<PropertiesSection> l = props.getProperties();
            File f = new File(path);
            if (f.getName().contains(".") && !f.getName().startsWith(".")) {
                File parent = f.getParentFile();
                if (parent != null && parent.isDirectory() && !parent.exists()) {
                    parent.mkdirs();
                }
                f.createNewFile();
                String data = "";
                data = data + "type = " + props.getPropertiesType() + "\n\n";
                for (PropertiesSection ps : l) {
                    data = data + ps.getSectionType() + " {\n";
                    for (Entry<String, String> e : ps.getEntries().entrySet()) {
                        data = data + "  " + (String) e.getKey() + " = " + (String) e.getValue() + "\n";
                    }
                    data = data + "}\n\n";
                }
                FileUtils.writeTextToFile(f, false, data);
            } else {
                System.out.println("############### CANNOT WRITE PROPERTIES! PATH IS NOT A FILE!");
            }
        } catch (Exception var10) {
            var10.printStackTrace();
        }
    }
}