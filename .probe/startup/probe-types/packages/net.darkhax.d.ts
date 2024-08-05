declare module "packages/net/darkhax/attributefix/temp/$RegistryHelper" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export interface $RegistryHelper<T> {

 "get"(arg0: $ResourceLocation$Type): T
 "isRegistered"(arg0: T): boolean
 "getId"(arg0: T): $ResourceLocation
 "exists"(arg0: $ResourceLocation$Type): boolean
 "getValues"(): $Collection<(T)>
}

export namespace $RegistryHelper {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $RegistryHelper$Type<T> = ($RegistryHelper<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $RegistryHelper_<T> = $RegistryHelper$Type<(T)>;
}}
declare module "packages/net/darkhax/attributefix/$AttributeRegistryHelper" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$RegistryHelper, $RegistryHelper$Type} from "packages/net/darkhax/attributefix/temp/$RegistryHelper"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $AttributeRegistryHelper implements $RegistryHelper<($Attribute)> {

constructor()

public "isRegistered"(arg0: $Attribute$Type): boolean
public "getId"(arg0: $Attribute$Type): $ResourceLocation
public "exists"(arg0: $ResourceLocation$Type): boolean
public "getValues"(): $Collection<($Attribute)>
get "values"(): $Collection<($Attribute)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributeRegistryHelper$Type = ($AttributeRegistryHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributeRegistryHelper_ = $AttributeRegistryHelper$Type;
}}
declare module "packages/net/darkhax/attributefix/$AttributeFixForge" {
import {$FMLLoadCompleteEvent, $FMLLoadCompleteEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLLoadCompleteEvent"

export class $AttributeFixForge {

constructor()

public static "onLoadComplete"(arg0: $FMLLoadCompleteEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributeFixForge$Type = ($AttributeFixForge);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributeFixForge_ = $AttributeFixForge$Type;
}}
declare module "packages/net/darkhax/attributefix/mixin/$AccessorRangedAttribute" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $AccessorRangedAttribute {

 "attributefix$setMinValue"(arg0: double): void
 "attributefix$setMaxValue"(arg0: double): void
}

export namespace $AccessorRangedAttribute {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AccessorRangedAttribute$Type = ($AccessorRangedAttribute);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AccessorRangedAttribute_ = $AccessorRangedAttribute$Type;
}}
declare module "packages/net/darkhax/attributefix/config/$AttributeConfig" {
import {$File, $File$Type} from "packages/java/io/$File"
import {$Attribute, $Attribute$Type} from "packages/net/minecraft/world/entity/ai/attributes/$Attribute"
import {$RegistryHelper, $RegistryHelper$Type} from "packages/net/darkhax/attributefix/temp/$RegistryHelper"

export class $AttributeConfig {

constructor()

public static "load"(arg0: $File$Type, arg1: $RegistryHelper$Type<($Attribute$Type)>): $AttributeConfig
public "applyChanges"(arg0: $RegistryHelper$Type<($Attribute$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $AttributeConfig$Type = ($AttributeConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $AttributeConfig_ = $AttributeConfig$Type;
}}
declare module "packages/net/darkhax/attributefix/$Constants" {
import {$Gson, $Gson$Type} from "packages/com/google/gson/$Gson"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$DecimalFormat, $DecimalFormat$Type} from "packages/java/text/$DecimalFormat"

export class $Constants {
static readonly "MOD_ID": string
static readonly "MOD_NAME": string
static readonly "LOG": $Logger
static readonly "GSON": $Gson
static readonly "FORMAT": $DecimalFormat

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Constants$Type = ($Constants);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Constants_ = $Constants$Type;
}}
