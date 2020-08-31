package data.infrastructure;

import data.models.IFileEntry;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public abstract class IFileReader {

    protected final List<IFileEntry> _rows;
    protected final String _baseDir;
    protected IFileEntry _template;

    public IFileReader(String baseDirectory){
        _baseDir = baseDirectory;
        _rows = new ArrayList<>();
    }

    public void setTemplate(IFileEntry template){
        _template = template;
    }

    protected void readFile(String fileName,boolean skipHeader) throws Exception {

        BufferedReader csvReader = null;
        String row;

        try {
            csvReader = new BufferedReader(new FileReader(_baseDir + fileName));
        } catch (FileNotFoundException e) {
            throw new Exception("The file could not be found :" + e.getMessage());
        }

        if(skipHeader)
            csvReader.readLine();

        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");

            _rows.add( _template.MakeCopy(data) );
            // do something with the data
        }
        csvReader.close();
    }

}
