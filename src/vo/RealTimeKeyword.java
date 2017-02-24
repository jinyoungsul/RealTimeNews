package vo;

public class RealTimeKeyword {
	private int ranking;
	private String keyword;
	private String keywordUrl;
	
	public RealTimeKeyword(){
		
	}
	public RealTimeKeyword(int ranking, String keyword, String keywordUrl) {
		this.ranking = ranking;
		this.keyword = keyword;
		this.keywordUrl = keywordUrl;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getKeywordUrl() {
		return keywordUrl;
	}
	public void setKeywordUrl(String keywordUrl) {
		this.keywordUrl = keywordUrl;
	}
}
