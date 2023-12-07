/*
 * The Open Geospatial Consortium licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 */
package com.occamlab.te.realm;

import java.security.Principal;
import java.util.Collections;

import static com.occamlab.te.realm.PasswordStorage.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.catalina.realm.GenericPrincipal;
import org.junit.BeforeClass;
import org.junit.Test;

import com.occamlab.te.realm.PasswordStorage.CannotPerformOperationException;

public class VerifyPBKDF2Realm {

    private static final String USERNAME = "alpha";
    private static GenericPrincipal principal;

    @BeforeClass
    public static void initPrincipal() {
        principal = new GenericPrincipal(USERNAME, Collections.singletonList("user"));
    }

    @Test
    public void correctPassword() throws CannotPerformOperationException {
        PBKDF2Realm iut = new PBKDF2Realm();
        PBKDF2Realm realmSpy = spy(iut);
        doReturn(principal).when(realmSpy).getPrincipal(USERNAME);
        Principal thePrincipal = realmSpy.authenticate(USERNAME, "correct");
        assertNotNull("Expected authentication to succeed.", thePrincipal);
    }

    @Test
    public void incorrectPassword() throws CannotPerformOperationException {
        PBKDF2Realm iut = new PBKDF2Realm();
        PBKDF2Realm realmSpy = spy(iut);
        doReturn(principal).when(realmSpy).getPrincipal(USERNAME);
        Principal thePrincipal = realmSpy.authenticate(USERNAME, "incorrect");
        assertNull("Expected authentication failure.", thePrincipal);
    }

    @Test
    public void invalidHash() {
        Principal other = new GenericPrincipal(USERNAME, Collections.singletonList("user"));
        PBKDF2Realm iut = new PBKDF2Realm();
        PBKDF2Realm realmSpy = spy(iut);
        doReturn(other).when(realmSpy).getPrincipal(USERNAME);
        Principal thePrincipal = realmSpy.authenticate(USERNAME, "correct");
        assertNull("Expected authentication failure.", thePrincipal);
    }
}
