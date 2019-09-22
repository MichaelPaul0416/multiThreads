package wq.callback.event;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:wangqiang20995
 * @description:简单模拟事件驱动
 * @Date:2017/10/3
 */
public class SimpleEventDemo {
    public static void main(String args[]){
        Event event = new Event();
        CallBack callBack = new OnClick();
        event.registry(callBack);

        event.fireRegistrySucceed();
    }

}


class Event{
    private List<CallBack> queue;

    public Event(){
        queue = new ArrayList<>();
    }

    public boolean registry(CallBack callBack){
        return queue.add(callBack);
    }

    public void fireRegistrySucceed(){
        for(CallBack callBack : queue) {
            callBack.run();
        }
    }
}

class OnClick implements CallBack{

    @Override
    public void run() {
        System.out.println("You click me just now");
    }
}

interface CallBack{
    void run();
}