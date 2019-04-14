package com.huawei.test;

import java.util.ArrayList;
import java.util.List;

import com.huawei.Factory.DataFactory;
import com.huawei.controller.CRController2;
import com.huawei.object.Car;

public class TestCheck {
	public static void main(String[] args) throws Exception {
		DataFactory.loadData(Data.carMap, Data.roadMap, Data.crossMap, null);

		CRController2 instance = CRController2.getInstance();
		List<Car> list = new ArrayList<Car>();
		list.addAll(Data.carMap.values());

//		List<Car> carRemove = new ArrayList<>();
//		for(Car car: list) {
//			if(Data.list.contains(car))
//				carRemove.add(car);
//		}
//		for (Car car: carRemove)
//			list.remove(car);
//		instance.init(null);

//		instance.addMapListCar(Data.list);
		instance.testAddMapListCar(list);
//		instance.addMapOneCar(Data.carMap.get(400));
		for(int i = 0; i < 1; i++) {
			instance.update();
		}

	}

	public static void testCross() throws Exception {
		CRController2 instance = CRController2.getInstance();
		List<Car> list = new ArrayList<Car>();
		list.addAll(Data.carMap.values());
		instance.init(null);
//		instance.addMapListCar(list);
		instance.testAddMapListCar(list);
//		instance.addMapOneCar(Data.carMap.get(400));
		for(int i = 0; i < 1; i++) {
			instance.update();
		}
	}
}
