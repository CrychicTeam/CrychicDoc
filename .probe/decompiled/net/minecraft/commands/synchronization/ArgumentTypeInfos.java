package net.minecraft.commands.synchronization;

import com.google.common.collect.Maps;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import java.util.Locale;
import java.util.Map;
import net.minecraft.SharedConstants;
import net.minecraft.commands.arguments.AngleArgument;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameModeArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.arguments.HeightmapTypeArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.commands.arguments.NbtTagArgument;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
import net.minecraft.commands.arguments.OperationArgument;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.ResourceOrTagArgument;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.commands.arguments.ScoreboardSlotArgument;
import net.minecraft.commands.arguments.SlotArgument;
import net.minecraft.commands.arguments.TeamArgument;
import net.minecraft.commands.arguments.TemplateMirrorArgument;
import net.minecraft.commands.arguments.TemplateRotationArgument;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import net.minecraft.commands.arguments.coordinates.RotationArgument;
import net.minecraft.commands.arguments.coordinates.SwizzleArgument;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.commands.synchronization.brigadier.DoubleArgumentInfo;
import net.minecraft.commands.synchronization.brigadier.FloatArgumentInfo;
import net.minecraft.commands.synchronization.brigadier.IntegerArgumentInfo;
import net.minecraft.commands.synchronization.brigadier.LongArgumentInfo;
import net.minecraft.commands.synchronization.brigadier.StringArgumentSerializer;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.TestClassNameArgument;
import net.minecraft.gametest.framework.TestFunctionArgument;

public class ArgumentTypeInfos {

    private static final Map<Class<?>, ArgumentTypeInfo<?, ?>> BY_CLASS = Maps.newHashMap();

    private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> ArgumentTypeInfo<A, T> register(Registry<ArgumentTypeInfo<?, ?>> registryArgumentTypeInfo0, String string1, Class<? extends A> classExtendsA2, ArgumentTypeInfo<A, T> argumentTypeInfoAT3) {
        BY_CLASS.put(classExtendsA2, argumentTypeInfoAT3);
        return Registry.register(registryArgumentTypeInfo0, string1, argumentTypeInfoAT3);
    }

    public static ArgumentTypeInfo<?, ?> bootstrap(Registry<ArgumentTypeInfo<?, ?>> registryArgumentTypeInfo0) {
        register(registryArgumentTypeInfo0, "brigadier:bool", BoolArgumentType.class, SingletonArgumentInfo.contextFree(BoolArgumentType::bool));
        register(registryArgumentTypeInfo0, "brigadier:float", FloatArgumentType.class, new FloatArgumentInfo());
        register(registryArgumentTypeInfo0, "brigadier:double", DoubleArgumentType.class, new DoubleArgumentInfo());
        register(registryArgumentTypeInfo0, "brigadier:integer", IntegerArgumentType.class, new IntegerArgumentInfo());
        register(registryArgumentTypeInfo0, "brigadier:long", LongArgumentType.class, new LongArgumentInfo());
        register(registryArgumentTypeInfo0, "brigadier:string", StringArgumentType.class, new StringArgumentSerializer());
        register(registryArgumentTypeInfo0, "entity", EntityArgument.class, new EntityArgument.Info());
        register(registryArgumentTypeInfo0, "game_profile", GameProfileArgument.class, SingletonArgumentInfo.contextFree(GameProfileArgument::m_94584_));
        register(registryArgumentTypeInfo0, "block_pos", BlockPosArgument.class, SingletonArgumentInfo.contextFree(BlockPosArgument::m_118239_));
        register(registryArgumentTypeInfo0, "column_pos", ColumnPosArgument.class, SingletonArgumentInfo.contextFree(ColumnPosArgument::m_118989_));
        register(registryArgumentTypeInfo0, "vec3", Vec3Argument.class, SingletonArgumentInfo.contextFree(Vec3Argument::m_120841_));
        register(registryArgumentTypeInfo0, "vec2", Vec2Argument.class, SingletonArgumentInfo.contextFree(Vec2Argument::m_120822_));
        register(registryArgumentTypeInfo0, "block_state", BlockStateArgument.class, SingletonArgumentInfo.contextAware(BlockStateArgument::m_234650_));
        register(registryArgumentTypeInfo0, "block_predicate", BlockPredicateArgument.class, SingletonArgumentInfo.contextAware(BlockPredicateArgument::m_234627_));
        register(registryArgumentTypeInfo0, "item_stack", ItemArgument.class, SingletonArgumentInfo.contextAware(ItemArgument::m_235279_));
        register(registryArgumentTypeInfo0, "item_predicate", ItemPredicateArgument.class, SingletonArgumentInfo.contextAware(ItemPredicateArgument::m_235353_));
        register(registryArgumentTypeInfo0, "color", ColorArgument.class, SingletonArgumentInfo.contextFree(ColorArgument::m_85463_));
        register(registryArgumentTypeInfo0, "component", ComponentArgument.class, SingletonArgumentInfo.contextFree(ComponentArgument::m_87114_));
        register(registryArgumentTypeInfo0, "message", MessageArgument.class, SingletonArgumentInfo.contextFree(MessageArgument::m_96832_));
        register(registryArgumentTypeInfo0, "nbt_compound_tag", CompoundTagArgument.class, SingletonArgumentInfo.contextFree(CompoundTagArgument::m_87657_));
        register(registryArgumentTypeInfo0, "nbt_tag", NbtTagArgument.class, SingletonArgumentInfo.contextFree(NbtTagArgument::m_100659_));
        register(registryArgumentTypeInfo0, "nbt_path", NbtPathArgument.class, SingletonArgumentInfo.contextFree(NbtPathArgument::m_99487_));
        register(registryArgumentTypeInfo0, "objective", ObjectiveArgument.class, SingletonArgumentInfo.contextFree(ObjectiveArgument::m_101957_));
        register(registryArgumentTypeInfo0, "objective_criteria", ObjectiveCriteriaArgument.class, SingletonArgumentInfo.contextFree(ObjectiveCriteriaArgument::m_102555_));
        register(registryArgumentTypeInfo0, "operation", OperationArgument.class, SingletonArgumentInfo.contextFree(OperationArgument::m_103269_));
        register(registryArgumentTypeInfo0, "particle", ParticleArgument.class, SingletonArgumentInfo.contextAware(ParticleArgument::m_245999_));
        register(registryArgumentTypeInfo0, "angle", AngleArgument.class, SingletonArgumentInfo.contextFree(AngleArgument::m_83807_));
        register(registryArgumentTypeInfo0, "rotation", RotationArgument.class, SingletonArgumentInfo.contextFree(RotationArgument::m_120479_));
        register(registryArgumentTypeInfo0, "scoreboard_slot", ScoreboardSlotArgument.class, SingletonArgumentInfo.contextFree(ScoreboardSlotArgument::m_109196_));
        register(registryArgumentTypeInfo0, "score_holder", ScoreHolderArgument.class, new ScoreHolderArgument.Info());
        register(registryArgumentTypeInfo0, "swizzle", SwizzleArgument.class, SingletonArgumentInfo.contextFree(SwizzleArgument::m_120807_));
        register(registryArgumentTypeInfo0, "team", TeamArgument.class, SingletonArgumentInfo.contextFree(TeamArgument::m_112088_));
        register(registryArgumentTypeInfo0, "item_slot", SlotArgument.class, SingletonArgumentInfo.contextFree(SlotArgument::m_111276_));
        register(registryArgumentTypeInfo0, "resource_location", ResourceLocationArgument.class, SingletonArgumentInfo.contextFree(ResourceLocationArgument::m_106984_));
        register(registryArgumentTypeInfo0, "function", FunctionArgument.class, SingletonArgumentInfo.contextFree(FunctionArgument::m_120907_));
        register(registryArgumentTypeInfo0, "entity_anchor", EntityAnchorArgument.class, SingletonArgumentInfo.contextFree(EntityAnchorArgument::m_90350_));
        register(registryArgumentTypeInfo0, "int_range", RangeArgument.Ints.class, SingletonArgumentInfo.contextFree(RangeArgument::m_105404_));
        register(registryArgumentTypeInfo0, "float_range", RangeArgument.Floats.class, SingletonArgumentInfo.contextFree(RangeArgument::m_105405_));
        register(registryArgumentTypeInfo0, "dimension", DimensionArgument.class, SingletonArgumentInfo.contextFree(DimensionArgument::m_88805_));
        register(registryArgumentTypeInfo0, "gamemode", GameModeArgument.class, SingletonArgumentInfo.contextFree(GameModeArgument::m_257772_));
        register(registryArgumentTypeInfo0, "time", TimeArgument.class, new TimeArgument.Info());
        register(registryArgumentTypeInfo0, "resource_or_tag", fixClassType(ResourceOrTagArgument.class), new ResourceOrTagArgument.Info());
        register(registryArgumentTypeInfo0, "resource_or_tag_key", fixClassType(ResourceOrTagKeyArgument.class), new ResourceOrTagKeyArgument.Info());
        register(registryArgumentTypeInfo0, "resource", fixClassType(ResourceArgument.class), new ResourceArgument.Info());
        register(registryArgumentTypeInfo0, "resource_key", fixClassType(ResourceKeyArgument.class), new ResourceKeyArgument.Info());
        register(registryArgumentTypeInfo0, "template_mirror", TemplateMirrorArgument.class, SingletonArgumentInfo.contextFree(TemplateMirrorArgument::m_234343_));
        register(registryArgumentTypeInfo0, "template_rotation", TemplateRotationArgument.class, SingletonArgumentInfo.contextFree(TemplateRotationArgument::m_234414_));
        register(registryArgumentTypeInfo0, "heightmap", HeightmapTypeArgument.class, SingletonArgumentInfo.contextFree(HeightmapTypeArgument::m_274509_));
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            register(registryArgumentTypeInfo0, "test_argument", TestFunctionArgument.class, SingletonArgumentInfo.contextFree(TestFunctionArgument::m_128088_));
            register(registryArgumentTypeInfo0, "test_class", TestClassNameArgument.class, SingletonArgumentInfo.contextFree(TestClassNameArgument::m_127917_));
        }
        return register(registryArgumentTypeInfo0, "uuid", UuidArgument.class, SingletonArgumentInfo.contextFree(UuidArgument::m_113850_));
    }

    private static <T extends ArgumentType<?>> Class<T> fixClassType(Class<? super T> classSuperT0) {
        return (Class<T>) classSuperT0;
    }

    public static boolean isClassRecognized(Class<?> class0) {
        return BY_CLASS.containsKey(class0);
    }

    public static <A extends ArgumentType<?>> ArgumentTypeInfo<A, ?> byClass(A a0) {
        ArgumentTypeInfo<?, ?> $$1 = (ArgumentTypeInfo<?, ?>) BY_CLASS.get(a0.getClass());
        if ($$1 == null) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "Unrecognized argument type %s (%s)", a0, a0.getClass()));
        } else {
            return (ArgumentTypeInfo<A, ?>) $$1;
        }
    }

    public static <A extends ArgumentType<?>> ArgumentTypeInfo.Template<A> unpack(A a0) {
        return byClass(a0).unpack(a0);
    }
}