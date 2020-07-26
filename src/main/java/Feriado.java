import java.util.List;

/**
 * Classe que auxilia na desserialização do json para consulta de feriados
 */
public class Feriado {

	private List<Holiday> Holiday;

	public Feriado() {
	}

	public List<Holiday> getHoliday() {
		return Holiday;
	}

	@Override
	public String toString() {
		return "Feriado [Holiday=" + Holiday + "]";
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

	@Override
	public String toString() {
		return "Holiday [Type=" + Type + ", Description=" + Description + ", Date=" + Date + ", Name=" + Name + "]";
	}
}