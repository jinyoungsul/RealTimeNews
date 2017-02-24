package service;

import dao.MemberDao;
import vo.MemberVO;

public class MemberService {
	// singleton
	private static MemberService instance = new MemberService();

	private MemberService() {
	}

	public static MemberService getInstance() {
		return instance;
	}

	/////////////////////////////////////////////////////////////
	// dao 활용할 일이 많으므로 dao를 멤버로 유지함.
	private MemberDao dao = MemberDao.getInstance();
	
	public MemberVO selectMember(String id) {
		MemberVO Member = dao.select(id);
		return Member;
	}

	// 회원가입 메소드
	public boolean join(MemberVO member) {
		MemberVO savedMember = dao.select(member.getId());

		if (savedMember != null) {
			// 입력한 아이디의 회원이 이미 데이터베이스에 있음.
			return false;
		} else {
			int result = dao.insert(member);
			if (result == 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	// 로그인 메소드
	public boolean login(String id, String password) {
		MemberVO savedMember = dao.select(id);

		if (savedMember != null && savedMember.getPassword().equals(password)) {
			// 해당 아이디가 존재하고 비밀번호 일치하면 성공
			return true;
		} else {
			return false;
		}
	}

	public boolean checkId(String id) {
		MemberVO savedMember = dao.select(id);

		if (savedMember != null) {
			return false; // 사용 불가능
		} else {
			return true; // 사용가능
		}
	}

	public boolean checkEmail(String email) {
		MemberVO savedMember = dao.selectEmail(email);
		if (savedMember != null) {
			return false; // 사용 불가능
		} else {
			return true; // 사용가능
		}
	}

	public boolean confirmEmail(String name, String email) {
		MemberVO savedMember = dao.selectEmail(email);
		if (savedMember != null && savedMember.getName().equals(name)) {
			return true; // 해당 이메일을 가지는 아이디가 존재함
		} else {
			return false; // 해당 이메일을 가지는 아이디가 없음
		}
	}

	public String findId(String email) {
		MemberVO savedMember = dao.selectEmail(email);
		return savedMember.getId();
	}

	public boolean checkPassword(String id1, String email) {
		MemberVO savedMember = dao.select(id1);
		if (savedMember == null) {
			return false;
		} else {
			if (savedMember.getEmail().equals(email)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean updateMember(String id, String password, String email) {
		int result = dao.update(id,password,email);
		if(result == 1){
			return true;
		} else {
			return false;
		}
	}
	
	public String findPassword(String id){
		MemberVO savedMember = dao.select(id);
		String password = savedMember.getPassword();
		return password;
	}
	
	public String findName(String id){
		MemberVO savedMember = dao.select(id);
		String name = savedMember.getName();
		return name;
	}
}
