---
authors: ['Gu-meng']
---
# 配方ID

* **关于Recipe ID(配方ID)**
  * 配方ID往往是配方文件的所在目录,在开发中用于查看配方类型以及写法时显得尤为重要
  * 常见的例子有删除配方,覆盖配方还有使用`custom`
  * 在添加`JEI`后游戏内`F3+H`开启`高级提示框`后便可查看\
    ![debug.png](/imgs/RecipeId/debug.png)
  * 在JEI内随便打开一个配方文件将光标放在输出上便可查看\
    ![id-view.png](/imgs/RecipeId/id-view.png)
  * 根据上图浇筑铁锭的配方所示,根据配方ID我们可以得知配方文件就是 `ModFile.jar/data/tconstruct/recipes/smeltery/casting/metal/iron/ingot_gold_cast.json`
  * JEI给我们提供了复制配方ID的按键,我们自行配置即可(默认没有绑定任何按键,按照自己的习惯绑定即可)\
    ![key-binding.png](/imgs/RecipeId/key-binding.png)
  * `REI`和`EMI`其实也可以,但是本教程只教`JEI`,使用`REI`或`EMI`的朋友请自行在Mod设置内寻找相关按键

* **Example**
    * 在写配方时后面加上`.id`方法时如果填入了原配方的ID会将原配方直接覆盖
    ```js
    ServerEvents.recipes((event) => {
		const { kubejs } = event.recipes

		kubejs.shaped('tconstruct:queens_slime_block', [
			'RRR',
			'RRR',
			'RRR'
		], {
			R: 'minecraft:rotten_flesh'
		}).id('tconstruct:common/materials/queens_slime_block_from_ingots')
	})
    ```
	修改前\
	![contrast-1](/imgs/RecipeId/contrast-1.png)
	修改后\
	![contrast-2](/imgs/RecipeId/contrast-2.png)

    * 如果每个配方都写一个json会很麻烦且不好管理,所以同时也可以根据配方ID找到配方文件后直接复制里面的内容供`ServerEvents.recipes`事件的`custom`使用,典型的例子就是

    ```js
    ServerEvents.recipes((event) => {
        event.custom({
      	    "type": "createmetallurgy:casting_in_table",
          	"ingredients": [
      		    { "fluid": "createmetallurgy:molten_gold", "amount": 90 },
      		    { "tag": "forge:plates" },
      	    ],
      	    "results": [{ "item": "createmetallurgy:graphite_plate_mold" }],
    	    "processingTime": 90
        })
    })
    ```
	* **注意在复制配方文件时一定要把前后的`{}`也一并复制上!**
    * 提一嘴`custom`也可以使用配方ID进行配方覆盖,和上面一样,后面加个`.id`方法