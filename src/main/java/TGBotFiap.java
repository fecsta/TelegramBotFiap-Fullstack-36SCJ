import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Classe que funciona como uma maquina de estado para receber e enviar as respostas ao telegram 
 */
public class TGBotFiap extends TelegramLongPollingBot {

    ChatInteracao ci = new ChatInteracao();


    @Override
    public void onUpdateReceived(Update update){

        Message message = update.getMessage();
		Long chatId = message.getChatId();
		
		ci.setChatId(chatId);
        
        //Pega a mensagem inicial
        SendMessage sendMessage = new SendMessage()
                .setChatId(chatId)
                .setText(message.getText());
        
        
        
        

        //Se o  estado do objetivo estiver null, identifica como primeira interação
        if (update.hasMessage() && message.hasText() && ci.getState() == null) {
        	
        	ci.setState("Menu");
            sendMessage.setText("Bem vindo ao TelegramBot Fiap - Curso FullStack");
            enviaMensagem(sendMessage);
            
            sendMessage.setText("Digite 'cep' para fazer uma busca de endereço por cep \nDigite 'finalizar' para encerrar o chat atual");
            enviaMensagem(sendMessage);

        } 
        else {
			String sendMessageText = sendMessage.getText();
			if (ci.getState().equals("ConsultarCEP") || (sendMessageText.equalsIgnoreCase("cep"))) {

			     if(ci.getState().equals("ConsultarCEP")){
			    	 
			         //Valida se foi digitado um cep antes de executar o serviço
			         if (sendMessageText.length() < 8 || sendMessageText.length() > 8 ){
			        	 
			             sendMessage.setText("Não foi digitado um CEP, por favor, digite um cep corretamente");
			             enviaMensagem(sendMessage);
			             return;
			         }
			         
			        Endereco endereco = Utils.buscaEnderecoPorCEP(sendMessageText);
			        
			        
			        if(endereco == null) {
			            sendMessage.setText("O cep digitado não é valido, digite novamente");
			            enviaMensagem(sendMessage);
			            
			        }
			        else{
			        	
			        	ci.setState("MenuIni");
			        	sendMessage.setText(endereco.getEnderecoFormatado());
			            enviaMensagem(sendMessage);
			        }
			        
			        
			        
			    }else{
			        sendMessage.setText("Digite o CEP a ser  consultado com 8 digitos");
			        ci.setState("ConsultarCEP");
			        enviaMensagem(sendMessage);
			    }

			}
			
			//Encerra o chat setando o estado como null        
			else if (sendMessageText.equalsIgnoreCase("finalizar")) {
				
			    ci.setState(null);
			    sendMessage.setText("Até Logo");
			    enviaMensagem(sendMessage);
			    
			}
			else{
			    sendMessage.setText("Não entendi\nDigite a opção anterior ou digite 'finalizar' para encerrar");
			    enviaMensagem(sendMessage);
			}
		}
        
        
        //Caso tenha saido de uma interação, repete o Menu para o usuário
        if (ci.getState().equals("MenuIni")){
        	ci.setState("Menu");
        	sendMessage.setText("Digite 'cep' para fazer uma busca de endereço por cep \nDigite 'finalizar' para encerrar o chat atual");
            enviaMensagem(sendMessage);
        }
    }

    /**
     * Dado uma {@link SendMessage} envia mensagem para o telegram
     * @param message
     */
	private void enviaMensagem(SendMessage message) {
		try {
		    execute(message);
		} catch (TelegramApiException e) {
		    e.printStackTrace();
		}
	}

    @Override
    public String getBotUsername() {
        return "TeleFiapBot";
    }

    @Override
    public String getBotToken() {
        return "1047254319:AAGuwKitDMCg4XyX7BsA3DhokLeAv3UqYrM";
    }
}
