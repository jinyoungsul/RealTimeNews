package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import vo.Bookmark;
import vo.News;

public class NewsDao {
	private static NewsDao instance = new NewsDao();
	private NewsDao(){}
	public static NewsDao getInstance(){
		return instance;
	}
	public News select(String newsLink) {
		Connection con = DBHelper.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		News news = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * ");
		sql.append("FROM NEWS ");
		sql.append("WHERE NEWS_LINK = ? ");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, newsLink);
			rs = pstmt.executeQuery();
			while(rs.next()){
				news = new News();
				news.setNewsNo(rs.getInt(1));
				news.setImgUrl(rs.getString(2));
				news.setTitle(rs.getString(3));
				news.setMainTitle(rs.getString(4));
				news.setContent(rs.getString(5));
				news.setMainContent(rs.getString(6));
				news.setPress(rs.getString(7));
				news.setNewsLink(rs.getString(8));
				news.setKeyword(rs.getString(9));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBHelper.closeResultSet(rs);
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}
		return news;
	}
	public int insertNews(News news) {
		Connection con = DBHelper.getConnection();
		PreparedStatement pstmt = null;
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO NEWS(img_url,title,main_title,content,main_content,press,news_link,keyword) ");
		sql.append("VALUES (?,?,?,?,?,?,?,?) ");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, news.getImgUrl());
			pstmt.setString(2, news.getTitle());
			pstmt.setString(3, news.getMainTitle());
			pstmt.setString(4, news.getContent());
			pstmt.setString(5, news.getMainContent());
			pstmt.setString(6, news.getPress());
			pstmt.setString(7, news.getNewsLink());
			pstmt.setString(8, news.getKeyword());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}
		return result;
	}
	public List<News> newsList(int startRow, int endRow, String loginId) {
		Connection con = DBHelper.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<News> newsList = new ArrayList<>();
		News news = null;
		StringBuffer sql = new StringBuffer();
		System.out.println("테스트");
		sql.append("SELECT n.NEWS_NO,n.IMG_URL,n.TITLE,n.MAIN_TITLE,n.CONTENT,n.MAIN_CONTENT,n.PRESS,n.NEWS_LINK,n.KEYWORD,b.ID,b.STATE ");
		sql.append("FROM news as n left join (select * from bookmark where id=? and state='true') as b ");
		sql.append("ON n.NEWS_NO = b.NEWS_NO ");
		sql.append("ORDER BY n.NEWS_NO DESC LIMIT ?,? ");
		
		//기존
//		sql.append("SELECT * ");
//		sql.append("FROM NEWS ");
//		sql.append("ORDER BY NEWS.NEWS_NO DESC LIMIT ?,? ");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, loginId);
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, endRow);
			rs = pstmt.executeQuery();
			while(rs.next()){
				news = new News();
				news.setNewsNo(rs.getInt(1));
				news.setImgUrl(rs.getString(2));
				news.setTitle(rs.getString(3));
				news.setMainTitle(rs.getString(4));
				news.setContent(rs.getString(5));
				news.setMainContent(rs.getString(6));
				news.setPress(rs.getString(7));
				news.setNewsLink(rs.getString(8));
				news.setKeyword(rs.getString(9));
				news.setId(rs.getString(10));
				news.setState(rs.getString(11));
				newsList.add(news);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBHelper.closeResultSet(rs);
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}
		return newsList;
	}
	public List<News> keywordNewsList(int startRow, int endRow, String searchKeyword, String loginId) {
		Connection con = DBHelper.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<News> newsList = new ArrayList<>();
		News news = null;
		StringBuffer sql = new StringBuffer();
//		기존
//		sql.append("SELECT * ");
//		sql.append("FROM NEWS ");
//		sql.append("WHERE KEYWORD LIKE ? ");
//		sql.append("ORDER BY NEWS_NO DESC LIMIT ?,? ");
		System.out.println("검색");
		sql.append("SELECT n.NEWS_NO,n.IMG_URL,n.TITLE,n.MAIN_TITLE,n.CONTENT,n.MAIN_CONTENT,n.PRESS,n.NEWS_LINK,n.KEYWORD,b.ID,b.STATE ");
		sql.append("FROM news as n left join (select * from bookmark where id=? and state='true') as b ");
		sql.append("ON n.NEWS_NO = b.NEWS_NO ");
		sql.append("WHERE KEYWORD LIKE ? ");
		sql.append("ORDER BY n.NEWS_NO DESC LIMIT ?,? ");
		System.out.println(loginId +","+searchKeyword);
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, loginId);
			pstmt.setString(2, '%'+searchKeyword+'%');
			pstmt.setInt(3, startRow);
			pstmt.setInt(4, endRow);
			rs = pstmt.executeQuery();
			while(rs.next()){
				news = new News();
				news.setNewsNo(rs.getInt(1));
				news.setImgUrl(rs.getString(2));
				news.setTitle(rs.getString(3));
				news.setMainTitle(rs.getString(4));
				news.setContent(rs.getString(5));
				news.setMainContent(rs.getString(6));
				news.setPress(rs.getString(7));
				news.setNewsLink(rs.getString(8));
				news.setKeyword(rs.getString(9));
				news.setId(rs.getString(10));
				news.setState(rs.getString(11));
				newsList.add(news);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBHelper.closeResultSet(rs);
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}
		return newsList;
	}
	public Bookmark checkBookmark(String loginId, String newsNo) {
		Connection con = DBHelper.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Bookmark bookmark = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * ");
		sql.append("FROM BOOKMARK ");
		sql.append("WHERE ID=? and NEWS_NO=? ");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1,loginId);
			pstmt.setString(2,newsNo);
			rs = pstmt.executeQuery();
			while(rs.next()){
				bookmark = new Bookmark();
				bookmark.setBookmarkNo(rs.getInt(1));
				bookmark.setId(rs.getString(2));
				bookmark.setNewsNo(rs.getInt(3));
				bookmark.setState(rs.getString(4));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBHelper.closeResultSet(rs);
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}
		return bookmark;
	}
	public int bookmark(String loginId, String newsNo) {
		Connection con = DBHelper.getConnection();
		PreparedStatement pstmt = null;
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO BOOKMARK(ID,NEWS_NO,STATE) ");
		sql.append("VALUES (?,?,'true') ");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, loginId);
			pstmt.setString(2, newsNo);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}
		return result;
	}
	public int modifyBookmark(Bookmark bookmark) {
		Connection con = DBHelper.getConnection();
		PreparedStatement pstmt = null;
		int result = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE BOOKMARK SET STATE = ? ");
		sql.append("WHERE ID = ? and NEWS_NO = ? ");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, bookmark.getState());
			pstmt.setString(2, bookmark.getId());
			pstmt.setInt(3, bookmark.getNewsNo());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}
		return result;
	}
	public List<News> keywordNewsList(int startRow, int endRow, String loginId) {
		Connection con = DBHelper.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<News> newsList = new ArrayList<>();
		News news = null;
		StringBuffer sql = new StringBuffer();
//		기존
//		sql.append("SELECT * ");
//		sql.append("FROM NEWS ");
//		sql.append("WHERE KEYWORD LIKE ? ");
//		sql.append("ORDER BY NEWS_NO DESC LIMIT ?,? ");
		System.out.println("북마크뉴스 검색");
		sql.append("SELECT NEWS.NEWS_NO,NEWS.IMG_URL,NEWS.TITLE,NEWS.MAIN_TITLE,NEWS.CONTENT,NEWS.MAIN_CONTENT,NEWS.PRESS,NEWS.NEWS_LINK,NEWS.KEYWORD,BOOKMARK.ID,BOOKMARK.STATE ");
		sql.append("FROM NEWS LEFT JOIN BOOKMARK ");
		sql.append("ON NEWS.NEWS_NO = BOOKMARK.NEWS_NO ");
		sql.append("WHERE ID = ? and STATE = 'true' ");
		sql.append("ORDER BY NEWS.NEWS_NO DESC LIMIT ?,? ");
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, loginId);
			pstmt.setInt(2, startRow);
			pstmt.setInt(3, endRow);
			rs = pstmt.executeQuery();
			while(rs.next()){
				news = new News();
				news.setNewsNo(rs.getInt(1));
				news.setImgUrl(rs.getString(2));
				news.setTitle(rs.getString(3));
				news.setMainTitle(rs.getString(4));
				news.setContent(rs.getString(5));
				news.setMainContent(rs.getString(6));
				news.setPress(rs.getString(7));
				news.setNewsLink(rs.getString(8));
				news.setKeyword(rs.getString(9));
				news.setId(rs.getString(10));
				news.setState(rs.getString(11));
				newsList.add(news);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBHelper.closeResultSet(rs);
			DBHelper.closePreparedStatment(pstmt);
			DBHelper.closeConnection(con);
		}
		return newsList;
	}
}
