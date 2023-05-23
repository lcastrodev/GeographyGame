package geographygame.openmeteo;

public class DataDTO {

    public String country;
    public String code;
    public String iso3;
    public PopulationCount[] populationCounts;

}

class PopulationCount {

    public int year;
    public long value;

}
