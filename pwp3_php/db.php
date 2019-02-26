<?php
  
   $conn = mysql_connect("localhost", "root");
   $param1 = $_POST['id'];
   //$param2 = $_POST['email'];
   $param3 = 10;
   $param4 = 0;
   if(! $conn ) {
      die('Could not connect: ' . mysql_error());
   }
   $sql2 = "SELECT MAX(u_id) AS 'Maximum' FROM user  ";
   $x2 = mysql_select_db('pwp');
   $retval2 = mysql_query($sql2,$conn);
   $row2 = mysql_fetch_assoc($retval2);
   $max = $row2['Maximum'] + 1;
   
   
   $sql = "SELECT * FROM user where email = ". "'$param1'" ;
   $x = mysql_select_db('pwp');
   $retval = mysql_query($sql,$conn);
   $row = mysql_fetch_assoc($retval);
   if("{$row['u_id']}" == "") {
       $sql2 = "INSERT INTO user ".
               "(email,u_id) "."VALUES ".
               "('$param1',$max)";
            $y = mysql_select_db('pwp');
            $retval2 = mysql_query($sql2,$conn); 
            
   }
   echo "{$row['sub_pd']}"
   mysql_close($conn);
?>
