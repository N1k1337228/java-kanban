public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;

    Task (String name, String description, int id) {

        this.name = name;
        this.description = description;
        this.id = id;
        status = Status.NEW;
    }

    void setStatus (Status status) {
        this.status = status;
    }

    Status getStatus () {
        return status;
    }

    int getId(){
        return id;
    }


}
