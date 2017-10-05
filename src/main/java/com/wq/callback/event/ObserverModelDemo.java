package com.wq.callback.event;

import org.omg.SendingContext.RunTime;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:wangqiang20995
 * @description:观察者模式
 * @Date:2017/10/5
 */
public class ObserverModelDemo {
    public static void main(String args[]){
        //主题
        AbstractWeatherSubject weatherSubject = new WeatherSubjectImpl();

        //具体观察者
        WeatherObserver observer = new WeatherObserverImpl(weatherSubject);

        weatherSubject.updateWeatherData(10,12,14);
    }
}

class WeatherSubjectImpl extends AbstractWeatherSubject{

    private List<WeatherObserver> observerList;

    private float temperature;

    private float pressure;

    private float humidity;

    public WeatherSubjectImpl(){
        observerList = new ArrayList<>();
    }

    @Override
    public void registryObserver(WeatherObserver observer) {
        if(observerList.contains(observer)){
            System.out.println(String.format("当前已注册了该观察者【%s】",observer.getClass().getName()));
        }else{
            observerList.add(observer);
        }
    }

    @Override
    public void removeObserver(WeatherObserver observer) {
        if(observerList.contains(observer)){
            observerList.remove(observer);
        }
    }

    @Override
    public void notifyObserver() {
        for(WeatherObserver observer : observerList){
            observer.update(temperature,humidity,pressure);
        }
    }


    private void fireDataChanged(){
        notifyObserver();
    }

    @Override
    public void updateWeatherData(float temperature,float humidity,float pressure){
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        fireDataChanged();
    }

}

abstract class AbstractWeatherSubject implements WeatherSubject{
    public abstract void updateWeatherData(float temperature,float humidity,float pressure);
}

class WeatherObserverImpl implements WeatherObserver,Show{

    private float temperature;

    private float humidity;

    private WeatherSubject weatherSubject;

    public WeatherObserverImpl(WeatherSubject subject){
        this.weatherSubject = subject;
        this.weatherSubject.registryObserver(this);
    }

    public WeatherObserverImpl(){
        if(weatherSubject == null){
            throw new RuntimeException("请先将当前观察者绑定到主题对象");
        }else {
            this.weatherSubject.registryObserver(this);
        }
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        display();
    }

    @Override
    public void display() {
        System.out.println(String.format("天气观察者【1】，更新观察数据【temperature,humidity】-->【%s,%s】",temperature,humidity));
    }
}
//主题接口【添加、移除观察者对象，当数据更改的时候通知观察者对象】
interface WeatherSubject{
    //注册
    void registryObserver(WeatherObserver observer);

    //移除
    void removeObserver(WeatherObserver observer);

    //通知
    void notifyObserver();
}

//观察者接口对象，负责不同的实现
interface WeatherObserver{
    void update(float temperature,float humidity,float pressure);
}

//展现接口
interface Show{
    void display();
}