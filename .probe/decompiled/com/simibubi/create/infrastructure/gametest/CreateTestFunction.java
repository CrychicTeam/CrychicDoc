package com.simibubi.create.infrastructure.gametest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import org.jetbrains.annotations.NotNull;

public class CreateTestFunction extends TestFunction {

    public static final Map<String, CreateTestFunction> NAMES_TO_FUNCTIONS = new HashMap();

    public final String fullName;

    public final String simpleName;

    protected CreateTestFunction(String fullName, String simpleName, String pBatchName, String pTestName, String pStructureName, Rotation pRotation, int pMaxTicks, long pSetupTicks, boolean pRequired, int pRequiredSuccesses, int pMaxAttempts, Consumer<GameTestHelper> pFunction) {
        super(pBatchName, pTestName, pStructureName, pRotation, pMaxTicks, pSetupTicks, pRequired, pRequiredSuccesses, pMaxAttempts, pFunction);
        this.fullName = fullName;
        this.simpleName = simpleName;
        NAMES_TO_FUNCTIONS.put(fullName, this);
    }

    @Override
    public String getTestName() {
        return this.simpleName;
    }

    public static Collection<TestFunction> getTestsFrom(Class<?>... classes) {
        return Stream.of(classes).map(Class::getDeclaredMethods).flatMap(Stream::of).map(CreateTestFunction::of).filter(Objects::nonNull).sorted(Comparator.comparing(TestFunction::m_128075_)).toList();
    }

    @Nullable
    public static TestFunction of(Method method) {
        GameTest gt = (GameTest) method.getAnnotation(GameTest.class);
        if (gt == null) {
            return null;
        } else {
            Class<?> owner = method.getDeclaringClass();
            GameTestGroup group = (GameTestGroup) owner.getAnnotation(GameTestGroup.class);
            String simpleName = owner.getSimpleName() + "." + method.getName();
            validateTestMethod(method, gt, owner, group, simpleName);
            String structure = "%s:gametest/%s/%s".formatted(group.namespace(), group.path(), gt.template());
            Rotation rotation = StructureUtils.getRotationForRotationSteps(gt.rotationSteps());
            String fullName = owner.getName() + "." + method.getName();
            return new CreateTestFunction(fullName, simpleName, gt.batch(), structure, structure, rotation, gt.timeoutTicks(), gt.setupTicks(), gt.required(), gt.requiredSuccesses(), gt.attempts(), asConsumer(method));
        }
    }

    private static void validateTestMethod(Method method, GameTest gt, Class<?> owner, GameTestGroup group, String simpleName) {
        if (gt.template().isEmpty()) {
            throw new IllegalArgumentException(simpleName + " must provide a template structure");
        } else if (!Modifier.isStatic(method.getModifiers())) {
            throw new IllegalArgumentException(simpleName + " must be static");
        } else if (method.getReturnType() != void.class) {
            throw new IllegalArgumentException(simpleName + " must return void");
        } else if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != CreateGameTestHelper.class) {
            throw new IllegalArgumentException(simpleName + " must take 1 parameter of type CreateGameTestHelper");
        } else if (group == null) {
            throw new IllegalArgumentException(owner.getName() + " must be annotated with @GameTestGroup");
        }
    }

    private static Consumer<GameTestHelper> asConsumer(Method method) {
        return helper -> {
            try {
                method.invoke(null, helper);
            } catch (InvocationTargetException | IllegalAccessException var3) {
                throw new RuntimeException(var3);
            }
        };
    }

    @Override
    public void run(@NotNull GameTestHelper helper) {
        StructureBlockEntity be = (StructureBlockEntity) helper.getBlockEntity(BlockPos.ZERO);
        be.getPersistentData().putString("CreateTestFunction", this.fullName);
        super.run(CreateGameTestHelper.of(helper));
    }
}