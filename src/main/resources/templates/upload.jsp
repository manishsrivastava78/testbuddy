<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h4>Upload Single File:</h4>
  <form method="POST" th:action="@{/uploadFile}" enctype="multipart/form-data">
    <input type="file" name="file"/> <br/><br/>
    <button type="submit">Submit</button>
  </form>
  <hr/>
  <div th:if="${message}">
    <h2 th:text="${message}"/>
  </div>
</body>
</html>