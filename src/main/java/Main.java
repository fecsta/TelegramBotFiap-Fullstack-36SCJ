import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

/**
 * @author Desenvolvido pelos alunos da turma 36SCJ - MBA FIAP
 * @version 1.0
 */
public class Main {
	
	public static void main(String[] args) throws TelegramApiRequestException {
		
		//Inicia a API
		ApiContextInitializer.init();

		//Instancia a TelegramBotsAPI  
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		
		//"Registra" o Bot
		try {
			telegramBotsApi.registerBot(new TGBotFiap());
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
		}
	}
}
