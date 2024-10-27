---
title: 51  粒子
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 51  粒子 相关教程
image: ./covers/d1a82a6a5dcb945c5753e847c86c6e1bccd85f80.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考
https://boson.v2mcdev.com/paticles/intro.html

## 粒子

需要说明的一点是粒子效果依旧是仅在客户端有的效果，服务器 并没有对于的效果，不过服务器端是可以通知客户端在哪里渲染粒子的。所以这其中自然存在服务器给客户端的发包操作，那么当然就需要你去处理你自己粒子的有些参数，而对于粒子效果是非常灵活的，相信你在B站应该见过各种的粒子的视频，他们是通过命令方块等实现的，说明了粒子的指令是灵活的，这样给你代码编写带来了一些问题，你得来处理这些参数的设置来实现灵活的粒子效果，包括不限于，移动的速度，颜色，大小等等。

这里我们参考了boson的粒子的文章，添加了一个新的粒子，并且允许你通过命令的方式设置粒子的速度，颜色和大小，讲解怎么添加粒子，怎么处理命令生成粒子。

首先我们创建一个粒子类，粒子的基类是particle，不过一般来说我们常用的是TextureSheetParticle这个类，其他的类你可以自行查看。去看继承树就可以了。

TextureSheetParticle这个类表示使用一张材质的图片作为粒子效果

```java

public class ExampleParticle extends TextureSheetParticle {
    // 除了父类要求的参数,这里我们还增加了几个参数,例如速度，颜色，大小等
    // 方便我们自定义我们自己的粒子效果,
    // 我们使用vector4i表示颜色
    public ExampleParticle(ClientLevel pLevel, double pX, double pY, double pZ, Vector3d speed, Vector4i color, float diameter) {
        super(pLevel, pX, pY, pZ, speed.x,speed.y,speed.z);
        this.lifetime = 100; // 这是粒子的存在的时间
        this.xd = speed.x; // 这里的是x,y,z方向的速度
        this.yd = speed.y; // 虽然父类中也有设置,但是父类会加上一个随机的速度
        this.zd = speed.z; // 我们并不希望这样做,就重新赋值
        this.setColor(color.x/255F, color.y/255F, color.z/255F);
        this.setAlpha(color.w/255F); // opengl中的颜色标准值是0-1,所以这里标准化
        final float PARTICLE_SCALE_FOR_ONE_METRE = 0.5F; // 这是我们自定义的粒子大小,单位是米
        this.quadSize = PARTICLE_SCALE_FOR_ONE_METRE * diameter;// 放大的倍数
        this.hasPhysics = true; // 粒子是否可以被碰撞
    }
        // 这里指明渲染是半透明
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}

```

像方块实体一样,对于一个方块实体有对于的一个type,这里也是,我们需要添加一个粒子的type.

你可以去看原版的粒子type你会发现他们继承了一个叫做simpleParticleType的类,其中有是实现一些功能,如果你的粒子不需要通过命令行的方式自定义的话可以使用这个作为你的type,不过这里我们希望我们的命令是可以自定义粒子的效果的,所以我们继承ParticleType类并且实现ParticleOptions接口.

```java
public class ExampleParticleType extends ParticleType implements ParticleOptions {
    // 我们需要存储的这几个参数,并且使用这几个参数创建的对于的粒子效果
    // 这几个参数从哪里来呢?,答案是从命令行的输入来
    private final Vector3d speed;
    private final Vector4i color;
    private final float diameter;
    // 我们需要一个序列化和反序列化的东西,
    // 1. 目的是为了从命令行读取str的颜色,速度,等内容.
    // 2 是可以将将网络的发送的数据包中读取对应的颜色,速度.这里说的是客户端接受
    public static final ParticleOptions.Deserializer<ExampleParticleType> DESERIALIZER = new ParticleOptions.Deserializer<ExampleParticleType>() {
        // 第一个就是处理命令中的字符串
        // 输入的命令是这样的形式.
        // /particle 粒子 0 0 0 0 0 255 100 3
        // 其中的是 空格0空格0空格0空格0空格0空格255空格100空格3
        @Override
        public ExampleParticleType fromCommand(ParticleType<ExampleParticleType> pParticleType, StringReader pReader) throws CommandSyntaxException {
            final int MIN_COLOUR = 0; 
            final int MAX_COLOUR = 255;
            pReader.expect(' '); // 处理空格
            double speedX = pReader.readDouble(); // 读取第一个0 
            pReader.expect(' '); // 处理空格
            double speedY = pReader.readDouble(); //....
            pReader.expect(' ');
            double speedZ = pReader.readDouble();
            pReader.expect(' '); 
            int red = Math.clamp(pReader.readInt(), MIN_COLOUR, MAX_COLOUR);// 这里我们读取颜色,并且保证颜色是合法的,限制在0-255之间
            pReader.expect(' ');
            int green = Math.clamp(pReader.readInt(), MIN_COLOUR, MAX_COLOUR);
            pReader.expect(' ');
            int blue = Math.clamp(pReader.readInt(), MIN_COLOUR, MAX_COLOUR);
            pReader.expect(' ');
            int alpha = Math.clamp(pReader.readInt(), 1, MAX_COLOUR);
            pReader.expect(' ');
            float diameter = pReader.readFloat();
            return new ExampleParticleType(new Vector3d(speedX, speedY, speedZ), new Vector4i(red, green, blue, alpha), diameter);
        }

        @Override
        public ExampleParticleType fromNetwork(ParticleType<ExampleParticleType> pParticleType, FriendlyByteBuf pBuffer) { // 这里是处理网络数据包的
            final int MIN_COLOUR = 0;
            final int MAX_COLOUR = 255;
            double speedX = pBuffer.readDouble();
            double speedY = pBuffer.readDouble();
            double speedZ = pBuffer.readDouble();
            int red = Math.clamp(pBuffer.readInt(), MIN_COLOUR, MAX_COLOUR);
            int green = Math.clamp(pBuffer.readInt(), MIN_COLOUR, MAX_COLOUR);
            int blue = Math.clamp(pBuffer.readInt(), MIN_COLOUR, MAX_COLOUR);
            int alpha = Math.clamp(pBuffer.readInt(), 1, MAX_COLOUR);
            float diameter = pBuffer.readFloat();
            return new ExampleParticleType(new Vector3d(speedX, speedY, speedZ), new Vector4i(red, green, blue, alpha), diameter);
        }
    };
    // 构造方法
    public ExampleParticleType(Vector3d speed, Vector4i color, float diameter) {
        // super 第一个参数表示粒子不在人的视野范围内的时候是否渲染,可以选择不渲染
        // 第二个参数是我们的DESERIALIZER,帮我我们处理序列化的
        super(false, ExampleParticleType.DESERIALIZER);
        this.speed = speed;
        this.color = color;
        this.diameter = diameter;
    }
    // 返回粒子的type等会我们注册
    @Override
    public ExampleParticleType getType() {
        return ModParticleType.EXAMPLE_PARTICLE_TYPE.get();
    }
    // 服务端发包要写的数据..
    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeDouble(this.speed.x);
        pBuffer.writeDouble(this.speed.y);
        pBuffer.writeDouble(this.speed.z);
        pBuffer.writeInt(this.color.x());
        pBuffer.writeInt(this.color.y());
        pBuffer.writeInt(this.color.z());
        pBuffer.writeInt(this.color.w());
        pBuffer.writeFloat(this.diameter);
    }
    // 写入成string
    @Override
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %i %i %i %i %.2d %.2d %.2d",
                this.getType().toString(), diameter, color.x(), color.y(), color.z(), color.w(), speed.x(), speed.y(), speed.z());
    }
     // codec,并不会用到他,不过还是给它返回一个数值吧
    @Override
    public Codec<ExampleParticleType> codec() {
        return Codec.unit(new ExampleParticleType(new Vector3d(0, 0, 0), new Vector4i(0,0,0,0), 0));
    }
    // 这里是我们自定义的粒子效果,我们需要返回一个粒子的provider,
    public Vector3d getSpeed() {
        return speed;
    }
    // 颜色,大小
    public Vector4i getColor() {
        return color;
    }

    public float getDiameter() {
        return diameter;
    }
}

```
我们来注册我们的粒子type吧.

```java

public class ModParticleType {
    public static final DeferredRegister <ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, ExampleMod.MODID);
    public static final Supplier<ExampleParticleType> EXAMPLE_PARTICLE_TYPE = register("example_particle_type", () -> new ExampleParticleType(new Vector3d(0,0,0),new Vector4i(0,0,0,0),0));

    public static <T extends ParticleType<?>> DeferredHolder<ParticleType<?>, T> register(String name, Supplier<T> particleType){
        return PARTICLE_TYPES.register(name, particleType);

    }
    // 别忘了注册到总线
    public static void register(IEventBus eventBus){
        PARTICLE_TYPES.register(eventBus);
    }


}
```

好了,我们注册了粒子type,但是我们还需要一个provider,使用个provider来创建客户端的粒子效果,并且把这个provider放在particleEngine上.

```java
// 我们来创建这样的类
public class ExampleParticleProvider implements ParticleProvider<ExampleParticleType> {
    // 存储一系列的纹理图
    private final SpriteSet sprites;

    // 构造方法
    // 传入对应的图片
    public ExampleParticleProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }
    // 创建粒子
    @Nullable
    @Override
    public Particle createParticle(ExampleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        ExampleParticle exampleParticle = new ExampleParticle(pLevel, pX, pY, pZ, pType.getSpeed(),pType.getColor(),pType.getDiameter());
        // 随机选择一张图片作为纹理图
        exampleParticle.pickSprite(this.sprites);
        // 返回创建好的粒子
        return exampleParticle;
    }
}
```

将我们的provider注册到particleEngine上.


```java
// neoforge为我们提供了对应的方法
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleFactoryRegistry {
    @SubscribeEvent
    public static void onParticleFactoryRegistration(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleType.EXAMPLE_PARTICLE_TYPE.get(), ExampleParticleProvider::new);
    }
}

```

下面是你的粒子的json文件和纹理的图片存放的位置

```java
.
├── META-INF
│   └── mods.toml
├── assets
│   └── examplemod
│       ├── blockstates
│       ├── lang
│       ├── models
│       ├── particles
│       │   └── example_particle_type.json
│       ├── sounds
│       ├── sounds.json
│       └── textures
│           ├── block
│           ├── entity
│           ├── gui
│           ├── item
│           └── particle
│               └── example_particle.png
├── data
└── pack.mcmeta
```

其中你的joson文件如下,描述一系列的纹理图片,这里只有一张

```json
{
  "textures": [
    "examplemod:example_particle"
  ]
}
```


进入游戏中输入下面的命令来看看你的粒子效果吧
`/particle examplemod:example_particle 0 0 0 0 0 255 100 3`
其中的 0 0 0 0 0 255 100 3 是你的粒子的速度,颜色,大小,分别对应x,y,z,red,green,blue,alpha,diameter.

代码中添加粒子效果可以使用world.addParticle()方法.可以查看原版是怎么使用的.