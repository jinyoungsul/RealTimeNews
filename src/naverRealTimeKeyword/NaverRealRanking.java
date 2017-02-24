package naverRealTimeKeyword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import service.NewsService;
import vo.News;
import vo.RealTimeKeyword;

public class NaverRealRanking {
	private static NaverRealRanking instance = null;
	private static NewsService service = NewsService.getInstance();
	public static NaverRealRanking getInstance() {
		if (instance == null) {
			instance = new NaverRealRanking();
		}
		return instance;
	}

	private NaverRealRanking() {
	}

	public static Document getDocument(String url) {
		HttpPost http = new HttpPost(url);
		Document doc = null;
		// 2.가져오기를 실행할 클라이언트 객체 생성
		HttpClient httpClient = HttpClientBuilder.create().build();
		try {
			// 3.실행결과를 response객체에 담기
			HttpResponse httpResponse = httpClient.execute(http);
			// 4.Response 받은 데이터 중, DOM 데이터를 가져와 Entity에 담음
			HttpEntity httpEntity = httpResponse.getEntity();
			// 6. Charset을 알아내기 위해 DOM의 컨텐트 타입을 가져와 담고 Charset을 가져옴
			ContentType contentType = ContentType.getOrDefault(httpEntity);
			Charset charset = contentType.getCharset();
			// 7. DOM 데이터를 한 줄씩 읽기 위해 Reader에 담음 (InputStream / Buffered 중 선택은
			// 개인취향)
			BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent(), charset));
			// 8. 가져온 DOM 데이터를 담기위한 그릇
			StringBuffer dom = new StringBuffer();

			// 9. DOM 데이터 가져오기
			String line = "";
			while ((line = br.readLine()) != null) {
				dom.append(line + "\n");
			}
			doc = Jsoup.parse(dom.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public List<RealTimeKeyword> getRealRanking() {
		List<RealTimeKeyword> realTimeKeywordList = new ArrayList<>();
		// 1.가져올 url세팅 (naver)
		Document doc = getDocument("http://www.naver.com");
		Elements elements = doc.select("ol#realrank > li:not(#lastrank) > a");
		for (int i = 0; i < elements.size(); i++) {
			RealTimeKeyword realTimeKeyword = new RealTimeKeyword();
			realTimeKeyword.setRanking(i + 1); // 순위
			realTimeKeyword.setKeyword(elements.get(i).attr("title")); // 검색어
			realTimeKeyword.setKeywordUrl(elements.get(i).attr("href")); // 링크
			realTimeKeywordList.add(realTimeKeyword);
		}
		return realTimeKeywordList;
	}

	public void getNewsList(List<RealTimeKeyword> newsList) {
		for (RealTimeKeyword keyword : newsList) {
			Document doc = null;
			try {
				doc = Jsoup.connect(keyword.getKeywordUrl()).timeout(60000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div.news.section > ul.type01 > li");
			if (el != null) {
				 for(int i = 0;i<el.size();i++){
					News news = new News();
					news.setImgUrl(el.get(i).select("div.thumb > a > img").attr("src"));
					news.setTitle(el.get(i).select("dl > dt > a").attr("title"));
					news.setContent(el.get(i).select("dl > dd:nth-child(3)").text());
					news.setPress(el.get(i).select("dl > dd.txt_inline > span._sp_each_source").text());
					news.setNewsLink(el.get(i).select("dl > dt > a").attr("href"));
					news.setKeyword(keyword.getKeyword());
					collectNews(news);
				}
			}
		}
	}

	public void collectNews(News news) {
		Document doc = null;
		int result = 0;
		if (news.getPress().equals("YTN")) {
			System.out.println("YTN일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#czone > div#zone1");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.article_tit").first();
					Element contentEl = el.get(i).select("div.article_wrap > div.article_paragraph > span").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("헤럴드경제")) {
			System.out.println("헤럴드경제일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(120000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div.view_bg");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.view_top > div.view_top_t2 > ul > li > h1").first();
					
					mainContent = el.get(i)
							.select("div.con_left > div#CmAdContent > div > div#content_ADTOM > div#articleText")
							.text();
					if (mainContent.length() == 0) {
						mainContent = el.get(i).select("div.con_left > div#articleText").text();
					} 
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("ZDNet Korea")) {
			System.out.println("ZDNet Korea일 경우");
			doc = getDocument(news.getNewsLink());
			Elements el = doc.select("div.sub_view_container");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.sub_view_tit.clfx > div.sub_view_tit2 > h2").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					mainContent = el.get(i).select("div.sub_view_cont.clfx > div#content > p").text().trim();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("중앙일보")) {
			System.out.println("중앙일보일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#body");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.article_head > div.subject > h1").first();
					Element contentEl = el.get(i).select("div#content > div#article_body").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				System.out.println(news);
				result = service.collect(news);
			}
		} else if (news.getPress().equals("한국일보")) {
			System.out.println("한국일보일 경우");
			doc = getDocument(news.getNewsLink());
			Elements el = doc.select("div.oneSection");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.newsStoryTit > div.titGroup.show > h4").first();
					Element contentEl = el.get(i).select("article.newsStoryTxt").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("뉴시스")) {
			System.out.println("뉴시스일 경우");
			try {
				 doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
				 } catch (IOException e) {
				 e.printStackTrace();
				 }
			Elements el = doc.select("body > table:nth-child(4)");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("td.viewnewstitle").first();
					Element contentEl = el.get(i).select("div#joinskmbox > div#articleBody").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("한국경제TV")) {
			System.out.println("한국경제TV일 경우");
			try {
				 doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
				 } catch (IOException e) {
				 e.printStackTrace();
				 }
			Elements el = doc.select("div#viewWrap");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div#viewTitle > h2").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					mainContent = el.get(i).select("div#viewContent_1 > div#article_cont > span.article_contents > div#viewContent_3").text().trim();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("서울경제")) {
			System.out.println("서울경제일 경우");
			try {
				 doc = Jsoup.connect(news.getNewsLink()).timeout(60000).get();
				 } catch (IOException e) {
				 e.printStackTrace();
				 }
			Elements el = doc.select("div#contents-article-view > div#v-left");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div#v-left-scroll-in > h2").first();
					Element contentEl = el.get(i).select("div#v-left-scroll-in > div.view_con").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("서울신문")) {
			System.out.println("서울신문일 경우");
			try {
				 doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
				 } catch (IOException e) {
				 e.printStackTrace();
				 }
			Elements el = doc.select("div.wrap");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.title > div.title_inner_art > div.article_tit > div.atit").first();
					Element contentEl = el.get(i).select("div.v_article").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("아이뉴스24")) {
			System.out.println("아이뉴스24일 경우");
			try {
				 doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
				 } catch (IOException e) {
				 e.printStackTrace();
				 }
			Elements el = doc.select("div.container > table > tbody > tr > td#LeftMenuArea");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.title").first();
					Element contentEl = el.get(i).select("div[itemprop=articleBody]").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("오마이뉴스")) {
			System.out.println("오마이뉴스일 경우");
			if(news.getNewsLink().contains("star")){	//스타오마이뉴스
				try {
					 doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
					 } catch (IOException e) {
					 e.printStackTrace();
					 }
				Elements el = doc.select("section.B-atc > section.arc-wrap");
				if (el != null) {
					for (int i = 0; i < el.size(); i++) {
						String mainTitle = "";
						String mainContent = "";
						Element titleEl = el.get(i).select("div.atc-head > h2.tit > a").first();
						Element contentEl = el.get(i).select("div.atc-text > div.text").first();
						for(TextNode node : titleEl.textNodes()){
							mainTitle += node.text().trim();
						}
						for(TextNode node : contentEl.textNodes()){
							mainContent += node.text().trim();
						}
						news.setMainTitle(mainTitle);
						news.setMainContent(mainContent);
					}
					result = service.collect(news);
				}
			} else {								//오마이뉴스
				try {
					 doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
					 } catch (IOException e) {
					 e.printStackTrace();
					 }
				Elements el = doc.select("div.content");
				if (el != null) {
					for (int i = 0; i < el.size(); i++) {
						String mainTitle = "";
						String mainContent = "";
						Element titleEl = el.get(i).select("div.newstitlwrap > div.newstitle > h3.tit_subject > a").first();
						Element contentEl = el.get(i).select("div.newswrap > div.news_body > div.news_view > div.article_view > div.at_contents").first();
						for(TextNode node : titleEl.textNodes()){
							mainTitle += node.text().trim();
						}
						for(TextNode node : contentEl.textNodes()){
							mainContent += node.text().trim();
						}
						news.setMainTitle(mainTitle);
						news.setMainContent(mainContent);
					}
					result = service.collect(news);
				}
			}
		} else if (news.getPress().equals("마이데일리")) {
			System.out.println("마이데일리일 경우");
			doc = getDocument(news.getNewsLink());
			Elements el = doc.select("div.read_view_wrap");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("dt").first();
					Element contentEl = el.get(i).select("dd > div#article").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("머니투데이")) {
			System.out.println("머니투데이일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#article");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("h1").first();
					Element contentEl = el.get(i).select("div#gisa_section > table > tbody > tr > td > div.view_text > div#textBody").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("노컷뉴스")) {
			System.out.println("노컷뉴스일 경우");
			doc = getDocument(news.getNewsLink());
			Elements el = doc.select("div#pnlWrap");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div#pnlContainer > div#pnlViewTop > div.reporter_info > h2").first();
					Element contentEl = el.get(i).select("div#pnlContainer > div.content > div#pnlViewBox > div#pnlContent").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("SBS 뉴스")) {
			System.out.println("SBS 뉴스일 경우");
			doc = getDocument(news.getNewsLink());
			Elements el = doc.select("div.w_article");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.w_article_title > h3#vmNewsTitle").first();
					Element contentEl = el.get(i).select("div.w_article_cont > div.w_article_left > div.article_cont_area > div.main_text").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("스포탈코리아")) {
			System.out.println("스포탈코리아일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#contentBox01");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div#reView > h2").first();
					Element contentEl = el.get(i).select("div#DivContents > div#CmAdContent div.review_text02").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}

					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("매일경제")) {
			
		} else if (news.getPress().equals("아시아경제")) {
			System.out.println("아시아경제일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div.view_bg_box");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.area_title > h1").first();
					Element contentEl = el.get(i).select("div#bodyContents > div.article > div.txt").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}

					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("스포츠조선")) {
			System.out.println("스포츠조선일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div.deatail1");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.acle_c > h1").first();
					Element contentEl = el.get(i).select("div.article > div.news_content > div.news_text > font.article").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("조선일보")) {
			System.out.println("조선일보일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#csContent");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("header.news_title > div.news_title_text > h1").first();
					Element contentEl = el.get(i).select("article.news_article > div#news_body_id > div.par").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("조선비즈")) {
			System.out.println("조선비즈일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#content");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.art_title_2011 > div.title_author_2011 > h2").first();
					Element contentEl = el.get(i).select("div#article_2011").first();
					for (TextNode node : titleEl.textNodes()) {
						mainTitle += node.text().trim();
					}
					for (TextNode node : contentEl.textNodes()) {
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("OSEN")) {
			System.out.println("OSEN일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#container");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = el.get(i).select("div.detailTitle > h1").text();
					String mainContent = el.get(i).select("div#contents2 > div.detailBox > div#_article > p").text();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("KBS 뉴스")) {
			System.out.println("KBS 뉴스일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#container");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = el.get(i).select("div.inner_container_left > div.sec_subject > div.subject_area")
							.text();
					String mainContent = el.get(i)
							.select("div.inner_container_left > div.sec_newstext > div.inner_newstext").text();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("한겨레")) {
			System.out.println("한겨레일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#container");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = el.get(i).select("div.article-head > h4").text();
					String mainContent = el.get(i)
							.select("div#a-left-scroll-start > div#a-left-scroll-in > div.article-text > div.article-text-font-size > div.text")
							.text();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("세계일보")) {
			System.out.println("세계일보일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(120000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div.content");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = el.get(i).select("div.titleh1 > h1").text();
					String mainContent = el.get(i).select("div#article_txt").text();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("프레시안")) {
			System.out.println("프레시안일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#wrap");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = el.get(i).select("div#container > div.layout_container > div.container > div.section_v2_123 > div.m01_arv > div.title").text();
					String mainContent = el.get(i).select("div#container > div.layout_container > div.container > div.section_v2_123 > div.section_v2_12 > div.m01_arv_notice > div.cnt_view.news_body_area > div.smartOutput > div[itemprop=articleBody] > div#CmAdContent").text();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("이데일리")) {
			System.out.println("이데일리일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#contentwrap");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = el.get(i).select("div#viewarea > h4").text();
					String mainContent = el.get(i).select("div#contentwrap > div#viewarea > span#content > div#viewcontent > span#viewcontent_inner").text();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("국민일보")) {
			System.out.println("국민일보일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#content");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = el.get(i).select("div.sub_header > div.nwsti_inner > div.nwsti > h3").text();
					String mainContent = el.get(i).select("div.sub_content > div.NwsCon > div#article > div.tx").text();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("국민일보")) {
			System.out.println("국민일보일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#content");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = el.get(i).select("div.sub_header > div.nwsti_inner > div.nwsti > h3").text();
					String mainContent = el.get(i).select("div.sub_content > div.NwsCon > div#article > div.tx").text();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("데일리안")) {
			System.out.println("데일리안일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(60000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#content1");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div#view_titlebox > div.view_titlebox_r1").first();
					Element contentEl = el.get(i).select("div#view_page > div#view_con").first();
					for (TextNode node : titleEl.textNodes()) {
						mainTitle += node.text().trim();
					}
					for (TextNode node : contentEl.textNodes()) {
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("디지털타임스")) {
			System.out.println("디지털타임스일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#container");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div#news_names > h1").first();
					Element contentEl = el.get(i).select("div#gisa > div#resizeContents > div#NewsAdContent").first();
					System.out.println(titleEl);
					for (TextNode node : titleEl.textNodes()) {
						mainTitle += node.text().trim();
					}
					for (TextNode node : contentEl.textNodes()) {
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("전자신문")) {
			System.out.println("전자신문일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#left1 > div.pos_r");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					System.out.println(news);
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.hgroup > h1").first();
					Element contentEl = el.get(i).select("div.article_body > div#talklink_contents > p").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for (TextNode node : contentEl.textNodes()) {
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("한국경제")) {
			System.out.println("한국경제일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div.row.line-y-730.news-container");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("section.news-atc-tit > h1").first();
					Element contentEl = el.get(i).select("section > div.cnt-news-wrap > article.hk-news-view").first();
					
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		}else if (news.getPress().equals("MBC")) {
			System.out.println("MBC 일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div.news_contents.current");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("section.header > h1").first();
					Element contentEl = el.get(i).select("section.body > section.txt").first();
					
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		} else if (news.getPress().equals("일간스포츠")) {
			System.out.println("일간스포츠 일 경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div.sec_article_view.article04");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.article_tit > h2").first();
					Element contentEl = el.get(i).select("div.article_cont > div#adiContents").first();
					
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		}else if (news.getPress().equals("동아일보")) {
			System.out.println("동아일보 일경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(180000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#container");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.article_title02 > h1").first();
					Element contentEl = el.get(i).select("div.article_view > div.article_txt").first();
					System.out.println(news);
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					for(TextNode node : contentEl.textNodes()){
						mainContent += node.text().trim();
					}
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		}else if (news.getPress().equals("미디어 오늘")) {
			System.out.println("미디어 오늘 일경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#container");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.arl_view_title_box > h1").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					mainContent = el.get(i).select("div.arl_view_content > div#talklink_contents > p").text();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		}else if (news.getPress().equals("MBN")) {
			if(news.getNewsLink().contains("star")){	//스타 MBN
				System.out.println("스타 MBN 일경우");
				try {
					doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Elements el = doc.select("div.container_left > div#content > div#article");
				if (el != null) {
					for (int i = 0; i < el.size(); i++) {
						String mainTitle = "";
						String mainContent = "";
						Element titleEl = el.get(i).select("h2").first();
						Element contentEl = el.get(i).select("div.detail > div#artText > div.read_txt").last();
						for(TextNode node : titleEl.textNodes()){
							mainTitle += node.text().trim();
						}
						for(TextNode node : contentEl.textNodes()){
							mainContent += node.text().trim();
						}
						news.setMainTitle(mainTitle);
						news.setMainContent(mainContent);
					}
					result = service.collect(news);
				}
			} else {									//그냥 MBN
				System.out.println("MBN 일경우");
				try {
					doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Elements el = doc.select("div#container");
				if (el != null) {
					for (int i = 0; i < el.size(); i++) {
						String mainTitle = "";
						String mainContent = "";
						Element titleEl = el.get(i).select("div.title_box > div.box1 > p.tit").first();
						Element contentEl = el.get(i).select("div.detail").first();
						if(titleEl==null){
							System.out.println("MBN -> titleEl");
						} else {
							for(TextNode node : titleEl.textNodes()){
								mainTitle += node.text().trim();
							}
						}
						for(TextNode node : contentEl.textNodes()){
							mainContent += node.text().trim();
						}
						news.setMainTitle(mainTitle);
						news.setMainContent(mainContent);
					}
					result = service.collect(news);
				}
			}
			
			
			
			
			
		}else if (news.getPress().equals("New Daily")) {
			System.out.println("New Daily 일경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div#ndBodyMain");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div#titlebox > h2").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					mainContent = el.get(i).select("div#ndArtBody > p").text();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		}else if (news.getPress().equals("The Korea Herald")) {
			System.out.println("The Korea Herald 일경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div.content.bg_n");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("section.nview > h1").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					mainContent = el.get(i).select("div.content_view").text();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		}else if (news.getPress().equals("Korea Times")) {
			System.out.println("Korea Times 일경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("div.content_wrapper");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("div.view_page_news_header_wrapper > h1").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					mainContent = el.get(i).select("div#adiContents").text();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		}else if (news.getPress().equals("문화 일보")) {
			System.out.println("문화일보 일경우");
			try {
				doc = Jsoup.connect(news.getNewsLink()).timeout(30000).get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Elements el = doc.select("body");
			if (el != null) {
				for (int i = 0; i < el.size(); i++) {
					String mainTitle = "";
					String mainContent = "";
					Element titleEl = el.get(i).select("span.title").first();
					for(TextNode node : titleEl.textNodes()){
						mainTitle += node.text().trim();
					}
					mainContent = el.get(i).select("div#view_body").text();
					news.setMainTitle(mainTitle);
					news.setMainContent(mainContent);
				}
				result = service.collect(news);
			}
		}
		
		System.out.println("--------------------------------------------------------");
		System.out.println(news);
		System.out.println("--------------------------------------------------------");
	}

	public static void collectNews() {
		Document doc = null;
//		doc = getDocument("http://www.newsis.com/ar_detail/view.html?ar_id=NISX20161117_0014523209&cID=10401&pID=10400");
//		 System.out.println(doc);
		 try {
		 doc =
		 Jsoup.connect("http://star.mbn.co.kr/view.php?no=813509&year=2016&refer=portal").get();
		 System.out.println(doc);
		 } catch (IOException e) {
		 e.printStackTrace();
		 }
		Elements el = doc.select("div.container_left > div#content > div#article");
//		 System.out.println("el : "+el);
		if (el != null) {
			for (int i = 0; i < el.size(); i++) {
				String mainTitle = "";
				String mainContent = "";
				Element titleEl = el.get(i).select("h2").first();
				Element contentEl = el.get(i).select("div.detail > div#artText > div.read_txt").last();
//				
//				System.out.println(contentEl);
				for(TextNode node : titleEl.textNodes()){
					mainTitle += node.text().trim();
				}
				for(TextNode node : contentEl.textNodes()){
					mainContent += node.text().trim();
				}
//				mainTitle = el.get(i).select("div.detailTitle > h1").text();
//				mainContent = el.get(i).select("div.detail > div#artText > div.read_txt").text();
				System.out.println("title : " + mainTitle + " content : " + mainContent);
			}
		}
	}

	public static void main(String[] args) {
		NewsCrawlingThread thread = new NewsCrawlingThread();
		thread.run();
//		collectNews();
	}
}

class NewsCrawlingThread extends Thread {
	@Override
	public void run() {
		int i = 1;
		while (true) {
			System.out.println(i + "번째 크롤링");
			NaverRealRanking n = NaverRealRanking.getInstance();
			List<RealTimeKeyword> realTimeKeywordList = n.getRealRanking();
			n.getNewsList(realTimeKeywordList);
			i++;
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
