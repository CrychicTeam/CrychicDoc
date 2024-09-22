---
authors: ['Gu-meng']
---
# 祭坛合成
本章涉及内容：自定义结构、配方检测、配方注册、JEI自定义注册、物品实体
涉及模组及版本:
1. jei-1.20.1-forge-15.3.0.4
2. rhino-forge-2001.2.2-build.18
3. architectury-9.2.14-forge
4. kubejsadditions-forge-4.3.2
5. kubejs-forge-2001.6.5-build.14
6. probejs-7.0.1-forge
## 注册方块和结构检测
**注：该代码应在`startup_scripts`里**
```js
const { $InteractionHand } = require("packages/net/minecraft/world/$InteractionHand")
const { $EntityType } = require("packages/net/minecraft/world/entity/$EntityType")
const { $ItemEntity } = require("packages/net/minecraft/world/entity/item/$ItemEntity")
const { $BlockPos } = require("packages/net/minecraft/core/$BlockPos");

StartupEvents.registry("block", event => {
    event.create("meng:test_block", "basic")
        .hardness(1)
        .defaultCutout()
        .box(1,0,1,15,3,15,true)
        .box(4,3,4,12,21,12,true)
        .box(6,21,6,10,21.2,10,true)
        .blockEntity(e => {
            e.inventory(1, 1)
            e.initialData({ item: "minecraft:air" })
        }).rightClick(e => {
            if (e.getHand() != $InteractionHand.MAIN_HAND) return
            let block = e.getBlock()
            let blockPos = block.getPos()
            let player = e.getPlayer()
            let world = player.getLevel()
            let mainHandItem = player.getMainHandItem().copy()
            let data = block.getEntityData().get("data")
            let dataInItem = data.get("item")
            if (player.isShiftKeyDown()) {
                if (getBlockStructure(world, blockPos)) {
                    let output = global.recipeStructure(dataInItem, getStructureItems(world, blockPos))
                    if (output != null) {
                        removeFloatItem(world, dataInItem, blockPos)
                        itemFloat(world, Item.of(output), block)
                        removeStructureItems(world, blockPos)
                    }
                }
                return
            }
            if (dataInItem != "minecraft:air") {
                removeFloatItem(world, dataInItem, blockPos)
                player.give(dataInItem)
            }

            itemFloat(world, mainHandItem, block, blockPos)
            player.getMainHandItem().count--
        })
})

/**
 * 删除悬浮的物品
 * @param {$Level} world 
 * @param {String} itemId 
 * @param {$BlockPos} pos 
 */
function removeFloatItem(world,itemId,pos){
    const {x,y,z} = pos
    world.getEntities($EntityType.ITEM, (value) => {
        // value type is $ItemEntity
        if (value.item.getId() == itemId) {
            if (value.x == x + 0.5 && value.y == y + 1.42 && value.z == z + 0.5) {
                // 删除物品的方式是丢弃(discarded)
                value.remove("discarded")
                return true
            }
        }
        return false
    });
}

/**
 * 使物品悬浮
 * @param {$Level} world 
 * @param {$ItemStack_} item 
 * @param {$BlockContainerJS} block 
 */
function itemFloat(world,item,block){
    let {x,y,z} = block.getPos()
    /**
     * @type {$ItemEntity}
     */
    let itemEntity = world.createEntity("item")
    itemEntity.setPos(x + 0.5, y + 1.42, z + 0.5)
    itemEntity.item = item
    itemEntity.age = -32768
    itemEntity.setPickUpDelay(-1)
    itemEntity.setNoGravity(true)
    itemEntity.item.count = 1
    itemEntity.spawn()
    block.setEntityData({ data: { item: itemEntity.item.id } })
}
/**
 * 获取结构的方块
 * @param {$Level} world 
 * @param {$BlockPos} blockPos 
 * @returns 
 */
function structureBlocksPos(world,blockPos){
    let { x, y, z } = blockPos
    let posList = [
        [x + 3, y, z],
        [x - 3, y, z],
        [x, y, z + 3],
        [x, y, z - 3],
        [x + 2, y, z + 2],
        [x + 2, y, z - 2],
        [x - 2, y, z + 2],
        [x - 2, y, z - 2]
    ]
    let blocks = []
    for (const pos of posList){
        blocks.push(world.getBlock(pos[0], pos[1], pos[2]))
    }
    return blocks
}

/**
 * 检测结构完整性
 * @param {$Level} world
 * @param {$BlockPos} blockPos 
 */
function getBlockStructure(world, blockPos) {
    let blocks = structureBlocksPos(world, blockPos)
    for (const block of blocks) if (block != "meng:test_block") return false
    return true
}

/**
 * 获取结构内的物品
 * @param {$Level} world
 * @param {$BlockPos} blockPos 
 */
function getStructureItems(world, blockPos) {
    let blocks = structureBlocksPos(world, blockPos)
    let itemList = []
    for (const block of blocks){
        let item = block.getEntityData().get("data").get("item")
        if (item == "minecraft:air") continue
        itemList.push(item.getAsString())
    }
    return itemList
}

/**
 * 删除结构内的物品
 * @param {$Level} world
 * @param {$BlockPos} blockPos 
 */
function removeStructureItems(world,blockPos){
    let blocks = structureBlocksPos(world, blockPos)
    for (const block of blocks){
        let itemId = block.getEntityData().get("data").get("item")
        removeFloatItem(world,itemId,block.getPos())
        itemFloat(world,Item.of("minecraft:air"),block)
    }
}
```

## 方块模型材质
**注:该代码应在`blockstates`里**
```json
{
    "variants":{
        "":{
            "model": "meng:block/test_block"
        }
    }
}
```

**注:该代码应在`models\block`里**
```json
{
	"credit": "Made with Blockbench",
	"texture_size": [64, 64],
	"textures": {
		"0": "minecraft:block/cobblestone",
		"particle": "minecraft:block/cobblestone"
	},
	"elements": [
		{
			"from": [1, 0, 1],
			"to": [15, 3, 15],
			"rotation": {"angle": 0, "axis": "y", "origin": [6, 0.5, 6]},
			"faces": {
				"north": {"uv": [4.5, 8, 8.25, 8.75], "texture": "#0"},
				"east": {"uv": [8.25, 0, 12, 0.75], "texture": "#0"},
				"south": {"uv": [8.25, 0.75, 12, 1.5], "texture": "#0"},
				"west": {"uv": [8.25, 1.5, 12, 2.25], "texture": "#0"},
				"up": {"uv": [3.75, 3.75, 0, 0], "texture": "#0"},
				"down": {"uv": [3.75, 3.75, 0, 7.5], "texture": "#0"}
			}
		},
		{
			"from": [4, 1, 4],
			"to": [12, 21, 12],
			"rotation": {"angle": 0, "axis": "y", "origin": [5, 2, 5]},
			"faces": {
				"north": {"uv": [3.75, 0, 6, 4], "texture": "#0"},
				"east": {"uv": [3.75, 4, 6, 8], "texture": "#0"},
				"south": {"uv": [6, 0, 8.25, 4], "texture": "#0"},
				"west": {"uv": [6, 4, 8.25, 8], "texture": "#0"},
				"up": {"uv": [2.25, 9.75, 0, 7.5], "texture": "#0"},
				"down": {"uv": [4.5, 8, 2.25, 10.25], "texture": "#0"}
			}
		},
		{
			"from": [6, 21, 6],
			"to": [10, 22, 10],
			"rotation": {"angle": 0, "axis": "y", "origin": [7, 22.25, 7]},
			"faces": {
				"north": {"uv": [2.25, 7.5, 3.5, 7.75], "texture": "#0"},
				"east": {"uv": [2.25, 7.75, 3.5, 8], "texture": "#0"},
				"south": {"uv": [8.25, 4.75, 9.5, 5], "texture": "#0"},
				"west": {"uv": [8.25, 5, 9.5, 5.25], "texture": "#0"},
				"up": {"uv": [9.5, 3.5, 8.25, 2.25], "texture": "#0"},
				"down": {"uv": [9.5, 3.5, 8.25, 4.75], "texture": "#0"}
			}
		}
	],
	"display": {
		"thirdperson_righthand": {
			"translation": [0, 3, 1],
			"scale": [0.55, 0.55, 0.55]
		},
		"thirdperson_lefthand": {
			"translation": [0, 3, 1],
			"scale": [0.55, 0.55, 0.55]
		},
		"firstperson_righthand": {
			"rotation": [0, -90, 25],
			"translation": [1.13, 3.2, 1.13],
			"scale": [0.68, 0.68, 0.68]
		},
		"ground": {
			"translation": [0, 0.25, 0],
			"scale": [0.5, 0.5, 0.5]
		},
		"gui": {
			"rotation": [28, 8, -11],
			"translation": [1, 4.5, 0],
			"scale": [1.13, 1.13, 1.13]
		},
		"head": {
			"rotation": [0, 180, 0],
			"translation": [-0.25, 14.5, -0.25]
		}
	},
	"groups": [
		{
			"name": "group",
			"origin": [8, 8, 8],
			"color": 0,
			"children": [0, 1, 2]
		}
	]
}
```

## 祭坛使用配方和注册配方
**注:该代码应在`server_scripts`里**
```js
/**
 * 模拟配方数组
 */
global.testRecipe = [
    {
        principalItem: 'minecraft:diamond',
        inputItems: [
            "minecraft:iron_ingot", 'minecraft:sugar', 'minecraft:netherite_scrap'
        ],
        outputItem: 'minecraft:netherite_ingot'
    }
]
/***
 * 判断两个字符串数组的元素是否相同
 */
function arraysEqualIgnoreOrder(arr1, arr2) {
    if (arr1.length !== arr2.length) {
        return false;
    }
    // 复制数组以避免修改原数组  
    const sortedArr1 = arr1.sort((a, b) => a.localeCompare(b));
    const sortedArr2 = arr2.sort((a, b) => a.localeCompare(b));
    return JSON.stringify(sortedArr1) === JSON.stringify(sortedArr2);
}
/**
 * 配方检测
 * @param {String} principalItem 中间主要的物品id
 * @param {*} recipesItems 配方物品id数组
 * @returns 合成出来的物品
 */
global.recipeStructure = (principalItem, recipesItems) => {
    for (const key of global.testRecipe) {
        if (key.principalItem == principalItem) {
            if (arraysEqualIgnoreOrder(key.inputItems, recipesItems)) {
                return key.outputItem
            }
        }
    }
    return null
}
/**
 * 注册配方
 * @param {String} output 输出物品
 * @param {String[]} input 祭坛周围的物品
 * @param {String} principal 祭坛中间的物品
 */
function regRecipe(output,principal,input){
    global.testRecipe.push({
        principalItem:principal,
        inputItems:input,
        outputItem:output
    })
}

regRecipe("meng:hello_block","meng:slab_block",["meng:stairs_block","minecraft:yellow_wool","minecraft:white_stained_glass"])
```

## 祭坛破坏掉落里面的物品
**注:该代码应在`server_scripts`里**
```js
BlockEvents.broken("meng:test_block", e => {
    let block = e.getBlock()
    let data = block.getEntityData().get("data")
    let { x, y, z } = block.getPos()
    let world = block.getLevel()
    if (data.get("item") == "minecraft:air") return
    world.getEntities($EntityType.ITEM, (value) => {
        if (value.x == x + 0.5 && value.y == y + 1.42 && value.z == z + 0.5) {
            value.age = 0
            value.setPickUpDelay(0)
            value.setNoGravity(false)
            return true

        }
        return false
    });
})
```

## JEI配方注册显示
**注:该代码应在`client_scripts`里,并且需要添加模组[KubeJS Additions](https://www.mcmod.cn/class/11814.html)**
```js
JEIAddedEvents.registerCategories((event) => {
    event.custom("meng:block_test", (category) => {
        const jeiHelpers = category.getJeiHelpers()
        const guiHelper = category.getJeiHelpers().getGuiHelper();
        category
            .title("祭坛合成")
            .background(guiHelper.createDrawable(new ResourceLocation("meng", "jei/synthetic_orientation.png"), 0, 0, 150, 100))
            .icon(guiHelper.createDrawableItemStack(Item.of("meng:test_block")))
            .isRecipeHandled((recipe) => {
                return global["verifyRecipe"](jeiHelpers, recipe);
            })
            .handleLookup((builder, recipe, focuses) => {
                global["handleLookup"](jeiHelpers, builder, recipe, focuses);
            })
    });
});
global["verifyRecipe"] = (jeiHelpers, recipe) => {
    return !!(
        recipe?.data?.inputs !== undefined &&
        recipe?.data?.output !== undefined &&
        recipe?.data?.principal !== undefined
    );
};

global["handleLookup"] = (jeiHelpers, builder, recipe, focuses) => {
    builder.addSlot("INPUT", 35, 45).addItemStack(Item.of(recipe.data.principal)).setSlotName("input");
    let recipeItems = recipe.data.inputs
    for (let index = 0; index < 8; index++) {
        if (recipeItems == null) {
            recipeItems = "air"
        }
    }
    builder.addSlot("INPUT", 35, 15).addItemStack(Item.of(recipeItems[0])).setSlotName("input");
    builder.addSlot("INPUT", 35, 75).addItemStack(Item.of(recipeItems[1])).setSlotName("input");
    builder.addSlot("INPUT", 5, 45).addItemStack(Item.of(recipeItems[2])).setSlotName("input");
    builder.addSlot("INPUT", 65, 45).addItemStack(Item.of(recipeItems[3])).setSlotName("input");

    builder.addSlot("INPUT", 15, 25).addItemStack(Item.of(recipeItems[4])).setSlotName("input");
    builder.addSlot("INPUT", 15, 65).addItemStack(Item.of(recipeItems[5])).setSlotName("input");
    builder.addSlot("INPUT", 55, 25).addItemStack(Item.of(recipeItems[6])).setSlotName("input");
    builder.addSlot("INPUT", 55, 65).addItemStack(Item.of(recipeItems[7])).setSlotName("input");

    builder.addSlot("OUTPUT", 125, 45).addItemStack(Item.of(recipe.data.output)).setSlotName("output");

    builder.addInvisibleIngredients("OUTPUT").addItemStack(Item.of(recipe.data.output));
};

JEIAddedEvents.registerRecipes((event) => {
    let cb = event.custom("meng:block_test")
    for (const key of global.testRecipe) {
        cb.add({ output: key.outputItem, inputs: key.inputItems, principal: key.principalItem })
    }
});

JEIAddedEvents.registerRecipeCatalysts(jei =>{
    jei.data.addRecipeCatalyst("meng:test_block","meng:block_test")
})
```

## 一些注意事项
1. 该项目的配方检测全是字符串，并不是ItemStack所以无法检测nbt，如需请自行更改
2. 该项目只是作为示例，很多地方并不是最优解，可自行进行解决
3. 如果对该项目代码部分不满可以将修改好的代码上传至[gitee项目仓库](https://gitee.com/gumengmengs/kubejs-course)
4. 项目完整代码[在这里](https://gitee.com/gumengmengs/kubejs-course/tree/main/Code/Projects/AltarComposition/kubejs)
5. 因为物品渲染使用的物品掉落物实体，所以会被kill @e[type="item"] 给清理掉，所以得注意一下(可以考虑写一个方块tick事件去检测解决)