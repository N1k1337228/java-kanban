package taskclasses;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        type = TaskType.EPIC;
        this.endTime = null;

    }

    public void addSubTasksId(SubTask subTask) {
        subTasks.add(subTask);
        updateDateTime();
    }

    public void updateDateTime() {
        epicStartAndEndTime();
    }

    private void epicStartAndEndTime() {
        if (subTasks.isEmpty()) {
            this.startTime = null;
            this.endTime = null;
            return;
        }
        ArrayList<LocalDateTime> startTimeSubTask = new ArrayList<>();
        ArrayList<LocalDateTime> endTimeSubTask = new ArrayList<>();
        long duration = 0;
        for (SubTask subTask : subTasks) {
            if (subTask.getStartTime() == null || subTask.duration == null) {
                continue;
            }
            startTimeSubTask.add(subTask.getStartTime());
            endTimeSubTask.add(subTask.getEndTime());
        }
        startTime = Collections.min(startTimeSubTask);
        endTime = Collections.max(endTimeSubTask);
        this.duration = Duration.between(startTime, endTime);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void removeSubTasksId() {
        subTasks.clear();
        updateDateTime();
    }

    public void setEndTime(LocalDateTime time) {
        this.endTime = time;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH:mm");
        return String.format("%d,EPIC,%s,%s,%s,%s,%s,%d%n",
                id,
                name,
                status,
                description,
                startTime != null ? startTime.format(formatter) : "null",
                endTime != null ? endTime.format(formatter) : "null",
                duration.toMinutes());
    }
}
