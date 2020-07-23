import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import com.google.gson.GsonBuilder;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;

/**
 * Classe utilitaria que fará algumas buscas por APIs
 */
public class Utils {

	/**
	 * Dado um determinado cep, faz a busca do {@link Endereco}
	 * 
	 * @param cep
	 * @return {@link Endereco}
	 * @throws IOException
	 */
	public static Endereco buscaEnderecoPorCEP(String cep) {

		try {

			URL url = new URL(String.format("http://viacep.com.br/ws/%s/json", cep));
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			StringBuilder jsonSb = new StringBuilder();

			in.lines().forEach(l -> jsonSb.append(l.trim()));

			in.close();

			return new GsonBuilder().create().fromJson(jsonSb.toString(), Endereco.class);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Dado um determinado cep, faz a validação
	 * @param cep
	 * @return
	 */
	public static boolean validaCEP(String cep) {
		String padrao = "\\d{5}\\d{3}";
		return cep.matches(padrao);
	}

	/**
	 * Dado um determinado cpf, faz a validação dele
	 * @param cpf
	 * @return true valido, false invalido
	 */
	public static boolean validaCPF(String cpf) {
		
		CPFValidator cpfValidator = new CPFValidator(); 
		List<ValidationMessage> erros = cpfValidator.invalidMessagesFor(cpf); 
		
		if(erros.size() > 0) 
			return false;
		
		return true;
	}
}
