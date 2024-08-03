declare module "packages/com/snowshock35/jeiintegration/config/$Config$Client" {
import {$ForgeConfigSpec$ConfigValue, $ForgeConfigSpec$ConfigValue$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec$ConfigValue"

export class $Config$Client {
readonly "burnTimeTooltipMode": $ForgeConfigSpec$ConfigValue<(string)>
readonly "durabilityTooltipMode": $ForgeConfigSpec$ConfigValue<(string)>
readonly "enchantabilityTooltipMode": $ForgeConfigSpec$ConfigValue<(string)>
readonly "foodTooltipMode": $ForgeConfigSpec$ConfigValue<(string)>
readonly "maxStackSizeTooltipMode": $ForgeConfigSpec$ConfigValue<(string)>
readonly "nbtTooltipMode": $ForgeConfigSpec$ConfigValue<(string)>
readonly "registryNameTooltipMode": $ForgeConfigSpec$ConfigValue<(string)>
readonly "tagsTooltipMode": $ForgeConfigSpec$ConfigValue<(string)>
readonly "translationKeyTooltipMode": $ForgeConfigSpec$ConfigValue<(string)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Config$Client$Type = ($Config$Client);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Config$Client_ = $Config$Client$Type;
}}
declare module "packages/com/snowshock35/jeiintegration/$TooltipEventHandler" {
import {$ItemTooltipEvent, $ItemTooltipEvent$Type} from "packages/net/minecraftforge/event/entity/player/$ItemTooltipEvent"

export class $TooltipEventHandler {

constructor()

public "onItemTooltip"(arg0: $ItemTooltipEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $TooltipEventHandler$Type = ($TooltipEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $TooltipEventHandler_ = $TooltipEventHandler$Type;
}}
declare module "packages/com/snowshock35/jeiintegration/$JEIIntegration" {
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $JEIIntegration {
static readonly "MOD_ID": string
static "logger": $Logger

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $JEIIntegration$Type = ($JEIIntegration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $JEIIntegration_ = $JEIIntegration$Type;
}}
declare module "packages/com/snowshock35/jeiintegration/config/$Config" {
import {$ModConfigEvent$Reloading, $ModConfigEvent$Reloading$Type} from "packages/net/minecraftforge/fml/event/config/$ModConfigEvent$Reloading"
import {$ForgeConfigSpec, $ForgeConfigSpec$Type} from "packages/net/minecraftforge/common/$ForgeConfigSpec"
import {$Config$Client, $Config$Client$Type} from "packages/com/snowshock35/jeiintegration/config/$Config$Client"
import {$ModConfigEvent$Loading, $ModConfigEvent$Loading$Type} from "packages/net/minecraftforge/fml/event/config/$ModConfigEvent$Loading"

export class $Config {
static readonly "clientSpec": $ForgeConfigSpec
static readonly "CLIENT": $Config$Client

constructor()

public static "onLoad"(arg0: $ModConfigEvent$Loading$Type): void
public static "onFileChange"(arg0: $ModConfigEvent$Reloading$Type): void
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
