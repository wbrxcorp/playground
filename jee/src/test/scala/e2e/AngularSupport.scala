package e2e

import org.openqa.selenium.support.ui.{WebDriverWait,ExpectedCondition}
import org.openqa.selenium.WebDriver
import org.openqa.selenium.JavascriptExecutor

trait AngularSupport {
  // http://stackoverflow.com/questions/25062969/testing-angularjs-with-selenium
  val waitForAngularScript = """try {
  if (document.readyState !== 'complete') {
    return false; // Page not loaded yet
  }
  if (window.jQuery) {
    if (window.jQuery.active) {
      return false;
    } else if (window.jQuery.ajax && window.jQuery.ajax.active) {
      return false;
    }
  }
  if (window.angular) {
    if (!window.qa) {
      // Used to track the render cycle finish after loading is complete
      window.qa = {
        doneRendering: false
      };
    }
    // Get the angular injector for this app (change element if necessary)
    var injector = window.angular.element('body').injector();
    // Store providers to use for these checks
    var $rootScope = injector.get('$rootScope');
    var $http = injector.get('$http');
    var $timeout = injector.get('$timeout');
    // Check if digest
    if ($rootScope.$$phase === '$apply' || $rootScope.$$phase === '$digest' || $http.pendingRequests.length !== 0) {
      window.qa.doneRendering = false;
      return false; // Angular digesting or loading data
    }
    if (!window.qa.doneRendering) {
      // Set timeout to mark angular rendering as finished
      $timeout(function() {
        window.qa.doneRendering = true;
      }, 0);
      return false;
    }
  }
  return true;
} catch (ex) {
  return false;
}"""

  def waitForAngular(timeout:Long = 5L)(implicit webDriver:WebDriver):Boolean = {
    new WebDriverWait(webDriver, timeout, 100).until(new ExpectedCondition[Boolean]() {
      override def apply(webDriver:WebDriver):Boolean = {
        webDriver.asInstanceOf[JavascriptExecutor].executeScript(waitForAngularScript).asInstanceOf[Boolean]
      }
    })
  }

}
