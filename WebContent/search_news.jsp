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
<title>키워드 뉴스</title>

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
<!-- 모든 컴파일된 플러그인을 포함합니다 (아래), 원하지 않는다면 필요한 각각의 파일을 포함하세요 -->
<style type="text/css">
.yellow {
	background-color: yellow;
}

.white {
	color: white;
}

a.disabled {
	pointer-events: none;
	cursor: default;
}

/* #dropdownMenu1 { */
/* 	background-color: black; */
/* 	border-color: black; */
/* 	height: 50px; */
/* 	font-size: normal; */
/* } */
.black {
	background-color: #222222;
	border-color: #222222;
	height: 50px;
	font-size: normal;
}

 .commentBtn {
 	background-color : #59B2E0;
 	outline : none;
 	color : #fff;
 	font-size : 14px;
 	height : auto;
 	font-weight : normal;
 	padding : 10px 40px;
 	text-transform : uppercase;
 	border-color : #59B2E6;
 }
 body {
 	font-family : 'Malgun Gothic';
 	font-size : 10pt;
 }
</style>
<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.1.1.js"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function(){
		var startRow = 0;
		var endRow = 10;
		var loginId = $('#loginid').val();
		var searchkeyword = $('#searchkeyword').val();
		loadranking();
		loadnews();
		setInterval(function(){
			$.ajax({
				url : "naver.do",
				type : "post",
				success : function(data){
					$(data).find('keyword').each(function(index){
						var ranking = $(this).find('ranking').text();
						var searchkey = $(this).find('searchkey').text();
						var i = index+1;
						$('table#naverrank tr:eq('+i+') td:eq('+(i-i)+')').text(ranking);
						$('table#naverrank tr:eq('+i+') td:eq('+(i-i+1)+')').text(searchkey);
					})
				},
				error : function(){
					alert("error");
				}
			})
		},1000);
		$(window).scroll(function(){
			var dh = $(document).height();
			var wh = $(window).height();
			var wt = $(window).scrollTop();
			if(wt+wh==dh){
				loadnews();
			}
		});
		function loadnews(){
			$.ajax({
				url : "news.do",
				type : "post",
				data : {startRow : startRow , endRow : endRow , searchkeyword : searchkeyword , loginId : loginId},
				success : function(data){
					if(data!='empty'){
							$(data).find('news').each(function(){
							var imgUrl = $(this).find('imgUrl').text();
							var title = $(this).find('title').text();
							var content = $(this).find('content').text();
							var press = $(this).find('press').text();
							var mainTitle = $(this).find('mainTitle').text();
							var mainContent = $(this).find('mainContent').text();
							var keyword = $(this).find('keyword').text();
							var newsNo = $(this).find('newsNo').text();
							var id = $(this).find('id').text();
							var state = $(this).find('state').text();
							var result ="<tr>";
							result += "<td>" + "<img src='"+imgUrl +"'>"+ "</td>";
							result += "<td>" + title + "</td>";
							result += "<td>" + content + "</td>";
							result += "<td>";
							if(id==loginId && state == 'true'){
								result += "<button type='button' id='bookmarkbtn' class='btn btn-default btn-lg yellow' value='"+newsNo+"'>";
								result += "<span class='glyphicon glyphicon-bookmark'></span>";
								result += "</button>";
							} else {
								result += "<button type='button' id='bookmarkbtn' class='btn btn-default btn-lg' value='"+newsNo+"'>";
								result += "<span class='glyphicon glyphicon-bookmark'></span>";
								result += "</button>";
							}
							result += "<button type='button' id='detailbtn' class='btn btn-default btn-lg' value='"+newsNo+"'>";
							result += "<span class='glyphicon glyphicon-chevron-down'></span>";
							result += "</button>";
							result += "</td>";
							
							
							result += "<tr id='detail"+newsNo+ "' style='display:none'>";
							result += "<div style='padding-right: 100px; padding-left: 100px'>"
							result += "<td colspan='4' align='center'>";	//게시물 자세히 보기
							result += "<div><b>" + mainTitle + "</b></div>";
							result += "<div>" + mainContent + "</div>";
							result += "<div>" + press + "</div>";
							result += "<div>" + keyword + "</div>";
							
							//댓글
							result += "<div id='co_div"+newsNo+"'>";
							result += "<fieldset id='co_div"+newsNo+"'>";
							result += "<legend align='left' style='font-size: 12px;'>댓글 쓰기</legend>"
							result += "<input type='hidden' id='newsNo' value="+newsNo+">";
							result += "<textarea id='content_text"+newsNo+"' name='content' rows='3' cols='100' style='resize: none'></textarea>";
							result += "<div style='margin-top:3px;margin-bottom:3px;'>";
							result += "<button type='button' class='btn btn-info commentBtn' id='btnCo' value="+newsNo+">등록</button>";
							result += "</div>";
							result += "</fieldset>";
							result += "</div>";		
									
							result += "<div id='emptycomment"+newsNo+ "' style='display:none' class='alert alert-info alert-dismissible' role='alert'>";
							result += "<strong>Sorry!</strong> 더 이상 댓글이 없습니다.";
							result += "</div>";
										
										
										
							result += "<div style='margin-top:8px;'>";
							result += "<button type='button' class='moreCo btn btn-info commentBtn' id='10' value="+newsNo+">더보기</button>";
							result += "</div>";
							
							result += "</td>";
							result += "</tr>";
							result += "</tr>";
							
							
							
							$('table#newslist tbody tr:last').before(result);
							
						})
						startRow += endRow;
					}
					if(data=='empty'){
						$('#emptynews').show();				
					}
				},
				error : function(){
					alert("error");
				}
			})
		}
		function loadranking(){
			$.ajax({
				url : "naver.do",
				type : "post",
				dataType : "xml",
				success : function(data){
					$(data).find('keyword').each(function(index){
						var ranking = $(this).find('ranking').text();
						var searchkey = $(this).find('searchkey').text();
						var i = index+1;
						$('table#naverrank tr:eq('+i+') td:eq('+(i-i)+')').text(ranking);
						$('table#naverrank tr:eq('+i+') td:eq('+(i-i+1)+')').text(searchkey);
					})
				},
				error : function(){
					alert("error");
				}
			})
		}
		
		$(document).on('click','#detailbtn',function(){
			var newsNo = $(this).attr('value');
			$('#detail'+newsNo).toggle();
			if($('#detail'+newsNo).css('display')!="none"){
				var startRow = 0;
				var endRow = 5;
				$.ajax({
					url: "/RealTimeNews/co.do",
					type: "post",
					data: "action=viewList&newsNo="+newsNo+"&startRow="+startRow+"&endRow="+endRow,
					datatype: "xml",
					success: function(data){
						console.log("test:"+data);
						$(data).find('comment').each(function() {
							var id =
								$(this).find('id').text();
							var content =
								$(this).find('content').text();
							var writeDate =
								$(this).find('writeDate').text();
							var list ="<tr colspan='4'>";
							list += "<td>";
							list +="<div style='border: 1px dashed #48BAE4; height: auto; padding:10px;width: 650px;margin-top: 3px;'>";
							list += "<div>" + "<p style='font-weight:bold;' align='left'>"+ id +"</p>"+"</div>";
							list += "<div>" + "<p style='font-weight:normal;' align='left'>"+ content + "</p>"+"</div>";
							list += "<div>" + "<p style='font-weight:lighter;' align='left'>"+ writeDate + "</p>"+"</div>";
							list += "</div>";
							list +="</td>";
							list +="</tr>";
							
							$('div#co_div'+newsNo+'').append(list);
							
						})
						startRow += endRow;
					},
					error: function(exception){
					}
				})
			} else {
				$('div#co_div'+newsNo+' > tr').empty();
				$('#emptycomment'+newsNo).hide();
				$('button.moreCo[value='+newsNo+']').attr('id',10);
			}
			$(this).find('span').toggleClass("glyphicon glyphicon-chevron-down glyphicon glyphicon-chevron-up");
		})
		//북마크 버튼
		$(document).on('click','#bookmarkbtn',function(){
			var newsNo = $(this).attr('value');
			$(this).toggleClass("btn btn-default btn-lg btn btn-default btn-lg yellow");
			$.ajax({
				url : "news.do",
				type : "post",
				data : {newsNo : newsNo , loginId : loginId, action : 'bookmark'},
				success : function(){
					
				},
				error : function(){
					alert("error");
				}
			})
		})
		
		//댓글 등록 버튼
				$(document).on('click','#btnCo', function(){
					
					var newsNo = $(this).attr('value');
					var content = $('#content_text'+newsNo+'').val();
					$.ajax({
						url: "co.do",
						type: "post",
						data: "action=writeco&content="+content+"&newsNo="+newsNo,
						datatype: "xml",
						success: function(data){
							console.log("test:"+data);
							$(data).find('comment').each(function() {
								var id =
									$(this).find('id').text();
								var content =
									$(this).find('content').text();
								var writeDate =
									$(this).find('writedate').text();
								var comment ="<tr colspan='4'>";
								comment += "<td>";
								comment +="<div style='border: 1px dashed #48BAE4; height: auto; padding:10px;width: 650px;margin-top: 3px;'>";
								comment += "<div>" + "<p style='font-weight:bold;' align='left'>"+ id +"</p>"+"</div>";
								comment += "<div>" + "<p style='font-weight:normal;' align='left'>"+ content + "</p>"+"</div>";
								comment += "<div>" + "<p style='font-weight:lighter;' align='left'>"+ writeDate + "</p>"+"</div>";
								comment += "</div>";
								comment +="</td>";
								comment +="</tr>";
								
								console.log('div#co_div'+newsNo+'');
								$('fieldset#co_div'+newsNo+'').after(comment);
								
							})
							
						},
						error: function(exception){
						}
					})
					$('#content_text'+newsNo+'').val('');
				})
				//더보기 버튼
		$(document).on('click','.moreCo', function(){
			
			var newsNo = $(this).attr('value');
			var startRowComment = $(this).attr('id');
			var endRow = 5;
			$.ajax({
				url: "co.do",
				type: "post",
				data: "action=viewListMore&newsNo="+newsNo+"&startRowComment="+startRowComment+"&endRow="+endRow,
				datatype: "xml",
				success: function(data){
					console.log("test:"+data);
					if (data != 'empty') {
						$(data).find('comment').each(function() {
							var id =
								$(this).find('id').text();
							var content =
								$(this).find('content').text();
							var writeDate =
								$(this).find('writeDate').text();
							var newsNo =
								$(this).find('newsNo').text();
							var comment ="<tr colspan='4'>";
							comment += "<td>";
							comment +="<div class=moreComment style='border: 1px dashed #48BAE4; height: auto; padding:10px;width: 650px;margin-top: 3px;'>";
							comment += "<div>" + "<p style='font-weight:bold;' align='left'>"+ id +"</p>"+"</div>";
							comment += "<div>" + "<p style='font-weight:normal;' align='left'>"+ content + "</p>"+"</div>";
							comment += "<div>" + "<p style='font-weight:lighter;' align='left'>"+ writeDate + "</p>"+"</div>";
							comment += "</div>";
							comment +="</td>";
							comment +="</tr>";
							$('div#co_div'+newsNo+'').append(comment);
							$('button.moreCo[value='+newsNo+']').attr('id',Number(startRowComment)+10);
						})
					} if (data == 'empty') {
						$('#emptycomment'+newsNo).show();
					}
					
				},
				error: function(exception){
				}
			})
			
			$('#content_text').val('');
		})
		
		$('img').attr('src', 'image/logo_01.png');
		$('img').attr('width', '150');
		$('img').attr('height', '20');
		$('#bookmark').on('click',function(){
			location.href="news.do?action=viewBookmark";
		})
		$('#inputMessage').on('keypress',function(e){
					if(e.which == 13){
						send();
        $('#messageWindow').scrollTop($('#messageWindow')[0].scrollHeight);
					}
		})
	})
</script>
</head>
<body>
	<input type="hidden" id="loginid" value="${sessionScope.loginId }">
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="member.do?action=main"><img /></a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#" class="disabled"><i
							class="glyphicon glyphicon-star white"></i>${sessionScope.name}님
							안녕하세요!</a></li>
					<li>
						<div class="dropdown">
							<button class="btn btn-default dropdown-toggle black" type="button"
								id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false">
								<i class="glyphicon glyphicon-user white"> </i>
							</button>
							<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
								<li><a href="member.do?action=logout">로그아웃</a></li>
								<li><a href="member.do?action=updateMember">회원정보수정</a></li>
							</ul>
						</div>
					</li>
					<li>
						<button class="btn btn-default btn-lg black" type="button" id="bookmark">
							<i class="glyphicon glyphicon-bookmark white"></i>
						</button>
					</li>
				</ul>
				<form class="navbar-form navbar-center" action='news.do'
					method='post'>
					<input type="text" name='searchkeyword' class="form-control"
						placeholder="Search..."> <input type="hidden"
						value="search" name="action">
				</form>
			</div>
		</div>
	</nav>

	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-3 col-md-2 sidebar">
				<table class="table table-hover" id="naverrank">
					<tr>
						<td>순위</td>
						<td>검색어</td>
					</tr>
					<tr>				<!-- RANK1 -->
						<td></td>
						<td></td>
					</tr>
					<tr>				<!-- RANK2 -->
						<td></td>
						<td></td>
					</tr>
					<tr>				<!-- RANK3 -->
						<td></td>
						<td></td>
					</tr>
					<tr>				<!-- RANK4 -->
						<td></td>
						<td></td>
					</tr>
					<tr>				<!-- RANK5 -->
						<td></td>
						<td></td>
					</tr>
					<tr>				<!-- RANK6 -->
						<td></td>
						<td></td>
					</tr>
					<tr>				<!-- RANK7 -->
						<td></td>
						<td></td>
					</tr>
					<tr>				<!-- RANK8 -->
						<td></td>
						<td></td>
					</tr>
					<tr>				<!-- RANK9 -->
						<td></td>
						<td></td>
					</tr>
					<tr>				<!-- RANK10 -->
						<td></td>
						<td></td>
					</tr>
				</table>
				<div>
					<fieldset>
						<legend>miniChat</legend>
						<textarea id="messageWindow" rows="20" cols="25"
							readonly="readonly" style='resize: none'></textarea>
						<br> <input type="text" id="inputMessage" size="25" /> 
					</fieldset>
				</div>
			</div>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

				<h2 class="sub-header"> <input type="hidden" value="${keyword }" id='searchkeyword'>핫한 뉴스!</h2>
				<div class="table-responsive">
					<table class="table table-hover" id="newslist">
						<thead>
							<tr>
								<th>이미지</th>
								<th>제목</th>
								<th>내용</th>
							</tr>
						</thead>
						<tbody>
							<tr id='emptynews' style='display:none'>
								<td colspan='4'>
									<div class="alert alert-info alert-dismissible" role="alert">
  										<strong>Sorry!</strong> 더 이상 뉴스가 없습니다.
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var loginId = $('#loginid').val();
		var webSocket = new WebSocket(
				'ws://70.12.109.96:8088/RealTimeNews/broadcasting');
		var textarea = document.getElementById("messageWindow");
		var inputMessage = document.getElementById('inputMessage');

		webSocket.onmessage = function(e) { //웹 소켓에서 메시지가 날라왔을 때 호출되는 이벤트
			onMessage(event)
		};
		webSocket.onopen = function(e) { //웹 소켓이 연결되었을 때 호출되는 이벤트
			onOpen(event)
		};
		webSocket.onerror = function(e) { //웹 소켓이 에러가 났을 때 호출되는 이벤트
			onError(event)
		};
		webSocket.onclose = function(e) { //웹 소켓이 연결해제 되었을때 호출되는 이벤트
			var msg = {
					type : "logout",
					text : loginId +"님이 퇴장하셨습니다.",
					id : loginId
				};
			webSocket.send(JSON.stringify(msg));
			alert(msg.text);
		};
		function onMessage(event) {
			var msg = JSON.parse(event.data);
			switch(msg.type){
			case "message":
				textarea.value += msg.id + " : "+ msg.text +"\n";
				break;
			case "login":
				textarea.value += msg.text +"\n";
				break;
			case "logout":
				textarea.value += msg.text +"\n";
				break;
			}
		}
		function onOpen(event) {
			textarea.value += loginId +"님이 입장하셨습니다.\n";
			var msg = {
					type : "login",
					text : loginId +"님이 입장하셨습니다."
			};
			webSocket.send(JSON.stringify(msg));
		}
		function onError(event) {
			alert(event.data);
		}
		function send() {
			var msg = {
				type : "message",
				text : inputMessage.value,
				id : loginId,
			};
			textarea.value += loginId + " : "+ inputMessage.value +"\n";
			webSocket.send(JSON.stringify(msg));
			inputMessage.value = "";
		}
	</script>
</body>
</html>