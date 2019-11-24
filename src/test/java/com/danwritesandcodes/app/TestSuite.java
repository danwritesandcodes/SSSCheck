package com.danwritesandcodes.app;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestReadPDFHTMLText.class,
        TestCommandChecker.class,
        TestStyleChecker.class,
        TestSpellChecker.class,
})

/**
 * TestSuite runs all tests specified in @Suite.SuiteClasses and
 * is handy for running a set of tests in the IDE.
 *
 * This class is excluded during Maven testing with the following
 * configuration:
 */

 // <plugin>
 //   <artifactId>maven-surefire-plugin</artifactId>
 //   <version>2.22.1</version>
 //   <configuration>
 //     <excludes>
 //       <exclude>**/TestSuite.java</exclude>
 //     </excludes>
 //   </configuration>
 // </plugin>

public class TestSuite {
}
