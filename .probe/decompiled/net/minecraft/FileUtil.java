package net.minecraft;

import com.mojang.serialization.DataResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;

public class FileUtil {

    private static final Pattern COPY_COUNTER_PATTERN = Pattern.compile("(<name>.*) \\((<count>\\d*)\\)", 66);

    private static final int MAX_FILE_NAME = 255;

    private static final Pattern RESERVED_WINDOWS_FILENAMES = Pattern.compile(".*\\.|(?:COM|CLOCK\\$|CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\..*)?", 2);

    private static final Pattern STRICT_PATH_SEGMENT_CHECK = Pattern.compile("[-._a-z0-9]+");

    public static String findAvailableName(Path path0, String string1, String string2) throws IOException {
        for (char $$3 : SharedConstants.ILLEGAL_FILE_CHARACTERS) {
            string1 = string1.replace($$3, '_');
        }
        string1 = string1.replaceAll("[./\"]", "_");
        if (RESERVED_WINDOWS_FILENAMES.matcher(string1).matches()) {
            string1 = "_" + string1 + "_";
        }
        Matcher $$4 = COPY_COUNTER_PATTERN.matcher(string1);
        int $$5 = 0;
        if ($$4.matches()) {
            string1 = $$4.group("name");
            $$5 = Integer.parseInt($$4.group("count"));
        }
        if (string1.length() > 255 - string2.length()) {
            string1 = string1.substring(0, 255 - string2.length());
        }
        while (true) {
            String $$6 = string1;
            if ($$5 != 0) {
                String $$7 = " (" + $$5 + ")";
                int $$8 = 255 - $$7.length();
                if (string1.length() > $$8) {
                    $$6 = string1.substring(0, $$8);
                }
                $$6 = $$6 + $$7;
            }
            $$6 = $$6 + string2;
            Path $$9 = path0.resolve($$6);
            try {
                Path $$10 = Files.createDirectory($$9);
                Files.deleteIfExists($$10);
                return path0.relativize($$10).toString();
            } catch (FileAlreadyExistsException var8) {
                $$5++;
            }
        }
    }

    public static boolean isPathNormalized(Path path0) {
        Path $$1 = path0.normalize();
        return $$1.equals(path0);
    }

    public static boolean isPathPortable(Path path0) {
        for (Path $$1 : path0) {
            if (RESERVED_WINDOWS_FILENAMES.matcher($$1.toString()).matches()) {
                return false;
            }
        }
        return true;
    }

    public static Path createPathToResource(Path path0, String string1, String string2) {
        String $$3 = string1 + string2;
        Path $$4 = Paths.get($$3);
        if ($$4.endsWith(string2)) {
            throw new InvalidPathException($$3, "empty resource name");
        } else {
            return path0.resolve($$4);
        }
    }

    public static String getFullResourcePath(String string0) {
        return FilenameUtils.getFullPath(string0).replace(File.separator, "/");
    }

    public static String normalizeResourcePath(String string0) {
        return FilenameUtils.normalize(string0).replace(File.separator, "/");
    }

    public static DataResult<List<String>> decomposePath(String string0) {
        int $$1 = string0.indexOf(47);
        if ($$1 == -1) {
            return switch(string0) {
                case "", ".", ".." ->
                    DataResult.error(() -> "Invalid path '" + string0 + "'");
                default ->
                    !isValidStrictPathSegment(string0) ? DataResult.error(() -> "Invalid path '" + string0 + "'") : DataResult.success(List.of(string0));
            };
        } else {
            List<String> $$2 = new ArrayList();
            int $$3 = 0;
            boolean $$4 = false;
            while (true) {
                String $$5 = string0.substring($$3, $$1);
                switch($$5) {
                    case "":
                    case ".":
                    case "..":
                        return DataResult.error(() -> "Invalid segment '" + $$5 + "' in path '" + string0 + "'");
                }
                if (!isValidStrictPathSegment($$5)) {
                    return DataResult.error(() -> "Invalid segment '" + $$5 + "' in path '" + string0 + "'");
                }
                $$2.add($$5);
                if ($$4) {
                    return DataResult.success($$2);
                }
                $$3 = $$1 + 1;
                $$1 = string0.indexOf(47, $$3);
                if ($$1 == -1) {
                    $$1 = string0.length();
                    $$4 = true;
                }
            }
        }
    }

    public static Path resolvePath(Path path0, List<String> listString1) {
        int $$2 = listString1.size();
        return switch($$2) {
            case 0 ->
                path0;
            case 1 ->
                path0.resolve((String) listString1.get(0));
            default ->
                {
                    String[] $$3 = new String[$$2 - 1];
                    for (int $$4 = 1; $$4 < $$2; $$4++) {
                        $$3[$$4 - 1] = (String) listString1.get($$4);
                    }
                    ???;
                }
        };
    }

    public static boolean isValidStrictPathSegment(String string0) {
        return STRICT_PATH_SEGMENT_CHECK.matcher(string0).matches();
    }

    public static void validatePath(String... string0) {
        if (string0.length == 0) {
            throw new IllegalArgumentException("Path must have at least one element");
        } else {
            for (String $$1 : string0) {
                if ($$1.equals("..") || $$1.equals(".") || !isValidStrictPathSegment($$1)) {
                    throw new IllegalArgumentException("Illegal segment " + $$1 + " in path " + Arrays.toString(string0));
                }
            }
        }
    }

    public static void createDirectoriesSafe(Path path0) throws IOException {
        Files.createDirectories(Files.exists(path0, new LinkOption[0]) ? path0.toRealPath() : path0);
    }
}