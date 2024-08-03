declare module "packages/fabric/me/thosea/badoptimizations/other/fabric/$PlatformMethodsImpl" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PlatformMethodsImpl {


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
declare module "packages/fabric/me/thosea/badoptimizations/interfaces/$EntityMethods" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $EntityMethods {

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
declare module "packages/fabric/me/thosea/badoptimizations/$ClientAccessor" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $ClientAccessor {

}

export namespace $ClientAccessor {
const probejs$$marker: never
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
declare module "packages/fabric/me/thosea/badoptimizations/mixin/$BOMixinPlugin" {
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
declare module "packages/fabric/me/thosea/badoptimizations/interfaces/$BlockEntityTypeMethods" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $BlockEntityTypeMethods {

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
declare module "packages/fabric/me/thosea/badoptimizations/interfaces/$BiomeSkyColorGetter" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $BiomeSkyColorGetter {

}

export namespace $BiomeSkyColorGetter {
const probejs$$marker: never
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
declare module "packages/fabric/me/thosea/badoptimizations/interfaces/$EntityTypeMethods" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $EntityTypeMethods {

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
declare module "packages/fabric/me/thosea/badoptimizations/other/$PlayerModelRendererHolder" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $PlayerModelRendererHolder {


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
declare module "packages/fabric/me/thosea/badoptimizations/other/$PlatformMethods" {
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
