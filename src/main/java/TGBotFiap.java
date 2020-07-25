import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class TGBotFiap extends TelegramLongPollingBot {

    ChatInteracao ci = new ChatInteracao();
    String comandos = "Digite 'cep' para fazer uma busca de endereço por cep\n"
                    + "Digite 'imc' para calcular o seu IMC\n"
                    + "Digite 'previsão' para a previsão do tempo\n"
                    + "Digite 'finalizar' a qualquer momento para encerrar o chat atual";
    IMC indice = new IMC();

    @Override
    public void onUpdateReceived(Update update){

        ci.setChatId(update.getMessage().getChatId());

        //Pega a mensagem inicial
        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(update.getMessage().getText());

        //Se o  estado do objetivo estiver null, identifica como primeira interação
        if (update.hasMessage() && update.getMessage().hasText()&& ci.getState() == null) {

            message.setText("Bem vindo ao TelegramBot Fiap - MBA FullStack");
            try {
                execute(message);
                ci.setState("Menu");

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            message.setText(comandos);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            //Encerra o chat setando o estado como null
        }else if (message.getText().equalsIgnoreCase("finalizar")) {

            ci.setState(null);
            message.setText("Até Logo");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            //Caso tenha digitado 'cep', será feito a solicitação do CEP e depois seta o estado ConsultarCEP para realizar a consulta
        }else if (ci.getState().equals("ConsultarCEP")||(message.getText().equalsIgnoreCase("cep"))) {

            if(ci.getState().equals("ConsultarCEP")){
                //Valida se foi digitado um cep antes de executar o serviço
                if (message.getText().length() < 8 || message.getText().length() > 8 ){
                    message.setText("Não foi digitado um CEP, por favor, digite um cep corretamente");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                Endereco endereco = Utils.buscaEnderecoPorCEP(message.getText());

                if(endereco == null) {
                    message.setText("O cep digitado não é valido, digite novamente");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }else{

                    StringBuilder enderecoString = new StringBuilder();
                    enderecoString.append("Endereço: ").append(endereco.getLogradouro());
                    enderecoString.append("\nBairro: ").append(endereco.getBairro());
                    enderecoString.append("\nCidade: ").append(endereco.getLocalidade());
                    enderecoString.append("\nEstado: ").append(endereco.getUf());

                    message.setText(enderecoString.toString());

                    ci.setState("MenuIni");

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                message.setText("Digite o CEP a ser  consultado com 8 digitos");
                ci.setState("ConsultarCEP");
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
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
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                // Valida intervalo de peso
                if (!indice.check("peso")) {
                    message.setText(indice.error + "\nTente novamente");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    indice.calcIMC();
                    message.setText(indice.toString());
                    
                    ci.setState("MenuIni");

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else if (ci.getState().equals("Altura")) {
                // Valida se altura é numérico
                try {
                    String text = message.getText().replace(",", ".");
                    indice.setAltura(Float.parseFloat(text));
                } catch (NumberFormatException exp) {
                    message.setText("O valor de altura deve ser numérico, tente novamente");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                // Valida intervalo de altura
                if (!indice.check("altura")) {
                    message.setText(indice.error + "\nTente novamente");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else {
                    ci.setState("Peso");
                    message.setText("Digite seu peso em kg");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                message.setText("Digite sua altura em metros");
                ci.setState("Altura");
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            //Caso tenha digitado 'previsão', será feito a solicitação da previsão do tempo e depois setado o estado PrevisaoTempo para realizar a consulta
        }else if (ci.getState().equals("PrevisaoTempo")||(message.getText().equalsIgnoreCase("previsão"))) {

            if(ci.getState().equals("PrevisaoTempo")){
                //Verifica a validade do nome de cidade antes de executar
                if (message.getText().length() < 3){
                    message.setText("Cidade não existente, digite um nome válido");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                Tempo tempo = Utils.buscaPrevisaoTempoCidade(message.getText());

                if(tempo == null) {
                    message.setText("A cidade digitada não existe, tente novamente");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }else{

                    StringBuilder tempoString = new StringBuilder();
                    tempoString.append("A previsão do tempo para: ").append(tempo.getName());
                    tempo.getMain();
                    tempoString.append("\nTemperatura: ").append(tempo.getMain().getTemp() + " °C");
                    tempoString.append("\nSensação Térmica: ").append(tempo.getMain().getFeels_like() +" °C");
                    tempoString.append("\nMáxima: ").append(tempo.getMain().getTemp_max() +" °C");
                    tempoString.append("\nMínima: ").append(tempo.getMain().getTemp_min() +" °C");
                    tempoString.append("\nUmidade do Ar: ").append(tempo.getMain().getHumidity()+" %");
                    tempoString.append("\nVelocidade do Vento: ").append(tempo.getWind());

                    message.setText(tempoString.toString());

                    ci.setState("MenuIni");

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                message.setText("Digite a cidade para qual deseja a previsão do tempo");
                ci.setState("PrevisaoTempo");
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }else{
            message.setText("Não entendi\nDigite uma opção válida ou digite 'finalizar' para encerrar");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        //Caso tenha saido de uma interação, repete o Menu para o usuário
        if (ci.getState().equals("MenuIni")){
            message.setText(comandos);
            ci.setState("Menu");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
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
