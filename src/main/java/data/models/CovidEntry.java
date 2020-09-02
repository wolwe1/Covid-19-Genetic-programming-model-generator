package data.models;

import data.models.IFileEntry;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("SpellCheckingInspection")
public class CovidEntry implements IFileEntry {

    public int serialNumber;
    public Date observationDate;
    public String province;
    public String state;
    public String country;
    public Date lastUpdate;
    public double confirmedCases;
    public double deaths;
    public double recoveries;

    @Override
    public IFileEntry MakeCopy(String[] data) throws Exception {
        //Three date formats possible
        //
        // mm/dd/yy hh:mm
        // mm/dd/yy
        // yyyy-mm-dd hh:mm:ss
        try{
            CovidEntry copy = new CovidEntry();

            copy.serialNumber = Integer.parseInt(data[0]);
            copy.observationDate = new SimpleDateFormat("MM/dd/yy").parse(data[1]);
            copy.province = data[2];

            if(data.length == 8){

                copy.state = "N/A";
                copy.country = data[3];
                try{
                    copy.lastUpdate =  new SimpleDateFormat("MM/dd/yy hh:mm").parse(data[4]);
                }catch(Exception e){

                    try{
                        copy.lastUpdate =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(data[4]);
                    }catch(Exception e2){
                        copy.lastUpdate =  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(data[4]);
                    }
                }
                copy.confirmedCases = Double.parseDouble(data[5]);
                copy.deaths = Double.parseDouble(data[6]);
                copy.recoveries = Double.parseDouble(data[7]);

            }else if(data.length == 9){

                copy.state = data[3];
                copy.country = data[4];
                try{
                    copy.lastUpdate =  new SimpleDateFormat("MM/dd/yy hh:mm").parse(data[5]);
                }catch(Exception e){
                    try{
                        copy.lastUpdate =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(data[5]);
                    }catch(Exception e2){
                        copy.lastUpdate =  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(data[5]);
                    }
                }
                copy.confirmedCases = Double.parseDouble(data[6]);
                copy.deaths = Double.parseDouble(data[7]);
                copy.recoveries = Double.parseDouble(data[8]);
            }else
            {
                throw new Exception("UNKNOWN FORMAT");
            }


            //Reformat dates to single format

            copy.observationDate = new SimpleDateFormat("yyyy-MM-dd").parse(copy.observationDate.toString());
            copy.lastUpdate = new SimpleDateFormat("yyyy-MM-dd").parse(copy.lastUpdate.toString());
            return copy;

        }catch(Exception e){

            throw new Exception("Could not parse covid entry: " + e.getMessage());
        }
    }

    public CovidEntry makeCopy() {

        CovidEntry newEntry = new CovidEntry();
        newEntry.serialNumber = serialNumber;
        newEntry.observationDate = observationDate;
        newEntry.province = province;
        newEntry.state = state;
        newEntry.country = country;
        newEntry.lastUpdate = lastUpdate;
        newEntry.confirmedCases = confirmedCases;
        newEntry.deaths = deaths;
        newEntry.recoveries = recoveries;

        return newEntry;
    }
}