package ie.ulster.exam;

class Room{

    private int id;
    private String name;
    private int capacity;
    private String feature;
    private boolean alcohol;


    public Room(int id, String name, int capacity, String feature, boolean alcohol){
        this.setId(id);
        this.setName(name);
        this.setCapacity(capacity);
        this.setFeature(feature);
        this.setAlcohol(alcohol);
    }

    /**
     * @return the alcohol
     */
    public boolean getAlcohol() {
        return alcohol;
    }

    /**
     * @param alcohol the alcohol to set
     */
    public void setAlcohol(boolean alcohol) {
        this.alcohol = alcohol;
    }

    /**
     * @return the feature
     */
    public String getFeature() {
        return feature;
    }

    /**
     * @param feature the feature to set
     */
    public void setFeature(String feature) {
        this.feature = feature;
    }

    /**
     * @return the capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
}