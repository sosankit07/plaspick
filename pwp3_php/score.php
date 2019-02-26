<?php
	$conn = mysql_connect("localhost","root");
	$param1 = $_POST['id'];
	if(!$conn){
		die('Could not connect: ' . mysql_error());
	}
	$sql="SELECT score as 'sc' FROM user where email = ". "'$param1'";
	$x2 = mysql_select_db('pwp');
	$sc = mysql_query($sql,$conn);
	$row = mysql_fetch_assoc($sc);
	echo $row['sc'];
?>
