package com.dean.j2ee.framework.command;

import java.io.*;
import java.util.List;

/**
 * 命令工具类
 */
public class CommandUtils {

    public static void execute(List<String> commands) throws IOException, InterruptedException {
        // 命令错误检查
        if (commands == null || commands.size() <= 0)
            return;

        // 执行命令并获取命令进程
        File wd = new File("/bin");
        Process process = Runtime.getRuntime().exec("/bin/bash", null, wd);
        if (process == null)
            return;

        PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(process.getOutputStream())), true);
        for (String command : commands) {
            System.out.println("[out]--> " + command);
            printWriter.println(command);
        }
        printWriter.println("exit");

        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader reader;
        // 获取输入流
        inputStream = process.getInputStream();
        inputStreamReader = new InputStreamReader(inputStream);
        reader = new BufferedReader(inputStreamReader);

        String line;
        while ((line = reader.readLine()) != null)
            System.out.println("[in]--> " + line);

        int state = process.waitFor();
        System.out.println("===============================================================");
        System.out.println(state == 0 ? "命令执行" : "命令执行失败!");

        inputStream.close();
        inputStreamReader.close();
        reader.close();
        printWriter.close();

        process.destroy();
    }

}
