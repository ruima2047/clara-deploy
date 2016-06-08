<html>
<head>
    <title>auto-depoly-sys</title>
</head>
<body>

<form action="UploadAction" method="POST" enctype="multipart/form-data" >
    <%--enctype="multipart/form-data"--%>

    <h3>WareHouse</h3>
    <input type="checkbox" name="gz" /> gz
    <input type="checkbox" name="guan" /> guan
    <input type="checkbox" name="sh" /> sh

    <h3>Module</h3>
    <select name="module">
        <option value="receiving">receiving</option>
        <option value="check">check</option>
        <option value="wave">wave</option>
        <option value="master">master</option>
    </select>

    <h3>FileUpload</h3>
    <br />
    <input type="file" name="file" size="50" />
    <br />
    <input type="submit" value="FileUpload" />
</form>

<form action="BuildAction" method="POST" enctype="multipart/form-data" >
    <%--enctype="multipart/form-data"--%>

        <input type="submit" value="BuildAction" />
</form>
</body>
</html>
