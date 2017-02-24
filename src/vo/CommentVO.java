package vo;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class CommentVO {
	
	private int commentNum;
	private String id;
	private int newsNo;
	private String content;
	private String writeDate;
	
	
	public CommentVO() {}


	public CommentVO(int commentNum, String id, int newsNo, String content, String writeDate) {
		this.commentNum = commentNum;
		this.id = id;
		this.newsNo = newsNo;
		this.content = content;
		this.writeDate = writeDate;
	}

	@XmlElement
	public int getCommentNum() {
		return commentNum;
	}


	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	@XmlElement
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	@XmlElement
	public int getNewsNo() {
		return newsNo;
	}


	public void setNewsNo(int newsNo) {
		this.newsNo = newsNo;
	}

	@XmlElement
	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}

	@XmlElement
	public String getWriteDate() {
		return writeDate;
	}


	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}


	@Override
	public String toString() {
		return "CommentVO [commentNum=" + commentNum + ", id=" + id + ", newsNo=" + newsNo + ", content=" + content
				+ ", writeDate=" + writeDate + "]";
	}
	

}
