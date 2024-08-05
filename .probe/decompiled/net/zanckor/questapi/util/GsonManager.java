package net.zanckor.questapi.util;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import net.zanckor.questapi.api.file.FileAbstract;

public class GsonManager {

    public static Gson gson = new Gson();

    public static <T extends FileAbstract> FileAbstract getJsonClass(File file, Class<T> fileClass) throws IOException {
        if (!file.exists()) {
            return null;
        } else {
            FileReader reader = new FileReader(file);
            FileAbstract dialog = (FileAbstract) gson.fromJson(reader, fileClass);
            reader.close();
            return dialog;
        }
    }

    public static <T extends FileAbstract> FileAbstract getJsonClass(String file, Class<T> fileClass) throws IOException {
        return (FileAbstract) gson.fromJson(file, fileClass);
    }

    public static <T extends FileAbstract> void writeJson(File file, T fileClass) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(gson.toJson(fileClass));
        writer.flush();
        writer.close();
    }
}