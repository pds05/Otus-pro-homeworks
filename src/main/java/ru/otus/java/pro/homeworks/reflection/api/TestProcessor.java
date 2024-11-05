package ru.otus.java.pro.homeworks.reflection.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

public class TestProcessor implements Tester {
    private Class testClass;
    private Map<Method, TestResult> result = new LinkedHashMap<>();

    public TestProcessor(Class clazz) {
        this.testClass = clazz;
    }


    @Override
    public void test() {
        TargetMethods targetMethods = findMethods();
        Method beforeSuiteMethod = targetMethods.getBeforeSuiteMethod();
        Object obj;
        try {
            obj = Arrays.stream(testClass.getDeclaredConstructors()).filter(c -> c.getParameterCount() == 0).findFirst().get().newInstance(null);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new TestException("Object " + testClass.getName() + " creation error");
        }
        if (beforeSuiteMethod != null) {
            try {
                beforeSuiteMethod.invoke(obj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new TestException("Error invoking " + " method " + testClass.getName() + "." + beforeSuiteMethod.getName());
            }
        }
        Stream.of(targetMethods.getTestMethodMap()).flatMap(methods -> methods.values().stream())
                .forEach(entry -> entry.stream().forEach(m -> {
                    try {
                        m.invoke(obj);
                        result.put(m, TestResult.OK);
                    } catch (Exception e) {
                        result.put(m, TestResult.NOK);
                        e.printStackTrace();
                    }
                }));
        Method afterSuiteMethod = targetMethods.getAfterSuiteMethod();
        if (afterSuiteMethod != null) {
            try {
                afterSuiteMethod.invoke(obj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new TestException("Error invoking " + " method " + testClass.getName() + "." + afterSuiteMethod.getName());
            }
        }

        System.out.println("Test result:");
        result.entrySet().stream().forEach(entry ->
                System.out.println(entry.getKey().getName() + " - " + entry.getValue())
        );
    }

    private TargetMethods findMethods() {
        TargetMethods targetMethods = new TargetMethods();
        Method[] methods = testClass.getDeclaredMethods();
        validate(methods);

        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                int priority = method.getAnnotation(Test.class).priority();
                targetMethods.setTestMethod(priority, method);
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                if (targetMethods.getAfterSuiteMethod() == null) {
                    targetMethods.setAfterSuiteMethod(method);
                }
            } else if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (targetMethods.getBeforeSuiteMethod() == null) {
                    targetMethods.setBeforeSuiteMethod(method);
                }
            }
        }
        return targetMethods;
    }

    private void validate(Method[] methods) {
        int[] countBeforeSuiteMethod = new int[]{0};
        int[] countAfterSuiteMethod = new int[]{0};
        int[] countTestMethod = new int[]{0};
        Stream.of(methods).forEach(method -> {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                countBeforeSuiteMethod[0]++;
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                countAfterSuiteMethod[0]++;
            } else if (method.isAnnotationPresent(Test.class)) {
                int priority = method.getAnnotation(Test.class).priority();
                if (priority < 1 || priority > 10) {
                    throw new TestException("Invalid Test priority " + priority + " for method " + method.getName() +". Value must be between 1 and 10");
                }
                countTestMethod[0]++;
            }
        });
        if (countBeforeSuiteMethod[0] > 1) {
            throw new TestException("Class " + testClass.getName() + " has more than one BeforeSuite method");
        }
        if (countAfterSuiteMethod[0] > 1) {
            throw new TestException("Class " + testClass.getName() + " has more than one AfterSuite method");
        }
        if (countTestMethod[0] == 0) {
            throw new TestException("Class " + testClass.getName() + " has no Test method");
        }
    }

    class TargetMethods {
        private Map<Integer, List<Method>> testMethodMap = new TreeMap<>((o1, o2) -> o2 - o1);
        ;
        private Method beforeSuiteMethod;
        private Method afterSuiteMethod;

        public Map<Integer, List<Method>> getTestMethodMap() {
            return testMethodMap;
        }

        public void setTestMethodMap(Map<Integer, List<Method>> testMethodMap) {
            this.testMethodMap = testMethodMap;
        }

        public void setTestMethod(int priority, Method method) {
            if (!testMethodMap.containsKey(priority)) {
                testMethodMap.put(priority, new ArrayList<>());
            }
            testMethodMap.get(priority).add(method);
        }

        public Method getTestMethod(String methodName) {
            return testMethodMap.values().stream().flatMap(methods -> methods.stream().filter(m -> m.getName().equals(methodName))).findFirst().orElse(null);
        }

        public List<Method> getTestMethods(int priority) {
            return testMethodMap.get(priority);
        }

        public Method getBeforeSuiteMethod() {
            return beforeSuiteMethod;
        }

        public void setBeforeSuiteMethod(Method beforeSuiteMethod) {
            this.beforeSuiteMethod = beforeSuiteMethod;
        }

        public Method getAfterSuiteMethod() {
            return afterSuiteMethod;
        }

        public void setAfterSuiteMethod(Method afterSuiteMethod) {
            this.afterSuiteMethod = afterSuiteMethod;
        }
    }

    enum TestResult {
        OK, NOK
    }
}
