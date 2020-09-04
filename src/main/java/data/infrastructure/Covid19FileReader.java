package data.infrastructure;

import data.models.CovidEntry;
import data.models.IFileEntry;

import java.util.ArrayList;
import java.util.List;

public class Covid19FileReader extends IFileReader{

    private final List<CovidEntry> _entries;
    private List<CovidEntry> _chosenEntries;
    private String _country;

    public Covid19FileReader(String baseDirectory) {
        super(baseDirectory);
        
        setTemplate( new CovidEntry());
        _entries = new ArrayList<>();
        _chosenEntries = new ArrayList<>();
    }

    public void readFile(String fileName) throws Exception {
        super.readFile(fileName,true);

        for (IFileEntry row : _rows) {
            _entries.add( (CovidEntry)row);
        }

        _chosenEntries = _entries;
    }

    public List<CovidEntry> getData(){
        return _chosenEntries;
    }

    public boolean setCountry(String country) {
        _country = country;
        List<CovidEntry> entriesForCountry = new ArrayList<>();

        for (CovidEntry entry : _entries) {
            if(entry.country.toUpperCase().equals(country.toUpperCase()))
                entriesForCountry.add(entry);
        }

        if(entriesForCountry.size() != 0){
            _chosenEntries = entriesForCountry;
            return true;
        }else{
            return false;
        }
    }
}
