package info.journeymap.shaded.org.eclipse.jetty.server.handler;

import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.resource.PathResource;
import info.journeymap.shaded.org.eclipse.jetty.util.resource.Resource;
import java.nio.file.Files;
import java.nio.file.Path;

public class AllowSymLinkAliasChecker implements ContextHandler.AliasCheck {

    private static final Logger LOG = Log.getLogger(AllowSymLinkAliasChecker.class);

    @Override
    public boolean check(String uri, Resource resource) {
        if (!(resource instanceof PathResource)) {
            return false;
        } else {
            PathResource pathResource = (PathResource) resource;
            try {
                Path path = pathResource.getPath();
                Path alias = pathResource.getAliasPath();
                if (path.equals(alias)) {
                    return false;
                }
                if (this.hasSymbolicLink(path) && Files.isSameFile(path, alias)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Allow symlink {} --> {}", resource, pathResource.getAliasPath());
                    }
                    return true;
                }
            } catch (Exception var6) {
                LOG.ignore(var6);
            }
            return false;
        }
    }

    private boolean hasSymbolicLink(Path path) {
        if (Files.isSymbolicLink(path)) {
            return true;
        } else {
            Path base = path.getRoot();
            for (Path segment : path) {
                base = base.resolve(segment);
                if (Files.isSymbolicLink(base)) {
                    return true;
                }
            }
            return false;
        }
    }
}