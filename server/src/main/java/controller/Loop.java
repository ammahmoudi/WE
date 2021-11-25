package controller;

//in name of Sadat

public class Loop
{
    private final double fps;
    protected Thread thread;
    private final Runnable updatable;
    private volatile boolean running;

    public Loop(double fps, Runnable updatable)
    {
        this.fps = fps;
        this.updatable = updatable;
        running = false;
        thread = new Thread(this::run);
    }

    public void start()
    {
        running = true;
        thread.start();
    }

    public void restart()
    {
        thread = new Thread(this::run);
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
        if (Thread.currentThread().equals(thread))
        {
            return;
        }
        try
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void run()
    {
        long lastCycleTime = System.nanoTime();
        double nanosecondsPerUpdate = 1000000000 / fps;
        double delta = 0;
        while (running)
        {
            long now = System.nanoTime();
            delta += (now - lastCycleTime) * 1.0 / nanosecondsPerUpdate;
            lastCycleTime = now;
            if (delta < 1)
            {
                sleep((long) (nanosecondsPerUpdate * (1 - delta)));
            }
            while (running && delta >= 1)
            {
                try
                {
                    update();
                }
                catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                }
                delta--;
            }
        }
    }

    public void update()
    {
        if (updatable != null)
        {
            updatable.run();
        }
    }

    public void sleep(long time)
    {
        int milliseconds = (int) (time) / 1000000;
        int nanoseconds = (int) (time) % 1000000;
        try
        {
            Thread.sleep(milliseconds, nanoseconds);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}