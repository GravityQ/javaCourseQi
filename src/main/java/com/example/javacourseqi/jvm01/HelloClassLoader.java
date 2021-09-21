package com.example.javacourseqi.jvm01;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 齐军林
 * @date 2021/9/18 3:26 下午
 */
public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) {
        HelloClassLoader loader = new HelloClassLoader();
        System.out.println(loader.getParent());
        try {
            Object instance = loader.findClass("Hello").newInstance();
            for (Method method : instance.getClass().getMethods()) {
                if (method.getName().equals("hello")) {
                    try {
                        method.invoke(instance);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 如果支持包名, 则需要进行路径转换
        String resourcePath = name.replace(".", "/");
        // 文件后缀
        final String suffix = ".xlass";
        // 获取输入流
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(resourcePath + suffix);
        byte[] bytes = new byte[0];
        try {
            bytes = new byte[stream.available()];
            stream.read(bytes);
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (255 - bytes[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return defineClass(name, bytes, 0, bytes.length);
    }


}
