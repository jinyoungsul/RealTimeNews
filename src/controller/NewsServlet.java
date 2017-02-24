package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import service.NewsService;
import vo.News;
import vo.NewsList;

@WebServlet(urlPatterns="/news.do")
public class NewsServlet extends HttpServlet{
	NewsService service = NewsService.getInstance();
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		execute(request, response);
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		execute(request, response);
	}
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String action=request.getParameter("action");
		String viewPath = "";
		if(action==null || action.equals("main")){
			String startRowStr = request.getParameter("startRow");
			String endRowStr = request.getParameter("endRow");
			String loginId = request.getParameter("loginId");
			int startRow = 0;
			int endRow = 0;
			if(startRowStr != null && startRowStr.length() > 0){
				startRow = Integer.parseInt(startRowStr);
			}
			if(endRowStr != null && endRowStr.length() > 0){
				endRow = Integer.parseInt(endRowStr);
			}
			String searchKeyword = request.getParameter("searchkeyword");
			NewsList newsList = new NewsList();
			List<News> news = new ArrayList<>();
			if(searchKeyword != null){
				news = service.keywordNewsList(startRow, endRow, searchKeyword,loginId);
			} else {
				news = service.newsList(startRow,endRow,loginId);
			}
			
			try {
				JAXBContext jc = JAXBContext.newInstance(NewsList.class);		//Object -> XML
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				for(News n : news){
					newsList.getNews().add(n);
				}
				 if(news.size()>0) {
					response.setContentType("text/xml;charset=UTF-8");
					PrintWriter out = response.getWriter();
					marshaller.marshal(newsList, out);
				} else {
					PrintWriter out2 = response.getWriter();
					out2.write("empty");
					
				}
//				marshaller.marshal(newsList, System.out);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(action.equals("search")){
			String searchKeyword = request.getParameter("searchkeyword");
			System.out.println("페이지 이동시 검색어 : "+searchKeyword);
			viewPath = "search_news.jsp";
			request.setAttribute("keyword",searchKeyword );
			HttpSession session = request.getSession();
			session.setAttribute("loginId", session.getAttribute("loginId"));
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
			dispatcher.forward(request, response);
		} else if(action.equals("bookmark")){
			String newsNo = request.getParameter("newsNo");
			String loginId = request.getParameter("loginId");
			service.bookmark(loginId,newsNo);
		} else if(action.equals("viewBookmark")){
			viewPath = "bookmark_news.jsp";
			HttpSession session = request.getSession();
			session.setAttribute("loginId", session.getAttribute("loginId"));
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
			dispatcher.forward(request, response);
		} else if(action.equals("selectBookmark")){
			String startRowStr = request.getParameter("startRow");
			String endRowStr = request.getParameter("endRow");
			String loginId = request.getParameter("loginId");
			int startRow = 0;
			int endRow = 0;
			if(startRowStr != null && startRowStr.length() > 0){
				startRow = Integer.parseInt(startRowStr);
			}
			if(endRowStr != null && endRowStr.length() > 0){
				endRow = Integer.parseInt(endRowStr);
			}
			NewsList newsList = new NewsList();
			List<News> news = new ArrayList<>();
			
			news = service.bookmarkNewsList(startRow,endRow,loginId);
			
			
			try {
				JAXBContext jc = JAXBContext.newInstance(NewsList.class);		//Object -> XML
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				for(News n : news){
					newsList.getNews().add(n);
				}
				 if(news.size()>0) {
					response.setContentType("text/xml;charset=UTF-8");
					PrintWriter out = response.getWriter();
					marshaller.marshal(newsList, out);
				} else {
					PrintWriter out2 = response.getWriter();
					out2.write("empty");
					
				}
//				marshaller.marshal(newsList, System.out);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
