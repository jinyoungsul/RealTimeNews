package vo;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CommentList {
	
	private Collection<CommentVO>comment;
	
	public CommentList(){
		comment = new ArrayList<CommentVO>();
	}

	@XmlElement
	public Collection<CommentVO> getComment() {
		return comment;
	}

	public void setComment(Collection<CommentVO> comment) {
		this.comment = comment;
	}
	

}
