package utils;

public class Progress {

    private String label;

    public Progress(String label) {
        this.label = label;
    }

    public void setProgress(double currentProgress, double targetProgress) {
        int percent = (int) (currentProgress / targetProgress * 100);
        int progress = (int) (currentProgress / targetProgress * 50);
        StringBuilder bar = new StringBuilder(label + " [");
        for (int i = 0; i < 50; i++) {
            if (i < progress) {
                bar.append("=");
            } else if (i == progress) {
                bar.append(">");
            } else {
                bar.append(" ");
            }
        }
        bar.append("]   ").append(percent).append("%");
        System.out.print("\r" + bar);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void complete(String message) {
        System.out.println("\r" + message);
    }

}
