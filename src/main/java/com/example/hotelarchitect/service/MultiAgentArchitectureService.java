package com.example.hotelarchitect.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MultiAgentArchitectureService {

    private final ChatClient analysisAgent;
    private final ChatClient designAgent;
    private final ChatClient reviewAgent;

    public MultiAgentArchitectureService(ChatClient.Builder builder) throws IOException {
        // 从 classpath 读取已经整理好的 Markdown 先验知识库
        String priorKnowledge = new String(
            new ClassPathResource("prior_knowledge.md").getInputStream().readAllBytes(),
            StandardCharsets.UTF_8
        );

        // 1. 初始化 需求解析官 (AnalysisAgent) -> 负责 ADD Step 1-3
        this.analysisAgent = builder
            .defaultSystem(priorKnowledge + "\n\n" +
                "【您的角色】：需求解析官 (AnalysisAgent)\n" +
                "【工作职责】：专门负责 ADD 3.0 的 Step 1、Step 2 和 Step 3。\n" +
                "【严格指令】：你必须根据当前的迭代目标，审查输入，选择本轮的核心驱动因素（Drivers）和需要细化的系统元素。严禁引入外部领域知识。")
            .build();

        // 2. 初始化 核心架构师 (DesignAgent) -> 负责 ADD Step 4-5
        this.designAgent = builder
            .defaultSystem(priorKnowledge + "\n\n" +
                "【您的角色】：核心架构师 (DesignAgent)\n" +
                "【工作职责】：专门负责 ADD 3.0 的 Step 4 和 Step 5。\n" +
                "【严格指令】：根据 AnalysisAgent 传过来的驱动因素，选择最匹配的设计概念（Design Concepts）。实例化系统元素，分配明确的职责，并利用 Java、Angular、Kafka 技术栈定义接口与交互逻辑。同时，你【必须】使用 Mermaid 或 PlantUML 代码来生成架构视图。")
            .build();

        // 3. 初始化 质量评审员 (ReviewAgent) -> 负责 ADD Step 6-7
        this.reviewAgent = builder
            .defaultSystem(priorKnowledge + "\n\n" +
                "【您的角色】：质量评审员 (ReviewAgent)\n" +
                "【工作职责】：专门负责 ADD 3.0 的 Step 6 和 Step 7。\n" +
                "【严格指令】：记录设计决策和理由（Rationale）。严格对照《质量属性场景(QA)》审查 DesignAgent 的方案。执行自我验证与反思。如果发现 any 隐式或外部规则、或是没有推导自系统指令的内容，必须指出并要求修正。")
            .build();
    }

    /**
     * 顺序执行一轮 ADD 迭代
     */
    public String executeIterationWorkflow(int iterationId, String iterationTitle) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder logBuilder = new StringBuilder();

        logBuilder.append(String.format("[%s] [SYSTEM] >>> 启动第 %d 轮迭代: %s <<<\n\n", dtf.format(LocalDateTime.now()), iterationId, iterationTitle));

        // ---- Step 1-3: Analysis Phase ----
        String analysisPrompt = String.format("当前进入第 %d 轮迭代：%s。请执行 ADD 方法的 Step 1, Step 2, Step 3，选出本轮的驱动因素和 refinement 目标。", iterationId, iterationTitle);
        logBuilder.append(String.format("[%s] [USER -> AnalysisAgent]: %s\n", dtf.format(LocalDateTime.now()), analysisPrompt));
        
        String analysisResponse = this.analysisAgent.prompt().user(analysisPrompt).call().content();
        logBuilder.append(String.format("[%s] [AnalysisAgent]:\n%s\n\n", dtf.format(LocalDateTime.now()), analysisResponse));

        // ---- Step 4-5: Design Phase ----
        String designPrompt = String.format("基于 AnalysisAgent 的分析结果：\n%s\n\n请执行 Step 4 和 Step 5，确定设计概念、实例化元素并产出 Mermaid 架构视图代码。", analysisResponse);
        logBuilder.append(String.format("[%s] [USER -> DesignAgent]: 发送分析产物，请求架构设计。\n", dtf.format(LocalDateTime.now())));
        
        String designResponse = this.designAgent.prompt().user(designPrompt).call().content();
        logBuilder.append(String.format("[%s] [DesignAgent]:\n%s\n\n", dtf.format(LocalDateTime.now()), designResponse));

        // ---- Step 6-7: Review Phase ----
        String reviewPrompt = String.format("请审查以下架构设计是否严格满足本轮迭代目标与系统知识约束：\n【分析】:\n%s\n【设计】:\n%s\n\n请执行 Step 6 和 Step 7，记录决策理由并评审目标达成情况。", analysisResponse, designResponse);
        logBuilder.append(String.format("[%s] [USER -> ReviewAgent]: 请求对设计进行质量评审与合规性检查。\n", dtf.format(LocalDateTime.now())));
        
        String reviewResponse = this.reviewAgent.prompt().user(reviewPrompt).call().content();
        logBuilder.append(String.format("[%s] [ReviewAgent]:\n%s\n\n", dtf.format(LocalDateTime.now()), reviewResponse));

        logBuilder.append(String.format("[%s] [SYSTEM] >>> 第 %d 轮迭代多智能体协作流完成 <<<\n\n=========================================\n\n", dtf.format(LocalDateTime.now()), iterationId));

        return logBuilder.toString();
    }
}
