---
authors: ['Gu-meng']
---
# `obj`模型
* `obj`模型是在制作上相比于`Minecraft`原版模型更为自由的模型,例如[**沉浸工程**](https://www.mcmod.cn/class/463.html)的[**电弧炉**](https://www.mcmod.cn/item/36383.html)和[**斗轮式挖掘机**](https://www.mcmod.cn/item/36550.html)等
  * ### 建立`obj`模型
    * 首先建立一个`Obj`模型,可以使用很多软件,例如市面上典型的[**`3DS Max`**](https://www.autodesk.com.cn/products/3ds-max) [**`Blender`**](https://www.blender.org/) [**`BlockBench`**](https://www.blockbench.net)等软件
    这里提供一个例子
	  ```
	  # Made in Blockbench 4.8.3
	  mtllib qqqqoo.mtl

	  o pyramid
	  v 1 -2.220446049250313e-16 1
	  v 1 0.9999999999999998 1.0000000000000002
	  v 0 0.9999999999999998 1.0000000000000002
	  v 0 -2.220446049250313e-16 1
	  v 0.5 0.4999999999999999 0.5000000000000001
	  vt 0.25 0.75
	  vt 0.25 1
	  vt 0 1
	  vt 0 0.75
	  vt 0 0.479471875
	  vt 0.25 0.479471875
	  vt 0.125 0.65625
	  vt 0.1875 0.620096875
	  vt 0.4375 0.620096875
	  vt 0.3125 0.796875
	  vt 0.265625 0.823221875
	  vt 0.515625 0.823221875
	  vt 0.390625 1
	  vt 0.375 0.479471875
	  vt 0.625 0.479471875
	  vt 0.5 0.65625
	  vn 0 -2.220446049250313e-16 1
	  vn 0.7071067811865476 1.5700924586837752e-16 -0.7071067811865476
	  vn 0 0.7071067811865477 -0.7071067811865475
	  vn 0 -0.7071067811865475 -0.7071067811865477
	  vn -0.7071067811865476 1.5700924586837752e-16 -0.7071067811865476
	  usemtl m_c5b90dae-3a75-90c1-2280-8a6b9dc0fba2
	  f 1/1/1 2/2/1 3/3/1 4/4/1
	  f 2/5/2 1/6/2 5/7/2
	  f 3/8/3 2/9/3 5/10/3
	  f 1/11/4 4/12/4 5/13/4
	  f 4/14/5 3/15/5 5/16/5
	  ```
	  同时导出时会自动生成一个`mtl`文件和一个`png`文件
	  ```
	  # Made in Blockbench 4.8.3
	  newmtl m_c5b90dae-3a75-90c1-2280-8a6b9dc0fba2
	  map_Kd kubejs:block/qqqqoo
	  newmtl none
	  ```

	  注意`obj`里面的`usemtl m_c5b90dae-3a75-90c1-2280-8a6b9dc0fba2`要与`mtl`文件里面的`newmtl m_c5b90dae-3a75-90c1-2280-8a6b9dc0fba2`对应上
	  `obj`的`mtllib qqqqoo.mtl`是确定mtl文件的,这里默认同级就行\
	  `mtl`里面的`map_Kd`则是要指定贴图的路径\
	  贴图和原版模型一样,放在`assets/${modid}/textures/block`下(因为这次的教程属于方  块,所以放在`block`下,制作物品时记得`item`下)
  * ### 指向`Obj`文件
    * 因为`Minecraft`原版并不能直接读取`obj`模型文件,因此需要一个`json`模型来指向`obj`文件
      ```json
	  {
	      "loader": "forge:obj",
	      "model": "kubejs:models/block/qqqqoo.obj",
	      "flip_v": true,
	      "textures": {
		      "particle": "kubejs:block/qqqqoo"
	      }
      }
	  ```
      `loader`表示要加载的类型,这里指定obj模型\
	  `model`表示要加载的Obj模型路径\
	  `flip_v`表示是否翻转贴图,因为`obj`模型贴图相对于MC来说是倒着的,所以要`true`\
	  `textures`表示要加载的贴图,这里指定`particle`贴图来确定粒子

  * ### 确定`blockstate`
    * 在加载`obj`模型时,需要确定`blockstate`,因为`blockstate`在加载`Obj`时不会自动生成的,因此会导致无法让方块确定模型
      ```json
	  {
	      "variants": {
	  	      "facing=down": {
	  	   	      "model": "kubejs:block/qqqqoo",
	  		      "x": 90
	  	      },
	  	      "facing=east": {
	  		      "model": "kubejs:block/qqqqoo",
	  		      "y": 90
	  	      },
	  	      "facing=north": {
	  		      "model": "kubejs:block/qqqqoo"
	  	      },
	  	      "facing=south": {
	  	          "model": "kubejs:block/qqqqoo",
	  		      "y": 180
	  	      },
	  	      "facing=up": {
	  		      "model": "kubejs:block/qqqqoo",
	  		      "x": 270
	  	      },
	  	      "facing=west": {
	  		      "model": "kubejs:block/qqqqoo",
	  		      "y": 270
	  	      }
	      }
      }
	  ```
	* ### 检查路径
      `mtl`和`obj`指向`obj`模型的`json`文件放在`assets/${modid}/models/block`下\
	  `png`则放在`assets/${modid}/textures/block`下\
	  确定`blockstate`的`json`文件需要放在`assets/${modid}/blockstate`下\
	  同时要该文件命名都为方块注册名\
	  注意导出模型在游戏里面有所偏移,在制作模型时修改即可

	# 参考自
	[[我的世界 1.20.4 NeoForge 最新模组教程]18 加载OBJ模型](https://www.bilibili.com/video/BV1jm421J7UR)\
	[[Boson 1.16 Modding Tutorial] - Obj](https://boson.v2mcdev.com/specialmodel/obj.html)