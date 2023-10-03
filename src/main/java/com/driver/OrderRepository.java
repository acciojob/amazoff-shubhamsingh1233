package com.driver;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String,Order> orderHashMap = new HashMap<>();
    HashMap<String,Order> unassignedOrderMap = new HashMap<>();
    HashMap<String,DeliveryPartner> partnerHashMap = new HashMap<>();
    HashMap<String, List<String>> orderPartnerPairMap = new HashMap<>();

    public void addOrderToDB(Order order)
    {
        String key = order.getId();
        orderHashMap.put(key,order);
        unassignedOrderMap.put(key,order);
    }

    public void addPartnerToDB(String partnerId)
    {
        partnerHashMap.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPairToDB(String orderId, String partnerId)
    {
        if(orderPartnerPairMap.containsKey(partnerId))
        {
            List<String> orderList = orderPartnerPairMap.get(partnerId);
            orderList.add(orderId);
            orderPartnerPairMap.put(partnerId,orderList);
            if(unassignedOrderMap.containsKey(orderId))
            {
                unassignedOrderMap.remove(orderId);
            }
        }
        else
        {
            List<String> orderList = new ArrayList<>();
            orderList.add(orderId);
            orderPartnerPairMap.put(partnerId,orderList);
            if(unassignedOrderMap.containsKey(orderId))
            {
                unassignedOrderMap.remove(orderId);
            }
        }
    }

    public Order getOrderByIdFromDB(String orderId)
    {
        if(orderHashMap.containsKey(orderId))
            return orderHashMap.get(orderId);

        return null;
    }

    public DeliveryPartner getPartnerByIdFromDB(String partnerId)
    {
        if(partnerHashMap.containsKey(partnerId))
            return partnerHashMap.get(partnerId);

        return null;
    }

    public List<String> getOrderCountByPartnerIdFromDB(String partnerId)
    {
        List<String> orderList = new ArrayList<>();

        if(orderPartnerPairMap.containsKey(partnerId))
        {
            orderList = orderPartnerPairMap.get(partnerId);
            return orderList;
        }

        return orderList;
    }

    public List<Order> getAllOrdersFromDB()
    {
        List<Order> orderList = new ArrayList<>();

        for(Order obj : orderHashMap.values())
        {
            orderList.add(obj);
        }
        return orderList;
    }

    public int getCountOfUnassignedOrdersFromDB()
    {
        return unassignedOrderMap.size();
    }


    public void deletePartnerIdFromDB(String partnerId)
    {
        if(orderPartnerPairMap.containsKey(partnerId))
        {
            List<String> orderList = orderPartnerPairMap.get(partnerId);
            orderPartnerPairMap.remove(partnerId);

            for(String orderId : orderList)
            {
                unassignedOrderMap.put(orderId,orderHashMap.get(orderId));
            }
        }
    }

    public void deleteOrderByIdFromDB(String orderId)
    {
        orderHashMap.remove(orderId);
        for(String key : orderPartnerPairMap.keySet())
        {
            List<String> orderList = orderPartnerPairMap.get(key);

            if(orderList.indexOf(orderId) != -1)
            {
                int idx = orderList.indexOf(orderId);
                orderList.remove(idx);
            }
        }
    }




}