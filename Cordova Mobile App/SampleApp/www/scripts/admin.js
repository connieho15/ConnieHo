function viewInfo() {

    var db = window.openDatabase("Database", "1.0", "Cordova Demo", 200000);

    db.transaction(queryDB);

    // Query the database    

    function queryDB(tx) {

        tx.executeSql('SELECT * FROM DEMO', [], querySuccess, errorCB);

        // FORMAT(column_name,format)

    }

    // Query the success callback  

    function querySuccess(tx, results) {

        var len = results.rows.length;

        console.log("DEMO table: " + len + " rows found.");

        var rows = []

        // clear rows on refresh
        for (i = 0; i < 10; i++)
        {
            rows[i] = "";
            $('#results' + i).text(rows[i]);

        }
        for (var i = 0; i < len; i++) {
            
            rows[i] = ("Row = " + i + " Employee Number = " + results.rows.item(i).id + " Name =  " + results.rows.item(i).data);
            $('#results' + i).text(rows[i]);

        }
        $('#summary').text("Returned rows = " + results.rows.length);

        }

    // Transaction error callback   

    function errorCB(err) {

        console.log("Error processing SQL: " + err.code);

        name = err.message;

    }

    // Transaction success callback       

    function successCB() {

        var db = window.openDatabase("Database", "1.0", "Cordova Demo", 200000);

        db.transaction(queryDB, errorCB);

    }

    // Cordova is ready     

    function onDeviceReady() {

        var db = window.openDatabase("Database", "1.0", "Cordova Demo", 200000);

        db.transaction(queryDB, errorCB, successCB);

    }



}


function deleteInfo() {

    var db = window.openDatabase("Database", "1.0", "Cordova Demo", 200000);

    db.transaction(deleteDB);

    var deleteid;

    deleteid = $('#delempnum').val();
    
    function deleteDB(tx) {

        tx.executeSql('DELETE FROM DEMO WHERE id = ?', [deleteid]);

    }


      
        // Transaction error callback   

        function errorCB(err) {
            console.log("Error processing SQL: " + err.code);
            console.log(err.message);
            alert("fail");

        }

        // Transaction success callback       

        function successCB() {
            var db = window.openDatabase("Database", "1.0", "Cordova Demo", 200000);
            db.transaction(deleteDB, errorCB);
            alert("success!");
        }

        function onDeviceReady() {
            var db = window.openDatabase("Database", "1.0", "Cordova Demo", 200000);
            db.transaction(deleteDB, errorCB, successCB);


        }
       

    
}