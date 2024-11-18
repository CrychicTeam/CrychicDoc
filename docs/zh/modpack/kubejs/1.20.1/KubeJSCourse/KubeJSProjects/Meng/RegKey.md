# 按键注册和使用
本章涉及内容：配方检测、物品实体、实体生成事件、世界tick事件
涉及模组及版本:
1. rhino-forge-2001.2.2-build.18
2. architectury-9.2.14-forge
3. kubejs-forge-2001.6.5-build.14
4. probejs-6.0.1-forge

## startup里的代码
按键注册，**这里得注意一下，虽然是写在startup里，但是还是属于客户端的内容，在做成服务端打包时，需要删除该文件，打包服务端删除该文件不会对客户端有影响**
```js
const $KeyMappingRegistry = Java.loadClass("dev.architectury.registry.client.keymappings.KeyMappingRegistry");
const $KeyMapping = Java.loadClass("net.minecraft.client.KeyMapping");
const $GLFWkey = Java.loadClass("org.lwjgl.glfw.GLFW");

global.regKeyB = new $KeyMapping(
  "key.meng.packsack", //按键的组名
  $GLFWkey.GLFW_KEY_B,
  "key.keybinding.meng.packsack" //按键的名字
);
 
ClientEvents.init(() => {
  $KeyMappingRegistry.register(global.regKeyB);
});
```
这里的按键组名和按键的名字是一个lang的key，就是需要在对应的lang文件里进行翻译，像下面这样

```json
{
    "key.meng.packsack":"背包",
    "key.keybinding.meng.packsack":"打开饰品栏背包"
}
```
就是说这里是可以自定义的

关于按键`$GLFWkey`的后面内容可以在该篇章的最下面查看

## client里的代码
```js
ClientEvents.tick(event => {
    const key = global.regKeyB;
    if (key.isDown()) {
        if (!event.player.getPersistentData().getBoolean("openBackpack")) {
            event.player.sendData("openBackpack")
            event.player.getPersistentData().putBoolean("openBackpack", true);
        }
    } else {
        if (event.player.getPersistentData().getBoolean("openBackpack")) {
            event.player.getPersistentData().putBoolean("openBackpack", false);
        }
    }
})
```
这里是给服务器发包，告诉服务器玩家按下了这个按键，不然服务器是不知道玩家按下了，这里做了简单的判断，防止玩家一直长按，给服务器一直发包

这个代码只会在玩家第一次按下才会发送一次包

## server里的代码
```js
NetworkEvents.dataReceived("openBackpack", event => {
    const player = event.player
    // 你的处理逻辑代码
})
```
在服务端这边只需要接受客户端发来的包，因为这里没有其他携带信息，所以只需要接受该内容

## 一些注意事项
1. 该项目只是作为示例，很多地方并不是最优解，可自行进行解决
2. 如果对该项目代码部分不满可以将修改好的代码上传至[gitee项目仓库](https://gitee.com/gumengmengs/kubejs-course)
3. 一定要在做服务端时，在startup里删除该文件内的所有代码，包括loadclass的内容

## GLFWkey对应按键
|        按键常量        |          对应按键          |
| :--------------------: | :------------------------: |
|     GLFW_KEY_SPACE     |            空格            |
|  GLFW_KEY_APOSTROPHE   |             '              |
|     GLFW_KEY_COMMA     |             ,              |
|     GLFW_KEY_MINUS     |             -              |
|    GLFW_KEY_PERIOD     |             .              |
|     GLFW_KEY_SLASH     |             /              |
|       GLFW_KEY_0       |             0              |
|       GLFW_KEY_1       |             1              |
|       GLFW_KEY_2       |             2              |
|       GLFW_KEY_3       |             3              |
|       GLFW_KEY_4       |             4              |
|       GLFW_KEY_5       |             5              |
|       GLFW_KEY_6       |             6              |
|       GLFW_KEY_7       |             7              |
|       GLFW_KEY_8       |             8              |
|       GLFW_KEY_9       |             9              |
|   GLFW_KEY_SEMICOLON   |             ;              |
|     GLFW_KEY_EQUAL     |             =              |
|       GLFW_KEY_A       |             A              |
|       GLFW_KEY_B       |             B              |
|       GLFW_KEY_C       |             C              |
|       GLFW_KEY_D       |             D              |
|       GLFW_KEY_E       |             E              |
|       GLFW_KEY_F       |             F              |
|       GLFW_KEY_G       |             G              |
|       GLFW_KEY_H       |             H              |
|       GLFW_KEY_I       |             I              |
|       GLFW_KEY_J       |             J              |
|       GLFW_KEY_K       |             K              |
|       GLFW_KEY_L       |             L              |
|       GLFW_KEY_M       |             M              |
|       GLFW_KEY_N       |             N              |
|       GLFW_KEY_O       |             O              |
|       GLFW_KEY_P       |             P              |
|       GLFW_KEY_Q       |             Q              |
|       GLFW_KEY_R       |             R              |
|       GLFW_KEY_S       |             S              |
|       GLFW_KEY_T       |             T              |
|       GLFW_KEY_U       |             U              |
|       GLFW_KEY_V       |             V              |
|       GLFW_KEY_W       |             W              |
|       GLFW_KEY_X       |             X              |
|       GLFW_KEY_Y       |             Y              |
|       GLFW_KEY_Z       |             Z              |
| GLFW_KEY_LEFT_BRACKET  |             [              |
|   GLFW_KEY_BACKSLASH   |             \              |
| GLFW_KEY_RIGHT_BRACKET |             ]              |
| GLFW_KEY_GRAVE_ACCENT  |             `              |
|    GLFW_KEY_ESCAPE     |            ESC             |
|     GLFW_KEY_ENTER     |        回车(Neter)         |
|      GLFW_KEY_TAB      |            Tab             |
|   GLFW_KEY_BACKSPACE   |      退格(backSpace)       |
|    GLFW_KEY_INSERT     |      插入(Insert/Ins)      |
|    GLFW_KEY_DELETE     |      删除(Delete/Del)      |
|     GLFW_KEY_RIGHT     |          方向右键          |
|     GLFW_KEY_LEFT      |          方向左键          |
|     GLFW_KEY_DOWN      |          方向下键          |
|      GLFW_KEY_UP       |          方向上键          |
|    GLFW_KEY_PAGE_UP    |        PageUp/PgUp         |
|   GLFW_KEY_PAGE_DOWN   |       PageDown/PgOn        |
|     GLFW_KEY_HOME      |            Home            |
|      GLFW_KEY_END      |            End             |
|   GLFW_KEY_CAPS_LOCK   |   锁定大小写(Caps Lock)    |
|  GLFW_KEY_SCROLL_LOCK  |      Scroll Lock/ScLk      |
|   GLFW_KEY_NUM_LOCK    |    锁定数字(Num block)     |
| GLFW_KEY_PRINT_SCREEN  |     截屏(Print Screen)     |
|     GLFW_KEY_PAUSE     |      Pause Break/PaBk      |
|      GLFW_KEY_F1       |             F1             |
|      GLFW_KEY_F2       |             F2             |
|      GLFW_KEY_F3       |             F3             |
|      GLFW_KEY_F4       |             F4             |
|      GLFW_KEY_F5       |             F5             |
|      GLFW_KEY_F6       |             F6             |
|      GLFW_KEY_F7       |             F7             |
|      GLFW_KEY_F8       |             F8             |
|      GLFW_KEY_F9       |             F9             |
|      GLFW_KEY_F10      |            F10             |
|      GLFW_KEY_F11      |            F11             |
|      GLFW_KEY_F12      |            F12             |
|     GLFW_KEY_KP_0      |          数字区 0          |
|     GLFW_KEY_KP_1      |          数字区 1          |
|     GLFW_KEY_KP_2      |          数字区 2          |
|     GLFW_KEY_KP_3      |          数字区 3          |
|     GLFW_KEY_KP_4      |          数字区 4          |
|     GLFW_KEY_KP_5      |          数字区 5          |
|     GLFW_KEY_KP_6      |          数字区 6          |
|     GLFW_KEY_KP_7      |          数字区 7          |
|     GLFW_KEY_KP_8      |          数字区 8          |
|     GLFW_KEY_KP_9      |          数字区 9          |
|  GLFW_KEY_KP_DECIMAL   |          数字区 .          |
|   GLFW_KEY_KP_DIVIDE   |          数字区 /          |
|  GLFW_KEY_KP_MULTIPLY  |          数字区 *          |
|  GLFW_KEY_KP_SUBTRACT  |          数字区 -          |
|    GLFW_KEY_KP_ADD     |          数字区 +          |
|   GLFW_KEY_KP_ENTER    |         数字区回车         |
|   GLFW_KEY_KP_EQUAL    |          数字区 =          |
|  GLFW_KEY_LEFT_SHIFT   |          左Shift           |
| GLFW_KEY_LEFT_CONTROL  |           左Ctrl           |
|   GLFW_KEY_LEFT_ALT    |           左Alt            |
|  GLFW_KEY_LEFT_SUPER   | 左Windows键/左Command(Mac) |
|  GLFW_KEY_RIGHT_SHIFT  |          右Shift           |
| GLFW_KEY_RIGHT_CONTROL |           右Ctrl           |
|   GLFW_KEY_RIGHT_ALT   |           右Alt            |
|  GLFW_KEY_RIGHT_SUPER  | 右Windows键/右Command(Mac) |