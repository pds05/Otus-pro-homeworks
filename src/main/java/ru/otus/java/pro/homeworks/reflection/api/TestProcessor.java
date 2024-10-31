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
        Method m0 = targetMethods.getBeforeSuiteMethod();
        Object obj;
        try {
            obj = Arrays.stream(testClass.getDeclaredConstructors()).filter(c -> c.getParameterCount() == 0).findFirst().get().newInstance(null);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if (m0 != null) {
            try {
                m0.invoke(obj);
                result.put(m0, TestResult.OK);
            } catch (IllegalAccessException | InvocationTargetException e) {
                result.put(m0, TestResult.NOK);
                e.printStackTrace();
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
        Method m11 = targetMethods.getAfterSuiteMethod();
        if (m11 != null) {
            try {
                m11.invoke(obj);
                result.put(m11, TestResult.OK);
            } catch (IllegalAccessException | InvocationTargetException e) {
                result.put(m11, TestResult.NOK);
                e.printStackTrace();
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

        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                int priority = method.getAnnotation(Test.class).priority();
                if (priority < 1) {
                    priority = 1;
                } else if (priority > 10) {
                    priority = 10;
                }
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
