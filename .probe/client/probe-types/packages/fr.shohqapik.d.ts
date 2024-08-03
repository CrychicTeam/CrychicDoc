declare module "packages/fr/shohqapik/w2pets/$W2PetsMod" {
import {$WaystoneTeleportEvent$Pre, $WaystoneTeleportEvent$Pre$Type} from "packages/net/blay09/mods/waystones/api/$WaystoneTeleportEvent$Pre"

export class $W2PetsMod {
static readonly "MODID": string

constructor()

public "onWaystoneTeleport"(arg0: $WaystoneTeleportEvent$Pre$Type): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $W2PetsMod$Type = ($W2PetsMod);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $W2PetsMod_ = $W2PetsMod$Type;
}}
