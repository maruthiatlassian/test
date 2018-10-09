package hello;

public class TlsCheckResponse {
    private String  url;
    private String  title;
    private Boolean isTlsAvailable;
    private Boolean isTlsAvailableOnly;
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setIsTlsAvailable(Boolean isTlsAvailable) {
        this.isTlsAvailable = isTlsAvailable;
    }
    
    public Boolean getIsTlsAvailable() {
        return this.isTlsAvailable;
    }
    
    public void setIsTlsAvailableOnly(Boolean isTlsAvailableOnly) {
        this.isTlsAvailableOnly = isTlsAvailableOnly;
    }
    
    public Boolean getisTlsAvailableOnly() {
        return this.isTlsAvailableOnly;
    }
}
