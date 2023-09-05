package com.saptarshi.das.demoservice.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
public @interface IsUser {
}
