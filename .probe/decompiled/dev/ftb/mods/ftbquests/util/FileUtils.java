package dev.ftb.mods.ftbquests.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtils {

    public static List<String> read(InputStream in) throws IOException {
        List<String> list = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
            list.add(line);
        }
        return list;
    }

    public static List<String> readFile(File file) {
        try {
            InputStream in = new FileInputStream(file);
            List var2;
            try {
                var2 = read(in);
            } catch (Throwable var5) {
                try {
                    in.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
                throw var5;
            }
            in.close();
            return var2;
        } catch (IOException var6) {
            return Collections.emptyList();
        }
    }

    public static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    delete(f);
                }
            }
        } else {
            file.delete();
        }
    }
}