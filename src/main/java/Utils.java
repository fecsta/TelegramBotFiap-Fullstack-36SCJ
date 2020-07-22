import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.GsonBuilder;

/**
 * Classe utilitaria que fará algumas buscas por APIs  
 */
public class Utils {
	
	public static void main(String[] args) throws IOException{
		
		Endereco buscaEnderecoPorCEP = buscaEnderecoPorCEP("04842050");
		
		System.out.println(buscaEnderecoPorCEP);
	}

	/**
	 * Dado um determinado cep, faz a busca do {@link Endereco}
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

		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
