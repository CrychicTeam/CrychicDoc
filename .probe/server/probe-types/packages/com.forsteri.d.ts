declare module "packages/com/forsteri/createliquidfuel/eventhandlers/$ModEventHandler" {
import {$FMLCommonSetupEvent, $FMLCommonSetupEvent$Type} from "packages/net/minecraftforge/fml/event/lifecycle/$FMLCommonSetupEvent"

export class $ModEventHandler {

constructor()

public static "commonSetup"(arg0: $FMLCommonSetupEvent$Type): void
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
declare module "packages/com/forsteri/createliquidfuel/$CreateLiquidFuel" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CreateLiquidFuel {
static readonly "MOD_ID": string

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CreateLiquidFuel$Type = ($CreateLiquidFuel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CreateLiquidFuel_ = $CreateLiquidFuel$Type;
}}
declare module "packages/com/forsteri/createliquidfuel/mixin/$BlazeBurnerAccessor" {
import {$BlazeBurnerBlock$HeatLevel, $BlazeBurnerBlock$HeatLevel$Type} from "packages/com/simibubi/create/content/processing/burner/$BlazeBurnerBlock$HeatLevel"

export interface $BlazeBurnerAccessor {

 "createliquidfuel$setRemainingBurnTime"(arg0: integer): void
 "createliquidfuel$invokeSetBlockHeat"(arg0: $BlazeBurnerBlock$HeatLevel$Type): void
 "createliquidfuel$getRemainingBurnTime"(): integer
}

export namespace $BlazeBurnerAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlazeBurnerAccessor$Type = ($BlazeBurnerAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlazeBurnerAccessor_ = $BlazeBurnerAccessor$Type;
}}
declare module "packages/com/forsteri/createliquidfuel/core/$BurnerStomachHandler" {
import {$Fluid, $Fluid$Type} from "packages/net/minecraft/world/level/material/$Fluid"
import {$Triplet, $Triplet$Type} from "packages/com/forsteri/createliquidfuel/util/$Triplet"
import {$SmartBlockEntity, $SmartBlockEntity$Type} from "packages/com/simibubi/create/foundation/blockEntity/$SmartBlockEntity"
import {$Pair, $Pair$Type} from "packages/com/mojang/datafixers/util/$Pair"
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"
import {$Map, $Map$Type} from "packages/java/util/$Map"
import {$CallbackInfoReturnable, $CallbackInfoReturnable$Type} from "packages/org/spongepowered/asm/mixin/injection/callback/$CallbackInfoReturnable"

export class $BurnerStomachHandler {
static "LIQUID_BURNER_FUEL_MAP": $Map<($Fluid), ($Pair<($ResourceLocation), ($Triplet<(integer), (boolean), (integer)>)>)>

constructor()

public static "tick"(arg0: $SmartBlockEntity$Type): void
public static "tryUpdateFuel"(arg0: $SmartBlockEntity$Type, arg1: $ItemStack$Type, arg2: boolean, arg3: boolean, arg4: $CallbackInfoReturnable$Type<(boolean)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BurnerStomachHandler$Type = ($BurnerStomachHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BurnerStomachHandler_ = $BurnerStomachHandler$Type;
}}
declare module "packages/com/forsteri/createliquidfuel/util/$Triplet" {
import {$Record, $Record$Type} from "packages/java/lang/$Record"

export class $Triplet<T, U, V> extends $Record {

constructor(getFirst: T, getSecond: U, getThird: V)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
public static "of"<T, U, V>(arg0: T, arg1: U, arg2: V): $Triplet<(T), (U), (V)>
public "getFirst"(): T
public "getSecond"(): U
public "getThird"(): V
get "first"(): T
get "second"(): U
get "third"(): V
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Triplet$Type<T, U, V> = ($Triplet<(T), (U), (V)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Triplet_<T, U, V> = $Triplet$Type<(T), (U), (V)>;
}}
declare module "packages/com/forsteri/createliquidfuel/core/$DrainableFuelLoader" {
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $DrainableFuelLoader {
static readonly "IDENTIFIER": $ResourceLocation

constructor()

public static "load"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $DrainableFuelLoader$Type = ($DrainableFuelLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $DrainableFuelLoader_ = $DrainableFuelLoader$Type;
}}
declare module "packages/com/forsteri/createliquidfuel/util/$MathUtil" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $MathUtil {

constructor()

public static "gcd"(arg0: integer, arg1: integer): integer
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
declare module "packages/com/forsteri/createliquidfuel/eventhandlers/$ForgeEventsHandler" {
import {$AddReloadListenerEvent, $AddReloadListenerEvent$Type} from "packages/net/minecraftforge/event/$AddReloadListenerEvent"

export class $ForgeEventsHandler {

constructor()

public static "addReloadListeners"(arg0: $AddReloadListenerEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ForgeEventsHandler$Type = ($ForgeEventsHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ForgeEventsHandler_ = $ForgeEventsHandler$Type;
}}
declare module "packages/com/forsteri/createliquidfuel/core/$LiquidBurnerFuelJsonLoader" {
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"
import {$ResourceLocation, $ResourceLocation$Type} from "packages/net/minecraft/resources/$ResourceLocation"

export class $LiquidBurnerFuelJsonLoader extends $SimpleJsonResourceReloadListener {
static readonly "IDENTIFIER": $ResourceLocation
static readonly "INSTANCE": $LiquidBurnerFuelJsonLoader

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LiquidBurnerFuelJsonLoader$Type = ($LiquidBurnerFuelJsonLoader);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LiquidBurnerFuelJsonLoader_ = $LiquidBurnerFuelJsonLoader$Type;
}}
