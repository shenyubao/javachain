package com.shenyubao.javachain.tool.calculator;

import com.shenyubao.javachain.llms.BaseLLM;
import com.shenyubao.javachain.tool.BaseTool;
import com.shenyubao.javachain.tool.ToolExecuteResult;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenyubao
 * @date 2023/6/28 22:47
 */
@Slf4j
public class LLMMathTool extends BaseTool {
    private BaseLLM llm;

    public LLMMathTool() {
        setName("Calculator");
        setDescription("Useful for when you need to answer questions about math.");
    }

    @Override
    public ToolExecuteResult run(String toolInput) {
        try {
            toolInput = toolInput.replaceAll("\\^", "**");
            Process process = new ProcessBuilder("python3",
                    LLMMathTool.class.getClassLoader().getResource("llmmath.py").getPath(),
                    toolInput).start();
            // 脚本执行异常时的输出信息
            BufferedReader errorReader;
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            List<String> errorString = read(errorReader);
            log.info("读取python文件 异常 errorString={}", errorString);

            // 脚本执行正常时的输出信息
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            List<String> returnString = read(inputReader);
            log.info("读取python文件 returnString={}", returnString);

//            return "Answer: 3.991298452658078";
            return new ToolExecuteResult("Answer: " + returnString);
        } catch (Exception e) {
            log.error("numexpr error", e);
            return null;
        }
    }

    private static List<String> read(BufferedReader reader) {
        List<String> resultList = new ArrayList<>();
        String res = "";
        while (true) {
            try {
                if ((res = reader.readLine()) == null) break;
            } catch (IOException e) {
            }
            resultList.add(res);
        }
        return resultList;
    }

    public BaseLLM getLlm() {
        return llm;
    }

    public void setLlm(BaseLLM llm) {
        this.llm = llm;
    }
}
