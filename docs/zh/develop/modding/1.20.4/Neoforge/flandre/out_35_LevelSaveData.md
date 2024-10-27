---
title: 35 LevelSaveData
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 35 LevelSaveData 相关教程
image: ./covers/48d60f5288b3399f306b7392b030aa3e7f3b2767.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考

https://boson.v2mcdev.com/worldsaveddata/example.html
https://docs.neoforged.net/docs/datastorage/saveddata

## SaveData

阅读前，请先自己大概阅读下`https://docs.neoforged.net/docs/datastorage/saveddata` 内容，大概了解下。

好了，我们正式开始，SaveData类是我的世界提供的一种维度级别的数据保存的方式，这种存储数据可以共享，并且可以跨纬度保存和获取到的，并且数据不存在存档文件夹中，而是在世界文件夹下面。

```
./<level_folder>/DIM-1/data/example.dat
```

这次我们介绍怎么使用这个类，我们创建一个存储物品和取出物品的方块，这个方块是谁的都可以存取和取出的（共享）。


```java
/**
 * ModLevelSaveData类用于管理和持久化游戏中的物品堆栈数据。
 * 它继承自SavedData类，用于将数据保存到硬盘并在需要时加载。
 */
public class ModLevelSaveData extends SavedData {
    /**
     * 保存数据的名称常量。
     * 注意这个NAME应该保持是唯一的，不要重复。
     */
    public static final String NAME = "mod_level_save_data";
        /**
     * 用于存储物品堆栈的栈结构。
     */
    private final Stack<ItemStack> itemStacks = new Stack<>();
    /**
     * 创建一个新的ModLevelSaveData实例。
     *
     * @return 新创建的ModLevelSaveData实例。
     */
    public static ModLevelSaveData create() {
        return new ModLevelSaveData();
    }
    /**
     * 将物品堆栈放入栈中。
     *
     * @param item 要放入栈中的物品堆栈。
     */
    public void putItem(ItemStack item) {
        itemStacks.push(item);
        setDirty();
    }
    /**
     * 从栈中获取物品堆栈。
     *
     * @return 如果栈为空，则返回空气物品堆栈；否则返回栈顶的物品堆栈。
     */
    public ItemStack getItem() {
        if (itemStacks.isEmpty()) {
            return new ItemStack(Items.AIR);
        }
        setDirty();
        return itemStacks.pop();
    }
    /**
     * 将ModLevelSaveData的数据保存到CompoundTag中。
     *
     * @param pCompoundTag 要保存数据的CompoundTag。
     * @return 保存了数据的CompoundTag。
     */
    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        ListTag listTag = new ListTag();
        itemStacks.forEach((stack) -> {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.put("itemstack", stack.getTag());
            listTag.add(compoundTag);
        });
        pCompoundTag.put("list", listTag);
        return pCompoundTag;
    }
  /**
     * 从CompoundTag中加载ModLevelSaveData的数据。
     *
     * @param nbt 包含数据的CompoundTag。
     * @return 加载了数据的ModLevelSaveData实例。
     */
    public ModLevelSaveData load(CompoundTag nbt) {
        ModLevelSaveData data = this.create();
        ListTag listNBT = (ListTag) nbt.get("list");
        if (listNBT != null) {
            for (Tag value : listNBT) {
                CompoundTag tag = (CompoundTag) value;
                ItemStack itemStack = ItemStack.of(tag.getCompound("itemstack"));
                itemStacks.push(itemStack);
            }
        }
        return data;
    }
/**
     * 从CompoundTag中解码ModLevelSaveData的数据。
     *
     * @param tag 包含数据的CompoundTag。
     * @return 解码后的ModLevelSaveData实例。
     */
    public static ModLevelSaveData decode(CompoundTag tag){
        ModLevelSaveData modLevelSaveData = ModLevelSaveData.create();
        modLevelSaveData.load(tag);
        return modLevelSaveData;
    }
/**
     * 获取指定世界的ModLevelSaveData实例。通过这个方法获得对应的data
     *
     * @param worldIn 要获取数据的世界。
     * @return 与指定世界关联的ModLevelSaveData实例。
* @throws RuntimeException 如果尝试从客户端世界获取数据，则抛出运行时异常。
     * **/
    public static ModLevelSaveData get(Level worldIn) {
        if (!(worldIn instanceof ServerLevel)) {
            throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
        }
        ServerLevel world = worldIn.getServer().getLevel(ServerLevel.OVERWORLD);
        DimensionDataStorage dataStorage = world.getDataStorage();
        return dataStorage.computeIfAbsent(new Factory<ModLevelSaveData>(ModLevelSaveData::create, ModLevelSaveData::decode), ModLevelSaveData.NAME);
    }
}
```

对应的方块的内容。

```java
/**
 * DataSaveBlock是一个自定义的方块类，用于与玩家进行交互并保存和读取物品数据。
 * 它继承自Block类，具有与石头方块相同的属性。
 */
public class DataSaveBlock extends Block {
    public DataSaveBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
    }
  /**
     * 当玩家使用该方块时触发的交互逻辑。这里如果是空手就从DATA中获得物品，如果手持物品就放入到DATA中去。
     *
     * @param pState 方块的状态。
     * @param pLevel 方块所在的世界。
     * @param pPos 方块的位置。
     * @param pPlayer 与方块交互的玩家。
     * @param pHand 玩家使用的手（主手或副手）。
     * @param pHit 玩家点击方块的碰撞结果。
     * @return 交互的结果，表示交互是否成功。
     */
    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
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
            }
        }
        return InteractionResult.SUCCESS;
    }
}

```