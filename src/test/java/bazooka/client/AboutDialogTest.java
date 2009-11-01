package bazooka.client;

import com.google.gwt.junit.client.GWTTestCase;

public class AboutDialogTest extends GWTTestCase {

  @Override public String getModuleName() {
    return "bazooka.Bazooka";
  }

  public void testAnythingYouWant() {
    assertTrue(true);
  }
/*
It's not possible to use GWTTestCase with UiBinder yet (2.0-ms2 don't fully support HtmlUnit).

com.google.gwt.junit.client.TimeoutException: The browser did not contact the server within 60000ms.
 - 1 client(s) haven't responded back to JUnitShell since the start of the test.
 Actual time elapsed: 60.009 seconds.

	at com.google.gwt.junit.JUnitShell.notDone(JUnitShell.java:800)
	at com.google.gwt.junit.JUnitShell.runTestImpl(JUnitShell.java:989)
	at com.google.gwt.junit.JUnitShell.runTest(JUnitShell.java:436)
	at com.google.gwt.junit.client.GWTTestCase.runTest(GWTTestCase.java:386)
	at com.google.gwt.junit.client.GWTTestCase.run(GWTTestCase.java:269)
	at com.intellij.junit3.JUnit3IdeaTestRunner.doRun(JUnit3IdeaTestRunner.java:108)
	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:60)

Process finished with exit code 255
*/
}