package pt.isel.g11.hagreve;

/**
 * Created by megs on 20/03/2015.
 */
public class Strike {
    private String description, endDate, sourceLink, startDate, company,
    starting,until, partial, total, type ;
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

        type = MainActivity.getStringFromResources(R.string.type);
        partial = MainActivity.getStringFromResources(R.string.partial);
        total = MainActivity.getStringFromResources(R.string.allDay);
        starting = MainActivity.getStringFromResources(R.string.starting);
        until = MainActivity.getStringFromResources(R.string.until);
    }

    @Override
    public String toString(){
        return company + "\nDe:" + startDate + "\nA: " + endDate +"\n" + description;
    }

    public String getCompany(){
        return company;
    }

    public String getDates() {
        return starting + " " + startDate + "\n" + until + " " + endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    public String getSourceLink(){ return sourceLink; }

    public String getIsParcial() { return type + " " + ( allDay ? total : partial); }

    public boolean isParcial() { return allDay; }
}
