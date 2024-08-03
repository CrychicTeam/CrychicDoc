declare module "packages/jdk/internal/event/$Event" {
export {} // Mark the file as a module, do not remove unless there are other import/exports!
export class $Event {


public "end"(): void
public "begin"(): void
public "set"(arg0: integer, arg1: any): void
public "commit"(): void
public "isEnabled"(): boolean
public "shouldCommit"(): boolean
get "enabled"(): boolean
}
/**
 * Class-specific type exported by ProbeJS, use global Type_
 * types for convenience unless there's a naming conflict.
 */
export type $Event$Type = ($Event);
/**
 * Global type exported for convenience, use class-specific
 * types if there's a naming conflict.
 */
declare global {
export type $Event_ = $Event$Type;
}}
