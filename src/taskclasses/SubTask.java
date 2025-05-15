package taskclasses;

import java.time.format.DateTimeFormatter;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        type = TaskType.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH:mm");
        return String.format("%d,SUBTASK,%s,%s,%s,%s,%s,%d,%d%n",
                id,
                name,
                status,
                description,
                startTime != null ? startTime.format(formatter) : "null",
                getEndTime() != null ? getEndTime().format(formatter) : "null",
                duration.toMinutes(),
                epicId);
    }
}