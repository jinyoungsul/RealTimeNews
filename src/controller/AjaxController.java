package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.MemberService;

@WebServlet(urlPatterns = "/ajax.do")
public class AjaxController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		execute(req,resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		execute(req,resp);
	}

	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MemberService service = MemberService.getInstance();
		String action = request.getParameter("action");
		if (action.equals("checkId")) {
			String id = request.getParameter("id");
			boolean idFlag = service.checkId(id);
			if (id.length() < 5) {
				idFlag = false;
			}
			if (idFlag == false) {
				response.getWriter().write("false");
			} else if (idFlag == true) {
				response.getWriter().write("true");
			}
		} else if(action.equals("checkEmail")){
			String email = request.getParameter("email");
			boolean emailFlag = service.checkEmail(email);
			if (emailFlag == false) {
				response.getWriter().write("false");
			} else if (emailFlag == true) {
				response.getWriter().write("true");
			}
		}

	}
}
