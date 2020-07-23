import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TGBotFiap extends TelegramLongPollingBot {

	ChatInteracao ci = new ChatInteracao();

	@Override
	public void onUpdateReceived(Update update) {

		ci.setChatId(update.getMessage().getChatId());

		// Pega a mensagem inicial
		SendMessage message = new SendMessage()
				.setChatId(update.getMessage().getChatId())
				.setText(update.getMessage().getText());

		// Se o estado do objetivo estiver null, identifica como primeira interação
		if (update.hasMessage() && update.getMessage().hasText() && ci.getState() == null) {

			ci.setState("Menu");
			
			message.setText("Bem vindo ao TelegramBot Fiap - Curso FullStack");
			enviaMensagemExecucao(message);
			
			StringBuilder mensagemOpcoes = new StringBuilder();
			mensagemOpcoes.append("Digite o opção desejada");
			mensagemOpcoes.append("\n1 - Consulta CEP");
			mensagemOpcoes.append("\n2 - Valida CPF");
			mensagemOpcoes.append("\n3 - Consulta clima");
			mensagemOpcoes.append("\n4 - Consulta trânsito");
			
			message.setText(mensagemOpcoes.toString());
			enviaMensagemExecucao(message);
		}

		// Caso tenha digitado o 'cep', será feito a solicitação do CEP e depois seta o
		// estado ConsultarCEP para realizar a consulta
		else if (ci.getState().equals("ConsultarCEP") || (message.getText().equalsIgnoreCase("cep"))) {

			if (ci.getState().equals("ConsultarCEP")) {

				Endereco endereco = Utils.buscaEnderecoPorCEP(message.getText());
				
				// Valida se foi digitado um cep antes de executar o serviço
				if (!Utils.validaCEP(message.getText()) || endereco == null) {
					message.setText("CEP informado incorreto, por favor digite o CEP corretamente");
					enviaMensagemExecucao(message);
					return;
				}

				ci.setState("MenuIni");

				message.setText(endereco.getEnderecoFormatado());
				enviaMensagemExecucao(message);

			} else {
				ci.setState("ConsultarCEP");

				message.setText("Digite o CEP a ser  consultado com 8 digitos");
				enviaMensagemExecucao(message);
			}

		}

		else if (ci.getState().equals("ConsultarCPF") || (message.getText().equalsIgnoreCase("cpf"))) {
			ci.setState("ConsultarCPF");
			
			if(!Utils.validaCPF(message.getText())) 
				message.setText("O CPF digitado está incorreto");
				enviaMensagemExecucao(message);
				
		}

		// Encerra o chat setando o estado como null
		else if (message.getText().equalsIgnoreCase("finalizar")) {
			ci.setState(null);
			
			message.setText("Até Logo");
			enviaMensagemExecucao(message);
		} 
		else {

			message.setText("Não entendi\nDigite a opção anterior ou digite 'finalizar' para encerrar");
			enviaMensagemExecucao(message);
		}

		if (ci.getState().equals("MenuIni")) {
			ci.setState("Menu");
			
			message.setText("Digite 'cep' para fazer uma busca de endereço por cep \nDigite 'finalizar' para encerrar o chat atual");
			enviaMensagemExecucao(message);
		}
	}
	
	/**
	 * Metodo que recebe uma {@link SendMessage} e envia
	 * @param message
	 */
	private void enviaMensagemExecucao(SendMessage message) {
		try {
			execute(message);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getBotUsername() {
		return "BotGuerraFiap";
	}

	@Override
	public String getBotToken() {
		return "1093442418:AAGJM0TSkM9nFV5TdsMCBwJqWTXj7KnmM-4";
	}
}
