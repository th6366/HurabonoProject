<?php
	include '../db/MySQLDB.php';

	$resultData = $_POST['data'];

	$database = new MySQLDB;
	$query = 'SELECT * FROM specialCharacter';
	$result = $database->MySQLSelect($query);
	
	while($row = mysql_fetch_array($result)) {
		$resultData = str_replace($row[2], $row[1], $resultData);
	}
?>
<html>
<head>
        <title>문자열 치환</title>
        <meta http-equiv='Content-Type' content='text/html; charset=utf-8' />
</head>
<body>
	<form action="index.php" method="post">
        <div class="background">
                <div class="title">
			Data Type 선택
                </div>
                <div class="textArea">
		<textarea name="data" rows="20" cols="100%"></textarea>
                </div>
                <div class="title">
                </div>
                <div class="textArea">
		<xmp>
		<?php
		if($resultData != NULL) {
			 echo $resultData;
			$result = NULL;
		}
		?>
		</xmp>
		<button type="submit" name="submit" value="submit-value">Submit</button>
        </div>
	</form>
</body>
</html>
