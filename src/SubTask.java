public class SubTask extends Task{

    private int EpicId;
    SubTask (String name, String description,int EpicId, int id) {
        super(name,description,id);
        this.EpicId = EpicId;
    }
    public int getEpicId(){
        return EpicId;
    }
}
