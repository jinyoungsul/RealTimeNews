package service;

import java.util.List;

import dao.CommentDao;
import vo.CommentVO;

public class CommentService {

	private static CommentService instance = new CommentService();

	public static CommentService getInstance() {
		return instance;
	}

	private CommentService() {
	}

	private CommentDao dao = CommentDao.getInstance();

	public List<CommentVO> coList(int newsNo, int startRow, int endRow) { // ��� ���
																			
		List<CommentVO> commentList = dao.selectCoList(newsNo, startRow, endRow);

		return commentList;
	}

	public int writeCo(CommentVO comment) { //��۾���
		int result = dao.insert(comment);
		return result;
	}

//	public boolean modifyCo(CommentVO comment) { //��� ����
//		CommentVO original = dao.selectCo(comment.getCommentNum());
//		if (original.getId().equals(comment.getId())) {
//			dao.update(comment);
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	public boolean deleteCo(String id, int commentNum) { //��ۻ���
//		CommentVO original = dao.selectCo(commentNum);
//		if (id != null && id.equals(original.getId())) {
//			dao.delete(commentNum);
//			return true;
//		} else {
//			return false;
//		}
//	}

}
