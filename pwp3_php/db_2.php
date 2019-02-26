<?php
	$conn = mysql_connect("localhost", "root");
	$param1 = $_POST['email'];
	$paramer2 = $_POST['score'];
	$paramer3 = $_POST['parle'];
	$paramer4 = $_POST['balaji'];
	$param5 = $_POST['region'];
	$param2 = (int)$paramer2;
	$param3 = (int)$paramer3;
	$param4 = (int)$paramer4;
	
	if(! $conn ) {
	die('Could not connect: ' . mysql_error());
	}
	
	#U_ID
	$sql3 = "SELECT * FROM user where email = ". "'$param1'" ;
	$x3 = mysql_select_db('pwp');
	$retval3 = mysql_query($sql3,$conn);
	$row3 = mysql_fetch_assoc($retval3);
	$UID = $row3['u_id'];
	echo $UID;
	$param2 = $param2 +$row3['score'];
	
	$sub = $row3['sub_pd']-1;
	echo $sub;
	
	#Update SCore and SUb_PD
	$sql5 = "UPDATE user SET score = $param2 WHERE u_id = $UID";
	$x5 = mysql_select_db('pwp');
	$retval5 = mysql_query($sql5,$conn);
if(!$retval5){
	die("COuld notconect" .mysql_error());
}
	#$row5 = mysql_fetch_assoc($retval5);
	$sql6 = "UPDATE user SET sub_pd = $sub WHERE u_id = $UID" ;
	$x6 = mysql_select_db('pwp');
	$retval6 = mysql_query($sql6,$conn);
	#$row6 = mysql_fetch_assoc($retval6);

	
	#S_id
	$sql4 = "SELECT MAX(s_id) AS 'Maximum' FROM submission  ";
	$x4 = mysql_select_db('pwp');
	$retval4 = mysql_query($sql4,$conn);
	$row4 = mysql_fetch_assoc($retval4);
	$SID = $row4['Maximum'] + 1;
	echo $SID;

	#TIME
	$TIME =date("Y-m-d H:i:s");
echo $TIME;
	$sql2 = "INSERT INTO submission ".
	       "(s_id,u_id,region) "."VALUES ".
	       "($SID,$UID,'$param5')";
	       mysql_select_db('pwp');
	    $retval = mysql_query( $sql2, $conn );

	#parle-g
	if($param3 != 0){
	$sql7 = "INSERT INTO sub_item ".
	       "(u_id,part_id,p_id,count) "."VALUES ".
	       "($UID,$SID,1,$param3)";
	       mysql_select_db('pwp');
	    $retval = mysql_query( $sql7, $conn );
	}
	#balaji
	if($param4 != 0){
	$sql8 = "INSERT INTO sub_item ".
	       "(u_id,part_id,p_id,count) "."VALUES ".
	       "($UID,$SID,2,$param4)";
	       mysql_select_db('pwp');
	    $retval = mysql_query( $sql8, $conn );
	    #$row = mysql_fetch_assoc($retval);
	}
	#if user is not there in DB then insert

	
	 
	  
	mysql_close($conn);
?>
