package Core;

public class GDAThread implements Runnable {
    private GDAForm form;
    private long sleep;

    public GDAThread(GDAForm f, long s) {
        form = f;
        sleep = s;
    }

    @Override
    public void run() {
        while (!form.getAlgorithm().isDone()) {
            form.getAlgorithm().tick();
            form.update();

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
            }
        }
    }
}
