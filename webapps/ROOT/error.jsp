<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Error Page</title>
    </head>
    <body>
            <h1>Error!</h1>
            <div> ${errorMessage} </div>
            <div>Return to <a href="${returnURL}"> previous </a></div>
    </body>
</html>