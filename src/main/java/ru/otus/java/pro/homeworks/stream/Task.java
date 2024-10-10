package ru.otus.java.pro.homeworks.stream;

import lombok.*;

@Builder()
@ToString
public class Task {
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    @NonNull
    private TaskStatus taskStatus;


    public enum TaskStatus {

        COMPLETED(5), CREATED(1), WAITING(3), OPENED(2), IN_PROGRESS(4);

        @Getter
        private int order;

        TaskStatus(int order) {
            this.order = order;
        }
    }
}
