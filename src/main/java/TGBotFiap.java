import java.util.Map;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class TGBotFiap extends TelegramLongPollingBot {

    ChatInteracao ci = new ChatInteracao();
    IMC indice = new IMC();
    
    String comandos = "Digite 'cep' para fazer uma busca de endereço por cep\n"
                    + "Digite 'imc' para calcular o seu IMC\n"
                    + "Digite 'previsao' para a previsão do tempo\n"
                    + "Digite 'cpf' para a validação\n"
                    + "Digite 'feriado' para consultar feriados\n"
                    + "Digite 'finalizar' a qualquer momento para encerrar o chat atual";

    @Override
    public void onUpdateReceived(Update update){

        ci.setChatId(update.getMessage().getChatId());

        //Pega a mensagem inicial
        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(update.getMessage().getText());

        //Se o  estado do objetivo estiver null, identifica como primeira interação
        if (update.hasMessage() && update.getMessage().hasText()&& ci.getState() == null) {
        	ci.setState("Menu");
        	
            message.setText("Bem vindo ao TelegramBot Fiap - MBA FullStack");
            enviaMessage(message);

            message.setText(comandos);
            enviaMessage(message);

            //Encerra o chat setando o estado como null
        }else if (message.getText().equalsIgnoreCase("finalizar")) {

            ci.setState(null);
            
            message.setText("Até Logo");
            enviaMessage(message);

            //Caso tenha digitado 'cep', será feito a solicitação do CEP e depois seta o estado ConsultarCEP para realizar a consulta
        }else if (ci.getState().equals("ConsultarCEP")||(message.getText().equalsIgnoreCase("cep"))) {

            if(ci.getState().equals("ConsultarCEP")){
            	
                //Valida se foi digitado um cep antes de executar o serviço
                if (!Utils.isCEPValido(message.getText())){
                    message.setText("Não foi digitado um CEP, por favor, digite um cep corretamente");
                    enviaMessage(message);
                    return;
                }

                Endereco endereco = Utils.buscaEnderecoPorCEP(message.getText());

                if(endereco == null) {
                    message.setText("O cep digitado não é valido, digite novamente");
                    enviaMessage(message);
                }else{
                	ci.setState("MenuIni");
                	
                    message.setText(endereco.getEnderecoFormatado());
                    enviaMessage(message);
                }
            }else{
            	ci.setState("ConsultarCEP");

            	message.setText("Digite o CEP a ser  consultado com 8 digitos");
                enviaMessage(message);
            }

            //Caso tenha digitado 'imc', será feito a solicitação da altura para depois setar o estado Altura
            //No estado Altura, valido o input do usuário para depois solicitar o peso e setar o estado Peso
            //No estado Peso, valido o input do usuário e calculo o IMC
        }else if (ci.getState().equals("Peso") || ci.getState().equals("Altura") || (message.getText().equalsIgnoreCase("imc"))) {

            if (ci.getState().equals("Peso")) {
                // Valida se peso é numérico
                try {
                    String text = message.getText().replace(",", ".");
                    indice.setPeso(Float.parseFloat(text));
                } catch (NumberFormatException exp) {
                    message.setText("O valor de peso deve ser numérico, tente novamente");
                    enviaMessage(message);
                    return;
                }

                // Valida intervalo de peso
                if (!indice.check("peso")) {
                    message.setText(indice.error + "\nTente novamente");
                    enviaMessage(message);
                }
                else {
                	ci.setState("MenuIni");

                	indice.calcIMC();
                    message.setText(indice.toString());
                    enviaMessage(message);
                }
            } else if (ci.getState().equals("Altura")) {
                // Valida se altura é numérico
                try {
                    String text = message.getText().replace(",", ".");
                    indice.setAltura(Float.parseFloat(text));
                } catch (NumberFormatException exp) {
                    message.setText("O valor de altura deve ser numérico, tente novamente");
                    enviaMessage(message);
                    return;
                }

                // Valida intervalo de altura
                if (!indice.check("altura")) {
                    message.setText(indice.error + "\nTente novamente");
                    enviaMessage(message);
                } else {
                    ci.setState("Peso");

                    message.setText("Digite seu peso em kg");
                    enviaMessage(message);
                }

            } else {
            	ci.setState("Altura");

            	message.setText("Digite sua altura em metros");
                enviaMessage(message);
            }

            //Caso tenha digitado 'previsão', será feito a solicitação da previsão do tempo e depois setado o estado PrevisaoTempo para realizar a consulta
        }else if (ci.getState().equals("PrevisaoTempo")||(message.getText().equalsIgnoreCase("previsão"))) {

            if(ci.getState().equals("PrevisaoTempo")){
                //Verifica a validade do nome de cidade antes de executar
                if (message.getText().length() < 3){
                    message.setText("Cidade não existente, digite um nome válido");
                    enviaMessage(message);
                    return;
                }

                Tempo tempo = Utils.buscaPrevisaoTempoCidade(message.getText());

                if(tempo == null) {
                    message.setText("A cidade digitada não existe, tente novamente");
                    enviaMessage(message);
                }else{
                	ci.setState("MenuIni");

                	message.setText(tempo.getTempoFormatado());
                    enviaMessage(message);
                }
            }else{
            	ci.setState("PrevisaoTempo");
                
            	message.setText("Digite a cidade para qual deseja a previsão do tempo");
                enviaMessage(message);
            }
        }
        
        else if(ci.getState().equals("ValidaCPF")||message.getText().equalsIgnoreCase("cpf")) {
        	
        	if(ci.getState().equals("ValidaCPF")){
        		
        		if(!Utils.isCPFValido(message.getText())) {
        			
                    message.setText(String.format("CPF: %s nao é valido, por favor, digite um CPF corretamente", message.getText()));
                    enviaMessage(message);
                    return;
        		}
        		
        		ci.setState("MenuIni");
        		
                message.setText(String.format("CPF: %s válido", message.getText()));
                enviaMessage(message);
        	}
        	else {
        		ci.setState("ValidaCPF");

        		message.setText("Digite o CPF a ser consultado com 11 digitos");
                enviaMessage(message);
        	}
        }
        
        else if(ci.getState().equals("buscaFeriado") || message.getText().equalsIgnoreCase("feriado")) {
        	
        	if(ci.getState().equals("buscaFeriado")) {
        				
				Feriado buscaFeriadosPorAno = Utils.buscaFeriadosPorAno(message.getText());
				
				Map<String, Holiday> feriados = buscaFeriadosPorAno.getMapaDataFeriado();
				
				StringBuilder feriadosString = new StringBuilder();
				
				feriados.values().forEach(h -> {
					feriadosString.append(h.getHolidayFormatado());
				});
				
				message.setText(feriadosString.toString());
				enviaMessage(message);
				
				ci.setState("MenuIni");
    		}
        		
        	else {
        		ci.setState("buscaFeriado");

        		message.setText("Digite uma data(dd/MM/yyyy) ou ano(yyyy) a ser consultado");
                enviaMessage(message);        		
        	}
        }
        
        else{
            message.setText("Não entendi\nDigite uma opção válida ou digite 'finalizar' para encerrar");
            enviaMessage(message);
        }
        
        //Caso tenha saido de uma interação, repete o Menu para o usuário
        if (ci.getState().equals("MenuIni")){
        	ci.setState("Menu");
        	message.setText(comandos);
            enviaMessage(message);
        }
    }

    /**
     * Dado uma {@link SendMessage} envia a mensagem para o telegram
     * @param message
     */
	private void enviaMessage(SendMessage message) {
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
        return "1093442418:AAGJM0TSkM9nFV5TdsMCBwJqWTXj7KnmM-4";
    }
}

