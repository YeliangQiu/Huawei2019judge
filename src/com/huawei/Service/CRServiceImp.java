package com.huawei.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.huawei.Factory.DataFactory;
import com.huawei.Factory.ServiceFactory;
import com.huawei.Status.CarDirection;
import com.huawei.Status.Location;
import com.huawei.Status.RunStatus;
import com.huawei.controller.CRController2;
import com.huawei.object.Car;
import com.huawei.object.CarStatus;
import com.huawei.object.CarUtil;
import com.huawei.object.Channel;
import com.huawei.object.Road;
import com.huawei.utl.MapUtils;

public class CRServiceImp implements CRService {
	
	RoadService roadService = null;
	CrossService crossService = null;
	CarService carService = null;
	
	public CRServiceImp() {
		roadService = ServiceFactory.roadService;
		crossService = ServiceFactory.crossService;
		carService = ServiceFactory.carService;
	}
	
	@Override
	public CarStatus getThroughCrossNextStatus(int distance, int nextRoadId, Car car) throws Exception {

		CarStatus carStatus = car.getCarStatus();
		Location loc = carStatus.getLocation();
		
		Road road = roadService.getRoadById(nextRoadId);
		int toCrossId = loc.getToCrossId();
		int toCrossId2 = road.getFrom();
		if(toCrossId == road.getFrom())
			toCrossId2 = road.getTo();
//		if(toCrossId == toCrossId2)
//			System.out.println(toCrossId);
//		int next2RoadId = carService.getNext2RoadId(car, toCrossId2, nextRoadId);
//		CarDirection direction = crossService.getDirection(toCrossId2, nextRoadId, next2RoadId);
		CarDirection direction = CarDirection.NOT;
		CarStatus s = new CarStatus(direction,
				new Location(nextRoadId, 0, -1, toCrossId, toCrossId2), 
				RunStatus.FINISHED, roadService.calculate(nextRoadId, car.getSpeed()));
		
		if(car.getId() == 103439 && loc.getRoadId() == 6284) {
			int time = CRController2.getInstance().getAllTime();
			System.out.println("s");
		}
		
		CarStatus occupyStatus = roadService.getFirstCanOccupyStatus(distance, s);
		
		if(carStatus.getDirection() == CarDirection.NONE)
			return occupyStatus;
		
		if(occupyStatus != null)
			return occupyStatus;
		
		road = roadService.getRoadById(loc.getRoadId());
		s = new CarStatus(carStatus);
		s.setRunStatus(RunStatus.FINISHED);
		s.getLocation().setParking(road.getLength() - 1);
		return s;
		
	}
	
//	public int getThroughNextDis(CarStatus carStatus, int vCar) {
//		int nowRoadId = carStatus.getLocation().getRoadId();
//		Road nowRoad = roadService.getRoadById(nowRoadId);
//		int nowRestLength = nowRoad.getLength() - 1 - carStatus.getLocation().getParking();
//		
//		return nowRestLength;
//	}
	
	@Override
	public int getThroughCrossNextDis(CarStatus carStatus, int vCar, int nextRoadId) {
		
		int nowRoadId = carStatus.getLocation().getRoadId();
		Road nowRoad = roadService.getRoadById(nowRoadId);
		int nowRestLength = nowRoad.getLength() - 1 - carStatus.getLocation().getParking();
		int nowGetSpeed = roadService.calculate(nextRoadId, vCar);
		int nextLength = nowGetSpeed - nowRestLength;
		nextLength = nextLength < 0 ? 0 : nextLength;
		return nextLength;
		
	}
	
	@Override
	public CarStatus getBeginDepStatus(Car car) throws Exception {
//		if(car.isUpdateWay()) {
//			carService.getCarNextRoadId(car, car.getFrom(), -1);
//			if(car.getPriority() == 1)
//				car.setUpdateWay(false);
//		}
		int nextRoadId = car.getPlanWayList().get(0);
		int distance = roadService.calculate(nextRoadId, car.getSpeed());
		car.getCarStatus().getLocation().setToCrossId(car.getFrom());

		return getThroughCrossNextStatus(distance, nextRoadId, car);
	}
	
	@Override
	public CarStatus getStraDepStatus(Car car) throws Exception {
		
		CarStatus carStatus = car.getCarStatus();
		
		int straNextRoadId = crossService.getNextRoadID(car);
		if((car.getIndexWay() == car.getPlanWayList().size() - 1 && straNextRoadId != carStatus.getLocation().getRoadId()) 
				|| (car.getIndexWay() + 1 < car.getPlanWayList().size() && straNextRoadId != car.getPlanWayList().get(car.getIndexWay() + 1)))
			throw new Exception(car.getId() + " have path with problem" + car);
		if(straNextRoadId == -1) {
			throw new Exception(car.getId() + " have path with problem" + car);
		}

		Location loc = carStatus.getLocation();
		Car depcar = getDepCarStatus(loc.getRoadId(), loc.getToCrossId(), straNextRoadId);
		if(isHaveDepCar(car, depcar) < 0)
			return null;
		
		if(carStatus.getLocation().getRoadId() == straNextRoadId) {
			int rest = roadService.getRoadById(straNextRoadId).getLength() 
					- 1 - carStatus.getLocation().getParking();
			CarStatus occupy = roadService.getSameChaCarId(carStatus.getLocation(), rest);
			if(rest < carStatus.getNowSpeed() && occupy == null) {
				depcar = getDepCarStatus(loc.getRoadId(), loc.getToCrossId(), crossService.getStraNextRoadId(loc.getToCrossId(),loc.getRoadId()));
				if(isHaveDepCar(car, depcar) < 0)
					return null;
				return new CarStatus(true);
			}
		}
		
		int distance = getThroughCrossNextDis(carStatus, car.getSpeed(), straNextRoadId);
		return getThroughCrossNextStatus(distance, straNextRoadId, car);
	}
	
	/**
	 * 优先级车依赖
	 * @param nowRoadId
	 * @return
	 */
	private Car getDepCarStatus(int nowRoadId, int toCrossId, int nextRoadId) {
		
		List<Integer> otherRoadId = crossService.getOtherRoadId(nowRoadId, toCrossId);
		TreeSet<CarUtil> depCars = new TreeSet<CarUtil>();
		for(int roadId: otherRoadId) {
			if(roadId == nextRoadId)
				continue;
			Car car = roadService.getLastCarIdByCRId(roadId, toCrossId);
			if (car == null)
				continue;
			CarStatus carStatus = car.getCarStatus();
			int nextRoaI = crossService.getNextRoadID(car);
			if(nextRoaI == nextRoadId && carStatus.getRunStatus() == RunStatus.WAITING) {
//				if(roadId == nextRoadId) {
//					System.out.println("");
//				}
				depCars.add(new CarUtil(car));
			}
		}
		
		if(depCars.size() == 0)
			return null;
		
		return depCars.first().getCar();
	}
	
	/**
	 * 当前车是否依赖depCar
	 * @param car
	 * @param depCar
	 * @return -1依赖，1为不依赖
	 * @throws Exception
	 */
	private int isHaveDepCar(Car car, Car depCar) throws Exception {
		
		if(depCar == null)
			return 1;
			
		CarStatus carStatus = car.getCarStatus();
//		if(depCar.getCarStatus().getDirection() == CarDirection.NONE)
//			throw new Exception(depCar.getId() + " have joined problem");
		
		if(depCar.getCarStatus().getDirection() == CarDirection.NOT)
			throw new Exception("isHaveDepCar lock");
		
		if(car.getPriority() == 1) {
			if(depCar.getPriority() == 1 &&
					depCar.getCarStatus().getDirection().getDirection() < carStatus.getDirection().getDirection())
				return -1;
		}else {
			if(depCar.getPriority() == 1
					|| depCar.getCarStatus().getDirection().getDirection() < carStatus.getDirection().getDirection())
				return -1;
		}
		
		return 1;
	}
	
	@Override
	public CarStatus getLeftDepStatus(Car car) throws Exception {
		
		CarStatus carStatus = car.getCarStatus();
//		int depRoaId = crossService.getDepRoadId(car, 0);
//		if(depRoaId != -1) {
//			CarStatus depCarStatus = getDepCarStatus(depRoaId, carStatus);
//			if(depCarStatus != null
//					&& depCarStatus.getDirection() == CarDirection.STRAIGHT 
//					&& depCarStatus.getRunStatus() == RunStatus.WAITING)
//				return null;
//		}
		
		int depRoaId = crossService.getNextRoadID(car);
		if(depRoaId != car.getPlanWayList().get(car.getIndexWay() + 1))
			throw new Exception(car.getId() + " have path with problem" + car);
		if(depRoaId == -1) {
			throw new Exception(car.getId() + " have path with problem");
		}

		if(car.getId() == 103)
			System.out.println(car);

		Location loc = carStatus.getLocation();
		Car depcar = getDepCarStatus(loc.getRoadId(), loc.getToCrossId(), depRoaId);
		
		if(isHaveDepCar(car, depcar) < 0)
			return null;
		
//		if(depcar != null) {
//			if(depcar.getCarStatus().getDirection() == CarDirection.NONE)
//				throw new Exception(depcar.getId() + " have joined problem");
//			
//			if(car.getPriority() == 1) {
//				if(depcar.getPriority() == 1 &&
//						depcar.getCarStatus().getDirection().getDirection() < carStatus.getDirection().getDirection())
//					return null;
//			}else {
//				if(depcar.getPriority() == 1)
//					return null;
//			}
//		}
			
		int distance = getThroughCrossNextDis(carStatus, car.getSpeed(), depRoaId);
		
		return getThroughCrossNextStatus(distance, depRoaId, car);
	}

	@Override
	public CarStatus getRightDepStatus(Car car) throws Exception {
		
		CarStatus carStatus = car.getCarStatus();
//		int depRoaId = crossService.getDepRoadId(car, 0);
//		if(depRoaId != -1) {
//			CarStatus depCarStatus = getDepCarStatus(depRoaId, carStatus);;
//			if(depCarStatus != null 
//					&& depCarStatus.getDirection() == CarDirection.LEFT
//					&& depCarStatus.getRunStatus() == RunStatus.WAITING)
//				return null;
//			
//		}
//		
//		depRoaId = crossService.getDepRoadId(car, 1);
//		if(depRoaId != -1) {
//			CarStatus depCarStatus = getDepCarStatus(depRoaId, carStatus);
//			if(depCarStatus != null 
//					&& depCarStatus.getDirection() == CarDirection.STRAIGHT 
//					&& depCarStatus.getRunStatus() == RunStatus.WAITING)
//				return null;
//		}
		
		int depRoaId = crossService.getNextRoadID(car);
		if(depRoaId != car.getPlanWayList().get(car.getIndexWay() + 1))
			throw new Exception(car.getId() + " have path with problem" + car);
		if(depRoaId == -1) {
			throw new Exception(car.getId() + " have path with problem" + car);
		}
		
		if(car.getId() == 103439 && carStatus.getLocation().getRoadId() == 6284) {
			int time = CRController2.getInstance().getAllTime();
			System.out.println("s");
		}
		
		Location loc = carStatus.getLocation();
		Car depcar = getDepCarStatus(loc.getRoadId(), loc.getToCrossId(), depRoaId);
		
		if(isHaveDepCar(car, depcar) < 0)
			return null;
		
		int distance = getThroughCrossNextDis(carStatus, car.getSpeed(), depRoaId);
		
		return getThroughCrossNextStatus(distance, depRoaId, car);
	}

	public void updateCrossRoadProCar(int toCrossId, Set<Integer> roadId_toCross) {

		for(int roadId: roadId_toCross) {
			if(roadId == -1)
				continue;
			Car car = roadService.getLastCarIdByCRId(roadId, toCrossId);
			if(car != null && car.getCarStatus().getRunStatus() == RunStatus.WAITING) {
				boolean isOut = roadService.isOut(roadId, car.getCarStatus().getLocation(), car.getCarStatus().getNowSpeed());
				if(isOut) {
					int nextRoadId = carService.getCarNextRoadId(car, toCrossId, roadId);
					CarDirection carDirection = crossService.getDirection(toCrossId, roadId, nextRoadId);
					car.getCarStatus().setDirection(carDirection);
				}
			}

		}

	}
	
	public void updateRoadByToCrossId(int roadId, int toCrossId) throws Exception {
		
		
//		if(roadId == 5065 && )
		
		Road road = roadService.getRoadById(roadId);
		int fromCrossId = road.getFrom();
		if(toCrossId == road.getFrom()) {
			if(road.getIsDuplex() != 1)
				return;
			fromCrossId = road.getTo();
		}
		List<Channel> channls = roadService.getChannls(roadId, fromCrossId, toCrossId);
		if(channls == null)
			throw new Exception("updateRoadByToCrossId");
		for(int i = 0; i < road.getChannel(); i++) {
//			Channel channel = channls.get(i);
			updateRoad(road, channls, i);
		}
	}
	
	private void updateRoad(Road road, List<Channel> channls, int i) throws Exception {
		
		Channel channel = channls.get(i);
		
		Car car = roadService.getLastCarByChannels(channel, road.getLength());
		if(car == null) {
			return;
		}
		CarStatus carStatus = car.getCarStatus();
		if(carStatus.getRunStatus() == RunStatus.FINISHED) {
			roadService.updateChannel(channel);
			return;
		}

		boolean isOut = roadService.isOut(road.getId(), carStatus.getLocation(), carStatus.getNowSpeed());
		if(!isOut) {
			roadService.updateChannel(channel);
		}else {
			if(car.getPriority() == 1 || roadService.getProChaCaId(carStatus.getLocation(), channls) == null)
				updateRoadThroughCross(car, channls, i, road);
		}
	}
	
	private void updateRoadThroughCross(Car car, List<Channel> channls, int i, Road road) throws Exception {
		CarStatus depStatus = null;
		
		CarDirection carDirection = car.getCarStatus().getDirection();
		Location loc = car.getCarStatus().getLocation();
		
//		if(car.isUpdateWay()) {
		int nextRoadId = carService.getCarNextRoadId(car, loc.getToCrossId(), loc.getRoadId());
		carDirection = crossService.getDirection(loc.getToCrossId(), loc.getRoadId(), nextRoadId);
		car.getCarStatus().setDirection(carDirection);
//			car.setUpdateWay(false);
//		}
		
		if(carDirection == CarDirection.STRAIGHT) {
			depStatus = getStraDepStatus(car);
		}else if(carDirection == CarDirection.LEFT) {
			depStatus = getLeftDepStatus(car);
		}else if(carDirection == CarDirection.RIGHT) {
			depStatus = getRightDepStatus(car);
		}
		
		if(depStatus == null || depStatus.getRunStatus() == RunStatus.WAITING)
			return;
		
//		if(!depStatus.getisFinished() && loc.getRoadId() != depStatus.getLocation().getRoadId())
//			car.setUpdateWay(true);
		CRController2.getInstance().updateThroughCross(car, depStatus);
		updateRoad(road, channls, i);
		
	}

	@Override
	public void driveCar(Car car, CarStatus carStatus) {
		
		Location loc = car.getCarStatus().getLocation();
		if(car.getCarStatus().getDirection() != CarDirection.NONE)
			roadService.clearChannelByLoc(loc);
		if(carStatus.getisFinished()) {
			car.setCarStatus(new CarStatus());
//			car.setUpdateWay(true);
			return;
		}
		int nextRoadId = carStatus.getLocation().getRoadId();
		carService.updateCar(car, carStatus);
		roadService.driveCar(nextRoadId, car);

	}

}
