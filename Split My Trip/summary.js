// var members = [
//  {"name":"Connie Ho", "runningTotal":100},
//  {"name":"Jessica Que", "runningTotal":-200},
//  {"name":"Ostap", "runningTotal":50},
//  {"name":"Eric", "runningTotal":50}
// ];

function getTotals(members) {

  var transactions = [];

  while (members.length > 0) {

    // get min and max values
    var iMax = 0;
    var max = 0;
    var iMin = 0;
    var min = 0;
    members.forEach(function(item, i) {
      if (item.runningTotal > max) {
        iMax = i;
        max = item.runningTotal;
      }
      if (item.runningTotal < min) {
        iMin = i;
        min = item.runningTotal;
      }
    });

    // add to transactions
    if ((members[iMin].runningTotal !== 0) && (members[iMax].runningTotal !== 0)) {
      transactions.push({payer:members[iMin].name,
        payee: members[iMax].name,
        amount: Math.min((Math.abs(members[iMin].runningTotal)), (Math.abs(members[iMax].runningTotal)))});
    }

    // remove from members group
    if (Math.abs(members[iMax].runningTotal) == Math.abs(members[iMin].runningTotal)) {
      if (iMax > iMin) {
        members.splice(iMax, 1);
        members.splice(iMin, 1);
      }
      else {
          members.splice(iMin, 1);
          members.splice(iMax, 1);
      }
    }
    else if (Math.abs(members[iMax].runningTotal) > Math.abs(members[iMin].runningTotal)) {
      members[iMax].runningTotal += members[iMin].runningTotal;
      members.splice(iMin, 1);
    }
    else {
      members[iMin].runningTotal += members[iMax].runningTotal;
      members.splice(iMax, 1);
    }
  }

  return transactions;
}

function summaryUpdate(transactions) {
  transactions.forEach(function(item) {
    $(".owe").append("<li><h3>" + item.payer + " owes " + item.payee + " $" + item.amount + "</h3></li>");
  });
}


// function summaryUpdate(transcations) {
//   transcations.forEach(function(item) {
//     $(".owe").append("<li>" + "I'm an item" + "</li>");
//   });
// }

$(document).ready(function() {
  summaryUpdate(getTotals(JSON.parse(localStorage.getItem('people'))));
});
