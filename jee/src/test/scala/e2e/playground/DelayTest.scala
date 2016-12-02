package e2e.playground


class DelayTest extends e2e.E2ETest with e2e.AngularSupport {

  "hidden input" should "be found" in {

    go to URL + "/playground/delay.html"
    click on "checkme"

    waitForAngular()

    textField("findmeifyoucan").value = "I found you!"
  }

}
