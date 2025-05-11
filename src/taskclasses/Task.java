package taskclasses;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected TaskType type;
    protected LocalDateTime startTime;
    protected Duration duration;
    protected LocalDateTime endTime;

    public Task(String name, String description, long taskExecutionTime) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
        type = TaskType.TASK;
        startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(taskExecutionTime);
        endTime = startTime.plus(duration);

    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public TaskType getType() {
        return type;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime time) {
        this.startTime = time.plusYears(0);
    }

    public void setEndTime(LocalDateTime time) {
        this.endTime = time.plusYears(0);
    }

    public void setDuration(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH:mm");
        return String.format("%d,TASK,%s,%s,%s,%s,%s,%d%n",
                id,
                name,
                status,
                description,
                startTime.format(formatter),
                endTime.format(formatter),
                duration.toMinutes());
    }
}
