package de.keksuccino.konkrete.file;

import com.google.common.io.Files;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;

public class FileUtils {

    public static void writeTextToFile(File f, boolean append, String... text) throws IOException {
        FileOutputStream fo = new FileOutputStream(f, append);
        OutputStreamWriter os = new OutputStreamWriter(fo, StandardCharsets.UTF_8);
        BufferedWriter writer = new BufferedWriter(os);
        if (text.length == 1) {
            writer.write(text[0]);
        } else if (text.length > 0) {
            for (String s : text) {
                writer.write(s + "\n");
            }
        }
        writer.flush();
        try {
            if (writer != null) {
                writer.close();
            }
            if (fo != null) {
                fo.close();
            }
            if (os != null) {
                os.close();
            }
        } catch (Exception var10) {
        }
    }

    public static List<String> getFileLines(File f) {
        List<String> list = new ArrayList();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8));
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                list.add(line);
            }
            in.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return list;
    }

    public static List<String> getFiles(String path) {
        List<String> list = new ArrayList();
        File f = new File(path);
        if (f.exists()) {
            for (File file : f.listFiles()) {
                list.add(file.getAbsolutePath());
            }
        }
        return list;
    }

    public static List<String> getFilenames(String path, boolean includeExtension) {
        List<String> list = new ArrayList();
        File f = new File(path);
        if (f.exists()) {
            for (File file : f.listFiles()) {
                if (includeExtension) {
                    list.add(file.getName());
                } else {
                    list.add(Files.getNameWithoutExtension(file.getName()));
                }
            }
        }
        return list;
    }

    public static String generateAvailableFilename(String dir, String baseName, String extension) {
        File f = new File(dir);
        if (!f.exists() && f.isDirectory()) {
            f.mkdirs();
        }
        File f2 = new File(f.getPath() + "/" + baseName + "." + extension.replace(".", ""));
        for (int i = 1; f2.exists(); i++) {
            f2 = new File(f.getPath() + "/" + baseName + "_" + i + "." + extension.replace(".", ""));
        }
        return f2.getName();
    }

    public static boolean copyFile(File from, File to) {
        if (!from.getAbsolutePath().replace("\\", "/").equals(to.getAbsolutePath().replace("\\", "/")) && from.exists() && from.isFile()) {
            File toParent = to.getParentFile();
            if (toParent != null && !toParent.exists()) {
                toParent.mkdirs();
            }
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(from));
                out = new BufferedOutputStream(new FileOutputStream(to));
                byte[] buffer = new byte[1024];
                int lengthRead;
                while ((lengthRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, lengthRead);
                    out.flush();
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
            try {
                for (int i = 0; !to.exists() && i < 200; i++) {
                    Thread.sleep(50L);
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
            return to.exists();
        } else {
            return false;
        }
    }

    public static boolean moveFile(File from, File to) throws IOException, InterruptedException {
        if (!from.getAbsolutePath().replace("\\", "/").equals(to.getAbsolutePath().replace("\\", "/")) && from.exists() && from.isFile()) {
            if (from.renameTo(to)) {
                for (int i = 0; !to.exists() && i < 200; i++) {
                    Thread.sleep(50L);
                }
                return true;
            }
            if (copyFile(from, to)) {
                if (from.delete()) {
                    return true;
                }
                if (from.exists() && to.exists()) {
                    to.delete();
                    return false;
                }
            }
        }
        return false;
    }

    public static void compressToZip(String pathToCompare, String zipFile) {
        byte[] buffer = new byte[1024];
        String source = new File(pathToCompare).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);
            for (String file : getFiles(pathToCompare)) {
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    FileInputStream in = new FileInputStream(file);
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    in.close();
                } catch (Exception var20) {
                    var20.printStackTrace();
                }
            }
            zos.closeEntry();
            fos.close();
        } catch (Exception var21) {
            var21.printStackTrace();
        } finally {
            try {
                zos.close();
                fos.close();
            } catch (Exception var19) {
                var19.printStackTrace();
            }
        }
    }

    public static void compressToZip(List<String> filePathsToCompare, String zipFile) {
        byte[] buffer = new byte[1024];
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);
            for (String file : filePathsToCompare) {
                ZipEntry ze = new ZipEntry(Files.getNameWithoutExtension(zipFile) + "/" + file);
                zos.putNextEntry(ze);
                try {
                    FileInputStream in = new FileInputStream(file);
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    in.close();
                } catch (Exception var19) {
                    var19.printStackTrace();
                }
            }
            zos.closeEntry();
        } catch (IOException var20) {
            var20.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException var18) {
                var18.printStackTrace();
            }
        }
    }

    public static void unpackZip(String zipPath, String outputDir) throws IOException {
        ZipFile zipFile = new ZipFile(zipPath);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            File entryDestination = new File(outputDir, entry.getName());
            if (entry.isDirectory()) {
                entryDestination.mkdirs();
            } else {
                entryDestination.getParentFile().mkdirs();
                InputStream in = zipFile.getInputStream(entry);
                OutputStream out = new FileOutputStream(entryDestination);
                IOUtils.copy(in, out);
            }
        }
        try {
            zipFile.close();
        } catch (Exception var8) {
        }
    }
}