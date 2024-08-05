declare module "packages/com/chunksending/$IChunksendingPlayer" {
import {$ChunkPos, $ChunkPos$Type} from "packages/net/minecraft/world/level/$ChunkPos"
import {$Packet, $Packet$Type} from "packages/net/minecraft/network/protocol/$Packet"

export interface $IChunksendingPlayer {

 "attachToPending"(arg0: $ChunkPos$Type, arg1: $Packet$Type<(any)>): boolean

(arg0: $ChunkPos$Type, arg1: $Packet$Type<(any)>): boolean
}

export namespace $IChunksendingPlayer {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IChunksendingPlayer$Type = ($IChunksendingPlayer);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IChunksendingPlayer_ = $IChunksendingPlayer$Type;
}}
declare module "packages/com/chunksending/$ChunkSending" {
import {$CommonConfiguration, $CommonConfiguration$Type} from "packages/com/chunksending/config/$CommonConfiguration"
import {$CupboardConfig, $CupboardConfig$Type} from "packages/com/cupboard/config/$CupboardConfig"
import {$Logger, $Logger$Type} from "packages/org/apache/logging/log4j/$Logger"
import {$Random, $Random$Type} from "packages/java/util/$Random"

export class $ChunkSending {
static readonly "MODID": string
static readonly "LOGGER": $Logger
static "config": $CupboardConfig<($CommonConfiguration)>
static "rand": $Random

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ChunkSending$Type = ($ChunkSending);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ChunkSending_ = $ChunkSending$Type;
}}
declare module "packages/com/chunksending/config/$CommonConfiguration" {
import {$ICommonConfig, $ICommonConfig$Type} from "packages/com/cupboard/config/$ICommonConfig"
import {$JsonObject, $JsonObject$Type} from "packages/com/google/gson/$JsonObject"

export class $CommonConfiguration implements $ICommonConfig {
 "maxChunksPerTick": integer
 "debugLogging": boolean

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
