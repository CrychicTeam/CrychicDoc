# 基础写法
在这一章会教会大家如何去魔改基础配方，比如工作台、熔炉等,如需请点=>[配方合成进阶](../KubeJSAdvanced/AdvancedRecipe)
## 工作台
首先工作台有有序合成、无序合成,在kjs里同样支持简易写法和json写法，这个可以根据个人需求喜好进行选择
### 有序合成
#### json写法
首先先来看看json写法是如何写的
```js
ServerEvents.recipes(event => {
    event.shaped("3x minecraft:stone", [
		'DDD',
		'   ',
		'W W'
	], {
		D: 'minecraft:diamond',
		W: 'minecraft:white_wool'
    });
});
```
在上面代码中，第一个参数是输出物品和输出物品个数，这里是用的字符串形式表示，当然你要是不喜欢这么写，也可以使用`Item.of(物品id,输出个数)`这样来写

第二个参数是一个数组，数组可以看成一个3x3的工作台，里面的字母就是占位符，表示在这一格需要使用到什么物品，空的位置可以直接使用空格来代替

第三个参数是一个json对象格式，在这里主要是解释上面的占位符，比如在本代码中占位符D表示的是钻石，占位符W表示的是白色羊毛，当然这里的占位符是可以自定义字母的，但是一定要解释占位符表示的是什么物品
#### 简易写法
在下面的写法在使用过crt的朋友可能会相对熟悉一点，这是一种比较简单的写法，看起来也会相对直观
```js
ServerEvents.recipes(event => {
    event.shaped(Item.of('minecraft:white_wool', 3), [
        ['minecraft:white_wool','minecraft:beacon'],[],
        ['minecraft:beacon','','minecraft:beacon']]);
});
```
在上面代码中，第一个参数是输出物品及数量

第二个参数是一个二维数组，数组里的每一个数组可以看作是工作台里的行数，比如第一个数组是第一行，然后里面输入物品id，如果没有物品的直接使用空字符串就可以了(像第三个数组的第二个参数一样),如果整行没有物品可以像第二个数组一样，直接空着

### 无序合成
```js
ServerEvents.recipes(event => {
    event.shapeless(Item.of('minecraft:redstone',2),[
        'minecraft:stone','minecraft:beacon','minecraft:white_wool','minecraft:enchanting_table'
    ]);
});
```
在上面就是无序合成的写法，无序合成无序的第二个参数是一个数组，里面直接放输入物品id就行

## 燃烧系列
在这里是所有燃烧系列，其中包括：熔炉、烟熏炉、高炉和营火(篝火)

他们的配方添加起来都大同小异，所以就丢到一起

```js
ServerEvents.recipes(event => {
    //熔炉
    event.smelting('minecraft:bell','minecraft:gold_ingot',1000,2000);
    //烟熏炉
    event.smoking('minecraft:bell','minecraft:gold_ingot',1000,2000);
    //营火
    event.campfireCooking('minecraft:bell','minecraft:gold_ingot',0,2000);
    //高炉
    event.blasting('minecraft:bell','minecraft:gold_ingot',1000,2000);
});
```
在上面，第一个参数代表输出物品，第二个参数代表着输入物品，第三个参数代表该配方产生的经验，第四个参数代表该物品需要燃烧的tick(20tick为1秒，所以你可以这样写20*秒数)
### 设置物品成燃料
是的，在kjs里也可以将物品设置成燃料，具体使用方法就是
```js
Item.getItem('stone').burnTime = 20*10;
```
在getItem的括号内填上物品的id然后去`.burnTime`这个参数去等于一个他燃烧的时间，这里是燃烧10秒所以是20tick*10次也就是10秒了
## 锻造台
锻造台也是非常简单，锻造台的逻辑就是**物品1+物品2=物品3**
```js
ServerEvents.recipes(event => {
    event.smithing('minecraft:golden_apple','stone','minecraft:apple', 'minecraft:gold_ingot');
});
```
所以在上面的代码中，第一个参数是输出物品，第二个参数是锻造模板(锻造台输入物品的第一个),第三和第四对应着，另外两个输入物品

当然如果，你不想自定义锻造模板，可以将第三第四参数向前移一位，这样默认就是下届合金模板
## 切石机
```js
ServerEvents.recipes(event => {
    event.stonecutting('minecraft:golden_apple', '#minecraft:planks');
});
```
第一个参数是输出物品，第二个参数是输入物品，这里可以看到，使用的#并不是一个物品，而是一个标签(tag)，只要是有携带木板tag的物品都可以分解成金苹果

## 关于配方id
可以看到，在上面的所有配方中，我们都没有进行添加配方id，但是并没有像crt一样报错，这是因为kjs自动生成了一个配方id，我们也可以将这个配方id自己进行添加，只需要在最后加上一个`.id(id名称)`就像下面一样
```js
ServerEvents.recipes(event => {
    event.shapeless(Item.of('minecraft:sand', 2), [
        'minecraft:stone',
		'minecraft:beacon',
		'minecraft:white_wool',
		'minecraft:enchanting_table'
    ]).id("meng_sand");
});
```
[**配方ID详细讲解传送门**](../Digression/RecipeId)

这里注意一下，如果你直接像上面一样写，该配方id则为minecraft下的配方

但是如果你像这样写:`meng:sand`，那么该配方则是在meng这个路径下的sand配方了，这个就类似于模组名+配方名的概念了

这里没有理解的可以看看mc百科的命名空间