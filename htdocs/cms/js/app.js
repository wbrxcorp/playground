angular.module("CMS", ["ngResource", "ngRoute", "ui.bootstrap"])
.config(["$routeProvider", function($routeProvider) {
  $routeProvider
    .when("/dashboard", {templateUrl:"dashboard.html",controller:"Dashboard"})
    .when("/profile", {templateUrl:"profile.html",controller:"Profile"})
    .when("/entry/:entryId", {templateUrl:"edit.html",controller:"Entry",controllerAs:"c"})
    .when("/prefix/:prefix*", {templateUrl:"prefix.html",controller:"Prefix",controllerAs:"c"})
    .otherwise({redirectTo:"/dashboard"});
}])
.controller("Login", ["$resource", "$window", function($resource, $window) {
  var login = $resource("./api.php/login");
  var self = this;

  self.login = function() {
    login.save({}, {}, function(result) {
      if (result.success) {
        $window.location = "./";
      }
    });
  };
}])
.controller("LoginStatus", ["$resource", "$window", function($resource, $window) {
  var login = $resource("./api.php/login");
  var logout = $resource("./api.php/logout");

  var self = this;
  self.status = false;

  login.get({}, function(result) {
    if (result.success) {
      self.status = true;
      self.username = result.info.username;
    }
  });

  self.logout = function() {
    logout.save({}, {}, function(result) {
      if (result.success) {
        self.status = false;
        delete self.username;
        $window.location = "./";
      }
    });
  };
}])
.controller("Dashboard", ["$resource", function($resource) {
  
}])
.run(["$rootScope","$resource",   function($scope, $resource) {
  var info = $resource("./api.php/");
  info.get({}, function(result) {
    $scope.version = result.version;
  });
}]);
angular.element(document).ready(function() {
  angular.bootstrap(document, ['CMS']);
});
