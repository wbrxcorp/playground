package e2e.playground

class SampleScalaE2ETest extends e2e.E2ETest {
  "form" should "be posted properly" in {
    go to URL
    println(pageTitle)
  }

  "form" should "be posted properly2" in {
    go to URL + "/playground/"
    println(pageTitle)
  }
}


class SampleScalaE2ETest2 extends e2e.E2ETest {
  "form" should "be posted properly" in {
    go to URL
    println(pageTitle)
  }

  "form" should "be posted properly2" in {
    go to URL + "/playground/"
    println(pageTitle)
  }
}
