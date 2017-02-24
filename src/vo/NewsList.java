package vo;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NewsList {
	private Collection<News> news;
	
	public NewsList(){
		news = new ArrayList<News>();
	}

	@XmlElement
	public Collection<News> getNews() {
		return news;
	}

	public void setNews(Collection<News> news) {
		this.news = news;
	}
	
	
}
