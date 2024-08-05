declare module "packages/com/fastasyncworldsave/config/$CommonConfiguration" {
import {$ICommonConfig, $ICommonConfig$Type} from "packages/com/cupboard/config/$ICommonConfig"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"

export class $CommonConfiguration implements $ICommonConfig {
 "skipWeatherOnSleep": boolean

constructor()

public "deserialize"(arg0: $JsonObject$Type): void
public "serialize"(): $JsonObject
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonConfiguration$Type = ($CommonConfiguration);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonConfiguration_ = $CommonConfiguration$Type;
}}
declare module "packages/com/fastasyncworldsave/event/$ClientEventHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ClientEventHandler {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientEventHandler$Type = ($ClientEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientEventHandler_ = $ClientEventHandler$Type;
}}
declare module "packages/com/fastasyncworldsave/event/$EventHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $EventHandler {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EventHandler$Type = ($EventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EventHandler_ = $EventHandler$Type;
}}
declare module "packages/com/fastasyncworldsave/event/$ModEventHandler" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ModEventHandler {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ModEventHandler$Type = ($ModEventHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ModEventHandler_ = $ModEventHandler$Type;
}}
declare module "packages/com/fastasyncworldsave/$FastAsyncWorldSave" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$Random, $Random$Type} from "packages/java/util/$Random"

export class $FastAsyncWorldSave {
static readonly "MOD_ID": string
static readonly "LOGGER": $Logger
static "rand": $Random

constructor()

public "clientSetup"(arg0: $FMLClientSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastAsyncWorldSave$Type = ($FastAsyncWorldSave);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastAsyncWorldSave_ = $FastAsyncWorldSave$Type;
}}
declare module "packages/com/fastasyncworldsave/$FastAsyncWorldSaveClient" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"

export class $FastAsyncWorldSaveClient {

constructor()

public static "onInitializeClient"(arg0: $FMLClientSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $FastAsyncWorldSaveClient$Type = ($FastAsyncWorldSaveClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $FastAsyncWorldSaveClient_ = $FastAsyncWorldSaveClient$Type;
}}
