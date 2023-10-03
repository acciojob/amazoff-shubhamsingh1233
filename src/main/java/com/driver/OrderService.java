package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void addOrder(Order order){
        orderRepository.addOrderToDB(order);
    }

    public void addPartner(String partnerId){
        orderRepository.addPartnerToDB(partnerId);
    }

    public void addOrderPartnerPair(String orderId, String partnerId){
        orderRepository.addOrderPartnerPairToDB(orderId,partnerId);
    }

    public Order getOrderById(String orderId){
        Order order = orderRepository.getOrderByIdFromDB(orderId);
        return order;
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return orderRepository.getPartnerByIdFromDB(partnerId);
    }

    public int getOrderCountByPartnerId(String partnerId){
        List<String> orderList = orderRepository.getOrderCountByPartnerIdFromDB(partnerId);
        return orderList.size();
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        return orderRepository.getOrderCountByPartnerIdFromDB(partnerId);
    }

    public List<String> getAllOrders(){
        List<Order> oderList = orderRepository.getAllOrdersFromDB();
        List<String> ansList = new ArrayList<>();
        for(Order obj : oderList){
            ansList.add(obj.getId());
        }
        return ansList;
    }

    public int getCountOfUnassignedOrders(){
        return orderRepository.getCountOfUnassignedOrdersFromDB();
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        List<String> orderList = orderRepository.getOrderCountByPartnerIdFromDB(partnerId);
        int undelivered = 0;
        int newtime = Integer.parseInt(time.substring(0,2)) * 60 + Integer.parseInt(time.substring(3));
        for(String orderId : orderList){
            if(orderRepository.orderHashMap.containsKey(orderId)){
                Order obj = orderRepository.orderHashMap.get(orderId);
                if(obj.getDeliveryTime() > newtime) undelivered++;
            }
        }
        return undelivered;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        List<String> orderList = orderRepository.getOrderCountByPartnerIdFromDB(partnerId);
        Order obj = orderRepository.orderHashMap.get(orderList.get(orderList.size()-1));
        int time = obj.getDeliveryTime();

        int hours = time / 60;
        int minutes = time % 60;

        // Format hours and minutes as strings with leading zeros
        String hoursStr = String.format("%02d", hours);
        String minutesStr = String.format("%02d", minutes);

        return hoursStr + ":" + minutesStr;
    }

    public void deletePartnerId(String partnerId){
        orderRepository.deletePartnerIdFromDB(partnerId);
    }

    public void deleteOrderById(String orderId){
        orderRepository.deleteOrderByIdFromDB(orderId);
    }

}