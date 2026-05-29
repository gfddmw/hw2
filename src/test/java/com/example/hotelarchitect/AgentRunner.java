package com.example.hotelarchitect;

import com.example.hotelarchitect.service.MultiAgentArchitectureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootTest
public class AgentRunner {

    @Autowired
    private MultiAgentArchitectureService architectureService;

    @Test
    public void runFullAssignment() throws IOException {
        StringBuilder fullConversationLog = new StringBuilder();

        // 作业要求的 4 轮迭代计划
        String[] iterations = {
            "Iteration 1: Establishing an Overall System Structure",
            "Iteration 2: Identifying Structures to Support Primary Functionality",
            "Iteration 3: Addressing Reliability and Availability Quality Attributes",
            "Iteration 4: Addressing Development and Operations"
        };

        // 依次执行多智能体工作流
        for (int i = 0; i < iterations.length; i++) {
            String log = architectureService.executeIterationWorkflow(i + 1, iterations[i]);
            fullConversationLog.append(log);
            System.out.println(log); // 实时打印到控制台
        }

        // 自动把带时间戳的完整对话日志持久化保存为文件，防止控制台缓冲区冲掉
        try (FileWriter writer = new FileWriter("complete_conversation_log.txt")) {
            writer.write(fullConversationLog.toString());
        }
        System.out.println("成功保存交付物：complete_conversation_log.txt");
    }
}
