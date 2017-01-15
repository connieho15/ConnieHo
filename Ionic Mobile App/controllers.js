angular.module('app.controllers', [])

.controller('mainCtrl', ['$scope', function ($scope, $state, $http) {

//    if (localStorage.getItem("name")) {
//        $scope.name = $scope.staffList[1];
//        console.log("refresh function ran! name is" + $scope.staffList[1])
//        $scope.Position = $scope.staffList[4];
//        $scope.Region = $scope.staffList[10];
//        $scope.Department = $scope.staffList[6];
//        $scope.staffcode = $scope.staffList[0];}

}

])

.controller('clientsCtrl', ['$scope', function ($scope, $http) {

    // dont touch this function
    var searchquery = "";
    searchquery = $scope.searchquery;

    $scope.getdata = function (searchquery) {
        console.log(searchquery);
        var search = searchquery;
        var url = "https://mapp1.deloitte.cn:7999/api/Engagement/getclient?searchtext=" + search +
        "&CurrencyCode=HKD";
        console.log(url);
        var settings = {
            "async": false,
            "crossDomain": true,
            "withCredentials": true,
            "url": url,
            "method": "GET",
            "headers": {
                "cache-control": "no-cache",
                "postman-token": "ea5501de-4d96-7b87-355a-75c239de6d8b"
            }
        }


        $.ajax(settings).done(function (response) {

            $scope.clientList = [];
            var list = [];
            console.log(response);
            for (var x in response) {
                list.push(response[x]);

            }

            var clientlist = list[0];
            for (var x in clientlist) {

                $scope.clientList.push(clientlist[x])

            }
            $scope.name = $scope.clientList[1];


        });
    }

}])

.controller('transactionsCtrl', ['$scope', function ($scope, $http) {

    var search = "";

    // retrieve username from local storage which was saved when we logged in and use it as search query
    console.log(localStorage.getItem("username"));
    search = localStorage.getItem("username");
    
    var url = "https://mapp1.deloitte.cn:7999/api/Staff/GetStaff?LastSynDate=&Locale=en&UserName=" + search + "&staffcode=";
    console.log(url);

    var settings = {
        "async": false,
        "crossDomain": true,
        // withCredentials should be true for cookies to work
        "withCredentials": true,
        "url": url,
        "method": "GET",
        "headers": {
            "cache-control": "no-cache",
            "postman-token": "ea5501de-4d96-7b87-355a-75c239de6d8b"
        }
    }


    $.ajax(settings).done(function (response) {

        $scope.staffList = [];
        var list = [];
        console.log(response);
        for (var x in response) {
            list.push(response[x]);
        }

        var stafflist = list[0];

        $scope.staffList.push(stafflist[1]);
        $scope.staffList.push(stafflist[4]);
        $scope.staffList.push(stafflist[10]);
        $scope.staffList.push(stafflist[6]);
        $scope.staffList.push(stafflist[0]);


        

        localStorage.setItem("name", $scope.staffList[0]);
        console.log(localStorage.getItem("name"));
        localStorage.setItem("position", $scope.staffList[1]);
        console.log(localStorage.getItem("position"));
        localStorage.setItem("region", $scope.staffList[2]);
        console.log(localStorage.getItem("region"));
        localStorage.setItem("department", $scope.staffList[3]);
        console.log(localStorage.getItem("department"));
        localStorage.setItem("staffcode", $scope.staffList[4]);
        console.log(localStorage.getItem("staffcode"));

       
        $scope.name = stafflist[1];
        $scope.Position = stafflist[4];
        $scope.Region = stafflist[10];
        $scope.Department = stafflist[6];
        $scope.staffcode = stafflist[0];

    });

    $scope.logout = function ($scope) {

        $ionicLoading.show({
            template: 'Logging out....'
        });
        $localstorage.set('loggin_state', '');

        $timeout(function () {
            $ionicLoading.hide();
            $ionicHistory.clearCache();
            $ionicHistory.clearHistory();
            $ionicHistory.nextViewOptions({
                disableBack: true,
                historyRoot: true
            });
            $state.go('login');
        }, 30);


        localStorage.clear();
        console.log("cleared everything!");
        $ionicHistory.clearCache();

        $scope.name = "";
        $scope.Position = "";
        $scope.Region = "";
        $scope.Department = "";
        $scope.staffcode = "";
        $scope.staffList = "";
        $scope.username = "";                                                                                                                                                        
        console.log($scope.staffList);
        console.log($scope.name);
        console.log($scope.username);



    }

}

])

.controller('approvalsCtrl', function ($scope) {

})



// based on https://devdactic.com/simple-login-example-with-ionic-and-angularjs/

.controller('loginCtrl', function ($scope, LoginService, $ionicPopup, $state, $ionicSideMenuDelegate) {

    // disable drag or view side menu
    $ionicSideMenuDelegate.canDragContent(false)
    $ionicSideMenuDelegate.toggleLeft(false);

    // initialize data
    $scope.data = {};

    $scope.login = function () {
        
        LoginService.loginUser($scope.data.username, $scope.data.password).success(function (data) {

            // if successful, change to clients page
            $state.go('tabsController.clients');


           // this.localStorage.setItem("username", $scope.data.username);
            console.log($scope.data.username + "is the new username");

            // if unsuccessful, pop up with fail
            }).error(function (data) {
            var alertPopup = $ionicPopup.alert({
                title: 'Login failed!',
                template: 'Please check your credentials!'
            });
        });
    }
})
