declare module "packages/com/gpumemleakfix/event/$ClientEventHandler" {
import {$ConcurrentLinkedQueue, $ConcurrentLinkedQueue$Type} from "packages/java/util/concurrent/$ConcurrentLinkedQueue"
import {$Vec3i, $Vec3i$Type} from "packages/net/minecraft/core/$Vec3i"
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"

export class $ClientEventHandler {
static "queue": $ConcurrentLinkedQueue<($Vec3i)>

constructor()

public static "onCLientTick"(arg0: $TickEvent$ClientTickEvent$Type): void
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
declare module "packages/com/gpumemleakfix/$GpumemleakfixClient" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"

export class $GpumemleakfixClient {

constructor()

public static "onInitializeClient"(arg0: $FMLClientSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GpumemleakfixClient$Type = ($GpumemleakfixClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GpumemleakfixClient_ = $GpumemleakfixClient$Type;
}}
declare module "packages/com/gpumemleakfix/$Gpumemleakfix" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"

export class $Gpumemleakfix {
static readonly "MOD_ID": string
static readonly "LOGGER": $Logger

constructor()

public "clientSetup"(arg0: $FMLClientSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Gpumemleakfix$Type = ($Gpumemleakfix);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Gpumemleakfix_ = $Gpumemleakfix$Type;
}}
