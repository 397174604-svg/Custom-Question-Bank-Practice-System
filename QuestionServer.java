import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Random;
import com.sun.net.httpserver.*;

public class QuestionServer {
    public static void main(String[] args) throws IOException {
        // 创建HTTP服务器
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/api/questions", new QuestionsHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8000");
    }

    static class QuestionsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                // 读取题目文件
                List<String> choiceQuestions = new ArrayList<>();
                List<String> choiceAnswers = new ArrayList<>();
                readQuestionsAndAnswers("选择.txt", choiceQuestions, choiceAnswers);

                List<String> judgmentQuestions = new ArrayList<>();
                List<String> judgmentAnswers = new ArrayList<>();
                readQuestionsAndAnswers("判断.txt", judgmentQuestions, judgmentAnswers);

                List<String> fillQuestions = new ArrayList<>();
                List<String> fillAnswers = new ArrayList<>();
                readQuestionsAndAnswers("填空.txt", fillQuestions, fillAnswers);

                List<String> explanationQuestions = new ArrayList<>();
                List<String> explanationAnswers = new ArrayList<>();
                readQuestionsAndAnswers("名词解释.txt", explanationQuestions, explanationAnswers);

                // 随机选择题目
                int choiceQuestionsToShow = Math.min(60, choiceQuestions.size());
                int judgmentQuestionsToShow = Math.min(10, judgmentQuestions.size());
                int fillQuestionsToShow = Math.min(5, fillQuestions.size());
                int explanationQuestionsToShow = Math.min(20, explanationQuestions.size());

                int[] choiceIndices = generateRandomIndices(choiceQuestions.size());
                int[] judgmentIndices = generateRandomIndices(judgmentQuestions.size());
                int[] fillIndices = generateRandomIndices(fillQuestions.size());
                int[] explanationIndices = generateRandomIndices(explanationQuestions.size());

                // 构建JSON响应
                StringBuilder response = new StringBuilder();
                response.append("{");

                // 添加选择题
                response.append("\"choice\": [");
                for (int i = 0; i < choiceQuestionsToShow && i < choiceIndices.length; i++) {
                    if (i > 0) response.append(",");
                    int index = choiceIndices[i];
                    response.append("{\"question\": \"").append(escapeJson(choiceQuestions.get(index)))
                           .append("\", \"answer\": \"").append(escapeJson(choiceAnswers.get(index))).append("\"}");
                }
                response.append("],");

                // 添加判断题
                response.append("\"judgment\": [");
                for (int i = 0; i < judgmentQuestionsToShow && i < judgmentIndices.length; i++) {
                    if (i > 0) response.append(",");
                    int index = judgmentIndices[i];
                    response.append("{\"question\": \"").append(escapeJson(judgmentQuestions.get(index)))
                           .append("\", \"answer\": \"").append(escapeJson(judgmentAnswers.get(index))).append("\"}");
                }
                response.append("],");

                // 添加填空题
                response.append("\"fill\": [");
                for (int i = 0; i < fillQuestionsToShow && i < fillIndices.length; i++) {
                    if (i > 0) response.append(",");
                    int index = fillIndices[i];
                    response.append("{\"question\": \"").append(escapeJson(fillQuestions.get(index)))
                           .append("\", \"answer\": \"").append(escapeJson(fillAnswers.get(index))).append("\"}");
                }
                response.append("],");

                // 添加名词解释题
                response.append("\"explanation\": [");
                for (int i = 0; i < explanationQuestionsToShow && i < explanationIndices.length; i++) {
                    if (i > 0) response.append(",");
                    int index = explanationIndices[i];
                    response.append("{\"question\": \"").append(escapeJson(explanationQuestions.get(index)))
                           .append("\", \"answer\": \"").append(escapeJson(explanationAnswers.get(index))).append("\"}");
                }
                response.append("]");

                response.append("}");

                // 设置响应头
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.sendResponseHeaders(200, response.toString().getBytes("UTF-8").length);

                // 发送响应
                OutputStream os = exchange.getResponseBody();
                os.write(response.toString().getBytes("UTF-8"));
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
                String errorResponse = "{\"error\": \"" + e.getMessage() + "\"}";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(500, errorResponse.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(errorResponse.getBytes());
                os.close();
            }
        }

        private void readQuestionsAndAnswers(String filename, List<String> questions, List<String> answers) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
            String line;
            StringBuilder currentQuestion = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.contains("答案：")) {
                    // 如果包含"答案："，表示当前题目已经结束，开始处理答案
                    String answer = line.substring(line.indexOf("答案：") + 3).trim(); // 获取答案部分
                    answers.add(answer); // 存储答案

                    // 将当前题目内容添加到questions列表
                    questions.add(currentQuestion.toString().trim());

                    // 清空currentQuestion，准备接收下一个题目
                    currentQuestion.setLength(0);
                } else {
                    // 如果是题目行（没有"答案："的行），将该行添加到当前题目的内容中
                    currentQuestion.append(line.trim()).append("\n");
                }
            }
            reader.close();
        }

        private String escapeJson(String text) {
            return text.replace("\\", "\\\\")
                      .replace("\"", "\\\"")
                      .replace("\n", "\\n")
                      .replace("\r", "\\r")
                      .replace("\t", "\\t");
        }
        
        /**
         * 生成随机索引数组，类似于Examaple.java中的Arr3方法
         * @param size 数组大小
         * @return 随机排列的索引数组
         */
        private int[] generateRandomIndices(int size) {
            int[] indices = new int[size];
            int[] shuffledIndices = new int[size];
            
            // 初始化索引数组
            for (int i = 0; i < size; i++) {
                indices[i] = i;
            }
            
            // 使用Fisher-Yates洗牌算法随机排列索引
            Random rand = new Random();
            int count = size;
            int randCount = 0;
            int position = 0;
            int k = 0;
            
            do {
                int r = count - randCount;
                position = rand.nextInt(r);
                shuffledIndices[k++] = indices[position];
                randCount++;
                indices[position] = indices[r - 1];
                indices[r - 1] = shuffledIndices[k - 1];
            } while (randCount < count);
            
            return shuffledIndices;
        }
    }
}
