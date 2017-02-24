package controller;


import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/broadcasting") //주소
public class BroadSocket {
	    //hashSet 으로 클라이언트 객체 및 쓰레드 safe
		private static Set<Session> clients = Collections
				.synchronizedSet(new HashSet<Session>());

		@OnMessage //메세지 처리
		public void onMessage(String message, Session session) throws IOException {
			synchronized (clients) {
				for (Session client : clients) {
					if (!client.equals(session)) {
						client.getBasicRemote().sendText(message); //클라이언트에게 받은메세지 돌려주기
					}
				}
			}
		}

		@OnOpen //클라이언트 입장 세션생성
		public void onOpen(Session session) {
			System.out.println(session);
			clients.add(session);
		}

		@OnClose //클라이언트 입장 세션종료
		public void onClose(Session session) {
			clients.remove(session);
			
		}

}
