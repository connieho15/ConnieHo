function addInfo() {
    var name = $('#name').val();
    var empnum = $('#empnum').val();
    $('#namee').text(name);
    $('#empnume').text(empnum);
}


function confirmInfo() {

    
    var db = window.openDatabase("Database", "1.0", "Cordova Demo", 200000);
    var name = $('#name').val();
    var empnum = $('#empnum').val();
       
    db.transaction(populateDB);
    console.log("caller method"); 

    function populateDB(tx) { 

       // tx.executeSql("DROP TABLE IF EXISTS DEMO");
        tx.executeSql('CREATE TABLE IF NOT EXISTS DEMO (id UNIQUE ON CONFLICT FAIL, data)');
        tx.executeSql('INSERT INTO DEMO (id, data) VALUES (?, ?)', [empnum, name], successCB, errorCB);
        
    }

    // Transaction error callback

    function errorCB(err) {

      //  alert("Error processing SQL: " + err);
       // console.log(err.message);
       // console.log(err.code);
        

    }

    // Transaction success callback 

    function successCB() {

        //   alert("success!"); runs five times - bug
        name = "";
        empnum = "";
        $('#namee').text(name);
        $('#empnume').text(empnum);


    }
    function onDeviceReady() {

        var db = window.openDatabase("Database", "1.0", "Cordova Demo", 200000);

        db.transaction(populateDB, errorCB, successCB);


    }



}

    function clearInfo() {
        name = "";
        empnum = "";
        $('#namee').text(name);
        $('#empnume').text(empnum);
    }


    