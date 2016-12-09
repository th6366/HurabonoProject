<html>
<head>
<title>자바스크립트를 이용한 토글 예제</title>

<script type="text/javascript">
function showID(id)
{
obj=document.getElementById(id);

if(obj.style.display == "none") 
  obj.style.display="inline";
else
  obj.style.display="none";
}
</script>
</head>

<body>
    <div id="content_bt">
        <a href="#" onclick="showID('content');">menu</a>
    </div>
    <div id="content" style="display:none;">
        내용
    </div>
</body>
</html>
