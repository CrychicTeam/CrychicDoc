declare module "packages/com/llamalad7/mixinextras/sugar/ref/$LocalFloatRef" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LocalFloatRef {

 "get"(): float
 "set"(arg0: float): void
}

export namespace $LocalFloatRef {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalFloatRef$Type = ($LocalFloatRef);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalFloatRef_ = $LocalFloatRef$Type;
}}
declare module "packages/com/llamalad7/mixinextras/sugar/ref/$LocalBooleanRef" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LocalBooleanRef {

 "get"(): boolean
 "set"(arg0: boolean): void
}

export namespace $LocalBooleanRef {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalBooleanRef$Type = ($LocalBooleanRef);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalBooleanRef_ = $LocalBooleanRef$Type;
}}
declare module "packages/com/llamalad7/mixinextras/sugar/ref/$LocalIntRef" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LocalIntRef {

 "get"(): integer
 "set"(arg0: integer): void
}

export namespace $LocalIntRef {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalIntRef$Type = ($LocalIntRef);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalIntRef_ = $LocalIntRef$Type;
}}
declare module "packages/com/llamalad7/mixinextras/injector/wrapoperation/$Operation" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $Operation<R> {

 "call"(...arg0: (any)[]): R

(...arg0: (any)[]): R
}

export namespace $Operation {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Operation$Type<R> = ($Operation<(R)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Operation_<R> = $Operation$Type<(R)>;
}}
declare module "packages/com/llamalad7/mixinextras/sugar/ref/$LocalRef" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $LocalRef<T> {

 "get"(): T
 "set"(arg0: T): void
}

export namespace $LocalRef {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $LocalRef$Type<T> = ($LocalRef<(T)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $LocalRef_<T> = $LocalRef$Type<(T)>;
}}
