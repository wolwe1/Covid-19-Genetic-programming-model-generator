package data.infrastructure;

import data.models.CovidEntry;
import data.models.IFileEntry;

import java.util.ArrayList;
import java.util.List;

public class Covid19FileReader extends IFileReader{

    private final List<CovidEntry> _entries;

    public Covid19FileReader(String baseDirectory) {
        super(baseDirectory);
        
        setTemplate( new CovidEntry());
        _entries = new ArrayList<>();
    }

    public void readFile(String fileName) throws Exception {
        super.readFile(fileName,true);

        for (IFileEntry row : _rows) {
            _entries.add( (CovidEntry)row);
        }
    }

    public List<CovidEntry> getData(){
        return _entries;
    }
}
