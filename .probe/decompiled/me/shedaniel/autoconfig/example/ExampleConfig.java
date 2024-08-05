package me.shedaniel.autoconfig.example;

import java.util.Arrays;
import java.util.List;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import org.jetbrains.annotations.ApiStatus.Internal;

@Config(name = "autoconfig1u_example")
@Config.Gui.Background("minecraft:textures/block/oak_planks.png")
@Config.Gui.CategoryBackground(category = "b", background = "minecraft:textures/block/stone.png")
@Internal
public class ExampleConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Category("a")
    @ConfigEntry.Gui.TransitiveObject
    public ExampleConfig.ModuleA moduleA = new ExampleConfig.ModuleA();

    @ConfigEntry.Category("a")
    @ConfigEntry.Gui.TransitiveObject
    public ExampleConfig.Empty empty = new ExampleConfig.Empty();

    @ConfigEntry.Category("b")
    @ConfigEntry.Gui.TransitiveObject
    public ExampleConfig.ModuleB moduleB = new ExampleConfig.ModuleB();

    @Config(name = "empty")
    public static class Empty implements ConfigData {
    }

    static enum ExampleEnum {

        FOO, BAR, BAZ
    }

    @Config(name = "module_a")
    public static class ModuleA implements ConfigData {

        @ConfigEntry.Gui.PrefixText
        public boolean aBoolean = true;

        @ConfigEntry.Gui.Tooltip(count = 2)
        public ExampleConfig.ExampleEnum anEnum = ExampleConfig.ExampleEnum.FOO;

        @ConfigEntry.Gui.Tooltip(count = 2)
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public ExampleConfig.ExampleEnum anEnumWithButton = ExampleConfig.ExampleEnum.FOO;

        @Comment("This tooltip was automatically applied from a Jankson @Comment")
        public String aString = "hello";

        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        public ExampleConfig.PairOfIntPairs anObject = new ExampleConfig.PairOfIntPairs(new ExampleConfig.PairOfInts(), new ExampleConfig.PairOfInts(3, 4));

        public List<Integer> list = Arrays.asList(1, 2, 3);

        public int[] array = new int[] { 1, 2, 3 };

        public List<ExampleConfig.PairOfInts> complexList = Arrays.asList(new ExampleConfig.PairOfInts(0, 1), new ExampleConfig.PairOfInts(3, 7));

        public ExampleConfig.PairOfInts[] complexArray = new ExampleConfig.PairOfInts[] { new ExampleConfig.PairOfInts(0, 1), new ExampleConfig.PairOfInts(3, 7) };
    }

    @Config(name = "module_b")
    public static class ModuleB implements ConfigData {

        @ConfigEntry.BoundedDiscrete(min = -1000L, max = 2000L)
        public int intSlider = 500;

        @ConfigEntry.BoundedDiscrete(min = -1000L, max = 2000L)
        public Long longSlider = 500L;

        @ConfigEntry.Gui.TransitiveObject
        public ExampleConfig.PairOfIntPairs anObject = new ExampleConfig.PairOfIntPairs(new ExampleConfig.PairOfInts(), new ExampleConfig.PairOfInts(3, 4));

        @ConfigEntry.Gui.Excluded
        public List<ExampleConfig.PairOfInts> aList = Arrays.asList(new ExampleConfig.PairOfInts(), new ExampleConfig.PairOfInts(3, 4));

        @ConfigEntry.ColorPicker
        public int color = 16777215;
    }

    public static class PairOfIntPairs {

        @ConfigEntry.Gui.CollapsibleObject
        public ExampleConfig.PairOfInts first;

        @ConfigEntry.Gui.CollapsibleObject
        public ExampleConfig.PairOfInts second;

        public PairOfIntPairs() {
            this(new ExampleConfig.PairOfInts(), new ExampleConfig.PairOfInts());
        }

        public PairOfIntPairs(ExampleConfig.PairOfInts first, ExampleConfig.PairOfInts second) {
            this.first = first;
            this.second = second;
        }
    }

    public static class PairOfInts {

        public int foo;

        public int bar;

        public PairOfInts() {
            this(1, 2);
        }

        public PairOfInts(int foo, int bar) {
            this.foo = foo;
            this.bar = bar;
        }
    }
}