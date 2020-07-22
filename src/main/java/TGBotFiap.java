import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TGBotFiap extends TelegramLongPollingBot {

    ChatInteracao ci = new ChatInteracao();


    @Override
    public void onUpdateReceived(Update update){

        ci.setChatId(update.getMessage().getChatId());
        //Pega a mensagem inicial
        SendMessage message = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(update.getMessage().getText());

        //Se o  estado do objetivo estiver null, identifica como primeira interação
        if (update.hasMessage() && update.getMessage().hasText()&& ci.getState() == null) {

            message.setText("Bem vindo ao TelegramBot Fiap - Curso FullStack");

            try {
                execute(message);
                ci.setState("Menu");

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            message.setText("Digite 'cep' para fazer uma busca de endereço por cep \nDigite 'finalizar' para encerrar o chat atual");

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        //Caso tenha digitado o 'cep', reconhecera o estado ConsultarCEP para realizar a consulta
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
                Map<String,String> mapaCep = ci.buscarCep(message.getText());
                if(mapaCep.size() <= 0) {
                    message.setText("O cep digitado não é valido, digite novamente");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }else{
                    message.setText("Endereço:" + mapaCep.get("logradouro") +
                            "\nBairro: " + mapaCep.get("bairro") +
                            "\nCidade: " + mapaCep.get("localidade") +
                            "\nEstado: " + mapaCep.get("uf"));
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
        //Encerra o chat setando o estado como null
        }else if (message.getText().equalsIgnoreCase("finalizar")) {
            ci.setState(null);
            message.setText("Até Logo");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }else{
            message.setText("Não entendi\nDigite a opção anterior ou digite 'finalizar' para encerrar");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        if (ci.getState().equals("MenuIni")){
            message.setText("Digite 'cep' para fazer uma busca de endereço por cep \nDigite 'finalizar' para encerrar o chat atual");
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
