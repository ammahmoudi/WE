package controller;

import event.user.ChangeUserInfoEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.TimerTask;
import java.util.concurrent.*;

import static java.lang.Thread.getAllStackTraces;
import static java.lang.Thread.sleep;

public
class Actions {
    static Logger LOGGER = LogManager.getLogger(Actions.class.getName());
    public static
    LinkedList<TimerTask> timerTasks=new LinkedList<>();
    public static  LinkedList<Timeline> timelines=new LinkedList <>();
    public static LinkedList<ScheduledService> updaters=new LinkedList <>();
    public static LinkedList<Service> services=new LinkedList <>();
    public  static LinkedList<Service> offlineQueueServices=new LinkedList <>();
    public static LinkedList<ChangeUserInfoEvent> changeUserInfoEvents=new LinkedList <>();
    public static
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
    public  static
    ExecutorService executorService=Executors.newSingleThreadExecutor();
    public static

    Timeline actionLopper(int seconds, EventHandler eventHandler) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(seconds),eventHandler));
        timeline.setCycleCount(Timeline.INDEFINITE);

        Task task=new Task() {
            @Override
            protected
            Object call() throws Exception {
                sleep(100);
                timeline.play();
                return null;
            }
        };

        new Thread(task).start();
        timelines.add(timeline);
        return timeline;
    }

    public static
    void nonUIActionLopper(int seconds, TimerTask task) {
        timerTasks.add(task);
        Object string;
        ScheduledFuture <?> future= executor.scheduleAtFixedRate(task, 0, seconds, TimeUnit.SECONDS);


    }
    public static
    void actionLopper2(int seconds, EventHandler eventHandler,Runnable runnable) {
        runnable.run();
        Task task=new Task() {
            @Override
            protected
            Object call() throws Exception {

                sleep(1000*seconds);


                return null;
            }
        };


task.setOnSucceeded(new EventHandler <WorkerStateEvent>() {
    @Override
    public
    void handle(WorkerStateEvent event) {
        eventHandler.handle(event);
        actionLopper2(seconds,eventHandler,runnable);
    }
});
new Thread(task).start();
    }

    public static
    ScheduledService actionLopper3(double period, Runnable backgroundRunnable,Runnable guiRunnable) {
        ScheduledService<Integer> scheduledService=new ScheduledService <Integer>() {
            @Override
            protected
            Task <Integer> createTask() {
                Task task=new Task() {
                    @Override
                    protected
                    Object call() throws Exception {
                        if(isCancelled())return null;
                        backgroundRunnable.run();
                        return 1;
                    }
                };
                if(guiRunnable!=null)
                task.setOnSucceeded(event -> guiRunnable.run());
                task.exceptionProperty().addListener(new ChangeListener() {
                    @Override
                    public
                    void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        LOGGER.warn(getException().getLocalizedMessage());
                        getException().printStackTrace();
                    }
                });
                return task;

            }
        };

        scheduledService.setPeriod(Duration.millis(period));
        scheduledService.setDelay(Duration.ZERO);
        updaters.add(scheduledService);

        return scheduledService;
    }


    public static
    Service executeOnce( Runnable backgroundRunnable, Runnable guiRunnable) {
       Service<Integer> service=new Service() {
           @Override
           protected

               Task <Integer> createTask() {
                   Task task=new Task() {
                       @Override
                       protected
                       Object call() throws Exception {
                           if(isCancelled())return null;
                           backgroundRunnable.run();
                           return 1;
                       }
                   };
                   if(guiRunnable!=null)
                   task.setOnSucceeded(event -> guiRunnable.run());
                   task.exceptionProperty().addListener(new ChangeListener() {
                       @Override
                       public
                       void changed(ObservableValue observable, Object oldValue, Object newValue) {
LOGGER.warn(getException().getLocalizedMessage());
                           getException().printStackTrace();
                       }
                   });
                   return task;
               }
           };
        service.exceptionProperty().addListener(new ChangeListener <Throwable>() {
            @Override
            public
            void changed(ObservableValue <? extends Throwable> observable, Throwable oldValue, Throwable newValue) {
                LOGGER.warn(service.getException().getLocalizedMessage());
                service.getException().printStackTrace();
            }
        });
        services.add(service);
        return service;
    }


    public static void executeTimerTasks(){
        for(TimerTask task:timerTasks){
        executor.scheduleAtFixedRate(task,0,1,TimeUnit.SECONDS);
        }
    }
    public static void pauseTimeLines(){
        for (Timeline timeline:timelines){
            timeline.pause();
        }
    }  public static void playTimeLines(){
        for (Timeline timeline:timelines){
            timeline.play();
        }
    }
    public static void cancelUpdaters(){
        for (ScheduledService scheduledService:updaters){
            scheduledService.cancel();
        }
    }
    public static void restartUpdaters(){
        for (ScheduledService scheduledService:updaters){

            scheduledService.restart();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }
    public static void singleAction(TimerTask task){
        executorService.execute(task);
    }

}
