import java.util.Map;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Classe que lida com as interações e "estados" no chat do Telegram
 */

public class TGBotFiap extends TelegramLongPollingBot {

    ChatInteracao ci = new ChatInteracao();
    IMC indice = new IMC();

    @Override
    public void onUpdateReceived(Update update){
    	
    	StringBuilder comando = new StringBuilder();	
    	comando.append("Digite 'cep' para fazer uma busca de endereço por cep\n");
        comando.append("Digite 'imc' para calcular o seu IMC\n");
        comando.append("Digite 'febre' para analisar estado febril\n");
		comando.append("Digite 'previsao' para a previsão do tempo\n");
		comando.append("Digite 'cpf' para a validação\n");
		comando.append("Digite 'feriado' para consultar feriados\n");
		comando.append("Digite 'finalizar' a qualquer momento para encerrar o chat atual");
		
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

            message.setText(comando.toString());
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
        //Caso tenha digitado 'febre', será feito a solicitação da temperatura medida para analisar as acoes a serem tomadas
        }else if (ci.getState().equals("febre")  || (message.getText().equalsIgnoreCase("febre"))) {
            Temperatura temperatura = new Temperatura();
                if (ci.getState().equals("febre")) {
                    // Valida se temperatura é numérico
                    try {
                        String text = message.getText().replace(",", ".");
                        temperatura.setTemp(Float.parseFloat(text));
                    } catch (NumberFormatException exp) {
                        message.setText("O valor da temperatura deve ser numérico, tente novamente");
                        enviaMessage(message);
                        return;
                    }
    
                    // Valida limite de temperatura
                if (!temperatura.check()) {
                    message.setText(temperatura.error + "\nTente novamente");
                        enviaMessage(message);
                } else {
                 ci.setState("MenuIni");
                    message.setText(temperatura.toString());
                    enviaMessage(message);
                }
    
                } else {
                    ci.setState("febre");
                    System.out.println();
                    message.setText("Vamos analisar sua temperatura (Adulto entre 18 e 60 anos) \n Digite sua temperatura em graus");
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
				
				StringBuilder feriadosString = new StringBuilder();
				feriadosString.append(String.format("* * * * Feriado para  %s * * * *", message.getText())).append("\n");
				
				if(message.getText().length() == 10) {

					Feriado buscaFeriadosPorAno = Utils.buscaFeriadosPorAno(message.getText());
					
					Map<String, Holiday> feriados = buscaFeriadosPorAno.getMapaDataFeriado();
					
					Holiday feriado = feriados.get(message.getText());
					
					if(feriado == null) {
	                    message.setText(String.format("Data: %s não é um feriado", message.getText()));
	                    enviaMessage(message);
	                    ci.setState("MenuIni");
	                    return;
					}
					
					feriadosString.append(feriado.getHolidayFormatado());
				}
				else if(message.getText().length() == 4) {
					
					Feriado buscaFeriadosPorAno = Utils.buscaFeriadosPorAno(message.getText());
					
					Map<String, Holiday> feriados = buscaFeriadosPorAno.getMapaDataFeriado();
					
					feriados.values().forEach(h -> {
						feriadosString.append(h.getHolidayFormatado());
					});
				}
				else {
                    message.setText(String.format("Data ou Ano: (%s) não digitado corretamente", message.getText()));
                    enviaMessage(message);
                    message.setText("Digite o ano(yyyy) ou uma data(dd/mm/yyyy) a ser consultado");
                    enviaMessage(message);
                    return;
				}
				
				message.setText(feriadosString.toString());
				enviaMessage(message);
				ci.setState("MenuIni");
    		}
        		
        	else {
        		ci.setState("buscaFeriado");

        		message.setText("Digite o ano(yyyy) ou uma data(dd/mm/yyyy) a ser consultado");
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
        	message.setText(comando.toString());
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
        return "1047254319:AAGuwKitDMCg4XyX7BsA3DhokLeAv3UqYrM";
    }
}
