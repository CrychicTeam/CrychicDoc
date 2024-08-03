declare module "packages/committee/nova/opack2reload/forge/api/$IPackSelectionScreen" {
import {$Button, $Button$Type} from "packages/net/minecraft/client/gui/components/$Button"

export interface $IPackSelectionScreen {

 "getCancelButton"(): $Button

(): $Button
}

export namespace $IPackSelectionScreen {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPackSelectionScreen$Type = ($IPackSelectionScreen);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPackSelectionScreen_ = $IPackSelectionScreen$Type;
}}
declare module "packages/committee/nova/opack2reload/forge/api/$IPackSelectionModel" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IPackSelectionModel {

 "cancel"(): void

(): void
}

export namespace $IPackSelectionModel {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IPackSelectionModel$Type = ($IPackSelectionModel);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IPackSelectionModel_ = $IPackSelectionModel$Type;
}}
declare module "packages/committee/nova/opack2reload/forge/mixin/$InvokerPackRepository" {
import {$Collection, $Collection$Type} from "packages/java/util/$Collection"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Pack, $Pack$Type} from "packages/net/minecraft/server/packs/repository/$Pack"

export interface $InvokerPackRepository {

 "callRebuildSelected"(arg0: $Collection$Type<(string)>): $List<($Pack)>

(arg0: $Collection$Type<(string)>): $List<($Pack)>
}

export namespace $InvokerPackRepository {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $InvokerPackRepository$Type = ($InvokerPackRepository);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $InvokerPackRepository_ = $InvokerPackRepository$Type;
}}
