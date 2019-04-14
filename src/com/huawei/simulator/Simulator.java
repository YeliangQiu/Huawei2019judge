package com.huawei.simulator;

import com.huawei.controller.CRController2;
import com.huawei.object.Car;
import com.huawei.object.CarStatus;
import com.huawei.object.Cross;
import com.huawei.object.Road;
import com.huawei.utl.FormatUtils;
import com.huawei.utl.MapUtils;

import java.util.*;

/**
 * @author : qiuyeliang
 * create at:  2019/3/9  18:04
 * @description: 进行模拟
 */
public class Simulator {
    private PriorityQueue<Car> readyQueue; //等待上路的队列
    private Map<Integer,Car> cars;
    private Map<Integer,Road> roads;
    private Map<Integer,Cross> crosses;
    private int[][] crossMatrix;
    public static double a = Integer.MAX_VALUE;

    public Simulator(Map<Integer, Car> cars, Map<Integer, Road> roads, Map<Integer, Cross> crosses, int[][] crossMatrix) {
        this.cars = cars;
        this.roads = roads;
        this.crosses = crosses;
        this.crossMatrix = crossMatrix;
    }

//    public Map<Integer,Car> TheFirstStage(){
//        for(int i=1;i<crossMatrix.length;i++){
//            for(int j=1;j<crossMatrix[i].length;j++) {
//                System.out.print(crossMatrix[i][j]+",");
//            }
//            System.out.println();
//        }
//        for(Map.Entry<Integer,Car> map:cars.entrySet()){
//            double[][] weightMatrix = MapUtils.createWeightMatrix(crossMatrix,crosses,roads,map.getValue());
//            System.out.println("此车的id是:"+map.getValue().getId()+"出发时间是"+map.getValue().getPlantime()+"此车的出发地是:"+map.getValue().getFrom()+"此车的目的地是:"+map.getValue().getTo()+"速度是"+map.getValue().getSpeed());
//            MapUtils.SPFA(crossMatrix,weightMatrix,weightMatrix,map.getValue(),map.getValue().getFrom());
//        }
//        return cars;
//        //MapUtils.SPFA(weightMap,start,end);
//    }

    public int[][] getCrossMatrix() {
        return crossMatrix;
    }

    //按id降序排列
    public static Comparator<Car> readyComparator = new Comparator<Car>() {
        @Override
        public int compare(Car c1, Car c2) {
            if(c1.getPlantime() > c2.getPlantime())
                return 1;
            else if(c1.getPlantime() < c2.getPlantime())
                return -1;
            if(c1.getId() > c2.getId())
                return 1;
            else
                return -1;
        }
    };
    public static Comparator<Car> carSpeedComparator = new Comparator<Car>() {
        @Override
        public int compare(Car c1, Car c2) {
            if(c1.getSpeed() > c2.getSpeed())
                return -1;
            else if(c1.getSpeed() < c2.getSpeed())
                return 1;
            if(c1.getId() > c2.getId())
                return 1;
            else
                return -1;
        }
    };

    //一辆车一辆车走
//    public void oneByOne(double rate){
//        readyQueue = new PriorityQueue<>(readyComparator);
//        for(Map.Entry<Integer,Car> map:cars.entrySet()){
//            readyQueue.add(map.getValue());
//        }
//        int plusTime = readyQueue.peek().getCompleteTime() + readyQueue.peek().getPlantime();
//        readyQueue.poll();
//        while(!readyQueue.isEmpty()){
//            Car car = readyQueue.poll();
//            //System.out.println("车的ID是"+car.getId()+"出发时间是"+car.getPlantime()+"最短路径时间是"+car.getCompleteTime());
//            //System.out.println(plusTime);
//            car.setPlantime(plusTime);
//            plusTime = (int)(rate * car.getCompleteTime()) + car.getPlantime();
//        }
//    }

//    public void carSpeedClassification(double rate1,double rate2){
//        readyQueue = new PriorityQueue<>(readyComparator);
//        PriorityQueue<Car> fastQueue = new PriorityQueue<>(readyComparator);
//        PriorityQueue<Car> slowQueue = new PriorityQueue<>(readyComparator);
//        for(Map.Entry<Integer,Car> map: cars.entrySet())
//            readyQueue.add(map.getValue()); //按照出发时间和id排序
//        while(!readyQueue.isEmpty()){
//            Car car = readyQueue.poll();
//            Car nextCar = readyQueue.peek();
//            int startRoadId = car.getPlanWayList().get(0);
//            int roadSpeed  = roads.get(startRoadId).getSpeed();
//            int carSpeed = car.getSpeed();
//            if(nextCar == null){
//                if(carSpeed > roadSpeed)
//                    fastQueue.add(car);
//                else
//                    slowQueue.add(car);
//                break;
//            }
//            System.out.println("newxtCar="+nextCar.getPlantime());
//            System.out.println("Car="+car.getPlantime());
//            if(carSpeed < roadSpeed && nextCar.getPlantime() - car.getPlantime() < 3)
//                slowQueue.add(car);
//            else
//                fastQueue.add(car);
//        }
//        int plusTime = fastQueue.peek().getCompleteTime() + fastQueue.peek().getPlantime();
//        fastQueue.poll();
//        while(!fastQueue.isEmpty()){
//            Car car = fastQueue.poll();
//            car.setPlantime(plusTime);
//            plusTime = (int)(rate1 * car.getCompleteTime() + car.getPlantime());
//        }
//        while (!slowQueue.isEmpty()){
//            Car car = slowQueue.poll();
//            car.setPlantime(plusTime);
//            plusTime = (int)(rate2 * car.getCompleteTime() + car.getPlantime());
//        }
//    }

    public  List<Car> mapToList(){
        List<Car> list = new ArrayList<>();
        for(Map.Entry<Integer,Car> map:cars.entrySet()){
            list.add(map.getValue());
        }
        return list;
    }

    public void update() {
        readyQueue = new PriorityQueue<>(readyComparator); //先拿优先队列排序，出发时间早的在前面，相同出发时间的要看id大小
        for(Map.Entry<Integer,Car> map:cars.entrySet()){
            readyQueue.add(map.getValue());
        }
       //carSpeedClassification();
        Map<Integer,List<Car>> SerializationCar = new HashMap<>(); //将car按出发时间序列化
        while(!readyQueue.isEmpty()){
            Car car = readyQueue.poll();
            if(SerializationCar.containsKey(car.getPlantime())){
                List<Car> list = SerializationCar.get(car.getPlantime());
                list.add(car);
            }else{
                List<Car> carList = new ArrayList<>();
                carList.add(car);
                SerializationCar.put(car.getPlantime(),carList);
            }
        }
        CRController2 controller = CRController2.getInstance();
        controller.init(SerializationCar.get(1)); //出发时间是1的车
        SerializationCar.remove(1);
        // System.out.println("Size="+SerializationCar.get(1).size());
        int count = 1;
        int maxMapCarSize = 0;
        int tim = 0;
        int CRCcarnum = controller.maxCarNum;
        while(!controller.finished()) {
            if(maxMapCarSize < controller.getMapCarSize())
                maxMapCarSize = controller.getMapCarSize();
//            long now = System.currentTimeMillis();
            try {
                controller.update();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("count=" + count + "Mapsize" + controller.getMapCarSize());
                break;
            }
//            System.out.println(System.currentTimeMillis() - now);
            List<Car> joinCars =SerializationCar.get(controller.getAllTime());
            SerializationCar.remove(controller.getAllTime());
//            List<Car> joinCars;
//            if(controller.getMapCarSize() <= CRCcarnum) { //如果本时刻路上的车小于*;
////                Map<Integer,List<Car>> tmpMap = new HashMap<>();
////                for(Map.Entry<Integer,List<Car>> map:SerializationCar.entrySet()){ //还未上路的所有车的出发时间都要+1
////                    List <Car> carList = map.getValue();
////                    for(Car car:carList){
////                        car.setPlantime(car.getPlantime()+tim);
////                    }
////                    tmpMap.put(map.getKey()+tim,carList);
////                }
////                SerializationCar = tmpMap;
//                joinCars = SerializationCar.get(controller.getAllTime()); //这一时刻的值
//                SerializationCar.remove(controller.getAllTime()); // 移出
//                tim = 0;
//            }else{
//                tim++;
//                Map<Integer,List<Car>> tmpMap = new HashMap<>();
//                for(Map.Entry<Integer,List<Car>> map:SerializationCar.entrySet()){ //还未上路的所有车的出发时间都要+1
//                    List <Car> carList = map.getValue();
//                    for(Car car:carList){
//                        car.setPlantime(car.getPlantime()+1);
//                    }
//                    tmpMap.put(map.getKey()+1,carList);
//                }
//                SerializationCar = tmpMap;
//                continue;
//            }
            if(joinCars != null && joinCars.size() != 0) {
                controller.addMapListCar(joinCars);
                count+=joinCars.size();
            }
//    		controller.addMapCar(null); //加入要跑图的车子序列
        }
        System.out.println("maxMapCarSize=" + maxMapCarSize);
        System.out.println("maxWeight"+controller.maxWeight);
        System.out.println("总调度时间"+controller.allScheduleTime);
        System.out.println("time" + controller.getAllTime());
        System.out.println("优先级车辆的最后到达时间" + controller.prorityTim);
        System.out.println("参数a="+a);
        System.out.println("系统调度时间" + (controller.getAllTime()+controller.prorityTim * a) );
        int stime =0;
        int priTime = 0;
        for(Map.Entry<Integer,Car> map:cars.entrySet()) {
            stime += map.getValue().getRealArriveTime();
            if(map.getValue().getPriority() == 1)
                priTime += map.getValue().getRealArriveTime();
        }
        System.out.println("test=" + stime);
        System.out.println("testpri=" + priTime);

    }
//
    public void randStartTime(int repeat){
        Map<Integer,Integer> startTimeCount = new HashMap<>();
        Set<Integer> repeatSet = new HashSet<>();
        for(Map.Entry<Integer,Car> map:cars.entrySet()){
            Car car = map.getValue();
            if(startTimeCount.containsKey(car.getPlantime()))
                startTimeCount.put(car.getPlantime(),startTimeCount.get(car.getPlantime())+1);
            else
                startTimeCount.put(car.getPlantime(),1);
        }
        for(Map.Entry<Integer,Integer> map:startTimeCount.entrySet()){
            if(map.getValue() >= repeat)
                repeatSet.add(map.getKey());
        }
        for(Map.Entry<Integer,Car> map:cars.entrySet()){
            Car car = map.getValue();
            if(repeatSet.contains(car.getPlantime())) {
                car.setPlantime(car.getPlantime() + (int) (Math.random() * (50 - 1 + 1)));
            }
        }
    }
//    public void carSpeedClassification(){
//        readyQueue = new PriorityQueue<>(readyComparator);
//        PriorityQueue<Car> fastQueue = new PriorityQueue<>(readyComparator);
//        PriorityQueue<Car> slowQueue = new PriorityQueue<>(readyComparator);
//        for(Map.Entry<Integer,Car> map: cars.entrySet())
//            readyQueue.add(map.getValue()); //按照出发时间和id排序
//        while(!readyQueue.isEmpty()){ //分割快慢队列
//            Car car = readyQueue.poll();
//            int startRoadId = car.getPlanWayList().get(0);
//            int roadSpeed  = roads.get(startRoadId).getSpeed();
//            int carSpeed = car.getSpeed();
////            System.out.println("newxtCar="+nextCar.getPlantime());
////            System.out.println("Car="+car.getPlantime());
//            if(carSpeed < roadSpeed)
//                slowQueue.add(car);
//            else
//                fastQueue.add(car);
//        }
//        for(int i=0;i<fastQueue.size()-1;i++) { //得到快速队列最后一辆车的出发时间
//            fastQueue.poll();
//        }
//        int fastQueueLastcarTime = 0;
//        if(fastQueue.size() != 0) { //高速队列有可能为空
//            fastQueueLastcarTime = fastQueue.poll().getPlantime();
//        }
//        while (!slowQueue.isEmpty()){ //所有慢车都延迟出发这个时间
//            Car car = slowQueue.poll();
//            car.setPlantime(car.getPlantime()+fastQueueLastcarTime);
//        }
//    }
    public void carSpeedClassification(){
        readyQueue = new PriorityQueue<>(readyComparator);
        Map<Integer,PriorityQueue<Car>> carMap = new HashMap<>();
        Queue<Car> carSpeedQueue = new PriorityQueue<>(carSpeedComparator);//先按速度排一遍
        int maxPlantime = 0;
        for(Map.Entry<Integer,Car> map:cars.entrySet()){
            if(maxPlantime < map.getValue().getPlantime())
                maxPlantime = map.getValue().getPlantime();
            carSpeedQueue.add(map.getValue());
        }
        for(int i=1;i<=maxPlantime+1;i++) //初始化carMap
            carMap.put(i,new PriorityQueue<Car>(carSpeedComparator));
        int carNum = 0,timeSlice = 1,unitTimePieceCarNum = (int)(cars.size() / maxPlantime);//车的数量，时间片，单位时间片的车数
        while (!carSpeedQueue.isEmpty()){
            Car car = carSpeedQueue.poll();
            if(car.getPlantime() > timeSlice){//如果这个车的出发时间大于当前时间片
                carMap.get(maxPlantime + 1).add(car); //去最后一个优先队列等待排序
            }else if(car.getPlantime() < timeSlice) {//如果小于当前时间片，则延迟出发时间
                car.setPlantime(timeSlice);
                carMap.get(timeSlice).add(car);//进入当前时间片的队列
            }else
                carMap.get(timeSlice).add(car);//进入当前时间片队列
            carNum ++; //遍历到第几台车
            if(carNum > unitTimePieceCarNum){ //大于当前时间片了
                timeSlice ++; //时间片+1
            unitTimePieceCarNum *= timeSlice; //再加上一个当前时间片的值
        }
    }
        PriorityQueue<Car> specialQueue = carMap.get(maxPlantime + 1);
        carMap.remove(maxPlantime + 1);
        for(Map.Entry<Integer,PriorityQueue<Car>> carmap:carMap.entrySet()){
            Queue<Car> carQueue = carmap.getValue();
            if(carQueue.size() == 0)
                continue;
        int thisTimeSliceCarMaxSpeed = carQueue.peek().getSpeed();
        while(!carQueue.isEmpty()){
            Car car = carQueue.poll();
            if(thisTimeSliceCarMaxSpeed == car.getSpeed())
                readyQueue.add(car);
            else
                specialQueue.add(car);
        }
    }
    //PriorityQueue<Car> specialQueue = carMap.get(maxPlantime + 1);
        if(specialQueue != null && specialQueue.size() != 0) {
            int delay = maxPlantime + 1; //延迟多少时间呢
            int speedFlag = specialQueue.peek().getSpeed(); //
            while (!specialQueue.isEmpty()) {
                Car car = specialQueue.poll();
                if (speedFlag == car.getSpeed()){
                    car.setPlantime(delay);
                    readyQueue.add(car);
                } else {
                    delay++;
                    speedFlag = car.getSpeed();
                    car.setPlantime(delay);
                    readyQueue.add(car);
                }
            }
        }
       System.out.println(readyQueue.size());
    }
}
