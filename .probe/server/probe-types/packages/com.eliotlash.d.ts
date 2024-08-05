declare module "packages/com/eliotlash/mclib/math/$IValue" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export interface $IValue {

 "get"(): double

(): double
}

export namespace $IValue {
const probejs$$marker: never
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $IValue$Type = ($IValue);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $IValue_ = $IValue$Type;
}}
declare module "packages/com/eliotlash/mclib/math/$Variable" {
import {$IValue, $IValue$Type} from "packages/com/eliotlash/mclib/math/$IValue"

export class $Variable implements $IValue {

constructor(arg0: string, arg1: double)

public "getName"(): string
public "get"(): double
public "toString"(): string
public "set"(arg0: double): void
get "name"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Variable$Type = ($Variable);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Variable_ = $Variable$Type;
}}
declare module "packages/com/eliotlash/mclib/math/functions/$Function" {
import {$IValue, $IValue$Type} from "packages/com/eliotlash/mclib/math/$IValue"

export class $Function implements $IValue {

constructor(arg0: ($IValue$Type)[], arg1: string)

public "getName"(): string
public "toString"(): string
public "getArg"(arg0: integer): double
public "getRequiredArguments"(): integer
public "get"(): double
get "name"(): string
get "requiredArguments"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Function$Type = ($Function);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Function_ = $Function$Type;
}}
declare module "packages/com/eliotlash/mclib/math/$MathBuilder" {
import {$Class, $Class$Type} from "packages/java/lang/$Class"
import {$List, $List$Type} from "packages/java/util/$List"
import {$Variable, $Variable$Type} from "packages/com/eliotlash/mclib/math/$Variable"
import {$IValue, $IValue$Type} from "packages/com/eliotlash/mclib/math/$IValue"
import {$Map, $Map$Type} from "packages/java/util/$Map"

export class $MathBuilder {
 "variables": $Map<(string), ($Variable)>
 "functions": $Map<(string), ($Class<(any)>)>

constructor()

public "register"(arg0: $Variable$Type): void
public "parse"(arg0: string): $IValue
public "breakdown"(arg0: string): (string)[]
public "breakdownChars"(arg0: (string)[]): $List<(any)>
public "parseSymbols"(arg0: $List$Type<(any)>): $IValue
public "valueFromObject"(arg0: any): $IValue
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $MathBuilder$Type = ($MathBuilder);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $MathBuilder_ = $MathBuilder$Type;
}}
