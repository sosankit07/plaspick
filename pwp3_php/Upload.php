<?php
if(isset($_POST['image'] )){
	$now = $_POST['time']; 
	$name = $_POST['email'];
		
	$upload_folder = "upload";
	$path = "$upload_folder/$name.png";
	$image= $_POST['image'];
	if(file_put_contents($path, base64_decode($image)) != false){
		echo "Uploaded_Success";
		$command= 'python /home/ankit_linux/netter/darknet/demo.py '.$name;
		$shell = `$command`;
		if(!$shell){
			echo "exec fail";
		}
		else{
			echo $shell;
		}exit;
	}
	else{
		echo "Uploaded_Failed";
		exit;
	}
}
else{
	echo "Image_not_in";
		exit;
	
}

?>
