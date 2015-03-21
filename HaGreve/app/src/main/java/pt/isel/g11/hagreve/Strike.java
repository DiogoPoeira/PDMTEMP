package pt.isel.g11.hagreve;

/**
 * Created by megs on 20/03/2015.
 */
public class Strike {
    private String description, endDate, sourceLink, startDate, company;
    private boolean allDay, state;

    public Strike(String desc, String ed, String sl, boolean ad,
                  String sd, boolean stt, String cmpn){
        this.description = desc;
        this.endDate = ed;
        this.sourceLink = sl;
        this.allDay = ad;
        this.startDate = sd;
        this.state = stt;
        this.company = cmpn;
    }

    @Override
    public String toString(){
        return company+"\n"+startDate+"\n";
    }

    public String getCompany(){
        return company;
    }

    public String getStart(){
        return startDate;
    }

    public String getEnd(){
        return endDate;
    }

}
