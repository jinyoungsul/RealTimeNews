<%@page import="java.io.IOException"%>
<%@page import="org.jsoup.select.Elements"%>
<%@page import="org.jsoup.Jsoup"%>
<%@page import="org.jsoup.nodes.Document"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 위 3개의 메타 태그는 *반드시* head 태그의 처음에 와야합니다; 어떤 다른 콘텐츠들은 반드시 이 태그들 *다음에* 와야 합니다 -->
    <title>Find Password</title>

    <!-- 부트스트랩 -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
	
	<!-- 대쉬보드  -->
	<link href="css/dashboard.css" rel="stylesheet">
	
    <!-- IE8 에서 HTML5 요소와 미디어 쿼리를 위한 HTML5 shim 와 Respond.js -->
    <!-- WARNING: Respond.js 는 당신이 file:// 을 통해 페이지를 볼 때는 동작하지 않습니다. -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<style type="text/css">
body {
    padding-top: 90px;
    background-color: white
}
.panel-check {
	border-color: #ccc;
	-webkit-box-shadow: 0px 2px 3px 0px rgba(0,0,0,0.2);
	-moz-box-shadow: 0px 2px 3px 0px rgba(0,0,0,0.2);
	box-shadow: 0px 2px 3px 0px rgba(0,0,0,0.2);
}
.panel-check>.panel-heading {
	color: #00415d;
	background-color: #fff;
	border-color: #fff;
	text-align:center;
}
.panel-check>.panel-heading a{
	text-decoration: none;
	color: #666;
	font-weight: bold;
	font-size: 15px;
	-webkit-transition: all 0.1s linear;
	-moz-transition: all 0.1s linear;
	transition: all 0.1s linear;
}
.panel-check>.panel-heading a.active{
	color: #4D4DA6;
	font-size: 18px;
}
.panel-check>.panel-heading hr{
	margin-top: 10px;
	margin-bottom: 0px;
	clear: both;
	border: 0;
	height: 1px;
	background-image: -webkit-linear-gradient(left,rgba(0, 0, 0, 0),rgba(0, 0, 0, 0.15),rgba(0, 0, 0, 0));
	background-image: -moz-linear-gradient(left,rgba(0,0,0,0),rgba(0,0,0,0.15),rgba(0,0,0,0));
	background-image: -ms-linear-gradient(left,rgba(0,0,0,0),rgba(0,0,0,0.15),rgba(0,0,0,0));
	background-image: -o-linear-gradient(left,rgba(0,0,0,0),rgba(0,0,0,0.15),rgba(0,0,0,0));
}
.panel-check input[type="text"],.panel-check input[type="email"],.panel-check input[type="password"] {
	height: 45px;
	border: 1px solid #ddd;
	font-size: 16px;
	-webkit-transition: all 0.1s linear;
	-moz-transition: all 0.1s linear;
	transition: all 0.1s linear;
}
.panel-check input:hover,
.panel-check input:focus {
	outline:none;
	-webkit-box-shadow: none;
	-moz-box-shadow: none;
	box-shadow: none;
	border-color: #ccc;
}
.btn-check {
	background-color: slategray;
	outline: none;
	color: #fff;
	font-size: 14px;
	height: auto;
	font-weight: normal;
	padding: 10px 40px;
	text-transform: uppercase;
	border-color: slategray;
}
.btn-check:hover,
.btn-check:focus {
	color: #fff;
	background-color: lightslategray ;
	border-color: slategray;
}
.icolor, .icolor a {
  color: #4D4DA6;
}

/* /////////////////////////////////////////////////////////////////////////////////////////////// */
.animated {
  -webkit-animation-duration: 1s;
	   -moz-animation-duration: 1s;
	     -o-animation-duration: 1s;
	        animation-duration: 1s;
	-webkit-animation-fill-mode: both;
	   -moz-animation-fill-mode: both;
	     -o-animation-fill-mode: both;
	        animation-fill-mode: both;
}

.animated.hinges {
	-webkit-animation-duration: 2s;
	   -moz-animation-duration: 2s;
	     -o-animation-duration: 2s;
	        animation-duration: 2s;
}

.animated.slow {
	-webkit-animation-duration: 3s;
	   -moz-animation-duration: 3s;
	     -o-animation-duration: 3s;
	        animation-duration: 3s;
}

.animated.snail {
	-webkit-animation-duration: 4s;
	   -moz-animation-duration: 4s;
	     -o-animation-duration: 4s;
	        animation-duration: 4s;
}

@-webkit-keyframes shake {
	0%, 100% {-webkit-transform: translateX(0);}
	10%, 30%, 50%, 70%, 90% {-webkit-transform: translateX(-10px);}
	20%, 40%, 60%, 80% {-webkit-transform: translateX(10px);}
}

@-moz-keyframes shake {
	0%, 100% {-moz-transform: translateX(0);}
	10%, 30%, 50%, 70%, 90% {-moz-transform: translateX(-10px);}
	20%, 40%, 60%, 80% {-moz-transform: translateX(10px);}
}

@-o-keyframes shake {
	0%, 100% {-o-transform: translateX(0);}
	10%, 30%, 50%, 70%, 90% {-o-transform: translateX(-10px);}
	20%, 40%, 60%, 80% {-o-transform: translateX(10px);}
}

@keyframes shake {
	0%, 100% {transform: translateX(0);}
	10%, 30%, 50%, 70%, 90% {transform: translateX(-10px);}
	20%, 40%, 60%, 80% {transform: translateX(10px);}
}

.shake {
	-webkit-animation-name: shake;
	-moz-animation-name: shake;
	-o-animation-name: shake;
	animation-name: shake;
	color: red;
	font-size: 12px;
	font-weight: bold;
}

.success{
	color: green;
	font-size: 12px;
	font-weight: bold;
}
</style>
<script type="text/javascript">
$(function() {
	$('#check-submit').click(function(){
		$('#checkMessage').html('');
		$('#checkMessage').removeClass('shake animated');
	})
	$('#check-form').submit(function(){
		if($('#id').val() == "") {
			$('#checkMessage').html('ID를 입력해주세요.');
			$('#checkMessage').addClass('shake animated');
			$('#userName').focus();
			return false;
		} else if($('#email').val() == ""){
			$('#checkMessage').html('이메일을 입력해주세요.');
			$('#checkMessage').addClass('shake animated');
			$('#email').focus();
			return false;
		}
		else return true;
	})
});

</script>
</head>
<body>
	<div class="container">
    	<div class="row">
			<div class="col-md-6 col-md-offset-3">
				<div class="panel panel-check">
					<div class="panel-heading">
						<div class="row">
							<p>
								<a class="active" id="check-form-link">Find Password</a>
							</p>
						</div>
					</div>
					<center>
					<a href="index.jsp"><i class="glyphicon glyphicon-home icolor"></i></a>
					<center>
					<div class="panel-body">
						<div class="row">
							<div class="col-lg-12">
								<form id="check-form" action="member.do" method="post" role="form" style="display: block;">
									<input type="hidden" name="action" value="checkPassword">
									<div class="form-group">
										<input type="text" name="id" id="id" tabindex="1" class="form-control" placeholder="ID" value="">
									</div>
									<div class="form-group">
										<input type="email" name="email" id="email" tabindex="2" class="form-control" placeholder="Email Address">
									</div>
									<div class="form-group">
										<div class="row">
											<div class="col-sm-6 col-sm-offset-3">
												<input type="submit" name="check-submit" id="check-submit" tabindex="4" class="form-control btn btn-check" value="Find Password">
											</div>
										</div>
									</div>
								</form>
								<div id = "checkMessage">
									<c:if test="${password_flag==true}"><p class="success">${name}님의 비밀번호 정보입니다.:${password}</p></c:if>
									<c:if test="${password_flag==false}"><p class="shake animated">회원정보가 없습니다.</p></c:if>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>


    <!-- jQuery (부트스트랩의 자바스크립트 플러그인을 위해 필요합니다) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <!-- 모든 컴파일된 플러그인을 포함합니다 (아래), 원하지 않는다면 필요한 각각의 파일을 포함하세요 -->
    <script src="js/bootstrap.min.js"></script>
  </body>
</html>