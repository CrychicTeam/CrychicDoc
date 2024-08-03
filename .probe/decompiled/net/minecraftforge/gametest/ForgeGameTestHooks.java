package net.minecraftforge.gametest;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.SharedConstants;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestRegistry;
import net.minecraftforge.event.RegisterGameTestsEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;

public class ForgeGameTestHooks {

    private static boolean registeredGametests = false;

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Type GAME_TEST_HOLDER = Type.getType(GameTestHolder.class);

    public static boolean isGametestEnabled() {
        return !FMLLoader.isProduction() && (SharedConstants.IS_RUNNING_IN_IDE || isGametestServer() || Boolean.getBoolean("forge.enableGameTest"));
    }

    public static boolean isGametestServer() {
        return !FMLLoader.isProduction() && Boolean.getBoolean("forge.gameTestServer");
    }

    public static void registerGametests() {
        if (!registeredGametests && isGametestEnabled() && ModLoader.isLoadingStateValid()) {
            Set<String> enabledNamespaces = getEnabledNamespaces();
            LOGGER.info("Enabled Gametest Namespaces: {}", enabledNamespaces);
            Set<Method> gameTestMethods = new HashSet();
            RegisterGameTestsEvent event = new RegisterGameTestsEvent(gameTestMethods);
            ModLoader.get().postEvent(event);
            ModList.get().getAllScanData().stream().map(ModFileScanData::getAnnotations).flatMap(Collection::stream).filter(a -> GAME_TEST_HOLDER.equals(a.annotationType())).forEach(a -> addGameTestMethods(a, gameTestMethods));
            for (Method gameTestMethod : gameTestMethods) {
                GameTestRegistry.register(gameTestMethod, enabledNamespaces);
            }
            registeredGametests = true;
        }
    }

    private static Set<String> getEnabledNamespaces() {
        String enabledNamespacesStr = System.getProperty("forge.enabledGameTestNamespaces");
        return enabledNamespacesStr == null ? Set.of() : (Set) Arrays.stream(enabledNamespacesStr.split(",")).filter(s -> !s.isBlank()).collect(Collectors.toUnmodifiableSet());
    }

    private static void addGameTestMethods(AnnotationData annotationData, Set<Method> gameTestMethods) {
        try {
            Class<?> clazz = Class.forName(annotationData.clazz().getClassName(), true, ForgeGameTestHooks.class.getClassLoader());
            gameTestMethods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        } catch (ClassNotFoundException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static String getTemplateNamespace(Method method) {
        GameTest gameTest = (GameTest) method.getAnnotation(GameTest.class);
        if (gameTest != null && !gameTest.templateNamespace().isEmpty()) {
            return gameTest.templateNamespace();
        } else {
            GameTestHolder gameTestHolder = (GameTestHolder) method.getDeclaringClass().getAnnotation(GameTestHolder.class);
            return gameTestHolder != null ? gameTestHolder.value() : "minecraft";
        }
    }

    public static boolean prefixGameTestTemplate(Method method) {
        PrefixGameTestTemplate annotation = (PrefixGameTestTemplate) method.getAnnotation(PrefixGameTestTemplate.class);
        if (annotation == null) {
            annotation = (PrefixGameTestTemplate) method.getDeclaringClass().getAnnotation(PrefixGameTestTemplate.class);
        }
        return annotation == null || annotation.value();
    }
}