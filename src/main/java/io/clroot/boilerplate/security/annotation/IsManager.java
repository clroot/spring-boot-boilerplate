package io.clroot.boilerplate.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ADMIN')")
public @interface IsManager {
}
