<?php
	$conn = mysql_connect("localhost","root");
	if(!$conn){
		die('Could not connect: ' . mysql_error());
	}
	$sql="select sum(count) as 'parle' from sub_item where p_id='1'";
	$x2 = mysql_select_db('pwp');
	$parle = mysql_query($sql,$conn);
	$row = mysql_fetch_assoc($parle);
	$sql1="select sum(count) as 'balaji' from sub_item where p_id='2'";
	$x1 = mysql_select_db('pwp');
	$balaji = mysql_query($sql1,$conn);
	$row1 = mysql_fetch_assoc($balaji);
	echo $row['parle']." ".$row1['balaji'];	
	#$a=$row['parle'];
	#$b=$row1['balaji'];
	#mysql_close($conn);
	#$command= "python dummy.py 2>&1";
	#var_dump($output);
	#$command= "python /home/ankit_linux/netter/darknet/graph.py $a $b";
	
	#echo shell_exec("python /opt/lampp/htdocs/news/graph.py 2>&1");
	#$shell=shell_exec("$command");
	#$command = escapeshellcmd("/home/ankit_linux/netter/darknet/demo.py b2.jpg");
	#echo $command;
	#$output = shell_exec($command);
	#if(!$output){
	#	echo "execution failed";
	#}	
	#else{
	#	echo $output;
	#}
	#echo $shell;
	
?>
