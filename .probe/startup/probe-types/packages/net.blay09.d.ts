declare module "packages/net/blay09/mods/craftingtweaks/api/impl/$DefaultGridRotateHandler" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$GridRotateHandler, $GridRotateHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridRotateHandler"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export class $DefaultGridRotateHandler implements $GridRotateHandler<($AbstractContainerMenu)> {

constructor()

public "rotateGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: $AbstractContainerMenu$Type, arg3: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultGridRotateHandler$Type = ($DefaultGridRotateHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultGridRotateHandler_ = $DefaultGridRotateHandler$Type;
}}
declare module "packages/net/blay09/mods/balm/api/client/rendering/$BalmModels" {
import {$Material, $Material$Type} from "packages/net/minecraft/client/resources/model/$Material"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$ModelBaker, $ModelBaker$Type} from "packages/net/minecraft/client/resources/model/$ModelBaker"
import {$ModelState, $ModelState$Type} from "packages/net/minecraft/client/resources/model/$ModelState"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Transformation, $Transformation$Type} from "packages/com/mojang/math/$Transformation"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$UnbakedModel, $UnbakedModel$Type} from "packages/net/minecraft/client/resources/model/$UnbakedModel"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export interface $BalmModels {

 "bakeModel"(arg0: $ResourceLocation$Type, arg1: $UnbakedModel$Type): $DeferredObject<($BakedModel)>
 "loadDynamicModel"(arg0: $ResourceLocation$Type, arg1: $Function$Type<($BlockState$Type), ($ResourceLocation$Type)>, arg2: $Function$Type<($BlockState$Type), ($Map$Type<(string), (string)>)>, arg3: $BiConsumer$Type<($BlockState$Type), ($Matrix4f$Type)>): $DeferredObject<($BakedModel)>
 "loadDynamicModel"(arg0: $ResourceLocation$Type, arg1: $Function$Type<($BlockState$Type), ($ResourceLocation$Type)>, arg2: $Function$Type<($BlockState$Type), ($Map$Type<(string), (string)>)>, arg3: $BiConsumer$Type<($BlockState$Type), ($Matrix4f$Type)>, arg4: $List$Type<($RenderType$Type)>): $DeferredObject<($BakedModel)>
 "overrideModel"(arg0: $Supplier$Type<($Block$Type)>, arg1: $Supplier$Type<($BakedModel$Type)>): void
 "createBaker"(arg0: $ResourceLocation$Type, arg1: $BiFunction$Type<($ResourceLocation$Type), ($Material$Type), ($TextureAtlasSprite$Type)>): $ModelBaker
 "loadModel"(arg0: $ResourceLocation$Type): $DeferredObject<($BakedModel)>
 "retexture"(arg0: $ModelBakery$Type, arg1: $ResourceLocation$Type, arg2: $Map$Type<(string), (string)>): $UnbakedModel
 "retexture"(arg0: $ResourceLocation$Type, arg1: $Map$Type<(string), (string)>): $DeferredObject<($BakedModel)>
 "getUnbakedModelOrMissing"(arg0: $ResourceLocation$Type): $UnbakedModel
 "getUnbakedMissingModel"(): $UnbakedModel
 "getModelState"(arg0: $Transformation$Type): $ModelState
}

export namespace $BalmModels {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmModels$Type = ($BalmModels);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmModels_ = $BalmModels$Type;
}}
declare module "packages/net/blay09/mods/balm/api/proxy/$ProxyResolutionException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"

export class $ProxyResolutionException extends $Exception {

constructor(arg0: string, arg1: $Throwable$Type, arg2: boolean, arg3: boolean)
constructor(arg0: $Throwable$Type)
constructor(arg0: string, arg1: $Throwable$Type)
constructor(arg0: string)
constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProxyResolutionException$Type = ($ProxyResolutionException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProxyResolutionException_ = $ProxyResolutionException$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/fluid/$ForgeFluidTank" {
import {$IFluidHandler, $IFluidHandler$Type} from "packages/net/minecraftforge/fluids/capability/$IFluidHandler"
import {$FluidStack, $FluidStack$Type} from "packages/net/minecraftforge/fluids/$FluidStack"
import {$IFluidHandler$FluidAction, $IFluidHandler$FluidAction$Type} from "packages/net/minecraftforge/fluids/capability/$IFluidHandler$FluidAction"
import {$FluidTank, $FluidTank$Type} from "packages/net/blay09/mods/balm/api/fluid/$FluidTank"

export class $ForgeFluidTank implements $IFluidHandler {

constructor(arg0: $FluidTank$Type)

public "fill"(arg0: $FluidStack$Type, arg1: $IFluidHandler$FluidAction$Type): integer
public "drain"(arg0: $FluidStack$Type, arg1: $IFluidHandler$FluidAction$Type): $FluidStack
public "drain"(arg0: integer, arg1: $IFluidHandler$FluidAction$Type): $FluidStack
public "getTanks"(): integer
public "isFluidValid"(arg0: integer, arg1: $FluidStack$Type): boolean
public "getFluidInTank"(arg0: integer): $FluidStack
public "getTankCapacity"(arg0: integer): integer
get "tanks"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeFluidTank$Type = ($ForgeFluidTank);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeFluidTank_ = $ForgeFluidTank$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$CraftingTweaksButtonStyles" {
import {$ButtonStyle, $ButtonStyle$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonStyle"

export class $CraftingTweaksButtonStyles {
static readonly "DEFAULT": $ButtonStyle
static readonly "SMALL_HEIGHT": $ButtonStyle
static readonly "SMALL_WIDTH": $ButtonStyle
static readonly "SMALL": $ButtonStyle

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksButtonStyles$Type = ($CraftingTweaksButtonStyles);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksButtonStyles_ = $CraftingTweaksButtonStyles$Type;
}}
declare module "packages/net/blay09/mods/waystones/compat/$JadeIntegration" {
import {$IWailaClientRegistration, $IWailaClientRegistration$Type} from "packages/snownee/jade/api/$IWailaClientRegistration"
import {$IWailaPlugin, $IWailaPlugin$Type} from "packages/snownee/jade/api/$IWailaPlugin"
import {$IWailaCommonRegistration, $IWailaCommonRegistration$Type} from "packages/snownee/jade/api/$IWailaCommonRegistration"

export class $JadeIntegration implements $IWailaPlugin {

constructor()

public "registerClient"(arg0: $IWailaClientRegistration$Type): void
public "register"(arg0: $IWailaCommonRegistration$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JadeIntegration$Type = ($JadeIntegration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JadeIntegration_ = $JadeIntegration$Type;
}}
declare module "packages/net/blay09/mods/waystones/item/$ReturnScrollItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$BoundScrollItem, $BoundScrollItem$Type} from "packages/net/blay09/mods/waystones/item/$BoundScrollItem"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ReturnScrollItem extends $BoundScrollItem {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Item$Properties$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReturnScrollItem$Type = ($ReturnScrollItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReturnScrollItem_ = $ReturnScrollItem$Type;
}}
declare module "packages/net/blay09/mods/waystones/block/$SharestoneBlock" {
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$WaystoneOrigin, $WaystoneOrigin$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneOrigin"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$DoubleBlockHalf, $DoubleBlockHalf$Type} from "packages/net/minecraft/world/level/block/state/properties/$DoubleBlockHalf"
import {$List, $List$Type} from "packages/java/util/$List"
import {$WaystoneBlockBase, $WaystoneBlockBase$Type} from "packages/net/blay09/mods/waystones/block/$WaystoneBlockBase"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $SharestoneBlock extends $WaystoneBlockBase {
static readonly "FACING": $DirectionProperty
static readonly "HALF": $EnumProperty<($DoubleBlockHalf)>
static readonly "WATERLOGGED": $BooleanProperty
static readonly "ORIGIN": $EnumProperty<($WaystoneOrigin)>
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type, arg1: $DyeColor$Type)

public "appendHoverText"(arg0: $ItemStack$Type, arg1: $BlockGetter$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
public "getColor"(): $DyeColor
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
get "color"(): $DyeColor
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SharestoneBlock$Type = ($SharestoneBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SharestoneBlock_ = $SharestoneBlock$Type;
}}
declare module "packages/net/blay09/mods/balm/api/block/$CustomFarmBlock" {
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $CustomFarmBlock {

 "canSustainPlant"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $Direction$Type, arg4: $Block$Type): boolean
 "isFertile"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type): boolean
}

export namespace $CustomFarmBlock {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomFarmBlock$Type = ($CustomFarmBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomFarmBlock_ = $CustomFarmBlock$Type;
}}
declare module "packages/net/blay09/mods/balm/config/$ExampleConfigData$ExampleCategory" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ExampleConfigData$ExampleCategory {
 "innerField": string
 "exampleFloat": float

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExampleConfigData$ExampleCategory$Type = ($ExampleConfigData$ExampleCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExampleConfigData$ExampleCategory_ = $ExampleConfigData$ExampleCategory$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/block/$ForgeBalmBlocks" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$BalmBlocks, $BalmBlocks$Type} from "packages/net/blay09/mods/balm/api/block/$BalmBlocks"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $ForgeBalmBlocks implements $BalmBlocks {

constructor()

public "register"(arg0: $Supplier$Type<($Block$Type)>, arg1: $Supplier$Type<($BlockItem$Type)>, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type): void
public "registerBlock"(arg0: $Supplier$Type<($Block$Type)>, arg1: $ResourceLocation$Type): $DeferredObject<($Block)>
public "blockProperties"(): $BlockBehaviour$Properties
public "registerBlockItem"(arg0: $Supplier$Type<($BlockItem$Type)>, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): $DeferredObject<($Item)>
public "register"(arg0: $Supplier$Type<($Block$Type)>, arg1: $Supplier$Type<($BlockItem$Type)>, arg2: $ResourceLocation$Type): void
public "registerBlockItem"(arg0: $Supplier$Type<($BlockItem$Type)>, arg1: $ResourceLocation$Type): $DeferredObject<($Item)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmBlocks$Type = ($ForgeBalmBlocks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmBlocks_ = $ForgeBalmBlocks$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$GridTransferHandler" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $GridTransferHandler<TMenu extends $AbstractContainerMenu> {

 "canTransferFrom"(arg0: $Player$Type, arg1: TMenu, arg2: $Slot$Type, arg3: $CraftingGrid$Type): boolean
 "transferIntoGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: TMenu, arg3: $Slot$Type): boolean
 "putIntoGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: $AbstractContainerMenu$Type, arg3: integer, arg4: $ItemStack$Type): $ItemStack
}

export namespace $GridTransferHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GridTransferHandler$Type<TMenu> = ($GridTransferHandler<(TMenu)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GridTransferHandler_<TMenu> = $GridTransferHandler$Type<(TMenu)>;
}}
declare module "packages/net/blay09/mods/balm/api/event/$ServerLevelTickHandler" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export interface $ServerLevelTickHandler {

 "handle"(arg0: $Level$Type): void

(arg0: $Level$Type): void
}

export namespace $ServerLevelTickHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerLevelTickHandler$Type = ($ServerLevelTickHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerLevelTickHandler_ = $ServerLevelTickHandler$Type;
}}
declare module "packages/net/blay09/mods/balm/api/recipe/$BalmRecipes" {
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export interface $BalmRecipes {

 "registerRecipeType"<T extends $Recipe<(any)>>(arg0: $Supplier$Type<($RecipeType$Type<(T)>)>, arg1: $Supplier$Type<($RecipeSerializer$Type<(T)>)>, arg2: $ResourceLocation$Type): $DeferredObject<($RecipeType<(T)>)>

(arg0: $Supplier$Type<($RecipeType$Type<(T)>)>, arg1: $Supplier$Type<($RecipeSerializer$Type<(T)>)>, arg2: $ResourceLocation$Type): $DeferredObject<($RecipeType<(T)>)>
}

export namespace $BalmRecipes {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmRecipes$Type = ($BalmRecipes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmRecipes_ = $BalmRecipes$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/client/rendering/$ForgeBalmModels" {
import {$Material, $Material$Type} from "packages/net/minecraft/client/resources/model/$Material"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$ModelBaker, $ModelBaker$Type} from "packages/net/minecraft/client/resources/model/$ModelBaker"
import {$ModelState, $ModelState$Type} from "packages/net/minecraft/client/resources/model/$ModelState"
import {$ForgeBalmModels$DeferredModel, $ForgeBalmModels$DeferredModel$Type} from "packages/net/blay09/mods/balm/forge/client/rendering/$ForgeBalmModels$DeferredModel"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$BalmModels, $BalmModels$Type} from "packages/net/blay09/mods/balm/api/client/rendering/$BalmModels"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Transformation, $Transformation$Type} from "packages/com/mojang/math/$Transformation"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$UnbakedModel, $UnbakedModel$Type} from "packages/net/minecraft/client/resources/model/$UnbakedModel"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $ForgeBalmModels implements $BalmModels {
readonly "modelsToBake": $List<($ForgeBalmModels$DeferredModel)>

constructor()

public "register"(): void
public "bakeModel"(arg0: $ResourceLocation$Type, arg1: $UnbakedModel$Type): $DeferredObject<($BakedModel)>
public "loadDynamicModel"(arg0: $ResourceLocation$Type, arg1: $Function$Type<($BlockState$Type), ($ResourceLocation$Type)>, arg2: $Function$Type<($BlockState$Type), ($Map$Type<(string), (string)>)>, arg3: $BiConsumer$Type<($BlockState$Type), ($Matrix4f$Type)>, arg4: $List$Type<($RenderType$Type)>): $DeferredObject<($BakedModel)>
public "overrideModel"(arg0: $Supplier$Type<($Block$Type)>, arg1: $Supplier$Type<($BakedModel$Type)>): void
public "createBaker"(arg0: $ResourceLocation$Type, arg1: $BiFunction$Type<($ResourceLocation$Type), ($Material$Type), ($TextureAtlasSprite$Type)>): $ModelBaker
public "loadModel"(arg0: $ResourceLocation$Type): $DeferredObject<($BakedModel)>
public "retexture"(arg0: $ResourceLocation$Type, arg1: $Map$Type<(string), (string)>): $DeferredObject<($BakedModel)>
public "getUnbakedModelOrMissing"(arg0: $ResourceLocation$Type): $UnbakedModel
public "getUnbakedMissingModel"(): $UnbakedModel
public "getModelState"(arg0: $Transformation$Type): $ModelState
public "onBakeModels"(arg0: $ModelBakery$Type, arg1: $BiFunction$Type<($ResourceLocation$Type), ($Material$Type), ($TextureAtlasSprite$Type)>): void
public "loadDynamicModel"(arg0: $ResourceLocation$Type, arg1: $Function$Type<($BlockState$Type), ($ResourceLocation$Type)>, arg2: $Function$Type<($BlockState$Type), ($Map$Type<(string), (string)>)>, arg3: $BiConsumer$Type<($BlockState$Type), ($Matrix4f$Type)>): $DeferredObject<($BakedModel)>
public "retexture"(arg0: $ModelBakery$Type, arg1: $ResourceLocation$Type, arg2: $Map$Type<(string), (string)>): $UnbakedModel
get "unbakedMissingModel"(): $UnbakedModel
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmModels$Type = ($ForgeBalmModels);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmModels_ = $ForgeBalmModels$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/$ForgeBalmRuntime" {
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$BalmEntities, $BalmEntities$Type} from "packages/net/blay09/mods/balm/api/entity/$BalmEntities"
import {$SidedProxy, $SidedProxy$Type} from "packages/net/blay09/mods/balm/api/proxy/$SidedProxy"
import {$BalmSounds, $BalmSounds$Type} from "packages/net/blay09/mods/balm/api/sound/$BalmSounds"
import {$BalmBlockEntities, $BalmBlockEntities$Type} from "packages/net/blay09/mods/balm/api/block/$BalmBlockEntities"
import {$BalmCommands, $BalmCommands$Type} from "packages/net/blay09/mods/balm/api/command/$BalmCommands"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$BalmStats, $BalmStats$Type} from "packages/net/blay09/mods/balm/api/stats/$BalmStats"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$BalmRecipes, $BalmRecipes$Type} from "packages/net/blay09/mods/balm/api/recipe/$BalmRecipes"
import {$BalmNetworking, $BalmNetworking$Type} from "packages/net/blay09/mods/balm/api/network/$BalmNetworking"
import {$BalmBlocks, $BalmBlocks$Type} from "packages/net/blay09/mods/balm/api/block/$BalmBlocks"
import {$BalmItems, $BalmItems$Type} from "packages/net/blay09/mods/balm/api/item/$BalmItems"
import {$BalmConfig, $BalmConfig$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfig"
import {$BalmEvents, $BalmEvents$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvents"
import {$BalmRegistries, $BalmRegistries$Type} from "packages/net/blay09/mods/balm/api/$BalmRegistries"
import {$BalmProviders, $BalmProviders$Type} from "packages/net/blay09/mods/balm/api/provider/$BalmProviders"
import {$BalmLootTables, $BalmLootTables$Type} from "packages/net/blay09/mods/balm/api/loot/$BalmLootTables"
import {$BalmHooks, $BalmHooks$Type} from "packages/net/blay09/mods/balm/api/$BalmHooks"
import {$BalmRuntime, $BalmRuntime$Type} from "packages/net/blay09/mods/balm/api/$BalmRuntime"
import {$BalmWorldGen, $BalmWorldGen$Type} from "packages/net/blay09/mods/balm/api/world/$BalmWorldGen"
import {$BalmMenus, $BalmMenus$Type} from "packages/net/blay09/mods/balm/api/menu/$BalmMenus"

export class $ForgeBalmRuntime implements $BalmRuntime {

constructor()

public "initialize"(arg0: string, arg1: $Runnable$Type): void
public "getProviders"(): $BalmProviders
public "getCommands"(): $BalmCommands
public "getEvents"(): $BalmEvents
public "initializeIfLoaded"(arg0: string, arg1: string): void
public "getConfig"(): $BalmConfig
public "getEntities"(): $BalmEntities
public "getStats"(): $BalmStats
public "getItems"(): $BalmItems
public "getModName"(arg0: string): string
public "isModLoaded"(arg0: string): boolean
public "getBlocks"(): $BalmBlocks
public "getWorldGen"(): $BalmWorldGen
public "getMenus"(): $BalmMenus
public "getHooks"(): $BalmHooks
public "sidedProxy"<T>(arg0: string, arg1: string): $SidedProxy<(T)>
public "getNetworking"(): $BalmNetworking
public "getLootTables"(): $BalmLootTables
public "getSounds"(): $BalmSounds
public "addServerReloadListener"(arg0: $ResourceLocation$Type, arg1: $PreparableReloadListener$Type): void
public "addServerReloadListener"(arg0: $ResourceLocation$Type, arg1: $Consumer$Type<($ResourceManager$Type)>): void
public "getRecipes"(): $BalmRecipes
public "getRegistries"(): $BalmRegistries
public "getBlockEntities"(): $BalmBlockEntities
get "providers"(): $BalmProviders
get "commands"(): $BalmCommands
get "events"(): $BalmEvents
get "config"(): $BalmConfig
get "entities"(): $BalmEntities
get "stats"(): $BalmStats
get "items"(): $BalmItems
get "blocks"(): $BalmBlocks
get "worldGen"(): $BalmWorldGen
get "menus"(): $BalmMenus
get "hooks"(): $BalmHooks
get "networking"(): $BalmNetworking
get "lootTables"(): $BalmLootTables
get "sounds"(): $BalmSounds
get "recipes"(): $BalmRecipes
get "registries"(): $BalmRegistries
get "blockEntities"(): $BalmBlockEntities
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmRuntime$Type = ($ForgeBalmRuntime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmRuntime_ = $ForgeBalmRuntime$Type;
}}
declare module "packages/net/blay09/mods/balm/api/$BalmEnvironment" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $BalmEnvironment extends $Enum<($BalmEnvironment)> {
static readonly "CLIENT": $BalmEnvironment
static readonly "SERVER": $BalmEnvironment


public static "values"(): ($BalmEnvironment)[]
public static "valueOf"(arg0: string): $BalmEnvironment
public "isClient"(): boolean
get "client"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmEnvironment$Type = (("server") | ("client")) | ($BalmEnvironment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmEnvironment_ = $BalmEnvironment$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/$ForgeBalmRuntimeFactory" {
import {$BalmRuntimeFactory, $BalmRuntimeFactory$Type} from "packages/net/blay09/mods/balm/api/$BalmRuntimeFactory"
import {$BalmRuntime, $BalmRuntime$Type} from "packages/net/blay09/mods/balm/api/$BalmRuntime"

export class $ForgeBalmRuntimeFactory implements $BalmRuntimeFactory {

constructor()

public "create"(): $BalmRuntime
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmRuntimeFactory$Type = ($ForgeBalmRuntimeFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmRuntimeFactory_ = $ForgeBalmRuntimeFactory$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/config/$DefaultOptionsConfig" {
import {$DefaultOptionsConfigData, $DefaultOptionsConfigData$Type} from "packages/net/blay09/mods/defaultoptions/config/$DefaultOptionsConfigData"

export class $DefaultOptionsConfig {

constructor()

public static "initialize"(): void
public static "getActive"(): $DefaultOptionsConfigData
get "active"(): $DefaultOptionsConfigData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultOptionsConfig$Type = ($DefaultOptionsConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultOptionsConfig_ = $DefaultOptionsConfig$Type;
}}
declare module "packages/net/blay09/mods/balm/api/container/$EmptyContainer" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ImplementedContainer, $ImplementedContainer$Type} from "packages/net/blay09/mods/balm/api/container/$ImplementedContainer"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $EmptyContainer implements $ImplementedContainer {
static readonly "INSTANCE": $EmptyContainer

constructor()

public "getItems"(): $NonNullList<($ItemStack)>
public static "of"(arg0: $NonNullList$Type<($ItemStack$Type)>): $ImplementedContainer
public "serializeInventory"(): $CompoundTag
public "setChanged"(): void
public static "deserializeInventory"(arg0: $CompoundTag$Type, arg1: integer): $NonNullList<($ItemStack)>
public "getItem"(arg0: integer): $ItemStack
public "getContainerSize"(): integer
public "removeItemNoUpdate"(arg0: integer): $ItemStack
public static "ofSize"(arg0: integer): $ImplementedContainer
public "slotChanged"(arg0: integer): void
public "removeItem"(arg0: integer, arg1: integer): $ItemStack
public "clearContent"(): void
public "isEmpty"(): boolean
public "stillValid"(arg0: $Player$Type): boolean
public "setItem"(arg0: integer, arg1: $ItemStack$Type): void
public "kjs$self"(): $Container
public "getBlock"(level: $Level$Type): $BlockContainerJS
public "startOpen"(arg0: $Player$Type): void
public "getMaxStackSize"(): integer
public "stopOpen"(arg0: $Player$Type): void
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public "canPlaceItem"(arg0: integer, arg1: $ItemStack$Type): boolean
public "countItem"(arg0: $Item$Type): integer
public "canTakeItem"(arg0: $Container$Type, arg1: integer, arg2: $ItemStack$Type): boolean
public "hasAnyMatching"(arg0: $Predicate$Type<($ItemStack$Type)>): boolean
public "getSlots"(): integer
public "getStackInSlot"(slot: integer): $ItemStack
public "insertItem"(slot: integer, stack: $ItemStack$Type, simulate: boolean): $ItemStack
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public "isMutable"(): boolean
public "hasAnyOf"(arg0: $Set$Type<($Item$Type)>): boolean
public "setChanged"(): void
public "asContainer"(): $Container
public "getHeight"(): integer
public "extractItem"(slot: integer, amount: integer, simulate: boolean): $ItemStack
public "isItemValid"(slot: integer, stack: $ItemStack$Type): boolean
public "getWidth"(): integer
public "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
public "getSlotLimit"(slot: integer): integer
public "clear"(): void
public static "tryClear"(arg0: any): void
public "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
public "countNonEmpty"(ingredient: $Ingredient$Type): integer
public "countNonEmpty"(): integer
public "getAllItems"(): $List<($ItemStack)>
public "find"(ingredient: $Ingredient$Type): integer
public "find"(): integer
public "clear"(ingredient: $Ingredient$Type): void
public "count"(ingredient: $Ingredient$Type): integer
public "count"(): integer
public "isEmpty"(): boolean
get "items"(): $NonNullList<($ItemStack)>
get "containerSize"(): integer
get "empty"(): boolean
get "maxStackSize"(): integer
get "slots"(): integer
get "mutable"(): boolean
get "height"(): integer
get "width"(): integer
get "allItems"(): $List<($ItemStack)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EmptyContainer$Type = ($EmptyContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EmptyContainer_ = $EmptyContainer$Type;
}}
declare module "packages/net/blay09/mods/balm/mixin/$MouseHandlerAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $MouseHandlerAccessor {

 "getMouseX"(): double
 "getMouseY"(): double
}

export namespace $MouseHandlerAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MouseHandlerAccessor$Type = ($MouseHandlerAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MouseHandlerAccessor_ = $MouseHandlerAccessor$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$PlayerOpenMenuEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export class $PlayerOpenMenuEvent extends $BalmEvent {

constructor()
constructor(arg0: $ServerPlayer$Type, arg1: $AbstractContainerMenu$Type)

public "getPlayer"(): $ServerPlayer
public "getMenu"(): $AbstractContainerMenu
public "getListenerList"(): $ListenerList
get "player"(): $ServerPlayer
get "menu"(): $AbstractContainerMenu
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerOpenMenuEvent$Type = ($PlayerOpenMenuEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerOpenMenuEvent_ = $PlayerOpenMenuEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/energy/$ForgeEnergyStorage" {
import {$IEnergyStorage, $IEnergyStorage$Type} from "packages/net/minecraftforge/energy/$IEnergyStorage"
import {$EnergyStorage, $EnergyStorage$Type} from "packages/net/blay09/mods/balm/api/energy/$EnergyStorage"

export class $ForgeEnergyStorage implements $IEnergyStorage {

constructor(arg0: $EnergyStorage$Type)

public "getEnergyStored"(): integer
public "getMaxEnergyStored"(): integer
public "canExtract"(): boolean
public "receiveEnergy"(arg0: integer, arg1: boolean): integer
public "canReceive"(): boolean
public "extractEnergy"(arg0: integer, arg1: boolean): integer
get "energyStored"(): integer
get "maxEnergyStored"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeEnergyStorage$Type = ($ForgeEnergyStorage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeEnergyStorage_ = $ForgeEnergyStorage$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/$ForgeBalm" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ForgeBalm {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalm$Type = ($ForgeBalm);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalm_ = $ForgeBalm$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/stats/$ForgeBalmStats" {
import {$BalmStats, $BalmStats$Type} from "packages/net/blay09/mods/balm/api/stats/$BalmStats"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ForgeBalmStats implements $BalmStats {

constructor()

public "register"(): void
public "registerCustomStat"(arg0: $ResourceLocation$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmStats$Type = ($ForgeBalmStats);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmStats_ = $ForgeBalmStats$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/config/$CraftingTweaksConfigData$Common" {
import {$List, $List$Type} from "packages/java/util/$List"

export class $CraftingTweaksConfigData$Common {
 "compressRequiresCraftingGrid": boolean
 "compressDenylist": $List<(string)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksConfigData$Common$Type = ($CraftingTweaksConfigData$Common);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksConfigData$Common_ = $CraftingTweaksConfigData$Common$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsCategory" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DefaultOptionsCategory extends $Enum<($DefaultOptionsCategory)> {
static readonly "OPTIONS": $DefaultOptionsCategory
static readonly "KEYS": $DefaultOptionsCategory
static readonly "SERVERS": $DefaultOptionsCategory


public static "values"(): ($DefaultOptionsCategory)[]
public static "valueOf"(arg0: string): $DefaultOptionsCategory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultOptionsCategory$Type = (("servers") | ("keys") | ("options")) | ($DefaultOptionsCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultOptionsCategory_ = $DefaultOptionsCategory$Type;
}}
declare module "packages/net/blay09/mods/balm/common/$BalmBlockEntity" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"
import {$BalmProviderHolder, $BalmProviderHolder$Type} from "packages/net/blay09/mods/balm/api/provider/$BalmProviderHolder"
import {$BalmProvider, $BalmProvider$Type} from "packages/net/blay09/mods/balm/api/provider/$BalmProvider"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BalmBlockEntityBase, $BalmBlockEntityBase$Type} from "packages/net/blay09/mods/balm/api/block/entity/$BalmBlockEntityBase"
import {$ClientGamePacketListener, $ClientGamePacketListener$Type} from "packages/net/minecraft/network/protocol/game/$ClientGamePacketListener"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BalmBlockEntity extends $BalmBlockEntityBase implements $BalmProviderHolder {
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "sync"(): void
public "getProvider"<T>(arg0: $Class$Type<(T)>): T
public "buildProviders"(arg0: $List$Type<(any)>): void
public "createUpdatePacket"(): $Packet<($ClientGamePacketListener)>
public "createUpdateTag"(): $CompoundTag
public "getUpdateTag"(): $CompoundTag
public "getUpdatePacket"(): $Packet<($ClientGamePacketListener)>
public "getProviders"(): $List<($BalmProvider<(any)>)>
public "getSidedProviders"(): $List<($Pair<($Direction), ($BalmProvider<(any)>)>)>
get "updateTag"(): $CompoundTag
get "updatePacket"(): $Packet<($ClientGamePacketListener)>
get "providers"(): $List<($BalmProvider<(any)>)>
get "sidedProviders"(): $List<($Pair<($Direction), ($BalmProvider<(any)>)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmBlockEntity$Type = ($BalmBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmBlockEntity_ = $BalmBlockEntity$Type;
}}
declare module "packages/net/blay09/mods/waystones/worldgen/namegen/$MrPorkNameGenerator" {
import {$INameGenerator, $INameGenerator$Type} from "packages/net/blay09/mods/waystones/worldgen/namegen/$INameGenerator"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"

export class $MrPorkNameGenerator implements $INameGenerator {

constructor()

public "randomName"(arg0: $RandomSource$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MrPorkNameGenerator$Type = ($MrPorkNameGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MrPorkNameGenerator_ = $MrPorkNameGenerator$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/keys/$DefaultKeyMapping" {
import {$KeyModifier, $KeyModifier$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$KeyModifier"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $DefaultKeyMapping {
readonly "input": $InputConstants$Key
readonly "modifier": $KeyModifier

constructor(arg0: $InputConstants$Key$Type, arg1: $KeyModifier$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultKeyMapping$Type = ($DefaultKeyMapping);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultKeyMapping_ = $DefaultKeyMapping$Type;
}}
declare module "packages/net/blay09/mods/balm/api/energy/$EnergyStorage" {
import {$Tag, $Tag$Type} from "packages/net/minecraft/nbt/$Tag"
import {$IntTag, $IntTag$Type} from "packages/net/minecraft/nbt/$IntTag"

export class $EnergyStorage {

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)
constructor(arg0: integer, arg1: integer, arg2: integer)
constructor(arg0: integer, arg1: integer)
constructor(arg0: integer)

public "fill"(arg0: integer, arg1: boolean): integer
public "drain"(arg0: integer, arg1: boolean): integer
public "getCapacity"(): integer
/**
 * 
 * @deprecated
 */
public "deserialize"(arg0: $IntTag$Type): void
public "deserialize"(arg0: $Tag$Type): void
public "serialize"(): $IntTag
public "getEnergy"(): integer
public "setEnergy"(arg0: integer): void
public "canFill"(): boolean
public "canDrain"(): boolean
get "capacity"(): integer
get "energy"(): integer
set "energy"(value: integer)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EnergyStorage$Type = ($EnergyStorage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EnergyStorage_ = $EnergyStorage$Type;
}}
declare module "packages/net/blay09/mods/balm/api/network/$SyncConfigMessage" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BalmConfigData, $BalmConfigData$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfigData"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $SyncConfigMessage<TData> {

constructor(arg0: TData)

public static "register"<TMessage extends $SyncConfigMessage<(TData)>, TData extends $BalmConfigData>(arg0: $ResourceLocation$Type, arg1: $Class$Type<(TMessage)>, arg2: $Function$Type<(TData), (TMessage)>, arg3: $Class$Type<(TData)>, arg4: $Supplier$Type<(TData)>): void
public "getData"(): TData
public static "createDeepCopyFactory"<TData>(arg0: $Supplier$Type<(TData)>, arg1: $Supplier$Type<(TData)>): $Supplier<(TData)>
public static "createEncoder"<TData, TMessage extends $SyncConfigMessage<(TData)>>(arg0: $Class$Type<(TData)>): $BiConsumer<(TMessage), ($FriendlyByteBuf)>
public static "createDecoder"<TData, TMessage extends $SyncConfigMessage<(TData)>>(arg0: $Class$Type<(any)>, arg1: $Function$Type<(TData), (TMessage)>, arg2: $Supplier$Type<(TData)>): $Function<($FriendlyByteBuf), (TMessage)>
get "data"(): TData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SyncConfigMessage$Type<TData> = ($SyncConfigMessage<(TData)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SyncConfigMessage_<TData> = $SyncConfigMessage$Type<(TData)>;
}}
declare module "packages/net/blay09/mods/waystones/network/message/$RequestEditWaystoneMessage" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $RequestEditWaystoneMessage {

constructor(arg0: $UUID$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $RequestEditWaystoneMessage
public static "encode"(arg0: $RequestEditWaystoneMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $ServerPlayer$Type, arg1: $RequestEditWaystoneMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RequestEditWaystoneMessage$Type = ($RequestEditWaystoneMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RequestEditWaystoneMessage_ = $RequestEditWaystoneMessage$Type;
}}
declare module "packages/net/blay09/mods/waystones/handler/$WaystoneActivationStatHandler" {
import {$WaystoneActivatedEvent, $WaystoneActivatedEvent$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneActivatedEvent"

export class $WaystoneActivationStatHandler {

constructor()

public static "onWaystoneActivated"(arg0: $WaystoneActivatedEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneActivationStatHandler$Type = ($WaystoneActivationStatHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneActivationStatHandler_ = $WaystoneActivationStatHandler$Type;
}}
declare module "packages/net/blay09/mods/waystones/$InternalMethodsImpl" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$WaystoneStyle, $WaystoneStyle$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneStyle"
import {$IWaystoneTeleportContext, $IWaystoneTeleportContext$Type} from "packages/net/blay09/mods/waystones/api/$IWaystoneTeleportContext"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$InternalMethods, $InternalMethods$Type} from "packages/net/blay09/mods/waystones/api/$InternalMethods"
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$WaystoneTeleportError, $WaystoneTeleportError$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneTeleportError"
import {$WarpMode, $WarpMode$Type} from "packages/net/blay09/mods/waystones/core/$WarpMode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $InternalMethodsImpl implements $InternalMethods {

constructor()

public "forceTeleportToWaystone"(arg0: $Entity$Type, arg1: $IWaystone$Type): $Either<($List<($Entity)>), ($WaystoneTeleportError)>
public "tryTeleportToWaystone"(arg0: $Entity$Type, arg1: $IWaystone$Type, arg2: $WarpMode$Type, arg3: $IWaystone$Type): $Either<($List<($Entity)>), ($WaystoneTeleportError)>
public "createDefaultTeleportContext"(arg0: $Entity$Type, arg1: $IWaystone$Type, arg2: $WarpMode$Type, arg3: $IWaystone$Type): $Either<($IWaystoneTeleportContext), ($WaystoneTeleportError)>
public "createCustomTeleportContext"(arg0: $Entity$Type, arg1: $IWaystone$Type): $Either<($IWaystoneTeleportContext), ($WaystoneTeleportError)>
public "placeSharestone"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $DyeColor$Type): $Optional<($IWaystone)>
public "placeWarpPlate"(arg0: $Level$Type, arg1: $BlockPos$Type): $Optional<($IWaystone)>
public "createAttunedShard"(arg0: $IWaystone$Type): $ItemStack
public "getBoundWaystone"(arg0: $ItemStack$Type): $Optional<($IWaystone)>
public "placeWaystone"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $WaystoneStyle$Type): $Optional<($IWaystone)>
public "setBoundWaystone"(arg0: $ItemStack$Type, arg1: $IWaystone$Type): void
public "forceTeleport"(arg0: $IWaystoneTeleportContext$Type): $List<($Entity)>
public "getWaystoneAt"(arg0: $Level$Type, arg1: $BlockPos$Type): $Optional<($IWaystone)>
public "getWaystone"(arg0: $Level$Type, arg1: $UUID$Type): $Optional<($IWaystone)>
public "tryTeleport"(arg0: $IWaystoneTeleportContext$Type): $Either<($List<($Entity)>), ($WaystoneTeleportError)>
public "createBoundScroll"(arg0: $IWaystone$Type): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalMethodsImpl$Type = ($InternalMethodsImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalMethodsImpl_ = $InternalMethodsImpl$Type;
}}
declare module "packages/net/blay09/mods/balm/api/network/$ClientboundMessageRegistration" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$MessageRegistration, $MessageRegistration$Type} from "packages/net/blay09/mods/balm/api/network/$MessageRegistration"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ClientboundMessageRegistration<T> extends $MessageRegistration<(T)> {

constructor(arg0: $ResourceLocation$Type, arg1: $Class$Type<(T)>, arg2: $BiConsumer$Type<(T), ($FriendlyByteBuf$Type)>, arg3: $Function$Type<($FriendlyByteBuf$Type), (T)>, arg4: $BiConsumer$Type<($Player$Type), (T)>)

public "getHandler"(): $BiConsumer<($Player), (T)>
get "handler"(): $BiConsumer<($Player), (T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientboundMessageRegistration$Type<T> = ($ClientboundMessageRegistration<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientboundMessageRegistration_<T> = $ClientboundMessageRegistration$Type<(T)>;
}}
declare module "packages/net/blay09/mods/balm/api/loot/$BalmLootModifier" {
import {$LootContext, $LootContext$Type} from "packages/net/minecraft/world/level/storage/loot/$LootContext"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $BalmLootModifier {

 "apply"(arg0: $LootContext$Type, arg1: $List$Type<($ItemStack$Type)>): void

(arg0: $LootContext$Type, arg1: $List$Type<($ItemStack$Type)>): void
}

export namespace $BalmLootModifier {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmLootModifier$Type = ($BalmLootModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmLootModifier_ = $BalmLootModifier$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/render/$PortstoneModel" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Model, $Model$Type} from "packages/net/minecraft/client/model/$Model"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"
import {$CubeDeformation, $CubeDeformation$Type} from "packages/net/minecraft/client/model/geom/builders/$CubeDeformation"

export class $PortstoneModel extends $Model {

constructor(arg0: $ModelPart$Type)

public static "createLayer"(arg0: $CubeDeformation$Type): $LayerDefinition
public "renderToBuffer"(arg0: $PoseStack$Type, arg1: $VertexConsumer$Type, arg2: integer, arg3: integer, arg4: float, arg5: float, arg6: float, arg7: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PortstoneModel$Type = ($PortstoneModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PortstoneModel_ = $PortstoneModel$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$GridGuiHandler" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export interface $GridGuiHandler {

 "createButtons"(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $CraftingGrid$Type, arg2: $Consumer$Type<($AbstractWidget$Type)>): void

(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $CraftingGrid$Type, arg2: $Consumer$Type<($AbstractWidget$Type)>): void
}

export namespace $GridGuiHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GridGuiHandler$Type = ($GridGuiHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GridGuiHandler_ = $GridGuiHandler$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$ClientLevelTickHandler" {
import {$ClientLevel, $ClientLevel$Type} from "packages/net/minecraft/client/multiplayer/$ClientLevel"

export interface $ClientLevelTickHandler {

 "handle"(arg0: $ClientLevel$Type): void

(arg0: $ClientLevel$Type): void
}

export namespace $ClientLevelTickHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientLevelTickHandler$Type = ($ClientLevelTickHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientLevelTickHandler_ = $ClientLevelTickHandler$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsPlugin" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $DefaultOptionsPlugin {

 "initialize"(): void

(): void
}

export namespace $DefaultOptionsPlugin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultOptionsPlugin$Type = ($DefaultOptionsPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultOptionsPlugin_ = $DefaultOptionsPlugin$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/impl/$DefaultCraftingGrid" {
import {$ButtonAlignment, $ButtonAlignment$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonAlignment"
import {$GridBalanceHandler, $GridBalanceHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridBalanceHandler"
import {$GridTransferHandler, $GridTransferHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridTransferHandler"
import {$GridClearHandler, $GridClearHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridClearHandler"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GridGuiSettings, $GridGuiSettings$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridGuiSettings"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$TweakType, $TweakType$Type} from "packages/net/blay09/mods/craftingtweaks/api/$TweakType"
import {$GridRotateHandler, $GridRotateHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridRotateHandler"
import {$ButtonStyle, $ButtonStyle$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonStyle"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$ButtonPosition, $ButtonPosition$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonPosition"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$CraftingGridDecorator, $CraftingGridDecorator$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridDecorator"

export class $DefaultCraftingGrid implements $CraftingGrid, $CraftingGridDecorator, $GridGuiSettings {

constructor(arg0: $ResourceLocation$Type, arg1: integer, arg2: integer)

public "getId"(): $ResourceLocation
public "getGridSize"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type): integer
public "getButtonAlignment"(): $ButtonAlignment
public "getButtonStyle"(): $ButtonStyle
public "getButtonPosition"(arg0: $TweakType$Type): $Optional<($ButtonPosition)>
public "isButtonVisible"(arg0: $TweakType$Type): boolean
public "clearHandler"(arg0: $GridClearHandler$Type<($AbstractContainerMenu$Type)>): $CraftingGridDecorator
public "clearHandler"(): $GridClearHandler<($AbstractContainerMenu)>
public "setButtonStyle"(arg0: $ButtonStyle$Type): $CraftingGridDecorator
public "getGridStartSlot"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type): integer
public "setButtonAlignment"(arg0: $ButtonAlignment$Type): $CraftingGridDecorator
public "setButtonAlignmentOffset"(arg0: integer, arg1: integer): $CraftingGridDecorator
public "hideAllTweakButtons"(): $CraftingGridDecorator
public "disableTweak"(arg0: $TweakType$Type): $CraftingGridDecorator
public "setButtonPosition"(arg0: $TweakType$Type, arg1: integer, arg2: integer): $CraftingGridDecorator
public "disableAllTweaks"(): $CraftingGridDecorator
public "usePhantomItems"(): $CraftingGridDecorator
public "hideTweakButton"(arg0: $TweakType$Type): $CraftingGridDecorator
public "isTweakActive"(arg0: $TweakType$Type): boolean
public "transferHandler"(arg0: $GridTransferHandler$Type<($AbstractContainerMenu$Type)>): $CraftingGridDecorator
public "transferHandler"(): $GridTransferHandler<($AbstractContainerMenu)>
public "balanceHandler"(arg0: $GridBalanceHandler$Type<($AbstractContainerMenu$Type)>): $CraftingGridDecorator
public "balanceHandler"(): $GridBalanceHandler<($AbstractContainerMenu)>
public "getButtonAlignmentOffsetX"(): integer
public "getButtonAlignmentOffsetY"(): integer
public "rotateHandler"(arg0: $GridRotateHandler$Type<($AbstractContainerMenu$Type)>): $CraftingGridDecorator
public "rotateHandler"(): $GridRotateHandler<($AbstractContainerMenu)>
public "getCraftingMatrix"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type): $Container
get "id"(): $ResourceLocation
get "buttonAlignment"(): $ButtonAlignment
get "buttonStyle"(): $ButtonStyle
set "buttonStyle"(value: $ButtonStyle$Type)
set "buttonAlignment"(value: $ButtonAlignment$Type)
get "buttonAlignmentOffsetX"(): integer
get "buttonAlignmentOffsetY"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultCraftingGrid$Type = ($DefaultCraftingGrid);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultCraftingGrid_ = $DefaultCraftingGrid$Type;
}}
declare module "packages/net/blay09/mods/waystones/network/message/$SortWaystoneMessage" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $SortWaystoneMessage {

constructor(arg0: integer, arg1: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SortWaystoneMessage
public static "encode"(arg0: $SortWaystoneMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $ServerPlayer$Type, arg1: $SortWaystoneMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SortWaystoneMessage$Type = ($SortWaystoneMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SortWaystoneMessage_ = $SortWaystoneMessage$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/$PlatformBindings" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$KeyModifier, $KeyModifier$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$KeyModifier"

export class $PlatformBindings {
static "INSTANCE": $PlatformBindings

constructor()

public "setKeyModifier"(arg0: $KeyMapping$Type, arg1: $KeyModifier$Type): void
public "setDefaultKeyModifier"(arg0: $KeyMapping$Type, arg1: $KeyModifier$Type): void
public "getDefaultKeyModifier"(arg0: $KeyMapping$Type): $KeyModifier
public "getKeyModifier"(arg0: $KeyMapping$Type): $KeyModifier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformBindings$Type = ($PlatformBindings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformBindings_ = $PlatformBindings$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/gui/screen/$SharestoneSelectionScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$WaystoneSelectionScreenBase, $WaystoneSelectionScreenBase$Type} from "packages/net/blay09/mods/waystones/client/gui/screen/$WaystoneSelectionScreenBase"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$WaystoneSelectionMenu, $WaystoneSelectionMenu$Type} from "packages/net/blay09/mods/waystones/menu/$WaystoneSelectionMenu"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export class $SharestoneSelectionScreen extends $WaystoneSelectionScreenBase {
static readonly "INVENTORY_LOCATION": $ResourceLocation
static readonly "SLOT_ITEM_BLIT_OFFSET": integer
 "imageWidth": integer
 "hoveredSlot": $Slot
 "leftPos": integer
 "topPos": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $WaystoneSelectionMenu$Type, arg1: $Inventory$Type, arg2: $Component$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SharestoneSelectionScreen$Type = ($SharestoneSelectionScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SharestoneSelectionScreen_ = $SharestoneSelectionScreen$Type;
}}
declare module "packages/net/blay09/mods/balm/api/container/$CombinedContainer" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ExtractionAwareContainer, $ExtractionAwareContainer$Type} from "packages/net/blay09/mods/balm/api/container/$ExtractionAwareContainer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $CombinedContainer implements $Container, $ExtractionAwareContainer {

constructor(...arg0: ($Container$Type)[])

public "canExtractItem"(arg0: integer): boolean
public "setChanged"(): void
public "getItem"(arg0: integer): $ItemStack
public "getContainerSize"(): integer
public "removeItemNoUpdate"(arg0: integer): $ItemStack
public "removeItem"(arg0: integer, arg1: integer): $ItemStack
public "clearContent"(): void
public "isEmpty"(): boolean
public "stillValid"(arg0: $Player$Type): boolean
public "setItem"(arg0: integer, arg1: $ItemStack$Type): void
public "kjs$self"(): $Container
public "getBlock"(level: $Level$Type): $BlockContainerJS
public "startOpen"(arg0: $Player$Type): void
public "getMaxStackSize"(): integer
public "stopOpen"(arg0: $Player$Type): void
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public "canPlaceItem"(arg0: integer, arg1: $ItemStack$Type): boolean
public "countItem"(arg0: $Item$Type): integer
public "canTakeItem"(arg0: $Container$Type, arg1: integer, arg2: $ItemStack$Type): boolean
public "hasAnyMatching"(arg0: $Predicate$Type<($ItemStack$Type)>): boolean
public "getSlots"(): integer
public "getStackInSlot"(slot: integer): $ItemStack
public "insertItem"(slot: integer, stack: $ItemStack$Type, simulate: boolean): $ItemStack
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public "isMutable"(): boolean
public "hasAnyOf"(arg0: $Set$Type<($Item$Type)>): boolean
public "setChanged"(): void
public "asContainer"(): $Container
public "getHeight"(): integer
public "extractItem"(slot: integer, amount: integer, simulate: boolean): $ItemStack
public "isItemValid"(slot: integer, stack: $ItemStack$Type): boolean
public "getWidth"(): integer
public "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
public "getSlotLimit"(slot: integer): integer
public "clear"(): void
public static "tryClear"(arg0: any): void
public "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
public "countNonEmpty"(ingredient: $Ingredient$Type): integer
public "countNonEmpty"(): integer
public "getAllItems"(): $List<($ItemStack)>
public "find"(ingredient: $Ingredient$Type): integer
public "find"(): integer
public "clear"(ingredient: $Ingredient$Type): void
public "count"(ingredient: $Ingredient$Type): integer
public "count"(): integer
public "isEmpty"(): boolean
get "containerSize"(): integer
get "empty"(): boolean
get "maxStackSize"(): integer
get "slots"(): integer
get "mutable"(): boolean
get "height"(): integer
get "width"(): integer
get "allItems"(): $List<($ItemStack)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CombinedContainer$Type = ($CombinedContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CombinedContainer_ = $CombinedContainer$Type;
}}
declare module "packages/net/blay09/mods/balm/common/client/rendering/$AbstractCachedDynamicModel" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$ItemOverrides, $ItemOverrides$Type} from "packages/net/minecraft/client/renderer/block/model/$ItemOverrides"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ItemDisplayContext, $ItemDisplayContext$Type} from "packages/net/minecraft/world/item/$ItemDisplayContext"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$BakedQuad, $BakedQuad$Type} from "packages/net/minecraft/client/renderer/block/model/$BakedQuad"
import {$Pair, $Pair$Type} from "packages/org/apache/commons/lang3/tuple/$Pair"
import {$ModelData, $ModelData$Type} from "packages/net/minecraftforge/client/model/data/$ModelData"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ItemTransforms, $ItemTransforms$Type} from "packages/net/minecraft/client/renderer/block/model/$ItemTransforms"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$ChunkRenderTypeSet, $ChunkRenderTypeSet$Type} from "packages/net/minecraftforge/client/$ChunkRenderTypeSet"
import {$BlockAndTintGetter, $BlockAndTintGetter$Type} from "packages/net/minecraft/world/level/$BlockAndTintGetter"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $AbstractCachedDynamicModel implements $BakedModel {

constructor(arg0: $ModelBakery$Type, arg1: $Function$Type<($BlockState$Type), ($ResourceLocation$Type)>, arg2: $List$Type<($Pair$Type<($Predicate$Type<($BlockState$Type)>), ($BakedModel$Type)>)>, arg3: $Function$Type<($BlockState$Type), ($Map$Type<(string), (string)>)>, arg4: $BiConsumer$Type<($BlockState$Type), ($Matrix4f$Type)>, arg5: $List$Type<($RenderType$Type)>, arg6: $ResourceLocation$Type)

public "usesBlockLight"(): boolean
public "isGui3d"(): boolean
public "getParticleIcon"(): $TextureAtlasSprite
public "getBlockRenderTypes"(arg0: $BlockState$Type, arg1: $RandomSource$Type): $List<($RenderType)>
public "getQuads"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $RandomSource$Type): $List<($BakedQuad)>
public "isCustomRenderer"(): boolean
public "getOverrides"(): $ItemOverrides
public "getTransforms"(): $ItemTransforms
public "useAmbientOcclusion"(): boolean
public "getItemRenderTypes"(arg0: $ItemStack$Type, arg1: boolean): $List<($RenderType)>
public "useAmbientOcclusion"(arg0: $BlockState$Type, arg1: $RenderType$Type): boolean
public "useAmbientOcclusion"(arg0: $BlockState$Type): boolean
public "getRenderTypes"(arg0: $ItemStack$Type, arg1: boolean): $List<($RenderType)>
public "getRenderTypes"(arg0: $BlockState$Type, arg1: $RandomSource$Type, arg2: $ModelData$Type): $ChunkRenderTypeSet
public "getRenderPasses"(arg0: $ItemStack$Type, arg1: boolean): $List<($BakedModel)>
public "applyTransform"(arg0: $ItemDisplayContext$Type, arg1: $PoseStack$Type, arg2: boolean): $BakedModel
public "getQuads"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $RandomSource$Type, arg3: $ModelData$Type, arg4: $RenderType$Type): $List<($BakedQuad)>
public "getParticleIcon"(arg0: $ModelData$Type): $TextureAtlasSprite
public "getModelData"(arg0: $BlockAndTintGetter$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $ModelData$Type): $ModelData
public "useAmbientOcclusionWithLightEmission"(arg0: $BlockState$Type, arg1: $RenderType$Type): boolean
get "gui3d"(): boolean
get "particleIcon"(): $TextureAtlasSprite
get "customRenderer"(): boolean
get "overrides"(): $ItemOverrides
get "transforms"(): $ItemTransforms
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractCachedDynamicModel$Type = ($AbstractCachedDynamicModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractCachedDynamicModel_ = $AbstractCachedDynamicModel$Type;
}}
declare module "packages/net/blay09/mods/balm/api/network/$MessageRegistration" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $MessageRegistration<T> {

constructor(arg0: $ResourceLocation$Type, arg1: $Class$Type<(T)>, arg2: $BiConsumer$Type<(T), ($FriendlyByteBuf$Type)>, arg3: $Function$Type<($FriendlyByteBuf$Type), (T)>)

public "getClazz"(): $Class<(T)>
public "getIdentifier"(): $ResourceLocation
public "getDecodeFunc"(): $Function<($FriendlyByteBuf), (T)>
public "getEncodeFunc"(): $BiConsumer<(T), ($FriendlyByteBuf)>
get "clazz"(): $Class<(T)>
get "identifier"(): $ResourceLocation
get "decodeFunc"(): $Function<($FriendlyByteBuf), (T)>
get "encodeFunc"(): $BiConsumer<(T), ($FriendlyByteBuf)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MessageRegistration$Type<T> = ($MessageRegistration<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MessageRegistration_<T> = $MessageRegistration$Type<(T)>;
}}
declare module "packages/net/blay09/mods/balm/api/client/keymappings/$KeyModifier" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $KeyModifier extends $Enum<($KeyModifier)> {
static readonly "NONE": $KeyModifier
static readonly "SHIFT": $KeyModifier
static readonly "CONTROL": $KeyModifier
static readonly "ALT": $KeyModifier


public static "values"(): ($KeyModifier)[]
public static "valueOf"(arg0: string): $KeyModifier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyModifier$Type = (("shift") | ("alt") | ("control") | ("none")) | ($KeyModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyModifier_ = $KeyModifier$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$ConnectedToServerEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $ConnectedToServerEvent extends $BalmEvent {

constructor()
constructor(arg0: $Minecraft$Type)

public "getClient"(): $Minecraft
public "getListenerList"(): $ListenerList
get "client"(): $Minecraft
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConnectedToServerEvent$Type = ($ConnectedToServerEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConnectedToServerEvent_ = $ConnectedToServerEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$DigSpeedEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"

export class $DigSpeedEvent extends $BalmEvent {

constructor(arg0: $Player$Type, arg1: $BlockState$Type, arg2: float)
constructor()

public "getState"(): $BlockState
public "getSpeedOverride"(): float
public "getSpeed"(): float
public "getPlayer"(): $Player
public "setSpeedOverride"(arg0: float): void
public "getListenerList"(): $ListenerList
get "state"(): $BlockState
get "speedOverride"(): float
get "speed"(): float
get "player"(): $Player
set "speedOverride"(value: float)
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DigSpeedEvent$Type = ($DigSpeedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DigSpeedEvent_ = $DigSpeedEvent$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$ButtonState" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ButtonState extends $Enum<($ButtonState)> {
static readonly "NORMAL": $ButtonState
static readonly "HOVER": $ButtonState
static readonly "DISABLED": $ButtonState


public static "values"(): ($ButtonState)[]
public static "valueOf"(arg0: string): $ButtonState
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ButtonState$Type = (("normal") | ("hover") | ("disabled")) | ($ButtonState);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ButtonState_ = $ButtonState$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/$ExtraDefaultOptionsHandler" {
import {$DefaultOptionsLoadStage, $DefaultOptionsLoadStage$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsLoadStage"
import {$DefaultOptionsHandler, $DefaultOptionsHandler$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsHandler"
import {$DefaultOptionsCategory, $DefaultOptionsCategory$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsCategory"

export class $ExtraDefaultOptionsHandler implements $DefaultOptionsHandler {

constructor()

public "getId"(): string
public "getLoadStage"(): $DefaultOptionsLoadStage
public "loadDefaults"(): void
public "shouldLoadDefaults"(): boolean
public "saveCurrentOptions"(): void
public "getCategory"(): $DefaultOptionsCategory
public "hasDefaults"(): boolean
public "saveCurrentOptionsAsDefault"(): void
get "id"(): string
get "loadStage"(): $DefaultOptionsLoadStage
get "category"(): $DefaultOptionsCategory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtraDefaultOptionsHandler$Type = ($ExtraDefaultOptionsHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtraDefaultOptionsHandler_ = $ExtraDefaultOptionsHandler$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridProvider" {
import {$CraftingGridBuilder, $CraftingGridBuilder$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridBuilder"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export interface $CraftingGridProvider {

 "handles"(arg0: $AbstractContainerMenu$Type): boolean
 "onInitialize"(): void
 "buildCraftingGrids"(arg0: $CraftingGridBuilder$Type, arg1: $AbstractContainerMenu$Type): void
 "requiresServerSide"(): boolean
 "getModId"(): string
}

export namespace $CraftingGridProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingGridProvider$Type = ($CraftingGridProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingGridProvider_ = $CraftingGridProvider$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$DisconnectedFromServerEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $DisconnectedFromServerEvent extends $BalmEvent {

constructor()
constructor(arg0: $Minecraft$Type)

public "getClient"(): $Minecraft
public "getListenerList"(): $ListenerList
get "client"(): $Minecraft
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DisconnectedFromServerEvent$Type = ($DisconnectedFromServerEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DisconnectedFromServerEvent_ = $DisconnectedFromServerEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/command/$ForgeBalmCommands" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"
import {$BalmCommands, $BalmCommands$Type} from "packages/net/blay09/mods/balm/api/command/$BalmCommands"

export class $ForgeBalmCommands implements $BalmCommands {

constructor()

public "register"(arg0: $Consumer$Type<($CommandDispatcher$Type<($CommandSourceStack$Type)>)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmCommands$Type = ($ForgeBalmCommands);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmCommands_ = $ForgeBalmCommands$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$ButtonAlignment" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ButtonAlignment extends $Enum<($ButtonAlignment)> {
static readonly "TOP": $ButtonAlignment
static readonly "RIGHT": $ButtonAlignment
static readonly "BOTTOM": $ButtonAlignment
static readonly "LEFT": $ButtonAlignment


public static "values"(): ($ButtonAlignment)[]
public static "valueOf"(arg0: string): $ButtonAlignment
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ButtonAlignment$Type = (("top") | ("left") | ("bottom") | ("right")) | ($ButtonAlignment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ButtonAlignment_ = $ButtonAlignment$Type;
}}
declare module "packages/net/blay09/mods/waystones/block/entity/$ModBlockEntities" {
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$WarpPlateBlockEntity, $WarpPlateBlockEntity$Type} from "packages/net/blay09/mods/waystones/block/entity/$WarpPlateBlockEntity"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$SharestoneBlockEntity, $SharestoneBlockEntity$Type} from "packages/net/blay09/mods/waystones/block/entity/$SharestoneBlockEntity"
import {$PortstoneBlockEntity, $PortstoneBlockEntity$Type} from "packages/net/blay09/mods/waystones/block/entity/$PortstoneBlockEntity"
import {$BalmBlockEntities, $BalmBlockEntities$Type} from "packages/net/blay09/mods/balm/api/block/$BalmBlockEntities"
import {$WaystoneBlockEntity, $WaystoneBlockEntity$Type} from "packages/net/blay09/mods/waystones/block/entity/$WaystoneBlockEntity"

export class $ModBlockEntities {
static "waystone": $DeferredObject<($BlockEntityType<($WaystoneBlockEntity)>)>
static "sharestone": $DeferredObject<($BlockEntityType<($SharestoneBlockEntity)>)>
static "warpPlate": $DeferredObject<($BlockEntityType<($WarpPlateBlockEntity)>)>
static "portstone": $DeferredObject<($BlockEntityType<($PortstoneBlockEntity)>)>

constructor()

public static "initialize"(arg0: $BalmBlockEntities$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModBlockEntities$Type = ($ModBlockEntities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModBlockEntities_ = $ModBlockEntities$Type;
}}
declare module "packages/net/blay09/mods/waystones/config/$DimensionalWarp" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DimensionalWarp extends $Enum<($DimensionalWarp)> {
static readonly "ALLOW": $DimensionalWarp
static readonly "GLOBAL_ONLY": $DimensionalWarp
static readonly "DENY": $DimensionalWarp


public static "values"(): ($DimensionalWarp)[]
public static "valueOf"(arg0: string): $DimensionalWarp
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DimensionalWarp$Type = (("allow") | ("deny") | ("global_only")) | ($DimensionalWarp);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DimensionalWarp_ = $DimensionalWarp$Type;
}}
declare module "packages/net/blay09/mods/waystones/config/$WaystonesConfigData$InventoryButton" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $WaystonesConfigData$InventoryButton {
 "inventoryButton": string
 "warpButtonX": integer
 "warpButtonY": integer
 "creativeWarpButtonX": integer
 "creativeWarpButtonY": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystonesConfigData$InventoryButton$Type = ($WaystonesConfigData$InventoryButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystonesConfigData$InventoryButton_ = $WaystonesConfigData$InventoryButton$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/client/$CraftingTweaksDebugger" {
import {$ContainerScreenDrawEvent$Background, $ContainerScreenDrawEvent$Background$Type} from "packages/net/blay09/mods/balm/api/event/client/screen/$ContainerScreenDrawEvent$Background"

export class $CraftingTweaksDebugger {

constructor()

public static "initialize"(): void
public static "onScreenDrawn"(arg0: $ContainerScreenDrawEvent$Background$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksDebugger$Type = ($CraftingTweaksDebugger);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksDebugger_ = $CraftingTweaksDebugger$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$WaystoneTeleportError" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $WaystoneTeleportError {

constructor()
constructor(arg0: string)

public "getTranslationKey"(): string
get "translationKey"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneTeleportError$Type = ($WaystoneTeleportError);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneTeleportError_ = $WaystoneTeleportError$Type;
}}
declare module "packages/net/blay09/mods/balm/api/container/$ContainerUtils" {
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $ContainerUtils {

constructor()

public static "copyStackWithSize"(arg0: $ItemStack$Type, arg1: integer): $ItemStack
public static "insertItemStacked"(arg0: $Container$Type, arg1: $ItemStack$Type, arg2: boolean): $ItemStack
public static "insertItem"(arg0: $Container$Type, arg1: integer, arg2: $ItemStack$Type, arg3: boolean): $ItemStack
public static "insertItem"(arg0: $Container$Type, arg1: $ItemStack$Type, arg2: boolean): $ItemStack
public static "extractItem"(arg0: $Container$Type, arg1: integer, arg2: integer, arg3: boolean): $ItemStack
public static "dropItems"(arg0: $Container$Type, arg1: $Level$Type, arg2: $BlockPos$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContainerUtils$Type = ($ContainerUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContainerUtils_ = $ContainerUtils$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/render/$SharestoneRenderer" {
import {$BlockEntityRendererProvider$Context, $BlockEntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider$Context"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$SharestoneBlockEntity, $SharestoneBlockEntity$Type} from "packages/net/blay09/mods/waystones/block/entity/$SharestoneBlockEntity"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BlockEntityRenderer, $BlockEntityRenderer$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRenderer"

export class $SharestoneRenderer implements $BlockEntityRenderer<($SharestoneBlockEntity)> {

constructor(arg0: $BlockEntityRendererProvider$Context$Type)

public "render"(arg0: $SharestoneBlockEntity$Type, arg1: float, arg2: $PoseStack$Type, arg3: $MultiBufferSource$Type, arg4: integer, arg5: integer): void
public "shouldRender"(arg0: $SharestoneBlockEntity$Type, arg1: $Vec3$Type): boolean
public "shouldRenderOffScreen"(arg0: $SharestoneBlockEntity$Type): boolean
public "getViewDistance"(): integer
get "viewDistance"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SharestoneRenderer$Type = ($SharestoneRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SharestoneRenderer_ = $SharestoneRenderer$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/command/$DefaultOptionsCommand" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $DefaultOptionsCommand {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultOptionsCommand$Type = ($DefaultOptionsCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultOptionsCommand_ = $DefaultOptionsCommand$Type;
}}
declare module "packages/net/blay09/mods/waystones/recipe/$ModRecipes" {
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$WarpPlateRecipe, $WarpPlateRecipe$Type} from "packages/net/blay09/mods/waystones/recipe/$WarpPlateRecipe"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BalmRecipes, $BalmRecipes$Type} from "packages/net/blay09/mods/balm/api/recipe/$BalmRecipes"

export class $ModRecipes {
static readonly "WARP_PLATE_RECIPE_GROUP": string
static readonly "WARP_PLATE_RECIPE_TYPE": $ResourceLocation
static "warpPlateRecipeType": $RecipeType<($WarpPlateRecipe)>
static "warpPlateRecipeSerializer": $RecipeSerializer<($WarpPlateRecipe)>

constructor()

public static "initialize"(arg0: $BalmRecipes$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModRecipes$Type = ($ModRecipes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModRecipes_ = $ModRecipes$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/client/rendering/$ForgeBalmModels$DeferredModel" {
import {$Material, $Material$Type} from "packages/net/minecraft/client/resources/model/$Material"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiFunction, $BiFunction$Type} from "packages/java/util/function/$BiFunction"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$TextureAtlasSprite, $TextureAtlasSprite$Type} from "packages/net/minecraft/client/renderer/texture/$TextureAtlasSprite"

export class $ForgeBalmModels$DeferredModel extends $DeferredObject<($BakedModel)> {

constructor(arg0: $ResourceLocation$Type)

public "resolve"(arg0: $ModelBakery$Type, arg1: $Map$Type<($ResourceLocation$Type), ($BakedModel$Type)>, arg2: $BiFunction$Type<($ResourceLocation$Type), ($Material$Type), ($TextureAtlasSprite$Type)>): $BakedModel
public "resolveAndSet"(arg0: $ModelBakery$Type, arg1: $Map$Type<($ResourceLocation$Type), ($BakedModel$Type)>, arg2: $BiFunction$Type<($ResourceLocation$Type), ($Material$Type), ($TextureAtlasSprite$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmModels$DeferredModel$Type = ($ForgeBalmModels$DeferredModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmModels$DeferredModel_ = $ForgeBalmModels$DeferredModel$Type;
}}
declare module "packages/net/blay09/mods/waystones/recipe/$WarpPlateRecipe" {
import {$InputReplacement, $InputReplacement$Type} from "packages/dev/latvian/mods/kubejs/recipe/$InputReplacement"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$RecipeSchema, $RecipeSchema$Type} from "packages/dev/latvian/mods/kubejs/recipe/schema/$RecipeSchema"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ReplacementMatch, $ReplacementMatch$Type} from "packages/dev/latvian/mods/kubejs/recipe/$ReplacementMatch"
import {$OutputReplacement, $OutputReplacement$Type} from "packages/dev/latvian/mods/kubejs/recipe/$OutputReplacement"

export class $WarpPlateRecipe implements $Recipe<($Container)> {

constructor(arg0: $ResourceLocation$Type, arg1: $ItemStack$Type, arg2: $Ingredient$Type, arg3: $NonNullList$Type<($Ingredient$Type)>)

public "getResultItem"(arg0: $RegistryAccess$Type): $ItemStack
public "getIngredients"(): $NonNullList<($Ingredient)>
public "getToastSymbol"(): $ItemStack
public "canCraftInDimensions"(arg0: integer, arg1: integer): boolean
public "getSerializer"(): $RecipeSerializer<(any)>
public "assemble"(arg0: $Container$Type, arg1: $RegistryAccess$Type): $ItemStack
public "getId"(): $ResourceLocation
public "matches"(arg0: $Container$Type, arg1: $Level$Type): boolean
public "isSpecial"(): boolean
public "getRemainingItems"(arg0: $Container$Type): $NonNullList<($ItemStack)>
public "isIncomplete"(): boolean
public "showNotification"(): boolean
public "getType"(): $ResourceLocation
public "replaceOutput"(match: $ReplacementMatch$Type, arg1: $OutputReplacement$Type): boolean
public "setGroup"(group: string): void
public "hasInput"(match: $ReplacementMatch$Type): boolean
public "getOrCreateId"(): $ResourceLocation
public "getSchema"(): $RecipeSchema
public "replaceInput"(match: $ReplacementMatch$Type, arg1: $InputReplacement$Type): boolean
public "hasOutput"(match: $ReplacementMatch$Type): boolean
public "getGroup"(): string
public "getMod"(): string
get "ingredients"(): $NonNullList<($Ingredient)>
get "toastSymbol"(): $ItemStack
get "serializer"(): $RecipeSerializer<(any)>
get "id"(): $ResourceLocation
get "special"(): boolean
get "incomplete"(): boolean
get "type"(): $ResourceLocation
set "group"(value: string)
get "orCreateId"(): $ResourceLocation
get "schema"(): $RecipeSchema
get "group"(): string
get "mod"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpPlateRecipe$Type = ($WarpPlateRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpPlateRecipe_ = $WarpPlateRecipe$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/$InternalMethodsImpl" {
import {$SimpleDefaultOptionsHandler, $SimpleDefaultOptionsHandler$Type} from "packages/net/blay09/mods/defaultoptions/api/$SimpleDefaultOptionsHandler"
import {$File, $File$Type} from "packages/java/io/$File"
import {$InternalMethods, $InternalMethods$Type} from "packages/net/blay09/mods/defaultoptions/api/$InternalMethods"
import {$DefaultOptionsHandler, $DefaultOptionsHandler$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsHandler"

export class $InternalMethodsImpl implements $InternalMethods {

constructor()

public "getDefaultOptionsFolder"(): $File
public "registerOptionsHandler"(arg0: $DefaultOptionsHandler$Type): void
public "registerOptionsFile"(arg0: $File$Type): $SimpleDefaultOptionsHandler
get "defaultOptionsFolder"(): $File
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalMethodsImpl$Type = ($InternalMethodsImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalMethodsImpl_ = $InternalMethodsImpl$Type;
}}
declare module "packages/net/blay09/mods/waystones/core/$WaystoneTeleportContext" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$WarpMode, $WarpMode$Type} from "packages/net/blay09/mods/waystones/core/$WarpMode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IWaystoneTeleportContext, $IWaystoneTeleportContext$Type} from "packages/net/blay09/mods/waystones/api/$IWaystoneTeleportContext"
import {$TeleportDestination, $TeleportDestination$Type} from "packages/net/blay09/mods/waystones/api/$TeleportDestination"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $WaystoneTeleportContext implements $IWaystoneTeleportContext {

constructor(arg0: $Entity$Type, arg1: $IWaystone$Type, arg2: $TeleportDestination$Type)

public "getLeashedEntities"(): $List<($Mob)>
public "getTargetWaystone"(): $IWaystone
public "setWarpMode"(arg0: $WarpMode$Type): void
public "setWarpItem"(arg0: $ItemStack$Type): void
public "setPlaysSound"(arg0: boolean): void
public "setFromWaystone"(arg0: $IWaystone$Type): void
public "getFromWaystone"(): $IWaystone
public "setPlaysEffect"(arg0: boolean): void
public "playsEffect"(): boolean
public "consumesWarpItem"(): boolean
public "getWarpItem"(): $ItemStack
public "getWarpMode"(): $WarpMode
public "playsSound"(): boolean
public "getXpCost"(): integer
public "setXpCost"(arg0: integer): void
public "getCooldown"(): integer
public "setCooldown"(arg0: integer): void
public "setDestination"(arg0: $TeleportDestination$Type): void
public "getEntity"(): $Entity
public "getDestination"(): $TeleportDestination
public "addAdditionalEntity"(arg0: $Entity$Type): void
public "getAdditionalEntities"(): $List<($Entity)>
public "isDimensionalTeleport"(): boolean
public "setConsumesWarpItem"(arg0: boolean): void
get "leashedEntities"(): $List<($Mob)>
get "targetWaystone"(): $IWaystone
set "warpMode"(value: $WarpMode$Type)
set "warpItem"(value: $ItemStack$Type)
set "fromWaystone"(value: $IWaystone$Type)
get "fromWaystone"(): $IWaystone
get "warpItem"(): $ItemStack
get "warpMode"(): $WarpMode
get "xpCost"(): integer
set "xpCost"(value: integer)
get "cooldown"(): integer
set "cooldown"(value: integer)
set "destination"(value: $TeleportDestination$Type)
get "entity"(): $Entity
get "destination"(): $TeleportDestination
get "additionalEntities"(): $List<($Entity)>
get "dimensionalTeleport"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneTeleportContext$Type = ($WaystoneTeleportContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneTeleportContext_ = $WaystoneTeleportContext$Type;
}}
declare module "packages/net/blay09/mods/balm/api/command/$BalmCommands" {
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export interface $BalmCommands {

 "register"(arg0: $Consumer$Type<($CommandDispatcher$Type<($CommandSourceStack$Type)>)>): void

(arg0: $Consumer$Type<($CommandDispatcher$Type<($CommandSourceStack$Type)>)>): void
}

export namespace $BalmCommands {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmCommands$Type = ($BalmCommands);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmCommands_ = $BalmCommands$Type;
}}
declare module "packages/net/blay09/mods/balm/api/provider/$BalmProviders" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $BalmProviders {

 "getProvider"<T>(arg0: $BlockEntity$Type, arg1: $Class$Type<(T)>): T
 "getProvider"<T>(arg0: $BlockEntity$Type, arg1: $Direction$Type, arg2: $Class$Type<(T)>): T
 "getProvider"<T>(arg0: $Entity$Type, arg1: $Class$Type<(T)>): T
}

export namespace $BalmProviders {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmProviders$Type = ($BalmProviders);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmProviders_ = $BalmProviders$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/impl/$DefaultGridClearHandler" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$GridClearHandler, $GridClearHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridClearHandler"

export class $DefaultGridClearHandler implements $GridClearHandler<($AbstractContainerMenu)> {

constructor()

public "clearGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: $AbstractContainerMenu$Type, arg3: boolean): void
public "setPhantomItems"(arg0: boolean): void
set "phantomItems"(value: boolean)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultGridClearHandler$Type = ($DefaultGridClearHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultGridClearHandler_ = $DefaultGridClearHandler$Type;
}}
declare module "packages/net/blay09/mods/waystones/recipe/$WarpPlateRecipe$Serializer" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$WarpPlateRecipe, $WarpPlateRecipe$Type} from "packages/net/blay09/mods/waystones/recipe/$WarpPlateRecipe"
import {$ICondition$IContext, $ICondition$IContext$Type} from "packages/net/minecraftforge/common/crafting/conditions/$ICondition$IContext"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"

export class $WarpPlateRecipe$Serializer implements $RecipeSerializer<($WarpPlateRecipe)> {


public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type): $WarpPlateRecipe
public "fromNetwork"(arg0: $ResourceLocation$Type, arg1: $FriendlyByteBuf$Type): $WarpPlateRecipe
public "toNetwork"(arg0: $FriendlyByteBuf$Type, arg1: $WarpPlateRecipe$Type): void
public static "register"<S extends $RecipeSerializer<(T)>, T extends $Recipe<(any)>>(arg0: string, arg1: S): S
public "fromJson"(arg0: $ResourceLocation$Type, arg1: $JsonObject$Type, arg2: $ICondition$IContext$Type): $WarpPlateRecipe
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpPlateRecipe$Serializer$Type = ($WarpPlateRecipe$Serializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpPlateRecipe$Serializer_ = $WarpPlateRecipe$Serializer$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$IAttunementItem" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $IAttunementItem {

 "getWaystoneAttunedTo"(arg0: $MinecraftServer$Type, arg1: $ItemStack$Type): $IWaystone
 "setWaystoneAttunedTo"(arg0: $ItemStack$Type, arg1: $IWaystone$Type): void
}

export namespace $IAttunementItem {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IAttunementItem$Type = ($IAttunementItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IAttunementItem_ = $IAttunementItem$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/event/$ForgeBalmEvents" {
import {$TickPhase, $TickPhase$Type} from "packages/net/blay09/mods/balm/api/event/$TickPhase"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$TickType, $TickType$Type} from "packages/net/blay09/mods/balm/api/event/$TickType"
import {$BalmEvents, $BalmEvents$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvents"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$EventPriority, $EventPriority$Type} from "packages/net/blay09/mods/balm/api/event/$EventPriority"
import {$EventPriority as $EventPriority$0, $EventPriority$Type as $EventPriority$0$Type} from "packages/net/minecraftforge/eventbus/api/$EventPriority"

export class $ForgeBalmEvents implements $BalmEvents {

constructor()

public "onEvent"<T>(arg0: $Class$Type<(T)>, arg1: $Consumer$Type<(T)>, arg2: $EventPriority$Type): void
public "fireEvent"<T>(arg0: T): void
public "onTickEvent"<T>(arg0: $TickType$Type<(T)>, arg1: $TickPhase$Type, arg2: T): void
public "registerTickEvent"<T>(arg0: $TickType$Type<(any)>, arg1: $TickPhase$Type, arg2: $Consumer$Type<(T)>): void
public "fireEventHandlers"<T>(arg0: $EventPriority$Type, arg1: T): void
public static "toForge"(arg0: $EventPriority$Type): $EventPriority$0
public "registerEvent"(arg0: $Class$Type<(any)>, arg1: $Consumer$Type<($EventPriority$Type)>, arg2: $Consumer$Type<(any)>): void
public "registerEvent"(arg0: $Class$Type<(any)>, arg1: $Consumer$Type<($EventPriority$Type)>): void
public "onEvent"<T>(arg0: $Class$Type<(T)>, arg1: $Consumer$Type<(T)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmEvents$Type = ($ForgeBalmEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmEvents_ = $ForgeBalmEvents$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/compat/$VanillaCraftingGridProvider" {
import {$CraftingGridBuilder, $CraftingGridBuilder$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridBuilder"
import {$CraftingGridProvider, $CraftingGridProvider$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridProvider"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export class $VanillaCraftingGridProvider implements $CraftingGridProvider {

constructor()

public "handles"(arg0: $AbstractContainerMenu$Type): boolean
public "buildCraftingGrids"(arg0: $CraftingGridBuilder$Type, arg1: $AbstractContainerMenu$Type): void
public "requiresServerSide"(): boolean
public "getModId"(): string
public "onInitialize"(): void
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VanillaCraftingGridProvider$Type = ($VanillaCraftingGridProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VanillaCraftingGridProvider_ = $VanillaCraftingGridProvider$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/client/$ITooltipProvider" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $ITooltipProvider {

 "getTooltipComponents"(): $List<($Component)>

(): $List<($Component)>
}

export namespace $ITooltipProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITooltipProvider$Type = ($ITooltipProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITooltipProvider_ = $ITooltipProvider$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/impl/$DefaultGridBalanceHandler" {
import {$GridBalanceHandler, $GridBalanceHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridBalanceHandler"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export class $DefaultGridBalanceHandler implements $GridBalanceHandler<($AbstractContainerMenu)> {

constructor()

public "spreadGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: $AbstractContainerMenu$Type): void
public "balanceGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: $AbstractContainerMenu$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultGridBalanceHandler$Type = ($DefaultGridBalanceHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultGridBalanceHandler_ = $DefaultGridBalanceHandler$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/sound/$ForgeBalmSounds" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$BalmSounds, $BalmSounds$Type} from "packages/net/blay09/mods/balm/api/sound/$BalmSounds"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ForgeBalmSounds implements $BalmSounds {

constructor()

public "register"(arg0: $ResourceLocation$Type): $DeferredObject<($SoundEvent)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmSounds$Type = ($ForgeBalmSounds);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmSounds_ = $ForgeBalmSounds$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$IWaystoneTeleportContext" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$WarpMode, $WarpMode$Type} from "packages/net/blay09/mods/waystones/core/$WarpMode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$TeleportDestination, $TeleportDestination$Type} from "packages/net/blay09/mods/waystones/api/$TeleportDestination"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $IWaystoneTeleportContext {

 "getLeashedEntities"(): $List<($Mob)>
 "getTargetWaystone"(): $IWaystone
 "setWarpMode"(arg0: $WarpMode$Type): void
 "setWarpItem"(arg0: $ItemStack$Type): void
 "setPlaysSound"(arg0: boolean): void
 "setFromWaystone"(arg0: $IWaystone$Type): void
 "getFromWaystone"(): $IWaystone
 "setPlaysEffect"(arg0: boolean): void
 "playsEffect"(): boolean
 "consumesWarpItem"(): boolean
 "getWarpItem"(): $ItemStack
 "getWarpMode"(): $WarpMode
 "playsSound"(): boolean
 "getXpCost"(): integer
 "setXpCost"(arg0: integer): void
 "getCooldown"(): integer
 "setCooldown"(arg0: integer): void
 "setDestination"(arg0: $TeleportDestination$Type): void
 "getEntity"(): $Entity
 "getDestination"(): $TeleportDestination
 "addAdditionalEntity"(arg0: $Entity$Type): void
 "getAdditionalEntities"(): $List<($Entity)>
 "isDimensionalTeleport"(): boolean
 "setConsumesWarpItem"(arg0: boolean): void
}

export namespace $IWaystoneTeleportContext {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWaystoneTeleportContext$Type = ($IWaystoneTeleportContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWaystoneTeleportContext_ = $IWaystoneTeleportContext$Type;
}}
declare module "packages/net/blay09/mods/waystones/network/message/$InventoryButtonMessage" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $InventoryButtonMessage {

constructor()

public static "decode"(arg0: $FriendlyByteBuf$Type): $InventoryButtonMessage
public static "encode"(arg0: $InventoryButtonMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $ServerPlayer$Type, arg1: $InventoryButtonMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InventoryButtonMessage$Type = ($InventoryButtonMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InventoryButtonMessage_ = $InventoryButtonMessage$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/client/$ClientProvider" {
import {$LocalPlayer, $LocalPlayer$Type} from "packages/net/minecraft/client/player/$LocalPlayer"
import {$CompressType, $CompressType$Type} from "packages/net/blay09/mods/craftingtweaks/$CompressType"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $ClientProvider {

constructor()

public "compress"(arg0: $LocalPlayer$Type, arg1: $AbstractContainerMenu$Type, arg2: $CraftingGrid$Type, arg3: $Slot$Type, arg4: $CompressType$Type): void
public "onItemCrafted"(arg0: $Container$Type): void
public "refillLastCrafted"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type, arg2: $CraftingGrid$Type, arg3: boolean): void
public "clearGrid"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type, arg2: $CraftingGrid$Type, arg3: boolean): void
public "rotateSlotId"(arg0: integer, arg1: boolean): integer
public "spreadGrid"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type, arg2: $CraftingGrid$Type): void
public "balanceGrid"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type, arg2: $CraftingGrid$Type): void
public "transferIntoGrid"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type, arg2: $CraftingGrid$Type, arg3: $Slot$Type): boolean
public "rotateIgnoresSlotId"(arg0: integer): boolean
public "rotateGrid"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type, arg2: $CraftingGrid$Type, arg3: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientProvider$Type = ($ClientProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientProvider_ = $ClientProvider$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$RenderHandEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $RenderHandEvent extends $BalmEvent {

constructor()
constructor(arg0: $InteractionHand$Type, arg1: $ItemStack$Type, arg2: float)

public "getHand"(): $InteractionHand
public "getItemStack"(): $ItemStack
public "getSwingProgress"(): float
public "getListenerList"(): $ListenerList
get "hand"(): $InteractionHand
get "itemStack"(): $ItemStack
get "swingProgress"(): float
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RenderHandEvent$Type = ($RenderHandEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RenderHandEvent_ = $RenderHandEvent$Type;
}}
declare module "packages/net/blay09/mods/waystones/block/$WarpPlateBlock" {
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$WaystoneOrigin, $WaystoneOrigin$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneOrigin"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$WarpPlateBlock$WarpPlateStatus, $WarpPlateBlock$WarpPlateStatus$Type} from "packages/net/blay09/mods/waystones/block/$WarpPlateBlock$WarpPlateStatus"
import {$DoubleBlockHalf, $DoubleBlockHalf$Type} from "packages/net/minecraft/world/level/block/state/properties/$DoubleBlockHalf"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$WaystoneBlockBase, $WaystoneBlockBase$Type} from "packages/net/blay09/mods/waystones/block/$WaystoneBlockBase"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$ChatFormatting, $ChatFormatting$Type} from "packages/net/minecraft/$ChatFormatting"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$BlockEntityTicker, $BlockEntityTicker$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityTicker"

export class $WarpPlateBlock extends $WaystoneBlockBase {
/**
 * 
 * @deprecated
 */
static readonly "ACTIVE": $BooleanProperty
static readonly "STATUS": $EnumProperty<($WarpPlateBlock$WarpPlateStatus)>
static readonly "FACING": $DirectionProperty
static readonly "HALF": $EnumProperty<($DoubleBlockHalf)>
static readonly "WATERLOGGED": $BooleanProperty
static readonly "ORIGIN": $EnumProperty<($WaystoneOrigin)>
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getTicker"<T extends $BlockEntity>(arg0: $Level$Type, arg1: $BlockState$Type, arg2: $BlockEntityType$Type<(T)>): $BlockEntityTicker<(T)>
public "playerWillDestroy"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Player$Type): void
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "entityInside"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Entity$Type): void
public "animateTick"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $RandomSource$Type): void
public static "getGalacticName"(arg0: $IWaystone$Type): $Component
public static "getColorForName"(arg0: string): $ChatFormatting
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpPlateBlock$Type = ($WarpPlateBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpPlateBlock_ = $WarpPlateBlock$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/$DefaultOptions" {
import {$File, $File$Type} from "packages/java/io/$File"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$DefaultOptionsHandler, $DefaultOptionsHandler$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsHandler"
import {$DefaultOptionsCategory, $DefaultOptionsCategory$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsCategory"

export class $DefaultOptions {
static readonly "MOD_ID": string
static readonly "logger": $Logger

constructor()

public static "initialize"(): void
public static "saveDefaultOptions"(arg0: $DefaultOptionsCategory$Type): void
public static "getDefaultOptionsFolder"(): $File
public static "addDefaultOptionsHandler"(arg0: $DefaultOptionsHandler$Type): void
public static "getDefaultOptionsHandlers"(): $List<($DefaultOptionsHandler)>
public static "getMinecraftDataDir"(): $File
get "defaultOptionsFolder"(): $File
get "defaultOptionsHandlers"(): $List<($DefaultOptionsHandler)>
get "minecraftDataDir"(): $File
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultOptions$Type = ($DefaultOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultOptions_ = $DefaultOptions$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$RecipesUpdatedEvent" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$RecipeManager, $RecipeManager$Type} from "packages/net/minecraft/world/item/crafting/$RecipeManager"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $RecipesUpdatedEvent extends $BalmEvent {

constructor()
constructor(arg0: $RecipeManager$Type, arg1: $RegistryAccess$Type)

public "getRegistryAccess"(): $RegistryAccess
public "getRecipeManager"(): $RecipeManager
public "getListenerList"(): $ListenerList
get "registryAccess"(): $RegistryAccess
get "recipeManager"(): $RecipeManager
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RecipesUpdatedEvent$Type = ($RecipesUpdatedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RecipesUpdatedEvent_ = $RecipesUpdatedEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/block/entity/$CustomRenderBoundingBox" {
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export interface $CustomRenderBoundingBox {

 "getRenderBoundingBox"(): $AABB

(): $AABB
}

export namespace $CustomRenderBoundingBox {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomRenderBoundingBox$Type = ($CustomRenderBoundingBox);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomRenderBoundingBox_ = $CustomRenderBoundingBox$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$TossItemEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $TossItemEvent extends $BalmEvent {

constructor()
constructor(arg0: $Player$Type, arg1: $ItemStack$Type)

public "getPlayer"(): $Player
public "getItemStack"(): $ItemStack
public "getListenerList"(): $ListenerList
get "player"(): $Player
get "itemStack"(): $ItemStack
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TossItemEvent$Type = ($TossItemEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TossItemEvent_ = $TossItemEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$EventPriority" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $EventPriority extends $Enum<($EventPriority)> {
static readonly "Lowest": $EventPriority
static readonly "Low": $EventPriority
static readonly "Normal": $EventPriority
static readonly "High": $EventPriority
static readonly "Highest": $EventPriority
static "values": ($EventPriority)[]


public static "values"(): ($EventPriority)[]
public static "valueOf"(arg0: string): $EventPriority
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventPriority$Type = (("normal") | ("high") | ("low") | ("highest") | ("lowest")) | ($EventPriority);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventPriority_ = $EventPriority$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/api/$SimpleDefaultOptionsHandler" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$DefaultOptionsLoadStage, $DefaultOptionsLoadStage$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsLoadStage"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$DefaultOptionsHandler, $DefaultOptionsHandler$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsHandler"
import {$DefaultOptionsCategory, $DefaultOptionsCategory$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsCategory"

export interface $SimpleDefaultOptionsHandler extends $DefaultOptionsHandler {

 "withLinePredicate"(arg0: $Predicate$Type<(string)>): $SimpleDefaultOptionsHandler
 "withLoadStage"(arg0: $DefaultOptionsLoadStage$Type): $SimpleDefaultOptionsHandler
 "withCategory"(arg0: $DefaultOptionsCategory$Type): $SimpleDefaultOptionsHandler
 "withSaveHandler"(arg0: $Runnable$Type): $SimpleDefaultOptionsHandler
 "getId"(): string
 "getLoadStage"(): $DefaultOptionsLoadStage
 "loadDefaults"(): void
 "shouldLoadDefaults"(): boolean
 "saveCurrentOptions"(): void
 "getCategory"(): $DefaultOptionsCategory
 "hasDefaults"(): boolean
 "saveCurrentOptionsAsDefault"(): void
}

export namespace $SimpleDefaultOptionsHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleDefaultOptionsHandler$Type = ($SimpleDefaultOptionsHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleDefaultOptionsHandler_ = $SimpleDefaultOptionsHandler$Type;
}}
declare module "packages/net/blay09/mods/balm/mixin/$ImageButtonAccessor" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $ImageButtonAccessor {

 "getResourceLocation"(): $ResourceLocation

(): $ResourceLocation
}

export namespace $ImageButtonAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImageButtonAccessor$Type = ($ImageButtonAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImageButtonAccessor_ = $ImageButtonAccessor$Type;
}}
declare module "packages/net/blay09/mods/waystones/config/$WaystonesConfigData$Restrictions" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$DimensionalWarp, $DimensionalWarp$Type} from "packages/net/blay09/mods/waystones/config/$DimensionalWarp"

export class $WaystonesConfigData$Restrictions {
 "restrictToCreative": boolean
 "restrictRenameToOwner": boolean
 "generatedWaystonesUnbreakable": boolean
 "transportLeashed": boolean
 "transportLeashedDimensional": boolean
 "leashedDenyList": $List<(string)>
 "dimensionalWarp": $DimensionalWarp
 "dimensionalWarpAllowList": $List<(string)>
 "dimensionalWarpDenyList": $List<(string)>
 "allowWaystoneToWaystoneTeleport": boolean
 "globalWaystoneSetupRequiresCreativeMode": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystonesConfigData$Restrictions$Type = ($WaystonesConfigData$Restrictions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystonesConfigData$Restrictions_ = $WaystonesConfigData$Restrictions$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/server/$ServerStartedEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $ServerStartedEvent extends $BalmEvent {

constructor()
constructor(arg0: $MinecraftServer$Type)

public "getServer"(): $MinecraftServer
public "getListenerList"(): $ListenerList
get "server"(): $MinecraftServer
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerStartedEvent$Type = ($ServerStartedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerStartedEvent_ = $ServerStartedEvent$Type;
}}
declare module "packages/net/blay09/mods/waystones/menu/$WaystoneSettingsMenu" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"

export class $WaystoneSettingsMenu extends $AbstractContainerMenu {
static readonly "SLOT_CLICKED_OUTSIDE": integer
static readonly "QUICKCRAFT_TYPE_CHARITABLE": integer
static readonly "QUICKCRAFT_TYPE_GREEDY": integer
static readonly "QUICKCRAFT_TYPE_CLONE": integer
static readonly "QUICKCRAFT_HEADER_START": integer
static readonly "QUICKCRAFT_HEADER_CONTINUE": integer
static readonly "QUICKCRAFT_HEADER_END": integer
static readonly "CARRIED_SLOT_SIZE": integer
readonly "lastSlots": $NonNullList<($ItemStack)>
readonly "slots": $NonNullList<($Slot)>
 "remoteSlots": $NonNullList<($ItemStack)>
 "containerId": integer

constructor(arg0: $MenuType$Type<($WaystoneSettingsMenu$Type)>, arg1: $IWaystone$Type, arg2: integer)

public "stillValid"(arg0: $Player$Type): boolean
public "getWaystone"(): $IWaystone
public "quickMoveStack"(arg0: $Player$Type, arg1: integer): $ItemStack
get "waystone"(): $IWaystone
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneSettingsMenu$Type = ($WaystoneSettingsMenu);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneSettingsMenu_ = $WaystoneSettingsMenu$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/$ModRenderers" {
import {$ModelLayerLocation, $ModelLayerLocation$Type} from "packages/net/minecraft/client/model/geom/$ModelLayerLocation"
import {$BalmRenderers, $BalmRenderers$Type} from "packages/net/blay09/mods/balm/api/client/rendering/$BalmRenderers"

export class $ModRenderers {
static "portstoneModel": $ModelLayerLocation
static "sharestoneModel": $ModelLayerLocation
static "waystoneModel": $ModelLayerLocation

constructor()

public static "initialize"(arg0: $BalmRenderers$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModRenderers$Type = ($ModRenderers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModRenderers_ = $ModRenderers$Type;
}}
declare module "packages/net/blay09/mods/balm/api/$BalmRuntimeSpi" {
import {$BalmRuntime, $BalmRuntime$Type} from "packages/net/blay09/mods/balm/api/$BalmRuntime"

export class $BalmRuntimeSpi {

constructor()

public static "create"(): $BalmRuntime
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmRuntimeSpi$Type = ($BalmRuntimeSpi);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmRuntimeSpi_ = $BalmRuntimeSpi$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$PlayerRespawnEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $PlayerRespawnEvent extends $BalmEvent {

constructor()
constructor(arg0: $ServerPlayer$Type, arg1: $ServerPlayer$Type)

public "getOldPlayer"(): $ServerPlayer
public "getNewPlayer"(): $ServerPlayer
public "getListenerList"(): $ListenerList
get "oldPlayer"(): $ServerPlayer
get "newPlayer"(): $ServerPlayer
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerRespawnEvent$Type = ($PlayerRespawnEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerRespawnEvent_ = $PlayerRespawnEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/mixin/$ScreenAccessor" {
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"

export interface $ScreenAccessor {

 "balm_getChildren"(): $List<($GuiEventListener)>
 "balm_getRenderables"(): $List<($Renderable)>
 "balm_getNarratables"(): $List<($NarratableEntry)>
}

export namespace $ScreenAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenAccessor$Type = ($ScreenAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenAccessor_ = $ScreenAccessor$Type;
}}
declare module "packages/net/blay09/mods/waystones/network/message/$UpdateWaystoneMessage" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $UpdateWaystoneMessage {

constructor(arg0: $IWaystone$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $UpdateWaystoneMessage
public static "encode"(arg0: $UpdateWaystoneMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $Player$Type, arg1: $UpdateWaystoneMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UpdateWaystoneMessage$Type = ($UpdateWaystoneMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UpdateWaystoneMessage_ = $UpdateWaystoneMessage$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$ServerPlayerTickHandler" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"

export interface $ServerPlayerTickHandler {

 "handle"(arg0: $ServerPlayer$Type): void

(arg0: $ServerPlayer$Type): void
}

export namespace $ServerPlayerTickHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerPlayerTickHandler$Type = ($ServerPlayerTickHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerPlayerTickHandler_ = $ServerPlayerTickHandler$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$GuiDrawEvent" {
import {$GuiDrawEvent$Element, $GuiDrawEvent$Element$Type} from "packages/net/blay09/mods/balm/api/event/client/$GuiDrawEvent$Element"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Window, $Window$Type} from "packages/com/mojang/blaze3d/platform/$Window"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $GuiDrawEvent extends $BalmEvent {

constructor()
constructor(arg0: $Window$Type, arg1: $GuiGraphics$Type, arg2: $GuiDrawEvent$Element$Type)

public "getElement"(): $GuiDrawEvent$Element
public "getWindow"(): $Window
public "getGuiGraphics"(): $GuiGraphics
public "getListenerList"(): $ListenerList
get "element"(): $GuiDrawEvent$Element
get "window"(): $Window
get "guiGraphics"(): $GuiGraphics
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiDrawEvent$Type = ($GuiDrawEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiDrawEvent_ = $GuiDrawEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$ChunkTrackingEvent" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"

export class $ChunkTrackingEvent {

constructor(arg0: $ServerLevel$Type, arg1: $ServerPlayer$Type, arg2: $ChunkPos$Type)

public "getLevel"(): $ServerLevel
public "getChunkPos"(): $ChunkPos
public "getPlayer"(): $ServerPlayer
get "level"(): $ServerLevel
get "chunkPos"(): $ChunkPos
get "player"(): $ServerPlayer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkTrackingEvent$Type = ($ChunkTrackingEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkTrackingEvent_ = $ChunkTrackingEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/client/$BalmClient" {
import {$BalmTextures, $BalmTextures$Type} from "packages/net/blay09/mods/balm/api/client/rendering/$BalmTextures"
import {$BalmKeyMappings, $BalmKeyMappings$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$BalmKeyMappings"
import {$BalmModels, $BalmModels$Type} from "packages/net/blay09/mods/balm/api/client/rendering/$BalmModels"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BalmScreens, $BalmScreens$Type} from "packages/net/blay09/mods/balm/api/client/screen/$BalmScreens"
import {$BalmRenderers, $BalmRenderers$Type} from "packages/net/blay09/mods/balm/api/client/rendering/$BalmRenderers"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export class $BalmClient {

constructor()

/**
 * 
 * @deprecated
 */
public static "initialize"(arg0: string): void
public static "initialize"(arg0: string, arg1: $Runnable$Type): void
public static "getModels"(): $BalmModels
public static "getScreens"(): $BalmScreens
public static "getTextures"(): $BalmTextures
/**
 * 
 * @deprecated
 */
public static "getClientPlayer"(): $Player
public static "getRenderers"(): $BalmRenderers
public static "getKeyMappings"(): $BalmKeyMappings
get "models"(): $BalmModels
get "screens"(): $BalmScreens
get "textures"(): $BalmTextures
get "clientPlayer"(): $Player
get "renderers"(): $BalmRenderers
get "keyMappings"(): $BalmKeyMappings
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmClient$Type = ($BalmClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmClient_ = $BalmClient$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$GuiDrawEvent$Element" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $GuiDrawEvent$Element extends $Enum<($GuiDrawEvent$Element)> {
static readonly "ALL": $GuiDrawEvent$Element
static readonly "HEALTH": $GuiDrawEvent$Element
static readonly "CHAT": $GuiDrawEvent$Element
static readonly "DEBUG": $GuiDrawEvent$Element
static readonly "BOSS_INFO": $GuiDrawEvent$Element
static readonly "PLAYER_LIST": $GuiDrawEvent$Element


public static "values"(): ($GuiDrawEvent$Element)[]
public static "valueOf"(arg0: string): $GuiDrawEvent$Element
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiDrawEvent$Element$Type = (("all") | ("debug") | ("chat") | ("health") | ("boss_info") | ("player_list")) | ($GuiDrawEvent$Element);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiDrawEvent$Element_ = $GuiDrawEvent$Element$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$WaystoneTeleportEvent$Pre" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$WaystoneTeleportEvent, $WaystoneTeleportEvent$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneTeleportEvent"
import {$IWaystoneTeleportContext, $IWaystoneTeleportContext$Type} from "packages/net/blay09/mods/waystones/api/$IWaystoneTeleportContext"
import {$EventResult, $EventResult$Type} from "packages/net/blay09/mods/waystones/api/$EventResult"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$TeleportDestination, $TeleportDestination$Type} from "packages/net/blay09/mods/waystones/api/$TeleportDestination"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $WaystoneTeleportEvent$Pre extends $WaystoneTeleportEvent {

constructor()
constructor(arg0: $IWaystoneTeleportContext$Type)

public "getContext"(): $IWaystoneTeleportContext
public "getXpCost"(): integer
public "setXpCost"(arg0: integer): void
public "getCooldown"(): integer
public "setCooldown"(arg0: integer): void
public "setDestination"(arg0: $ServerLevel$Type, arg1: $Vec3$Type, arg2: $Direction$Type): void
public "setConsumeItemResult"(arg0: $EventResult$Type): void
public "setDimensionalTeleportResult"(arg0: $EventResult$Type): void
public "getConsumeItemResult"(): $EventResult
public "getDimensionalTeleportResult"(): $EventResult
public "getDestination"(): $TeleportDestination
public "addAdditionalEntity"(arg0: $Entity$Type): void
public "getListenerList"(): $ListenerList
get "context"(): $IWaystoneTeleportContext
get "xpCost"(): integer
set "xpCost"(value: integer)
get "cooldown"(): integer
set "cooldown"(value: integer)
set "consumeItemResult"(value: $EventResult$Type)
set "dimensionalTeleportResult"(value: $EventResult$Type)
get "consumeItemResult"(): $EventResult
get "dimensionalTeleportResult"(): $EventResult
get "destination"(): $TeleportDestination
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneTeleportEvent$Pre$Type = ($WaystoneTeleportEvent$Pre);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneTeleportEvent$Pre_ = $WaystoneTeleportEvent$Pre$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/render/$PortstoneRenderer" {
import {$BlockEntityRendererProvider$Context, $BlockEntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider$Context"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$PortstoneBlockEntity, $PortstoneBlockEntity$Type} from "packages/net/blay09/mods/waystones/block/entity/$PortstoneBlockEntity"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$BlockEntityRenderer, $BlockEntityRenderer$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRenderer"

export class $PortstoneRenderer implements $BlockEntityRenderer<($PortstoneBlockEntity)> {

constructor(arg0: $BlockEntityRendererProvider$Context$Type)

public "render"(arg0: $PortstoneBlockEntity$Type, arg1: float, arg2: $PoseStack$Type, arg3: $MultiBufferSource$Type, arg4: integer, arg5: integer): void
public "shouldRender"(arg0: $PortstoneBlockEntity$Type, arg1: $Vec3$Type): boolean
public "shouldRenderOffScreen"(arg0: $PortstoneBlockEntity$Type): boolean
public "getViewDistance"(): integer
get "viewDistance"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PortstoneRenderer$Type = ($PortstoneRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PortstoneRenderer_ = $PortstoneRenderer$Type;
}}
declare module "packages/net/blay09/mods/balm/api/menu/$BalmMenus" {
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BalmMenuFactory, $BalmMenuFactory$Type} from "packages/net/blay09/mods/balm/api/menu/$BalmMenuFactory"

export interface $BalmMenus {

 "registerMenu"<T extends $AbstractContainerMenu>(arg0: $ResourceLocation$Type, arg1: $BalmMenuFactory$Type<(T)>): $DeferredObject<($MenuType<(T)>)>

(arg0: $ResourceLocation$Type, arg1: $BalmMenuFactory$Type<(T)>): $DeferredObject<($MenuType<(T)>)>
}

export namespace $BalmMenus {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmMenus$Type = ($BalmMenus);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmMenus_ = $BalmMenus$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/provider/$ForgeBalmProviders" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BalmProviders, $BalmProviders$Type} from "packages/net/blay09/mods/balm/api/provider/$BalmProviders"
import {$CapabilityToken, $CapabilityToken$Type} from "packages/net/minecraftforge/common/capabilities/$CapabilityToken"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ForgeBalmProviders implements $BalmProviders {

constructor()

public "register"<T>(arg0: $Class$Type<(T)>, arg1: $CapabilityToken$Type<(T)>): void
public "getProvider"<T>(arg0: $Entity$Type, arg1: $Class$Type<(T)>): T
public "getProvider"<T>(arg0: $BlockEntity$Type, arg1: $Direction$Type, arg2: $Class$Type<(T)>): T
public "getProvider"<T>(arg0: $BlockEntity$Type, arg1: $Class$Type<(T)>): T
public "getCapability"<T>(arg0: $Class$Type<(T)>): $Capability<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmProviders$Type = ($ForgeBalmProviders);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmProviders_ = $ForgeBalmProviders$Type;
}}
declare module "packages/net/blay09/mods/waystones/network/message/$SyncWaystonesConfigMessage" {
import {$SyncConfigMessage, $SyncConfigMessage$Type} from "packages/net/blay09/mods/balm/api/network/$SyncConfigMessage"
import {$WaystonesConfigData, $WaystonesConfigData$Type} from "packages/net/blay09/mods/waystones/config/$WaystonesConfigData"

export class $SyncWaystonesConfigMessage extends $SyncConfigMessage<($WaystonesConfigData)> {

constructor(arg0: $WaystonesConfigData$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SyncWaystonesConfigMessage$Type = ($SyncWaystonesConfigMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SyncWaystonesConfigMessage_ = $SyncWaystonesConfigMessage$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$InternalClientMethods" {
import {$TweakType, $TweakType$Type} from "packages/net/blay09/mods/craftingtweaks/api/$TweakType"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$ButtonStyle, $ButtonStyle$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonStyle"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$GridGuiHandler, $GridGuiHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridGuiHandler"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export interface $InternalClientMethods {

 "createTweakButton"(arg0: $CraftingGrid$Type, arg1: $AbstractContainerScreen$Type<(any)>, arg2: integer, arg3: integer, arg4: $ButtonStyle$Type, arg5: $TweakType$Type, arg6: $TweakType$Type): $Button
 "registerCraftingGridGuiHandler"<TScreen extends $AbstractContainerScreen<(TMenu)>, TMenu extends $AbstractContainerMenu>(arg0: $Class$Type<(TScreen)>, arg1: $GridGuiHandler$Type): void
}

export namespace $InternalClientMethods {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalClientMethods$Type = ($InternalClientMethods);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalClientMethods_ = $InternalClientMethods$Type;
}}
declare module "packages/net/blay09/mods/balm/api/config/$BalmConfig" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Table, $Table$Type} from "packages/com/google/common/collect/$Table"
import {$SyncConfigMessage, $SyncConfigMessage$Type} from "packages/net/blay09/mods/balm/api/network/$SyncConfigMessage"
import {$File, $File$Type} from "packages/java/io/$File"
import {$BalmConfigProperty, $BalmConfigProperty$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfigProperty"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BalmConfigData, $BalmConfigData$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfigData"

export interface $BalmConfig {

 "updateConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>, arg1: $Consumer$Type<(T)>): void
 "handleSync"<T extends $BalmConfigData>(arg0: $Player$Type, arg1: $SyncConfigMessage$Type<(T)>): void
 "getBackingConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): T
 "getActive"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): T
 "getConfigFile"(arg0: string): $File
 "saveBackingConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): void
 "registerConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>, arg1: $Function$Type<(T), ($SyncConfigMessage$Type<(T)>)>): void
 "getConfigProperties"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): $Table<(string), (string), ($BalmConfigProperty<(any)>)>
 "getConfigName"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): string
 "initializeBackingConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): T
 "getConfigDir"(): $File
 "resetToBackingConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): void
 "resetToBackingConfigs"(): void
}

export namespace $BalmConfig {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmConfig$Type = ($BalmConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmConfig_ = $BalmConfig$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$WaystoneActivatedEvent" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $WaystoneActivatedEvent extends $BalmEvent {

constructor()
constructor(arg0: $Player$Type, arg1: $IWaystone$Type)

public "getPlayer"(): $Player
public "getWaystone"(): $IWaystone
public "getListenerList"(): $ListenerList
get "player"(): $Player
get "waystone"(): $IWaystone
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneActivatedEvent$Type = ($WaystoneActivatedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneActivatedEvent_ = $WaystoneActivatedEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/server/$ServerReloadedEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$ReloadableServerResources, $ReloadableServerResources$Type} from "packages/net/minecraft/server/$ReloadableServerResources"

export class $ServerReloadedEvent extends $BalmEvent {

constructor()
constructor(arg0: $ReloadableServerResources$Type)

public "getResources"(): $ReloadableServerResources
public "getServer"(): $MinecraftServer
public "getListenerList"(): $ListenerList
get "resources"(): $ReloadableServerResources
get "server"(): $MinecraftServer
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerReloadedEvent$Type = ($ServerReloadedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerReloadedEvent_ = $ServerReloadedEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/screen/$ContainerScreenDrawEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ContainerScreenDrawEvent extends $BalmEvent {

constructor()
constructor(arg0: $Screen$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer)

public "getMouseX"(): integer
public "getMouseY"(): integer
public "getScreen"(): $Screen
public "getGuiGraphics"(): $GuiGraphics
public "getListenerList"(): $ListenerList
get "mouseX"(): integer
get "mouseY"(): integer
get "screen"(): $Screen
get "guiGraphics"(): $GuiGraphics
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContainerScreenDrawEvent$Type = ($ContainerScreenDrawEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContainerScreenDrawEvent_ = $ContainerScreenDrawEvent$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/$InventoryCraftingDecompress" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$RecipeHolder, $RecipeHolder$Type} from "packages/net/minecraft/world/inventory/$RecipeHolder"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$TransientCraftingContainer, $TransientCraftingContainer$Type} from "packages/net/minecraft/world/inventory/$TransientCraftingContainer"

export class $InventoryCraftingDecompress extends $TransientCraftingContainer implements $RecipeHolder {
readonly "menu": $AbstractContainerMenu

constructor(arg0: $AbstractContainerMenu$Type, arg1: $ItemStack$Type)

public "setRecipeUsed"(arg0: $Recipe$Type<(any)>): void
public "getRecipeUsed"(): $Recipe<(any)>
public "awardUsedRecipes"(arg0: $Player$Type, arg1: $List$Type<($ItemStack$Type)>): void
public "setRecipeUsed"(arg0: $Level$Type, arg1: $ServerPlayer$Type, arg2: $Recipe$Type<(any)>): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
set "recipeUsed"(value: $Recipe$Type<(any)>)
get "recipeUsed"(): $Recipe<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InventoryCraftingDecompress$Type = ($InventoryCraftingDecompress);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InventoryCraftingDecompress_ = $InventoryCraftingDecompress$Type;
}}
declare module "packages/net/blay09/mods/waystones/network/message/$RemoveWaystoneMessage" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $RemoveWaystoneMessage {

constructor(arg0: $UUID$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $RemoveWaystoneMessage
public static "encode"(arg0: $RemoveWaystoneMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $ServerPlayer$Type, arg1: $RemoveWaystoneMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RemoveWaystoneMessage$Type = ($RemoveWaystoneMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RemoveWaystoneMessage_ = $RemoveWaystoneMessage$Type;
}}
declare module "packages/net/blay09/mods/waystones/worldgen/namegen/$SequencedNameGenerator" {
import {$INameGenerator, $INameGenerator$Type} from "packages/net/blay09/mods/waystones/worldgen/namegen/$INameGenerator"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"

export class $SequencedNameGenerator implements $INameGenerator {

constructor(...arg0: ($INameGenerator$Type)[])

public "randomName"(arg0: $RandomSource$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SequencedNameGenerator$Type = ($SequencedNameGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SequencedNameGenerator_ = $SequencedNameGenerator$Type;
}}
declare module "packages/net/blay09/mods/waystones/config/$IWaystonesConfig" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$DimensionalWarp, $DimensionalWarp$Type} from "packages/net/blay09/mods/waystones/config/$DimensionalWarp"
import {$WorldGenStyle, $WorldGenStyle$Type} from "packages/net/blay09/mods/waystones/config/$WorldGenStyle"
import {$InventoryButtonMode, $InventoryButtonMode$Type} from "packages/net/blay09/mods/waystones/config/$InventoryButtonMode"
import {$NameGenerationMode, $NameGenerationMode$Type} from "packages/net/blay09/mods/waystones/worldgen/namegen/$NameGenerationMode"

export interface $IWaystonesConfig {

 "blocksPerXPLevel"(): integer
 "worldGenFrequency"(): integer
 "warpButtonY"(): integer
 "warpButtonX"(): integer
 "disableTextGlow"(): boolean
 "restrictToCreative"(): boolean
 "dimensionalWarp"(): $DimensionalWarp
 "transportLeashed"(): boolean
 "leashedDenyList"(): $List<(string)>
 "getInventoryButtonMode"(): $InventoryButtonMode
 "warpStoneUseTime"(): integer
 "warpStoneCooldown"(): integer
 "scrollUseTime"(): integer
 "sharestoneXpCostMultiplier"(): double
 "inventoryButtonXpCostMultiplier"(): double
 "warpStoneXpCostMultiplier"(): double
 "globalWaystoneXpCostMultiplier"(): double
 "waystoneXpCostMultiplier"(): double
 "dimensionalWarpXpCost"(): integer
 "portstoneXpCostMultiplier"(): double
 "warpPlateXpCostMultiplier"(): double
 "creativeWarpButtonX"(): integer
 "creativeWarpButtonY"(): integer
 "transportLeashedDimensional"(): boolean
 "restrictRenameToOwner"(): boolean
 "generatedWaystonesUnbreakable"(): boolean
 "dimensionalWarpDenyList"(): $List<(string)>
 "dimensionalWarpAllowList"(): $List<(string)>
 "allowWaystoneToWaystoneTeleport"(): boolean
 "inventoryButton"(): string
 "nameGenerationMode"(): $NameGenerationMode
 "spawnInVillages"(): boolean
 "worldGenStyle"(): $WorldGenStyle
 "globalWaystoneCooldownMultiplier"(): double
 "inventoryButtonCooldown"(): integer
 "minimumXpCost"(): double
 "maximumXpCost"(): double
 "forceSpawnInVillages"(): boolean
 "customWaystoneNames"(): $List<(string)>
 "xpCostPerLeashed"(): integer
 "inverseXpCost"(): boolean
 "globalWaystoneRequiresCreative"(): boolean
 "worldGenDimensionAllowList"(): $List<(string)>
 "worldGenDimensionDenyList"(): $List<(string)>
}

export namespace $IWaystonesConfig {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWaystonesConfig$Type = ($IWaystonesConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWaystonesConfig_ = $IWaystonesConfig$Type;
}}
declare module "packages/net/blay09/mods/waystones/network/message/$PlayerWaystoneCooldownsMessage" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $PlayerWaystoneCooldownsMessage {

constructor(arg0: long, arg1: long)

public static "decode"(arg0: $FriendlyByteBuf$Type): $PlayerWaystoneCooldownsMessage
public static "encode"(arg0: $PlayerWaystoneCooldownsMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $Player$Type, arg1: $PlayerWaystoneCooldownsMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerWaystoneCooldownsMessage$Type = ($PlayerWaystoneCooldownsMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerWaystoneCooldownsMessage_ = $PlayerWaystoneCooldownsMessage$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/config/$CraftingTweaksConfigData$Client" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$CraftingTweaksMode, $CraftingTweaksMode$Type} from "packages/net/blay09/mods/craftingtweaks/config/$CraftingTweaksMode"

export class $CraftingTweaksConfigData$Client {
 "rightClickCraftsStack": boolean
 "hideVanillaCraftingGuide": boolean
 "mode": $CraftingTweaksMode
 "disabledAddons": $List<(string)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksConfigData$Client$Type = ($CraftingTweaksConfigData$Client);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksConfigData$Client_ = $CraftingTweaksConfigData$Client$Type;
}}
declare module "packages/net/blay09/mods/waystones/config/$WorldGenStyle" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $WorldGenStyle extends $Enum<($WorldGenStyle)> {
static readonly "DEFAULT": $WorldGenStyle
static readonly "MOSSY": $WorldGenStyle
static readonly "SANDY": $WorldGenStyle
static readonly "BIOME": $WorldGenStyle


public static "values"(): ($WorldGenStyle)[]
public static "valueOf"(arg0: string): $WorldGenStyle
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldGenStyle$Type = (("default") | ("mossy") | ("biome") | ("sandy")) | ($WorldGenStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldGenStyle_ = $WorldGenStyle$Type;
}}
declare module "packages/net/blay09/mods/balm/api/entity/$BalmPlayer" {
import {$Pose, $Pose$Type} from "packages/net/minecraft/world/entity/$Pose"

export interface $BalmPlayer {

 "getForcedPose"(): $Pose
 "setForcedPose"(arg0: $Pose$Type): void
}

export namespace $BalmPlayer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmPlayer$Type = ($BalmPlayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmPlayer_ = $BalmPlayer$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$UseBlockEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $UseBlockEvent extends $BalmEvent {

constructor()
constructor(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type, arg3: $BlockHitResult$Type)

public "setResult"(arg0: $InteractionResult$Type): void
public "getLevel"(): $Level
public "getInteractionResult"(): $InteractionResult
public "getHand"(): $InteractionHand
public "getPlayer"(): $Player
public "getHitResult"(): $BlockHitResult
public "getListenerList"(): $ListenerList
set "result"(value: $InteractionResult$Type)
get "level"(): $Level
get "interactionResult"(): $InteractionResult
get "hand"(): $InteractionHand
get "player"(): $Player
get "hitResult"(): $BlockHitResult
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UseBlockEvent$Type = ($UseBlockEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UseBlockEvent_ = $UseBlockEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/world/$BalmWorldGen" {
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$GenerationStep$Decoration, $GenerationStep$Decoration$Type} from "packages/net/minecraft/world/level/levelgen/$GenerationStep$Decoration"
import {$BiomePredicate, $BiomePredicate$Type} from "packages/net/blay09/mods/balm/api/world/$BiomePredicate"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Feature, $Feature$Type} from "packages/net/minecraft/world/level/levelgen/feature/$Feature"
import {$PlacementModifierType, $PlacementModifierType$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifierType"

export interface $BalmWorldGen {

 "registerFeature"<T extends $Feature<(any)>>(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<(T)>): $DeferredObject<(T)>
 "addFeatureToBiomes"(arg0: $BiomePredicate$Type, arg1: $GenerationStep$Decoration$Type, arg2: $ResourceLocation$Type): void
 "registerPlacementModifier"<T extends $PlacementModifierType<(any)>>(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<(T)>): $DeferredObject<(T)>
}

export namespace $BalmWorldGen {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmWorldGen$Type = ($BalmWorldGen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmWorldGen_ = $BalmWorldGen$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/$ForgeBalmRegistries" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$BalmRegistries, $BalmRegistries$Type} from "packages/net/blay09/mods/balm/api/$BalmRegistries"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"

export class $ForgeBalmRegistries implements $BalmRegistries {

constructor()

public "getKey"(arg0: $Fluid$Type): $ResourceLocation
public "getKey"(arg0: $EntityType$Type<(any)>): $ResourceLocation
public "getKey"(arg0: $MenuType$Type<(any)>): $ResourceLocation
public "getKey"(arg0: $Block$Type): $ResourceLocation
public "getKey"(arg0: $Item$Type): $ResourceLocation
public "getAttribute"(arg0: $ResourceLocation$Type): $Attribute
public "getItem"(arg0: $ResourceLocation$Type): $Item
public "getBlock"(arg0: $ResourceLocation$Type): $Block
public "getMilkFluid"(): $Fluid
public "getItemKeys"(): $Collection<($ResourceLocation)>
public "getMobEffect"(arg0: $ResourceLocation$Type): $MobEffect
public "enableMilkFluid"(): void
public "getFluid"(arg0: $ResourceLocation$Type): $Fluid
public "getItemTag"(arg0: $ResourceLocation$Type): $TagKey<($Item)>
get "milkFluid"(): $Fluid
get "itemKeys"(): $Collection<($ResourceLocation)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmRegistries$Type = ($ForgeBalmRegistries);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmRegistries_ = $ForgeBalmRegistries$Type;
}}
declare module "packages/net/blay09/mods/waystones/network/message/$EditWaystoneMessage" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $EditWaystoneMessage {

constructor(arg0: $UUID$Type, arg1: string, arg2: boolean)

public static "decode"(arg0: $FriendlyByteBuf$Type): $EditWaystoneMessage
public static "encode"(arg0: $EditWaystoneMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $ServerPlayer$Type, arg1: $EditWaystoneMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EditWaystoneMessage$Type = ($EditWaystoneMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EditWaystoneMessage_ = $EditWaystoneMessage$Type;
}}
declare module "packages/net/blay09/mods/balm/api/$BalmHooks" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Pose, $Pose$Type} from "packages/net/minecraft/world/entity/$Pose"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$ConfiguredFeature, $ConfiguredFeature$Type} from "packages/net/minecraft/world/level/levelgen/feature/$ConfiguredFeature"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $BalmHooks {

 "growCrop"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type): boolean
 "isFakePlayer"(arg0: $Player$Type): boolean
 "canItemsStack"(arg0: $ItemStack$Type, arg1: $ItemStack$Type): boolean
 "isShield"(arg0: $ItemStack$Type): boolean
 "useFluidTank"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): boolean
 "setForcedPose"(arg0: $Player$Type, arg1: $Pose$Type): void
 "getPersistentData"(arg0: $Player$Type): $CompoundTag
 "getPersistentData"(arg0: $Entity$Type): $CompoundTag
 "getCraftingRemainingItem"(arg0: $ItemStack$Type): $ItemStack
 "curePotionEffects"(arg0: $LivingEntity$Type, arg1: $ItemStack$Type): void
 "getServer"(): $MinecraftServer
 "isRepairable"(arg0: $ItemStack$Type): boolean
 "setBurnTime"(arg0: $Item$Type, arg1: integer): void
 "getBurnTime"(arg0: $ItemStack$Type): integer
 "getColor"(arg0: $ItemStack$Type): $DyeColor
 "blockGrowFeature"(arg0: $Level$Type, arg1: $RandomSource$Type, arg2: $BlockPos$Type, arg3: $Holder$Type<($ConfiguredFeature$Type<(any), (any)>)>): boolean
 "firePlayerCraftingEvent"(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $Container$Type): void
 "getBlockReachDistance"(arg0: $Player$Type): double
}

export namespace $BalmHooks {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmHooks$Type = ($BalmHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmHooks_ = $BalmHooks$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid" {
import {$GridBalanceHandler, $GridBalanceHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridBalanceHandler"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$TweakType, $TweakType$Type} from "packages/net/blay09/mods/craftingtweaks/api/$TweakType"
import {$GridTransferHandler, $GridTransferHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridTransferHandler"
import {$GridRotateHandler, $GridRotateHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridRotateHandler"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$GridClearHandler, $GridClearHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridClearHandler"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $CraftingGrid {

 "getId"(): $ResourceLocation
 "getGridSize"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type): integer
 "clearHandler"(): $GridClearHandler<($AbstractContainerMenu)>
 "getGridStartSlot"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type): integer
 "isTweakActive"(arg0: $TweakType$Type): boolean
 "getCraftingMatrix"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type): $Container
 "transferHandler"(): $GridTransferHandler<($AbstractContainerMenu)>
 "balanceHandler"(): $GridBalanceHandler<($AbstractContainerMenu)>
 "rotateHandler"(): $GridRotateHandler<($AbstractContainerMenu)>

(): $ResourceLocation
}

export namespace $CraftingGrid {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingGrid$Type = ($CraftingGrid);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingGrid_ = $CraftingGrid$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/keys/$KeyMappingDefaultsHandler" {
import {$DefaultOptionsLoadStage, $DefaultOptionsLoadStage$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsLoadStage"
import {$DefaultOptionsHandler, $DefaultOptionsHandler$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsHandler"
import {$DefaultOptionsCategory, $DefaultOptionsCategory$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsCategory"

export class $KeyMappingDefaultsHandler implements $DefaultOptionsHandler {

constructor()

public "getId"(): string
public "getLoadStage"(): $DefaultOptionsLoadStage
public "loadDefaults"(): void
public "shouldLoadDefaults"(): boolean
public "saveCurrentOptions"(): void
public "getCategory"(): $DefaultOptionsCategory
public "hasDefaults"(): boolean
public "saveCurrentOptionsAsDefault"(): void
get "id"(): string
get "loadStage"(): $DefaultOptionsLoadStage
get "category"(): $DefaultOptionsCategory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyMappingDefaultsHandler$Type = ($KeyMappingDefaultsHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyMappingDefaultsHandler_ = $KeyMappingDefaultsHandler$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/gui/widget/$WaystoneButton" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $WaystoneButton extends $Button {
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: integer, arg1: integer, arg2: $IWaystone$Type, arg3: integer, arg4: $Button$OnPress$Type)

public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneButton$Type = ($WaystoneButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneButton_ = $WaystoneButton$Type;
}}
declare module "packages/net/blay09/mods/waystones/item/$WarpScrollItem" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IResetUseOnDamage, $IResetUseOnDamage$Type} from "packages/net/blay09/mods/waystones/api/$IResetUseOnDamage"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$ScrollItemBase, $ScrollItemBase$Type} from "packages/net/blay09/mods/waystones/item/$ScrollItemBase"
import {$InteractionResultHolder, $InteractionResultHolder$Type} from "packages/net/minecraft/world/$InteractionResultHolder"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $WarpScrollItem extends $ScrollItemBase implements $IResetUseOnDamage {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Item$Properties$Type)

public "getUseDuration"(arg0: $ItemStack$Type): integer
public "finishUsingItem"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $LivingEntity$Type): $ItemStack
public "use"(arg0: $Level$Type, arg1: $Player$Type, arg2: $InteractionHand$Type): $InteractionResultHolder<($ItemStack)>
public "isFoil"(arg0: $ItemStack$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpScrollItem$Type = ($WarpScrollItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpScrollItem_ = $WarpScrollItem$Type;
}}
declare module "packages/net/blay09/mods/balm/api/$BalmRuntime" {
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$BalmNetworking, $BalmNetworking$Type} from "packages/net/blay09/mods/balm/api/network/$BalmNetworking"
import {$BalmBlocks, $BalmBlocks$Type} from "packages/net/blay09/mods/balm/api/block/$BalmBlocks"
import {$BalmEntities, $BalmEntities$Type} from "packages/net/blay09/mods/balm/api/entity/$BalmEntities"
import {$SidedProxy, $SidedProxy$Type} from "packages/net/blay09/mods/balm/api/proxy/$SidedProxy"
import {$BalmSounds, $BalmSounds$Type} from "packages/net/blay09/mods/balm/api/sound/$BalmSounds"
import {$BalmItems, $BalmItems$Type} from "packages/net/blay09/mods/balm/api/item/$BalmItems"
import {$BalmBlockEntities, $BalmBlockEntities$Type} from "packages/net/blay09/mods/balm/api/block/$BalmBlockEntities"
import {$BalmCommands, $BalmCommands$Type} from "packages/net/blay09/mods/balm/api/command/$BalmCommands"
import {$BalmConfig, $BalmConfig$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfig"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$BalmEvents, $BalmEvents$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvents"
import {$BalmStats, $BalmStats$Type} from "packages/net/blay09/mods/balm/api/stats/$BalmStats"
import {$BalmRegistries, $BalmRegistries$Type} from "packages/net/blay09/mods/balm/api/$BalmRegistries"
import {$BalmProviders, $BalmProviders$Type} from "packages/net/blay09/mods/balm/api/provider/$BalmProviders"
import {$BalmLootTables, $BalmLootTables$Type} from "packages/net/blay09/mods/balm/api/loot/$BalmLootTables"
import {$BalmHooks, $BalmHooks$Type} from "packages/net/blay09/mods/balm/api/$BalmHooks"
import {$BalmWorldGen, $BalmWorldGen$Type} from "packages/net/blay09/mods/balm/api/world/$BalmWorldGen"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$BalmMenus, $BalmMenus$Type} from "packages/net/blay09/mods/balm/api/menu/$BalmMenus"
import {$BalmRecipes, $BalmRecipes$Type} from "packages/net/blay09/mods/balm/api/recipe/$BalmRecipes"

export interface $BalmRuntime {

 "initialize"(arg0: string, arg1: $Runnable$Type): void
 "getProviders"(): $BalmProviders
 "getCommands"(): $BalmCommands
 "getEvents"(): $BalmEvents
 "initializeIfLoaded"(arg0: string, arg1: string): void
 "getConfig"(): $BalmConfig
 "getEntities"(): $BalmEntities
 "getStats"(): $BalmStats
 "getItems"(): $BalmItems
 "getModName"(arg0: string): string
 "isModLoaded"(arg0: string): boolean
 "getBlocks"(): $BalmBlocks
 "getWorldGen"(): $BalmWorldGen
 "getMenus"(): $BalmMenus
 "getHooks"(): $BalmHooks
 "sidedProxy"<T>(arg0: string, arg1: string): $SidedProxy<(T)>
 "getNetworking"(): $BalmNetworking
 "getLootTables"(): $BalmLootTables
 "getSounds"(): $BalmSounds
 "addServerReloadListener"(arg0: $ResourceLocation$Type, arg1: $PreparableReloadListener$Type): void
 "addServerReloadListener"(arg0: $ResourceLocation$Type, arg1: $Consumer$Type<($ResourceManager$Type)>): void
 "getRecipes"(): $BalmRecipes
 "getRegistries"(): $BalmRegistries
 "getBlockEntities"(): $BalmBlockEntities
}

export namespace $BalmRuntime {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmRuntime$Type = ($BalmRuntime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmRuntime_ = $BalmRuntime$Type;
}}
declare module "packages/net/blay09/mods/balm/api/config/$AbstractBalmConfig" {
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Table, $Table$Type} from "packages/com/google/common/collect/$Table"
import {$SyncConfigMessage, $SyncConfigMessage$Type} from "packages/net/blay09/mods/balm/api/network/$SyncConfigMessage"
import {$File, $File$Type} from "packages/java/io/$File"
import {$BalmConfigProperty, $BalmConfigProperty$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfigProperty"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$BalmConfigData, $BalmConfigData$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfigData"
import {$BalmConfig, $BalmConfig$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfig"

export class $AbstractBalmConfig implements $BalmConfig {

constructor()

public "initialize"(): void
public "updateConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>, arg1: $Consumer$Type<(T)>): void
public "handleSync"<T extends $BalmConfigData>(arg0: $Player$Type, arg1: $SyncConfigMessage$Type<(T)>): void
public "getActive"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): T
public "getConfigFile"(arg0: string): $File
public "registerConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>, arg1: $Function$Type<(T), ($SyncConfigMessage$Type<(T)>)>): void
public "getConfigProperties"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): $Table<(string), (string), ($BalmConfigProperty<(any)>)>
public "getConfigName"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): string
public "getConfigSyncMessage"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): $SyncConfigMessage<(T)>
public "getConfigSyncMessageFactory"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): $Function<($BalmConfigData), ($SyncConfigMessage<($BalmConfigData)>)>
public "resetToBackingConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): void
public "resetToBackingConfigs"(): void
public "setActiveConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>, arg1: T): void
public "getBackingConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): T
public "saveBackingConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): void
public "initializeBackingConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): T
public "getConfigDir"(): $File
get "configDir"(): $File
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractBalmConfig$Type = ($AbstractBalmConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractBalmConfig_ = $AbstractBalmConfig$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/render/$WaystoneModel" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Model, $Model$Type} from "packages/net/minecraft/client/model/$Model"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"
import {$CubeDeformation, $CubeDeformation$Type} from "packages/net/minecraft/client/model/geom/builders/$CubeDeformation"

export class $WaystoneModel extends $Model {

constructor(arg0: $ModelPart$Type)

public static "createLayer"(arg0: $CubeDeformation$Type): $LayerDefinition
public "renderToBuffer"(arg0: $PoseStack$Type, arg1: $VertexConsumer$Type, arg2: integer, arg3: integer, arg4: float, arg5: float, arg6: float, arg7: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneModel$Type = ($WaystoneModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneModel_ = $WaystoneModel$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/render/$WaystoneRenderer" {
import {$BlockEntityRendererProvider$Context, $BlockEntityRendererProvider$Context$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider$Context"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"
import {$WaystoneBlockEntity, $WaystoneBlockEntity$Type} from "packages/net/blay09/mods/waystones/block/entity/$WaystoneBlockEntity"
import {$BlockEntityRenderer, $BlockEntityRenderer$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRenderer"

export class $WaystoneRenderer implements $BlockEntityRenderer<($WaystoneBlockEntity)> {

constructor(arg0: $BlockEntityRendererProvider$Context$Type)

public "render"(arg0: $WaystoneBlockEntity$Type, arg1: float, arg2: $PoseStack$Type, arg3: $MultiBufferSource$Type, arg4: integer, arg5: integer): void
public "shouldRender"(arg0: $WaystoneBlockEntity$Type, arg1: $Vec3$Type): boolean
public "shouldRenderOffScreen"(arg0: $WaystoneBlockEntity$Type): boolean
public "getViewDistance"(): integer
get "viewDistance"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneRenderer$Type = ($WaystoneRenderer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneRenderer_ = $WaystoneRenderer$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$LivingDamageEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $LivingDamageEvent extends $BalmEvent {

constructor()
constructor(arg0: $LivingEntity$Type, arg1: $DamageSource$Type, arg2: float)

public "getDamageAmount"(): float
public "setDamageAmount"(arg0: float): void
public "getDamageSource"(): $DamageSource
public "getEntity"(): $LivingEntity
public "getListenerList"(): $ListenerList
get "damageAmount"(): float
set "damageAmount"(value: float)
get "damageSource"(): $DamageSource
get "entity"(): $LivingEntity
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingDamageEvent$Type = ($LivingDamageEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingDamageEvent_ = $LivingDamageEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/config/$ExpectedType" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $ExpectedType extends $Annotation {

 "value"(): $Class<(any)>
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $ExpectedType {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExpectedType$Type = ($ExpectedType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExpectedType_ = $ExpectedType$Type;
}}
declare module "packages/net/blay09/mods/waystones/network/$ModNetworking" {
import {$BalmNetworking, $BalmNetworking$Type} from "packages/net/blay09/mods/balm/api/network/$BalmNetworking"

export class $ModNetworking {

constructor()

public static "initialize"(arg0: $BalmNetworking$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModNetworking$Type = ($ModNetworking);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModNetworking_ = $ModNetworking$Type;
}}
declare module "packages/net/blay09/mods/waystones/$Waystones" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Waystones {
static readonly "MOD_ID": string

constructor()

public static "initialize"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Waystones$Type = ($Waystones);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Waystones_ = $Waystones$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/impl/$DefaultGridGuiSettings" {
import {$GridGuiSettings, $GridGuiSettings$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridGuiSettings"
import {$ButtonAlignment, $ButtonAlignment$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonAlignment"
import {$TweakType, $TweakType$Type} from "packages/net/blay09/mods/craftingtweaks/api/$TweakType"
import {$ButtonStyle, $ButtonStyle$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonStyle"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ButtonPosition, $ButtonPosition$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonPosition"

export class $DefaultGridGuiSettings implements $GridGuiSettings {

constructor()

public "getButtonAlignment"(): $ButtonAlignment
public "getButtonStyle"(): $ButtonStyle
public "getButtonPosition"(arg0: $TweakType$Type): $Optional<($ButtonPosition)>
public "isButtonVisible"(arg0: $TweakType$Type): boolean
public "getButtonAlignmentOffsetX"(): integer
public "getButtonAlignmentOffsetY"(): integer
get "buttonAlignment"(): $ButtonAlignment
get "buttonStyle"(): $ButtonStyle
get "buttonAlignmentOffsetX"(): integer
get "buttonAlignmentOffsetY"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultGridGuiSettings$Type = ($DefaultGridGuiSettings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultGridGuiSettings_ = $DefaultGridGuiSettings$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsAPI" {
import {$SimpleDefaultOptionsHandler, $SimpleDefaultOptionsHandler$Type} from "packages/net/blay09/mods/defaultoptions/api/$SimpleDefaultOptionsHandler"
import {$InternalMethods, $InternalMethods$Type} from "packages/net/blay09/mods/defaultoptions/api/$InternalMethods"
import {$File, $File$Type} from "packages/java/io/$File"
import {$DefaultOptionsHandler, $DefaultOptionsHandler$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsHandler"

export class $DefaultOptionsAPI {
static "__internalMethods": $InternalMethods

constructor()

public static "getDefaultOptionsFolder"(): $File
public static "registerOptionsHandler"(arg0: $DefaultOptionsHandler$Type): void
public static "registerOptionsFile"(arg0: $File$Type): $SimpleDefaultOptionsHandler
get "defaultOptionsFolder"(): $File
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultOptionsAPI$Type = ($DefaultOptionsAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultOptionsAPI_ = $DefaultOptionsAPI$Type;
}}
declare module "packages/net/blay09/mods/waystones/core/$WaystoneProxy" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$TeleportDestination, $TeleportDestination$Type} from "packages/net/blay09/mods/waystones/api/$TeleportDestination"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$WaystoneOrigin, $WaystoneOrigin$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneOrigin"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$IMutableWaystone, $IMutableWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IMutableWaystone"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $WaystoneProxy implements $IWaystone, $IMutableWaystone {

constructor(arg0: $MinecraftServer$Type, arg1: $UUID$Type)

public "getName"(): string
public "setName"(arg0: string): void
public "isValid"(): boolean
public "getDimension"(): $ResourceKey<($Level)>
public "setPos"(arg0: $BlockPos$Type): void
public "isValidInLevel"(arg0: $ServerLevel$Type): boolean
public "resolveDestination"(arg0: $ServerLevel$Type): $TeleportDestination
public "setOwnerUid"(arg0: $UUID$Type): void
public "setGlobal"(arg0: boolean): void
public "isGlobal"(): boolean
public "getBackingWaystone"(): $IWaystone
public "getPos"(): $BlockPos
public "getOrigin"(): $WaystoneOrigin
public "getOwnerUid"(): $UUID
public "getWaystoneUid"(): $UUID
public "wasGenerated"(): boolean
public "isOwner"(arg0: $Player$Type): boolean
public "getWaystoneType"(): $ResourceLocation
public "setDimension"(arg0: $ResourceKey$Type<($Level$Type)>): void
public "hasName"(): boolean
public "hasOwner"(): boolean
get "name"(): string
set "name"(value: string)
get "valid"(): boolean
get "dimension"(): $ResourceKey<($Level)>
set "pos"(value: $BlockPos$Type)
set "ownerUid"(value: $UUID$Type)
set "global"(value: boolean)
get "global"(): boolean
get "backingWaystone"(): $IWaystone
get "pos"(): $BlockPos
get "origin"(): $WaystoneOrigin
get "ownerUid"(): $UUID
get "waystoneUid"(): $UUID
get "waystoneType"(): $ResourceLocation
set "dimension"(value: $ResourceKey$Type<($Level$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneProxy$Type = ($WaystoneProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneProxy_ = $WaystoneProxy$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/impl/$DefaultGridGuiHandler" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$GridGuiHandler, $GridGuiHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridGuiHandler"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $DefaultGridGuiHandler implements $GridGuiHandler {

constructor()

public "createButtons"(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $CraftingGrid$Type, arg2: $Consumer$Type<($AbstractWidget$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultGridGuiHandler$Type = ($DefaultGridGuiHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultGridGuiHandler_ = $DefaultGridGuiHandler$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$GridRotateHandler" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export interface $GridRotateHandler<TMenu extends $AbstractContainerMenu> {

 "rotateGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: TMenu, arg3: boolean): void

(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: TMenu, arg3: boolean): void
}

export namespace $GridRotateHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GridRotateHandler$Type<TMenu> = ($GridRotateHandler<(TMenu)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GridRotateHandler_<TMenu> = $GridRotateHandler$Type<(TMenu)>;
}}
declare module "packages/net/blay09/mods/balm/api/container/$BalmContainerProvider" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $BalmContainerProvider {

 "insertItemStacked"(arg0: $ItemStack$Type, arg1: boolean): $ItemStack
 "getContainer"(): $Container
 "getContainer"(arg0: $Direction$Type): $Container
 "insertItem"(arg0: $ItemStack$Type, arg1: integer, arg2: boolean): $ItemStack
 "extractItem"(arg0: integer, arg1: integer, arg2: boolean): $ItemStack
 "dropItems"(arg0: $Level$Type, arg1: $BlockPos$Type): void

(arg0: $ItemStack$Type, arg1: boolean): $ItemStack
}

export namespace $BalmContainerProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmContainerProvider$Type = ($BalmContainerProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmContainerProvider_ = $BalmContainerProvider$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/registry/$CraftingTweaksRegistrationData" {
import {$CraftingTweaksRegistrationData$TweakData, $CraftingTweaksRegistrationData$TweakData$Type} from "packages/net/blay09/mods/craftingtweaks/registry/$CraftingTweaksRegistrationData$TweakData"

export class $CraftingTweaksRegistrationData {

constructor()

public "isEnabled"(): boolean
public "getGridSize"(): integer
public "setEnabled"(arg0: boolean): void
public "setModId"(arg0: string): void
public "getButtonStyle"(): string
public "getContainerClass"(): string
public "setTweakRotate"(arg0: $CraftingTweaksRegistrationData$TweakData$Type): void
public "setHideButtons"(arg0: boolean): void
public "setButtonStyle"(arg0: string): void
public "setButtonOffsetX"(arg0: integer): void
public "setTweakClear"(arg0: $CraftingTweaksRegistrationData$TweakData$Type): void
public "setTweakBalance"(arg0: $CraftingTweaksRegistrationData$TweakData$Type): void
public "setPhantomItems"(arg0: boolean): void
public "setButtonOffsetY"(arg0: integer): void
public "setAlignToGrid"(arg0: string): void
public "setGridSize"(arg0: integer): void
public "isHideButtons"(): boolean
public "getGridSlotNumber"(): integer
public "getAlignToGrid"(): string
public "getTweakBalance"(): $CraftingTweaksRegistrationData$TweakData
public "isSilent"(): boolean
public "getButtonOffsetX"(): integer
public "getTweakClear"(): $CraftingTweaksRegistrationData$TweakData
public "getButtonOffsetY"(): integer
public "getTweakRotate"(): $CraftingTweaksRegistrationData$TweakData
public "isPhantomItems"(): boolean
public "setContainerClass"(arg0: string): void
public "setGridSlotNumber"(arg0: integer): void
public "setContainerCallbackClass"(arg0: string): void
public "setValidContainerPredicateClass"(arg0: string): void
public "setGetGridStartFunctionClass"(arg0: string): void
public "getContainerCallbackClass"(): string
public "getGetGridStartFunctionClass"(): string
public "getValidContainerPredicateClass"(): string
public "getModId"(): string
get "enabled"(): boolean
get "gridSize"(): integer
set "enabled"(value: boolean)
set "modId"(value: string)
get "buttonStyle"(): string
get "containerClass"(): string
set "tweakRotate"(value: $CraftingTweaksRegistrationData$TweakData$Type)
set "hideButtons"(value: boolean)
set "buttonStyle"(value: string)
set "buttonOffsetX"(value: integer)
set "tweakClear"(value: $CraftingTweaksRegistrationData$TweakData$Type)
set "tweakBalance"(value: $CraftingTweaksRegistrationData$TweakData$Type)
set "phantomItems"(value: boolean)
set "buttonOffsetY"(value: integer)
set "alignToGrid"(value: string)
set "gridSize"(value: integer)
get "hideButtons"(): boolean
get "gridSlotNumber"(): integer
get "alignToGrid"(): string
get "tweakBalance"(): $CraftingTweaksRegistrationData$TweakData
get "silent"(): boolean
get "buttonOffsetX"(): integer
get "tweakClear"(): $CraftingTweaksRegistrationData$TweakData
get "buttonOffsetY"(): integer
get "tweakRotate"(): $CraftingTweaksRegistrationData$TweakData
get "phantomItems"(): boolean
set "containerClass"(value: string)
set "gridSlotNumber"(value: integer)
set "containerCallbackClass"(value: string)
set "validContainerPredicateClass"(value: string)
set "getGridStartFunctionClass"(value: string)
get "containerCallbackClass"(): string
get "getGridStartFunctionClass"(): string
get "validContainerPredicateClass"(): string
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksRegistrationData$Type = ($CraftingTweaksRegistrationData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksRegistrationData_ = $CraftingTweaksRegistrationData$Type;
}}
declare module "packages/net/blay09/mods/balm/api/client/keymappings/$KeyConflictContext" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $KeyConflictContext extends $Enum<($KeyConflictContext)> {
static readonly "UNIVERSAL": $KeyConflictContext
static readonly "GUI": $KeyConflictContext
static readonly "INGAME": $KeyConflictContext


public static "values"(): ($KeyConflictContext)[]
public static "valueOf"(arg0: string): $KeyConflictContext
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyConflictContext$Type = (("gui") | ("ingame") | ("universal")) | ($KeyConflictContext);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyConflictContext_ = $KeyConflictContext$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/$DefaultOptionsInitializer" {
import {$ClientStartedEvent, $ClientStartedEvent$Type} from "packages/net/blay09/mods/balm/api/event/client/$ClientStartedEvent"

export class $DefaultOptionsInitializer {

constructor()

public static "postLoad"(arg0: $ClientStartedEvent$Type): void
public static "preLoad"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultOptionsInitializer$Type = ($DefaultOptionsInitializer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultOptionsInitializer_ = $DefaultOptionsInitializer$Type;
}}
declare module "packages/net/blay09/mods/kuma/api/$ScreenInputEvent" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export class $ScreenInputEvent extends $Record {

constructor(screen: $Screen$Type, mouseX: double, mouseY: double)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "mouseX"(): double
public "mouseY"(): double
public "screen"(): $Screen
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenInputEvent$Type = ($ScreenInputEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenInputEvent_ = $ScreenInputEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/screen/$ScreenKeyEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $ScreenKeyEvent extends $BalmEvent {

constructor()
constructor(arg0: $Screen$Type, arg1: integer, arg2: integer, arg3: integer)

public "getModifiers"(): integer
public "getKey"(): integer
public "getScreen"(): $Screen
public "getScanCode"(): integer
public "getListenerList"(): $ListenerList
get "modifiers"(): integer
get "key"(): integer
get "screen"(): $Screen
get "scanCode"(): integer
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenKeyEvent$Type = ($ScreenKeyEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenKeyEvent_ = $ScreenKeyEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$ConfigReloadedEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $ConfigReloadedEvent extends $BalmEvent {

constructor()

public "getListenerList"(): $ListenerList
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigReloadedEvent$Type = ($ConfigReloadedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigReloadedEvent_ = $ConfigReloadedEvent$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/$ForgeCraftingTweaks" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ForgeCraftingTweaks {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeCraftingTweaks$Type = ($ForgeCraftingTweaks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeCraftingTweaks_ = $ForgeCraftingTweaks$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$IFOVOnUse" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IFOVOnUse {

}

export namespace $IFOVOnUse {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IFOVOnUse$Type = ($IFOVOnUse);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IFOVOnUse_ = $IFOVOnUse$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$PlayerConnectedEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $PlayerConnectedEvent extends $BalmEvent {

constructor()
constructor(arg0: $ServerPlayer$Type)

public "getPlayer"(): $ServerPlayer
public "getListenerList"(): $ListenerList
get "player"(): $ServerPlayer
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerConnectedEvent$Type = ($PlayerConnectedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerConnectedEvent_ = $PlayerConnectedEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/mixin/$KeyMappingAccessor" {
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export interface $KeyMappingAccessor {

 "getKey"(): $InputConstants$Key

(): $InputConstants$Key
}

export namespace $KeyMappingAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyMappingAccessor$Type = ($KeyMappingAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyMappingAccessor_ = $KeyMappingAccessor$Type;
}}
declare module "packages/net/blay09/mods/balm/api/client/rendering/$BalmTextures" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $BalmTextures {

}

export namespace $BalmTextures {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmTextures$Type = ($BalmTextures);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmTextures_ = $BalmTextures$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$WaystoneStyles" {
import {$WaystoneStyle, $WaystoneStyle$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneStyle"

export class $WaystoneStyles {
static "DEFAULT": $WaystoneStyle
static "MOSSY": $WaystoneStyle
static "SANDY": $WaystoneStyle

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneStyles$Type = ($WaystoneStyles);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneStyles_ = $WaystoneStyles$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/$InventoryCraftingCompress" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$RecipeHolder, $RecipeHolder$Type} from "packages/net/minecraft/world/inventory/$RecipeHolder"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$TransientCraftingContainer, $TransientCraftingContainer$Type} from "packages/net/minecraft/world/inventory/$TransientCraftingContainer"

export class $InventoryCraftingCompress extends $TransientCraftingContainer implements $RecipeHolder {
readonly "menu": $AbstractContainerMenu

constructor(arg0: $AbstractContainerMenu$Type, arg1: integer, arg2: $ItemStack$Type)

public "setRecipeUsed"(arg0: $Recipe$Type<(any)>): void
public "getRecipeUsed"(): $Recipe<(any)>
public "awardUsedRecipes"(arg0: $Player$Type, arg1: $List$Type<($ItemStack$Type)>): void
public "setRecipeUsed"(arg0: $Level$Type, arg1: $ServerPlayer$Type, arg2: $Recipe$Type<(any)>): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public static "tryClear"(arg0: any): void
set "recipeUsed"(value: $Recipe$Type<(any)>)
get "recipeUsed"(): $Recipe<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InventoryCraftingCompress$Type = ($InventoryCraftingCompress);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InventoryCraftingCompress_ = $InventoryCraftingCompress$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$GenerateWaystoneNameEvent" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $GenerateWaystoneNameEvent extends $BalmEvent {

constructor()
constructor(arg0: $IWaystone$Type, arg1: string)

public "getName"(): string
public "setName"(arg0: string): void
public "getWaystone"(): $IWaystone
public "getListenerList"(): $ListenerList
get "name"(): string
set "name"(value: string)
get "waystone"(): $IWaystone
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GenerateWaystoneNameEvent$Type = ($GenerateWaystoneNameEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GenerateWaystoneNameEvent_ = $GenerateWaystoneNameEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/client/rendering/$ForgeBalmRenderers" {
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$BalmRenderers, $BalmRenderers$Type} from "packages/net/blay09/mods/balm/api/client/rendering/$BalmRenderers"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ItemColor, $ItemColor$Type} from "packages/net/minecraft/client/color/item/$ItemColor"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$EntityRendererProvider, $EntityRendererProvider$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ModelLayerLocation, $ModelLayerLocation$Type} from "packages/net/minecraft/client/model/geom/$ModelLayerLocation"
import {$BlockEntityRendererProvider, $BlockEntityRendererProvider$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ForgeBalmRenderers implements $BalmRenderers {

constructor()

public "register"(): void
public "registerItemColorHandler"(arg0: $ItemColor$Type, arg1: $Supplier$Type<(($ItemLike$Type)[])>): void
public "registerBlockColorHandler"(arg0: $BlockColor$Type, arg1: $Supplier$Type<(($Block$Type)[])>): void
public "registerEntityRenderer"<T extends $Entity>(arg0: $Supplier$Type<($EntityType$Type<(T)>)>, arg1: $EntityRendererProvider$Type<(any)>): void
public "registerBlockEntityRenderer"<T extends $BlockEntity>(arg0: $Supplier$Type<($BlockEntityType$Type<(T)>)>, arg1: $BlockEntityRendererProvider$Type<(any)>): void
public "setBlockRenderType"(arg0: $Supplier$Type<($Block$Type)>, arg1: $RenderType$Type): void
public "registerModel"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($LayerDefinition$Type)>): $ModelLayerLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmRenderers$Type = ($ForgeBalmRenderers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmRenderers_ = $ForgeBalmRenderers$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/gui/screen/$WaystoneSelectionScreenBase" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$WaystoneSelectionMenu, $WaystoneSelectionMenu$Type} from "packages/net/blay09/mods/waystones/menu/$WaystoneSelectionMenu"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export class $WaystoneSelectionScreenBase extends $AbstractContainerScreen<($WaystoneSelectionMenu)> {
static readonly "INVENTORY_LOCATION": $ResourceLocation
static readonly "SLOT_ITEM_BLIT_OFFSET": integer
 "imageWidth": integer
 "hoveredSlot": $Slot
 "leftPos": integer
 "topPos": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $WaystoneSelectionMenu$Type, arg1: $Inventory$Type, arg2: $Component$Type)

public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "m_7856_"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneSelectionScreenBase$Type = ($WaystoneSelectionScreenBase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneSelectionScreenBase_ = $WaystoneSelectionScreenBase$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/$ModClientEventHandlers" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModClientEventHandlers {

constructor()

public static "initialize"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModClientEventHandlers$Type = ($ModClientEventHandlers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModClientEventHandlers_ = $ModClientEventHandlers$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$PlayerChangedDimensionEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $PlayerChangedDimensionEvent extends $BalmEvent {

constructor()
constructor(arg0: $ServerPlayer$Type, arg1: $ResourceKey$Type<($Level$Type)>, arg2: $ResourceKey$Type<($Level$Type)>)

public "getPlayer"(): $ServerPlayer
public "getFromDim"(): $ResourceKey<($Level)>
public "getToDim"(): $ResourceKey<($Level)>
public "getListenerList"(): $ListenerList
get "player"(): $ServerPlayer
get "fromDim"(): $ResourceKey<($Level)>
get "toDim"(): $ResourceKey<($Level)>
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerChangedDimensionEvent$Type = ($PlayerChangedDimensionEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerChangedDimensionEvent_ = $PlayerChangedDimensionEvent$Type;
}}
declare module "packages/net/blay09/mods/kuma/api/$ManagedKeyMapping" {
import {$WorldInputEvent, $WorldInputEvent$Type} from "packages/net/blay09/mods/kuma/api/$WorldInputEvent"
import {$InputBinding, $InputBinding$Type} from "packages/net/blay09/mods/kuma/api/$InputBinding"
import {$ScreenInputEvent, $ScreenInputEvent$Type} from "packages/net/blay09/mods/kuma/api/$ScreenInputEvent"

export interface $ManagedKeyMapping {

 "matchesKey"(arg0: integer, arg1: integer, arg2: integer): boolean
 "isActiveAndMatchesMouse"(arg0: integer): boolean
 "isActiveAndMatchesKey"(arg0: integer, arg1: integer, arg2: integer): boolean
 "areModifiersActive"(): boolean
 "isContextActive"(): boolean
 "isActiveAndDown"(): boolean
 "setBinding"(arg0: $InputBinding$Type): void
 "matchesMouse"(arg0: integer): boolean
 "handleWorldInput"(arg0: $WorldInputEvent$Type): boolean
 "handleScreenInput"(arg0: $ScreenInputEvent$Type): boolean
 "getBinding"(): $InputBinding
 "isDown"(): boolean
}

export namespace $ManagedKeyMapping {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ManagedKeyMapping$Type = ($ManagedKeyMapping);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ManagedKeyMapping_ = $ManagedKeyMapping$Type;
}}
declare module "packages/net/blay09/mods/waystones/config/$WaystonesConfigData" {
import {$WaystonesConfigData$Compatibility, $WaystonesConfigData$Compatibility$Type} from "packages/net/blay09/mods/waystones/config/$WaystonesConfigData$Compatibility"
import {$WaystonesConfigData$WorldGen, $WaystonesConfigData$WorldGen$Type} from "packages/net/blay09/mods/waystones/config/$WaystonesConfigData$WorldGen"
import {$WaystonesConfigData$Cooldowns, $WaystonesConfigData$Cooldowns$Type} from "packages/net/blay09/mods/waystones/config/$WaystonesConfigData$Cooldowns"
import {$WaystonesConfigData$Client, $WaystonesConfigData$Client$Type} from "packages/net/blay09/mods/waystones/config/$WaystonesConfigData$Client"
import {$WaystonesConfigData$XpCost, $WaystonesConfigData$XpCost$Type} from "packages/net/blay09/mods/waystones/config/$WaystonesConfigData$XpCost"
import {$BalmConfigData, $BalmConfigData$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfigData"
import {$WaystonesConfigData$Restrictions, $WaystonesConfigData$Restrictions$Type} from "packages/net/blay09/mods/waystones/config/$WaystonesConfigData$Restrictions"
import {$WaystonesConfigData$InventoryButton, $WaystonesConfigData$InventoryButton$Type} from "packages/net/blay09/mods/waystones/config/$WaystonesConfigData$InventoryButton"
import {$InventoryButtonMode, $InventoryButtonMode$Type} from "packages/net/blay09/mods/waystones/config/$InventoryButtonMode"

export class $WaystonesConfigData implements $BalmConfigData {
 "xpCost": $WaystonesConfigData$XpCost
 "restrictions": $WaystonesConfigData$Restrictions
 "cooldowns": $WaystonesConfigData$Cooldowns
 "inventoryButton": $WaystonesConfigData$InventoryButton
 "worldGen": $WaystonesConfigData$WorldGen
 "client": $WaystonesConfigData$Client
 "compatibility": $WaystonesConfigData$Compatibility

constructor()

public "getInventoryButtonMode"(): $InventoryButtonMode
get "inventoryButtonMode"(): $InventoryButtonMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystonesConfigData$Type = ($WaystonesConfigData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystonesConfigData_ = $WaystonesConfigData$Type;
}}
declare module "packages/net/blay09/mods/waystones/block/entity/$WaystoneBlockEntity" {
import {$BalmMenuProvider, $BalmMenuProvider$Type} from "packages/net/blay09/mods/balm/api/menu/$BalmMenuProvider"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$WaystoneBlockEntityBase, $WaystoneBlockEntityBase$Type} from "packages/net/blay09/mods/waystones/block/entity/$WaystoneBlockEntityBase"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $WaystoneBlockEntity extends $WaystoneBlockEntityBase {
 "blockState": $BlockState

constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "getSettingsMenuProvider"(): $BalmMenuProvider
public "getMenuProvider"(): $BalmMenuProvider
get "settingsMenuProvider"(): $BalmMenuProvider
get "menuProvider"(): $BalmMenuProvider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneBlockEntity$Type = ($WaystoneBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneBlockEntity_ = $WaystoneBlockEntity$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/menu/$ForgeBalmMenus" {
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BalmMenuFactory, $BalmMenuFactory$Type} from "packages/net/blay09/mods/balm/api/menu/$BalmMenuFactory"
import {$BalmMenus, $BalmMenus$Type} from "packages/net/blay09/mods/balm/api/menu/$BalmMenus"

export class $ForgeBalmMenus implements $BalmMenus {

constructor()

public "registerMenu"<T extends $AbstractContainerMenu>(arg0: $ResourceLocation$Type, arg1: $BalmMenuFactory$Type<(T)>): $DeferredObject<($MenuType<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmMenus$Type = ($ForgeBalmMenus);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmMenus_ = $ForgeBalmMenus$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$TickType" {
import {$ClientLevelTickHandler, $ClientLevelTickHandler$Type} from "packages/net/blay09/mods/balm/api/event/client/$ClientLevelTickHandler"
import {$ServerPlayerTickHandler, $ServerPlayerTickHandler$Type} from "packages/net/blay09/mods/balm/api/event/$ServerPlayerTickHandler"
import {$ClientTickHandler, $ClientTickHandler$Type} from "packages/net/blay09/mods/balm/api/event/client/$ClientTickHandler"
import {$ServerTickHandler, $ServerTickHandler$Type} from "packages/net/blay09/mods/balm/api/event/$ServerTickHandler"
import {$ServerLevelTickHandler, $ServerLevelTickHandler$Type} from "packages/net/blay09/mods/balm/api/event/$ServerLevelTickHandler"

export class $TickType<T> {
static readonly "Client": $TickType<($ClientTickHandler)>
static readonly "ClientLevel": $TickType<($ClientLevelTickHandler)>
static readonly "Server": $TickType<($ServerTickHandler)>
static readonly "ServerLevel": $TickType<($ServerLevelTickHandler)>
static readonly "ServerPlayer": $TickType<($ServerPlayerTickHandler)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickType$Type<T> = ($TickType<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickType_<T> = $TickType$Type<(T)>;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$InternalMethods" {
import {$GridBalanceHandler, $GridBalanceHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridBalanceHandler"
import {$GridRotateHandler, $GridRotateHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridRotateHandler"
import {$GridTransferHandler, $GridTransferHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridTransferHandler"
import {$CraftingGridProvider, $CraftingGridProvider$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridProvider"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$GridClearHandler, $GridClearHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridClearHandler"

export interface $InternalMethods {

 "defaultFourByFourRotateHandler"(): $GridRotateHandler<($AbstractContainerMenu)>
 "defaultRectangularRotateHandler"(): $GridRotateHandler<($AbstractContainerMenu)>
 "defaultClearHandler"(): $GridClearHandler<($AbstractContainerMenu)>
 "defaultBalanceHandler"(): $GridBalanceHandler<($AbstractContainerMenu)>
 "defaultTransferHandler"(): $GridTransferHandler<($AbstractContainerMenu)>
 "defaultRotateHandler"(): $GridRotateHandler<($AbstractContainerMenu)>
 "unregisterCraftingGridProvider"(arg0: $CraftingGridProvider$Type): void
 "registerCraftingGridProvider"(arg0: $CraftingGridProvider$Type): void
}

export namespace $InternalMethods {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalMethods$Type = ($InternalMethods);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalMethods_ = $InternalMethods$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/$CraftingTweaksProviderManager" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$CraftingGridProvider, $CraftingGridProvider$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridProvider"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $CraftingTweaksProviderManager {

constructor()

public static "registerProvider"(arg0: $CraftingGridProvider$Type): void
public static "unregisterProvider"(arg0: $CraftingGridProvider$Type): void
public static "getDefaultCraftingGrid"(arg0: $AbstractContainerMenu$Type): $Optional<($CraftingGrid)>
public static "getCraftingGrid"(arg0: $AbstractContainerMenu$Type, arg1: $ResourceLocation$Type): $Optional<($CraftingGrid)>
public static "getCraftingGrids"(arg0: $AbstractContainerMenu$Type): $List<($CraftingGrid)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksProviderManager$Type = ($CraftingTweaksProviderManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksProviderManager_ = $CraftingTweaksProviderManager$Type;
}}
declare module "packages/net/blay09/mods/waystones/block/$PortstoneBlock" {
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PathComputationType, $PathComputationType$Type} from "packages/net/minecraft/world/level/pathfinder/$PathComputationType"
import {$WaystoneOrigin, $WaystoneOrigin$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneOrigin"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$DoubleBlockHalf, $DoubleBlockHalf$Type} from "packages/net/minecraft/world/level/block/state/properties/$DoubleBlockHalf"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$WaystoneBlockBase, $WaystoneBlockBase$Type} from "packages/net/blay09/mods/waystones/block/$WaystoneBlockBase"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"

export class $PortstoneBlock extends $WaystoneBlockBase {
static readonly "FACING": $DirectionProperty
static readonly "HALF": $EnumProperty<($DoubleBlockHalf)>
static readonly "WATERLOGGED": $BooleanProperty
static readonly "ORIGIN": $EnumProperty<($WaystoneOrigin)>
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "appendHoverText"(arg0: $ItemStack$Type, arg1: $BlockGetter$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
public "isPathfindable"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $PathComputationType$Type): boolean
public "use"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): $InteractionResult
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PortstoneBlock$Type = ($PortstoneBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PortstoneBlock_ = $PortstoneBlock$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$PlayerLoginEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $PlayerLoginEvent extends $BalmEvent {

constructor()
constructor(arg0: $ServerPlayer$Type)

public "getPlayer"(): $ServerPlayer
public "getListenerList"(): $ListenerList
get "player"(): $ServerPlayer
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerLoginEvent$Type = ($PlayerLoginEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerLoginEvent_ = $PlayerLoginEvent$Type;
}}
declare module "packages/net/blay09/mods/waystones/worldgen/$WaystonePlacement" {
import {$Heightmap$Types, $Heightmap$Types$Type} from "packages/net/minecraft/world/level/levelgen/$Heightmap$Types"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$PlacementContext, $PlacementContext$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementContext"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$PlacementModifier, $PlacementModifier$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifier"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$PlacementModifierType, $PlacementModifierType$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifierType"

export class $WaystonePlacement extends $PlacementModifier {
static readonly "CODEC": $Codec<($WaystonePlacement)>

constructor()
constructor(arg0: $Heightmap$Types$Type)

public "getPositions"(arg0: $PlacementContext$Type, arg1: $RandomSource$Type, arg2: $BlockPos$Type): $Stream<($BlockPos)>
public "type"(): $PlacementModifierType<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystonePlacement$Type = ($WaystonePlacement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystonePlacement_ = $WaystonePlacement$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$GridGuiSettings" {
import {$ButtonAlignment, $ButtonAlignment$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonAlignment"
import {$TweakType, $TweakType$Type} from "packages/net/blay09/mods/craftingtweaks/api/$TweakType"
import {$ButtonStyle, $ButtonStyle$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonStyle"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ButtonPosition, $ButtonPosition$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonPosition"

export interface $GridGuiSettings {

 "getButtonAlignment"(): $ButtonAlignment
 "getButtonStyle"(): $ButtonStyle
 "getButtonPosition"(arg0: $TweakType$Type): $Optional<($ButtonPosition)>
 "isButtonVisible"(arg0: $TweakType$Type): boolean
 "getButtonAlignmentOffsetX"(): integer
 "getButtonAlignmentOffsetY"(): integer
}

export namespace $GridGuiSettings {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GridGuiSettings$Type = ($GridGuiSettings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GridGuiSettings_ = $GridGuiSettings$Type;
}}
declare module "packages/net/blay09/mods/kuma/mixin/$KeyMappingAccessor" {
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export interface $KeyMappingAccessor {

 "getKey"(): $InputConstants$Key

(): $InputConstants$Key
}

export namespace $KeyMappingAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyMappingAccessor$Type = ($KeyMappingAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyMappingAccessor_ = $KeyMappingAccessor$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/difficulty/$UnobfuscatedDifficulty" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Difficulty, $Difficulty$Type} from "packages/net/minecraft/world/$Difficulty"

export class $UnobfuscatedDifficulty extends $Enum<($UnobfuscatedDifficulty)> {
static readonly "PEACEFUL": $UnobfuscatedDifficulty
static readonly "EASY": $UnobfuscatedDifficulty
static readonly "NORMAL": $UnobfuscatedDifficulty
static readonly "HARD": $UnobfuscatedDifficulty


public static "values"(): ($UnobfuscatedDifficulty)[]
public static "valueOf"(arg0: string): $UnobfuscatedDifficulty
public "toDifficulty"(): $Difficulty
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UnobfuscatedDifficulty$Type = (("normal") | ("hard") | ("easy") | ("peaceful")) | ($UnobfuscatedDifficulty);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UnobfuscatedDifficulty_ = $UnobfuscatedDifficulty$Type;
}}
declare module "packages/net/blay09/mods/waystones/block/$WaystoneBlockBase" {
import {$LevelAccessor, $LevelAccessor$Type} from "packages/net/minecraft/world/level/$LevelAccessor"
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$SimpleWaterloggedBlock, $SimpleWaterloggedBlock$Type} from "packages/net/minecraft/world/level/block/$SimpleWaterloggedBlock"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Mirror, $Mirror$Type} from "packages/net/minecraft/world/level/block/$Mirror"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$FluidState, $FluidState$Type} from "packages/net/minecraft/world/level/material/$FluidState"
import {$WaystoneOrigin, $WaystoneOrigin$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneOrigin"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$DoubleBlockHalf, $DoubleBlockHalf$Type} from "packages/net/minecraft/world/level/block/state/properties/$DoubleBlockHalf"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$BlockPlaceContext, $BlockPlaceContext$Type} from "packages/net/minecraft/world/item/context/$BlockPlaceContext"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$LevelReader, $LevelReader$Type} from "packages/net/minecraft/world/level/$LevelReader"
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BaseEntityBlock, $BaseEntityBlock$Type} from "packages/net/minecraft/world/level/block/$BaseEntityBlock"
import {$Rotation, $Rotation$Type} from "packages/net/minecraft/world/level/block/$Rotation"
import {$RenderShape, $RenderShape$Type} from "packages/net/minecraft/world/level/block/$RenderShape"

export class $WaystoneBlockBase extends $BaseEntityBlock implements $SimpleWaterloggedBlock {
static readonly "FACING": $DirectionProperty
static readonly "HALF": $EnumProperty<($DoubleBlockHalf)>
static readonly "WATERLOGGED": $BooleanProperty
static readonly "ORIGIN": $EnumProperty<($WaystoneOrigin)>
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "getStateForPlacement"(arg0: $BlockPlaceContext$Type): $BlockState
public "playerDestroy"(arg0: $Level$Type, arg1: $Player$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type, arg4: $BlockEntity$Type, arg5: $ItemStack$Type): void
public "setPlacedBy"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $LivingEntity$Type, arg4: $ItemStack$Type): void
public "playerWillDestroy"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Player$Type): void
public "appendHoverText"(arg0: $ItemStack$Type, arg1: $BlockGetter$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
public "updateShape"(arg0: $BlockState$Type, arg1: $Direction$Type, arg2: $BlockState$Type, arg3: $LevelAccessor$Type, arg4: $BlockPos$Type, arg5: $BlockPos$Type): $BlockState
public "use"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): $InteractionResult
public "getRenderShape"(arg0: $BlockState$Type): $RenderShape
public "onRemove"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type, arg4: boolean): void
public "getFluidState"(arg0: $BlockState$Type): $FluidState
public "rotate"(arg0: $BlockState$Type, arg1: $Rotation$Type): $BlockState
public "mirror"(arg0: $BlockState$Type, arg1: $Mirror$Type): $BlockState
public "canSurvive"(arg0: $BlockState$Type, arg1: $LevelReader$Type, arg2: $BlockPos$Type): boolean
public "getDestroyProgress"(arg0: $BlockState$Type, arg1: $Player$Type, arg2: $BlockGetter$Type, arg3: $BlockPos$Type): float
public "getPickupSound"(): $Optional<($SoundEvent)>
public "canPlaceLiquid"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $Fluid$Type): boolean
public "placeLiquid"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type, arg3: $FluidState$Type): boolean
public "pickupBlock"(arg0: $LevelAccessor$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type): $ItemStack
public "getPickupSound"(arg0: $BlockState$Type): $Optional<($SoundEvent)>
get "pickupSound"(): $Optional<($SoundEvent)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneBlockBase$Type = ($WaystoneBlockBase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneBlockBase_ = $WaystoneBlockBase$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsHandler" {
import {$DefaultOptionsLoadStage, $DefaultOptionsLoadStage$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsLoadStage"
import {$DefaultOptionsCategory, $DefaultOptionsCategory$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsCategory"

export interface $DefaultOptionsHandler {

 "getId"(): string
 "getLoadStage"(): $DefaultOptionsLoadStage
 "loadDefaults"(): void
 "shouldLoadDefaults"(): boolean
 "saveCurrentOptions"(): void
 "getCategory"(): $DefaultOptionsCategory
 "hasDefaults"(): boolean
 "saveCurrentOptionsAsDefault"(): void
}

export namespace $DefaultOptionsHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultOptionsHandler$Type = ($DefaultOptionsHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultOptionsHandler_ = $DefaultOptionsHandler$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/$CompressType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $CompressType extends $Enum<($CompressType)> {
static readonly "COMPRESS_ONE": $CompressType
static readonly "COMPRESS_STACK": $CompressType
static readonly "COMPRESS_ALL": $CompressType
static readonly "DECOMPRESS_ONE": $CompressType
static readonly "DECOMPRESS_STACK": $CompressType
static readonly "DECOMPRESS_ALL": $CompressType


public static "values"(): ($CompressType)[]
public static "valueOf"(arg0: string): $CompressType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompressType$Type = (("compress_one") | ("decompress_all") | ("compress_all") | ("decompress_one") | ("decompress_stack") | ("compress_stack")) | ($CompressType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompressType_ = $CompressType$Type;
}}
declare module "packages/net/blay09/mods/waystones/worldgen/namegen/$MixedNameGenerator" {
import {$INameGenerator, $INameGenerator$Type} from "packages/net/blay09/mods/waystones/worldgen/namegen/$INameGenerator"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"

export class $MixedNameGenerator implements $INameGenerator {

constructor(...arg0: ($INameGenerator$Type)[])

public "randomName"(arg0: $RandomSource$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixedNameGenerator$Type = ($MixedNameGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixedNameGenerator_ = $MixedNameGenerator$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/event/$ForgeBalmCommonEvents" {
import {$ForgeBalmEvents, $ForgeBalmEvents$Type} from "packages/net/blay09/mods/balm/forge/event/$ForgeBalmEvents"

export class $ForgeBalmCommonEvents {

constructor()

public static "registerEvents"(arg0: $ForgeBalmEvents$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmCommonEvents$Type = ($ForgeBalmCommonEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmCommonEvents_ = $ForgeBalmCommonEvents$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/$InventoryButtonGuiHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $InventoryButtonGuiHandler {

constructor()

public static "initialize"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InventoryButtonGuiHandler$Type = ($InventoryButtonGuiHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InventoryButtonGuiHandler_ = $InventoryButtonGuiHandler$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/gui/widget/$ITooltipProvider" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $ITooltipProvider {

 "getTooltipComponents"(): $List<($Component)>
 "shouldShowTooltip"(): boolean
}

export namespace $ITooltipProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ITooltipProvider$Type = ($ITooltipProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ITooltipProvider_ = $ITooltipProvider$Type;
}}
declare module "packages/net/blay09/mods/balm/api/provider/$ProviderUtils" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $ProviderUtils {

constructor()

public static "getProvider"<T>(arg0: $BlockEntity$Type, arg1: $Class$Type<(T)>): T
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ProviderUtils$Type = ($ProviderUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ProviderUtils_ = $ProviderUtils$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/config/$CraftingTweaksMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $CraftingTweaksMode extends $Enum<($CraftingTweaksMode)> {
static readonly "DEFAULT": $CraftingTweaksMode
static readonly "BUTTONS": $CraftingTweaksMode
static readonly "HOTKEYS": $CraftingTweaksMode
static readonly "DISABLED": $CraftingTweaksMode


public static "values"(): ($CraftingTweaksMode)[]
public static "valueOf"(arg0: string): $CraftingTweaksMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksMode$Type = (("default") | ("buttons") | ("hotkeys") | ("disabled")) | ($CraftingTweaksMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksMode_ = $CraftingTweaksMode$Type;
}}
declare module "packages/net/blay09/mods/waystones/core/$PersistentPlayerWaystoneData" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IPlayerWaystoneData, $IPlayerWaystoneData$Type} from "packages/net/blay09/mods/waystones/core/$IPlayerWaystoneData"

export class $PersistentPlayerWaystoneData implements $IPlayerWaystoneData {

constructor()

public "getWarpStoneCooldownUntil"(arg0: $Player$Type): long
public "getInventoryButtonCooldownUntil"(arg0: $Player$Type): long
public "getWaystones"(arg0: $Player$Type): $List<($IWaystone)>
public "setWarpStoneCooldownUntil"(arg0: $Player$Type, arg1: long): void
public "activateWaystone"(arg0: $Player$Type, arg1: $IWaystone$Type): void
public "swapWaystoneSorting"(arg0: $Player$Type, arg1: integer, arg2: integer): void
public "isWaystoneActivated"(arg0: $Player$Type, arg1: $IWaystone$Type): boolean
public "deactivateWaystone"(arg0: $Player$Type, arg1: $IWaystone$Type): void
public "setInventoryButtonCooldownUntil"(arg0: $Player$Type, arg1: long): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PersistentPlayerWaystoneData$Type = ($PersistentPlayerWaystoneData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PersistentPlayerWaystoneData_ = $PersistentPlayerWaystoneData$Type;
}}
declare module "packages/net/blay09/mods/balm/api/$DeferredObject" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $DeferredObject<T> {

constructor(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<(T)>, arg2: $Supplier$Type<(boolean)>)
constructor(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<(T)>)

public "get"(): T
public static "of"<T>(arg0: $ResourceLocation$Type, arg1: T): $DeferredObject<(T)>
public "resolve"(): T
public "getIdentifier"(): $ResourceLocation
public "canResolve"(): boolean
public "resolveImmediately"(): $DeferredObject<(T)>
get "identifier"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeferredObject$Type<T> = ($DeferredObject<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeferredObject_<T> = $DeferredObject$Type<(T)>;
}}
declare module "packages/net/blay09/mods/balm/api/fluid/$FluidTank" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"

export class $FluidTank {

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)
constructor(arg0: integer, arg1: integer, arg2: integer)
constructor(arg0: integer, arg1: integer)
constructor(arg0: integer)

public "isEmpty"(): boolean
public "fill"(arg0: $Fluid$Type, arg1: integer, arg2: boolean): integer
public "drain"(arg0: $Fluid$Type, arg1: integer, arg2: boolean): integer
public "getCapacity"(): integer
public "deserialize"(arg0: $CompoundTag$Type): void
public "setAmount"(arg0: integer): void
public "getAmount"(): integer
public "serialize"(): $CompoundTag
public "setFluid"(arg0: $Fluid$Type, arg1: integer): void
public "canFill"(arg0: $Fluid$Type): boolean
public "canDrain"(arg0: $Fluid$Type): boolean
public "setChanged"(): void
public "getFluid"(): $Fluid
get "empty"(): boolean
get "capacity"(): integer
set "amount"(value: integer)
get "amount"(): integer
get "fluid"(): $Fluid
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FluidTank$Type = ($FluidTank);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FluidTank_ = $FluidTank$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$KeyInputEvent" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $KeyInputEvent {

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)

public "getModifiers"(): integer
public "getKey"(): integer
public "getAction"(): integer
public "getScanCode"(): integer
get "modifiers"(): integer
get "key"(): integer
get "action"(): integer
get "scanCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyInputEvent$Type = ($KeyInputEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyInputEvent_ = $KeyInputEvent$Type;
}}
declare module "packages/net/blay09/mods/waystones/item/$BoundScrollItem" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IResetUseOnDamage, $IResetUseOnDamage$Type} from "packages/net/blay09/mods/waystones/api/$IResetUseOnDamage"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$IFOVOnUse, $IFOVOnUse$Type} from "packages/net/blay09/mods/waystones/api/$IFOVOnUse"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$IAttunementItem, $IAttunementItem$Type} from "packages/net/blay09/mods/waystones/api/$IAttunementItem"
import {$ScrollItemBase, $ScrollItemBase$Type} from "packages/net/blay09/mods/waystones/item/$ScrollItemBase"
import {$InteractionResultHolder, $InteractionResultHolder$Type} from "packages/net/minecraft/world/$InteractionResultHolder"
import {$UseOnContext, $UseOnContext$Type} from "packages/net/minecraft/world/item/context/$UseOnContext"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $BoundScrollItem extends $ScrollItemBase implements $IResetUseOnDamage, $IFOVOnUse, $IAttunementItem {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Item$Properties$Type)

public "getWaystoneAttunedTo"(arg0: $MinecraftServer$Type, arg1: $ItemStack$Type): $IWaystone
public "setWaystoneAttunedTo"(arg0: $ItemStack$Type, arg1: $IWaystone$Type): void
public "getUseDuration"(arg0: $ItemStack$Type): integer
public "finishUsingItem"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $LivingEntity$Type): $ItemStack
public "use"(arg0: $Level$Type, arg1: $Player$Type, arg2: $InteractionHand$Type): $InteractionResultHolder<($ItemStack)>
public "useOn"(arg0: $UseOnContext$Type): $InteractionResult
public "appendHoverText"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
/**
 * 
 * @deprecated
 */
public static "setBoundTo"(arg0: $ItemStack$Type, arg1: $IWaystone$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BoundScrollItem$Type = ($BoundScrollItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BoundScrollItem_ = $BoundScrollItem$Type;
}}
declare module "packages/net/blay09/mods/kuma/api/$InputBinding" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$KeyModifiers, $KeyModifiers$Type} from "packages/net/blay09/mods/kuma/api/$KeyModifiers"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $InputBinding extends $Record {

constructor(key: $InputConstants$Key$Type, modifiers: $KeyModifiers$Type)

public "modifiers"(): $KeyModifiers
public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"(arg0: $KeyMapping$Type): $InputBinding
public "key"(): $InputConstants$Key
public static "key"(arg0: integer, arg1: $KeyModifiers$Type): $InputBinding
public static "key"(arg0: integer): $InputBinding
public static "none"(): $InputBinding
public static "mouse"(arg0: integer, arg1: $KeyModifiers$Type): $InputBinding
public static "mouse"(arg0: integer): $InputBinding
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InputBinding$Type = ($InputBinding);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InputBinding_ = $InputBinding$Type;
}}
declare module "packages/net/blay09/mods/balm/config/$ExampleConfig" {
import {$ExampleConfigData, $ExampleConfigData$Type} from "packages/net/blay09/mods/balm/config/$ExampleConfigData"

export class $ExampleConfig {

constructor()

public static "initialize"(): void
public static "getActive"(): $ExampleConfigData
get "active"(): $ExampleConfigData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExampleConfig$Type = ($ExampleConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExampleConfig_ = $ExampleConfig$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/gui/widget/$WaystoneInventoryButton" {
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $WaystoneInventoryButton extends $Button {
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: $AbstractContainerScreen$Type<(any)>, arg1: $Button$OnPress$Type, arg2: $Supplier$Type<(boolean)>, arg3: $Supplier$Type<(integer)>, arg4: $Supplier$Type<(integer)>)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneInventoryButton$Type = ($WaystoneInventoryButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneInventoryButton_ = $WaystoneInventoryButton$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$LivingHealEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $LivingHealEvent extends $BalmEvent {

constructor()
constructor(arg0: $LivingEntity$Type, arg1: float)

public "getAmount"(): float
public "getEntity"(): $LivingEntity
public "getListenerList"(): $ListenerList
get "amount"(): float
get "entity"(): $LivingEntity
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingHealEvent$Type = ($LivingHealEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingHealEvent_ = $LivingHealEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/server/$ServerStoppedEvent" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $ServerStoppedEvent extends $Record {

constructor(server: $MinecraftServer$Type)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public "server"(): $MinecraftServer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerStoppedEvent$Type = ($ServerStoppedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerStoppedEvent_ = $ServerStoppedEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/client/$BalmClientRuntimeSpi" {
import {$BalmClientRuntime, $BalmClientRuntime$Type} from "packages/net/blay09/mods/balm/api/client/$BalmClientRuntime"

export class $BalmClientRuntimeSpi {

constructor()

public static "create"(): $BalmClientRuntime
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmClientRuntimeSpi$Type = ($BalmClientRuntimeSpi);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmClientRuntimeSpi_ = $BalmClientRuntimeSpi$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/$DeferredRegisters" {
import {$IEventBus, $IEventBus$Type} from "packages/net/minecraftforge/eventbus/api/$IEventBus"
import {$IForgeRegistry, $IForgeRegistry$Type} from "packages/net/minecraftforge/registries/$IForgeRegistry"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Registry, $Registry$Type} from "packages/net/minecraft/core/$Registry"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"

export class $DeferredRegisters {

constructor()

public static "get"<T>(arg0: $ResourceKey$Type<($Registry$Type<(T)>)>, arg1: string): $DeferredRegister<(T)>
public static "get"<T>(arg0: $IForgeRegistry$Type<(T)>, arg1: string): $DeferredRegister<(T)>
public static "register"(arg0: string, arg1: $IEventBus$Type): void
public static "getByModId"(arg0: string): $Collection<($DeferredRegister<(any)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DeferredRegisters$Type = ($DeferredRegisters);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DeferredRegisters_ = $DeferredRegisters$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/$DefaultOptionsDefaultHandlers" {
import {$DefaultOptionsPlugin, $DefaultOptionsPlugin$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsPlugin"

export class $DefaultOptionsDefaultHandlers implements $DefaultOptionsPlugin {

constructor()

public "initialize"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultOptionsDefaultHandlers$Type = ($DefaultOptionsDefaultHandlers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultOptionsDefaultHandlers_ = $DefaultOptionsDefaultHandlers$Type;
}}
declare module "packages/net/blay09/mods/balm/api/stats/$BalmStats" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $BalmStats {

 "registerCustomStat"(arg0: $ResourceLocation$Type): void

(arg0: $ResourceLocation$Type): void
}

export namespace $BalmStats {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmStats$Type = ($BalmStats);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmStats_ = $BalmStats$Type;
}}
declare module "packages/net/blay09/mods/balm/api/provider/$BalmProvider" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export class $BalmProvider<T> {

constructor(arg0: $Class$Type<(T)>, arg1: T)

public "getInstance"(): T
public "getProviderClass"(): $Class<(T)>
get "instance"(): T
get "providerClass"(): $Class<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmProvider$Type<T> = ($BalmProvider<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmProvider_<T> = $BalmProvider$Type<(T)>;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridBuilder" {
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$CraftingGridDecorator, $CraftingGridDecorator$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridDecorator"

export interface $CraftingGridBuilder {

 "addGrid"(arg0: integer, arg1: integer): $CraftingGridDecorator
 "addGrid"(arg0: string, arg1: integer, arg2: integer): $CraftingGridDecorator
 "addCustomGrid"(arg0: $CraftingGrid$Type): void
}

export namespace $CraftingGridBuilder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingGridBuilder$Type = ($CraftingGridBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingGridBuilder_ = $CraftingGridBuilder$Type;
}}
declare module "packages/net/blay09/mods/balm/api/config/$Synced" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Synced extends $Annotation {

 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Synced {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Synced$Type = ($Synced);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Synced_ = $Synced$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/$WaystonesClient" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $WaystonesClient {

constructor()

public static "initialize"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystonesClient$Type = ($WaystonesClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystonesClient_ = $WaystonesClient$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$UseItemInputEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $UseItemInputEvent extends $BalmEvent {

constructor()
constructor(arg0: $InteractionHand$Type)

public "getHand"(): $InteractionHand
public "getListenerList"(): $ListenerList
get "hand"(): $InteractionHand
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UseItemInputEvent$Type = ($UseItemInputEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UseItemInputEvent_ = $UseItemInputEvent$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/$CraftingGuideButtonFixer" {
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $CraftingGuideButtonFixer {

constructor()

public static "fixMistakes"(arg0: $AbstractContainerScreen$Type<(any)>): $Button
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingGuideButtonFixer$Type = ($CraftingGuideButtonFixer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingGuideButtonFixer_ = $CraftingGuideButtonFixer$Type;
}}
declare module "packages/net/blay09/mods/waystones/worldgen/$WaystoneStructurePoolElement" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $WaystoneStructurePoolElement {

 "waystones$setIsWaystone"(arg0: boolean): void
 "waystones$isWaystone"(): boolean
}

export namespace $WaystoneStructurePoolElement {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneStructurePoolElement$Type = ($WaystoneStructurePoolElement);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneStructurePoolElement_ = $WaystoneStructurePoolElement$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/difficulty/$DefaultDifficultyHandler" {
import {$ScreenInitEvent$Post, $ScreenInitEvent$Post$Type} from "packages/net/blay09/mods/balm/api/event/client/screen/$ScreenInitEvent$Post"

export class $DefaultDifficultyHandler {

constructor()

public static "initialize"(): void
public static "onInitGui"(arg0: $ScreenInitEvent$Post$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultDifficultyHandler$Type = ($DefaultDifficultyHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultDifficultyHandler_ = $DefaultDifficultyHandler$Type;
}}
declare module "packages/net/blay09/mods/waystones/item/$ScrollItemBase" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$UseAnim, $UseAnim$Type} from "packages/net/minecraft/world/item/$UseAnim"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ScrollItemBase extends $Item {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Item$Properties$Type)

public "onUseTick"(arg0: $Level$Type, arg1: $LivingEntity$Type, arg2: $ItemStack$Type, arg3: integer): void
public "getUseAnimation"(arg0: $ItemStack$Type): $UseAnim
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScrollItemBase$Type = ($ScrollItemBase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScrollItemBase_ = $ScrollItemBase$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$PlayerLogoutEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $PlayerLogoutEvent extends $BalmEvent {

constructor()
constructor(arg0: $ServerPlayer$Type)

public "getPlayer"(): $ServerPlayer
public "getListenerList"(): $ListenerList
get "player"(): $ServerPlayer
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerLogoutEvent$Type = ($PlayerLogoutEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerLogoutEvent_ = $PlayerLogoutEvent$Type;
}}
declare module "packages/net/blay09/mods/waystones/config/$WaystonesConfig" {
import {$WaystonesConfigData, $WaystonesConfigData$Type} from "packages/net/blay09/mods/waystones/config/$WaystonesConfigData"

export class $WaystonesConfig {

constructor()

public static "initialize"(): void
public static "getActive"(): $WaystonesConfigData
get "active"(): $WaystonesConfigData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystonesConfig$Type = ($WaystonesConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystonesConfig_ = $WaystonesConfig$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$ClientTickHandler" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"

export interface $ClientTickHandler {

 "handle"(arg0: $Minecraft$Type): void

(arg0: $Minecraft$Type): void
}

export namespace $ClientTickHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientTickHandler$Type = ($ClientTickHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientTickHandler_ = $ClientTickHandler$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/network/$BalanceMessage" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $BalanceMessage {

constructor(arg0: $ResourceLocation$Type, arg1: boolean)

public static "decode"(arg0: $FriendlyByteBuf$Type): $BalanceMessage
public static "encode"(arg0: $BalanceMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $ServerPlayer$Type, arg1: $BalanceMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalanceMessage$Type = ($BalanceMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalanceMessage_ = $BalanceMessage$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/registry/$DataDrivenGridFactory" {
import {$CraftingGridProvider, $CraftingGridProvider$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridProvider"
import {$CraftingTweaksRegistrationData, $CraftingTweaksRegistrationData$Type} from "packages/net/blay09/mods/craftingtweaks/registry/$CraftingTweaksRegistrationData"

export class $DataDrivenGridFactory {

constructor()

public static "createGridProvider"(arg0: $CraftingTweaksRegistrationData$Type): $CraftingGridProvider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DataDrivenGridFactory$Type = ($DataDrivenGridFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DataDrivenGridFactory_ = $DataDrivenGridFactory$Type;
}}
declare module "packages/net/blay09/mods/balm/common/client/keymappings/$CommonBalmKeyMappings" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$BalmKeyMappings, $BalmKeyMappings$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$BalmKeyMappings"
import {$KeyModifiers, $KeyModifiers$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$KeyModifiers"
import {$KeyConflictContext, $KeyConflictContext$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$KeyConflictContext"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$KeyModifier, $KeyModifier$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$KeyModifier"
import {$InputConstants$Type, $InputConstants$Type$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Type"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $CommonBalmKeyMappings implements $BalmKeyMappings {

constructor()

public "isKeyDownIgnoreContext"(arg0: $KeyMapping$Type): boolean
public "isActiveAndWasPressed"(arg0: $KeyMapping$Type): boolean
public "shouldIgnoreConflicts"(arg0: $KeyMapping$Type): boolean
public "ignoreConflicts"(arg0: $KeyMapping$Type): void
public "registerKeyMapping"(arg0: string, arg1: integer, arg2: string): $KeyMapping
public "registerKeyMapping"(arg0: string, arg1: $KeyConflictContext$Type, arg2: $KeyModifiers$Type, arg3: integer, arg4: string): $KeyMapping
public "registerKeyMapping"(arg0: string, arg1: $KeyConflictContext$Type, arg2: $KeyModifier$Type, arg3: integer, arg4: string): $KeyMapping
public "registerKeyMapping"(arg0: string, arg1: $InputConstants$Type$Type, arg2: integer, arg3: string): $KeyMapping
public "isActiveAndKeyDown"(arg0: $KeyMapping$Type): boolean
public "isActiveAndMatches"(arg0: $KeyMapping$Type, arg1: $InputConstants$Key$Type): boolean
public "isActiveAndMatches"(arg0: $KeyMapping$Type, arg1: integer, arg2: integer): boolean
public "isActiveAndMatches"(arg0: $KeyMapping$Type, arg1: $InputConstants$Type$Type, arg2: integer, arg3: integer): boolean
public "conflictsWith"(arg0: $KeyMapping$Type, arg1: $KeyMapping$Type): $Optional<(boolean)>
public "registerKeyMapping"(arg0: string, arg1: $KeyConflictContext$Type, arg2: $KeyModifiers$Type, arg3: $InputConstants$Type$Type, arg4: integer, arg5: string): $KeyMapping
public "registerKeyMapping"(arg0: string, arg1: $KeyConflictContext$Type, arg2: $KeyModifier$Type, arg3: $InputConstants$Type$Type, arg4: integer, arg5: string): $KeyMapping
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonBalmKeyMappings$Type = ($CommonBalmKeyMappings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonBalmKeyMappings_ = $CommonBalmKeyMappings$Type;
}}
declare module "packages/net/blay09/mods/waystones/core/$Waystone" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$TeleportDestination, $TeleportDestination$Type} from "packages/net/blay09/mods/waystones/api/$TeleportDestination"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$WaystoneOrigin, $WaystoneOrigin$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneOrigin"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$IMutableWaystone, $IMutableWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IMutableWaystone"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $Waystone implements $IWaystone, $IMutableWaystone {

constructor(arg0: $ResourceLocation$Type, arg1: $UUID$Type, arg2: $ResourceKey$Type<($Level$Type)>, arg3: $BlockPos$Type, arg4: $WaystoneOrigin$Type, arg5: $UUID$Type)

public "getName"(): string
public static "write"(arg0: $FriendlyByteBuf$Type, arg1: $IWaystone$Type): void
public static "write"(arg0: $IWaystone$Type, arg1: $CompoundTag$Type): $CompoundTag
public static "read"(arg0: $FriendlyByteBuf$Type): $IWaystone
public static "read"(arg0: $CompoundTag$Type): $IWaystone
public "setName"(arg0: string): void
public "isValid"(): boolean
public "getDimension"(): $ResourceKey<($Level)>
public "setPos"(arg0: $BlockPos$Type): void
public "isValidInLevel"(arg0: $ServerLevel$Type): boolean
public "resolveDestination"(arg0: $ServerLevel$Type): $TeleportDestination
public "setOwnerUid"(arg0: $UUID$Type): void
public "setGlobal"(arg0: boolean): void
public "isGlobal"(): boolean
public "getPos"(): $BlockPos
public "getOrigin"(): $WaystoneOrigin
public "getOwnerUid"(): $UUID
public "getWaystoneUid"(): $UUID
public "isOwner"(arg0: $Player$Type): boolean
public "getWaystoneType"(): $ResourceLocation
public "setDimension"(arg0: $ResourceKey$Type<($Level$Type)>): void
public "hasName"(): boolean
public "hasOwner"(): boolean
public "wasGenerated"(): boolean
get "name"(): string
set "name"(value: string)
get "valid"(): boolean
get "dimension"(): $ResourceKey<($Level)>
set "pos"(value: $BlockPos$Type)
set "ownerUid"(value: $UUID$Type)
set "global"(value: boolean)
get "global"(): boolean
get "pos"(): $BlockPos
get "origin"(): $WaystoneOrigin
get "ownerUid"(): $UUID
get "waystoneUid"(): $UUID
get "waystoneType"(): $ResourceLocation
set "dimension"(value: $ResourceKey$Type<($Level$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Waystone$Type = ($Waystone);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Waystone_ = $Waystone$Type;
}}
declare module "packages/net/blay09/mods/balm/api/container/$SubContainer" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ExtractionAwareContainer, $ExtractionAwareContainer$Type} from "packages/net/blay09/mods/balm/api/container/$ExtractionAwareContainer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $SubContainer implements $Container, $ExtractionAwareContainer {

constructor(arg0: $Container$Type, arg1: integer, arg2: integer)

public "canExtractItem"(arg0: integer): boolean
public "setChanged"(): void
public "getItem"(arg0: integer): $ItemStack
public "getContainerSize"(): integer
public "removeItemNoUpdate"(arg0: integer): $ItemStack
public "removeItem"(arg0: integer, arg1: integer): $ItemStack
public "clearContent"(): void
public "isEmpty"(): boolean
public "startOpen"(arg0: $Player$Type): void
public "getMaxStackSize"(): integer
public "stillValid"(arg0: $Player$Type): boolean
public "stopOpen"(arg0: $Player$Type): void
public "canPlaceItem"(arg0: integer, arg1: $ItemStack$Type): boolean
public "setItem"(arg0: integer, arg1: $ItemStack$Type): void
/**
 * 
 * @deprecated
 */
public "containsSlot"(arg0: integer): boolean
public "containsOuterSlot"(arg0: integer): boolean
public "kjs$self"(): $Container
public "getBlock"(level: $Level$Type): $BlockContainerJS
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public "countItem"(arg0: $Item$Type): integer
public "canTakeItem"(arg0: $Container$Type, arg1: integer, arg2: $ItemStack$Type): boolean
public "hasAnyMatching"(arg0: $Predicate$Type<($ItemStack$Type)>): boolean
public "getSlots"(): integer
public "getStackInSlot"(slot: integer): $ItemStack
public "insertItem"(slot: integer, stack: $ItemStack$Type, simulate: boolean): $ItemStack
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public "isMutable"(): boolean
public "hasAnyOf"(arg0: $Set$Type<($Item$Type)>): boolean
public "setChanged"(): void
public "asContainer"(): $Container
public "getHeight"(): integer
public "extractItem"(slot: integer, amount: integer, simulate: boolean): $ItemStack
public "isItemValid"(slot: integer, stack: $ItemStack$Type): boolean
public "getWidth"(): integer
public "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
public "getSlotLimit"(slot: integer): integer
public "clear"(): void
public static "tryClear"(arg0: any): void
public "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
public "countNonEmpty"(ingredient: $Ingredient$Type): integer
public "countNonEmpty"(): integer
public "getAllItems"(): $List<($ItemStack)>
public "find"(ingredient: $Ingredient$Type): integer
public "find"(): integer
public "clear"(ingredient: $Ingredient$Type): void
public "count"(ingredient: $Ingredient$Type): integer
public "count"(): integer
public "isEmpty"(): boolean
get "containerSize"(): integer
get "empty"(): boolean
get "maxStackSize"(): integer
get "slots"(): integer
get "mutable"(): boolean
get "height"(): integer
get "width"(): integer
get "allItems"(): $List<($ItemStack)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SubContainer$Type = ($SubContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SubContainer_ = $SubContainer$Type;
}}
declare module "packages/net/blay09/mods/balm/common/$CommonBalmLootTables" {
import {$BalmLootTables, $BalmLootTables$Type} from "packages/net/blay09/mods/balm/api/loot/$BalmLootTables"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$BalmLootModifier, $BalmLootModifier$Type} from "packages/net/blay09/mods/balm/api/loot/$BalmLootModifier"

export class $CommonBalmLootTables implements $BalmLootTables {
readonly "lootModifiers": $Map<($ResourceLocation), ($BalmLootModifier)>

constructor()

public "registerLootModifier"(arg0: $ResourceLocation$Type, arg1: $BalmLootModifier$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonBalmLootTables$Type = ($CommonBalmLootTables);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonBalmLootTables_ = $CommonBalmLootTables$Type;
}}
declare module "packages/net/blay09/mods/balm/api/container/$ExtractionAwareContainer" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ExtractionAwareContainer {

 "canExtractItem"(arg0: integer): boolean

(arg0: integer): boolean
}

export namespace $ExtractionAwareContainer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExtractionAwareContainer$Type = ($ExtractionAwareContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExtractionAwareContainer_ = $ExtractionAwareContainer$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/impl/$InternalMethodsImpl" {
import {$GridBalanceHandler, $GridBalanceHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridBalanceHandler"
import {$GridRotateHandler, $GridRotateHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridRotateHandler"
import {$GridTransferHandler, $GridTransferHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridTransferHandler"
import {$CraftingGridProvider, $CraftingGridProvider$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridProvider"
import {$InternalMethods, $InternalMethods$Type} from "packages/net/blay09/mods/craftingtweaks/api/$InternalMethods"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$GridClearHandler, $GridClearHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridClearHandler"

export class $InternalMethodsImpl implements $InternalMethods {

constructor()

public "defaultFourByFourRotateHandler"(): $GridRotateHandler<($AbstractContainerMenu)>
public "defaultRectangularRotateHandler"(): $GridRotateHandler<($AbstractContainerMenu)>
public "defaultClearHandler"(): $GridClearHandler<($AbstractContainerMenu)>
public "defaultBalanceHandler"(): $GridBalanceHandler<($AbstractContainerMenu)>
public "defaultTransferHandler"(): $GridTransferHandler<($AbstractContainerMenu)>
public "defaultRotateHandler"(): $GridRotateHandler<($AbstractContainerMenu)>
public "unregisterCraftingGridProvider"(arg0: $CraftingGridProvider$Type): void
public "registerCraftingGridProvider"(arg0: $CraftingGridProvider$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalMethodsImpl$Type = ($InternalMethodsImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalMethodsImpl_ = $InternalMethodsImpl$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/gui/widget/$RemoveWaystoneButton" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ITooltipProvider, $ITooltipProvider$Type} from "packages/net/blay09/mods/waystones/client/gui/widget/$ITooltipProvider"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $RemoveWaystoneButton extends $Button implements $ITooltipProvider {
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: $IWaystone$Type, arg5: $Button$OnPress$Type)

public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "getTooltipComponents"(): $List<($Component)>
public "shouldShowTooltip"(): boolean
get "tooltipComponents"(): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RemoveWaystoneButton$Type = ($RemoveWaystoneButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RemoveWaystoneButton_ = $RemoveWaystoneButton$Type;
}}
declare module "packages/net/blay09/mods/balm/api/client/screen/$BalmScreens" {
import {$BalmScreenFactory, $BalmScreenFactory$Type} from "packages/net/blay09/mods/balm/api/client/screen/$BalmScreenFactory"
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$MenuAccess, $MenuAccess$Type} from "packages/net/minecraft/client/gui/screens/inventory/$MenuAccess"

export interface $BalmScreens {

 "registerScreen"<T extends $AbstractContainerMenu, S extends ($Screen) & ($MenuAccess<(T)>)>(arg0: $Supplier$Type<($MenuType$Type<(any)>)>, arg1: $BalmScreenFactory$Type<(T), (S)>): void
 "addRenderableWidget"(arg0: $Screen$Type, arg1: $AbstractWidget$Type): $AbstractWidget
}

export namespace $BalmScreens {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmScreens$Type = ($BalmScreens);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmScreens_ = $BalmScreens$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/client/$ModKeyMappings" {
import {$CompressType, $CompressType$Type} from "packages/net/blay09/mods/craftingtweaks/$CompressType"
import {$ManagedKeyMapping, $ManagedKeyMapping$Type} from "packages/net/blay09/mods/kuma/api/$ManagedKeyMapping"

export class $ModKeyMappings {
static "keyRotate": $ManagedKeyMapping
static "keyRotateCounterClockwise": $ManagedKeyMapping
static "keyBalance": $ManagedKeyMapping
static "keySpread": $ManagedKeyMapping
static "keyClear": $ManagedKeyMapping
static "keyForceClear": $ManagedKeyMapping
static "keyCompressOne": $ManagedKeyMapping
static "keyCompressStack": $ManagedKeyMapping
static "keyCompressAll": $ManagedKeyMapping
static "keyDecompressOne": $ManagedKeyMapping
static "keyDecompressStack": $ManagedKeyMapping
static "keyDecompressAll": $ManagedKeyMapping
static "keyRefillLast": $ManagedKeyMapping
static "keyRefillLastStack": $ManagedKeyMapping
static "keyTransferStack": $ManagedKeyMapping

constructor()

public static "initialize"(): void
public static "getCompressTypeForKey"(arg0: integer, arg1: integer, arg2: integer): $CompressType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModKeyMappings$Type = ($ModKeyMappings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModKeyMappings_ = $ModKeyMappings$Type;
}}
declare module "packages/net/blay09/mods/balm/api/config/$BalmConfigData" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $BalmConfigData {

}

export namespace $BalmConfigData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmConfigData$Type = ($BalmConfigData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmConfigData_ = $BalmConfigData$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/network/$TransferStackMessage" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $TransferStackMessage {

constructor(arg0: $ResourceLocation$Type, arg1: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $TransferStackMessage
public static "encode"(arg0: $TransferStackMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $ServerPlayer$Type, arg1: $TransferStackMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TransferStackMessage$Type = ($TransferStackMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TransferStackMessage_ = $TransferStackMessage$Type;
}}
declare module "packages/net/blay09/mods/balm/api/block/$BalmBlockEntities" {
import {$BalmBlockEntityFactory, $BalmBlockEntityFactory$Type} from "packages/net/blay09/mods/balm/api/block/entity/$BalmBlockEntityFactory"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $BalmBlockEntities {

 "registerBlockEntity"<T extends $BlockEntity>(arg0: $ResourceLocation$Type, arg1: $BalmBlockEntityFactory$Type<(T)>, arg2: $Supplier$Type<(($Block$Type)[])>): $DeferredObject<($BlockEntityType<(T)>)>
 "registerBlockEntity"<T extends $BlockEntity>(arg0: $ResourceLocation$Type, arg1: $BalmBlockEntityFactory$Type<(T)>, ...arg2: ($DeferredObject$Type<($Block$Type)>)[]): $DeferredObject<($BlockEntityType<(T)>)>
}

export namespace $BalmBlockEntities {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmBlockEntities$Type = ($BalmBlockEntities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmBlockEntities_ = $BalmBlockEntities$Type;
}}
declare module "packages/net/blay09/mods/waystones/item/$ModItems" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$BalmItems, $BalmItems$Type} from "packages/net/blay09/mods/balm/api/item/$BalmItems"

export class $ModItems {
static "creativeModeTab": $DeferredObject<($CreativeModeTab)>
static "returnScroll": $Item
static "boundScroll": $Item
static "warpScroll": $Item
static "warpStone": $Item
static "warpDust": $Item
static "attunedShard": $Item
static "crumblingAttunedShard": $Item

constructor()

public static "initialize"(arg0: $BalmItems$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModItems$Type = ($ModItems);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModItems_ = $ModItems$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/network/$NetworkChannels" {
import {$SimpleChannel, $SimpleChannel$Type} from "packages/net/minecraftforge/network/simple/$SimpleChannel"

export class $NetworkChannels {

constructor()

public static "get"(arg0: string): $SimpleChannel
public static "allowClientOnly"(arg0: string): void
public static "allowServerOnly"(arg0: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NetworkChannels$Type = ($NetworkChannels);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NetworkChannels_ = $NetworkChannels$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsLoadStage" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $DefaultOptionsLoadStage extends $Enum<($DefaultOptionsLoadStage)> {
static readonly "PRE_LOAD": $DefaultOptionsLoadStage
static readonly "POST_LOAD": $DefaultOptionsLoadStage


public static "values"(): ($DefaultOptionsLoadStage)[]
public static "valueOf"(arg0: string): $DefaultOptionsLoadStage
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultOptionsLoadStage$Type = (("pre_load") | ("post_load")) | ($DefaultOptionsLoadStage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultOptionsLoadStage_ = $DefaultOptionsLoadStage$Type;
}}
declare module "packages/net/blay09/mods/balm/config/$ExampleConfigData$ExampleEnum" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $ExampleConfigData$ExampleEnum extends $Enum<($ExampleConfigData$ExampleEnum)> {
static readonly "Hello": $ExampleConfigData$ExampleEnum
static readonly "World": $ExampleConfigData$ExampleEnum


public static "values"(): ($ExampleConfigData$ExampleEnum)[]
public static "valueOf"(arg0: string): $ExampleConfigData$ExampleEnum
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExampleConfigData$ExampleEnum$Type = (("world") | ("hello")) | ($ExampleConfigData$ExampleEnum);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExampleConfigData$ExampleEnum_ = $ExampleConfigData$ExampleEnum$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$IResetUseOnDamage" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IResetUseOnDamage {

}

export namespace $IResetUseOnDamage {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IResetUseOnDamage$Type = ($IResetUseOnDamage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IResetUseOnDamage_ = $IResetUseOnDamage$Type;
}}
declare module "packages/net/blay09/mods/waystones/core/$WarpMode" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $WarpMode extends $Enum<($WarpMode)> {
static readonly "INVENTORY_BUTTON": $WarpMode
static readonly "WARP_SCROLL": $WarpMode
static readonly "RETURN_SCROLL": $WarpMode
static readonly "BOUND_SCROLL": $WarpMode
static readonly "WARP_STONE": $WarpMode
static readonly "WAYSTONE_TO_WAYSTONE": $WarpMode
static readonly "SHARESTONE_TO_SHARESTONE": $WarpMode
static readonly "WARP_PLATE": $WarpMode
static readonly "PORTSTONE_TO_WAYSTONE": $WarpMode
static readonly "CUSTOM": $WarpMode
static "values": ($WarpMode)[]


public "getXpCostMultiplier"(): double
public "getAllowTeleportPredicate"(): $BiPredicate<($Entity), ($IWaystone)>
public static "values"(): ($WarpMode)[]
public static "valueOf"(arg0: string): $WarpMode
public "consumesItem"(): boolean
get "xpCostMultiplier"(): double
get "allowTeleportPredicate"(): $BiPredicate<($Entity), ($IWaystone)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpMode$Type = (("bound_scroll") | ("warp_scroll") | ("inventory_button") | ("warp_plate") | ("warp_stone") | ("custom") | ("return_scroll") | ("portstone_to_waystone") | ("sharestone_to_sharestone") | ("waystone_to_waystone")) | ($WarpMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpMode_ = $WarpMode$Type;
}}
declare module "packages/net/blay09/mods/waystones/block/$WaystoneBlock" {
import {$BooleanProperty, $BooleanProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$BooleanProperty"
import {$VoxelShape, $VoxelShape$Type} from "packages/net/minecraft/world/phys/shapes/$VoxelShape"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$CollisionContext, $CollisionContext$Type} from "packages/net/minecraft/world/phys/shapes/$CollisionContext"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IdMapper, $IdMapper$Type} from "packages/net/minecraft/core/$IdMapper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$PathComputationType, $PathComputationType$Type} from "packages/net/minecraft/world/level/pathfinder/$PathComputationType"
import {$WaystoneOrigin, $WaystoneOrigin$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneOrigin"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$EnumProperty, $EnumProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$EnumProperty"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$DoubleBlockHalf, $DoubleBlockHalf$Type} from "packages/net/minecraft/world/level/block/state/properties/$DoubleBlockHalf"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$WaystoneBlockBase, $WaystoneBlockBase$Type} from "packages/net/blay09/mods/waystones/block/$WaystoneBlockBase"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$DirectionProperty, $DirectionProperty$Type} from "packages/net/minecraft/world/level/block/state/properties/$DirectionProperty"

export class $WaystoneBlock extends $WaystoneBlockBase {
static readonly "FACING": $DirectionProperty
static readonly "HALF": $EnumProperty<($DoubleBlockHalf)>
static readonly "WATERLOGGED": $BooleanProperty
static readonly "ORIGIN": $EnumProperty<($WaystoneOrigin)>
/**
 * 
 * @deprecated
 */
static readonly "BLOCK_STATE_REGISTRY": $IdMapper<($BlockState)>
static readonly "UPDATE_NEIGHBORS": integer
static readonly "UPDATE_CLIENTS": integer
static readonly "UPDATE_INVISIBLE": integer
static readonly "UPDATE_IMMEDIATE": integer
static readonly "UPDATE_KNOWN_SHAPE": integer
static readonly "UPDATE_SUPPRESS_DROPS": integer
static readonly "UPDATE_MOVE_BY_PISTON": integer
static readonly "UPDATE_NONE": integer
static readonly "UPDATE_ALL": integer
static readonly "UPDATE_ALL_IMMEDIATE": integer
static readonly "INDESTRUCTIBLE": float
static readonly "INSTANT": float
static readonly "UPDATE_LIMIT": integer
 "descriptionId": string
 "properties": $BlockBehaviour$Properties
 "drops": $ResourceLocation

constructor(arg0: $BlockBehaviour$Properties$Type)

public "isPathfindable"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $PathComputationType$Type): boolean
public "getShape"(arg0: $BlockState$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type, arg3: $CollisionContext$Type): $VoxelShape
public "animateTick"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $RandomSource$Type): void
public "newBlockEntity"(arg0: $BlockPos$Type, arg1: $BlockState$Type): $BlockEntity
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneBlock$Type = ($WaystoneBlock);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneBlock_ = $WaystoneBlock$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$WaystonesAPI" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$WaystoneStyle, $WaystoneStyle$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneStyle"
import {$IWaystoneTeleportContext, $IWaystoneTeleportContext$Type} from "packages/net/blay09/mods/waystones/api/$IWaystoneTeleportContext"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$InternalMethods, $InternalMethods$Type} from "packages/net/blay09/mods/waystones/api/$InternalMethods"
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$WaystoneTeleportContext, $WaystoneTeleportContext$Type} from "packages/net/blay09/mods/waystones/core/$WaystoneTeleportContext"
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$WaystoneTeleportError, $WaystoneTeleportError$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneTeleportError"
import {$WarpMode, $WarpMode$Type} from "packages/net/blay09/mods/waystones/core/$WarpMode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $WaystonesAPI {
static "__internalMethods": $InternalMethods

constructor()

public static "forceTeleportToWaystone"(arg0: $Entity$Type, arg1: $IWaystone$Type): $Either<($List<($Entity)>), ($WaystoneTeleportError)>
public static "tryTeleportToWaystone"(arg0: $Entity$Type, arg1: $IWaystone$Type, arg2: $WarpMode$Type, arg3: $IWaystone$Type): $Either<($List<($Entity)>), ($WaystoneTeleportError)>
public static "createDefaultTeleportContext"(arg0: $Entity$Type, arg1: $IWaystone$Type, arg2: $WarpMode$Type, arg3: $IWaystone$Type): $Either<($IWaystoneTeleportContext), ($WaystoneTeleportError)>
public static "createCustomTeleportContext"(arg0: $Entity$Type, arg1: $IWaystone$Type): $Either<($IWaystoneTeleportContext), ($WaystoneTeleportError)>
public static "placeSharestone"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $DyeColor$Type): $Optional<($IWaystone)>
public static "placeWarpPlate"(arg0: $Level$Type, arg1: $BlockPos$Type): $Optional<($IWaystone)>
public static "createAttunedShard"(arg0: $IWaystone$Type): $ItemStack
public static "getBoundWaystone"(arg0: $ItemStack$Type): $Optional<($IWaystone)>
public static "placeWaystone"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $WaystoneStyle$Type): $Optional<($IWaystone)>
public static "setBoundWaystone"(arg0: $ItemStack$Type, arg1: $IWaystone$Type): void
/**
 * 
 * @deprecated
 */
public static "forceTeleport"(arg0: $WaystoneTeleportContext$Type): $List<($Entity)>
public static "forceTeleport"(arg0: $IWaystoneTeleportContext$Type): $List<($Entity)>
public static "getWaystoneAt"(arg0: $ServerLevel$Type, arg1: $BlockPos$Type): $Optional<($IWaystone)>
public static "getWaystone"(arg0: $Level$Type, arg1: $UUID$Type): $Optional<($IWaystone)>
public static "tryTeleport"(arg0: $IWaystoneTeleportContext$Type): $Either<($List<($Entity)>), ($WaystoneTeleportError)>
/**
 * 
 * @deprecated
 */
public static "tryTeleport"(arg0: $WaystoneTeleportContext$Type): $Either<($List<($Entity)>), ($WaystoneTeleportError)>
public static "createBoundScroll"(arg0: $IWaystone$Type): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystonesAPI$Type = ($WaystonesAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystonesAPI_ = $WaystonesAPI$Type;
}}
declare module "packages/net/blay09/mods/waystones/network/message/$TeleportEffectMessage" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $TeleportEffectMessage {

constructor(arg0: $BlockPos$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $TeleportEffectMessage
public static "encode"(arg0: $TeleportEffectMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $Player$Type, arg1: $TeleportEffectMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TeleportEffectMessage$Type = ($TeleportEffectMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TeleportEffectMessage_ = $TeleportEffectMessage$Type;
}}
declare module "packages/net/blay09/mods/waystones/handler/$LoginHandler" {
import {$PlayerLoginEvent, $PlayerLoginEvent$Type} from "packages/net/blay09/mods/balm/api/event/$PlayerLoginEvent"

export class $LoginHandler {

constructor()

public static "onPlayerLogin"(arg0: $PlayerLoginEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LoginHandler$Type = ($LoginHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LoginHandler_ = $LoginHandler$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$IWaystone" {
import {$WaystoneOrigin, $WaystoneOrigin$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneOrigin"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$TeleportDestination, $TeleportDestination$Type} from "packages/net/blay09/mods/waystones/api/$TeleportDestination"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $IWaystone {

 "getName"(): string
 "isValid"(): boolean
 "hasName"(): boolean
 "getDimension"(): $ResourceKey<($Level)>
 "isValidInLevel"(arg0: $ServerLevel$Type): boolean
 "resolveDestination"(arg0: $ServerLevel$Type): $TeleportDestination
 "isGlobal"(): boolean
 "getPos"(): $BlockPos
 "getOrigin"(): $WaystoneOrigin
 "getOwnerUid"(): $UUID
 "getWaystoneUid"(): $UUID
 "hasOwner"(): boolean
 "wasGenerated"(): boolean
 "isOwner"(arg0: $Player$Type): boolean
 "getWaystoneType"(): $ResourceLocation
}

export namespace $IWaystone {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IWaystone$Type = ($IWaystone);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IWaystone_ = $IWaystone$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/client/rendering/$ForgeCachedDynamicModel" {
import {$Pair, $Pair$Type} from "packages/org/apache/commons/lang3/tuple/$Pair"
import {$ModelData, $ModelData$Type} from "packages/net/minecraftforge/client/model/data/$ModelData"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Matrix4f, $Matrix4f$Type} from "packages/org/joml/$Matrix4f"
import {$BakedModel, $BakedModel$Type} from "packages/net/minecraft/client/resources/model/$BakedModel"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$List, $List$Type} from "packages/java/util/$List"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$AbstractCachedDynamicModel, $AbstractCachedDynamicModel$Type} from "packages/net/blay09/mods/balm/common/client/rendering/$AbstractCachedDynamicModel"
import {$ChunkRenderTypeSet, $ChunkRenderTypeSet$Type} from "packages/net/minecraftforge/client/$ChunkRenderTypeSet"
import {$ModelBakery, $ModelBakery$Type} from "packages/net/minecraft/client/resources/model/$ModelBakery"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $ForgeCachedDynamicModel extends $AbstractCachedDynamicModel {

constructor(arg0: $ModelBakery$Type, arg1: $Function$Type<($BlockState$Type), ($ResourceLocation$Type)>, arg2: $List$Type<($Pair$Type<($Predicate$Type<($BlockState$Type)>), ($BakedModel$Type)>)>, arg3: $Function$Type<($BlockState$Type), ($Map$Type<(string), (string)>)>, arg4: $BiConsumer$Type<($BlockState$Type), ($Matrix4f$Type)>, arg5: $List$Type<($RenderType$Type)>, arg6: $ResourceLocation$Type)

public "getBlockRenderTypes"(arg0: $BlockState$Type, arg1: $RandomSource$Type): $List<($RenderType)>
public "getRenderTypes"(arg0: $BlockState$Type, arg1: $RandomSource$Type, arg2: $ModelData$Type): $ChunkRenderTypeSet
public "getRenderTypes"(arg0: $ItemStack$Type, arg1: boolean): $List<($RenderType)>
public "getItemRenderTypes"(arg0: $ItemStack$Type, arg1: boolean): $List<($RenderType)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeCachedDynamicModel$Type = ($ForgeCachedDynamicModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeCachedDynamicModel_ = $ForgeCachedDynamicModel$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/gui/screen/$WarpPlateScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$WarpPlateContainer, $WarpPlateContainer$Type} from "packages/net/blay09/mods/waystones/menu/$WarpPlateContainer"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export class $WarpPlateScreen extends $AbstractContainerScreen<($WarpPlateContainer)> {
static readonly "INVENTORY_LOCATION": $ResourceLocation
static readonly "SLOT_ITEM_BLIT_OFFSET": integer
 "imageWidth": integer
 "hoveredSlot": $Slot
 "leftPos": integer
 "topPos": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $WarpPlateContainer$Type, arg1: $Inventory$Type, arg2: $Component$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpPlateScreen$Type = ($WarpPlateScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpPlateScreen_ = $WarpPlateScreen$Type;
}}
declare module "packages/net/blay09/mods/waystones/compat/$RepurposedStructuresIntegration" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$DeferredRegister, $DeferredRegister$Type} from "packages/net/minecraftforge/registries/$DeferredRegister"
import {$RegistryObject, $RegistryObject$Type} from "packages/net/minecraftforge/registries/$RegistryObject"

export class $RepurposedStructuresIntegration {
static readonly "RS_CONDITIONS_REGISTRY": $DeferredRegister<($Supplier<(boolean)>)>
static readonly "WAYSTONE_CONFIG_CONDITION": $RegistryObject<($Supplier<(boolean)>)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RepurposedStructuresIntegration$Type = ($RepurposedStructuresIntegration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RepurposedStructuresIntegration_ = $RepurposedStructuresIntegration$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$CraftingTweaksClientAPI" {
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$TweakType, $TweakType$Type} from "packages/net/blay09/mods/craftingtweaks/api/$TweakType"
import {$ButtonStyle, $ButtonStyle$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonStyle"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$GridGuiHandler, $GridGuiHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridGuiHandler"
import {$InternalClientMethods, $InternalClientMethods$Type} from "packages/net/blay09/mods/craftingtweaks/api/$InternalClientMethods"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $CraftingTweaksClientAPI {

constructor()

public static "createTweakButton"(arg0: $CraftingGrid$Type, arg1: integer, arg2: integer, arg3: $TweakType$Type, arg4: $ButtonStyle$Type): $AbstractWidget
public static "createTweakButton"(arg0: $CraftingGrid$Type, arg1: integer, arg2: integer, arg3: $TweakType$Type): $AbstractWidget
public static "setupAPI"(arg0: $InternalClientMethods$Type): void
public static "registerCraftingGridGuiHandler"<TMenu extends $AbstractContainerMenu, TScreen extends $AbstractContainerScreen<(TMenu)>>(arg0: $Class$Type<(TScreen)>, arg1: $GridGuiHandler$Type): void
/**
 * 
 * @deprecated
 */
public static "createClearButton"(arg0: $CraftingGrid$Type, arg1: integer, arg2: integer): $Button
/**
 * 
 * @deprecated
 */
public static "createRotateButton"(arg0: $CraftingGrid$Type, arg1: integer, arg2: integer): $Button
/**
 * 
 * @deprecated
 */
public static "createBalanceButton"(arg0: $CraftingGrid$Type, arg1: integer, arg2: integer): $Button
/**
 * 
 * @deprecated
 */
public static "createBalanceButtonRelative"(arg0: $CraftingGrid$Type, arg1: $AbstractContainerScreen$Type<(any)>, arg2: integer, arg3: integer): $Button
/**
 * 
 * @deprecated
 */
public static "createClearButtonRelative"(arg0: $CraftingGrid$Type, arg1: $AbstractContainerScreen$Type<(any)>, arg2: integer, arg3: integer): $Button
public static "createTweakButtonRelative"(arg0: $CraftingGrid$Type, arg1: $AbstractContainerScreen$Type<(any)>, arg2: integer, arg3: integer, arg4: $TweakType$Type): $AbstractWidget
public static "createTweakButtonRelative"(arg0: $CraftingGrid$Type, arg1: $AbstractContainerScreen$Type<(any)>, arg2: integer, arg3: integer, arg4: $TweakType$Type, arg5: $ButtonStyle$Type): $AbstractWidget
/**
 * 
 * @deprecated
 */
public static "createRotateButtonRelative"(arg0: $CraftingGrid$Type, arg1: $AbstractContainerScreen$Type<(any)>, arg2: integer, arg3: integer): $Button
set "upAPI"(value: $InternalClientMethods$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksClientAPI$Type = ($CraftingTweaksClientAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksClientAPI_ = $CraftingTweaksClientAPI$Type;
}}
declare module "packages/net/blay09/mods/waystones/handler/$ModEventHandlers" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModEventHandlers {

constructor()

public static "initialize"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModEventHandlers$Type = ($ModEventHandlers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModEventHandlers_ = $ModEventHandlers$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/config/$CraftingTweaksConfig" {
import {$CraftingTweaksConfigData, $CraftingTweaksConfigData$Type} from "packages/net/blay09/mods/craftingtweaks/config/$CraftingTweaksConfigData"

export class $CraftingTweaksConfig {

constructor()

public static "initialize"(): void
public static "getActive"(): $CraftingTweaksConfigData
get "active"(): $CraftingTweaksConfigData
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksConfig$Type = ($CraftingTweaksConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksConfig_ = $CraftingTweaksConfig$Type;
}}
declare module "packages/net/blay09/mods/balm/config/$ExampleConfigData" {
import {$ExampleConfigData$ExampleEnum, $ExampleConfigData$ExampleEnum$Type} from "packages/net/blay09/mods/balm/config/$ExampleConfigData$ExampleEnum"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BalmConfigData, $BalmConfigData$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfigData"
import {$ExampleConfigData$ExampleCategory, $ExampleConfigData$ExampleCategory$Type} from "packages/net/blay09/mods/balm/config/$ExampleConfigData$ExampleCategory"

export class $ExampleConfigData implements $BalmConfigData {
 "exampleBoolean": boolean
 "exampleInt": integer
 "exampleString": string
 "exampleMultilineString": string
 "exampleEnum": $ExampleConfigData$ExampleEnum
 "exampleStringList": $List<(string)>
 "exampleIntList": $List<(integer)>
 "exampleEnumList": $List<($ExampleConfigData$ExampleEnum)>
 "exampleCategory": $ExampleConfigData$ExampleCategory

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ExampleConfigData$Type = ($ExampleConfigData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ExampleConfigData_ = $ExampleConfigData$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/world/$BiomeModification" {
import {$PlacedFeature, $PlacedFeature$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacedFeature"
import {$GenerationStep$Decoration, $GenerationStep$Decoration$Type} from "packages/net/minecraft/world/level/levelgen/$GenerationStep$Decoration"
import {$BiomePredicate, $BiomePredicate$Type} from "packages/net/blay09/mods/balm/api/world/$BiomePredicate"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"

export class $BiomeModification {


public "getBiomePredicate"(): $BiomePredicate
public "getConfiguredFeatureKey"(): $ResourceKey<($PlacedFeature)>
public "getStep"(): $GenerationStep$Decoration
get "biomePredicate"(): $BiomePredicate
get "configuredFeatureKey"(): $ResourceKey<($PlacedFeature)>
get "step"(): $GenerationStep$Decoration
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeModification$Type = ($BiomeModification);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeModification_ = $BiomeModification$Type;
}}
declare module "packages/net/blay09/mods/balm/api/config/$Comment" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Comment extends $Annotation {

 "value"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Comment {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Comment$Type = ($Comment);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Comment_ = $Comment$Type;
}}
declare module "packages/net/blay09/mods/waystones/block/$WarpPlateBlock$WarpPlateStatus" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $WarpPlateBlock$WarpPlateStatus extends $Enum<($WarpPlateBlock$WarpPlateStatus)> implements $StringRepresentable {
static readonly "IDLE": $WarpPlateBlock$WarpPlateStatus
static readonly "ACTIVE": $WarpPlateBlock$WarpPlateStatus
static readonly "INVALID": $WarpPlateBlock$WarpPlateStatus


public static "values"(): ($WarpPlateBlock$WarpPlateStatus)[]
public static "valueOf"(arg0: string): $WarpPlateBlock$WarpPlateStatus
public "getSerializedName"(): string
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpPlateBlock$WarpPlateStatus$Type = (("idle") | ("invalid") | ("active")) | ($WarpPlateBlock$WarpPlateStatus);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpPlateBlock$WarpPlateStatus_ = $WarpPlateBlock$WarpPlateStatus$Type;
}}
declare module "packages/net/blay09/mods/waystones/config/$WaystonesConfigData$WorldGen" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$WorldGenStyle, $WorldGenStyle$Type} from "packages/net/blay09/mods/waystones/config/$WorldGenStyle"
import {$NameGenerationMode, $NameGenerationMode$Type} from "packages/net/blay09/mods/waystones/worldgen/namegen/$NameGenerationMode"

export class $WaystonesConfigData$WorldGen {
 "worldGenStyle": $WorldGenStyle
 "frequency": integer
 "dimensionAllowList": $List<(string)>
 "dimensionDenyList": $List<(string)>
 "nameGenerationMode": $NameGenerationMode
 "customWaystoneNames": $List<(string)>
 "spawnInVillages": boolean
 "forceSpawnInVillages": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystonesConfigData$WorldGen$Type = ($WaystonesConfigData$WorldGen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystonesConfigData$WorldGen_ = $WaystonesConfigData$WorldGen$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/$DefaultOptionsHandlerException" {
import {$Throwable, $Throwable$Type} from "packages/java/lang/$Throwable"
import {$Exception, $Exception$Type} from "packages/java/lang/$Exception"
import {$DefaultOptionsHandler, $DefaultOptionsHandler$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsHandler"

export class $DefaultOptionsHandlerException extends $Exception {

constructor(arg0: $DefaultOptionsHandler$Type, arg1: string, arg2: $Throwable$Type, arg3: boolean, arg4: boolean)
constructor(arg0: $DefaultOptionsHandler$Type, arg1: $Throwable$Type)
constructor(arg0: $DefaultOptionsHandler$Type, arg1: string, arg2: $Throwable$Type)
constructor(arg0: $DefaultOptionsHandler$Type, arg1: string)
constructor(arg0: $DefaultOptionsHandler$Type)

public "getHandlerId"(): string
get "handlerId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultOptionsHandlerException$Type = ($DefaultOptionsHandlerException);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultOptionsHandlerException_ = $DefaultOptionsHandlerException$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/config/$DefaultOptionsConfigData" {
import {$UnobfuscatedDifficulty, $UnobfuscatedDifficulty$Type} from "packages/net/blay09/mods/defaultoptions/difficulty/$UnobfuscatedDifficulty"
import {$BalmConfigData, $BalmConfigData$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfigData"

export class $DefaultOptionsConfigData implements $BalmConfigData {
 "defaultDifficulty": $UnobfuscatedDifficulty
 "lockDifficulty": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultOptionsConfigData$Type = ($DefaultOptionsConfigData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultOptionsConfigData_ = $DefaultOptionsConfigData$Type;
}}
declare module "packages/net/blay09/mods/waystones/stats/$ModStats" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ModStats {
static readonly "waystoneActivated": $ResourceLocation

constructor()

public static "initialize"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModStats$Type = ($ModStats);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModStats_ = $ModStats$Type;
}}
declare module "packages/net/blay09/mods/balm/api/$Balm" {
import {$PreparableReloadListener, $PreparableReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener"
import {$BalmEntities, $BalmEntities$Type} from "packages/net/blay09/mods/balm/api/entity/$BalmEntities"
import {$SidedProxy, $SidedProxy$Type} from "packages/net/blay09/mods/balm/api/proxy/$SidedProxy"
import {$BalmSounds, $BalmSounds$Type} from "packages/net/blay09/mods/balm/api/sound/$BalmSounds"
import {$BalmBlockEntities, $BalmBlockEntities$Type} from "packages/net/blay09/mods/balm/api/block/$BalmBlockEntities"
import {$BalmCommands, $BalmCommands$Type} from "packages/net/blay09/mods/balm/api/command/$BalmCommands"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$BalmProxy, $BalmProxy$Type} from "packages/net/blay09/mods/balm/api/$BalmProxy"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$BalmStats, $BalmStats$Type} from "packages/net/blay09/mods/balm/api/stats/$BalmStats"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$BalmRecipes, $BalmRecipes$Type} from "packages/net/blay09/mods/balm/api/recipe/$BalmRecipes"
import {$BalmNetworking, $BalmNetworking$Type} from "packages/net/blay09/mods/balm/api/network/$BalmNetworking"
import {$BalmBlocks, $BalmBlocks$Type} from "packages/net/blay09/mods/balm/api/block/$BalmBlocks"
import {$BalmItems, $BalmItems$Type} from "packages/net/blay09/mods/balm/api/item/$BalmItems"
import {$BalmConfig, $BalmConfig$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfig"
import {$BalmEvents, $BalmEvents$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvents"
import {$BalmRegistries, $BalmRegistries$Type} from "packages/net/blay09/mods/balm/api/$BalmRegistries"
import {$BalmProviders, $BalmProviders$Type} from "packages/net/blay09/mods/balm/api/provider/$BalmProviders"
import {$BalmLootTables, $BalmLootTables$Type} from "packages/net/blay09/mods/balm/api/loot/$BalmLootTables"
import {$BalmHooks, $BalmHooks$Type} from "packages/net/blay09/mods/balm/api/$BalmHooks"
import {$BalmWorldGen, $BalmWorldGen$Type} from "packages/net/blay09/mods/balm/api/world/$BalmWorldGen"
import {$BalmMenus, $BalmMenus$Type} from "packages/net/blay09/mods/balm/api/menu/$BalmMenus"

export class $Balm {

constructor()

public static "initialize"(arg0: string, arg1: $Runnable$Type): void
public static "initialize"(arg0: string): void
public static "getProviders"(): $BalmProviders
public static "getCommands"(): $BalmCommands
public static "getEvents"(): $BalmEvents
public static "initializeIfLoaded"(arg0: string, arg1: string): void
public static "getConfig"(): $BalmConfig
public static "getEntities"(): $BalmEntities
public static "getStats"(): $BalmStats
public static "getItems"(): $BalmItems
public static "getProxy"(): $BalmProxy
public static "getModName"(arg0: string): string
public static "isModLoaded"(arg0: string): boolean
public static "getBlocks"(): $BalmBlocks
public static "getWorldGen"(): $BalmWorldGen
public static "getMenus"(): $BalmMenus
public static "getHooks"(): $BalmHooks
public static "sidedProxy"<T>(arg0: string, arg1: string): $SidedProxy<(T)>
public static "getNetworking"(): $BalmNetworking
public static "getLootTables"(): $BalmLootTables
public static "getSounds"(): $BalmSounds
public static "addServerReloadListener"(arg0: $ResourceLocation$Type, arg1: $PreparableReloadListener$Type): void
public static "addServerReloadListener"(arg0: $ResourceLocation$Type, arg1: $Consumer$Type<($ResourceManager$Type)>): void
public static "getRecipes"(): $BalmRecipes
public static "getRegistries"(): $BalmRegistries
public static "getBlockEntities"(): $BalmBlockEntities
get "providers"(): $BalmProviders
get "commands"(): $BalmCommands
get "events"(): $BalmEvents
get "config"(): $BalmConfig
get "entities"(): $BalmEntities
get "stats"(): $BalmStats
get "items"(): $BalmItems
get "proxy"(): $BalmProxy
get "blocks"(): $BalmBlocks
get "worldGen"(): $BalmWorldGen
get "menus"(): $BalmMenus
get "hooks"(): $BalmHooks
get "networking"(): $BalmNetworking
get "lootTables"(): $BalmLootTables
get "sounds"(): $BalmSounds
get "recipes"(): $BalmRecipes
get "registries"(): $BalmRegistries
get "blockEntities"(): $BalmBlockEntities
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Balm$Type = ($Balm);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Balm_ = $Balm$Type;
}}
declare module "packages/net/blay09/mods/balm/api/client/rendering/$BalmRenderers" {
import {$BlockColor, $BlockColor$Type} from "packages/net/minecraft/client/color/block/$BlockColor"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$ItemColor, $ItemColor$Type} from "packages/net/minecraft/client/color/item/$ItemColor"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$RenderType, $RenderType$Type} from "packages/net/minecraft/client/renderer/$RenderType"
import {$EntityRendererProvider, $EntityRendererProvider$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRendererProvider"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ModelLayerLocation, $ModelLayerLocation$Type} from "packages/net/minecraft/client/model/geom/$ModelLayerLocation"
import {$BlockEntityRendererProvider, $BlockEntityRendererProvider$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRendererProvider"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $BalmRenderers {

 "registerItemColorHandler"(arg0: $ItemColor$Type, arg1: $Supplier$Type<(($ItemLike$Type)[])>): void
 "registerBlockColorHandler"(arg0: $BlockColor$Type, arg1: $Supplier$Type<(($Block$Type)[])>): void
 "registerEntityRenderer"<T extends $Entity>(arg0: $Supplier$Type<($EntityType$Type<(T)>)>, arg1: $EntityRendererProvider$Type<(any)>): void
 "registerBlockEntityRenderer"<T extends $BlockEntity>(arg0: $Supplier$Type<($BlockEntityType$Type<(T)>)>, arg1: $BlockEntityRendererProvider$Type<(any)>): void
/**
 * 
 * @deprecated
 */
 "setBlockRenderType"(arg0: $Supplier$Type<($Block$Type)>, arg1: $RenderType$Type): void
 "registerModel"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($LayerDefinition$Type)>): $ModelLayerLocation
}

export namespace $BalmRenderers {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmRenderers$Type = ($BalmRenderers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmRenderers_ = $BalmRenderers$Type;
}}
declare module "packages/net/blay09/mods/waystones/config/$WaystonesConfigData$Compatibility" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $WaystonesConfigData$Compatibility {
 "displayWaystonesOnJourneyMap": boolean
 "preferJourneyMapIntegration": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystonesConfigData$Compatibility$Type = ($WaystonesConfigData$Compatibility);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystonesConfigData$Compatibility_ = $WaystonesConfigData$Compatibility$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/entity/$ForgeBalmEntities" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$BalmEntities, $BalmEntities$Type} from "packages/net/blay09/mods/balm/api/entity/$BalmEntities"
import {$EntityType$Builder, $EntityType$Builder$Type} from "packages/net/minecraft/world/entity/$EntityType$Builder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$AttributeSupplier$Builder, $AttributeSupplier$Builder$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeSupplier$Builder"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ForgeBalmEntities implements $BalmEntities {

constructor()

public "register"(): void
public "registerEntity"<T extends $LivingEntity>(arg0: $ResourceLocation$Type, arg1: $EntityType$Builder$Type<(T)>, arg2: $Supplier$Type<($AttributeSupplier$Builder$Type)>): $DeferredObject<($EntityType<(T)>)>
public "registerEntity"<T extends $Entity>(arg0: $ResourceLocation$Type, arg1: $EntityType$Builder$Type<(T)>): $DeferredObject<($EntityType<(T)>)>
/**
 * 
 * @deprecated
 */
public "registerEntity"<T extends $LivingEntity>(arg0: $ResourceLocation$Type, arg1: $EntityType$Builder$Type<(T)>, arg2: $AttributeSupplier$Builder$Type): $DeferredObject<($EntityType<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmEntities$Type = ($ForgeBalmEntities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmEntities_ = $ForgeBalmEntities$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$NoopHandler" {
import {$GridBalanceHandler, $GridBalanceHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridBalanceHandler"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$GridTransferHandler, $GridTransferHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridTransferHandler"
import {$GridRotateHandler, $GridRotateHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridRotateHandler"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$GridClearHandler, $GridClearHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridClearHandler"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $NoopHandler implements $GridBalanceHandler<($AbstractContainerMenu)>, $GridTransferHandler<($AbstractContainerMenu)>, $GridClearHandler<($AbstractContainerMenu)>, $GridRotateHandler<($AbstractContainerMenu)> {

constructor()

public "clearGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: $AbstractContainerMenu$Type, arg3: boolean): void
public "canTransferFrom"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type, arg2: $Slot$Type, arg3: $CraftingGrid$Type): boolean
public "spreadGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: $AbstractContainerMenu$Type): void
public "balanceGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: $AbstractContainerMenu$Type): void
public "transferIntoGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: $AbstractContainerMenu$Type, arg3: $Slot$Type): boolean
public "rotateGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: $AbstractContainerMenu$Type, arg3: boolean): void
public "putIntoGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: $AbstractContainerMenu$Type, arg3: integer, arg4: $ItemStack$Type): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NoopHandler$Type = ($NoopHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NoopHandler_ = $NoopHandler$Type;
}}
declare module "packages/net/blay09/mods/waystones/worldgen/namegen/$NameGenerator" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$SavedData, $SavedData$Type} from "packages/net/minecraft/world/level/saveddata/$SavedData"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$NameGenerationMode, $NameGenerationMode$Type} from "packages/net/blay09/mods/waystones/worldgen/namegen/$NameGenerationMode"

export class $NameGenerator extends $SavedData {

constructor()

public "getName"(arg0: $IWaystone$Type, arg1: $RandomSource$Type, arg2: $NameGenerationMode$Type): string
public static "get"(arg0: $MinecraftServer$Type): $NameGenerator
public static "load"(arg0: $CompoundTag$Type): $NameGenerator
public "save"(arg0: $CompoundTag$Type): $CompoundTag
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NameGenerator$Type = ($NameGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NameGenerator_ = $NameGenerator$Type;
}}
declare module "packages/net/blay09/mods/balm/api/entity/$BalmEntities" {
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$EntityType$Builder, $EntityType$Builder$Type} from "packages/net/minecraft/world/entity/$EntityType$Builder"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$AttributeSupplier$Builder, $AttributeSupplier$Builder$Type} from "packages/net/minecraft/world/entity/ai/attributes/$AttributeSupplier$Builder"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $BalmEntities {

/**
 * 
 * @deprecated
 */
 "registerEntity"<T extends $LivingEntity>(arg0: $ResourceLocation$Type, arg1: $EntityType$Builder$Type<(T)>, arg2: $AttributeSupplier$Builder$Type): $DeferredObject<($EntityType<(T)>)>
 "registerEntity"<T extends $LivingEntity>(arg0: $ResourceLocation$Type, arg1: $EntityType$Builder$Type<(T)>, arg2: $Supplier$Type<($AttributeSupplier$Builder$Type)>): $DeferredObject<($EntityType<(T)>)>
 "registerEntity"<T extends $Entity>(arg0: $ResourceLocation$Type, arg1: $EntityType$Builder$Type<(T)>): $DeferredObject<($EntityType<(T)>)>
}

export namespace $BalmEntities {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmEntities$Type = ($BalmEntities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmEntities_ = $BalmEntities$Type;
}}
declare module "packages/net/blay09/mods/waystones/compat/$Compat" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Compat {
static readonly "THEONEPROBE": string
static readonly "VIVECRAFT": string
static "isVivecraftInstalled": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Compat$Type = ($Compat);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Compat_ = $Compat$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/client/$GuiTweakButton" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TweakType, $TweakType$Type} from "packages/net/blay09/mods/craftingtweaks/api/$TweakType"
import {$ButtonStyle, $ButtonStyle$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonStyle"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ITooltipProvider, $ITooltipProvider$Type} from "packages/net/blay09/mods/craftingtweaks/client/$ITooltipProvider"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$GuiImageButton, $GuiImageButton$Type} from "packages/net/blay09/mods/craftingtweaks/client/$GuiImageButton"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $GuiTweakButton extends $GuiImageButton implements $ITooltipProvider {
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: $AbstractContainerScreen$Type<(any)>, arg1: integer, arg2: integer, arg3: $ButtonStyle$Type, arg4: $CraftingGrid$Type, arg5: $TweakType$Type, arg6: $TweakType$Type)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "onClick"(arg0: double, arg1: double): void
public "getTooltipComponents"(): $List<($Component)>
get "tooltipComponents"(): $List<($Component)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiTweakButton$Type = ($GuiTweakButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiTweakButton_ = $GuiTweakButton$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$ClientStartedEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $ClientStartedEvent extends $BalmEvent {

constructor()
constructor(arg0: $Minecraft$Type)

public "getMinecraft"(): $Minecraft
public "getListenerList"(): $ListenerList
get "minecraft"(): $Minecraft
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientStartedEvent$Type = ($ClientStartedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientStartedEvent_ = $ClientStartedEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/container/$DefaultContainer" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ImplementedContainer, $ImplementedContainer$Type} from "packages/net/blay09/mods/balm/api/container/$ImplementedContainer"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $DefaultContainer implements $ImplementedContainer {

constructor(arg0: integer)
constructor(arg0: $NonNullList$Type<($ItemStack$Type)>)

public "deserialize"(arg0: $CompoundTag$Type): void
public "serialize"(): $CompoundTag
public "getItems"(): $NonNullList<($ItemStack)>
public static "of"(arg0: $NonNullList$Type<($ItemStack$Type)>): $ImplementedContainer
public "serializeInventory"(): $CompoundTag
public "setChanged"(): void
public static "deserializeInventory"(arg0: $CompoundTag$Type, arg1: integer): $NonNullList<($ItemStack)>
public "getItem"(arg0: integer): $ItemStack
public "getContainerSize"(): integer
public "removeItemNoUpdate"(arg0: integer): $ItemStack
public static "ofSize"(arg0: integer): $ImplementedContainer
public "slotChanged"(arg0: integer): void
public "removeItem"(arg0: integer, arg1: integer): $ItemStack
public "clearContent"(): void
public "isEmpty"(): boolean
public "stillValid"(arg0: $Player$Type): boolean
public "setItem"(arg0: integer, arg1: $ItemStack$Type): void
public "kjs$self"(): $Container
public "getBlock"(level: $Level$Type): $BlockContainerJS
public "startOpen"(arg0: $Player$Type): void
public "getMaxStackSize"(): integer
public "stopOpen"(arg0: $Player$Type): void
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public "canPlaceItem"(arg0: integer, arg1: $ItemStack$Type): boolean
public "countItem"(arg0: $Item$Type): integer
public "canTakeItem"(arg0: $Container$Type, arg1: integer, arg2: $ItemStack$Type): boolean
public "hasAnyMatching"(arg0: $Predicate$Type<($ItemStack$Type)>): boolean
public "getSlots"(): integer
public "getStackInSlot"(slot: integer): $ItemStack
public "insertItem"(slot: integer, stack: $ItemStack$Type, simulate: boolean): $ItemStack
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public "isMutable"(): boolean
public "hasAnyOf"(arg0: $Set$Type<($Item$Type)>): boolean
public "setChanged"(): void
public "asContainer"(): $Container
public "getHeight"(): integer
public "extractItem"(slot: integer, amount: integer, simulate: boolean): $ItemStack
public "isItemValid"(slot: integer, stack: $ItemStack$Type): boolean
public "getWidth"(): integer
public "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
public "getSlotLimit"(slot: integer): integer
public "clear"(): void
public static "tryClear"(arg0: any): void
public "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
public "countNonEmpty"(ingredient: $Ingredient$Type): integer
public "countNonEmpty"(): integer
public "getAllItems"(): $List<($ItemStack)>
public "find"(ingredient: $Ingredient$Type): integer
public "find"(): integer
public "clear"(ingredient: $Ingredient$Type): void
public "count"(ingredient: $Ingredient$Type): integer
public "count"(): integer
public "isEmpty"(): boolean
get "items"(): $NonNullList<($ItemStack)>
get "containerSize"(): integer
get "empty"(): boolean
get "maxStackSize"(): integer
get "slots"(): integer
get "mutable"(): boolean
get "height"(): integer
get "width"(): integer
get "allItems"(): $List<($ItemStack)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultContainer$Type = ($DefaultContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultContainer_ = $DefaultContainer$Type;
}}
declare module "packages/net/blay09/mods/waystones/core/$IPlayerWaystoneData" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"

export interface $IPlayerWaystoneData {

 "getWarpStoneCooldownUntil"(arg0: $Player$Type): long
 "getInventoryButtonCooldownUntil"(arg0: $Player$Type): long
 "getWaystones"(arg0: $Player$Type): $List<($IWaystone)>
 "setWarpStoneCooldownUntil"(arg0: $Player$Type, arg1: long): void
 "activateWaystone"(arg0: $Player$Type, arg1: $IWaystone$Type): void
 "swapWaystoneSorting"(arg0: $Player$Type, arg1: integer, arg2: integer): void
 "isWaystoneActivated"(arg0: $Player$Type, arg1: $IWaystone$Type): boolean
 "deactivateWaystone"(arg0: $Player$Type, arg1: $IWaystone$Type): void
 "setInventoryButtonCooldownUntil"(arg0: $Player$Type, arg1: long): void
}

export namespace $IPlayerWaystoneData {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPlayerWaystoneData$Type = ($IPlayerWaystoneData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPlayerWaystoneData_ = $IPlayerWaystoneData$Type;
}}
declare module "packages/net/blay09/mods/waystones/compat/jei/$AttunedShardJeiRecipe" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $AttunedShardJeiRecipe {

constructor()

public "getInputs"(): $List<($ItemStack)>
public "getOutput"(): $ItemStack
get "inputs"(): $List<($ItemStack)>
get "output"(): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttunedShardJeiRecipe$Type = ($AttunedShardJeiRecipe);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttunedShardJeiRecipe_ = $AttunedShardJeiRecipe$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$GridBalanceHandler" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export interface $GridBalanceHandler<TMenu extends $AbstractContainerMenu> {

 "spreadGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: TMenu): void
 "balanceGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: TMenu): void
}

export namespace $GridBalanceHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GridBalanceHandler$Type<TMenu> = ($GridBalanceHandler<(TMenu)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GridBalanceHandler_<TMenu> = $GridBalanceHandler$Type<(TMenu)>;
}}
declare module "packages/net/blay09/mods/waystones/client/gui/screen/$InventoryButtonReturnConfirmScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$ConfirmScreen, $ConfirmScreen$Type} from "packages/net/minecraft/client/gui/screens/$ConfirmScreen"
import {$BooleanConsumer, $BooleanConsumer$Type} from "packages/it/unimi/dsi/fastutil/booleans/$BooleanConsumer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $InventoryButtonReturnConfirmScreen extends $ConfirmScreen {
readonly "callback": $BooleanConsumer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor()
constructor(arg0: string)

public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InventoryButtonReturnConfirmScreen$Type = ($InventoryButtonReturnConfirmScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InventoryButtonReturnConfirmScreen_ = $InventoryButtonReturnConfirmScreen$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/server/$ServerReloadFinishedEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $ServerReloadFinishedEvent extends $BalmEvent {

constructor()
constructor(arg0: $MinecraftServer$Type)

public "getServer"(): $MinecraftServer
public "getListenerList"(): $ListenerList
get "server"(): $MinecraftServer
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerReloadFinishedEvent$Type = ($ServerReloadFinishedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerReloadFinishedEvent_ = $ServerReloadFinishedEvent$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/client/$CraftingTweaksClient" {
import {$ScreenInitEvent, $ScreenInitEvent$Type} from "packages/net/blay09/mods/balm/api/event/client/screen/$ScreenInitEvent"
import {$ScreenMouseEvent, $ScreenMouseEvent$Type} from "packages/net/blay09/mods/balm/api/event/client/screen/$ScreenMouseEvent"
import {$ScreenKeyEvent, $ScreenKeyEvent$Type} from "packages/net/blay09/mods/balm/api/event/client/screen/$ScreenKeyEvent"
import {$ClientProvider, $ClientProvider$Type} from "packages/net/blay09/mods/craftingtweaks/client/$ClientProvider"
import {$ScreenDrawEvent, $ScreenDrawEvent$Type} from "packages/net/blay09/mods/balm/api/event/client/screen/$ScreenDrawEvent"

export class $CraftingTweaksClient {

constructor()

public static "initialize"(): void
public static "screenMouseClick"(arg0: $ScreenMouseEvent$Type): boolean
public static "screenDrawn"(arg0: $ScreenDrawEvent$Type): void
public static "screenInitialized"(arg0: $ScreenInitEvent$Type): void
public static "screenMouseRelease"(arg0: $ScreenMouseEvent$Type): boolean
public static "screenKeyPressed"(arg0: $ScreenKeyEvent$Type): boolean
public static "screenAboutToDraw"(arg0: $ScreenDrawEvent$Type): void
public static "getClientProvider"(): $ClientProvider
get "clientProvider"(): $ClientProvider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksClient$Type = ($CraftingTweaksClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksClient_ = $CraftingTweaksClient$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/command/$CraftingTweaksCommand" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $CraftingTweaksCommand {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksCommand$Type = ($CraftingTweaksCommand);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksCommand_ = $CraftingTweaksCommand$Type;
}}
declare module "packages/net/blay09/mods/waystones/network/message/$KnownWaystonesMessage" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $KnownWaystonesMessage {

constructor(arg0: $ResourceLocation$Type, arg1: $List$Type<($IWaystone$Type)>)

public static "decode"(arg0: $FriendlyByteBuf$Type): $KnownWaystonesMessage
public static "encode"(arg0: $KnownWaystonesMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $Player$Type, arg1: $KnownWaystonesMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KnownWaystonesMessage$Type = ($KnownWaystonesMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KnownWaystonesMessage_ = $KnownWaystonesMessage$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$WaystoneStyle" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $WaystoneStyle {

constructor(arg0: $ResourceLocation$Type)

public "getBlockRegistryName"(): $ResourceLocation
get "blockRegistryName"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneStyle$Type = ($WaystoneStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneStyle_ = $WaystoneStyle$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/gui/widget/$SortWaystoneButton" {
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $SortWaystoneButton extends $Button {
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer, arg4: integer, arg5: $Button$OnPress$Type)

public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SortWaystoneButton$Type = ($SortWaystoneButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SortWaystoneButton_ = $SortWaystoneButton$Type;
}}
declare module "packages/net/blay09/mods/balm/api/config/$BalmConfigPropertyImpl" {
import {$BalmConfigProperty, $BalmConfigProperty$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfigProperty"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"
import {$BalmConfigData, $BalmConfigData$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfigData"

export class $BalmConfigPropertyImpl<T> implements $BalmConfigProperty<(T)> {

constructor(arg0: $BalmConfigData$Type, arg1: $Field$Type, arg2: $Field$Type, arg3: $BalmConfigData$Type)

public "getValue"(): T
public "setValue"(arg0: T): void
public "getType"(): $Class<(T)>
public "getDefaultValue"(): T
public "getInnerType"(): $Class<(T)>
get "value"(): T
set "value"(value: T)
get "type"(): $Class<(T)>
get "defaultValue"(): T
get "innerType"(): $Class<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmConfigPropertyImpl$Type<T> = ($BalmConfigPropertyImpl<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmConfigPropertyImpl_<T> = $BalmConfigPropertyImpl$Type<(T)>;
}}
declare module "packages/net/blay09/mods/waystones/compat/$TheOneProbeIntegration" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $TheOneProbeIntegration {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TheOneProbeIntegration$Type = ($TheOneProbeIntegration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TheOneProbeIntegration_ = $TheOneProbeIntegration$Type;
}}
declare module "packages/net/blay09/mods/waystones/menu/$WarpPlateAttunementSlot" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$WarpPlateBlockEntity, $WarpPlateBlockEntity$Type} from "packages/net/blay09/mods/waystones/block/entity/$WarpPlateBlockEntity"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $WarpPlateAttunementSlot extends $Slot {
readonly "container": $Container
 "index": integer
 "x": integer
 "y": integer

constructor(arg0: $WarpPlateBlockEntity$Type, arg1: integer, arg2: integer, arg3: integer)

public "getMaxStackSize"(arg0: $ItemStack$Type): integer
public "mayPickup"(arg0: $Player$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpPlateAttunementSlot$Type = ($WarpPlateAttunementSlot);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpPlateAttunementSlot_ = $WarpPlateAttunementSlot$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/world/$BalmBiomeModifier" {
import {$ModifiableBiomeInfo$BiomeInfo$Builder, $ModifiableBiomeInfo$BiomeInfo$Builder$Type} from "packages/net/minecraftforge/common/world/$ModifiableBiomeInfo$BiomeInfo$Builder"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$BiomeModifier, $BiomeModifier$Type} from "packages/net/minecraftforge/common/world/$BiomeModifier"
import {$BiomeModifier$Phase, $BiomeModifier$Phase$Type} from "packages/net/minecraftforge/common/world/$BiomeModifier$Phase"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"

export class $BalmBiomeModifier implements $BiomeModifier {
static readonly "INSTANCE": $BalmBiomeModifier

constructor()

public "modify"(arg0: $Holder$Type<($Biome$Type)>, arg1: $BiomeModifier$Phase$Type, arg2: $ModifiableBiomeInfo$BiomeInfo$Builder$Type): void
public "codec"(): $Codec<(any)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmBiomeModifier$Type = ($BalmBiomeModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmBiomeModifier_ = $BalmBiomeModifier$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$WaystoneOrigin" {
import {$StringRepresentable$EnumCodec, $StringRepresentable$EnumCodec$Type} from "packages/net/minecraft/util/$StringRepresentable$EnumCodec"
import {$StringRepresentable, $StringRepresentable$Type} from "packages/net/minecraft/util/$StringRepresentable"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Keyable, $Keyable$Type} from "packages/com/mojang/serialization/$Keyable"
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $WaystoneOrigin extends $Enum<($WaystoneOrigin)> implements $StringRepresentable {
static readonly "UNKNOWN": $WaystoneOrigin
static readonly "WILDERNESS": $WaystoneOrigin
static readonly "DUNGEON": $WaystoneOrigin
static readonly "VILLAGE": $WaystoneOrigin
static readonly "PLAYER": $WaystoneOrigin


public static "values"(): ($WaystoneOrigin)[]
public static "valueOf"(arg0: string): $WaystoneOrigin
public "getSerializedName"(): string
public static "fromEnumWithMapping"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>, arg1: $Function$Type<(string), (string)>): $StringRepresentable$EnumCodec<(E)>
public static "keys"(arg0: ($StringRepresentable$Type)[]): $Keyable
public static "fromEnum"<E extends ($Enum<(E)>) & ($StringRepresentable)>(arg0: $Supplier$Type<((E)[])>): $StringRepresentable$EnumCodec<(E)>
get "serializedName"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneOrigin$Type = (("wilderness") | ("village") | ("dungeon") | ("unknown") | ("player")) | ($WaystoneOrigin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneOrigin_ = $WaystoneOrigin$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/client/screen/$ForgeBalmScreens" {
import {$BalmScreenFactory, $BalmScreenFactory$Type} from "packages/net/blay09/mods/balm/api/client/screen/$BalmScreenFactory"
import {$AbstractWidget, $AbstractWidget$Type} from "packages/net/minecraft/client/gui/components/$AbstractWidget"
import {$BalmScreens, $BalmScreens$Type} from "packages/net/blay09/mods/balm/api/client/screen/$BalmScreens"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$MenuAccess, $MenuAccess$Type} from "packages/net/minecraft/client/gui/screens/inventory/$MenuAccess"

export class $ForgeBalmScreens implements $BalmScreens {

constructor()

public "register"(): void
public "registerScreen"<T extends $AbstractContainerMenu, S extends ($Screen) & ($MenuAccess<(T)>)>(arg0: $Supplier$Type<($MenuType$Type<(any)>)>, arg1: $BalmScreenFactory$Type<(T), (S)>): void
public "addRenderableWidget"(arg0: $Screen$Type, arg1: $AbstractWidget$Type): $AbstractWidget
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmScreens$Type = ($ForgeBalmScreens);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmScreens_ = $ForgeBalmScreens$Type;
}}
declare module "packages/net/blay09/mods/waystones/worldgen/$ModWorldGen" {
import {$RegistryAccess, $RegistryAccess$Type} from "packages/net/minecraft/core/$RegistryAccess"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$WaystonePlacement, $WaystonePlacement$Type} from "packages/net/blay09/mods/waystones/worldgen/$WaystonePlacement"
import {$BalmWorldGen, $BalmWorldGen$Type} from "packages/net/blay09/mods/balm/api/world/$BalmWorldGen"
import {$PlacementModifierType, $PlacementModifierType$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifierType"

export class $ModWorldGen {
static "waystonePlacement": $DeferredObject<($PlacementModifierType<($WaystonePlacement)>)>

constructor()

public static "initialize"(arg0: $BalmWorldGen$Type): void
public static "setupDynamicRegistries"(arg0: $RegistryAccess$Type): void
set "upDynamicRegistries"(value: $RegistryAccess$Type)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModWorldGen$Type = ($ModWorldGen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModWorldGen_ = $ModWorldGen$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/client/$ForgeBalmClientRuntimeFactory" {
import {$BalmClientRuntimeFactory, $BalmClientRuntimeFactory$Type} from "packages/net/blay09/mods/balm/api/client/$BalmClientRuntimeFactory"
import {$BalmClientRuntime, $BalmClientRuntime$Type} from "packages/net/blay09/mods/balm/api/client/$BalmClientRuntime"

export class $ForgeBalmClientRuntimeFactory implements $BalmClientRuntimeFactory {

constructor()

public "create"(): $BalmClientRuntime
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmClientRuntimeFactory$Type = ($ForgeBalmClientRuntimeFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmClientRuntimeFactory_ = $ForgeBalmClientRuntimeFactory$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/api/$InternalMethods" {
import {$SimpleDefaultOptionsHandler, $SimpleDefaultOptionsHandler$Type} from "packages/net/blay09/mods/defaultoptions/api/$SimpleDefaultOptionsHandler"
import {$File, $File$Type} from "packages/java/io/$File"
import {$DefaultOptionsHandler, $DefaultOptionsHandler$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsHandler"

export interface $InternalMethods {

 "getDefaultOptionsFolder"(): $File
 "registerOptionsHandler"(arg0: $DefaultOptionsHandler$Type): void
 "registerOptionsFile"(arg0: $File$Type): $SimpleDefaultOptionsHandler
}

export namespace $InternalMethods {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalMethods$Type = ($InternalMethods);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalMethods_ = $InternalMethods$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$WaystoneUpdateReceivedEvent" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $WaystoneUpdateReceivedEvent extends $BalmEvent {

constructor()
constructor(arg0: $IWaystone$Type)

public "getWaystone"(): $IWaystone
public "getListenerList"(): $ListenerList
get "waystone"(): $IWaystone
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneUpdateReceivedEvent$Type = ($WaystoneUpdateReceivedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneUpdateReceivedEvent_ = $WaystoneUpdateReceivedEvent$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/$SimpleDefaultOptionsFileHandler" {
import {$SimpleDefaultOptionsHandler, $SimpleDefaultOptionsHandler$Type} from "packages/net/blay09/mods/defaultoptions/api/$SimpleDefaultOptionsHandler"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$DefaultOptionsLoadStage, $DefaultOptionsLoadStage$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsLoadStage"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$DefaultOptionsCategory, $DefaultOptionsCategory$Type} from "packages/net/blay09/mods/defaultoptions/api/$DefaultOptionsCategory"

export class $SimpleDefaultOptionsFileHandler implements $SimpleDefaultOptionsHandler {

constructor(arg0: $File$Type)

public "getId"(): string
public "getFile"(): $File
public "getLoadStage"(): $DefaultOptionsLoadStage
public "loadDefaults"(): void
public "shouldLoadDefaults"(): boolean
public "withLinePredicate"(arg0: $Predicate$Type<(string)>): $SimpleDefaultOptionsHandler
public "withLoadStage"(arg0: $DefaultOptionsLoadStage$Type): $SimpleDefaultOptionsHandler
public "saveCurrentOptions"(): void
public "withCategory"(arg0: $DefaultOptionsCategory$Type): $SimpleDefaultOptionsHandler
public "withSaveHandler"(arg0: $Runnable$Type): $SimpleDefaultOptionsHandler
public "getDefaultsFile"(): $File
public "getCategory"(): $DefaultOptionsCategory
public "hasDefaults"(): boolean
public "saveCurrentOptionsAsDefault"(): void
get "id"(): string
get "file"(): $File
get "loadStage"(): $DefaultOptionsLoadStage
get "defaultsFile"(): $File
get "category"(): $DefaultOptionsCategory
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SimpleDefaultOptionsFileHandler$Type = ($SimpleDefaultOptionsFileHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SimpleDefaultOptionsFileHandler_ = $SimpleDefaultOptionsFileHandler$Type;
}}
declare module "packages/net/blay09/mods/kuma/api/$KeyModifier" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $KeyModifier extends $Enum<($KeyModifier)> {
static readonly "NONE": $KeyModifier
static readonly "SHIFT": $KeyModifier
static readonly "CONTROL": $KeyModifier
static readonly "ALT": $KeyModifier


public static "values"(): ($KeyModifier)[]
public static "valueOf"(arg0: string): $KeyModifier
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyModifier$Type = (("shift") | ("alt") | ("control") | ("none")) | ($KeyModifier);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyModifier_ = $KeyModifier$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/config/$CraftingTweaksConfigData" {
import {$CraftingTweaksConfigData$Common, $CraftingTweaksConfigData$Common$Type} from "packages/net/blay09/mods/craftingtweaks/config/$CraftingTweaksConfigData$Common"
import {$CraftingTweaksMode, $CraftingTweaksMode$Type} from "packages/net/blay09/mods/craftingtweaks/config/$CraftingTweaksMode"
import {$CraftingTweaksConfigData$Client, $CraftingTweaksConfigData$Client$Type} from "packages/net/blay09/mods/craftingtweaks/config/$CraftingTweaksConfigData$Client"
import {$BalmConfigData, $BalmConfigData$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfigData"

export class $CraftingTweaksConfigData implements $BalmConfigData {
 "common": $CraftingTweaksConfigData$Common
 "client": $CraftingTweaksConfigData$Client

constructor()

public "getCraftingTweaksMode"(arg0: string): $CraftingTweaksMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksConfigData$Type = ($CraftingTweaksConfigData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksConfigData_ = $CraftingTweaksConfigData$Type;
}}
declare module "packages/net/blay09/mods/balm/api/energy/$BalmEnergyStorageProvider" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$EnergyStorage, $EnergyStorage$Type} from "packages/net/blay09/mods/balm/api/energy/$EnergyStorage"

export interface $BalmEnergyStorageProvider {

 "getEnergyStorage"(): $EnergyStorage
 "getEnergyStorage"(arg0: $Direction$Type): $EnergyStorage

(): $EnergyStorage
}

export namespace $BalmEnergyStorageProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmEnergyStorageProvider$Type = ($BalmEnergyStorageProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmEnergyStorageProvider_ = $BalmEnergyStorageProvider$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/screen/$ScreenDrawEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ScreenDrawEvent extends $BalmEvent {

constructor()
constructor(arg0: $Screen$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer, arg4: float)

public "getMouseX"(): integer
public "getMouseY"(): integer
public "getTickDelta"(): float
public "getScreen"(): $Screen
public "getGuiGraphics"(): $GuiGraphics
public "getListenerList"(): $ListenerList
get "mouseX"(): integer
get "mouseY"(): integer
get "tickDelta"(): float
get "screen"(): $Screen
get "guiGraphics"(): $GuiGraphics
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenDrawEvent$Type = ($ScreenDrawEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenDrawEvent_ = $ScreenDrawEvent$Type;
}}
declare module "packages/net/blay09/mods/waystones/mixin/$StructureTemplatePoolAccessor" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$StructurePoolElement, $StructurePoolElement$Type} from "packages/net/minecraft/world/level/levelgen/structure/pools/$StructurePoolElement"
import {$ObjectArrayList, $ObjectArrayList$Type} from "packages/it/unimi/dsi/fastutil/objects/$ObjectArrayList"

export interface $StructureTemplatePoolAccessor {

 "setTemplates"(arg0: $ObjectArrayList$Type<($StructurePoolElement$Type)>): void
 "getRawTemplates"(): $List<($Pair<($StructurePoolElement), (integer)>)>
 "setRawTemplates"(arg0: $List$Type<($Pair$Type<($StructurePoolElement$Type), (integer)>)>): void
 "getTemplates"(): $ObjectArrayList<($StructurePoolElement)>
}

export namespace $StructureTemplatePoolAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StructureTemplatePoolAccessor$Type = ($StructureTemplatePoolAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StructureTemplatePoolAccessor_ = $StructureTemplatePoolAccessor$Type;
}}
declare module "packages/net/blay09/mods/balm/api/menu/$BalmMenuProvider" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export interface $BalmMenuProvider extends $MenuProvider {

 "writeScreenOpeningData"(arg0: $ServerPlayer$Type, arg1: $FriendlyByteBuf$Type): void
 "getDisplayName"(): $Component
 "createMenu"(arg0: integer, arg1: $Inventory$Type, arg2: $Player$Type): $AbstractContainerMenu
}

export namespace $BalmMenuProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmMenuProvider$Type = ($BalmMenuProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmMenuProvider_ = $BalmMenuProvider$Type;
}}
declare module "packages/net/blay09/mods/balm/api/world/$BiomePredicate" {
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $BiomePredicate {

 "test"(arg0: $ResourceLocation$Type, arg1: $Holder$Type<($Biome$Type)>): boolean

(arg0: $ResourceLocation$Type, arg1: $Holder$Type<($Biome$Type)>): boolean
}

export namespace $BiomePredicate {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomePredicate$Type = ($BiomePredicate);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomePredicate_ = $BiomePredicate$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/network/$HelloMessage" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $HelloMessage {

constructor()

public static "decode"(arg0: $FriendlyByteBuf$Type): $HelloMessage
public static "encode"(arg0: $HelloMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $Player$Type, arg1: $HelloMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HelloMessage$Type = ($HelloMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HelloMessage_ = $HelloMessage$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$ButtonPosition" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ButtonPosition {

constructor(arg0: integer, arg1: integer)

public "getY"(): integer
public "getX"(): integer
get "y"(): integer
get "x"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ButtonPosition$Type = ($ButtonPosition);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ButtonPosition_ = $ButtonPosition$Type;
}}
declare module "packages/net/blay09/mods/waystones/worldgen/namegen/$CustomNameGenerator" {
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$INameGenerator, $INameGenerator$Type} from "packages/net/blay09/mods/waystones/worldgen/namegen/$INameGenerator"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"

export class $CustomNameGenerator implements $INameGenerator {

constructor(arg0: boolean, arg1: $Set$Type<(string)>)

public "randomName"(arg0: $RandomSource$Type): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CustomNameGenerator$Type = ($CustomNameGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CustomNameGenerator_ = $CustomNameGenerator$Type;
}}
declare module "packages/net/blay09/mods/balm/api/block/entity/$BalmBlockEntityBase" {
import {$LazyOptional, $LazyOptional$Type} from "packages/net/minecraftforge/common/util/$LazyOptional"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Capability, $Capability$Type} from "packages/net/minecraftforge/common/capabilities/$Capability"

export class $BalmBlockEntityBase extends $BlockEntity {
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "getProvider"<T>(arg0: $Class$Type<(T)>): T
public "getCapability"<T>(arg0: $Capability$Type<(T)>, arg1: $Direction$Type): $LazyOptional<(T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmBlockEntityBase$Type = ($BalmBlockEntityBase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmBlockEntityBase_ = $BalmBlockEntityBase$Type;
}}
declare module "packages/net/blay09/mods/balm/api/entity/$BalmEntity" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"

export interface $BalmEntity {

 "setFabricBalmData"(arg0: $CompoundTag$Type): void
 "getFabricBalmData"(): $CompoundTag
}

export namespace $BalmEntity {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmEntity$Type = ($BalmEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmEntity_ = $BalmEntity$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/$ModTextures" {
import {$BalmTextures, $BalmTextures$Type} from "packages/net/blay09/mods/balm/api/client/rendering/$BalmTextures"

export class $ModTextures {

constructor()

public static "initialize"(arg0: $BalmTextures$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModTextures$Type = ($ModTextures);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModTextures_ = $ModTextures$Type;
}}
declare module "packages/net/blay09/mods/waystones/core/$WaystoneManager" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$SavedData, $SavedData$Type} from "packages/net/minecraft/world/level/saveddata/$SavedData"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Stream, $Stream$Type} from "packages/java/util/stream/$Stream"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $WaystoneManager extends $SavedData {

constructor()

public static "get"(arg0: $MinecraftServer$Type): $WaystoneManager
public static "read"(arg0: $CompoundTag$Type): $WaystoneManager
public "getWaystonesByType"(arg0: $ResourceLocation$Type): $Stream<($IWaystone)>
public "getGlobalWaystones"(): $List<($IWaystone)>
public "getWaystoneById"(arg0: $UUID$Type): $Optional<($IWaystone)>
public "findWaystoneByName"(arg0: string): $Optional<($IWaystone)>
public "save"(arg0: $CompoundTag$Type): $CompoundTag
public "getWaystoneAt"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type): $Optional<($IWaystone)>
public "updateWaystone"(arg0: $IWaystone$Type): void
public "removeWaystone"(arg0: $IWaystone$Type): void
public "addWaystone"(arg0: $IWaystone$Type): void
get "globalWaystones"(): $List<($IWaystone)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneManager$Type = ($WaystoneManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneManager_ = $WaystoneManager$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/network/$ClearMessage" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ClearMessage {

constructor(arg0: $ResourceLocation$Type, arg1: boolean)

public static "decode"(arg0: $FriendlyByteBuf$Type): $ClearMessage
public static "encode"(arg0: $ClearMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $ServerPlayer$Type, arg1: $ClearMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClearMessage$Type = ($ClearMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClearMessage_ = $ClearMessage$Type;
}}
declare module "packages/net/blay09/mods/waystones/compat/jei/$WarpPlateJeiRecipeCategory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$IRecipeLayoutBuilder, $IRecipeLayoutBuilder$Type} from "packages/mezz/jei/api/gui/builder/$IRecipeLayoutBuilder"
import {$RecipeType, $RecipeType$Type} from "packages/mezz/jei/api/recipe/$RecipeType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$IRecipeSlotsView, $IRecipeSlotsView$Type} from "packages/mezz/jei/api/gui/ingredient/$IRecipeSlotsView"
import {$IRecipeCategory, $IRecipeCategory$Type} from "packages/mezz/jei/api/recipe/category/$IRecipeCategory"
import {$List, $List$Type} from "packages/java/util/$List"
import {$AttunedShardJeiRecipe, $AttunedShardJeiRecipe$Type} from "packages/net/blay09/mods/waystones/compat/jei/$AttunedShardJeiRecipe"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$IFocusGroup, $IFocusGroup$Type} from "packages/mezz/jei/api/recipe/$IFocusGroup"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"
import {$IGuiHelper, $IGuiHelper$Type} from "packages/mezz/jei/api/helpers/$IGuiHelper"

export class $WarpPlateJeiRecipeCategory implements $IRecipeCategory<($AttunedShardJeiRecipe)> {
static readonly "TYPE": $RecipeType<($AttunedShardJeiRecipe)>
static readonly "UID": $ResourceLocation

constructor(arg0: $IGuiHelper$Type)

public "getRecipeType"(): $RecipeType<($AttunedShardJeiRecipe)>
public "getIcon"(): $IDrawable
public "getTitle"(): $Component
public "setRecipe"(arg0: $IRecipeLayoutBuilder$Type, arg1: $AttunedShardJeiRecipe$Type, arg2: $IFocusGroup$Type): void
public "getBackground"(): $IDrawable
public "draw"(arg0: $AttunedShardJeiRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: $GuiGraphics$Type, arg3: double, arg4: double): void
public "getWidth"(): integer
public "getHeight"(): integer
public "isHandled"(arg0: $AttunedShardJeiRecipe$Type): boolean
public "handleInput"(arg0: $AttunedShardJeiRecipe$Type, arg1: double, arg2: double, arg3: $InputConstants$Key$Type): boolean
public "getTooltipStrings"(arg0: $AttunedShardJeiRecipe$Type, arg1: $IRecipeSlotsView$Type, arg2: double, arg3: double): $List<($Component)>
public "getRegistryName"(arg0: $AttunedShardJeiRecipe$Type): $ResourceLocation
get "recipeType"(): $RecipeType<($AttunedShardJeiRecipe)>
get "icon"(): $IDrawable
get "title"(): $Component
get "background"(): $IDrawable
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpPlateJeiRecipeCategory$Type = ($WarpPlateJeiRecipeCategory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpPlateJeiRecipeCategory_ = $WarpPlateJeiRecipeCategory$Type;
}}
declare module "packages/net/blay09/mods/balm/api/client/$BalmClientRuntimeFactory" {
import {$BalmClientRuntime, $BalmClientRuntime$Type} from "packages/net/blay09/mods/balm/api/client/$BalmClientRuntime"

export interface $BalmClientRuntimeFactory {

 "create"(): $BalmClientRuntime

(): $BalmClientRuntime
}

export namespace $BalmClientRuntimeFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmClientRuntimeFactory$Type = ($BalmClientRuntimeFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmClientRuntimeFactory_ = $BalmClientRuntimeFactory$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$CropGrowEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $CropGrowEvent extends $BalmEvent {

constructor()
constructor(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "getState"(): $BlockState
public "getLevel"(): $Level
public "getPos"(): $BlockPos
public "getListenerList"(): $ListenerList
get "state"(): $BlockState
get "level"(): $Level
get "pos"(): $BlockPos
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CropGrowEvent$Type = ($CropGrowEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CropGrowEvent_ = $CropGrowEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/client/rendering/$ForgeBalmTextures" {
import {$BalmTextures, $BalmTextures$Type} from "packages/net/blay09/mods/balm/api/client/rendering/$BalmTextures"

export class $ForgeBalmTextures implements $BalmTextures {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmTextures$Type = ($ForgeBalmTextures);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmTextures_ = $ForgeBalmTextures$Type;
}}
declare module "packages/net/blay09/mods/balm/api/proxy/$SidedProxy" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $SidedProxy<T> {

constructor(arg0: string, arg1: string)

public "get"(): T
public "resolveClient"(): void
public "resolveCommon"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SidedProxy$Type<T> = ($SidedProxy<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SidedProxy_<T> = $SidedProxy$Type<(T)>;
}}
declare module "packages/net/blay09/mods/waystones/config/$WaystonesConfigData$XpCost" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $WaystonesConfigData$XpCost {
 "inverseXpCost": boolean
 "blocksPerXpLevel": integer
 "minimumBaseXpCost": double
 "maximumBaseXpCost": double
 "xpCostPerLeashed": integer
 "dimensionalWarpXpCost": integer
 "globalWaystoneXpCostMultiplier": double
 "warpStoneXpCostMultiplier": double
 "waystoneXpCostMultiplier": double
 "sharestoneXpCostMultiplier": double
 "portstoneXpCostMultiplier": double
 "warpPlateXpCostMultiplier": double
 "inventoryButtonXpCostMultiplier": double

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystonesConfigData$XpCost$Type = ($WaystonesConfigData$XpCost);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystonesConfigData$XpCost_ = $WaystonesConfigData$XpCost$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridDecorator" {
import {$ButtonAlignment, $ButtonAlignment$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonAlignment"
import {$GridBalanceHandler, $GridBalanceHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridBalanceHandler"
import {$TweakType, $TweakType$Type} from "packages/net/blay09/mods/craftingtweaks/api/$TweakType"
import {$GridTransferHandler, $GridTransferHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridTransferHandler"
import {$GridRotateHandler, $GridRotateHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridRotateHandler"
import {$ButtonStyle, $ButtonStyle$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonStyle"
import {$GridClearHandler, $GridClearHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridClearHandler"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export interface $CraftingGridDecorator {

 "clearHandler"(arg0: $GridClearHandler$Type<($AbstractContainerMenu$Type)>): $CraftingGridDecorator
 "setButtonStyle"(arg0: $ButtonStyle$Type): $CraftingGridDecorator
 "setButtonAlignment"(arg0: $ButtonAlignment$Type): $CraftingGridDecorator
 "setButtonAlignmentOffset"(arg0: integer, arg1: integer): $CraftingGridDecorator
 "hideAllTweakButtons"(): $CraftingGridDecorator
 "disableTweak"(arg0: $TweakType$Type): $CraftingGridDecorator
 "setButtonPosition"(arg0: $TweakType$Type, arg1: integer, arg2: integer): $CraftingGridDecorator
 "disableAllTweaks"(): $CraftingGridDecorator
 "usePhantomItems"(): $CraftingGridDecorator
 "hideTweakButton"(arg0: $TweakType$Type): $CraftingGridDecorator
 "transferHandler"(arg0: $GridTransferHandler$Type<($AbstractContainerMenu$Type)>): $CraftingGridDecorator
 "balanceHandler"(arg0: $GridBalanceHandler$Type<($AbstractContainerMenu$Type)>): $CraftingGridDecorator
 "rotateHandler"(arg0: $GridRotateHandler$Type<($AbstractContainerMenu$Type)>): $CraftingGridDecorator
}

export namespace $CraftingGridDecorator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingGridDecorator$Type = ($CraftingGridDecorator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingGridDecorator_ = $CraftingGridDecorator$Type;
}}
declare module "packages/net/blay09/mods/balm/api/$BalmProxy" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $BalmProxy {

constructor()

public "getClientPlayer"(): $Player
public "isConnectedToServer"(): boolean
get "clientPlayer"(): $Player
get "connectedToServer"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmProxy$Type = ($BalmProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmProxy_ = $BalmProxy$Type;
}}
declare module "packages/net/blay09/mods/waystones/item/$AbstractAttunedShardItem" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$IAttunementItem, $IAttunementItem$Type} from "packages/net/blay09/mods/waystones/api/$IAttunementItem"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AbstractAttunedShardItem extends $Item implements $IAttunementItem {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Item$Properties$Type)

public "getWaystoneAttunedTo"(arg0: $MinecraftServer$Type, arg1: $ItemStack$Type): $IWaystone
public "setWaystoneAttunedTo"(arg0: $ItemStack$Type, arg1: $IWaystone$Type): void
public "isFoil"(arg0: $ItemStack$Type): boolean
public "appendHoverText"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractAttunedShardItem$Type = ($AbstractAttunedShardItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractAttunedShardItem_ = $AbstractAttunedShardItem$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/impl/$DefaultRectangleGridRotateHandler" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$GridRotateHandler, $GridRotateHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridRotateHandler"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export class $DefaultRectangleGridRotateHandler implements $GridRotateHandler<($AbstractContainerMenu)> {

constructor()

public "rotateGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: $AbstractContainerMenu$Type, arg3: boolean): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultRectangleGridRotateHandler$Type = ($DefaultRectangleGridRotateHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultRectangleGridRotateHandler_ = $DefaultRectangleGridRotateHandler$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/$ForgeBalmHooks" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Pose, $Pose$Type} from "packages/net/minecraft/world/entity/$Pose"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$ConfiguredFeature, $ConfiguredFeature$Type} from "packages/net/minecraft/world/level/levelgen/feature/$ConfiguredFeature"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$BalmHooks, $BalmHooks$Type} from "packages/net/blay09/mods/balm/api/$BalmHooks"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ForgeBalmHooks implements $BalmHooks {
readonly "burnTimes": $Map<($Item), (integer)>

constructor()

public "growCrop"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type): boolean
public "isFakePlayer"(arg0: $Player$Type): boolean
public "canItemsStack"(arg0: $ItemStack$Type, arg1: $ItemStack$Type): boolean
public "isShield"(arg0: $ItemStack$Type): boolean
public "useFluidTank"(arg0: $BlockState$Type, arg1: $Level$Type, arg2: $BlockPos$Type, arg3: $Player$Type, arg4: $InteractionHand$Type, arg5: $BlockHitResult$Type): boolean
public "setForcedPose"(arg0: $Player$Type, arg1: $Pose$Type): void
public "getPersistentData"(arg0: $Entity$Type): $CompoundTag
public "getCraftingRemainingItem"(arg0: $ItemStack$Type): $ItemStack
public "curePotionEffects"(arg0: $LivingEntity$Type, arg1: $ItemStack$Type): void
public "getServer"(): $MinecraftServer
public "isRepairable"(arg0: $ItemStack$Type): boolean
public "setBurnTime"(arg0: $Item$Type, arg1: integer): void
public "getBurnTime"(arg0: $ItemStack$Type): integer
public "getColor"(arg0: $ItemStack$Type): $DyeColor
public "blockGrowFeature"(arg0: $Level$Type, arg1: $RandomSource$Type, arg2: $BlockPos$Type, arg3: $Holder$Type<($ConfiguredFeature$Type<(any), (any)>)>): boolean
public "firePlayerCraftingEvent"(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $Container$Type): void
public "getBlockReachDistance"(arg0: $Player$Type): double
public "getPersistentData"(arg0: $Player$Type): $CompoundTag
get "server"(): $MinecraftServer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmHooks$Type = ($ForgeBalmHooks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmHooks_ = $ForgeBalmHooks$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$TeleportDestination" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $TeleportDestination {

constructor(arg0: $ServerLevel$Type, arg1: $Vec3$Type, arg2: $Direction$Type)

public "getLocation"(): $Vec3
public "getLevel"(): $ServerLevel
public "getDirection"(): $Direction
get "location"(): $Vec3
get "level"(): $ServerLevel
get "direction"(): $Direction
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TeleportDestination$Type = ($TeleportDestination);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TeleportDestination_ = $TeleportDestination$Type;
}}
declare module "packages/net/blay09/mods/waystones/item/$CrumblingAttunedShardItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$AbstractAttunedShardItem, $AbstractAttunedShardItem$Type} from "packages/net/blay09/mods/waystones/item/$AbstractAttunedShardItem"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CrumblingAttunedShardItem extends $AbstractAttunedShardItem {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Item$Properties$Type)

public "appendHoverText"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CrumblingAttunedShardItem$Type = ($CrumblingAttunedShardItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CrumblingAttunedShardItem_ = $CrumblingAttunedShardItem$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/impl/$CraftingGridBuilderImpl" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$CraftingGridBuilder, $CraftingGridBuilder$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridBuilder"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$CraftingGridDecorator, $CraftingGridDecorator$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridDecorator"

export class $CraftingGridBuilderImpl implements $CraftingGridBuilder {

constructor()

public "addGrid"(arg0: string, arg1: integer, arg2: integer): $CraftingGridDecorator
public "addCustomGrid"(arg0: $CraftingGrid$Type): void
public "setActiveModId"(arg0: string): void
public "getGrids"(): $List<($CraftingGrid)>
public "addGrid"(arg0: integer, arg1: integer): $CraftingGridDecorator
set "activeModId"(value: string)
get "grids"(): $List<($CraftingGrid)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingGridBuilderImpl$Type = ($CraftingGridBuilderImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingGridBuilderImpl_ = $CraftingGridBuilderImpl$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$ButtonProperties" {
import {$ButtonStateProperties, $ButtonStateProperties$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonStateProperties"
import {$ButtonState, $ButtonState$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonState"

export class $ButtonProperties {

constructor(arg0: integer, arg1: integer)

public "getState"(arg0: $ButtonState$Type): $ButtonStateProperties
public "withState"(arg0: $ButtonState$Type, arg1: integer, arg2: integer): $ButtonProperties
public "withState"(arg0: $ButtonState$Type, arg1: $ButtonStateProperties$Type): $ButtonProperties
public "getWidth"(): integer
public "getHeight"(): integer
get "width"(): integer
get "height"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ButtonProperties$Type = ($ButtonProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ButtonProperties_ = $ButtonProperties$Type;
}}
declare module "packages/net/blay09/mods/waystones/network/message/$SelectWaystoneMessage" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $SelectWaystoneMessage {

constructor(arg0: $UUID$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $SelectWaystoneMessage
public static "encode"(arg0: $SelectWaystoneMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $ServerPlayer$Type, arg1: $SelectWaystoneMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SelectWaystoneMessage$Type = ($SelectWaystoneMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SelectWaystoneMessage_ = $SelectWaystoneMessage$Type;
}}
declare module "packages/net/blay09/mods/balm/api/client/keymappings/$BalmKeyMappings" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$KeyModifiers, $KeyModifiers$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$KeyModifiers"
import {$KeyConflictContext, $KeyConflictContext$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$KeyConflictContext"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$KeyModifier, $KeyModifier$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$KeyModifier"
import {$InputConstants$Type, $InputConstants$Type$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Type"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export interface $BalmKeyMappings {

 "isKeyDownIgnoreContext"(arg0: $KeyMapping$Type): boolean
 "isActiveAndWasPressed"(arg0: $KeyMapping$Type): boolean
 "shouldIgnoreConflicts"(arg0: $KeyMapping$Type): boolean
 "ignoreConflicts"(arg0: $KeyMapping$Type): void
 "registerKeyMapping"(arg0: string, arg1: integer, arg2: string): $KeyMapping
 "registerKeyMapping"(arg0: string, arg1: $KeyConflictContext$Type, arg2: $KeyModifiers$Type, arg3: $InputConstants$Type$Type, arg4: integer, arg5: string): $KeyMapping
 "registerKeyMapping"(arg0: string, arg1: $InputConstants$Type$Type, arg2: integer, arg3: string): $KeyMapping
 "registerKeyMapping"(arg0: string, arg1: $KeyConflictContext$Type, arg2: $KeyModifiers$Type, arg3: integer, arg4: string): $KeyMapping
 "registerKeyMapping"(arg0: string, arg1: $KeyConflictContext$Type, arg2: $KeyModifier$Type, arg3: integer, arg4: string): $KeyMapping
 "registerKeyMapping"(arg0: string, arg1: $KeyConflictContext$Type, arg2: $KeyModifier$Type, arg3: $InputConstants$Type$Type, arg4: integer, arg5: string): $KeyMapping
 "isActiveAndKeyDown"(arg0: $KeyMapping$Type): boolean
 "isActiveAndMatches"(arg0: $KeyMapping$Type, arg1: $InputConstants$Type$Type, arg2: integer, arg3: integer): boolean
 "isActiveAndMatches"(arg0: $KeyMapping$Type, arg1: integer, arg2: integer): boolean
 "isActiveAndMatches"(arg0: $KeyMapping$Type, arg1: $InputConstants$Key$Type): boolean
 "conflictsWith"(arg0: $KeyMapping$Type, arg1: $KeyMapping$Type): $Optional<(boolean)>
}

export namespace $BalmKeyMappings {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmKeyMappings$Type = ($BalmKeyMappings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmKeyMappings_ = $BalmKeyMappings$Type;
}}
declare module "packages/net/blay09/mods/balm/api/client/$BalmClientRuntime" {
import {$BalmTextures, $BalmTextures$Type} from "packages/net/blay09/mods/balm/api/client/rendering/$BalmTextures"
import {$BalmKeyMappings, $BalmKeyMappings$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$BalmKeyMappings"
import {$BalmModels, $BalmModels$Type} from "packages/net/blay09/mods/balm/api/client/rendering/$BalmModels"
import {$BalmScreens, $BalmScreens$Type} from "packages/net/blay09/mods/balm/api/client/screen/$BalmScreens"
import {$BalmRenderers, $BalmRenderers$Type} from "packages/net/blay09/mods/balm/api/client/rendering/$BalmRenderers"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"

export interface $BalmClientRuntime {

 "initialize"(arg0: string, arg1: $Runnable$Type): void
 "getModels"(): $BalmModels
 "getScreens"(): $BalmScreens
 "getTextures"(): $BalmTextures
 "getRenderers"(): $BalmRenderers
 "getKeyMappings"(): $BalmKeyMappings
}

export namespace $BalmClientRuntime {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmClientRuntime$Type = ($BalmClientRuntime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmClientRuntime_ = $BalmClientRuntime$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$ItemCraftedEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $ItemCraftedEvent extends $BalmEvent {

constructor()
constructor(arg0: $Player$Type, arg1: $ItemStack$Type, arg2: $Container$Type)

public "getPlayer"(): $Player
public "getItemStack"(): $ItemStack
public "getCraftMatrix"(): $Container
public "getListenerList"(): $ListenerList
get "player"(): $Player
get "itemStack"(): $ItemStack
get "craftMatrix"(): $Container
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemCraftedEvent$Type = ($ItemCraftedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemCraftedEvent_ = $ItemCraftedEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/sound/$BalmSounds" {
import {$SoundEvent, $SoundEvent$Type} from "packages/net/minecraft/sounds/$SoundEvent"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $BalmSounds {

 "register"(arg0: $ResourceLocation$Type): $DeferredObject<($SoundEvent)>

(arg0: $ResourceLocation$Type): $DeferredObject<($SoundEvent)>
}

export namespace $BalmSounds {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmSounds$Type = ($BalmSounds);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmSounds_ = $BalmSounds$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/network/$CraftStackMessage" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $CraftStackMessage {

constructor(arg0: integer)

public static "decode"(arg0: $FriendlyByteBuf$Type): $CraftStackMessage
public static "encode"(arg0: $CraftStackMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $ServerPlayer$Type, arg1: $CraftStackMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftStackMessage$Type = ($CraftStackMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftStackMessage_ = $CraftStackMessage$Type;
}}
declare module "packages/net/blay09/mods/balm/api/container/$DelegateContainer" {
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$ExtractionAwareContainer, $ExtractionAwareContainer$Type} from "packages/net/blay09/mods/balm/api/container/$ExtractionAwareContainer"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export class $DelegateContainer implements $Container, $ExtractionAwareContainer {

constructor(arg0: $Container$Type)

public "canExtractItem"(arg0: integer): boolean
public "setChanged"(): void
public "getItem"(arg0: integer): $ItemStack
public "getContainerSize"(): integer
public "removeItemNoUpdate"(arg0: integer): $ItemStack
public "removeItem"(arg0: integer, arg1: integer): $ItemStack
public "clearContent"(): void
public "isEmpty"(): boolean
public "startOpen"(arg0: $Player$Type): void
public "getMaxStackSize"(): integer
public "stillValid"(arg0: $Player$Type): boolean
public "stopOpen"(arg0: $Player$Type): void
public "canPlaceItem"(arg0: integer, arg1: $ItemStack$Type): boolean
public "countItem"(arg0: $Item$Type): integer
public "hasAnyOf"(arg0: $Set$Type<($Item$Type)>): boolean
public "setItem"(arg0: integer, arg1: $ItemStack$Type): void
public "kjs$self"(): $Container
public "getBlock"(level: $Level$Type): $BlockContainerJS
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public "canTakeItem"(arg0: $Container$Type, arg1: integer, arg2: $ItemStack$Type): boolean
public "hasAnyMatching"(arg0: $Predicate$Type<($ItemStack$Type)>): boolean
public "getSlots"(): integer
public "getStackInSlot"(slot: integer): $ItemStack
public "insertItem"(slot: integer, stack: $ItemStack$Type, simulate: boolean): $ItemStack
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public "isMutable"(): boolean
public "setChanged"(): void
public "asContainer"(): $Container
public "getHeight"(): integer
public "extractItem"(slot: integer, amount: integer, simulate: boolean): $ItemStack
public "isItemValid"(slot: integer, stack: $ItemStack$Type): boolean
public "getWidth"(): integer
public "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
public "getSlotLimit"(slot: integer): integer
public "clear"(): void
public static "tryClear"(arg0: any): void
public "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
public "countNonEmpty"(ingredient: $Ingredient$Type): integer
public "countNonEmpty"(): integer
public "getAllItems"(): $List<($ItemStack)>
public "find"(ingredient: $Ingredient$Type): integer
public "find"(): integer
public "clear"(ingredient: $Ingredient$Type): void
public "count"(ingredient: $Ingredient$Type): integer
public "count"(): integer
public "isEmpty"(): boolean
get "containerSize"(): integer
get "empty"(): boolean
get "maxStackSize"(): integer
get "slots"(): integer
get "mutable"(): boolean
get "height"(): integer
get "width"(): integer
get "allItems"(): $List<($ItemStack)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DelegateContainer$Type = ($DelegateContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DelegateContainer_ = $DelegateContainer$Type;
}}
declare module "packages/net/blay09/mods/waystones/worldgen/namegen/$NameGenerationMode" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $NameGenerationMode extends $Enum<($NameGenerationMode)> {
static readonly "PRESET_FIRST": $NameGenerationMode
static readonly "RANDOM_ONLY": $NameGenerationMode
static readonly "PRESET_ONLY": $NameGenerationMode
static readonly "MIXED": $NameGenerationMode


public static "values"(): ($NameGenerationMode)[]
public static "valueOf"(arg0: string): $NameGenerationMode
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $NameGenerationMode$Type = (("random_only") | ("preset_only") | ("mixed") | ("preset_first")) | ($NameGenerationMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $NameGenerationMode_ = $NameGenerationMode$Type;
}}
declare module "packages/net/blay09/mods/balm/mixin/$SlotAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $SlotAccessor {

 "setX"(arg0: integer): void
 "setY"(arg0: integer): void
}

export namespace $SlotAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SlotAccessor$Type = ($SlotAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SlotAccessor_ = $SlotAccessor$Type;
}}
declare module "packages/net/blay09/mods/waystones/block/$ModBlocks" {
import {$BalmBlocks, $BalmBlocks$Type} from "packages/net/blay09/mods/balm/api/block/$BalmBlocks"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $ModBlocks {
static "waystone": $Block
static "mossyWaystone": $Block
static "sandyWaystone": $Block
static "sharestone": $Block
static "warpPlate": $Block
static "portstone": $Block
static "scopedSharestones": ($Block)[]

constructor()

public static "initialize"(arg0: $BalmBlocks$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModBlocks$Type = ($ModBlocks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModBlocks_ = $ModBlocks$Type;
}}
declare module "packages/net/blay09/mods/waystones/menu/$WaystoneSelectionMenu" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$WarpMode, $WarpMode$Type} from "packages/net/blay09/mods/waystones/core/$WarpMode"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"

export class $WaystoneSelectionMenu extends $AbstractContainerMenu {
static readonly "SLOT_CLICKED_OUTSIDE": integer
static readonly "QUICKCRAFT_TYPE_CHARITABLE": integer
static readonly "QUICKCRAFT_TYPE_GREEDY": integer
static readonly "QUICKCRAFT_TYPE_CLONE": integer
static readonly "QUICKCRAFT_HEADER_START": integer
static readonly "QUICKCRAFT_HEADER_CONTINUE": integer
static readonly "QUICKCRAFT_HEADER_END": integer
static readonly "CARRIED_SLOT_SIZE": integer
readonly "lastSlots": $NonNullList<($ItemStack)>
readonly "slots": $NonNullList<($Slot)>
 "remoteSlots": $NonNullList<($ItemStack)>
 "containerId": integer

constructor(arg0: $MenuType$Type<($WaystoneSelectionMenu$Type)>, arg1: $WarpMode$Type, arg2: $IWaystone$Type, arg3: integer, arg4: $List$Type<($IWaystone$Type)>)

public "getWaystones"(): $List<($IWaystone)>
public "getWarpMode"(): $WarpMode
public static "createWaystoneSelection"(arg0: integer, arg1: $Player$Type, arg2: $WarpMode$Type, arg3: $IWaystone$Type): $WaystoneSelectionMenu
public static "createSharestoneSelection"(arg0: $MinecraftServer$Type, arg1: integer, arg2: $IWaystone$Type, arg3: $BlockState$Type): $WaystoneSelectionMenu
public "stillValid"(arg0: $Player$Type): boolean
public "getWaystoneFrom"(): $IWaystone
public "quickMoveStack"(arg0: $Player$Type, arg1: integer): $ItemStack
get "waystones"(): $List<($IWaystone)>
get "warpMode"(): $WarpMode
get "waystoneFrom"(): $IWaystone
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneSelectionMenu$Type = ($WaystoneSelectionMenu);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneSelectionMenu_ = $WaystoneSelectionMenu$Type;
}}
declare module "packages/net/blay09/mods/waystones/item/$WarpStoneItem" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IResetUseOnDamage, $IResetUseOnDamage$Type} from "packages/net/blay09/mods/waystones/api/$IResetUseOnDamage"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$InteractionResultHolder, $InteractionResultHolder$Type} from "packages/net/minecraft/world/$InteractionResultHolder"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$List, $List$Type} from "packages/java/util/$List"
import {$UseAnim, $UseAnim$Type} from "packages/net/minecraft/world/item/$UseAnim"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $WarpStoneItem extends $Item implements $IResetUseOnDamage {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Item$Properties$Type)

public "getUseDuration"(arg0: $ItemStack$Type): integer
public "onUseTick"(arg0: $Level$Type, arg1: $LivingEntity$Type, arg2: $ItemStack$Type, arg3: integer): void
public "finishUsingItem"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $LivingEntity$Type): $ItemStack
public "use"(arg0: $Level$Type, arg1: $Player$Type, arg2: $InteractionHand$Type): $InteractionResultHolder<($ItemStack)>
public "isBarVisible"(arg0: $ItemStack$Type): boolean
public "getBarWidth"(arg0: $ItemStack$Type): integer
public "isFoil"(arg0: $ItemStack$Type): boolean
public "appendHoverText"(arg0: $ItemStack$Type, arg1: $Level$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type): void
public "getUseAnimation"(arg0: $ItemStack$Type): $UseAnim
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpStoneItem$Type = ($WarpStoneItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpStoneItem_ = $WarpStoneItem$Type;
}}
declare module "packages/net/blay09/mods/balm/api/network/$ConfigReflection" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$Field, $Field$Type} from "packages/java/lang/reflect/$Field"

export class $ConfigReflection {

constructor()

public static "deepCopy"(arg0: any, arg1: any): any
public static "getSyncedFields"(arg0: $Class$Type<(any)>): $List<($Field)>
public static "getAllFields"(arg0: $Class$Type<(any)>): $List<($Field)>
public static "isSyncedFieldOrObject"(arg0: $Field$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ConfigReflection$Type = ($ConfigReflection);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ConfigReflection_ = $ConfigReflection$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/client/$ForgeBalmClientRuntime" {
import {$BalmTextures, $BalmTextures$Type} from "packages/net/blay09/mods/balm/api/client/rendering/$BalmTextures"
import {$BalmKeyMappings, $BalmKeyMappings$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$BalmKeyMappings"
import {$BalmModels, $BalmModels$Type} from "packages/net/blay09/mods/balm/api/client/rendering/$BalmModels"
import {$BalmScreens, $BalmScreens$Type} from "packages/net/blay09/mods/balm/api/client/screen/$BalmScreens"
import {$BalmRenderers, $BalmRenderers$Type} from "packages/net/blay09/mods/balm/api/client/rendering/$BalmRenderers"
import {$Runnable, $Runnable$Type} from "packages/java/lang/$Runnable"
import {$BalmClientRuntime, $BalmClientRuntime$Type} from "packages/net/blay09/mods/balm/api/client/$BalmClientRuntime"

export class $ForgeBalmClientRuntime implements $BalmClientRuntime {

constructor()

public "initialize"(arg0: string, arg1: $Runnable$Type): void
public "getModels"(): $BalmModels
public "getScreens"(): $BalmScreens
public "getTextures"(): $BalmTextures
public "getRenderers"(): $BalmRenderers
public "getKeyMappings"(): $BalmKeyMappings
get "models"(): $BalmModels
get "screens"(): $BalmScreens
get "textures"(): $BalmTextures
get "renderers"(): $BalmRenderers
get "keyMappings"(): $BalmKeyMappings
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmClientRuntime$Type = ($ForgeBalmClientRuntime);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmClientRuntime_ = $ForgeBalmClientRuntime$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/$IMCHandler" {
import {$InterModProcessEvent, $InterModProcessEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$InterModProcessEvent"

export class $IMCHandler {

constructor()

public static "processInterMod"(arg0: $InterModProcessEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMCHandler$Type = ($IMCHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMCHandler_ = $IMCHandler$Type;
}}
declare module "packages/net/blay09/mods/waystones/item/$AttunedShardItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$AbstractAttunedShardItem, $AbstractAttunedShardItem$Type} from "packages/net/blay09/mods/waystones/item/$AbstractAttunedShardItem"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $AttunedShardItem extends $AbstractAttunedShardItem {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Item$Properties$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttunedShardItem$Type = ($AttunedShardItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttunedShardItem_ = $AttunedShardItem$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/$ForgeDefaultOptions" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ForgeDefaultOptions {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeDefaultOptions$Type = ($ForgeDefaultOptions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeDefaultOptions_ = $ForgeDefaultOptions$Type;
}}
declare module "packages/net/blay09/mods/balm/api/network/$BalmNetworking" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $BalmNetworking {

 "sendToTracking"<T>(arg0: $ServerLevel$Type, arg1: $BlockPos$Type, arg2: T): void
 "sendToTracking"<T>(arg0: $Entity$Type, arg1: T): void
 "allowClientOnly"(arg0: string): void
 "allowServerOnly"(arg0: string): void
 "openGui"(arg0: $Player$Type, arg1: $MenuProvider$Type): void
 "sendToAll"<T>(arg0: $MinecraftServer$Type, arg1: T): void
 "sendTo"<T>(arg0: $Player$Type, arg1: T): void
 "reply"<T>(arg0: T): void
 "allowClientAndServerOnly"(arg0: string): void
 "registerServerboundPacket"<T>(arg0: $ResourceLocation$Type, arg1: $Class$Type<(T)>, arg2: $BiConsumer$Type<(T), ($FriendlyByteBuf$Type)>, arg3: $Function$Type<($FriendlyByteBuf$Type), (T)>, arg4: $BiConsumer$Type<($ServerPlayer$Type), (T)>): void
 "registerClientboundPacket"<T>(arg0: $ResourceLocation$Type, arg1: $Class$Type<(T)>, arg2: $BiConsumer$Type<(T), ($FriendlyByteBuf$Type)>, arg3: $Function$Type<($FriendlyByteBuf$Type), (T)>, arg4: $BiConsumer$Type<($Player$Type), (T)>): void
 "sendToServer"<T>(arg0: T): void
}

export namespace $BalmNetworking {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmNetworking$Type = ($BalmNetworking);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmNetworking_ = $BalmNetworking$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/impl/$InternalClientMethodsImpl" {
import {$TweakType, $TweakType$Type} from "packages/net/blay09/mods/craftingtweaks/api/$TweakType"
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$ButtonStyle, $ButtonStyle$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonStyle"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$GridGuiHandler, $GridGuiHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridGuiHandler"
import {$InternalClientMethods, $InternalClientMethods$Type} from "packages/net/blay09/mods/craftingtweaks/api/$InternalClientMethods"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $InternalClientMethodsImpl implements $InternalClientMethods {

constructor()

public "createTweakButton"(arg0: $CraftingGrid$Type, arg1: $AbstractContainerScreen$Type<(any)>, arg2: integer, arg3: integer, arg4: $ButtonStyle$Type, arg5: $TweakType$Type, arg6: $TweakType$Type): $Button
public "registerCraftingGridGuiHandler"<TScreen extends $AbstractContainerScreen<(TMenu)>, TMenu extends $AbstractContainerMenu>(arg0: $Class$Type<(TScreen)>, arg1: $GridGuiHandler$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalClientMethodsImpl$Type = ($InternalClientMethodsImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalClientMethodsImpl_ = $InternalClientMethodsImpl$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$ButtonStyle" {
import {$TweakType, $TweakType$Type} from "packages/net/blay09/mods/craftingtweaks/api/$TweakType"
import {$ButtonProperties, $ButtonProperties$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonProperties"

export class $ButtonStyle {

constructor(arg0: integer, arg1: integer, arg2: integer, arg3: integer)

public "getMarginY"(): integer
public "getMarginX"(): integer
public "getSpacingX"(): integer
public "getSpacingY"(): integer
public "getTweak"(arg0: $TweakType$Type): $ButtonProperties
public "withTweak"(arg0: $TweakType$Type, arg1: $ButtonProperties$Type): $ButtonStyle
get "marginY"(): integer
get "marginX"(): integer
get "spacingX"(): integer
get "spacingY"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ButtonStyle$Type = ($ButtonStyle);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ButtonStyle_ = $ButtonStyle$Type;
}}
declare module "packages/net/blay09/mods/balm/api/menu/$BalmMenuFactory" {
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export interface $BalmMenuFactory<T extends $AbstractContainerMenu> {

 "create"(arg0: integer, arg1: $Inventory$Type, arg2: $FriendlyByteBuf$Type): T

(arg0: integer, arg1: $Inventory$Type, arg2: $FriendlyByteBuf$Type): T
}

export namespace $BalmMenuFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmMenuFactory$Type<T> = ($BalmMenuFactory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmMenuFactory_<T> = $BalmMenuFactory$Type<(T)>;
}}
declare module "packages/net/blay09/mods/waystones/api/$KnownWaystonesEvent" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $KnownWaystonesEvent extends $BalmEvent {

constructor()
constructor(arg0: $List$Type<($IWaystone$Type)>)

public "getWaystones"(): $List<($IWaystone)>
public "getListenerList"(): $ListenerList
get "waystones"(): $List<($IWaystone)>
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KnownWaystonesEvent$Type = ($KnownWaystonesEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KnownWaystonesEvent_ = $KnownWaystonesEvent$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/mixin/$KeyMappingAccessor" {
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export interface $KeyMappingAccessor {

 "setDefaultKey"(arg0: $InputConstants$Key$Type): void

(arg0: $InputConstants$Key$Type): void
}

export namespace $KeyMappingAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyMappingAccessor$Type = ($KeyMappingAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyMappingAccessor_ = $KeyMappingAccessor$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$WaystoneTeleportEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $WaystoneTeleportEvent extends $BalmEvent {

constructor()

public "getListenerList"(): $ListenerList
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneTeleportEvent$Type = ($WaystoneTeleportEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneTeleportEvent_ = $WaystoneTeleportEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$ItemTooltipEvent" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$TooltipFlag, $TooltipFlag$Type} from "packages/net/minecraft/world/item/$TooltipFlag"
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ItemTooltipEvent extends $BalmEvent {

constructor()
constructor(arg0: $ItemStack$Type, arg1: $Player$Type, arg2: $List$Type<($Component$Type)>, arg3: $TooltipFlag$Type)

public "getFlags"(): $TooltipFlag
public "getPlayer"(): $Player
public "getItemStack"(): $ItemStack
public "getToolTip"(): $List<($Component)>
public "getListenerList"(): $ListenerList
get "flags"(): $TooltipFlag
get "player"(): $Player
get "itemStack"(): $ItemStack
get "toolTip"(): $List<($Component)>
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemTooltipEvent$Type = ($ItemTooltipEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemTooltipEvent_ = $ItemTooltipEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/screen/$ScreenInitEvent$Post" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$ScreenInitEvent, $ScreenInitEvent$Type} from "packages/net/blay09/mods/balm/api/event/client/screen/$ScreenInitEvent"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"

export class $ScreenInitEvent$Post extends $ScreenInitEvent {

constructor(arg0: $Screen$Type)
constructor()

public "getListenerList"(): $ListenerList
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenInitEvent$Post$Type = ($ScreenInitEvent$Post);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenInitEvent$Post_ = $ScreenInitEvent$Post$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/client/$CraftingTweaksClientProviderManager" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$GridGuiHandler, $GridGuiHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridGuiHandler"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"

export class $CraftingTweaksClientProviderManager {

constructor()

public static "getGridGuiHandler"(arg0: $AbstractContainerScreen$Type<(any)>): $GridGuiHandler
public static "registerCraftingGridGuiHandler"<TScreen extends $AbstractContainerScreen<(TMenu)>, TMenu extends $AbstractContainerMenu>(arg0: $Class$Type<(TScreen)>, arg1: $GridGuiHandler$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksClientProviderManager$Type = ($CraftingTweaksClientProviderManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksClientProviderManager_ = $CraftingTweaksClientProviderManager$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/config/$ForgeBalmConfig" {
import {$File, $File$Type} from "packages/java/io/$File"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$AbstractBalmConfig, $AbstractBalmConfig$Type} from "packages/net/blay09/mods/balm/api/config/$AbstractBalmConfig"
import {$BalmConfigData, $BalmConfigData$Type} from "packages/net/blay09/mods/balm/api/config/$BalmConfigData"

export class $ForgeBalmConfig extends $AbstractBalmConfig {

constructor()

public "getBackingConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): T
public "saveBackingConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): void
public "initializeBackingConfig"<T extends $BalmConfigData>(arg0: $Class$Type<(T)>): T
public "getConfigDir"(): $File
get "configDir"(): $File
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmConfig$Type = ($ForgeBalmConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmConfig_ = $ForgeBalmConfig$Type;
}}
declare module "packages/net/blay09/mods/waystones/block/entity/$WaystoneBlockEntityBase" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$WaystoneOrigin, $WaystoneOrigin$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneOrigin"
import {$OnLoadHandler, $OnLoadHandler$Type} from "packages/net/blay09/mods/balm/api/block/entity/$OnLoadHandler"
import {$CustomRenderBoundingBox, $CustomRenderBoundingBox$Type} from "packages/net/blay09/mods/balm/api/block/entity/$CustomRenderBoundingBox"
import {$ServerLevelAccessor, $ServerLevelAccessor$Type} from "packages/net/minecraft/world/level/$ServerLevelAccessor"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$Waystone, $Waystone$Type} from "packages/net/blay09/mods/waystones/core/$Waystone"
import {$BalmBlockEntity, $BalmBlockEntity$Type} from "packages/net/blay09/mods/balm/common/$BalmBlockEntity"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $WaystoneBlockEntityBase extends $BalmBlockEntity implements $OnLoadHandler, $CustomRenderBoundingBox {
 "blockState": $BlockState

constructor(arg0: $BlockEntityType$Type<(any)>, arg1: $BlockPos$Type, arg2: $BlockState$Type)

public "onLoad"(): void
public "initializeFromExisting"(arg0: $ServerLevelAccessor$Type, arg1: $Waystone$Type, arg2: $ItemStack$Type): void
public "getSettingsMenuProvider"(): $MenuProvider
public "uninitializeWaystone"(): void
public "getMenuProvider"(): $MenuProvider
public "load"(arg0: $CompoundTag$Type): void
public "getWaystone"(): $IWaystone
public "getRenderBoundingBox"(): $AABB
public "setSilkTouched"(arg0: boolean): void
public "isSilkTouched"(): boolean
public "initializeFromBase"(arg0: $WaystoneBlockEntityBase$Type): void
public "initializeWaystone"(arg0: $ServerLevelAccessor$Type, arg1: $LivingEntity$Type, arg2: $WaystoneOrigin$Type): void
public "writeUpdateTag"(arg0: $CompoundTag$Type): void
get "settingsMenuProvider"(): $MenuProvider
get "menuProvider"(): $MenuProvider
get "waystone"(): $IWaystone
get "renderBoundingBox"(): $AABB
set "silkTouched"(value: boolean)
get "silkTouched"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneBlockEntityBase$Type = ($WaystoneBlockEntityBase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneBlockEntityBase_ = $WaystoneBlockEntityBase$Type;
}}
declare module "packages/net/blay09/mods/waystones/core/$InMemoryPlayerWaystoneData" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$List, $List$Type} from "packages/java/util/$List"
import {$IPlayerWaystoneData, $IPlayerWaystoneData$Type} from "packages/net/blay09/mods/waystones/core/$IPlayerWaystoneData"

export class $InMemoryPlayerWaystoneData implements $IPlayerWaystoneData {

constructor()

public "getWarpStoneCooldownUntil"(arg0: $Player$Type): long
public "getInventoryButtonCooldownUntil"(arg0: $Player$Type): long
public "getWaystones"(arg0: $Player$Type): $List<($IWaystone)>
public "setWarpStoneCooldownUntil"(arg0: $Player$Type, arg1: long): void
public "activateWaystone"(arg0: $Player$Type, arg1: $IWaystone$Type): void
public "swapWaystoneSorting"(arg0: $Player$Type, arg1: integer, arg2: integer): void
public "isWaystoneActivated"(arg0: $Player$Type, arg1: $IWaystone$Type): boolean
public "deactivateWaystone"(arg0: $Player$Type, arg1: $IWaystone$Type): void
public "setWaystones"(arg0: $List$Type<($IWaystone$Type)>): void
public "setInventoryButtonCooldownUntil"(arg0: $Player$Type, arg1: long): void
set "waystones"(value: $List$Type<($IWaystone$Type)>)
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InMemoryPlayerWaystoneData$Type = ($InMemoryPlayerWaystoneData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InMemoryPlayerWaystoneData_ = $InMemoryPlayerWaystoneData$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/container/$BalmInvWrapper" {
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$InvWrapper, $InvWrapper$Type} from "packages/net/minecraftforge/items/wrapper/$InvWrapper"

export class $BalmInvWrapper extends $InvWrapper {

constructor(arg0: $Container$Type)

public "extractItem"(arg0: integer, arg1: integer, arg2: boolean): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmInvWrapper$Type = ($BalmInvWrapper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmInvWrapper_ = $BalmInvWrapper$Type;
}}
declare module "packages/net/blay09/mods/waystones/handler/$WarpDamageResetHandler" {
import {$LivingDamageEvent, $LivingDamageEvent$Type} from "packages/net/blay09/mods/balm/api/event/$LivingDamageEvent"

export class $WarpDamageResetHandler {

constructor()

public static "onDamage"(arg0: $LivingDamageEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpDamageResetHandler$Type = ($WarpDamageResetHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpDamageResetHandler_ = $WarpDamageResetHandler$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/network/$CompressMessage" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$CompressType, $CompressType$Type} from "packages/net/blay09/mods/craftingtweaks/$CompressType"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"

export class $CompressMessage {

constructor(arg0: integer, arg1: $CompressType$Type)

public static "decode"(arg0: $FriendlyByteBuf$Type): $CompressMessage
public static "encode"(arg0: $CompressMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $ServerPlayer$Type, arg1: $CompressMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CompressMessage$Type = ($CompressMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CompressMessage_ = $CompressMessage$Type;
}}
declare module "packages/net/blay09/mods/balm/api/block/entity/$OnLoadHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $OnLoadHandler {

 "onLoad"(): void

(): void
}

export namespace $OnLoadHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OnLoadHandler$Type = ($OnLoadHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OnLoadHandler_ = $OnLoadHandler$Type;
}}
declare module "packages/net/blay09/mods/waystones/$ForgeWaystones" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ForgeWaystones {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeWaystones$Type = ($ForgeWaystones);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeWaystones_ = $ForgeWaystones$Type;
}}
declare module "packages/net/blay09/mods/balm/api/config/$Config" {
import {$Annotation, $Annotation$Type} from "packages/java/lang/annotation/$Annotation"
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $Config extends $Annotation {

 "value"(): string
 "equals"(arg0: any): boolean
 "toString"(): string
 "hashCode"(): integer
 "annotationType"(): $Class<(any)>
}

export namespace $Config {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Config$Type = ($Config);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Config_ = $Config$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/world/$ForgeBalmWorldGen" {
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$GenerationStep$Decoration, $GenerationStep$Decoration$Type} from "packages/net/minecraft/world/level/levelgen/$GenerationStep$Decoration"
import {$BalmBiomeModifier, $BalmBiomeModifier$Type} from "packages/net/blay09/mods/balm/forge/world/$BalmBiomeModifier"
import {$BiomePredicate, $BiomePredicate$Type} from "packages/net/blay09/mods/balm/api/world/$BiomePredicate"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$ModifiableBiomeInfo$BiomeInfo$Builder, $ModifiableBiomeInfo$BiomeInfo$Builder$Type} from "packages/net/minecraftforge/common/world/$ModifiableBiomeInfo$BiomeInfo$Builder"
import {$Holder, $Holder$Type} from "packages/net/minecraft/core/$Holder"
import {$BiomeModifier$Phase, $BiomeModifier$Phase$Type} from "packages/net/minecraftforge/common/world/$BiomeModifier$Phase"
import {$Biome, $Biome$Type} from "packages/net/minecraft/world/level/biome/$Biome"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BalmWorldGen, $BalmWorldGen$Type} from "packages/net/blay09/mods/balm/api/world/$BalmWorldGen"
import {$Feature, $Feature$Type} from "packages/net/minecraft/world/level/levelgen/feature/$Feature"
import {$PlacementModifierType, $PlacementModifierType$Type} from "packages/net/minecraft/world/level/levelgen/placement/$PlacementModifierType"

export class $ForgeBalmWorldGen implements $BalmWorldGen {
static readonly "BALM_BIOME_MODIFIER_CODEC": $Codec<($BalmBiomeModifier)>

constructor()

public "register"(): void
public "registerFeature"<T extends $Feature<(any)>>(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<(T)>): $DeferredObject<(T)>
public "addFeatureToBiomes"(arg0: $BiomePredicate$Type, arg1: $GenerationStep$Decoration$Type, arg2: $ResourceLocation$Type): void
public "modifyBiome"(arg0: $Holder$Type<($Biome$Type)>, arg1: $BiomeModifier$Phase$Type, arg2: $ModifiableBiomeInfo$BiomeInfo$Builder$Type): void
public "registerPlacementModifier"<T extends $PlacementModifierType<(any)>>(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<(T)>): $DeferredObject<(T)>
public static "initializeBalmBiomeModifiers"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmWorldGen$Type = ($ForgeBalmWorldGen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmWorldGen_ = $ForgeBalmWorldGen$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$InternalMethods" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$WaystoneStyle, $WaystoneStyle$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneStyle"
import {$IWaystoneTeleportContext, $IWaystoneTeleportContext$Type} from "packages/net/blay09/mods/waystones/api/$IWaystoneTeleportContext"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$WaystoneTeleportError, $WaystoneTeleportError$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneTeleportError"
import {$WarpMode, $WarpMode$Type} from "packages/net/blay09/mods/waystones/core/$WarpMode"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $InternalMethods {

 "forceTeleportToWaystone"(arg0: $Entity$Type, arg1: $IWaystone$Type): $Either<($List<($Entity)>), ($WaystoneTeleportError)>
 "tryTeleportToWaystone"(arg0: $Entity$Type, arg1: $IWaystone$Type, arg2: $WarpMode$Type, arg3: $IWaystone$Type): $Either<($List<($Entity)>), ($WaystoneTeleportError)>
 "createDefaultTeleportContext"(arg0: $Entity$Type, arg1: $IWaystone$Type, arg2: $WarpMode$Type, arg3: $IWaystone$Type): $Either<($IWaystoneTeleportContext), ($WaystoneTeleportError)>
 "createCustomTeleportContext"(arg0: $Entity$Type, arg1: $IWaystone$Type): $Either<($IWaystoneTeleportContext), ($WaystoneTeleportError)>
 "placeSharestone"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $DyeColor$Type): $Optional<($IWaystone)>
 "placeWarpPlate"(arg0: $Level$Type, arg1: $BlockPos$Type): $Optional<($IWaystone)>
 "createAttunedShard"(arg0: $IWaystone$Type): $ItemStack
 "getBoundWaystone"(arg0: $ItemStack$Type): $Optional<($IWaystone)>
 "placeWaystone"(arg0: $Level$Type, arg1: $BlockPos$Type, arg2: $WaystoneStyle$Type): $Optional<($IWaystone)>
 "setBoundWaystone"(arg0: $ItemStack$Type, arg1: $IWaystone$Type): void
 "forceTeleport"(arg0: $IWaystoneTeleportContext$Type): $List<($Entity)>
 "getWaystoneAt"(arg0: $Level$Type, arg1: $BlockPos$Type): $Optional<($IWaystone)>
 "getWaystone"(arg0: $Level$Type, arg1: $UUID$Type): $Optional<($IWaystone)>
 "tryTeleport"(arg0: $IWaystoneTeleportContext$Type): $Either<($List<($Entity)>), ($WaystoneTeleportError)>
 "createBoundScroll"(arg0: $IWaystone$Type): $ItemStack
}

export namespace $InternalMethods {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InternalMethods$Type = ($InternalMethods);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InternalMethods_ = $InternalMethods$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/network/$ModNetworking" {
import {$BalmNetworking, $BalmNetworking$Type} from "packages/net/blay09/mods/balm/api/network/$BalmNetworking"

export class $ModNetworking {

constructor()

public static "initialize"(arg0: $BalmNetworking$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModNetworking$Type = ($ModNetworking);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModNetworking_ = $ModNetworking$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/network/$ForgeBalmNetworking" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$BalmNetworking, $BalmNetworking$Type} from "packages/net/blay09/mods/balm/api/network/$BalmNetworking"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $ForgeBalmNetworking implements $BalmNetworking {

constructor()

public "sendToTracking"<T>(arg0: $ServerLevel$Type, arg1: $BlockPos$Type, arg2: T): void
public "sendToTracking"<T>(arg0: $Entity$Type, arg1: T): void
public "allowClientOnly"(arg0: string): void
public "allowServerOnly"(arg0: string): void
public "openGui"(arg0: $Player$Type, arg1: $MenuProvider$Type): void
public "sendToAll"<T>(arg0: $MinecraftServer$Type, arg1: T): void
public "sendTo"<T>(arg0: $Player$Type, arg1: T): void
public "reply"<T>(arg0: T): void
public "registerServerboundPacket"<T>(arg0: $ResourceLocation$Type, arg1: $Class$Type<(T)>, arg2: $BiConsumer$Type<(T), ($FriendlyByteBuf$Type)>, arg3: $Function$Type<($FriendlyByteBuf$Type), (T)>, arg4: $BiConsumer$Type<($ServerPlayer$Type), (T)>): void
public "registerClientboundPacket"<T>(arg0: $ResourceLocation$Type, arg1: $Class$Type<(T)>, arg2: $BiConsumer$Type<(T), ($FriendlyByteBuf$Type)>, arg3: $Function$Type<($FriendlyByteBuf$Type), (T)>, arg4: $BiConsumer$Type<($Player$Type), (T)>): void
public "sendToServer"<T>(arg0: T): void
public "allowClientAndServerOnly"(arg0: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmNetworking$Type = ($ForgeBalmNetworking);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmNetworking_ = $ForgeBalmNetworking$Type;
}}
declare module "packages/net/blay09/mods/balm/api/client/keymappings/$KeyModifiers" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$KeyModifier, $KeyModifier$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$KeyModifier"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $KeyModifiers {


public "isEmpty"(): boolean
public "size"(): integer
public static "of"(...arg0: ($KeyModifier$Type)[]): $KeyModifiers
public "contains"(arg0: $KeyModifier$Type): boolean
public "asList"(): $List<($KeyModifier)>
public "getCustomModifiers"(): $List<($InputConstants$Key)>
public "hasCustomModifiers"(): boolean
public static "ofCustom"(...arg0: ($InputConstants$Key$Type)[]): $KeyModifiers
public "addCustomModifier"(arg0: $InputConstants$Key$Type): $KeyModifiers
public "addCustomModifier"(arg0: integer): $KeyModifiers
get "empty"(): boolean
get "customModifiers"(): $List<($InputConstants$Key)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyModifiers$Type = ($KeyModifiers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyModifiers_ = $KeyModifiers$Type;
}}
declare module "packages/net/blay09/mods/waystones/compat/$JourneyMapIntegration" {
import {$KnownWaystonesEvent, $KnownWaystonesEvent$Type} from "packages/net/blay09/mods/waystones/api/$KnownWaystonesEvent"
import {$IClientAPI, $IClientAPI$Type} from "packages/journeymap/client/api/$IClientAPI"
import {$IClientPlugin, $IClientPlugin$Type} from "packages/journeymap/client/api/$IClientPlugin"
import {$ClientEvent, $ClientEvent$Type} from "packages/journeymap/client/api/event/$ClientEvent"
import {$WaystoneUpdateReceivedEvent, $WaystoneUpdateReceivedEvent$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneUpdateReceivedEvent"

export class $JourneyMapIntegration implements $IClientPlugin {

constructor()

public static "getInstance"(): $JourneyMapIntegration
public "initialize"(arg0: $IClientAPI$Type): void
public "onEvent"(arg0: $ClientEvent$Type): void
public "onKnownWaystones"(arg0: $KnownWaystonesEvent$Type): void
public "onWaystoneUpdateReceived"(arg0: $WaystoneUpdateReceivedEvent$Type): void
public "getModId"(): string
get "instance"(): $JourneyMapIntegration
get "modId"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JourneyMapIntegration$Type = ($JourneyMapIntegration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JourneyMapIntegration_ = $JourneyMapIntegration$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/screen/$ScreenInitEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $ScreenInitEvent extends $BalmEvent {

constructor()
constructor(arg0: $Screen$Type)

public "getScreen"(): $Screen
public "getListenerList"(): $ListenerList
get "screen"(): $Screen
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenInitEvent$Type = ($ScreenInitEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenInitEvent_ = $ScreenInitEvent$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/registry/$CraftingTweaksRegistrationData$TweakData" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CraftingTweaksRegistrationData$TweakData {

constructor()

public "isEnabled"(): boolean
public "setEnabled"(arg0: boolean): void
public "setButtonY"(arg0: integer): void
public "setButtonX"(arg0: integer): void
public "setShowButton"(arg0: boolean): void
public "getButtonX"(): integer
public "getButtonY"(): integer
public "isShowButton"(): boolean
get "enabled"(): boolean
set "enabled"(value: boolean)
set "buttonY"(value: integer)
set "buttonX"(value: integer)
set "showButton"(value: boolean)
get "buttonX"(): integer
get "buttonY"(): integer
get "showButton"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksRegistrationData$TweakData$Type = ($CraftingTweaksRegistrationData$TweakData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksRegistrationData$TweakData_ = $CraftingTweaksRegistrationData$TweakData$Type;
}}
declare module "packages/net/blay09/mods/waystones/core/$WaystoneSyncManager" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export class $WaystoneSyncManager {

constructor()

public static "sendWarpPlates"(arg0: $ServerPlayer$Type): void
public static "sendActivatedWaystones"(arg0: $Player$Type): void
public static "sendWaystoneCooldowns"(arg0: $Player$Type): void
public static "sendWaystoneUpdate"(arg0: $Player$Type, arg1: $IWaystone$Type): void
public static "sendWaystoneUpdateToAll"(arg0: $MinecraftServer$Type, arg1: $IWaystone$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneSyncManager$Type = ($WaystoneSyncManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneSyncManager_ = $WaystoneSyncManager$Type;
}}
declare module "packages/net/blay09/mods/balm/api/config/$BalmConfigProperty" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"

export interface $BalmConfigProperty<T> {

 "getValue"(): T
 "setValue"(arg0: T): void
 "getType"(): $Class<(T)>
 "getDefaultValue"(): T
 "getInnerType"(): $Class<(T)>
}

export namespace $BalmConfigProperty {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmConfigProperty$Type<T> = ($BalmConfigProperty<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmConfigProperty_<T> = $BalmConfigProperty$Type<(T)>;
}}
declare module "packages/net/blay09/mods/kuma/api/$KeyModifiers" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$KeyModifier, $KeyModifier$Type} from "packages/net/blay09/mods/kuma/api/$KeyModifier"
import {$List, $List$Type} from "packages/java/util/$List"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $KeyModifiers {


public "toString"(): string
public "isEmpty"(): boolean
public "size"(): integer
public static "of"(arg0: $KeyMapping$Type): $KeyModifiers
public static "of"(...arg0: ($KeyModifier$Type)[]): $KeyModifiers
public "contains"(arg0: $KeyModifier$Type): boolean
public "asList"(): $List<($KeyModifier)>
public "getCustomModifiers"(): $List<($InputConstants$Key)>
public "hasCustomModifiers"(): boolean
public static "none"(): $KeyModifiers
public static "ofCustom"(...arg0: ($InputConstants$Key$Type)[]): $KeyModifiers
public "addCustomModifier"(arg0: $InputConstants$Key$Type): $KeyModifiers
public "addCustomModifier"(arg0: integer): $KeyModifiers
get "empty"(): boolean
get "customModifiers"(): $List<($InputConstants$Key)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KeyModifiers$Type = ($KeyModifiers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KeyModifiers_ = $KeyModifiers$Type;
}}
declare module "packages/net/blay09/mods/waystones/block/entity/$WarpPlateBlockEntity" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$BalmMenuProvider, $BalmMenuProvider$Type} from "packages/net/blay09/mods/balm/api/menu/$BalmMenuProvider"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"
import {$WaystoneOrigin, $WaystoneOrigin$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneOrigin"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$List, $List$Type} from "packages/java/util/$List"
import {$ServerLevelAccessor, $ServerLevelAccessor$Type} from "packages/net/minecraft/world/level/$ServerLevelAccessor"
import {$ImplementedContainer, $ImplementedContainer$Type} from "packages/net/blay09/mods/balm/api/container/$ImplementedContainer"
import {$Waystone, $Waystone$Type} from "packages/net/blay09/mods/waystones/core/$Waystone"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ContainerData, $ContainerData$Type} from "packages/net/minecraft/world/inventory/$ContainerData"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$Optional, $Optional$Type} from "packages/java/util/$Optional"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$WaystoneBlockEntityBase, $WaystoneBlockEntityBase$Type} from "packages/net/blay09/mods/waystones/block/entity/$WaystoneBlockEntityBase"

export class $WarpPlateBlockEntity extends $WaystoneBlockEntityBase implements $ImplementedContainer {
 "blockState": $BlockState

constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "getTargetWaystone"(): $Optional<($IWaystone)>
public "getContainerData"(): $ContainerData
public "initializeFromExisting"(arg0: $ServerLevelAccessor$Type, arg1: $Waystone$Type, arg2: $ItemStack$Type): void
public "getSettingsMenuProvider"(): $MenuProvider
public "isCompletedFirstAttunement"(): boolean
public "getMaxAttunementTicks"(): integer
public "getTargetAttunementStack"(): $ItemStack
public "markEntityForCooldown"(arg0: $Entity$Type): void
public "markReadyForAttunement"(): void
public "removeItemNoUpdate"(arg0: integer): $ItemStack
public "onEntityCollision"(arg0: $Entity$Type): void
public "getMenuProvider"(): $BalmMenuProvider
public "load"(arg0: $CompoundTag$Type): void
public "m_183515_"(arg0: $CompoundTag$Type): void
public "removeItem"(arg0: integer, arg1: integer): $ItemStack
public "canPlaceItem"(arg0: integer, arg1: $ItemStack$Type): boolean
public "getItems"(): $NonNullList<($ItemStack)>
public "serverTick"(): void
public "initializeWaystone"(arg0: $ServerLevelAccessor$Type, arg1: $LivingEntity$Type, arg2: $WaystoneOrigin$Type): void
public static "of"(arg0: $NonNullList$Type<($ItemStack$Type)>): $ImplementedContainer
public "serializeInventory"(): $CompoundTag
public "setChanged"(): void
public static "deserializeInventory"(arg0: $CompoundTag$Type, arg1: integer): $NonNullList<($ItemStack)>
public "getItem"(arg0: integer): $ItemStack
public "getContainerSize"(): integer
public static "ofSize"(arg0: integer): $ImplementedContainer
public "slotChanged"(arg0: integer): void
public "clearContent"(): void
public "isEmpty"(): boolean
public "stillValid"(arg0: $Player$Type): boolean
public "setItem"(arg0: integer, arg1: $ItemStack$Type): void
public "kjs$self"(): $Container
public "getBlock"(level: $Level$Type): $BlockContainerJS
public "startOpen"(arg0: $Player$Type): void
public "getMaxStackSize"(): integer
public "stopOpen"(arg0: $Player$Type): void
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
public "countItem"(arg0: $Item$Type): integer
public "canTakeItem"(arg0: $Container$Type, arg1: integer, arg2: $ItemStack$Type): boolean
public "hasAnyMatching"(arg0: $Predicate$Type<($ItemStack$Type)>): boolean
public "getSlots"(): integer
public "getStackInSlot"(slot: integer): $ItemStack
public "insertItem"(slot: integer, stack: $ItemStack$Type, simulate: boolean): $ItemStack
public static "stillValidBlockEntity"(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
public "isMutable"(): boolean
public "hasAnyOf"(arg0: $Set$Type<($Item$Type)>): boolean
public "setChanged"(): void
public "asContainer"(): $Container
public "getHeight"(): integer
public "extractItem"(slot: integer, amount: integer, simulate: boolean): $ItemStack
public "isItemValid"(slot: integer, stack: $ItemStack$Type): boolean
public "getWidth"(): integer
public "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
public "getSlotLimit"(slot: integer): integer
public "clear"(): void
public static "tryClear"(arg0: any): void
public "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
public "countNonEmpty"(ingredient: $Ingredient$Type): integer
public "countNonEmpty"(): integer
public "getAllItems"(): $List<($ItemStack)>
public "find"(ingredient: $Ingredient$Type): integer
public "find"(): integer
public "clear"(ingredient: $Ingredient$Type): void
public "count"(ingredient: $Ingredient$Type): integer
public "count"(): integer
public "isEmpty"(): boolean
get "targetWaystone"(): $Optional<($IWaystone)>
get "containerData"(): $ContainerData
get "settingsMenuProvider"(): $MenuProvider
get "completedFirstAttunement"(): boolean
get "maxAttunementTicks"(): integer
get "targetAttunementStack"(): $ItemStack
get "menuProvider"(): $BalmMenuProvider
get "items"(): $NonNullList<($ItemStack)>
get "containerSize"(): integer
get "empty"(): boolean
get "maxStackSize"(): integer
get "slots"(): integer
get "mutable"(): boolean
get "height"(): integer
get "width"(): integer
get "allItems"(): $List<($ItemStack)>
get "empty"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpPlateBlockEntity$Type = ($WarpPlateBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpPlateBlockEntity_ = $WarpPlateBlockEntity$Type;
}}
declare module "packages/net/blay09/mods/waystones/item/$WarpDustItem" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $WarpDustItem extends $Item {
static readonly "BY_BLOCK": $Map<($Block), ($Item)>
static readonly "BASE_ATTACK_DAMAGE_UUID": $UUID
static readonly "MAX_STACK_SIZE": integer
static readonly "EAT_DURATION": integer
static readonly "MAX_BAR_WIDTH": integer
 "craftingRemainingItem": $Item
 "descriptionId": string
 "renderProperties": any

constructor(arg0: $Item$Properties$Type)

public "isFoil"(arg0: $ItemStack$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpDustItem$Type = ($WarpDustItem);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpDustItem_ = $WarpDustItem$Type;
}}
declare module "packages/net/blay09/mods/waystones/worldgen/namegen/$RomanNumber" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $RomanNumber {

constructor()

public static "toRoman"(arg0: integer): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RomanNumber$Type = ($RomanNumber);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RomanNumber_ = $RomanNumber$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/gui/screen/$WaystoneSettingsScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$WaystoneSettingsMenu, $WaystoneSettingsMenu$Type} from "packages/net/blay09/mods/waystones/menu/$WaystoneSettingsMenu"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$AbstractContainerScreen, $AbstractContainerScreen$Type} from "packages/net/minecraft/client/gui/screens/inventory/$AbstractContainerScreen"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export class $WaystoneSettingsScreen extends $AbstractContainerScreen<($WaystoneSettingsMenu)> {
static readonly "INVENTORY_LOCATION": $ResourceLocation
static readonly "SLOT_ITEM_BLIT_OFFSET": integer
 "imageWidth": integer
 "hoveredSlot": $Slot
 "leftPos": integer
 "topPos": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $WaystoneSettingsMenu$Type, arg1: $Inventory$Type, arg2: $Component$Type)

public "keyPressed"(arg0: integer, arg1: integer, arg2: integer): boolean
public "m_7856_"(): void
public "render"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
public "mouseClicked"(arg0: double, arg1: double, arg2: integer): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneSettingsScreen$Type = ($WaystoneSettingsScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneSettingsScreen_ = $WaystoneSettingsScreen$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$ButtonStateProperties" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ButtonStateProperties {

constructor(arg0: integer, arg1: integer)

public "getTextureX"(): integer
public "getTextureY"(): integer
get "textureX"(): integer
get "textureY"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ButtonStateProperties$Type = ($ButtonStateProperties);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ButtonStateProperties_ = $ButtonStateProperties$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$EntityAddedEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $EntityAddedEvent extends $BalmEvent {

constructor()
constructor(arg0: $Entity$Type, arg1: $Level$Type)

public "getLevel"(): $Level
public "getEntity"(): $Entity
public "getListenerList"(): $ListenerList
get "level"(): $Level
get "entity"(): $Entity
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityAddedEvent$Type = ($EntityAddedEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityAddedEvent_ = $EntityAddedEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/network/$ServerboundMessageRegistration" {
import {$BiConsumer, $BiConsumer$Type} from "packages/java/util/function/$BiConsumer"
import {$MessageRegistration, $MessageRegistration$Type} from "packages/net/blay09/mods/balm/api/network/$MessageRegistration"
import {$Function, $Function$Type} from "packages/java/util/function/$Function"
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ServerboundMessageRegistration<T> extends $MessageRegistration<(T)> {

constructor(arg0: $ResourceLocation$Type, arg1: $Class$Type<(T)>, arg2: $BiConsumer$Type<(T), ($FriendlyByteBuf$Type)>, arg3: $Function$Type<($FriendlyByteBuf$Type), (T)>, arg4: $BiConsumer$Type<($ServerPlayer$Type), (T)>)

public "getHandler"(): $BiConsumer<($ServerPlayer), (T)>
get "handler"(): $BiConsumer<($ServerPlayer), (T)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerboundMessageRegistration$Type<T> = ($ServerboundMessageRegistration<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerboundMessageRegistration_<T> = $ServerboundMessageRegistration$Type<(T)>;
}}
declare module "packages/net/blay09/mods/waystones/core/$PlayerWaystoneManager" {
import {$IWaystoneTeleportContext, $IWaystoneTeleportContext$Type} from "packages/net/blay09/mods/waystones/api/$IWaystoneTeleportContext"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$Either, $Either$Type} from "packages/com/mojang/datafixers/util/$Either"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$IPlayerWaystoneData, $IPlayerWaystoneData$Type} from "packages/net/blay09/mods/waystones/core/$IPlayerWaystoneData"
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Mob, $Mob$Type} from "packages/net/minecraft/world/entity/$Mob"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$WarpMode, $WarpMode$Type} from "packages/net/blay09/mods/waystones/core/$WarpMode"
import {$WaystoneTeleportError, $WaystoneTeleportError$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneTeleportError"
import {$BalmEnvironment, $BalmEnvironment$Type} from "packages/net/blay09/mods/balm/api/$BalmEnvironment"
import {$List, $List$Type} from "packages/java/util/$List"
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"
import {$WaystoneEditPermissions, $WaystoneEditPermissions$Type} from "packages/net/blay09/mods/waystones/core/$WaystoneEditPermissions"

export class $PlayerWaystoneManager {

constructor()

public static "getWarpStoneCooldownUntil"(arg0: $Player$Type): long
public static "getInventoryButtonCooldownUntil"(arg0: $Player$Type): long
public static "mayTeleportToWaystone"(arg0: $Player$Type, arg1: $IWaystone$Type): boolean
public static "getCooldownMultiplier"(arg0: $IWaystone$Type): double
public static "getWaystones"(arg0: $Player$Type): $List<($IWaystone)>
public static "getExperienceLevelCost"(arg0: $Entity$Type, arg1: $IWaystone$Type, arg2: $WarpMode$Type, arg3: $IWaystoneTeleportContext$Type): integer
public static "setWarpStoneCooldownUntil"(arg0: $Player$Type, arg1: long): void
public static "informRejectedTeleport"(arg0: $Entity$Type): $Consumer<($WaystoneTeleportError)>
public static "activateWaystone"(arg0: $Player$Type, arg1: $IWaystone$Type): void
public static "findLeashedAnimals"(arg0: $Entity$Type): $List<($Mob)>
public static "getCooldownPeriod"(arg0: $WarpMode$Type, arg1: $IWaystone$Type): integer
public static "doTeleport"(arg0: $IWaystoneTeleportContext$Type): $List<($Entity)>
public static "findWarpItem"(arg0: $Entity$Type, arg1: $WarpMode$Type): $ItemStack
public static "getWarpStoneCooldownLeft"(arg0: $Player$Type): long
public static "getInventoryButtonCooldownLeft"(arg0: $Player$Type): long
public static "getPlayerWaystoneData"(arg0: $Level$Type): $IPlayerWaystoneData
public static "getPlayerWaystoneData"(arg0: $BalmEnvironment$Type): $IPlayerWaystoneData
public static "removeKnownWaystone"(arg0: $MinecraftServer$Type, arg1: $IWaystone$Type): void
public static "swapWaystoneSorting"(arg0: $Player$Type, arg1: integer, arg2: integer): void
public static "tryTeleportToWaystone"(arg0: $Entity$Type, arg1: $IWaystone$Type, arg2: $WarpMode$Type, arg3: $IWaystone$Type): $Either<($List<($Entity)>), ($WaystoneTeleportError)>
public static "isWaystoneActivated"(arg0: $Player$Type, arg1: $IWaystone$Type): boolean
public static "mayEditWaystone"(arg0: $Player$Type, arg1: $Level$Type, arg2: $IWaystone$Type): $WaystoneEditPermissions
public static "canUseWarpStone"(arg0: $Player$Type, arg1: $ItemStack$Type): boolean
public static "getNearestWaystone"(arg0: $Player$Type): $IWaystone
public static "tryTeleport"(arg0: $IWaystoneTeleportContext$Type): $Either<($List<($Entity)>), ($WaystoneTeleportError)>
public static "deactivateWaystone"(arg0: $Player$Type, arg1: $IWaystone$Type): void
public static "mayBreakWaystone"(arg0: $Player$Type, arg1: $BlockGetter$Type, arg2: $BlockPos$Type): boolean
public static "mayPlaceWaystone"(arg0: $Player$Type): boolean
public static "predictExperienceLevelCost"(arg0: $Entity$Type, arg1: $IWaystone$Type, arg2: $WarpMode$Type, arg3: $IWaystone$Type): integer
public static "setInventoryButtonCooldownUntil"(arg0: $Player$Type, arg1: long): void
public static "activeWaystoneForEveryone"(arg0: $MinecraftServer$Type, arg1: $IWaystone$Type): void
public static "canUseInventoryButton"(arg0: $Player$Type): boolean
public static "mayEditGlobalWaystones"(arg0: $Player$Type): boolean
public static "getInventoryButtonWaystone"(arg0: $Player$Type): $IWaystone
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerWaystoneManager$Type = ($PlayerWaystoneManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerWaystoneManager_ = $PlayerWaystoneManager$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$BalmEvents" {
import {$TickPhase, $TickPhase$Type} from "packages/net/blay09/mods/balm/api/event/$TickPhase"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$TickType, $TickType$Type} from "packages/net/blay09/mods/balm/api/event/$TickType"
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$EventPriority, $EventPriority$Type} from "packages/net/blay09/mods/balm/api/event/$EventPriority"

export interface $BalmEvents {

 "onEvent"<T>(arg0: $Class$Type<(T)>, arg1: $Consumer$Type<(T)>): void
 "onEvent"<T>(arg0: $Class$Type<(T)>, arg1: $Consumer$Type<(T)>, arg2: $EventPriority$Type): void
 "fireEvent"<T>(arg0: T): void
 "onTickEvent"<T>(arg0: $TickType$Type<(T)>, arg1: $TickPhase$Type, arg2: T): void
}

export namespace $BalmEvents {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmEvents$Type = ($BalmEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmEvents_ = $BalmEvents$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/network/$SyncCraftingTweaksConfigMessage" {
import {$SyncConfigMessage, $SyncConfigMessage$Type} from "packages/net/blay09/mods/balm/api/network/$SyncConfigMessage"
import {$CraftingTweaksConfigData, $CraftingTweaksConfigData$Type} from "packages/net/blay09/mods/craftingtweaks/config/$CraftingTweaksConfigData"

export class $SyncCraftingTweaksConfigMessage extends $SyncConfigMessage<($CraftingTweaksConfigData)> {

constructor(arg0: $CraftingTweaksConfigData$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SyncCraftingTweaksConfigMessage$Type = ($SyncCraftingTweaksConfigMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SyncCraftingTweaksConfigMessage_ = $SyncCraftingTweaksConfigMessage$Type;
}}
declare module "packages/net/blay09/mods/waystones/config/$WaystonesConfigData$Cooldowns" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $WaystonesConfigData$Cooldowns {
 "globalWaystoneCooldownMultiplier": double
 "warpStoneCooldown": integer
 "warpStoneUseTime": integer
 "warpPlateUseTime": integer
 "scrollUseTime": integer
 "inventoryButtonCooldown": integer

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystonesConfigData$Cooldowns$Type = ($WaystonesConfigData$Cooldowns);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystonesConfigData$Cooldowns_ = $WaystonesConfigData$Cooldowns$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/impl/$DefaultFourByFourGridRotateHandler" {
import {$DefaultGridRotateHandler, $DefaultGridRotateHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/impl/$DefaultGridRotateHandler"

export class $DefaultFourByFourGridRotateHandler extends $DefaultGridRotateHandler {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultFourByFourGridRotateHandler$Type = ($DefaultFourByFourGridRotateHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultFourByFourGridRotateHandler_ = $DefaultFourByFourGridRotateHandler$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/$CraftingTweaks" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CraftingTweaks {
static readonly "MOD_ID": string
static "debugMode": boolean
static "isServerSideInstalled": boolean

constructor()

public static "initialize"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaks$Type = ($CraftingTweaks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaks_ = $CraftingTweaks$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$PlayerAttackEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export class $PlayerAttackEvent extends $BalmEvent {

constructor()
constructor(arg0: $Player$Type, arg1: $Entity$Type)

public "getTarget"(): $Entity
public "getPlayer"(): $Player
public "getListenerList"(): $ListenerList
get "target"(): $Entity
get "player"(): $Player
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerAttackEvent$Type = ($PlayerAttackEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerAttackEvent_ = $PlayerAttackEvent$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/network/$RotateMessage" {
import {$ServerPlayer, $ServerPlayer$Type} from "packages/net/minecraft/server/level/$ServerPlayer"
import {$FriendlyByteBuf, $FriendlyByteBuf$Type} from "packages/net/minecraft/network/$FriendlyByteBuf"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $RotateMessage {

constructor(arg0: $ResourceLocation$Type, arg1: boolean)

public static "decode"(arg0: $FriendlyByteBuf$Type): $RotateMessage
public static "encode"(arg0: $RotateMessage$Type, arg1: $FriendlyByteBuf$Type): void
public static "handle"(arg0: $ServerPlayer$Type, arg1: $RotateMessage$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RotateMessage$Type = ($RotateMessage);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RotateMessage_ = $RotateMessage$Type;
}}
declare module "packages/net/blay09/mods/balm/api/fluid/$BalmFluidTankProvider" {
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$FluidTank, $FluidTank$Type} from "packages/net/blay09/mods/balm/api/fluid/$FluidTank"

export interface $BalmFluidTankProvider {

 "getFluidTank"(): $FluidTank
 "getFluidTank"(arg0: $Direction$Type): $FluidTank

(): $FluidTank
}

export namespace $BalmFluidTankProvider {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmFluidTankProvider$Type = ($BalmFluidTankProvider);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmFluidTankProvider_ = $BalmFluidTankProvider$Type;
}}
declare module "packages/net/blay09/mods/waystones/core/$InvalidWaystone" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$WaystoneOrigin, $WaystoneOrigin$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneOrigin"
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ServerLevel, $ServerLevel$Type} from "packages/net/minecraft/server/level/$ServerLevel"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$TeleportDestination, $TeleportDestination$Type} from "packages/net/blay09/mods/waystones/api/$TeleportDestination"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $InvalidWaystone implements $IWaystone {
static readonly "INSTANCE": $IWaystone

constructor()

public "getName"(): string
public "isValid"(): boolean
public "getDimension"(): $ResourceKey<($Level)>
public "isGlobal"(): boolean
public "getPos"(): $BlockPos
public "getOrigin"(): $WaystoneOrigin
public "getOwnerUid"(): $UUID
public "getWaystoneUid"(): $UUID
public "isOwner"(arg0: $Player$Type): boolean
public "getWaystoneType"(): $ResourceLocation
public "hasName"(): boolean
public "isValidInLevel"(arg0: $ServerLevel$Type): boolean
public "resolveDestination"(arg0: $ServerLevel$Type): $TeleportDestination
public "hasOwner"(): boolean
public "wasGenerated"(): boolean
get "name"(): string
get "valid"(): boolean
get "dimension"(): $ResourceKey<($Level)>
get "global"(): boolean
get "pos"(): $BlockPos
get "origin"(): $WaystoneOrigin
get "ownerUid"(): $UUID
get "waystoneUid"(): $UUID
get "waystoneType"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InvalidWaystone$Type = ($InvalidWaystone);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InvalidWaystone_ = $InvalidWaystone$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$BalmEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Event, $Event$Type} from "packages/net/minecraftforge/eventbus/api/$Event"

export class $BalmEvent extends $Event {

constructor()

public "isCancelable"(): boolean
public "getListenerList"(): $ListenerList
public "hasResult"(): boolean
get "cancelable"(): boolean
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmEvent$Type = ($BalmEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmEvent_ = $BalmEvent$Type;
}}
declare module "packages/net/blay09/mods/waystones/compat/$WaystonesWailaUtils" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Consumer, $Consumer$Type} from "packages/java/util/function/$Consumer"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $WaystonesWailaUtils {
static readonly "WAYSTONE_UID": $ResourceLocation


public static "appendTooltip"(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: $Consumer$Type<($Component$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystonesWailaUtils$Type = ($WaystonesWailaUtils);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystonesWailaUtils_ = $WaystonesWailaUtils$Type;
}}
declare module "packages/net/blay09/mods/waystones/core/$WaystoneTypes" {
import {$DyeColor, $DyeColor$Type} from "packages/net/minecraft/world/item/$DyeColor"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $WaystoneTypes {
static readonly "WAYSTONE": $ResourceLocation
static readonly "WARP_PLATE": $ResourceLocation
static readonly "PORTSTONE": $ResourceLocation

constructor()

public static "getSharestone"(arg0: $DyeColor$Type): $ResourceLocation
public static "isSharestone"(arg0: $ResourceLocation$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneTypes$Type = ($WaystoneTypes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneTypes_ = $WaystoneTypes$Type;
}}
declare module "packages/net/blay09/mods/balm/api/container/$ImplementedContainer" {
import {$CompoundTag, $CompoundTag$Type} from "packages/net/minecraft/nbt/$CompoundTag"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Predicate, $Predicate$Type} from "packages/java/util/function/$Predicate"
import {$Ingredient, $Ingredient$Type} from "packages/net/minecraft/world/item/crafting/$Ingredient"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$BlockContainerJS, $BlockContainerJS$Type} from "packages/dev/latvian/mods/kubejs/level/$BlockContainerJS"
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Container, $Container$Type} from "packages/net/minecraft/world/$Container"

export interface $ImplementedContainer extends $Container {

 "serializeInventory"(): $CompoundTag
 "setChanged"(): void
 "getItem"(arg0: integer): $ItemStack
 "getContainerSize"(): integer
 "removeItemNoUpdate"(arg0: integer): $ItemStack
 "slotChanged"(arg0: integer): void
 "removeItem"(arg0: integer, arg1: integer): $ItemStack
 "clearContent"(): void
 "isEmpty"(): boolean
 "stillValid"(arg0: $Player$Type): boolean
 "getItems"(): $NonNullList<($ItemStack)>
 "setItem"(arg0: integer, arg1: $ItemStack$Type): void
 "kjs$self"(): $Container
 "getBlock"(level: $Level$Type): $BlockContainerJS
 "startOpen"(arg0: $Player$Type): void
 "getMaxStackSize"(): integer
 "stopOpen"(arg0: $Player$Type): void
 "canPlaceItem"(arg0: integer, arg1: $ItemStack$Type): boolean
 "countItem"(arg0: $Item$Type): integer
 "canTakeItem"(arg0: $Container$Type, arg1: integer, arg2: $ItemStack$Type): boolean
 "hasAnyMatching"(arg0: $Predicate$Type<($ItemStack$Type)>): boolean
 "getSlots"(): integer
 "getStackInSlot"(slot: integer): $ItemStack
 "insertItem"(slot: integer, stack: $ItemStack$Type, simulate: boolean): $ItemStack
 "isMutable"(): boolean
 "hasAnyOf"(arg0: $Set$Type<($Item$Type)>): boolean
 "setChanged"(): void
 "asContainer"(): $Container
 "getHeight"(): integer
 "extractItem"(slot: integer, amount: integer, simulate: boolean): $ItemStack
 "isItemValid"(slot: integer, stack: $ItemStack$Type): boolean
 "getWidth"(): integer
 "setStackInSlot"(slot: integer, stack: $ItemStack$Type): void
 "getSlotLimit"(slot: integer): integer
 "clear"(): void
 "insertItem"(stack: $ItemStack$Type, simulate: boolean): $ItemStack
 "countNonEmpty"(ingredient: $Ingredient$Type): integer
 "countNonEmpty"(): integer
 "getAllItems"(): $List<($ItemStack)>
 "find"(ingredient: $Ingredient$Type): integer
 "find"(): integer
 "clear"(ingredient: $Ingredient$Type): void
 "count"(ingredient: $Ingredient$Type): integer
 "count"(): integer
 "isEmpty"(): boolean

(arg0: $NonNullList$Type<($ItemStack$Type)>): $ImplementedContainer
}

export namespace $ImplementedContainer {
function of(arg0: $NonNullList$Type<($ItemStack$Type)>): $ImplementedContainer
function deserializeInventory(arg0: $CompoundTag$Type, arg1: integer): $NonNullList<($ItemStack)>
function ofSize(arg0: integer): $ImplementedContainer
function stillValidBlockEntity(arg0: $BlockEntity$Type, arg1: $Player$Type, arg2: integer): boolean
function stillValidBlockEntity(arg0: $BlockEntity$Type, arg1: $Player$Type): boolean
function tryClear(arg0: any): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ImplementedContainer$Type = ($ImplementedContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ImplementedContainer_ = $ImplementedContainer$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$CraftingTweaksDefaultHandlers" {
import {$GridBalanceHandler, $GridBalanceHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridBalanceHandler"
import {$GridRotateHandler, $GridRotateHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridRotateHandler"
import {$GridTransferHandler, $GridTransferHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridTransferHandler"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$GridClearHandler, $GridClearHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridClearHandler"

export class $CraftingTweaksDefaultHandlers {

constructor()

public static "defaultFourByFourRotateHandler"(): $GridRotateHandler<($AbstractContainerMenu)>
public static "defaultRectangularRotateHandler"(): $GridRotateHandler<($AbstractContainerMenu)>
public static "defaultClearHandler"(): $GridClearHandler<($AbstractContainerMenu)>
public static "defaultBalanceHandler"(): $GridBalanceHandler<($AbstractContainerMenu)>
public static "defaultTransferHandler"(): $GridTransferHandler<($AbstractContainerMenu)>
public static "defaultRotateHandler"(): $GridRotateHandler<($AbstractContainerMenu)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksDefaultHandlers$Type = ($CraftingTweaksDefaultHandlers);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksDefaultHandlers_ = $CraftingTweaksDefaultHandlers$Type;
}}
declare module "packages/net/blay09/mods/balm/api/block/$BalmBlocks" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$BlockItem, $BlockItem$Type} from "packages/net/minecraft/world/item/$BlockItem"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$BlockBehaviour$Properties, $BlockBehaviour$Properties$Type} from "packages/net/minecraft/world/level/block/state/$BlockBehaviour$Properties"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export interface $BalmBlocks {

 "register"(arg0: $Supplier$Type<($Block$Type)>, arg1: $Supplier$Type<($BlockItem$Type)>, arg2: $ResourceLocation$Type): void
 "register"(arg0: $Supplier$Type<($Block$Type)>, arg1: $Supplier$Type<($BlockItem$Type)>, arg2: $ResourceLocation$Type, arg3: $ResourceLocation$Type): void
 "registerBlock"(arg0: $Supplier$Type<($Block$Type)>, arg1: $ResourceLocation$Type): $DeferredObject<($Block)>
 "blockProperties"(): $BlockBehaviour$Properties
 "registerBlockItem"(arg0: $Supplier$Type<($BlockItem$Type)>, arg1: $ResourceLocation$Type): $DeferredObject<($Item)>
 "registerBlockItem"(arg0: $Supplier$Type<($BlockItem$Type)>, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): $DeferredObject<($Item)>
}

export namespace $BalmBlocks {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmBlocks$Type = ($BalmBlocks);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmBlocks_ = $BalmBlocks$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/gui/screen/$WaystoneSelectionScreen" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$GuiEventListener, $GuiEventListener$Type} from "packages/net/minecraft/client/gui/components/events/$GuiEventListener"
import {$WaystoneSelectionScreenBase, $WaystoneSelectionScreenBase$Type} from "packages/net/blay09/mods/waystones/client/gui/screen/$WaystoneSelectionScreenBase"
import {$Screen$DeferredTooltipRendering, $Screen$DeferredTooltipRendering$Type} from "packages/net/minecraft/client/gui/screens/$Screen$DeferredTooltipRendering"
import {$Renderable, $Renderable$Type} from "packages/net/minecraft/client/gui/components/$Renderable"
import {$WaystoneSelectionMenu, $WaystoneSelectionMenu$Type} from "packages/net/blay09/mods/waystones/menu/$WaystoneSelectionMenu"
import {$List, $List$Type} from "packages/java/util/$List"
import {$NarratableEntry, $NarratableEntry$Type} from "packages/net/minecraft/client/gui/narration/$NarratableEntry"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export class $WaystoneSelectionScreen extends $WaystoneSelectionScreenBase {
static readonly "INVENTORY_LOCATION": $ResourceLocation
static readonly "SLOT_ITEM_BLIT_OFFSET": integer
 "imageWidth": integer
 "hoveredSlot": $Slot
 "leftPos": integer
 "topPos": integer
static readonly "BACKGROUND_LOCATION": $ResourceLocation
 "title": $Component
readonly "children": $List<($GuiEventListener)>
readonly "narratables": $List<($NarratableEntry)>
 "width": integer
 "height": integer
readonly "renderables": $List<($Renderable)>
 "deferredTooltipRendering": $Screen$DeferredTooltipRendering

constructor(arg0: $WaystoneSelectionMenu$Type, arg1: $Inventory$Type, arg2: $Component$Type)

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneSelectionScreen$Type = ($WaystoneSelectionScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneSelectionScreen_ = $WaystoneSelectionScreen$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/impl/$DefaultGridTransferHandler" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$GridTransferHandler, $GridTransferHandler$Type} from "packages/net/blay09/mods/craftingtweaks/api/$GridTransferHandler"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $DefaultGridTransferHandler implements $GridTransferHandler<($AbstractContainerMenu)> {

constructor()

public "canTransferFrom"(arg0: $Player$Type, arg1: $AbstractContainerMenu$Type, arg2: $Slot$Type, arg3: $CraftingGrid$Type): boolean
public "transferIntoGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: $AbstractContainerMenu$Type, arg3: $Slot$Type): boolean
public "putIntoGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: $AbstractContainerMenu$Type, arg3: integer, arg4: $ItemStack$Type): $ItemStack
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DefaultGridTransferHandler$Type = ($DefaultGridTransferHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DefaultGridTransferHandler_ = $DefaultGridTransferHandler$Type;
}}
declare module "packages/net/blay09/mods/waystones/tag/$ModItemTags" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"

export class $ModItemTags {
static readonly "BOUND_SCROLLS": $TagKey<($Item)>
static readonly "RETURN_SCROLLS": $TagKey<($Item)>
static readonly "WARP_SCROLLS": $TagKey<($Item)>
static readonly "WARP_STONES": $TagKey<($Item)>
static readonly "WARP_SHARDS": $TagKey<($Item)>
static readonly "SINGLE_USE_WARP_SHARDS": $TagKey<($Item)>
static readonly "WAYSTONES": $TagKey<($Item)>
static readonly "SHARESTONES": $TagKey<($Item)>
static readonly "DYED_SHARESTONES": $TagKey<($Item)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModItemTags$Type = ($ModItemTags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModItemTags_ = $ModItemTags$Type;
}}
declare module "packages/net/blay09/mods/defaultoptions/mixin/$ForgeKeyMappingAccessor" {
import {$KeyModifier, $KeyModifier$Type} from "packages/net/minecraftforge/client/settings/$KeyModifier"

export interface $ForgeKeyMappingAccessor {

 "setKeyModifierDefault"(arg0: $KeyModifier$Type): void

(arg0: $KeyModifier$Type): void
}

export namespace $ForgeKeyMappingAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeKeyMappingAccessor$Type = ($ForgeKeyMappingAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeKeyMappingAccessor_ = $ForgeKeyMappingAccessor$Type;
}}
declare module "packages/net/blay09/mods/waystones/tag/$ModTags" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

/**
 * 
 * @deprecated
 */
export class $ModTags {
static readonly "BOUND_SCROLLS": $TagKey<($Item)>
static readonly "RETURN_SCROLLS": $TagKey<($Item)>
static readonly "WARP_SCROLLS": $TagKey<($Item)>
static readonly "WARP_STONES": $TagKey<($Item)>
static readonly "IS_TELEPORT_TARGET": $TagKey<($Block)>
static readonly "WAYSTONES": $TagKey<($Block)>
static readonly "SHARESTONES": $TagKey<($Block)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModTags$Type = ($ModTags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModTags_ = $ModTags$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/client/$ForgeBalmClient" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"

export class $ForgeBalmClient {

constructor()

public static "onInitializeClient"(arg0: $FMLClientSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmClient$Type = ($ForgeBalmClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmClient_ = $ForgeBalmClient$Type;
}}
declare module "packages/net/blay09/mods/waystones/menu/$ModMenus" {
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$WaystoneSettingsMenu, $WaystoneSettingsMenu$Type} from "packages/net/blay09/mods/waystones/menu/$WaystoneSettingsMenu"
import {$WaystoneSelectionMenu, $WaystoneSelectionMenu$Type} from "packages/net/blay09/mods/waystones/menu/$WaystoneSelectionMenu"
import {$WarpPlateContainer, $WarpPlateContainer$Type} from "packages/net/blay09/mods/waystones/menu/$WarpPlateContainer"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$BalmMenus, $BalmMenus$Type} from "packages/net/blay09/mods/balm/api/menu/$BalmMenus"

export class $ModMenus {
static "waystoneSelection": $DeferredObject<($MenuType<($WaystoneSelectionMenu)>)>
static "sharestoneSelection": $DeferredObject<($MenuType<($WaystoneSelectionMenu)>)>
static "warpPlate": $DeferredObject<($MenuType<($WarpPlateContainer)>)>
static "waystoneSettings": $DeferredObject<($MenuType<($WaystoneSettingsMenu)>)>

constructor()

public static "initialize"(arg0: $BalmMenus$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModMenus$Type = ($ModMenus);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModMenus_ = $ModMenus$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$IMutableWaystone" {
import {$UUID, $UUID$Type} from "packages/java/util/$UUID"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$ResourceKey, $ResourceKey$Type} from "packages/net/minecraft/resources/$ResourceKey"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $IMutableWaystone {

 "setName"(arg0: string): void
 "setPos"(arg0: $BlockPos$Type): void
 "setOwnerUid"(arg0: $UUID$Type): void
 "setGlobal"(arg0: boolean): void
 "setDimension"(arg0: $ResourceKey$Type<($Level$Type)>): void
}

export namespace $IMutableWaystone {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IMutableWaystone$Type = ($IMutableWaystone);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IMutableWaystone_ = $IMutableWaystone$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$TweakType" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $TweakType extends $Enum<($TweakType)> {
static readonly "Rotate": $TweakType
static readonly "Balance": $TweakType
static readonly "Clear": $TweakType
static readonly "RotateCounterClockwise": $TweakType
static readonly "Spread": $TweakType
static readonly "ForceClear": $TweakType


public static "values"(): ($TweakType)[]
public static "valueOf"(arg0: string): $TweakType
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TweakType$Type = (("rotate") | ("balance") | ("clear") | ("rotatecounterclockwise") | ("spread") | ("forceclear")) | ($TweakType);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TweakType_ = $TweakType$Type;
}}
declare module "packages/net/blay09/mods/kuma/api/$WorldInputEvent" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $WorldInputEvent extends $Record {

constructor()

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WorldInputEvent$Type = ($WorldInputEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WorldInputEvent_ = $WorldInputEvent$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/registry/$JsonCompatLoader" {
import {$CompletableFuture, $CompletableFuture$Type} from "packages/java/util/concurrent/$CompletableFuture"
import {$PreparableReloadListener$PreparationBarrier, $PreparableReloadListener$PreparationBarrier$Type} from "packages/net/minecraft/server/packs/resources/$PreparableReloadListener$PreparationBarrier"
import {$ResourceManagerReloadListener, $ResourceManagerReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManagerReloadListener"
import {$Executor, $Executor$Type} from "packages/java/util/concurrent/$Executor"
import {$ResourceManager, $ResourceManager$Type} from "packages/net/minecraft/server/packs/resources/$ResourceManager"
import {$ProfilerFiller, $ProfilerFiller$Type} from "packages/net/minecraft/util/profiling/$ProfilerFiller"

export class $JsonCompatLoader implements $ResourceManagerReloadListener {

constructor()

public "onResourceManagerReload"(arg0: $ResourceManager$Type): void
public "reload"(arg0: $PreparableReloadListener$PreparationBarrier$Type, arg1: $ResourceManager$Type, arg2: $ProfilerFiller$Type, arg3: $ProfilerFiller$Type, arg4: $Executor$Type, arg5: $Executor$Type): $CompletableFuture<(void)>
public "getName"(): string
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JsonCompatLoader$Type = ($JsonCompatLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JsonCompatLoader_ = $JsonCompatLoader$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/event/$ForgeBalmClientEvents" {
import {$ForgeBalmEvents, $ForgeBalmEvents$Type} from "packages/net/blay09/mods/balm/forge/event/$ForgeBalmEvents"

export class $ForgeBalmClientEvents {

constructor()

public static "registerEvents"(arg0: $ForgeBalmEvents$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmClientEvents$Type = ($ForgeBalmClientEvents);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmClientEvents_ = $ForgeBalmClientEvents$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/client/$GuiImageButton" {
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"
import {$ButtonProperties, $ButtonProperties$Type} from "packages/net/blay09/mods/craftingtweaks/api/$ButtonProperties"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"
import {$Button$OnPress, $Button$OnPress$Type} from "packages/net/minecraft/client/gui/components/$Button$OnPress"

export class $GuiImageButton extends $Button {
static readonly "SMALL_WIDTH": integer
static readonly "DEFAULT_WIDTH": integer
static readonly "DEFAULT_HEIGHT": integer
 "onPress": $Button$OnPress
static readonly "WIDGETS_LOCATION": $ResourceLocation
static readonly "ACCESSIBILITY_TEXTURE": $ResourceLocation
 "height": integer
 "x": integer
 "y": integer
 "active": boolean
 "visible": boolean
static readonly "UNSET_FG_COLOR": integer

constructor(arg0: integer, arg1: integer, arg2: $ButtonProperties$Type)

public "m_87963_"(arg0: $GuiGraphics$Type, arg1: integer, arg2: integer, arg3: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GuiImageButton$Type = ($GuiImageButton);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GuiImageButton_ = $GuiImageButton$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$LivingDeathEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$DamageSource, $DamageSource$Type} from "packages/net/minecraft/world/damagesource/$DamageSource"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $LivingDeathEvent extends $BalmEvent {

constructor()
constructor(arg0: $LivingEntity$Type, arg1: $DamageSource$Type)

public "getDamageSource"(): $DamageSource
public "getEntity"(): $LivingEntity
public "getListenerList"(): $ListenerList
get "damageSource"(): $DamageSource
get "entity"(): $LivingEntity
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingDeathEvent$Type = ($LivingDeathEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingDeathEvent_ = $LivingDeathEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/client/keymappings/$ForgeBalmKeyMappings" {
import {$KeyMapping, $KeyMapping$Type} from "packages/net/minecraft/client/$KeyMapping"
import {$KeyModifiers, $KeyModifiers$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$KeyModifiers"
import {$KeyConflictContext, $KeyConflictContext$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$KeyConflictContext"
import {$KeyModifier, $KeyModifier$Type} from "packages/net/blay09/mods/balm/api/client/keymappings/$KeyModifier"
import {$CommonBalmKeyMappings, $CommonBalmKeyMappings$Type} from "packages/net/blay09/mods/balm/common/client/keymappings/$CommonBalmKeyMappings"
import {$InputConstants$Type, $InputConstants$Type$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Type"
import {$InputConstants$Key, $InputConstants$Key$Type} from "packages/com/mojang/blaze3d/platform/$InputConstants$Key"

export class $ForgeBalmKeyMappings extends $CommonBalmKeyMappings {

constructor()

public "register"(): void
public "registerKeyMapping"(arg0: string, arg1: $KeyConflictContext$Type, arg2: $KeyModifiers$Type, arg3: $InputConstants$Type$Type, arg4: integer, arg5: string): $KeyMapping
public "registerKeyMapping"(arg0: string, arg1: $KeyConflictContext$Type, arg2: $KeyModifier$Type, arg3: $InputConstants$Type$Type, arg4: integer, arg5: string): $KeyMapping
public "isActiveAndMatches"(arg0: $KeyMapping$Type, arg1: integer, arg2: integer): boolean
public "isActiveAndMatches"(arg0: $KeyMapping$Type, arg1: $InputConstants$Type$Type, arg2: integer, arg3: integer): boolean
public "isActiveAndMatches"(arg0: $KeyMapping$Type, arg1: $InputConstants$Key$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmKeyMappings$Type = ($ForgeBalmKeyMappings);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmKeyMappings_ = $ForgeBalmKeyMappings$Type;
}}
declare module "packages/net/blay09/mods/balm/mixin/$CheckboxAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $CheckboxAccessor {

 "setSelected"(arg0: boolean): void

(arg0: boolean): void
}

export namespace $CheckboxAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CheckboxAccessor$Type = ($CheckboxAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CheckboxAccessor_ = $CheckboxAccessor$Type;
}}
declare module "packages/net/blay09/mods/balm/api/$BalmRuntimeFactory" {
import {$BalmRuntime, $BalmRuntime$Type} from "packages/net/blay09/mods/balm/api/$BalmRuntime"

export interface $BalmRuntimeFactory {

 "create"(): $BalmRuntime

(): $BalmRuntime
}

export namespace $BalmRuntimeFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmRuntimeFactory$Type = ($BalmRuntimeFactory);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmRuntimeFactory_ = $BalmRuntimeFactory$Type;
}}
declare module "packages/net/blay09/mods/balm/api/$BalmRegistries" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$EntityType, $EntityType$Type} from "packages/net/minecraft/world/entity/$EntityType"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$MenuType, $MenuType$Type} from "packages/net/minecraft/world/inventory/$MenuType"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$MobEffect, $MobEffect$Type} from "packages/net/minecraft/world/effect/$MobEffect"

export interface $BalmRegistries {

 "getKey"(arg0: $MenuType$Type<(any)>): $ResourceLocation
 "getKey"(arg0: $EntityType$Type<(any)>): $ResourceLocation
 "getKey"(arg0: $Fluid$Type): $ResourceLocation
 "getKey"(arg0: $Block$Type): $ResourceLocation
 "getKey"(arg0: $Item$Type): $ResourceLocation
 "getAttribute"(arg0: $ResourceLocation$Type): $Attribute
 "getItem"(arg0: $ResourceLocation$Type): $Item
 "getBlock"(arg0: $ResourceLocation$Type): $Block
 "getMilkFluid"(): $Fluid
 "getItemKeys"(): $Collection<($ResourceLocation)>
 "getMobEffect"(arg0: $ResourceLocation$Type): $MobEffect
 "enableMilkFluid"(): void
 "getFluid"(arg0: $ResourceLocation$Type): $Fluid
 "getItemTag"(arg0: $ResourceLocation$Type): $TagKey<($Item)>
}

export namespace $BalmRegistries {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmRegistries$Type = ($BalmRegistries);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmRegistries_ = $BalmRegistries$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$GridClearHandler" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$CraftingGrid, $CraftingGrid$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGrid"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"

export interface $GridClearHandler<TMenu extends $AbstractContainerMenu> {

 "clearGrid"(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: TMenu, arg3: boolean): void

(arg0: $CraftingGrid$Type, arg1: $Player$Type, arg2: TMenu, arg3: boolean): void
}

export namespace $GridClearHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GridClearHandler$Type<TMenu> = ($GridClearHandler<(TMenu)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GridClearHandler_<TMenu> = $GridClearHandler$Type<(TMenu)>;
}}
declare module "packages/net/blay09/mods/balm/api/item/$BalmItems" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $BalmItems {

 "registerCreativeModeTab"(arg0: $Supplier$Type<($ItemStack$Type)>, arg1: $ResourceLocation$Type): $DeferredObject<($CreativeModeTab)>
/**
 * 
 * @deprecated
 */
 "registerCreativeModeTab"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($ItemStack$Type)>): $DeferredObject<($CreativeModeTab)>
 "addToCreativeModeTab"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<(($ItemLike$Type)[])>): void
 "registerItem"(arg0: $Supplier$Type<($Item$Type)>, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): $DeferredObject<($Item)>
 "registerItem"(arg0: $Supplier$Type<($Item$Type)>, arg1: $ResourceLocation$Type): $DeferredObject<($Item)>
 "itemProperties"(): $Item$Properties
}

export namespace $BalmItems {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmItems$Type = ($BalmItems);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmItems_ = $BalmItems$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/screen/$ScreenMouseEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $ScreenMouseEvent extends $BalmEvent {

constructor()
constructor(arg0: $Screen$Type, arg1: double, arg2: double, arg3: integer)

public "getMouseX"(): double
public "getMouseY"(): double
public "getButton"(): integer
public "getScreen"(): $Screen
public "getListenerList"(): $ListenerList
get "mouseX"(): double
get "mouseY"(): double
get "button"(): integer
get "screen"(): $Screen
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ScreenMouseEvent$Type = ($ScreenMouseEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ScreenMouseEvent_ = $ScreenMouseEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$TickPhase" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $TickPhase extends $Enum<($TickPhase)> {
static readonly "Start": $TickPhase
static readonly "End": $TickPhase


public static "values"(): ($TickPhase)[]
public static "valueOf"(arg0: string): $TickPhase
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TickPhase$Type = (("start") | ("end")) | ($TickPhase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TickPhase_ = $TickPhase$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/$ModScreens" {
import {$BalmScreens, $BalmScreens$Type} from "packages/net/blay09/mods/balm/api/client/screen/$BalmScreens"

export class $ModScreens {

constructor()

public static "initialize"(arg0: $BalmScreens$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModScreens$Type = ($ModScreens);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModScreens_ = $ModScreens$Type;
}}
declare module "packages/net/blay09/mods/balm/api/provider/$BalmProviderHolder" {
import {$BalmProvider, $BalmProvider$Type} from "packages/net/blay09/mods/balm/api/provider/$BalmProvider"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Direction, $Direction$Type} from "packages/net/minecraft/core/$Direction"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"

export interface $BalmProviderHolder {

 "getProviders"(): $List<($BalmProvider<(any)>)>
 "getSidedProviders"(): $List<($Pair<($Direction), ($BalmProvider<(any)>)>)>
}

export namespace $BalmProviderHolder {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmProviderHolder$Type = ($BalmProviderHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmProviderHolder_ = $BalmProviderHolder$Type;
}}
declare module "packages/net/blay09/mods/balm/mixin/$AbstractContainerScreenAccessor" {
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export interface $AbstractContainerScreenAccessor {

 "getLeftPos"(): integer
 "getImageWidth"(): integer
 "getTopPos"(): integer
 "getHoveredSlot"(): $Slot
 "callIsHovering"(arg0: $Slot$Type, arg1: double, arg2: double): boolean
 "callRenderSlot"(arg0: $GuiGraphics$Type, arg1: $Slot$Type): void
 "getImageHeight"(): integer
}

export namespace $AbstractContainerScreenAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AbstractContainerScreenAccessor$Type = ($AbstractContainerScreenAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AbstractContainerScreenAccessor_ = $AbstractContainerScreenAccessor$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$FovUpdateEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $FovUpdateEvent extends $BalmEvent {

constructor()
constructor(arg0: $LivingEntity$Type)

public "setFov"(arg0: float): void
public "getEntity"(): $LivingEntity
public "getFov"(): float
public "getListenerList"(): $ListenerList
set "fov"(value: float)
get "entity"(): $LivingEntity
get "fov"(): float
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FovUpdateEvent$Type = ($FovUpdateEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FovUpdateEvent_ = $FovUpdateEvent$Type;
}}
declare module "packages/net/blay09/mods/waystones/handler/$WarpStoneFOVHandler" {
import {$FovUpdateEvent, $FovUpdateEvent$Type} from "packages/net/blay09/mods/balm/api/event/client/$FovUpdateEvent"

export class $WarpStoneFOVHandler {

constructor()

public static "onFOV"(arg0: $FovUpdateEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpStoneFOVHandler$Type = ($WarpStoneFOVHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpStoneFOVHandler_ = $WarpStoneFOVHandler$Type;
}}
declare module "packages/net/blay09/mods/waystones/block/entity/$PortstoneBlockEntity" {
import {$CustomRenderBoundingBox, $CustomRenderBoundingBox$Type} from "packages/net/blay09/mods/balm/api/block/entity/$CustomRenderBoundingBox"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BalmBlockEntity, $BalmBlockEntity$Type} from "packages/net/blay09/mods/balm/common/$BalmBlockEntity"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$AABB, $AABB$Type} from "packages/net/minecraft/world/phys/$AABB"

export class $PortstoneBlockEntity extends $BalmBlockEntity implements $CustomRenderBoundingBox {
 "blockState": $BlockState

constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "getRenderBoundingBox"(): $AABB
get "renderBoundingBox"(): $AABB
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PortstoneBlockEntity$Type = ($PortstoneBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PortstoneBlockEntity_ = $PortstoneBlockEntity$Type;
}}
declare module "packages/net/blay09/mods/balm/api/loot/$BalmLootTables" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$BalmLootModifier, $BalmLootModifier$Type} from "packages/net/blay09/mods/balm/api/loot/$BalmLootModifier"

export interface $BalmLootTables {

 "registerLootModifier"(arg0: $ResourceLocation$Type, arg1: $BalmLootModifier$Type): void

(arg0: $ResourceLocation$Type, arg1: $BalmLootModifier$Type): void
}

export namespace $BalmLootTables {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmLootTables$Type = ($BalmLootTables);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmLootTables_ = $BalmLootTables$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$ServerTickHandler" {
import {$MinecraftServer, $MinecraftServer$Type} from "packages/net/minecraft/server/$MinecraftServer"

export interface $ServerTickHandler {

 "handle"(arg0: $MinecraftServer$Type): void

(arg0: $MinecraftServer$Type): void
}

export namespace $ServerTickHandler {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ServerTickHandler$Type = ($ServerTickHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ServerTickHandler_ = $ServerTickHandler$Type;
}}
declare module "packages/net/blay09/mods/balm/api/block/entity/$BalmBlockEntityFactory" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export interface $BalmBlockEntityFactory<T extends $BlockEntity> {

 "create"(arg0: $BlockPos$Type, arg1: $BlockState$Type): T

(arg0: $BlockPos$Type, arg1: $BlockState$Type): T
}

export namespace $BalmBlockEntityFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmBlockEntityFactory$Type<T> = ($BalmBlockEntityFactory<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmBlockEntityFactory_<T> = $BalmBlockEntityFactory$Type<(T)>;
}}
declare module "packages/net/blay09/mods/waystones/worldgen/namegen/$INameGenerator" {
import {$RandomSource, $RandomSource$Type} from "packages/net/minecraft/util/$RandomSource"

export interface $INameGenerator {

 "randomName"(arg0: $RandomSource$Type): string

(arg0: $RandomSource$Type): string
}

export namespace $INameGenerator {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $INameGenerator$Type = ($INameGenerator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $INameGenerator_ = $INameGenerator$Type;
}}
declare module "packages/net/blay09/mods/waystones/config/$InventoryButtonMode" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $InventoryButtonMode {

constructor(arg0: string)

public "isEnabled"(): boolean
public "hasNamedTarget"(): boolean
public "getNamedTarget"(): string
public "isReturnToNearest"(): boolean
public "isReturnToAny"(): boolean
get "enabled"(): boolean
get "namedTarget"(): string
get "returnToNearest"(): boolean
get "returnToAny"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InventoryButtonMode$Type = ($InventoryButtonMode);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InventoryButtonMode_ = $InventoryButtonMode$Type;
}}
declare module "packages/net/blay09/mods/waystones/menu/$WarpPlateContainer" {
import {$IWaystone, $IWaystone$Type} from "packages/net/blay09/mods/waystones/api/$IWaystone"
import {$NonNullList, $NonNullList$Type} from "packages/net/minecraft/core/$NonNullList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$WarpPlateBlockEntity, $WarpPlateBlockEntity$Type} from "packages/net/blay09/mods/waystones/block/entity/$WarpPlateBlockEntity"
import {$Slot, $Slot$Type} from "packages/net/minecraft/world/inventory/$Slot"
import {$AbstractContainerMenu, $AbstractContainerMenu$Type} from "packages/net/minecraft/world/inventory/$AbstractContainerMenu"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"
import {$ContainerData, $ContainerData$Type} from "packages/net/minecraft/world/inventory/$ContainerData"

export class $WarpPlateContainer extends $AbstractContainerMenu {
static readonly "SLOT_CLICKED_OUTSIDE": integer
static readonly "QUICKCRAFT_TYPE_CHARITABLE": integer
static readonly "QUICKCRAFT_TYPE_GREEDY": integer
static readonly "QUICKCRAFT_TYPE_CLONE": integer
static readonly "QUICKCRAFT_HEADER_START": integer
static readonly "QUICKCRAFT_HEADER_CONTINUE": integer
static readonly "QUICKCRAFT_HEADER_END": integer
static readonly "CARRIED_SLOT_SIZE": integer
readonly "lastSlots": $NonNullList<($ItemStack)>
readonly "slots": $NonNullList<($Slot)>
 "remoteSlots": $NonNullList<($ItemStack)>
 "containerId": integer

constructor(arg0: integer, arg1: $WarpPlateBlockEntity$Type, arg2: $ContainerData$Type, arg3: $Inventory$Type)

public "getAttunementProgress"(): float
public "stillValid"(arg0: $Player$Type): boolean
public "getWaystone"(): $IWaystone
public "quickMoveStack"(arg0: $Player$Type, arg1: integer): $ItemStack
get "attunementProgress"(): float
get "waystone"(): $IWaystone
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WarpPlateContainer$Type = ($WarpPlateContainer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WarpPlateContainer_ = $WarpPlateContainer$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$OpenScreenEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"

export class $OpenScreenEvent extends $BalmEvent {

constructor()
constructor(arg0: $Screen$Type)

public "setScreen"(arg0: $Screen$Type): void
public "getNewScreen"(): $Screen
public "getScreen"(): $Screen
public "getListenerList"(): $ListenerList
set "screen"(value: $Screen$Type)
get "newScreen"(): $Screen
get "screen"(): $Screen
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $OpenScreenEvent$Type = ($OpenScreenEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $OpenScreenEvent_ = $OpenScreenEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/screen/$ContainerScreenDrawEvent$Background" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$ContainerScreenDrawEvent, $ContainerScreenDrawEvent$Type} from "packages/net/blay09/mods/balm/api/event/client/screen/$ContainerScreenDrawEvent"
import {$Screen, $Screen$Type} from "packages/net/minecraft/client/gui/screens/$Screen"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $ContainerScreenDrawEvent$Background extends $ContainerScreenDrawEvent {

constructor(arg0: $Screen$Type, arg1: $GuiGraphics$Type, arg2: integer, arg3: integer)
constructor()

public "getListenerList"(): $ListenerList
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ContainerScreenDrawEvent$Background$Type = ($ContainerScreenDrawEvent$Background);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ContainerScreenDrawEvent$Background_ = $ContainerScreenDrawEvent$Background$Type;
}}
declare module "packages/net/blay09/mods/waystones/compat/jei/$JEIAddon" {
import {$IGuiHandlerRegistration, $IGuiHandlerRegistration$Type} from "packages/mezz/jei/api/registration/$IGuiHandlerRegistration"
import {$IJeiConfigManager, $IJeiConfigManager$Type} from "packages/mezz/jei/api/runtime/config/$IJeiConfigManager"
import {$IAdvancedRegistration, $IAdvancedRegistration$Type} from "packages/mezz/jei/api/registration/$IAdvancedRegistration"
import {$IVanillaCategoryExtensionRegistration, $IVanillaCategoryExtensionRegistration$Type} from "packages/mezz/jei/api/registration/$IVanillaCategoryExtensionRegistration"
import {$IRecipeRegistration, $IRecipeRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeRegistration"
import {$IRecipeTransferRegistration, $IRecipeTransferRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeTransferRegistration"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$IJeiRuntime, $IJeiRuntime$Type} from "packages/mezz/jei/api/runtime/$IJeiRuntime"
import {$IRecipeCatalystRegistration, $IRecipeCatalystRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCatalystRegistration"
import {$IModPlugin, $IModPlugin$Type} from "packages/mezz/jei/api/$IModPlugin"
import {$IRuntimeRegistration, $IRuntimeRegistration$Type} from "packages/mezz/jei/api/registration/$IRuntimeRegistration"
import {$IRecipeCategoryRegistration, $IRecipeCategoryRegistration$Type} from "packages/mezz/jei/api/registration/$IRecipeCategoryRegistration"
import {$IModIngredientRegistration, $IModIngredientRegistration$Type} from "packages/mezz/jei/api/registration/$IModIngredientRegistration"
import {$ISubtypeRegistration, $ISubtypeRegistration$Type} from "packages/mezz/jei/api/registration/$ISubtypeRegistration"
import {$IPlatformFluidHelper, $IPlatformFluidHelper$Type} from "packages/mezz/jei/api/helpers/$IPlatformFluidHelper"

export class $JEIAddon implements $IModPlugin {

constructor()

public "getPluginUid"(): $ResourceLocation
public "registerRecipes"(arg0: $IRecipeRegistration$Type): void
public "registerCategories"(arg0: $IRecipeCategoryRegistration$Type): void
public "registerItemSubtypes"(arg0: $ISubtypeRegistration$Type): void
public "registerVanillaCategoryExtensions"(arg0: $IVanillaCategoryExtensionRegistration$Type): void
public "registerFluidSubtypes"<T>(arg0: $ISubtypeRegistration$Type, arg1: $IPlatformFluidHelper$Type<(T)>): void
public "onConfigManagerAvailable"(arg0: $IJeiConfigManager$Type): void
public "registerGuiHandlers"(arg0: $IGuiHandlerRegistration$Type): void
public "onRuntimeUnavailable"(): void
public "registerIngredients"(arg0: $IModIngredientRegistration$Type): void
public "registerRecipeTransferHandlers"(arg0: $IRecipeTransferRegistration$Type): void
public "registerRecipeCatalysts"(arg0: $IRecipeCatalystRegistration$Type): void
public "registerAdvanced"(arg0: $IAdvancedRegistration$Type): void
public "onRuntimeAvailable"(arg0: $IJeiRuntime$Type): void
public "registerRuntime"(arg0: $IRuntimeRegistration$Type): void
get "pluginUid"(): $ResourceLocation
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEIAddon$Type = ($JEIAddon);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEIAddon_ = $JEIAddon$Type;
}}
declare module "packages/net/blay09/mods/waystones/api/$EventResult" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $EventResult extends $Enum<($EventResult)> {
static readonly "DEFAULT": $EventResult
static readonly "ALLOW": $EventResult
static readonly "DENY": $EventResult


public static "values"(): ($EventResult)[]
public static "valueOf"(arg0: string): $EventResult
public "withDefault"(arg0: $Supplier$Type<(boolean)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventResult$Type = (("allow") | ("default") | ("deny")) | ($EventResult);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventResult_ = $EventResult$Type;
}}
declare module "packages/net/blay09/mods/balm/api/client/$BalmClientProxy" {
import {$BalmProxy, $BalmProxy$Type} from "packages/net/blay09/mods/balm/api/$BalmProxy"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"

export class $BalmClientProxy extends $BalmProxy {

constructor()

public "getClientPlayer"(): $Player
public "isConnectedToServer"(): boolean
get "clientPlayer"(): $Player
get "connectedToServer"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmClientProxy$Type = ($BalmClientProxy);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmClientProxy_ = $BalmClientProxy$Type;
}}
declare module "packages/net/blay09/mods/waystones/block/entity/$SharestoneBlockEntity" {
import {$BalmMenuProvider, $BalmMenuProvider$Type} from "packages/net/blay09/mods/balm/api/menu/$BalmMenuProvider"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$MenuProvider, $MenuProvider$Type} from "packages/net/minecraft/world/$MenuProvider"
import {$WaystoneBlockEntityBase, $WaystoneBlockEntityBase$Type} from "packages/net/blay09/mods/waystones/block/entity/$WaystoneBlockEntityBase"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $SharestoneBlockEntity extends $WaystoneBlockEntityBase {
 "blockState": $BlockState

constructor(arg0: $BlockPos$Type, arg1: $BlockState$Type)

public "getSettingsMenuProvider"(): $BalmMenuProvider
public "getMenuProvider"(): $MenuProvider
get "settingsMenuProvider"(): $BalmMenuProvider
get "menuProvider"(): $MenuProvider
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SharestoneBlockEntity$Type = ($SharestoneBlockEntity);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SharestoneBlockEntity_ = $SharestoneBlockEntity$Type;
}}
declare module "packages/net/blay09/mods/waystones/tag/$ModBlockTags" {
import {$TagKey, $TagKey$Type} from "packages/net/minecraft/tags/$TagKey"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $ModBlockTags {
static readonly "IS_TELEPORT_TARGET": $TagKey<($Block)>
static readonly "WAYSTONES": $TagKey<($Block)>
static readonly "SHARESTONES": $TagKey<($Block)>
static readonly "DYED_SHARESTONES": $TagKey<($Block)>

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModBlockTags$Type = ($ModBlockTags);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModBlockTags_ = $ModBlockTags$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/client/$BlockHighlightDrawEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$BlockHitResult, $BlockHitResult$Type} from "packages/net/minecraft/world/phys/$BlockHitResult"
import {$Camera, $Camera$Type} from "packages/net/minecraft/client/$Camera"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$MultiBufferSource, $MultiBufferSource$Type} from "packages/net/minecraft/client/renderer/$MultiBufferSource"

export class $BlockHighlightDrawEvent extends $BalmEvent {

constructor()
constructor(arg0: $BlockHitResult$Type, arg1: $PoseStack$Type, arg2: $MultiBufferSource$Type, arg3: $Camera$Type)

public "getPoseStack"(): $PoseStack
public "getMultiBufferSource"(): $MultiBufferSource
public "getCamera"(): $Camera
public "getHitResult"(): $BlockHitResult
public "getListenerList"(): $ListenerList
get "poseStack"(): $PoseStack
get "multiBufferSource"(): $MultiBufferSource
get "camera"(): $Camera
get "hitResult"(): $BlockHitResult
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockHighlightDrawEvent$Type = ($BlockHighlightDrawEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockHighlightDrawEvent_ = $BlockHighlightDrawEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$BreakBlockEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BreakBlockEvent extends $BalmEvent {

constructor()
constructor(arg0: $Level$Type, arg1: $Player$Type, arg2: $BlockPos$Type, arg3: $BlockState$Type, arg4: $BlockEntity$Type)

public "getState"(): $BlockState
public "getLevel"(): $Level
public "getBlockEntity"(): $BlockEntity
public "getPlayer"(): $Player
public "getPos"(): $BlockPos
public "getListenerList"(): $ListenerList
get "state"(): $BlockState
get "level"(): $Level
get "blockEntity"(): $BlockEntity
get "player"(): $Player
get "pos"(): $BlockPos
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BreakBlockEvent$Type = ($BreakBlockEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BreakBlockEvent_ = $BreakBlockEvent$Type;
}}
declare module "packages/net/blay09/mods/waystones/worldgen/$WaystoneFeature" {
import {$BonusChestFeature, $BonusChestFeature$Type} from "packages/net/minecraft/world/level/levelgen/feature/$BonusChestFeature"
import {$RandomFeatureConfiguration, $RandomFeatureConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$RandomFeatureConfiguration"
import {$SpikeConfiguration, $SpikeConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$SpikeConfiguration"
import {$LakeFeature$Configuration, $LakeFeature$Configuration$Type} from "packages/net/minecraft/world/level/levelgen/feature/$LakeFeature$Configuration"
import {$BlockColumnConfiguration, $BlockColumnConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$BlockColumnConfiguration"
import {$MultifaceGrowthConfiguration, $MultifaceGrowthConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$MultifaceGrowthConfiguration"
import {$RootSystemConfiguration, $RootSystemConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$RootSystemConfiguration"
import {$UnderwaterMagmaConfiguration, $UnderwaterMagmaConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$UnderwaterMagmaConfiguration"
import {$RandomPatchConfiguration, $RandomPatchConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$RandomPatchConfiguration"
import {$ProbabilityFeatureConfiguration, $ProbabilityFeatureConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$ProbabilityFeatureConfiguration"
import {$Codec, $Codec$Type} from "packages/com/mojang/serialization/$Codec"
import {$TreeConfiguration, $TreeConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$TreeConfiguration"
import {$TwistingVinesConfig, $TwistingVinesConfig$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$TwistingVinesConfig"
import {$FeaturePlaceContext, $FeaturePlaceContext$Type} from "packages/net/minecraft/world/level/levelgen/feature/$FeaturePlaceContext"
import {$DripstoneClusterConfiguration, $DripstoneClusterConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$DripstoneClusterConfiguration"
import {$LayerConfiguration, $LayerConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$LayerConfiguration"
import {$HugeMushroomFeatureConfiguration, $HugeMushroomFeatureConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$HugeMushroomFeatureConfiguration"
import {$ReplaceBlockConfiguration, $ReplaceBlockConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$ReplaceBlockConfiguration"
import {$BlockStateConfiguration, $BlockStateConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$BlockStateConfiguration"
import {$SimpleBlockConfiguration, $SimpleBlockConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$SimpleBlockConfiguration"
import {$HugeFungusConfiguration, $HugeFungusConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/$HugeFungusConfiguration"
import {$DiskConfiguration, $DiskConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$DiskConfiguration"
import {$SeagrassFeature, $SeagrassFeature$Type} from "packages/net/minecraft/world/level/levelgen/feature/$SeagrassFeature"
import {$ReplaceSphereConfiguration, $ReplaceSphereConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$ReplaceSphereConfiguration"
import {$FossilFeatureConfiguration, $FossilFeatureConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/$FossilFeatureConfiguration"
import {$DeltaFeatureConfiguration, $DeltaFeatureConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$DeltaFeatureConfiguration"
import {$PointedDripstoneConfiguration, $PointedDripstoneConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$PointedDripstoneConfiguration"
import {$SpringConfiguration, $SpringConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$SpringConfiguration"
import {$BlockPileConfiguration, $BlockPileConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$BlockPileConfiguration"
import {$OreConfiguration, $OreConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$OreConfiguration"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$SimpleRandomFeatureConfiguration, $SimpleRandomFeatureConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$SimpleRandomFeatureConfiguration"
import {$SculkPatchConfiguration, $SculkPatchConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$SculkPatchConfiguration"
import {$NetherForestVegetationConfig, $NetherForestVegetationConfig$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$NetherForestVegetationConfig"
import {$GeodeConfiguration, $GeodeConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$GeodeConfiguration"
import {$LargeDripstoneConfiguration, $LargeDripstoneConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$LargeDripstoneConfiguration"
import {$RandomBooleanFeatureConfiguration, $RandomBooleanFeatureConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$RandomBooleanFeatureConfiguration"
import {$ColumnFeatureConfiguration, $ColumnFeatureConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$ColumnFeatureConfiguration"
import {$CountConfiguration, $CountConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$CountConfiguration"
import {$VegetationPatchConfiguration, $VegetationPatchConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$VegetationPatchConfiguration"
import {$Feature, $Feature$Type} from "packages/net/minecraft/world/level/levelgen/feature/$Feature"
import {$NoneFeatureConfiguration, $NoneFeatureConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$NoneFeatureConfiguration"
import {$EndGatewayConfiguration, $EndGatewayConfiguration$Type} from "packages/net/minecraft/world/level/levelgen/feature/configurations/$EndGatewayConfiguration"

export class $WaystoneFeature extends $Feature<($NoneFeatureConfiguration)> {
static readonly "NO_OP": $Feature<($NoneFeatureConfiguration)>
static readonly "TREE": $Feature<($TreeConfiguration)>
static readonly "FLOWER": $Feature<($RandomPatchConfiguration)>
static readonly "NO_BONEMEAL_FLOWER": $Feature<($RandomPatchConfiguration)>
static readonly "RANDOM_PATCH": $Feature<($RandomPatchConfiguration)>
static readonly "BLOCK_PILE": $Feature<($BlockPileConfiguration)>
static readonly "SPRING": $Feature<($SpringConfiguration)>
static readonly "CHORUS_PLANT": $Feature<($NoneFeatureConfiguration)>
static readonly "REPLACE_SINGLE_BLOCK": $Feature<($ReplaceBlockConfiguration)>
static readonly "VOID_START_PLATFORM": $Feature<($NoneFeatureConfiguration)>
static readonly "DESERT_WELL": $Feature<($NoneFeatureConfiguration)>
static readonly "FOSSIL": $Feature<($FossilFeatureConfiguration)>
static readonly "HUGE_RED_MUSHROOM": $Feature<($HugeMushroomFeatureConfiguration)>
static readonly "HUGE_BROWN_MUSHROOM": $Feature<($HugeMushroomFeatureConfiguration)>
static readonly "ICE_SPIKE": $Feature<($NoneFeatureConfiguration)>
static readonly "GLOWSTONE_BLOB": $Feature<($NoneFeatureConfiguration)>
static readonly "FREEZE_TOP_LAYER": $Feature<($NoneFeatureConfiguration)>
static readonly "VINES": $Feature<($NoneFeatureConfiguration)>
static readonly "BLOCK_COLUMN": $Feature<($BlockColumnConfiguration)>
static readonly "VEGETATION_PATCH": $Feature<($VegetationPatchConfiguration)>
static readonly "WATERLOGGED_VEGETATION_PATCH": $Feature<($VegetationPatchConfiguration)>
static readonly "ROOT_SYSTEM": $Feature<($RootSystemConfiguration)>
static readonly "MULTIFACE_GROWTH": $Feature<($MultifaceGrowthConfiguration)>
static readonly "UNDERWATER_MAGMA": $Feature<($UnderwaterMagmaConfiguration)>
static readonly "MONSTER_ROOM": $Feature<($NoneFeatureConfiguration)>
static readonly "BLUE_ICE": $Feature<($NoneFeatureConfiguration)>
static readonly "ICEBERG": $Feature<($BlockStateConfiguration)>
static readonly "FOREST_ROCK": $Feature<($BlockStateConfiguration)>
static readonly "DISK": $Feature<($DiskConfiguration)>
static readonly "LAKE": $Feature<($LakeFeature$Configuration)>
static readonly "ORE": $Feature<($OreConfiguration)>
static readonly "END_SPIKE": $Feature<($SpikeConfiguration)>
static readonly "END_ISLAND": $Feature<($NoneFeatureConfiguration)>
static readonly "END_GATEWAY": $Feature<($EndGatewayConfiguration)>
static readonly "SEAGRASS": $SeagrassFeature
static readonly "KELP": $Feature<($NoneFeatureConfiguration)>
static readonly "CORAL_TREE": $Feature<($NoneFeatureConfiguration)>
static readonly "CORAL_MUSHROOM": $Feature<($NoneFeatureConfiguration)>
static readonly "CORAL_CLAW": $Feature<($NoneFeatureConfiguration)>
static readonly "SEA_PICKLE": $Feature<($CountConfiguration)>
static readonly "SIMPLE_BLOCK": $Feature<($SimpleBlockConfiguration)>
static readonly "BAMBOO": $Feature<($ProbabilityFeatureConfiguration)>
static readonly "HUGE_FUNGUS": $Feature<($HugeFungusConfiguration)>
static readonly "NETHER_FOREST_VEGETATION": $Feature<($NetherForestVegetationConfig)>
static readonly "WEEPING_VINES": $Feature<($NoneFeatureConfiguration)>
static readonly "TWISTING_VINES": $Feature<($TwistingVinesConfig)>
static readonly "BASALT_COLUMNS": $Feature<($ColumnFeatureConfiguration)>
static readonly "DELTA_FEATURE": $Feature<($DeltaFeatureConfiguration)>
static readonly "REPLACE_BLOBS": $Feature<($ReplaceSphereConfiguration)>
static readonly "FILL_LAYER": $Feature<($LayerConfiguration)>
static readonly "BONUS_CHEST": $BonusChestFeature
static readonly "BASALT_PILLAR": $Feature<($NoneFeatureConfiguration)>
static readonly "SCATTERED_ORE": $Feature<($OreConfiguration)>
static readonly "RANDOM_SELECTOR": $Feature<($RandomFeatureConfiguration)>
static readonly "SIMPLE_RANDOM_SELECTOR": $Feature<($SimpleRandomFeatureConfiguration)>
static readonly "RANDOM_BOOLEAN_SELECTOR": $Feature<($RandomBooleanFeatureConfiguration)>
static readonly "GEODE": $Feature<($GeodeConfiguration)>
static readonly "DRIPSTONE_CLUSTER": $Feature<($DripstoneClusterConfiguration)>
static readonly "LARGE_DRIPSTONE": $Feature<($LargeDripstoneConfiguration)>
static readonly "POINTED_DRIPSTONE": $Feature<($PointedDripstoneConfiguration)>
static readonly "SCULK_PATCH": $Feature<($SculkPatchConfiguration)>

constructor(arg0: $Codec$Type<($NoneFeatureConfiguration$Type)>, arg1: $BlockState$Type)

public "place"(arg0: $FeaturePlaceContext$Type<($NoneFeatureConfiguration$Type)>): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneFeature$Type = ($WaystoneFeature);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneFeature_ = $WaystoneFeature$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$LivingFallEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$LivingEntity, $LivingEntity$Type} from "packages/net/minecraft/world/entity/$LivingEntity"

export class $LivingFallEvent extends $BalmEvent {

constructor()
constructor(arg0: $LivingEntity$Type)

public "getFallDamageOverride"(): float
public "setFallDamageOverride"(arg0: float): void
public "getEntity"(): $LivingEntity
public "getListenerList"(): $ListenerList
get "fallDamageOverride"(): float
set "fallDamageOverride"(value: float)
get "entity"(): $LivingEntity
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LivingFallEvent$Type = ($LivingFallEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LivingFallEvent_ = $LivingFallEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/block/entity/$ForgeBalmBlockEntities" {
import {$BalmBlockEntityFactory, $BalmBlockEntityFactory$Type} from "packages/net/blay09/mods/balm/api/block/entity/$BalmBlockEntityFactory"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockEntityType, $BlockEntityType$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntityType"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BalmBlockEntities, $BalmBlockEntities$Type} from "packages/net/blay09/mods/balm/api/block/$BalmBlockEntities"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Block, $Block$Type} from "packages/net/minecraft/world/level/block/$Block"

export class $ForgeBalmBlockEntities implements $BalmBlockEntities {

constructor()

public "registerBlockEntity"<T extends $BlockEntity>(arg0: $ResourceLocation$Type, arg1: $BalmBlockEntityFactory$Type<(T)>, ...arg2: ($DeferredObject$Type<($Block$Type)>)[]): $DeferredObject<($BlockEntityType<(T)>)>
public "registerBlockEntity"<T extends $BlockEntity>(arg0: $ResourceLocation$Type, arg1: $BalmBlockEntityFactory$Type<(T)>, arg2: $Supplier$Type<(($Block$Type)[])>): $DeferredObject<($BlockEntityType<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmBlockEntities$Type = ($ForgeBalmBlockEntities);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmBlockEntities_ = $ForgeBalmBlockEntities$Type;
}}
declare module "packages/net/blay09/mods/waystones/client/render/$SharestoneModel" {
import {$VertexConsumer, $VertexConsumer$Type} from "packages/com/mojang/blaze3d/vertex/$VertexConsumer"
import {$PoseStack, $PoseStack$Type} from "packages/com/mojang/blaze3d/vertex/$PoseStack"
import {$Model, $Model$Type} from "packages/net/minecraft/client/model/$Model"
import {$ModelPart, $ModelPart$Type} from "packages/net/minecraft/client/model/geom/$ModelPart"
import {$LayerDefinition, $LayerDefinition$Type} from "packages/net/minecraft/client/model/geom/builders/$LayerDefinition"
import {$CubeDeformation, $CubeDeformation$Type} from "packages/net/minecraft/client/model/geom/builders/$CubeDeformation"

export class $SharestoneModel extends $Model {

constructor(arg0: $ModelPart$Type)

public static "createLayer"(arg0: $CubeDeformation$Type): $LayerDefinition
public "renderToBuffer"(arg0: $PoseStack$Type, arg1: $VertexConsumer$Type, arg2: integer, arg3: integer, arg4: float, arg5: float, arg6: float, arg7: float): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $SharestoneModel$Type = ($SharestoneModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $SharestoneModel_ = $SharestoneModel$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/item/$ForgeBalmItems" {
import {$Item, $Item$Type} from "packages/net/minecraft/world/item/$Item"
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$CreativeModeTab, $CreativeModeTab$Type} from "packages/net/minecraft/world/item/$CreativeModeTab"
import {$Item$Properties, $Item$Properties$Type} from "packages/net/minecraft/world/item/$Item$Properties"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$BalmItems, $BalmItems$Type} from "packages/net/blay09/mods/balm/api/item/$BalmItems"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ItemLike, $ItemLike$Type} from "packages/net/minecraft/world/level/$ItemLike"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $ForgeBalmItems implements $BalmItems {

constructor()

public "register"(): void
public "registerCreativeModeTab"(arg0: $Supplier$Type<($ItemStack$Type)>, arg1: $ResourceLocation$Type): $DeferredObject<($CreativeModeTab)>
public "addToCreativeModeTab"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<(($ItemLike$Type)[])>): void
public "registerItem"(arg0: $Supplier$Type<($Item$Type)>, arg1: $ResourceLocation$Type, arg2: $ResourceLocation$Type): $DeferredObject<($Item)>
public "itemProperties"(): $Item$Properties
/**
 * 
 * @deprecated
 */
public "registerCreativeModeTab"(arg0: $ResourceLocation$Type, arg1: $Supplier$Type<($ItemStack$Type)>): $DeferredObject<($CreativeModeTab)>
public "registerItem"(arg0: $Supplier$Type<($Item$Type)>, arg1: $ResourceLocation$Type): $DeferredObject<($Item)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmItems$Type = ($ForgeBalmItems);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmItems_ = $ForgeBalmItems$Type;
}}
declare module "packages/net/blay09/mods/balm/api/event/$UseItemEvent" {
import {$ListenerList, $ListenerList$Type} from "packages/net/minecraftforge/eventbus/$ListenerList"
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$InteractionHand, $InteractionHand$Type} from "packages/net/minecraft/world/$InteractionHand"
import {$InteractionResult, $InteractionResult$Type} from "packages/net/minecraft/world/$InteractionResult"
import {$BalmEvent, $BalmEvent$Type} from "packages/net/blay09/mods/balm/api/event/$BalmEvent"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"

export class $UseItemEvent extends $BalmEvent {

constructor(arg0: $Player$Type, arg1: $Level$Type, arg2: $InteractionHand$Type)
constructor()

public "setResult"(arg0: $InteractionResult$Type): void
public "getLevel"(): $Level
public "getInteractionResult"(): $InteractionResult
public "getHand"(): $InteractionHand
public "getPlayer"(): $Player
public "getListenerList"(): $ListenerList
set "result"(value: $InteractionResult$Type)
get "level"(): $Level
get "interactionResult"(): $InteractionResult
get "hand"(): $InteractionHand
get "player"(): $Player
get "listenerList"(): $ListenerList
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $UseItemEvent$Type = ($UseItemEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $UseItemEvent_ = $UseItemEvent$Type;
}}
declare module "packages/net/blay09/mods/balm/api/client/screen/$BalmScreenFactory" {
import {$Component, $Component$Type} from "packages/net/minecraft/network/chat/$Component"
import {$Inventory, $Inventory$Type} from "packages/net/minecraft/world/entity/player/$Inventory"

export interface $BalmScreenFactory<T, S> {

 "create"(arg0: T, arg1: $Inventory$Type, arg2: $Component$Type): S

(arg0: T, arg1: $Inventory$Type, arg2: $Component$Type): S
}

export namespace $BalmScreenFactory {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BalmScreenFactory$Type<T, S> = ($BalmScreenFactory<(T), (S)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BalmScreenFactory_<T, S> = $BalmScreenFactory$Type<(T), (S)>;
}}
declare module "packages/net/blay09/mods/waystones/config/$WaystonesConfigData$Client" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $WaystonesConfigData$Client {
 "disableTextGlow": boolean

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystonesConfigData$Client$Type = ($WaystonesConfigData$Client);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystonesConfigData$Client_ = $WaystonesConfigData$Client$Type;
}}
declare module "packages/net/blay09/mods/craftingtweaks/api/$CraftingTweaksAPI" {
import {$CraftingGridProvider, $CraftingGridProvider$Type} from "packages/net/blay09/mods/craftingtweaks/api/$CraftingGridProvider"

export class $CraftingTweaksAPI {

constructor()

public static "unregisterCraftingGridProvider"(arg0: $CraftingGridProvider$Type): void
public static "registerCraftingGridProvider"(arg0: $CraftingGridProvider$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CraftingTweaksAPI$Type = ($CraftingTweaksAPI);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CraftingTweaksAPI_ = $CraftingTweaksAPI$Type;
}}
declare module "packages/net/blay09/mods/waystones/core/$WaystoneEditPermissions" {
import {$Enum, $Enum$Type} from "packages/java/lang/$Enum"

export class $WaystoneEditPermissions extends $Enum<($WaystoneEditPermissions)> {
static readonly "ALLOW": $WaystoneEditPermissions
static readonly "NOT_CREATIVE": $WaystoneEditPermissions
static readonly "NOT_THE_OWNER": $WaystoneEditPermissions
static readonly "GET_CREATIVE": $WaystoneEditPermissions


public static "values"(): ($WaystoneEditPermissions)[]
public static "valueOf"(arg0: string): $WaystoneEditPermissions
public "getLangKey"(): string
get "langKey"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WaystoneEditPermissions$Type = (("allow") | ("not_creative") | ("get_creative") | ("not_the_owner")) | ($WaystoneEditPermissions);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WaystoneEditPermissions_ = $WaystoneEditPermissions$Type;
}}
declare module "packages/net/blay09/mods/balm/forge/recipe/$ForgeBalmRecipes" {
import {$DeferredObject, $DeferredObject$Type} from "packages/net/blay09/mods/balm/api/$DeferredObject"
import {$RecipeType, $RecipeType$Type} from "packages/net/minecraft/world/item/crafting/$RecipeType"
import {$RecipeSerializer, $RecipeSerializer$Type} from "packages/net/minecraft/world/item/crafting/$RecipeSerializer"
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Recipe, $Recipe$Type} from "packages/net/minecraft/world/item/crafting/$Recipe"
import {$BalmRecipes, $BalmRecipes$Type} from "packages/net/blay09/mods/balm/api/recipe/$BalmRecipes"

export class $ForgeBalmRecipes implements $BalmRecipes {

constructor()

public "registerRecipeType"<T extends $Recipe<(any)>>(arg0: $Supplier$Type<($RecipeType$Type<(T)>)>, arg1: $Supplier$Type<($RecipeSerializer$Type<(T)>)>, arg2: $ResourceLocation$Type): $DeferredObject<($RecipeType<(T)>)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeBalmRecipes$Type = ($ForgeBalmRecipes);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeBalmRecipes_ = $ForgeBalmRecipes$Type;
}}
