---
title: 27 网络
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 27 网络 相关教程
image: ./covers/a8e4dc1a735d0df9414275e16cd2e766e8abb3df.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考

https://boson.v2mcdev.com/networking/custompack.html
https://github.dev/illusivesoulworks/comforts
https://github.dev/Tschipp/CarryOn
https://docs.neoforged.net/docs/networking/payload

## 自定义网络包

在之前的方块实体的数据同步中，我们介绍了一种数据同步的方法，用于方块实体的数据不同，但是问题是同步的数据量小，只能服务器往客户端发送数据，并且只能用在方块实体上。
这次我们介绍一种更加方便通用的方法用于数据同步，这就是自定义数据包。

网络传输是字节流数据，所以我们需要一种方法能将数据转为字节，然后在网络上传输，幸运的是MC已经提供了这样的方法，我们子需要定义自己的网路包，以及处理网络包就可以了。

网络传输的流程大概如下
```java
                         通过本地、局域网传输或者因特网传输
构建数据包=>序列化成字节流 ===============================> 反序列化成实例 => 实现操作
```

> 内容来之https://boson.v2mcdev.com/networking/custompack.html

有两部分是我们要处理的，第一个是定义网络包，第二个实现接受包的操作。

我们先看看，我们的数据包是怎么定义的。

```java

public record MyData(String message, int age) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(ExampleMod.MODID,"my_data");

    public MyData(final FriendlyByteBuf buf){
        this(buf.readUtf(),buf.readInt());
    }

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeUtf(message());
        pBuffer.writeInt(age());
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}

```
这样的一个记录，字段有一个string的 message和一个int的age，我们通过网络传输这个message和age。

所以我们需要一个decode方法和一个encode方法，即将数据序列化为字节流，和将字节反序列化为数据。而FriendlyByteBuf这个类为我们提供了简单方便的方法将数据处理为字节，以及将字节处理为数据，而这个类中的MyData构造方法就是将字节流处理为我们的数据，这里返回了一个新的MyData的实例。而wirte方法就是将我们的数据转为字节流。只需要写入到FriendlyByteBuf中即可，FriendlyByteBuf提供很多方法，例如这里的写入字节流，和整数，也有其他的方法，自己看即可。

这个ID是我们自定义数据包要实现CustomPacketPayload接口后必须要重写的方法。返回一个ID即可，ID书写方式

```java
    public static final ResourceLocation ID = new ResourceLocation(ExampleMod.MODID,"my_data"); /// 你的modid和包名
```
下面我们来看看当我们接受到数据包时候怎么处理。

第一情况是服务端发包。客户端接受处理数据包。

我们写了这样的一个类，其中使用了单例的设计模式，LOGGER是我们打印信息使用的非必要。而handleData是我们处理数据包的函数，其中第一个参数就是处理的对应的数据包的类型。第二个参数是网路的上下文，包含了网络的发包的人以及其他的信息。

当你的数据要在网络的线程中处理，那么你的代码写在workhandler之外，如果你的数据要在主线程处理，写在submitAsync内。对于异常的处理写在exceptionally内。

```java

public class ClientPayloadHandler {
    // 此代码用于打印信息的。非必要
    private static final Logger LOGGER = LogUtils.getLogger();

    // 实现单例
    private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

    public static ClientPayloadHandler getInstance() {
        return INSTANCE;
    }

    public void handleData(final MyData data, final PlayPayloadContext context) {
        // Do something with the data, on the network thread
        // 在network 线程中对data数据做一些处理的话，代码写在这里。

        // Do something with the data, on the main thread
        // 在Main 线程里面做一些什么，代码西在下面
        context.workHandler().submitAsync(() -> {
            // 写在这里
                 LOGGER.info(data.message());
                })
                .exceptionally(e -> {
                    // 处理异常
                    // Handle exception
                    context.packetHandler().disconnect(Component.translatable("my_mod.networking.failed", e.getMessage()));
                    return null;
                });
    }

}
```

第二种情况是客户端发包，服务端接受处理，大体上是类似的这里就不过多做介绍了。

```java

public class ServerPayloadHandler {
    private static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ServerPayloadHandler getInstance() {
        return INSTANCE;
    }

    public void handleData(final MyData data, final PlayPayloadContext context) {
        // Do something with the data, on the network thread

        // Do something with the data, on the main thread
        context.workHandler().submitAsync(() -> {
                    LOGGER.info(data.message());
                })
                .exceptionally(e -> {
                    // Handle exception
                    context.packetHandler().disconnect(Component.translatable("my_mod.networking.failed", e.getMessage()));
                    return null;
                });
    }
}
```
那么我们的网络包和处理的数据的两个handler应该怎么注册呢？

下面是他们的注册的方法。通过IPayloadRegistrar的play方法进行注册。其中的第一个参数是网络包的ID，第二个参数是将字节流反序列化为数据包对象的方法。第三个参数是对应的数据包的处理方法。我们分别设置了客户端的处理方法和服务端的处理方法，其中的方法引用就是我们写的handlerData，其中getInstance就是获得单例。

```java

@Mod.EventBusSubscriber(modid = ExampleMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class Networking {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(ExampleMod.MODID);
        registrar.play(MyData.ID,MyData::new,handler ->
                handler.client(ClientPayloadHandler.getInstance()::handleData)
                        .server(ServerPayloadHandler.getInstance()::handleData));
    }

}

```

好了让我们再来看看怎么使用这个网络包把。这里我们写一个Item，这个item右键后会从客户端向服务器发包，其中的message是From client，同时也会从服务器想客户端发包，其中message的内容是From server，数据包接受后就打屏幕上打印出来。

其中提的提一下的就是服务器往客户端发包，我们可以指定具体的玩家，通过with指定，如果是所有玩家使用ALL，详细见PacketDistributor类下的内容。

```java

public class MessageItem extends Item {
    public MessageItem() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pLevel.isClientSide){
            PacketDistributor.SERVER.noArg().send(new MyData("From client",2));
        }
        if(!pLevel.isClientSide){
            PacketDistributor.PLAYER.with((ServerPlayer) pPlayer).send(new MyData("From server",2));
        }
        return super.use(pLevel,pPlayer,pUsedHand);
    }
}
```

其中的Render thread是客户端线程，Server thread是服务端线程。

```cmd
[18:37:30] [Server thread/INFO] [ne.fl.ex.ne.ServerPayloadHandler/]: From client
[18:37:30] [Render thread/INFO] [ne.fl.ex.ne.ClientPayloadHandler/]: From server
[18:37:31] [Server thread/INFO] [ne.fl.ex.ne.ServerPayloadHandler/]: From client
[18:37:31] [Render thread/INFO] [ne.fl.ex.ne.ClientPayloadHandler/]: From server
[18:37:31] [Server thread/INFO] [ne.fl.ex.ne.ServerPayloadHandler/]: From client
```
