package info.journeymap.shaded.kotlin.spark.resource;

import info.journeymap.shaded.kotlin.spark.staticfiles.DirectoryTraversal;
import info.journeymap.shaded.kotlin.spark.utils.Assert;
import info.journeymap.shaded.org.slf4j.Logger;
import info.journeymap.shaded.org.slf4j.LoggerFactory;
import java.net.MalformedURLException;

public class ClassPathResourceHandler extends AbstractResourceHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ClassPathResourceHandler.class);

    private final String baseResource;

    private String welcomeFile;

    public ClassPathResourceHandler(String baseResource) {
        this(baseResource, null);
    }

    public ClassPathResourceHandler(String baseResource, String welcomeFile) {
        Assert.notNull(baseResource);
        this.baseResource = baseResource;
        this.welcomeFile = welcomeFile;
    }

    @Override
    protected AbstractFileResolvingResource getResource(String path) throws MalformedURLException {
        if (path != null && path.startsWith("/")) {
            try {
                path = UriPath.canonical(path);
                String addedPath = addPaths(this.baseResource, path);
                ClassPathResource resource = new ClassPathResource(addedPath);
                if (resource.exists() && path.endsWith("/")) {
                    if (this.welcomeFile != null) {
                        resource = new ClassPathResource(addPaths(resource.getPath(), this.welcomeFile));
                    } else {
                        resource = null;
                    }
                }
                if (resource != null && resource.exists()) {
                    DirectoryTraversal.protectAgainstInClassPath(resource.getPath());
                    return resource;
                } else {
                    return null;
                }
            } catch (DirectoryTraversal.DirectoryTraversalDetection var4) {
                throw var4;
            } catch (Exception var5) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(var5.getClass().getSimpleName() + " when trying to get resource. " + var5.getMessage());
                }
                return null;
            }
        } else {
            throw new MalformedURLException(path);
        }
    }
}