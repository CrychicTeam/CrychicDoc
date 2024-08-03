declare module "packages/com/cupboard/util/$BlockSearch" {
import {$BlockGetter, $BlockGetter$Type} from "packages/net/minecraft/world/level/$BlockGetter"
import {$BiPredicate, $BiPredicate$Type} from "packages/java/util/function/$BiPredicate"
import {$BlockPos, $BlockPos$Type} from "packages/net/minecraft/core/$BlockPos"

export class $BlockSearch {

constructor()

public static "findAround"(arg0: $BlockGetter$Type, arg1: $BlockPos$Type, arg2: integer, arg3: integer, arg4: integer, arg5: $BiPredicate$Type<($BlockGetter$Type), ($BlockPos$Type)>): $BlockPos
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockSearch$Type = ($BlockSearch);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockSearch_ = $BlockSearch$Type;
}}
declare module "packages/com/cupboard/$CupboardClient" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"

export class $CupboardClient {

constructor()

public static "onInitializeClient"(arg0: $FMLClientSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CupboardClient$Type = ($CupboardClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CupboardClient_ = $CupboardClient$Type;
}}
declare module "packages/com/cupboard/event/$ModEventHandler" {
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
declare module "packages/com/cupboard/util/$MathUtil" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MathUtil {

constructor()

public static "limitToMinMax"(arg0: integer, arg1: integer, arg2: integer): integer
public static "limitToMin"(arg0: integer, arg1: integer): integer
public static "limitToMin"(arg0: double, arg1: double): double
public static "limitToMax"(arg0: double, arg1: double): double
public static "limitToMax"(arg0: integer, arg1: integer): integer
public static "minMax"(arg0: double, arg1: double, arg2: double): double
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MathUtil$Type = ($MathUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MathUtil_ = $MathUtil$Type;
}}
declare module "packages/com/cupboard/config/$ICommonConfig" {
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"

export interface $ICommonConfig {

 "deserialize"(arg0: $JsonObject$Type): void
 "serialize"(): $JsonObject
}

export namespace $ICommonConfig {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ICommonConfig$Type = ($ICommonConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ICommonConfig_ = $ICommonConfig$Type;
}}
declare module "packages/com/cupboard/$Cupboard" {
import {$FMLClientSetupEvent, $FMLClientSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLClientSetupEvent"
import {$CupboardConfig, $CupboardConfig$Type} from "packages/com/cupboard/config/$CupboardConfig"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$Random, $Random$Type} from "packages/java/util/$Random"
import {$CommonConfiguration, $CommonConfiguration$Type} from "packages/com/cupboard/config/$CommonConfiguration"

export class $Cupboard {
static readonly "MOD_ID": string
static readonly "LOGGER": $Logger
static "config": $CupboardConfig<($CommonConfiguration)>
static "rand": $Random

constructor()

public "clientSetup"(arg0: $FMLClientSetupEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Cupboard$Type = ($Cupboard);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Cupboard_ = $Cupboard$Type;
}}
declare module "packages/com/cupboard/util/$VectorUtil" {
import {$Vec3, $Vec3$Type} from "packages/net/minecraft/world/phys/$Vec3"

export class $VectorUtil {

constructor()

public static "rotateLeft"(arg0: $Vec3$Type): $Vec3
public static "rotateRight"(arg0: $Vec3$Type): $Vec3
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $VectorUtil$Type = ($VectorUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $VectorUtil_ = $VectorUtil$Type;
}}
declare module "packages/com/cupboard/config/$CupboardConfig" {
import {$ICommonConfig, $ICommonConfig$Type} from "packages/com/cupboard/config/$ICommonConfig"

export class $CupboardConfig<C extends $ICommonConfig> {

constructor(arg0: string, arg1: C)

public "equals"(arg0: any): boolean
public "hashCode"(): integer
public "load"(): void
public "load"(arg0: boolean): void
public "save"(): void
public "getCommonConfig"(): C
public static "pollConfigs"(): void
public static "initloadAll"(): void
get "commonConfig"(): C
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CupboardConfig$Type<C> = ($CupboardConfig<(C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CupboardConfig_<C> = $CupboardConfig$Type<(C)>;
}}
declare module "packages/com/cupboard/config/$CommonConfiguration" {
import {$ICommonConfig, $ICommonConfig$Type} from "packages/com/cupboard/config/$ICommonConfig"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"

export class $CommonConfiguration implements $ICommonConfig {
 "showCommandExecutionErrors": boolean
 "debugChunkloadAttempts": boolean
 "logOffthreadEntityAdd": boolean
 "skipErrorOnEntityLoad": boolean
 "forceHeapDumpOnOOM": boolean

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
declare module "packages/com/cupboard/event/$EventHandler" {
import {$ServerStartedEvent, $ServerStartedEvent$Type} from "packages/net/minecraftforge/event/server/$ServerStartedEvent"
import {$TickEvent$ServerTickEvent, $TickEvent$ServerTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ServerTickEvent"

export class $EventHandler {

constructor()

public static "onServerTick"(arg0: $TickEvent$ServerTickEvent$Type): void
public static "serverstart"(arg0: $ServerStartedEvent$Type): void
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
declare module "packages/com/cupboard/event/$ClientEventHandler" {
import {$ScreenEvent$Opening, $ScreenEvent$Opening$Type} from "packages/net/minecraftforge/client/event/$ScreenEvent$Opening"
import {$TickEvent$ClientTickEvent, $TickEvent$ClientTickEvent$Type} from "packages/net/minecraftforge/event/$TickEvent$ClientTickEvent"

export class $ClientEventHandler {

constructor()

public static "onClientTick"(arg0: $TickEvent$ClientTickEvent$Type): void
public static "onClientTick"(arg0: $ScreenEvent$Opening$Type): void
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
