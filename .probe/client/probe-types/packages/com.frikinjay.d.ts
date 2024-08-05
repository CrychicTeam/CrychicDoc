declare module "packages/com/frikinjay/lmd/config/$LetMeDespawnConfig" {
import {$Set, $Set$Type} from "packages/java/util/$Set"

export class $LetMeDespawnConfig {

constructor()

public static "load"(): $LetMeDespawnConfig
public "save"(): void
public "addMobName"(arg0: string): void
public "removeMobName"(arg0: string): void
public "getMobNames"(): $Set<(string)>
public static "createDefaultConfig"(): void
get "mobNames"(): $Set<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LetMeDespawnConfig$Type = ($LetMeDespawnConfig);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LetMeDespawnConfig_ = $LetMeDespawnConfig$Type;
}}
declare module "packages/com/frikinjay/lmd/$LetMeDespawn" {
import {$LetMeDespawnConfig, $LetMeDespawnConfig$Type} from "packages/com/frikinjay/lmd/config/$LetMeDespawnConfig"
import {$File, $File$Type} from "packages/java/io/$File"
import {$Logger, $Logger$Type} from "packages/java/util/logging/$Logger"

export class $LetMeDespawn {
static readonly "MOD_ID": string
static "logger": $Logger
static readonly "CONFIG_FILE": $File
static "config": $LetMeDespawnConfig

constructor()

}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LetMeDespawn$Type = ($LetMeDespawn);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LetMeDespawn_ = $LetMeDespawn$Type;
}}
declare module "packages/com/frikinjay/lmd/command/$LetMeDespawnCommands" {
import {$CommandSourceStack, $CommandSourceStack$Type} from "packages/net/minecraft/commands/$CommandSourceStack"
import {$CommandDispatcher, $CommandDispatcher$Type} from "packages/com/mojang/brigadier/$CommandDispatcher"

export class $LetMeDespawnCommands {

constructor()

public static "register"(arg0: $CommandDispatcher$Type<($CommandSourceStack$Type)>): void
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LetMeDespawnCommands$Type = ($LetMeDespawnCommands);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LetMeDespawnCommands_ = $LetMeDespawnCommands$Type;
}}
