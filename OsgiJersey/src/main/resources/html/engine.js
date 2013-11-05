function registerMessages() {
    var req = new XMLHttpRequest();
    req.open("GET", "/rest/sse", true);
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    req.onreadystatechange = function() {
        if (req.readyState === 4 && req.status === 204) {
            console.log(req.readyState);
        }
    };
    req.send();
}

function receiveMessages() {
    if (typeof(EventSource) !== "undefined") {
        // Yes! Server-sent events support!
        var source = new EventSource("/rest/sse");
        source.onmessage = function(event) {
            console.log('Received unnamed event: ' + event.data);
            display("Added new item: " + event.data, "#444444");
        };

        source.addEventListener("size", function(e) {
            console.log('Received event ' + event.name + ': ' + event.data);
            display("New items size: " + event.data, "#0000FF");
        }, false);

        source.onopen = function(event) {
            console.log("event source opened");
        };

        source.onerror = function(event) {
            console.log('Received error event: ' + event.data);
            display(event.data, "#FF0000");
        };
    } else {
        // Sorry! No server-sent events support..
        display('SSE not supported by browser.', "#FF0000");
    }
}

function display(text) {
    messages = document.getElementById("messages");
    var p = document.createElement("p");
    p.innerHTML = text;
    messages.appendChild(p);
}
