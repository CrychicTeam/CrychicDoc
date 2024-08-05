package net.blay09.mods.defaultoptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Predicate;
import net.blay09.mods.defaultoptions.api.DefaultOptionsCategory;
import net.blay09.mods.defaultoptions.api.DefaultOptionsLoadStage;
import net.blay09.mods.defaultoptions.api.SimpleDefaultOptionsHandler;
import org.apache.commons.io.FileUtils;

public class SimpleDefaultOptionsFileHandler implements SimpleDefaultOptionsHandler {

    private final File file;

    private DefaultOptionsCategory category = DefaultOptionsCategory.OPTIONS;

    private DefaultOptionsLoadStage loadStage = DefaultOptionsLoadStage.PRE_LOAD;

    private Runnable saveHandler;

    private Predicate<String> linePredicate;

    public SimpleDefaultOptionsFileHandler(File file) {
        this.file = file;
    }

    @Override
    public String getId() {
        return this.file.getName();
    }

    public File getFile() {
        return this.file;
    }

    public File getDefaultsFile() {
        return new File(DefaultOptions.getDefaultOptionsFolder(), this.file.getName());
    }

    @Override
    public DefaultOptionsCategory getCategory() {
        return this.category;
    }

    @Override
    public DefaultOptionsLoadStage getLoadStage() {
        return this.loadStage;
    }

    @Override
    public boolean hasDefaults() {
        return this.getDefaultsFile().exists();
    }

    @Override
    public void saveCurrentOptions() {
        if (this.saveHandler != null) {
            this.saveHandler.run();
        }
    }

    @Override
    public void saveCurrentOptionsAsDefault() throws DefaultOptionsHandlerException {
        this.saveCurrentOptions();
        if (this.file.exists()) {
            try {
                if (this.linePredicate != null) {
                    copyFileLineByLine(this.file, this.getDefaultsFile(), this.linePredicate);
                } else {
                    FileUtils.copyFile(this.file, this.getDefaultsFile());
                }
            } catch (IOException var2) {
                throw new DefaultOptionsHandlerException(this, var2);
            }
        }
    }

    @Override
    public boolean shouldLoadDefaults() {
        return !this.file.exists() && this.hasDefaults();
    }

    @Override
    public void loadDefaults() throws DefaultOptionsHandlerException {
        try {
            if (this.linePredicate != null) {
                copyFileLineByLine(this.getDefaultsFile(), this.file, this.linePredicate);
            } else {
                FileUtils.copyFile(this.getDefaultsFile(), this.file);
            }
        } catch (IOException var2) {
            throw new DefaultOptionsHandlerException(this, var2);
        }
    }

    @Override
    public SimpleDefaultOptionsHandler withSaveHandler(Runnable saveHandler) {
        this.saveHandler = saveHandler;
        return this;
    }

    @Override
    public SimpleDefaultOptionsHandler withLinePredicate(Predicate<String> linePredicate) {
        this.linePredicate = linePredicate;
        return this;
    }

    @Override
    public SimpleDefaultOptionsHandler withCategory(DefaultOptionsCategory category) {
        this.category = category;
        return this;
    }

    @Override
    public SimpleDefaultOptionsHandler withLoadStage(DefaultOptionsLoadStage loadStage) {
        this.loadStage = loadStage;
        return this;
    }

    private static void copyFileLineByLine(File source, File target, Predicate<String> linePredicate) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(target));
        try {
            BufferedReader reader = new BufferedReader(new FileReader(source));
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    if (linePredicate.test(line)) {
                        writer.println(line);
                    }
                }
            } catch (Throwable var9) {
                try {
                    reader.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }
                throw var9;
            }
            reader.close();
        } catch (Throwable var10) {
            try {
                writer.close();
            } catch (Throwable var7) {
                var10.addSuppressed(var7);
            }
            throw var10;
        }
        writer.close();
    }
}