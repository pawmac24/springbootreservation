package com.pm.reservation.service;

import com.pm.reservation.model.MyObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Created by pmackiewicz on 2015-10-15.
 */
@Service
public class TaskServiceObjectImpl implements TaskServiceObject{
    private final Logger logger = Logger.getLogger(this.getClass());

    private List<MyObject> listMyObject = new ArrayList<>();

    @Override
    public List<MyObject> execute() {
        try {
            Thread.sleep(3000);
            logger.info("Slow task executed");
            //listMyObject = new ArrayList<>();
            List<Long> longList = new Random().longs(0, 100).limit(2).boxed().collect(Collectors.toList());
            //
            Long long1 = longList.get(0);
            listMyObject.add(new MyObject("x" + long1, long1));
            //
            Long long2 = longList.get(1);
            listMyObject.add(new MyObject("y" + long2, long2));
            //
            return listMyObject;
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }
}
