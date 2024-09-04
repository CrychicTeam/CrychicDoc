# 基础写法-删除和修改
在本章中，将介绍**配方过滤器**和使用**配方过滤器**删除和修改配方
## 配方过滤器
在kubejs中，我们可以通过配方过滤器在配方修改或者删除中进行使用，来匹配相符条件的物品配方

最基础的配方过滤器有以下五种
```js
{output : '物品id'}
{input : '物品id'}
{mod : '模组id'}
{id : '配方id'}
{type : '配方类型'}
```
下面会为大家解释上面五种基础使用方法对应的使用：
1. 第一种代表着匹配输出物品,比如ABC合成D,那么D就是输出物品，如果配方删除时输出物品是D的配方都会被删除
2. 第二种代表着匹配输入物品,比如ABC合成D,那么ABC为输入物品,如果这里物品id给的B,那么只要合成配方包含B的都会被删除
3. 第三种代表着匹配模组,比如现有模组id位create(机械动力),如果填写进去，那么将会删除机械动力里的所有配方
4. 第四种代表着匹配配方id,在游戏内安装JEI模组切打开了F3+H的高级显示，那么就可以看到物品的合成配方id,如果填写进去将会精准的删掉该配方
5. 第五种代表着匹配配方类型,比如配方类型:工作台、熔炉等，如果填写进去那么将会删掉整个工作台或者整个熔炉的配方

再以上基础的五种上，kubejs也提供了将这五种进行组合的方法，在实际魔改的过程中，我们往往需要考虑的更多，所以下面是kjs提供的三种额外方式
```js
{基础配方过滤器1,基础配方过滤器2,基础配方过滤器3,...}
[{基础配方过滤器1,基础配方过滤器2,基础配方过滤器3,...},{基础配方过滤器1,基础配方过滤器2,基础配方过滤器3,...},...]
{not:{基础配方过滤器1,基础配方过滤器2,基础配方过滤器3,...}}
```
下面会为大家解释上面三种高级配方过滤器的方法和对应的使用例子：
1. 第一种代表着匹配所有的基础配方过滤器,如果有一个不满足那么就失效,比如我们拿上面的第二种`匹配输入物品`的过滤器，这里我们如果这样写:
```js
{{input : 'stone'},{input : 'redstone'},{input : 'grass'}}
```
那么就需要一个配方里的输入物品满足以上三个物品，才会选择到该配方,如果只是满足两个或者一个,那么就不会进入到该过滤器中

2. 第二种是第一种的衍生，但是不太一样，第二种是一个数组，数组里包含的是第一种，只要该配方满足数组里的其中一组那么就可以被修改成功
3. 第三种也是第一种的变异，代表着只要不是第一种里的都可以进入到配方过滤器中
## 删除配方
在上面简单介绍了几种配方过滤器，在这里我们进行实际体验一下配方过滤器，因为光在上面进行看的话可能有点看的云里雾里的，不如直接上手体验一下

首先删除配方只有一个方法那就是`event.remove();`,所有的操作都需要依赖上面所讲的配方过滤器
```js
ServerEvents.recipes(event => {
    //删除配方输出为石砖的所有配方
    event.remove({output : 'stone_bricks'});
    //删除配方中包含石砖的所有配方
    event.remove({input : 'stone_bricks'});
    //删除模组id为minecraft中的所有配方
    event.remove({mod : 'minecraft'});
    //删除配方id为oak_wood的配方(四个橡木原木合成的橡木)
    event.remove({id : "minecraft:oak_wood"});
    //删除熔炉这个工作方块里的所有配方
    event.remove({type : "smelting"});
});
```
我们发现，除了直接删除配方id这个方法精准一些外，其他四种都删除的范围很大，如果一个物品在工作台里也有配方在熔炉里也有配方，我们只想删除熔炉里的该物品配方就可以使用到高级配方过滤器
```js
ServerEvents.recipes(event => {
    event.remove({type:"crafting_shaped",output:"end_stone_bricks"});
});
```
在1.20中末地石砖可以被工作台和切石机做出来，所以上面只删除了工作台里的末地石砖的配方，切石机的末地石砖配方是还存在的

**<font color=red>注: 在目前，无法使用kjs删除自己添加的配方，无论什么方式</font>**
## 配方替换
在上面配方删除时，并没有很好的感受到高级配方过滤器的使用，但是在配方物品修改章节我们可以很好的体验到高级配方过滤器

首先在kjs中配方替换有两种，一种是更改所有输入配方`replaceInput`，一种是更改所有输出配方`replaceOutput`

下面会简单举三例子来感受一下配方替换和高级配方过滤器
```js
ServerEvents.recipes(event =>{
    //配方需要满足输入木棍和木板标签才能将输入的木棍替换为烈焰棒
    event.replaceInput({input:'minecraft:stick',input:'#minecraft:planks'},'minecraft:stick','minecraft:blaze_rod');
    //配方需要满足输出物品为:发射器、投掷器、熔炉时才能将输入物品为圆石替换为钻石
    event.replaceInput([{output:'minecraft:dispenser'},{output:'minecraft:dropper'},{output:'minecraft:furnace'}],"minecraft:cobblestone",'minecraft:diamond');
    //配方只要不满足输入物品为木棍和木板标签的都将输出物品为木头标签替换为石砖
    event.replaceOutput({not:{input:'minecraft:stick',input:'#minecraft:planks'}},'#minecraft:logs','minecraft:stone_bricks');
})
```