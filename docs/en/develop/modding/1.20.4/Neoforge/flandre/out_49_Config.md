---
title: 49 配置信息设置
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 49 配置信息设置 相关教程
image: ./covers/768fdd470a4054e60fec74b522f586fe29c0a701.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考
https://boson.v2mcdev.com/configure/configure.html
https://github.com/neoforged/MDK/blob/main/src/main/java/com/example/examplemod/ExampleMod.java
## 配置文件
这次我们说配置文件的编写，最后的配置文件长这个样子

这里有四个配置项，第一个是int数值，第二个是布尔数值，第三个是string，第四个是一个string的list
\# 开头的是注释
```toml
#General settings
[general]
	#config value
	#Range: > 0
	value = 10
	#config boolean value
	boolean_value = true
	#What you want the introduction message to be for the magic number
	magicNumberIntroduction = "The magic number is... "
	#A list of items to log on common setup.
	items = ["minecraft:iron_ingot"]
```

好了，来看看的我们是怎么创建这个配置的把。以及怎么读取配置中的数据

```java


@Mod.EventBusSubscriber(modid = ExampleMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD) // 这个注解是非必要的。这里我们的使用了一个事件，所以加了这个注解
public class Config { // 添加一个config类
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder() // 创建一个modconfigspec的builder我们需要通过这个builder构建配置
            .comment("General settings") // 就是[general] 上面的注解
            .push("general"); // 这是表示添加一个新的配置列表，名字是[general]

    public static final ModConfigSpec.IntValue VALUE = BUILDER
            .comment("config value")// 使用bulider构建一个int数值 
            .defineInRange("value",10,0,Integer.MAX_VALUE);// 设置名称，初始值，最小值，最大值

    public static final ModConfigSpec.BooleanValue BOOLEAN_VALUE = BUILDER
            .comment("config boolean value")
            .define("boolean_value", true);// 构建一个布尔值

    public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    public  static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);

    // 检验item名称是否合法
    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(new ResourceLocation(itemName));
    }
    // 构建配置
    public static final ModConfigSpec SPEC = BUILDER.pop().build();
    // 下面演示的是怎么获得配置中的数值 这部分并不是必要的
    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntroduction;
    public static Set<Item> items;
    // 这里的配置事件的记载时候的调用，我们将配置中的数值赋值给了对于的字段，等会打印这些字段
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        logDirtBlock = BOOLEAN_VALUE.get();
        magicNumber = VALUE.get();
        magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

        // convert the list of strings into a set of items
        items = ITEM_STRINGS.get().stream()
                .map(itemName -> BuiltInRegistries.ITEM.get(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());

    }

}
```


```java


// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    public static final String MODID = "examplemod";
    private static final Logger LOGGER = LogUtils.getLogger();
     public ExampleMod(IEventBus modEventBus)
    {
        //config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

        // log config info
        // 根据读取的字段的数值进行打印
        if (Config.logDirtBlock) // boolean值
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        // string和value数值
        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
        // list
        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));

    }


}

```

当你修改配置文件中的数值的时候相应的内容也会发生变化。并且这个配置的数值是实时生效的，不需要你重启客户端。
