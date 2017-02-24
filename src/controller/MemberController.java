package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.plaf.basic.BasicTreeUI.CellEditorHandler;

import service.MemberService;
import vo.MemberVO;

@WebServlet("/member.do")
public class MemberController extends HttpServlet {
	private MemberService service = MemberService.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		execute(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// URL占쏙옙 占싣댐옙 post 占쏙옙커占쏙옙占쏙옙占� 占싼깍옙 占쏙옙占쌘듸옙占쏙옙 占십울옙占쏙옙 占쏙옙 占쏙옙占쏙옙.
		req.setCharacterEncoding("UTF-8");
		execute(req, resp);
	}

	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// action 占식띰옙占쏙옙庫占� 占쏙옙청占쏙옙 占쏙옙占쏙옙占쏙옙 占식억옙
		String action = request.getParameter("action");
		String viewPath = "";
		
		if(action.equals("index") || action == null) {
			viewPath = "index.jsp";
		} else if(action.equals("main") || action == null) {
			viewPath = "main.jsp";
		} else if (action.equals("join")) {
			// 占쌉뤄옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 회占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙
			MemberVO member = new MemberVO();
			member.setId(request.getParameter("id"));
			member.setEmail(request.getParameter("email"));
			member.setName(request.getParameter("username"));
			member.setPassword(request.getParameter("password"));
			if (service.join(member) == true) {
				viewPath = "index.jsp";
			} else {
				viewPath = "default.jsp";
			}
		} else if (action.equals("login")) {
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String isRemember = request.getParameter("remember");
			MemberVO member = service.selectMember(id);
			boolean login_flag = service.login(id, password);
			if (login_flag == true) {
				String name = member.getName();
				HttpSession session = request.getSession();
				session.setAttribute("loginId", id);
				session.setAttribute("name", name);
				// make Cookie;
				if(isRemember != null) {
					Cookie c = new Cookie("rememberId", id);
					c.setMaxAge(24*60*60); // 쿠키 남아있는 기간!
					response.addCookie(c);
				}
				viewPath = "main.jsp";
			} else if(login_flag == false){
				request.setAttribute("login_flag", "false");
				viewPath = "index.jsp";
			}
		} else if (action.equals("logout")) {
			HttpSession session = request.getSession();
			session.invalidate();
			viewPath = "index.jsp";
		} else if (action.equals("forgotId")) {
			viewPath = "FindId.jsp";
		} else if (action.equals("forgotPassword")) {
			viewPath = "FindPassword.jsp";
		} else if(action.equals("findId")){
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			boolean idFlag = service.confirmEmail(name, email);
			if(idFlag == true) {
				request.setAttribute("idFlag", idFlag);
				String findedId = service.findId(email);
				request.setAttribute("name", name);
				request.setAttribute("findedId", findedId);
			} else if(idFlag == false) {
				request.setAttribute("idFlag", idFlag);
			}
			viewPath = "FindId.jsp";
		} else if (action.equals("checkPassword")) {
			String id = request.getParameter("id");
			String email = request.getParameter("email");
			boolean password_flag = service.checkPassword(id, email);
			if (password_flag == true) {
				String password = service.findPassword(id);
				String name = service.findName(id);
				request.setAttribute("name", name);
				request.setAttribute("password", password);
				request.setAttribute("password_flag", "true");
			} else if(password_flag == false){
				request.setAttribute("password_flag", "false");
			}
			viewPath = "FindPassword.jsp";
		} else if (action.equals("updateMember")) {
			boolean checkPassword = true;
			request.setAttribute("checkPassword", checkPassword);
			viewPath = "updateMember.jsp";
		} else if (action.equals("confirmPassword")) {
			String insertedPassword = request.getParameter("password");
			HttpSession session = request.getSession();
			String id = (String) session.getAttribute("loginId");
			MemberVO member = service.selectMember(id);
			String savedPassword = member.getPassword();
			boolean checkPassword;
			boolean passwordFlag;
			if(insertedPassword.equals(savedPassword)){
				request.setAttribute("member", member);
				checkPassword = false;
				passwordFlag = true;
			} else {
				checkPassword = true;
				passwordFlag = false;
			}
			request.setAttribute("id", id);
			request.setAttribute("checkPassword", checkPassword);
			request.setAttribute("passwordFlag", passwordFlag);
			viewPath = "updateMember.jsp";
		} else if (action.equals("editAccount")) {
			String password = request.getParameter("password");
			String email = request.getParameter("email");
			HttpSession session = request.getSession();
			String id = (String) session.getAttribute("loginId");
			boolean updateResult = service.updateMember(id,password,email);
			MemberVO member = service.selectMember(id);
			request.setAttribute("id", id);
			request.setAttribute("updateResult", updateResult);
			request.setAttribute("member", member);
			viewPath = "updateMember_result.jsp";
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
		dispatcher.forward(request, response);
	}
}
