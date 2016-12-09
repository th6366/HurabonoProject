<?
for($i=99;$i>=0;$i--){
	if($i==0){
		echo "No more bottles of beer on the wall, no more bottles of beer.<br>";
		echo "Go to the store and buy some more, 99 bottles of beer on the wall.<br>";
	}else if($i==1){
		echo "1 bottle of beer on the wall, 1 bottle of beer.<br>";
		echo "Take one down and pass it around, no more bottles of beer on the wall.<br>";
	}else{
		echo $i." bottles of beer on the wall, ".$i." bottles of beer.<br>";
		echo "Take one down and pass it around, ".($i-1)." bottles of beer on the wall.<br>";
	}
}
?>
