package taskclasses;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Epic extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, 0);
        type = TaskType.EPIC;
        this.duration = Duration.ofMinutes(0);
        this.startTime = null;
        this.endTime = null;

    }

    public void addSubTasksId(SubTask subTask) {
        subTasks.add(subTask);
        updateDateTime();

    }

    private void updateDateTime() {
        epicDuration();
        epicStartTime();
        epicEndTime();
    }

    private void epicDuration() {
        if (subTasks.isEmpty()) {
            this.duration = Duration.ZERO;
        }
        long duration = getSubTasks().stream()
                .mapToLong(time -> time.duration.toMinutes())
                .sum();
        this.duration = Duration.ofMinutes(duration);
    }

    private void epicStartTime() {
        if (subTasks.isEmpty()) {
            this.startTime = null;
        }
        LocalDateTime localDateTime = subTasks.stream()
                .map(SubTask::getStartTime)
                .min(LocalDateTime::compareTo).get();
        this.startTime = localDateTime.plusYears(0);

    }

    private void epicEndTime() {
        if (subTasks.isEmpty()) {
            this.startTime = null;
        }
        LocalDateTime localDateTime = subTasks.stream()
                .map(SubTask::getEndTime)
                .max(LocalDateTime::compareTo).get();
        this.endTime = localDateTime.plusYears(0);
    }


    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void removeSubTasksId() {
        subTasks.clear();
    }

    @Override
    public void setStartTime(LocalDateTime time) {
        if (time == null) {
            this.startTime = null;
        } else {
            this.startTime = time.plusYears(0);
        }
    }

    @Override
    public void setEndTime(LocalDateTime time) {
        if (time == null) {
            this.endTime = null;
        } else {
            this.endTime = time.plusYears(0);
        }
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
