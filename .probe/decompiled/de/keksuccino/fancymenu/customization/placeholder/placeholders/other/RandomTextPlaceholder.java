package de.keksuccino.fancymenu.customization.placeholder.placeholders.other;

import de.keksuccino.fancymenu.customization.placeholder.DeserializedPlaceholderString;
import de.keksuccino.fancymenu.customization.placeholder.Placeholder;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.konkrete.file.FileUtils;
import de.keksuccino.konkrete.math.MathUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RandomTextPlaceholder extends Placeholder {

    public static Map<String, RandomTextPlaceholder.RandomTextPackage> randomTextIntervals = new HashMap();

    public RandomTextPlaceholder() {
        super("randomtext");
    }

    @Override
    public String getReplacementFor(DeserializedPlaceholderString dps) {
        String pathString = (String) dps.values.get("path");
        String intervalString = (String) dps.values.get("interval");
        if (pathString != null && intervalString != null) {
            File path = new File(pathString);
            if (!path.exists() || !path.getAbsolutePath().replace("\\", "/").startsWith(Minecraft.getInstance().gameDirectory.getAbsolutePath().replace("\\", "/"))) {
                path = new File(Minecraft.getInstance().gameDirectory, pathString);
            }
            if (MathUtils.isLong(intervalString) && path.isFile() && path.getPath().toLowerCase().endsWith(".txt")) {
                long interval = Long.parseLong(intervalString) * 1000L;
                if (interval < 0L) {
                    interval = 0L;
                }
                long currentTime = System.currentTimeMillis();
                RandomTextPlaceholder.RandomTextPackage p;
                if (randomTextIntervals.containsKey(path.getPath())) {
                    p = (RandomTextPlaceholder.RandomTextPackage) randomTextIntervals.get(path.getPath());
                } else {
                    p = new RandomTextPlaceholder.RandomTextPackage();
                    randomTextIntervals.put(path.getPath(), p);
                }
                if ((interval > 0L || p.currentText == null) && p.lastChange + interval <= currentTime) {
                    p.lastChange = currentTime;
                    List<String> txtLines = FileUtils.getFileLines(path);
                    if (!txtLines.isEmpty()) {
                        p.currentText = (String) txtLines.get(MathUtils.getRandomNumberInRange(0, txtLines.size() - 1));
                    } else {
                        p.currentText = null;
                    }
                }
                if (p.currentText != null) {
                    return p.currentText;
                }
                return "";
            }
        }
        return null;
    }

    @Nullable
    @Override
    public List<String> getValueNames() {
        List<String> l = new ArrayList();
        l.add("path");
        l.add("interval");
        return l;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.variables.randomtext");
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(LocalizationUtils.splitLocalizedStringLines("fancymenu.fancymenu.editor.dynamicvariabletextfield.variables.randomtext.desc"));
    }

    @Override
    public String getCategory() {
        return I18n.get("fancymenu.fancymenu.editor.dynamicvariabletextfield.categories.other");
    }

    @NotNull
    @Override
    public DeserializedPlaceholderString getDefaultPlaceholderString() {
        DeserializedPlaceholderString dps = new DeserializedPlaceholderString();
        dps.placeholderIdentifier = this.getIdentifier();
        dps.values.put("path", "randomtexts.txt");
        dps.values.put("interval", "10");
        return dps;
    }

    public static class RandomTextPackage {

        public String currentText = null;

        public long lastChange = 0L;
    }
}