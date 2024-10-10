package ru.otus.java.pro.homeworks.stream;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class TaskManager {

    public static void main(String[] args) {
        List<Task> taskList = List.of(
                Task.builder().id(1).name("Сделать зарядку").taskStatus(Task.TaskStatus.COMPLETED).build(),
                Task.builder().id(2).name("Позавтракать").taskStatus(Task.TaskStatus.COMPLETED).build(),
                Task.builder().id(3).name("Поездка на работу").taskStatus(Task.TaskStatus.IN_PROGRESS).build(),
                Task.builder().id(4).name("Почитать книгу").taskStatus(Task.TaskStatus.OPENED).build(),
                Task.builder().id(5).name("Провести рабочую встречу").taskStatus(Task.TaskStatus.WAITING).build(),
                Task.builder().id(6).name("Заполнить отчет").taskStatus(Task.TaskStatus.CREATED).build(),
                Task.builder().id(7).name("Посещение тренировки").taskStatus(Task.TaskStatus.CREATED).build());

        //Вывести список выполненных задач
        List<Task> completedTask = taskList.stream().filter(t -> t.getTaskStatus() == Task.TaskStatus.COMPLETED).toList();
        System.out.println(completedTask);

        System.out.println("--------");

        //Проверить задачу с id=5
        Optional<Task> result = taskList.stream().filter(t -> t.getId() == 5).findFirst();
        System.out.println(result.get());

        System.out.println("--------");

        //Сортировать по статусу
        taskList.stream()
                .sorted(new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                        return o1.getTaskStatus().getOrder() - o2.getTaskStatus().getOrder();
                    }
                }).forEach(System.out::println);

        System.out.println("--------");

        //Количество задач по статусу
        Long count = taskList.stream().filter(t -> t.getTaskStatus() == Task.TaskStatus.COMPLETED).count();
        System.out.println("Completed tasks = " + count);
    }
}
