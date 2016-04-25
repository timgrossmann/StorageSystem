//Global var of the fetched ItemData
var resultData;

//upon pageLoad, all the Items are added to the table (sorted)
$.getJSON("http://localhost:3000/db", function(data) {

    //Sorting the Items by their name
    data.sort(function(a, b) {
        return a["name"] > b["name"];
    });

    //setting the global data var to the sorted Items
    resultData = data;
    
    //Updating the table, allow all Item names by using "."
    updateTable(/./, "name"); 
});

//listening on the input of the Text, reacting on change
$("#searchField").on("input", handleEvent);

//covering the change of the RadioButton
$("input:radio[name='inlineRadioOptions']").on("change", handleEvent);

//handle either the change of the text input or the radio buttons
function handleEvent() {
    //regex pattern with the value of the textfield, ignoring case
    var regEx = new RegExp($("#searchField").val(), 'i');
    
    //getting the checked radioButtons
    var selected = $("input:radio[name='inlineRadioOptions']:checked").val();
    
    //updating the table with the created RegEx and the selected radioButtons Value
    updateTable(regEx, selected);
}


//Updates the table
function updateTable(regEx, selected) {
    
    //setting the tables content to just the table headers
    $("#tbl").html("<tr><th>Name</th><th>Amount</th><th>Categories</th></tr>");
    
    //filtering the fetched data for matching Items
    var filtered = resultData.filter(function(a) {
        //checking if the Items contain the string
        return (regEx.test(a[selected]));
    });
    
    //for every remaining Item, add it to the table with name, amount and categorie
    for(var i in filtered) {
        
        var name = filtered[i]['name'];
        var amount = filtered[i]['amount'];
        var categories = filtered[i]['categories'];
        
        $("#tbl").append('<tr><td>' + name + '</td><td>' + amount + '</td><td>' + categories + '</td></tr>');
    }  
}