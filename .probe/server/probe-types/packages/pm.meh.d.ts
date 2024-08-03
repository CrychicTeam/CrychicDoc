declare module "packages/pm/meh/icterine/forge/$ReloadListenerHandler" {
import {$AddReloadListenerEvent, $AddReloadListenerEvent$Type} from "packages/net/minecraftforge/event/$AddReloadListenerEvent"

export class $ReloadListenerHandler {

constructor()

public static "addReloadListeners"(arg0: $AddReloadListenerEvent$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReloadListenerHandler$Type = ($ReloadListenerHandler);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReloadListenerHandler_ = $ReloadListenerHandler$Type;
}}
declare module "packages/pm/meh/icterine/impl/$ReloadListenerHandlerBase" {
import {$SimpleJsonResourceReloadListener, $SimpleJsonResourceReloadListener$Type} from "packages/net/minecraft/server/packs/resources/$SimpleJsonResourceReloadListener"

export class $ReloadListenerHandlerBase extends $SimpleJsonResourceReloadListener {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ReloadListenerHandlerBase$Type = ($ReloadListenerHandlerBase);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ReloadListenerHandlerBase_ = $ReloadListenerHandlerBase$Type;
}}
declare module "packages/pm/meh/icterine/util/$ItemStackUtil" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $ItemStackUtil {

constructor()

public static "processItemStackInTriggerSlotListeners"(arg0: $ItemStack$Type, arg1: $ItemStack$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ItemStackUtil$Type = ($ItemStackUtil);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ItemStackUtil_ = $ItemStackUtil$Type;
}}
declare module "packages/pm/meh/icterine/util/$LogHelper" {
import {$Supplier, $Supplier$Type} from "packages/java/util/function/$Supplier"

export class $LogHelper {

constructor()

public static "debug"(arg0: $Supplier$Type<(string)>): void
public static "debug"(arg0: string): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LogHelper$Type = ($LogHelper);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LogHelper_ = $LogHelper$Type;
}}
declare module "packages/pm/meh/icterine/$Common" {
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"
import {$Config, $Config$Type} from "packages/pm/meh/icterine/util/$Config"

export class $Common {
static readonly "MOD_ID": string
static readonly "MOD_NAME": string
static readonly "LOG": $Logger
static readonly "config": $Config

constructor()

public static "init"(): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Common$Type = ($Common);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Common_ = $Common$Type;
}}
declare module "packages/pm/meh/icterine/impl/$StackSizeThresholdManager" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export class $StackSizeThresholdManager {

constructor()

public static "add"(arg0: integer): void
public static "clear"(): void
public static "debugPrint"(): void
public static "doesStackPassThreshold"(arg0: $ItemStack$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $StackSizeThresholdManager$Type = ($StackSizeThresholdManager);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $StackSizeThresholdManager_ = $StackSizeThresholdManager$Type;
}}
declare module "packages/pm/meh/icterine/util/$Config" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Config {
readonly "DEBUG_MODE": boolean
readonly "IGNORE_TRIGGERS_FOR_EMPTIED_STACKS": boolean
readonly "IGNORE_TRIGGERS_FOR_DECREASED_STACKS": boolean
readonly "OPTIMIZE_MULTIPLE_PREDICATE_TRIGGER": boolean
readonly "INITIALIZE_INVENTORY_LAST_SLOTS": boolean
readonly "OPTIMIZE_TRIGGERS_FOR_INCREASED_STACKS": boolean
readonly "CHECK_COUNT_BEFORE_ITEM_PREDICATE_MATCH": boolean

constructor()

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
declare module "packages/pm/meh/icterine/util/$MixinPlugin" {
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
declare module "packages/pm/meh/icterine/iface/$IItemStackMixin" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IItemStackMixin {

 "icterine$setPreviousStackSize"(arg0: integer): void
 "icterine$getPreviousStackSize"(): integer
}

export namespace $IItemStackMixin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IItemStackMixin$Type = ($IItemStackMixin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IItemStackMixin_ = $IItemStackMixin$Type;
}}
declare module "packages/pm/meh/icterine/iface/$IItemPredicateMixin" {
import {$ItemStack, $ItemStack$Type} from "packages/net/minecraft/world/item/$ItemStack"

export interface $IItemPredicateMixin {

 "icterine$fasterMatches"(arg0: $ItemStack$Type): boolean

(arg0: $ItemStack$Type): boolean
}

export namespace $IItemPredicateMixin {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IItemPredicateMixin$Type = ($IItemPredicateMixin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IItemPredicateMixin_ = $IItemPredicateMixin$Type;
}}
declare module "packages/pm/meh/icterine/$Icterine" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Icterine {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Icterine$Type = ($Icterine);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Icterine_ = $Icterine$Type;
}}
