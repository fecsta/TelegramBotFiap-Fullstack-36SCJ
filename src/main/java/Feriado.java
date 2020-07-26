import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe que auxilia na desserialização do json para consulta de feriados
 */
public class Feriado {

	private List<Holiday> Holiday;
	private Map<String, Holiday> mapaDataFeriado = new HashMap<String, Holiday>();

	public Feriado() {
	}

	public List<Holiday> getHoliday() {
		return Holiday;
	}

	@Override
	public String toString() {
		return "Feriado [Holiday=" + Holiday + "]";
	}
	
	public Holiday buscaNoMapaPorData(String data) {
		mapaDataFeriado = this.getHoliday().stream().collect(Collectors.toMap(h -> h.getDateFormatado(), h -> h));
		return mapaDataFeriado.get(data);
	}
}

class Holiday {

	private String Type;
	private String Description;
	private String Date;
	private String Name;

	public Holiday() {
	}

	public String getType() {
		return Type;
	}

	public String getDescription() {
		return Description;
	}

	public String getDate() {
		return Date;
	}

	public String getName() {
		return Name;
	}
	
	public String getDateFormatado() {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = (Date)formatter.parse(Date.replace("T", " "));
			
			return new SimpleDateFormat("dd/MM/yyyy").format(date);
		}
		catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getHolidayFormatado() {
		
		StringBuilder holiday = new StringBuilder();
		holiday.append("Dia: ").append(this.getDateFormatado());
		holiday.append("Tipo feriado: ").append(this.getType());
		holiday.append("Nome: ").append(this.getName());
		
		return holiday.toString();
	}
	
	@Override
	public String toString() {
		return "Holiday [Type=" + Type + ", Description=" + Description + ", Date=" + Date + ", Name=" + Name + "]";
	}
}