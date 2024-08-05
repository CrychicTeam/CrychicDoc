package info.journeymap.shaded.kotlin.spark.staticfiles;

import info.journeymap.shaded.kotlin.spark.utils.StringUtils;
import java.nio.file.Paths;

public class DirectoryTraversal {

    public static void protectAgainstInClassPath(String path) {
        if (!StringUtils.removeLeadingAndTrailingSlashesFrom(path).startsWith(StaticFilesFolder.local())) {
            throw new DirectoryTraversal.DirectoryTraversalDetection("classpath");
        }
    }

    public static void protectAgainstForExternal(String path) {
        String nixLikePath = Paths.get(path).toAbsolutePath().toString().replace("\\", "/");
        if (!StringUtils.removeLeadingAndTrailingSlashesFrom(nixLikePath).startsWith(StaticFilesFolder.external())) {
            throw new DirectoryTraversal.DirectoryTraversalDetection("external");
        }
    }

    public static final class DirectoryTraversalDetection extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public DirectoryTraversalDetection(String msg) {
            super(msg);
        }
    }
}