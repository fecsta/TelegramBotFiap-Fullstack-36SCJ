import java.util.List;

/**
 * Classe que auxilia na desserialização de um json ao buscar pela previsão do
 * tempo de determinada cidade
 */

class Tempo{
	
	private Coord coord;
	private List<Weather> weather;
	private String base;
	private MainT main;
	private Long visibility;
	private Wind wind;
	private Clouds clouds;
	private Long dt;
	private Sys sys;
	private Long timezone;
	private Integer id;
	private String name;
	private Integer cod;
	
	public Tempo() {
	}

	public Coord getCoord() {
		return coord;
	}

	public List<Weather> getWeather() {
		return weather;
	}

	public String getBase() {
		return base;
	}

	public MainT getMain() {
		return main;
	}

	public Long getVisibility() {
		return visibility;
	}

	public Wind getWind() {
		return wind;
	}

	public Clouds getClouds() {
		return clouds;
	}

	public Long getDt() {
		return dt;
	}

	public Sys getSys() {
		return sys;
	}

	public Long getTimezone() {
		return timezone;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getCod() {
		return cod;
	}
	
  @Override
  public String toString() {
    return "Previsão do tempo para: " + name + main + wind;
  }  
}
class Coord{
	private Double lon;
	private Double lat;
	public Coord() {
	}
	public Double getLon() {
		return lon;
	}
	public Double getLat() {
		return lat;
	}
}
class Weather{
	private Integer id;
	private String main;
	private String description;
	private String icon;
	public Weather() {
	}
	public Integer getId() {
		return id;
	}
	public String getMain() {
		return main;
	}
	public String getDescription() {
		return description;
	}
	public String getIcon() {
		return icon;
	}	
}

class MainT{
	private  Double temp;
	private  Double feels_like;
	private  Double temp_min;
	private  Double temp_max;
	private Integer pressure;
	private  Integer humidity;
	public MainT() {
	}
	public Double getTemp() {
		return  temp;
	}
	public  Double getFeels_like() {
		return feels_like;
	}
	public  Double getTemp_min() {
		return temp_min;
	}
	public  Double getTemp_max() {
		return temp_max;
	}
	public Integer getPressure() {
		return pressure;
	}
	public  Integer getHumidity() {
		return humidity;
  }
	
	@Override
  public String toString() {
    return  "Temperatura = " + temp + " °C , Sensação Térmica = " + feels_like + " °C , Máxima = " + temp_max + " °C , Mínima = " + temp_min + " °C , Umidade do Ar = " + humidity + " % ";
  }
 
}
class Wind{
	private  Double speed;
	private Integer deg;
	public Wind() {
	}
	public  Double getSpeed() {
		return speed;
	}
	public Integer getDeg() {
		return deg;
	}
	
	@Override
  public String toString() {
    return  speed + " km/h";
	}
	
}
class Clouds{
	private Integer all;
	public Clouds() {
	}
	public Integer getAll() {
		return all;
	}
}
class Sys{
	private Integer type;
	private Integer id;
	private String country;
	private Long sunrise;
	private Long sunset;
	public Sys() {
	}
	public Integer getType() {
		return type;
	}
	public Integer getId() {
		return id;
	}
	public String getCountry() {
		return country;
	}
	public Long getSunrise() {
		return sunrise;
	}
	public Long getSunset() {
		return sunset;
	}	
} 
