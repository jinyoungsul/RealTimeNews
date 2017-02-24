package vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class News {
	private int newsNo;
	private String imgUrl;	
	private String title;	
	private String mainTitle;
	private String content;
	private String mainContent;
	private String press;
	private String newsLink;
	private String keyword;
	private String id;
	private String state;
	
	public News(){}
	public News(int newsNo, String imgUrl, String title, String mainTitle, String content, String mainContent,
			String press, String newsLink, String keyword, String id, String state) {
		super();
		this.newsNo = newsNo;
		this.imgUrl = imgUrl;
		this.title = title;
		this.mainTitle = mainTitle;
		this.content = content;
		this.mainContent = mainContent;
		this.press = press;
		this.newsLink = newsLink;
		this.keyword = keyword;
		this.id = id;
		this.state = state;
	}
	@XmlElement
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	@XmlElement
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@XmlElement
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@XmlElement
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
	@XmlElement
	public String getNewsLink() {
		return newsLink;
	}
	public void setNewsLink(String newsLink) {
		this.newsLink = newsLink;
	}
	@XmlElement
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	@XmlElement
	public String getMainTitle() {
		return mainTitle;
	}
	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}
	@XmlElement
	public String getMainContent() {
		return mainContent;
	}
	public void setMainContent(String mainContent) {
		this.mainContent = mainContent;
	}
	@XmlElement
	public int getNewsNo() {
		return newsNo;
	}
	public void setNewsNo(int newsNo) {
		this.newsNo = newsNo;
	}
	@XmlElement
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@XmlElement
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "News [newsNo=" + newsNo + ", imgUrl=" + imgUrl + ", title=" + title + ", mainTitle=" + mainTitle
				+ ", content=" + content + ", mainContent=" + mainContent + ", press=" + press + ", newsLink="
				+ newsLink + ", keyword=" + keyword + ", id=" + id + ", state=" + state + "]";
	}
}
