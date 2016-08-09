package e2e;

import org.junit.Test;

public class SampleE2ETest extends E2ETestBase {
  @Test public void testBar()
  {
    driver.get(root() + "/markdown/");
  }
}
