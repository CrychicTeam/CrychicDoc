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
regRecipe("minecraft:yellow_bed","minecraft:air",["minecraft:diamond","minecraft:yellow_wool","minecraft:white_stained_glass"])