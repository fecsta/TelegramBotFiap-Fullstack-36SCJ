import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.JSONObject;
import org.json.XML;

import com.google.gson.GsonBuilder;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;


/**
 * Classe utilitaria que fará algumas buscas por APIs  
 */
public class Utils {
	
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
	/**
	 * Dado um determinado cep, faz a busca do {@link Endereco}
	 * 
	 * @param cep
	 * @return {@link Endereco}
	 * @throws IOException
	 */
	public static Endereco buscaEnderecoPorCEP(String cep) {

		String urlString = String.format("http://viacep.com.br/ws/%s/json", cep);
		
		try {

			String jsonString = buscaUrl(urlString);

			return new GsonBuilder().create().fromJson(jsonString, Endereco.class);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Dado uma determinada url, faz a busca na api e retorna uma string com valores(json ou xml)
	 * @param urlString
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static String buscaUrl(String urlString) throws MalformedURLException, IOException {
		
		URL url = new URL(urlString);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

		StringBuilder string = new StringBuilder();

		in.lines().forEach(l -> string.append(l.trim()));
		
		in.close();
		
		return string.toString();
	}

	/**
	 * Dado um determinado ano, faz a buscas dos {@link Feriado}
	 * @param ano
	 * @return
	 */
	public static Feriado buscaFeriadosPorAno(int ano) {
		
		String urlString = String.format("http://services.sapo.pt/Holiday/GetNationalHolidays?year=%s", ano);
		
		try {
			
			String xmlString = buscaUrl(urlString);
			
			JSONObject paisesJson = XML.toJSONObject(xmlString);			
			JSONObject getNationalHolidaysResponse = (JSONObject) paisesJson.get("GetNationalHolidaysResponse");
			Object getNationalHolidaysResult = getNationalHolidaysResponse.get("GetNationalHolidaysResult");
			
			return new GsonBuilder().create().fromJson(getNationalHolidaysResult.toString(), Feriado.class);
			
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

	public static void main(String[] args) throws IOException{
    
		Tempo buscaPrevisaoTempoCidade = buscaPrevisaoTempoCidade("Paris");
		Endereco buscaEnderecoPorCep = buscaEnderecoPorCEP("04671010");
		Feriado buscaFeriadosPorAno = buscaFeriadosPorAno(2020);
	
		System.out.println(buscaPrevisaoTempoCidade);
		System.out.println(buscaEnderecoPorCep);
		System.out.println(buscaFeriadosPorAno);
	}
}
