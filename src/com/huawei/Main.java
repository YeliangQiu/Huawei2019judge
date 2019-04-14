package com.huawei;


import com.huawei.object.Car;
import com.huawei.object.Cross;
import com.huawei.object.Road;
import com.huawei.simulator.Simulator;
import com.huawei.utl.FormatUtils;
import com.huawei.utl.MapUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * @author : qiuyeliang
 * create at:  2019/3/9  12:41
 * @description:
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);
    public static void main(String[] args)
    {
//        if (args.length != 4) {
//            logger.error("please input args: inputFilePath, resultFilePath");
//            return;
//        }

        logger.info("Start...");
        long startTime = System.currentTimeMillis();

//

//        String carPath = args[0];
//        String roadPath = args[1];
//        String crossPath = args[2];
//        String presetAnswerPath = args[3];
//        String answerPath = args[4];

//        String carPath = "J:/huawei/2-training-training-2-answer/car.txt";
//        String roadPath = "J:/huawei/2-training-training-2-answer/road.txt";
//        String crossPath = "J:/huawei/2-training-training-2-answer/cross.txt";
//        String presetAnswerPath = "J:/huawei/2-training-training-2-answer/presetAnswer.txt";
//        String answerPath = "J:/huawei/2-training-training-2-answer/answer.txt";
        
//        String carPath = "J:/huawei/2-map-training-2/car.txt";
//        String roadPath = "J:/huawei/2-map-training-2/road.txt";
//        String crossPath = "J:/huawei/2-map-training-2/cross.txt";
//        String presetAnswerPath = "J:/huawei/2-map-training-2/presetAnswer.txt";
//        String answerPath = "J:/huawei/2-map-training-2/answer.txt";

        String carPath = "/Users/qiuyeliang/Desktop/map/2-map-exam-2/car.txt";
        String roadPath = "/Users/qiuyeliang/Desktop/map/2-map-exam-2/road.txt";
        String crossPath = "/Users/qiuyeliang/Desktop/map/2-map-exam-2/cross.txt";
        String presetAnswerPath = "/Users/qiuyeliang/Desktop/map/2-map-exam-2/presetAnswer.txt";
        String answerPath = "/Users/qiuyeliang/Desktop/map/2-map-exam-2/answer.txt";
//
//        String carPath = "/Users/qiuyeliang/Desktop/map/2-training-training-2-answer/car.txt";
//        String roadPath = "/Users/qiuyeliang/Desktop/map/2-training-training-2-answer/road.txt";
//        String crossPath = "/Users/qiuyeliang/Desktop/map/2-training-training-2-answer/cross.txt";
//        String presetAnswerPath = "/Users/qiuyeliang/Desktop/map/2-training-training-2-answer/presetAnswer.txt";
//        String answerPath = "/Users/qiuyeliang/Desktop/map/2-training-training-2-answer/answer.txt";

        logger.info("carPath = " + carPath + " roadPath = " + roadPath + " crossPath = " + crossPath + " and answerPath = " + answerPath);

        // TODO:read input files
      //  logger.info("start read input files");
        mymain.run(carPath,roadPath,crossPath,presetAnswerPath,answerPath);
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
        // TODO: calc

        // TODO: write answer.txt
      //  logger.info("Start write output file");

       // logger.info("End...");
    }
}
