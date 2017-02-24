package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import service.CommentService;
import vo.CommentList;
import vo.CommentVO;

@WebServlet("/co.do")
public class CommentController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		execute(req, resp);
		req.setCharacterEncoding("UTF-8");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		execute(req, resp);
		req.setCharacterEncoding("UTF-8");
	}

	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CommentService service = CommentService.getInstance();

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		if (action.equals("writeco")) {
			
			int newsNo = 0;
			resp.setContentType("text/xml;charset=UTF-8");
			PrintWriter out = resp.getWriter();
			String content = req.getParameter("content");
			String newsNoStr = req.getParameter("newsNo");
			HttpSession session = req.getSession();
			String id = (String) session.getAttribute("loginId");
			
			if (newsNoStr != null) {
				newsNo = Integer.parseInt(newsNoStr);
			}
			System.out.println("��۳���:" + content);
			System.out.println("������ȣ:" + newsNoStr);
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			CommentVO comment = new CommentVO();
			comment.setId(id);
			comment.setContent(content);
			comment.setNewsNo(newsNo);
			comment.setWriteDate(date.format(new Date()));
			
			service.writeCo(comment);
			out.print("<commentlist>");
			out.print("<comment>");
			out.print("<id>"+comment.getId()+"</id>");
			out.print("<content>"+comment.getContent()+"</content>");
			out.print("<newsno>"+comment.getNewsNo()+"</newsno>");
			out.print("<writedate>"+comment.getWriteDate()+"</writedate>");
			out.print("</comment>");
			out.print("</commentlist>");
			
			
			

		}else if (action.equals("viewList")) {
			int newsNo = 0;
			int startRow = 0;
			int endRow = 0;
			resp.setContentType("text/xml;charset=UTF-8");
			PrintWriter out = resp.getWriter();
			String newsNoStr = req.getParameter("newsNo");
			String startRowStr = req.getParameter("startRow");
			String endRowStr = req.getParameter("endRow");
			
			if(startRowStr != null && startRowStr.length() > 0){
				startRow = Integer.parseInt(startRowStr);
			}
			if(endRowStr != null && endRowStr.length() > 0){
				endRow = Integer.parseInt(endRowStr);
			}
			if (newsNoStr != null) {
				newsNo = Integer.parseInt(newsNoStr);
			}
			System.out.println(startRow+","+endRow+","+newsNo);
			CommentList coList = new CommentList();
			List<CommentVO> commentList = new ArrayList<>();
			commentList = service.coList(newsNo, startRow, endRow);
			try {
				JAXBContext jc = JAXBContext.newInstance(CommentList.class);		//Object -> XML
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				for(CommentVO c : commentList){
					coList.getComment().add(c);
				}
				marshaller.marshal(coList, out);
//				marshaller.marshal(newsList, System.out);
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (action.equals("viewListMore")) {
			int newsNo = 0;
			int startRow = 0;
			int endRow = 0;
			
			String newsNoStr = req.getParameter("newsNo");
			String startRowStr = req.getParameter("startRowComment");
			String endRowStr = req.getParameter("endRow");
			
			if(startRowStr != null && startRowStr.length() > 0){
				startRow = Integer.parseInt(startRowStr);
			}
			if(endRowStr != null && endRowStr.length() > 0){
				endRow = Integer.parseInt(endRowStr);
			}
			if (newsNoStr != null) {
				newsNo = Integer.parseInt(newsNoStr);
			}
			System.out.println(startRow+","+endRow+","+newsNo);
			CommentList coList = new CommentList();
			List<CommentVO> commentList = new ArrayList<>();
			commentList = service.coList(newsNo, startRow, endRow);
			try {
				JAXBContext jc = JAXBContext.newInstance(CommentList.class);		//Object -> XML
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				for(CommentVO c : commentList){
					coList.getComment().add(c);
				}
				 if(commentList.size()>0) {
						resp.setContentType("text/xml;charset=UTF-8");
						PrintWriter out = resp.getWriter();
						marshaller.marshal(coList, out);
					} else {
						PrintWriter out2 = resp.getWriter();
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
