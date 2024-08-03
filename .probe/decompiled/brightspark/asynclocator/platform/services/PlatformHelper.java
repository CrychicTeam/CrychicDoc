package brightspark.asynclocator.platform.services;

public interface PlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String var1);

    boolean isDevelopmentEnvironment();
}