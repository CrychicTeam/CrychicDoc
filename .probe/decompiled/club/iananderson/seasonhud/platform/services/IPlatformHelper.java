package club.iananderson.seasonhud.platform.services;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String var1);

    boolean isDevelopmentEnvironment();
}