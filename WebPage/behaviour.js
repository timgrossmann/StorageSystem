$.getJSON("http://localhost:3000/db", function(data) {
    
    data.sort(function(a, b) {
        return a["name"] > b["name"];
    });
    
    for(var i in data) {
        
        var name = data[i]['name'];
        var amount = data[i]['amount'];
        var categories = data[i]['categories'];
        
        $("#tbl").append('<tr><td>' + name + '</td><td>' + amount + '</td><td>' + categories + '</td></tr>');
    }
    
    
});