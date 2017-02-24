package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import naverRealTimeKeyword.NaverRealRanking;
import vo.RealTimeKeyword;

@WebServlet(urlPatterns="/naver.do")
public class NaverServlet extends HttpServlet{
	NaverRealRanking n = NaverRealRanking.getInstance();
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=UTF-8");
		List<RealTimeKeyword> realTimeKeywordList = n.getRealRanking();
		PrintWriter out = response.getWriter();
		out.print("<naverkeyword>");
		for(RealTimeKeyword keyword : realTimeKeywordList){
			out.print("<keyword>");
			out.print("<ranking>"+keyword.getRanking()+"</ranking>");
			out.print("<searchkey>"+keyword.getKeyword()+"</searchkey>");
			out.print("</keyword>");
		}
		out.print("</naverkeyword>");
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=UTF-8");
		List<RealTimeKeyword> realTimeKeywordList = n.getRealRanking();
		PrintWriter out = response.getWriter();
		out.print("<naverkeyword>");
		for(RealTimeKeyword keyword : realTimeKeywordList){
			out.print("<keyword>");
			out.print("<ranking>"+keyword.getRanking()+"</ranking>");
			out.print("<searchkey>"+keyword.getKeyword()+"</searchkey>");
			out.print("</keyword>");
		}
		out.print("</naverkeyword>");
 	}
}
