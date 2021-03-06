<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.css">
	<script type="text/javascript" src="js/jquery-1.8.0.js"></script>
</head>
<body>

<div id="login">
</div>

<form class="well" id="form">
	<label>Username:</label>
	<input type="text" class="span3" placeholder="Username here" name="name"/><br/>

	<label>Content:</label>
	<input type="text" class="span3" placeholder="Content" name="content"/><br/>

	<button class="btn btn-primary" id="submit">Sign Guestbook</button>
	<button class="btn">Clear</button>
</form>

<div id="content"></div>

<h2>Signatures:</h2>
<div id="signatures">
<button class="btn" id="load-signatures">Load Signatures</button>
</div>

<script
		type="text/javascript"
		src="js/bootstrap.js">
</script>
</body>
<script type="text/javascript">
	$("#submit").click(function (event) {
		event.preventDefault();
		var $moo = $("#form"),
				fooVal = $moo.find('input[name="content"]').val();


		$.post("/rest/sign", { content:fooVal },
				function (data) {
					$("#content").html("<pre>" + data + "</pre>");
				}
		);
	});

	$(document).ready(function () {
		$.get("/rest/login", function(data){
			$('#login').html(data);
		});
	});

	$("#load-signatures").click(function(event){
	    event.preventDefault();
	    $.getJSON("/rest/sign/signatures", function(data) {
	        $.each(data, function(i, data){
	            var div_data = "<div>" + data.propertyMap.content + "</div>";
	            $(div_data).appendTo("#signatures");
	        });
	    });
	});
</script>
</html>
