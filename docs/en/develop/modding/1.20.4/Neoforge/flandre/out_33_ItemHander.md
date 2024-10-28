---
title: 33 原版提供的能力ItemHander物品处理能力
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 33 原版提供的能力ItemHander物品处理能力 相关教程
image: ./covers/48d60f5288b3399f306b7392b030aa3e7f3b2767.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考

https://boson.v2mcdev.com/capability/simpleusage.html
https://docs.neoforged.net/docs/datastorage/capabilities

## 开始构建能力

这次我们来使用原版给我们提供的ItemHandler处理物品的输入和输出。关于介绍看之前的教程，然后我们这次实现一个垃圾桶，从上方输入，然后只能输入石头，然后删除石头。

常规的方块注册。

```java
public class TrashBlock extends BaseEntityBlock {
    public TrashBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TrashBlockEntity(pPos,pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}

```

方块实体注册

```java
public class TrashBlockEntity extends BlockEntity {
    public TrashBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.TRASH_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
}
```

```java
    public static final Supplier<Block> TRASH_BLOCK = registerBlock("trash_block",TrashBlock::new);

```

```java
    public static final Supplier<BlockEntityType<TrashBlockEntity>> TRASH_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("trash_block_entity", () ->
                    BlockEntityType.Builder.of(TrashBlockEntity::new,
                            ModBlocks.TRASH_BLOCK.get()).build(null));

```

给我们的实体注册的对于的物品处理的能力。我们让provider返回物品处理的API接口，我们自己实现这个API接口。

```java
/**
 * ModEventBus 类是一个静态内部类，用于处理 Mod 事件总线上的事件。
 * 它使用 @Mod.EventBusSubscriber 注解来订阅 Mod 事件总线上的事件。
 */
@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public static class ModEventBus {
    /**
     * registerCapabilities 方法用于注册方块实体的能力。
     * 它使用 @SubscribeEvent 注解来订阅 RegisterCapabilitiesEvent 事件。
     *
     * @param event RegisterCapabilitiesEvent 事件对象
     */
    @SubscribeEvent
    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        // 注册方块实体的物品处理器能力
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.TRASH_BLOCK_ENTITY.get(),
                (myBlockEntity, side) -> {
                    // 如果方块实体的面为上面，则返回一个自定义的物品处理器
                    if (side == Direction.UP) {
                        return new IItemHandler() {
                            /**
                             * 获取物品处理器的槽位数量。
                             *
                             * @return 槽位数量，固定为 1
                             */
                            @Override
                            public int getSlots() {
                                return 1;
                            }

                            /**
                             * 获取指定槽位的物品堆叠。
                             *
                             * @param slot 槽位索引
                             * @return 空的物品堆叠
                             */
                            @Override
                            public @NotNull ItemStack getStackInSlot(int slot) {
                                return ItemStack.EMPTY;
                            }

                            /**
                             * 将物品插入指定槽位。
                             *
                             * @param slot     槽位索引
                             * @param stack    要插入的物品堆叠
                             * @param simulate 是否为模拟插入
                             * @return 插入后剩余的物品堆叠
                             */
                            @Override
                            public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                                // 如果物品不合法，则直接返回原物品堆叠
                                if (!this.isItemValid(slot, stack)) return stack;
                                // 如果是模拟插入，则返回空的物品堆叠
                                if (simulate) {
                                    return ItemStack.EMPTY;
                                } else {
                                    // 复制物品堆叠并减少数量，返回剩余的物品堆叠
                                    stack = stack.copy();
                                    stack.shrink(1);
                                    return stack;
                                }
                            }

                            /**
                             * 从指定槽位提取物品。
                             *
                             * @param slot    槽位索引
                             * @param amount  要提取的数量
                             * @param simulate 是否为模拟提取
                             * @return 空的物品堆叠
                             */
                            @Override
                            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                                return ItemStack.EMPTY;
                            }

                            /**
                             * 获取指定槽位的物品数量限制。
                             *
                             * @param slot 槽位索引
                             * @return 槽位索引值
                             */
                            @Override
                            public int getSlotLimit(int slot) {
                                return slot;
                            }
                            /**
                            * 判断指定槽位是否可以接受给定的物品堆叠。
                            *
                            * @param slot 槽位索引
                            * @param stack 物品堆叠
                            * @return 如果物品为圆石，则返回 true，否则返回 false
                            */
                            @Override
                            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                                return stack.getItem() == Items.COBBLESTONE;
                            }
                            };
                        } else {
                            // 如果方块实体的面不是上面，则返回 null
                            return null;
                        }
                    }
            );
        }
    }

```