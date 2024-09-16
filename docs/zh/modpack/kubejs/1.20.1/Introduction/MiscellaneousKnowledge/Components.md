# 文本组件 (Component)

## 前言

- **`文本组件 (Component)`**，游戏中常用于作为富文本表达。

参见[Wiki 文本组件格式](https://zh.minecraft.wiki/w/%E6%96%87%E6%9C%AC%E7%BB%84%E4%BB%B6)以及[Wiki 文本组件教程](https://zh.minecraft.wiki/w/Tutorial:%E6%96%87%E6%9C%AC%E7%BB%84%E4%BB%B6)。
本文不会重复叙述以上内容，即默认已阅读以上内容，而是着重叙述代码实现等方面。

`文本组件 (Component)`是一个原生原味的 Minecraft 类，它的 KJS 包装类为`ComponentKJS`，但实际上却是被`可变文本组件 (MutableComponet)`继承。
尽管大部分情况下它们并无差别，但也不要将两者混淆。

注意：KJS 中的全局类`Component`实际上是`TextWrapper`，部分`Component`内原有的内容需要通过导入该类来实现。
本文中会用 `$Component` 标识。

## 表示

文本组件的内部维护着一个`组件内容 (ComponentContents)`，这是文本组件实际存储的内容。
文本组件只作为一个包装。

本文所述的表示实际上是组件内容。

### 纯文本 (LiteralContents)

纯文本只包含文本，不包含各种变量。

::: code-group

```js [字符串]
// 字符串形式常用作参数等
"对，就是纯粹的字符串。";
```

```js [对象]
Component.literal("这是一个文本组件"); // 注意：传参为null时会报错
$Component.nullToEmpty("这是一个文本组件"); // 等同于上一行，但当传参为null时会返回空文本
```

:::

### 翻译文本 (TranslatableContents)

该组件内容是可以加入参数的。该组件内容依赖于客户端的[语言文件](https://zh.minecraft.wiki/w/%E8%B5%84%E6%BA%90%E5%8C%85#%E8%AF%AD%E8%A8%80)。
在不同的语言下会呈现出不一样的结果。

本文会给出语言文件的内容和示例代码以供更好的理解。

_Tips：翻译键一般以该规则命名`category.modid.id`_

::: code-group

```js [对象]
// 在英语(en_us)下显示为：“This is a text without any args”
// 在中文(zh_cn)下显示为：“这是一个不带参数的文本”
// 以下几行以此类推
Component.translatable("key.example.example_text1");

// 用以下的方式将语言文件中的“%s”替换为“100%”，甚至是你想要的其他内容
// 参数不只可以有一个
Component.translatable("key.example.example_text2", "100%");

// 该方法会在找不到翻译键的情况下用fallback（即第二个参数）代替所呈现的内容
// 在英语下效果等同于第一行，显示为：“Test Text 0”
// 但由于在中文下没有该翻译键，所以显示为：“找不到该文本”
$Component.translatableWithFallback(
  "key.example.example_text3",
  "找不到该文本"
);

// 同样的，它也可以附带参数。第二个参数为fallback，往后为参数
$Component.translatableWithFallback(
  "key.example.example_text3",
  "找不到该文本",
  "Zero"
);
```

:::

::: code-group

```json [语言文件 zh_cn.json]
{
  "key.example.example_text1": "这是一个不带参数的文本",
  "key.example.example_text2": "进度条：%s"
}
```

```json [语言文件 en_us.json]
{
  "key.example.example_text1": "This is a text without any args",
  "key.example.example_text2": "Progress: %s",
  "key.example.example_text3": "Test Text 0",
  "key.example.example_text4": "Test Text %s"
}
```

:::

### 记分板分数（ScoreContents）

该组件内容依赖于[记分板](https://zh.minecraft.wiki/w/%E8%AE%B0%E5%88%86%E6%9D%BF)。

::: code-group

```js [对象]
// 传入一个分数持有者和记分项
Component.score("@s", "test"); // 显示在test记分项下自身的分数
```

:::

### 实体名称（SelectorContents）

该组件内容为[目标选择器](https://zh.minecraft.wiki/w/%E7%9B%AE%E6%A0%87%E9%80%89%E6%8B%A9%E5%99%A8)找到的实体名称。

::: code-group

```js [对象]
// 传入一个目标选择器（组件中的selector）和分隔符（组件中的separator）
$Component.selector("@s", $Optional.empty()); // 显示自身的名称
```

:::

### 按键绑定 (KeybindContents)

该组件内容依赖于客户端的[键位](https://zh.minecraft.wiki/w/%E6%8E%A7%E5%88%B6#%E5%8F%AF%E8%AE%BE%E7%BD%AE%E7%9A%84%E9%94%AE%E4%BD%8D)。

::: code-group

```js [对象]
// 传入一个绑定键位标识符（组件中的keybind）
Component.keybind("key.inventory"); // 在默认设置下显示为“E”
```

:::

### NBT 标签值 (NbtContents)

该组件内容依赖于[NBT 标签](https://zh.minecraft.wiki/w/NBT%E6%A0%87%E7%AD%BE)。
较复杂且不常用，本文只给出大致用法。

::: code-group

```js [对象]
/*
 * 第一个参数为NBT标签下的路径（即组件中的nbt）
 * 第二个参数为是否解析为文本（即组件中的interpret）
 * 第三个参数为文本组件间的分隔符（即组件中的separator）
 * 第四个参数为NBT的来源（即组件中的source）
 * 该行代码会显示自身的NBT标签
 */
$Component.nbt("", false, $Optional.empty(), new $EntityDataSource("@s"));
```

:::

## 样式（Style）

用于修饰文本组件，比如颜色、点击事件、悬浮事件等。

::: code-group

```js [颜色]
Component.literal("这是一个文本组件").color(0x9400d3); // 颜色为0x9400D3的“这是一个文本组件”
Component.literal("这是一个文本组件").noColor(); // 将一个已有颜色的文本组件设为无色（默认色）

Component.blue("这是一个文本组件"); // 显示为蓝色的“这是一个文本组件”
Component.literal("这是一个文本组件").blue(); // 等同于上一行，将一个已有的文本组件设为蓝色
// 另外还有更多颜色都是类似的使用方式
```

```js [样式]
Component.bold(true); // 将一个已有的文本组件设为粗体
Component.bold(); // 等同于上一行
// 下面的方法以此类推
Component.italic(); // 斜体
Component.underlined(); // 下划线
Component.strikethrough(); // 删除线
Component.obfuscated(); // 随机字符

Component.font("RandomFont"); // 显示该文本组件的字体

Component.hasStyle(); // 判断该文本组件是否有样式
Component.isEmpty(); // 判断该文本组件是否为空

// 单独附加样式
let style = new $Style().withBold(true); // 附加一个粗体
$Component.withStyle(style);
```

```js [事件]
Component.clickRunCommand("/tell @s Harry"); // 点击时执行该命令
Component.clickSuggestCommand("/tell @s Harry"); // 点击时将该命令加入聊天框中
Component.clickCopy("剪贴板"); // 点击时将改内容复制到剪贴板中
Component.clickChangePage("2"); // 点击时改变书的页数
Component.clickOpenUrl("https://docs.mihono.cn"); // 点击时打开一个URL
Component.clickOpenFile("file.txt"); // 点击时打开一个文件

Component.hover("其他东西"); // 将鼠标放到文本上时显示该内容

Component.insertion("其他东西"); // Shift点击时将该内容加入聊天框中
```

:::

## 子组件 (Sibling)

继承于父组件的样式，也可以独立覆盖父组件的样式。

::: code-group

```js [颜色]
// 添加子组件，显示为“这是一个文本组件，这是另一个文本组件。”
Component.literal("这是一个文本组件，").append(
  Component.literal("这是另一个文本组件。")
);

Component.hasSiblings(); // 该组件是否有子组件
```

:::
