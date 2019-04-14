package com.huawei.utl;

import com.huawei.Factory.DataFactory;
import com.huawei.object.Car;
import com.huawei.object.Cross;
import com.huawei.object.Road;

import java.util.*;

/**
 * @author : qiuyeliang
 * create at:  2019/3/10  15:27
 * @description:
 */
public class MapUtils {
    static double [][] weightMatrix;
    static  double [][] carNumMatrix;

    public static void init (int len){
        weightMatrix = new double [len][len];
        carNumMatrix = new double[len][len];
    }
    public static int[][]createCrossMap(Map<Integer,Cross> crossMap, Map<Integer, Road>roadMap){
        int maxCrossNumber = 0;
        for(Map.Entry<Integer,Cross> map:crossMap.entrySet()){
            if(maxCrossNumber < map.getKey()){
                maxCrossNumber = map.getKey();
            }
        }
        System.out.println(maxCrossNumber);
        int[][] crossMatrix = new int[maxCrossNumber+1][maxCrossNumber+1]; //路口的id没有0
        for(Map.Entry<Integer,Road> map:roadMap.entrySet()){
            Road road = map.getValue();  //路对象
            int start = road.getFrom(); //开始的路口
            int end = road.getTo();  //结束的路口
            int id = road.getId(); //道路的ID
            int duplex = road.getDuplex();//是否双向
            if(duplex == 1) { //如果双向
                crossMatrix[start][end] = id;
                crossMatrix[end][start] = id;
            }else//如果单向
                crossMatrix[start][end] = id;
        }
        return crossMatrix;
    }

    // 对每辆车都有一个自己的权重矩阵
    public static double[][] createWeightMatrix(int[][] crossMatrix, Map<Integer,Cross>crossMap, Map<Integer,Road>roadMap, Car car){
        double weightMatrix[][] = new double[crossMatrix.length][crossMatrix.length];
//        for(Map.Entry<Integer,Cross> map:crossMap.entrySet()){
//            int[] id = map.getValue().getRoads();
//        }
        int carSpeed = car.getSpeed(); //车的速度
        for(int i=1;i<crossMatrix.length;i++){
            for(int j=1;j<crossMatrix[i].length;j++){
                if( crossMatrix[i][j] == 0 || i == j) //如果路口不通或出发点与结束点一样
                    continue;
                int roadSpeed = roadMap.get(crossMatrix[i][j]).getSpeed(); //获取道路速度
                int roadlenth = roadMap.get(crossMatrix[i][j]).getLength(); //获取道路长度
                if(roadSpeed > carSpeed) {
                    weightMatrix[i][j] = roadlenth * 1.0 / carSpeed;
                }
                else {
                    weightMatrix[i][j] = roadlenth * 1.0 / roadSpeed;
                }
            }
        }
        return weightMatrix;
    }

    //利用SPFA算法找到最短路径，并且把最短路径存在对象car
    public static void SPFA(int[][] crossMatrix, double[][] weightMatrix, double [][] carNumMatrix, Car car, int startCross,Map<Integer,Cross> crossMap){
        int crossNum = weightMatrix.length;
        double[] dist = new double[crossNum]; //源点到顶点i的最短距离
        int[] path = new int[crossNum]; //恢复路径用
        int start = startCross;
        int end = car.getTo();
        List<Integer> planWay = new ArrayList<>();
        List<Integer> planCross = new ArrayList<>();
        Queue<Integer> queue = new ArrayDeque<>();
        Stack<Integer> stack = new Stack<>();
        queue.add(start);
        while(!queue.isEmpty()){
            int cross = queue.poll();
            for(Map.Entry<Integer,Cross> map:crossMap.entrySet()){ //遍历所有结点,100多次
                int i = map.getKey();
                if(weightMatrix[cross][i]!=0 && (weightMatrix[cross][i] + dist[cross] < dist[i] || dist[i] == 0)){
                    dist[i] = weightMatrix[cross][i] + dist[cross];
                    path[i] = cross;
                    queue.add(i);
                }
            }
        }
        //System.out.println("最短路径(混合权重)"+dist[end]);
        car.setWeight(dist[end]);
        //car.setCompleteTime((int)dist[end]);
        int node =  end; //用于迭代路径
        stack.push(node);
        while(start != node){
            stack.push(path[node]);
            node = path[node];
        }
        while(!stack.empty()){
//            planCross.add(stack.peek());
//            System.out.println("-->" + stack.pop());
            planCross.add(stack.pop());
        }
       // System.out.println("经过的道路");
        int weight = 0;//拥堵权重
        List<Double> weightList = new ArrayList<>();
        for(int i=1;i<planCross.size();i++){
//            System.out.println("-->");
//            System.out.println(crossMatrix[planCross.get(i-1)][planCross.get(i)]);
            int roadId = crossMatrix[planCross.get(i-1)][planCross.get(i)];
            planWay.add(roadId);
            weight += carNumMatrix[planCross.get(i-1)][planCross.get(i)];
            weightList.add(carNumMatrix[planCross.get(i-1)][planCross.get(i)]);
        }
        car.setPlanWayList(planWay);
        car.setPlanCrossList(planCross);
        car.setWeight(weight);
        car.setWeightList(weightList);
    }
//    public static void changeCarListPlanWay(Car car,int start,int[][] crossMatrix,Map<Integer,Road> roadMap){
//        Map<String,Integer> roadSpeedMap = null;
//        for(Map.Entry<Integer,Road> map:roadMap.entrySet()){
//            roadSpeedMap = map.getValue().getNowSpeed();
//            break;
//        }
//        SPFA(crossMatrix,updateWeightMatrix(crossMatrix,roadSpeedMap,roadMap,car),car,start);
//    }

//    public static void changeCarPlanWay(Car car,int[][] crossMatrix,Map<Integer,Road> roadMap){
//        Map<String,Integer> roadSpeedMap = null;
//        for(Map.Entry<Integer,Road> map:roadMap.entrySet()){
//            roadSpeedMap = map.getValue().getNowSpeed();
//            break;
//        }
//        SPFA(crossMatrix,updateWeightMatrix(crossMatrix,roadSpeedMap,roadMap,car),car);
//    }
//public static void changeCarPlanWay(Car car,int start,int[][] crossMatrix,Map<Integer,Road> roadMap,int nextRoadId,Map<Integer,Cross> crossMap){
////    long now = System.currentTimeMillis();
////    double[][] weightMatrix = new double[crossMatrix.length][crossMatrix.length];
////    double [][] carNumMatrix = new double[crossMatrix.length][crossMatrix.length];
////    System.out.println(System.currentTimeMillis() - now);
//    //List<Integer> realWayList = car.getRealWayList();
//    int carSpeed = car.getSpeed();
////    long now = System.currentTimeMillis();
//    for(Map.Entry<Integer,Road> map:roadMap.entrySet()){ //遍历roadMap中的所有道路
//        Road road = map.getValue();
//        Map<String,String> nowSpeed = road.getNowSpeed();
//        for(Map.Entry<String,String> speedMap:nowSpeed.entrySet()){ //遍历这条道路的两个结点
//            String str = speedMap.getKey();
//            int[] fromTo = strToint(str.split("-"));//分割
//            int[] carSpeedAndNum = strToint(speedMap.getValue().split("-"));
//            // weightMatrix[fromTo[0]][fromTo[1]] = carSpeedAndNum[0]; //权重矩阵此时的速度更改为
//            double roadSpeed = carSpeedAndNum[0]; //当前道路最后一辆车的速度
//            double roadLenth = roadMap.get(crossMatrix[fromTo[0]][fromTo[1]]).getLength();//路的长度
//            int chennl = roadMap.get(crossMatrix[fromTo[0]][fromTo[1]]).getChannel(); //有多少车道
//            carNumMatrix[fromTo[0]][fromTo[1]] = carSpeedAndNum[1] / (roadLenth * chennl); //车子数量的占道比率
////                if(carNumMatrix[fromTo[0]][fromTo[1]] > 0.9)
////                    System.out.println(111);
//            if(roadSpeed > carSpeed) //如果路的速度大于车的速度
//                weightMatrix[fromTo[0]][fromTo[1]] = roadLenth / carSpeed + (carSpeedAndNum[1]/(roadLenth * chennl)) * 10;
//                //weightMatrix[fromTo[0]][fromTo[1]] = carSpeedAndNum[1];
//            else
//                weightMatrix[fromTo[0]][fromTo[1]] = roadLenth / roadSpeed + (carSpeedAndNum[1]/(roadLenth * chennl)) * 10;
//            //weightMatrix[fromTo[0]][fromTo[1]] = carSpeedAndNum[1];
//            //System.out.println("carSppedAndNum="+carSpeedAndNum[1]);
//        }
//    }
////        for(Integer roadId:realWayList){ //不走回头路
////            Road road = roadMap.get(roadId);
////            weightMatrix[road.getFrom()][road.getTo()] = 0;
////            weightMatrix[road.getTo()][road.getFrom()] = 0;
////        }
//    if(nextRoadId != -1) { //初始化的时候不知道下一条路是什么,不走回头路
//        Road road = roadMap.get(nextRoadId);
//        weightMatrix[road.getFrom()][road.getTo()] = 0;
//        weightMatrix[road.getTo()][road.getFrom()] = 0;
////            if(realWayList.size() > 1) {
////                road = roadMap.get(realWayList.get(realWayList.size() - 1));
////                weightMatrix[road.getFrom()][road.getTo()] = 0;
////                weightMatrix[road.getTo()][road.getFrom()] = 0;
////                road = roadMap.get(realWayList.get(realWayList.size() - 2));
////                weightMatrix[road.getFrom()][road.getTo()] = 0;
////                weightMatrix[road.getTo()][road.getFrom()] = 0;
////            }
//    }
//    //SPFA(crossMatrix,updateWeightMatrix(crossMatrix,weightMatrix,roadMap,car),car,start);
//    SPFA(crossMatrix,weightMatrix,carNumMatrix,car,start, crossMap);
//}


    
    public static int[] strToint(String[] str){
        int[] res = new int[str.length];
        for(int i=0;i<res.length;i++)
            res[i] = Integer.parseInt(str[i]);
        return res;
    }

    public static double[][] updateWeightMatrix(int[][] crossMatrix, Map<String,Integer> roadSpeedMap,Map<Integer,Road> roadMap,Car car){
        double[][] weightMatrix = new double[crossMatrix.length][crossMatrix.length]; //创建weightMatrix数组
        int carSpeed = car.getSpeed(); //车子的最大速度
        for(Map.Entry<String,Integer> map:roadSpeedMap.entrySet()){
            String str = map.getKey();
            int[] fromTo = strToint(str.split("-"));//分割
            weightMatrix[fromTo[0]][fromTo[1]] = map.getValue();//赋值,如果太堵可能会是0
            System.out.println("map.getValues" + map.getValue());
        }
        for (int i=1;i<crossMatrix.length;i++){
            for(int j=1;j<crossMatrix.length;j++) {
                if(crossMatrix[i][j] == 0 || i == j) //如果道路不通
                    continue;
                double roadSpeed = weightMatrix[i][j]; //路的当前速度
                double roadLenth = roadMap.get(crossMatrix[i][j]).getLength();//路的长度
                //System.out.println("道路现在的速度="+roadSpeed);
                if(roadSpeed > carSpeed)
                    weightMatrix[i][j] = roadLenth / carSpeed;
                else
                    weightMatrix[i][j] = roadLenth / roadSpeed;
            }
        }
        return weightMatrix;
    }

//    //找到两个cross之间道路的速度
//    private static int roadspeed(int start,int end,Map<Integer,Cross> crossMap,Map<Integer,Road> roadMap){
//        Cross cross = crossMap.get(start); //首先根据结点id拿到路口map
//        int[] roadId = cross.getRoads();//取出路由四条道路的id
//        int res = 0;
//        for(int i=0;i<4;i++){ //遍历4个id
//            if(roadId[i] == 0) //如果id为0说明路口的这个方向没有这条路
//                continue;
//            if((roadMap.get(roadId[i]).getFrom()==start && roadMap.get(roadId[i]).getDuplex()==1 )|| roadMap.get(roadId[i]).getTo() == end) {
//                res = roadMap.get(roadId[i]).getSpeed();
//                break;
//            }
//        }
//        return res;
//    }
}
