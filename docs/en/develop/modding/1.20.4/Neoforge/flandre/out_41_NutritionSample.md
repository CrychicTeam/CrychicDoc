---
title: 41 饥渴值例子
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 41 饥渴值例子 相关教程
image: ./covers/237c82845e473225f9027f52d8645fed71473497.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考


## 给玩家添加口渴度

这次我们使用之前讲解的capabilitiy能力系统，HUD，网络发包，热键注册等功能给玩家添加一个口渴度的系统，并且借助HUD功能将这个数值渲染到屏幕上。通过按键O增加口渴度，随时间口渴度逐渐减少

偷懒了。没写能力的接口，直接写了实现类。

```java

public class PlayerThirst {
    // 玩家当前的口渴值
    private int thirst;
     // 最小和最大口渴值的常量
    private final int MIN_THIRST = 0;
    private final int MAX_THIRST = 10;
    // 获取当前的口渴值
    public int getThirst() {
        return thirst;
    }

 // 增加口渴值，但不能超过最大值
    public void addThirst(int add) {
        this.thirst = Math.min(thirst + add, MAX_THIRST);
    }

    // 减少口渴值，但不能低于最小值
    public void subThirst(int sub) {
        this.thirst = Math.max(thirst - sub, MIN_THIRST);
    }

    // 从另一个 PlayerThirst 对象复制口渴值
    public void copyFrom(PlayerThirst source) {
        this.thirst = source.thirst;
    }

    // 将口渴值保存到 NBT 数据中
    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("thirst", thirst);
    }

    // 从 NBT 数据中加载口渴值
    public void loadNBTData(CompoundTag nbt) {
        thirst = nbt.getInt("thirst");
    }
}

```

能力的提供者capabilityProvider，以及数据的持久化

```java


public class PlayerThirstProvider implements ICapabilityProvider<Player,Void, PlayerThirst>, INBTSerializable<CompoundTag> {
// 存储玩家口渴值的实例
    private PlayerThirst thirst = null;

    // 创建 PlayerThirst 实例的私有方法
    private PlayerThirst createPlayerThirst() {
        if (this.thirst == null) {
            this.thirst = new PlayerThirst();
        }
        return this.thirst;
    }

    // 实现 INBTSerializable 接口的 serializeNBT 方法，用于将口渴值保存到 NBT 数据中
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerThirst().saveNBTData(nbt);
        return nbt;
    }

    // 实现 INBTSerializable 接口的 deserializeNBT 方法，用于从 NBT 数据中加载口渴值
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerThirst().loadNBTData(nbt);
    }

    // 实现 ICapabilityProvider 接口的 getCapability 方法，用于获取玩家的口渴值实例
    @Override
    public @Nullable PlayerThirst getCapability(Player player, Void context) {
        return this.createPlayerThirst();
    }
}

```

能力的创建

```java

public class ModCapabilities {

    public static final EntityCapability<PlayerThirst,Void> PLAYER_THIRST_HANDLER =
            EntityCapability.createVoid(new ResourceLocation(ExampleMod.MODID,"player_thirst_handler"),
                    PlayerThirst.class);

}

```

给玩家实体添加能力。

```java

    @Mod.EventBusSubscriber(modid = ExampleMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBus{
        @SubscribeEvent
        private static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.registerEntity(ModCapabilities.PLAYER_THIRST_HANDLER,
                    EntityType.PLAYER,
                    new PlayerThirstProvider());
        }
    }
```

由于按键只能在客户端的上，所以喝水的增加口渴值的逻辑需要发包处理，这里添加一个数据包。

```java
// 需要同步的数据只有thirst
public record ThirstData(int thirst) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(ExampleMod.MODID,"thirst_data");


    public ThirstData(final FriendlyByteBuf buf){
        this(buf.readInt());
    }

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(thirst());
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}

```

如果是客户端收到了发包怎么处理数据包

```java

public class ClientPayloadHandler {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

    public static ClientPayloadHandler getInstance() {
        return INSTANCE;
    }
    // 给我们的在客户端存储thirst的地方赋值
    public void handleThirstData(final ThirstData data,final PlayPayloadContext context){
        context.workHandler().submitAsync(()->{
            ClientPlayerThirstData.set(data.thirst());
        }).exceptionally(e->{
            context.packetHandler().disconnect(Component.translatable("my_mod.networking.failed", e.getMessage()));
            return null;
        });
    }

}


```

服务端接受到数据包怎么处理

```java

public class ServerPayloadHandler {
    private static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ServerPayloadHandler getInstance() {
        return INSTANCE;
    }

    public void handleThirstData(final ThirstData data, final PlayPayloadContext context){
        context.workHandler().submitAsync(()->{
            context.player().ifPresent(player -> {
                // 玩家应该是服务端的玩家
                if (player instanceof ServerPlayer serverPlayer){
                    // 服务端的level
                    ServerLevel level = (ServerLevel) player.level();
                    // 判断玩家附近是否有水
                    if(hasWaterAroundThem(player,level,2)){
                        // 播放喝水的声音
                        level.playSound(null, player.getOnPos(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS,
                                0.5F, level.random.nextFloat() * 0.1F + 0.9F);
                        // 从玩家身上获得口渴的能力，如果存在就增加数值，并发包同步数据
                        Optional.ofNullable(player.getCapability(ModCapabilities.PLAYER_THIRST_HANDLER)).ifPresent(thirst->{
                            thirst.addThirst(1);

                            player.sendSystemMessage(Component.literal("Current Thirst " + thirst.getThirst())
                                    .withStyle(ChatFormatting.AQUA));

                            PacketDistributor.PLAYER.with(serverPlayer).send(new ThirstData(thirst.getThirst()));
                        });

                    }else{
                        // 同理
                        Optional.ofNullable(player.getCapability(ModCapabilities.PLAYER_THIRST_HANDLER)).ifPresent(thirst -> {
                            player.sendSystemMessage(Component.literal("Current Thirst " + thirst.getThirst())
                                    .withStyle(ChatFormatting.AQUA));
                            PacketDistributor.PLAYER.with(serverPlayer).send(new ThirstData(thirst.getThirst()));
                        });
                    }
                }
            });
        }).exceptionally(e->{
            context.packetHandler().disconnect(Component.translatable("my_mod.networking.failed", e.getMessage()));
            return null;
        });
    }
    // 判断玩家附近是否有水方块
    private boolean hasWaterAroundThem(Player player, ServerLevel level, int size) {
        return level.getBlockStates(player.getBoundingBox().inflate(size))
                .filter(state -> state.is(Blocks.WATER)).toArray().length > 0;
    }
}
```

网络包的注册，以及对网络包的处理。

```java
@Mod.EventBusSubscriber(modid = ExampleMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class Networking {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(ExampleMod.MODID);
        registrar.play(ThirstData.ID,ThirstData::new, handler ->
                handler.client(ClientPayloadHandler.getInstance()::handleThirstData)
                        .server(ServerPayloadHandler.getInstance()::handleThirstData));
    }

}
```

我们客户端上的辅助的类，用于存储playerThirst数值，并给HUD提供渲染支持。

```java
@OnlyIn(Dist.CLIENT)
public class ClientPlayerThirstData {
    private static int playerThirst;

    public static void set(int thirst) {
        ClientPlayerThirstData.playerThirst = thirst;
    }

    public static int getPlayerThirst() {
        return playerThirst;
    }
}

```
玩家安下O键时候，给服务器发包，服务器处理数据包完成喝水的过程。
```java
@Mod.EventBusSubscriber(modid = ExampleMod.MODID,value = Dist.CLIENT)
public class ForgeClientEventHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if(KeyBinding.DRINKING_KEY.consumeClick()){
//            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Press the key!"));
            PacketDistributor.SERVER.noArg().send(new ThirstData(ClientPlayerThirstData.getPlayerThirst()));
        }
    }
}
```

玩家每隔一段时间就数值-1，并发包同步数据

```java
    @Mod.EventBusSubscriber(modid = ExampleMod.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents{

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if(event.side == LogicalSide.SERVER) {
                Optional<PlayerThirst> optionalPlayerThirst = Optional.ofNullable(event.player.getCapability(ModCapabilities.PLAYER_THIRST_HANDLER));
                optionalPlayerThirst .ifPresent(thirst -> {
                    if(thirst.getThirst() > 0 && event.player.getRandom().nextFloat() < 0.005f) { // Once Every 10 Seconds on Avg
                        thirst.subThirst(1);
                        PacketDistributor.PLAYER.with((ServerPlayer) event.player).send(new ThirstData(thirst.getThirst()));
                        event.player.sendSystemMessage(Component.literal("Subtracted Thirst"));
                    }
                });
            }
        }
    // 当玩家刚刚加入世界时候同步数据
        @SubscribeEvent
        public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
            if(!event.getLevel().isClientSide()) {
                if(event.getEntity() instanceof ServerPlayer player) {
                    Optional<PlayerThirst> optionalPlayerThirst = Optional.ofNullable(player.getCapability(ModCapabilities.PLAYER_THIRST_HANDLER));
                    optionalPlayerThirst.ifPresent(thirst -> {
                        PacketDistributor.PLAYER.with((ServerPlayer) player).send(new ThirstData(thirst.getThirst()));
                    });
                }
            }
        }

    }
```

使用HUD绘制水瓶

```java

public class ThirstHud {
    // 有水和无水的水瓶
    private static final ResourceLocation FILLED_THIRST = new ResourceLocation(ExampleMod.MODID,
            "textures/gui/filled_thirst.png");
    private static final ResourceLocation EMPTY_THIRST = new ResourceLocation(ExampleMod.MODID,
            "textures/gui/empty_thirst.png");
    public static final IGuiOverlay HUD_THIRST = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        int x = screenWidth / 2;
        int y = screenHeight;
        // 绘制无水的水瓶
        for(int i = 0; i < 10; i++) {
            guiGraphics.blit(EMPTY_THIRST,x - 94 + (i * 9), y - 54,90,0,0,12,12,
                    12,12);
        }
        // 绘制有水的水瓶
        // ClientPlayerThirstData我们提供的client辅助的类存储数值
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

注册HUD。

```java
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ModClientEventHandler {
    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll(new ResourceLocation(ExampleMod.MODID,"thirst_hud"), ThirstHud.HUD_THIRST);
    }
}
```

好了进入游戏看看逻辑把，应该绘制出来了无水的水瓶，在水方块附近按下o，看看是不是增加了一个有水的水瓶，推出游戏在进入看看是不是数据是不是可以持久化。以及等一段时间看看瓶子会不会减少，对应了tick事件的方法。