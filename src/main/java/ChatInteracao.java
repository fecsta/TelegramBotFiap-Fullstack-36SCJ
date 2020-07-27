/**
 * Classe que estabelece como se dará a interação por estados
 */

public class ChatInteracao {

	private long chatId;
	private String state;
	
	public long getChatId() {
		return chatId;
	}

	public void setChatId(long chatId) {
		this.chatId = chatId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
