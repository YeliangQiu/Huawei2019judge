package com.huawei.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.huawei.Status.CarDirection;
import com.huawei.Status.Location;
import com.huawei.Status.RunStatus;
import com.huawei.object.Car;
import com.huawei.object.CarStatus;
import com.huawei.object.Channel;
import com.huawei.object.Cross;
import com.huawei.object.Road;

public class Data {
	public static Map<Integer, Car> carMap = new HashMap<>();
	public static Map<Integer, Road> roadMap = new HashMap<>();
	public static Map<Integer, Cross> crossMap = new HashMap<>();
	public static List<Car> list = new ArrayList<>();

	static {
		initCar1();
		initRoad();
		initCross();

//		 initSpecial1();
//		 initSpecial2();
		
		
//		initSpecial3();

//		initSpecial5();
//		initSpecial6();
//		initSpecial7();

//		initSpecial8();

		System.out.println(carMap.get(201));
		System.out.println(roadMap.get(5000).getFromChannels().get(0).getParkings());

	}

	public static void Test() {

	}

	public static void initSpecial1() {

		int roadId = 5018, channel = 0, parking = 0, from = 5, to = 1;
		Car car = new Car(600, from, to, 0, 1, 0, 0);
		car.setCarStatus(new CarStatus(CarDirection.STRAIGHT, new Location(roadId, channel, parking, from, to),
				RunStatus.WAITING, 0));
		List<Integer> planWayList = new ArrayList<Integer>();
		planWayList.add(5018);
		car.setPlanWayList(planWayList);

		carMap.put(car.getId(), car);

		Road road = roadMap.get(roadId);
		road.getToChannels().get(channel).getParkings().set(parking, car);

		road.setSpeed(2);
		for (Channel channl : road.getFromChannels()) {
			for (Car ca : channl.getParkings()) {
				if (ca != null)
					ca.getCarStatus().setNowSpeed(2);
			}
		}
	}

	public static void initSpecial2() {

		int roadId = 5018, channel = 0, parking = 0, from = 1, to = 5;
		Car car = new Car(600, from, to, 1, 1, 0, 0);
		car.setCarStatus(new CarStatus(CarDirection.STRAIGHT, new Location(roadId, channel, parking, from, to),
				RunStatus.WAITING, 1));
		List<Integer> planWayList = new ArrayList<Integer>();
		planWayList.add(roadId);
		car.setPlanWayList(planWayList);

		carMap.put(car.getId(), car);

		Road road = roadMap.get(roadId);
		road.getFromChannels().get(channel).getParkings().set(parking, car);

		channel = 1;
		parking = 1;
		car = new Car(601, from, to, 0, 1, 0, 0);
		car.setCarStatus(new CarStatus(CarDirection.STRAIGHT, new Location(roadId, channel, parking, from, to),
				RunStatus.WAITING, 0));
		planWayList = new ArrayList<Integer>();
		planWayList.add(roadId);
		car.setPlanWayList(planWayList);

		carMap.put(car.getId(), car);

		road.getFromChannels().get(channel).getParkings().set(parking, car);

	}

	public static void initSpecial3() {
		
		Car car = new Car(400, 5, 2, 5, 1, 1, 1);
		car.setCarStatus(new CarStatus());
		List<Integer> planWayList = new ArrayList<Integer>();
		planWayList.add(5001);
		car.setPlanWayList(planWayList);
		carMap.put(400, car);
		
	}

	public static void initSpecial5() {

		carMap.get(101).setPriority(1);
		carMap.get(302).setPriority(1);
		carMap.get(402).setPriority(1);
	}

	public static void initSpecial6() {

		Car car = new Car(600, 4, 1, 5, 1, 1, 0);
		car.setCarStatus(new CarStatus());
		List<Integer> planWayList = new ArrayList<Integer>();
		planWayList.add(5000);
		planWayList.add(5018);
		car.setPlanWayList(planWayList);
		carMap.put(car.getId(), car);

		Car car1 = new Car(602, 4, 1, 2, 1, 1, 0);
		car1.setCarStatus(new CarStatus());
		List<Integer> planWayList1 = new ArrayList<Integer>();
		planWayList1.add(5000);
		planWayList1.add(5018);
		car1.setPlanWayList(planWayList1);
		carMap.put(car1.getId(), car1);

		Car car2 = new Car(601, 4, 1, 5, 1, 1, 0);
		car2.setCarStatus(new CarStatus());
		List<Integer> planWayList2 = new ArrayList<Integer>();
		planWayList2.add(5000);
		planWayList2.add(5018);
		car2.setPlanWayList(planWayList1);
		carMap.put(car2.getId(), car2);

		list.add(car);
		list.add(car1);
		list.add(car2);

	}

	public static void initSpecial7() {

		int roadId = 5000, channel = 0, parking = 0, from = 4, to = 1;
		Car car = new Car(500, from, to, 1, 1, 0, 0);
		car.setCarStatus(new CarStatus(CarDirection.STRAIGHT, new Location(roadId, channel, parking, 4, 5),
				RunStatus.WAITING, 1));
		List<Integer> planWayList = new ArrayList<Integer>();
		planWayList.add(5018);
		car.setPlanWayList(planWayList);

		carMap.put(car.getId(), car);

		Road road = roadMap.get(roadId);
		road.getFromChannels().get(channel).getParkings().set(parking, car);
	}

	public static void initSpecial8() {

		int roadId = 5000, channel = 0, parking = 0, from = 4, to = 5;
		Car car = new Car(100, from, to, 5, 1, 0, 0);
		car.setCarStatus(new CarStatus(CarDirection.STRAIGHT, new Location(roadId, channel, parking, 4, 5),
				RunStatus.WAITING, 5));
		List<Integer> planWayList = new ArrayList<Integer>();
		planWayList.add(5000);
		car.setPlanWayList(planWayList);

		carMap.put(car.getId(), car);
		Road road = roadMap.get(roadId);
		road.getFromChannels().get(channel).getParkings().set(parking, car);

		roadId = 5018;
		channel = 0;
		parking = 0;
		from = 1;
		to = 5;
		Car car1 = new Car(200, from, to, 5, 1, 1, 0);
		car1.setCarStatus(new CarStatus(CarDirection.STRAIGHT, new Location(roadId, channel, parking, 1, 5),
				RunStatus.WAITING, 5));
		List<Integer> planWayList1 = new ArrayList<Integer>();
		planWayList1.add(5018);
//		planWayList1.add(5001);
		car1.setPlanWayList(planWayList1);
//		car1.setIndexWay(0);

		carMap.put(car1.getId(), car1);

		road = roadMap.get(roadId);
		road.getFromChannels().get(channel).getParkings().set(parking, car1);

//		roadId = 5010;
//		channel = 0;
//		parking = 0;
//		from = 3;
//		to = 5;
//		Car car2 = new Car(300, from, to, 5, 1, 1, 0);
//		car2.setCarStatus(new CarStatus(CarDirection.STRAIGHT, new Location(roadId, channel, parking, 3, 5),
//				RunStatus.WAITING, 5));
//		List<Integer> planWayList2 = new ArrayList<Integer>();
//		planWayList2.add(5010);
//		car2.setPlanWayList(planWayList2);
//		carMap.put(car2.getId(), car2);
//
//		road = roadMap.get(roadId);
//		road.getFromChannels().get(channel).getParkings().set(parking, car2);
	}
	
	public static void initCar2() {
		
		int[][] priority = {{0,1,0,1}, {1,1,0,1}, {0,1,1,0}};
		
		updateCarData2(100, priority);
		updateCarData2(200, priority);
		updateCarData2(300, priority);
		
	}
	
	public static void updateCarData2(int carId, int[][] priority) {
		
		int channel = carId / 100 - 1;
		List<Integer> planWayList = new ArrayList<Integer>();
		planWayList.add(5000);
		planWayList.add(5001);
		for(int i = carId; i < carId + 4; i++) {
			Car car = new Car(i, 4, 2, 5, 1, priority[channel][i - carId], 1);
			car.setIndexWay(0);
			Location location = new Location(5000, channel, 5 + carId - i - 1, 4, 5);
			CarStatus carStatus = new CarStatus(CarDirection.STRAIGHT, location, RunStatus.WAITING, 5);
			car.setCarStatus(carStatus);
			car.setPlanWayList(planWayList);
			carMap.put(i, car);
		}
		
	}

	public static void initCar1() {
		Map<String, Integer> directionToCrossId = new HashMap<>();
		directionToCrossId.put("5000-" + CarDirection.STRAIGHT, 2);
		directionToCrossId.put("5000-" + CarDirection.LEFT, 1);
		directionToCrossId.put("5000-" + CarDirection.RIGHT, 3);
		directionToCrossId.put("5010-" + CarDirection.RIGHT, 2);
		directionToCrossId.put("5010-" + CarDirection.STRAIGHT, 1);
		directionToCrossId.put("5010-" + CarDirection.LEFT, 4);
		directionToCrossId.put("5001-" + CarDirection.LEFT, 3);
		directionToCrossId.put("5001-" + CarDirection.STRAIGHT, 4);
		directionToCrossId.put("5001-" + CarDirection.RIGHT, 1);
		directionToCrossId.put("5018-" + CarDirection.RIGHT, 4);
		directionToCrossId.put("5018-" + CarDirection.STRAIGHT, 3);
		directionToCrossId.put("5018-" + CarDirection.LEFT, 2);
		Map<String, Integer> directionPlan = new HashMap<>();
		directionPlan.put("5000-" + CarDirection.STRAIGHT, 5001);
		directionPlan.put("5000-" + CarDirection.LEFT, 5018);
		directionPlan.put("5000-" + CarDirection.RIGHT, 5010);
		directionPlan.put("5010-" + CarDirection.RIGHT, 5001);
		directionPlan.put("5010-" + CarDirection.STRAIGHT, 5018);
		directionPlan.put("5010-" + CarDirection.LEFT, 5000);
		directionPlan.put("5001-" + CarDirection.LEFT, 5010);
		directionPlan.put("5001-" + CarDirection.STRAIGHT, 5000);
		directionPlan.put("5001-" + CarDirection.RIGHT, 5018);
		directionPlan.put("5018-" + CarDirection.RIGHT, 5000);
		directionPlan.put("5018-" + CarDirection.STRAIGHT, 5010);
		directionPlan.put("5018-" + CarDirection.LEFT, 5001);
		CarDirection[] list5000 = { CarDirection.STRAIGHT, CarDirection.LEFT, CarDirection.RIGHT, CarDirection.LEFT,
				CarDirection.STRAIGHT, CarDirection.STRAIGHT, CarDirection.STRAIGHT, CarDirection.RIGHT,
				CarDirection.RIGHT };
		CarDirection[] list5010 = { CarDirection.STRAIGHT, CarDirection.STRAIGHT, CarDirection.STRAIGHT,
				CarDirection.LEFT, CarDirection.LEFT, CarDirection.RIGHT, CarDirection.RIGHT, CarDirection.STRAIGHT,
				CarDirection.RIGHT };
		CarDirection[] list5001 = { CarDirection.LEFT, CarDirection.LEFT, CarDirection.RIGHT, CarDirection.RIGHT,
				CarDirection.STRAIGHT, CarDirection.RIGHT, CarDirection.RIGHT, CarDirection.LEFT,
				CarDirection.STRAIGHT };
		CarDirection[] list5018 = { CarDirection.STRAIGHT, CarDirection.STRAIGHT, CarDirection.LEFT, CarDirection.LEFT,
				CarDirection.LEFT, CarDirection.RIGHT, CarDirection.RIGHT, CarDirection.STRAIGHT,
				CarDirection.STRAIGHT };

		updateCarData(4, 5000, 100, directionToCrossId, list5000, directionPlan);
		updateCarData(3, 5010, 200, directionToCrossId, list5010, directionPlan);
		updateCarData(2, 5001, 300, directionToCrossId, list5001, directionPlan);
		updateCarData(1, 5018, 400, directionToCrossId, list5018, directionPlan);

		for (Map.Entry<Integer, Car> entry: carMap.entrySet()) {
			entry.getValue().getCarStatus().setDirection(CarDirection.STRAIGHT);
		}

	}

	public static void updateCarData(int fromCrossId, int roadId, int carId, Map<String, Integer> directionToCrossId,
			CarDirection[] list, Map<String, Integer> directionPlan) {
		int channel = 0, parking = 4;
		for (int i = carId; i < carId + 9; i++) {
			Car car = new Car(i, fromCrossId, directionToCrossId.get(roadId + "-" + list[i - carId]), 5, 1, 0, 0);
//			 Car car = new Car(i, fromCrossId,
//			 directionToCrossId.get(roadId+"-"+list[i - carId]), 5, 1);
			 car.setCarStatus(new CarStatus(list[i - carId], new Location(roadId, channel, parking, fromCrossId, 5), RunStatus.WAITING, 5));
			 car.setIndexWay(0);
			 carMap.put(car.getId(), car);
			 channel++;
			 if(channel > 2) {
			 	channel = 0;
			 	parking--;
			 }
			List<Integer> planWayList = new ArrayList<Integer>();
			planWayList.add(roadId);
			planWayList.add(directionPlan.get(roadId + "-" + list[i - carId]));
			car.setPlanWayList(planWayList);
			carMap.put(i, car);
		}
	}

	public static void initRoad() {
		updateRoadData(100, 5000, 4, 5);
		updateRoadData(200, 5010, 3, 5);
		updateRoadData(300, 5001, 2, 5);
		updateRoadData(400, 5018, 1, 5);
	}

	public static void initRoad2() {
		updateRoadData2(5000, 4, 5);
		updateRoadData2(5010, 3, 5);
		updateRoadData2(5001, 2, 5);
		updateRoadData2(5018, 1, 5);
		updateRoadSpecialData(5000);
	}
	
	public static void updateRoadSpecialData(int roadId) {
		
		Road road = roadMap.get(5000);
		List<Channel> channels = road.getFromChannels();
		int carId = 100;
		for(int i = 0; i < 3; i++) {
			for(int j = 4; j >= 1; j--) {
				channels.get(i).getParkings().set(j, carMap.get(carId));
				carId++;
				if(carId % 100 == 4) {
					carId = carId + 100 - 4;
				}
			}
		}
		
	}

	public static void updateRoadData(int carId, int roadId, int fromCrossId, int toCrossId) {
		Road road = new Road(roadId, 5, 5, 3, fromCrossId, toCrossId, 1);
		roadMap.put(road.getId(), road);
//		List<Channel> channels = road.getFromChannels();
//		int nowCarId = carId;
//		for(int i = 4; i >= 2; i--) {
//			for(int j = 0; j < 3; j++) {
//				channels.get(j).getParkings().set(i, carMap.get(nowCarId));
//				nowCarId++;
//			}
//		}
	}

	public static void updateRoadData2(int roadId, int fromCrossId, int toCrossId) {
		Road road = new Road(roadId, 5, 5, 3, fromCrossId, toCrossId, 1);
		roadMap.put(road.getId(), road);
		// List<Channel> channels = road.getFromChannels();
		//
		// int nowCarId = carId;
		// for(int i = 4; i >= 2; i--) {
		// for(int j = 0; j < 3; j++) {
		// channels.get(j).getParkings().set(i, carMap.get(nowCarId));
		// nowCarId++;
		// }
		// }
	}

	public static void initCross() {
		List<Integer> roadId_fromCross = new ArrayList<>();
		roadId_fromCross.add(5018);
		roadId_fromCross.add(5001);
		roadId_fromCross.add(5010);
		roadId_fromCross.add(5000);
		List<Integer> roadId_toCross = new ArrayList<>();
		roadId_toCross.add(5018);
		roadId_toCross.add(5001);
		roadId_toCross.add(5010);
		roadId_toCross.add(5000);
		int[] roadIds = { 5018, 5001, 5010, 5000 };
		Cross cross = new Cross(5, roadIds, roadId_fromCross, roadId_toCross);
		crossMap.put(cross.getId(), cross);

		roadId_fromCross = new ArrayList<>();
		roadId_fromCross.add(-1);
		roadId_fromCross.add(-1);
		roadId_fromCross.add(5018);
		roadId_fromCross.add(-1);
		roadId_toCross = new ArrayList<>();
		roadId_toCross.add(-1);
		roadId_toCross.add(-1);
		roadId_toCross.add(5018);
		roadId_toCross.add(-1);
		int[] roadIds2 = { -1, -1, 5018, -1 };
		cross = new Cross(1, roadIds2, roadId_fromCross, roadId_toCross);
		crossMap.put(cross.getId(), cross);

		roadId_fromCross = new ArrayList<>();
		roadId_fromCross.add(-1);
		roadId_fromCross.add(-1);
		roadId_fromCross.add(-1);
		roadId_fromCross.add(5001);
		roadId_toCross = new ArrayList<>();
		roadId_toCross.add(-1);
		roadId_toCross.add(-1);
		roadId_toCross.add(-1);
		roadId_toCross.add(5001);
		int[] roadIds3 = { -1, -1, -1, 5001 };
		cross = new Cross(2, roadIds3, roadId_fromCross, roadId_toCross);
		crossMap.put(cross.getId(), cross);

		roadId_fromCross = new ArrayList<>();
		roadId_fromCross.add(5010);
		roadId_fromCross.add(-1);
		roadId_fromCross.add(-1);
		roadId_fromCross.add(-1);
		roadId_toCross = new ArrayList<>();
		roadId_toCross.add(5010);
		roadId_toCross.add(-1);
		roadId_toCross.add(-1);
		roadId_toCross.add(-1);
		int[] roadIds4 = { 5010, -1, -1, -1 };
		cross = new Cross(3, roadIds4, roadId_fromCross, roadId_toCross);
		crossMap.put(cross.getId(), cross);

		roadId_fromCross = new ArrayList<>();
		roadId_fromCross.add(-1);
		roadId_fromCross.add(5000);
		roadId_fromCross.add(-1);
		roadId_fromCross.add(-1);
		roadId_toCross = new ArrayList<>();
		roadId_toCross.add(-1);
		roadId_toCross.add(5000);
		roadId_toCross.add(-1);
		roadId_toCross.add(-1);
		int[] roadIds5 = { -1, 5000, -1, -1 };
		cross = new Cross(4, roadIds5, roadId_fromCross, roadId_toCross);
		crossMap.put(cross.getId(), cross);
	}

}
