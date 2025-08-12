// 题目数据
let questionData = {
    choice: [],
    judgment: [],
    fillBlank: [],
    definition: []
};

// 当前状态
let currentState = {
    type: 'choice', // 当前题型：choice, judgment, fillBlank, definition
    index: 0,       // 当前题目索引
    scores: {       // 得分情况
        choice: 0,
        judgment: 0,
        fillBlank: 0,
        definition: 0
    },
    answered: false, // 是否已回答当前题目
    shuffledQuestions: {
        choice: [],
        judgment: [],
        fillBlank: [],
        definition: []
    },
    loading: true,   // 是否正在加载数据
    debug: true      // 是否启用调试模式
};

// 调试日志函数
function debugLog(...args) {
    if (currentState.debug) {
        console.log('[DEBUG]', ...args);
    }
}

// 从后端API获取题目数据
async function fetchQuestionData() {
    try {
        // 显示加载状态
        document.getElementById('loadingIndicator').style.display = 'block';
        currentState.loading = true;

        // 从后端API获取题目数据
        const response = await fetch('http://localhost:8000/api/questions');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();

        // 处理选择题数据 - 增强版解析逻辑
        questionData.choice = data.choice.map((item, qIndex) => {
            debugLog(`处理选择题 #${qIndex + 1}:`, item.question);
            
            // 分割题目和选项
            const lines = item.question.split('\n').filter(line => line.trim().length > 0);
            const questionText = lines[0];
            debugLog(`题目文本: ${questionText}`);
            
            // 提取选项内容
            const options = ['', '', '', '']; // 初始化4个空选项
            let optionsFound = false;
            
            // 处理选项行
            for (let i = 1; i < lines.length; i++) {
                const line = lines[i].trim();
                debugLog(`处理行 ${i}: "${line}"`);
                
                // 检查是否是一行包含多个选项的情况
                const containsMultipleOptions = 
                    (line.match(/A[\.、\s]/) && line.match(/B[\.、\s]/)) || 
                    (line.includes('A') && line.includes('B') && (line.includes('C') || line.includes('D')));
                
                if (containsMultipleOptions) {
                    debugLog(`检测到多选项行: "${line}"`);
                    optionsFound = true;
                    
                    // 使用更强大的正则表达式分割选项
                    // 查找以A-D开头，后面可能跟着点、顿号或空格的模式
                    const optionParts = line.split(/(?=[A-D][\.、\s])/).filter(part => part.trim());
                    debugLog(`分割后的选项部分:`, optionParts);
                    
                    optionParts.forEach(part => {
                        // 匹配选项字母和内容
                        const match = part.match(/^([A-D])[\.、\s]?(.*)/);
                        if (match) {
                            const optionLetter = match[1];
                            const optionContent = match[2].trim();
                            const index = optionLetter.charCodeAt(0) - 'A'.charCodeAt(0);
                            
                            debugLog(`选项 ${optionLetter}: "${optionContent}"`);
                            
                            if (index >= 0 && index < 4) {
                                options[index] = optionContent;
                            }
                        } else {
                            debugLog(`无法解析选项部分: "${part}"`);
                        }
                    });
                } else {
                    // 单个选项的情况
                    // 检查是否有明确的ABCD前缀（支持A.或A、格式）
                    const match = line.match(/^([A-D])[\.、\s]?(.*)/);
                    if (match) {
                        optionsFound = true;
                        const optionLetter = match[1];
                        const optionContent = match[2].trim();
                        const index = optionLetter.charCodeAt(0) - 'A'.charCodeAt(0);
                        
                        debugLog(`单行选项 ${optionLetter}: "${optionContent}"`);
                        
                        if (index >= 0 && index < 4) {
                            options[index] = optionContent;
                        }
                    } else if (i <= 4 && !optionsFound) { 
                        // 如果没有前缀但位置是选项位置，且之前没有找到带前缀的选项
                        debugLog(`无前缀选项 ${i-1}: "${line}"`);
                        options[i-1] = line;
                    }
                }
            }

            // 检查选项是否为空，如果是，尝试其他解析方法
            if (options.every(opt => !opt) && lines.length > 1) {
                debugLog(`所有选项为空，尝试备用解析方法`);
                
                // 备用方法：假设题目后的所有行都是选项
                for (let i = 1; i < Math.min(lines.length, 5); i++) {
                    options[i-1] = lines[i].trim();
                    debugLog(`备用选项 ${i-1}: "${options[i-1]}"`);
                }
            }

            debugLog(`最终选项:`, options);
            
            return {
                question: questionText,
                options: options,
                answer: item.answer
            };
        });

        // 处理判断题数据
        questionData.judgment = data.judgment.map(item => ({
            question: item.question,
            answer: item.answer
        }));

        // 处理填空题数据
        questionData.fillBlank = data.fill.map(item => ({
            question: item.question,
            answer: item.answer
        }));

        // 处理名词解释题数据
        questionData.definition = data.explanation.map(item => ({
            question: item.question,
            answer: item.answer
        }));

        // 隐藏加载状态
        document.getElementById('loadingIndicator').style.display = 'none';
        currentState.loading = false;

        // 初始化题目
        shuffleQuestions();
        showQuestion();
        updateScoreDisplay();

        console.log('题目数据加载成功:', questionData);
    } catch (error) {
        console.error('获取题目数据失败:', error);
        document.getElementById('loadingIndicator').style.display = 'none';
        document.getElementById('errorMessage').textContent = '加载题目数据失败，请刷新页面重试';
        document.getElementById('errorMessage').style.display = 'block';
        currentState.loading = false;
    }
}

// 初始化函数
function init() {
    // 绑定事件
    document.getElementById('choiceBtn').addEventListener('click', () => switchQuestionType('choice'));
    document.getElementById('judgmentBtn').addEventListener('click', () => switchQuestionType('judgment'));
    document.getElementById('fillBlankBtn').addEventListener('click', () => switchQuestionType('fillBlank'));
    document.getElementById('definitionBtn').addEventListener('click', () => switchQuestionType('definition'));

    document.getElementById('submitBtn').addEventListener('click', submitAnswer);
    document.getElementById('nextBtn').addEventListener('click', nextQuestion);

    // 从后端获取题目数据
    fetchQuestionData();
}

// 打乱题目顺序
function shuffleQuestions() {
    for (const type in questionData) {
        currentState.shuffledQuestions[type] = [...questionData[type]];
        for (let i = currentState.shuffledQuestions[type].length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [currentState.shuffledQuestions[type][i], currentState.shuffledQuestions[type][j]] =
            [currentState.shuffledQuestions[type][j], currentState.shuffledQuestions[type][i]];
        }
    }
}

// 切换题型
function switchQuestionType(type) {
    // 如果是同一题型，不做任何操作
    if (currentState.type === type) return;

    // 更新当前题型
    currentState.type = type;
    currentState.index = 0;
    currentState.answered = false;

    // 更新UI
    document.querySelectorAll('.exam-type button').forEach(btn => btn.classList.remove('active'));
    document.getElementById(`${type}Btn`).classList.add('active');

    // 隐藏所有答题区域
    document.getElementById('choiceOptions').style.display = 'none';
    document.getElementById('judgmentOptions').style.display = 'none';
    document.getElementById('textInputContainer').style.display = 'none';

    // 显示当前题型的答题区域
    if (type === 'choice') {
        document.getElementById('choiceOptions').style.display = 'block';
    } else if (type === 'judgment') {
        document.getElementById('judgmentOptions').style.display = 'block';
    } else {
        document.getElementById('textInputContainer').style.display = 'block';
    }

    // 重置反馈
    document.getElementById('feedback').className = 'feedback';
    document.getElementById('feedback').style.display = 'none';
    document.getElementById('resultMessage').textContent = '';
    document.getElementById('correctAnswer').textContent = '';

    // 显示新题目
    showQuestion();
}

// 显示当前题目
function showQuestion() {
    const { type, index } = currentState;
    const questions = currentState.shuffledQuestions[type];

    if (questions.length === 0) {
        document.getElementById('questionContent').textContent = '该题型暂无题目';
        return;
    }

    if (index >= questions.length) {
        document.getElementById('questionContent').textContent = '该题型的题目已答完';
        return;
    }

    const question = questions[index];
    document.getElementById('questionNumber').textContent = `第${index + 1}题`;
    document.getElementById('questionContent').textContent = question.question;

    // 如果是选择题，更新选项内容
    if (type === 'choice' && question.options) {
        const optionLabels = document.querySelectorAll('#choiceOptions .option label');
        question.options.forEach((option, i) => {
            if (i < optionLabels.length) {
                optionLabels[i].textContent = `${String.fromCharCode(65 + i)}. ${option}`;
            }
        });
    }

    // 重置选项和输入
    document.querySelectorAll('input[type="radio"]').forEach(radio => radio.checked = false);
    document.getElementById('textAnswer').value = '';

    // 重置反馈
    document.getElementById('feedback').className = 'feedback';
    document.getElementById('feedback').style.display = 'none';
    document.getElementById('resultMessage').textContent = '';
    document.getElementById('correctAnswer').textContent = '';

    // 重置回答状态
    currentState.answered = false;
}

// 提交答案
function submitAnswer() {
    if (currentState.answered) {
        alert('您已回答过此题，请点击"下一题"继续');
        return;
    }

    const { type, index } = currentState;
    const questions = currentState.shuffledQuestions[type];

    if (index >= questions.length) {
        alert('该题型的题目已答完');
        return;
    }

    const question = questions[index];
    let userAnswer = '';
    let isCorrect = false;

    // 获取用户答案
    if (type === 'choice') {
        const selectedOption = document.querySelector('input[name="choice"]:checked');
        if (!selectedOption) {
            alert('请选择一个选项');
            return;
        }
        userAnswer = selectedOption.value;
    } else if (type === 'judgment') {
        const selectedOption = document.querySelector('input[name="judgment"]:checked');
        if (!selectedOption) {
            alert('请选择正确或错误');
            return;
        }
        userAnswer = selectedOption.value;
    } else {
        userAnswer = document.getElementById('textAnswer').value.trim();
        if (!userAnswer) {
            alert('请输入您的答案');
            return;
        }
    }

    // 判断答案是否正确
    if (type === 'choice' || type === 'judgment') {
        isCorrect = userAnswer === question.answer;
    } else {
        // 对于填空题和名词解释，我们需要更灵活的匹配
        // 这里简单实现为包含关键词即可
        isCorrect = question.answer.includes(userAnswer) || userAnswer.includes(question.answer);
    }

    // 更新得分
    if (isCorrect) {
        if (type === 'choice') currentState.scores.choice++;
        else if (type === 'judgment') currentState.scores.judgment++;
        else if (type === 'fillBlank') currentState.scores.fillBlank++;
        else if (type === 'definition') currentState.scores.definition++;
    }

    // 显示反馈
    const feedback = document.getElementById('feedback');
    feedback.className = isCorrect ? 'feedback correct' : 'feedback incorrect';
    feedback.style.display = 'block';
    document.getElementById('resultMessage').textContent = isCorrect ? '回答正确！' : '回答错误！';
    document.getElementById('correctAnswer').textContent = `正确答案: ${question.answer}`;

    // 更新得分显示
    updateScoreDisplay();

    // 标记为已回答
    currentState.answered = true;
}

// 下一题
function nextQuestion() {
    const { type, index } = currentState;
    const questions = currentState.shuffledQuestions[type];

    if (index + 1 >= questions.length) {
        alert('该题型的题目已答完');
        return;
    }

    currentState.index++;
    currentState.answered = false;

    showQuestion();
}

// 更新得分显示
function updateScoreDisplay() {
    const { scores } = currentState;
    const totalScore = scores.choice + scores.judgment + scores.fillBlank + scores.definition;

    document.getElementById('choiceScore').textContent = `${scores.choice}/${questionData.choice.length}`;
    document.getElementById('judgmentScore').textContent = `${scores.judgment}/${questionData.judgment.length}`;
    document.getElementById('fillBlankScore').textContent = `${scores.fillBlank}/${questionData.fillBlank.length}`;
    document.getElementById('definitionScore').textContent = `${scores.definition}/${questionData.definition.length}`;
    document.getElementById('totalScore').textContent = totalScore;
}

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', init);