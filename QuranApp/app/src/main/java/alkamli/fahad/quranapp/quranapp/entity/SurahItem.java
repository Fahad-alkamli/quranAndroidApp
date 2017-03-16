package alkamli.fahad.quranapp.quranapp.entity;


public class SurahItem {
    private String title;
    private String order;
    private boolean downloadState=true;

    public SurahItem(String title, String order) {
        this.title = title;
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public boolean isDownloadState() {
        return downloadState;
    }

    public void setDownloadState(boolean downloadState) {
        this.downloadState = downloadState;
    }
}
