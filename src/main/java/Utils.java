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
    
		Tempo buscaPrevisaoTempoCidade = buscaPrevisaoTempoCidade("Paris");
	
		System.out.println(buscaPrevisaoTempoCidade);
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

			System.out.println(jsonSb);
			
			return new GsonBuilder().create().fromJson(jsonSb.toString(), Endereco.class);

		} 
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Fornecida a cidade, faz a busca da {@link Previsão do Tempo}
	 * @param cidade
	 * @return {@link Previsão do Tempo}
	 * @throws IOException
	 */
	public static Tempo buscaPrevisaoTempoCidade(String cidade) {
		
		try {
			URL urlTempo = new URL(String.format("http://api.openweathermap.org/data/2.5/weather?q=" + cidade + "&appid=4a601093649a88540fad287e496bfc07&units=metric"));
			BufferedReader brd = new BufferedReader(new InputStreamReader(urlTempo.openStream()));
			StringBuilder jsonSt = new StringBuilder();
			String line;

        while((line = brd.readLine()) != null) {
          jsonSt.append(line);
        }

			brd.close();

			System.out.println(jsonSt);
		
			
			return new GsonBuilder().create().fromJson(jsonSt.toString(), Tempo.class);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
