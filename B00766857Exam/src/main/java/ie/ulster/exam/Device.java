package ie.ulster.exam;

class Device{

    private String deviceName;
    private int duration;
    private int noOfCopies;
    private boolean offsite;


    public Device(String deviceName, int duration, int noOfCopies, boolean offsite){
        this.setName(deviceName);
        this.setDuration(duration);
        this.setNoOfCopies(noOfCopies);
        this.setOffsite(offsite);
    }

  /**
     * @return the device name
     */
    public String getName() {
        return deviceName;
    }

   

    /**
     * @return the duration    */
    public int getDuration() {
        return duration;
    }



    /**
     * @return the noOfCopies
     */
    public int getNoOfCopies() {
        return noOfCopies;
    }

   


  /**
     * @return offsite
     */
    public boolean getOffSite() {
        return offsite;
    }

  

    private void setName(String deviceName2) {
    }

    private void setOffsite(boolean offsite2) {
    }

    private void setNoOfCopies(int noOfCopies2) {
    }

    private void setDuration(int duration2) {
    }

  

}