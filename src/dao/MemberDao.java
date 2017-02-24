package dao;

import java.sql.*;

import vo.MemberVO;

public class MemberDao {
	// Dao ��ü ��ü�� �������� ���� �ʿ䰡 �����Ƿ�
	// �ϳ��� instance ��ü�� ���� ���� �ϴ� Singleton
	private static MemberDao instance = new MemberDao();
	private MemberDao(){}
	public static MemberDao getInstance(){
		return instance;
	}
///////////////////////////////////////////////////////////
	public int insert(MemberVO member){
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			con = DBHelper.getConnection();
			String sql = "INSERT INTO MEMBER"
					+ "(ID,PASSWORD,NAME,EMAIL) VALUES(?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getEmail());
			
			result = pstmt.executeUpdate(); // SQL ����
		} catch (SQLException e) {
			System.out.println("insert member ����");
			e.printStackTrace();
		} finally{
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}
		return result;
	}
	
	public MemberVO select(String id){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberVO result = null;
		try {
			con = DBHelper.getConnection();
			String sql = "SELECT ID,PASSWORD,NAME,EMAIL "
					+ "FROM MEMBER WHERE ID=?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, id);
			rs = pstmt.executeQuery(); // SQL ����
			if(rs.next()){
				result = new MemberVO();
				result.setId(rs.getString(1));
				result.setPassword(rs.getString(2));
				result.setName(rs.getString(3));
				result.setEmail(rs.getString(4));		
			}
		} catch (SQLException e) {
			System.out.println("select member ����");
			e.printStackTrace();
		} finally{
			DBHelper.closeResultSet(rs);
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}
		return result;
	}	
	
	public MemberVO selectEmail(String email){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberVO result = null;
		try {
			con = DBHelper.getConnection();
			String sql = "SELECT ID,PASSWORD,NAME,EMAIL "
					+ "FROM MEMBER WHERE EMAIL=?";
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, email);
			rs = pstmt.executeQuery(); // SQL ����
			if(rs.next()){
				result = new MemberVO();
				result.setId(rs.getString(1));
				result.setPassword(rs.getString(2));
				result.setName(rs.getString(3));
				result.setEmail(rs.getString(4));				
			}
		} catch (SQLException e) {
			System.out.println("selectEmail ����");
			e.printStackTrace();
		} finally{
			DBHelper.closeResultSet(rs);
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}
		return result;
	}
	
	public int update(String id, String password, String email){
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			con = DBHelper.getConnection();
			String sql = "UPDATE MEMBER SET PASSWORD=?, EMAIL=? WHERE ID = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, password);
			pstmt.setString(2, email);
			pstmt.setString(3, id);
			
			result = pstmt.executeUpdate(); // SQL ����
		} catch (SQLException e) {
			System.out.println("update member ����");
			e.printStackTrace();
		} finally{
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}
		return result;
	}
}
