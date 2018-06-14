package com.mperminov.saythesame.base.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

@Scope
//Identifies scope annotations. A scope annotation applies to a class containing an injectable
// constructor and governs how the injector reuses instances of the type. By default, if no scope
// annotation is present, the injector creates an instance (by injecting the type's constructor),
// uses the instance for one injection, and then forgets it. If a scope annotation is present, the
// injector may retain the instance for possible reuse in a later injection. If multiple threads
// can access a scoped instance, its implementation should be thread safe. The implementation
// of the scope itself is left up to the injector.
@Retention(RetentionPolicy.RUNTIME)//annotation specifies how the marked annotation is stored
//RetentionPolicy.RUNTIME – The marked annotation is
//retained by the JVM so it can be used by the runtime environment.
public @interface UserScope {
}
