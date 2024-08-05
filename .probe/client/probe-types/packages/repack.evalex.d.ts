declare module "packages/repack/evalex/$Expression$Operator" {
import {$BigDecimal, $BigDecimal$Type} from "packages/java/math/$BigDecimal"
import {$Expression, $Expression$Type} from "packages/repack/evalex/$Expression"

export class $Expression$Operator {

constructor(arg0: $Expression$Type, arg1: string, arg2: integer, arg3: boolean)

public "eval"(arg0: $BigDecimal$Type, arg1: $BigDecimal$Type): $BigDecimal
public "getPrecedence"(): integer
public "isLeftAssoc"(): boolean
public "getOper"(): string
get "precedence"(): integer
get "leftAssoc"(): boolean
get "oper"(): string
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Expression$Operator$Type = ($Expression$Operator);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Expression$Operator_ = $Expression$Operator$Type;
}}
declare module "packages/repack/evalex/$Expression$Function" {
import {$List, $List$Type} from "packages/java/util/$List"
import {$BigDecimal, $BigDecimal$Type} from "packages/java/math/$BigDecimal"
import {$Expression, $Expression$Type} from "packages/repack/evalex/$Expression"

export class $Expression$Function {

constructor(arg0: $Expression$Type, arg1: string, arg2: integer)

public "getName"(): string
public "eval"(arg0: $List$Type<($BigDecimal$Type)>): $BigDecimal
public "getNumParams"(): integer
get "name"(): string
get "numParams"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Expression$Function$Type = ($Expression$Function);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Expression$Function_ = $Expression$Function$Type;
}}
declare module "packages/repack/evalex/$Expression" {
import {$Expression$Operator, $Expression$Operator$Type} from "packages/repack/evalex/$Expression$Operator"
import {$Expression$Function, $Expression$Function$Type} from "packages/repack/evalex/$Expression$Function"
import {$BigDecimal, $BigDecimal$Type} from "packages/java/math/$BigDecimal"
import {$RoundingMode, $RoundingMode$Type} from "packages/java/math/$RoundingMode"
import {$Iterator, $Iterator$Type} from "packages/java/util/$Iterator"

export class $Expression {
static readonly "PI": $BigDecimal

constructor(arg0: string)

public "and"(arg0: string, arg1: $BigDecimal$Type): $Expression
public "and"(arg0: string, arg1: string): $Expression
public "setRoundingMode"(arg0: $RoundingMode$Type): $Expression
public "with"(arg0: string, arg1: $BigDecimal$Type): $Expression
public "with"(arg0: string, arg1: string): $Expression
public "eval"(): $BigDecimal
public "addFunction"(arg0: $Expression$Function$Type): $Expression$Function
public "setVariable"(arg0: string, arg1: string): $Expression
public "setVariable"(arg0: string, arg1: $BigDecimal$Type): $Expression
public "setPrecision"(arg0: integer): $Expression
public "toRPN"(): string
public "addOperator"(arg0: $Expression$Operator$Type): $Expression$Operator
public "getExpressionTokenizer"(): $Iterator<(string)>
set "roundingMode"(value: $RoundingMode$Type)
set "precision"(value: integer)
get "expressionTokenizer"(): $Iterator<(string)>
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Expression$Type = ($Expression);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Expression_ = $Expression$Type;
}}
