<?php
if(isset($_POST['email'] )){ 
	$name = $_POST['email'];
	$upload_folder = "Result";
	$path = "$upload_folder/$name.txt";
	$fh = fopen($path,"r");
	$content = fread($fh,filesize($path));
	fclose($fh);
	echo "$content";
}

?>
