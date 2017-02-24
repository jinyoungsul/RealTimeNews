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
    <title>index</title>

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
.panel-login {
	border-color: #ccc;
	-webkit-box-shadow: 0px 2px 3px 0px rgba(0,0,0,0.2);
	-moz-box-shadow: 0px 2px 3px 0px rgba(0,0,0,0.2);
	box-shadow: 0px 2px 3px 0px rgba(0,0,0,0.2);
}
.panel-login>.panel-heading {
	color: #00415d;
	background-color: #fff;
	border-color: #fff;
	text-align:center;
}
.panel-login>.panel-heading a{
	text-decoration: none;
	color: #666;
	font-weight: bold;
	font-size: 15px;
	-webkit-transition: all 0.1s linear;
	-moz-transition: all 0.1s linear;
	transition: all 0.1s linear;
}
.panel-login>.panel-heading a.active{
	color: navy;
	font-size: 18px;
}
.panel-login>.panel-heading hr{
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
.panel-login input[type="text"],.panel-login input[type="email"],.panel-login input[type="password"] {
	height: 45px;
	border: 1px solid #ddd;
	font-size: 16px;
	-webkit-transition: all 0.1s linear;
	-moz-transition: all 0.1s linear;
	transition: all 0.1s linear;
}
.panel-login input:hover,
.panel-login input:focus {
	outline:none;
	-webkit-box-shadow: none;
	-moz-box-shadow: none;
	box-shadow: none;
	border-color: #ccc;
}
.btn-login {
	background-color: #59B2E0;
	outline: none;
	color: #fff;
	font-size: 14px;
	height: auto;
	font-weight: normal;
	padding: 10px 40px;
	text-transform: uppercase;
	border-color: #59B2E6;
}
.btn-login:hover,
.btn-login:focus {
	color: #fff;
	background-color: #53A3CD;
	border-color: #53A3CD;
}
.forgot-password {
	text-decoration: underline;
	color: #888;
}
.forgot-password:hover,
.forgot-password:focus {
	text-decoration: underline;
	color: #666;
}

.forgot-id {
	text-decoration: underline;
	color: #888;
}
.forgot-id:hover,
.forgot-id:focus {
	text-decoration: underline;
	color: #666;
}

.btn-register {
	background-color: #1CB94E;
	outline: none;
	color: #fff;
	font-size: 14px;
	height: auto;
	weight: auto;
	font-weight: normal;
	padding: 10px 40px;
	text-transform: uppercase;
	border-color: #1CB94A;
}
.btn-register:hover,
.btn-register:focus {
	color: #fff;
	background-color: #1CA347;
	border-color: #1CA347;
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
</style>
<script type="text/javascript">
	$(function() {
		$('#login-form-link').click(function(e) {
			$("#login-form").delay(100).fadeIn(100);
			$("#register-form").fadeOut(100);
			$('#register-form-link').removeClass('active');
			$(this).addClass('active');
			e.preventDefault();
		});
		$('#register-form-link').click(function(e) {
			$("#register-form").delay(100).fadeIn(100);
			$("#login-form").fadeOut(100);
			$('#login-form-link').removeClass('active');
			$(this).addClass('active');
			e.preventDefault();
		});
//////////////////////////////////////////////////////////////////////////////////
		var checkId = false;
		var idFlag;
		$('#joinId').keyup(function() {
			var id = $('#joinId').val();
			$.ajax({
				url : "/RealTimeNews/ajax.do",
				type : "post",
				data : "action=checkId&id=" + id,
				datatype : "text",
				success : function(data) {
					var email = $('#joinEmail').val();
					if (data == 'false') {
						$('#joinId').css('background', 'red');
						idFlag = "isDuplicated"
					} else {
						$('#joinId').css('background', 'lightgreen');
						idFlag = "isOk";
					}			
				},
				error : function(exception) {
					alert("ajax id error : " + exception.message);
				}
			})
		})
		
		function checkEmailFunction(email) {
			var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    		return re.test(email);
		}

		var checkEmail = false;
		var emailFlag;
		$('#joinEmail').keyup(function() {
			var email = $('#joinEmail').val();
			$.ajax({
				url : "/RealTimeNews/ajax.do",
				type : "post",
				data : "action=checkEmail&email=" + email,
				datatype : "text",
				success : function(data) {
					if (checkEmailFunction(email) == false) {
						$('#joinEmail').css('background', 'red');
						emailFlag = 'isWrongForm';
					} else{
						if (data == 'false') {
							$('#joinEmail').css('background', 'red');
							emailFlag = 'isDuplicated';
						} else if (data == 'true') {
							$('#joinEmail').css('background', 'lightgreen');
							emailFlag = 'isOk';
						}
					}
				},
				error : function(exception) {
					alert("ajax email error : " + exception.message);
				}
			})
		})
		//////////////////////////////////////////////////////////////////////////////
		function checkPassword(id, password) {
			if (!/^[a-zA-Z0-9]{5,15}$/.test(password)) {
				$('#pwCheckMessage').html('숫자와 영문자 조합으로 5~15자리를 사용해야 합니다.');
				return false;
			}
			var checkNumber = password.search(/[0-9]/g);
			var checkEnglish = password.search(/[a-z]/ig);

			if (checkNumber < 0 || checkEnglish < 0) {
				$('#pwCheckMessage').html('숫자와 영문자를 혼용하여야 합니다.');
				return false;
			}
			if (/(\w)\1\1\1/.test(password)) {
				$('#pwCheckMessage').html('같은 문자를 4번 이상 사용하실 수 없습니다.');
				return false;
			}
			if (password.search(id) > -1) {
				$('#pwCheckMessage').html('비밀번호에 아이디가 포함되었습니다.');
				return false;
			}
			return true;
		}
		$('#joinPassword').keyup(function() {
			var id = $('#joinId').val();
			var password = $('#joinPassword').val();
			if (checkPassword(id, password) == true) {
				$('#pwCheckMessage').html('사용가능한 비밀번호입니다.');
				$('#pwCheckMessage').removeClass('shake')
				$('#pwCheckMessage').addClass('success');
			} else {
				$('#pwCheckMessage').removeClass('success')
				$('#pwCheckMessage').addClass('red');
			}
		})
		$('#joinConfirm_password').keyup(
				function() {
					if ($('#joinPassword').val() == $('#joinConfirm_password').val() && $('#joinPassword').val() != "" && $('#joinConfirm_password').val() != "") {
						$('#pwCheckMessage').html('비밀번호가 일치합니다.');
						$('#pwCheckMessage').removeClass('shake')
						$('#pwCheckMessage').addClass('success');
					} else {
						$('#pwCheckMessage').html('비밀번호가 일치하지 않습니다.');
						$('#pwCheckMessage').removeClass('success')
						$('#pwCheckMessage').addClass('red');
					}
				})
				
		$('#register-submit').click(function() {
			$('#pwCheckMessage').html("");	
			$('#pwCheckMessage').removeClass('shake animated red success');		
		})
		$('#register-form').submit(
			function() {
				if ($('#joinId').val() == ""|| $('#joinId').val().length < 5) {
					$('#pwCheckMessage').html('5글자 이상의 아이디를 입력해주세요.');
					$('#pwCheckMessage').addClass('shake animated');
					$('#joinId').focus();
					return false;
				} else if ($('#joinUsername').val() == "") {
					$('#pwCheckMessage').html('이름을 입력해주세요.');
					$('#pwCheckMessage').addClass('shake animated');
					$('#joinUsername').focus();
					return false;
				} else if ($('#joinEmail').val() == "") {
					$('#pwCheckMessage').html('이메일을 입력해주세요.');
					$('#pwCheckMessage').addClass('shake animated');
					$('#joineMail').focus();
					return false;
				} else if(emailFlag=='isWrongForm'){
					$('#pwCheckMessage').html('이메일 형식을 확인해주세요.');
					$('#pwCheckMessage').addClass('shake animated');
					$('#joineMail').focus();
					return false;
				} else if(emailFlag=='isDuplicated'){
					$('#pwCheckMessage').html('중복된 이메일입니다.');
					$('#pwCheckMessage').addClass('shake animated');
					$('#joineMail').focus();
					return false;
				} else if ($('#joinPassword').val() == "") {
					$('#pwCheckMessage').html('Password을 입력해주세요.');
					$('#pwCheckMessage').addClass('shake animated');
					$('#joinPassword').focus();
					return false;
				} else if ($('#joinPassword').val() != ($('#joinConfirm_password').val())) {
					$('#pwCheckMessage').html('비밀번호가 불일치합니다.');
					$('#pwCheckMessage').addClass('shake animated');
					$('#confirmPassword').focus();
					return false;
				} else if (!checkPassword($('#joinId').val(),$('#joinPassword').val())) {
					$('#pwCheckMessage').addClass('shake animated');
					return false;
				} else if ($('#joinId').val().lenth < 5){
					$('#pwCheckMessage').html('5글자 이상의 아이디를 입력하세요.');
					$('#pwCheckMessage').addClass('shake animated');
					$('#confirmPassword').focus();
					return false;
				} else if (idFlag == 'isDuplicated'){
					$('#pwCheckMessage').html('중복된 아이디입니다.');
					$('#pwCheckMessage').addClass('shake animated');
					$('#confirmPassword').focus();
					return false;
				} else return true;
			})
			
		$('#login-submit').click(function() {
			$('#loginCheckMessage').html('');
			$('#loginCheckMessage').removeClass('shake animated');
		})
		$('#login-form').submit(function() {
			if ($('#id').val() == "") {
				$('#loginCheckMessage').html('ID를 입력해주세요.');
				$('#loginCheckMessage').addClass('shake animated');
				$('#joinId').focus();
				return false;
			} else if ($('#password').val() == "") {
				$('#loginCheckMessage').html('Password를 입력해주세요.');
				$('#loginCheckMessage').addClass('shake animated');
				$('#joinId').focus();
				return false;
			} else
				return true;
		})
	});
</script>
</head>
<body>
<center>
	<div class="container">
    	<div class="row">
			<div class="col-md-6 col-md-offset-3">
				<div class="panel panel-login">
					<div class="panel-heading">
						<div class="row">
							<p class="col-xs-6">
								<a href="#" class="active" id="login-form-link">Login</a>
							</p>
							<p class="col-xs-6">
								<a href="#" id="register-form-link">Register</a>
							</p>
						</div>
						<hr>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-lg-12">
								<form id="login-form" action="member.do" method="post" role="form" style="display: block;">
									<input type="hidden" name="action" value="login">
									<div class="form-group">
										<input type="text" name="id" id="id" tabindex="1" class="form-control" placeholder="ID" 
												value='<c:if test="${cookie.rememberId != null}">${cookie.rememberId.value}</c:if>'>
									</div>
									<div class="form-group">
										<input type="password" name="password" id="password" tabindex="2" class="form-control" placeholder="Password">
									</div>
									<div class="form-group text-center">
										<input type="checkbox" tabindex="3" class="" name="remember" id="remember">
										<label for="remember"> Remember Me</label>
									</div>
									<div class="form-group">
										<div class="row">
											<div class="col-sm-6 col-sm-offset-3">
												<input type="submit" name="login-submit" id="login-submit" tabindex="4" class="form-control btn btn-login" value="Log In">
											</div>
										</div>
									</div>
									<div id="loginCheckMessage">
										<c:if test="${login_flag==false}"><p class="shake animated">로그인 정보를 확인해주세요.</p></c:if>
									</div>
									<div class="form-group">
										<div class="row">
											<div class="col-lg-12">
												<div class="text-center">
													<a href="member.do?action=forgotId" tabindex="5" class="forgot-id">Forgot Id?</a>
												</div>
											</div>
										</div>
									</div>
									<div class="form-group">
										<div class="row">
											<div class="col-lg-12">
												<div class="text-center">
													<a href="member.do?action=forgotPassword" tabindex="5" class="forgot-password">Forgot Password?</a>
												</div>
											</div>
										</div>
									</div>
								</form>
								<form id="register-form" action="member.do" method="post" role="form" style="display: none;">
									<input type="hidden" name="action" value="join">
									<div class="form-group">
										<input type="text" name="id" id="joinId" tabindex="1" class="form-control" placeholder="ID" value="">
									</div>
									<div class="form-group">
										<input type="text" name="username" id="joinUsername" tabindex="1" class="form-control" placeholder="Username" value="">
									</div>
									<div class="form-group">
										<input type="email" name="email" id="joinEmail" tabindex="1" class="form-control" placeholder="Email Address" value="">
									</div>
									<div class="form-group">
										<input type="password" name="password" id="joinPassword" tabindex="2" class="form-control" placeholder="Password">
									</div>
									<div class="form-group">
										<input type="password" name="confirm_password" id="joinConfirm_password" tabindex="2" class="form-control" placeholder="Confirm Password">
									</div>
									<div id="pwCheckMessage"></div>
									<div class="form-group">
										<div class="row">
											<div class="col-sm-6 col-sm-offset-3">
												<input type="submit" name="register-submit" id="register-submit" tabindex="4" class="form-control btn btn-register" value="Register Now">
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
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