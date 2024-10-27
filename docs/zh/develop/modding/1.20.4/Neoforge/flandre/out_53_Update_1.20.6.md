---
title: 53 更新到Minecraft 1.20.6版本
published: 2024-05-18
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 53 Minecraft Neoforge 版本更新
image: https://cdn.jsdelivr.net/gh/Flandre923/CDN@latest/img/wallhaven-jxjv3w.png
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 更新gradle

到github中下载心的gradle，替换掉之前的gradle

https://github.com/neoforged/MDK/blob/main/gradle/wrapper/gradle-wrapper.properties

![20240517193044](https://cdn.jsdelivr.net/gh/Flandre923/CDN@latest/img/20240517193044.png)

## 修改mods.toml 为 neoforge.mods.toml

![20240517193314](https://cdn.jsdelivr.net/gh/Flandre923/CDN@latest/img/20240517193314.png)

修改其中的内容如下

```java
# This is an example neoforge.mods.toml file. It contains the data relating to the loading mods.
# There are several mandatory fields (#mandatory), and many more that are optional (#optional).
# The overall format is standard TOML format, v0.5.0.
# Note that there are a couple of TOML lists in this file.
# Find more information on toml format here:  https://github.com/toml-lang/toml
# The name of the mod loader type to load - for regular FML @Mod mods it should be javafml
modLoader="javafml" #mandatory

# A version range to match for said mod loader - for regular FML @Mod it will be the the FML version. This is currently 47.
loaderVersion="${loader_version_range}" #mandatory

# The license for you mod. This is mandatory metadata and allows for easier comprehension of your redistributive properties.
# Review your options at https://choosealicense.com/. All rights reserved is the default copyright stance, and is thus the default here.
license="${mod_license}"

# A URL to refer people to when problems occur with this mod
#issueTrackerURL="https://change.me.to.your.issue.tracker.example.invalid/" #optional

# A list of mods - how many allowed here is determined by the individual mod loader
[[mods]] #mandatory

# The modid of the mod
modId="${mod_id}" #mandatory

# The version number of the mod
version="${mod_version}" #mandatory

# A display name for the mod
displayName="${mod_name}" #mandatory

# A URL to query for updates for this mod. See the JSON update specification https://docs.neoforged.net/docs/misc/updatechecker/
#updateJSONURL="https://change.me.example.invalid/updates.json" #optional

# A URL for the "homepage" for this mod, displayed in the mod UI
#displayURL="https://change.me.to.your.mods.homepage.example.invalid/" #optional

# A file name (in the root of the mod JAR) containing a logo for display
#logoFile="examplemod.png" #optional

# A text field displayed in the mod UI
#credits="" #optional

# A text field displayed in the mod UI
authors="${mod_authors}" #optional

# Display Test controls the display for your mod in the server connection screen
# MATCH_VERSION means that your mod will cause a red X if the versions on client and server differ. This is the default behaviour and should be what you choose if you have server and client elements to your mod.
# IGNORE_SERVER_VERSION means that your mod will not cause a red X if it's present on the server but not on the client. This is what you should use if you're a server only mod.
# IGNORE_ALL_VERSION means that your mod will not cause a red X if it's present on the client or the server. This is a special case and should only be used if your mod has no server component.
# NONE means that no display test is set on your mod. You need to do this yourself, see IExtensionPoint.DisplayTest for more information. You can define any scheme you wish with this value.
# IMPORTANT NOTE: this is NOT an instruction as to which environments (CLIENT or DEDICATED SERVER) your mod loads on. Your mod should load (and maybe do nothing!) whereever it finds itself.
#displayTest="MATCH_VERSION" # MATCH_VERSION is the default if nothing is specified (#optional)

# The description text for the mod (multi line!) (#mandatory)
description='''${mod_description}'''

# The [[mixins]] block allows you to declare your mixin config to FML so that it gets loaded.
#[[mixins]]
#config="${mod_id}.mixins.json"

# The [[accessTransformers]] block allows you to declare where your AT file is.
# If this block is omitted, a fallback attempt will be made to load an AT from META-INF/accesstransformer.cfg
#[[accessTransformers]]
#file="META-INF/accesstransformer.cfg"

# The coremods config file path is not configurable and is always loaded from META-INF/coremods.json

# A dependency - use the . to indicate dependency for a specific modid. Dependencies are optional.
[[dependencies.${mod_id}]] #optional
    # the modid of the dependency
    modId="neoforge" #mandatory
    # The type of the dependency. Can be one of "required", "optional", "incompatible" or "discouraged" (case insensitive).
    # 'required' requires the mod to exist, 'optional' does not
    # 'incompatible' will prevent the game from loading when the mod exists, and 'discouraged' will show a warning
    type="required" #mandatory
    # Optional field describing why the dependency is required or why it is incompatible
    # reason="..."
    # The version range of the dependency
    versionRange="${neo_version_range}" #mandatory
    # An ordering relationship for the dependency.
    # BEFORE - This mod is loaded BEFORE the dependency
    # AFTER - This mod is loaded AFTER the dependency
    ordering="NONE"
    # Side this dependency is applied on - BOTH, CLIENT, or SERVER
    side="BOTH"

# Here's another dependency
[[dependencies.${mod_id}]]
    modId="minecraft"
    type="required"
    # This version range declares a minimum of the current minecraft version up to but not including the next major version
    versionRange="${minecraft_version_range}"
    ordering="NONE"
    side="BOTH"

# Features are specific properties of the game environment, that you may want to declare you require. This example declares
# that your mod requires GL version 3.2 or higher. Other features will be added. They are side aware so declaring this won't
# stop your mod loading on the server for example.
#[features.${mod_id}]
#openGLVersion="[3.2,)"
```

##  修改下build.gradle文件

修改插件中的neoforge.gradle 的版本

```gradle
plugins {
    id 'java-library'
    id 'eclipse'
    id 'idea'
    id 'maven-publish'
    id 'net.neoforged.gradle.userdev' version '7.0.124'
}

```

修改Java版本为21

```java
java.toolchain.languageVersion = JavaLanguageVersion.of(21)
```

由于之前不知道什么更新，导致gradle下载jar不会下载source，要自己下载，导致有点麻烦，并且不会下载全，这个问题应该可以通过新的这个方式修复

文件最下方写上这个。

```gradle

idea {
    module {
        downloadSources = true
        downloadJavadoc = true
    }
}

```

并且去掉JEI的相关依赖，因为JEI没有更新1.20.6的版本。


修改mods.toml 为 neoforge.mods.toml , 不然启动不了


## 修改gralde.properties文件主要是版本

各位参考更新即可。

```properties
# Sets default memory used for gradle commands. Can be overridden by user or command line properties.
#org.gradle.jvmargs=
org.gradle.daemon=false
org.gradle.debug=false

#read more on this at https://github.com/neoforged/NeoGradle/blob/NG_7.0/README.md#apply-parchment-mappings
# you can also find the latest versions at: https://parchmentmc.org/docs/getting-started
neogradle.subsystems.parchment.minecraftVersion=1.20.6
neogradle.subsystems.parchment.mappingsVersion=2024.05.01
# Environment Properties
# You can find the latest versions here: https://projects.neoforged.net/neoforged/neoforge
# The Minecraft version must agree with the Neo version to get a valid artifact
minecraft_version=1.20.6
# The Minecraft version range can use any release version of Minecraft as bounds.
# Snapshots, pre-releases, and release candidates are not guaranteed to sort properly
# as they do not follow standard versioning conventions.
minecraft_version_range=[1.20.6,1.21)
# The Neo version must agree with the Minecraft version to get a valid artifact
neo_version=20.6.62-beta
# The Neo version range can use any version of Neo as bounds
neo_version_range=[20.6,)
# The loader version range can only use the major version of FML as bounds
loader_version_range=[2,)

## Mod Properties
mc_version=1.20.6
#jei_version=17.0.0.30

terrablender_version=3.5.0.4

# The unique mod identifier for the mod. Must be lowercase in English locale. Must fit the regex [a-z][a-z0-9_]{1,63}
# Must match the String constant located in the main mod class annotated with @Mod.
mod_id=examplemod
# The human-readable display name for the mod.
mod_name=Example Mod
# The license of the mod. Review your options at https://choosealicense.com/. All Rights Reserved is the default.
mod_license=All Rights Reserved
# The mod version. See https://semver.org/
mod_version=1.0.0
# The group ID for the mod. It is only important when publishing as an artifact to a Maven repository.
# This should match the base package used for the mod sources.
# See https://maven.apache.org/guides/mini/guide-naming-conventions.html
mod_group_id=net.flandre923.examplemod
# The authors of the mod. This is a simple text string that is used for display purposes in the mod list.
mod_authors=YourNameHere, OtherNameHere
# The description of the mod. This is a simple multiline text string that is used for display purposes in the mod list.
mod_description=Example mod description.\nNewline characters can be used and will be replaced properly.
```

## 最后重新realod项目

修改完毕后，重新reload项目，他会自动配置相关的内容。等待完成。

## 来更新报错的类

## GiveAppleTrigger


修改Codec部分

```java
        public static final Codec<GiveAppleTrigger.Instance> CODEC = RecordCodecBuilder.create(
                p_312304_ -> p_312304_.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(GiveAppleTrigger.Instance::player)
                        )
                        .apply(p_312304_, GiveAppleTrigger.Instance::new)
        );
```

## BlockEntity部分

load,save 方法改变

CounterBlockEntity

```java
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        counter = pTag.getInt("counter");
        super.loadAdditional(pTag, pRegistries);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.putInt("counter",counter);

    }
```

FirstMenuBlockEntity

```JAVA
    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        CompoundTag inventory = pTag.getCompound("inventory");
        this.itemHandler.deserializeNBT(pRegistries,inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
        pTag.put("inventory",this.itemHandler.serializeNBT(pRegistries));
    }
```

数据同步相关的内容，修改

ZombieBlockEntity

```java

    //而onDataPacket才是客户端接受数据包的方法。
    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
        handleUpdateTag(pkt.getTag(),lookupProvider);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag compoundNBT = super.getUpdateTag(pRegistries);
        compoundNBT.putBoolean("flag", flag);
        return compoundNBT;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        flag = tag.getBoolean("flag");
    }
```

## Block的更新

DataSaveBlock

方块的use方法被拆为了2个，不使用物品右键和不使用物品右键

```java


    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(!pLevel.isClientSide){
            ModLevelSaveData data  = ModLevelSaveData.get(pLevel);
            ItemStack mainHandItem = pPlayer.getMainHandItem();
            if(mainHandItem.isEmpty()){
                ItemStack itemStack = data.getItem();
                pPlayer.setItemInHand(InteractionHand.MAIN_HAND,itemStack);
            }else{
                ItemStack itemStack =mainHandItem.copy();
                mainHandItem.shrink(mainHandItem.getCount());
                data.putItem(itemStack);
                InitTrigger.GIVE_RUBY_APPLE.get().trigger((ServerPlayer) pPlayer);
            }
        }
        return ItemInteractionResult.SUCCESS;
    }
```

firstMenuBlock

```java

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide){
            pPlayer.openMenu(this.getMenuProvider(pState,pLevel,pPos),pPos);
        }
        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
    }

```

LampBlock

```java

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(!pLevel.isClientSide){
            pLevel.setBlock(pPos,pState.cycle(LIT),3);
        }
        return super.useItemOn(pStack, pState, pLevel, pPos, pPlayer, pHand, pHitResult);
    }

```

PortalBlock 

```java

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pPlayer.canChangeDimensions()){
            handlePortalTeleport(pLevel,pPos,pPlayer);
            return ItemInteractionResult.SUCCESS;
        }else{
            return ItemInteractionResult.FAIL;
        }
    }

```

RubyCounterBlock

```java


    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide){
            var rubyBlocksEntity = (CounterBlockEntity) pLevel.getBlockEntity(pPos);
            int counter = rubyBlocksEntity.increase();
            pPlayer.sendSystemMessage(Component.literal("counter:"+counter));
        }
        return ItemInteractionResult.SUCCESS;
    }

```

RubyFrame

```java


    @Override
    protected boolean isPathfindable(BlockState pState, PathComputationType pPathComputationType) {
        switch(pPathComputationType) {
            case LAND:
                return false;
            case WATER:
                return pState.getFluidState().is(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }

```

ModBlocks

流体的注册，需要一个FlowingFluid了不是一个supplier也可以了。

```java
   public static final Supplier<LiquidBlock> MY_FLUID_BLOCK = BLOCKS.register("my_fluid_block",
            ()->new LiquidBlock(ModFluids.MY_SOURCE_FLUID_BLOCK.get(),BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

```

SpeedUpCapability的修改

其他几个类类似，就不写了。

```java


    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compoundNBT = new CompoundTag();
        compoundNBT.putInt("level", this.level);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.level = nbt.getInt("level");
    }
```

## HUD的接口的变化

ExampleHud

```java

public class ExampleHud implements LayeredDraw.Layer {
    private static final ExampleHud hud = new ExampleHud();
    private final int width;
    private final int height;
    private final Minecraft minecraft;
    private final ResourceLocation HUD = new ResourceLocation(ExampleMod.MODID, "textures/gui/hud.png");

    public ExampleHud() {
        this.width = Minecraft.getInstance().getWindow().getScreenWidth();
        this.height = Minecraft.getInstance().getWindow().getScreenHeight();
        this.minecraft = Minecraft.getInstance();
    }


    @Override
    public void render(GuiGraphics pGuiGraphics, float pPartialTick) {
        if (minecraft.player.getMainHandItem().getItem()!= ModItems.RUBY_APPLE.get())
            return;
        pGuiGraphics.setColor(1,1,1,1);
        pGuiGraphics.blit(HUD,width/2-16,height/2-64,0,0,32,32,32,32);
    }

    public static ExampleHud getInstance() {
        return hud;
    }
}

```

ThirstHud

```java


public class ThirstHud {

    private static final ResourceLocation FILLED_THIRST = new ResourceLocation(ExampleMod.MODID,
            "textures/gui/filled_thirst.png");
    private static final ResourceLocation EMPTY_THIRST = new ResourceLocation(ExampleMod.MODID,
            "textures/gui/empty_thirst.png");
    public static final LayeredDraw.Layer HUD_THIRST =  (guiGraphics, partialTick) -> {
        int screenWidth = Minecraft.getInstance().getWindow().getScreenWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getScreenHeight();
        int x = screenWidth / 2;
        int y = screenHeight;
        for(int i = 0; i < 10; i++) {
            guiGraphics.blit(EMPTY_THIRST,x - 94 + (i * 9), y - 54,90,0,0,12,12,
                    12,12);
        }

        for(int i = 0; i < 10; i++) {
            if(ClientPlayerThirstData.getPlayerThirst() > i) {
                guiGraphics.blit(FILLED_THIRST,x - 94 + (i * 9),y - 54,90,0,0,12,12,
                        12,12);
            } else {
                break;
            }
        }
    };
}
```

## 命名空间的修改


```java

@EventBusSubscriber(modid = ExampleMod.MODID,bus = EventBusSubscriber.Bus.MOD)
```

## datagenerator的修改

LootTableProvider

```java

public class ModLootTableProvider extends LootTableProvider {


    public ModLootTableProvider(PackOutput pOutput, Set<ResourceKey<LootTable>> pRequiredTables, List<SubProviderEntry> pSubProviders, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRequiredTables, pSubProviders, pRegistries);
    }
}

```

DataGenerator的修改

```java
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModLootTableProvider>)pOutput -> new ModLootTableProvider(pOutput, Collections.emptySet(),
                        List.of(
                                new LootTableProvider.SubProviderEntry(ModBlockLootProvider::new, LootContextParamSets.BLOCK)
                        ),lp)
        );
```

修改所有的bootstrap的类型BootstrapContext

```java
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> pContext) {
        RuleTest stoneOreReplaceRuleTest = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepSlateOreReplaceRuleTest = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);


        List<OreConfiguration.TargetBlockState> list = List.of(
                OreConfiguration.target(stoneOreReplaceRuleTest, ModBlocks.RUBY_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepSlateOreReplaceRuleTest, ModBlocks.RUBY_BLOCK.get().defaultBlockState())
        );

        FeatureUtils.register(pContext, ORE_RUBY, Feature.ORE, new OreConfiguration(list, 9));

    }
```

## 药水

ExampleEffect

该方法具有的返回类型

```java

    @Override
    public boolean applyEffectTick(LivingEntity pEntity, int pAmplifier) {
        super.applyEffectTick(pEntity, pAmplifier);
        if (pEntity.getHealth() < pEntity.getMaxHealth()) {
            pEntity.heal(2.0F);
            return true;
        }
        return false;
    }
```

Effect的注册改下

```java
    public static final Holder<MobEffect> EXAMPLE_EFFECT = register("example_effect", ()->new ExampleEffect(MobEffectCategory.BENEFICIAL, 16262179));

```
POTION改下

```java
    public static final DeferredHolder<Potion,Potion> EXAMPLE_POTION = register("example_potion", () -> new Potion("example_potion",new MobEffectInstance(ModEffects.EXAMPLE_EFFECT,1200,1)));

```


## 附魔

附魔的类别进行了修改，现在使用tag的json文件作为标识了。

![20240517212413](https://cdn.jsdelivr.net/gh/Flandre923/CDN@latest/img/20240517212413.png)

ExampleModReference 文件不再需要

注册


```java

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Registries.ENCHANTMENT, ExampleMod.MODID);

    public static final Supplier<Enchantment> POWER_ARROWS = ENCHANTMENTS.register("my_power", MyPowerfulEnchantment::new);

    public static void register(IEventBus bus) {
        ENCHANTMENTS.register(bus);
    }
}

```

新增一个附魔的方法，其中参数比较多，不过还是之前的一些数据，其中这数据不需要在重写了。


```java

public class MyPowerfulEnchantment extends Enchantment {
    protected MyPowerfulEnchantment() {
        super(Enchantment.definition(ItemTags.BOW_ENCHANTABLE,2,2,Enchantment.dynamicCost(10,20),Enchantment.dynamicCost(60,20),4,EquipmentSlot.MAINHAND));
    }


}

```

## 实体

Animal添加一个新的抽象方法siFood


```java
        // Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on the animal type)
    @Override
    public boolean isFood(ItemStack pStack) {
        return false;
    }

```

实体定义数据初始值的方法改变了。


```java
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(COUNTER,0);
    }


```

## 物品

装备材料类需要进行修改

```java

public enum ModItemTiers implements Tier {
    RUBY(BlockTags.INCORRECT_FOR_DIAMOND_TOOL,3,2000,10F,4F,30,() -> Ingredient.of(ModItems.RUBY.get()));

    private final TagKey<Block> incorrectBlockForDrops;
    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private ModItemTiers(TagKey<Block> pIncorrectBlockForDrops,int pLevel, int pUses, float pSpeed, float pDamage, int pEnchantmentValue, Supplier<Ingredient> pRepairIngredient) {
        this.incorrectBlockForDrops = pIncorrectBlockForDrops;
        this.level = pLevel;
        this.uses = pUses;
        this.speed = pSpeed;
        this.damage = pDamage;
        this.enchantmentValue = pEnchantmentValue;
        this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
    }


    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.damage;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return incorrectBlockForDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

}

```

装备材料修改较大，变为注册的了，下面是添加一个新的方法。

```java

public class ModArmorMaterial  {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIAL = DeferredRegister.create(Registries.ARMOR_MATERIAL, ExampleMod.MODID);

    public static final DeferredHolder<ArmorMaterial,ArmorMaterial> RUBY = register(
            "ruby",
            Util.make(new EnumMap<>(ArmorItem.Type.class),typeObjectEnumMap -> {
                typeObjectEnumMap.put(ArmorItem.Type.BOOTS,1);
                typeObjectEnumMap.put(ArmorItem.Type.LEGGINGS,2);
                typeObjectEnumMap.put(ArmorItem.Type.CHESTPLATE,3);
                typeObjectEnumMap.put(ArmorItem.Type.HELMET,1);
                typeObjectEnumMap.put(ArmorItem.Type.BODY,3);
            }),
            15,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0F,
            0F,
            ()-> Ingredient.of(ModItems.RUBY.get()),
            List.of(new ArmorMaterial.Layer(new ResourceLocation(ExampleMod.MODID,"ruby"),"",true),new ArmorMaterial.Layer(new ResourceLocation(ExampleMod.MODID,"ruby"),"_overlay",false))
    );

    public static DeferredHolder<ArmorMaterial, ArmorMaterial> register(String name,
                                                                                 EnumMap<ArmorItem.Type,Integer> pDefense,
                                                                                 int pEnchantmentValue,
                                                                                 Holder<SoundEvent> pEquipSound,
                                                                                 float pToughness,
                                                                                 float pKnockResistance,
                                                                                 Supplier<Ingredient> pRepairIngridient,
                                                                                 List<ArmorMaterial.Layer> pLayers){

        EnumMap<ArmorItem.Type,Integer> enummap = new EnumMap<>(ArmorItem.Type.class);

        for(ArmorItem.Type armorMaterial:ArmorItem.Type.values()){
            enummap.put(armorMaterial,pDefense.get(armorMaterial));
        }

        return ARMOR_MATERIAL.register(name,()->new ArmorMaterial(enummap,pEnchantmentValue,pEquipSound,pRepairIngridient,pLayers,pToughness,pKnockResistance));
    }
    // 别忘记注册到总线
    public static void register(IEventBus eventBus){
        ARMOR_MATERIAL.register(eventBus);
    }

}
```


好了好需要修改贴图的位置，将原来的位置放到你的命名空间中去吧。（吐槽这遗留问题终于解决了。）

![20240517220826](https://cdn.jsdelivr.net/gh/Flandre923/CDN@latest/img/20240517220826.png)

RubyApple


```java
public class RubyApple extends Item {
    private static final FoodProperties FOOD_PROPERTIES = new FoodProperties.Builder()
            .saturationModifier(10)
            .nutrition(20)
            .effect(()-> new MobEffectInstance(MobEffects.POISON,3*20,1),1)
            .build();
    public RubyApple() {
        super(new Properties().food(FOOD_PROPERTIES));
    }
}


```

pixcle

```java

public class RubyPickaxe extends PickaxeItem {
    public RubyPickaxe() {
        super(ModItemTiers.RUBY,new Properties());
    }
}

```

sword

```java
public class RubySword extends SwordItem {

    public RubySword(){
        super(ModItemTiers.RUBY,new Item.Properties());
    }
}

```

messageItem 

```java
//网路的更新

public class MessageItem extends Item {
    public MessageItem() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pLevel.isClientSide){
//            PacketDistributor.PLAYER.with(pPlayer).send(new );
            PacketDistributor.sendToServer(new MyData("From client",2));
        }
        if(!pLevel.isClientSide){
            PacketDistributor.sendToPlayer((ServerPlayer) pPlayer,new MyData("From server",2));
        }
        return super.use(pLevel,pPlayer,pUsedHand);
    }
}
```

items

```java
    // MY_SOURCE_FLUID_BLOCK后添加get()
    // supplier的方法没了
    public static final Supplier<Item> MY_FLUID_BUCKET = register("my_fluid_bucket", ()->new BucketItem(ModFluids.MY_SOURCE_FLUID_BLOCK.get(),new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

```

## ModLevelSaveData

由于ItemStack中的NBT没了，所以添加了新的方式获得ItemStack的NBT。下面是新的代码

```java
    private ModLevelSaveData load(CompoundTag nbt,HolderLookup.Provider pLevelRegistry) {
        ListTag listNBT = (ListTag) nbt.get("list");
        if (listNBT != null) {
            for (Tag value : listNBT) {
                CompoundTag tag = (CompoundTag) value;
                ItemStack itemStack = ItemStack.parse(pLevelRegistry,tag).orElse(ItemStack.EMPTY);
                if (itemStack.equals(ItemStack.EMPTY)){
                    continue;
                }
                itemStacks.push(itemStack);
            }
        }
        // Load saved data
        return this;
    }

    private static ModLevelSaveData decode(CompoundTag tag,HolderLookup.Provider pRegistries){
        return ModLevelSaveData.create().load(tag,pRegistries);
    }


    public static ModLevelSaveData get(Level worldIn) {
        if (!(worldIn instanceof ServerLevel)) {
            throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
        }
        ServerLevel world = worldIn.getServer().getLevel(ServerLevel.OVERWORLD);
        DimensionDataStorage dataStorage = world.getDataStorage();
        return dataStorage.computeIfAbsent(new Factory<ModLevelSaveData>(ModLevelSaveData::create, ModLevelSaveData::decode), ModLevelSaveData.NAME);
    }

    @Override
    public CompoundTag save(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        ListTag listTag = new ListTag();
        itemStacks.forEach((stack) -> {
            CompoundTag compoundTag = new CompoundTag();
            listTag.add(stack.save(pRegistries,compoundTag));
        });
        pTag.put("list", listTag);
        return pTag;
    }
```

## 网络

自定载荷修改

添加了Codec作为序列化反序列化的工具
使用CustomPacketPayload.TYPE代替ID作为表示.

MyData

```java

public record MyData(String message, int age) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf,MyData> STREAM_CODEC =
            CustomPacketPayload.codec(MyData::write,MyData::new);
    public static final Type<MyData> TYPE = new Type<>(new ResourceLocation(ExampleMod.MODID,"my_data"));

    public MyData(final FriendlyByteBuf buf){
        this(buf.readUtf(),buf.readInt());
    }

    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeUtf(message());
        pBuffer.writeInt(age());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

```

ThirstData

```java

public record ThirstData(int thirst) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf,ThirstData> STREAM_CODEC =
            CustomPacketPayload.codec(ThirstData::write,ThirstData::new);
    public static final CustomPacketPayload.Type<ThirstData> TYPE =
            new CustomPacketPayload.Type<>(new ResourceLocation(ExampleMod.MODID,"thirst_data"));

    public ThirstData(final FriendlyByteBuf buf){
        this(buf.readInt());
    }

    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(thirst());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

```

服务端和客户端数据包处理的修改

client 

```java


public class ClientPayloadHandler {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

    public static ClientPayloadHandler getInstance() {
        return INSTANCE;
    }

    public void handleData(final MyData data, final IPayloadContext context) {
        // Do something with the data, on the network thread

        // Do something with the data, on the main thread
        context.enqueueWork(() -> {
                 LOGGER.info(data.message());
                })
                .exceptionally(e -> {
                    // Handle exception
                    context.disconnect(Component.translatable("my_mod.networking.failed", e.getMessage()));
                    return null;
                });
    }

    public void handleThirstData(final ThirstData data,final IPayloadContext context){
        context.enqueueWork(()->{
            ClientPlayerThirstData.set(data.thirst());
        }).exceptionally(e->{
            context.disconnect(Component.translatable("my_mod.networking.failed", e.getMessage()));
            return null;
        });
    }

}

```
server 

```java

public class ServerPayloadHandler {
    private static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ServerPayloadHandler getInstance() {
        return INSTANCE;
    }

    public void handleData(final MyData data, final IPayloadContext context) {
        // Do something with the data, on the network thread

        // Do something with the data, on the main thread
        context.enqueueWork(() -> {
                    LOGGER.info(data.message());
                })
                .exceptionally(e -> {
                    // Handle exception
                    context.disconnect(Component.translatable("my_mod.networking.failed", e.getMessage()));
                    return null;
                });
    }

    public void handleThirstData(final ThirstData data, final IPayloadContext context){
        context.enqueueWork(()->{
            Player player = context.player();

            if (player instanceof ServerPlayer serverPlayer){
                ServerLevel level = (ServerLevel) player.level();
                if(hasWaterAroundThem(player,level,2)){

                    level.playSound(null, player.getOnPos(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS,
                            0.5F, level.random.nextFloat() * 0.1F + 0.9F);

                    Optional.ofNullable(player.getCapability(ModCapabilities.PLAYER_THIRST_HANDLER)).ifPresent(thirst->{
                        thirst.addThirst(1);

                        player.sendSystemMessage(Component.literal("Current Thirst " + thirst.getThirst())
                                .withStyle(ChatFormatting.AQUA));

                        PacketDistributor.sendToPlayer(serverPlayer,new ThirstData(thirst.getThirst()));
                    });

                }else{
                    Optional.ofNullable(player.getCapability(ModCapabilities.PLAYER_THIRST_HANDLER)).ifPresent(thirst -> {
                        player.sendSystemMessage(Component.literal("Current Thirst " + thirst.getThirst())
                                .withStyle(ChatFormatting.AQUA));
                        // 添加这个代码，当玩家按下o建的时候同步数据
//                            ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(thirst.getThirst()),player);
                        PacketDistributor.sendToPlayer(serverPlayer,new ThirstData(thirst.getThirst()));
                    });
                }
            }
        }).exceptionally(e->{
            context.disconnect(Component.translatable("my_mod.networking.failed", e.getMessage()));
            return null;
        });
    }

    private boolean hasWaterAroundThem(Player player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }
}
```

网络包的注册修改

```java

@EventBusSubscriber(modid = ExampleMod.MODID,bus = EventBusSubscriber.Bus.MOD)
public class Networking {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(ExampleMod.MODID);
        registrar.playBidirectional(
                MyData.TYPE,
                MyData.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleData,
                        ServerPayloadHandler::handleData
                )
        );

        registrar.playBidirectional(
                ThirstData.TYPE,
                ThirstData.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ClientPayloadHandler::handleThirstData,
                        ServerPayloadHandler::handleThirstData
                )
        );
    }

}
```

## 建筑

改了个名字

```java
    public static final MapCodec<MyStructure> CODEC = simpleCodec(MyStructure::new);

```

## CONFIG

```java
public ExampleMod(IEventBus modEventBus, ModContainer modContainer)
    {
      
        //config
        modContainer.registerConfig(ModConfig.Type.COMMON,Config.SPEC);
        NeoForge.EVENT_BUS.register(this);
    }
```

## 粒子

例子砍掉了Deserializer，改成了codec。我还没搞清楚这个codec怎么用，所以这里我按照原版的方法添加例子，就处理命令行输入的问题了。


粒子
```java

public class ExampleParticle extends TextureSheetParticle {
    public ExampleParticle(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
        this.lifetime = 100;
        this.hasPhysics = true;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
```

粒子type

```java

public class ModParticleType {
    public static final DeferredRegister <ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, ExampleMod.MODID);
    public static final Supplier<SimpleParticleType> EXAMPLE_PARTICLE_TYPE = register("example_particle_type", () -> new SimpleParticleType(true));

    public static <T extends ParticleType<?>> DeferredHolder<ParticleType<?>, T> register(String name, Supplier<T> particleType){
        return PARTICLE_TYPES.register(name, particleType);

    }
    public static void register(IEventBus eventBus){
        PARTICLE_TYPES.register(eventBus);
    }


}
```

例子provider

```java

public class ExampleParticleProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;


    public ExampleParticleProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }

    @Nullable
    @Override
    public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        ExampleParticle exampleParticle = new ExampleParticle(pLevel, pX, pY, pZ);
        exampleParticle.pickSprite(this.sprites);
        return exampleParticle;
    }
}
```

粒子引擎

```java

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleFactoryRegistry {
    @SubscribeEvent
    public static void onParticleFactoryRegistration(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleType.EXAMPLE_PARTICLE_TYPE.get(), ExampleParticleProvider::new);
//        Minecraft.getInstance().particleEngine.register(ModParticleType.EXAMPLE_PARTICLE_TYPE.get(), ExampleParticleProvider::new);
    }
}

```

对于ExampleParticleType，我直接注解掉了。

## Event

ForgeClientEventHandler

```java

@EventBusSubscriber(modid = ExampleMod.MODID,value = Dist.CLIENT)
public class ForgeClientEventHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if(KeyBinding.DRINKING_KEY.consumeClick()){
            PacketDistributor.sendToServer(new ThirstData(ClientPlayerThirstData.getPlayerThirst()));
        }
    }

}

```

ModClientEventHandler

GUI的注册放在一个单独的事件中了。

```java 
    @SubscribeEvent
    public static void registerMenuScreen(RegisterMenuScreensEvent event){
        event.register(ModMenuTypes.FIRST_MENU.get(),FistMenuGui::new);
    }

```

村民交易事件修改

```java
        @SubscribeEvent
        public static void addCustomTrades(VillagerTradesEvent event) {
            if(event.getType() == VillagerProfession.TOOLSMITH) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.MAGIC_INGOT.get(), 1);
                int villagerLevel = 1;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 2),
                        stack,10,8,0.02F));
            }

            if(event.getType() == ModVillagers.RUBY_MASTER.get()) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                ItemStack stack = new ItemStack(ModItems.RUBY.get(), 15);
                int villagerLevel = 1;

                trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 5),
                        stack,10,8,0.02F));
            }
        }

```

playerTick

```java

        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            if(event.getEntity() instanceof ServerPlayer serverPlayer) {
                Optional<PlayerThirst> optionalPlayerThirst = Optional.ofNullable(serverPlayer.getCapability(ModCapabilities.PLAYER_THIRST_HANDLER));
                optionalPlayerThirst .ifPresent(thirst -> {
                    if(thirst.getThirst() > 0 && serverPlayer.getRandom().nextFloat() < 0.005f) { // Once Every 10 Seconds on Avg
                        thirst.subThirst(1);
                        // TODO 同步数据
//                        ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(thirst.getThirst()),(ServerPlayer) event.player);
                        PacketDistributor.sendToPlayer(serverPlayer,new ThirstData(thirst.getThirst()));
                        serverPlayer.sendSystemMessage(Component.literal("Subtracted Thirst"));
                    }
                });
            }
        }
```

joinLevelEvent

```java

        @SubscribeEvent
        public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
            if(!event.getLevel().isClientSide()) {
                if(event.getEntity() instanceof ServerPlayer player) {
                    Optional<PlayerThirst> optionalPlayerThirst = Optional.ofNullable(player.getCapability(ModCapabilities.PLAYER_THIRST_HANDLER));
                    optionalPlayerThirst.ifPresent(thirst -> {
                        // TODO 同步数据
                        PacketDistributor.sendToPlayer(player,new ThirstData(thirst.getThirst()));
//                        ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(thirst.getThirst()), player);
                    });
                }

            }
        }
```

MyPowerEnchantmentEvent

```java
        boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY, bow) > 0;
```



MyPowerEnchantmentEvent

```java

@EventBusSubscriber
public class MyPowerEnchantmentEvent {
    @SubscribeEvent
    public static void MyPowerEnchantmentShot(ArrowLooseEvent event){
        ItemStack bow = event.getBow();
        Player player = event.getEntity();
        int i = event.getCharge();
        Level level = event.getLevel();
        ItemStack itemstack = player.getProjectile(bow);
        boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY, bow) > 0;

        shot(itemstack,flag,i,player,level,bow,player.getUsedItemHand());
        // 取消掉原版的弓的射箭逻辑
        event.setCanceled(true);

    }

    public static void shot(ItemStack itemstack, Boolean flag, int i, Player player, Level pLevel, ItemStack pStack, InteractionHand pHand){
        if (!itemstack.isEmpty() || flag) {
            if (itemstack.isEmpty()) {
                itemstack = new ItemStack(Items.ARROW);
            }

            float f = getPowerForTime(i);
            if (!((double)f < 0.1)) {
                boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, pStack, player));
                if (!pLevel.isClientSide) {
                    ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                    AbstractArrow abstractarrow = arrowitem.createArrow(pLevel, itemstack, player);
                    abstractarrow = customArrow(abstractarrow, itemstack);
                    abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
                    if (f == 1.0F) {
                        abstractarrow.setCritArrow(true);
                    }

                    int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER, pStack);
                    if (j > 0) {
                        abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double)j * 0.5 + 0.5);
                    }

                    int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER, pStack);
                    if (k > 0) {
                        abstractarrow.setKnockback(k);
                    }

                    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAME, pStack) > 0) {
                        abstractarrow.setRemainingFireTicks(100);
                    }
                    // 这里是我们的附魔，这里为了效果直接不按照等级，基于了20点基础伤害
                    if(EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.POWER_ARROWS.get(),pStack) > 0){
                        abstractarrow.setBaseDamage(20);
                    }

                    pStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(pHand));
                    if (flag1 || player.getAbilities().instabuild && (itemstack.is(Items.SPECTRAL_ARROW) || itemstack.is(Items.TIPPED_ARROW))) {
                        abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }

                    pLevel.addFreshEntity(abstractarrow);
                }

                pLevel.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.ARROW_SHOOT,
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F
                );
                if (!flag1 && !player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        player.getInventory().removeItem(itemstack);
                    }
                }

            }
        }
    }

    public static float getPowerForTime(int pCharge) {
        float f = (float)pCharge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }


    public static AbstractArrow customArrow(AbstractArrow arrow, ItemStack stack) {
        return arrow;
    }


}
```