package geographygame.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Country {

    private String[] names;
    private String capital;
    private String map;
    private String iso2;
    private String iso3;
    private PopulationCount population;
    private int currentWeather;

    public void setCurrentWeather(int currentWeather) {
        this.currentWeather = currentWeather;
    }

    public CountryDTO toDTO() {
        CountryDTO dto = new CountryDTO();
        dto.names = names;
        dto.capital = capital;
        dto.map = String.format("https://upload.wikimedia.org/wikipedia/commons/thumb%s", map);
        dto.flag = String.format("https://flagcdn.com/h240/%s.jpg", iso2.toLowerCase());
        dto.population = population;
        dto.currentWeather = currentWeather;
        return dto;
    }

}

@AllArgsConstructor
@Getter
class PopulationCount {

    private int year;
    private long value;

}
