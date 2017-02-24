package vo;

public class Bookmark {
	private int bookmarkNo;
	private String id;
	private int newsNo;
	private String state;
	
	public Bookmark(){}

	public Bookmark(int bookmarkNo, String id, int newsNo, String state) {
		super();
		this.bookmarkNo = bookmarkNo;
		this.id = id;
		this.newsNo = newsNo;
		this.state = state;
	}

	public int getBookmarkNo() {
		return bookmarkNo;
	}

	public void setBookmarkNo(int bookmarkNo) {
		this.bookmarkNo = bookmarkNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNewsNo() {
		return newsNo;
	}

	public void setNewsNo(int newsNo) {
		this.newsNo = newsNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Bookmark [bookmarkNo=" + bookmarkNo + ", id=" + id + ", newsNo=" + newsNo + ", state=" + state + "]";
	}
	
}
