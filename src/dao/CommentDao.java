package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import vo.CommentVO;

public class CommentDao {
	
	private static CommentDao instance = new CommentDao();
	
	public static CommentDao getInstance(){
		return instance;
	}
	private CommentDao(){}
	
	public List<CommentVO> selectCoList(int newsNo ,int startRow,int endRow){ //목록
		Connection con  = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<CommentVO>commentList = new ArrayList<>();
		
		con = DBHelper.getConnection();
		try {
		String sql = "SELECT * FROM COMMENT WHERE NEWS_NO=? ORDER BY COMMENT_NUM DESC LIMIT ?,?";
			pstmt = con.prepareStatement(sql);
	        pstmt.setInt(1, newsNo);
	        pstmt.setInt(2, startRow);
	        pstmt.setInt(3, endRow);
	        
	        rs = pstmt.executeQuery();
	        while(rs.next()){
	        	CommentVO co = new CommentVO();
	        	co.setCommentNum(rs.getInt(1));
	        	co.setId(rs.getString(2));
	        	co.setNewsNo(rs.getInt(3));
	        	co.setContent(rs.getString(4));
	        	co.setWriteDate(rs.getString(5));
	        	
	        	commentList.add(co);
	        }
		
		} catch (SQLException e) {
			System.out.println("selectCoList error");
			e.printStackTrace();
		}finally {
			DBHelper.closeResultSet(rs);
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}return commentList;
	}
	public int insert(CommentVO comment){
		Connection con = null;
		PreparedStatement pstmt = null;
		int result=0;
		
		con = DBHelper.getConnection();
		try {
			System.out.println(comment);
		String sql ="INSERT INTO COMMENT(ID,NEWS_NO,CONTENT,WRITE_DATE)" 
		             + "VALUES(?,?,?,str_to_date(?,'%Y-%m-%d %H:%i:%s'))";
			pstmt = con.prepareStatement(sql);
		    pstmt.setString(1, comment.getId());
		    pstmt.setInt(2, comment.getNewsNo());
		    pstmt.setString(3, comment.getContent());
		    pstmt.setString(4, comment.getWriteDate());
		    
		    result = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("comment insert error");
			e.printStackTrace();
		}finally {
			DBHelper.closePreparedStatment(pstmt);;
			DBHelper.closeConnection(con);
		}return result;
	}
		public int update(CommentVO comment){ //수정
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			con = DBHelper.getConnection();
			String sql = 
					  "UPDATE BOARD "
					+ "SET CONTENT=? "
					+ "WHERE COMMENT_NUM=?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, comment.getContent());
			pstmt.setInt(2, comment.getCommentNum());
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(" comment update error");
			e.printStackTrace();
		} finally{
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}
		return result;
	}
	public int delete(int commentNum){ //삭제
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			con = DBHelper.getConnection();
			String sql = 
					  "DELETE FROM COMMENT "
					+ "WHERE COMMENT_NUM=?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, commentNum);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("comment delete error");
			e.printStackTrace();
		} finally{
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}
		return result;
	}
//	public CommentVO selectCo(int commentNum) { // 읽기
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//	    CommentVO vo = null;
//
//		con = DBHelper.getConnection();
//
//		try {
//			String sql = "SELECT * FROM COMMENT WHERE COMMENT_NUM=?";
//			pstmt = con.prepareStatement(sql);
//			pstmt.setInt(1, commentNum);
//
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				vo = new CommentVO();
//				vo.setCommentNum(rs.getInt(1));
//				vo.setId(rs.getString(2));
//				vo.setNewsNo(rs.getInt(3));
//				vo.setContent(rs.getString(4));
//				vo.setWriteDate(rs.getTimestamp(5));
//			}
//
//		} catch (SQLException e) {
//			System.out.println("selectCo error");
//			e.printStackTrace();
//		} finally {
//			DBHelper.closePreparedStatment(pstmt);
//			DBHelper.closeConnection(con);
//		}
//		return vo;
//	}

}
