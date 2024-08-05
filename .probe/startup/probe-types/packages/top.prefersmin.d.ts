declare module "packages/top/prefersmin/waystoneandlightman/config/$ModConfig" {
import {$ModConfigEvent, $ModConfigEvent$Type} from "packages/net/minecraftforge/fml/event/config/$ModConfigEvent"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"

export class $ModConfig {
static readonly "SPEC": $ForgeConfigSpec
static "isEnableMoneyCost": boolean
static "moneyCostPerHundredMeter": integer
static "roundUp": boolean
static "minimumCost": integer
static "maximumCost": integer
static "enableConsoleLog": boolean
static "enableCostTip": boolean
static "forceEnableChineseLanguage": boolean

constructor()

public static "onLoad"(arg0: $ModConfigEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModConfig$Type = ($ModConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModConfig_ = $ModConfig$Type;
}}
declare module "packages/top/prefersmin/waystoneandlightman/vo/$TeleportCostVo" {
import {$MoneyValue, $MoneyValue$Type} from "packages/io/github/lightman314/lightmanscurrency/api/money/value/$MoneyValue"

export class $TeleportCostVo {
 "canAfford": boolean
 "cost": $MoneyValue

constructor()

public "free"(): $TeleportCostVo
public "cost"(arg0: $MoneyValue$Type): $TeleportCostVo
public "canAfford"(arg0: boolean): $TeleportCostVo
public "isCanAfford"(): boolean
public "getCost"(): $MoneyValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TeleportCostVo$Type = ($TeleportCostVo);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TeleportCostVo_ = $TeleportCostVo$Type;
}}
declare module "packages/top/prefersmin/waystoneandlightman/handler/$TeleportHandler" {
import {$WaystoneTeleportEvent$Pre, $WaystoneTeleportEvent$Pre$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneTeleportEvent$Pre"

export class $TeleportHandler {

constructor()

public "onWayStoneTeleport"(arg0: $WaystoneTeleportEvent$Pre$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TeleportHandler$Type = ($TeleportHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TeleportHandler_ = $TeleportHandler$Type;
}}
declare module "packages/top/prefersmin/waystoneandlightman/$WayStoneAndLightMan" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $WayStoneAndLightMan {
static readonly "MODID": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $WayStoneAndLightMan$Type = ($WayStoneAndLightMan);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $WayStoneAndLightMan_ = $WayStoneAndLightMan$Type;
}}
declare module "packages/top/prefersmin/waystoneandlightman/mixin/$MixinPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $MixinPlugin implements $IMixinConfigPlugin {

constructor()

public "onLoad"(arg0: string): void
public "postApply"(arg0: string, arg1: $ClassNode$Type, arg2: string, arg3: $IMixinInfo$Type): void
public "getMixins"(): $List<(string)>
public "getRefMapperConfig"(): string
public "shouldApplyMixin"(arg0: string, arg1: string): boolean
public "preApply"(arg0: string, arg1: $ClassNode$Type, arg2: string, arg3: $IMixinInfo$Type): void
public "acceptTargets"(arg0: $Set$Type<(string)>, arg1: $Set$Type<(string)>): void
get "mixins"(): $List<(string)>
get "refMapperConfig"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MixinPlugin$Type = ($MixinPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MixinPlugin_ = $MixinPlugin$Type;
}}
declare module "packages/top/prefersmin/waystoneandlightman/util/$CostUtil" {
import {$Player, $Player$Type} from "packages/net/minecraft/world/entity/player/$Player"
import {$TeleportCostVo, $TeleportCostVo$Type} from "packages/top/prefersmin/waystoneandlightman/vo/$TeleportCostVo"

export class $CostUtil {

constructor()

public static "TeleportCostCalculate"(arg0: $Player$Type, arg1: integer): $TeleportCostVo
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CostUtil$Type = ($CostUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CostUtil_ = $CostUtil$Type;
}}
