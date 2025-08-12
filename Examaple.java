import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class Examaple {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);		
		int achievement=0;//记录选择
		int achievement1=0;//记录填空
		int achievement2=0;//记录名词解释
		int achievement3=0;//记录判断题

//单选题
		System.out.println("单选题60道：");
		// 假设传入文件路径
		String filePath = "选择.txt";
		// 获取文件的所有行
		String[] allLines = Subject1(filePath);

		// 创建一个List用于存储所有的题目内容，另一个List用于存储答案
		List<String> questions = new ArrayList<>();
		List<String> answers = new ArrayList<>();
		// 用来存储当前题目的内容
		StringBuilder currentQuestion = new StringBuilder();
		// 遍历所有行，处理包含“答案：”的行
		for (String line : allLines) {
			//System.out.println(line);  // 输出原始的行内容（不分隔）
			if (line.contains("答案：")) {
				// 如果包含"答案："，表示当前题目已经结束，开始处理答案
				String answer = line.substring(line.indexOf("答案：") + 3).trim(); // 获取答案部分
				answers.add(answer); // 存储答案  // 将包含答案的行添加到answers列表

				// 将当前题目内容添加到questions列表
				questions.add(currentQuestion.toString().trim());

				// 清空currentQuestion，准备接收下一个题目
				currentQuestion.setLength(0);
			} else {
				// 如果是题目行（没有“答案：”的行），将该行添加到当前题目的内容中
				currentQuestion.append(line.trim()).append("\n");
			}
		}
		//输出单选题目
		int totalChoiceQuestions = questions.size(); // 获取实际题目数量
		int choiceQuestionsToShow = 60; // 要显示的题目数量
		int []sub=Arr3(totalChoiceQuestions); // 使用实际题目数量生成随机数组
		for(int i=0; i<choiceQuestionsToShow; i++) {
			System.out.println("第"+(i+1)+"题");
			System.out.println(questions.get(sub[i]));
			String x=input.next();//接收用户输入的答案
			if(x.equals(answers.get(sub[i]))) {
				System.out.println("答案正确！");
			}else {
				System.out.println("答案错误！");
				achievement++;
				System.out.println("正确答案为:"+answers.get(sub[i]));
			}	System.out.println();
		}

		System.out.println("错误了"+achievement+"道题,"+"得分"+60*(60-achievement)/60);
		System.out.println();


//判断题
		System.out.println("判断10道：（对T，错F）");
		// 假设传入文件路径
		String filePath3 = "判断.txt";
		// 获取文件的所有行
		String[] allLines3 = Subject1(filePath3);

		// 创建一个List用于存储所有的题目内容，另一个List用于存储答案
		List<String> questions3 = new ArrayList<>();
		List<String> answers3 = new ArrayList<>();
		// 用来存储当前题目的内容
		StringBuilder currentQuestion3 = new StringBuilder();
		// 遍历所有行，处理包含“答案：”的行
		for (String line : allLines3) {
//			System.out.println(line);  // 输出原始的行内容（不分隔）
			if (line.contains("答案：")) {
				// 如果包含"答案："，表示当前题目已经结束，开始处理答案
				String answer = line.substring(line.indexOf("答案：") + 3).trim(); // 获取答案部分
				answers3.add(answer); // 存储答案  // 将包含答案的行添加到answers列表

				// 将当前题目内容添加到questions列表
				questions3.add(currentQuestion3.toString().trim());

				// 清空currentQuestion，准备接收下一个题目
				currentQuestion3.setLength(0);
			} else {
				// 如果是题目行（没有“答案：”的行），将该行添加到当前题目的内容中
				currentQuestion3.append(line.trim()).append("\n");
			}
		}
		//输出判断
		int totalJudgmentQuestions = questions3.size(); // 获取实际题目数量
		int judgmentQuestionsToShow = 10; // 要显示的题目数量
		int []sub3=Arr3(totalJudgmentQuestions); // 使用实际题目数量生成随机数组
		for(int i=0; i<judgmentQuestionsToShow; i++) {
			System.out.println("第"+(i+1)+"题");
			System.out.println(questions3.get(sub3[i]));
			String x=input.next();//接收用户输入的答案
			if(x.equals(answers3.get(sub3[i]))) {
				System.out.println("答案正确！");
			}else {
				System.out.println("答案错误！");
				achievement3++;
				System.out.println("正确答案为:"+answers3.get(sub3[i]));
			}	System.out.println();
		}

		System.out.println("错误了"+achievement3+"道题,"+"得分"+10*(10-achievement3)/10);
		System.out.println();


//填空题
		System.out.println("填空5道：");
		// 假设传入文件路径
		String filePath1 = "填空.txt";
		// 获取文件的所有行
		String[] allLines1 = Subject1(filePath1);

		// 创建一个List用于存储所有的题目内容，另一个List用于存储答案
		List<String> questions1 = new ArrayList<>();
		List<String> answers1 = new ArrayList<>();
		// 用来存储当前题目的内容
		StringBuilder currentQuestion1 = new StringBuilder();
		// 遍历所有行，处理包含“答案：”的行
		for (String line : allLines1) {
//			System.out.println(line);  // 输出原始的行内容（不分隔）
			if (line.contains("答案：")) {
				// 如果包含"答案："，表示当前题目已经结束，开始处理答案
				String answer = line.substring(line.indexOf("答案：") + 3).trim(); // 获取答案部分
				answers1.add(answer); // 存储答案  // 将包含答案的行添加到answers列表

				// 将当前题目内容添加到questions列表
				questions1.add(currentQuestion1.toString().trim());

				// 清空currentQuestion，准备接收下一个题目
				currentQuestion1.setLength(0);
			} else {
				// 如果是题目行（没有“答案：”的行），将该行添加到当前题目的内容中
				currentQuestion1.append(line.trim()).append("\n");
			}
		}
		//输出填空
		int totalFillBlankQuestions = questions1.size(); // 获取实际题目数量
		int fillBlankQuestionsToShow = 5; // 要显示的题目数量，根据提示"填空5道："
		int []sub1=Arr3(totalFillBlankQuestions); // 使用实际题目数量生成随机数组
		for(int i=0; i<fillBlankQuestionsToShow; i++) {
			System.out.println("第"+(i+1)+"题");
			System.out.println(questions1.get(sub1[i]));
			String x=input.next();//接收用户输入的答案
			if(x.equals(answers1.get(sub1[i]))) {
				System.out.println("答案正确！");
			}else {
				System.out.println("答案错误！");
				achievement1++;
				System.out.println("正确答案为:"+answers1.get(sub1[i]));
			}	System.out.println();
		}

		System.out.println("错误了"+achievement1+"道题,"+"得分"+5*(5-achievement1)/5);
		System.out.println("总分="+(60*(60-achievement)/60+10*(10-achievement3)/10+5*(5-achievement1)/5));
		System.out.println();



////名词解释
//		System.out.println("名词解释20道：");
//		// 假设传入文件路径
//		String filePath2 = "名词解释.txt";
//		// 获取文件的所有行
//		String[] allLines2 = Subject1(filePath2);
//
//		// 创建一个List用于存储所有的题目内容，另一个List用于存储答案
//		List<String> questions2 = new ArrayList<>();
//		List<String> answers2 = new ArrayList<>();
//		// 用来存储当前题目的内容
//		StringBuilder currentQuestion2 = new StringBuilder();
//		// 遍历所有行，处理包含“答案：”的行
//		for (String line : allLines2) {
////			System.out.println(line);  // 输出原始的行内容（不分隔）
//			if (line.contains("答案：")) {
//				// 如果包含"答案："，表示当前题目已经结束，开始处理答案
//				String answer = line.substring(line.indexOf("答案：") + 3).trim(); // 获取答案部分
//				answers2.add(answer); // 存储答案  // 将包含答案的行添加到answers列表
//
//				// 将当前题目内容添加到questions列表
//				questions2.add(currentQuestion2.toString().trim());
//
//				// 清空currentQuestion，准备接收下一个题目
//				currentQuestion2.setLength(0);
//			} else {
//				// 如果是题目行（没有“答案：”的行），将该行添加到当前题目的内容中
//				currentQuestion2.append(line.trim()).append("\n");
//			}
//		}
//		//输出
//		int totalDefinitionQuestions = questions2.size(); // 获取实际题目数量
//		int definitionQuestionsToShow = 20; // 要显示的题目数量
//		int []sub2=Arr3(totalDefinitionQuestions); // 使用实际题目数量生成随机数组
//		for(int i=0; i<definitionQuestionsToShow; i++) {
//			System.out.println("第"+(i+1)+"题");
//			System.out.println(questions2.get(sub2[i]));
//			String x=input.next();//接收用户输入的答案
//			if(x.equals(answers2.get(sub2[i]))) {
//				System.out.println("答案正确！");
//			}else {
//				System.out.println("答案错误！");
//				achievement2++;
//				System.out.println("正确答案为:"+answers2.get(sub2[i]));
//			}	System.out.println();
//		}
//		System.out.println("错误了"+achievement2+"道题,"+"得分"+20*(20-achievement2)/20);
//		System.out.println();
//		System.out.println("总分="+(50*(50-achievement)/50+20*(20-achievement2)/20+20*(20-achievement1)/20));
//
//
}

	//获取题目
	public static String[] Subject(String a, int b) {
		String []str=new String[b]; 
		try {
		BufferedReader br = new BufferedReader(
			new InputStreamReader
			(new FileInputStream(a),"UTF-8"));//读取a文件并将字节流转化成字符流
		int k =0;
		while ((str[k] = br.readLine()) != null) {//按行读取br将其存储到str[]数组中，k为下标
			k++;
			}
		}catch(Exception e) {
			System.out.println("捕获到异常："+e);
		}
		return str;
	}

	public static String[] Subject1(String a) {
		List<String> lines = new ArrayList<>();//动态列表
		String line;
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader
							(new FileInputStream(a),"UTF-8"));//读取a文件并将字节流转化成字符流
			int k =0;
			while ((line = br.readLine()) != null) {//按行读取br将其存储到str[]数组中，k为下标
				lines.add(line);
			}
		}catch(Exception e) {
			System.out.println("捕获到异常："+e);
		}
		String[] arr = lines.toArray(new String[0]);
		return arr;
	}

	//单选题与多选题随机下标，洗牌算法
	public static int[] Arr(int a) {
		int [] dan =new int[a];
		int [] dan2=new int[dan.length];
		int j=0;
		for (int i=0;i<a*5;i+=5) {
			dan[j++]=i;
			}
		int count = dan.length;  
		int cbRandCount = 0;// 索引  
		int cbPosition = 0;// 位置  
		int k =0;  
		do {  
			Random rand = new Random();  
			int r = count - cbRandCount; //判断还剩余几个位置，位置的填充顺序是从后往前
			cbPosition = rand.nextInt(r);//从没有出现的位置中随机选择一个
			dan2[k++] = dan[cbPosition]; //将随机选择出的引索的值付给dan2数组，
			cbRandCount++;  
			dan[cbPosition] = dan[r - 1];// 将最后一位数值赋值给随机选出的下标，选择最后一位的原因是，下次随机不会随机到最后一个下标
			dan[r - 1]=dan2[k-1];	//将原来存在于随机位置的，暂存于dan2中的值赋值给dan的最后一个位置
		} while (cbRandCount < count) ;
		return dan;
	}
	//判断题下标
		public static int[] Arr1(int a) {
			int [] dan =new int[a];
			int [] dan2=new int[dan.length];
			for (int i =0;i<a;i++) {
				dan[i]=i;}
			int count = dan.length;  
			int cbRandCount = 0;// 索引  
			int cbPosition = 0;// 位置  
			int k =0;  
			do {  
				Random rand = new Random();  
				int r = count - cbRandCount;  
				cbPosition = rand.nextInt(r);   
				dan2[k++] = dan[cbPosition];  
				cbRandCount++;  
				dan[cbPosition] = dan[r - 1];// 将最后一位数值赋值给已经被使用的cbPosition  
				dan[r-1]=dan2[k-1];
			} while (cbRandCount < count) ;
			return dan;
		}

//填空下标
		public static int[] Arr2(int a) {
			int [] dan =new int[a];
			int [] dan2=new int[dan.length];
			int j=0;
			for (int i =0;i<a*2;i+=2) {
				dan[j++]=i;}
			int count = dan.length;  
			int cbRandCount = 0;// 索引  
			int cbPosition = 0;// 位置  
			int k =0;  
			do {  
				Random rand = new Random();  
				int r = count - cbRandCount;  
				cbPosition = rand.nextInt(r);   
				dan2[k++] = dan[cbPosition];  
				cbRandCount++;  
				dan[cbPosition] = dan[r - 1];// 将最后一位数值赋值给已经被使用的cbPosition  
				dan[r-1]=dan2[k-1];
			} while (cbRandCount < count) ;
			return dan;
		}

	public static int[] Arr3(int a) {
		int [] dan =new int[a];
		int [] dan2=new int[dan.length];
		int j=0;
		for (int i=0;i<a;i+=1) {
			dan[j++]=i;
		}
		int count = dan.length;
		int cbRandCount = 0;// 索引
		int cbPosition = 0;// 位置
		int k =0;
		do {
			Random rand = new Random();
			int r = count - cbRandCount; //判断还剩余几个位置，位置的填充顺序是从后往前
			cbPosition = rand.nextInt(r);//从没有出现的位置中随机选择一个
			dan2[k++] = dan[cbPosition]; //将随机选择出的引索的值付给dan2数组，
			cbRandCount++;
			dan[cbPosition] = dan[r - 1];// 将最后一位数值赋值给随机选出的下标，选择最后一位的原因是，下次随机不会随机到最后一个下标
			dan[r - 1]=dan2[k-1];	//将原来存在于随机位置的，暂存于dan2中的值赋值给dan的最后一个位置
		} while (cbRandCount < count) ;
		return dan;
	}
}
