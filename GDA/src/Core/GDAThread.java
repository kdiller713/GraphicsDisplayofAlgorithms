package Core;

public class GDAThread implements Runnable {
    private GDAForm form;
    private Algorithm algo;
    private long sleep;

    public GDAThread(Algorithm a, long s) {
        form = new GDAForm(a);
        algo = a;
        sleep = s;
    }

    @Override
    public void run() {
        while (!algo.isDone()) {
            algo.tick();
            form.update();

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
            }
        }
    }
}
