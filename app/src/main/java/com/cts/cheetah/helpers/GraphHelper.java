package com.cts.cheetah.helpers;

import com.cts.cheetah.model.Earnings;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

/**
 * Created by manu.palassery on 31-03-2017.
 */

public class GraphHelper {


    public static int getStartValue(ArrayList<Earnings> arrayList){
        double highestValue=0;
        int startValue = 0;
        if(arrayList.size() > 0){
            for(int i=0;i<arrayList.size();i++) {
                double amount = arrayList.get(i).getAmount();
                if (amount > highestValue){
                    highestValue = amount;
                }
            }

            int tempHighestValue = (int) highestValue;
            switch(String.valueOf(tempHighestValue).length()){
                case 1:
                    startValue = 10;
                    break;
                case 2:
                    startValue = 10;
                    break;
                case 3:
                    startValue = 100;
                    break;
                case 4:
                    startValue = 1000;
                    break;
                case 5:
                    startValue = 10000;
                    break;
                case 6:
                    startValue = 100000;
                    break;
            }

            /*if(highestValue <= 10){
                startValue = 10;
            }else if(highestValue > 10 && highestValue <= 100){
                startValue = 100;
            }else if(highestValue > 100 && highestValue <= 1000){
                startValue = 1000;
            }else if(highestValue > 1000 && highestValue <= 10000){
                startValue = 10000;
            }*/
        }
        return startValue;
    }

    public static DataPoint[] getDataPoints(int startValue,ArrayList<Earnings> arrayList){
        DataPoint[] dpa = new DataPoint[arrayList.size()+1];
        dpa[0] = new DataPoint(0, 0);
        for(int i=0;i<arrayList.size();i++){
            dpa[i+1] = new DataPoint(startValue*(i+1), Double.valueOf(arrayList.get(i).getAmount()));
        }

        return dpa;
    }
}
