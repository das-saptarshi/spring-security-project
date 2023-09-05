package com.saptarshi.das.demoservice.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public @interface IsAdmin {
}
