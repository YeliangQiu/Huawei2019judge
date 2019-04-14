package com.huawei;

import com.huawei.Factory.DataFactory;
import com.huawei.controller.CRController2;
import com.huawei.object.Car;
import com.huawei.object.Cross;
import com.huawei.object.Road;
import com.huawei.simulator.Simulator;
import com.huawei.utl.FormatUtils;
import com.huawei.utl.MapUtils;

import java.util.Map;

/**
 * @author : qiuyeliang
 * create at:  2019/3/16  13:36
 * @description:
 */
public class mymain {
    public static void initFactory(Map<Integer, Car> carMap, Map<Integer, Road> roadMap, Map<Integer, Cross> crossMap,int[][] Matrix) {
        DataFactory.loadData(carMap, roadMap, crossMap,Matrix);
    }
    public static void run(String carPath,String roadPath,String crossPath,String presetAnswerPath,String answerPath){

        //logger.info("carPath = " + carPath + " roadPath = " + roadPath + " crossPath = " + crossPath + " and answerPath = " + answerPath);
        Map<Integer, Car> carMap = FormatUtils.createCar(FormatUtils.load(carPath));
        Map<Integer, Road> roadMap = FormatUtils.createRoad(FormatUtils.load(roadPath));
        Map<Integer, Cross> crossMap = FormatUtils.createCross(FormatUtils.load(crossPath),roadMap);
        FormatUtils.createPreSetWay(FormatUtils.load(presetAnswerPath),carMap);
        FormatUtils.createPlanWay(carMap,FormatUtils.load(answerPath));
        Simulator simulator = new Simulator(carMap,roadMap,crossMap, MapUtils.createCrossMap(crossMap,roadMap));
        Car car = carMap.get(51404);
//        simulator.TheFirstStage();
       //simulator.carSpeedClassification();
        //Map<Integer,Car> cars = simulator.TheFirstStage(); //初始化路径，无权重
//        simulator.carSpeedClassification(0.035,0.014); //0.035,0.015
        //simulator.oneByOne(0.017);
        //simulator.randStartTime(10);
        //FormatUtils.writeAnswer(answerPath,cars);
        //初始化工厂-杜
        initFactory(carMap, roadMap, crossMap,simulator.getCrossMatrix());
//        if(simulator.getCrossMatrix().length < 150) {
//            CRController2.getInstance().maxCarNum = 4000;
//            CRController2.getInstance().weight = 0.8;
//        }
//        else {
//            CRController2.getInstance().maxCarNum = 4000;
//            CRController2.getInstance().weight = 0.8;
//        }
//        MapUtils.init(simulator.getCrossMatrix().length);
        simulator.update();
//        FormatUtils.writeAnswer(answerPath,carMap);
    }
}
