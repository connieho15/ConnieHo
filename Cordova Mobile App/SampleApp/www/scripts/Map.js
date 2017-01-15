function getLocation() {

    navigator.geolocation.getCurrentPosition(onSuccess, onError, { enableHighAccuracy: true });

    $('#description').text("Determining your current location ...");
    $('#get-weather').prop("disabled", true);

}

var onSuccess = function (position) {

    var latitude = position.coords.latitude;
    var longitude = position.coords.longitude;
    $('#lat').text(latitude);
    $('#long').text(longitude);
   
    // Get zipCode by using latitude and longitude.
}

function onError(error) {
    console.log('code: ' + error.code + '\n' +
      'message: ' + error.message + '\n');
}
