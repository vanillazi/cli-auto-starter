package cn.vanillazi.tool;

import com.sun.jna.Platform;
import lombok.Getter;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Getter
    static class Person{
        public String name;

        public void test(){

        }
    }

    @FunctionalInterface
    interface Func<T,R> extends Function<T,R>, Serializable {

    }

    @FunctionalInterface
    interface Func1 extends Serializable {
        String func(Object obj);
    }

    public void consume(Func func){

    }

    @Test
    public void test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        var p=new Person();
         Func<Person,Object> a=Person::getName;
         var ac=a.getClass();
         System.out.println(a.getClass().getName());
         var ms=ac.getDeclaredMethods();
        for (Method m : ms) {
            System.out.println(m.getName());
        }
        var wr=(SerializedLambda)ac.getDeclaredMethod("writeReplace").invoke(a);
        System.out.println(wr.getImplMethodName());
    }

    @Test
    public void testOSAndArch(){
        var arch=System.getProperty("os.arch");
        var name=System.getProperty("os.name");
    }
}