// var members = [
//  {"name":"Connie Ho", "runningTotal":100},
//  {"name":"Jessica Que", "runningTotal":-200},
//  {"name":"Ostap", "runningTotal":50},
//  {"name":"Eric", "runningTotal":50}
// ];
//
// localStorage.setItem('people', JSON.stringify(members));

function updateTotals(payer, amount, party) {
  console.log(payer);
  console.log(amount);
  console.log(party);
  var perPerson = amount / party.length;

  var members = JSON.parse(localStorage.getItem('people'));

  members.forEach(function(member, i) {
    if (member.name == payer) {
      member.runningTotal += amount;
    }
    party.forEach(function(partier, i) {
      if (member.name == partier)
      member.runningTotal -= perPerson;
    });
  });

  localStorage.setItem('people', JSON.stringify(members));
}

function getAmount() {
  return parseInt($('.amount').val());
}

var payer;
var party = [];

$(document).ready(function() {
  $('.submit').on("click", function() {
    updateTotals(payer, getAmount(), party);
    payer = null;
    party = [];
    window.location.href = "dashboard.html";
  });
  $('.payer-jessica').on("click", function() {
    payer = "Jessica";
    console.log("test j payer");
  });
  $('.payer-connie').on("click", function() {
    payer = "Connie";
        console.log("test c payer");

  });
  $('.payer-ostap').on("click", function() {
    payer = "Ostap";
        console.log("test o payer");

  });
  $('.payer-eric').on("click", function() {
    payer = "Eric";
        console.log("test e payer");

  });
  $('.payee-jessica').on("click", function() {
    party.push("Jessica");
        console.log("test j payee");

  });
  $('.payee-connie').on("click", function() {
    party.push("Connie");
        console.log("test c payee");

  });
  $('.payee-ostap').on("click", function() {
    party.push("Ostap");
        console.log("test o payee");

  });
  $('.payee-eric').on("click", function() {
    party.push("Eric");
        console.log("test e payee");

  });
});
