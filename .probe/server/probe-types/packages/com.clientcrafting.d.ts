declare module "packages/com/clientcrafting/$ClientCraftingMod" {
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$Random, $Random$Type} from "packages/java/util/$Random"

export class $ClientCraftingMod {
static readonly "MOD_ID": string
static readonly "LOGGER": $Logger
static "rand": $Random

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientCraftingMod$Type = ($ClientCraftingMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientCraftingMod_ = $ClientCraftingMod$Type;
}}
declare module "packages/com/clientcrafting/$ClientCraftingClient" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $ClientCraftingClient {

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientCraftingClient$Type = ($ClientCraftingClient);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientCraftingClient_ = $ClientCraftingClient$Type;
}}
