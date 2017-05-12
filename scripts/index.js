// For an introduction to the Blank template, see the following documentation:
// http://go.microsoft.com/fwlink/?LinkID=397704
// To debug code on page load in Ripple or on Android devices/emulators: launch your app, set breakpoints, 
// and then run "window.location.reload()" in the JavaScript Console.
(function () {
    "use strict";

    document.addEventListener( 'deviceready', onDeviceReady.bind( this ), false );

    function onDeviceReady() {
        // Handle the Cordova pause and resume events
        document.addEventListener( 'pause', onPause.bind( this ), false );
        document.addEventListener( 'resume', onResume.bind( this ), false );
        
        // TODO: Cordova has been loaded. Perform any initialization that requires Cordova here.
        var parentElement = document.getElementById('deviceready');
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');
        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');
    };

    function onPause() {
        // TODO: This application has been suspended. Save application state here.
    };

    function onResume() {
        // TODO: This application has been reactivated. Restore application state here.
    };

    // Add a "checked" symbol when clicking on a list item
    var list = document.querySelector('ul');

    var feedback = ["1", "2", "3", "4", "5", "6", "7"];

    list.addEventListener('click', function (ev) {
        if (ev.target.tagName === 'LI') {
            ev.target.classList.toggle('checked');
        }
    }, false);

    // Get the modal
    var modal = document.getElementById('myModal');

    // Get the button that opens the modal
    var btn = document.getElementById("myBtn");
    var resetbtn = document.getElementById("resetBtn")

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    resetBtn.onclick = function (){
        var ul = document.getElementById("myUL");
        var items = ul.getElementsByTagName("li");
        for (var i = 0; i < items.length; ++i) {
            items[i].classList.remove('checked');
        }
        
    }

    // When the user clicks on the button, open the modal
    btn.onclick = function () {
        var nums = document.getElementById("myUL");
        var listItem = nums.getElementsByTagName("li");
        var missed = "";
        var ul = document.getElementById("myUL");
        var items = ul.getElementsByTagName("li");
        for (var i = 0; i < items.length; ++i) {
            if (!items[i].classList.contains('checked')) {
                missed = missed + '\n' + "- " + items[i].innerText;

            }
        }
        document.getElementById("missed").innerText = missed;
        modal.style.display = "block";
    }

    // When the user clicks on <span> (x), close the modal
    span.onclick = function () {
        modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

} )();