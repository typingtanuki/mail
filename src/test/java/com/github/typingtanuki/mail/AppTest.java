/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.github.typingtanuki.mail;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    @Test public void testAppHasAGreeting() {
        MailReader classUnderTest = new MailReader();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
