declare module "packages/org/antlr/v4/runtime/misc/$Triple" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Triple<A, B, C> {
readonly "a": A
readonly "b": B
readonly "c": C

constructor(arg0: A, arg1: B, arg2: C)

public "equals"(arg0: any): boolean
public "toString"(): string
public "hashCode"(): integer
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Triple$Type<A, B, C> = ($Triple<(A), (B), (C)>);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Triple_<A, B, C> = $Triple$Type<(A), (B), (C)>;
}}
