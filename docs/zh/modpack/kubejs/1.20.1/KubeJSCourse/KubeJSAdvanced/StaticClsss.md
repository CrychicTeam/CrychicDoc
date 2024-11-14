# 全局静态类
kubejs提供了以下的全局静态类和对应的方法

## Utils
| 方法 | 方法参数 | 方法作用 | 方法返回参数 | 备注 | 
| :--: | :--: | :--: | :--: | :--: |
| toTitleCase(string) | -> | 将传入字符串第一个文本转化为大写 | 处理后的字符串 string | 除了"a", "an", "the", "of", "on", "in", "and", "or", "but" 和 "for" |
| getStat(ResourceLocation) | ~ | ~ | Stat\<ResourceLocation\> | 后续补充 |
| snakeCaseToTitleCase(string) | -> | 将蛇形命名转换为标题大小写 | 处理后的字符串 string |  如a_bc_def 转为 A Bc Def |
| newCountingMap() | - | 获取一个新的CountingMap | CountingMap | - |
| toTitleCase(string,bool) | -> | 设置会true后将上面除了的内容也会进行大写 | 理后的字符串 string | - |
| parseDouble(any,number) | -> | 将第一个参数转化为Double类型，如果失败了，则返回第二个数 | number | - |
| getRandom() | - | 获取Random类 | Random | - |
| newList() | - | 获取一个列表 | List\<T\> | - |
| rollChestLoot(ResourceLocation) | 战利品表id | 获取指定战利品表里的战利品数组 | List\<ItemStack\> | - |
| newRandom(number) | -> | 用一个数字当作种子生成一个Random类 | Random | - |
| getRegistryIds(ResourceLocation) | -> | 获取指定注册表里的的所有id | List<ResourceLocation\> | - |
| emptyList() | - | 获取一个不可变的空列表? | List<T> | - |
| getSystemTime() | - | 获取当前系统时间，以毫秒为单位 | number | - |
| supplyAsync(Supplier<any\>) | ~ | ~ | CompletableFuture<any\> | 后续补充 |
| id(string,string) | -> | 将字符串转为ResourceLocation | ResourceLocation | - |
| lazy(Supplier<T\>) | ~ | ~ | Lazy<T\> | ? |
| isWrapped(any) | -> | 判断传入对象是否为WrappedJS | boolean | - |
| snakeCaseToCamelCase(string) | -> | 将蛇形命名转化为驼峰命名 | 处理后的字符串 string | 如 a_bc_def 转为 aBcDef |
| findCreativeTab(ResourceLocation) | id | 获取该id的所在创造物品栏 | CreativeModeTab | - |
| emptyMap() | - | 获取一个空的不可变的map? | Map<K, V\> | - |
| expiringLazy(Supplier<T\>,number) | ~ | ~ | Lazy<T> | 后续补充 | 
| getSound(ResourceLocation) | id | 从id中获取SoundEvent | SoundEvent | - |
| randomOf(Random,Collection<any\>) | ~ | 使用传入的参数从列表中获取随机对象? | any | ? |
| newMap() | - | 获取一个map | Map<any, any> | - |
| getRegistry(ResourceLocation) | id | 通过id获取对应的注册信息 | RegistryInfo<any> | - |
| particleOptions(any) | ~ | ~ | ParticleOptions | - |
| copy(any) | -> | 复制一份传入的对象，如果不可以则返回本身 | any | - |
| regex(string,number) | string pattern / number flags | ~ | Pattern | - |
| id(string) | -> | 将字符串直接包装成ResourceLocation | ResourceLocation | - |
| regex(any) | -> | ~ | Pattern | - |
| runAsync(Runnable) | -> | ~ | CompletableFuture<void> | - |
| parseBlockState(any) | -> | 从输入内容中解析方块状态可能会抛出无效输入 | BlockState | - |
| queueIO(Runnable) | -> | 立即在try-catch块中运行传递的可运行函数，并在它抛出异常时记录异常? | void | - |
| parseInt(any,number) | -> | 将第一个参数转化为整数，如果失败则返回第二个参数 | number | - |
| getServer() | - | 获取游戏服务端，如果是在没有服务器的地方调用则为null(startup和client) | MinecraftServer | - |
| rollChestLoot(ResourceLocation,Entity) | 战利品表id，触发实体 | 用指定实体生成一个战利品表物品列表 | List<ItemStack> | 不一定是箱子 |

## JsonIO
| 方法 | 方法参数 | 方法作用 | 方法返回参数 | 备注 | 
| :--: | :--: | :--: | :--: | :--: |
| readJson(path) | 路径字符串 | 读取指定路径的文件 | JsonElement | 文件必须为json |
| toPrettyString(JsonElement) | -> | 将json转化为字符串 | string | - |
| getJsonHashString(JsonElement) | -> | 获取json的hash值 | string | - |
| toObject(JsonElement) | -> | 将json转化为对象??? | any | 后续测试 |
| primitiveOf(any) | ? | ? | JsonPrimitive | 后续测试 |
| readString(path) | 路径 | 读取指定路径的文件 | string | 读取为string格式 |
| writeJsonHash(DataOutputStream,JsonElement) | ? | ~ | void | 后续测试 |
| parseRaw(string) | ? | ? | JsonElement | 后续测试 |
| write(path,JsonObject) | -> | 将json对象写入到指定路径的文件里 | void | 一定要是json对象，不能是数组 |
| read(path) | -> | 读取指定路径的文件 | Map<any,any> | 建议使用readJson |
| toArray(JsonElement) | -> | 将JsonElement转为JsonArray | JsonArray | - |
| parse(string) | -> | ? | any | 后续测试 |
| toPrimitive(JsonElement) | -> | ? | any | 后续测试 |
| copy(JsonElement) | -> | 复制一份JsonElement | JsonElement | - |
| toString(JsonElement) | -> | 将json转为字符串 | string | - |
| getJsonHashBytes(JsonElement) | -> | 将json转为hashByte | number[] | - |
| of(any) | ? | ? | JsonElement | - |

## BlockPos
## Block
## Vec3d
## KMath
## RotationAxis
## ResourceLocation
## Items
## SoundType
## Stats
## Duration
## OutputItem
## Fluid
## InputItem
## DamageSource
## Platform
## Vec3f
## Vec4f
## Notification
## Quaternionf
## JavaMath
## BlockProperties
## Vec3i
## Matrix3f
## Matrix4f
## Blocks
## Component