declare module "packages/com/xiaohunao/createheatjs/event/$registerHeatEvent" {
import {$EventJS, $EventJS$Type} from "packages/dev/latvian/mods/kubejs/event/$EventJS"
import {$HeatData$Builder, $HeatData$Builder$Type} from "packages/com/xiaohunao/createheatjs/$HeatData$Builder"

export class $registerHeatEvent extends $EventJS {

constructor()

public "registerHeat"(arg0: string, arg1: integer, arg2: integer): $HeatData$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $registerHeatEvent$Type = ($registerHeatEvent);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $registerHeatEvent_ = $registerHeatEvent$Type;
}}
declare module "packages/com/xiaohunao/createheatjs/$HeatData" {
import {$TriPredicate, $TriPredicate$Type} from "packages/net/minecraftforge/common/util/$TriPredicate"
import {$HeatCondition, $HeatCondition$Type} from "packages/com/simibubi/create/content/processing/recipe/$HeatCondition"
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$BlazeBurnerBlock$HeatLevel, $BlazeBurnerBlock$HeatLevel$Type} from "packages/com/simibubi/create/content/processing/burner/$BlazeBurnerBlock$HeatLevel"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $HeatData {


public "getName"(): string
public "getHeatSource"(): $Map<($Pair<(string), (string)>), ($TriPredicate<($Level), ($BlockPos), ($BlockState)>)>
public "getHeatSource"(arg0: string): $TriPredicate<($Level), ($BlockPos), ($BlockState)>
public static "getHeatData"(arg0: string): $HeatData
public "addHeatSource"(arg0: string, arg1: string, arg2: $TriPredicate$Type<($Level$Type), ($BlockPos$Type), ($BlockState$Type)>): $HeatData
public "addHeatSource"(arg0: string, arg1: $TriPredicate$Type<($Level$Type), ($BlockPos$Type), ($BlockState$Type)>): $HeatData
public "addHeatSource"(arg0: string, arg1: string): $HeatData
public "addHeatSource"(arg0: string): $HeatData
public static "getHeatDataByPriority"(arg0: integer): $Collection<($HeatData)>
public "getHeatLevel"(): $BlazeBurnerBlock$HeatLevel
public "getColor"(): integer
public "setHeatCondition"(arg0: $HeatCondition$Type): $HeatData
public "setHeatLevel"(arg0: $BlazeBurnerBlock$HeatLevel$Type): $HeatData
public "setHeatSource"(arg0: $Map$Type<($Pair$Type<(string), (string)>), ($TriPredicate$Type<($Level$Type), ($BlockPos$Type), ($BlockState$Type)>)>): $HeatData
public "isJeiTip"(): boolean
public "setJeiTip"(arg0: boolean): $HeatData
public "getHeatCondition"(): $HeatCondition
public "getPrior"(): integer
public "srtJeiTip"(arg0: boolean): $HeatData
get "name"(): string
get "heatSource"(): $Map<($Pair<(string), (string)>), ($TriPredicate<($Level), ($BlockPos), ($BlockState)>)>
get "heatLevel"(): $BlazeBurnerBlock$HeatLevel
get "color"(): integer
set "heatCondition"(value: $HeatCondition$Type)
set "heatLevel"(value: $BlazeBurnerBlock$HeatLevel$Type)
set "heatSource"(value: $Map$Type<($Pair$Type<(string), (string)>), ($TriPredicate$Type<($Level$Type), ($BlockPos$Type), ($BlockState$Type)>)>)
get "jeiTip"(): boolean
set "jeiTip"(value: boolean)
get "heatCondition"(): $HeatCondition
get "prior"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HeatData$Type = ($HeatData);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HeatData_ = $HeatData$Type;
}}
declare module "packages/com/xiaohunao/createheatjs/$KubeJSCreatePlugin" {
import {$KubeJSPlugin, $KubeJSPlugin$Type} from "packages/dev/latvian/mods/kubejs/$KubeJSPlugin"
import {$EventHandler, $EventHandler$Type} from "packages/dev/latvian/mods/kubejs/event/$EventHandler"
import {$EventGroup, $EventGroup$Type} from "packages/dev/latvian/mods/kubejs/event/$EventGroup"

export class $KubeJSCreatePlugin extends $KubeJSPlugin {
static readonly "GROUP": $EventGroup
static readonly "REGISTRY_HEAT": $EventHandler

constructor()

public "initStartup"(): void
public "registerEvents"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $KubeJSCreatePlugin$Type = ($KubeJSCreatePlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $KubeJSCreatePlugin_ = $KubeJSCreatePlugin$Type;
}}
declare module "packages/com/xiaohunao/createheatjs/$CreateHeatJS" {
import {$HeatData, $HeatData$Type} from "packages/com/xiaohunao/createheatjs/$HeatData"
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $CreateHeatJS {
static readonly "MOD_ID": string
static readonly "heatDataMap": $Map<(string), ($HeatData)>

constructor()

public "onFMLCommonSetup"(arg0: $FMLCommonSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreateHeatJS$Type = ($CreateHeatJS);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreateHeatJS_ = $CreateHeatJS$Type;
}}
declare module "packages/com/xiaohunao/createheatjs/util/$BlockHelper" {
import {$HeatCondition, $HeatCondition$Type} from "packages/com/simibubi/create/content/processing/recipe/$HeatCondition"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$IDrawable, $IDrawable$Type} from "packages/mezz/jei/api/gui/drawable/$IDrawable"
import {$GuiGraphics, $GuiGraphics$Type} from "packages/net/minecraft/client/gui/$GuiGraphics"

export class $BlockHelper {

constructor()

public static "parseBlockState"(arg0: string): $BlockState
public static "hetaSourceRender"(arg0: $GuiGraphics$Type, arg1: $IDrawable$Type, arg2: $HeatCondition$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockHelper$Type = ($BlockHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockHelper_ = $BlockHelper$Type;
}}
declare module "packages/com/xiaohunao/createheatjs/$HeatData$Builder" {
import {$TriPredicate, $TriPredicate$Type} from "packages/net/minecraftforge/common/util/$TriPredicate"
import {$HeatData, $HeatData$Type} from "packages/com/xiaohunao/createheatjs/$HeatData"
import {$Level, $Level$Type} from "packages/net/minecraft/world/level/$Level"
import {$BlockState, $BlockState$Type} from "packages/net/minecraft/world/level/block/state/$BlockState"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $HeatData$Builder {

constructor(arg0: string, arg1: integer, arg2: integer)

public "register"(): $HeatData
public "addHeatSource"(arg0: string, arg1: $TriPredicate$Type<($Level$Type), ($BlockPos$Type), ($BlockState$Type)>): $HeatData$Builder
public "addHeatSource"(arg0: string): $HeatData$Builder
public "addHeatSource"(arg0: string, arg1: string, arg2: $TriPredicate$Type<($Level$Type), ($BlockPos$Type), ($BlockState$Type)>): $HeatData$Builder
public "jeiTip"(): $HeatData$Builder
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $HeatData$Builder$Type = ($HeatData$Builder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $HeatData$Builder_ = $HeatData$Builder$Type;
}}
