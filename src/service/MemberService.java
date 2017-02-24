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
	// dao Ȱ���� ���� �����Ƿ� dao�� ����� ������.
	private MemberDao dao = MemberDao.getInstance();
	
	public MemberVO selectMember(String id) {
		MemberVO Member = dao.select(id);
		return Member;
	}

	// ȸ������ �޼ҵ�
	public boolean join(MemberVO member) {
		MemberVO savedMember = dao.select(member.getId());

		if (savedMember != null) {
			// �Է��� ���̵��� ȸ���� �̹� �����ͺ��̽��� ����.
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

	// �α��� �޼ҵ�
	public boolean login(String id, String password) {
		MemberVO savedMember = dao.select(id);

		if (savedMember != null && savedMember.getPassword().equals(password)) {
			// �ش� ���̵� �����ϰ� ��й�ȣ ��ġ�ϸ� ����
			return true;
		} else {
			return false;
		}
	}

	public boolean checkId(String id) {
		MemberVO savedMember = dao.select(id);

		if (savedMember != null) {
			return false; // ��� �Ұ���
		} else {
			return true; // ��밡��
		}
	}

	public boolean checkEmail(String email) {
		MemberVO savedMember = dao.selectEmail(email);
		if (savedMember != null) {
			return false; // ��� �Ұ���
		} else {
			return true; // ��밡��
		}
	}

	public boolean confirmEmail(String name, String email) {
		MemberVO savedMember = dao.selectEmail(email);
		if (savedMember != null && savedMember.getName().equals(name)) {
			return true; // �ش� �̸����� ������ ���̵� ������
		} else {
			return false; // �ش� �̸����� ������ ���̵� ����
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
