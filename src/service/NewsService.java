package service;

import java.util.List;

import dao.NewsDao;
import vo.Bookmark;
import vo.News;

public class NewsService {
	private static NewsService instance = new NewsService();
	private NewsService(){}
	public static NewsService getInstance(){
		return instance;
	}
	private NewsDao dao = NewsDao.getInstance();
	public int collect(News news){
		News savedNews = dao.select(news.getNewsLink());
		int result = 0;
		if(savedNews ==null){
			result = dao.insertNews(news);
		}
		return result;
	}
	public List<News> newsList(int startRow, int endRow, String loginId) {
		return dao.newsList(startRow,endRow,loginId);
	}
	public List<News> keywordNewsList(int startRow, int endRow, String searchKeyword, String loginId) {
		return dao.keywordNewsList(startRow,endRow,searchKeyword,loginId);
	}
	public void bookmark(String loginId, String newsNo) {
		Bookmark bookmark = null;
		bookmark = dao.checkBookmark(loginId, newsNo);
		if(bookmark == null){
			System.out.println("북마크 널");
			int result = dao.bookmark(loginId, newsNo);
			if(result==1){
				System.out.println("북마크 성공!");
			}
		} else {
			if(bookmark.getState().equals("true")){
				bookmark.setState("false");
				int result = dao.modifyBookmark(bookmark);
				if(result==1){
					System.out.println("북마크 수정 성공! true -> false");
				}
			} else {
				bookmark.setState("true");
				int result = dao.modifyBookmark(bookmark);
				if(result==1){
					System.out.println("북마크 수정 성공! false -> true");
				}
			}
		}
		
	}
	public List<News> bookmarkNewsList(int startRow, int endRow, String loginId) {
		return dao.keywordNewsList(startRow,endRow,loginId);
	}
}
