package com.shenyubao.javachain.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shenyubao
 * @date 2023/6/27 13:11
 */
@Slf4j
public class PythonUtils {
    private static final String OS = System.getProperty("os.name");


    /**
     * 调用python方法
     *
     * @param pythonFilePath
     * @param args
     * @return
     */
    public static String invokeMethod(String pythonFilePath, String... args) {
        return executePythonCode(getCommand(pythonFilePath, args));
    }

    /**
     * 通过resource文件调用python方法
     *
     * @param classType
     * @param pythonFileName
     * @param args
     * @return
     */
    public static String invokeMethodAsResource(Class<?> classType, String pythonFileName, String... args) {
        String pythonFilePath = classType.getClassLoader().getResource(pythonFileName).getPath();
        return executePythonCode(getCommand(pythonFilePath, args));
    }

    public static String executePythonCode(String... command) {
        try {
            Process process = new ProcessBuilder(command).start();

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String errorResult = read(errorReader);
            log.info("read python error={}", errorResult);

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String successResult = read(inputReader);
            log.info("read python success={}", successResult);

            return successResult;
        } catch (Exception e) {
            log.error("executePythonCode error", e);
            return null;
        }
    }

    private static String[] getCommand(String pythonFilePath, String... args) {
        List<String> commands = new ArrayList<>();
        if (OS.startsWith("Windows")) {
            commands.add("cmd.exe");
            commands.add("/c");
            commands.add("python");
        } else {
            commands.add("python3");
        }
        commands.add(pythonFilePath);
        if (args != null) {
            commands.addAll(Arrays.asList(args).stream()
                    .filter(o -> o != null).collect(Collectors.toList()));
        }
        return commands.toArray(new String[]{});
    }

    private static String read(BufferedReader reader) {
        List<String> resultList = new ArrayList<>();
        String response = StringUtils.EMPTY;
        while (true) {
            try {
                if (!((response = reader.readLine()) != null))
                    break;
            } catch (IOException e) {
            }
            resultList.add(response);
        }
        return resultList.stream().collect(Collectors.joining("\n"));
    }
}
