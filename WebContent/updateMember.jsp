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
    <title>회원정보수정</title>

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
	color: navy;
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
	background-color: lightslategray;
	outline: none;
	color: #fff;
	font-size: 14px;
	height: auto;
	font-weight: normal;
	padding: 10px 40px;
	text-transform: uppercase;
	border-color: lightslategray;
}
.btn-check:hover,
.btn-check:focus {
	color: #fff;
	background-color: #647382 ;
	border-color: lightslategray;
}
.navy, .navy a {
  color: navy;
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

.red{
	color: red;
	font-size: 12px;
	font-weight: bold;
}

.success{
	color: green;
	font-size: 12px;
	font-weight: bold;
}

.label-insertPassword{
	color: black;
	font-size: 13px;
	font-weight: bold;
}
</style>
<script type="text/javascript">
$(function() {
	function checkPassword(password,id){
		if(!/^[a-zA-Z0-9]{5,15}$/.test(password)){
			$('#pwCheckMessage').html('숫자와 영문자 조합으로 5~15자리를 사용해야 합니다.');
			return false;
		}	
		var checkNumber = password.search(/[0-9]/g);
		var checkEnglish = password.search(/[a-z]/ig);
		
		if(checkNumber <0 || checkEnglish <0){
			$('#pwCheckMessage').html('숫자와 영문자를 혼용하여야 합니다.');
			return false;
		}
		if(/(\w)\1\1\1/.test(password)){
			$('#pwCheckMessage').html('같은 문자를 4번 이상 사용하실 수 없습니다.');
			return false;
		}
		if (password.search(id) > -1) {
			$('#pwCheckMessage').html('비밀번호에 아이디가 포함되었습니다.');
			return false;
		}
		return true;
	}
	$('#password').keyup(function(){
		var id = $('#hiddenId').val();
		var password = $('#password').val();
		if(checkPassword(password,id) == true) {
			$('#pwCheckMessage').html('사용가능한 비밀번호입니다.');
			$('#pwCheckMessage').removeClass('shake')
			$('#pwCheckMessage').addClass('success');
		} else {
			$('#pwCheckMessage').removeClass('success')
			$('#pwCheckMessage').addClass('red');
		}
	})
	$('#confirm_password').keyup(function(){
		if($('#password').val()==$('#confirm_password').val()&& $('#password').val() != "" && $('#confirm_password').val() != ""){
			$('#pwCheckMessage').html('비밀번호가 일치합니다.');
			$('#pwCheckMessage').removeClass('shake')
			$('#pwCheckMessage').addClass('success');
		}else{
			$('#pwCheckMessage').html('비밀번호가 일치하지 않습니다.');
			$('#pwCheckMessage').removeClass('success')
			$('#pwCheckMessage').addClass('red');
		}
	})
	
	function checkEmailFunction(email) {
		var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    	return re.test(email);
	}
	
	var checkEmail = false;
	var originalEmail = $('#email').val();
	var emailFlag;
	$('#email').keyup(function(){
		var email = $('#email').val();
		console.log("test");
		$.ajax({
			url : "/RealTimeNews/ajax.do",
			type : "post",
			data : "action=checkEmail&email=" + email,
			datatype : "text",
			success : function(data) {
				if (checkEmailFunction(email) == false) {
					$('#email').css('background', 'red');
					emailFlag = 'isWrongForm';
				} else{
					if(originalEmail == email){
						$('#email').css('background', 'lightgreen');
						emailFlag = 'isOk';
					} else if (data == 'false') {
						$('#email').css('background', 'red');
						emailFlag = 'isDuplicated';
					} else if (data == 'true') {
						$('#email').css('background', 'lightgreen');
						emailFlag = 'isOk';
					}
				}
			},
			error : function(exception) {
				alert("ajax email error : " + exception.message);
			}
		})
	})
	
	$('#check-submit-password').click(function(){
		$('#pwCheckMessage').html("");
		$('#pwCheckMessage').removeClass('shake animated red success');
	})
	$('#update-account-submit').click(function(){
		$('#pwCheckMessage').html("");
		$('#pwCheckMessage').removeClass('shake animated red success');
	})
	$('#update-account-form').submit(function(){
		if($('#password').val() == "") {
			$('#pwCheckMessage').html('Password를 입력해주세요.');
			$('#pwCheckMessage').addClass('shake animated');
			$('#password').focus();
			return false;
		} else if($('#confirm_password').val() == "") {
			$('#pwCheckMessage').html('Confirm Password를 입력해주세요.');
			$('#pwCheckMessage').addClass('shake animated');
			$('#confirm_password').focus();
			return false;
		} else if($('#email').val() == ""){
			$('#pwCheckMessage').html('이메일을 입력해주세요.');
			$('#pwCheckMessage').addClass('shake animated');
			$('#email').focus();
			return false;
		} else if(emailFlag=='isWrongForm'){
			$('#pwCheckMessage').html('이메일 형식을 확인해주세요.');
			$('#pwCheckMessage').addClass('shake animated');
			$('#email').focus();
			return false;
		} else if (!checkPassword($('#password').val(),$('#hiddenId').val())) {
			$('#pwCheckMessage').addClass('shake animated');
			return false;
		} else if(emailFlag=='isDuplicated'){
			$('#pwCheckMessage').html('중복된 이메일입니다.');
			$('#pwCheckMessage').addClass('shake animated');
			$('#email').focus();
			return false;
		} else if($('#password').val() != ($('#confirm_password').val())) {
			$('#pwCheckMessage').html('비밀번호가 불일치합니다.');
			$('#pwCheckMessage').addClass('shake animated');
			$('#confirm_assword').focus();
			return false;
		} else return true;
	})
});

</script>
</head>
<body>
<center>
	<div class="container">
    	<div class="row">
			<div class="col-md-6 col-md-offset-3">
				<div class="panel panel-check">
					<div class="panel-heading">
						<div class="row">
							<p>
								<a class="active" id="check-form-link">Edit Account</a>
							</p>
						</div>
					</div>
					<a href="main.jsp"><i class="glyphicon glyphicon-arrow-left navy"></i></a>
					<div class="panel-body">
						<div class="row">
							<div class="col-lg-12">
							<c:if test="${checkPassword==true}">
							<div id="check-password">
								<form id="check-password-form" action="member.do" method="post" role="form" style="display: block;">
									<input type="hidden" name="action" value="confirmPassword">
									<div class="label-insertPassword"> 비밀번호를 입력하세요</div>
									<div class="form-group">
										<input type="password" name="password" id="check_password" tabindex="1" class="form-control" placeholder="************" value="">
									</div>
									<div class="form-group">
										<div class="row">
											<div class="col-sm-6 col-sm-offset-3">
												<input type="submit" name="check-submit" id="check-submit-password" tabindex="4" class="form-control btn btn-check" value="Check Password">
											</div>
										</div>
									</div>
									<p class = "shake animated">
										<c:if test="${passwordFlag==false}">잘못된 비밀번호입니다.</c:if> 
									</p>
								</form>
							</div>
							</c:if>

							<c:if test="${passwordFlag==true}">
							<div id="update-account">
								<form id="update-account-form" action="member.do" method="post" role="form" style="display: block;">
									<input type="hidden" name="action" value="editAccount">
									<input type="hidden" id="hiddenId" value="${id}">
									<div class="label-insertPassword"> 비밀번호 확인을 완료하였습니다. 변경하실 비밀번호를 입력해주십시오.</div>
									<div class="form-group">
										<input type="password" name="password" id="password" tabindex="1" class="form-control" placeholder="Password" value="">
									</div>
									<div class="form-group">
										<input type="password" name="confirm_password" id="confirm_password" tabindex="2" class="form-control" placeholder="Confirm Password">
									</div>
									<div class="form-group">
										<input type="email" name="email" id="email" tabindex="2" class="form-control" placeholder="Email Address" value="${member.email}">
									</div>
									<div id="pwCheckMessage"></div><br>
									<div class="form-group">
										<div class="row">
											<div class="col-sm-6 col-sm-offset-3">
												<input type="submit" name="update-account-submit" id="update-account-submit" tabindex="4" class="form-control btn btn-check" value="Edit Account">
											</div>
										</div>
									</div>
								</form>
							</div>
						</c:if>		
					</div>
				</div>
			</div>
		</div>
	</div>
</center>

    <!-- jQuery (부트스트랩의 자바스크립트 플러그인을 위해 필요합니다) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <!-- 모든 컴파일된 플러그인을 포함합니다 (아래), 원하지 않는다면 필요한 각각의 파일을 포함하세요 -->
    <script src="js/bootstrap.min.js"></script>
  </body>
</html>