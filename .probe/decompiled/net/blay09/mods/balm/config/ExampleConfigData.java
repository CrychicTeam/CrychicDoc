package net.blay09.mods.balm.config;

import java.util.Arrays;
import java.util.List;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Comment;
import net.blay09.mods.balm.api.config.Config;
import net.blay09.mods.balm.api.config.ExpectedType;

@Config("balm")
public class ExampleConfigData implements BalmConfigData {

    @Comment("This is an example boolean property")
    public boolean exampleBoolean = true;

    public int exampleInt = 42;

    public String exampleString = "Hello World";

    public String exampleMultilineString = "Hello World";

    public ExampleConfigData.ExampleEnum exampleEnum = ExampleConfigData.ExampleEnum.Hello;

    @ExpectedType(String.class)
    public List<String> exampleStringList = Arrays.asList("Hello", "World");

    @ExpectedType(Integer.class)
    public List<Integer> exampleIntList = Arrays.asList(12, 24);

    @ExpectedType(ExampleConfigData.ExampleEnum.class)
    public List<ExampleConfigData.ExampleEnum> exampleEnumList = Arrays.asList(ExampleConfigData.ExampleEnum.Hello, ExampleConfigData.ExampleEnum.World);

    @Comment("This is an example category")
    public ExampleConfigData.ExampleCategory exampleCategory = new ExampleConfigData.ExampleCategory();

    public static class ExampleCategory {

        @Comment("This is an example string inside a category")
        public String innerField = "I am inside";

        public float exampleFloat = 42.84F;
    }

    public static enum ExampleEnum {

        Hello, World
    }
}