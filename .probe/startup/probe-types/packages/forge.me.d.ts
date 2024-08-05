declare module "packages/forge/me/thosea/badoptimizations/interfaces/$EntityTypeMethods" {
import {$EntityRenderer, $EntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRenderer"

export interface $EntityTypeMethods {

 "bo$getRenderer"(): $EntityRenderer<(any)>
 "bo$setRenderer"(arg0: $EntityRenderer$Type<(any)>): void
}

export namespace $EntityTypeMethods {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityTypeMethods$Type = ($EntityTypeMethods);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityTypeMethods_ = $EntityTypeMethods$Type;
}}
declare module "packages/forge/me/thosea/badoptimizations/interfaces/$EntityMethods" {
import {$EntityRenderer, $EntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRenderer"
import {$Entity, $Entity$Type} from "packages/net/minecraft/world/entity/$Entity"

export interface $EntityMethods {

 "bo$refreshEntityData"(arg0: integer): void
 "bo$getRenderer"<T extends $Entity>(): $EntityRenderer<(T)>
}

export namespace $EntityMethods {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $EntityMethods$Type = ($EntityMethods);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $EntityMethods_ = $EntityMethods$Type;
}}
declare module "packages/forge/me/thosea/badoptimizations/other/$CommonColorFactors" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $CommonColorFactors {
static readonly "SKY_COLOR": $CommonColorFactors
static readonly "LIGHTMAP": $CommonColorFactors
static "lastRainGradient": float
static "lastThunderGradient": float
static "lastLightningTicks": integer
static "rainGradientMultiplier": float
static "thunderGradientMultiplier": float


public static "tick"(tickDelta: float): void
public "isDirty"(): boolean
public "didTickChange"(): boolean
public "updateLastTime"(): void
public "getTimeDelta"(): long
get "dirty"(): boolean
get "timeDelta"(): long
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $CommonColorFactors$Type = ($CommonColorFactors);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $CommonColorFactors_ = $CommonColorFactors$Type;
}}
declare module "packages/forge/me/thosea/badoptimizations/$ClientAccessor" {
import {$Minecraft, $Minecraft$Type} from "packages/net/minecraft/client/$Minecraft"

export interface $ClientAccessor {

 "badoptimizations$updateFpsString"(): void

(): void
}

export namespace $ClientAccessor {
function shouldUpdateFpsString(client: $Minecraft$Type): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $ClientAccessor$Type = ($ClientAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $ClientAccessor_ = $ClientAccessor$Type;
}}
declare module "packages/forge/me/thosea/badoptimizations/other/$Config" {
import {$File, $File$Type} from "packages/java/io/$File"
import {$Logger, $Logger$Type} from "packages/org/slf4j/$Logger"

export class $Config {
static readonly "LOGGER": $Logger
static readonly "FILE": $File
static readonly "CONFIG_VER": integer
static "error": string
static "enable_lightmap_caching": boolean
static "lightmap_time_change_needed_for_update": integer
static "enable_sky_color_caching": boolean
static "skycolor_time_change_needed_for_update": integer
static "enable_debug_renderer_disable_if_not_needed": boolean
static "enable_particle_manager_optimization": boolean
static "enable_toast_optimizations": boolean
static "enable_sky_angle_caching_in_worldrenderer": boolean
static "enable_entity_renderer_caching": boolean
static "enable_block_entity_renderer_caching": boolean
static "enable_entity_flag_caching": boolean
static "enable_remove_redundant_fov_calculations": boolean
static "enable_remove_tutorial_if_not_demo": boolean
static "show_f3_text": boolean
static "ignore_mod_incompatibilities": boolean
static "log_config": boolean


public static "load"(): void
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
declare module "packages/forge/me/thosea/badoptimizations/interfaces/$BiomeSkyColorGetter" {
import {$BiomeManager, $BiomeManager$Type} from "packages/net/minecraft/world/level/biome/$BiomeManager"

export interface $BiomeSkyColorGetter {

 "get"(arg0: integer, arg1: integer, arg2: integer): integer

(arg0: integer, arg1: integer, arg2: integer): integer
}

export namespace $BiomeSkyColorGetter {
function of(access: $BiomeManager$Type): $BiomeSkyColorGetter
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BiomeSkyColorGetter$Type = ($BiomeSkyColorGetter);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BiomeSkyColorGetter_ = $BiomeSkyColorGetter$Type;
}}
declare module "packages/forge/me/thosea/badoptimizations/other/$PlatformMethods" {
import {$File, $File$Type} from "packages/java/io/$File"

export class $PlatformMethods {


public static "getVersion"(): string
public static "getConfigFolder"(): $File
public static "isModLoaded"(id: string): boolean
get "version"(): string
get "configFolder"(): $File
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformMethods$Type = ($PlatformMethods);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformMethods_ = $PlatformMethods$Type;
}}
declare module "packages/forge/me/thosea/badoptimizations/other/$PlayerModelRendererHolder" {
import {$EntityRenderer, $EntityRenderer$Type} from "packages/net/minecraft/client/renderer/entity/$EntityRenderer"

export class $PlayerModelRendererHolder {
static "WIDE_RENDERER": $EntityRenderer<(any)>
static "SLIM_RENDERER": $EntityRenderer<(any)>


}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerModelRendererHolder$Type = ($PlayerModelRendererHolder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerModelRendererHolder_ = $PlayerModelRendererHolder$Type;
}}
declare module "packages/forge/me/thosea/badoptimizations/other/forge/$PlatformMethodsImpl" {
import {$File, $File$Type} from "packages/java/io/$File"

export class $PlatformMethodsImpl {


public static "getVersion"(): string
public static "getConfigFolder"(): $File
public static "isModLoaded"(id: string): boolean
get "version"(): string
get "configFolder"(): $File
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlatformMethodsImpl$Type = ($PlatformMethodsImpl);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlatformMethodsImpl_ = $PlatformMethodsImpl$Type;
}}
declare module "packages/forge/me/thosea/badoptimizations/mixin/accessor/$PlayerAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $PlayerAccessor {

 "bo$underwaterVisibilityTicks"(): integer

(): integer
}

export namespace $PlayerAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $PlayerAccessor$Type = ($PlayerAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $PlayerAccessor_ = $PlayerAccessor$Type;
}}
declare module "packages/forge/me/thosea/badoptimizations/interfaces/$BlockEntityTypeMethods" {
import {$BlockEntity, $BlockEntity$Type} from "packages/net/minecraft/world/level/block/entity/$BlockEntity"
import {$BlockEntityRenderer, $BlockEntityRenderer$Type} from "packages/net/minecraft/client/renderer/blockentity/$BlockEntityRenderer"

export interface $BlockEntityTypeMethods {

 "bo$getRenderer"<T extends $BlockEntity>(): $BlockEntityRenderer<(T)>
 "bo$setRenderer"(arg0: $BlockEntityRenderer$Type<(any)>): void
}

export namespace $BlockEntityTypeMethods {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BlockEntityTypeMethods$Type = ($BlockEntityTypeMethods);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BlockEntityTypeMethods_ = $BlockEntityTypeMethods$Type;
}}
declare module "packages/forge/me/thosea/badoptimizations/mixin/accessor/$GameRendererAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $GameRendererAccessor {

 "bo$getSkyDarkness"(): float

(): float
}

export namespace $GameRendererAccessor {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $GameRendererAccessor$Type = ($GameRendererAccessor);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $GameRendererAccessor_ = $GameRendererAccessor$Type;
}}
declare module "packages/forge/me/thosea/badoptimizations/mixin/$BOMixinPlugin" {
import {$IMixinInfo, $IMixinInfo$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinInfo"
import {$ClassNode, $ClassNode$Type} from "packages/org/objectweb/asm/tree/$ClassNode"
import {$IMixinConfigPlugin, $IMixinConfigPlugin$Type} from "packages/org/spongepowered/asm/mixin/extensibility/$IMixinConfigPlugin"
import {$Set, $Set$Type} from "packages/java/util/$Set"
import {$List, $List$Type} from "packages/java/util/$List"

export class $BOMixinPlugin implements $IMixinConfigPlugin {

constructor()

public "onLoad"(mixinPackage: string): void
public "postApply"(targetClassName: string, targetClass: $ClassNode$Type, mixinClassName: string, mixinInfo: $IMixinInfo$Type): void
public "getMixins"(): $List<(string)>
public "getRefMapperConfig"(): string
public "shouldApplyMixin"(targetClassName: string, mixin: string): boolean
public "preApply"(targetClassName: string, targetClass: $ClassNode$Type, mixinClassName: string, mixinInfo: $IMixinInfo$Type): void
public "acceptTargets"(myTargets: $Set$Type<(string)>, otherTargets: $Set$Type<(string)>): void
get "mixins"(): $List<(string)>
get "refMapperConfig"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $BOMixinPlugin$Type = ($BOMixinPlugin);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $BOMixinPlugin_ = $BOMixinPlugin$Type;
}}
