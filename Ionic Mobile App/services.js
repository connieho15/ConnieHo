angular.module('app.services', [])

.service('LoginService', function ($q) {
    var list = [];
    var key = "";
    key = localStorage.getItem("key");
    console.log(key);
    return {
        
        loginUser: function (name, pw) {
           
            // SET UP HTTP POST　REQUEST
            var settings = {
                "async": false,
                "crossDomain": true,
                "url": "https://mapp1.deloitte.cn:7999/api/Login/Post",
                "withCredentials": true,
                "method": "POST",
                "headers": {
                    "cache-control": "no-cache",
                    "postman-token": "06062abb-0657-791f-a7cc-29b7531fa876",
                    "content-type": "application/x-www-form-urlencoded"
                },
                
                
                "data": {

                    "UserName": name,
                    "password": pw,
                    "SecretKey": key,
                    "BuildNumber": "1.0",
                    "SerialNumber": device.serial,
                    "OSVersion": device.version,
                    "OS": device.platform,                    
                    "DeviceType": "Phone"
                }
            }

            // EXECUTE HTTP POST REQUEST
            var loggedin;
      
                $.ajax(settings).done(function (response) {

                    // true if successful, false if login failed
                    console.log("login status: " + response);
                    loggedin = response;
                });
            

            var deferred = $q.defer();
            var promise = deferred.promise;

            if (loggedin == true) {
                deferred.resolve('Welcome ' + name + '!');

                // save username in local storage to use for searchquery in later get request
                localStorage.setItem("username", name);
            } else {
                deferred.reject('Wrong credentials 用户名或密码错误');
            }
            promise.success = function (fn) {
                promise.then(fn);
                return promise;
            }
            promise.error = function (fn) {
                promise.then(null, fn);
                return promise;
            }
            return promise;
        }

        // END LOGIN USER FUNCTION - login user function returns result of login - true or false
    }
}

)
